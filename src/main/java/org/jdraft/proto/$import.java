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
    implements Template<_import>, $proto<_import, $import> {
    
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
        return new $import( _i ).and(constraint);
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
        return new $import( _i  ).and(constraint);
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
        return new $import( _proto ).and(constraint);
    }
    
    public Predicate<_import> constraint = t-> true;
        
    public Stencil importStencil;
    
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
            this.importStencil = Stencil.of( proto.getName()+".*" );
        } else{
            this.importStencil = Stencil.of( proto.getName() );
        }
        this.isStatic = proto.isStatic();
        this.isWildcard = proto.isWildcard();        
    }

    private $import( Predicate<_import> constraint ){
        this.importStencil = Stencil.of("$any$");
        this.constraint = constraint;
        this.isStatic = false;
        this.isWildcard = false;   
    }
    
    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified prototype
     */
    public $import and(Predicate<_import>constraint ){
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
            Tokens ts = importStencil.parse( name );
            if( ts != null ){
                return new Select(_i, $tokens.of(ts) );
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
        return "($import) : \"" +this.importStencil + "\"";
    }

    @Override
    public _import draft(Translator translator, Map<String, Object> keyValues) {
        _import _ii = _import.of(importStencil.draft(translator, keyValues));
        _ii.setStatic(this.isStatic);
        _ii.setWildcard(this.isWildcard);
        return _ii;
    }
    
    @Override
    public $import $(String target, String $Name) {
        this.importStencil = this.importStencil.$(target, $Name);
        return this;
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $import hardcode$( Translator translator, Tokens kvs ) {
        this.importStencil = this.importStencil.hardcode$(translator,kvs);
        return this;
    }

    @Override
    public List<String> list$() {
        return this.importStencil.list$();
    }

    @Override
    public List<String> list$Normalized() {
        return this.importStencil.list$Normalized();
    }

    public boolean match( Node node ) {
        if (node instanceof ImportDeclaration) {
            return matches((ImportDeclaration) node);
        }
        return false;
    }

    /**
     * Returns the first _import that matches the pattern and constraint
     * @param astNode the node to look through
     * @param _importMatchFn
     * @return  the first _import that matches (or null if none found)
     */
    @Override
    public _import firstIn(Node astNode, Predicate<_import> _importMatchFn){
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
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _import that matches (or null if none found)
     */
    public Select selectFirstIn( _java _j, Predicate<Select> selectConstraint ){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return selectFirstIn(((_code) _j).astCompilationUnit(), selectConstraint);
            } else{
                return selectFirstIn(((_type) _j).ast(), selectConstraint);
            }
        }
        return selectFirstIn(((_node) _j).ast(), selectConstraint);
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
        return listSelectedIn((_type)_java.type(clazz), selectConstraint);
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
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( _java _j, Predicate<Select> selectConstraint ){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return listSelectedIn(((_code) _j).astCompilationUnit(), selectConstraint);
            } else{
                return listSelectedIn(((_type) _j).ast(), selectConstraint);
            }
        }
        return listSelectedIn(((_node) _j).ast(), selectConstraint);
            
    }
    
    /**
     * 
     * @param clazz
     * @param importClass
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, Class importClass){
        return (_CT)replaceIn( (_type)_java.type(clazz), importClass);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param importClass
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, Class importClass){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), importClass);
                return _j;
            } else{
                replaceIn(((_type) _j).ast(), importClass);
                return _j;
            }
        }
        replaceIn(((_node) _j).ast(), importClass);
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param importDecl
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, String importDecl){
        return (_CT)replaceIn( (_type)_java.type(clazz), importDecl);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param importDecl
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, String importDecl){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), $import.of(importDecl));
                return _j;
            } else{
                replaceIn(((_type) _j).ast(), $import.of(importDecl));
                return _j;
            }
        }
        replaceIn(((_node) _j).ast(), $import.of(importDecl));
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param _i
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, _import _i){
        return (_CT)replaceIn( (_type)_java.type(clazz), _i);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param _i
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, _import _i){
         if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), $import.of(_i));
                return _j;
            } else{
                replaceIn(((_type) _j).ast(), $import.of(_i));
                return _j;
            }
        }
        replaceIn(((_node) _j).ast(), $import.of(_i));
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param $i
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn(Class clazz, $import $i ){
        return (_CT)replaceIn((_type)_java.type(clazz), $i);
    }
    
    /**
     * Replace all occurrences of the template in the code with the replacement
     * (composing the replacement from the constructed tokens in the source)
     *
     * @param _j the model to find replacements
     * @param $i the template to be constructed as the replacement
     * @param <_J> the TYPE of model
     * @return
     */
    public <_J extends _java> _J replaceIn(_J _j, $import $i ){
         if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                replaceIn( ((_code) _j).astCompilationUnit(), $i);
                return _j;
            } else{
                replaceIn(((_type) _j).ast(), $i);
                return _j;
            }
        }
        replaceIn(((_node) _j).ast(), $i);
        return _j;
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
                    ImportDeclaration rep = $i.draft(sel.tokens).ast();
                    if( isS){
                        rep.setStatic(true);
                    }
                    if( isW ){
                        rep.setAsterisk(true);
                    }
                    //System.out.println( "REPLACEMENT "+ rep);
                    id.replace( rep );
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
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Consumer<Select> selectConsumer){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <_C>
     * @param _c
     * @param selectConsumer
     * @return 
     */
    public <_C extends _code> _C forSelectedIn(_C _c, Consumer<Select> selectConsumer ){
        forSelectedIn(_c.astCompilationUnit(), selectConsumer);
        return _c;
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
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectConsumer );
    }
    
    /**
     * 
     * @param <_C>
     * @param _c
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_C extends _code> _C forSelectedIn(_C _c, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        forSelectedIn(_c.astCompilationUnit(), selectConstraint, selectConsumer);
        return _c;
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
            selectAst<ImportDeclaration>,
            select_java<_import> {
    
        public final _import _i;
        public final $tokens tokens;

        public Select(_import _i, $tokens tokens){
            this._i = _i;  
            this.tokens = tokens;
        }
        
        public Select( ImportDeclaration astImport, $tokens tokens){
            this._i = _import.of(astImport );
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$import.Select {"+ System.lineSeparator()+
                Text.indent( _i.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
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
        public _import _node() {
            return _i;
        }
    }
}
