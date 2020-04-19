package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import org.jdraft.io._batch;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * multiple {@link _codeUnit}s that can optionally be ordered or queried
 */
public class _codeUnits {

    public static _codeUnits of(){
        return new _codeUnits();
    }

    public static _codeUnits of( _batch..._batches){
        _codeUnits _c = new _codeUnits();
        _c.add(_batches);
        return _c;
    }

    public static _codeUnits of(Class...classes){
        _codeUnits _c = new _codeUnits();
        _c.add(classes);
        return _c;
    }

    public static _codeUnits of(_codeUnit..._cus){
        _codeUnits _c = new _codeUnits();
        _c.add(_cus);
        return _c;
    }

    public _codeUnits(){
        this.cache = new ArrayList<>();
    }

    /**
     * A cache of _codeUnits
     */
    public List<_codeUnit> cache;

    /**
     * The number of _codeUnits
     * @return
     */
    public int size(){
        return this.cache.size();
    }

    /**
     *
     * @return
     */
    public List<_codeUnit> list(){
        return cache;
    }

    /**
     *
     * <PRE>
     * List<_type> _ts = _cus.list(_type.class);
     * </PRE>
     * @param _cuClass
     * @param <_CU>
     * @return
     */
    public <_CU extends _codeUnit> List<_CU> list(Class<_CU> _cuClass ){
        return (List<_CU>)this.cache.stream().filter( c-> _cuClass.isAssignableFrom(c.getClass()) ).collect(Collectors.toList());
    }

    /**
     *
     * <PRE>
     * //all types that import FileNotFoundException
     * List<_type> _ts = _cus.list(_type.class, _t.isImports(FileNotFoundException.class));
     * </PRE>
     * @param _cuClass
     * @param <_CU>
     * @return
     */
    public <_CU extends _codeUnit> List<_CU> list(Class<_CU> _cuClass, Predicate<_CU>_matchFn){
        return (List<_CU>)this.cache.stream().filter( c-> {
            if(_cuClass.isAssignableFrom(c.getClass()) ){
                return _matchFn.test( (_CU) c);
            }
            return false;
        } ).collect(Collectors.toList());
    }

    /**
     * Returns a stream of {@link _codeUnit}s
     * @return
     */
    public Stream<_codeUnit> stream(){
        return cache.stream();
    }

    /**
     * Returns a stream of {@link _codeUnit}s of the given _cuClass
     * i.e.
     * <PRE>
     * Stream<_type> _ts = _cus.stream(_type.class);
     * Stream<_class>_cs = _cus.stream(_class.class);
     * </PRE>
     * @return
     */
    public  <_CU extends _codeUnit> Stream<_CU> stream(Class<_CU> _cuClass){
        return list(_cuClass).stream();
    }

    /**
     * Returns a stream of {@link _codeUnit}s of the given _cuClass
     * i.e.
     * <PRE>
     *
     * //return a stream of {@link _type}s that import IOException.class
     * Stream<_type> _ts = _cus.stream(_type.class, _t-> _t.hasImport(IOException.class));
     *
     * //return a stream of {@link _class}es that are final
     * Stream<_class>_cs = _cus.stream(_class.class, _c-> _c.isFinal() );
     * </PRE>
     * @return
     */
    public  <_CU extends _codeUnit> Stream<_CU> stream(Class<_CU> _cuClass, Predicate<_CU>_matchFn){
        return list(_cuClass, _matchFn).stream();
    }

    /**
     * Sort the _codeUnits in the cache
     * @param cuComparator
     * @return
     */
    public _codeUnits sort(Comparator<_codeUnit> cuComparator ){
        Collections.sort( cache, cuComparator );
        return this;
    }

    /**
     * find and return the first _code instance of the codeClass that matches the _codeMatchFn
     * (or null if none are found)
     * @param codeClass the code class (i.e. _class, _enum, _packageInfo, _moduleInfo, ...)
     * @param _codeMatchFn matching Function
     * @param <_CU> the code class runtime type
     * @return the first matching or null
     */
    public <_CU extends _codeUnit> _CU first(Class<_CU> codeClass, Predicate<_CU> _codeMatchFn) {
        return first(codeClass, _codeMatchFn, c -> Function.identity() );
    }

    /**
     * find the first _codeUnit instance of the codeClass that matches _codeMatchFn, perform some action on it and return
     * it (or return null if not found)
     * @param codeClass the code class (i.e. _class, _enum, _packageInfo, _moduleInfo, ...)
     * @param _codeMatchFn matching Function
     * @param <_CU> the code class runtime type
     * @return the updated instance that was modified by the _codeActionFn
     */
    public <_CU extends _codeUnit> _CU first(Class<_CU> codeClass, Predicate<_CU> _codeMatchFn, Consumer<_CU> _codeActionFn) {
        Optional<_CU> oc = (Optional<_CU>)this.cache.stream().filter(c -> {
            if(codeClass.isAssignableFrom(c.getClass())){
                return _codeMatchFn.test( (_CU)c );
            }
            return false;
        }).findFirst();
        if( oc.isPresent() ){
            _CU cc = oc.get();
            _codeActionFn.accept( cc );
            return oc.get();
        }
        return null;
    }

    public <_CU extends _codeUnit> _CU get(Class<_CU> _codeClass, Predicate<_CU>_codeMatchFn){
        return first(_codeClass, _codeMatchFn);
    }

    public _class get_class(String className){
        return first(_class.class, c-> c.getFullName().equals(className) || c.getSimpleName().equals(className));
    }

    public _class get_class(Predicate<_class>_classMatchFn){
        return first(_class.class, _classMatchFn);
    }

    public _interface get_interface(String interfaceName){
        return first(_interface.class, c-> c.getFullName().equals(interfaceName) || c.getSimpleName().equals(interfaceName));
    }

    public _interface get_interface(Predicate<_interface>_interfaceMatchFn){
        return first(_interface.class, _interfaceMatchFn);
    }

    public _enum get_enum(String enumName){
        return first(_enum.class, c-> c.getFullName().equals(enumName) || c.getSimpleName().equals(enumName));
    }

    public _enum get_enum(Predicate<_enum>_enumMatchFn){
        return first(_enum.class, _enumMatchFn);
    }

    public _annotation get_annotation(String annotationName){
        return first(_annotation.class, c-> c.getFullName().equals(annotationName) || c.getSimpleName().equals(annotationName));
    }

    public _annotation get_annotation(Predicate<_annotation>_annotationMatchFn){
        return first(_annotation.class, _annotationMatchFn);
    }

    public List<_codeUnit> forEach(Consumer<_codeUnit> _codeActionFn) {
        return forEach(c->true, _codeActionFn);
    }

    public <_CU extends _codeUnit> List<_CU> forEach(Class<_CU> _cuClass, Consumer<_CU> _codeActionFn) {
        return forEach(_cuClass, c->true, _codeActionFn);
    }

    public List<_codeUnit> forEach(Predicate<_codeUnit> _codeMatchFn, Consumer<_codeUnit> _codeActionFn) {
        return forEach(_codeUnit.class, _codeMatchFn, _codeActionFn);
    }

    public <_CU extends _codeUnit> List<_CU> forEach(Class<_CU> codeClass, Predicate<_CU> _codeMatchFn, Consumer<_CU> _codeActionFn) {
        List<_CU> acted = new ArrayList<>();
        cache.forEach(c -> {
            if(codeClass.isAssignableFrom( c.getClass() ) && _codeMatchFn.test( (_CU)c) ){
                _codeActionFn.accept( (_CU)c );
                acted.add((_CU)c);
            }
        });
        return acted;
    }

    /**
     * Adds a bunch of _ models (THAT MAY HAVE @macros) to the code cache
     * @param clazzes
     * @return
     */
    public _codeUnits add(Class...clazzes ){
        Arrays.stream(clazzes).forEach(c-> cache.add( _type.of(c) ) );
        return this;
    }

    public _codeUnits add(_codeUnit ..._cus ){
        Arrays.stream(_cus).forEach( cc-> { cache.add( cc ); } );
        return this;
    }

    public _codeUnits add(CompilationUnit... asts){
        Arrays.stream(asts).forEach( cc-> { cache.add( _codeUnit.of(cc )); } );
        return this;
    }

    public _codeUnits add(_batch..._batches){
        Arrays.stream( _batches).forEach(b-> add(b.load()) );
        return this;
    }

    public _codeUnits add(_codeUnits...cuss){
        Arrays.stream( cuss).forEach( cus -> add(cus.list()) );
        return this;
    }

    public _codeUnits add( List<_codeUnit> cus){
        this.cache.addAll(cus);
        return this;
    }

    /**
     * Removes
     * @param fullyQualifiedClassNames
     * @return
     */
    public _codeUnits remove(String...fullyQualifiedClassNames){
        Arrays.stream(fullyQualifiedClassNames).forEach( cn ->
                this.cache.removeIf(t-> t.getFullName().equals(cn)) );
        return this;
    }

    /**
     *
     * @param _matchFn
     * @return
     */
    public _codeUnits remove(Predicate<_codeUnit> _matchFn){
        this.cache.removeIf(_matchFn);
        return this;
    }

    /**
     * Build & return a copy of the sources
     * @return
     */
    public _codeUnits copy(){
        List<_codeUnit>copyList = new ArrayList<>();
        this.cache.forEach(c -> copyList.add( c.copy() ) );
        _codeUnits srcs = new _codeUnits();
        srcs.cache = copyList;
        return srcs;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("_codeUnits{").append(System.lineSeparator());
        this.cache.forEach(c -> sb.append("    ").append(c.getFullName() ).append(System.lineSeparator()) );
        sb.append("}");
        return sb.toString();
    }
}
