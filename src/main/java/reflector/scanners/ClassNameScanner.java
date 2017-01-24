package reflector.scanners;

import javassist.bytecode.ClassFile;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;

public class ClassNameScanner implements Function<ClassFile, Map<String, Collection<Object>>> {
  @Override
  public Map<String, Collection<Object>> apply(ClassFile x) {
    return singletonMap(ClassNameScanner.class.getName(),  singleton(x));
  }
}
