package org.jdraft;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Modifiers {
    ;

    private Modifiers(){}

    /* FOR MODIFIERS translate between a String, an int (used in the runtime modifiers bitmask) and an Ast Enum */
    public static final Map<String, Integer> MODS_KEYWORD_TO_BIT_MAP = new HashMap<>();
    public static final Map<Integer, String> MODS_BIT_TO_KEYWORD_MAP = new HashMap<>();
    public static final Map<Integer, com.github.javaparser.ast.Modifier> MODS_BIT_TO_ENUM_MAP = new HashMap<>();
    public static final Map<String, com.github.javaparser.ast.Modifier> MODS_KEYWORD_TO_ENUM_MAP = new HashMap<>();
    public static final Map<com.github.javaparser.ast.Modifier, String> MODS_ENUM_TO_KEYWORD_MAP = new HashMap<>();

    static {
        MODS_KEYWORD_TO_BIT_MAP.put( "public", java.lang.reflect.Modifier.PUBLIC );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PUBLIC, "public" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PUBLIC, com.github.javaparser.ast.Modifier.publicModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.publicModifier(), "public" );
        MODS_KEYWORD_TO_ENUM_MAP.put( "public", com.github.javaparser.ast.Modifier.publicModifier() );

        MODS_KEYWORD_TO_BIT_MAP.put( "protected", java.lang.reflect.Modifier.PROTECTED );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PROTECTED, "protected" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PROTECTED, com.github.javaparser.ast.Modifier.protectedModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.protectedModifier(), "protected" );
        MODS_KEYWORD_TO_ENUM_MAP.put( "protected", com.github.javaparser.ast.Modifier.protectedModifier() );

        MODS_KEYWORD_TO_BIT_MAP.put( "private", java.lang.reflect.Modifier.PRIVATE );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PRIVATE, "private" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PRIVATE, com.github.javaparser.ast.Modifier.privateModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "private", com.github.javaparser.ast.Modifier.privateModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.privateModifier(), "private" );

        MODS_KEYWORD_TO_BIT_MAP.put( "static", java.lang.reflect.Modifier.STATIC );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.STATIC, "static" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.STATIC, com.github.javaparser.ast.Modifier.staticModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "static", com.github.javaparser.ast.Modifier.staticModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.staticModifier(), "static" );

        MODS_KEYWORD_TO_BIT_MAP.put( "synchronized", java.lang.reflect.Modifier.SYNCHRONIZED );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.SYNCHRONIZED, "synchronized" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.SYNCHRONIZED, com.github.javaparser.ast.Modifier.synchronizedModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "synchronized", com.github.javaparser.ast.Modifier.synchronizedModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.synchronizedModifier(), "synchronized" );

        MODS_KEYWORD_TO_BIT_MAP.put( "abstract", java.lang.reflect.Modifier.ABSTRACT );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.ABSTRACT, "abstract" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.ABSTRACT, com.github.javaparser.ast.Modifier.abstractModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "abstract", com.github.javaparser.ast.Modifier.abstractModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.abstractModifier(), "abstract" );

        MODS_KEYWORD_TO_BIT_MAP.put( "final", java.lang.reflect.Modifier.FINAL );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.FINAL, "final" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.FINAL, com.github.javaparser.ast.Modifier.finalModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "final", com.github.javaparser.ast.Modifier.finalModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.finalModifier(), "final" );

        MODS_KEYWORD_TO_BIT_MAP.put( "native", java.lang.reflect.Modifier.NATIVE );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.NATIVE, "native" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.NATIVE, com.github.javaparser.ast.Modifier.nativeModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "native", com.github.javaparser.ast.Modifier.nativeModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.nativeModifier(), "native" );

        MODS_KEYWORD_TO_BIT_MAP.put( "transient", java.lang.reflect.Modifier.TRANSIENT );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.TRANSIENT, "transient" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.TRANSIENT, com.github.javaparser.ast.Modifier.transientModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "transient", com.github.javaparser.ast.Modifier.transientModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.transientModifier(), "transient" );

        MODS_KEYWORD_TO_BIT_MAP.put( "volatile", java.lang.reflect.Modifier.VOLATILE );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.VOLATILE, "volatile" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.VOLATILE, com.github.javaparser.ast.Modifier.volatileModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "volatile", com.github.javaparser.ast.Modifier.volatileModifier() );

        MODS_KEYWORD_TO_BIT_MAP.put( "strictfp", java.lang.reflect.Modifier.STRICT );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.STRICT, "strictfp" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.STRICT, com.github.javaparser.ast.Modifier.strictfpModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "strictfp", com.github.javaparser.ast.Modifier.strictfpModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.strictfpModifier(), "strictfp" );

        //ANY_FOR DEFAULT INTERFACES
        MODS_KEYWORD_TO_BIT_MAP.put( "default", 1 << 12 );
        MODS_BIT_TO_KEYWORD_MAP.put( 1 << 12, "default" );
        //BIT_TO_ENUM_MAP.put( 1 << 12, com.github.javaparser.ast.Modifier );
        //KEYWORD_TO_ENUM_MAP.put( "default", com.github.javaparser.ast.Modifier.DEFAULT );
    }


    public static NodeList<Modifier> getImpliedModifiers(NodeWithModifiers nwm ){
        if( nwm instanceof VariableDeclarator){
            NodeList<Modifier> nlm = getImpliedModifiers( (VariableDeclarator)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof FieldDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (FieldDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof AnnotationDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (AnnotationDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof ClassOrInterfaceDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (ClassOrInterfaceDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof ConstructorDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (ConstructorDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof EnumDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (EnumDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof MethodDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (MethodDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof TypeDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (TypeDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof AnnotationMemberDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (AnnotationMemberDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        //Parameter?
        throw new _jdraftException("Unable to get implied modifiers for "+ nwm+ " of "+nwm.getClass());
    }

    public static NodeList<Modifier> getImpliedModifiers(Parameter p){
        return new NodeList<Modifier>();
    }

    public static NodeList<Modifier> getImpliedModifiers(AnnotationMemberDeclaration amd){
        return new NodeList<Modifier>();
    }

    public static NodeList<Modifier> getImpliedModifiers(VariableDeclarator vd) {
        if (!vd.getParentNode().isPresent()) {
            return null;
        }
        return getImpliedModifiers((FieldDeclaration) vd.getParentNode().get());
    }

    public static NodeList<Modifier> getImpliedModifiers(FieldDeclaration fd) {
        if (fd == null) {
            return null;
        }
        if (!fd.getParentNode().isPresent()) {
            return null;
        }
        Node parent = fd.getParentNode().get();
        if (parent instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) parent;
            if (coid.isInterface()) {
                //check if initilized ??
                return NodeList.nodeList(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier());
            }
            return new NodeList<>();
        }
        if (parent instanceof AnnotationDeclaration) {
            AnnotationDeclaration ad = (AnnotationDeclaration) parent;
            return NodeList.nodeList(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier());
        }
        return new NodeList<>();
    }

    /**
     * Add modifiers from a to b (avoiding duplicates)
     *
     * @param target the nodelist to be merged to
     * @param source the nodelist to add to the target
     * @return the merged nodelist (target) with all of the added Modifier s
     */
    public static NodeList<Modifier> merge(NodeList<Modifier> target, NodeList<Modifier> source) {
        if( source != null ){
            source.forEach(m -> {
                if (!target.contains(m)) {
                    target.add(m);
                }
            });
        }
        return target;
    }

    /**
     *
     * @param left
     * @param right
     * @return
     */
    public static boolean equals(NodeList<Modifier> left, NodeList<Modifier> right) {
        return left.size() == right.size()
                && right.containsAll(left);
    }

    public static boolean modifiersEqual(NodeWithModifiers left, NodeWithModifiers right) {
        if (left == null) {
            return right == null;
        }
        if (right == null) {
            return false;
        }
        //return modifiersEqual(left.getModifiers(), right.getModifiers());
        //I dont know why I'd be doing this.... but I have to look at IMPLIED modifiers
        // and if they arent the same AST type... then WHY?... but ok lets just check modifiers
        // straight up
        if (left.getClass() != right.getClass()) {
            return Objects.equals(left, right);
        }

        // If they are both the same class, I need to derive the Implied Modifiers
        // based on the "container type" (i.e. an initialized  Field on a Interface like this:
        // interface I { int x=100;}
        // ...is REALLY equal to this:
        // interface I { public static final int x = 100; }
        // so it's IMPLIED Modifiers are "public static final"
        // because (even though they dont exist syntactically, they are implied by the context)
        /// TODO what about access modifiers (i need to make sure I dont do public & private when I merge the EnumSets
        // ...so I derive the implied modifiers
        NodeList<Modifier> leftImplied = null;
        NodeList<Modifier> rightImplied = null;
        //the following types need to take into account Implied Modifiers
        // we need to look them up so we can compare like for like
        if (left instanceof FieldDeclaration) {
            leftImplied = getImpliedModifiers((FieldDeclaration) left);
            rightImplied = getImpliedModifiers((FieldDeclaration) right);
        } else if (left instanceof MethodDeclaration) {
            leftImplied = getImpliedModifiers((MethodDeclaration) left);
            rightImplied = getImpliedModifiers((MethodDeclaration) right);
        } else if (left instanceof TypeDeclaration) {
            leftImplied = getImpliedModifiers((TypeDeclaration) left);
            rightImplied = getImpliedModifiers((TypeDeclaration) right);
        } else if (left instanceof ConstructorDeclaration) {
            leftImplied = getImpliedModifiers((ConstructorDeclaration) left);
            rightImplied = getImpliedModifiers((ConstructorDeclaration) right);
        }
        //
        if (leftImplied != null && rightImplied != null) { //Both are "connected" fields (not dangling without a parent node)
            leftImplied = merge(leftImplied, left.getModifiers());
            rightImplied = merge(rightImplied, right.getModifiers());
            return equals(leftImplied, rightImplied);
        }
        if (leftImplied == null && rightImplied == null) { //BOTH are dangling (without parents)
            return equals(left.getModifiers(), right.getModifiers());
        }
        if (leftImplied == null) { //if one or the other is null, then add the implied to BOTH
            leftImplied = NodeList.nodeList(rightImplied);
            leftImplied = merge(leftImplied, left.getModifiers());
            rightImplied = merge(rightImplied, right.getModifiers());
            return equals(leftImplied, rightImplied);
        }
        rightImplied = NodeList.nodeList(leftImplied);
        rightImplied = merge(rightImplied, right.getModifiers());

        leftImplied = merge(leftImplied, left.getModifiers());
        return equals(leftImplied, rightImplied);
    }

    public static NodeList<Modifier> getImpliedModifiers(TypeDeclaration td) {
        if (td instanceof ClassOrInterfaceDeclaration) {
            return getImpliedModifiers((ClassOrInterfaceDeclaration) td);
        }
        if (td instanceof EnumDeclaration) {
            return getImpliedModifiers((EnumDeclaration) td);
        }
        return getImpliedModifiers((AnnotationDeclaration) td);
    }

    public static NodeList<Modifier> getImpliedModifiers(ClassOrInterfaceDeclaration coid) {
        if (coid.isInterface()) {
            return NodeList.nodeList(Modifier.abstractModifier());
        }
        return new NodeList<>();
    }

    public static NodeList<Modifier> getImpliedModifiers(EnumDeclaration ed) {
        return NodeList.nodeList(Modifier.staticModifier(), Modifier.finalModifier());
    }

    public static NodeList<Modifier> getImpliedModifiers(AnnotationDeclaration ad) {
        return NodeList.nodeList(Modifier.abstractModifier(), Modifier.staticModifier());
    }

    public static NodeList<Modifier> getImpliedModifiers(MethodDeclaration md) {
        if (md == null) {
            return null;
        }
        if (!md.getParentNode().isPresent()) {
            // we have a single rule that applies for a method with no parent
            // ...in the absense of a method body i.e. "void m();" (body is NULL)
            // it is implied that the method is public & abstract
            if (!md.getBody().isPresent()) {
                return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
            }
            return new NodeList();
        }
        Node parent = md.getParentNode().get();
        if (parent instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) parent;
            if (coid.isInterface()) {
                if (!md.getBody().isPresent()) { //no body & on an interface
                    return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
                }
                //if it's a static or default method on an interface its public by default
                return NodeList.nodeList(Modifier.publicModifier());
            }

            if (!md.getBody().isPresent()) {
                return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
            }
            return new NodeList<>();
        }
        if (parent instanceof AnnotationDeclaration) { //annotation methods
            return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
        }
        if (!md.getBody().isPresent()) { //No body, must be public abstract
            return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
        }
        return new NodeList<>();
    }

    public static NodeList<Modifier> getImpliedModifiers(ConstructorDeclaration cd) {
        if (cd == null) {
            return null;
        }
        if (!cd.getParentNode().isPresent()) {
            return null;
        }
        Node parent = cd.getParentNode().get();
        if (parent instanceof EnumDeclaration) {
            return NodeList.nodeList(Modifier.privateModifier());
        }
        return new NodeList<>();
    }
}
