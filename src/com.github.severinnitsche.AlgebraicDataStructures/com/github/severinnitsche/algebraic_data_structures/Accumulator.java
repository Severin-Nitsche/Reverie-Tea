package com.github.severinnitsche.algebraic_data_structures;

import com.github.severinnitsche.function.BiPredicate;
import com.github.severinnitsche.function.Function;

public record Accumulator<I, O>(List<I>buffer, List<O>accumulated, Function<List<I>, Either<Throwable,O>> transform,
                                BiPredicate<List<I>, I>acceptor, BiPredicate<List<I>, I> before,
                                BiPredicate<List<I>, I>on, BiPredicate<List<I>, I>after) {
  public Either<Throwable,Accumulator<I, O>> reduce() {
    if (buffer instanceof List.Cons)
      return reduce(transform.apply(buffer));
    else
      return Either.from(this);
  }

  @SuppressWarnings("unchecked")
  private Either<Throwable,Accumulator<I,O>> reduce(Either<Throwable,O> either) {
    if(either instanceof Either.Right<Throwable,O> right)
      return Either.from(new Accumulator<>(List.empty(),accumulated.append(right.value()),transform,acceptor,before,on,after));
    else
      return (Either<Throwable, Accumulator<I, O>>)either;
  }

  //TODO: refactor the functional interfaces into arguments to this function.
  // They are not part of the data of the Accumulator, they are its functionality
  public Either<Throwable, Accumulator<I, O>> accumulate(I in) {
    if (acceptor.test(buffer, in))
      if (before.test(buffer, in) && after.test(buffer, in))
        return reduce().map(accumulator -> accumulator.push(in)).flatmap(Accumulator::reduce);
      else if (before.test(buffer, in))
        return reduce().map(accumulator -> accumulator.push(in));
      else if (after.test(buffer, in))
        return push(in).reduce();
      else if (on.test(buffer, in))
        return reduce();
      else
        return Either.from(push(in));
    else
      return Either.from(new Throwable(in + " was not accepted in the context " + buffer));
  }

  private Accumulator<I, O> push(I in) {
    return new Accumulator<>(buffer.append(in), accumulated, transform, acceptor, before, on, after);
  }
}
