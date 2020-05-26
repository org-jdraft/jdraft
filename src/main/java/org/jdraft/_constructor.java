package org.jdraft;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
//import com.github.javaparser.ast.nodeTypes.NodeWithConstructors;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import org.jdraft.macro._remove;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import org.jdraft.macro._toCtor;
import org.jdraft.macro.macro;
import org.jdraft.text.Text;

/**
 * Representation of the Java source code of a constructor ({@link ConstructorDeclaration}
 * (i.e. "X(){}" in : "<code>class X { X() { } }</code>)
 *
 * @author Eric
 */
public final class _constructor implements _annoExprs._withAnnoExprs<_constructor>,
        _javadocComment._withJavadoc<_constructor>, _throws._withThrows<_constructor>,
        _body._withBody<_constructor>, _modifiers._withModifiers<_constructor>, //_modifiers._hasModifiers<_constructor>,
        _params._withParams<_constructor>, _typeParams._withTypeParams<_constructor>,
        _receiverParam._withReceiverParam<_constructor>, _java._declared<ConstructorDeclaration, _constructor> {

    public static final Function<String, _constructor> PARSER = s-> _constructor.of(s);

    public static _constructor of( String signature ){
        return of( new String[]{signature});
    }

    /**
     * Build a constructor from a method on an anonymous object BODY
     *
     * @param anonymousObjectBody
     * @return
     */
    public static _constructor of(Object anonymousObjectBody ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Exprs.newExpr( ste );
        
        _class _c = _class.of("C");
        if( oce.getAnonymousClassBody().isPresent() ){
            NodeList<BodyDeclaration<?>> bs = oce.getAnonymousClassBody().get();
            bs.forEach( b -> _c.ast().addMember(b));
        }
        
        //run macros on the things
        macro.to( anonymousObjectBody.getClass(), _c);
        
        MethodDeclaration theMethod = (MethodDeclaration)
            oce.getAnonymousClassBody().get().stream().filter(m -> m instanceof MethodDeclaration &&
                !m.isAnnotationPresent(_remove.class) ).findFirst().get();
        
        //build the base method first
        _constructor _ct = _constructor.of( theMethod.getNameAsString() + " " + _params.of( theMethod )+"{}" );
        
        //MODIFIERS
        if( theMethod.isPublic() ){
            _ct.setPublic();
        }
        if(theMethod.isProtected()){
            _ct.setProtected();
        }
        if(theMethod.isPrivate()){
            _ct.setPrivate();
        }
        if( theMethod.hasJavaDocComment() ){
            _ct.setJavadoc(theMethod.getJavadocComment().get());
        }
        //System.out.println( "Setting throws");
        _ct.setThrows( theMethod.getThrownExceptions() );
        _ct.addAnnoExprs( theMethod.getAnnotations()); //add annos
        _ct.removeAnnoExprs(_toCtor.class); //remove the _ctor anno if it exists
        _ct.setBody( theMethod.getBody().get() ); //BODY
        
        return _ct;
    }

    public static _constructor of(){
        return of( new ConstructorDeclaration());
    }

    public static _constructor of( String... ctorDecl ) {
        //I need to do shortcut CONSTRUCTORS
        if( ctorDecl.length == 1 ){
            String[] toks = ctorDecl[0].split(" ");
            if( toks.length == 1){
                String token = toks[0];
                if( token.endsWith("}")){
                    return new _constructor( Ast.constructor(toks[0]));
                }
                if( token.endsWith(")")){
                    return new _constructor( Ast.constructor(toks[0]+"{}"));
                }
                return new _constructor( Ast.constructor("public "+toks[0]+"(){}"));
            }
        }
        return new _constructor( Ast.constructor( ctorDecl ) );
    }

    public static _constructor of( ConstructorDeclaration ctorDecl ) {
        return new _constructor( ctorDecl );
    }

    public static _feature._one<_constructor, _javadocComment> JAVADOC = new _feature._one<>(_constructor.class, _javadocComment.class,
            _feature._id.JAVADOC,
            a -> a.getJavadoc(),
            (_constructor a, _javadocComment _jd) -> a.setJavadoc(_jd), PARSER);

    public static _feature._one<_constructor, _annoExprs> ANNO_EXPRS = new _feature._one<>(_constructor.class, _annoExprs.class,
            _feature._id.ANNO_EXPRS,
            a -> a.getAnnoExprs(),
            (_constructor a, _annoExprs _ta) -> a.setAnnoExprs(_ta), PARSER);

    public static _feature._one<_constructor, _modifiers> MODIFIERS = new _feature._one<>(_constructor.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getModifiers(),
            (_constructor a, _modifiers _ms) -> a.setModifiers(_ms), PARSER);

    public static _feature._one<_constructor, _typeParams> TYPE_PARAMS = new _feature._one<>(_constructor.class, _typeParams.class,
            _feature._id.TYPE_PARAMS,
            a -> a.getTypeParams(),
            (_constructor a, _typeParams _tps) -> a.setTypeParams(_tps), PARSER);

    public static _feature._one<_constructor, String> NAME = new _feature._one<>(_constructor.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_constructor a, String s) -> a.setName(s), PARSER);

    public static _feature._one<_constructor, _receiverParam> RECEIVER_PARAM = new _feature._one<>(_constructor.class, _receiverParam.class,
            _feature._id.RECEIVER_PARAM,
            a -> a.getReceiverParam(),
            (_constructor a, _receiverParam _r) -> a.setReceiverParam(_r), PARSER);

    public static _feature._one<_constructor, _params> PARAMS = new _feature._one<>(_constructor.class, _params.class,
            _feature._id.PARAMS,
            a -> a.getParams(),
            (_constructor a, _params _p) -> a.setParams(_p), PARSER);

    public static _feature._one<_constructor, _throws> THROWS = new _feature._one<>(_constructor.class, _throws.class,
            _feature._id.THROWS,
            a -> a.getThrows(),
            (_constructor a, _throws _t) -> a.setThrows(_t), PARSER);

    public static _feature._one<_constructor, _body> BODY = new _feature._one<>(_constructor.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_constructor a, _body _b) -> a.setBody(_b), PARSER);

    public static _feature._meta<_constructor> META = _feature._meta.of(_constructor.class,
            JAVADOC, ANNO_EXPRS, MODIFIERS, TYPE_PARAMS, NAME, RECEIVER_PARAM, PARAMS, THROWS, BODY );

    private final ConstructorDeclaration astCtor;

    public _constructor( ConstructorDeclaration md ) {
        this.astCtor = md;
    }

    @Override
    public _constructor copy(){
        return new _constructor( this.astCtor.clone());
    }

    @Override
    public NodeList<Modifier> getEffectiveModifiers() {
        NodeList<Modifier> em = Modifiers.getImpliedModifiers( this.astCtor );
        if( em == null ){
            return this.astCtor.getModifiers();
        }
        return Modifiers.merge( em, this.astCtor.getModifiers());
    }

    @Override
    public ConstructorDeclaration ast() {
        return astCtor;
    }
    
    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final _constructor other = (_constructor)obj;
        if( this.astCtor == other.astCtor ) {
            return true; //two _constructor instances pointing to same ConstructorDeclaration instance
        }        
        if( ! Exprs.equalAnnos(this.astCtor, other.astCtor)){
            return false;
        }
        if( !Objects.equals( this.getBody(), other.getBody() ) ) {
            return false;
        }
        if( this.hasJavadoc() != other.hasJavadoc() ) {
            return false;
        }
        //if( this.hasJavadoc() && !Objects.equals( this.getJavadoc().getContent().trim(), other.getJavadoc().getContent().trim() ) ) {
        //    return false;
        //}
        if( this.hasJavadoc() && !Objects.equals( this.getJavadoc().getText(), other.getJavadoc().getText() ) ) {
            return false;
        }
        if( !Modifiers.modifiersEqual(this.astCtor, other.astCtor) ){
            return false;
        }       
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            return false;
        }
        if( !Objects.equals( this.getParams(), other.getParams() ) ) {
            return false;
        }
        if( !Types.equal( this.astCtor.getThrownExceptions(), other.astCtor.getThrownExceptions()) ){
            return false;
        }        
        if( !Objects.equals( this.getTypeParams(), other.getTypeParams() ) ) {
            return false;
        }
        if( !Objects.equals( this.getReceiverParam(), other.getReceiverParam() ) ) {
            return false;
        }
        return true;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.ANNO_EXPRS, getAnnoExprs() );
        parts.put( _java.Feature.BODY, getBody() );
        parts.put( _java.Feature.MODIFIERS, getModifiers() );
        parts.put( _java.Feature.JAVADOC, getJavadoc() );
        parts.put( _java.Feature.PARAMS, getParams() );
        parts.put( _java.Feature.RECEIVER_PARAM, getReceiverParam() );
        parts.put( _java.Feature.TYPE_PARAMS, getTypeParams() );
        parts.put( _java.Feature.THROWS, getThrows() );
        parts.put( _java.Feature.NAME, getName() );
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hash(
            Exprs.hashAnnos(astCtor),
            this.getBody(), 
            this.getJavadoc(),
            this.getEffectiveModifiers(),
            this.getName(), 
            this.getParams(),
            Types.hash( astCtor.getThrownExceptions()),
            this.getTypeParams(),
            this.getReceiverParam() );
        return hash;
    }

    @Override
    public _constructor setName(String name ) {
        this.astCtor.setName( name );
        return this;
    }

    @Override
    public _constructor setJavadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _constructor setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public _throws getThrows() {
        return _throws.of( astCtor );
    }

    public _constructor setTypeParams(_typeParams _tps ){
        this.astCtor.setTypeParameters( _tps.ast() );
        return this;
    }

    public _constructor setTypeParams(String typeParameters ) {
        this.astCtor.setTypeParameters( Types.typeParams( typeParameters ) );
        return this;
    }

    @Override
    public _typeParams getTypeParams() {
        return _typeParams.of( this.astCtor );
    }

    @Override
    public boolean hasTypeParams() {
        return this.astCtor.getTypeParameters().isNonEmpty();
    }

    /**
     * 
     * @param ctor
     * @return 
     */
    public boolean isParams(java.lang.reflect.Constructor ctor ){
        java.lang.reflect.Type[] genericParameterTypes = ctor.getGenericParameterTypes();
        List<_param> pl = this.listParams();
        int delta = 0;
        if( genericParameterTypes.length != pl.size() ){
            if( genericParameterTypes.length == pl.size() + 1 && ctor.getDeclaringClass().isLocalClass()){
                if( ctor.getDeclaringClass().isLocalClass() ){
                    delta = 1;
                }
            } else{
                return false;
            }            
        }
        for(int i=0;i<pl.size(); i++){
            _typeRef _t = _typeRef.of(genericParameterTypes[i+delta]);
            if( !pl.get(i).isTypeRef( _t ) ){
                if( ctor.isVarArgs() &&  //if last parameter and varargs
                    Types.equal( pl.get(i).getTypeRef().getElementType(),
                        _t.getElementType())  ){                    
                } else{             
                    return false;
                }
            }
        }        
        return true;        
    }
    
    @Override
    public boolean is( String...constructorDeclaration ){
        try {
            _constructor _ct = of(constructorDeclaration);
            _ct.astCtor.setModifiers( Modifiers.merge(_ct.ast().getModifiers(), Modifiers.getImpliedModifiers( this.astCtor ) ) );
            return equals(_ct);
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean is( ConstructorDeclaration astCd ){
        _constructor _ct = of( astCd );
        return equals( _ct );
    }

    @Override
    public _body getBody() {
        return _body.of( this.astCtor );
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public _modifiers getModifiers() {
        return _modifiers.of( astCtor );
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of( astCtor );
    }

    public SimpleName getNameNode() { return this.astCtor.getName(); }

    @Override
    public String getName() {
        return astCtor.getNameAsString();
    }

    @Override
    public _params getParams() {
        return _params.of( astCtor );
    }

    @Override
    public String toString() {
        return this.astCtor.toString();
    }

    public boolean isPublic() {
        return this.astCtor.isPublic();
    }

    public boolean isProtected() {
        return this.astCtor.isProtected();
    }

    public boolean isPrivate() {
        return this.astCtor.isPrivate() || this.getEffectiveModifiers().contains(Modifier.privateModifier());
    }

    public boolean isStrictFp() {
        return this.astCtor.isStrictfp();
    }

    public boolean isFinal() {
        return this.astCtor.isFinal();
    }

    public _constructor setPublic() {
        this.astCtor.setPrivate(false);
        this.astCtor.setProtected(false);
        this.astCtor.setPublic(true);
        return this;
    }

    public _constructor setProtected() {
        this.astCtor.setPrivate(false);
        this.astCtor.setProtected(true);
        this.astCtor.setPublic(false);
        return this;
    }

    public _constructor setPrivate() {
        this.astCtor.setPrivate(true);
        this.astCtor.setProtected(false);
        this.astCtor.setPublic(false);
        return this;
    }

    public _constructor setDefaultAccess() {
        this.astCtor.setPrivate(false);
        this.astCtor.setProtected(false);
        this.astCtor.setPublic(false);
        return this;
    }

    @Override
    public _constructor setBody( BlockStmt body ) {
        this.astCtor.setBody( body );
        return this;
    }

    @Override
    public _constructor clearBody() {
        this.astCtor.replace( this.astCtor.getBody(), new BlockStmt() );
        return this;
    }

    @Override
    public _constructor add( Statement... statements ) {
        Arrays.stream( statements ).forEach( s -> this.astCtor.getBody().addStatement( s ) );
        return this;
    }

    @Override
    public _constructor add( int startStatementIndex, Statement...statements ){
        for( int i=0;i<statements.length;i++) {
            this.astCtor.getBody().addStatement( i+ startStatementIndex, statements[i] );
        }
        return this;
    }
    
    /**
     *
     * @author Eric
     * @param <_WC>
     * @param <N> the AST node type (must implement NodeWithConstructors)
     */
    public interface _withConstructors<_WC extends _withConstructors & _type, N extends TypeDeclaration>
        extends _java._domain {

        /** 
         * Gets the node that is the nodeWithConstructors (i.e._class, _enum)
         * @return  
         */
        N ast();
        
        /**
         * list all of the (explicit) constructors
         * @return 
         */
        List<_constructor> listConstructors();

        /**
         * 
         * @param index
         * @return 
         */
        _constructor getConstructor( int index );

        /**
         * Check if all individual arg ({@link _constructor}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allConstructors( Predicate<_constructor> matchFn){
            return listConstructors().stream().allMatch(matchFn);
        }

        /**
         * gets the FIRST constructor that matches the _ctorMatchFn (or returns null)
         * @param _ctorMatchFn
         * @return 
         */
        default _constructor getConstructor( Predicate<_constructor> _ctorMatchFn){
            List<_constructor> ctors = listConstructors(_ctorMatchFn);
            if( ctors.isEmpty() ){
                return null;
            }
            return ctors.get(0); //just get the first one
        }
        
        /**
         * Does this entity have any (explicit) constructors
         * @return 
         */
        default boolean hasConstructors() {
            return listConstructors().size() > 0;
        }

        /**
         * does this entity have any explicit constructors that match the lambda?
         * @param _ctorMatchFn the lambda matching function
         * @return true if a 
         */
        default List<_constructor> listConstructors(
                Predicate<_constructor> _ctorMatchFn ) {
            return listConstructors().stream().filter( _ctorMatchFn ).collect( Collectors.toList() );
        }

        /**
         * 
         * @param constructorConsumer
         * @return 
         */
        default _WC forConstructors(Consumer<_constructor> constructorConsumer ) {
            return forConstructors( m -> true, constructorConsumer );
        }

        /**
         * 
         * @param constructorMatchFn
         * @param constructorConsumer
         * @return 
         */
        default _WC forConstructors(
            Predicate<_constructor> constructorMatchFn,
            Consumer<_constructor> constructorConsumer ) {
            listConstructors( constructorMatchFn ).forEach( constructorConsumer );
            return (_WC)this;
        }

        /**
         * remove the constructor defined by this astConstructor
         * @param astConstructor the ast representation of the constructor
         * @return  the modified T         
         */ 
        default _WC removeConstructor(ConstructorDeclaration astConstructor ){
            return removeConstructor( _constructor.of(astConstructor).setName(((_type)this).getName()) );
        }

        /**
         * Remove the constructor and return the modified T
         * @param _ct the constructor instance to remove
         * @return 
         */
        default _WC removeConstructor(_constructor _ct){
            _constructor _cc = _ct.copy().setName( ((_type)this).getName() );
        
            listConstructors( c-> c.equals( _cc ) ).forEach(c-> c.ast().removeForced() );        
            return (_WC)this;
        }

        /**
         * Remove all constructors that match the _constructorMatchFn and return 
         * the modified T
         * @param ctorMatchFn function for matching constructors for removal
         * @return the modified T
         */ 
        default _WC removeConstructors(Predicate<_constructor> ctorMatchFn){
            listConstructors(ctorMatchFn).forEach(c -> removeConstructor(c));
            return (_WC)this;
        }
        
        /**
         * Build and add a constructor based on the contents of the anonymous Object passed in
         *
         * @param anonymousObjectContainingConstructor
         * @return
         */
        default _WC addConstructor(Object anonymousObjectContainingConstructor ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            ObjectCreationExpr oce = Exprs.newExpr(ste);
            MethodDeclaration theMethod = (MethodDeclaration)
                    oce.getAnonymousClassBody().get().stream().filter(m -> m instanceof MethodDeclaration &&
                            !m.isAnnotationPresent(_remove.class) ).findFirst().get();
            //build the base method first
            _constructor _ct = _constructor.of( theMethod.getNameAsString() + " " + _params.of( theMethod )+"{}" );
            //MODIFIERS
            if( theMethod.isPublic() ){
                _ct.setPublic();
            }
            if(theMethod.isProtected()){
                _ct.setProtected();
            }
            if(theMethod.isPrivate()){
                _ct.setPrivate();
            }
            if( !theMethod.getTypeParameters().isEmpty()){
                theMethod.getTypeParameters().forEach(tp -> _ct.getTypeParams().add(tp) );
            }
            _ct.setThrows( theMethod.getThrownExceptions() ); 
            _ct.setBody( theMethod.getBody().get() ); //BODY
            _ct.addAnnoExprs( theMethod.getAnnotations() ); //ANNOTATIONS
            if( theMethod.hasJavaDocComment() ){
                _ct.ast().setJavadocComment( theMethod.getJavadocComment().get());
            }
            addConstructor(_ct);
            return (_WC)this;
        }

        /**
         * Build & add a constructor based on the AST representation
         * @param astConstructor
         * @return 
         */
        _WC addConstructor(ConstructorDeclaration astConstructor );

        /**
         * Build & Add a constructor from the String representation of the code
         * @param ctor the constructor that was built
         * @return 
         */
        default _WC addConstructor(String ctor){
            return addConstructor( new String[]{ctor});
        }
        
        /**
         * builds a {@link _constructor} from a String and adds it to the
         *
         * ALSO supports the "shortCut Constructor" (you can avoid
         * passing in the signature or PARAMETERS
         *
         * 1) shortcut: pass in PARAMETERS and BODY
         * _type _t = _class.of("aaaa.bbb.C");
         * _t.constructor( "(String NAME){ this.NAME = NAME;}" );
         *
         * //builds the following constructor:
         *  public C (String NAME){
         *     this.NAME = NAME;
         *  }
         *
         * 2) shortcut: pass in only BODY
         * _t.constructor("{ System.out.println(100); }");
         *
         * //builds the following constructor:
         *  public C (){
         *     System.out.println(100);
         *  }
         *
         * "{this.}
         * @param constructor
         * @return
         */
        default _WC addConstructor(String... constructor ) {

            String combined = Text.combine(constructor);
            if( combined.startsWith("(")) {
                _constructor _ct = null;
                if( this instanceof _enum ){ //enums inferred private CONSTRUCTORS
                    _ct = _constructor.of(((_type) this).getName() + combined);
                } else {
                    _ct = _constructor.of("public " + ((_type) this).getName() + combined);
                }
                return addConstructor(_ct);
            }
            if( combined.startsWith("{")){ //no arg default public constructor
                _constructor _ct = null;
                if( this instanceof _enum ){ //enums only private CONSTRUCTORS
                    _ct = _constructor.of( ((_type) this).getName() + "()"+combined);
                } else {
                    _ct = _constructor.of("public " + ((_type) this).getName() + "()"+combined);
                }
                return addConstructor(_ct);
            }

            return addConstructor( Ast.constructor( constructor ) );
        }
        
        /**
         * Add the constructor
         * @param _c
         * @return 
         */
        default _WC addConstructor(_constructor _c ) {
            return addConstructor( _c.ast() );
        }
    }

    /**
     * Does this Runtime Constructor match this Ast Constructor Declaration?
     * @param c the runtime constrtuctor
     * @param cd the Ast Constructor declaration
     * @return true if the name and parameters match... (ALSO checking if the Constructor has
     * the first argument as a Instance reference (for member or Local classes)
     *
     */
    public static final boolean isMatch(Constructor c,ConstructorDeclaration cd ){
        //System.out.println( "Constructor "+c);
        //System.out.println( "ConstructorDeclaration "+cd);
        Class declClass = c.getDeclaringClass();
        //if the first parameter is of type
        if( !java.lang.reflect.Modifier.isStatic(c.getModifiers()) &&
                (declClass.isLocalClass() || declClass.isMemberClass() ) ){
            //System.out.println( "non-static local or member class ");
            //if( c.getParameterTypes()[0] == declClass.getDeclaringClass() ){
                Class[] skipFirst = Arrays.copyOfRange( c.getParameterTypes(), 1, c.getParameterCount());
                //local or member classes implicitly pass in the Declaring/Containing class
                //as the first argument in the constructor... however this is not explicitly modeled
                //in the AST, so
                return cd.hasParametersOfType(skipFirst);
            //}
            //System.out.println( "Not first one was of type "+declClass.getDeclaringClass() );
            //throw new _jDraftException("Blah");
        }
        return cd.hasParametersOfType( c.getParameterTypes());
    }
}
