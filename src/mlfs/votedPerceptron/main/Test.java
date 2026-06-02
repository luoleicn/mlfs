package mlfs.votedPerceptron.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mlfs.votedPerceptron.corpus.CorpusReader;
import mlfs.votedPerceptron.model.VPEvent;
import mlfs.votedPerceptron.model.VotedPerceptronModel;

public class Test {

	private static final String DEFAULT_TEST_FILE = "corpus/votedperceptron/a1a.t";
	private static final String DEFAULT_MODEL_FILE = "votedperceptron.model";
	private static final String LEGACY_MODEL_FILE = "votedpercetpron.model";

	private static void usage()
	{
		System.out.println("java -cp <classpath> mlfs.votedPerceptron.main.Test [modelFile testFile]");
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 0 && args.length != 2)
		{
			usage();
			System.exit(-1);
		}

		String modelFile = args.length == 2 ? args[0] : getDefaultModelFile();
		String testFile = args.length == 2 ? args[1] : DEFAULT_TEST_FILE;
		
		System.out.println("Loading model...");
		VotedPerceptronModel model = VotedPerceptronModel.load(modelFile);
		
		int t = 0;
		int f = 0;
		System.out.println("loading all test events");
		CorpusReader reader = new CorpusReader(testFile);
		System.out.println("Predict...");
		List<VPEvent> events = reader.getAllEvent();
		for (VPEvent e : events)
		{
			int label = model.predict(e);
			if (label == e.label)
				t++;
			else
				f++;
		}
		
		System.out.println("" + 1.0*t/(t+f));	
	}

	private static String getDefaultModelFile()
	{
		if (new File(DEFAULT_MODEL_FILE).exists())
			return DEFAULT_MODEL_FILE;
		return LEGACY_MODEL_FILE;
	}

}
