package tech.picnic.errorprone.bugpatterns;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.BugCheckerRefactoringTestHelper.TestMode;
import com.google.errorprone.CompilationTestHelper;
import org.junit.jupiter.api.Test;

final class DirectReturnTest {
  @Test
  void identification() {
    CompilationTestHelper.newInstance(DirectReturn.class, getClass())
        .addSourceLines(
            "A.java",
            """
            import static org.mockito.Mockito.mock;
            import static org.mockito.Mockito.spy;

            import java.util.function.Supplier;

            class A {
              private String field;

              void emptyMethod() {}

              void voidMethod() {
                toString();
                return;
              }

              String directReturnOfParam(String param) {
                return param;
              }

              String assignmentToField() {
                field = toString();
                return field;
              }

              Object redundantAssignmentToParam(String param) {
                // BUG: Diagnostic contains:
                param = toString();
                return param;
              }

              String redundantMockAssignmentToParam(String param) {
                // BUG: Diagnostic contains:
                param = mock();
                return param;
              }

              Object redundantMockWithExplicitTypeAssignmentToParam(String param) {
                // BUG: Diagnostic contains:
                param = mock(String.class);
                return param;
              }

              Object salientMockAssignmentToParam(String param) {
                param = mock();
                return param;
              }

              String redundantAssignmentToLocalVariable() {
                String variable = null;
                // BUG: Diagnostic contains:
                variable = toString();
                return variable;
              }

              String unusedAssignmentToLocalVariable(String param) {
                String variable = null;
                variable = toString();
                return param;
              }

              String redundantVariableDeclaration() {
                // BUG: Diagnostic contains:
                String variable = toString();
                return variable;
              }

              String redundantSpyVariableDeclaration() {
                // BUG: Diagnostic contains:
                String variable = spy();
                return variable;
              }

              Object redundantSpyWithExplicitTypeVariableDeclaration() {
                // BUG: Diagnostic contains:
                String variable = spy(String.class);
                return variable;
              }

              Object salientSpyTypeVariableDeclaration() {
                String variable = spy("name");
                return variable;
              }

              String unusedVariableDeclaration(String param) {
                String variable = toString();
                return param;
              }

              String assignmentToAnnotatedVariable() {
                @SuppressWarnings("HereBeDragons")
                String variable = toString();
                return variable;
              }

              String complexReturnStatement() {
                String variable = toString();
                return variable + toString();
              }

              String assignmentInsideIfClause() {
                String variable = null;
                if (true) {
                  variable = toString();
                }
                return variable;
              }

              String redundantAssignmentInsideElseClause() {
                String variable = toString();
                if (true) {
                  return variable;
                } else {
                  // BUG: Diagnostic contains:
                  variable = "foo";
                  return variable;
                }
              }

              Supplier<String> redundantAssignmentInsideLambda() {
                return () -> {
                  // BUG: Diagnostic contains:
                  String variable = toString();
                  return variable;
                };
              }
            }
            """)
        .doTest();
  }

  @Test
  void replacement() {
    BugCheckerRefactoringTestHelper.newInstance(DirectReturn.class, getClass())
        .addInputLines(
            "A.java",
            """
            class A {
              String m1() {
                String variable = null;
                variable = toString();
                return variable;
              }

              String m2() {
                String variable = toString();
                return variable;
              }
            }
            """)
        .addOutputLines(
            "A.java",
            """
            class A {
              String m1() {
                String variable = null;
                return toString();
              }

              String m2() {
                return toString();
              }
            }
            """)
        .doTest(TestMode.TEXT_MATCH);
  }
}
