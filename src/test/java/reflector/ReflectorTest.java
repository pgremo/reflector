package reflector;

import javassist.bytecode.ClassFile;
import org.junit.Test;

import static java.util.stream.Collectors.toList;
import static reflector.URIs.getURIs;

public class ReflectorTest {

  @Test
  public void shouldScanClasses() {
    System.out.println(new Reflector().scan(getURIs("junit/framework")).map(ClassFile::getName).collect(toList()));
  }

}