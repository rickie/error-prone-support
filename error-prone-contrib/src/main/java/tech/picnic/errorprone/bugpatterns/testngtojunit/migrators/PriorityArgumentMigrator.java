package tech.picnic.errorprone.bugpatterns.testngtojunit.migrators;

import com.google.errorprone.VisitorState;
import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.fixes.SuggestedFix;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import java.util.Optional;
import org.testng.annotations.Test;
import tech.picnic.errorprone.bugpatterns.testngtojunit.Migrator;
import tech.picnic.errorprone.bugpatterns.testngtojunit.TestNGMetadata;
import tech.picnic.errorprone.bugpatterns.testngtojunit.TestNGMigrationContext;
import tech.picnic.errorprone.bugpatterns.util.SourceCode;

/** An {@link Migrator} that migrates the {@link Test#priority()} argument. */
@Immutable
public class PriorityArgumentMigrator implements Migrator {
  @Override
  public Optional<SuggestedFix> createFix(
      TestNGMigrationContext context,
      MethodTree methodTree,
      ExpressionTree argumentValue,
      VisitorState state) {
    return Optional.of(
        SuggestedFix.builder()
            .addImport("org.junit.jupiter.api.Order")
            .addImport("org.junit.jupiter.api.TestMethodOrder")
            .addImport("org.junit.jupiter.api.MethodOrderer")
            .merge(
                SuggestedFix.prefixWith(
                    methodTree,
                    String.format("@Order(%s)\n", SourceCode.treeToString(argumentValue, state))))
            .merge(
                SuggestedFix.prefixWith(
                    context.getClassTree(),
                    "@TestMethodOrder(MethodOrderer.OrderAnnotation.class)\n"))
            .build());
  }

  @Override
  public boolean canFix(
      TestNGMigrationContext context, TestNGMetadata.AnnotationMetadata annotationMetadata) {
    return annotationMetadata.getArguments().containsKey("priority");
  }
}
