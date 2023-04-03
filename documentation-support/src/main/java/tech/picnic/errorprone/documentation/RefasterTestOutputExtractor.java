package tech.picnic.errorprone.documentation;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.VisitorState;
import com.google.errorprone.annotations.Immutable;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import java.util.Optional;
import java.util.regex.Pattern;
import tech.picnic.errorprone.documentation.models.RefasterTemplateCollectionTestData;
import tech.picnic.errorprone.documentation.models.RefasterTemplateTestData;

@Immutable
@AutoService(Extractor.class)
public final class RefasterTestOutputExtractor
    implements Extractor<RefasterTemplateCollectionTestData> {
  private static final Pattern TEST_INPUT_CLASS_NAME_PATTERN = Pattern.compile("(.*)TestOutput");

  @Override
  public String identifier() {
    return "refaster-test-output";
  }

  @Override
  public Optional<RefasterTemplateCollectionTestData> tryExtract(
      ClassTree tree, VisitorState state) {
    Optional<String> className = getClassUnderTest(tree);
    if (className.isEmpty()) {
      return Optional.empty();
    }

    ImmutableList<RefasterTemplateTestData> templateTests =
        tree.getMembers().stream()
            .filter(m -> m instanceof MethodTree)
            .map(MethodTree.class::cast)
            .filter(m -> m.getName().toString().startsWith("test"))
            .map(
                m ->
                    RefasterTemplateTestData.create(
                        m.getName().toString().replace("test", ""), getSourceCode(m, state)))
            .collect(toImmutableList());

    return Optional.of(
        RefasterTemplateCollectionTestData.create(className.orElseThrow(), true, templateTests));
  }

  private static Optional<String> getClassUnderTest(ClassTree tree) {
    return Optional.of(TEST_INPUT_CLASS_NAME_PATTERN.matcher(tree.getSimpleName().toString()))
        .filter(java.util.regex.Matcher::matches)
        .map(m -> m.group(1));
  }

  // XXX: Duplicated from `SourceCode`. Can we do better?
  private String getSourceCode(MethodTree tree, VisitorState state) {
    String src = state.getSourceForNode(tree);
    return src != null ? src : tree.toString();
  }
}
