package limo.exrel.features.re.structured;

import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

import limo.cluster.WordEmbeddingDictionary;
import limo.core.Mention;
import limo.core.Relation;
import limo.core.Sentence;
import limo.core.trees.AbstractTree;
import limo.core.trees.constituency.ParseTree;

public class VEChm  extends RelationExtractionStructuredFeature {
	
	protected String _extract(AbstractTree parseTree, Mention mention1,
			Mention mention2, Relation relation, Sentence sentence,  ArrayList<Object> resources) throws IOException {
		
		int dimension = 50;
		try {
			ParseTree decorated = (ParseTree)parseTree;
			
			WordEmbeddingDictionary wordEmbeddingDictionary = (WordEmbeddingDictionary) resources.get(4);
			
			if (wordEmbeddingDictionary== null) {
				System.err.println("VEChm: Did you load the Word Embedding file? Check config file.");
				System.exit(-1);
			}
			
			dimension = wordEmbeddingDictionary.getDimension();
			
			String tmpFirstTarget = "ENTITY-INFOT1";
			String tmpSecondTarget = "ENTITY-INFOT2";
			
			int[] tokenIds1 = mention1.getTokenIds();
			int[] tokenIds2 = mention2.getTokenIds();
			
			String decoratedStr = decorated.insertNodes(tokenIds1, tmpFirstTarget, tokenIds2, tmpSecondTarget);
			ParseTree parseTreeDecorated = new ParseTree(decoratedStr);
			
			int spanTokenIdStart = min(tokenIds1, tokenIds2);
			int spanTokenIdEnd = max(tokenIds1, tokenIds2);
		
			String pet = parseTreeDecorated.getPathEnclosedTree(spanTokenIdStart,spanTokenIdEnd);
			
			
			String firstTarget = "E1";
			String secondTarget = "E2";
					
			pet = pet.replaceAll(tmpFirstTarget, firstTarget);
			pet = pet.replaceAll(tmpSecondTarget, secondTarget);
		
			
			if (pet.length()==0)
				System.err.println("no pet found???");
			
			ParseTree tree = new ParseTree(pet);
			
			ArrayList<Double> wed = null;
			ArrayList<String> fet1 = new ArrayList<String>(), fet2 = new ArrayList<String>();
			
			for (Tree terminal : tree.getTerminals()) {
				String word = terminal.label().value();
				Tree pos = terminal.parent(tree.getRootNode());
				Tree posParent = pos.parent(tree.getRootNode());
				wed = wordEmbeddingDictionary.getWordEmbedding(word);
				
				if (posParent.label().value().startsWith("E1") && wed != null) {
					int start = 1;
					if (!fet1.isEmpty()) fet1 = new ArrayList<String>();
					for (Double d : wed) {
						fet1.add(start + ":" + d.toString());
						start++;
					}
				}
				
				if (posParent.label().value().startsWith("E2") && wed != null) {
					int start = wordEmbeddingDictionary.getDimension() + 1;
					if (!fet2.isEmpty()) fet2 = new ArrayList<String>();
					for (Double d : wed) {
						fet2.add(start + ":" + d.toString());
						start++;
					}
				}
				    
			}
			
			String res = "";
			for (String f : fet1)
				res += f + " ";
			for (String f : fet2)
				res += f + " ";
			
			res = res.trim();
			return res.isEmpty() ? zeroFeature(dimension) : res;	
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Problem with: "+sentence.toString());
			System.out.println(parseTree.toString());
			System.out.println("Mention1: "+mention1.toString());
			System.out.println("Mention2: "+mention2.toString());
			return zeroFeature(dimension);
		}
		
	}
	
	private String zeroFeature(int dimension) {
		String res = "";
		for (int i = 1; i <= 2*dimension; i++) {
			res += i + ":0.0" + " ";
		}
		return res.trim();
	}

}
