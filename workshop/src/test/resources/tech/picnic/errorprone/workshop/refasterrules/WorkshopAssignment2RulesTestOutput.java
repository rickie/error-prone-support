package tech.picnic.errorprone.workshop.refasterrules;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import tech.picnic.errorprone.refaster.test.RefasterRuleCollectionTestCase;

final class WorkshopAssignment2RulesTest implements RefasterRuleCollectionTestCase {
  ImmutableSet<List<Integer>> testImmutableListOfOne() {
    return ImmutableSet.of(ImmutableList.of(1), ImmutableList.of(2));
  }
}
