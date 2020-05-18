package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.*;

import com.github.javaparser.utils.Log;
import org.jdraft.text.Text;

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
    public A annotation;

    public final Class<A> aType;

    public macro(Class<A> aType ){
        this.aType = aType;
    }

    public macro(A annotation){
        this.aType = (Class<A>)annotation.annotationType();
        this.annotation = annotation;
        //Log.info("          Constructing %s ", ()->toString());
    }

    public void accept(N node){
        Log.info("          Expanding %s\n          on:\n%s", ()->toString(), ()-> Text.indent(node.toString(), "          "));
        expand(node);
        removeAnnotation(node, aType);
    }

    public abstract void expand(N node);

    /** Remove the annotation from the AST Node (after completing the macro task, clean up and remove th annotation */
    public static <N extends Node> N removeAnnotation(N node, Class<? extends Annotation> annClass){
        if( node instanceof VariableDeclarator ){
            _field.of( (VariableDeclarator)node).removeAnnoExprs(annClass);
        } else {
            _annoExprs.of((NodeWithAnnotations) node).remove(annClass);
        }
        return node;
    }

    public static _method to(Class clazz, MethodDeclaration md){
        //_method _mm = macro.to(anonymousObjectContainingMethod.getClass(), md);
        _method _m = _method.of(md);

        Method rm = Arrays.stream(clazz.getDeclaredMethods()).filter(mm ->_m.hasParamsOf(mm)).findFirst().get();

        macro.to(md, rm );
        return _method.of(md);
    }

    public static <_T extends _type> _T to(Class clazz, _T _t){
        TypeDeclaration td = (TypeDeclaration)_t.ast();
        //if( n instanceof  TypeDeclaration ) {
        //System.out.println( "CLAZZ "+ clazz+" to "+_t );
        //TypeDeclaration td = (TypeDeclaration)n;
        toType(td, clazz);
        return _type.of(td);
        //}
        //CompilationUnit cu = (CompilationUnit)n;
        //TypeDeclaration td =cu.getTypes().stream().filter(t-> t.getNameAsString().equals(_t.getName())).findFirst().get();
        //toType(td, clazz);
        //return _java.type(td);
    }

    public static <_T extends _type> _T to(Class clazz, TypeDeclaration td){
        Log.info("  Macros to %s", () -> clazz);
        toType( td, clazz);
        return _type.of( td );
    }

    public static <_T extends _type> _T to(Class clazz ) {
        TypeDeclaration td = Ast.typeDecl(clazz);
        return to(clazz, td);
    }

    public static void toType( TypeDeclaration td, Class clazz ){
        Log.info("    1 of 5) fields of %s", ()->clazz );
        //we can't use lambdas, because we (potentially) remove
        List<FieldDeclaration> fs = new ArrayList<>();
        fs.addAll(td.getFields()); //we need a copy because we may remove

        fs.forEach(
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
        List<MethodDeclaration>mds = new ArrayList<>();
        mds.addAll(td.getMethods());
        mds.forEach(
                m ->{
                    _method _m = _method.of( (MethodDeclaration)m);
                    Optional<Method> om =
                            Arrays.stream(clazz.getDeclaredMethods()).filter(
                                    em -> em.getName().equals(_m.getName()) && _m.hasParamsOf(em)).findFirst();

                    if( om.isPresent() ) {
                        Method mm = om.get();

                        //i need to skip the first one if it's a non-static nested type
                        //int startIndex = 0;
                        //if( mm.getParameterCount() > _m.getParameters().size()){
                        //    startIndex = 1;
                        //}
                        for(int i = 0; i<_m.getParams().size(); i++){
                            to( _m.getParam(i).ast(), mm.getParameters()[i]);
                        }
                        to( (MethodDeclaration)m, om.get());
                    }
                }
        );
        Log.info("    3 of 5) constructors of %s", ()->clazz );
        List<ConstructorDeclaration> cds = new ArrayList<>();
        cds.addAll( td.getConstructors());
        cds.forEach(
                c->{
                    _constructor _ct = _constructor.of( (ConstructorDeclaration)c );
                    Optional<Constructor> oc =
                            Arrays.stream(clazz.getDeclaredConstructors()).filter( ct -> _ct.isParameters(ct) ).findFirst();
                    if( oc.isPresent() ){
                        Constructor ct = oc.get();
                        //todo do parameters
                        //i need to skip the first one if it'sd a receiver parameter or nested class parameter
                        //int startIndex = 0;
                        //if( ct.getParameterCount() > _ct.getParameters().size()){
                            //System.out.println( "MisMatched Parameter count" + ct.getParameterCount()+" "+ _ct.getParameters().size());
                        //    startIndex = 1;
                        //}

                        for(int i = 0; i<_ct.getParams().size(); i++){
                            //System.out.println( "ON "+ ct.getParameters()[i] );
                            to( _ct.getParam(i).ast(), ct.getParameters()[i]);
                            //to( _ct.getParameter(i).ast(), ct.getAnnotatedParameterTypes()[i+startIndex]);
                        }
                        to((ConstructorDeclaration)c, ct);
                    }
                }
        );

        //nested types
        Log.info("    4 of 5) nested types of %s", ()->clazz );
        List<TypeDeclaration> ncd = new ArrayList<>();
        td.getMembers().forEach(m -> {
            if( m instanceof TypeDeclaration){
                //System.out.println("A MEMBER "+ m);
                ncd.add( (TypeDeclaration)m);
            }
        } );
        //td.getMembers().stream().filter( m-> m instanceof TypeDeclaration ).forEach(t -> ncd.add( (TypeDeclaration)t ) );
        //td.stream($.DIRECT_CHILDREN).filter(n -> TypeDeclaration.class.isAssignableFrom(n.getClass())).forEach( cc -> ncd.add( (TypeDeclaration)cc)); //collect(Collectors.toList()) );
        ncd.forEach(
                nt->{
                    //we do this because a nested class of an anonymous class has a name like $1$NestedName,
                    Optional<Class> oc =
                            Arrays.stream(clazz.getDeclaredClasses()).filter(c-> c.getSimpleName().endsWith( ((TypeDeclaration)nt).getNameAsString()) ).findFirst();
                    if( oc.isPresent() ){
                        toType( (TypeDeclaration)nt, oc.get());
                    } else{
                        //System.out.println( "NOt Present " +nt );
                        Arrays.stream(clazz.getDeclaredClasses()).forEach(dc -> System.out.println("DC "+ dc.getName()));
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
                    Log.info( "%s", ()->e  );
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
