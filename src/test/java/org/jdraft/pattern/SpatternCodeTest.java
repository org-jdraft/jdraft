package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.utils.Log;
import org.jdraft.*;

import java.net.URI;
import java.net.URLConnection;
import java.util.*;

import junit.framework.TestCase;
import org.jdraft.macro._static;

/**
 *
 * @author Eric
 */
public class SpatternCodeTest extends TestCase {

    public void testIsParentMemberNotParentMember(){
        _class _c = _class.of(
                "public class C{", //1
                "    public int i = 0;", //2
                "    public int x = 1;", //3
                "    public C(){ this.i = 2; }", //4
                "    public C(int i){ this.i = i; }", //5
                "    public int getU(){ return 3; }", //6
                "    public int getI(){ return i; }", //7
                "    { System.out.println(4); }",  //8
                "    { System.out.println(5); }",  //9
                "}");//10
        assertEquals( 2, $.intLiteral().$isParentMember($field.of()).countIn(_c)); //0,1
        assertEquals(4, $.intLiteral().$isNotParentMember($field.of()).countIn(_c)); //2,3,4,5

        assertEquals( 1, $.intLiteral(1).$isParentMember($field.of()).countIn(_c));
        assertEquals( 0, $.intLiteral(1).$isNotParentMember($field.of()).countIn(_c));

    }
    public void testIsParentMember(){
        _class _c = _class.of(
                "public class C{", //1
                "    public int i = 0;", //2
                "    public int x = 1;", //3
                "    public C(){ this.i = 2; }", //4
                "    public C(int i){ this.i = i; }", //5
                "    public int getU(){ return 3; }", //6
                "    public int getI(){ return i; }", //7
                "    { System.out.println(4); }",  //8
                "    { System.out.println(5); }",  //9
                "}");//10

        assertEquals( $field.of().countIn(_c), 2);


        $member[] members = new $member[]{$field.of()};
        _expression e = $.of(0).firstIn(_c);

        assertTrue( Ast.isParentMember(e.ast(), pm -> {
            System.out.println(pm+" "+ pm.getClass());
            return $field.of().match(pm);
        }));

        //assertTrue( Ast.isParentMember(node, nn-> Arrays.stream(members).filter($m ->Ast$m.match(nn)).findFirst().isPresent()) );
        assertTrue( Ast.isParentMember(e.ast(), nn-> Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent()) );

        //there are (2) int literals (0, 2) which have parents associated with fields ("int i = 0", "int x = 2")
        assertEquals(2, $.intLiteral().$isParentMember($field.of()).countIn(_c)); //i,x
        //System.out.println( "Is parent ");
        //$.intLiteral().$isParentMember($field.of()).printIn(_c);

        //there are (3) int literal (23, 1, 2) which are Not part of members that are fields (
        //System.out.println( "Is Not parent ");
        //$.intLiteral().$isNotParentMember($field.of()).printIn(_c);

        assertEquals(4, $.intLiteral().$isNotParentMember($field.of()).countIn(_c));
    }

    public void testIsInRange(){
        _class _c = _class.of(
                "public class C{", //1
                "    public int i = 0;", //2
                "    public int x = 2;", //3
                "    public C(){ this.i = 0; }", //4
                "    public C(int i){ this.i = i; }", //5
                "    public int getU(){ return 23; }", //6
                "    public int getI(){ return i; }", //7
                "    { System.out.println(1); }",  //8
                "    { System.out.println(2); }",  //9
                "}");//10
        assertEquals( 2, $constructor.of().countIn(_c) );
        assertEquals( 2, $constructor.of().$isInRange(0,0, 10,100).countIn(_c) );
        assertEquals( 2, $constructor.of().$isInRange(0, 10).countIn(_c) );

        //verify (2) fields on lines 2 and 3 (2, 3)
        assertEquals(2, $field.of().countIn(_c) );
        //1 field on line 2
        assertEquals(1, $field.of().$atLine(2).countIn(_c) );
        //1 field on line 3
        assertEquals(1, $field.of().$atLine(3).countIn(_c) );
        //field on line 2
        $field $f = $.field().$isInRange(2,0, 2,100);
        assertEquals(1, $f.countIn(_c));

        //exact (2, 4) - (2-21)
        $f = $.field().$isInRange(2,4, 2,21);
        assertEquals(1, $f.countIn(_c));

        //on single line
        $f = $.field().$isInRange(2,2);
        assertEquals(1, $f.countIn(_c));assertEquals(1, $f.countIn(_c));

    }

    public void test$ParentChildDescendant(){
        class C <A extends Map> {
            int x = 0;
            Map m;

            public @_static final UUID ID = UUID.randomUUID();

            public int getX(){
                return this.x;
            }

            C (int x){
                this.x = x;
            }
        }
        _class _c = _class.of(C.class);

        $typeRef.of().printIn(_c);

        System.out.println( $typeParameter.of().firstIn(_c) );
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        Walk.describe( $typeParameter.of().firstIn(_c) );
        Log.setAdapter(new Log.SilentAdapter());
        assertEquals( 2, $typeRef.of(Map.class).countIn(_c) );
        assertEquals( 1, $typeRef.of(Map.class).$hasAncestor($typeParameter.of()).countIn(_c) ); //a map used in a TypeParameter
        //assertEquals( 1, $typeRef.of().$hasParent($typeParameter.class).count(_c) );
        //assertEquals( 1, $typeRef.of("Map").$hasParent($typeParameter.class).count(_c) );
        assertEquals( 1, $method.of().$hasDescendant($typeRef.of(int.class)).countIn(_c)); //a method that uses an int
        assertEquals( 0, $method.of().$hasDescendant($typeRef.of(float.class)).countIn(_c)); //a method that uses an int

        $method $m = $.method( new Object(){
            int $name$;
            public int get$Name$(){
                return this.$name$;
            }
        });

        $m.$hasAncestor($.of(ClassOrInterfaceDeclaration.class));
        assertEquals(1, $m.countIn(_c));

        $m = $.method( new Object(){
            int $name$;
            public int get$Name$(){
                return this.$name$;
            }
        });
        $m.$hasAncestor($.of(EnumDeclaration.class));
        assertEquals(0, $m.countIn(_c));

        $node $n = $node.of( ClassOrInterfaceDeclaration.class).$hasDescendant( $method.of().$type(int.class) );

        assertEquals(1, $n.countIn( C.class ));

        $n = $node.of( ClassOrInterfaceDeclaration.class).$hasDescendant( $field.of( $modifiers.of("private")));

        assertEquals(0, $n.countIn( C.class ));



    }

    public void testAstTypes(){
        CompilationUnit classAst = Ast.of( 
            "package com.github.javaparser.ast;"+System.lineSeparator()+"public class A{}");
            
        CompilationUnit packageInfoAst = Ast.of(
            "/* License Erics 101 */"+ System.lineSeparator() +    
            "package com.github.javaparser.ast;"+System.lineSeparator()+ 
            "import java.util.*;"+System.lineSeparator()+         
            "/** Javadoc Comment */");
        
        CompilationUnit modAst = Ast.of(
            "/* License 101 */"    + System.lineSeparator()+
            "import java.util.Map;" + System.lineSeparator()+
            "module aaaa {" + System.lineSeparator()+
            "    requires bbbb;" + System.lineSeparator()+
            "    requires static cccc;" + System.lineSeparator()+
            "}"); 
        _codeUnit _c = (_codeUnit)_java.codeUnit(classAst);
        _codeUnit _pI = (_codeUnit)_java.codeUnit(packageInfoAst);
        _codeUnit _mI = (_codeUnit)_java.codeUnit(modAst);
        
        
        assertTrue( _c instanceof _type);
        assertTrue( _pI instanceof _packageInfo);
        assertTrue( _mI instanceof _moduleInfo);
        
        _packageInfo _pkgInfo = (_packageInfo)_pI;
        _moduleInfo _modInfo = (_moduleInfo)_mI;
        _class _cA = (_class)_c;
        
        //I can add all _code items to a list
        List<_codeUnit> cms = new ArrayList<>();
        cms.add(_pkgInfo);
        cms.add( _modInfo);
        cms.add(_cA);
        
        //add the URI class to each of these (modInfo, packageInfo) 
        cms.forEach(s -> s.addImports(URI.class));
        
        //verify that I can query ... 
        assertNotNull( $import.of(URI.class).firstIn(_modInfo) );
        assertNotNull( $import.of(URI.class).firstIn(_pkgInfo) );
        assertNotNull( $import.of(URI.class).firstIn(_cA) );
        
        
        assertNotNull( $node.of("com.github.javaparser.ast").firstIn(_pkgInfo) );
        
        //modify        
        //$import.of(URI.class)
        //    .replaceIn(_c.astCompilationUnit(), HashMap.class);
        
        //here I can modify the _code, by passing each
        cms.forEach(c-> $import.of(URI.class).replaceIn( c.astCompilationUnit(), URLConnection.class));
        
        //$import.of(URI.class).replaceIn(_pkgInfo, HashMap.class);
                
        System.out.println( $node.of("com.github.javaparser.ast").firstIn(_pkgInfo).getClass()  );
        
        cms.forEach( c -> assertTrue( c.hasImport(URLConnection.class) ));
        
    }
}
