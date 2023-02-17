package tech.picnic.errorprone.testngjunit;

import static com.google.errorprone.matchers.Matchers.hasAnnotation;
import static com.google.errorprone.matchers.Matchers.isType;

import com.google.errorprone.matchers.Matcher;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;

// XXX: It would be awesome to have a test for this class as well. It should be very similar to
// `MoreJUnitMatchersTest`.
/**
 * A collection of TestNG-specific helper methods and {@link Matcher}s.
 *
 * <p>These constants and methods are additions to the ones found in {@link
 * com.google.errorprone.matchers.TestNgMatchers}.
 */
public final class TestNGMatchers {
  /**
   * Matches the {@link org.testng.annotations.Test} annotation specifically. As {@link
   * com.google.errorprone.matchers.TestNgMatchers#hasTestNgAnnotation(ClassTree)} also other TestNG
   * annotations.
   */
  public static final Matcher<AnnotationTree> TESTNG_TEST_ANNOTATION =
      isType("org.testng.annotations.Test");

  /** Matches the {@link org.testng.annotations.DataProvider} annotation specifically. */
  public static final Matcher<MethodTree> TESTNG_VALUE_FACTORY_METHOD =
      hasAnnotation("org.testng.annotations.DataProvider");

  private TestNGMatchers() {}
}