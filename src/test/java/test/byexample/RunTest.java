package test.byexample;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.runtime._runtime;

import java.util.UUID;

public class RunTest extends TestCase {

    //at runtime, extend an existing class and implement an interface/method
    public void testBuild(){
        //build a new _class that extends an existing class
        _class _c = _class.of("aaa.Custom").extend(_runtimeByExampleTest.ExistingClass.class);

        //modify the source to implement the interface and add the field & method
        _c.implement(new _runtimeByExampleTest.Descriptive(){
            public int a = 100;
            @Override
            public String describe(){
                return "Custom Class ("+a+")";
            }
        });
        //compile & load the class then return a new instance
        _runtimeByExampleTest.Descriptive adhoc = (_runtimeByExampleTest.Descriptive) _runtime.instanceOf( _c );
        assertEquals("Custom Class (100)", adhoc.describe());
        //the instance can be cast to the base class type
        _runtimeByExampleTest.ExistingClass ec = (_runtimeByExampleTest.ExistingClass)adhoc;
        assertNotNull( ec.id() ); //we can call the instance method from base class
    }
    public static class ExistingClass{
        public String id(){
            return UUID.randomUUID().toString();
        }
    }
    public interface Descriptive{
        String describe();
    }
}
