package finalproject;



import weka.core.Instances;
import weka.core.Instances;
import weka.core.Instance;


public class ModelMakerTestDriver {
	
	public static void main(String[] args){
		String testFile = "dummydata/iris.csv";
		ModelMaker m = new ModelMaker();
		Instances data = m.readData(testFile);
		//ModelMaker.print(data.get(0));
		Instance i = data.instance(0);
		//ModelMaker.print(i.getClass());
		//ModelMaker.print(i);
		
	}

}
