package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;

import java.lang.reflect.Method;
import java.util.*;
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
public class _imports implements _java._nodeList<ImportDeclaration, _import, _imports> {

    public static _imports of(){
        return new _imports(new CompilationUnit());
    }

    public static _imports of( CompilationUnit cu ){
        return new _imports(cu);
    }

    public CompilationUnit astCompilationUnit;

    public _imports( CompilationUnit astCu ){
        this.astCompilationUnit = astCu;
    }

    @Override
    public _imports copy() {
        return _imports.of(astCompilationUnit);
    }


    @Override
    public List<ImportDeclaration> listAstElements() {
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
}