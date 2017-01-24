package reflector;

import java.util.Enumeration;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public class EnumerationStream<T> {
  public static <T> Stream<T> of(Enumeration<T> e) {
    return stream(
      new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
        public boolean tryAdvance(Consumer<? super T> action) {
          if (e.hasMoreElements()) {
            action.accept(e.nextElement());
            return true;
          }
          return false;
        }

        public void forEachRemaining(Consumer<? super T> action) {
          while (e.hasMoreElements()) action.accept(e.nextElement());
        }
      }, false);
  }
}
