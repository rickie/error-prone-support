package tech.picnic.errorprone.workshop.bugpatterns;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.BugPattern.StandardTags.SIMPLIFICATION;
import static com.google.errorprone.matchers.ChildMultiMatcher.MatchType.AT_LEAST_ONE;
import static com.google.errorprone.matchers.Matchers.annotations;
import static com.google.errorprone.matchers.Matchers.anyOf;
import static com.google.errorprone.matchers.Matchers.isType;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.errorprone.BugPattern;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.matchers.MultiMatcher;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import javax.lang.model.element.Modifier;

/** A {@link BugChecker} that flags non-canonical JUnit method declarations. */
@AutoService(BugChecker.class)
@BugPattern(
    summary = "JUnit method declaration can likely be improved",
    severity = WARNING,
    tags = SIMPLIFICATION)
@SuppressWarnings("UnusedVariable" /* This check is yet to be implemented as part of the demo. */)
public final class JUnitTestMethodDeclaration extends BugChecker {
  private static final long serialVersionUID = 1L;
  private static final ImmutableSet<Modifier> ILLEGAL_MODIFIERS =
      Sets.immutableEnumSet(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.PUBLIC);
  private static final MultiMatcher<MethodTree, AnnotationTree> TEST_METHOD =
      annotations(AT_LEAST_ONE, anyOf(isType("org.junit.jupiter.api.Test")));

  /** Instantiates a new {@link JUnitTestMethodDeclaration} instance. */
  public JUnitTestMethodDeclaration() {}

  // XXX: Part 1: Ensure JUnit test methods don't use {@link ILLEGAL_MODIFIERS}.
  // XXX: Part 2: If a method name starts with `test`, drop it.
}
