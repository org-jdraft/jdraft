package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Representation of the java source code of a list of imports ({@link _import}s/{@link ImportDeclaration}s)
 * i.e. <PRE>
 * import java.util.*;
 * import java.net.URI;
 * import static java.lang.System.*;
 * </PRE>
 *
 * @see _import representation of a single import declaration
 * @author Eric
 */
public final class _imports implements _tree._set<ImportDeclaration, _import, _imports> {

    public static final Function<String, _imports> PARSER = s-> _imports.of(s);

    public static _imports of(Class...clazzes){
        CompilationUnit cu = new CompilationUnit();
        Arrays.stream(clazzes).forEach(i -> cu.addImport(i));
        return of( cu );
    }

    public static _imports of(String... imports) {
        CompilationUnit cu = new CompilationUnit();
        Arrays.stream(imports).forEach(i -> {
            if( i.endsWith(";") ){
                i = i.substring(0,i.length()-1);
            }
            if(i.startsWith("import ")){
                cu.addImport(i.substring("import ".length()));
            } else {
                cu.addImport(i);
            }
        });
        return of( cu );
    }

    public static _imports of(){
        return new _imports(new CompilationUnit());
    }

    public static _imports of( CompilationUnit cu ){
        return new _imports(cu);
    }

    public static _feature._many<_imports, _import> IMPORTS = new _feature._many<>(_imports.class, _import.class,
            _feature._id.IMPORTS, _feature._id.IMPORT,
            a->a.list(),
            (_imports a, List<_import> es)-> a.set(es), PARSER, s-> _import.of(s))
            .setOrdered(false);

    public static _feature._features<_imports> FEATURES = _feature._features.of(_imports.class,  PARSER, IMPORTS);

    public CompilationUnit astCompilationUnit;

    public _imports( CompilationUnit astCu ){
        this.astCompilationUnit = astCu;
    }

    public _feature._features<_imports> features(){
        return FEATURES;
    }

    @Override
    public _imports copy() {
        return _imports.of(astCompilationUnit);
    }

    @Override
    public NodeList<ImportDeclaration> listAstElements() {
        return this.astCompilationUnit.getImports();
    }

    /**
     * Top level Classes in the same package are "implied imports"
     * @param clazz the class to check
     * @return true if this import is implied
     */
    public boolean isImpliedImport(Class clazz){
        return isImpliedImport(clazz.getCanonicalName());
    }

    /**
     *
     * @param _t
     * @return
     */
    public boolean isImpliedImport( _type _t ){
        return _t.isTopLevel() && isImpliedImport( _t.getFullName() );
    }

    /**
     * IF the
     * @param className the name of the class
     * @return
     */
    public boolean isImpliedImport(String className){
        if( this.astCompilationUnit == null ){
            return false;
        }
        if(astCompilationUnit.getPackageDeclaration().isPresent()){

            String pkgName = astCompilationUnit.getPackageDeclaration().get().getNameAsString();
            if( className.startsWith(pkgName) ){
                String left = className.substring(pkgName.length() + 1 );
                return !left.contains(".");
            }
            return false;
        }
        //its in the base package, so anything in the base package is fair game
        return !className.contains(".");
    }

    public boolean hasImport(Class clazz) {
        return isImpliedImport(clazz) ||
                (astCompilationUnit != null &&
                        astCompilationUnit.getImports().stream().anyMatch(i -> {
                            return _import.hasImport(i, clazz);
                        }));
    }

    public boolean hasImport(String className) {
        return isImpliedImport(className) ||
                (astCompilationUnit != null &&
                        astCompilationUnit.getImports().stream().anyMatch(i -> {
                            return _import.hasImport(i, className);
                        }));
    }

    public boolean hasImports( Class...classes ){
        return Arrays.stream(classes).allMatch(i-> _imports.this.hasImport(i) );
    }

    public boolean hasImports( String...imports  ){
        return Arrays.stream(imports).allMatch(i-> _imports.this.hasImport(i) );
    }

    public boolean hasImports( _type..._ts ){
        return Arrays.stream(_ts).allMatch( i-> hasImport(i) );
    }

    public boolean hasImport(Method m){
        if( this.astCompilationUnit == null ){
            return false;
        }
        return astCompilationUnit.getImports().stream().anyMatch(i -> {
            return _import.hasImport(i, m);
        });
    }

    public boolean hasImport( _type _t){
        return _imports.this.hasImport( _t.getFullName() );
    }

    public List<_import> list(){
        List<_import> l = new ArrayList<>();
        if( this.astCompilationUnit == null ){
            return l;
        }
        astCompilationUnit.getImports().forEach(i -> l.add( _import.of(i)));
        return l;
    }

    public List<_import> list( Predicate<_import> _importMatchFn ){
        return list().stream().filter(_importMatchFn).collect(Collectors.toList());
    }

    public _imports clear(){
        this.astCompilationUnit.getImports().clear();
        return this;
    }

    public _imports remove( _type _t){
        return remove( _t.getFullName() );
    }

    public _imports remove( _type... _ts){
        Arrays.stream(_ts).forEach(t-> remove(t));
        return this;
    }

    public _imports remove(_import toRemove){
        return remove( toRemove.astId );
    }

    public _imports remove( Class clazz ){
        if( this.astCompilationUnit == null ){
            return this;
        }
        List<ImportDeclaration> toRemove = new ArrayList<>();
        this.astCompilationUnit.getImports().forEach(i -> {
            if( _import.is(i, clazz) ) {
                toRemove.add( i );
            }
        });
        toRemove.forEach(i -> this.astCompilationUnit.getImports().remove(i));
        return this;
    }

    public _imports remove( String importDeclaration ){
        try{
            return remove(Ast.importDeclaration(importDeclaration));
        }catch(Exception e){
            return this;
        }
    }

    public _imports remove( String...importDeclarations ){
        Arrays.stream(importDeclarations).forEach( i -> remove(i) );
        return this;
    }

    /**
     * removeIn imports by classes
     * @param clazzes
     * @return
     */
    public _imports remove( Class...clazzes ){
        Arrays.stream( clazzes ).forEach( r -> remove( r) );
        return this;
    }

    @Override
    public int hashCode(){
        Set<Integer> is = new HashSet<>();
        if( this.astCompilationUnit == null ){
            return 0;
        }
        this.astCompilationUnit.getImports().forEach( i -> is.add( i.hashCode()));
        return is.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if( this.astCompilationUnit == null ){
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final _imports other = (_imports) obj;
        Set<ImportDeclaration> t = new HashSet<>();
        Set<ImportDeclaration> o = new HashSet<>();

        this.astCompilationUnit.getImports().forEach( i -> t.add( i ));
        other.astCompilationUnit.getImports().forEach( i -> o.add( i ));

        return Objects.equals(t, o);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        this.astCompilationUnit.getImports().forEach(i -> sb.append( i.toString() ) );
        return sb.toString();
    }

    /**
     * Tools for comparing sets of Imports
     * normally this code is used internally, because it uses the JavaParser API
     * and added to this inner class because it would be confusing putting it on the public
     * API of the top level _imports
     */
    public static class Compare{

        public static boolean importsEqual(TypeDeclaration left, TypeDeclaration right) {
            if (left.isTopLevelType()) {
                if (right.isTopLevelType()) {
                    //both left and right are compilationUnits
                    return importsEqual(left.findCompilationUnit().get(), right.findCompilationUnit().get());
                }
                return left.findCompilationUnit().get().getImports().isEmpty();
            }
            if (!right.isTopLevelType()) {
                return true;
            }
            return right.findCompilationUnit().isPresent()
                    && right.findCompilationUnit().get().getImports().isEmpty();
        }

        public static boolean importsEqual(CompilationUnit left, CompilationUnit right) {
            if (left == null) {
                return right == null;
            }
            if (right == null) {
                return false;
            }
            if (left == right) {
                return true;
            }
            return importsEqual(left.getImports(), right.getImports());
        }

        public static boolean importsEqual(List<ImportDeclaration> li1, List<ImportDeclaration> li2) {
            Set<ImportDeclaration> ti = new HashSet<>();
            Set<ImportDeclaration> to = new HashSet<>();
            ti.addAll(li1);
            to.addAll(li2);
            if (!Objects.equals(ti, to)) {
                return false;
            }
            return true;
        }

        public static int importsHash(TypeDeclaration td) {
            if (!td.isTopLevelType()) {
                return 0;
            }
            if (!td.findCompilationUnit().isPresent()) {
                return 0;
            }
            return importsHash(td.findCompilationUnit().get());
        }

        public static int importsHash(CompilationUnit cu) {
            Set<Integer> is = new HashSet<>();
            cu.getImports().forEach(i -> is.add(i.hashCode()));
            return is.hashCode();
        }
    }
}