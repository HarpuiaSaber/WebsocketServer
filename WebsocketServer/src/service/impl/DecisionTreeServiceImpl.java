package service.impl;

import algorithm.DecisionTree;
import service.DecisionTreeService;
import weka.core.DenseInstance;
import weka.core.Instance;

public class DecisionTreeServiceImpl implements DecisionTreeService {

	@Override
	public int makeDecision(double temp, double humidity) {
		Instance instance = new DenseInstance(5);
		instance.setDataset(DecisionTree.data);
		double label = 0;

		try {
			instance.setValue(DecisionTree.data.attribute("temp"), temp);
			instance.setValue(DecisionTree.data.attribute("humidity"), humidity);
			instance.setClassMissing();

			label = DecisionTree.tree.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		instance.setClassValue(label);
		return instance.stringValue(4).equals("Y") ? 1 : 0;
	}
}
