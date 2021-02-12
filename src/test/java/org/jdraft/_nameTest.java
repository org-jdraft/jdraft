package org./*comment*/jdraft;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.Name;
import junit.framework.TestCase;
import org.jdraft.walk.Walk;

import java.util.Set;

public class _nameTest extends TestCase {

    /*
    public void testTypeRef(){
        _name n = _name.of(_typeRef.of("T").ast().asClassOrInterfaceType().getName());
        assertTrue( n.isTypeRefName() );
    }
     */

    /**
     * This illustrates the issue where comments are mangled due to how they are stored (separately, outside the nodelist)
     * when you get them from the parent structures (i.e. here we get a Name from a PackageDeclaration)... INSIDE
     * the name, we have a comment, and this comment location is lost relative to the other Name nodes that are in
     * the
     */
    public void testBrokenOrder(){
        PackageDeclaration pd = StaticJavaParser.parsePackageDeclaration("package aaaa./*comment*/bbbb.cccc;");
        Name nm = pd.getName();
        Name quali = nm.getQualifier().get();
        //THIS SHOULD BE TRUE, but it's NOT, because (internally the order of the nodes*/
        //assertEquals( "aaaa./*comment*/bbb", quali.toString().trim() );
        assertEquals("aaaa.bbbb/*comment*/", quali.toString().trim());

        // instead, the interior comment is moved, because, when traversing the list of nodes in a NodeList, it doesnt
        // check at each node, whether a comment node is between "node" A and "node" B (comments are not stored in the
        // NodeList when you get the child nodes

    }

    public void testWhenThingsGetHard(){
        _name _nm = _name.of("System.  out  .  println");
        System.out.println( _nm );
        //System.out.println(  _nm.getUse() );
        System.  out  .  println();
        System./*comment*/  out  /*comment*/ . /*comment*/  println();
        _nm = _name.of("System. /*comment*/  out  /*comment*/.  /*comment*/ println");
        System.out.println( _nm.node.getClass());

        System.out.println( _nm );
        System.out.println( _nm.node );
        System.out.println( _nm.node.getChildNodes() );
        Name nm = (Name)_nm.node;
        System.out.println( nm.getIdentifier() );
        System.out.println( nm.getId() );
        //NoSuchElement System.out.println( nm.getQualifier().get() );

        _nm.node.getChildNodes().stream().forEach( n -> System.out.println( n.getClass()));

        assertTrue( Ast.JAVAPARSER.parsePackageDeclaration("package aaaa  . bbb;").isSuccessful() );
        ParseResult<PackageDeclaration> ppd = Ast.JAVAPARSER.parsePackageDeclaration("package aaaa /*comment*/ . bbb;");
        assertTrue(ppd.isSuccessful());


    }
    public void testPackageName(){
        _package _p = _package.of("ffff.lang.dddd;");
        assertTrue(_name.of( _p.node().getName()).isPackageName());
    }

    public void testImportName(){
        _import _i = _import.of("ffff.lang.dddd;");
        _i.getNameNode().stream(Walk.PARENTS).forEach(e-> System.out.println( e.getClass()) );

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

        _methodCallExpr _mc = _methodCallExpr.of("m()");
        assertTrue( _name.of(_mc.getNameNode()).isMethodName() );
    }

    public void testNameIs(){

        _name _n = _name.of( _import.of("aaaa.bbbb.C").node.getName() );
        Set<_name.Use> use = _n.getUse();
        assertTrue(use.contains(_name.Use.IMPORT_NAME));
        assertTrue(use.size() == 1);
        assertTrue(_n.isImportName());       //name of an import "aaaa" or "B" in "import aaaa.B;"
        assertFalse(_n.isLabelName());
        assertFalse(_n.isBreakLabelName());
        assertFalse(_n.isContinueLabelName());
        assertFalse(_n.isLabelStatementLabelName());
        assertFalse(_n.isClassDeclarationName());
        assertFalse(_n.isEnumDeclarationName());
        assertFalse(_n.isInterfaceDeclarationName());
        assertFalse(_n.isAnnotationDeclarationName());

        assertFalse(_n.isAnnotationEntryName());
        assertFalse(_n.isEnumConstantName());

        assertFalse(_n.isMethodReferenceName()); //name of a MethodReference "A" and "B" in "A::B"
        assertFalse(_n.isPackageName());     //name of a package "aaaa", "bbbb" in "package aaaa.bbbb;"
        assertFalse(_n.isTypeDeclarationName());        //name of a TypeDeclaration "c" in "class C{}"
        //assertFalse(_n.isTypeRefName());     //name of a Type reference "Integer" in "Integer i;"

        assertFalse(_n.isVariableName());    //name of a variable "x" in "int x"
        assertFalse(_n.isParamName());   //name of a parameter "p" in "(int p)"

        assertFalse(_n.isAnnoExprName());        //name of an annotation "A" in "@A()"
        assertFalse(_n.isAnnoEntryPairName()); //name of annotation member "k" in "@A(k=1)"

        assertTrue( _name.of( _import.of("aaaa.bbbb.C").node().getName() ).isImportName());

        assertTrue( _name.of( _import.of("aaaa.bbbb.C").node().getName().getQualifier().get() ).isImportName());

        assertTrue( _name.of( _methodRefExpr.of("A::B").node() ).isMethodReferenceName());
        assertTrue( _name.of( Ast.packageDeclaration("package aaaa.bbbb").getName() ).isPackageName() );
        assertTrue( _name.of( Ast.typeDeclaration("class C{}").asClassOrInterfaceDeclaration().getName() ).isTypeDeclarationName() );
        //assertTrue( _name.of( Types.of("C").asClassOrInterfaceType().getName() ).isTypeRefName() );

        assertTrue( _name.of( Ast.variableDeclarator("int i").getName() ).isVariableName() );
        assertTrue( _name.of( Ast.parameter("int i").getName() ).isParamName() );
        assertTrue( _name.of( Ast.annotationExpr("@A").getName() ).isAnnoExprName() );
        assertTrue( _name.of( Ast.annotationExpr("@A(k=1)").asNormalAnnotationExpr().getPairs().get(0).getName() ).isAnnoEntryPairName() );
    }

}
