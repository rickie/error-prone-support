package tech.picnic.errorprone.bugpatterns;

import static com.google.common.base.Predicates.containsPattern;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.CompilationTestHelper;
import org.junit.jupiter.api.Test;

public final class OptionalOrElseTest {
  private final CompilationTestHelper compilationTestHelper =
      CompilationTestHelper.newInstance(OptionalOrElse.class, getClass())
          .expectErrorMessage(
              "X",
              containsPattern(
                  "Prefer `Optional#orElseGet` over `Optional#orElse` if the fallback requires additional computation"));
  private final BugCheckerRefactoringTestHelper refactoringTestHelper =
      BugCheckerRefactoringTestHelper.newInstance(OptionalOrElse.class, getClass());

  @Test
  void identification() {
    compilationTestHelper
        .addSourceLines(
            "A.java",
            "import com.google.errorprone.refaster.Refaster;",
            "import java.util.Optional;",
            "",
            "class A {",
            "  private final Optional<Object> optional = Optional.empty();",
            "  private final String string = optional.toString();",
            "",
            "  void m() {",
            "    Optional.empty().orElse(null);",
            "    optional.orElse(null);",
            "    optional.orElse(\"constant\");",
            "    optional.orElse(\"constant\" + 0);",
            "    optional.orElse(Boolean.TRUE);",
            "    optional.orElse(string);",
            "    optional.orElse(this.string);",
            "    optional.orElse(Refaster.anyOf(\"constant\", \"another\"));",
            "",
            "    // BUG: Diagnostic matches: X",
            "    Optional.empty().orElse(string + \"constant\");",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(string + \"constant\");",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(\"constant\".toString());",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(string.toString());",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(this.string.toString());",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(String.valueOf(42));",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(string.toString().length());",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(\"constant\".equals(string));",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(string.equals(string));",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(this.string.equals(string));",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(foo());",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(this.foo());",
            "    // BUG: Diagnostic matches: X",
            "    optional.orElse(new Object() {});",
            "  }",
            "",
            "  private <T> T foo() {",
            "    return null;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  void replacement() {
    refactoringTestHelper
        .addInputLines(
            "in/A.java",
            "import java.util.Optional;",
            "",
            "class A {",
            "  private final Optional<Object> optional = Optional.empty();",
            "  private final String string = optional.toString();",
            "",
            "  void m() {",
            "    optional.orElse(string + \"constant\");",
            "    optional.orElse(\"constant\".toString());",
            "    optional.orElse(string.toString());",
            "    optional.orElse(this.string.toString());",
            "    optional.orElse(String.valueOf(42));",
            "    optional.orElse(string.toString().length());",
            "    optional.orElse(string.equals(string));",
            "    optional.orElse(foo());",
            "    optional.orElse(this.<Number>foo());",
            "    optional.orElse(this.<String, Integer>bar());",
            "    optional.orElse(new Object() {});",
            "  }",
            "",
            "  private <T> T foo() {",
            "    return null;",
            "  }",
            "",
            "  private <S, T> T bar() {",
            "    return null;",
            "  }",
            "}")
        .addOutputLines(
            "out/A.java",
            "import java.util.Optional;",
            "",
            "class A {",
            "  private final Optional<Object> optional = Optional.empty();",
            "  private final String string = optional.toString();",
            "",
            "  void m() {",
            "    optional.orElseGet(() -> string + \"constant\");",
            "    optional.orElseGet(\"constant\"::toString);",
            "    optional.orElseGet(string::toString);",
            "    optional.orElseGet(this.string::toString);",
            "    optional.orElseGet(() -> String.valueOf(42));",
            "    optional.orElseGet(() -> string.toString().length());",
            "    optional.orElseGet(() -> string.equals(string));",
            "    optional.orElseGet(() -> foo());",
            "    optional.orElseGet(this::<Number>foo);",
            "    optional.orElseGet(this::<String, Integer>bar);",
            "    optional.orElseGet(() -> new Object() {});",
            "  }",
            "",
            "  private <T> T foo() {",
            "    return null;",
            "  }",
            "",
            "  private <S, T> T bar() {",
            "    return null;",
            "  }",
            "}")
        .doTest(BugCheckerRefactoringTestHelper.TestMode.TEXT_MATCH);
  }
}
