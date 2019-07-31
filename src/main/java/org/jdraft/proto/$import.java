package org.jdraft.proto;

import org.jdraft._java;
import org.jdraft._code;
import org.jdraft._type;
import org.jdraft._import;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import org.jdraft.*;
import org.jdraft._node;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * prototype for an _import declaration on a Java top level _type 
 *
 */
public final class $import
    implements Template<_import>, $proto<_import> {
    
    /**
     * $proto representing any import
     * @return 
     */
    public static $import any(){
        return of();
    }
    
    /**
     * Match ANY import
     * @return 
     */
    public static $import of(){
        return new $import( t-> true );        
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $import of( String pattern){
        _import _i = _import.of(pattern );
        return new $import( _i  );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $import of( Predicate<_import> constraint ){
        return new $import( constraint );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $import of( String pattern, Predicate<_import> constraint){
        _import _i = _import.of(pattern );
        return new $import( _i ).addConstraint(constraint);
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    public static $import of( Class clazz ){
        _import _i = _import.of( clazz );
        return new $import( _i  );
    }
    
    /**
     *      
     * @param clazz
     * @param isStatic is this a static import
     * @param isWildcard is a Wildcard import
     * @return 
     */
    public static $import of( Class clazz, boolean isStatic, boolean isWildcard){
        _import _i = _import.of( clazz ).setStatic(isStatic).setWildcard(isWildcard);
        return new $import( _i  );
    }
    
    /**
     * 
     * @param clazz
     * @param constraint
     * @return 
     */
    public static $import of( Class clazz, Predicate<_import> constraint){
        _import _i = _import.of( clazz );
        return new $import( _i  ).addConstraint(constraint);
    }
    
    /**
     * 
     * @param _proto
     * @return 
     */
    public static $import of( _import _proto){
        return new $import( _proto  );
    }

    /**
     * 
     * @param _proto
     * @param constraint
     * @return 
     */
    public static $import of( _import _proto, Predicate<_import> constraint){
        return new $import( _proto ).addConstraint(constraint);
    }
    
    public Predicate<_import> constraint = t-> true;
        
    public Stencil importPattern;
    
    /** 
     * Only match on static imports & compose a static import 
     * NOTE: if isStatic is False, this will still match static imports
     */
    public final Boolean isStatic;
    
    /** 
     * Only match on wildcard (.*) imports & compose a wildcard (.*) import 
     * NOTE: if isWildcard is false, this will still match wildcard imports
     */
    public final Boolean isWildcard;
    
    private $import(_import proto ){
        if( proto.isWildcard() ){
            this.importPattern = Stencil.of( proto.getName()+".*" );
        } else{
            this.importPattern = Stencil.of( proto.getName() );
        }
        this.isStatic = proto.isStatic();
        this.isWildcard = proto.isWildcard();        
    }

    private $import( Predicate<_import> constraint ){
        this.importPattern = Stencil.of("$any$");
        this.constraint = constraint;
        this.isStatic = false;
        this.isWildcard = false;   
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $import addConstraint( Predicate<_import>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * 
     * @param imprt
     * @return 
     */
    public boolean matches( String imprt ){
        return matches(_import.of(imprt) );
    }

    /**
     * 
     * @param astImport
     * @return 
     */
    public boolean matches( ImportDeclaration astImport ){
        return select(astImport ) != null;
    }

    /**
     * 
     * @param _i
     * @return 
     */
    public boolean matches( _import _i){
        return select( _i ) != null;
    }

    /**
     * 
     * @param _i
     * @return 
     */
    public Select select(_import _i){
        if( this.constraint.test(_i)){
            //if this $import EXPECTS static or WIlcard imports and the             
            if( this.isStatic && !_i.isStatic() || this.isWildcard && ! _i.isWildcard() ){
                return null;
            }                        
            String name = _i.getName(); 
            if( _i.isWildcard() ){
                name += ".*";
            }            
            Tokens ts = importPattern.deconstruct( name );
            if( ts != null ){
                return new Select(_i, $args.of(ts) );
            }            
        }
        return null;
    }

    /**
     * 
     * @param astImport
     * @return 
     */
    public Select select(ImportDeclaration astImport){
        return select(_import.of(astImport) );        
    }
    
    @Override
    public String toString() {
        return "($import) : \"" +this.importPattern + "\"";
    }

    @Override
    public _import construct(Translator translator, Map<String, Object> keyValues) {        
        _import _ii = _import.of(importPattern.construct(translator, keyValues));
        _ii.setStatic(this.isStatic);
        _ii.setWildcard(this.isWildcard);
        return _ii;
    }
    
    @Override
    public $import $(String target, String $Name) {
        this.importPattern = this.importPattern.$(target, $Name);
        return this;
    }

    /**
     * Hardcode (one or more) parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param hardcodedKeyValues the key parameter NAME and VALUE to hardcode
     * @return the modified Stencil
     */
    public $import hardcode$( Tokens hardcodedKeyValues ) {
        return hardcode$(Translator.DEFAULT_TRANSLATOR, hardcodedKeyValues );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $import hardcode$( Object... keyValues ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $import hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $import hardcode$( Translator translator, Tokens kvs ) {
        this.importPattern = this.importPattern.hardcode$(translator,kvs);
        return this;
    }

    @Override
    public List<String> list$() {
        return this.importPattern.list$();
    }

    @Override
    public List<String> list$Normalized() {
        return this.importPattern.list$Normalized();
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @param _importMatchFn
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public _import firstIn( Node astNode, Predicate<_import> _importMatchFn){
        if( astNode.findCompilationUnit().isPresent() ){
            Optional<ImportDeclaration> f = astNode.findCompilationUnit().get()
                .findFirst(ImportDeclaration.class, s ->{
                    Select sel = select(s);
                    return sel != null && _importMatchFn.test(sel._i);
                });         
            if( f.isPresent()){
                return _import.of(f.get());
            }
        }
        return null;
    }
    
    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        if( astNode.findCompilationUnit().isPresent() ){
            Optional<ImportDeclaration> f = astNode.findCompilationUnit().get()
                    .findFirst(ImportDeclaration.class, s -> this.matches(s) );         
            if( f.isPresent()){
                return select(f.get());
            }         
        }
        return null;
    }
    
    /**
     * Returns the first _import that matches the pattern and constraint
     * @param _n the _java node
     * @param selectConstraint
     * @return  the first _import that matches (or null if none found)
     */
    public Select selectFirstIn( _java _n, Predicate<Select> selectConstraint ){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return selectFirstIn(((_code) _n).astCompilationUnit(), selectConstraint);
            } else{
                return selectFirstIn(((_type) _n).ast(), selectConstraint);
            }
        }
        return selectFirstIn(((_node) _n).ast(), selectConstraint);       
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first _import that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint ){
        if( astNode.findCompilationUnit().isPresent() ){
            Optional<ImportDeclaration> f = astNode.findCompilationUnit().get()
                    .findFirst(ImportDeclaration.class, s -> {
                        Select sel = this.select(s); 
                        return sel != null && selectConstraint.test(sel);
                    });         
            if( f.isPresent()){
                return select(f.get());
            }         
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn( Node astNode ){
        List<Select>sts = new ArrayList<>();
        if(astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(ImportDeclaration.class, e-> {
                Select s = select( e );
                if( s != null ){
                    sts.add( s);
                }
            });
        }
        return sts;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( Class clazz, Predicate<Select> selectConstraint ){
        return listSelectedIn(_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( Node astNode, Predicate<Select> selectConstraint ){
        List<Select>sts = new ArrayList<>();
        if(astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(ImportDeclaration.class, e-> {
                Select s = select( e );
                if( s != null && selectConstraint.test(s)){
                    sts.add( s);
                }
            });
        }
        return sts;
    }

    /**
     * 
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( _java _n, Predicate<Select> selectConstraint ){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return listSelectedIn(((_code) _n).astCompilationUnit(), selectConstraint);
            } else{
                return listSelectedIn(((_type) _n).ast(), selectConstraint);
            }
        }
        return listSelectedIn(((_node) _n).ast(), selectConstraint);
            
    }
    
    /**
     * 
     * @param clazz
     * @param importClass
     * @return 
     */
    public _type replaceIn(Class clazz, Class importClass){
        return replaceIn( _java.type(clazz), importClass);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param importClass
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, Class importClass){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), importClass);
                return _n;
            } else{
                replaceIn(((_type) _n).ast(), importClass);
                return _n;
            }
        }
        replaceIn(((_node) _n).ast(), importClass);
        return _n;
    }
    
    /**
     * 
     * @param clazz
     * @param importDecl
     * @return 
     */
    public _type replaceIn(Class clazz, String importDecl){
        return replaceIn( _java.type(clazz), importDecl);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param importDecl
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, String importDecl){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), $import.of(importDecl));
                return _n;
            } else{
                replaceIn(((_type) _n).ast(), $import.of(importDecl));
                return _n;
            }
        }
        replaceIn(((_node) _n).ast(), $import.of(importDecl));
        return _n;
    }
    
    /**
     * 
     * @param clazz
     * @param _i
     * @return 
     */
    public _type replaceIn(Class clazz, _import _i){
        return replaceIn( _java.type(clazz), _i);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param _i
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, _import _i){
         if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), $import.of(_i));
                return _n;
            } else{
                replaceIn(((_type) _n).ast(), $import.of(_i));
                return _n;
            }
        }
        replaceIn(((_node) _n).ast(), $import.of(_i));
        return _n;       
    }
    
    /**
     * 
     * @param clazz
     * @param $i
     * @return 
     */
    public _type replaceIn(Class clazz, $import $i ){
        return replaceIn(_java.type(clazz), $i);
    }
    
    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param _n the model to find replacements
     * @param $i the template to be constructed as the replacement
     * @param <N> the TYPE of model
     * @return
     */
    public <N extends _java> N replaceIn(N _n, $import $i ){
         if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                replaceIn( ((_code) _n).astCompilationUnit(), $i);
                return _n;
            } else{
                replaceIn(((_type) _n).ast(), $i);
                return _n;
            }
        }
        replaceIn(((_node) _n).ast(), $i);
        return _n;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param clazz
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, Class clazz){
        return replaceIn(astNode, $import.of(clazz));
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $i
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $import $i ){
        if( astNode.findCompilationUnit().isPresent() ){
            astNode.findCompilationUnit().get().walk(ImportDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    ImportDeclaration id = sel.ast();
                    boolean isS = id.isStatic();
                    boolean isW = id.isAsterisk();
                    ImportDeclaration rep = $i.construct(sel.args).ast();
                    if( isS){
                        rep.setStatic(true);
                    }
                    if( isW ){
                        rep.setAsterisk(true);
                    }
                    System.out.println( "REPLACEMENT "+ rep);
                    id.replace( rep );
                    
                    /*
                    if( $i.isWildcard || ){
                        id.setAsterisk($i.isWildcard);
                    }
                    if( $i.isStatic ){
                        id.setStatic($i.isStatic);
                    }
                    */
                }
            });
        }
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return forSelectedIn(_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <T>
     * @param _t
     * @param selectConsumer
     * @return 
     */
    public <T extends _code> T forSelectedIn( T _t, Consumer<Select> selectConsumer ){
        forSelectedIn(_t.astCompilationUnit(), selectConsumer);
        return _t;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectConsumer ){
        if( astNode.findCompilationUnit().isPresent()){
            astNode.findCompilationUnit().get().walk(ImportDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null ){
                    selectConsumer.accept( sel );
                }
            });
        }
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        return forSelectedIn(_java.type(clazz), selectConstraint, selectConsumer );
    }
    
    /**
     * 
     * @param <T>
     * @param _t
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <T extends _code> T forSelectedIn(T _t, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        forSelectedIn(_t.astCompilationUnit(), selectConstraint, selectConsumer);
        return _t;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        if( astNode.findCompilationUnit().isPresent()){
            astNode.findCompilationUnit().get().walk(ImportDeclaration.class, e-> {
                Select sel = select( e );
                if( sel != null && selectConstraint.test(sel) ){
                    selectConsumer.accept( sel );
                }
            });
        }
        return astNode;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_import> _importMatchFn, Consumer<_import> _importActionFn){
        astNode.walk(ImportDeclaration.class, e-> {
            Select sel = select(e);
            if( sel != null && _importMatchFn.test(sel._i) ) {
                _importActionFn.accept( sel._i );
            }
        });
        return astNode;
    }

    /**
     * A Matched Selection result returned from matching a prototype $import
     * inside of some CompilationUnit
     */
    public static class Select implements $proto.selected, 
        $proto.selectedAstNode<ImportDeclaration>, 
        $proto.selected_model<_import> {
    
        public final _import _i;
        public final $args args;

        public Select(_import _i, $args tokens){
            this._i = _i;  
            this.args = tokens;
        }
        
        public Select( ImportDeclaration astImport, $args tokens){
            this._i = _import.of(astImport );
            this.args = tokens;
        }
        
        @Override
        public $args args(){
            return args;
        }
        
        @Override
        public String toString(){
            return "$import.Select {"+ System.lineSeparator()+
                Text.indent( _i.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }

        @Override
        public ImportDeclaration ast() {
            return _i.ast();
        }
        
        public boolean isStatic(){
            return _i.isStatic();
        }
        
        public boolean isWildcard(){
            return _i.isWildcard();
        }

        public boolean is(String importDecl){
            return _i.is(importDecl);
        }
        
        public boolean is(Class clazz){
            return _i.is(clazz);
        }
        
        @Override
        public _import model() {
            return _i;
        }
    }
}
