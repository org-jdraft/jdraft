package org.jdraft;

import com.github.javaparser.ast.Node;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class _modelEntityWalkTest extends TestCase {

    public static final int ID = 100;

    /** Javadoc */
    static{
        System.out.println("static block");
    }

    public static class NestedClass{
        public String toString(){
            return "A";
        }

    }

    public enum E{

    }

    public interface I{

    }

    public @interface A{

    }

    public void testE(){
        _class _c = _class.of(_modelEntityWalkTest.class);


        List<_type> ts = new ArrayList<>();
        Walk.in( _c, _type.class, n->ts.add(n) );
        //_c.walk(_type.class, n->ts.add(n) );
        assertEquals( 5, ts.size());



        List<Node> l = new ArrayList<>();
        Walk.postOrder(_c, n-> l.add( n) );
        //_c.walkPostOrder(n-> l.add( n) );
    }
}
