import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.patterns.surface.AnnotatedTextReader;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class KeywordDeriver {
	private Properties props;
	private StanfordCoreNLP pipeline; 
	
	public KeywordDeriver() {
		props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);
	}
	
	public void deriveKeywordFor(Log log) {
		for(Utterance utter: log.getUtterances()) {
			this.deriveKeywordFor(utter);
		}
	}
	
	private void deriveKeywordFor(Utterance utter) {
		String content = utter.getContentString();
		ArrayList<Word> nounLemmaList = new ArrayList<Word>();
		ArrayList<Word> pnList = new ArrayList<Word>();
		this.getNounLemmaList(content, nounLemmaList, pnList);
		utter.setNounLemmaList(nounLemmaList);
		utter.setProperNounList(pnList);
	}
	
	private void getNounLemmaList(String content, ArrayList<Word> lemmaList, ArrayList<Word> pnList) {
		Annotation annot = new Annotation(content);
		pipeline.annotate(annot);
		
		List<CoreMap> sentences = annot.get( SentencesAnnotation.class );
		for(CoreMap sentence: sentences) {
			for(CoreLabel token: sentence.get(TokensAnnotation.class)){
				String pos = token.get(PartOfSpeechAnnotation.class);
				String ner = token.get(NamedEntityTagAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class);
				if( !"O".equals(ner) ) {
					pnList.add( new Word(lemma) );
				}
				else if("NN".equals(pos) || "NNS".equals(pos) ) {
					lemmaList.add( new Word(lemma) );
				}
			}
		}
	}
}
