package tech.picnic.errorprone.refastertemplates;

import static com.google.common.collect.MoreCollectors.toOptional;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

final class ReactorTemplatesTest implements RefasterTemplateTestCase {
  @Override
  public ImmutableSet<?> elidedTypesAndStaticImports() {
    return ImmutableSet.of(assertThat(0));
  }

  ImmutableSet<Mono<Integer>> testMonoFromOptional() {
    return ImmutableSet.of(
        Mono.defer(() -> Mono.justOrEmpty(Optional.of(1))),
        Mono.defer(() -> Mono.justOrEmpty(Optional.of(2))));
  }

  Mono<Void> testMonoDeferredError() {
    return Mono.error(() -> new IllegalStateException());
  }

  Flux<Void> testFluxDeferredError() {
    return Flux.error(() -> new IllegalStateException());
  }

  Mono<Void> testMonoErrorSupplier() {
    return Mono.error(((Supplier<RuntimeException>) null));
  }

  Flux<Void> testFluxErrorSupplier() {
    return Flux.error(((Supplier<RuntimeException>) null));
  }

  Mono<String> testMonoThenReturn() {
    return Mono.empty().thenReturn("foo");
  }

  Mono<Integer> testMonoSwitchIfEmptyOfEmptyPublisher() {
    return Mono.just(1);
  }

  ImmutableSet<Flux<Integer>> testFluxSwitchIfEmptyOfEmptyPublisher() {
    return ImmutableSet.of(Flux.just(1), Flux.just(2));
  }

  ImmutableSet<Flux<Integer>> testFluxConcatMap() {
    return ImmutableSet.of(Flux.just(1).concatMap(Mono::just), Flux.just(2).concatMap(Mono::just));
  }

  Flux<Integer> testFluxConcatMapIterable() {
    return Flux.just(1, 2).concatMapIterable(ImmutableList::of);
  }

  Flux<String> testMonoFlatMapToFlux() {
    return Mono.just("foo").flatMap(s -> Mono.just(s + s)).flux();
  }

  Flux<String> testMonoFlux() {
    return Mono.just("foo").flux();
  }

  ImmutableSet<Mono<Optional<String>>> testMonoCollectToOptional() {
    return ImmutableSet.of(
        Mono.just("foo").flux().collect(toOptional()),
        Mono.just("bar").flux().collect(toOptional()));
  }

  Mono<Number> testMonoCast() {
    return Mono.just(1).cast(Number.class);
  }

  Flux<Number> testFluxCast() {
    return Flux.just(1).cast(Number.class);
  }

  ImmutableSet<PublisherProbe<Void>> testPublisherProbeEmpty() {
    return ImmutableSet.of(PublisherProbe.empty(), PublisherProbe.empty());
  }

  StepVerifier.FirstStep<Integer> testStepVerifierFromMono() {
    return Mono.just(1).as(StepVerifier::create);
  }

  StepVerifier.FirstStep<Integer> testStepVerifierFromFlux() {
    return Flux.just(1).as(StepVerifier::create);
  }

  StepVerifier.Step<Integer> testStepVerifierStepExpectNextEmpty() {
    return StepVerifier.create(Mono.just(0));
  }

  ImmutableSet<StepVerifier.Step<String>> testStepVerifierStepExpectNext() {
    return ImmutableSet.of(
        StepVerifier.create(Mono.just("foo")).expectNext("bar"),
        StepVerifier.create(Mono.just("baz")).expectNext("qux"));
  }

  Duration testStepVerifierLastStepVerifyComplete() {
    return StepVerifier.create(Mono.empty()).verifyComplete();
  }

  Duration testStepVerifierLastStepVerifyError() {
    return StepVerifier.create(Mono.empty()).verifyError();
  }

  ImmutableSet<Duration> testStepVerifierLastStepVerifyErrorClass() {
    return ImmutableSet.of(
        StepVerifier.create(Mono.empty()).verifyError(IllegalArgumentException.class),
        StepVerifier.create(Mono.empty()).verifyError(IllegalStateException.class));
  }

  Duration testStepVerifierLastStepVerifyErrorMatches() {
    return StepVerifier.create(Mono.empty())
        .verifyErrorMatches(IllegalArgumentException.class::equals);
  }

  Duration testStepVerifierLastStepVerifyErrorSatisfies() {
    return StepVerifier.create(Mono.empty()).verifyErrorSatisfies(t -> {});
  }

  Duration testStepVerifierLastStepVerifyErrorMessage() {
    return StepVerifier.create(Mono.empty()).verifyErrorMessage("foo");
  }

  Duration testStepVerifierLastStepVerifyTimeout() {
    return StepVerifier.create(Mono.empty()).verifyTimeout(Duration.ZERO);
  }
}