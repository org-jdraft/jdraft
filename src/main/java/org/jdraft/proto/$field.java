package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._javadoc;
import org.jdraft._typeRef;
import org.jdraft._class;
import org.jdraft._java;
import org.jdraft._field;
import org.jdraft._anno;
import org.jdraft._type;
import org.jdraft._walk;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft.Expr;
import org.jdraft._node;
import org.jdraft.macro._macro;
import org.jdraft.macro._remove;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Prototype of a _field (for querying and composing)
 *
 * @author Eric
 */
public final class $field implements Template<_field>, $proto<_field, $field>,  $proto.$java<_field, $field> {

    public Class<_field> javaType(){
        return _field.class;
    }

    public interface $part{} 

    /** @return BUILD AND RETURN prototype instances */    
    public static $field of(){
        return of (_field.of(" $type$ $name$;") );
    }

    /**
     *
     * @param anonymousObjectWithField
     * @return
     */
    public static $field of( Object anonymousObjectWithField ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.anonymousObject(ste);
        FieldDeclaration fd = (FieldDeclaration) oce.getAnonymousClassBody().get().stream().filter(bd -> bd instanceof FieldDeclaration
                && !bd.getAnnotationByClass(_remove.class).isPresent()).findFirst().get();

        //add the field to a class so I can run macros
        _class _c = _class.of("Temp").add(_field.of(fd.clone().getVariable(0)));
        _macro.to(anonymousObjectWithField.getClass(), _c);

        return of( _c.getField(0) );        
    }

    /**
     *
     * @param field
     * @return
     */
    public static $field of(String field ){
        return of( _field.of(field));
    }

    /**
     *
     * @param field
     * @return
     */
    public static $field of( String...field ){
        return of( _field.of(field));
    }

    /**
     *
     * @param field
     * @param constraint
     * @return
     */
    public static $field of( String field, Predicate<_field> constraint){
        return of( _field.of(field)).$and(constraint);
    }

    /**
     *
     * @param _f
     * @param constraint
     * @return
     */
    public static $field of( _field _f, Predicate<_field> constraint){
        return of(_f).$and(constraint);
    }

    /**
     *
     * @param constraint
     * @return
     */
    public static $field of( Predicate<_field> constraint ){
        return of().$and( constraint);
    }
    
    public static $field of( _field _f ){
        $field $inst = new $field();
        if( _f.hasJavadoc() ){
            $inst.javadoc = $comment.javadocComment(_f.getJavadoc());
        }
        if( _f.hasAnnos() ){
            $inst.annos = $annos.of(_f.getAnnos());
        }
        $inst.modifiers = $modifiers.of(_f);        
        $inst.type = $typeRef.of(_f.getType());
        $inst.name = $id.of( _f.getName() );
        if( _f.hasInit() ){
            $inst.init = $expr.of(_f.getInit() );
        }
        return $inst;
    }
    
    public static $field of($part part ){
        $part[] parts = new $part[]{part};
        return of(parts);
    }
    
    /**
     * 
     * @param parts
     * @return 
     */
    public static $field of($part...parts ){
        return new $field(parts);
    }
    
    public Predicate<_field> constraint = t->true;
    public $comment<JavadocComment> javadoc = $comment.javadocComment("$javadoc$");
    public $annos annos = new $annos(); 
    public $modifiers modifiers = $modifiers.of();
    public $typeRef type = $typeRef.of();
    public $id name = $id.of("$name$");
    public $expr init = null;
    
    private $field( $part...parts ){
        for(int i=0;i<parts.length;i++){
            if(parts[i] instanceof $annos){
                this.annos = ($annos)parts[i];
            }
            else if( parts[i] instanceof $anno ){
                this.annos.$annosList.add( ($anno)parts[i]);
            }
            else if( parts[i] instanceof $modifiers ){
                this.modifiers = ($modifiers)parts[i]; 
            }
            else if( parts[i] instanceof $typeRef){
                this.type = ($typeRef)parts[i];
            }
            else if( parts[i] instanceof $id){
                this.name = ($id)parts[i];
            }
            else if( parts[i] instanceof $expr ){
                this.init = ($expr)parts[i];
            }
            else if(parts[i] instanceof $comment ){
                this.javadoc = ($comment<JavadocComment>)parts[i];
            }
        }
    }
    
    private $field(){        
    }
    
    /** prototype post parameterization (i.e. the query can change) */
    
    /**
     * Post parameterize the javadoc field (accept any and return it as "javadoc")
     * @return 
     */
    public $field $javadoc(){
        this.javadoc = $comment.javadocComment("$javadoc$");        
        return this;
    }

    /**
     *
     * @param javadoc
     * @return
     */
    public $field $javadoc( String... javadoc ){
        this.javadoc.contentsStencil = Stencil.of((Object[])javadoc);
        return this;
    }

    /**
     *
     * @param _jd
     * @return
     */
     public $field $javadoc( _javadoc _jd ){
        this.javadoc.contentsStencil = Stencil.of(_jd.getContent() );
        return this;
     }

    /**
     *
     * @return
     */
    public $field $annos(){
        this.annos = $annos.of();
        return this;
    }

    /**
     *
     * @param as
     * @return
     */
    public $field $annos( Predicate<_anno._annos> as ){
        this.annos.$and(as);
        return this;
    }

    /**
     *
     * @param annoPatterns
     * @return
     */
    public $field $annos(String...annoPatterns ){
        this.annos.add(annoPatterns);
        return this;
    }

    /**
     *
     * @param $as
     * @return
     */
    public $field $annos($annos $as ){
        this.annos = $as;
        return this;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public $field $anno( Class clazz){
        this.annos.$annosList.add($anno.of(clazz) );
        return this;
    }

    /**
     *
     * @param _an
     * @return
     */
    public $field $anno( _anno _an){
        this.annos.$annosList.add($anno.of(_an) );
        return this;
    }

    /**
     * set a constraint on the 
     * @param javadocConstraint
     * @return 
     */
    public $field $javadoc(Predicate<JavadocComment> javadocConstraint ){
        this.javadoc.$and(javadocConstraint);
        return this;
    }

    /**
     *
     * @return
     */
    public $field $type(){
        this.type = $typeRef.of("$type$");
        return this;
    }

    /**
     *
     * @param _tr
     * @return
     */
    public $field $type( _typeRef _tr){
        this.type = $typeRef.of(_tr);
        return this;
    }

    /**
     *
     * @param tr
     * @return
     */
    public $field $type( $typeRef tr){
        this.type = tr;
        return this;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public $field $type(Class clazz){
        this.type = $typeRef.of(clazz);
        return this;
    }

    /**
     *
     * @param pattern
     * @return
     */
    public $field $type( String pattern ){
        this.type.type = Ast.typeRef(pattern);
        //this.type.typePattern = Stencil.of(_typeRef.of(pattern).toString());
        return this;
    }

    /**
     *
     * @param typeConstraint
     * @return
     */
    public $field $type(Predicate<_typeRef> typeConstraint ){
        this.type.constraint = this.type.constraint.and(typeConstraint);
        return this;
    }

    /**
     *
     * @return
     */
    public $field $name(){
        this.name.idStencil = Stencil.of("$name$");
        return this;
    }

    /**
     *
     * @param id
     * @return
     */
    public $field $name( $id id ){
        this.name = id;
        return this;
    }

    /**
     *
     * @param pattern
     * @return
     */
    public $field $name(String pattern ){
        this.name.idStencil = Stencil.of(pattern);
        return this;
    }

    /**
     *
     * @param nameConstraint
     * @return
     */
    public $field $name(Predicate<String> nameConstraint ){
        this.name.constraint = this.name.constraint.and(nameConstraint);
        return this;
    }

    /**
     *
     * @return
     */
    public $field $init(){
        this.init = $expr.of();        
        this.init.exprStencil = Stencil.of( "$init$" );
        return this;
    }

    /**
     *
     * @param initPattern
     * @return
     */
    public $field $init( String initPattern ){
        this.init.exprStencil = Stencil.of(Expr.of(initPattern).toString() );
        return this;
    }

    /**
     *
     * @param initProto
     * @return
     */
    public $field $init( Expression initProto ){
        this.init.exprStencil = Stencil.of(initProto.toString() );
        return this;
    }

    /**
     *
     * @param initConstraint
     * @return
     */
    public $field $init( Predicate<Expression> initConstraint ){
        this.init.constraint = this.init.constraint.and(initConstraint);
        return this;
    }

    @Override
    public $field hardcode$(Translator translator, Tokens kvs) {
        this.init = this.init.hardcode$(translator, kvs);
        this.annos = this.annos.hardcode$(translator, kvs);
        this.type = this.type.hardcode$(translator, kvs);
        this.javadoc = this.javadoc.hardcode$(translator, kvs);
        this.name = this.name.hardcode$(translator, kvs);
        this.modifiers = this.modifiers.hardcode$(translator, kvs);
        return this;
    }

    /**
     * Adds ANOTHER constraint to the existing _field level constraint
     * @param constraint
     * @return 
     */ 
    public $field $and(Predicate<_field> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public boolean match( Node astNode){
        if( astNode instanceof VariableDeclarator ){
            return matches( (VariableDeclarator) astNode);
        }
        return false;
    }

    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astStartNode the node to look through
     * @param _fieldMatchFn
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public _field firstIn(Node astStartNode, Predicate<_field> _fieldMatchFn){
        Optional<VariableDeclarator> f = astStartNode.findFirst(VariableDeclarator.class, s ->{
            Select sel = select(s);
            return sel != null && _fieldMatchFn.test(sel._f);
            });         
        if( f.isPresent()){
            return _field.of(f.get());
        }
        return null;
    }

    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first _field that matches (or null if none found)
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<VariableDeclarator> f = astNode.findFirst(VariableDeclarator.class, s -> this.matches(s) );         
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( Class clazz, Predicate<Select>selectConstraint ){
       return selectFirstIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * Returns the first _field that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first _field that matches (or null if none found)
     */
    public Select selectFirstIn( _java _j, Predicate<Select> selectConstraint){
         if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return selectFirstIn(((_code) _j).astCompilationUnit(), selectConstraint);
            }
            return selectFirstIn(((_type) _j).ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_j).ast(), selectConstraint );
    }

    /**
     * Returns the first _field that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first _field that matches (or null if none found)
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint){
        Optional<VariableDeclarator> f = astNode.findFirst(VariableDeclarator.class, s ->{
            Select sel = this.select(s);
            return sel != null && selectConstraint.test(sel);
            });         
        
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode ){
        List<Select>sts = new ArrayList<>();
        astNode.walk(VariableDeclarator.class, e-> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }

    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint){
        List<Select>sts = new ArrayList<>();
        astNode.walk(VariableDeclarator.class, e-> {
            Select s = select( e );
            if( s != null && selectConstraint.test(s) ){
                sts.add( s);
            }
        });
        return sts;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint){
        return listSelectedIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(_java _j, Predicate<Select> selectConstraint){
         if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return listSelectedIn(((_code) _j).astCompilationUnit(), selectConstraint);
            }
            return listSelectedIn(((_type) _j).ast(), selectConstraint);
        }
        return listSelectedIn( ((_node)_j).ast(), selectConstraint);
    }

    /**
     * 
     * @param clazz
     * @param $replaceProto
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $field $replaceProto ){
        return (_CT)replaceIn( (_type)_java.type(clazz), $replaceProto);
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param $replaceProto
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $field $replaceProto ){
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel._f.ast().replace($replaceProto.draft(sel.tokens).ast() );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replaceProto
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, $field $replaceProto ){
        _walk.in(_j, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel._f.ast().replace($replaceProto.draft(sel.tokens).ast() );
            }
        });
        return _j;
    }

    /**
     * 
     * @param clazz
     * @param selectConsumer
     * @return 
     */
    public _type forSelectedIn(Class clazz, Consumer<Select> selectConsumer ){
       return forSelectedIn( (_type)_java.type(clazz), selectConsumer );
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> selectConsumer ){
        _walk.in(_j, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return _j;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select> selectConsumer ){
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
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
        return (_CT)forSelectedIn( (_type)_java.type(clazz), selectConstraint, selectConsumer);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectConsumer ){
        _walk.in(_j, VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return _j;
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
        astNode.walk(VariableDeclarator.class, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_field> _fieldMatchFn, Consumer<_field> _fieldActionFn){
        astNode.walk(VariableDeclarator.class, e-> {
            //Tokens tokens = this.stencil.partsMap( e.toString());
            Select sel = select( e );
            if( sel != null && _fieldMatchFn.test(sel._f)){
                _fieldActionFn.accept( sel._f );
            }
        });
        return astNode;
    }
    
    /**
     * @param field 
     * @return true is the field declaration  matches the prototype
     */
    public boolean matches( String...field ){
        try{
            return matches(_field.of(field));
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     * does this variable match the prototype
     * @param astVar
     * @return 
     */
    public boolean matches( VariableDeclarator astVar ){
        return select(astVar) != null;        
    }
    
    /**
     * does this field match the prototype?
     * @param _f
     * @return 
     */
    public boolean matches( _field _f ){
        return select(_f) != null;
    }

    /**
     * Returns a Select based on a field declaration 
     * OR NULL if this field declaration does not fit the prototype
     * @param field field declaration
     * @return a Select of the field
     */
    public Select select(String...field){
        try{
            return select(_field.of(field));
        }catch(Exception e){
            return null;
        }        
    }
    
    /**
     * 
     * @param _f
     * @return 
     */
    public Select select(_field _f){
        if( this.constraint.test(_f) && modifiers.select(_f) != null ){            
            Tokens all = new Tokens();
            if (_f.getJavadoc() == null ){
                all = javadoc.parseTo(_f.getJavadoc().ast(), all);
            } else{
                all = javadoc.parseTo(null, all);
            }            
            all = annos.parseTo(_f.getAnnos(), all);
            all = type.parseTo(_f.getType(), all);
            all = name.parseTo(_f.getName(), all);
            if( init == null ){
                //if we dont care what the init is or is not
            }else{
                if( !_f.hasInit() ){
                    return null;
                }
                $expr.Select sel = init.select(_f.getInit());
                if( all.isConsistent(sel.tokens.asTokens()) ){
                    all.putAll( sel.tokens().asTokens() );
                } else{
                    return null;
                }
                //all = init.decomposeTo(_f.getInit(), all);
            }
            if(all != null){
                return new Select( _f, $tokens.of(all));
            }
        }
        return null;
    }

    /**
     * 
     * @param astField
     * @return 
     */
    public List<Select> select(FieldDeclaration astField){
        List<Select> sels = new ArrayList<>();
        astField.getVariables().forEach( v-> {
            Select sel = select( _field.of(v) );
            if( sel != null ){
                sels.add(sel);
            }
        });
        return sels;
    }

    /**
     * 
     * @param astVar
     * @return 
     */
    public Select select(VariableDeclarator astVar){
        if( astVar.getParentNode().isPresent() && astVar.getParentNode().get() instanceof FieldDeclaration ){
            return select( _field.of(astVar));            
        }   
        return null;
    } 
    
    @Override
    public _field draft(Translator translator, Map<String, Object> keyValues) {
        Map<String,Object> baseMap = new HashMap<>();
        //we need to set these to empty
        baseMap.put("javadoc", "" );
        baseMap.put("annos", "");
        baseMap.put("modifiers", "");
        baseMap.put("type", "");
        baseMap.put("name", "");
        baseMap.put("init", "");
        
        baseMap.putAll(keyValues);
        
        StringBuilder sb = new StringBuilder();
        JavadocComment jdc = javadoc.draft(translator, baseMap);
        if( jdc != null){
            sb.append(jdc);
            sb.append(System.lineSeparator());
        }        
        sb.append(annos.draft(translator, baseMap) );
        sb.append(System.lineSeparator());
        sb.append(modifiers.draft(translator, baseMap) );
        sb.append(" ");
        sb.append(type.draft(translator, baseMap) );
        sb.append(" ");
        sb.append(name.idStencil.draft(translator, baseMap) );
        if( init != null ){
            Expression expr = init.draft(translator, baseMap);
            if( expr != null ){
                sb.append( " = ");
                sb.append( expr );
            }
        }
        sb.append(";");
        return _field.of( sb.toString() );        
    }

    public static final List<String> DEFAULT_COMPONENT_$NAMES = new ArrayList();
    
    static{
        DEFAULT_COMPONENT_$NAMES.add("javadoc");
        DEFAULT_COMPONENT_$NAMES.add("annos");
        DEFAULT_COMPONENT_$NAMES.add("modifiers");
        DEFAULT_COMPONENT_$NAMES.add("type");
        DEFAULT_COMPONENT_$NAMES.add("name");
        DEFAULT_COMPONENT_$NAMES.add("init");
    }
    
    @Override
    public $field $(String target, String $Name) {
        javadoc.$(target, $Name);
        annos.$(target, $Name);
        type.$(target, $Name);
        name.$(target, $Name);
        if( init != null ){
            init.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> vars = new ArrayList<>();
        vars.addAll( javadoc.list$() );
        vars.addAll( annos.list$() );
        vars.addAll( type.list$() );
        vars.addAll( name.list$() );
        if( init != null ){
            vars.addAll( init.list$() );
        }        
        return vars;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> vars = new ArrayList<>();
        vars.addAll( javadoc.list$Normalized() );
        vars.addAll( annos.list$Normalized() );
        vars.addAll( type.list$Normalized() );
        vars.addAll( name.list$Normalized() );
        if( init != null ){
            vars.addAll( init.list$Normalized() );
        }
        return vars.stream().distinct().collect(Collectors.toList());
    }    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(javadoc );
        sb.append(System.lineSeparator());
        sb.append(annos );
        sb.append(System.lineSeparator());
        sb.append(modifiers.toString()); //construct(Translator.TypeTranslate, new HashMap<String,Object>() ) );
        sb.append(" ");
        sb.append(type.toString() );
        sb.append(" ");
        sb.append(name.idStencil);
        if( init != null ){
            sb.append(" = ");
            sb.append(init.toString());
        }
        sb.append(";");
        
        return "($field): "+ System.lineSeparator()+ Text.indent( sb.toString() );
    }
    
     /**
     * A Matched Selection result returned from matching a prototype $field
     * inside of some Node or _node
     */
    public static class Select implements $proto.selected,
             selectAst<VariableDeclarator>,
             select_java<_field> {
        
        public final _field _f;
        public final $tokens tokens;

        public Select( _field _f, $tokens tokens){
            this._f = _f;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$field.Select{"+ System.lineSeparator()+
                Text.indent( _f.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public VariableDeclarator ast() {
            return _f.ast();
        }

        public boolean is( String fieldDeclaration ){
            return _f.is(fieldDeclaration);
        }
        
        public boolean hasJavadoc(){
            return _f.hasJavadoc();
        }
        
        public boolean isStatic(){
            return _f.isStatic();
        }
        
        public boolean isFinal(){            
            return _f.isFinal();
        }
        
        public boolean hasAnnos(){
            return _f.hasAnnos();
        }
        
        public boolean hasAnno(Class<? extends Annotation> annoClass){            
            return _f.hasAnno(annoClass);
        }
        
        public boolean isInit( Predicate<Expression> initMatchFn){            
            return _f.isInit(initMatchFn);
        }
        
        public boolean isInit(String init){
            return _f.isInit(init);
        }
        
        public boolean isInit(byte init){
            return _f.isInit(init);
        }
        
        public boolean isInit(short init){
            return _f.isInit(init);
        }
        
        public boolean isInit(long init){
            return _f.isInit(init);
        }
        
        public boolean isInit(char init){
            return _f.isInit(init);
        }
        
        public boolean isInit(double init){
            return _f.isInit(init);
        }
        
        public boolean isInit(float init){
            return _f.isInit(init);
        }
        
        public boolean isInit(boolean init){
            return _f.isInit(init);
        }
        
        public boolean isInit(int init){
            return _f.isInit(init);
        }
        
        public boolean isInit(Expression init){
            return _f.isInit(init);
        }
        
        public boolean isType( Class expectedType){
            return _f.isType(expectedType);
        }
        
        public boolean isType( String expectedType){
            return _f.isType(expectedType);
        }
        
        public boolean isType( _typeRef expectedType){
            return _f.isType(expectedType);
        }
        
        public boolean isType( Type expectedType){
            return _f.isType(expectedType);
        }
        
        @Override
        public _field _node() {
            return _f;
        }
    }
}
