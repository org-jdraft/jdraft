package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalScope;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

/**
 * Unify things that can use fully qualified names
 * i.e. method calls
 * "java.lang.System.out.println()" or "System.out.println()" method Calls
 * field accesses
 * "System.out" or "Integer.MAX_VALUE"
 * type
 *
 */
public final class _qualifiedName implements _java._node<Node, _qualifiedName> {

    public String getNameString(){
        String firstPart = "";
        if( this.name instanceof NodeWithOptionalScope ){
            NodeWithOptionalScope nwos = (NodeWithOptionalScope)this.name;
            if( nwos.getScope().isPresent() && nwos.getScope().get() instanceof NameExpr){
                firstPart = nwos.getScope().get().toString();
            } else{
                firstPart = "";
            }
        }
        if( this.name instanceof NodeWithName ){
            NodeWithName nwn = (NodeWithName)this.name;
            if( firstPart.length() > 0 ){
                return firstPart + "." + nwn.getNameAsString();
            }
            return nwn.getNameAsString();
        }
        if( this.name instanceof NodeWithSimpleName){
            NodeWithSimpleName nwn = (NodeWithSimpleName)this.name;
            if( firstPart.length() > 0 ){
                return firstPart + "." + nwn.getNameAsString();
            }
            return nwn.getNameAsString();
        }
        throw new _jdraftException("Can't handle node "+this.name.getClass()+" of:"+System.lineSeparator() + this.name);
    }
    //nodeWithScope + nodeWithSimpleName

    public static _qualifiedName of( Node n){
        //java.lang.System.out.println()
        //System.err;
        //Integer.MAX_VALUE;
        if( n instanceof Name){ //could be the scope of a method
            return of( (Name)n);
        }
        if( n instanceof ClassOrInterfaceType){
            return of( (ClassOrInterfaceType)n );
        }
        if( n instanceof PackageDeclaration){
            return of( (PackageDeclaration) n );
        }
        if( n instanceof MethodCallExpr){
            return of( (MethodCallExpr) n);
        }
        if( n instanceof FieldAccessExpr ){
            return of( (FieldAccessExpr)n);
        }
        throw new _jdraftException("cannot create _name of Node type "+ n.getClass());
    }

    public static _qualifiedName of( MethodCallExpr mce){
        return new _qualifiedName(mce);
    }

    public static _qualifiedName of( FieldAccessExpr fae){
        return new _qualifiedName(fae);
    }

    public static _qualifiedName of( ClassOrInterfaceType coit){
        return new _qualifiedName(coit);
    }

    public static _qualifiedName of( PackageDeclaration pd){
        return new _qualifiedName(pd);
    }

    public static _qualifiedName of( Name name){
        return new _qualifiedName(name);
    }

    public static _qualifiedName of( MethodReferenceExpr simpleName){
        return new _qualifiedName(simpleName);
    }

    public static _qualifiedName of(String...name){

        String str = Text.combine(name);

        if( str.contains("::")){
            return of( Expr.methodReferenceExpr(str));
        }
        return of( new Name(str) );
    }

    public _qualifiedName(Node sn){
        this.name = sn;
    }

    public static Function<String, _qualifiedName> PARSER = (s)->_qualifiedName.of(s);

    public _feature._features<_qualifiedName> FEATURES = _feature._features.of(_qualifiedName.class, PARSER );

    public _feature._features<_qualifiedName> features(){
        return FEATURES;
    }

    /** the underlying name */
    public Node name;

    @Override
    public _qualifiedName copy() {
        return _qualifiedName.of( name.clone());
    }

    @Override
    public Node ast() {
        return name;
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( Text.combine(stringRep), getNameString());
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
                &&
                (name.getParentNode().get() instanceof MethodDeclaration
                        || name.getParentNode().get() instanceof MethodCallExpr);
    }

    public boolean isConstructorName(){
        return name.getParentNode().isPresent()
                &&
                (name.getParentNode().get() instanceof ConstructorDeclaration
                        || name.getParentNode().get() instanceof ObjectCreationExpr); //new
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
        return name instanceof ClassOrInterfaceType || name.getParentNode().isPresent()
                && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof ClassOrInterfaceType);
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
