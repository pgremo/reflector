package reflector;

import javassist.bytecode.ClassFile;
import reflector.scanners.ClassNameScanner;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.Files.newInputStream;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.*;

public class Reflector {

  public Stream<ClassFile> scan(Stream<URI> resources) {
    Map<String, Set<Object>> store = scan(resources, new ClassNameScanner());
    return store.get(ClassNameScanner.class.getName()).stream().map(ClassFile.class::cast);
  }

  @SafeVarargs
  private final Map<String, Set<Object>> scan(Stream<URI> resources, Function<ClassFile, Map<String, Collection<Object>>>... scanners) {
    return resources
      .map(uri -> {
        try {
          return newFileSystem(uri, emptyMap()).provider().getPath(uri);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      })
      .flatMap(path -> {
        try {
          return Files.find(path, Integer.MAX_VALUE, (p, a) -> a.isRegularFile() && p.getFileName().toString().endsWith(".class"));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      })
      .parallel()
      .map(path -> {
        try (DataInputStream in = new DataInputStream(newInputStream(path))) {
          return new ClassFile(in);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      })
      .flatMap(type -> Stream.of(scanners).map(x -> x.apply(type)))
      .map(Map::entrySet)
      .flatMap(Collection::stream)
      .flatMap(x -> x.getValue().stream().map(y -> new AbstractMap.SimpleEntry<>(x.getKey(), y)))
      .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));
  }
}
