package mlfs.chineseSeg.main;


import java.io.IOException;
import java.util.List;

import mlfs.chineseSeg.corpus.CorpusProcessing;
import mlfs.crf.CRFLBFGSTrainer;
import mlfs.crf.Features;
import mlfs.crf.TemplateHandler;
import mlfs.crf.corpus.CorpusReader;
import mlfs.crf.model.CRFEvent;
import mlfs.crf.model.CRFModel;

public class Train {

	private static void usage()
	{
		System.out.println("java -cp <classpath> mlfs.chineseSeg.main.Train templatefile trainfile model");
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length != 3)
		{
			usage();
			System.exit(-1);
		}
		String templateFile = args[0];
		String trainFile = args[1];
		String modelFle = args[2];
//		//从train语料中统计特征，这里注释掉是因为已经统计过了，并生成CHINESE_SEGMENT_CRF.train，没必要反复统计
//		CorpusProcessing processing = new CorpusProcessing("corpus/chineseSegment/pku_training.utf8");
//		processing.buildTrainFile();
//		processing = null;
		
		TemplateHandler template = new TemplateHandler(templateFile);
		
		CorpusReader corpus = new CorpusReader(trainFile);
		List<CRFEvent> events = corpus.getAllTrainEvents();
		
		Features featuresHandle = new Features(template, corpus.getTagMap(), events, modelFle);
		
		CRFLBFGSTrainer trainer = new CRFLBFGSTrainer(events, featuresHandle) ;
		
		CRFModel model = trainer.train(1000);
		model.save(modelFle);
	
	}
}
