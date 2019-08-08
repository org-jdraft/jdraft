package org.jdraft.runtime;

import java.lang.reflect.*;

import org.jdraft._class;
import org.jdraft._code;

/**
 * Wrapper around an {@link #instance} to simplify access to:
 * <UL>
 *  <LI>calling methods {@link #call(java.lang.String, java.lang.Object...)} 
 *  <LI>accessing fields/properties {@link #get(java.lang.String)}
 *  <LI>updating fields/properties {@link #set(java.lang.String, java.lang.Object)}
 * </UL>
 *
 * @author M. Eric DeFazio
 * @param <I> the instance type
 * @param <C> the code model type
 */
public final class _proxy<I extends Object, C extends _code>  {

    public static final _proxy of( _class _c, Object...ctorArgs){
        return _runtime.of(_c).proxy(_c, ctorArgs);
    }

    /** the proxied instance of a class */
    public I instance;
    
    @Override
    public boolean equals( Object o ){
        if( !( o instanceof _proxy)){
            return this.instance.equals(o);
        }
        _proxy test = (_proxy)o;
        return this.instance.equals( test.instance );
    }

    @Override
    public int hashCode(){
        return instance.hashCode();
    }

    @Override
    public String toString(){
        return instance.toString();
    }

    /**
     * Build and return another new Proxy instance given the constructor args
     *
     * @param ctorArgs constructor args
     * @return a new Proxy instance of the same (instance) type
     */
    public _proxy<I,C> of(Object...ctorArgs ){
        Class c = instance.getClass();
        return new _proxy( _runtime.tryAllCtors(c.getConstructors(), ctorArgs ) );
    }

    /**
     * A proxy around 
     * @param obj 
     */
    public _proxy( I obj ) {
        this.instance = obj;
    }

    /**
     * Gets the classLoader for the instance class loaded
     * (NOTE: it is expected to be an AdhocClassLoader but doesn't HAVE to be)
     * @return the ClassLoader for the proxied class
     */
    public ClassLoader getClassLoader(){
        return this.instance.getClass().getClassLoader();
    }
    
    /**
     * Gets the specific codeModel instance 
     * <UL>
     *     <LI>JavaParser {@link com.github.javaparser.ast.CompilationUnit},
     *     <LI>draft {@link org.jdraft._code}
     * </UL>
     * @return the codemodel from the ClassLoader
     */
    public _class get_class(){
        ClassLoader cl = getClassLoader();
        if( cl instanceof _classLoader){
            _classLoader ahcl = (_classLoader)cl;
            return (_class)ahcl.get_code(this.instance.getClass());
        }
        throw new _runtimeException("The instance class "+ instance.getClass()
            + " was NOT loaded into the Proxy with an AdhocClassLoader, so no "
            + "CodeModel is present");
    }

    /**
     * Gets the _classFile
     * @return
     */
    public _classFile get_classFile(){
        ClassLoader cl = getClassLoader();
        if( cl instanceof _classLoader){
            _classLoader ahcl = (_classLoader)cl;
            return ahcl.get_classFile(this.instance.getClass());
        }
        throw new _runtimeException("The instance class "+ instance.getClass()
                + " was NOT loaded into the Proxy with an Adhoc _classLoader, so no "
                + "_bytecodeFile is present");
    }

    /**
     *
     * @param instance
     * @param m
     * @param args
     * @return
     */
    private static Object call(Object instance, Method m, Object... args ) {
        try {
            return m.invoke( instance, args );
        }
        catch( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex ) {
            throw new _runtimeException("error invoking \"" + m.getName() + "\" on " + instance, ex );
        }
    }

    /**
     * Gets the value for the Field on the instance
     * @param F the reflective field
     * @return the value
     */
    public Object get( Field F ) {
        try {
            return F.get( instance );
        }
        catch( IllegalArgumentException ex ) {
            throw new _runtimeException( "Illegal Arg", ex );
        }
        catch( IllegalAccessException ex ) {
            throw new _runtimeException( "Illegal Access", ex );
        }
    }

    /**
     * Sets the value of the field
     * @param F the reflective Field object
     * @param value the value to set the Field to
     */
    public void set(Field F, Object value){
        try {
            F.set( instance, value );
        }
        catch( IllegalArgumentException ex ) {
            throw new _runtimeException( "Illegal Argument "+value+" for Field " + F, ex );
        }
        catch( IllegalAccessException ex ) {
            throw new _runtimeException( "Illegal Access for Field " + F, ex );
        }
    }

    /**
     * Calls the set(x) method or sets the value of a field with the NAME
     * and return the Proxy
     * @param propertyName the name of the field or property
     * @param value the value to set the field to
     * @return the proxy after modifying the value/calling the set method
     */
    public _proxy set(String propertyName, Object value ){
        try {
            Field F = this.instance.getClass().getField( propertyName );

            if( F != null && (Modifier.isPublic( F.getModifiers() )) ) {
                set( F, value );
                return this;
            }
        }
        catch( _runtimeException | NoSuchFieldException | SecurityException e ) {
            //it's OK if this fails, we MIGHT need to call set...() method
        }

        Method[] ms = this.instance.getClass().getMethods();
        for( int i = 0; i < ms.length; i++ ) {
            if( Modifier.isPublic( ms[ i ].getModifiers() )
                    && ms[ i ].getParameterCount() == 1
                    && (ms[ i ].getName().equalsIgnoreCase( "set" + propertyName )) ) {
                call( instance, ms[ i ], new Object[ ]{value} );
                return this;
            }
        }
        throw new _runtimeException( "Could not find \"" + propertyName + "\"" );
    }

    /**
     * gets (via a reflection on a Field, OR by a get_type()...is()...method)
     * the VALUE of a property on the _new
     *
     * @param propertyName
     * @return
     */
    public Object get( String propertyName ) {
        try{
            Field F = this.instance.getClass().getField( propertyName );

            if( F != null && (Modifier.isPublic( F.getModifiers() )) ) {
                return get( F );
            }
        }catch( NoSuchFieldException e ) {
            //throw new _jDraftException(e);
        }
        //
        Method[] ms = this.instance.getClass().getMethods();
        for( int i = 0; i < ms.length; i++ ) {
            if( Modifier.isPublic( ms[ i ].getModifiers() )
                    && ms[ i ].getParameterCount() == 0
                    && (ms[ i ].getName().equalsIgnoreCase( "get" + propertyName )
                    || ms[ i ].getName().equalsIgnoreCase( "is" + propertyName )) ) {
                return call( instance, ms[ i ], new Object[ 0 ] );
            }
        }
        throw new _runtimeException( "Could not find \"" + propertyName + "\"" );
    }

    /** 
     * call the static main method on the _new with the (optional) string 
     * ARGUMENTS
     * 
     * @param args
     */
    public void main( String...args ){
        if( args.length == 0 ){
            call("main", (Object)new String[0] );
        }
        else{
            call("main", (Object)args );
        }
    }

    /** call a method (_new or static) on this _new
     * <PRE>
     * note: the following works:
     * call( "hashCode");
     * call("hashCode;");
     * call("hashCode()");
     * call("hashCode();");
     *
     * call("withParams", 1, "e");
     * call("withParams()", 1, "e");
     * call("withParams(x, y)", 1, "e");
     * </PRE>
     * @param methodName the name of the method on the instance
     * @param args the method arguments
     * @return
     */
    public Object call(String methodName, Object... args ) {
        methodName = methodName.trim();
        if( methodName.endsWith(";") ){
            methodName = methodName.substring(0, methodName.length() -1);
        }
        int openIndex = methodName.indexOf('(');
        if( openIndex > 0){
            methodName = methodName.substring(0, openIndex );
        }
        Method[] ms = instance.getClass().getDeclaredMethods();
        if( ms.length == 0 ) {
            throw new _runtimeException(
                    "Could not find method with NAME \"" + methodName + "\" on " + instance );
        }
        if( ms.length == 1 ) {
            if( !ms[0].getName().equals(methodName) ){
                throw new _runtimeException( "Could not find method \""+methodName+"\"" );
            }
            return call( instance, ms[ 0 ], args );
        }
        //more than one...
        _runtimeException exception = null;
        for( int i = 0; i < ms.length; i++ ) {
            Method m = ms[ i ];

            if( m.getName().equals( methodName )
                    && (m.getParameterCount() == args.length
                    || (args.length > m.getParameterCount() && m.isVarArgs())) ) {
                try {
                    return call( instance, m, args );
                }
                catch( _runtimeException de ) {
                    exception = de;
                }
            }
        }
        if( exception == null ){
            throw new _runtimeException("could not find method \""+methodName +"\"");
        }
        throw exception;
    }    
}
