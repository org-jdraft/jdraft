package test;

import junit.framework.TestCase;
import org.jdraft._class;

public class InitializerTest extends TestCase {
    class C{
        {System.out.println("init");}
        public C(){
            System.out.println("construct");
        }
        {System.out.println("now"+this);}
    }

    public void testI(){
        C c = new C();
        C d = new C();
    }

    public void testC(){
        _class _c = _class.of( C.class);
        System.out.println( _c );
    }
}
