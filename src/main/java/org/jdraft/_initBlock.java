package org.jdraft;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.macro._static;
import org.jdraft.text.Text;

/**
 * Representation of the source code of a Java initializer block:
 * i.e.:<PRE>
 * class C{}
 *  static { ... } //a static initializer block
 *  { ...} // a non-static (instance) initializer block called before constructor
 * }
 * </PRE>
 * 
 * There can be one or more static initializer block in each {@link _class} or 
 * {@link _enum}
 * 
 * @author Eric
 */
public final class _initBlock
        implements _body._withBody<_initBlock>, _javadocComment._withJavadoc<_initBlock>,
        _java._member<InitializerDeclaration, _initBlock> {

    public static final Function<String, _initBlock> PARSER = s-> _initBlock.of(s);

    /**
     *
     *
     * <PRE>
     * i.e. _initBlock.of( new Object(){
     *     { System.out.println(1); }
     * } );
     *
     * for a static block:
     * _initBlock.of( new @_static Object(){
     *     { System.out.println( 1 ); }
     * } );
     * </PRE>
     * @param anonymousObject
     * @return
     */
    public static _initBlock of( Object anonymousObject ){
        ObjectCreationExpr oce = Expr.newExpr(Thread.currentThread().getStackTrace()[2]);

        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        InitializerDeclaration id =
                (InitializerDeclaration)bds.stream().filter( b -> b instanceof InitializerDeclaration ).findFirst().get();
        id.removeForced();
        if( anonymousObject.getClass().getAnnotation(_static.class) != null ){
            id.setStatic(true);
        }
        return of(id);
    }

    public static _initBlock of(){
        return of("{}");
    }

    public static _initBlock of(String body ) {
        InitializerDeclaration id = Ast.initializerDeclaration( body );
        return of( id );
    }

    public static _initBlock of(String... body ) {
        InitializerDeclaration id = Ast.initializerDeclaration( body );
        return of( id );
    }

    public static _initBlock of(Statement st ){
        if( st instanceof BlockStmt ){
            return of( (BlockStmt)st);
        }
        BlockStmt bs = new BlockStmt().addStatement(st);
        return of( bs );
    }

    public static _initBlock of(BlockStmt body ){
        InitializerDeclaration id = new InitializerDeclaration(true, body);
        return new _initBlock(id);
    }

    public static _initBlock of(InitializerDeclaration id ) {
        return new _initBlock( id );
    }

    public static _initBlock of(Expr.Command lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        if( bdy instanceof BlockStmt ) {
            return of( (BlockStmt)bdy);
        }
        return of( new BlockStmt().addStatement(bdy) );
    }

    public static <A extends Object> _initBlock of (Consumer<A> lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        if( bdy instanceof BlockStmt ) {
            return of( (BlockStmt)bdy);
        }
        return of( new BlockStmt().addStatement(bdy) );
    }

    public static <A extends Object, B extends Object> _initBlock of(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        if( bdy instanceof BlockStmt ) {
            return of( (BlockStmt)bdy);
        }
        return of( new BlockStmt().addStatement(bdy) );
    }

    public static <A extends Object, B extends Object,C extends Object> _initBlock of(Expr.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        if( bdy instanceof BlockStmt ) {
            return of( (BlockStmt)bdy);
        }
        return of( new BlockStmt().addStatement(bdy) );
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _initBlock of(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        if( bdy instanceof BlockStmt ) {
            return of( (BlockStmt)bdy);
        }
        return of( new BlockStmt().addStatement(bdy) );
    }

    public _initBlock(){
        this(new InitializerDeclaration());
    }

    public _initBlock(InitializerDeclaration id ) {
        this.astInit = id;
    }

    @Override
    public InitializerDeclaration ast() {
        return this.astInit;
    }

    public static _feature._one<_initBlock, Boolean> IS_STATIC = new _feature._one<>(_initBlock.class, Boolean.class,
            _feature._id.IS_STATIC,
            a -> a.isStatic(),
            (_initBlock a, Boolean b) -> a.setStatic(b), PARSER);

    public static _feature._one<_initBlock, _body> BODY = new _feature._one<>(_initBlock.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_initBlock a, _body b) -> a.setBody(b), PARSER);

    public static _feature._meta<_initBlock> META = _feature._meta.of(_initBlock.class, IS_STATIC, BODY);

    public final InitializerDeclaration astInit;

    @Override
    public _body getBody() {
        return _body.of(astInit);
    }

    /**
     * Build and return a new copy of this ast static block
     * @return 
     */
    @Override
    public _initBlock copy(){
        return of( this.astInit.toString() );
    }
    
    @Override
    public boolean is( String... code ) {
        try {
            return of(code).equals(this);
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean is( InitializerDeclaration astId){
        return of(astId).equals(this);
    }

    @Override
    public _initBlock setBody(BlockStmt body ) {
        this.astInit.setBody( body );
        return this;
    }

    public _initBlock setStatic(){
        return setStatic(true);
    }

    public _initBlock setStatic( boolean toSet){
        this.astInit.setStatic(toSet);
        return this;
    }

    public boolean isStatic(){
        return this.astInit.isStatic();
    }

    @Override
    public _initBlock clearBody() {

        this.astInit.getBody().setStatements( Ast.blockStmt( "{}" ).getStatements() );
        return this;
    }

    @Override
    public _initBlock add(Statement... statements ) {
        for( int i = 0; i < statements.length; i++ ) {
            this.astInit.getBody().addStatement( statements[ i ] );
        }
        return this;
    }

    @Override
    public _initBlock add(int startStatementIndex, Statement...statements ){

        for( int i=0;i<statements.length;i++) {
            this.astInit.getBody().addStatement( i+ startStatementIndex, statements[i] );
        }
        return this;
    }

    @Override
    public String toString() {
        return this.astInit.toString();
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
        final _initBlock other = (_initBlock)obj;
        if( this.astInit == other.astInit) {
            return true; //two _staticBlocks pointing to the same InitializerDeclaration
        }
        if( this.hasJavadoc() != other.hasJavadoc() ) {
            return false;
        }
        if( this.hasJavadoc() && !this.getJavadoc().equals( other.getJavadoc() ) ) {
            return false;
        }
        if( !Objects.equals( _body.of( this.astInit), _body.of( other.astInit) ) ) {
            return false;
        }
        return true;
    }

    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put(_java.Feature.BODY, getBody() );
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode( _body.of( this.astInit) );
        return hash;
    }

    //@Override
    //public _javadoc getJavadoc() {
    //    return _javadoc.of( this.astInit);
    //}
    @Override
    public _javadocComment getJavadoc() {
        if( this.astInit.getJavadocComment().isPresent() ){
            return _javadocComment.of( this.astInit.getJavadocComment().get());
        }
        return null;
    }

    @Override
    public _initBlock setJavadoc(JavadocComment astJavadocComment ){
        this.astInit.setJavadocComment( astJavadocComment );
        return this;
    }
        
    @Override
    public _initBlock setJavadoc(String... content ) {
        if( this.astInit.getJavadocComment().isPresent() ) {
            this.astInit.getJavadocComment().get().setContent( Text.combine( content ) );
            return this;
        }
        this.astInit.setJavadocComment( new JavadocComment( Text.combine( content ) ) );
        return this;
    }

    @Override
    public boolean hasJavadoc() {
        return this.astInit.getJavadocComment().isPresent();
    }

    //@Override
    public _initBlock removeJavadoc() {
        this.astInit.removeJavaDocComment();
        return this;
    }

    /**
     * {@_type}s that may contain one or more static initializer blocks
     * @author Eric
     * @param <_WIB>
     */
    public interface _withInitBlocks<_WIB extends _withInitBlocks & _type>
            extends _java._domain {

        /** 
         * returns the initializer blocks (static or instance) on the _type (ordered by where they are declared)
         * @return 
         */
        default List<_initBlock> listInitBlocks(){
            NodeWithMembers nwm = (NodeWithMembers)((_java._node)this).ast();
            List<_initBlock> sbs = new ArrayList<>();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration){
                    sbs.add(new _initBlock( (InitializerDeclaration)mem));
                }
            }
            return sbs;
        }


        /**
         * returns the static Blocks on the _type (ordered by when they are declared)
         * @return
         */
        default List<_initBlock> listStaticBlocks(){
            NodeWithMembers nwm = (NodeWithMembers)((_java._node)this).ast();
            List<_initBlock> sbs = new ArrayList<>();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration && mem.asInitializerDeclaration().isStatic()){
                    sbs.add(new _initBlock( (InitializerDeclaration)mem));
                }
            }
            return sbs;
        }

        /**
         * return a list of matching _initBlock(s) that are static
         * @param _matchFn
         * @return
         */
        default List<_initBlock> listStaticBlocks(Predicate<_initBlock> _matchFn){
            NodeWithMembers nwm = (NodeWithMembers)((_java._node)this).ast();
            List<_initBlock> sbs = new ArrayList<>();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration && mem.asInitializerDeclaration().isStatic()){
                    _initBlock _ib = _initBlock.of((InitializerDeclaration)mem );
                    if( _matchFn.test(_ib)) {
                        sbs.add(_ib);
                    }
                }
            }
            return sbs;
        }

        default _initBlock getStaticBlock(int index){
            NodeWithMembers nwm = (NodeWithMembers)((_java._node)this).ast();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration && mem.asInitializerDeclaration().isStatic()){
                    if( index == 0 ){
                        return new _initBlock( (InitializerDeclaration)mem);
                    }
                    index --;
                }
            }
            return null;
        }

        /**
         * @param index 
         * @return the index<SUP>th</SUP> static block declared in the _type 
         */
        default _initBlock getInitBlock(int index ){
            NodeWithMembers nwm = (NodeWithMembers)((_java._node)this).ast();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration){
                    if( index == 0 ){
                        return new _initBlock( (InitializerDeclaration)mem);
                    }
                    index --;
                }
            }
            return null;
        }

        /**
         * finds the first initBlock that matched with _initBlockMatchFn
         * @param _initBlockMatchFn
         * @return
         */
        default _initBlock getInitBlock( Predicate<_initBlock> _initBlockMatchFn){
            List<_initBlock> ibs = listInitBlocks(_initBlockMatchFn);
            if( ibs.isEmpty()){
                return null;
            }
            return ibs.get(0);
        }
        
        /** 
         * returns the static Blocks on the _type matching the matchFn 
         * @param _staticBlockMatchFn
         * @return 
         */
        default List<_initBlock> listInitBlocks(Predicate<_initBlock>_staticBlockMatchFn){
            return listInitBlocks().stream().filter(_staticBlockMatchFn).collect(Collectors.toList());
        }

        /**
         * Apply an "action" function to all static blocks
         * @param _staticBlockAction action to take on static blocks
         * @return the modified T
         */
        default _WIB forInitBlocks(Consumer<_initBlock> _staticBlockAction ){
            listInitBlocks().forEach(_staticBlockAction );
            return (_WIB)this;
        }

        /**
         * Apply an action function to all static blocks that match the function
         * @param _staticBlockMatchFn matches Static blocks to act on
         * @param _staticBlockAction the action to take on matching _staticBlocks
         * @return the modified T
         */
        default _WIB forInitBlocks(Predicate<_initBlock> _staticBlockMatchFn, Consumer<_initBlock> _staticBlockAction ){
            listInitBlocks(_staticBlockMatchFn).forEach(_staticBlockAction );
            return (_WIB)this;
        }

        /**
         * Check if all individual arg ({@link _initBlock}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allInitBlocks( Predicate<_initBlock> matchFn){
            return listInitBlocks().stream().allMatch(matchFn);
        }

        /**
         * adds a Static block based on the body of the lambda
         * @param command
         * @return
         */
        default _WIB addStaticBlock(Expr.Command command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addStaticBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Command Body
         * @param command the lambda command body (to get the source of the Static Block)
         * @param <A> the command type
         * @return the modified T
         */
        default <A extends Object> _WIB addStaticBlock(Consumer<A> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addStaticBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Body
         * @param command the lambda command body (to get the source of the Static Block)
         * @param <A> the command type
         * @param <B>
         * @return the modified T
         */
        default <A extends Object, B extends Object> _WIB addStaticBlock(BiConsumer<A, B> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addStaticBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Body
         * @param <A> the command type
         * @param <B>
         * @param <C>
         * @param command the lambda command body (to get the source of the Static Block)
         * @return the modified T
         */
        default <A extends Object, B extends Object, C extends Object> _WIB addStaticBlock(Expr.TriConsumer<A, B, C> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addStaticBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Body
         * @param <A> the command type
         * @param <B>
         * @param <C>
         * @param <D>
         * @param command the lambda command body (to get the source of the Static Block)
         * @return the modified T
         */
        default <A extends Object, B extends Object, C extends Object, D extends Object> _WIB addStaticBlock(Expr.QuadConsumer<A, B, C, D> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addStaticBlock( Stmt.blockStmt(ste));
        }

        /**
         * builds and adds a new staticBlock
         * @param block the blockStmt
         * @return the modified block container
         */
        default _WIB addStaticBlock(BlockStmt block){
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addStaticInitializer();
            bs.setStatements( block.getStatements());
            return (_WIB)this;
        }

        /**
         *
         * @param anonymousObjectWithInitBlock
         * @return
         */
        default _WIB addStaticBlock(Object anonymousObjectWithInitBlock){
            ObjectCreationExpr oce = Expr.newExpr( Thread.currentThread().getStackTrace()[2] );
            InitializerDeclaration id =
                    (InitializerDeclaration)oce.getAnonymousClassBody().get().stream().filter(t-> t instanceof InitializerDeclaration).findFirst().get();
            id.setStatic(true);
            return addInitBlock( _initBlock.of(id) );
        }

        /**
         *
         * @param content
         * @return
         */
        default _WIB addStaticBlock(String  content){
            return addStaticBlock(new String[]{content});
        }

        /**
         *
         * @param content
         * @return
         */
        default _WIB addStaticBlock(String... content){
            //reserve the static initializer on the _type
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addStaticInitializer();

            bs.setStatements( Ast.blockStmt( content ).getStatements());
            return (_WIB)this;
        }

        /**
         *
         * @param sb
         * @return
         */
        default _WIB addStaticBlock(_initBlock sb){
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addStaticInitializer();
            bs.setStatements(sb.astInit.getBody().getStatements());
            return (_WIB)this;
        }

        /**
         * adds a Static block based on the body of the lambda
         * @param command
         * @return
         */
        default _WIB addInitBlock(Expr.Command command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addInitBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Command Body
         * @param command the lambda command body (to get the source of the Static Block)
         * @param <A> the command type
         * @return the modified T
         */
        default <A extends Object> _WIB addInitBlock(Consumer<A> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addInitBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Body         
         * @param command the lambda command body (to get the source of the Static Block)
         * @param <A> the command type
         * @param <B>
         * @return the modified T
         */
        default <A extends Object, B extends Object> _WIB addInitBlock(BiConsumer<A, B> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addInitBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Body
         * @param <A> the command type
         * @param <B>
         * @param <C>
         * @param command the lambda command body (to get the source of the Static Block)         
         * @return the modified T
         */
        default <A extends Object, B extends Object, C extends Object> _WIB addInitBlock(Expr.TriConsumer<A, B, C> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addInitBlock( Stmt.blockStmt(ste));
        }

        /**
         * Build a static Block based on the Lambda Body
         * @param <A> the command type
         * @param <B>
         * @param <C>
         * @param <D>
         * @param command the lambda command body (to get the source of the Static Block)         
         * @return the modified T
         */
        default <A extends Object, B extends Object, C extends Object, D extends Object> _WIB addInitBlock(Expr.QuadConsumer<A, B, C, D> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return addInitBlock( Stmt.blockStmt(ste));
        }

        /**
         *
         * @param block
         * @return
         */
        default _WIB addInitBlock(BlockStmt block){
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addInitializer();
            bs.setStatements( block.getStatements());
            return (_WIB)this;
        }

        /**
         *
         * @param anonymousObjectWithInitBlock
         * @return
         */
        default _WIB addInitBlock(Object anonymousObjectWithInitBlock){
            ObjectCreationExpr oce = Expr.newExpr( Thread.currentThread().getStackTrace()[2] );
            InitializerDeclaration id =
                    (InitializerDeclaration)oce.getAnonymousClassBody().get().stream().filter(t-> t instanceof InitializerDeclaration).findFirst().get();
            if( anonymousObjectWithInitBlock.getClass().getAnnotation(_static.class) != null){
                id.setStatic(true);
            }
            return addInitBlock( _initBlock.of(id) );
        }

        /**
         *
         * @param content
         * @return
         */
        default _WIB addInitBlock(String  content){
            return addInitBlock(new String[]{content});
        }

        /**
         *
         * @param content
         * @return
         */
        default _WIB addInitBlock(String... content){
            //reserve the static initializer on the _type
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addInitializer();

            bs.setStatements( Ast.blockStmt( content ).getStatements());
            return (_WIB)this;
        }

        /**
         *
         * @param sb
         * @return
         */
        default _WIB addInitBlock(_initBlock sb){
            BlockStmt bs = null;
            if( sb.isStatic() ) {
                bs = ((TypeDeclaration) ((_type) this).ast()).addStaticInitializer();
            }else{
                bs = ((TypeDeclaration) ((_type) this).ast()).addInitializer();
            }
            bs.setStatements(sb.astInit.getBody().getStatements());
            return (_WIB)this;
        }

        /**
         * returns true if the TypeDeclaration_model instance has one or more _initBlocks)
         * @return
         */
        default boolean hasInitBlocks(){
            return ((TypeDeclaration)((_type)this).ast()).stream().anyMatch( m -> m instanceof InitializerDeclaration );
        }

        /**
         * Returns true if the entity has one (or more) static initializer blocks
         * @return true if the entity has one (or more) static initializer blocks
         */
        default boolean hasStaticBlocks(){
            return ((TypeDeclaration)((_type)this).ast()).stream().anyMatch( m -> m instanceof InitializerDeclaration && ((InitializerDeclaration) m).asInitializerDeclaration().isStatic());
        }

        /** 
         * remove the _staticBlock from the _type and return the _type 
         * @param _sb the staticBlock
         * @return the modified T
         */
        default _WIB removeInitBlock(_initBlock _sb ){
            this.listInitBlocks(sb -> sb.equals(_sb))
                .forEach(s -> s.ast().removeForced() );        
            return (_WIB)this;
        }

        /** 
         * remove the static block from the _type and return the _type
         * @param astInitializerDeclaration
         * @return the modified T
         */
        default _WIB removeInitBlock(InitializerDeclaration astInitializerDeclaration ){
            return removeInitBlock( _initBlock.of(astInitializerDeclaration));
        }        
    }    
}
