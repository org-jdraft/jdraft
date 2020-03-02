package org.jdraft.io;

import com.github.javaparser.ast.CompilationUnit;
import org.jdraft.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * In-memory representations of multiple java source {@link _codeUnit} entities
 * (as apposed to Strings)
 *
 * @see _codeUnit._provider
 * @see _archive to read the .java sources from a jar or zip file (i.e. "spring-2.1.1-sources.jar")
 * @see _path to read the .java sources from a directory (i.e. "Myproject/src/main/java")
 */
public class _sources<_C extends _codeUnit> implements _codeUnit._provider {

    public static _sources of(_codeUnit._provider..._providers ){
        List<_codeUnit> all = new ArrayList<>();
        Arrays.stream(_providers).forEach(_p -> all.addAll(_p.list_code()));
        return of(all);
    }

    public static _sources of(_codeUnit._provider _p ){
        return of(_p.list_code());
    }

    /**
     *
     * @param pathNames
     * @return
     */
    public static _sources of(String... pathNames ){
        Path[] paths = new Path[pathNames.length];
        for(int i=0;i<pathNames.length;i++){
            paths[i] = Paths.get(pathNames[i]);
        }
        return of (paths);
    }

    /**
     * Builds a code cache based on the code present at these paths (if the path is to a .jar, or .zip file
     * it will use an {@link _archive}, otherwise the Path will be a {@link _path})
     * @param paths the paths to include in the code cache
     * @return
     */
    public static _sources of(Path... paths ){
        _codeUnit._provider[] _ps = new _codeUnit._provider[paths.length];

        for(int i=0;i<paths.length;i++){
            //what if it's a .zip or .jar
            if( paths[i].toString().endsWith(".jar") ||  paths[i].toString().endsWith(".zip") ){
                _ps[i] = _archive.of(paths[i]);
            } else if( paths[i].toString().endsWith(".java") ){
                _ps[i] = _sources.of(_java.codeUnit( paths[i]  ) ); //add a cache with a single .java file
            } else{
                _ps[i] = _path.of(paths[i]); //Paths.get(pathNames[i]);
            }
        }
        return of (_ps);
    }

    public static <_C extends _codeUnit> _sources of(Class<?>... clazzes){
        List<_C> cus = new ArrayList<>();
        Arrays.stream(clazzes).forEach(c -> cus.add(_java.type(c)));
        return new _sources( cus );
    }

    public static <_C extends _codeUnit> _sources of(_C... _codeToCache){
        return of(Arrays.stream(_codeToCache).collect(Collectors.toList()));
    }

    public static <_C extends _codeUnit> _sources of(List<_C> _codeToCache){
        return new _sources( _codeToCache );
    }

    List<_C> codeList;

    public _sources(List<_C> _codeToCache ){
        this.codeList = _codeToCache;
    }

    /**
     * find and return the first _code instance of the codeClass that matches the _codeMatchFn
     * (or null if none are found)
     * @param codeClass the code class (i.e. _class, _enum, _packageInfo, _moduleInfo, ...)
     * @param _codeMatchFn matching Function
     * @param <_CC> the code class runtime type
     * @return the first matching or null
     */
    public <_CC extends _codeUnit> _CC first(Class<_CC> codeClass, Predicate<_CC> _codeMatchFn) {
        return first(codeClass, _codeMatchFn, c -> Function.identity() );
    }

    /**
     * find the first _code instance of the codeClass that matches _codeMatchFn, perform some action on it and return
     * it (or return null if not found)
     * @param codeClass the code class (i.e. _class, _enum, _packageInfo, _moduleInfo, ...)
     * @param _codeMatchFn matching Function
     * @param <_CC> the code class runtime type
     * @return the updated instance that was modified by the _codeActionFn
     */
    public <_CC extends _codeUnit> _CC first(Class<_CC> codeClass, Predicate<_CC> _codeMatchFn, Consumer<_CC> _codeActionFn) {
        Optional<_CC> oc = (Optional<_CC>)this.codeList.stream().filter(c -> {
            if(codeClass.isAssignableFrom(c.getClass())){
                return _codeMatchFn.test( (_CC)c );
            }
            return false;
        }).findFirst();
        if( oc.isPresent() ){
            _CC cc = oc.get();
            _codeActionFn.accept( cc );
            return oc.get();
        }
        return null;
    }

    public <_C extends _codeUnit> _C get(Class<_C> _codeClass, Predicate<_C>_codeMatchFn){
        return first(_codeClass, _codeMatchFn);
    }

    public _class get_class(Predicate<_class>_classMatchFn){
        return first(_class.class, _classMatchFn);
    }

    public _interface get_interface(Predicate<_interface>_interfaceMatchFn){
        return first(_interface.class, _interfaceMatchFn);
    }

    public _enum get_enum(Predicate<_enum>_enumMatchFn){
        return first(_enum.class, _enumMatchFn);
    }

    public _annotation get_annotation(Predicate<_annotation>_annotationMatchFn){
        return first(_annotation.class, _annotationMatchFn);
    }

    @Override
    public <_C extends _codeUnit> List<_C> for_code(Class<_C> codeClass, Predicate<_C> _codeMatchFn, Consumer<_C> _codeActionFn) {
        List<_C> acted = new ArrayList<>();
        codeList.forEach( c -> {
            if(codeClass.isAssignableFrom( c.getClass() ) && _codeMatchFn.test( (_C)c) ){
                _codeActionFn.accept( (_C)c );
                acted.add((_C)c);
            }
        });
        return acted;
    }

    /**
     * Adds a bunch of class models (THAT MAY HAVE @macros) to the code cache
     * @param clazz
     * @return
     */
    public _sources add(Class...clazz ){
        Arrays.stream(clazz).forEach( c-> codeList.add( _java.type(c) ) );
        return this;
    }

    public _sources add(_C ..._cd ){
        Arrays.stream(_cd).forEach( cc-> { codeList.add( cc ); } );
        return this;
    }

    public _sources add(CompilationUnit... asts){
        Arrays.stream(asts).forEach( cc-> { codeList.add( (_C)_java.type(cc )); } );
        return this;
    }

    public _sources add(_codeUnit._provider..._providers){
        Arrays.stream(_providers).forEach(_p -> this.codeList.addAll( (List<_C>) _p.list_code()));
        return this;
    }

    /**
     * Build & return a copy of the _code
     * @return
     */
    public _sources<_C> copy(){
        List<_C>copyList = new ArrayList<>();
        this.codeList.forEach(c -> copyList.add( (_C)c.copy() ) );
        return new _sources<_C>(copyList);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("_sources{").append(System.lineSeparator());
        this.codeList.forEach(c -> sb.append("    ").append(c.getFullName() ).append(System.lineSeparator()) );
        sb.append("}");
        return sb.toString();
    }
}
