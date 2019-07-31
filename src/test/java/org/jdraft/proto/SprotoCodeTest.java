package org.jdraft.proto;

import org.jdraft.proto.$node;
import org.jdraft.proto.$import;
import com.github.javaparser.ast.CompilationUnit;
import org.jdraft.Ast;
import org.jdraft._class;
import org.jdraft._code;
import org.jdraft._moduleInfo;
import org.jdraft._packageInfo;
import org.jdraft._java;
import org.jdraft._type;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SprotoCodeTest extends TestCase {
    
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
        _code _c = (_code)_java.of(classAst);
        _code _pI = (_code)_java.of(packageInfoAst);
        _code _mI = (_code)_java.of(modAst);
        
        
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
