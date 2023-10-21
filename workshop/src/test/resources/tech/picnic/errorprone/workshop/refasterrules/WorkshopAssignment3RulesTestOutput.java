package tech.picnic.errorprone.workshop.refasterrules;

import static com.google.common.base.Preconditions.checkArgument;

import tech.picnic.errorprone.refaster.test.RefasterRuleCollectionTestCase;

final class WorkshopAssignment3RulesTest implements RefasterRuleCollectionTestCase {
  void testCheckArgumentWithoutAMessage() {
    checkArgument("foo".isEmpty());
  }

  void testCheckArgumentWithAMessage() {
    checkArgument("foo".isEmpty(), "The string is empty");
  }
}
