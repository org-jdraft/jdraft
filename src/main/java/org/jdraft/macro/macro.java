package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.*;
import org.jdraft.proto.$;

import com.github.javaparser.utils.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Accepts a (mutable) TypeDeclaration & it's corresponding runtime Class and recursively walks down the runtime Class
 * looking for Runtime {@link AnnotatedElement}s (like {@link Method}, {@link Field}...)
 * using reflection to search for {@link AnnotatedElement}s that have "Runtime" {@link Annotation}s i.e. ( @Retention(RetentionPolicy.RUNTIME) )
 * that contain an "embedded" or adhoc with some "Macro" {@link Consumer<Node>} that will be applied
 * to the Ast model ({@link TypeDeclaration}, {@link MethodDeclaration}, {@link ConstructorDeclaration}...) of
 * the code.
 *
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.RetentionPolicy#RUNTIME
 */
public abstract class macro<A extends Annotation,N extends Node> implements Consumer<N> {

    /** Annotation instance */
    public final A annotation;

    public macro(A annotation){
        this.annotation = annotation;
        Log.info("          Constructing %s ", ()->toString());
    }

    public void accept(N node){
        Log.info("          Expanding %s\n          on:\n%s", ()->toString(), ()->Text.indent(node.toString(), "          "));
        expand(node);
        removeAnnotation(node, annotation.annotationType());
    }

    public abstract void expand(N node);

    /** Remove the annotation from the AST Node (after completing the macro task, clean up and remove th annotation */
    public static <N extends Node> N removeAnnotation(N node, Class<? extends Annotation> annClass){
        if( node instanceof VariableDeclarator ){
            _field.of( (VariableDeclarator)node).removeAnnos(annClass);
        } else {
            _anno._annos.of((NodeWithAnnotations) node).remove(annClass);
        }
        return node;
    }

    public static <_T extends _type> _T to(Class clazz ){
        Log.info("  Macros to %s", ()->clazz);
        TypeDeclaration td = Ast.typeDecl(clazz);
        toType( td, clazz);
        return _java.type( td );
    }

    public static void toType( TypeDeclaration td, Class clazz ){
        Log.info("    1 of 5) fields of %s", ()->clazz );
        td.getFields().forEach(
                f-> ((FieldDeclaration)f).getVariables().forEach(v->{
                        try {
                            Field ff = clazz.getDeclaredField(v.getNameAsString());
                            to( v, ff);
                        }catch(Exception e){
                            Log.error("Exception processing %s\n%s",()->v, ()->e);
                        }
                })
        );
        Log.info("    2 of 5) methods of %s", ()->clazz );
        td.getMethods().forEach(
                m ->{
                    _method _m = _method.of( (MethodDeclaration)m);
                    Optional<Method> om =
                            Arrays.stream(clazz.getDeclaredMethods()).filter( em -> _m.hasParametersOf(em)).findFirst();
                    if( om.isPresent() ) {
                        to( (MethodDeclaration)m, om.get());
                    }
                }
        );
        Log.info("    3 of 5) constructors of %s", ()->clazz );
        td.getConstructors().forEach(
                c->{
                    _constructor _ct = _constructor.of( c );
                    Optional<Constructor> oc =
                            Arrays.stream(clazz.getDeclaredConstructors()).filter( ct -> _ct.hasParametersOf(ct) ).findFirst();
                    if( oc.isPresent() ){
                        to((ConstructorDeclaration)c, oc.get());
                    }
                }
        );

        //nested types
        Log.info("    4 of 5) nested types of %s", ()->clazz );
        td.stream($.DIRECT_CHILDREN).filter(n -> TypeDeclaration.class.isAssignableFrom(n.getClass())).forEach(
                nt->{
                    Optional<Class> oc =
                            Arrays.stream(clazz.getDeclaredClasses()).filter(c-> c.getName().equals( ((TypeDeclaration)nt).getNameAsString()) ).findFirst();
                    if( oc.isPresent() ){
                        to(nt, oc.get());
                    }
                }
        );
        //at last do the class itself
        Log.info("    5 of 5) to type %s",()->clazz );
        to( td, clazz );
    }

    //
    public static void to(Node ast, AnnotatedElement rte ){
        List<Consumer<Node>>macros = from(rte);

        //2) process each macro on the ast
        macros.forEach(m -> {
            //Log.info("          apply: %s\n          to:\n%s", ()->m.toString(), ()->Text.indent(ast.toString(), "          "));
            m.accept(ast);
            Log.info("          result: \n%s", ()-> Text.indent(ast.toString(), "          "));
        });
    }


    /**
     * Given a runtime AnnotatedElement, resolve the Macros (i.e. Consumer<Node>) in the
     * declaration order they appear.
     *
     * Note: some annotations DO NOT have associated macros
     * @param ae
     * @return
     */
    public static List<Consumer<Node>> from(AnnotatedElement ae ){
        Log.info("     --Resolving Annotation/Macros on %s", ()->ae);
        //Map<Annotation, Consumer<Node>> annoMacroMap = new HashMap<>();
        List<Consumer<Node>>macros = new ArrayList<>();

        //1) find a reflective Field on the Annotation that implements Consumer
        // i.e.
        //
        // @interface AM{
        //     Consumer<Node> aField = (n)-> System.out.println( n ); //<--this field
        // }
        // -or-
        // @interface AM{
        //     Do aField = new Do(); //<-- this field's type implements Consumer
        //     class Do implements Consumer<Node>{
        //          public void accept(Node n){ ...}
        //     }
        // }
        Arrays.stream(ae.getDeclaredAnnotations()).forEach(a -> {
            Field[] fs = a.annotationType().getDeclaredFields();
            Optional<Field> of = Arrays.stream(fs).filter(f -> {
                return Consumer.class.isAssignableFrom( f.getType());
            } ).findFirst();
            if( of.isPresent() ){ //If I find a Field that is a Consumer add it as a macro
                Field f = of.get();
                try {
                    Consumer<Node> cn = (Consumer<Node>)f.get(null);
                    Log.info("     ---Resolved Static Field Macro %s", ()->cn);
                    macros.add( cn );
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else{ //2) get all of the declared classes beneath the Annotation that implement Consumer
                Class[] decls = a.annotationType().getDeclaredClasses();
                List<Class> lcs = Arrays.stream(decls).filter( dc -> Consumer.class.isAssignableFrom(dc) ).collect(Collectors.toList());

                if( !lcs.isEmpty() ){
                    //2a)try to find a constructor on the (Consumer) that takes in the Annotation as a constructor arg
                    Optional<Constructor> oc = lcs.stream().map(c -> {
                        try{
                            //System.out.println( "CONSTRUCTOR " + c );
                            return c.getDeclaredConstructor(a.annotationType());
                        }catch(Exception e){
                            //System.out.println( "NO CONSTRUCTOR WITH "+a.annotationType());
                            return null;
                        }
                    }).filter( n -> n != null).findFirst();
                    if( oc.isPresent() ){ //I found a Class that has a constructor that takes the annotation Type as the first parameter
                        try {
                             // Log.info("     ---Resolved Instance(%s)",()->a );
                            Constructor ct = oc.get();
                            //Log.info("     ---Resolved Ct (%s)",()->ct );
                            //ct.getDeclaringClass().newInstance();
                            Object o = ct.newInstance(a);
                            Consumer<Node> cn = (Consumer<Node>)o;
                            Log.info("     ---Resolved Instance(%s) Macro %s",()->a,()->cn);
                            macros.add( cn );
                        }catch(Exception e){
                            Log.error("     ---Unable to construct Macro for (%s): %s",()->a, ()->e );
                        }
                    }
                    //2b) find the constructor that takes NO arguments
                    oc = lcs.stream().map(c -> {
                        try{
                            return c.getConstructor();
                        }catch(Exception e){
                            return null;
                        }
                    }).filter(n -> n != null).findFirst();

                    if( oc.isPresent() ){ //I found a Class that has a constructor that takes no args as the first parameter
                        try {
                            Consumer<Node> cn = (Consumer<Node>)oc.get().newInstance(a);
                            Log.info("---Resolved Instance() Macro %s", ()->cn);
                            macros.add(cn);
                        }catch(Exception e){ }
                    }
                }
            }
        });
        return macros;
    }
}
