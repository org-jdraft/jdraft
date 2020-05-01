package org.jdraft.pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._annoRef;
import org.jdraft._annoRefs;

public class SpackageTest extends TestCase {

    public void testPackageDraft(){
        assertEquals( Ast.packageDeclaration("a"), $package.of("a").draft());
        assertEquals( Ast.packageDeclaration("a"), $package.of().draft("$packageName", "a")); //override parameter

        _annoRefs _as = $annoRefs.of("@ANN").draft();

        PackageDeclaration pd = $package.of("b", $annoRefs.of("@ANN"), t->true ).draft();

        PackageDeclaration pppd = Ast.packageDeclaration("package b;");
        pppd.addAnnotation("ANN");

        assertTrue( _annoRef.of("A").equals(_annoRef.of("A()")) );

        assertTrue( pd.getNameAsString().equals("b"));
        assertEquals( _annoRefs.of(pd), _annoRefs.of("@ANN"));
    }


    public void testAnyOfMatch(){
        assertTrue( $package.of().isMatchAny() );

        CompilationUnit cu = new CompilationUnit();
        assertTrue( $package.of().matches(cu) ); //works wth no packlage
        cu.setPackageDeclaration("a.b.c.d.e.f");
        assertTrue( $package.of().matches(cu) ); //works wth any package

        //add annotations to the package declaration
        cu.getPackageDeclaration().get().addAnnotation("A");
        cu.getPackageDeclaration().get().addAnnotation(Ast.anno("B(1)"));
        cu.getPackageDeclaration().get().addAnnotation(Ast.anno("C(key1=1,value=3)"));

        assertTrue( $package.of().matches(cu) ); //works wth any package with annotations

        assertTrue( $package.of().matches((PackageDeclaration)null) );
        assertTrue( $package.of().matches("package aaaa;") );
    }

    public void testMatchSelectPattern(){
        //matchConstant
        assertTrue( $package.of("aaaa").matches("package aaaa;") );
        assertTrue( $package.of("a.b.c.d").matches("package a.b.c.d;") );

        //verify that you can pass in a fully explained "package XXX;" and it'll work
        assertTrue( $package.of("package aaaa;").matches( "package aaaa;"));
    }
}
