package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.type.Type;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class _methodReference implements _expression<MethodReferenceExpr, _methodReference>,
        _java._multiPart<MethodReferenceExpr, _methodReference> {

    public static _methodReference of(){
        return new _methodReference( new MethodReferenceExpr());
    }
    public static _methodReference of(MethodReferenceExpr mre){
        return new _methodReference(mre);
    }
    public static _methodReference of( String...code){
        return new _methodReference(Ex.methodReferenceEx( code));
    }

    public MethodReferenceExpr mre;

    public _methodReference(MethodReferenceExpr mre){
        this.mre = mre;
    }

    @Override
    public _methodReference copy() {
        return new _methodReference(this.mre.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.methodReferenceEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(MethodReferenceExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public MethodReferenceExpr ast(){
        return mre;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.SCOPE, mre.getScope());
        if( mre.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, mre.getTypeArguments().get());
        }
        comps.put(_java.Component.IDENTIFIER, mre.getId());
        return comps;
    }

    public boolean isScope(String...scope){
        return Objects.equals( _expression.of(scope), getScope());
    }

    public boolean isScope( _expression _e ){
        return Objects.equals( _e, getScope());
    }

    public boolean isScope( Expression e ){
        return Objects.equals( _expression.of(e), getScope());
    }
    public _methodReference setScope(String...scope){
        this.mre.setScope(Ex.of(scope));
        return this;
    }

    public _methodReference setScope( _expression _e ){
        this.mre.setScope(_e.ast());
        return this;
    }

    public _methodReference setScope( Expression e ){
        this.mre.setScope(e);
        return this;
    }

    public _expression getScope(){
        return _expression.of(this.mre.getScope());
    }

    public List<_typeRef> listTypeArgs(){
        if(this.mre.getTypeArguments().isPresent()){
            return this.mre.getTypeArguments().get().stream().map( ta-> _typeRef.of(ta) ).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public _methodReference setTypeArgs( _typeRef..._tas){
        NodeList<Type> tas = new NodeList<>();
        Arrays.stream(_tas).forEach(t-> tas.add( t.ast()));
        this.mre.setTypeArguments(tas);
        return this;
    }

    public _methodReference setTypeArgs( Type..._tas){
        NodeList<Type> tas = new NodeList<>();
        Arrays.stream(_tas).forEach(t-> tas.add( t));
        this.mre.setTypeArguments(tas);
        return this;
    }

    public boolean isIdentifier( String id){
        return Objects.equals( this.mre.getIdentifier(), id);
    }

    public boolean isIdentifier( Predicate<String> matchFn){
        return matchFn.test( this.mre.getIdentifier() );
    }

    public _methodReference setIdentifier( String id){
        this.mre.setIdentifier(id);
        return this;
    }

    public String getIdentifier(){
        return this.mre.getIdentifier();
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public List<_typeRef> getTypeArguments(){
        if( mre.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            mre.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }

    public boolean equals(Object other){
        if( other instanceof _methodReference){
            return ((_methodReference)other).mre.equals( this.mre);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.mre.hashCode();
    }
    
    public String toString(){
        return this.mre.toString();
    }
}
