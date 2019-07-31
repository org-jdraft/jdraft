
package org.jdraft;

import org.jdraft._class;
import org.jdraft._body;
import org.jdraft._method;
import org.jdraft._constructor;
import org.jdraft._interface;
import org.jdraft._staticBlock;
import org.jdraft.Ast;
import org.jdraft.macro._abstract;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _bodyTest extends TestCase {

    public void testBodyCommentEqualsHashCode(){
        class C{
            @_abstract void noBody(){}
            @_abstract void noBodyComment(){ /*comment*/ }
            
            void withComment(){
                //a comment
                System.out.println(1);
            }
            void withoutComment(){
                System.out.println(1);
            }            
        }
        _class _c = _class.of(C.class);
        _body _wComment = _c.getMethod("withComment").getBody();
        _body _woComment = _c.getMethod("withoutComment").getBody();
        
        assertEquals( _wComment, _woComment);
        assertEquals( _wComment.hashCode(), _woComment.hashCode());
        
        _body _noBody = _c.getMethod("noBody").getBody();
        _body _noBodyComment = _c.getMethod("noBodyComment").getBody();
        
        assertEquals( _noBody, _noBodyComment);
        assertEquals( _noBody.hashCode(), _noBodyComment.hashCode());
        
    }
    
    public void testBodyAddComment(){
        class C{
            public void f(){
                // comment
                System.out.println(1);
            }
        }
        _class _c = _class.of(C.class);
        _method _m = _c.getMethod("f");
        System.out.println( _m.getBody() );
        _m.add("/** comment */" );
        System.out.println( _m.getBody() );
    }

    public void testFlattenLabel(){
        class G{
            void k(){
                System.out.println(0);
                label:{
                    System.out.println(1);
                    System.out.println(2);
                }
                System.out.println(3);

                label:{
                    System.out.println(4);
                    System.out.println(5);
                }
                System.out.println(6);
            }
        }
        _class _c = _class.of(G.class);
        //_c.getMethod("k").flattenLabel("label");
        Ast.flattenLabel( _c.getMethod("k").ast(), "label");
        //System.out.println( _c );
        //_c.getMethod("k").flattenLabel("label");
        //Ast.flattenLabel( _c.getMethod("k").ast(), "label");
        System.out.println( _c );
    }

    /** A JAVADOC */
    static{
        
    }
    
    interface bodyTypes {
        void noBody();
        
        static void someBody(){
            System.out.println("The Body");
        }        
    }
    
    static class F{
        static{
            System.out.println("Body");
        }
        public F(){}
    }
    
    public void testBody(){
        _interface _i = _interface.of(bodyTypes.class);
        _method _m = _i.getMethod("noBody");
        _method _sm = _i.getMethod("someBody");
        _class _c = _class.of(F.class);
        _staticBlock _sb = _c.getStaticBlock(0);
        _constructor _ct = _c.getConstructor( 0 );
        
        assertNotNull( _ct.getBody() );
        assertTrue( _ct.hasBody() );
        assertTrue( _ct.getBody().isEmpty() );
        assertTrue( _ct.getBody().is( ""));
        
        assertNotNull( _sb.getBody() );
        assertTrue( _sb.hasBody() );
        assertTrue( _sb.is( "System.out.println(\"Body\");"));

        assertNotNull(_m.getBody());
        assertFalse( _m.hasBody() );
        assertFalse(_m.getBody().isImplemented());
        //assertTrue(_m.getBody().isEmpty());
        //assertTrue(_m.getBody().is( null )); //you can ask
        
        assertNotNull(_sm.getBody());
        assertTrue(_sm.getBody().isImplemented());
        assertFalse(_sm.getBody().isEmpty());
        //note the code "style" is different, BUT thjey are still equal
        assertTrue( _sm.getBody().is( "System.out.println(\"The Body\");") );
        assertTrue( _sm.getBody().is( "System.out.println( \"The Body\" );") );
        
    }
    
    public void testStaticBody(){
        _class _c = _class.of(_bodyTest.class );
        assertTrue( _c.listStaticBlocks().get(0).ast().getJavadocComment().isPresent());
        
        
        assertTrue( _c.getStaticBlock(0).ast().getJavadocComment().get().getContent().contains( "A JAVADOC"));
        
        _c.getStaticBlock(0).ast().addAnnotation( Ast.anno( "@ann"));
        
        _c.getStaticBlock(0).ast().getJavadocComment().get().setContent( "Different Javadoc");
        
        System.out.println( _c );
        
    }
}
