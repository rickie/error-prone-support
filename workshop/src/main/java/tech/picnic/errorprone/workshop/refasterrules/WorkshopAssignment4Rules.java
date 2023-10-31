package tech.picnic.errorprone.workshop.refasterrules;

import com.google.errorprone.refaster.Refaster;
import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.AlsoNegation;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import java.util.Objects;

/** Refaster rules for the fourth assignment of the workshop. */
@SuppressWarnings("java:S1698" /* Reference comparison is valid for enums. */)
final class WorkshopAssignment4Rules {
  private WorkshopAssignment4Rules() {}

  // XXX: Get the test to pass by improving the Refaster rule.

  /** Prefer reference-based quality for enums. */
  static final class PrimitiveOrReferenceEqualityEnum<T> {
    @BeforeTemplate
    boolean before(T a, T b) {
      return Refaster.anyOf(a.equals(b), Objects.equals(a, b));
    }

    @AfterTemplate
    @AlsoNegation
    boolean after(T a, T b) {
      return a == b;
    }
  }
}
