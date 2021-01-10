package com.github.severinnitsche.algebraic_data_types;

import com.github.severinnitsche.function.CharPredicate;
import com.github.severinnitsche.function.Function;
import com.github.severinnitsche.function.ObjCharPredicate;
import com.github.severinnitsche.util.Arrays;

public record CharAccumulator<O>(CharList buffer, List<O>accumulated, Function<CharList, Either<Throwable,O>>transform,
                                 ObjCharPredicate<CharList>acceptor, ObjCharPredicate<CharList>before,
                                 ObjCharPredicate<CharList>on, ObjCharPredicate<CharList>after) {
  public Either<Throwable,CharAccumulator<O>> reduce() {
    if (buffer instanceof CharList.Cons)
      return reduce(transform.apply(buffer));
    else
      return Either.from(this);
  }

  @SuppressWarnings("unchecked")
  private Either<Throwable,CharAccumulator<O>> reduce(Either<Throwable,O> either) {
    if(either instanceof Either.Right<Throwable,O> right)
      return Either.from(new CharAccumulator<>(CharList.empty(),accumulated.append(right.value()),transform,acceptor,before,on,after));
    else
      return (Either<Throwable, CharAccumulator<O>>)either;
  }

  public Either<Throwable, CharAccumulator<O>> accumulate(char in) {
    if (acceptor.test(buffer, in))
      if (before.test(buffer, in) && after.test(buffer, in))
        return reduce().map(accumulator -> accumulator.push(in)).flatmap(CharAccumulator::reduce);
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

  private CharAccumulator<O> push(char in) {
    return new CharAccumulator<>(buffer.append(in), accumulated, transform, acceptor, before, on, after);
  }
}
