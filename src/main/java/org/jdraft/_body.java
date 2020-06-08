package org.jdraft;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import org.jdraft.macro._remove;
import org.jdraft.text.Text;

/**
 * The "body" part of methods, constructors, and static blocks (i.e.
 * {@link Statement}s getting stuff done) OR the empty ; (lack of a body)
 * for anonymous 
 *
 * @author Eric
 */
public final class _body implements _java._domain {

    public static final Function<String, _body> PARSER = s-> _body.of(s);

    /**
     * NOTE: this is an Object, because it can EITHER be a {@link NodeWithBlockStmt}
     * or {@link NodeWithOptionalBlockStmt} (i.e. for method which can
     * lack a body, like "abstract m();" )
     * or {@link NodeWithBody} implementation which can be a single statement:
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
            return of(_method.of("void UNKNOWN();").ast());
        }
        // "we" need to put the body inside a parent... lets make it a 
        // method

        //_method _m = _method.of("void __BODYHOLDER();");
        //the INTENT was to give the body (braces and all)
        if( body.startsWith("{") && body.endsWith("}")){
            _constructor _c = _constructor.of("UNKNOWN()" + body);
            return of(_c.ast());
        } else {
            _constructor _c = _constructor.of("UNKNOWN(){" + System.lineSeparator() + body + System.lineSeparator() + "}");
            //_c.add(body);
            return of(_c.ast());
        }
    }

    public static _body of( Expr.Command ec ){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( Supplier<? extends Object> s){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( Consumer<? extends Object> s){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( BiConsumer<? extends Object, ? extends Object> s){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( Expr.TriConsumer<? extends Object, ? extends Object, ? extends Object> s){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( Function<? extends Object, ? extends Object> f){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( BiFunction<? extends Object, ? extends Object, ? extends Object> f){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }
    public static _body of( Expr.TriFunction<? extends Object, ? extends Object, ? extends Object, ? extends Object> f){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    public static _body of( Expr.QuadFunction<? extends Object, ? extends Object, ? extends Object, ? extends Object, ? extends Object> f){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from( le);
    }

    private static _body from( LambdaExpr le ){

        if( le.getBody().isBlockStmt() ){
            return of( le.getBody().asBlockStmt() );
        } else {
            NodeList<Statement> nl = new NodeList<>();
            nl.add(le.getBody());
            return of(new BlockStmt(nl));
        }
    }

    /**
     * 
     * @param body
     * @return 
     */
    public static _body of( String...body ){
        return of( _constructor.of("UNKNOWN(){}").add(body).ast() );
    }
    
    /**
     * 
     * @param statement
     * @return 
     */
    public static _body of( Statement statement ){
        if( statement.getParentNode().isPresent()){
            Node parent = statement.getParentNode().get();
            if( parent instanceof NodeWithBlockStmt){
                return of( (NodeWithBlockStmt)parent);
            }
            if( parent instanceof NodeWithOptionalBlockStmt){
                return of( (NodeWithOptionalBlockStmt)parent);
            }
            if( parent instanceof NodeWithBody ){
                return of( (NodeWithBody)parent);
            }
        }
        //they COULD be

        if( statement instanceof BlockStmt ){
            return of( _constructor.of("UNKNOWN(){}").setBody((BlockStmt)statement).ast());
            //return of( _method.of("void __BODYHOLDER();").setBody((BlockStmt)statement).ast());
        }
        return of( _constructor.of("UNKNOWN(){}").add(statement).ast());
        //return of( _method.of("void __BODYHOLDER();").add(statement).ast());
    }

    /**
     * Build and return a new Body made of a block statement
     * @return
     */
    public static _body of(){
        return of( new BlockStmt());
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
        ObjectCreationExpr oce = Expr.newExpr(ste);
        Optional<BodyDeclaration<?>> on = oce.getAnonymousClassBody().get().stream().filter(m -> 
                m instanceof MethodDeclaration 
                && !((MethodDeclaration)m)
                    .getAnnotationByClass(_remove.class).isPresent() )
                    .findFirst();
        if(!on.isPresent()){
            throw new _jdraftException("Could not locate the method containing the body in "+ oce);
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

    public static _body of(NodeWithBody astNodeWithBody){
        return new _body(astNodeWithBody);
    }

    /**
     * is the body implemented, or (abstract/NOT implemented)
     * <CODE>void m(){}</CODE> has an IMPLEMENTED BODY because its an empty block statement
     * -vs-
     * <CODE>void m();</CODE> has a NOT IMPLEMENTED BODY because ";" )
     */
    public static _feature._one<_body, Boolean> IS_IMPLEMENTED = new _feature._one<>(_body.class, Boolean.class,
            _feature._id.IS_IMPLEMENTED,
            a -> a.isImplemented(),
            (_body a, Boolean b) -> a.setImplemented(b), PARSER);

    public static _feature._many<_body, _stmt> STATEMENTS = new _feature._many<>(_body.class, _stmt.class,
            _feature._id.STATEMENTS, _feature._id.STATEMENT,
            a->a.list(),
            (_body a, List<_stmt> es)-> a.set(es), PARSER, s-> _stmt.of(s))
            .setOrdered(true);/** the order of the statements matter */

    public static _feature._features<_body> FEATURES = _feature._features.of(_body.class,  PARSER, IS_IMPLEMENTED, STATEMENTS);

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


    public _body setImplemented(Boolean implement){
        if( ! implement ){
            if( isImplemented() ){ //the only time I need to change
                NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt) astParentNode;
                nobs.removeBody();
            } else{
                //this means CLEAR
                this.clear();
            }
        } else{ //here io want to replace "void m();" with "void m(){}"
            if( ! isImplemented() ){
                NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt) astParentNode;
                nobs.createBody();
            }
        }
        return this;
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
    public Statement ast() {
        if (astParentNode instanceof NodeWithOptionalBlockStmt) {
            NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt) astParentNode;
            if (nobs.getBody().isPresent()) {
                return (BlockStmt) nobs.getBody().get();
            }
            return null; //abstract body, i.e. "void m();"
        }
        if(astParentNode instanceof NodeWithBody){
            NodeWithBody nwb = (NodeWithBody)astParentNode;
            return nwb.getBody();
        }
        NodeWithBlockStmt nbs = (NodeWithBlockStmt) astParentNode;
        return nbs.getBody();
    }

    /**
     * 
     * @param index
     * @return 
     */
    public Statement getAstStatement(int index) {
        if( this.isImplemented() ){
            Statement st = astStatement();
            if( st instanceof BlockStmt ){
                return st.asBlockStmt().getStatement(index);
            } else{
                if( index > 0){
                    //System.out.println( "STATEMENT IS "+st.getClass()+" "+st );
                    return null;
                }
                return st;
            }
            //return ast().getStatement(index);
        }
        throw new _jdraftException("No Statement at ["+index+"] for non-implemented body");
    }

    /**
     * list of _stmts
     * @return
     */
    public List<_stmt> list(){
        List<_stmt> sts = new ArrayList<>();
        NodeList<Statement> ns = getAstStatements();
        if( ns != null ){
            ns.forEach(n -> sts.add(_stmt.of(n)));
        }
        return sts;
    }

    /**
     * Set the statements in this body
     * @param stmts
     * @return
     */
    public _body set(List<_stmt> stmts){
        //setting this to null will remove the body Assuming
        if ( stmts == null && astParentNode instanceof NodeWithOptionalBlockStmt) {
            NodeWithOptionalBlockStmt nobs = (NodeWithOptionalBlockStmt) astParentNode;
            nobs.removeBody();
            return this;
        }
        if( stmts == null ){
            this.getAstStatements().clear();
            return this;
        }
        this.clear(); //clear existing statements
        stmts.forEach(s-> this.getAstStatements().add(s.ast()));
        return this;
    }

    public Statement astStatement(){
        if(this.astParentNode instanceof NodeWithBody){
            return ((NodeWithBody)this.astParentNode).getBody();
        }
        if(this.astParentNode instanceof NodeWithBlockStmt){
            return ((NodeWithBlockStmt)this.astParentNode).getBody();
        }
        if(this.astParentNode instanceof NodeWithOptionalBlockStmt){
            NodeWithOptionalBlockStmt nwobs = (NodeWithOptionalBlockStmt)this.astParentNode;
            if( nwobs.getBody().isPresent()){
                return (Statement)nwobs.getBody().get();
            }
            return null;
        }
        throw new _jdraftException("Parent node is not NodeWithBody, NodeWithBlockStmt, NodeWithOptionalBlockStmt "+ this.astParentNode.getClass()+" "+astParentNode);
    }

    /**
     * Returns the AST statements node list within the body
     * @return a NodeList with statements,
     * null represents NO BODY i.e. "void m();"
     * empty list means empty body "void m(){}"
     */
    public NodeList<Statement> getAstStatements() {
        if (isImplemented()) {
            Statement st = astStatement();
            if( st.isBlockStmt() ) {
                return st.asBlockStmt().getStatements();
            } else{
                NodeList<Statement> nls = new NodeList<Statement>();
                nls.add( st );
                return nls;
            }
        }
        return new NodeList<>();
        //return null;
        // returning null as I dont want to return an empty node list that
        // someone might presume will be modifyable ( which the changes WONT be 
        // reflected in the underlying...its a bold strategy cotton, lets see if it pays off for em
        //https://www.youtube.com/watch?v=4Ru8DMW-grY
    }

    public boolean is(_stmt _st ){
        return is( _st.ast());
    }

    public boolean is(Statement st){
        return Objects.equals( this.astStatement(), st);
    }

    /**
     * is the _body equal to the String... provided
     * @param body
     * @return 
     */
    public boolean is(String... body) {
        if( body == null || body.length == 0
                || (body.length == 1 && body[0].trim().length()==0)
                || (body.length == 1 && body[0].trim().equals(";"))) {
            return !this.isImplemented() || this.isEmpty();
        }
        Statement thisStatement = astStatement();

        //Ast.statement(body);
        Statement testStmt;
        try {
            testStmt = Ast.statement(body);
        } catch(Exception e){
            testStmt = Ast.blockStmt(body);
        }
        if( thisStatement.getClass() == testStmt.getClass() ) {
            //return Objects.equals(thisStatement, testStmt);
            return Objects.equals(thisStatement.toString(Print.PRINT_NO_COMMENTS),testStmt.toString(Print.PRINT_NO_COMMENTS) );
            //return Objects.equals(_stmt.of(thisStatement),_stmt.of(testStmt) );
        } else{
            if( thisStatement instanceof BlockStmt
                    && thisStatement.asBlockStmt().getStatements().size() == 1){

                return Objects.equals(thisStatement.toString(Print.PRINT_NO_COMMENTS), _blockStmt.of().add(testStmt).ast().toString(Print.PRINT_NO_COMMENTS) );
                //return Objects.equals( _stmt.of(thisStatement), _blockStmt.of().add(testStmt));
            }
        }
        return false;
        //BlockStmt bs = Ast.blockStmt(body);
        //return Objects.equals(this.ast(), bs);
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
        if( this.astParentNode instanceof NodeWithBody ){
            ((NodeWithBody)this.astParentNode).setBody(new EmptyStmt());
            return this;
        }
        ((NodeWithBlockStmt)this.astParentNode).setBody(Ast.blockStmt("{}"));
        return this;        
    }
    
    public <A extends Object> boolean is( Expr.Command lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object> boolean is( Consumer<A> lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object, B extends Object> boolean is( Function<A,B> lambdaWithBody ){            
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object, B extends Object,C extends Object> boolean is( BiFunction<A,B,C> lambdaWithBody ){            
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object, B extends Object,C extends Object, D extends Object> boolean is( Expr.TriFunction<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> boolean is( Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object, B extends Object> boolean is( BiConsumer<A,B> lambdaWithBody ){            
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }
        
    public <A extends Object, B extends Object,C extends Object> boolean is(Expr.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> boolean is(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return bdy.toString(Print.PRINT_NO_COMMENTS).equals(this.toString(Print.PRINT_NO_COMMENTS));
    }

    /*
    public boolean is(Class<? extends _stmt> statementClass){
        if( isImplemented()){
            if( this.astParentNode instanceof NodeWithOptionalBlockStmt){
                System.out.println( "NWOBS");
                return statementClass.isAssignableFrom(BlockStmt.class);
            } else if( this.astParentNode instanceof NodeWithStatements ){
                System.out.println( "NWSS");
            } else if( this.astParentNode instanceof NodeWithBlockStmt){
                System.out.println( "NWBS");
                BlockStmt bs = ((NodeWithBlockStmt) this.astParentNode).getBody();
                return statementClass.isAssignableFrom(BlockStmt.class);
            }
            System.out.println("PARENT"+ this.astParentNode );
            System.out.println("AST" + this.ast());

            if( this.getAstStatements().size() == 1){
                System.out.println("AST" + this.ast());
                return statementClass.isAssignableFrom( _stmt.of(this.ast()).getClass() );
            }

        }
        return false;
    }
     */

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
            Statement st = astStatement();
            if( ! (st instanceof BlockStmt)){
                return false;
            }
            NodeList<Statement> ts = st.asBlockStmt().getStatements();
            NodeList<Statement> os = bs.getStatements();

            if (ts.size() != os.size()) {
                //if we can avoid printing, we should
                return false;
            }
            for(int i=0;i<ts.size();i++){ //this might be improved
                if( !Objects.equals( 
                    ts.get(i).toString(Print.PRINT_NO_COMMENTS),
                    os.get(i).toString(Print.PRINT_NO_COMMENTS) ) ) {
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

        Statement t = this.astStatement();
        Statement o = other.astStatement();
        if( t instanceof EmptyStmt ){
            return o instanceof EmptyStmt || o instanceof BlockStmt &&
                    ( o.asBlockStmt().isEmpty() || (o.asBlockStmt().getStatements().size() ==1 && o.asBlockStmt().getStatement(0) instanceof EmptyStmt));
        }
        if( o instanceof EmptyStmt){
            return t instanceof BlockStmt &&
                    ( t.asBlockStmt().isEmpty() || (t.asBlockStmt().getStatements().size() ==1 && t.asBlockStmt().getStatement(0) instanceof EmptyStmt));
        }
        //System.out.println( t.getClass());
        //System.out.println( o.getClass());
        return Objects.equals( _stmt.of(t), _stmt.of(o));
        /*
        String left = t.toString(Print.PRINT_NO_COMMENTS);
        String right =  o.toString(Print.PRINT_NO_COMMENTS);

        //System.out.println( left );
        //System.out.println( right );
        return Objects.equals(left, right);
        //return _stmt.of(t).equals( _stmt.of(o));
        //return Objects.equals( _stmt.of(t).toString(), _stmt.of(o));
         */
        /*
        BlockStmt t = this.ast();
        BlockStmt o = other.ast();

        NodeList<Statement> ts = t.getStatements();
        NodeList<Statement> os = o.getStatements();

        if (ts.size() != os.size()) {
            //if we can avoid printing, we should
            return false;
        }
        String tnc = t.toString(Print.PRINT_NO_COMMENTS);
        String onc = o.toString(Print.PRINT_NO_COMMENTS);
        return tnc.equals(onc);
         */
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (!isImplemented()) {
            return 0;
        }
        hash = 53 * hash + Objects.hashCode(astStatement().toString(Print.PRINT_NO_COMMENTS));
        return hash;
    }

    /**
     * Prints out the body using the pretty printer configuration provided
     *
     * For an EXAMPLE on how to create a PreetyPrinterConfiguration, see:
     * {@link Print#PRINT_NO_COMMENTS}
     *
     * @param ppc
     * @return
     */
    public String toString(PrettyPrinterConfiguration ppc) {
        if (!isImplemented()) {
            return "";
        }
        return astStatement().toString(ppc);
    }

    @Override
    public String toString() {
        if (!isImplemented()) {
            return "";
        }
        return astStatement().toString();
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
        if( isImplemented() ){
            return astStatement().isEmptyStmt() || (astStatement().isBlockStmt() && astStatement().asBlockStmt().isEmpty());
        }
        return true;
    }

    /**
     * Examples {@link _initBlock}, {@link _method}, {@link _constructor}
     *
     * @author Eric
     * @param <_WB> enclosing TYPE (to access this)
     */
    public interface _withBody<_WB extends _withBody>
        extends _java._domain {
        
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
        _WB setBody(BlockStmt body);

        /**
         * @return return true if the member has a body
         */
        default boolean hasBody() {
            return getBody().isImplemented();
        }

        /**
         * "" empty string
         * Is the body equal to
         * @param statements
         * @return
         */
        default boolean isBody(String...statements){
            String all = Text.combine(statements);
            if( all.trim().length() == 0 ){
                return !isImplemented();
            }
            if(all.equals(";") ){
                return this.statementCount() == 1 && Objects.equals( this.getAt(0), _emptyStmt.of());
            }
            if( !isImplemented() ){
                return false;
            }
            _body _bd = _body.of(all);
            System.out.println( "testing "+ listStatements()+" to "+ _bd.list());
            if(_bd.list().size() != this.listAstStatements().size()){
                return false;
            }
            List<Statement> tst = this.listAstStatements();

            List<_stmt> st = _bd.list();
            for(int i=0;i<st.size(); i++){
                if( !Objects.equals( st.get(i), _stmt.of(tst.get(i)) ) ){
                    System.out.println("NOT EQ"+ st.get(i)+ " to "+tst.get(i));
                    return false;
                }
            }
            return true;
        }

        default _WB removeStatements(Predicate<_stmt> stmtMatchFn){
            List<_stmt> sts = new ArrayList<>();
            this.listStatements().forEach( s-> {
                if( stmtMatchFn.test( s)){
                    sts.add( s );
                }
            } );
            Statement st = getBody().astStatement();
            if( st.isBlockStmt() ){
                sts.forEach(s-> st.asBlockStmt().getStatements().remove( s.ast()));
                return (_WB)this;
            } else{
                if( stmtMatchFn.test( _stmt.of(st)) ){
                    setBody(new EmptyStmt());
                }
                return (_WB)this;
            }
            /*
            sts.forEach( s -> {
                System.out.println( "REMOVING "+ s.ast()+" from "+s.ast().getParentNode().get() );
                boolean removed = s.ast().remove();
                if( !removed ){
                    System.out.println("NOT REMOVED "+ s.ast() );
                    s.ast().getParentNode().get().remove(s.ast());
                }
                //s.replace(new EmptyStmt() );
                //boolean st = this.listAstStatements().remove(s.ast());


                //System.out.println( "REMOVED "+ st+" "+s );
                //st = s.ast().remove();
                //System.out.println( "REMOVED "+ st+" "+s );

                //if( !st ){

                //    st = s.ast().replace( new EmptyStmt());
                //    System.out.println("ST "+st);
               // }
            } );
            return (_WB)this;
             */
        }

        default <_SI extends _stmt> _WB removeStatements(Class<_SI> stmtInstanceClass){
            return removeStatements( s-> stmtInstanceClass.isAssignableFrom( s.getClass() ));
        }

        default List<_stmt> listStatements(){
            NodeList<Statement> sts = listAstStatements();
            if( sts == null ){
                return new ArrayList<>();
            }
            return sts.stream().map(s-> _stmt.of(s)).collect(Collectors.toList());
        }

        /**
         * Returns a list of statements
         * NOTE: this lists the "top statements" 
         * (this DOES NOT WALK INTO statements, like compound statements, 
         * statements in for loops and nested blocks)
         * 
         * @return 
         */
        default NodeList<Statement> listAstStatements(){
            if( this.hasBody() ){
                return getBody().getAstStatements();
            }
            return new NodeList<>();
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
        default <S extends Statement> List<S> listAstStatements(Class<S> stmtClass ){
            if( this.hasBody() ){
                List<S> stmts = new ArrayList<>();
                getBody().getAstStatements().stream()
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
         * @see Tree#list(com.github.javaparser.ast.Node, java.lang.Class)
         * @param <S> the statement class
         * @param stmtClass the statement class
         * @param stmtPredicate 
         * @return the list of statements of the class
         */
        default <S extends Statement> List<S> listAstStatements(Class<S> stmtClass, Predicate<S> stmtPredicate){
            return listAstStatements(stmtClass).stream().filter(stmtPredicate).collect(Collectors.toList());
        }
        
        default <A extends Object> _WB setBody(Expr.Command lambdaWithBody){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy); 
        }
        
        default <A extends Object> _WB setBody(Consumer<A> lambdaWithBody){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy); 
        }
        
        default <A extends Object, B extends Object> _WB setBody(Function<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object> _WB setBody(BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _WB setBody(Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> _WB setBody(Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);           
        }
        
        default <A extends Object, B extends Object> _WB setBody(BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);            
        }
        
        default <A extends Object, B extends Object,C extends Object> _WB setBody(Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);            
        }

        default <A extends Object, B extends Object,C extends Object, D extends Object> _WB setBody(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            return setBody(bdy);            
        }

        /**
         *
         * @param _bs
         * @return
         */
        default _WB setBody(_blockStmt _bs) {
            return setBody(_bs.ast());
        }

        /**
         *
         * @param body
         * @return
         */
        default _WB setBody(_body body) {
            return setBody(body.ast());
        }


        /**
         *
         * @param body
         * @return
         */
        default _WB setBody(String... body) {
            return setBody(Ast.blockStmt(body));
        }

        default _WB setBody(_stmt _st){
            if (_st instanceof _blockStmt) {
                return setBody( ((_blockStmt) _st).ast());
            }
            BlockStmt bs = new BlockStmt();
            bs.addStatement(_st.ast());
            return setBody(bs);
        }

        /**
         * 
         * @param astStmt
         * @return 
         */
        default _WB setBody(Statement astStmt) {
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
        _WB clearBody();

        /**
         * Adds statements
         * @param _stmts
         * @return
         */
        default _WB add(_stmt... _stmts){
            return add(statementCount(), _stmts);
        }

        /**
         * Add Statements to the end of the BODY
         *
         * @param statements the statements to add to the tail of the BODY
         * @return the modified T (_method, _constructor, _staticBlock)
         */
        default _WB add(Statement... statements){
            return add(statementCount(), statements);
        }
        
        default <A extends Object> _WB add(int index, Expr.Command lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add( index,bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );
        }
        
        default <A extends Object> _WB add(Expr.Command lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add( bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add( bdy );
        }
        
        default <A extends Object> _WB add(Consumer<A> lambdaWithBody){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add( bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add( bdy );
        }

        default <A extends Object> _WB add(int index, Consumer<A> lambdaWithBody){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add( index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add( index, bdy );
        }
        
        default <A extends Object, B extends Object> _WB add(Function<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object> _WB add(int index, Function<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object> _WB add(BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _WB add(int index, BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object, D extends Object> _WB add(Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _WB add(int index, Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
                
        default <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _WB add(Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> _WB add(int index, Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object> _WB add(BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object> _WB add(int index, BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }
        
        default <A extends Object, B extends Object, C extends Object> _WB add(Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _WB add(int index, Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }

        default <A extends Object, B extends Object, C extends Object, D extends Object> _WB add(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _WB add(int index, Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return add(index, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return add(index, bdy );            
        }

        /**
         * Add one or more statements to the body starting at the startStatementIndex
         * @param startStatementIndex
         * @param statements
         * @return
         */
        default _WB add(int startStatementIndex, _stmt...statements){
            Statement[] sts = new Statement[statements.length];
            for(int i=0;i<statements.length;i++){
                sts[i] = statements[i].ast();
            }
            return add(startStatementIndex, sts);
        }

        /**
         * Add Statements to the BODY starting at startStatementIndex (0-based)
         *
         * @param startStatementIndex index of the position to add the
         * statements
         * @param statements the statements to add to the tail of the BODY
         * @return the modified T (_method, _constructor, _staticBlock)
         */
        _WB add(int startStatementIndex, Statement... statements);

        /**
         * Add Statements to the end of the BODY
         *
         * @param statements the statements as an Array of Strings
         * @return the modified T (_method, _constructor, _staticBlock)
         */
        default _WB add(String... statements) {
            BlockStmt bs = Ast.blockStmt(statements);

            if( !this.getBody().isImplemented() ){ //empty body
                if( bs.getStatements().isEmpty() ) {
                    //System.out.println("creating empty body");
                    this.setBody("{}");
                    return (_WB)this;
                } else{
                    this.setBody(bs);
                    return (_WB)this;
                }
            }

            //organize orphan comments
            List<com.github.javaparser.ast.comments.Comment> coms = bs.getOrphanComments();
            com.github.javaparser.ast.comments.Comment c = null;
            if (coms.size() > 0) {
                c = coms.get(0);
            }
            Collections.sort(coms, new Comments.CommentPositionComparator());

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
                } else if( getBody().astParentNode() instanceof NodeWithBody ) {
                    Statement bd = getBody().astStatement();
                    if( bd instanceof BlockStmt ){
                        bs.getStatements().forEach(s -> bd.asBlockStmt().addStatement(s));
                    } else{
                        if( bd instanceof EmptyStmt ){
                            setBody(bs); //remove the emptystatement with the block statement
                        } else{
                            BlockStmt nbs = new BlockStmt();
                            nbs.addStatement( bd);
                            bs.getStatements().forEach(s -> nbs.addStatement(s));
                            setBody(nbs);
                        }
                    }
                }
                else{
                    ((BlockStmt)getBody().ast()).addStatement(st);
                }
            }
            coms.forEach(co -> getBody().ast().addOrphanComment(co));
            return (_WB) this;
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
        default _WB add(int statementStartIndex, String... statements) {
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
            return this.listAstStatements().size();
        }
        
        /**
         * Gets the statement at the statement index, throws an exception if the 
         * Statement index is out of bounds
         * @param statementIndex
         * @return 
         */
        default Statement getAstStatement(int statementIndex){
            return this.listAstStatements().get(statementIndex);
        }
                
        default <A extends Object> _WB addAt(String labelName, Expr.Command lambdaWithBody){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt( labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );
        }
        
        default <A extends Object> _WB addAt(String labelName, Consumer<A> lambdaWithBody){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt( labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );
        }
        
        default <A extends Object, B extends Object> _WB addAt(String labelName, Function<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _WB addAt(String labelName, BiFunction<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object> _WB addAt(String labelName, Expr.TriFunction<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object> _WB addAt(String labelName, Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object> _WB addAt(String labelName, BiConsumer<A,B> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }
        
        default <A extends Object, B extends Object,C extends Object> _WB addAt(String labelName, Expr.TriConsumer<A,B,C> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
            if( bdy.isBlockStmt() ){
                return addAt(labelName, bdy.asBlockStmt().getStatements().toArray(new Statement[0]));
            }
            return addAt(labelName, bdy );            
        }

        default <A extends Object, B extends Object,C extends Object, D extends Object> _WB addAt(String labelName, Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
            Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
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
        default _WB addAt(String labelName, String... statements) {
            if( !isImplemented() ){
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\"");
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
            return (_WB) this;
        }
        
        default _WB addAt(String labelName, Statement...statements){
             if( !isImplemented() ){
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\"");
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
            return (_WB) this;
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
        default _WB addAt(String labelName, Statement astStmt) {
            if( !isImplemented() ){
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            List<Statement> stmts = new ArrayList<>();
            stmts.add(astStmt);
            return addAt(labelName, stmts);
        }

        /**
         * Appends Ast Statements to the label within the code 
         * (always adds at the tail/ end if the label already contains statements)
         * @param labelName the name of the label where statements are to be added
         * @param astStmts a list of Statements to add at the label
         * @return the modified entity
         */
        default _WB addAt(String labelName, List<Statement> astStmts) {
            if( !isImplemented() ){
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                    = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\"");
            }
            Statement st = ols.get().getStatement();
            if (st.isBlockStmt()) {
                BlockStmt bs = st.asBlockStmt();
                astStmts.forEach(s -> bs.addStatement(s));
            } else {
                BlockStmt bs = Ast.blockStmt(astStmts.toArray(new Statement[0]));
                bs.addStatement(0, st);
                st.replace(bs);
            }
            return (_WB) this;
        }

        default _stmt getAt( int index ){
            if( !isImplemented() ){
                return null;
            }
            return _stmt.of(listAstStatements().get(index));
        }

        /**
         * Gets the Labeled statement based on the label
         *
         * @param labelName
         * @return the Labeled Statement or THROWS EXCEPTION if not found
         */
        default _labeledStmt getAt(String labelName) {
            if( !isImplemented() ){
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\" on no-implemented body");
            }
            Optional<LabeledStmt> ols
                = this.getBody().ast().findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
            if (!ols.isPresent()) {
                throw new _jdraftException("cannot find labeled Statement \"" + labelName + "\"");
            }
            return _labeledStmt.of(ols.get());
        }
        
        /**
         * _walk the body in preorder fashion, intercepting all Statements that
         * implement statementClass and match the stmtMatchFn and processing
         * all of which with the statConsumer
         * @param stmtActionFn the "processing" function for matching statements
         * @return the modified T
         */
        default _WB forAstStmts(Consumer<Statement> stmtActionFn ){
            if( !isImplemented() ){
                return (_WB)this;
            }           
            Tree.in((BlockStmt)this.getBody().ast(), Statement.class, stmtActionFn );
            return (_WB)this;
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
        default <S extends Statement> _WB forAstStmts(Class<S> statementClass, Consumer<S> stmtActionFn ){
            if( !isImplemented() ){
                return (_WB)this;
            }
            Tree.in((BlockStmt)this.getBody().ast(), statementClass, stmtActionFn );
            return (_WB)this;
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
        default <S extends Statement> _WB forAstStmts(Class<S> statementClass, Predicate<S>stmtMatchFn, Consumer<S> stmtActionFn ){
            if( !isImplemented() ){
                return (_WB)this;
            }
            Tree.in((BlockStmt)this.getBody().ast(), statementClass, stmtMatchFn, stmtActionFn );
            return (_WB)this;
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
        default <S extends Statement> S firstAstStmt(Class<S> stmtClass ){
            return firstAstStmt(stmtClass, s->true);
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
        default <S extends Statement> S firstAstStmt(Class<S> stmtClass, Predicate<S> stmtMatchFn ){
            if( !isImplemented() ){
                return null;
            }
            Optional<S> of = getBody().ast().findFirst(stmtClass, stmtMatchFn);
            if (of.isPresent()) {
                return of.get();
            }
            return null;
        }
    }    
}
