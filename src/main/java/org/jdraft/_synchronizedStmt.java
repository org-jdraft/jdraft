package org.jdraft;

import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SynchronizedStmt;

import java.util.Objects;
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

    public static _feature._features<_synchronizedStmt> FEATURES = _feature._features.of(_synchronizedStmt.class,  PARSER, EXPRESSION, BODY );

    public _feature._features<_synchronizedStmt> features(){
        return FEATURES;
    }

    private SynchronizedStmt node;

    public _synchronizedStmt(SynchronizedStmt rs){
        this.node = rs;
    }

    @Override
    public _synchronizedStmt copy() {
        return new _synchronizedStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _synchronizedStmt replace(SynchronizedStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _body getBody(){
        return _body.of( (NodeWithBlockStmt) node);
    }

    @Override
    public _synchronizedStmt setBody(BlockStmt body) {
        this.node.setBody(body);
        return this;
    }

    @Override
    public _synchronizedStmt clearBody() {
        this.node.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _synchronizedStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.node.getBody();
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

    public SynchronizedStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _synchronizedStmt ){
            return Objects.equals( ((_synchronizedStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
