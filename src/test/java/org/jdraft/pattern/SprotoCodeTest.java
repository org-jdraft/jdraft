package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import org.jdraft.*;

import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.TestCase;
import org.jdraft.macro._static;

/**
 *
 * @author Eric
 */
public class SprotoCodeTest extends TestCase {

    public void test$protoParentChildDescendant(){
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


        _java.describe( $typeParameter.of().firstIn(_c) );
        assertEquals( 2, $typeRef.of(Map.class).count(_c) );
        assertEquals( 1, $typeRef.of(Map.class).$hasAncestor($typeParameter.of()).count(_c) ); //a map used in a TypeParameter
        //assertEquals( 1, $typeRef.of().$hasParent($typeParameter.class).count(_c) );
        //assertEquals( 1, $typeRef.of("Map").$hasParent($typeParameter.class).count(_c) );
        assertEquals( 1, $method.of().$hasDescendant($typeRef.of(int.class)).count(_c)); //a method that uses an int
        assertEquals( 0, $method.of().$hasDescendant($typeRef.of(float.class)).count(_c)); //a method that uses an int

        $method $m = $.method( new Object(){
            int $name$;
            public int get$Name$(){
                return this.$name$;
            }
        });

        $m.$hasAncestor($.of(ClassOrInterfaceDeclaration.class));
        assertEquals(1, $m.count(_c));

        $m = $.method( new Object(){
            int $name$;
            public int get$Name$(){
                return this.$name$;
            }
        });
        $m.$hasAncestor($.of(EnumDeclaration.class));
        assertEquals(0, $m.count(_c));

        $node $n = $node.of( ClassOrInterfaceDeclaration.class).$hasDescendant( $method.of().$type(int.class) );

        assertEquals(1, $n.count( C.class ));

        $n = $node.of( ClassOrInterfaceDeclaration.class).$hasDescendant( $field.of( $modifiers.of("private")));

        assertEquals(0, $n.count( C.class ));



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
        _code _c = (_code)_java.code(classAst);
        _code _pI = (_code)_java.code(packageInfoAst);
        _code _mI = (_code)_java.code(modAst);
        
        
        assertTrue( _c instanceof _type);
        assertTrue( _pI instanceof _packageInfo);
        assertTrue( _mI instanceof _moduleInfo);
        
        _packageInfo _pkgInfo = (_packageInfo)_pI;
        _moduleInfo _modInfo = (_moduleInfo)_mI;
        _class _cA = (_class)_c;
        
        //I can add all _code items to a list
        List<_code> cms = new ArrayList<>();
        cms.add(_pkgInfo);
        cms.add( _modInfo);
        cms.add(_cA);
        
        //add the URI class to each of these (modInfo, packageInfo) 
        cms.forEach(s -> s.imports(URI.class));
        
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
