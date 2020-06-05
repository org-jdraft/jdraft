package org.jdraft;

import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SynchronizedStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _synchronizedStmt implements
        _stmt<SynchronizedStmt, _synchronizedStmt>,
        _body._withBody<_synchronizedStmt>,
        _java._withExpression<SynchronizedStmt, _synchronizedStmt> {

    public static final Function<String, _synchronizedStmt> PARSER = s-> _synchronizedStmt.of(s);

    public static _synchronizedStmt of(){
        return new _synchronizedStmt( new SynchronizedStmt( ));
    }
    public static _synchronizedStmt of(SynchronizedStmt ss){
        return new _synchronizedStmt( ss);
    }
    public static _synchronizedStmt of(String...code){
        return new _synchronizedStmt(Stmt.synchronizedStmt(code));
    }

    public static _feature._one<_synchronizedStmt, _expr> EXPRESSION = new _feature._one<>(_synchronizedStmt.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_synchronizedStmt p, _expr _es) -> p.setExpression(_es), PARSER);

    public static _feature._one<_synchronizedStmt, _body> BODY = new _feature._one<>(_synchronizedStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_synchronizedStmt p, _body _bs) -> p.setBody(_bs), PARSER);

    public static _feature._features<_synchronizedStmt> FEATURES = _feature._features.of(_synchronizedStmt.class, EXPRESSION, BODY );

    private SynchronizedStmt astStmt;

    public _synchronizedStmt(SynchronizedStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _synchronizedStmt copy() {
        return new _synchronizedStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        AtomicInteger ai = new AtomicInteger();
        Consumer<Integer> ci =  (a)->{
            synchronized (ai){
                System.out.println(1);
            }
        };
        try{
            return is( Stmt.synchronizedStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    /*
    public _expression getExpression(){
        return _expression.of(this.astStmt.getExpression());
    }

    public _synchronizedStmt setExpression(_expression _e){
        this.astStmt.setExpression( _e.ast());
        return this;
    }
    */

    public _body getBody(){
        return _body.of( (NodeWithBlockStmt)astStmt);
    }

    @Override
    public _synchronizedStmt setBody(BlockStmt body) {
        this.astStmt.setBody(body);
        return this;
    }

    /*
    public _synchronizedStmt setBody(_body _b){
        Statement st = _b.astStatement();
        if( st instanceof BlockStmt){
            this.astStmt.setBody()
        } else {
            this.astStmt.setBody(_b.astStatement());
        }
        return this;
    }
     */

    @Override
    public _synchronizedStmt clearBody() {
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _synchronizedStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.astStmt.getBody();
        if( bd instanceof BlockStmt ){
            for(int i=0;i<statements.length; i++) {
                bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
            }
            return this;
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(bd);
        for(int i=0;i<statements.length; i++) {
            bd.asBlockStmt().addStatement(1+startStatementIndex, statements[i]);
        }
        return this;
    }

    /*
    @Override
    public boolean is(SynchronizedStmt astNode) {
        return this.astStmt.equals( astNode);
    }
     */

    public SynchronizedStmt ast(){
        return astStmt;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put(_java.Feature.EXPRESSION, astStmt.getExpression());
        comps.put(_java.Feature.BODY, astStmt.getBody());
        return comps;
    }
     */

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _synchronizedStmt ){
            return Objects.equals( ((_synchronizedStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
