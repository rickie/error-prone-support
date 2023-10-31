package tech.picnic.errorprone.workshop.bugpatterns;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.BugPattern.StandardTags.SIMPLIFICATION;
import static com.google.errorprone.matchers.ChildMultiMatcher.MatchType.AT_LEAST_ONE;
import static com.google.errorprone.matchers.Matchers.annotations;
import static com.google.errorprone.matchers.Matchers.anyOf;
import static com.google.errorprone.matchers.Matchers.isType;
import static java.util.function.Predicate.not;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.fixes.SuggestedFixes;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.MultiMatcher;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.code.Symbol;
import java.util.Optional;
import javax.lang.model.element.Modifier;

/** A {@link BugChecker} that flags non-canonical JUnit method declarations. */
@AutoService(BugChecker.class)
@BugPattern(
    summary = "JUnit method declaration can likely be improved",
    severity = WARNING,
    tags = SIMPLIFICATION)
@SuppressWarnings("UnusedVariable" /* This check is yet to be implemented as part of the demo. */)
public final class JUnitTestMethodDeclaration extends BugChecker
    implements BugChecker.MethodTreeMatcher {
  private static final long serialVersionUID = 1L;
  private static final ImmutableSet<Modifier> ILLEGAL_MODIFIERS =
      Sets.immutableEnumSet(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.PUBLIC);
  private static final MultiMatcher<MethodTree, AnnotationTree> TEST_METHOD =
      annotations(AT_LEAST_ONE, anyOf(isType("org.junit.jupiter.api.Test")));

  /** Instantiates a new {@link JUnitTestMethodDeclaration} instance. */
  public JUnitTestMethodDeclaration() {}

  @Override
  public Description matchMethod(MethodTree tree, VisitorState state) {
    if (!TEST_METHOD.matches(tree, state)) {
      return Description.NO_MATCH;
    }

    SuggestedFix.Builder fixBuilder = SuggestedFix.builder();

    SuggestedFixes.removeModifiers(tree.getModifiers(), state, ILLEGAL_MODIFIERS)
        .ifPresent(fixBuilder::merge);

    if (tree.getName().toString().startsWith("test")) {
      Optional<String> improved = tryCanonicalizeMethodName(ASTHelpers.getSymbol(tree));
      improved.map(newName -> fixBuilder.merge(SuggestedFixes.renameMethod(tree, newName, state)));
    }

    return fixBuilder.isEmpty() ? Description.NO_MATCH : describeMatch(tree, fixBuilder.build());
  }

  private static Optional<String> tryCanonicalizeMethodName(Symbol.MethodSymbol symbol) {
    return Optional.of(symbol.getQualifiedName().toString())
        .filter(name -> name.startsWith("test"))
        .map(name -> name.substring("test".length()))
        .filter(not(String::isEmpty))
        .map(name -> Character.toLowerCase(name.charAt(0)) + name.substring(1))
        .filter(name -> !Character.isDigit(name.charAt(0)));
  }
  // XXX: Part 1: Ensure JUnit test methods don't use {@link ILLEGAL_MODIFIERS}.
  // XXX: Part 2: If a method name starts with `test`, drop it.
}
