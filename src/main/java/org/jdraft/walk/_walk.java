package org.jdraft.walk;

import com.github.javaparser.ast.Node;
import org.jdraft.Print;
import org.jdraft._java;
import org.jdraft._tree;
import org.jdraft.bot.Select;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Fluent intermediate object used to set up a walk through AST-nodes given:
 * <UL>
 * <LI>a starting node
 * <LI>a treeTraversal strategy</LI>
 *</UL>
 * //print all of the int literals within the source of MyClass.class
 * _class.of(MyClass.class).walk().print(_int.class);
 *
 * //list all lambda expressions with parameters within AClass.class
 * List<_lambda> _ls = _class.of(AClass.class).walk().list(_lambda.class, _l->_l.hasParameters());
 *
 *
 */
public class _walk<_T extends _java._domain> {

    /** How to walk the AST */
    public Node.TreeTraversal treeTraversal = Node.TreeTraversal.PREORDER;

    /** The start node for where to begin the walk */
    public _tree._node _n;

    /** The target class to intercept along the walk */
    public Class<_T>targetClass;

    /** A Selector applied to all instances of the target class to further constrain (& return variables) for the walk */
    public Function<_T, Tokens> selector = t-> new Tokens();

    public _walk(_tree._node _n, _T instance){
       this(Node.TreeTraversal.PREORDER, _n, instance);
    }

    public _walk(Node.TreeTraversal tt, _tree._node _n, _T instance){
        this.treeTraversal = tt;
        this._n = _n;
        this.targetClass = (Class<_T>)instance.getClass();
        Stencil st = Stencil.of( ((_tree._node)instance).toString(Print.PRINT_NO_COMMENTS));
        this.selector = t-> st.parse( ((_tree._node)t).toString(Print.PRINT_NO_COMMENTS));
    }

    public _walk(_tree._node _n, Class<_T> targetClass){
        this._n = _n;
        this.targetClass = targetClass;
    }

    public _walk(Node.TreeTraversal tt, _tree._node _n, Class<_T> targetClass){
        this.treeTraversal = tt;
        this._n = _n;
        this.targetClass = targetClass;
    }

    public boolean has( ){
        return first(t->true) != null;
    }

    public boolean has(Predicate<_T> _matchFn){
        return first(_matchFn) != null;
    }

    public int count(){
        return list(t->true).size();
    }

    public int count(Predicate<_T> _matchFn){
        return list(_matchFn).size();
    }


    /*
    public <_N extends _node> int count(String... stencil){

        //this might seem strange at first, that we construct an entity and then write it to a string
        //but it serves a valuable purpose... first it validates that the string IS a valid entity of _nodeClass,
        //secondly it NORMALIZES the form so that (if we encounter a like entity), we don't exclude a match
        //purely because it has "STYLISTIC" differences (i.e. spaces within parameters, "( 1 )" -vs- "(1)" etc.)
        Stencil st = Stencil.of(_java.node(_nodeClass, stencil).toString(Print.PRINT_NO_COMMENTS));
        return list(_nodeClass, _n -> st.matches( _n.toString(Print.PRINT_NO_COMMENTS))).size();
    }
     */

    public int count(Stencil stencil){
        return list(_n -> stencil.matches( ((_tree._node)_n).toString(Print.PRINT_NO_COMMENTS))).size();
    }

    public void print(){
        forEach(e-> System.out.println(e));
    }

    public void print(Predicate<_T> _matchFn){
        forEach(_matchFn, e-> System.out.println(e));
    }

    public void printTree(){
        forEach(e-> Print.tree((_tree._node)e));
    }

    public void printTree(Predicate<_T> _matchFn){
        forEach(_matchFn, e-> Print.tree((_tree._node)e));
    }

    public _T first(){
        return Walk.first(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null);
    }

    public _T first(Predicate<_T> _matchFn){
        return Walk.first(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null && _matchFn.test(t));
    }

    public List<_T> list(){
        return Walk.list(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null);
    }

    public List<_T> list(Predicate<_T> _matchFn){
        return Walk.list(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null && _matchFn.test(t));
    }

    public Stream<_T> stream(){
        return Walk.stream(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null);
    }

    public Stream<_T> stream(Predicate<_T> _matchFn){
        return Walk.stream(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null && _matchFn.test(t));
    }

    public List<_T> forEach(Consumer<_T> actionFn){
        List<_T> _l = list(t-> this.selector.apply(t) != null);
        _l.forEach(actionFn);
        return _l;
    }

    public List<_T> forEach(Predicate<_T> _matchFn, Consumer<_T> actionFn){
        List<_T> _l = Walk.list(this.treeTraversal, this._n, targetClass, t-> this.selector.apply(t) != null && _matchFn.test(t));
        _l.forEach(actionFn);
        return _l;
    }
}
