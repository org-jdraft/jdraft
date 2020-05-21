package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import org.jdraft.io._batch;
import org.jdraft.io._io;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jdraft.io._io.ORIGIN_KEY;

/**
 * A Forest containing "top level" {@link _codeUnit}s that can be sorted, queried & modified in place
 *
 * NOTE: each underlying {@link _codeUnit} MAY have a reference to the {@link _project}
 * that can be accessed by {@link _codeUnit#getParentProject()} and this reference is stored
 * within the {@link CompilationUnit#getData(DataKey)} as the {@link #PARENT_PROJECT_KEY}
 */
public class _project {

    /**
     * Data to be put into the CompilationUnit useful for maintaining a reference to the parent _project
     * (so we can travers "UP" an AST Tree to a "forest" of ASTs for "resolving" or other cross referenceing
     * tasks and relationships between classes (superclasses, implemented classes, etc.)
     * @param <_project> the underlying project instance that is the parent of a compilationUnit
     *
     * @see com.github.javaparser.ast.CompilationUnit#setData
     * @see com.github.javaparser.ast.CompilationUnit#getData
     */
    public static DataKey<_project> PARENT_PROJECT_KEY = new DataKey<_project>(){ };

    /**
     * Returns the origin of the project (i.e. where the source java files originated from)
     * (could be null) this is an extension of the {@link CompilationUnit.Storage} which only allows
     * the storage to be a path on the local file system
     *
     * @return
     */
    public _io._origin getOrigin(){
        return this.origin;
    }

    public static _project of(){
        return new _project();
    }

    public static _project of(_batch..._batches){
        _project _p = new _project();
        _p.add(_batches);
        return _p;
    }

    public static _project of(Class...classes){
        _project _p = new _project();
        for(int i=0;i<classes.length;i++){
            _p.add( (_codeUnit) _type.of(classes[i]) );
        }
        //Arrays.stream(classes).forEach( c-> _p.add(_type.of(c)));
        return _p;
    }

    public static _project of(_project..._projs){
        _project _p = new _project();
        _p.add(_projs);
        return _p;
    }

    public static _project of(_codeUnit..._cus){
        _project _p = new _project();
        _p.add(_cus);
        return _p;
    }

    public _project(){
        this.cache = new ArrayList<>();
    }

    /** A cache of _codeUnits*/
    public List<_codeUnit> cache;

    /** The origin where this project came from (it could be null if the project is peiced together) */
    public _io._origin origin;

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
     * @param _cuMatchFn
     * @return
     */
    public List<_codeUnit> list(Predicate<_codeUnit> _cuMatchFn){
        return this.cache.stream().filter( _cuMatchFn ).collect(Collectors.toList());
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
     * Set the origin on all AST Compilation Units
     * @param origin
     * @return
     */
    public _project setOrigin(_io._origin origin){
        this.origin = origin;
        forEach(c-> c.astCompilationUnit().setData(ORIGIN_KEY, origin));
        return this;
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
    public _project sort(Comparator<_codeUnit> cuComparator ){
        Collections.sort( cache, cuComparator );
        return this;
    }

    /**
     * Returns the first of top level _codeUnit type or null if not found
     * @param codeClass the type of codeUnit
     * @param <_CU> the underlying type
     * @return the first one (or null if there are none)
     */
    public <_CU extends _codeUnit> _CU first(Class<_CU> codeClass) {
        return first(codeClass, _c->true, c -> Function.identity() );
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

    public _class getClass(String className){
        return first(_class.class, c-> c.getFullName().equals(className) || c.getSimpleName().equals(className));
    }

    public _class getClass(Predicate<_class>_classMatchFn){
        return first(_class.class, _classMatchFn);
    }

    public _interface getInterface(String interfaceName){
        return first(_interface.class, c-> c.getFullName().equals(interfaceName) || c.getSimpleName().equals(interfaceName));
    }

    public _interface getInterface(Predicate<_interface>_interfaceMatchFn){
        return first(_interface.class, _interfaceMatchFn);
    }

    public _enum getEnum(String enumName){
        return first(_enum.class, c-> c.getFullName().equals(enumName) || c.getSimpleName().equals(enumName));
    }

    public _enum getEnum(Predicate<_enum>_enumMatchFn){
        return first(_enum.class, _enumMatchFn);
    }

    public _annotation getAnnotation(String annotationName){
        return first(_annotation.class, c-> c.getFullName().equals(annotationName) || c.getSimpleName().equals(annotationName));
    }

    public _annotation getAnnotation(Predicate<_annotation>_annotationMatchFn){
        return first(_annotation.class, _annotationMatchFn);
    }

    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _annotationActionFn
     * @return the modified project
     */
    public _project toAnnotations(Consumer<_annotation>_annotationActionFn){
        forAnnotations(_annotationActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _annotationMatchFn
     * @param _annotationActionFn
     * @return
     */
    public _project toAnnotations(Predicate<_annotation> _annotationMatchFn, Consumer<_annotation>_annotationActionFn){
        forAnnotations(_annotationMatchFn, _annotationActionFn);
        return this;
    }

    public List<_annotation> forAnnotations(Consumer<_annotation>_annotationActionFn){
        return forAnnotations(m->true, _annotationActionFn);
    }

    public List<_annotation> forAnnotations(Predicate<_annotation> _annotationMatchFn, Consumer<_annotation>_annotationActionFn){
        List<_annotation> _ms = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof AnnotationDeclaration)
                    .forEach(md-> {
                        _annotation _e = _annotation.of( (AnnotationDeclaration)md );
                        if( _annotationMatchFn.test(_e) ){
                            _annotationActionFn.accept(_e);
                            _ms.add(_e);
                        }
                    } );
        });
        return _ms;
    }


    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _enumActionFn
     * @return the modified project
     */
    public _project toEnums(Consumer<_enum>_enumActionFn){
        forEnums(_enumActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _enumMatchFn
     * @param _enumActionFn
     * @return
     */
    public _project toEnums(Predicate<_enum> _enumMatchFn, Consumer<_enum>_enumActionFn){
        forEnums(_enumMatchFn, _enumActionFn);
        return this;
    }

    public List<_enum> forEnums(Consumer<_enum>_enumActionFn){
        return forEnums(m->true, _enumActionFn);
    }

    public List<_enum> forEnums(Predicate<_enum> _enumMatchFn, Consumer<_enum>_enumActionFn){
        List<_enum> _ms = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof EnumDeclaration)
                    .forEach(md-> {
                        _enum _e = _enum.of( (EnumDeclaration)md );
                        if( _enumMatchFn.test(_e) ){
                            _enumActionFn.accept(_e);
                            _ms.add(_e);
                        }
                    } );
        });
        return _ms;
    }

    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _interfaceActionFn
     * @return the modified project
     */
    public _project toInterfaces(Consumer<_interface>_interfaceActionFn){
        forInterfaces(_interfaceActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _interfaceMatchFn
     * @param _interfaceActionFn
     * @return
     */
    public _project toInterfaces(Predicate<_interface> _interfaceMatchFn, Consumer<_interface>_interfaceActionFn){
        forInterfaces(_interfaceMatchFn, _interfaceActionFn);
        return this;
    }

    public List<_interface> forInterfaces(Consumer<_interface>_interfaceActionFn){
        return forInterfaces(m->true, _interfaceActionFn);
    }

    public List<_interface> forInterfaces(Predicate<_interface> _interfaceMatchFn, Consumer<_interface>_interfaceActionFn){
        List<_interface> _ms = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) n).asClassOrInterfaceDeclaration().isInterface())
                    .forEach(md-> {
                        _interface _i = _interface.of( (ClassOrInterfaceDeclaration)md );
                        if( _interfaceMatchFn.test(_i) ){
                            _interfaceActionFn.accept(_i);
                            _ms.add(_i);
                        }
                    } );
        });
        return _ms;
    }

    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _classActionFn
     * @return the modified project
     */
    public _project toClasses(Consumer<_class>_classActionFn){
        forClasses(_classActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _classMatchFn
     * @param _classActionFn
     * @return
     */
    public _project toClasses(Predicate<_class> _classMatchFn, Consumer<_class>_classActionFn){
        forClasses(_classMatchFn, _classActionFn);
        return this;
    }

    public List<_class> forClasses(Consumer<_class>_classActionFn){
        return forClasses(m->true, _classActionFn);
    }

    public List<_class> forClasses(Predicate<_class> _classMatchFn, Consumer<_class>_classActionFn){
        List<_class> _ms = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) n).asClassOrInterfaceDeclaration().isInterface())
                    .forEach(md-> {
                        _class _c = _class.of( (ClassOrInterfaceDeclaration)md );
                        if( _classMatchFn.test(_c) ){
                            _classActionFn.accept(_c);
                            _ms.add(_c);
                        }
                    } );
        });
        return _ms;
    }

    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _methodActionFn
     * @return the modified project
     */
    public _project toMethods(Consumer<_method>_methodActionFn){
        forMethods(_methodActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _methodMatchFn
     * @param _methodActionFn
     * @return
     */
    public _project toMethods(Predicate<_method> _methodMatchFn, Consumer<_method>_methodActionFn){
        forMethods(_methodMatchFn, _methodActionFn);
        return this;
    }

    public List<_method> forMethods(Consumer<_method>_methodActionFn){
        return forMethods(m->true, _methodActionFn);
    }

    public List<_method> forMethods(Predicate<_method> _methodMatchFn, Consumer<_method>_methodActionFn){
        List<_method> _ms = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof MethodDeclaration)
                    .forEach(md-> {
                        _method _m = _method.of( (MethodDeclaration)md );
                        if( _methodMatchFn.test(_m) ){
                            _methodActionFn.accept(_m);
                            _ms.add(_m);
                        }
                    } );
        });
        return _ms;
    }

    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _initBlockActionFn
     * @return the modified project
     */
    public _project toInitBlocks(Consumer<_initBlock>_initBlockActionFn){
        forInitBlocks(_initBlockActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _initBlockMatchFn
     * @param _initBlockActionFn
     * @return
     */
    public _project toInitBlocks(Predicate<_initBlock> _initBlockMatchFn, Consumer<_initBlock>_initBlockActionFn){
        forInitBlocks(_initBlockMatchFn, _initBlockActionFn);
        return this;
    }

    public List<_initBlock> forInitBlocks(Consumer<_initBlock>_initBlockActionFn){
        return forInitBlocks(m->true, _initBlockActionFn);
    }

    public List<_initBlock> forInitBlocks(Predicate<_initBlock> _initBlockMatchFn, Consumer<_initBlock>_initBlockActionFn){
        List<_initBlock> _cs = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof InitializerDeclaration)
                    .forEach(cd-> {
                        _initBlock _c = _initBlock.of( (InitializerDeclaration)cd );
                        if( _initBlockMatchFn.test(_c) ){
                            _initBlockActionFn.accept(_c);
                            _cs.add(_c);
                        }
                    } );
        });
        return _cs;
    }

    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _constructorActionFn
     * @return the modified project
     */
    public _project toConstructors(Consumer<_constructor>_constructorActionFn){
        forConstructors(_constructorActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _constructorMatchFn
     * @param _constructorActionFn
     * @return
     */
    public _project toConstructors(Predicate<_constructor> _constructorMatchFn, Consumer<_constructor>_constructorActionFn){
        forConstructors(_constructorMatchFn, _constructorActionFn);
        return this;
    }

    public List<_constructor> forConstructors(Consumer<_constructor>_constructorActionFn){
        return forConstructors(m->true, _constructorActionFn);
    }

    public List<_constructor> forConstructors(Predicate<_constructor> _constructorMatchFn, Consumer<_constructor>_constructorActionFn){
        List<_constructor> _cs = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof ConstructorDeclaration)
                    .forEach(cd-> {
                        _constructor _c = _constructor.of( (ConstructorDeclaration)cd );
                        if( _constructorMatchFn.test(_c) ){
                            _constructorActionFn.accept(_c);
                            _cs.add(_c);
                        }
                    } );
        });
        return _cs;
    }


    /**
     * Apply this consumer to all methods and return the modified _project
     * @param _fieldActionFn
     * @return the modified project
     */
    public _project toFields(Consumer<_field>_fieldActionFn){
        forFields(_fieldActionFn);
        return this;
    }

    /**
     * Apply this consumer to all methods and return the modified project
     * @param _fieldMatchFn
     * @param _fieldActionFn
     * @return
     */
    public _project toFields(Predicate<_field> _fieldMatchFn, Consumer<_field>_fieldActionFn){
        forFields(_fieldMatchFn, _fieldActionFn);
        return this;
    }

    public List<_field> forFields(Consumer<_field>_fieldActionFn){
        return forFields(m->true, _fieldActionFn);
    }

    public List<_field> forFields(Predicate<_field> _fieldMatchFn, Consumer<_field>_fieldActionFn){
        List<_field> _fs = new ArrayList<>();
        this.cache.forEach(_cu -> {
            CompilationUnit cu = _cu.astCompilationUnit();
            cu.stream(Node.TreeTraversal.POSTORDER).filter(n -> n instanceof FieldDeclaration)
                    .forEach(fd-> {
                        ((FieldDeclaration) fd).getVariables().forEach(v -> {
                            _field _f = _field.of((VariableDeclarator) v);
                            if (_fieldMatchFn.test(_f)) {
                                _fieldActionFn.accept(_f);
                                _fs.add(_f);
                            }
                        });
                    });
                    });
        return _fs;
    }

    public List<_type> forTypes(Predicate<_type> _typeMatchFn, Consumer<_type> _codeActionFn) {
        return forEach(_type.class, _typeMatchFn, _codeActionFn);
    }

    public List<_type> forTypes(Consumer<_type> _codeActionFn) {
        return forEach(_type.class, c->true, _codeActionFn);
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
    public _project add(Class...clazzes ){
        Arrays.stream(clazzes).forEach(c-> add( _type.of(c) ) );
        return this;
    }

    public _project add(_codeUnit ..._cus ){
        Arrays.stream(_cus).forEach( cc-> {
            cache.add( cc );
            cc.astCompilationUnit().setData(PARENT_PROJECT_KEY, this);
        } );
        return this;
    }

    public _project add(CompilationUnit... asts){
        Arrays.stream(asts).forEach( cc-> { add( _codeUnit.of(cc )); } );
        return this;
    }

    public _project add(_batch..._batches){
        Arrays.stream( _batches).forEach(b-> add(b.load()) );
        return this;
    }

    public _project add(_project...cuss){
        Arrays.stream( cuss).forEach( cus -> {
            add(cus.list());
        } );
        return this;
    }

    public _project add(List<_codeUnit> cus){
        this.cache.addAll(cus);
        cus.forEach( cu-> cu.astCompilationUnit().setData(PARENT_PROJECT_KEY, this));
        return this;
    }

    /**
     * Removes
     * @param fullyQualifiedClassNames
     * @return
     */
    public _project remove(String...fullyQualifiedClassNames){
        Arrays.stream(fullyQualifiedClassNames).forEach( cn ->
                this.cache.removeIf(t-> t.getFullName().equals(cn)) );
        return this;
    }

    /**
     *
     * @param _matchFn
     * @return
     */
    public _project remove(Predicate<_codeUnit> _matchFn){
        this.cache.removeIf(_matchFn);
        return this;
    }

    /**
     * Build & return a copy of the sources
     * @return
     */
    public _project copy(){
        List<_codeUnit>copyList = new ArrayList<>();
        this.cache.forEach(c -> copyList.add( c.copy() ) );
        _project srcs = new _project();
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
