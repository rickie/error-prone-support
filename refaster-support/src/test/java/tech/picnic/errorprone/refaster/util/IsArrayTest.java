package tech.picnic.errorprone.refaster.util;

import static com.google.errorprone.BugPattern.SeverityLevel.ERROR;

import com.google.errorprone.BugPattern;
import com.google.errorprone.CompilationTestHelper;
import com.google.errorprone.bugpatterns.BugChecker;
import org.junit.jupiter.api.Test;

final class IsArrayTest {
  @Test
  void matches() {
    CompilationTestHelper.newInstance(TestChecker.class, getClass())
        .addSourceLines(
            "A.java",
            "class A {",
            "  Object negative1() {",
            "    return alwaysNull();",
            "  }",
            "",
            "  String negative2() {",
            "    return alwaysNull();",
            "  }",
            "",
            "  int negative3() {",
            "    return alwaysNull();",
            "  }",
            "",
            "  Object[] positive1() {",
            "    // BUG: Diagnostic contains:",
            "    return alwaysNull();",
            "  }",
            "",
            "  String[] positive2() {",
            "    // BUG: Diagnostic contains:",
            "    return alwaysNull();",
            "  }",
            "",
            "  int[] positive3() {",
            "    // BUG: Diagnostic contains:",
            "    return alwaysNull();",
            "  }",
            "",
            "  private static <T> T alwaysNull() {",
            "    return null;",
            "  }",
            "}")
        .doTest();
  }

  /** A {@link BugChecker} which simply delegates to {@link IsArray}. */
  @BugPattern(summary = "Flags array-typed expressions", severity = ERROR)
  @SuppressWarnings({"RedundantModifier", "serial"})
  public static final class TestChecker extends AbstractMatcherChecker {
    public TestChecker() {
      super(new IsArray());
    }
  }
}
