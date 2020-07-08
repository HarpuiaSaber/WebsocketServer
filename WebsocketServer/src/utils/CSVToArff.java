package utils;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

public class CSVToArff {

    public static void main(String[] args) {
        cvsToArff("/home/harpuiasaber/autowater/training_data/water_data.csv", "/home/harpuiasaber/autowater/training_data/water_data.arff");
    }

    public static void cvsToArff(String cvsDir, String arffDir) {
        try {
            // load CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(cvsDir));
            Instances data = loader.getDataSet();

            // save ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(arffDir));
            //saver.setDestination(new File(arffDir));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
