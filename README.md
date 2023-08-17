# cellDistanceClassification
This groovy script helps to quantify min distances among cells to classify them into single, nearby attached and umbrella cells. Moreover, two additional groovy scripts (``doublePositiveCellQuantification.groovy``, ``singlePositiveCellQuantification.groovy``) are provided in order to quantify single, and double positive- negative cells.

## Download cellDistanceClassification code
1. Go to the ``GitHub`` repository
2. Click on ``<Code>``>``Download ZIP``
3. The repo will be found at ``Downloads`` directory.

## Please note that you must have installed the IJPB-Plugins library in ImageJ UpdateSite:
1. Click on ``Help``>``Update``
<p align="center">
    <img width="600" height="350" src="https://github.com/cnio-cmu-BioimageAnalysis/fluorescenceQuantification_code/assets/83207172/81cbd730-b481-4f9c-bfa8-5c4a7792c3f4">
    </p>

## Running cellDistanceClassification in headless mode through ImageJ/Windows Windows Terminal (ALL parameters)

``ImageJ-win64.exe --ij2 --headless --run "/absolute_path/to/groovyscript/cellDistanceClassification.groovy" "headless=true, inputFilesDir='/absolute_path/to/inputFiles/images',outputDir='/absolute_path/to/outputDirectory/results',gfpModel='/absolute_path/to/gfpModel',greenChannel=1"``
### Parameters Explanation:
- ``headless`` : true. 
- ``inputFilesDir`` : Directory in which the images (tiff, jpeg... files) to be analyzed are located. ``'/home/anaacayuela/Ana_pruebas_imageJ/margarita/images'``.
- ``outputDir`` : Directory in which the outputs are saved. ``'/home/anaacayuela/Ana_pruebas_imageJ/margarita/results'``
- ``gfpModel`` : Absolute path to file containing GFP model for segmentation. ``/home/anaacayuela/Ana_pruebas_imageJ/models/model_gfp``
- ``greenChannel`` : Channel in which GFP marker is located ``1``
## Running through ImageJ/Fiji 
1. Navigate to reach Script Editor tool:
   - By writing ``true`` on the search tool or by ``File``>``New``>``Script...``
     <p align="center">
    <img width="650" height="350" src="https://github.com/cnio-cmu-BioimageAnalysis/cellQuantification_code/assets/83207172/0ad85b7b-d214-41a1-83a3-ac4c9395231b">
    </p>

2. Browse to find the directory in which the corresponding the groovy script is stored: ``cellDistanceClassification.groovy``
    <p align="center">
    <img width="500" height="350" src="https://github.com/cnio-cmu-BioimageAnalysis/cellQuantification_code/assets/83207172/5b34dde0-2f35-4908-85f2-ffc4f89341d5">
    </p>
 
3. Press ``Run`` button to compile the script.
    <p align="center">
    <img width="500" height="350" src="https://github.com/cnio-cmu-BioimageAnalysis/cellQuantification_code/assets/83207172/1886af45-c01a-44d3-804b-30e289a2aa38">
    </p>

4. Then a dialog will be displayed in order to set both the input directory path in which the images (not ready to deal with ``LIF`` files) to be analyzed are stored and the output directory path to save the outputs.
   <p align="center">
    <img width="400" height="150" src="https://github.com/cnio-cmu-BioimageAnalysis/fluorescenceQuantification_code/assets/83207172/3c50a3ed-3918-40e3-b49b-71f060230901">
    </p>


5. A log window will appear to update about the processing status.
  <p align="center">
    <img width="350" height="150" src="https://github.com/cnio-cmu-BioimageAnalysis/cellQuantification_code/assets/83207172/ae08ebc2-a720-451c-8a50-542a708972fa">
    </p>
 
6. Finally, you will be enabled to check the outputs (one``CSV table`` corresponding to each image located in the output directory previously selected.
  <p align="center">
    <img width="600" height="40" src="https://github.com/cnio-cmu-BioimageAnalysis/fluorescenceQuantification_code/assets/83207172/647a9e29-4b7c-4957-a1dc-a7ea40d2cc16">
    </p>



