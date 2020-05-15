package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * a name (in _jdraft parlance)
 * tries to unify the many different "ways" names (or identifiers) are modelled
 * and represented in JavaParser..
 *
 * for example, there are typically (3) distinct Node types that are covered under this umbrella
 * <UL>
 *     <LI>{@link Name}</LI>
 *     <LI>{@link SimpleName}</LI>
 *     <LI>{@link MethodReferenceExpr}</LI>
 * </UL>
 *
 */
public final class _name implements _java._uniPart<Node, _name> {

    public static _name of(){
        return of( new Name() );
    }

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
        if( n instanceof ClassOrInterfaceType){
            return of( (ClassOrInterfaceType) n);
        }
        throw new _jdraftException("cannot create _name of Node type "+ n.getClass());
    }

    public static _name of( ClassOrInterfaceType coit){
        return new _name(coit);
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
        if( name.length == 0 ){
            return of();
        }
        String str = Text.combine(name);
        if( !str.contains(".") && !str.contains("::") ){
            //MUST be a name
            return of( new SimpleName(str) );
        }
        if( str.contains("::")){
            return of( Exprs.methodReferenceEx(str));
        }
        return of( new Name(str) );

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

    public Set<Use> getUse(){
        return Use.of(this.name);
    }

    public boolean isLabelName(){
        return Use.BREAK_LABEL_NAME.is(this.name) || Use.LABELED_STATEMENT_LABEL_NAME.is(this.name) || Use.CONTINUE_LABEL_NAME.is(this.name);
    }

    public boolean isBreakLabelName(){
        return Use.BREAK_LABEL_NAME.is(this.name);
    }

    public boolean isContinueLabelName(){
        return Use.CONTINUE_LABEL_NAME.is(this.name);
    }

    public boolean isLabelStatementLabelName(){
        return Use.LABELED_STATEMENT_LABEL_NAME.is(this.name);
    }

    public boolean isAnnoName(){
        return Use.ANNO_NAME.is(this.name);
    }

    public boolean isAnnoMemberValueName(){
        return Use.ANNO_MEMBER_VALUE_NAME.is(this.name);
    }

    /** Is this "name" type one that is being used in the context of a package name? */
    public boolean isPackageName(){
        return Use.PACKAGE_NAME.is(this.name);
    }

    /** */
    public boolean isMethodName(){
        return Use.METHOD_NAME.is(this.name);
    }

    /** */
    public boolean isConstructorName(){
        return Use.CONSTRUCTOR_NAME.is(this.name);
    }

    /** Is this "name" type one that is being used in the context of a variable name? i.e. "i" within "int i" */
    public boolean isVariableName(){
        return Use.VARIABLE_NAME.is(this.name);
    }

    /** Is this "name" type one that is being used in a parameter i.e. "p" within "(int p)" */
    public boolean isParameterName(){
        return Use.PARAMETER_NAME.is(this.name);
    }

    /** is this name used as a Reference to a Type i.e. "int" within "int i;" */
    public boolean isTypeParameterName(){
        return Use.TYPE_PARAMETER_NAME.is(this.name);
    }

    /** is this name used as a Reference to a Type i.e. "int" within "int i;" */
    public boolean isTypeRefName(){
        return Use.TYPE_REF_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole _type/TypeDeclaration name i.e. "C" within "class C{}" */
    public boolean isTypeName(){
        return Use.CLASS_DECLARATION_NAME.is(this.name) || Use.INTERFACE_DECLARATION_NAME.is(this.name) || Use.ENUM_DECLARATION_NAME.is(this.name) || Use.ANNOTATION_DECLARATION_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole _class/ClassOrInterfaceDeclaration name i.e. "C" within "class C{}" */
    public boolean isClassName(){
        return Use.CLASS_DECLARATION_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole _interface/ClassOrInterfaceDeclaration name i.e. "I" within "interface I{}" */
    public boolean isInterfaceName(){
        return Use.INTERFACE_DECLARATION_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole _enum/EnumDeclaration name i.e. "E" within "enum E{ ; }" */
    public boolean isEnumName(){
        return Use.ENUM_DECLARATION_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole _annotation/AnnotationDeclaration name i.e. "A" within "@interface A{}" */
    public boolean isAnnotationName(){
        return Use.ANNOTATION_DECLARATION_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole _annotation._element/AnnotationMemberDeclaration name */
    public boolean isAnnotationElementName(){
        return Use.ANNOTATION_ELEMENT_NAME.is(this.name);
    }

    /** Is the name being used as an Enum Constant (i.e. "CLUBS" in "enum Suit{ CLUBS, HEARTS, DIAMONDS, SPADES; }" */
    public boolean isEnumConstantName(){
        return Use.ENUM_CONSTANT_NAME.is(this.name);
    }


    /** Is this name a "part" of a MethodReference? */
    public boolean isMethodReference(){
        return Use.METHOD_REFERENCE_NAME.is(this.name);
    }

    /** Is this name being used as "part" or a whole import name? */
    public boolean isImportName(){
        return Use.IMPORT_NAME.is(this.name);
    }

    /**
     * the context around which the given NAME has been used
     */
    public enum Use {
        ANNO_NAME( name -> name.getParentNode().isPresent() && name.getParentNode().get() instanceof AnnotationExpr),
        ANNO_MEMBER_VALUE_NAME(name -> name.getParentNode().isPresent() && name.getParentNode().get() instanceof MemberValuePair),
        ANNOTATION_DECLARATION_NAME(name ->  name.getParentNode().isPresent() && name.getParentNode().get() instanceof AnnotationDeclaration),
        ANNOTATION_ELEMENT_NAME( name -> name.getParentNode().isPresent() && name.getParentNode().get() instanceof AnnotationMemberDeclaration),
        CLASS_DECLARATION_NAME(name -> name instanceof SimpleName && name.getParentNode().isPresent() &&
                name.getParentNode().get() instanceof ClassOrInterfaceDeclaration &&
                !((ClassOrInterfaceDeclaration) name.getParentNode().get()).asClassOrInterfaceDeclaration().isInterface()),
        CONSTRUCTOR_NAME( name -> name instanceof SimpleName && name.getParentNode().isPresent()
                && (name.getParentNode().get() instanceof ConstructorDeclaration
                        || name.getParentNode().get() instanceof ObjectCreationExpr)),
        ENUM_DECLARATION_NAME(name -> name.getParentNode().isPresent()
                && (name.getParentNode().get() instanceof EnumDeclaration)),
        ENUM_CONSTANT_NAME(name -> name.getParentNode().isPresent()
                && (name.getParentNode().get() instanceof EnumConstantDeclaration)),
        INTERFACE_DECLARATION_NAME(name -> name.getParentNode().isPresent() &&
                name.getParentNode().get() instanceof ClassOrInterfaceDeclaration &&
                ((ClassOrInterfaceDeclaration) name.getParentNode().get()).asClassOrInterfaceDeclaration().isInterface()),
        IMPORT_NAME(name -> name instanceof ImportDeclaration || name.getParentNode().isPresent() && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof ImportDeclaration)),
        BREAK_LABEL_NAME(name->name.getParentNode().isPresent() && name.getParentNode().get() instanceof BreakStmt),
        CONTINUE_LABEL_NAME(name-> name.getParentNode().isPresent() && name.getParentNode().get() instanceof ContinueStmt),
        LABELED_STATEMENT_LABEL_NAME(name-> name.getParentNode().isPresent() && name.getParentNode().get() instanceof LabeledStmt),
        METHOD_NAME(name-> name.getParentNode().isPresent() && (name.getParentNode().get() instanceof MethodCallExpr || name.getParentNode().get() instanceof MethodDeclaration)),
        METHOD_REFERENCE_NAME(name -> name instanceof MethodReferenceExpr || name.getParentNode().isPresent() && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof MethodReferenceExpr)),

        PACKAGE_NAME(name-> name.getParentNode().isPresent() && name.getParentNode().get() instanceof PackageDeclaration),
        PARAMETER_NAME(name-> name instanceof SimpleName && name.getParentNode().isPresent() && name.getParentNode().get() instanceof Parameter), //instanceof SimpleName?
        TYPE_PARAMETER_NAME(name->name instanceof TypeParameter),
        TYPE_REF_NAME(name->name instanceof Type && (!( name instanceof TypeParameter)) || (name.getParentNode().isPresent()
                && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof Type))),
        VARIABLE_NAME(name-> name instanceof SimpleName && name.getParentNode().isPresent() && name.getParentNode().get() instanceof VariableDeclarator);

        final Predicate<Node> useFn;

        Use(Predicate<Node> useFn){
            this.useFn = useFn;
        }

        public boolean is(_java._node _node){
            return is(_node.ast());
        }

        public boolean is(Node name){
            return useFn.test(name);
        }

        public static Set<Use> of(Node n){
            Set<Use> uses = new HashSet<>();
            Arrays.stream(Use.values()).forEach(u -> {
                if(u.is(n)){
                    uses.add(u);
                }
            });
            return uses;
        }

        public static Set<Use> all(){
            Set<Use> useSet = new HashSet<>();
            Arrays.stream(Use.values()).forEach( e -> useSet.add(e));
            return useSet;
        }

        public static Set<Use> allExcept(Use...excludedUse){
            Set<Use> useSet = all();
            Arrays.stream(excludedUse).forEach(e-> useSet.remove(e));
            return useSet;
        }

        public static Set<Use> only(Use... use){
            Set<Use> useSet = new HashSet<>();
            Arrays.stream(use).forEach( e -> useSet.add(e));
            return useSet;
        }

        public String toString(){
            return this.name();
        }
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
