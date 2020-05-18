package org.jdraft.diff;

import org.jdraft.*;
import org.jdraft.diff._namedDiff._changeName;
import org.jdraft.diff._typeRefDiff._change_type;
import org.jdraft.diff._diff._diffList;
import org.jdraft._java.Feature;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import test.ComplexAnnotationType;
import test.ComplexClass;
import test.ComplexEnum;
import test.ComplexInterface;

/**
 *
 * @author Eric
 */
public class _diffTest extends TestCase {
    
    public void testDiffComplexTypes(){
        _class _c = _class.of(ComplexClass.class);
        _class _c2 = _c.copy();
        assertTrue(_diff.of(_c, _c2).isEmpty() ) ;
        assertTrue(_diff.of(_c2, _c).isEmpty() ) ;
        
        _interface _i = _interface.of(ComplexInterface.class);
        _interface _i2 = _i.copy();
        
        assertTrue(_diff.of(_i, _i2).isEmpty() ) ;
        assertTrue(_diff.of(_i2, _i).isEmpty() ) ;
        
        _enum _e = _enum.of(ComplexEnum.class);
        _enum _e2 = _e.copy();
        
        assertTrue(_diff.of(_e, _e2).isEmpty() );
        assertTrue(_diff.of(_e2, _e).isEmpty() );
        
        _annotation _a = _annotation.of(ComplexAnnotationType.class);
        _annotation _a2 = _a.copy();
        
        //System.out.println(_diffList.of(_a, _a2) );        
        //System.out.println( _diffList.of( _a.getNest("nested"), _a2.getNest("nested") ) );        
        //assertEquals( _a.getNest("nested"), _a2.getNest("nested"));        
        assertTrue(_diff.of(_a, _a2).isEmpty() );
        assertTrue(_diff.of(_a2, _a).isEmpty() );        
    }
    
    /** 
     * verify (2) classes are equal no diffs, then add all component
     * types to another
     */
    public void testFullDiffClass(){
        _class _c1 = _class.of("C");
        _class _c2 = _class.of("C");
        
        _nodePath path = new _nodePath();
        _diffList dt = new _diffList(_c1, _c2);
        _classDiff.INSTANCE.diff(path, dt, _c1, _c2, _c1, _c2);
        
        assertTrue( dt.isEmpty() );
        _c1.addBodyMembers(new Serializable(){
            int x,y;
            Map m() throws IOException {
                Map m = new HashMap();
                m.put("1", 0);
                return m;
            }                      
        });
        _c1.setPackage("aaaa.bbbb");
        _c1.setName("B");
        _c1.addAnnoExprs(Deprecated.class);
        _c1.addExtend("G");
        _c1.addConstructor("public C(){System.out.println(1);}");
        _c1.typeParams("<T extends base>");
        _c1.setJavadoc("some javadoc");
        _c1.addInitBlock(()-> System.out.println("Static block"));
        _c1.addInner(_interface.of("I") );
        _class _c3 = _c1.copy();
        //System.out.println( _c1 );
        
        _classDiff.INSTANCE.diff(path, dt, _c1, _c2, _c1, _c2);
        
        assertEquals(16, dt.size() );
        //System.out.println("BEFORE " + _c2 );
        dt.patchRightToLeft();
        //System.out.println("AFTER C2"+ _c2 );        
        //System.out.println("AFTER C1"+ _c1 );
        //assertEquals( _c1, _class.of("C"));
        assertEquals( _c2, _class.of("C"));
        
        dt.patchLeftToRight(); //now 
        assertEquals( _c2, _c3);
        assertEquals( _c1, _c3);
        
        dt.diffNodeList.clear();
        
        
        _classDiff.INSTANCE.diff(path, dt, _c2, _c1, _c2, _c1);
        System.out.println( dt );
        
        //
        
        //everything should be upgraded
        /*
        dt.keepRight();        
        _mydiff dd = new _mydiff();
        _class.INSPECT_CLASS.diff(path, dt, _c2, _c1, _c2, _c1);
        assertTrue( dd.isEmpty() );
        
        dt.keepLeft();
        
        _class.INSPECT_CLASS.diff(path, dt, _c2, _c1, _c2, _c1);
        assertTrue( dd.isEmpty() );
        
        System.out.println( _c2 );
        */
        
    }
    
    public void test_annotationDiff(){
        _annotation _a1 = _annotation.of("A");
        _annotation _a2 = _annotation.of("A");
        
        _nodePath path = new _nodePath();
        _diffList dt = new _diffList(_a1, _a2);
        _annotationDiff.INSTANCE.diff(path, dt, null, null, _a1, _a2);
        assertTrue( dt.isEmpty());
        
        _a1.setTargetType();
        _a1.addEntry("int a() default 1;");
        _a1.addImports(IOException.class);
        _a1.addField("public static final int ID = 1;");
        _a1.setJavadoc("javadoc");
        _a1.setPackage("dev.diff");
        _a1.setProtected();
        
        _annotationDiff.INSTANCE.diff(path, dt, _a1, _a2, _a1, _a2);
        System.out.println( dt );        
    }
    
    
    public void test_enumConstantListDiff(){
        _constant _a1 = _constant.of("A");
        _constant _b1 = _constant.of("B");
        
        _constant _a2 = _constant.of("A");
        _constant _b2 = _constant.of("B");
        
        _enum _e1 = _enum.of("E");
        _enum _e2 = _enum.of("E");
        
        _e1.addConstant(_a1).addConstant(_b1);
        _e2.addConstant(_b2).addConstant(_a2);
        _nodePath path = new _nodePath();
        _diffList dt = new _diffList(_e1, _e2);
        assertEquals( _e1, _e2);
        _enumDiff.ENUM_CONSTANTS_DIFF.diff(path, dt, _e1, _e2, _e1.listConstants(), _e2.listConstants());
        assertTrue( dt.isEmpty() );
        System.out.println( dt );
        
        _e1.addConstant("C");
        _enumDiff.ENUM_CONSTANTS_DIFF.diff(path, dt, _e1, _e2, _e1.listConstants(), _e2.listConstants());
        
        assertEquals( 1, dt.listLeftOnlys().size() );
        System.out.println( dt);
        
        dt.diffNodeList.clear();        
        _enumDiff.ENUM_CONSTANTS_DIFF.diff(path, dt, _e2, _e1, _e2.listConstants(), _e1.listConstants());        
        assertEquals( 1, dt.listRightOnlys().size() );
        
        
        _e2.addConstant("C");
        _a1.addArg(true);
        dt.diffNodeList.clear();        
        _enumDiff.ENUM_CONSTANTS_DIFF.diff(path, dt, _e2, _e1, _e2.listConstants(), _e1.listConstants());
        
        assertEquals( 1, dt.listChanges().size() );
        System.out.println( dt);        
        //assertTrue(dt.isEmpty());
    }
    
    public void test_enumConstantDiff(){
        _constant _a1 = _constant.of("A");
        _constant _a2 = _constant.of("A");
        _diffList dt = new _diffList(_a1, _a2);
        _enum _e1 = _enum.of("E");
        _enum _e2 = _enum.of("E");
        _e1.addConstant(_a1);
        _e2.addConstant(_a2);
        
        _java._multiPart leftRoot = _e1;
        _java._multiPart rightRoot = _e2;
        _nodePath path = new _nodePath().in(Feature.ENUM, "E");
        _enumDiff.ENUM_CONSTANT_DIFF.diff(path, dt, leftRoot, rightRoot, _a1, _a2);
        System.out.println( dt );
        
        _a1.addMethod("int m(){ return 1; }");
        _a1.addField("int i=100;");
        _a1.addArg(0);
        _a1.addAnnoExprs(Deprecated.class);
        _a1.setJavadoc("Javadoc ");
        
        _enumDiff.ENUM_CONSTANT_DIFF.diff(path, dt, leftRoot, rightRoot, _a1, _a2);
        System.out.println( dt );
        
        dt.forEach( d-> d.patchLeftToRight() );
        
        dt = new _diffList(leftRoot, rightRoot);
        _enumDiff.ENUM_CONSTANT_DIFF.diff(path, dt, leftRoot, rightRoot, _a1, _a2);
        
        //System.out.println( dt );
        assertEquals( _a1, _a2);        
        //_method.INSPECT_METHOD.diff(new _path(), dt, leftRoot, rightRoot, _m1, _m2);
    }
    
    public void test_methodDiff(){
        _method _m1 = _method.of("int m(){ return 1; }");
        _method _m2 = _method.of("float n(){ return 1.0f; }");
        
        _diffList dt = new _diffList(_m1, _m2);
        
        _java._multiPart leftRoot = null;
        _java._multiPart rightRoot = null;
        _methodDiff.INSTANCE.diff(new _nodePath(), dt, leftRoot, rightRoot, _m1, _m2);
        
        dt = new _diffList(leftRoot, rightRoot);
        _m1.setJavadoc("Hello");
        _methodDiff.INSTANCE.diff(new _nodePath(), dt, leftRoot, rightRoot, _m1, _m2);
        
        dt = new _diffList(leftRoot, rightRoot);
        _m1.typeParams("<T extends A>");
        _m1.receiverParam("rp this");
        _m1.addParam("int i");
        _m1.addThrows(IOException.class);
        _methodDiff.INSTANCE.diff(new _nodePath(), dt, leftRoot, rightRoot, _m1, _m2);
        
        System.out.println( dt );
        
        assertFalse( _m1.equals(_m2));
        
        //ok manually do this
        //_m2.setBody("return 1;");
        dt.forEach(c-> c.patchLeftToRight() );        
        assertEquals(_m1, _m2);
        
        
        /*_change_type _ct = new _change_type(_path.of(Component.CLASS, "C"), _m1, _m2 );
        
        _ct.keepRight();
        assertEquals( _typeRef.of(float.class), _m1.getType() );
        _ct.keepLeft();
        assertEquals( _typeRef.of(int.class), _m1.getType() );       
        
        _changeName _cn = new _changeName(_path.of(Component.CLASS, "C"), _m1, _m2 );
        
        _cn.keepRight();
        assertEquals( "n", _m1.getName() );
        _cn.keepLeft();
        assertEquals( "m", _m1.getName() );               
        */
    }
    
    public void testDirectChangeInstance(){
        _method _m1 = _method.of("int m(){ return 1; }");
        _method _m2 = _method.of("float n(){ return 1.0f; }");
        
        _change_type _ct = new _change_type(_nodePath.of(Feature.CLASS, "C"), _m1, _m2 );
        
        _ct.patchRightToLeft();
        assertEquals( _typeRef.of(float.class), _m1.getTypeRef() );
        _ct.patchLeftToRight();
        assertEquals( _typeRef.of(int.class), _m1.getTypeRef() );
        
        _changeName _cn = new _changeName(_nodePath.of(Feature.CLASS, "C"), _m1, _m2 );
        
        _cn.patchRightToLeft();
        assertEquals( "n", _m1.getName() );
        _cn.patchLeftToRight();
        assertEquals( "m", _m1.getName() );               
    }    
    
}

