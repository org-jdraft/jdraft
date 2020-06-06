
package org.jdraft;

import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import org.jdraft.macro._abstract;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _bodyTest extends TestCase {

    public void testBodyTypes(){

        //these are the (3) different types of bodies I want to unify with _body

        //nodeWithBlockStmt always has a blockStmt (it can never be a single statement, or null)
        NodeWithBlockStmt nwbs = Ast.constructorDeclaration("A(){ System.out.println(1);}");

        //nodewithOptional... can have a "totally empty" body (i.e. its null OR a blockStmt)
        NodeWithOptionalBlockStmt nwobs = Ast.methodDeclaration("void m();");

        //nodeWithBody CAN have a single statement (that is not a blockStmt) or a blockStmt
        NodeWithBody nwb = Ast.doStmt("do System.out.println(3); while(true);");

        _body _nwbs = _body.of(nwbs);
        _body _nwobs = _body.of(nwobs);
        _body _nwb = _body.of(nwb);

        assertFalse( _nwb.isEmpty());
        assertTrue( _body.of(";").isEmpty());
        assertTrue(_body.of("{}").isEmpty());
        assertTrue( _nwobs.isEmpty());
        assertFalse( _nwbs.isEmpty());

        assertEquals( nwbs, _nwbs.astParentNode());
        assertEquals( nwobs, _nwobs.astParentNode());
        assertEquals( nwb, _nwb.astParentNode());

        assertTrue( _nwbs.astStatement() instanceof BlockStmt);
        assertNull( _nwobs.astStatement() ); //this should be
        assertTrue( _nwb.astStatement() instanceof ExpressionStmt);

        assertEquals(1, _nwbs.getAstStatements().size());
        assertEquals(0, _nwobs.getAstStatements().size());

        assertEquals(1, _nwb.getAstStatements().size());
        assertNotNull(_nwb.astStatement());
        //System.out.println( _nwb.astStatement() );

        assertTrue(_nwbs.isImplemented() );
        assertFalse(_nwobs.isImplemented() );
        assertTrue(_nwb.isImplemented() );

        assertTrue(_nwb.is("System.out.println(3);"));
        assertTrue(_nwb.is(()->System.out.println(3)));
        assertTrue( _nwbs.is("{ System.out.println(1); }"));
        assertTrue( _nwbs.is(()->{ System.out.println(1); }));
        assertTrue(_nwobs.is(";"));
        assertTrue(_nwobs.is(""));

        assertTrue( _nwb.ast() instanceof ExpressionStmt);
        assertNull(_nwobs.ast());
        assertTrue( _nwbs.ast() instanceof BlockStmt);

        _nwb.is(_stmt.of("System.out.println(3);"));
        _nwobs.is(_stmt.of("{ System.out.println(1); }"));
        _nwbs.is(_emptyStmt.of());

        _nwb.hashCode();
        _nwobs.hashCode();
        _nwbs.hashCode();



        _nwobs.hashCode();
        _nwbs.hashCode();


        _nwb.clear();
        _nwobs.clear();
        _nwbs.clear();


    }

    public void testFromScratch(){
        _body _b = _body.of();
        System.out.println(_b);
    }

    public void testAddBodyToNonImplementedMethod(){
        _method m = _method.of("public void m();");
        m.add( "System.out.println(1);");
        assertEquals( Stmt.of(()->System.out.println(1)), m.getAstStatement(0));
        m.getBody().clear();
    }

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
        

        assertEquals( _wComment.hashCode(), _woComment.hashCode());
        assertEquals( _wComment, _woComment);
        
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
        Tree.flattenLabel( _c.getMethod("k").ast(), "label");
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
        _initBlock _sb = _c.getInitBlock(0);
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
        assertTrue( _c.listInitBlocks().get(0).ast().getJavadocComment().isPresent());
        
        
        assertTrue( _c.getInitBlock(0).ast().getJavadocComment().get().getContent().contains( "A JAVADOC"));
        
        _c.getInitBlock(0).ast().addAnnotation( Ast.annotationExpr( "@ann"));
        
        _c.getInitBlock(0).ast().getJavadocComment().get().setContent( "Different Javadoc");
        
        //System.out.println( _c );
        
    }
}
