package mlfs.votedPerceptron.main;

import java.io.IOException;
import java.util.List;

import mlfs.votedPerceptron.VPTrainer;
import mlfs.votedPerceptron.corpus.CorpusReader;
import mlfs.votedPerceptron.model.VPEvent;
import mlfs.votedPerceptron.model.VotedPerceptronModel;

public class Train {

	private static final String DEFAULT_TRAIN_FILE = "corpus/votedperceptron/a1a.txt";
	private static final String DEFAULT_MODEL_FILE = "votedperceptron.model";

	private static void usage()
	{
		System.out.println("java -cp <classpath> mlfs.votedPerceptron.main.Train [trainFile modelFile numIter]");
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 0 && args.length != 3)
		{
			usage();
			System.exit(-1);
		}

		String trainFile = args.length == 3 ? args[0] : DEFAULT_TRAIN_FILE;
		String modelFile = args.length == 3 ? args[1] : DEFAULT_MODEL_FILE;
		int numIter = args.length == 3 ? Integer.parseInt(args[2]) : 1;

		CorpusReader reader = new CorpusReader(trainFile);
		List<VPEvent> events = reader.getAllEvent();
		
		VPTrainer trainer = new VPTrainer(events);
		VotedPerceptronModel model = trainer.train(numIter);
		
		System.out.println(model.getNumPerceptrons());
		model.save(modelFile);
	}
}
