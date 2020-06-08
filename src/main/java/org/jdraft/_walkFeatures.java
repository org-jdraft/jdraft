package org.jdraft;

import com.github.javaparser.ast.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Fluent intermediate object used to set up a walk through AST-nodes given:
 * <UL>
 * <LI>a starting node
 * <LI>a treeTraversal strategy</LI>
 * <LI>the target node type</LI>
 *</UL>
 *
 * this simplifies "walk" queries where the target is known
 *
 * i.e.
 * _class _c = _class.of(MyClass.class);
 * //this is a walkTarget implementation
 * assertEquals(2, _c.walk(_method.class).count(_m->_m.isStatic()));
 * //this is a normal implementation
 *
 *
 */
public class _walkFeatures<_F> {

    /** How to walk the AST */
    public Node.TreeTraversal treeTraversal = Node.TreeTraversal.PREORDER;

    /** The start node for where to begin the walk */
    public _tree._node _n;

    /** The relevant features to intercept*/
    public List<_feature<?, _F>> features;

    public _walkFeatures(_tree._node _n, _feature<?, _F>...features){
        this._n = _n;
        this.features = Stream.of(features).collect(Collectors.toList());
    }

    public _walkFeatures(Node.TreeTraversal tt, _tree._node _n, _feature<?, _F>...features){
        this._n = _n;
        this.treeTraversal = tt;
        this.features = Stream.of(features).collect(Collectors.toList());
    }

    public boolean has(){
        return has(t->true);
    }

    public  boolean has(Predicate<_F> _matchFn){
        return first(_matchFn) != null;
    }

    public _F first(){
        return first( t->true);
    }

    public <_I extends _F> _I first(Class<_I> implClass){
        return (_I) first( t-> implClass.isAssignableFrom(t.getClass()));
    }


    /**
     *
     *  List<_feature<?, _F>> featuresOnThisNode =
     *     features.stream().filter( f-> f.getTargetClass().isAssignableFrom(n.getClass()) ).collect(Collectors.toList());
     *
     *  featuresOnThisNode.stream().filter( f-> _matchFn.test( f.getter( _n) ) );
     * @param _matchFn
     * @return
     */
    public <_T> _F first(Predicate<_F> _matchFn){
        List<_F> found = new ArrayList<>();
        this._n.walk().first(  n->{
            for(int i=0; i<features.size();i++){
                _feature<_T, _F> _f = (_feature<_T, _F>) this.features.get(i);

                if( _f.getTargetClass().isAssignableFrom(n.getClass()) ){
                    _F f = _f.get( (_T)n);
                    if(_matchFn.test(f)){
                        found.add(f);
                        return true;
                    }
                }
            }
            return false;
        });
        if( found.isEmpty()){
            return null;
        }
        return found.get(0);
    }
}
