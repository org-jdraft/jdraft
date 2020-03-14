package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import org.jdraft.bot.$name;
import org.jdraft.text.Text;

public class _name implements _java._uniPart<Node, _name> {

    public static _name of( Node n){
        if( n instanceof Name ){
            return of( (Name)n);
        }
        if( n instanceof SimpleName){
            return of( (SimpleName)n );
        }
        if( n instanceof MethodReferenceExpr){
            return of( (MethodReferenceExpr) n );
        }
        throw new _jdraftException("cannot create _name of Node type "+ n.getClass());
    }

    public static _name of( Name name){
        return new _name(name);
    }

    public static _name of( SimpleName simpleName){
        return new _name(simpleName);
    }

    public static _name of( MethodReferenceExpr simpleName){
        return new _name(simpleName);
    }

    public static _name of(String...name){
        String str = Text.combine(name);
        if( !str.contains(".")){
            //MUST be a name
            return of( new SimpleName(str) );
        } else{
            return of( new Name(str) );
        }
        //return new _name( new SimpleName( Text.combine(name)) );
    }

    public _name(Node sn){
        this.name = sn;
    }

    /** the underlying name */
    public Node name;

    @Override
    public _name copy() {
        return _name.of( name.clone());
    }

    @Override
    public Node ast() {
        return name;
    }

    @Override
    public boolean is(String... stringRep) {
        return of( Text.combine(stringRep) ).equals(this);
    }

    public boolean isAnnoName(){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof AnnotationExpr;
    }

    public boolean isAnnoMemberValueName(){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof MemberValuePair;
    }

    /** Is this "name" type one that is being used in the context of a package name? */
    public boolean isPackageName(){
        return name.getParentNode().isPresent() && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof PackageDeclaration);
    }

    public boolean isMethodName(){
        return name.getParentNode().isPresent()
                && name.getParentNode().get() instanceof MethodDeclaration
                && name.getParentNode().get() instanceof MethodCallExpr;
    }

    public boolean isConstructorName(){
        return name.getParentNode().isPresent()
                && name.getParentNode().get() instanceof ConstructorDeclaration
                && name.getParentNode().get() instanceof ObjectCreationExpr; //new
    }

    /** Is this "name" type one that is being used in the context of a variable name? */
    public boolean isVariableName(){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof VariableDeclarator;
    }

    /** Is this "name" type one that is being used in a parameter */
    public boolean isParameterName(){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof Parameter;
    }

    public boolean isTypeRefName(){
        return name.getParentNode().isPresent()  && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof ClassOrInterfaceType);
    }

    /** Is this name being used as "part" or a whole type name*/
    public boolean isTypeDeclarationName(){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof TypeDeclaration;
    }

    /** Is this name a "part" of a MethodReference? */
    public boolean isMethodReference(){
        return name instanceof MethodReferenceExpr; //name.getParentNode().isPresent() && name.getParentNode().get() instanceof MethodReferenceExpr;
    }

    /** Is this name being used as "part" or a whole import name? */
    public boolean isImportName(){
        return name.getParentNode().isPresent() && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof ImportDeclaration);
    }

    public String toString(){
        return this.name.toString();
    }

    public int hashCode(){
        return 31 * this.name.hashCode();
    }

    public boolean equals( Object o){
        if( o instanceof _name ){
            return this.name.toString().equals( o.toString());
        }
        return false;
    }
}
