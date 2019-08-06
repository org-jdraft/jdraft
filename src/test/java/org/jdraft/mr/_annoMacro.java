package org.jdraft.mr;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithConstructors;
import org.jdraft._constructor;
import org.jdraft._draftException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Links individual annotations to {@code Consumer<Node>} macros
 *
 * a simple replacement for Javas "Annotation Processing" that uses JavaParser
 * with the following conventions:
 *
 * 1) the Annotation MUST be a Runtime Annotation (i.e. the RetentionPolicy MUST be RetentionPolicy.RUNTIME):
 * <PRE>{@code
 * @Retention(RetentionPolicy.RUNTIME)
 * @interface _abstract{
 * }
 * </PRE>
 *
 * 2) the Annotation MUST have ONE of the following
 *   A) a public static field that is type {@code Consumer}
 * <PRE>{@code
 *    @Retention(RetentionPolicy.RUNTIME)
 *    @interface _abstract {
 *         Consumer<Node> $ = n -> {
 *             if(n instanceof NodeWithAbstractModifier){
 *                 NodeWithAbstractModifier nwa = (NodeWithAbstractModifier)n;
 *                 nwa.setAbstract(true);
 *                 AnnotationExprMacro.removeAnnotation(n, _abstract.class);
 *             } else{
 *                 throw new AnnoMacroException("Annotation _abstract applied to a non abstract Node "+ n.getClass);
 *             }
 *         }
 *    }
 * }</PRE>
 *
 */
public class _annoMacro {

    /**
     * Applies Macros that are annotated in the clazz to the CompilationUnit
     * @param clazz the runtime Class
     * @param cu the AST compilationUnit (a model of the source code to be modified)
     */
    public static void to( Class clazz, CompilationUnit cu ){
        Apply.to(clazz, cu);
    }

    /**
     * Applies Macros that are annotated in the clazz to the CompilationUnit
     * @param clazz the runtime Class
     * @param td the TypeDeclaration by this class
     */
    public static void to(Class clazz, TypeDeclaration td ){
        Apply.to(clazz, td);
    }

    public static class Resolve {
        public static <N extends Node> List<Consumer<N>> from(Method method) {
            return from(method.getDeclaredAnnotations());
        }

        public static <N extends Node> List<Consumer<N>> from(Class clazz) {
            return from(clazz.getDeclaredAnnotations());
        }

        public static <N extends Node> List<Consumer<N>> from(Constructor ctor) {
            return from(ctor.getDeclaredAnnotations());
        }

        public static <N extends Node> List<Consumer<N>> from(Field f) {
            return from(f.getDeclaredAnnotations());
        }

        public static <N extends Node> List<Consumer<N>> from(java.lang.reflect.Parameter p) {
            //System.out.println("  >>>>>PARAMS " + p);

            Annotation[] ats = p.getAnnotations();
            //System.out.println("OHH HELLO ");
            //Annotation[] ats = p.getDeclaredAnnotations();
            //System.out.println("  >>>>>ANNS " + ats);
            return from(ats);
        }

        public static <N extends Node> List<Consumer<N>> from(AnnotatedType at) {
            return from(at.getDeclaredAnnotations());
        }

        public static <N extends Node> List<Consumer<N>> from(Annotation[] as) {
            List<Consumer<N>> macrosResolved = new ArrayList<>();
            for (int i = 0; i < as.length; i++) {
                Consumer<N> c = resolveMacro(as[i]);
                if (c != null) {
                    macrosResolved.add(c);
                }
            }
            return macrosResolved;
        }

        /**
         * Resolve a single Macro {@code (Consumer<Node>)} associated with a runtime annotation
         * @param an
         * @param <N>
         * @return a Consumer<N> for running the macro
         */
        public static <N extends Node> Consumer<N> resolveMacro(Annotation an){
            //first check
            // if there is a declared static field that is named "$" and
            Field[] fs = an.annotationType().getDeclaredFields();

            //System.out.println( "FOUND "+ fs.length+" FIELDS");
            //&& f.getType().isAssignableFrom(Consumer.class)
            //for(int i=0;i<fs.length;i++){
            //    System.out.println( fs[i] );
            //}

            //1) look for a public static field that is a Consumer on the Annotation Class
            //   NOTE: this is for Macros that are simple idempotent functions (dont require any input/state)
            Optional<Field> of =
                    Arrays.stream( fs ).filter(
                            f -> Modifier.isPublic(f.getModifiers())
                                    && Modifier.isStatic(f.getModifiers())
                                    && f.getType().isAssignableFrom(Consumer.class)).findFirst();
            //&& f.getName().equals("$")).findFirst();
            if( of.isPresent() ){
                //System.out.println( "FOUND "+ of.get());
                Field f = of.get();
                try {
                    return (Consumer<N>) f.get(null);
                }catch(Exception e){
                    System.out.println("EXCEPTION "+e);
                }
            }
            //2) look for a static inner declared class that takes the
            // Annotation as the only argument in the constructor
            // NOTE: this is for macros that
            // i.e.
            // @interface _extends {
            //     Class[] value();
            //
            //     class Act implements Consumer<Node>{
            //          Class[] toExtend
            //          public Macro( _extends _e){
            //               this.toExtend = _e.value();
            //          }
            //          public void accept(Node n) {
            //               //macro code in here
            //          }
            //      }
            // }
            Class[] clazzes = an.annotationType().getDeclaredClasses();

            //is there a public static nested class that implements Consumer
            // that takes the Annotation as a constructor argument
            Optional<Class> oc = Arrays.stream( clazzes ).filter(
                    c -> Modifier.isPublic(c.getModifiers())
                            && Modifier.isStatic(c.getModifiers())
                            && Consumer.class.isAssignableFrom(c)
                            && getConstructorWithSingleArg( c, an.annotationType() ) != null)
                    .findFirst();

            if( oc.isPresent() ){
                Constructor ct = getConstructorWithSingleArg( oc.get(), an.annotationType());
                try {
                    return (Consumer<N>) ct.newInstance(an);
                } catch(ReflectiveOperationException ie){
                    throw new AnnoMacroException("found ctor, couldnt run macro for "+ct, ie);
                }
            }
            //3) lastly check if there is a constructor with NO arguments
            //is there a public static nested class that implements Consumer
            // with no constructor arguments
            // i.e.
            // @interface _remove {
            //
            //     class Act implements Consumer<Node>{
            //          public void accept(Node n) {
            //               n.remove();
            //          }
            //      }
            // }
            oc = Arrays.stream( clazzes ).filter(
                    c -> Modifier.isPublic(c.getModifiers())
                            && Modifier.isStatic(c.getModifiers())
                            && Consumer.class.isAssignableFrom(c)
                            && getConstructorEmptyArgs(c)  != null)
                    .findFirst();

            if( oc.isPresent() ){
                Constructor ct = getConstructorEmptyArgs( oc.get() );
                try {
                    return (Consumer<N>) ct.newInstance();
                } catch(ReflectiveOperationException ie){
                    throw new AnnoMacroException("found ctor, couldnt run macro for "+ct, ie);
                }
            }
            return null;
        }

        private static Constructor getConstructorEmptyArgs( Class clazz ){
            try {
                return clazz.getDeclaredConstructor();
            }catch(Exception ee){ }
            return null;
        }

        private static Constructor getConstructorWithSingleArg( Class clazz, Class annType){
            try {
                return clazz.getDeclaredConstructor(annType);
            }catch(Exception ee){ }
            return null;
        }
    }



    public static class Apply {

        public static void to( Class clazz, CompilationUnit cu ){
            Optional<TypeDeclaration<?>> otd =
                    cu.getTypes().stream().filter( t-> {
                        //System.out.println("TYPE NAME "+ t.getNameAsString() );
                        //System.out.println("CLASS NAME "+ clazz.getSimpleName() );
                        return t.getNameAsString().equals( clazz.getSimpleName() );
                    }).findFirst();
            if( !otd.isPresent()){
                throw new AnnoMacroException("cannot find type for "+ clazz);
            }
            to( clazz, otd.get());

            //companion types
        }

        // prêt-à-porter
        // v
        // prototype
        // lambdas
        // local classes
        // etc. (I dont want to run/use/this/ I'm just building this thing to build the code)
        public static void to( Class clazz, TypeDeclaration td ){

            //1) process annotation macros on member classes / member types
            List<TypeDeclaration> tds = (List<TypeDeclaration>)
                    td.getMembers().stream().filter( m -> m instanceof TypeDeclaration ).collect(Collectors.toList());
            Class[] memberClasses = clazz.getDeclaredClasses();
            tds.forEach( t-> {
                //first find the member class associated with this type
                Optional<Class> omc =
                        Arrays.stream(memberClasses).filter(m-> m.getSimpleName().equals(t.getNameAsString())).findFirst();
                if( !omc.isPresent() ){
                    throw new AnnoMacroException("Cannot find Class for member type "+ t);
                }
                to( omc.get(), t);
            });

            //2) process annotation macros on Fields
            td.getFields().forEach( f-> to(clazz, (FieldDeclaration)f) );

            //3) process annotation macros on Methods
            td.getMethods().forEach( m -> to(clazz, (MethodDeclaration)m));

            //4) process annotation macros on Constructors
            if( td instanceof NodeWithConstructors ){
                NodeWithConstructors nwc = (NodeWithConstructors) td;
                nwc.getConstructors().forEach( c -> to(clazz, (ConstructorDeclaration)c) );
            }

            //5) process the top level annotations
            List<Consumer<Node>> macros = Resolve.from(clazz);
            macros.forEach(mac -> mac.accept(td));
        }

        static void to(Constructor c, ConstructorDeclaration cd) {
            //
            Annotation[][] pas = c.getParameterAnnotations();
            int adjust = 0;
            if (c.getParameterCount() > cd.getParameters().size()) {
                //System.out.println("Adjusting +1 ");
                adjust = 1;
            }
            if (adjust < cd.getParameters().size()) {
                //System.out.println("PARAMETERS LENGTH " + pas.length);
                //System.out.println("PARAMETERS LENGTH " + cd);
                //System.out.println("PARAMETERS LENGTH " + c);
                for (int i = 0; i < pas.length; i++) {
                    List<Consumer<Node>> lcs = Resolve.from(pas[i]);
                    Parameter pp = cd.getParameter(i);
                    lcs.forEach(cn -> cn.accept(pp));
                }
            }
        /*
        int adjust = 0;

        for(int i=adjust; i < c.getParameterCount();i++){
            System.out.println( " i = "+ i);
            System.out.println( " i adjust = "+ (i - adjust));
            java.lang.reflect.Parameter p = c.getParameters()[i];
            com.github.javaparser.ast.body.Parameter ap = cd.getParameter(i - adjust);
            System.out.println( " P "+ p );
            System.out.println( " A P " +ap );
            List<Consumer<Node>> lcn = resolveMacros(  );
            to( p, ap);
        }
        */

            //c.getAnnotatedReturnType()

            //3) apply annotation parameters to the exception types
            AnnotatedType[] ats = c.getAnnotatedExceptionTypes();
            for (int i = 0; i < ats.length; i++) {
                to(ats[i], cd.getThrownException(i));
            }

            //4) lastly, apply annotation macros to the method itself
            List<Consumer<Node>> macros = Resolve.from(c);
            macros.forEach(mac -> mac.accept(cd));
        }

        static void to(Class clazz, ConstructorDeclaration cd) {
            //List<Constructor> cts = null;
            Optional<Constructor> oc =
                    Arrays.stream(clazz.getDeclaredConstructors()).filter(c -> _constructor.isMatch(c, cd)).findFirst();
            if (!oc.isPresent()) {
                throw new AnnoMacroException("Couldnt find ctor");
            }
            Constructor c = oc.get();
            //System.out.println("CTOR " + c);
        /*
        if( clazz.isMemberClass() || clazz.isLocalClass() && !Modifier.isStatic( clazz.getModifiers()) ){
            System.out.println( "Checking a non static local or member ctor");

                    cts = Arrays.stream(clazz.getDeclaredConstructors()).filter(
                    c -> c.getName().equals(cd.getNameAsString())
                            && cd.hasParametersOfType(
                                    Arrays.copyOfRange( c.getParameterTypes(),1, c.getParameterCount() )))
                    .collect(Collectors.toList());
            if( cts.size() != 1){
                throw new AnnoMacroException("Couldnt find ctor");
            }
        } else {
            cts = Arrays.stream(clazz.getDeclaredConstructors()).filter(
                    m -> m.getName().equals(cd.getNameAsString())
                            && cd.hasParametersOfType(m.getParameterTypes()))
                    .collect(Collectors.toList());
        }
        if( cts.size() == 0 ){
            throw new AnnoMacroException( "Runtime Constructor for "+cd +" not found");
        }
        if( cts.size() > 1 ){
            throw new AnnoMacroException( "Ambiguous Runtime Constructor for "+cd +" not found" + cts);
        }
         */
            //Constructor c = cts.get(0);
            to(c, cd);
        }

        static void to(Class clazz, MethodDeclaration md) {
            List<Method> mds =
                    Arrays.stream(clazz.getDeclaredMethods()).filter(
                            m -> m.getName().equals(md.getNameAsString())
                                    && md.hasParametersOfType(m.getParameterTypes()))
                            .collect(Collectors.toList());
            if (mds.size() == 0) {
                //System.out.println("NOT FOUND");
                throw new AnnoMacroException("Runtime Method for " + md + " not found");
            }
            if (mds.size() > 1) {
                throw new AnnoMacroException("Ambiguous Runtime Method for " + md + " not found" + mds);
            }
            Method m = mds.get(0);

            //1) apply macros to each parameter
            for (int i = 0; i < m.getParameterCount(); i++) {
                to(m.getParameters()[i], md.getParameter(i));
            }

            //2) apply annotation macros to the return type
            to(m.getAnnotatedReturnType(), md.getType());

            //Yeah we're not going to go there
            //m.getAnnotatedReceiverType();

            //3) apply annotation macros to the exception types
            AnnotatedType[] ats = m.getAnnotatedExceptionTypes();
            for (int i = 0; i < ats.length; i++) {
                to(ats[i], md.getThrownException(i));
            }

            //4) lastly, apply annotation macros to the method itself
            List<Consumer<Node>> macros = Resolve.from(mds.get(0));
            macros.forEach(mac -> mac.accept(md));
        }

        static void to(AnnotatedType aType, com.github.javaparser.ast.type.Type astType) {
            List<Consumer<Node>> macros = Resolve.from(aType);
            macros.forEach(m -> m.accept(astType));
        }

        static void to(java.lang.reflect.Parameter param, Parameter astParam) {
            //System.out.println("    PARAM     " + param);
            //System.out.println("    AST PARAM " + astParam);
            List<Consumer<Node>> macros = Resolve.from(param);
            //System.out.println(macros);
            macros.forEach(m -> m.accept(astParam));
        }

        static void to(Class clazz, FieldDeclaration fd) {
            try {
                for (int i = 0; i < fd.getVariables().size(); i++) {

                    if (fd.getVariables().size() > 1) {
                        Field f = clazz.getDeclaredField(fd.getVariables().get(i).getNameAsString());
                        List<Consumer<Node>> macros = Resolve.from(f);
                        for (int j = 0; j < fd.getVariables().size(); j++) {
                            VariableDeclarator vd = fd.getVariable(j);
                            macros.forEach(m -> m.accept(vd));
                        }
                    } else {
                        Field f = clazz.getDeclaredField(fd.getVariables().get(i).getNameAsString());
                        List<Consumer<Node>> macros = Resolve.from(f);
                        macros.forEach(m -> m.accept(fd.getVariable(0)));
                    }
                }
            } catch (NoSuchFieldException nsfe) {
                throw new AnnoMacroException("no Field " + fd + " on " + clazz, nsfe);
            }
        }
    }

    public static <N extends Node> N removeAnnotation( N node, Class<? extends Annotation> annClass ){
        if(node instanceof NodeWithAnnotations){
            NodeWithAnnotations nwa = (NodeWithAnnotations)node;

            List<AnnotationExpr> aes = (List<AnnotationExpr>)nwa.getAnnotations().stream().filter(a ->
                    ((AnnotationExpr)a).getNameAsString().equals(annClass.getSimpleName()) ||
                            ((AnnotationExpr)a).getNameAsString().equals(annClass.getCanonicalName())).collect(Collectors.toList());
            aes.forEach( ae -> nwa.getAnnotations().remove(ae));
        } else{
            throw new AnnoMacroException("Node is not a NodeWithAnnotations "+ node.getClass() );
        }
        return node;
    }

    public static <N extends Node> N removeAnnotation( N node, Annotation an ){
        if(node instanceof NodeWithAnnotations){
            NodeWithAnnotations nwa = (NodeWithAnnotations)node;
            List<AnnotationExpr> aes = (List<AnnotationExpr>)nwa.getAnnotations().stream().filter(a ->
                   ((AnnotationExpr)a).getNameAsString().equals(an.annotationType().getSimpleName()) ||
                   ((AnnotationExpr)a).getNameAsString().equals(an.annotationType().getCanonicalName())).collect(Collectors.toList());
            aes.forEach( ae -> nwa.getAnnotations().remove(ae));
        } else{
            throw new AnnoMacroException("Node is not a NodeWithAnnotations "+ node.getClass() );
        }
        return node;
    }

    /**
     * An exception when resolving or using Annotation Macros with
     */
    public static class AnnoMacroException extends _draftException {

        public AnnoMacroException( String message){
            super( message);
        }

        public AnnoMacroException( String message, Throwable t){
            super( message, t);
        }
    }
}
