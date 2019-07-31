package org.jdraft.adhoc;

import org.jdraft.*;
import org.jdraft.macro.*;
import junit.framework.TestCase;

public class _projectTest extends TestCase {


    /**
     * test the new call methods to make srue
     */
    public void testNewCallMethods(){
        _class _c = _class.of("aaaa.bbbb.C");
        _method _m = _method.of("public static int val(){ return 1;}");
        _c.add( _m);
        
        _adhoc _p = _adhoc.of(_c);
        
        //_p.call(fullyQualifiedClassName, methodName, args)
        //because there is only 1 public static method on _c, call that
        assertEquals( 1, _p.call( _c, "val")); 
        
        //call this specific method on 
        assertEquals( 1, _p.call( _m ));        
    }
    
    /**
     * Test Project inheritance
     * i.e. Child classLoaders can access classes in the Parent ClassLoader
     
    public void testProjectInheritance() throws ClassNotFoundException, IOException {
        _class _base = _dto.Macro.to(_class.of("aaaa.bbbb.Base").fields("int x,y,z"));
        _class _derived = _dto.Macro.to( _class.of("aaaa.bbbb.Derived").fields("float a, b,c;").extend(_base) );


        //_class _base = _autoDto.Macro.to(_class.of("abase.Base").FIELDS("int x,y,z"));

        //_class _base2 = _autoDto.Macro.to(_class.of("abase.Base", new Object(){int x,y,z;}));
        //_class _derived = _autoDto.Macro.to( _class.of("abase.Derived").FIELDS("float a, b,c;").extend("abase.Base") );

        //System.out.println( _base );
        //System.out.println( _derived );

        _project _parentP = _project.of(_base);


        //compile the base class "A" in the parent _project


        assertNotNull(_parentP.get_type("aaaa.bbbb.Base"));
        assertNotNull(_parentP.getClass("aaaa.bbbb.Base"));
        assertNotNull(_parentP.getClass(_base));
        assertNotNull(_parentP.getClassLoader().getClass("aaaa.bbbb.Base"));
        assertNotNull(_parentP.getClassLoader().get_class("aaaa.bbbb.Base"));
        assertNotNull(_parentP.getClassLoader().getClass(_base));
        assertNotNull(_parentP.getClassLoader().findClass("aaaa.bbbb.Base"));

        assertNotNull(_parentP.getClassLoader()._fileMan.getJavaFileForInput(
                StandardLocation.CLASS_PATH,
                "aaaa.bbbb.Base",
                JavaFileObject.Kind.CLASS ) );

        assertNotNull(_parentP.getClassLoader()._fileMan.getJavaFileForInput(
                StandardLocation.SOURCE_PATH,
                "aaaa.bbbb.Base",
                JavaFileObject.Kind.SOURCE ) );

        assertNotNull(_parentP.getClassLoader()._fileMan.getJavaSourceFiles().get("aaaa.bbbb.Base"));
        assertNotNull(_parentP.getClassLoader()._fileMan.getJavaSourceFiles().getFile("aaaa.bbbb.Base"));

        //System.out.println ( _parentP.listClasses() );


        // compile the derived class B in the child _project (make sure the derived class can access classes in ints
        // parent _project/_classLoader
        _project _childP = _project.of(_parentP, _derived );

        //_parent or _child projects/classLoaders can create _base class "Base"
        _parentP._new(_base);
        _childP._new(_base);

        //only child can create "Derived" classes
        _childP._new(_derived);
        try{
            _parentP._new(_derived);
            fail("expected Exception .. classNotFound");
        } catch(DraftException de){
            //expected
        }
    }
    * */

    public void testProjectProxy(){
        @_promote("aaaa.gggg")
        @_dto
        class L{
            int x=10, y=20, z=42;
            @_static public void main(String[] args){
                System.setProperty("maincalled", "true");
            }

            public int add(){
                return x + y + z;
            }

            public int add( int a){
                return a + x + y + z;
            }
        }
        _class _cc = _class.of(L.class);
        System.out.println( "AST COMP UNIT "+ _cc.astCompilationUnit().toString() +" <><><><><><><<>");
        
        assertTrue( _cc.hasMain() );
        assertTrue( _cc.getMethod("main").isMain() );
        
        System.out.println( _cc );
        _adhoc _p = _adhoc.of(_class.of( L.class));

        _proxy _pr = _p.proxy("aaaa.gggg.L");


        //set
        _pr.set("x", 1);
        _pr.set("y", 2);
        _pr.set("z", 3);
        //get
        assertEquals(1, _pr.get("x"));
        assertEquals(2, _pr.get("y"));
        assertEquals(3, _pr.get("z"));

        //call method
        assertEquals( 6, _pr.call("add"));
        assertEquals( 12, _pr.call("add", 6)); //overloaded method
        

        //call main method
        _p.main();
        assertEquals( "true", System.getProperty("maincalled"));

        _proxy _pr2 = _pr.of();
        _pr2.set("x", 1);
        _pr2.set("y", 2);
        _pr2.set("z", 3);
        assertEquals( _pr, _pr2);
        assertEquals( _pr.hashCode(), _pr2.hashCode());

        assertNotNull( _p.getClass("aaaa.gggg.L"));
    }

}
