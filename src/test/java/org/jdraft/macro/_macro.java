package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.*;
import com.github.javaparser.ast.body.FieldDeclaration;
import org.jdraft._jdraftException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Runs all runtime _macro ANNOTATIONS that have the convention
 * where the annotation class has an static inner class that implements _macro<T>
 * for example:
 * <PRE>
 *
 * // The Top level Annotation
 * // 1) MUST have RETENTION POLICY RUNTIME
 *
 * @Retention(RetentionPolicy.RUNTIME)
 * @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
 * public @interface _final {
 *
 *     // 2) MUST have a Static member class that implements _macro<T>
 *     class Macro implements _macro<_anno._hasAnnos> {
 *         @Override
 *         public _anno._hasAnnos expand(_anno._hasAnnos _model) {
 *             return to(_model);
 *         }
 *         public static <T extends _anno._hasAnnos> T to( T _model ){
 *             ((_modifiers._hasModifiers) _model).getModifiers().setFinal();
 *             return _model;
 *         }
 *     }
 * }
 * </PRE>
 *
 * ...If the annotation has PARAMETERS that it has to the _macro
 * (like for the _macro ANNOTATIONS {@link _implement} or {@link _extend}, {@link _import}
 * we pass the annotation instance in as the only constructor arg.
 *
 * @see _implement for an example passing PARAMETERS in the annotation instance to the _macro constructor
 *
 * A _macro is a mutation to be applied to a {@link _java._multiPart}.
 * By convention, Macros can be applied directly via a static method, or through
 * A Runtime Annotation and it's corresponding _macro.
 *
 * @see _macro#to(Class, _type)
 *
 * @param <M> the TYPE to apply the
 */
public interface _macro<M extends _annoExprs._withAnnoExprs>
        extends Function<M,M> {

    /**
     * Given a Runtime Annotation instance, find & instantiate a new {@link _macro} to process it
     * (or return null if there is no {@link _macro} to process
     * @param ann the runtime annotation instance
     * @return the _macro instance to process the annotation (or null if none are found)
     */
    static _macro macroFor(Annotation ann ){

        //look for nested classes that are defined on the Annotation that are instances of _macro
        Class[] inners = ann.annotationType().getDeclaredClasses();
        //check for a member class that implements _macro

        Optional<Class> om = Arrays.stream(inners).filter(dc -> _macro.class.isAssignableFrom(dc)).findFirst();
        if (om.isPresent()) {
            try { //check if there is a no arg constructor
                return (_macro)om.get().getDeclaredConstructor().newInstance();
            } catch (Exception ee){ /*might not have a no arg constructor*/ }
            try { //create new an instance of the _macro, passing in the annotation instance as the constructor argument
                return (_macro)om.get().getDeclaredConstructor(ann.annotationType()).newInstance( ann );
            } catch (Exception e) {
                throw new _jdraftException("couldn't call constructor for annotation _macro "+om.get()+" " +
                        "expected public "+om.get()+"() or public "+om.get()+"("+ann.annotationType()+")", e);
            }
        }
        //2) Look for FIELDS that are on the Annotation that are instances of _macro
        Field[] fs = ann.annotationType().getDeclaredFields();
        Optional<Field> of = Arrays.stream(fs)
                .filter(df -> Modifier.isPublic( df.getModifiers()) && Modifier.isStatic( df.getModifiers())
                        && _macro.class.isAssignableFrom(df.getType()))
                .findFirst();
        if(of.isPresent() ){
            try {
                return (_macro) of.get().get(ann);
            }
            catch(IllegalAccessException iae){
                //shouldnt happen but OK
            }
        }
        return null; //NORMAL: not all ANNOTATIONS have corresponding macros i.e. @NonNull or @Deprecated
    }

    /**
     * Given a runtime {@link AnnotatedElement} (a {@link Class}, {@link Method}, {@link Constructor}, {@link Field})
     * and it's corresponding model;
     * Find and apply all {@link _macro}s based on the runtime {@link  Annotation}s on the AnnotatedElement to the _model
     * @param _model the mutable annotated model entity ( {@link _class}, {@link _method}, {@link _constructor}, {@link _field}
     * @param ae The Reflective Runtime Entity ({@link Class}, {@link Method}, {@link Constructor}, {@link Field})
     * @param <T> the underlying TYPE of the model (some _anno._hasAnnos)
     * @return the modified _model (with all {@link _macro}s applied)
     */
    static <T extends _annoExprs._withAnnoExprs> T applyAllAnnotationMacros(T _model, AnnotatedElement ae ) {
        //Arrays.stream(ae.getAnnotations()).forEach( a-> System.out.println( a ) );
        //System.out.println( "Applying macros to "+ae.getAnnotations());
        Annotation[] anns = ae.getAnnotations();
        for (int i = 0; i < anns.length; i++) {
            _macro _ma = macroFor(anns[i]);
            if (_ma != null) {
                
                _model = (T)_ma.apply( (T)_model);
                //System.out.println( "MODEL AFTER "+ _model);
                if( _model != null ) {
                    //the _macro expansion MAY have actually removed the
                    // entity entirely, so double check... if not, make sure the
                    // annotation is removed after the _macro processes
                    _model = (T)_model.removeAnnoRefs(anns[i].annotationType());
                }
                //System.out.println( "After "+_ma+" "+_model);
            }
        }
        return _model;
    }

    /**
     * Apply Annotation Macros to the _field based on the RuntimeAnnotations on the field in the Clazz
     *
     * @param clazz
     * @param _fl
     * @return
     */
    static _field to( Class clazz, _field _fl ){
        try {
            Field f = clazz.getDeclaredField(_fl.getName());
            applyAllAnnotationMacros(_fl, f);
            return _fl;
        } catch( NoSuchFieldException nsfe ){
            throw new _jdraftException("no Field "+ _fl+" on "+clazz, nsfe);
        }
    }

    static _method to( Class clazz, _method _mm ){
        
        if( !_mm.hasAnnoRefs()){
            return _mm;
        }
        Method mm = null;
        List<Method> ms = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(_mm.getName())).collect(Collectors.toList());

        for (int i = 0; i < ms.size(); i++) {

            if( _mm.hasParametersOf(ms.get(i))) {
                mm = ms.get(i);
            }
        }
        if( mm == null){
            //TBH if I cant find it... and they dont have any annotations... who cares
            
            //
            
            //I have a theory that this happens ALWAYS TO member classes
            if( clazz.isAnonymousClass() ){ 
                //there can be lots of issues with nested private anonymous member classes
                //for now, lets not burp, capture and move along silently
                return _mm;
            }
            if( clazz.isLocalClass() ){
                throw new _jdraftException("Could not find method "+ _mm +" on LOCAL class "+ clazz );
            }
            if( clazz.isMemberClass() ){
                throw new _jdraftException("Could not find method "+ _mm +" on MEMBER class "+ clazz );
            }
            if( clazz.isSynthetic() ){
                throw new _jdraftException("Could not find method "+ _mm +" on SYNTHETIC class "+ clazz );
            }
            
            throw new _jdraftException("Could not find method "+ _mm +" on class "+ clazz );
            
        }
        Parameter[] ps = mm.getParameters();
        for(int i=0;i<ps.length; i++){
            applyAllAnnotationMacros(_mm.getParameter(i), ps[i] );
        }
        //apply ANNOTATIONS to the method AFTER PARAMETERS
        return applyAllAnnotationMacros(_mm, mm );
    }

    /**
     * Given a Class and _type, check if annotations are Macro annotations, and if so,
     *
     * @param clazz a Runtime Class that may have ANNOTATIONS (or BODY with Macro ANNOTATIONS)
     * @param _t the _type model instance
     * @param <T> the specific TYPE
     * @return the modified _type
     */
    static <T extends _type> T to(Class clazz, T _t) {
        //we need to do this b/c field declarations can be
        // @_final int x, y, z;
        // and this SINGLE FieldDeclaration represents (3) _field models
        // so, we only process the annotation once (the first one)
        Set<FieldDeclaration> fds = new HashSet<>();
        _t.forFields(_f -> {
            _field _fl = (_field)_f;
            if( _fl.ast() != null
                    && _fl.ast().getParentNode().isPresent()
                    && !fds.contains( _fl.getFieldDeclaration() ) ) {
                to(clazz, _fl );
                fds.add( _fl.getFieldDeclaration() );
                /*
                try {
                    Field f = clazz.getDeclaredField(((_field) _f).getName());
                    applyAllAnnotationMacros(_fl, f);
                    fds.add( _fl.getFieldDeclaration() );
                } catch( NoSuchFieldException nsfe ){
                    throw new _jDraftException("Unable to in reflective Field "+ _fl+" ");
                }
                */
            }
        });
        //We process CONSTRUCTORS 
        if (_t instanceof _constructor._withConstructors) {
            ((_constructor._withConstructors) _t).forConstructors(_c -> to(clazz, (_constructor)_c));
        }
        //Process methods
        if (_t instanceof _method._withMethods) {
            ((_method._withMethods) _t).forMethods(_m -> {
                _method _mm = ((_method) _m);

                to( clazz, _mm);
                /*
                Method mm = null;
                List<Method> ms = Arrays.stream(clazz.getDeclaredMethods())
                        .filter(m -> m.getName().equals(_mm.getName())).collect(Collectors.toList());
                for (int i = 0; i < ms.size(); i++) {
                    if (_mm.hasParametersOfType(ms.get(i).getParameterTypes())) {
                        mm = ms.get(i);
                        break;
                    }
                }
                if( mm == null){
                    throw new _jDraftException("Could not find method "+ _mm + System.lineSeparator()+" for class "+ clazz.getName() );
                }

                Parameter[] ps = mm.getParameters();
                for(int i=0;i<ps.length; i++){
                    applyAllAnnotationMacros(_mm.getParameter(i), ps[i] );
                }
                //apply ANNOTATIONS to the method AFTER PARAMETERS
                applyAllAnnotationMacros(_mm, mm );
                */
            });
        }

        Class[] declaredClazzes = clazz.getDeclaredClasses();
        List<Class> nestedClasses = Arrays.stream(declaredClazzes).collect(Collectors.toList());

        //process nested classes
        _t.forInnerTypes(_nt -> {
            //System.out.println( "IN NESTED CLASS ");
            Optional<Class> foundClass = nestedClasses.stream().filter(c -> c.getSimpleName().equals(((_type) _nt).getName())).findFirst();
            if (!foundClass.isPresent()) {
                throw new _jdraftException("Could not find class for nested _type" + _nt);
            }
            to( foundClass.get(), (_type)_nt);
        });

        //the last thing to do is to run on the top level _type
        return applyAllAnnotationMacros(_t, clazz);
    }

    /**
     * 
     * @param clazz
     * @param _c
     * @return 
     */
    static _constructor to(Class clazz, _constructor _c ) {
        if( !_c.hasAnnoRefs() ){
            return _c;
        }
        List<Constructor> cs = Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList());
        if (clazz.isEnum()) {
            
            // enums have (2) implied params NAME and ordinal that are not explicit in the signature
            for (int i = 0; i < cs.size(); i++) {    
                
                Constructor ct = cs.get(i);
                if( _c.isParameters( ct ) ){
                    Parameter[] ps = ct.getParameters();
                    int delta = ps.length - _c.getParameters().size();
                    for(int j =0; j< _c.getParameters().size();j++ ){
                        applyAllAnnotationMacros(_c.getParameter(j), ps[j+delta]);
                    }
                    return applyAllAnnotationMacros(_c, ct); //process the constructor
                }
                /*
                System.out.println( "ENUM CT " + ct );                
                if( ct.getParameterCount() >= 2 ){ 
                    System.out.println( "COPY PARAMS 2, "+ct.getParameterCount() );
                    System.out.println( "COPY PARAMS 2, "+ct.getGenericParameterTypes().length );
                    Type[] co = Arrays.copyOfRange(ct.getGenericParameterTypes(), 2, ct.getParameterCount());
                    for(int j=0;j<co.length; j++){
                        System.out.println( "    "+co[j] );
                    }
                    if (_c.hasParametersOfType(co)) { //I found the constructor
                        Parameter[] ps = cs.get(i).getParameters();
                        ps = Arrays.copyOfRange(ps, 2, ps.length); //disreguard the first (2) PARAMETERS
                        for (int j = 0; j < ps.length; j++) { //process annotation macros on PARAMETERS first
                            applyAllAnnotationMacros(_c.getParameter(j), ps[j]);
                        }
                        return applyAllAnnotationMacros(_c, cs.get(i)); //process the constructor
                    }
                }
                */   
            }
        } else {
            for (int i = 0; i < cs.size(); i++) {
                Constructor ct = cs.get(i);
                //Constructor ct = cs.get(i);
                if( _c.isParameters( ct ) ){
                    Parameter[] ps = ct.getParameters();
                    int delta = ps.length - _c.getParameters().size();
                    for(int j =0; j< _c.getParameters().size();j++ ){
                        applyAllAnnotationMacros(_c.getParameter(j), ps[j+delta]);
                    }
                    return applyAllAnnotationMacros(_c, ct); //process the constructor
                }
                /*
                System.out.println( ct );
                //member classes have implicit declaration class as first parameter
                if (ct.getParameterCount() > 0 && ct.getParameterTypes()[0] == ct.getDeclaringClass().getEnclosingClass()) {
                    Class[] co = Arrays.copyOfRange(ct.getParameterTypes(), 1, ct.getParameterCount());
                    if (_c.hasParametersOfType(co)) {
                        Parameter[] ps = cs.get(i).getParameters();
                        ps = Arrays.copyOfRange(ps, 1, ps.length); //disreguard the first parameter
                        for (int j = 0; j < ps.length; j++) { //process annotation macros on PARAMETERS first
                            applyAllAnnotationMacros(_c.getParameter(j), ps[j]);
                        }
                        return applyAllAnnotationMacros(_c, cs.get(i)); //process the constructor _macro ANNOTATIONS
                    }
                } else if (_c.hasParametersOfType(ct.getParameterTypes())) {
                    Parameter[] ps = ct.getParameters();
                    for (int j = 0; j < ps.length; j++) { //process annotation macros on PARAMETERS first
                        applyAllAnnotationMacros(_c.getParameter(j), ps[j]);
                    }
                    return applyAllAnnotationMacros(_c, ct); //process the constructor _macro ANNOTATIONS
                }
             */       
            }
        }
        
        //TODO REMOVE THIS AFTER FIX
        if( clazz.isAnonymousClass() ){
            return _c;
            //throw new _jDraftException("Could not find constructor for ANONYMOUS class" + _c);
        }
        if( clazz.isLocalClass() ){
            throw new _jdraftException("Could not find constructor for LOCAL class" + _c);
        }
        if( clazz.isMemberClass() ){
            throw new _jdraftException("Could not find constructor for MEMBER class" + _c);
        }        
        throw new _jdraftException("Could not find constructor for " + _c);
    }


    public static <N extends Node> N removeAnnotation(N node, Class<? extends Annotation> annClass ){
        if(node instanceof NodeWithAnnotations){
            NodeWithAnnotations nwa = (NodeWithAnnotations)node;

            List<AnnotationExpr> aes = (List<AnnotationExpr>)nwa.getAnnotations().stream().filter(a ->
                    ((AnnotationExpr)a).getNameAsString().equals(annClass.getSimpleName()) ||
                            ((AnnotationExpr)a).getNameAsString().equals(annClass.getCanonicalName())).collect(Collectors.toList());
            aes.forEach( ae -> nwa.getAnnotations().remove(ae));
        } else{
            throw new _jdraftException("Node is not a NodeWithAnnotations "+ node.getClass() );
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
            throw new _jdraftException("Node is not a NodeWithAnnotations "+ node.getClass() );
        }
        return node;
    }
}
