package tech.picnic.errorprone.workshop.refasterrules;

import tech.picnic.errorprone.refaster.test.RefasterRuleCollectionTestCase;

final class WorkshopAssignment3RulesTest implements RefasterRuleCollectionTestCase {
  void testCheckArgumentWithoutAMessage() {
    if ("foo".isEmpty()) {
      throw new IllegalArgumentException();
    }
  }

  void testCheckArgumentWithAMessage() {
    if ("foo".isEmpty()) {
      throw new IllegalArgumentException("The string is empty");
    }
  }
}
