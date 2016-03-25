package edu.cmu.cs.lti.learning.feature.mention_pair.functions;

import edu.cmu.cs.lti.learning.model.MentionCandidate;
import edu.cmu.cs.lti.utils.Configuration;
import gnu.trove.map.TObjectDoubleMap;
import org.apache.uima.jcas.JCas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 9/27/15
 * Time: 5:18 PM
 *
 * @author Zhengzhong Liu
 */
public abstract class AbstractMentionPairFeatures {
    protected final Logger logger;

    protected Configuration featureConfig;
    protected Configuration generalConfig;

    public AbstractMentionPairFeatures(Configuration generalConfig, Configuration featureConfig) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Register feature extractor : " + featureName());
        this.featureConfig = featureConfig;
        this.generalConfig = generalConfig;
    }

    public abstract void initDocumentWorkspace(JCas context);

    /**
     * Extract features from the annotation pair, without label specific features.
     *
     * @param documentContext The UIMA context.
     * @param featuresNoLabel Features don't need labels will be added to this raw feature map.
     * @param candidates
     * @param firstIndex
     * @param secondIndex
     */
    public abstract void extract(JCas documentContext, TObjectDoubleMap<String> featuresNoLabel,
                                 List<MentionCandidate> candidates, int firstIndex, int secondIndex);

    /**
     * Extract features from the annotation pair, with candidate specific features.
     * @param documentContext   The UIMA context.
     * @param featuresNeedLabel Features don't need labels will be added to this raw feature map.
     * @param candidates
     * @param firstIndex
     * @param secondIndex
     */
    public abstract void extractCandidateRelated(JCas documentContext, TObjectDoubleMap<String> featuresNeedLabel,
                                                 List<MentionCandidate> candidates, int firstIndex, int secondIndex);


    /**
     * Extract features from the annotation pair, with label specific features.
     *
     * @param documentContext The UIMA context
     * @param featuresNoLabel Features need labels will be added to this raw feature map.
     * @param secondCandidate Second mention to extract from.
     */
    public abstract void extract(JCas documentContext, TObjectDoubleMap<String> featuresNoLabel,
                                 MentionCandidate secondCandidate);

    /**
     * Extract features from the annotation pair, with label specific features.
     *
     * @param documentContext   The UIMA context
     * @param featuresNeedLabel Features need labels will be added to this raw feature map.
     * @param secondCandidate   Second mention to extract from.
     */
    public abstract void extractCandidateRelated(JCas documentContext, TObjectDoubleMap<String> featuresNeedLabel,
                                                 MentionCandidate secondCandidate);


    /**
     * A name to describe this feature, it use the full class name by default
     *
     * @return Name of a feature function.
     */
    public String featureName() {
        return this.getClass().getName();
    }

    protected void addBoolean(TObjectDoubleMap<String> rawFeatures, String featureName) {
        rawFeatures.put(featureName, 1);
    }

}
