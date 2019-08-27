package org.jdraft;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import org.jdraft.macro._remove;

/**
 * The "body" part of methods, constructors, and static blocks (i.e.
 * {@link Statement}s getting stuff done) OR the empty ; (lack of a body)
 * for anonymous 
 *
 * @author Eric
 */
public final class _body implements _java {

    /**
     * NOTE: this is an Object, because it can EITHER be a {@link NodeWithBlockStmt}
     * or {@link NodeWithOptionalBlockStmt} implementation (i.e. for method which can 
     * lack a body, like "abstract m();" )
     * so, internally we just say it's an Object
     */
    private final Object astParentNode;

    /**
     * returns an empty body (i.e. "{}")
     * @return an empty body
     */
    public static _body empty(){
        return of("{}");
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public static _body of( String body ){
        if( body.trim().equals(";")){
            return of( 
                _method.of("void __BODYHOLDER();").ast());
        }
        // "we" need to put the body inside a parent... lets make it a 
        // method

        _method _m = _method.of("void __BODYHOLDER();");
        _m.add(body);
        return of(  _m.ast() );
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public static _body of( String...body ){
        String bd = Text.combine(body).trim();
        if( bd.equals(";")){
            return of( _method.of("void __BODYHOLDER();").ast());
        }
        return of( _method.of("void __BODYHOLDER();").add(body).ast() );
    }
    
    /**
     * 
     * @param statement
     * @return 
     */
    public static _body of( Statement statement ){
        if( statement instanceof BlockStmt ){
            return of( _method.of("void __BODYHOLDER();").setBody((BlockStmt)statement).ast());
        }
        return of( _method.of("void __BODYHOLDER();").add(statement).ast());
    }
    
    /**
     * Here, we create a "body" by passing in an Anonymous class containing
     * a method with a code body... Note we look for the FIRST method declaration 
     * in  the code that has a code body and is NOT annotated with @_remove 
     * and the name, parameters, modifiers, annotations, javadoc of the _method
     * are IGNORED... Likewise other fields, methods, or other members within 
     * the body of the anonymous class are IGNORED (this is useful if the body
     * calls out to a member method that has to be stubbed/implemented...)
     * <PRE>
     * for example:
     * _body _b = _body.of( new Object(){
     * 
     *     void theBody(String aPropertyName){
     *         String m = calledFromBodyRequiredToCompile();
     *         System.setProperty(aPropertyName, m);
     *     }
     * 
     *     //this method exists to allow the body code above to compile
     *     // but it is NOT used
     *     @_remove String calledFromBodyRequiredToCompile(){
     *         return "Doesn't matter";
     *     }
     * });
     * 
     * Where the "body" is these (2) statements:
     * {
     *     String m = calledFromBodyRequiredToCompile();
     *     System.setProperty(aPropertyName, m); 
     * }
     * </PRE>  
     * 
     * @param anonymousClassWithMethodContainingBody the "holder" of the body text
     * @return the _body representing the block statement (or lack of body) of
     * the first method in the anonymous Class without an @_remove annotation
     */
    public static _body of(Object anonymousClassWithMethodContainingBody){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.anonymousObject(ste);
        Optional<BodyDeclaration<?>> on = oce.getAnonymousClassBody().get().stream().filter(m -> 
                m instanceof MethodDeclaration 
                && !((MethodDeclaration)m)
                    .getAnnotationByClass(_remove.class).isPresent() )
                    .findFirst();
        if(!on.isPresent()){
            throw new _draftException("Could not locate the method containing the body in "+ oce);
        }
        MethodDeclaration md = (MethodDeclaration)on.get();
        md.getParentNode().get().remove(md); //decouple it from the "old" 
        return of( md );
    }
    
    /**
     * Builder accepting {@link NodeWithBlockStmt} implementations like
     * {@link com.github.javaparser.ast.body.InitializerDeclaration}, 
     * and {@link com.github.javaparser.ast.body.ConstructorDeclaration}
     * @param astNodeWithBlockStmt the AST Constructor or InitializerDeclaration (static block)
     * @return the _body representation of Statements
     */
    public static _body of(NodeWithBlockStmt astNodeWithBlockStmt) {
        return new _body(astNodeWithBlockStmt);
    }

    /**
     * Builder accepting {@link NodeWithOptionalBlockStmt} implementations like
     * {@link com.github.javaparser.ast.body.InitializerDeclaration}, 
     * and {@link com.github.javaparser.ast.body.ConstructorDeclaration}
     * @param astNodeWithOptionalBlock the AST MethodDeclaration
     * @return the _body representation of Statements
     */
    public static _body of(NodeWithOptionalBlockStmt astNodeWithOptionalBlock) {
        return new _body(astNodeWithOptionalBlock);
    }

    /**
     * Private constructor, lets not confuse everyone by having a single Object
     * constructor on the API
     * @param astParentNode 
     */
    private _body(Object astParentNode) {
        this.astParentNode = astParentNode;
    }

    /**
     * is the body "present" i.e. not null (i.e. even "empty" is implemented)
     * 
     * @return false IFF the body is not there (i.e. for abstract methods "abstract void m();")
     * otherwise, returns true (EVEN FOR EMPTY NO/OP bodies like "void m(){}" )
     */
    public boolean isImplemented() {
        if (astParentNode instanceof NodeWithOptionalBlockStmt) {
            NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt) astParentNode;
            return nobs.getBody().isPresent();
        }
        return true;
    }

    /**
     * Gets the AST parent node... which is either
     * a NodeWithBlockStmt
     * or NodeWithOptionalBlockStmt
     * @return 
     */
    public Object astParentNode(){
        return this.astParentNode;
    }
    
    /**
     * NOTE this could be null
     *
     * @return
     */
    public BlockStmt ast() {
        if (astParentNode instanceof NodeWithOptionalBlockStmt) {
            NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt) astParentNode;
            if (nobs.getBody().isPresent()) {
                return (BlockStmt) nobs.getBody().get();
            }
            return null; //abstract body
        }
        NodeWithBlockStmt nbs = (NodeWithBlockStmt) astParentNode;
        return nbs.getBody();
    }

    /**
     * 
     * @param index
     * @return 
     */
    public Statement getStatement(int index) {
        if( this.isImplemented() ){
            return ast().getStatement(index);
        }
        throw new _draftException("No Statement at ["+index+"] for non-implemented body");
    }

    /**
     * Returns the statements within the body
     * @return 
     */
    public NodeList<Statement> getStatements() {
        if (isImplemented()) {
            return ast().getStatements();
        }
        return null; 
        // returning null as I dont want to return an empty node list that
        // someone might presume will be modifyable ( which the changes WONT be 
        // refleceted in the underlying 
    }

    /**
     * is the _body equal to the String... provided
     * @param body
     * @return 
     */
    public boolean is(String... body) {
        if( body == null || body.length == 0 || (body.length == 1 && body[0].trim().equals(";"))) {
            return !this.isImplemented();
        }
        BlockStmt bs = Ast.blockStmt(body);
        return Objects.equals(this.ast(), bs);
    }

    /**
     * 
     * @return 
     */
    public _body clear(){
        if( this.astParentNode instanceof NodeWithOptionalBlockStmt){
            ((NodeWithOptionalBlockStmt)this.astParentNode).setBody(Ast.blockStmt("{}"));
            return this;
        }
        ((NodeWithBlockStmt)this.astParentNode).setBody(Ast.blockStmt("{}"));
        return this;        
    }
    
    public <A extends Object> boolean is( Expr.Command lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object> boolean is( Consumer<A> lambdaWithBody){
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object, B extends Object> boolean is( Function<A,B> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object, B extends Object,C extends Object> boolean is( BiFunction<A,B,C> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object, B extends Object,C extends Object, D extends Object> boolean is( Expr.TriFunction<A,B,C,D> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> boolean is( Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object, B extends Object> boolean is( BiConsumer<A,B> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
        
    public <A extends Object, B extends Object,C extends Object> boolean is(Expr.TriConsumer<A,B,C> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> boolean is(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){            
        Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return bdy.toString(Ast.PRINT_NO_COMMENTS).equals(this.toString(Ast.PRINT_NO_COMMENTS));        
    }
    
    /**
     * Does a comparison WITHOUT COMMENTS to determine if the
     * statements of the two 
     * 
     * @param bs
     * @return 
     */
    public boolean is(BlockStmt bs) {
        if( bs == null ){
            return !this.isImplemented();
        }
        if( this.isImplemented() ) {
            if( bs.getParentNode().isPresent() 
                && this.astParentNode.equals(bs.getParentNode().get()) ){
                return true;
            }
            NodeList<Statement> ts = this.ast().getStatements();
            NodeList<Statement> os = bs.getStatements();

            if (ts.size() != os.size()) {
                //if we can avoid printing, we should
                return false;
            }
            for(int i=0;i<ts.size();i++){ //this might be improved
                if( !Objects.equals( 
                    ts.get(i).toString(Ast.PRINT_NO_COMMENTS), 
                    os.get(i).toString(Ast.PRINT_NO_COMMENTS) ) ) {
                    return false;
                }                
            }
            return true;
        }
        return false;    
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final _body other = (_body) obj;
        if (this.astParentNode == other.astParentNode) {
            return true; //two different _body referring to the same NodeWithBody (short curcuit)
        }
        if (isImplemented() != other.isImplemented()) {
            return false;
        }
        if (!isImplemented()) {
            return true;
        }

        BlockStmt t = this.ast();
        BlockStmt o = other.ast();

        NodeList<Statement> ts = t.getStatements();
        NodeList<Statement> os = o.getStatements();

        if (ts.size() != os.size()) {
            //if we can avoid printing, we should
            return false;
        }
        String tnc = t.toString(Ast.PRINT_NO_COMMENTS);
        String onc = o.toString(Ast.PRINT_NO_COMMENTS);
        return tnc.equals(onc);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (!isImplemented()) {
            return 0;
        }
        hash = 53 * hash + Objects.hashCode(ast().toString(Ast.PRINT_NO_COMMENTS));
        return hash;
    }

    /**
     * Prints out the body using the pretty printer configuration provided
     *
     * For an EXAMPLE on how to create a PreetyPrinterConfiguration, see:
     * {@link org.jdraft.Ast#PRINT_NO_COMMENTS}
     *
     * @param ppc
     * @return
     */
    public String toString(PrettyPrinterConfiguration ppc) {
        if (!isImplemented()) {
            return "";
        }
        return ast().toString(ppc);
    }

    @Override
    public String toString() {
        if (!isImplemented()) {
            return "";
        }
        return ast().toString();
    }

    /**
     * <PRE>
     * Is the body empty "{}" 
     * NOTE: NOT the same as Not implemented (i.e. like abstract methods:
     * "public abstract void m();")
     * </PRE>
     * @return 
     */
    public boolean isEmpty() {
        return isImplemented() && ast().isEmpty();
    }

    /**
     * Examples {@link _initBlock}, {@link _method}, {@link _constructor}
     *
     * @author Eric
     * @param <_HB> enclosing TYPE (to access this)
     */
    public interface _hasBody<_HB extends _hasBody>
        extends _java {
        
        /**
         * @return gets the body
         */
        _body getBody();

        /**
         * Replace the entire _body with the body passed in
         *
         * @param body the new BODY
         * @return
         */
        _HB setBody(BlockStmt body);

        /**
         * @return return true if the member has a body
         */
        default boolean hasBody() {
            return getBody().isImplemented();
        }

        /**
         * Returns a list of statements
         * NOTE: this lists the "top statements" 
         * (this DOES NOT WALK INTO statements, like compound statements, 
         * statements in for loops and nested blocks)
         * 
         * @return 
         */
        default List<Statement> listStatements(){
            if( this.hasBody() ){
                return getBody().getStatements();
            }
            return Collections.EMPTY_LIST;
        }
        
        /**
         * Returns the statements that are of the StmtClass
         * <PRE>
         * //list all return statements from the class
         * List<ReturnStmt> retSts = _m.listStatements( Ast.RETURN_STMT );
         * List<ThrowStmt> throwSts = _m.listStatements( Ast.THROW_STMT );
         * </PRE>
         * 
         * NOTE: this lists the "top statements" 
         * (this DOES NOT WALK INTO statements, like compound statements, 
         * statements in for loops and nested blocks)
         * @param <S> the statement class
         * @param stmtClass the statement class
         * @return the list of statements of the class
         */
        default <S extends Statement> List<S> listStatements(Class<S> stmtClass ){
            if( this.hasBody() ){
                List<S> stmts = new ArrayList<>();
                getBody().getStatements().stream()
                    .filter(s-> stmtClass.isAssignableFrom(s.getClass()))
                    .forEach(s -> stmts.add( (S)s ));
                return stmts;
            }
            return Collections.EMPTY_LIST;
        }
        
        /**
         * Returns the statements that are of the StmtClass
         * <PRE>
         * //list all return statements from the class
         * //return null;
         * List<ReturnStmt> retSts = _m.listStatements( Ast.RETURN_STMT, r-> Objects.equals( r.getValue(), new NullExpr()) );
         * 
         * List<ThrowStmt> throwSts = _m.listStatements( Ast.THROW_STMT, t-> t.getComent().isPresent() );
         * </PRE>
         * 
         * NOTE: this lists the "top statements" 
         * (this DOES NOT WALK INTO statements, like compound statements, 
         * statements in for loops and nested blocks)
         * @see _walk#list(com.github.javaparser.ast.Node, java.lang.Class)
         * @param <S> the statement class
         * @param stmtClass the statement class
         * @param stmtPredicate 
         * @return the list of statements of the class
         */
        default <S extends Statement> List<S> listStatements(Class<S> stmtClass, Predicate<S> stmtPredicate){
            return listStatements(stmtClass).stream().filter(stmtPredicate).collect(Collectors.toList());
        }
        
        default <A extends Object> _HB setBody(Expr.Command lambdaWithBody){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy); 
        }
        
        default <A extends Object> _HB setBody(Consumer<A> lambdaWithBody){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy); 
        }
        
        default <A extends Object, B extends Object> _HB setBody(Function<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object> _HB setBody(BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _HB setBody(Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> _HB setBody(Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);           
        }
        
        default <A extends Object, B extends Object> _HB setBody(BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object> _HB setBody(Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);            
        }

        default <A extends Object, B extends Object,C extends Object, D extends Object> _HB setBody(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            return setBody(bdy);            
        }
        
        /**
         *
         * @param body
         * @return
         */
        default _HB setBody(_body body) {
            return setBody(body.ast());
        }

        /**
         *
         * @param body
         * @return
         */
        default _HB setBody(String... body) {
            return setBody(Ast.blockStmt(body));
        }

        /**
         * 
         * @param astStmt
         * @return 
         */
        default _HB setBody(Statement astStmt) {
            if (astStmt.isBlockStmt()) {
                return setBody(astStmt.asBlockStmt());
            }
            BlockStmt bs = new BlockStmt();
            bs.addStatement(astStmt);
            return setBody(bs);
        }

        /**
         * clear the contents of the BODY (the statements)
         *
         * @return the modified T
         */
        _HB clearBody();

        /**
         * Add Statements to the end of the BODY
         *
         * @param statements the statements to add to the tail of the BODY
         * @return the modified T (_method, _constructor, _staticBlock)
         */
        _HB add(Statement... statements);
        
        default <A extends Object> _HB add(int index, Expr.Command lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add( index,bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );
        }
        
        default <A extends Object> _HB add(Expr.Command lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add( bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );
        }
        
        default <A extends Object> _HB add(Consumer<A> lambdaWithBody){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add( bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add( bdy );
        }

        default <A extends Object> _HB add(int index, Consumer<A> lambdaWithBody){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add( index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add( index, bdy );
        }
        
        default <A extends Object, B extends Object> _HB add(Function<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object> _HB add(int index, Function<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object> _HB add(BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _HB add(int index, BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object, D extends Object> _HB add(Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _HB add(int index, Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }

                
        default <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _HB add(Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> _HB add(int index, Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object> _HB add(BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object> _HB add(int index, BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object> _HB add(Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _HB add(int index, Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }

        default <A extends Object, B extends Object, C extends Object, D extends Object> _HB add(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _HB add(int index, Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        /**
         * Add Statements to the BODY starting at startStatementIndex (0-based)
         *
         * @param startStatementIndex index of the position to add the
         * statements
         * @param statements the statements to add to the tail of the BODY
         * @return the modified T (_method, _constructor, _staticBlock)
         */
        _HB add(int startStatementIndex, Statement... statements);

        /**
         * Add Statements to the end of the BODY
         *
         * @param statements the statements as an Array of Strings
         * @return the modified T (_method, _constructor, _staticBlock)
         */
        default _HB add(String... statements) {
            BlockStmt bs = Ast.blockStmt(statements);

            if( !this.getBody().isImplemented() ){ //empty body
                if( bs.getStatements().isEmpty() ) {
                    //System.out.println("creating empty body");
                    this.setBody("{}");
                    return (_HB)this;
                } else{
                    this.setBody(bs);
                    return (_HB)this;
                }
            }

            //organize orphan comments
            List<Comment> coms = bs.getOrphanComments();
            Comment c = null;
            if (coms.size() > 0) {
                c = coms.get(0);
            }
            Collections.sort(coms, new Ast.CommentPositionComparator());

            for (int i = 0; i < bs.getStatements().size(); i++) {
                Statement st = bs.getStatement(i);
                if (c != null && c.getBegin().get().isBefore(st.getBegin().get())) {
                    getBody().ast().addOrphanComment(c);
                    coms.remove(0);
                    if (!coms.isEmpty()) {
                        c = coms.get(0);
                    } else {
                        c = null;
                    }
                }
                if( getBody().astParentNode() instanceof NodeWithOptionalBlockStmt){
                    NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt)getBody().astParentNode;
                    if( !nobs.getBody().isPresent()){
                        BlockStmt bss = nobs.createBody();
                        bs.getStatements().forEach(s -> bss.addStatement(s));
                    } else{
                        ((BlockStmt)nobs.getBody().get()).addStatement(st);
                    }
                } else{
                    getBody().ast().addStatement(st);
                }
            }
            coms.forEach(co -> getBody().ast().addOrphanComment(co));
            return (_HB) this;
        }

        /**
         * Add Statements to the BODY starting at the startStatementIndex
         * (0-based)
         *
         * @param statementStartIndex the statement index (0 is the first
         * statement)
         * @param statements a Block of Strings representing statements (this is
         * first parsed before added
         * @return the modified T
         */
        default _HB add(int statementStartIndex, String... statements) {
            BlockStmt bs = Ast.blockStmt(statements);
            return add(statementStartIndex, bs.getStatements().toArray(new Statement[0]));
        }

        /**
         * Counts the number of statements within this method... 
         * Note: this is the "top level" statements, 
         * <PRE>
         * _method _m = _method.of("public void m(){", 
         *     "assertTrue(true);",
         *     "}");
         * assertTrue( 1, _m.countStatements());
         * _method _m = _method.of("public void m(){", 
         *     "    if( i==0){"
         *     "        assert(true);",
         *     "        System.out.println(1);
         *     "    }" 
         *     "}");
         * //there is only (1) top level statement, (the if statement) 
         * assertTrue( 1, _m.countStatements());
         * 
         * </PRE>
         * 
         * @return the number of "top level" Statements of this body (0 for unimplmented)
         */
        default int statementCount(){
            return this.listStatements().size();
        }
        
        /**
         * Gets the statement at the statement index, throws an exception if the 
         * Statement index is out of bounds
         * @param statementIndex
         * @return 
         */
        default Statement getStatement(int statementIndex){
            return this.listStatements().get(statementIndex);
        }
                
        default <A extends Object> _HB addAt(String labelName, Expr.Command lambdaWithBody){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt( labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );
        }
        
        default <A extends Object> _HB addAt(String labelName, Consumer<A> lambdaWithBody){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt( labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );
        }
        
        default <A extends Object, B extends Object> _HB addAt(String labelName, Function<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _HB addAt(String labelName, BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _HB addAt(String labelName, Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> _HB addAt(String labelName, Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object> _HB addAt(String labelName, BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _HB addAt(String labelName, Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }

        default <A extends Object, B extends Object,C extends Object, D extends Object> _HB addAt(String labelName, Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambda.from( Thread.currentThread().getStackTrace()[2]).getBody(); 
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        /**
         * Adds Statements to the end of the labeled Statement with NAME
         * "labelName"
         *
         * @param labelName the label to populate with the statements
         * @param statements the statements to add
         * @return the modified T
         */
        default _HB addAt(String labelName, String... statements) {
            if( !isImplemented() ){
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\"");
            }
            Statement st = ols.get().getStatement();
            if (st.isBlockStmt()) {
                BlockStmt bs = st.asBlockStmt();
                NodeList<Statement> stmts = Ast.blockStmt(statements).getStatements();
                stmts.forEach(s -> bs.addStatement(s));
            } else {
                BlockStmt bs = Ast.blockStmt(statements);
                bs.addStatement(0, st);
                st.replace(bs);
            }
            return (_HB) this;
        }
        
        default _HB addAt(String labelName, Statement...statements){
             if( !isImplemented() ){
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\"");
            }
            Statement st = ols.get().getStatement();
            if (st.isBlockStmt()) { //the labeled statement is a block statement
                BlockStmt bs = st.asBlockStmt();
                NodeList<Statement> stmts = Ast.blockStmt(statements).getStatements();
                stmts.forEach(s -> bs.addStatement(s));
            } else { //a simple label statement, need to convert it to a block
                BlockStmt bs = Ast.blockStmt(statements);
                bs.addStatement(0, st);
                st.replace(bs);
            }
            return (_HB) this;
        }

        /**
         * Is there an implementation (even an empty one like "m(){}")
         * as apposed to an abstract implementation like "m();"
         * @return true if the body is NOT abstract (there is an implementation block) 
         * false otherwise
         */
        default boolean isImplemented(){
            return getBody().isImplemented();
        }
        
        /**
         * Does the body contain a LabeledStatement  with the label name
         * (i.e.)
         * "outer: return false;"
         * 
         * @param labelName the name of the label (i.e. "outer" from above)
         * @return true if the 
         */
        default boolean hasLabel(String labelName) {            
            return this.isImplemented() && 
                this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName)).isPresent();
        }

        /**
         * Appends an Ast Statement at the END of the label  
         * @param labelName the name of the label where the code will be added
         * @param astStmt
         * @return the modified entity
         */
        default _HB addAt(String labelName, Statement astStmt) {
            if( !isImplemented() ){
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            List<Statement> stmts = new ArrayList<>();
            stmts.add(astStmt);
            return addAt(labelName, stmts);
        }

        /**
         * Appends Ast Statements to the label within the code 
         * (always adds at the tail/ end if the label already contains statements)
         * @param labelName the name of the label where statements are to be added
         * @param stmts a list of Statements to add at the label
         * @return the modified entity
         */
        default _HB addAt(String labelName, List<Statement> stmts) {
            if( !isImplemented() ){
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                    = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\"");
            }
            Statement st = ols.get().getStatement();
            if (st.isBlockStmt()) {
                BlockStmt bs = st.asBlockStmt();
                stmts.forEach(s -> bs.addStatement(s));
            } else {
                BlockStmt bs = Ast.blockStmt(stmts.toArray(new Statement[0]));
                bs.addStatement(0, st);
                st.replace(bs);
            }
            return (_HB) this;
        }

        /**
         * Gets the Labeled statement based on the label
         *
         * @param labelName
         * @return the Labeled Statement or THROWS EXCEPTION if not found
         */
        default LabeledStmt getAt(String labelName) {
            if( !isImplemented() ){
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _draftException("cannot find labeled Statement \"" + labelName + "\"");
            }
            return ols.get();
        }

        /**
         * For each Expression in the body, call the exprActionFn lambda
         * @param exprActionFn lambda to call on all expressions
         * @return  the T
         */
        default _HB forExprs(Consumer<Expression> exprActionFn ){
            if( !isImplemented() ){
                return (_HB)this;
            }
            _walk.in((BlockStmt)this.getBody().ast(), Expression.class, exprActionFn );
            return (_HB)this;
        }

        /**
         * _walk the body in preorder fashion, intercepting all {@link Expression}s
         * that implement exprClass and calling the exprActionFn
         * 
         * @see _walk#in(Node.TreeTraversal, Node, Class, Predicate, Consumer)
         * @see _walk#in(com.github.javaparser.ast.Node, java.lang.Class, java.util.function.Predicate, java.util.function.Consumer)
         * 
         * @param <E> the underlying target Expression class
         * @param exprClass the target Expression class
         * @param exprActionFn the "processing" function for matching exprs
         * @return the modified T
         */
        default <E extends Expression> _HB forExprs(Class<E> exprClass, Consumer<E> exprActionFn ){
            if( !isImplemented() ){
                return (_HB)this;
            }                     
            _walk.in((BlockStmt)this.getBody().ast(), exprClass, exprActionFn );
            return (_HB)this;
        }
        
        /**
         * _walk the body in preorder fashion, intercepting all {@link Expression}s
         * that implement exprClass and match the exprMatchFn and processing
         * all of which with the exprActionFn
         * 
         * @see _walk#in(com.github.javaparser.ast.Node.TreeTraversal, com.github.javaparser.ast.Node, java.lang.Class, java.util.function.Predicate, java.util.function.Consumer)
         * @see _walk#in(com.github.javaparser.ast.Node, java.lang.Class, java.util.function.Predicate, java.util.function.Consumer)
         * 
         * @param <E> the underlying target Expression class
         * @param exprClass the target Expression class
         * @param exprMatchFn the matching function to choose which exprs to process
         * @param exprActionFn the "processing" function for matching exprs
         * @return the modified T
         */
        default <E extends Expression> _HB forExprs(Class<E> exprClass, Predicate<E>exprMatchFn, Consumer<E> exprActionFn ){
            if( !isImplemented() ){
                return (_HB)this;
            }
            _walk.in((BlockStmt)this.getBody().ast(), exprClass, exprMatchFn, exprActionFn );
            return (_HB)this;
        }
        
        /**
         * _walk the body in preorder fashion, intercepting all Statements that
         * implement statementClass and match the stmtMatchFn and processing
         * all of which with the statConsumer
         * @param stmtActionFn the "processing" function for matching statements
         * @return the modified T
         */
        default _HB forStmts(Consumer<Statement> stmtActionFn ){
            if( !isImplemented() ){
                return (_HB)this;
            }           
            _walk.in((BlockStmt)this.getBody().ast(), Statement.class, stmtActionFn );
            return (_HB)this;
        }
        
        /**
         * _walk the body in preorder fashion, intercepting all Statements that
         * implement statementClass and match the stmtMatchFn and processing
         * all of which with the statConsumer
         * @param <S> the underlying target Statement class
         * @param statementClass the target statement class
         * @param stmtActionFn the "processing" function for matching statements
         * @return the modified T
         */
        default <S extends Statement> _HB forStmts(Class<S> statementClass, Consumer<S> stmtActionFn ){
            if( !isImplemented() ){
                return (_HB)this;
            }
            _walk.in((BlockStmt)this.getBody().ast(), statementClass, stmtActionFn );
            return (_HB)this;
        }
        
        /**
         * _walk the body in preorder fashion, intercepting all Statements that
         * implement statementClass and match the stmtMatchFn and processing
         * all of which with the statConsumer
         * @param <S> the underlying target Statement class
         * @param statementClass the target statement class
         * @param stmtMatchFn the matching function to choose which statements to process
         * @param stmtActionFn the "processing" function for matching statements
         * @return the modified T
         */
        default <S extends Statement> _HB forStmts(Class<S> statementClass, Predicate<S>stmtMatchFn, Consumer<S> stmtActionFn ){
            if( !isImplemented() ){
                return (_HB)this;
            }
            _walk.in((BlockStmt)this.getBody().ast(), statementClass, stmtMatchFn, stmtActionFn );
            return (_HB)this;
        }
        
        /**
         * Find and return the first Statement of the statementClass
         * (i.e.
         * <PRE>
         * ReturnStmt rs = _body.firstStmt(ReturnStmt.class);
         * ReturnStmt rs = _body.firstStmt(Stmt.RETURN);
         * </PRE>
         * @see Stmt 
         * 
         * @param <S>
         * @param stmtClass the specific Statement class 
         * @return the first matching statement or null if no statements match
         */
        default <S extends Statement> S firstStmt( Class<S> stmtClass ){
            return firstStmt(stmtClass, s->true);
        }
        
         /**
         * Find and return the first Statement of the statementClass that matches 
         * the stmtMatchFn
         * (i.e.
         * <PRE>
         * IfStmt rs = _body.firstStmt(IfStmt.class, i->i.hasElse() );
         * IfStmt rs = _body.firstStmt(Stmt.IF, i->i.hasElse() );
         * </PRE>
         * @see Stmt 
         * 
         * @param <S>
         * @param stmtClass the specific Statement class 
         * @param stmtMatchFn 
         * @return the first matching statement or null if no statements match
         */
        default <S extends Statement> S firstStmt( Class<S> stmtClass, Predicate<S> stmtMatchFn ){
            if( !isImplemented() ){
                return null;
            }
            Optional<S> of = getBody().ast().findFirst(stmtClass, stmtMatchFn);
            if (of.isPresent()) {
                return of.get();
            }
            return null;
        }
        
        /**
         * Find and return the first Statement of the statementClass
         * (i.e.
         * <PRE>
         * BinaryExpr be = _body.firstExpr(BinaryExpr.class);
         * BinaryExpr be = _body.firstExpr(Expr.BINARY);
         * </PRE>
         * @see Expr
         * 
         * @param <E>
         * @param exprClass the specific Statement class 
         * @return the first matching statement or null if no statements match
         */
        default <E extends Expression> E firstExpr( Class<E> exprClass ){
            return firstExpr(exprClass, e->true);
        }
        
         /**
         * Find and return the first Statement of the statementClass that matches 
         * the stmtMatchFn
         * (i.e.
         * <PRE>
         * IfStmt rs = _body.firstStmt(IfStmt.class, i->i.hasElse() );
         * IfStmt rs = _body.firstStmt(Stmt.IF, i->i.hasElse() );
         * </PRE>
         * @see Stmt 
         * 
         * @param <E>
         * @param exprClass the specific Expression class 
         * @param exprMatchFn the matching lambda function for the Expression
         * @return the first matching statement or null if no statements match
         */
        default <E extends Expression> E firstExpr( Class<E> exprClass, Predicate<E> exprMatchFn ){
            if( !isImplemented() ){
                return null;
            }
            Optional<E> of = getBody().ast().findFirst(exprClass, exprMatchFn);
            if (of.isPresent()) {
                return of.get();
            }
            return null;
        }        
    }    
}
