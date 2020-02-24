package org.jdraft.runtime;

import java.util.Arrays;
import junit.framework.TestCase;
import org.jdraft._codeUnit;
import org.junit.Assert;

/**
 *
 * @author Eric
 */
public class _proxyTest extends TestCase {
    
    public void testSimpleProxy(){
        _proxy p = new _proxy("");
        assertEquals("", p.instance);
        
        //call constructor & create new proxy
        _proxy p2 = p.of("Eric");
        //assertNotNull( String.class.getClassLoader() );
        //assertNotNull( p.getClassLoader( ));
        
        try{
            p.get_class();
            fail("expected exception for no code model present");
        }catch(_runtimeException ahe){
            //expected
        }
        
        //call method
        assertEquals(0, p.call("length"));
        //call method
        assertTrue (Arrays.equals( new byte[0], (byte[])p.call("getBytes")) );
        String s = new String("");
        
    }
    
    public void testProxyEqualsHashcodeToString(){
        _runtime adhoc = _runtime.of(
            "public class A{",
            "    private int i = 1;",
            "    public boolean equals(Object other){",
            "        return (other instanceof A) && ((A)other).i == i;",
            "    }",
            "    public void setI(int i){",
            "        this.i = i;",
            "    }",
            "    public int hashCode(){",
            "        return i;",
            "    }",
            "    public String toString(){",
            "        return i+\"\";",
            "    }",    
            "}");
        _proxy a = adhoc.proxy("A");
        _proxy b = adhoc.proxy("A");
        
        
        //make sure when the proxies are equal, they have the same 
        // equals, toString, and hashCode
        assertEquals(a, b); //we can use the proxy equals method
        assertTrue( a.equals(b));
        assertTrue( b.equals(a));
        
        assertEquals( a.hashCode(), b.hashCode()); //proxy hashcode delegates to instance hashcode
        assertEquals( a.call("hashCode"), b.call("hashCode"));
        assertEquals( a.hashCode(), b.call("hashCode"));
        
        assertEquals( a.toString(), b.toString());
        assertEquals( a.toString(), b.call("toString"));
        assertEquals( a.call("toString"), b.call("toString"));       
        
        //now change value on B and verify that everything changes
        b.set("i",12345);
        
        Assert.assertNotEquals(a, b); //we can use the proxy equals method
        assertFalse( a.equals(b));
        assertFalse( b.equals(a));
        
        Assert.assertNotEquals( a.hashCode(), b.hashCode()); //proxy hashcode delegates to instance hashcode
        Assert.assertNotEquals( a.call("hashCode"), b.call("hashCode"));
        Assert.assertNotEquals( a.hashCode(), b.call("hashCode"));
        
        Assert.assertNotEquals( a.toString(), b.toString());
        Assert.assertNotEquals( a.toString(), b.call("toString"));
        Assert.assertNotEquals( a.call("toString"), b.call("toString"));               
    }
    
    public void testGetOrIsBoolean(){
        _runtime adhoc = _runtime.of(
            "public class A{",
            "    private boolean done = false;",
            "    public boolean isDone(){",
            "        return done;",
            "    }",
            "    public A setDone(boolean done){",
            "        this.done = done;",
            "        return this;",
            "    }",
            "}");
        
        _proxy proxy = adhoc.proxy("A");
        assertEquals(false, proxy.get("done")); //this should call isDone; done is private
        proxy.set("done", true);//call setDone()
        assertEquals(true, proxy.get("done")); //this should call isDone; done is private
    }
    
    public void testProxyFullApi(){
        _javaFile sc = _javaFile.of( 
            "public class A{",
            "    private boolean done = false;",
            "    public A( boolean d){",
            "        this.done = d;",
            "    }",    
            "    public boolean isDone(){",
            "        return done;",
            "    }",
            "    public A setDone(boolean done){",
            "        this.done = done;",
            "        return this;",
            "    }",
            "}"); 
        _runtime adhoc = _runtime.of(sc);
        
        //try a proxy with a ctor arg
        _proxy p = adhoc.proxy("A", false);
        assertNotNull( p.instance );
        assertTrue( p.getClassLoader() instanceof _classLoader);
        _codeUnit thecode = p.get_class();
        assertEquals(thecode, sc.codeModel);
    }
}
