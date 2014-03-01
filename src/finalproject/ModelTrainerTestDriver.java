package finalproject;



import weka.core.Instances;
import weka.core.Instances;
import weka.core.Instance;


public class ModelTrainerTestDriver {
	
	public static void main(String[] args){
		String testFile = "dummydata/iris.csv";
		ModelTrainer m = new ModelTrainer();
		Instances data = m.readData(testFile);
		//ModelMaker.print(data.get(0));
		Instance i = data.instance(0);
		//ModelMaker.print(i.getClass());
		//ModelMaker.print(i);
		
	}

}
