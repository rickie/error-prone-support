package tech.picnic.errorprone.workshop.refasterrules;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.errorprone.refaster.ImportPolicy.STATIC_IMPORT_ALWAYS;

import com.google.common.base.Preconditions;
import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import com.google.errorprone.refaster.annotation.UseImportPolicy;

/** Refaster rules for the third assignment of the workshop. */
@SuppressWarnings("UnusedTypeParameter" /* Ignore this for demo purposes. */)
final class WorkshopAssignment3Rules {
  private WorkshopAssignment3Rules() {}

  /** Prefer {@link Preconditions#checkArgument(boolean)} over more verbose alternatives. */
  static final class CheckArgumentWithoutMessage<T> {
    @BeforeTemplate
    void before(boolean condition) {
      if (condition) {
        throw new IllegalArgumentException();
      }
    }

    @AfterTemplate
    @UseImportPolicy(STATIC_IMPORT_ALWAYS)
    void after(boolean condition) {
      checkArgument(condition);
    }
  }

  /** Prefer {@link Preconditions#checkArgument(boolean, Object)} over more verbose alternatives. */
  static final class CheckArgumentWithMessage<T> {
    @BeforeTemplate
    void before(boolean condition, String message) {
      if (condition) {
        throw new IllegalArgumentException(message);
      }
    }

    @AfterTemplate
    @UseImportPolicy(STATIC_IMPORT_ALWAYS)
    void after(boolean condition, String message) {
      checkArgument(condition, message);
    }
  }
}
