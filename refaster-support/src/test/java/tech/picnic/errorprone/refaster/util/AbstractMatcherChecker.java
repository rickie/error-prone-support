package tech.picnic.errorprone.refaster.util;

import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.NewClassTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;

/** Abstract class for {@link BugChecker}s which simply delegate to a specific {@link Matcher}. */
// XXX: Extend so that all `ExpressionTree` matchers are implemented.
abstract class AbstractMatcherChecker extends BugChecker
    implements MethodInvocationTreeMatcher, NewClassTreeMatcher {
  private static final long serialVersionUID = 1L;

  private final Matcher<ExpressionTree> matcher;

  AbstractMatcherChecker(Matcher<ExpressionTree> matcher) {
    this.matcher = matcher;
  }

  @Override
  public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state) {
    return match(tree, state);
  }

  @Override
  public Description matchNewClass(NewClassTree tree, VisitorState state) {
    return match(tree, state);
  }

  private Description match(ExpressionTree tree, VisitorState state) {
    return matcher.matches(tree, state) ? describeMatch(tree) : Description.NO_MATCH;
  }
}
