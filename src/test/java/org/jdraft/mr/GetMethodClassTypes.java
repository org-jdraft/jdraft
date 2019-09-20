package org.jdraft.mr;

import com.github.javaparser.ast.Node;
import junit.framework.TestCase;
import org.jdraft.Stencil;
import org.jdraft.Tokens;
import org.jdraft._class;
import org.jdraft.proto.$;
import org.jdraft.proto.$parameter;
import org.jdraft.proto.$proto;
import org.jdraft.proto.$typeRef;
import org.jdraft.runtime._typeTree;

public class GetMethodClassTypes extends TestCase {


    public static class FF{

    }
    public void testGetMethodParameterTypes(){
        class c extends FF{
            void m(String s, int w){

            }
        }
        //$parameter.of().printIn(c.class);
        $.of().$hasChild();
        $.of().$hasParent();
        $.of().$hasAncestor();
        $.of().$hasDescendant();

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
        $proto.$tokens t = new $proto.$tokens();

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
