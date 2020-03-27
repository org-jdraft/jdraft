package org.jdraft;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eric
 */
public class _initBlockTest extends TestCase {

    public void testBuildFromScratch(){
        _initBlock _ib = _initBlock.of();
        _ib.setStatic();
        assertEquals( _initBlock.of("static{}"), _ib);
        assertEquals( Ast.initBlock( "static {}"), _ib.astInit);
    }
    
    @interface T{
        
    }


    public void testAddStaticBlock(){
        _class _c = _class.of("aaaa.b.C").staticBlock(new Object(){
            {System.out.println(1);}
        });
        assertEquals(1, _c.listInitBlocks().size());
        assertTrue( _c.getInitBlock(0).isStatic());
    }

    public void testAddInitBlock(){
        _class _c = _class.of("aaaa.b.C").staticBlock(new Object(){
            {System.out.println(1);}
        });
        assertEquals(1, _c.listInitBlocks().size());
        assertTrue( _c.getInitBlock(0).isStatic());
    }

    public void testClassInitBlock(){
        class C{
            {System.out.println( 1 );}
        }

        _class _c = _class.of(C.class);
        assertEquals(1, _c.listInitBlocks().size());
        //System.out.println( _c.listInitBlocks().get(0) );
        assertFalse(_c.listInitBlocks().get(0).isStatic());
    }


    public void testClassStaticInitBlock(){
        class C{
            {System.out.println( 1 );}
        }

        _class _c = _class.of(C.class);
        assertEquals(1, _c.listInitBlocks().size());
        //System.out.println( _c.listInitBlocks().get(0) );
        assertFalse(_c.listInitBlocks().get(0).isStatic());
    }


    class C{
        int a;
        {System.out.println(a);}

        int b;
        {System.out.println(b);}
    }
    public void testMultipleStaticAndInstanceInitBlocks(){
        _initBlock _ib = _initBlock.of( new Object(){
            int a;
            {System.out.println(a);}

        });

        assertFalse(_ib.isStatic());


    }

    public void testInitStaticBlockInitializer(){
        _initBlock _ib = _initBlock.of( new Object(){
            {
                System.out.println(1);
                System.out.println(2);
            }
        }).setStatic();
        assertTrue(_ib.isStatic());
        assertEquals( _ib.getStatement(0), Statements.of( ()-> System.out.println(1)) );
        assertEquals( _ib.getStatement(1), Statements.of( ()-> System.out.println(2)) );
    }
    
    public void testIB(){

        _initBlock _sb = _initBlock.of("i = 100;");
        assertFalse(_sb.isStatic() );
        _sb.setStatic(true);
        assertTrue(_sb.isStatic() );
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

    public void testInitBlocksContainingLocalClass(){
        _class _c = _class.of(_initBlockTest.class);
        _initBlock _sb = _c.getInitBlock(0);

        List<_class> localClasses = new ArrayList<_class>();
        //_sb.walkBody( ClassOrInterfaceDeclaration.class, c-> localClasses.add( _class.of(c) ) );
        Tree.in( _sb.getBody().ast(), ClassOrInterfaceDeclaration.class, c-> localClasses.add( _class.of(c) ) );
        System.out.println( localClasses );

    }

    public void testC(){
        _initBlock sb = _initBlock.of("System.out.println(1);").setStatic(true);
        assertTrue( sb.is( "System.out.println(1);") );
        sb.setJavadoc( "Javadoc" );
        assertFalse( sb.is( "System.out.println(1);") );
        System.out.println( sb );

        InitializerDeclaration id = Ast.initBlock( "/**",
                " * Javadoc",
                " */",
                "static {",
                "System.out.println(1);",
                "}");
        System.out.println( id );
        System.out.println( id.getJavadocComment().get());

        assertTrue( sb.is( "/**",
                           " * Javadoc",
                           " */", 
                           "static {", 
                              "System.out.println(1);",
                            "}") );
        
    }
}
