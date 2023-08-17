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
import inra.ijpb.morphology.Strel
import loci.plugins.BF
import loci.plugins.in.ImporterOptions
import net.imglib2.converter.ChannelARGBConverter
import org.apache.commons.compress.utils.FileNameUtils

import java.awt.geom.Point2D
import java.io.File;

// INPUT UI
//
#@File(label = "Input File Directory", style = "directory") inputFilesDir
#@File(label = "Output directory", style = "directory") outputDir
//#@File(label = "GFP model", style = "file") gfpModel
//@Integer(label = "GFP Channel", value = 1) greenChannel


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

for (def i = 0; i < listOfFiles.length; i++) {
    def tableDistances = new ResultsTable()
    if (!listOfFiles[i].getName().contains("DS")) {
            IJ.log(inputFilesDir.getAbsolutePath() + File.separator + listOfFiles[i].getName())
            def imp = IJ.openImage(inputFilesDir.getAbsolutePath() + File.separator + listOfFiles[i].getName())
             imp.show()
      /*      def channels = ChannelSplitter.split(imp)
            def greenCh = channels[greenChannel.intValue()]
            greenCh.show()
            IJ.run(greenCh, "Cellpose Advanced (custom model)", String.format("diameter=0 cellproba_threshold=0.0 flow_threshold=0.4 anisotropy=1.0 diam_threshold=12.0 model_path=%s model=%s nuclei_channel=0 cyto_channel=0 dimensionmode=2D stitch_threshold=-1.0 omni=false cluster=false additional_flags=", gfpModel.getAbsolutePath(), gfpModel.getAbsolutePath()));
            greenCh.hide()
            def greenChSeg = WindowManager.getImage(greenCh.getShortTitle() + "-cellpose")*/
            IJ.run(imp, "Label image to ROIs", "");
            //greenChSeg.hide()
            def rm = RoiManager.getInstance();
            def roisGfp = rm.getRoisAsArray();
            rm.reset()
            imp.hide()
            def xCoordinate = new ArrayList<Double>();
            def yCoordinate = new ArrayList<Double>();
            def minDistance = new ArrayList<Double>();
            for (def j = 0.intValue(); j < roisGfp.length; j++) {
                xCoordinate.add(roisGfp[j].getContourCentroid()[0])
                yCoordinate.add(roisGfp[j].getContourCentroid()[1])
                def perCoordinate = new ArrayList<Double>();
                for (def k = 0.intValue(); k < roisGfp.length; k++) {
                    if(roisGfp[j].getContourCentroid()[0]!= roisGfp[k].getContourCentroid()[0] ){
                    perCoordinate.add(distance(roisGfp[j].getContourCentroid()[0], roisGfp[j].getContourCentroid()[1], roisGfp[k].getContourCentroid()[0], roisGfp[k].getContourCentroid()[1]));

                    }
                }
                minDistance.add(Collections.min(perCoordinate))
                tableDistances.incrementCounter()
                tableDistances.setValue("Image Title", j, listOfFiles[i].getName())
                tableDistances.setValue("Coordinate X", j, roisGfp[j].getContourCentroid()[0])
                tableDistances.setValue("Coordinate Y", j, roisGfp[j].getContourCentroid()[1])
                tableDistances.setValue("Min Distance", j, Collections.min(perCoordinate))
            }

            tableDistances.saveAs(outputDir.getAbsolutePath()+File.separator+listOfFiles[i].getName()+"_minDistances"+".csv")

    }


}

double distance(
        double x1,
        double y1,
        double x2,
        double y2) {

    return Point2D.distance(x1, y1, x2, y2);
}

IJ.log("Done!!!")


