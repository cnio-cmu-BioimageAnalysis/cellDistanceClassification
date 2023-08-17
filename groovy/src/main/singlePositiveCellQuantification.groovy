import ij.IJ
import ij.ImagePlus
import ij.WindowManager
import ij.gui.Roi
import ij.gui.ShapeRoi
import ij.measure.ResultsTable
import ij.plugin.ChannelSplitter
import ij.plugin.ZProjector
import ij.plugin.frame.RoiManager
import inra.ijpb.binary.BinaryImages
import inra.ijpb.color.CommonColors
import inra.ijpb.data.image.ColorImages
import inra.ijpb.morphology.Strel
import loci.plugins.BF
import loci.plugins.in.ImporterOptions
import net.imglib2.converter.ChannelARGBConverter
import org.apache.commons.compress.utils.FileNameUtils

import java.io.File;

// INPUT UI
//
#@File(label = "Input File Directory", style = "directory") inputFilesDir
#@File(label = "Output directory", style = "directory") outputDir
#@Integer(label = "Dapi Channel", value = 0) dapiChannel
#@Integer(label = "GFP Channel", value = 1) greenChannel
//#@Integer(label = "Tomato Channel", value = 3) redChannel
//#@Integer(label = "MDK Channel", value = 4) magentaChannel
//#@Boolean(label = "Apply DAPI?") applyDAPI


// IDE
//
//
//def headless = true;
//new ImageJ().setVisible(true);

IJ.log("-Parameters selected: ")
IJ.log("    -inputFileDir: " + inputFilesDir)
IJ.log("    -outputDir: " + outputDir)
IJ.log("                                                           ");
/** Get files (images) from input directory */
def listOfFiles = inputFilesDir.listFiles();

def tableConditions = new ResultsTable();

for (def i = 0; i < listOfFiles.length; i++) {

    if (!listOfFiles[i].getName().contains("DS")) {


        def imp = new ImagePlus(inputFilesDir.getAbsolutePath() + File.separator + listOfFiles[i].getName())
        /** Split Channels */
        def channels = ChannelSplitter.split(imp)
        /** Get each individual channel */
        def dapi = channels[dapiChannel.intValue()].duplicate()
        def green = channels[greenChannel.intValue()].duplicate()


        /** Process dapi channel */
        dapi.show()
        /** Segment dapi channel with Cellpose */
        IJ.run(dapi, "Cellpose Advanced (custom model)", String.format("diameter=17 cellproba_threshold=0.0 flow_threshold=0.4 anisotropy=1.0 diam_threshold=12.0 model_path=cyto2 model=cyto2 nuclei_channel=0 cyto_channel=0 dimensionmode=2D stitch_threshold=-1.0 omni=false cluster=false additional_flags="));
        dapi.hide()
        def dapiSeg = WindowManager.getImage(dapi.getShortTitle() + "-cellpose")
        /** Cellpose labels to rois */
        IJ.run(dapiSeg, "Label image to ROIs", "");
        def rm = RoiManager.getInstance();
        def roisDapi = rm.getRoisAsArray();
        rm.reset()
        /** Convert Cellpose label image to binary image */
        def color = CommonColors.fromLabel("Red").getColor();
        // Call binary overlay conversion
        def binaryDapi = ColorImages.binaryOverlay(dapiSeg, dapiSeg, color);
        dapiSeg.hide()
        IJ.run(binaryDapi, "8-bit", "");
        IJ.run(binaryDapi, "Auto Threshold", "method=Otsu ignore_black white");
        IJ.run(binaryDapi, "Create Selection", "");
        /**  Get dapi roi from binary image */
        def roiDapi = binaryDapi.getRoi()


        /** Process green channel- Single positive green */
        green.setRoi(roiDapi)
        def greenMean = green.getStatistics().mean
        def greenStd = green.getStatistics().stdDev
        def positiveGreenRois = new ArrayList<Roi>();
        for (def j = 0.intValue(); j < roisDapi.length; j++) {
            green.setRoi(roisDapi[j])
            if (green.getStatistics().mean > (greenMean + greenStd))
                positiveGreenRois.add(roisDapi[j])
        }



        tableConditions.incrementCounter();
        tableConditions.setValue("Image Title", i, listOfFiles[i].getName())
        tableConditions.setValue("N of Dapi Cells", i, roisDapi.length)
        tableConditions.setValue(" N of Single Positive Green Cells", i, positiveGreenRois.size())
        tableConditions.setValue(" N of Single Negative Green Cells", i, (roisDapi.length - positiveGreenRois.size()))

    }
}
tableConditions.saveAs(outputDir.getAbsolutePath() + File.separator + listOfFiles[0].getParentFile().getName() + "_singlePositiveCells" + ".csv")

IJ.log("Done!!!")




