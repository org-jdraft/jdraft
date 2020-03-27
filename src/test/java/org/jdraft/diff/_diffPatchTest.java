package org.jdraft.diff;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.Log;
import org.jdraft.*;

import java.io.Serializable;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import test.ComplexClass;
import test.ComplexInterface;

import static org.jdraft._java.Component.*;
import java.io.IOException;
import org.jdraft.macro._toCtor;

/**
 *
 * @author Eric
 */
//@Ast.cache
public class _diffPatchTest 
        extends TestCase {
    
    public void testDiffClasses(){
        class L{
            int i=0;
            public void m(){
                System.out.println( "A" );
            }
        }
        class M{
            int i=0;
            public void m(){
                System.out.println("A");
            }
        }
        System.out.println( _diff.of(L.class, M.class) );
        assertEquals(1, _diff.of(L.class, M.class).size());
    }

    public void setUp(){
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
    }
    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());
    }

    public void testDiffParts(){
        class L{
            int i=0;
            public void m(){
                System.out.println( "A" );
            }
            public L(int a){
                this.i = a;
            }
        }

        _class _c1 = _class.of(L.class);
        assertTrue( _c1.isTopLevel());
        assertNotNull( _c1.astCompilationUnit() );

        //System.out.println( "COMP UNIT " + _c1.astCompilationUnit() );

        //System.out.println( "TYPES "+ _c1.astCompilationUnit().getTypes() );

        //assertTrue(_c1.astCompilationUnit().getPrimaryType().isPresent());
        //assertTrue(_c1.astCompilationUnit().getPrimaryTypeName().isPresent());

        _class _c2 = _c1.copy();
        
        assertTrue(_diff.of(_c1,_c2).isEmpty() );
        assertTrue(_diff.fieldsOf(_c1,_c2).isEmpty() );
        assertTrue(_diff.methodsOf(_c1,_c2).isEmpty() );
        assertTrue(_diff.constructorsOf(_c1,_c2).isEmpty() );
        assertTrue(_diff.annosOf(_c1,_c2).isEmpty() );
        
        _c2.addBodyMembers(new Object(){
            
            int f = 10;
            
            public void m2(){
                System.out.println("Hi");
            }
            
            @_toCtor public void L(){}
        }).addAnnos(Deprecated.class);
                
        assertTrue(_diff.of(_c1,_c2).hasRightOnlyAt(FIELD) );
        assertTrue(_diff.fieldsOf(_c1,_c2).hasRightOnlyAt(FIELD) );
        assertTrue(_diff.methodsOf(_c1,_c2).hasRightOnlyAt(METHOD) );
        assertTrue(_diff.constructorsOf(_c1,_c2).hasRightOnlyAt(CONSTRUCTOR) );
        assertTrue(_diff.annosOf(_c1,_c2).hasRightOnlyAt(ANNO) );
        
        assertTrue(_diff.of(_c1,_c2).firstOn(FIELD, "f").isRightOnly() );
        assertTrue(_diff.of(_c1,_c2).firstOn(FIELD).isRightOnly() );
        assertTrue(_diff.fieldsOf(_c1,_c2).firstOn(FIELD).isRightOnly() );
        assertTrue(_diff.methodsOf(_c1,_c2).firstOn(METHOD).isRightOnly() );
        assertTrue(_diff.methodsOf(_c1,_c2).firstOn(METHOD, "m2()").isRightOnly() );
        
        assertTrue(_diff.constructorsOf(_c1,_c2).firstOn(CONSTRUCTOR).isRightOnly() );
        System.out.println( _diff.constructorsOf(_c1,_c2) );
        assertTrue(_diff.constructorsOf(_c1,_c2).firstOn(CONSTRUCTOR, "L()").isRightOnly() );
        assertTrue(_diff.annosOf(_c1,_c2).firstOn(ANNO).isRightOnly() );
        assertTrue(_diff.annosOf(_c1,_c2).firstOn(ANNO, "Deprecated").isRightOnly() );
        
    }

    /*
    public void testDiffTypes(){
        
        _types._impl _ts = _types._impl.of( _class.of("aaaa.bbbb.C"), _interface.of("aaaa.I"));
        _types._impl _ts2 = _types._impl.of( _interface.of("aaaa.I"), _class.of("aaaa.bbbb.C") );
        System.out.println( _typesDiff.INSTANCE.diff(_ts, _ts2) );
        assertTrue( _typesDiff.INSTANCE.diff(_ts, _ts2).isEmpty());
        
        _ts2.add(_enum.of("aaaa.bbbb.cccc.E").constant("A"));
        _diff dl = _typesDiff.INSTANCE.diff(_ts, _ts2); 
        System.out.println( dl );
        assertTrue( dl.hasRightOnly() );
        assertTrue( dl.hasRightOnlyOn(ENUM) );
        assertTrue( dl.hasRightOnlyOn(ENUM, "aaaa.bbbb.cccc.E") );
    }

     */
    
    public void testDiffPackageChange(){
        //verify a complex class copied has no diff
        _class _c = _class.of(ComplexClass.class);
        assertEquals(1, _c.listInnerTypes().size() );
        _class _c2 = _c.copy();
        assertEquals(1, _c2.listInnerTypes().size() );
        System.out.println("C2 "+ _c2 );
        assertTrue(_diff.of(_c, _c2).isEmpty() ) ;
        
        _diff dl = null;
        //start to diff components and verify we can 
       
        _c.setPackage("blaasss");
        assertEquals("NESTST  1 ", 1, _c.listInnerTypes().size() );
        
        dl = _diff.of(_c, _c2);
        System.out.println( dl );
        assertEquals("NESTST  1.1 ", 1, _c.listInnerTypes().size() );
        assertTrue( dl.firstOn(PACKAGE).isChange() );
        dl.patchLeftToRight();        
        
        System.out.println( _c );
        
        
        assertEquals("NESTST  1.5 ", 1, _c.listInnerTypes().size() );
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        assertEquals("NESTST  2 ", 1, _c.listInnerTypes().size() );
        
    }

    public void testCtorChange(){
        _class _c = _class.of("C", new Object(){
            @_toCtor void C(){
            }
        });
        _class _c2 = _class.of("C", new Object(){
            @_toCtor void C(){
            }
        });
        //this will change api
        _c.getConstructor(0).addParameter("final int lastParameter");

        System.out.println( _c );
        System.out.println( _c2 );
        _diff dl = _diff.of(_c, _c2);
        System.out.println( dl);

        dl.hasLeftOnlyAt(_constructor.class);
        dl.hasRightOnlyAt(_constructor.class);

        //assertTrue( dl.firstOn(CONSTRUCTOR).isChange() );
        //assertTrue( dl.hasChange());

        assertTrue(dl.hasLeftOnlyOn(CONSTRUCTOR));
        assertTrue(dl.hasRightOnlyOn(CONSTRUCTOR));
        //assertTrue(dl.hasChangeAt(PARAMETERS));
        dl.patchLeftToRight();

        //verify the patch worked (no changes)
        assertTrue(_diff.of(_c, _c2).isEmpty());
    }
    public void testDiffAndPatchComplexTypes(){
        //verify a complex class copied has no diff
        _class _c = _class.of(ComplexClass.class);
        assertEquals(1, _c.listInnerTypes().size() );
        _class _c2 = _c.copy();
        assertEquals(1, _c2.listInnerTypes().size() );

        System.out.println("C2 "+ _c2 );

        assertTrue(_diff.of(_c, _c2).isEmpty() ) ;
        
        _diff dl = null;
        //start to diff components and verify we can 
       
        /*
        _c.setPackage("blaasss");
        assertEquals("NESTST  1 ", 1, _c.listNests().size() );
        
        dl = _diffList.of(_c, _c2);
        System.out.println( dl );
        assertEquals("NESTST  1.1 ", 1, _c.listNests().size() );
        assertTrue( dl.firstOn(PACKAGE_NAME).isChange() );        
        dl.keepLeft();        
        
        System.out.println( _c );
        
        
        assertEquals("NESTST  1.5 ", 1, _c.listNests().size() );
        assertTrue(_diffList.of(_c, _c2).isEmpty());
        
        assertEquals("NESTST  2 ", 1, _c.listNests().size() );
        */
        
        _c.addImports("java.net.*");
        assertTrue(_diff.of(_c, _c2).firstOn(IMPORT).isLeftOnly() );
        //System.out.println( _diffList.of(_c, _c2) );
        dl = _diff.of(_c, _c2);
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
               
        assertEquals("NESTST  5 ", 1, _c.listInnerTypes().size() );
        
        _c.addAnnos("@AAAAA");
        System.out.println(_diff.of(_c, _c2));
        assertNotNull(_diff.of(_c, _c2).firstOn(ANNO) );
        dl = _diff.of(_c, _c2);
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.setPrivate();
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(MODIFIERS).isChange() );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.setJavadoc(" A Diff Javadoc");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(JAVADOC).isChange() );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
         _c.addExtend("Blarg<Integer>");
        _diff _dl = _diff.of(_c, _c2);        
        assertNotNull( _dl.firstOn(EXTENDS) );        
        _dl.patchLeftToRight();
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.addImplement(Serializable.class);
        _dl = _diff.of(_c, _c2);
        assertTrue( _dl.firstAt(IMPLEMENTS).isLeftOnly()); //verify that an implement was removed between c -> c2        
        _dl.patchLeftToRight(); //ok, push the change to both left and right
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.addImplement("SomeDiffInterface");
        dl = _diff.of(_c, _c2);
        assertNotNull( dl.firstOn(IMPLEMENTS) );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        
        _c.removeTypeParameters();
        dl = _diff.of(_c, _c2);
        assertNotNull( dl.firstOn(TYPE_PARAMETERS) );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.removeInitBlock( _c.getInitBlock(0));
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(INIT_BLOCK).isRightOnly() );
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        
        assertEquals("NESTST   ", 1, _c.listInnerTypes().size() );
        
        //--------------FIELD ON CLASS------------------
        _field _f = _field.of("public int aFieldIAdded = 1023;");
        _c.addField(_f.copy());
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(FIELD).isLeftOnly() );        
        assertEquals( dl.firstOn(FIELD).asLeftOnly().left(), _f );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getField("aFieldIAdded").addAnnos(Deprecated.class);
        
        dl = _diff.of(_c, _c2);
        //System.out.println(_c.getField("aFieldIAdded"));
        //System.out.println(_c2.getField("aFieldIAdded"));
        //assertTrue( dl.firstAt(ANNO)  );       
        System.out.println("DIFFS"+ dl);
        
        //there are many ways to find/signify the same diff
        assertEquals( dl.firstOn(FIELD, "aFieldIAdded").asLeftOnly().left(), _anno.of(Deprecated.class) );    
        assertEquals( dl.firstOn("aFieldIAdded").asLeftOnly().left(), _anno.of(Deprecated.class) );    
        assertEquals( dl.firstOn(FIELD).asLeftOnly().left(), _anno.of(Deprecated.class) );    
        assertEquals( dl.firstAt(ANNO).asLeftOnly().left(), _anno.of(Deprecated.class) );    
        assertEquals( dl.firstAt(ANNO, "Deprecated").asLeftOnly().left(), _anno.of(Deprecated.class) );    
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getField("aFieldIAdded").setInit(54321);
        dl = _diff.of(_c, _c2);
        assertEquals( dl.firstOn(FIELD, "aFieldIAdded").asChange().right(), Expressions.of(1023) );
        assertEquals( dl.firstOn(FIELD, "aFieldIAdded").asChange().left(), Expressions.of(54321) );
        assertEquals( dl.firstAt(INIT).asChange().left(), Expressions.of(54321) );
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());        
        
        _c.getField("aFieldIAdded").setStatic();
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertEquals( dl.at(FIELD, "aFieldIAdded", MODIFIERS).asChange().left(), _modifiers.of("public", "static").ast() );
        //assertEquals( dl.firstOn(FIELD, "aFieldIAdded").asChange().left(), Expr.of(54321) );    
        
        //assertEquals( dl.firstAt(MODIFIERS).asChange().left(), Expr.of(54321) );    
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getField("aFieldIAdded").removeInit();
        _c2.getField("aFieldIAdded").removeInit();
        
        _c.getField("aFieldIAdded").setTypeRef(float.class);
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertEquals( dl.at(FIELD, "aFieldIAdded", TYPE).asChange().left(), _typeRef.of(float.class));
        assertEquals( dl.at(FIELD, "aFieldIAdded", TYPE).asChange().right(), _typeRef.of(int.class));        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getField("aFieldIAdded").setJavadoc("field javadoc");
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertNotNull( dl.at(FIELD, "aFieldIAdded", JAVADOC) );        
        //assertEquals( dl.firstOn(FIELD, "aFieldIAdded").asChange().left(), Expr.of(54321) );    
        
        //assertEquals( dl.firstAt(MODIFIERS).asChange().left(), Expr.of(54321) );    
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        //--------------CONSTRUCTOR ON CLASS------------------
        _c.getConstructor(0).addAnnos(Deprecated.class);
         dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(ANNO).isLeftOnly() );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getConstructor(0).addThrows("BlahException");
         dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(THROWS).isLeftOnly() );                
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getConstructor(0).addAnnos("AGGGG");
        dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(CONSTRUCTOR).isLeftOnly() );                
        assertNotNull( dl.firstOn(ANNO).isLeftOnly() );
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        


        _c.getConstructor(0).clearBody();
        dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(CONSTRUCTOR).isEdit() );                
        assertNotNull( dl.firstOn(BODY).isEdit() );
        
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getConstructor(0).setPublic();
        dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(CONSTRUCTOR).isChange() );                
        assertNotNull( dl.firstOn(MODIFIERS).isChange() );
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        
        _c.getConstructor(0).removeTypeParameters();
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertNotNull( dl.firstOn(CONSTRUCTOR).isRightOnly() );                
        assertNotNull( dl.firstOn(TYPE_PARAMETERS).isRightOnly() );
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        /*
        //this will change api
        _c.getConstructor(0).addParameter("final int lastParameter");
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertNotNull( dl.firstOn(CONSTRUCTOR).isChange() );
        assertTrue( dl.hasChange());
        assertTrue(dl.hasChangeOn(CONSTRUCTOR));
        assertTrue(dl.hasChangeAt(PARAMETERS));
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        */

        /** ----METHOD ON CLASS */
        _c.getMethod(0).addAnnos(Deprecated.class);
         dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(ANNO).isLeftOnly() );        
        dl.patchLeftToRight();        
        assertTrue(_diff.of(_c, _c2).isEmpty());


        
        _c.getMethod(0).addThrows("BlahException");
         dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(THROWS).isLeftOnly() );                
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());

        System.out.println("******** "+ _c.getMethod("doIt"));

        _c.getMethod(0).addAnnos("AGGGG");
        dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(METHOD).isLeftOnly() );                
        assertNotNull( dl.firstOn(ANNO).isLeftOnly() );
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());

        System.out.println("****>>>> "+ _c.getMethod("doIt"));
        
        _c.getMethod(0).setBody( new BlockStmt());
        System.out.println("CBCBCBCB****>>>> "+ _c.getMethod("doIt"));
        dl = _diff.of(_c, _c2);
        //System.out.println( dl);
        assertNotNull( dl.firstOn(METHOD).isEdit() );                
        assertNotNull( dl.firstOn(BODY).isEdit() );
        
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());

        System.out.println("<<<<<<1>>>>>> "+ _c.getMethod("doIt"));

        _c.getMethod(0).setPrivate();
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertNotNull( dl.firstOn(METHOD).isChange() );                
        assertNotNull( dl.firstOn(MODIFIERS).isChange() );
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());

        System.out.println("AFTER "+ _c.getMethod("doIt"));
        
        
        _c.getMethod(0).removeTypeParameters();
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertNotNull( dl.firstOn(METHOD).isRightOnly() );                
        assertNotNull( dl.firstOn(TYPE_PARAMETERS).isRightOnly() );
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());

        System.out.println("BEFORE "+ _c );

        //this will change api
        _c.getMethod(0).getParameters().astHolder().getParameters().add( 0, Ast.parameter( "final int firstParameter"));
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        assertTrue(dl.hasLeftOnly());
        assertTrue(dl.hasRightOnly());
        assertTrue(dl.hasLeftOnlyOn(METHOD));
        //assertTrue(dl.hasRightOnlyOn(PARAMETER));

        //assertTrue( dl.firstOn(METHOD).isChange() );
        //assertTrue( dl.hasChange());
        //assertTrue(dl.hasChangeOn(METHOD));
        //assertTrue(dl.hasChangeAt(PARAMETERS));
        dl.patchLeftToRight();                
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        System.out.println("SIZE" +  _c.listInnerTypes().size() );
        assertEquals(1, _c.listInnerTypes().size());
        System.out.println("SIZE" +  _c2.listInnerTypes().size() );
        assertEquals(1, _c2.listInnerTypes().size());
        
        
        //------------------------NESTED ENUM
        System.out.println( _c.listInnerTypes() );
        assertEquals(1, _c.listInnerTypes().size() );
        
        _c.getDeclared(_enum.class, "E").setJavadoc("Nested Enum Javadoc");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isChange());
        assertTrue( dl.firstOn(JAVADOC).isChange());
        assertTrue( dl.firstOn(ENUM, "E").isChange());        
        dl.patchLeftToRight();                   
        assertTrue(_diff.of(_c, _c2).isEmpty());        
        
        _c.getDeclared(_enum.class, "E").addAnnos("AFG");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isLeftOnly());
        assertTrue( dl.firstOn(ANNO).isLeftOnly());
        assertTrue( dl.firstOn(ENUM, "E").isLeftOnly());        
        dl.patchLeftToRight();                     
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getDeclared(_enum.class, "E").setPrivate();
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isChange());
        assertTrue( dl.firstOn(MODIFIERS).isChange());
        assertTrue( dl.firstOn(ENUM, "E").isChange());        
        dl.patchLeftToRight();                     
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        
        _c.getDeclared(_enum.class, "E").addImplements("IM");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isLeftOnly());
        assertTrue( dl.firstOn(IMPLEMENTS).isLeftOnly());
        assertTrue( dl.firstOn(ENUM, "E").isLeftOnly());        
        dl.patchLeftToRight();                     
        assertTrue(_diff.of(_c, _c2).isEmpty());
     
        _c.getDeclared(_enum.class, "E").addField("int aa = 11;");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isLeftOnly());
        assertTrue( dl.firstOn(FIELD).isLeftOnly());
        assertTrue( dl.firstOn(FIELD, "aa").isLeftOnly());
        assertTrue( dl.firstOn(ENUM, "E").isLeftOnly());        
        dl.patchLeftToRight();                     
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getDeclared(_enum.class, "E").addConstant("D(2)");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isLeftOnly());
        assertTrue( dl.firstOn(CONSTANT).isLeftOnly());
        assertTrue( dl.firstOn(CONSTANT, "D").isLeftOnly());
        assertTrue( dl.firstOn(ENUM, "E").isLeftOnly());        
        dl.patchLeftToRight();                    
        System.out.println(_diff.of(_c, _c2) );
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getDeclared(_enum.class, "E").addConstructor("(String s){}");
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isLeftOnly());
        assertTrue( dl.firstOn(CONSTRUCTOR).isLeftOnly());
        assertTrue( dl.firstOn(CONSTRUCTOR, "E(String)").isLeftOnly());        
        assertTrue( dl.firstOn(ENUM, "E").isLeftOnly());        
        dl.patchLeftToRight();                    
        System.out.println(_diff.of(_c, _c2) );
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        
        
        _c.getDeclared(_enum.class, "E").main(()-> System.out.println("MainMethod"));
        dl = _diff.of(_c, _c2);
        assertTrue( dl.firstOn(ENUM).isLeftOnly());
        assertTrue( dl.firstOn(METHOD).isLeftOnly());
        assertTrue( dl.firstOn(METHOD, "main(String[])").isLeftOnly());        
        dl.patchLeftToRight();                    
        System.out.println(_diff.of(_c, _c2) );
        assertTrue(_diff.of(_c, _c2).isEmpty());
        
        _c.getDeclared(_enum.class, "E").getMethod("main").addThrows(IOException.class);
        
        System.out.println( _c.getDeclared(_enum.class, "E").getMethod("main") );
        System.out.println( _c2.getDeclared(_enum.class, "E").getMethod("main") );
        dl = _diff.of(_c, _c2);
        System.out.println( dl);
        
        assertTrue( dl.firstOn(ENUM).isChange());
        assertTrue( dl.firstOn(METHOD).isChange());
        assertTrue( dl.firstOn(THROWS).isChange());
        assertTrue( dl.firstOn(METHOD, "main(String[])").isChange());     
        
        dl.patchLeftToRight();                    
        System.out.println(_diff.of(_c, _c2) );
        assertTrue(_diff.of(_c, _c2).isEmpty());        
        
        
    }
    
    public void testComplexInterface(){
        _interface _i = _interface.of(ComplexInterface.class);
        _i.addImports("java.util.*");
        System.out.println(_i );
        _interface _i2 = _i.copy();
        
        _diff dl = _diff.of(_i, _i2);
        assertTrue( dl.isEmpty());
        
        _i.addExtend("AnotherE");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(EXTENDS));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.addAnnos("Annop");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(ANNO));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.setJavadoc("The changed javadoc");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasChangeAt(JAVADOC));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.addField("int someNewField = 1234;");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(FIELD));
        assertTrue( dl.hasLeftOnlyAt(FIELD, "someNewField"));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.setPrivate();
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasChangeAt(MODIFIERS));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.addMethod("void methodM();");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(METHOD));
        assertTrue( dl.hasLeftOnlyAt(METHOD, "methodM()"));
        assertTrue( dl.isDiffOf(_i.getMethod("methodM")));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.removeTypeParameters();
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasChangeAt(TYPE_PARAMETERS));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _i.addImports("aaaa.bbbb.C");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(IMPORT));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        //--------------------------NESTED INNER CLASS
        
        _class _c = _i.getDeclared(_class.class, "C");
        _c.setStatic(false);
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasChangeAt(MODIFIERS));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.removeInitBlock(_c.getInitBlock(0));
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasRightOnlyAt(INIT_BLOCK));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.addConstructor("(){System.out.println(1);}");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(CONSTRUCTOR));        
        assertTrue( dl.hasLeftOnlyAt(CONSTRUCTOR, "C()"));
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.addExtend("ExtendEd");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(EXTENDS));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.addImplement("Impled");
        dl = _diff.of(_i, _i2);
        assertTrue( dl.hasLeftOnlyAt(IMPLEMENTS));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.addAnnos("Annoed");
        dl = _diff.of(_i, _i2);
        System.out.println( dl);
        assertTrue( dl.hasLeftOnlyAt(ANNO, "Annoed"));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.removeTypeParameters();
        dl = _diff.of(_i, _i2);
        System.out.println( dl);
        assertTrue( dl.hasChangeAt(TYPE_PARAMETERS));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        _c.addField("int addedThisField=100;");
        dl = _diff.of(_i, _i2);
        System.out.println( dl);
        assertTrue( dl.hasLeftOnlyAt(FIELD, "addedThisField"));        
        assertTrue( dl.hasLeftOnlyOn(FIELD));        
        dl.patchLeftToRight();
        assertTrue(_diff.of(_i, _i2).isEmpty());
        
        
        
    }
    
    /*    
        
        
        
        _interface _i = _interface.of(ComplexInterface.class);
        _interface _i2 = _i.copy();
        
        assertTrue(_diffList.of(_i, _i2).isEmpty() ) ;
        assertTrue(_diffList.of(_i2, _i).isEmpty() ) ;
        
        _enum _e = _enum.of(ComplexEnum.class);
        _enum _e2 = _e.copy();
        
        assertTrue(_diffList.of(_e, _e2).isEmpty() );
        assertTrue(_diffList.of(_e2, _e).isEmpty() );
        
        _annotation _a = _annotation.of(ComplexAnnotationType.class);
        _annotation _a2 = _a.copy();
        
        //System.out.println(_diffList.of(_a, _a2) );        
        //System.out.println( _diffList.of( _a.getNest("nested"), _a2.getNest("nested") ) );        
        //_diffList( _a.getNest("nested"), _a2.getNest("nested"));        
        assertTrue(_diffList.of(_a, _a2).isEmpty() );
        assertTrue(_diffList.of(_a2, _a).isEmpty() );        
    }
    */
}
