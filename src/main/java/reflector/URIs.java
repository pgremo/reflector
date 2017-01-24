package reflector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.System.getProperty;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

final public class URIs {
  private URIs() {
  }

  public static Stream<URI> getURIs(String target) {
    try {
      return EnumerationStream.of(currentThread().getContextClassLoader().getResources(target))
        .map(x -> {
          try {
            return x.toURI();
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
        });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Stream<URI> getJavaClassPath() {
    return stream(getProperty("java.class.path").split(":")).map(Paths::get).map(Path::toUri);
  }
}
