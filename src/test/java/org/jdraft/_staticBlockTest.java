package org.jdraft;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class _staticBlockTest extends TestCase {

    
    @interface T{
        
    }
    
    
    
    
    public void testSB(){
        _staticBlock _sb = _staticBlock.of("i = 100;");
        
        //System.out.println( _sb );
    }

    //put a local class inside a static block?
    static{
        class LocalClass{
            int field;
            void methodInLocalClassInStaticBlock(){
                System.out.println(1);

                class LocalClassInLocalClass{
                    String fieldInLocalClassInLocalClass = "Fred";
                    void gt(){
                        System.out.println("888");
                    }
                }
            }
        }
    }

    public void testStaticBlocksContainingLocalClass(){
        _class _c = _class.of(_staticBlockTest.class);
        _staticBlock _sb = _c.getStaticBlock(0);

        List<_class> localClasses = new ArrayList<_class>();
        //_sb.walkBody( ClassOrInterfaceDeclaration.class, c-> localClasses.add( _class.of(c) ) );
        _walk.in( _sb.getBody().ast(), ClassOrInterfaceDeclaration.class, c-> localClasses.add( _class.of(c) ) );
        System.out.println( localClasses );

    }

    public void testC(){
        _staticBlock sb = _staticBlock.of("System.out.println(1);");
        assertTrue( sb.is( "System.out.println(1);") );
        sb.javadoc( "Javadoc" );
        assertFalse( sb.is( "System.out.println(1);") );
        System.out.println( sb );
        
        assertTrue( sb.is( "/**",
                           " * Javadoc",
                           " */", 
                           "static {", 
                              "System.out.println(1);",
                            "}") );
        
    }
}
