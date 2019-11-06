package org.jdraft.mr;

import com.github.javaparser.ast.Node;
import junit.framework.TestCase;
import org.jdraft.text.Stencil;
import org.jdraft._class;
import org.jdraft.pattern.*;
import org.jdraft.runtime._typeTree;

public class GetMethodClassTypes extends TestCase {


    public static class FF{

    }
    public void testGetMethodParameterTypes(){
        class c extends FF{
            void m(String s, int w){

            }
        }
        // $parameter.of().printIn(c.class);



        //$parameter.of().$hasDescendant();
        //$typeRef.of().$hasParent($parameter.of()).printIn( c.class );

        $typeRef.of(String.class).forSelectedIn(c.class, s-> System.out.println(s));


        _class _c = _class.of(c.class);
        System.out.println( _typeTree.of(_c).listAllAncestors(FF.class));
    }

    //parse the properties from the node as key value pairs into the tokens
    //t.parseTo(node);

    public static String printTemplate(Node n, String...format ){

        Stencil s = Stencil.of(format);
        $pattern.$tokens t = new $pattern.$tokens();

        //NodeTokens or some shit
        t.put("class", n.getClass() );
        if( n.getComment().isPresent() ){
            t.put( "comment", n.getComment().get());
        }
        t.put("end", n.getEnd().get());
        t.put("range", n.getRange());
        t.put("begin", n.getBegin().get());
        return null;
    }
}
