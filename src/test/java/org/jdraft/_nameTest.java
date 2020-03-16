package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.bot.$name;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class _nameTest extends TestCase {

    public void testTypeRef(){
        _name n = _name.of(_typeRef.of("T").ast().asClassOrInterfaceType().getName());
        assertTrue( n.isTypeRefName() );
    }

    public void testPackageName(){
        _package _p = _package.of("ffff.lang.dddd;");
        assertTrue(_name.of( _p.ast().getName()).isPackageName());
    }

    public void testImportName(){
        _import _i = _import.of("ffff.lang.dddd;");
        _i.getNameNode().stream(Walk.PARENTS).forEach( e-> System.out.println( e.getClass()) );

        assertTrue(_name.of( _i.getNameNode()).isImportName());
        assertTrue(_name.of( _i.getNameNode().getQualifier().get()).isImportName());
    }

    public void testIsTypeName(){
        assertTrue( _name.of( _class.of("F").getNameNode()).isTypeDeclarationName() );
        assertTrue( _name.of( _interface.of("F").getNameNode()).isTypeDeclarationName() );
        assertTrue( _name.of( _enum.of("F").getNameNode()).isTypeDeclarationName() );
        assertTrue( _name.of( _annotation.of("F").getNameNode()).isTypeDeclarationName() );
    }

    public void testMethodName(){
        _method _m = _method.of("void m(){}");
        assertTrue( _name.of(_m.getNameNode()).isMethodName() );

        _methodCall _mc = _methodCall.of("m()");
        assertTrue( _name.of(_mc.getNameNode()).isMethodName() );

    }
    public void testNameIs(){

        _name _n = _name.of( _import.of("aaaa.bbbb.C").astId.getName() );
        assertTrue(_n.isImportName());       //name of an import "aaaa" or "B" in "import aaaa.B;"
        assertFalse(_n.isMethodReference()); //name of a MethodReference "A" and "B" in "A::B"
        assertFalse(_n.isPackageName());     //name of a package "aaaa", "bbbb" in "package aaaa.bbbb;"
        assertFalse(_n.isTypeDeclarationName());        //name of a TypeDeclaration "c" in "class C{}"
        assertFalse(_n.isTypeRefName());     //name of a Type reference "Integer" in "Integer i;"

        assertFalse(_n.isVariableName());    //name of a variable "x" in "int x"
        assertFalse(_n.isParameterName());   //name of a parameter "p" in "(int p)"

        assertFalse(_n.isAnnoName());        //name of an annotation "A" in "@A()"
        assertFalse(_n.isAnnoMemberValueName()); //name of annotation member "k" in "@A(k=1)"

        assertTrue( _name.of( _import.of("aaaa.bbbb.C").ast().getName() ).isImportName());

        assertTrue( _name.of( _import.of("aaaa.bbbb.C").ast().getName().getQualifier().get() ).isImportName());

        assertTrue( _name.of( _methodReference.of("A::B").ast() ).isMethodReference());
        assertTrue( _name.of( Ast.packageDeclaration("package aaaa.bbbb").getName() ).isPackageName() );
        assertTrue( _name.of( Ast.typeDecl("class C{}").asClassOrInterfaceDeclaration().getName() ).isTypeDeclarationName() );
        assertTrue( _name.of( Ast.typeRef("C").asClassOrInterfaceType().getName() ).isTypeRefName() );

        assertTrue( _name.of( Ast.varDecl("int i").getName() ).isVariableName() );
        assertTrue( _name.of( Ast.parameter("int i").getName() ).isParameterName() );
        assertTrue( _name.of( Ast.anno("@A").getName() ).isAnnoName() );
        assertTrue( _name.of( Ast.anno("@A(k=1)").asNormalAnnotationExpr().getPairs().get(0).getName() ).isAnnoMemberValueName() );
    }

}
