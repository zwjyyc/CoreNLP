package edu.stanford.nlp.ling.tokensregex;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.util.CoreMap;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests triggering of sequence patterns
 *
 * @author Angel Chang
 */
public class SequencePatternTriggerTest extends TestCase {

  public void testSimpleTrigger() throws Exception {
    List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
    patterns.add(TokenSequencePattern.compile("which word should be matched"));

    MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
        new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
          new CoreMapNodePatternTrigger(patterns));

    Collection<SequencePattern<CoreMap>> triggered = trigger.apply(Sentence.toCoreLabelList("one", "two", "three"));
    assertEquals(0, triggered.size());

    triggered = trigger.apply(Sentence.toCoreLabelList("which"));
    assertEquals(0, triggered.size());

    triggered = trigger.apply(Sentence.toCoreLabelList("which", "word", "should", "be", "matched"));
    assertEquals(1, triggered.size());
  }

  public void testOptionalTrigger() throws Exception {
    List<TokenSequencePattern> patterns = new ArrayList<TokenSequencePattern>();
    patterns.add(TokenSequencePattern.compile("which word should? be matched?"));

    MultiPatternMatcher.SequencePatternTrigger<CoreMap> trigger =
        new MultiPatternMatcher.BasicSequencePatternTrigger<CoreMap>(
            new CoreMapNodePatternTrigger(patterns));

    Collection<SequencePattern<CoreMap>> triggered = trigger.apply(Sentence.toCoreLabelList("one", "two", "three"));
    assertEquals(0, triggered.size());

    triggered = trigger.apply(Sentence.toCoreLabelList("which"));
    assertEquals(1, triggered.size());

    triggered = trigger.apply(Sentence.toCoreLabelList("which", "word", "be"));
    assertEquals(1, triggered.size());

    triggered = trigger.apply(Sentence.toCoreLabelList("which", "word", "should", "be", "matched"));
    assertEquals(1, triggered.size());
  }
}