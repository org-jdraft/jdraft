package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TypeArgument Lists used in:
 * {@link _methodCallExpr}
 * {@link _constructorCallStmt}
 * {@link _newExpr}
 * {@link _fieldAccessExpr}
 * {@link _methodRefExpr}
 * order doesnt matter
 */
public final class _typeArgs
        implements _tree._view<_typeArgs>, _tree._group<Type, _typeRef, _typeArgs> {

    public static final Function<String, _typeArgs> PARSER = s-> _typeArgs.of(s);

    public static _typeArgs of(){
        return of( Expr.newExpr("new empty()"));
    }

    public static _typeArgs of(_typeRef...rt){
        return of().add(rt);
    }

    public static _typeArgs of(Type...rt){
        return of().add(rt);
    }

    public static _typeArgs of(String...args){
        if( args.length == 0 ){
            return of();
        }
        if( args.length == 1){
            String a = args[0].trim();
            if( a.startsWith("<") && a.endsWith(">")){
                a = a.substring(1, a.length()-1).trim();
                if( a.length() == 0 ) {
                    ObjectCreationExpr oce = Expr.newExpr("new <> unknown()");
                    return of(oce);
                }
                ObjectCreationExpr oce = Expr.newExpr("new <"+ a + "> unknown()");
                return of( oce);
            }
            if( a.startsWith("<") ){
                a = a.substring(1, a.length()-1);
            }
            ObjectCreationExpr oce = Expr.newExpr("new <"+ a + "> unknown()");
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
        return of( Expr.newExpr("new<"+ sb.toString() + "> unknown()"));
    }

    public static _typeArgs of(NodeWithTypeArguments nwta){
         return new _typeArgs(nwta);
    }

    public static _feature._many<_typeArgs, _typeRef> TYPE_ARGS = new _feature._many<>(_typeArgs.class, _typeRef.class,
            _feature._id.TYPE_ARGS,
            _feature._id.TYPE_ARG,
            a -> a.list(),
            (_typeArgs p, List<_typeRef> _ccs) -> p.set(_ccs), PARSER, s->_typeRef.of(s))
            .setOrdered(false);

    public static _feature._features<_typeArgs> FEATURES = _feature._features.of(_typeArgs.class,  PARSER, TYPE_ARGS );

    public NodeWithTypeArguments parentNode;

    public _typeArgs(NodeWithTypeArguments nwta){
        this.parentNode = nwta;
    }

    public _feature._features<_typeArgs> features(){
        return FEATURES;
    }

    public _typeArgs add(Class...clazz){
        return add(Arrays.stream(clazz).map(a -> _typeRef.of(a) ).collect(Collectors.toList()).toArray(new _typeRef[0]));
    }

    public _typeArgs add(String...args){
        return add(Arrays.stream(args).map(a -> _typeRef.of(a) ).collect(Collectors.toList()).toArray(new _typeRef[0]));
    }

    public _typeArgs add(Type... astElements) {
        if( !this.parentNode.getTypeArguments().isPresent()){
            this.parentNode.setTypeArguments(new NodeList<Type>());
        }
        for( Type el : astElements ) {
            this.astList().add(el);
        }
        return this;
    }

    public _typeArgs add(_typeRef... elements) {
        if( !this.parentNode.getTypeArguments().isPresent()){
            this.parentNode.setTypeArguments(new NodeList<Type>());
        }
        for( _typeRef el : elements ) {
            this.astList().add(el.node());
        }
        return this;
    }

    public <N extends Node> N anchorNode(){
        return (N) parentNode;
    }

    @Override
    public boolean isEmpty(){
        return !hasTypeArgs() || size() == 0;
    }

    @Override
    public _typeArgs copy() {
        Node n = (Node) parentNode;
        return new _typeArgs( (NodeWithTypeArguments) (n.clone()) );
    }

    public _typeRef getAt(int index){
        NodeList<Type> tsa = (NodeList<Type>)this.parentNode.getTypeArguments().get();
        return _typeRef.of( tsa.get(index));
    }

    @Override
    public List<_typeRef> list() {
        if( !this.parentNode.getTypeArguments().isPresent()){
            return null; //null... and no diamond operator
        }
        return astList().stream().map(e-> _typeRef.of(e) ).collect(Collectors.toList());
    }

    @Override
    public NodeList<Type> astList() {
        if( !this.parentNode.getTypeArguments().isPresent()){
            return null; //null... and no diamond operator
        }
        return (NodeList<Type>)this.parentNode.getTypeArguments().get();
    }

    public boolean hasTypeArgs(){
        return this.parentNode.getTypeArguments().isPresent();
    }

    public boolean isUsingDiamondOperator(){
        return this.parentNode.isUsingDiamondOperator();
    }

    public _typeArgs setUseDiamondOperator(){
        this.parentNode.setDiamondOperator();
        return this;
    }

    public _typeArgs removeDiamondOperator(){
        this.parentNode.removeTypeArguments();
        return this;
    }

    public String toString(){
       return toString(new PrettyPrinterConfiguration());
    }

    public String toString(PrettyPrinterConfiguration ppc){
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
            sb.append(trs.get(i).toString(ppc));
        }
        return sb.toString();
    }

    public int hashCode(){
        if( !this.parentNode.getTypeArguments().isPresent()){
            return 57;
        }
        return 31 * this.parentNode.getTypeArguments().get().hashCode();
    }

    public boolean is(String code){
        return is(new String[]{code});
    }

    public boolean is(String...code){
        String allCode = Text.combine(code);
        //short circuit for matchAny
        if( allCode.startsWith("$") && allCode.endsWith("$")){
            Stencil st = Stencil.of(allCode);
            if( st.isMatchAny() ){
                return true;
            }
        }
        try {
            _typeArgs _otas = PARSER.apply(allCode);
            if( _otas.anyMatch(_ta-> !Stencil.of(_ta.toString()).isFixedText() )){
                //if ANY of the type args have $var$ s
                for(int i=0;i<_otas.size(); i++){
                    _typeRef _t = _otas.getAt(i);
                    Stencil st = Stencil.of(_t.toString());
                    if( st.isFixedText() ){
                        if( ! has(_t)){
                            return false;
                        }
                    } else{
                        if( ! has( tr-> st.matches(tr.toString()))){
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return Objects.equals(this, _otas);
            }
        }catch(Exception e){
            return false;
        }
    }

    public boolean equals( Object o){
        if( o instanceof _typeArgs){
            _typeArgs _as = (_typeArgs) o;
            if( !_as.hasTypeArgs() ){
                return !this.hasTypeArgs();
            }
            if( !this.hasTypeArgs()){
                return false;
            }
            NodeList<Type> tt = (NodeList<Type>)this.parentNode.getTypeArguments().get();
            NodeList<Type> ot = (NodeList<Type>)_as.parentNode.getTypeArguments().get();
            if( tt.size() == ot.size()){
                for(int i=0;i<tt.size(); i++){
                    Type t = tt.get(i);
                    if( !Types.equal(t, ot.get(i))){
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
            return astList().indexOf(target);
        }
        return -1;
    }

    /**
     * TypeArgument set used in:
     * {@link _methodCallExpr}
     * {@link _constructorCallStmt}
     * {@link _newExpr}
     * (order matters list of {@link _typeRef} )
     */
    public interface _withTypeArgs<N extends Node, _WTA extends _tree._node> extends _tree._node<N, _WTA> {

        /**
         * Creates and returns an {@link _typeArgs} to model the whole arguments list
         * @return an _args modelling 0...n arguments in the arguments list)
         */
        default _typeArgs getTypeArgs(){
            return of( (NodeWithTypeArguments) node());
        }

        default _WTA setTypeArgs(_typeArgs _tas){
            if( _tas.hasTypeArgs() ) {
                ((NodeWithTypeArguments) node()).setTypeArguments(_tas.astList());
            } else{
                ((NodeWithTypeArguments) node()).removeTypeArguments();
            }
            return (_WTA)this;
        }

        /**
         * gets the first type argument that matches the _matchFn
         * @param _matchFn
         * @return
         */
        default _typeRef getTypeArg(Predicate<_typeRef> _matchFn){
            if( hasTypeArgs()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments) node()).getTypeArguments().get();
                for(int i=0;i<tt.size(); i++){
                    _typeRef _a = _typeRef.of(tt.get(i));
                    if( _matchFn.test(_a) ){
                        return _a;
                    }
                }
            }
            return null;
        }

        default _typeRef getTypeArg(int index){
            if( hasTypeArgs()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments) node()).getTypeArguments().get();
                return _typeRef.of( tt.get(index) );
            }
            return null;
            //throw new _jdraftException("no type argument at ["+ index+"]");
        }

        default _WTA removeTypeArg(int index){
            if( hasTypeArgs()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments) node()).getTypeArguments().get();
                tt.remove(index);
                return (_WTA)this;
            }
            throw new _jdraftException("no type argument at ["+ index+"]");
        }

        default boolean hasTypeArgs(){
            return ((NodeWithTypeArguments) node()).getTypeArguments().isPresent();
        }

        default List<_typeRef> listTypeArgs(){
            if( hasTypeArgs()){
                NodeList<Type> tt = (NodeList<Type>) ((NodeWithTypeArguments) node()).getTypeArguments().get();
                return tt.stream().map( t-> _typeRef.of( t )).collect(Collectors.toList());
            }
            return null;
        }

        default List<_typeRef> listTypeArgs(Predicate<_typeRef> matchFn){
            List<_typeRef> lts = listTypeArgs();
            if( lts == null ){
                return null;
            }
            return lts.stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean isTypeArgs(Type... es){
            _typeRef[] _es = new _typeRef[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _typeRef.of(es[i]);
            }
            return isTypeArgs(_es);
        }

        default boolean isTypeArgs(_typeRef... _es){
            List<_typeRef> _tes = listTypeArgs();
            if(_es.length == _tes.size()){
                for(int i=0;i<_es.length;i++){
                    if( ! Types.equal( _es[i].node(), _tes.get(i).node() ) ){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isTypeArgs(Predicate<_typeArgs> matchFn){
            return matchFn.test( getTypeArgs() );
        }

        /**
         * Check if all individual ({@link _typeRef}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allTypeArgs(Predicate<_typeRef> matchFn){
            if( listTypeArgs() != null ) {
                return listTypeArgs().stream().allMatch(matchFn);
            }
            return false;
        }

        default boolean isUsingDiamondOperator(){
            return this.getTypeArgs().isUsingDiamondOperator();
        }

        default _WTA setUseDiamondOperator(){
            this.getTypeArgs().setUseDiamondOperator();
            return (_WTA)this;
        }

        default _WTA addTypeArg(Type e){
            _typeArgs _ta = getTypeArgs();
            _ta.add(e);
            return (_WTA)this;
        }

        default _WTA addTypeArgs(Class... clazz){
            _typeArgs _ta = getTypeArgs();
            Arrays.stream(clazz).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA addTypeArgs(String... es){
            _typeArgs _ta = getTypeArgs();
            Arrays.stream(es).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA addTypeArgs(Type... es){
            _typeArgs _ta = getTypeArgs();
            Arrays.stream(es).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA addTypeArgs(_typeRef... _tr){
            _typeArgs _ta = getTypeArgs();
            Arrays.stream(_tr).forEach(e -> _ta.add(e));
            return (_WTA)this;
        }

        default _WTA removeTypeArgs(){
            ((NodeWithTypeArguments) node()).removeTypeArguments();
            return (_WTA)this;
        }

        default _WTA removeTypeArgs(Predicate<_typeRef> matchFn){
            _typeArgs _ta = getTypeArgs();
            _ta.remove(matchFn);
            return (_WTA)this;
        }

        default _WTA removeTypeArgs(_typeRef... es){
            _typeArgs _ta = getTypeArgs();
            for(int i=0;i<es.length;i++){
                _ta.remove(es[i]);
            }
            return (_WTA)this;
        }

        default _WTA removeTypeArgs(Type... es){
            _typeArgs _ta = getTypeArgs();
            for(int i=0;i<es.length;i++){
                _ta.remove(es[i]);
            }
            return (_WTA)this;
        }

        default _WTA forTypeArgs(Consumer<_typeRef> argFn){
            _typeArgs _ta = getTypeArgs();
            _ta.toEach(argFn);
            return (_WTA)this;
        }

        default _WTA forTypeArgs(Predicate<_typeRef> matchFn, Consumer<_typeRef> argFn){
            _typeArgs _ta = getTypeArgs();
            _ta.toEach(matchFn, argFn);
            return (_WTA)this;
        }
    }
}
