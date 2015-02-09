package limo.exrel.features.re.linear;

import limo.core.Sentence;
import limo.core.Mention;
import limo.core.Relation;
import limo.core.trees.constituency.ParseTree;


public class EntityType1conf extends RelationExtractionLinearFeature {

	@Override
	protected String _extract(ParseTree parseTree, Mention mention1, Mention mention2,
			Relation relation, Sentence sentence, String groupId) {
			return mention1.getEntityReference().getType() + RelationExtractionLinearFeature.CONFIDENCEseparator + mention1.getEntityReference().getConfidence();
	}

}
