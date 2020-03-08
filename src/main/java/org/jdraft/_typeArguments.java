package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TypeArgument Lists used in:
 * {@link _methodCall}
 * {@link _constructorCallStmt}
 * {@link _new}
 * {@link _fieldAccess}
 * {@link _methodReference}
 * order doesnt matter
 */
public class _typeArguments
        implements _java._set<Type, _typeRef, _typeArguments> {

    public static _typeArguments of(){
        return of( Ex.newEx("new empty()"));
    }

    public static _typeArguments of( _typeRef...rt){
        return of().add(rt);
    }

    public static _typeArguments of( Type...rt){
        return of().add(rt);
    }

    public static _typeArguments of(String...args){
        if( args.length == 1){
            String a = args[0].trim();
            if( a.startsWith("<") && a.endsWith(">")){
                a = a.substring(1, a.length()-1).trim();
                if( a.length() == 0 ) {
                    //System.out.println("NEW EMPTY WITH DIAMOND");
                    ObjectCreationExpr oce = Ex.newEx("new <> empty()");
                    //
                    // System.out.println(oce.isUsingDiamondOperator());
                    return of(oce);
                }
                ObjectCreationExpr oce = Ex.newEx("new <"+ a + "> empty()");
                //System.out.println( "NNN" + oce);
                return of( oce);
            }
            if( a.startsWith("<") ){
                a = a.substring(1, a.length()-1);
            }
            ObjectCreationExpr oce = Ex.newEx("new <"+ a + "> empty()");
            return of( oce);
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<args.length; i++){
            if( i > 0 ){
                sb.append(",");
            }
            String a = args[i].trim();
            sb.append(a);
        }
        return of( Ex.newEx("new<"+ sb.toString() + "> empty()"));
    }

    public static _typeArguments of(NodeWithTypeArguments nwta){
         return new _typeArguments(nwta);
    }

    public NodeWithTypeArguments nwta;

    public _typeArguments(NodeWithTypeArguments nwta){
        this.nwta = nwta;
    }

    public _typeArguments add(Class...clazz){
        return add(Arrays.stream(clazz).map(a -> _typeRef.of(a) ).collect(Collectors.toList()).toArray(new _typeRef[0]));
    }

    public _typeArguments add(String...args){
        return add(Arrays.stream(args).map(a -> _typeRef.of(a) ).collect(Collectors.toList()).toArray(new _typeRef[0]));
    }

    public _typeArguments add(Type... astElements) {
        if( !this.nwta.getTypeArguments().isPresent()){
            this.nwta.setTypeArguments(new NodeList<Type>());
        }
        for( Type el : astElements ) {
            this.listAstElements().add(el);
        }
        return this;
    }

    public _typeArguments add(_typeRef... elements) {
        if( !this.nwta.getTypeArguments().isPresent()){
            this.nwta.setTypeArguments(new NodeList<Type>());
        }
        for( _typeRef el : elements ) {
            this.listAstElements().add(el.ast());
        }
        return this;
    }

    @Override
    public boolean isEmpty(){
        return !hasTypeArgs() || size() == 0;
    }

    @Override
    public _typeArguments copy() {
        Node n = (Node)nwta;
        return new _typeArguments( (NodeWithTypeArguments) (n.clone()) );
    }

    @Override
    public List<_typeRef> list() {
        if( !this.nwta.getTypeArguments().isPresent()){
            return null; //null... and no diamond operator
        }
        return listAstElements().stream().map(e-> _typeRef.of(e) ).collect(Collectors.toList());
    }

    @Override
    public NodeList<Type> listAstElements() {
        if( !this.nwta.getTypeArguments().isPresent()){
            return null; //null... and no diamond operator
        }
        return (NodeList<Type>)this.nwta.getTypeArguments().get();
    }

    public boolean hasTypeArgs(){
        return this.nwta.getTypeArguments().isPresent();
    }

    public boolean isUsingDiamondOperator(){
        return this.nwta.isUsingDiamondOperator();
    }

    public _typeArguments setUseDiamondOperator(){
        this.nwta.setDiamondOperator();
        return this;
    }

    public _typeArguments removeDiamondOperator(){
        this.nwta.removeTypeArguments();
        return this;
    }

    public String toString(){
        if( this.isUsingDiamondOperator()){
            return "<>";
        }
        if( this.isEmpty()){
            return "";
        }
        List<_typeRef> trs = this.list();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<trs.size(); i++){
            if( i > 0 ){
                sb.append(", ");
            }
            sb.append(trs.get(i));
        }
        return sb.toString();
    }

    public int hashCode(){
        if( !this.nwta.getTypeArguments().isPresent()){
            return 57;
        }
        return 31 * this.nwta.getTypeArguments().get().hashCode();
    }

    public boolean equals( Object o){
        if( o instanceof _typeArguments){
            _typeArguments _as = (_typeArguments) o;
            if( !_as.hasTypeArgs() ){
                return !this.hasTypeArgs();
            }
            if( !this.hasTypeArgs()){
                return false;
            }
            NodeList<Type> tt = (NodeList<Type>)this.nwta.getTypeArguments().get();
            NodeList<Type> ot = (NodeList<Type>)_as.nwta.getTypeArguments().get();
            if( tt.size() == ot.size()){
                for(int i=0;i<tt.size(); i++){
                    Type t = tt.get(i);
                    if( !Ast.typesEqual(t, ot.get(i))){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int indexOf(_typeRef target){
        if( !isEmpty() ) {
            return list().indexOf(target);
        }
        return -1;
    }

    public int indexOf(Type target){
        if( !isEmpty() ) {
            return listAstElements().indexOf(target);
        }
        return -1;
    }

    /**
     * TypeArgument set used in:
     * {@link _methodCall}
     * {@link _constructorCallStmt}
     * {@link _new}
     * (order matters list of {@link _typeRef} )
     */
    public interface _withTypeArguments<N extends Node, _WTA extends _java._node> extends _java._node<N, _WTA> {

        /**
         * Creates and returns an {@link _typeArguments} to model the whole arguments list
         * @return an _args modelling 0...n arguments in the arguments list)
         */
        default _typeArguments getTypeArguments(){
            return of( (NodeWithTypeArguments)ast());
        }

        default _WTA setTypeArguments( _typeArguments _tas){
            if( _tas.hasTypeArgs() ) {
                ((NodeWithTypeArguments) ast()).setTypeArguments(_tas.listAstElements());
            } else{
                ((NodeWithTypeArguments) ast()).removeTypeArguments();
            }
            return (_WTA)this;
        }

        default _typeRef getTypeArgument(int index){
            if( hasTypeArguments()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments)ast()).getTypeArguments().get();
                return _typeRef.of( tt.get(index) );
            }
            throw new _jdraftException("no type argument at ["+ index+"]");
        }

        default _WTA removeTypeArgument(int index){
            if( hasTypeArguments()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments)ast()).getTypeArguments().get();
                tt.remove(index);
                return (_WTA)this;
            }
            throw new _jdraftException("no type argument at ["+ index+"]");
        }

        default boolean hasTypeArguments(){
            return ((NodeWithTypeArguments)ast()).getTypeArguments().isPresent();
        }

        default List<_typeRef> listTypeArguments(){
            if( hasTypeArguments()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments)ast()).getTypeArguments().get();
                return tt.stream().map( t-> _typeRef.of( t )).collect(Collectors.toList());
            }
            return null;
        }

        default List<_typeRef> listTypeArguments(Predicate<_typeRef> matchFn){
            List<_typeRef> lts = listTypeArguments();
            if( lts == null ){
                return null;
            }
            return lts.stream().filter(matchFn).collect(Collectors.toList());
        }


        default boolean isTypeArguments(Type... es){
            _typeRef[] _es = new _typeRef[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _typeRef.of(es[i]);
            }
            return isTypeArguments(_es);
        }

        default boolean isTypeArguments(_typeRef... _es){
            List<_typeRef> _tes = listTypeArguments();
            if(_es.length == _tes.size()){
                for(int i=0;i<_es.length;i++){
                    if( ! Ast.typesEqual( _es[i].ast(), _tes.get(i).ast() ) ){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isTypeArguments(Predicate<_typeArguments> matchFn){
            return matchFn.test( getTypeArguments() );
        }

        /**
         * Check if all individual ({@link _typeRef}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allTypeArguments( Predicate<_typeRef> matchFn){
            return listTypeArguments().stream().allMatch(matchFn);
        }

        default boolean isUsingDiamondOperator(){
            return this.getTypeArguments().isUsingDiamondOperator();
        }

        default _WTA setUseDiamondOperator(){
            this.getTypeArguments().setUseDiamondOperator();
            return (_WTA)this;
        }

        default _WTA addTypeArgument(Type e){
            _typeArguments _ta = getTypeArguments();
            _ta.add(e);
            return (_WTA)this;
        }

        default _WTA addTypeArguments(Class... clazz){
            _typeArguments _ta = getTypeArguments();
            Arrays.stream(clazz).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA addTypeArguments(String... es){
            _typeArguments _ta = getTypeArguments();
            Arrays.stream(es).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA addTypeArguments(Type... es){
            _typeArguments _ta = getTypeArguments();
            Arrays.stream(es).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA addTypeArguments(_typeRef... _tr){
            _typeArguments _ta = getTypeArguments();
            Arrays.stream(_tr).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA removeTypeArguments(){
            ((NodeWithTypeArguments)ast()).removeTypeArguments();
            return (_WTA)this;
        }

        default _WTA removeTypeArguments(Predicate<_typeRef> matchFn){
            _typeArguments _ta = getTypeArguments();
            _ta.remove(matchFn);
            return (_WTA)this;
        }


        default _WTA removeTypeArguments(_typeRef... es){
            _typeArguments _ta = getTypeArguments();
            for(int i=0;i<es.length;i++){
                _ta.remove(es[i]);
            }
            return (_WTA)this;
        }

        default _WTA removeTypeArguments(Type... es){
            _typeArguments _ta = getTypeArguments();
            for(int i=0;i<es.length;i++){
                _ta.remove(es[i]);
            }
            return (_WTA)this;
        }

        default _WTA forTypeArguments(Consumer<_typeRef> argFn){
            _typeArguments _ta = getTypeArguments();
            _ta.forEach(argFn);
            return (_WTA)this;
        }

        default _WTA forTypeArguments(Predicate<_typeRef> matchFn, Consumer<_typeRef> argFn){
            _typeArguments _ta = getTypeArguments();
            _ta.forEach(matchFn, argFn);
            return (_WTA)this;
        }
    }
}
