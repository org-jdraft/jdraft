package org.jdraft;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

/**
 * Model of a Java static initializer block
 * static { ... }
 * 
 * There can be one or more static initializer block in each {@link _class} or 
 * {@link _enum}
 * 
 * @author Eric
 */
public final class _staticBlock
        implements _body._hasBody<_staticBlock>, _javadoc._hasJavadoc<_staticBlock>,
        _node<InitializerDeclaration, _staticBlock> {

    public static _staticBlock of( String... body ) {
        InitializerDeclaration id = Ast.staticBlock( body );
        return of( id );
    }

    public static _staticBlock of( InitializerDeclaration id ) {
        return new _staticBlock( id );
    }

    public _staticBlock( InitializerDeclaration id ) {
        this.astStaticInit = id;
    }

    @Override
    public InitializerDeclaration ast() {
        return this.astStaticInit;
    }

    public final InitializerDeclaration astStaticInit;

    @Override
    public _body getBody() {
        return _body.of( astStaticInit );
    }

    /**
     * Build and return a new copy of this ast static block
     * @return 
     */
    @Override
    public _staticBlock copy(){
        return of( this.astStaticInit.toString() );
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
    public _staticBlock setBody( BlockStmt body ) {
        this.astStaticInit.setBody( body );
        return this;
    }

    @Override
    public _staticBlock clearBody() {

        this.astStaticInit.getBody().setStatements( Ast.blockStmt( "{}" ).getStatements() );
        return this;
    }

    @Override
    public _staticBlock add( Statement... statements ) {
        for( int i = 0; i < statements.length; i++ ) {
            this.astStaticInit.getBody().addStatement( statements[ i ] );
        }
        return this;
    }

    @Override
    public _staticBlock add( int startStatementIndex, Statement...statements ){

        for( int i=0;i<statements.length;i++) {
            this.astStaticInit.getBody().addStatement( i+ startStatementIndex, statements[i] );
        }
        return this;
    }

    @Override
    public String toString() {
        return this.astStaticInit.toString();
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
        final _staticBlock other = (_staticBlock)obj;
        if( this.astStaticInit == other.astStaticInit ) {
            return true; //two _staticBlocks pointing to the same InitializerDeclaration
        }
        if( this.hasJavadoc() != other.hasJavadoc() ) {
            return false;
        }
        if( this.hasJavadoc() && !this.getJavadoc().equals( other.getJavadoc() ) ) {
            return false;
        }
        if( !Objects.equals( _body.of( this.astStaticInit ), _body.of( other.astStaticInit ) ) ) {
            return false;
        }
        return true;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put(_java.Component.BODY, getBody() );
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode( _body.of( this.astStaticInit ) );
        return hash;
    }

    @Override
    public _javadoc getJavadoc() {
        return _javadoc.of( this.astStaticInit );
    }

    @Override
    public _staticBlock javadoc( JavadocComment astJavadocComment ){
        this.astStaticInit.setJavadocComment( astJavadocComment );
        return this;
    }
        
    @Override
    public _staticBlock javadoc( String... content ) {
        if( this.astStaticInit.getJavadocComment().isPresent() ) {
            this.astStaticInit.getJavadocComment().get().setContent( Text.combine( content ) );
            return this;
        }
        this.astStaticInit.setJavadocComment( new JavadocComment( Text.combine( content ) ) );
        return this;
    }

    @Override
    public boolean hasJavadoc() {
        return this.astStaticInit.getJavadocComment().isPresent();
    }

    @Override
    public _staticBlock removeJavadoc() {
        this.astStaticInit.removeJavaDocComment();
        return this;
    }

    /**
     * {@_type}s that may contain one or more static initializer blocks
     * @author Eric
     * @param <T>
     */
    public interface _hasStaticBlocks<T extends _hasStaticBlocks & _type>
            extends _java {

        /** 
         * returns the static Blocks on the _type (ordered by when they are declared) 
         * @return 
         */
        default List<_staticBlock> listStaticBlocks(){
            NodeWithMembers nwm = (NodeWithMembers)((_node)this).ast();
            List<_staticBlock> sbs = new ArrayList<>();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration){
                    sbs.add(new _staticBlock( (InitializerDeclaration)mem));
                }
            }
            return sbs;
        }

        /**
         * @param index 
         * @return the index<SUP>th</SUP> static block declared in the _type 
         */
        default _staticBlock getStaticBlock(int index ){
            NodeWithMembers nwm = (NodeWithMembers)((_node)this).ast();
            NodeList<BodyDeclaration<?>> mems = nwm.getMembers();
            for( BodyDeclaration mem : mems ){
                if( mem instanceof InitializerDeclaration){
                    if( index == 0 ){
                        return new _staticBlock( (InitializerDeclaration)mem);
                    }
                    index --;
                }
            }
            return null;
        }
        
        /** 
         * returns the static Blocks on the _type matching the matchFn 
         * @param _staticBlockMatchFn
         * @return 
         */
        default List<_staticBlock> listStaticBlocks( Predicate<_staticBlock>_staticBlockMatchFn){
            return listStaticBlocks().stream().filter(_staticBlockMatchFn).collect(Collectors.toList());
        }

        /** 
         * adds a Static block based on the body of the lambda
         * @param command
         * @return 
         */
        default T staticBlock( Expr.Command command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return staticBlock( Stmt.block(ste));
        }

        /**
         * Apply an "action" function to all static blocks
         * @param _staticBlockAction action to take on static blocks
         * @return the modified T
         */
        default T forStaticBlocks( Consumer<_staticBlock> _staticBlockAction ){
            listStaticBlocks().forEach(_staticBlockAction );
            return (T)this;
        }

        /**
         * Apply an action function to all static blocks that match the function
         * @param _staticBlockMatchFn matches Static blocks to act on
         * @param _staticBlockAction the action to take on matching _staticBlocks
         * @return the modified T
         */
        default T forStaticBlocks(Predicate<_staticBlock> _staticBlockMatchFn, Consumer<_staticBlock> _staticBlockAction ){
            listStaticBlocks(_staticBlockMatchFn).forEach(_staticBlockAction );
            return (T)this;
        }

        /**
         * Build a static Block based on the Lambda Command Body
         * @param command the lambda command body (to get the source of the Static Block)
         * @param <A> the command type
         * @return the modified T
         */
        default <A extends Object> T staticBlock( Consumer<A> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return staticBlock( Stmt.block(ste));
        }

        /**
         * Build a static Block based on the Lambda Body         
         * @param command the lambda command body (to get the source of the Static Block)
         * @param <A> the command type
         * @param <B>
         * @return the modified T
         */
        default <A extends Object, B extends Object> T staticBlock( BiConsumer<A, B> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return staticBlock( Stmt.block(ste));
        }

        /**
         * Build a static Block based on the Lambda Body
         * @param <A> the command type
         * @param <B>
         * @param <C>
         * @param command the lambda command body (to get the source of the Static Block)         
         * @return the modified T
         */
        default <A extends Object, B extends Object, C extends Object> T staticBlock( Expr.TriConsumer<A, B, C> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return staticBlock( Stmt.block(ste));
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
        default <A extends Object, B extends Object, C extends Object, D extends Object> T staticBlock( Expr.QuadConsumer<A, B, C, D> command ){
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            return staticBlock( Stmt.block(ste));
        }

        default boolean hasStaticBlock(){
            return ((TypeDeclaration)((_type)this).ast()).stream().anyMatch( m -> m instanceof InitializerDeclaration );
        }

        default T staticBlock(BlockStmt block){
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addStaticInitializer();
            bs.setStatements( block.getStatements());
            return (T)this;
        }

        default T staticBlock(String... content){
            //reserve the static initializer on the _type            
            BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addStaticInitializer();

            bs.setStatements( Ast.blockStmt( content ).getStatements());
            return (T)this;
        }

        default T staticBlock( _staticBlock sb){
             BlockStmt bs = ((TypeDeclaration)((_type)this).ast()).addStaticInitializer();
             bs.setStatements(sb.astStaticInit.getBody().getStatements());
             return (T)this;
        }
        
        /** 
         * remove the _staticBlock from the _type and return the _type 
         * @param _sb the staticBlock
         * @return the modified T
         */
        default T removeStaticBlock( _staticBlock _sb ){
            this.listStaticBlocks(sb -> sb.equals(_sb))
                .forEach(s -> s.ast().removeForced() );        
            return (T)this;
        }

        /** 
         * remove the static block from the _type and return the _type
         * @param astInitializerDeclaration
         * @return the modified T
         */
        default T removeStaticBlock( InitializerDeclaration astInitializerDeclaration ){
            return removeStaticBlock( _staticBlock.of(astInitializerDeclaration));        
        }        
    }    
}
