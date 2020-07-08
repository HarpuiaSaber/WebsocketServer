package algorithm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import utils.FileStorage;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

public class DecisionTree {
	private static final String TRAINING_DATA = "water_data.csv";

    public static Instances data;
    public static Classifier tree;

    //This method is to load the data set.
    public static void loadSetting() {
        String ext = FilenameUtils.getExtension(TRAINING_DATA);
        AbstractFileLoader loader = null;
        switch (ext) {
            case "csv":
                loader = new CSVLoader();
                break;
            case "arff":
                loader = new ArffLoader();
                break;
        }
        try {
            loader.setSource(new File(FileStorage.trainDir + TRAINING_DATA));
            data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            tree = new J48();
            tree.buildClassifier(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
