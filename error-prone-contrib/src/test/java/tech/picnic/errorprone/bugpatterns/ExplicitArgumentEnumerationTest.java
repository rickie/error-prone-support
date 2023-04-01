package tech.picnic.errorprone.bugpatterns;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.BugCheckerRefactoringTestHelper.TestMode;
import com.google.errorprone.CompilationTestHelper;
import org.junit.jupiter.api.Test;

final class ExplicitArgumentEnumerationTest {
  @Test
  void identification() {
    CompilationTestHelper.newInstance(ExplicitArgumentEnumeration.class, getClass())
        .addSourceLines(
            "A.java",
            "import static org.assertj.core.api.Assertions.assertThat;",
            "",
            "import com.google.common.collect.ImmutableList;",
            "import com.google.errorprone.CompilationTestHelper;",
            "import com.google.errorprone.bugpatterns.BugChecker;",
            "",
            "class A {",
            "  void m() {",
            "    ImmutableList<String> list = ImmutableList.of();",
            "    assertThat(ImmutableList.of()).containsAnyElementsOf(list);",
            "",
            "    ImmutableList.<ImmutableList<String>>builder().add(ImmutableList.of());",
            "",
            "    // BUG: Diagnostic contains:",
            "    assertThat(ImmutableList.of()).containsAnyElementsOf(ImmutableList.of());",
            "",
            "    CompilationTestHelper.newInstance(BugChecker.class, getClass())",
            "        // BUG: Diagnostic contains:",
            "        .setArgs(ImmutableList.of(\"foo\"))",
            "        .withClasspath();",
            "  }",
            "}")
        .doTest();
  }

  @Test
  void replacement() {
    BugCheckerRefactoringTestHelper.newInstance(ExplicitArgumentEnumeration.class, getClass())
        .addInputLines(
            "A.java",
            "import static org.assertj.core.api.Assertions.assertThat;",
            "",
            "import com.google.common.collect.ImmutableList;",
            "import com.google.common.collect.ImmutableMultiset;",
            "import com.google.common.collect.ImmutableSet;",
            "import com.google.errorprone.BugCheckerRefactoringTestHelper;",
            "import com.google.errorprone.CompilationTestHelper;",
            "import com.google.errorprone.bugpatterns.BugChecker;",
            "import java.util.Arrays;",
            "import java.util.List;",
            "import java.util.Set;",
            "",
            "class A {",
            "  void m() {",
            "    assertThat(ImmutableList.of()).containsAnyElementsOf(ImmutableList.of());",
            "    assertThat(ImmutableList.of()).containsAll(ImmutableMultiset.of());",
            "    assertThat(ImmutableList.of()).containsExactlyElementsOf(ImmutableSet.of());",
            "    assertThat(ImmutableList.of()).containsExactlyInAnyOrderElementsOf(Arrays.asList());",
            "    assertThat(ImmutableList.of()).containsSequence(List.of());",
            "    assertThat(ImmutableList.of()).containsSubsequence(Set.of());",
            "    assertThat(ImmutableList.of()).doesNotContainAnyElementsOf(ImmutableList.of(1));",
            "    assertThat(ImmutableList.of()).doesNotContainSequence(ImmutableMultiset.of(1));",
            "    assertThat(ImmutableList.of()).doesNotContainSubsequence(ImmutableSet.of(1));",
            "    assertThat(ImmutableList.of()).hasSameElementsAs(Arrays.asList(1));",
            "    assertThat(ImmutableList.of()).isSubsetOf(List.of(1));",
            "",
            "    BugCheckerRefactoringTestHelper.newInstance(BugChecker.class, getClass())",
            "        .setArgs(ImmutableList.of(\"foo\", \"bar\"));",
            "    CompilationTestHelper.newInstance(BugChecker.class, getClass())",
            "        .setArgs(ImmutableList.of(\"foo\", \"bar\"));",
            "  }",
            "}")
        .addOutputLines(
            "A.java",
            "import static org.assertj.core.api.Assertions.assertThat;",
            "",
            "import com.google.common.collect.ImmutableList;",
            "import com.google.common.collect.ImmutableMultiset;",
            "import com.google.common.collect.ImmutableSet;",
            "import com.google.errorprone.BugCheckerRefactoringTestHelper;",
            "import com.google.errorprone.CompilationTestHelper;",
            "import com.google.errorprone.bugpatterns.BugChecker;",
            "import java.util.Arrays;",
            "import java.util.List;",
            "import java.util.Set;",
            "",
            "class A {",
            "  void m() {",
            "    assertThat(ImmutableList.of()).containsAnyOf();",
            "    assertThat(ImmutableList.of()).contains();",
            "    assertThat(ImmutableList.of()).containsExactly();",
            "    assertThat(ImmutableList.of()).containsExactlyInAnyOrder();",
            "    assertThat(ImmutableList.of()).containsSequence();",
            "    assertThat(ImmutableList.of()).containsSubsequence();",
            "    assertThat(ImmutableList.of()).doesNotContain(1);",
            "    assertThat(ImmutableList.of()).doesNotContainSequence(1);",
            "    assertThat(ImmutableList.of()).doesNotContainSubsequence(1);",
            "    assertThat(ImmutableList.of()).containsOnly(1);",
            "    assertThat(ImmutableList.of()).isSubsetOf(1);",
            "",
            "    BugCheckerRefactoringTestHelper.newInstance(BugChecker.class, getClass()).setArgs(\"foo\", \"bar\");",
            "    CompilationTestHelper.newInstance(BugChecker.class, getClass()).setArgs(\"foo\", \"bar\");",
            "  }",
            "}")
        .doTest(TestMode.TEXT_MATCH);
  }
}
