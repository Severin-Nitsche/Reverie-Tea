package com.github.severinnitsche.algebraic_data_types;

import com.github.severinnitsche.function.BiFunction;
import com.github.severinnitsche.function.BiPredicate;
import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.function.Predicate;

public record NestedAccumulator<I, O>(List<I> buffer, Optional<NestedAccumulator<I, O>> nested,
                                      Optional<O> accumulated) {

  @SuppressWarnings("MethodNameSameAsClassName")
  public static <I, O> NestedAccumulator<I, O> NestedAccumulator(List<I> buffer, Optional<NestedAccumulator<I, O>> nested, Optional<O> accumulated) {
    return new NestedAccumulator<>(buffer, nested, accumulated);
  }

  NestedAccumulator<I, O> append(I input) {
    if (nested instanceof Optional.Cons<NestedAccumulator<I, O>> cons)
      return cons.value().append(input);
    else
      return NestedAccumulator(buffer.append(input), nested, accumulated);
  }

  public static <I, O> BiFunction<Either<Throwable, NestedAccumulator<I, O>>, I, Either<Throwable, NestedAccumulator<I, O>>> accumulate(BiPredicate<NestedAccumulator<I, O>, I> accept, Predicate<I> compactBefore, Predicate<I> compactOn, Predicate<I> compactAfter, Function<NestedAccumulator<I, O>, Either<Throwable, NestedAccumulator<I, O>>> compact) {
    return (either, input) -> either.chain(accumulator -> {
      if (accept.test(accumulator, input))
        if (compactBefore.test(input) && compactAfter.test(input))
          return compact.apply(accumulator).map(acc -> acc.append(input)).chain(compact);
        else if (compactBefore.test(input))
          return compact.apply(accumulator).map(acc -> acc.append(input));
        else if (compactAfter.test(input))
          return compact.apply(accumulator.append(input));
        else if (compactOn.test(input))
          return compact.apply(accumulator);
        else
          return Either.from(accumulator.append(input));
      else
        return Either.from(new Throwable(input + " is not acceptable in the context: " + accumulator));
    });
  }
}
