package org.jdraft;

import com.github.javaparser.ast.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Sometimes we have groupings of entities that do not
 * map to a specific Ast entity but a grouping of AST entities
 *
 * @see _annos<com.github.javaparser.ast.expr.AnnotationExpr,_anno>
 * @see _imports<com.github.javaparser.ast.ImportDeclaration,_import>
 * @see _modifiers <com.github.javaparser.ast.Modifier,_modifier>
 * @see _parameters<com.github.javaparser.ast.body.Parameter,_parameter>
 * @see _throws<com.github.javaparser.ast.type.ReferenceType,_typeRef>
 * @see _typeParameters<com.github.javaparser.ast.type.TypeParameter,_typeParameter>
 *
 */
interface _nodeList<EL extends Node, _EL extends _node, _NL extends _nodeList> extends _mrJava {

    _NL copy();

    default boolean isEmpty(){
        return size() == 0;
    }

    default int size(){
        return listAstElements().size();
    }

    List<_EL> listElements();

    List<EL> listAstElements();

    default List<_EL> listElements(Predicate<_EL> matchFn){
        return listElements().stream().filter(matchFn).collect(Collectors.toList());
    }

    default List<EL> listAstElements(Predicate<EL> matchFn){
        return listAstElements().stream().filter(matchFn).collect(Collectors.toList());
    }

    default boolean is( List<_EL> _els){
        if( this.size() != _els.size() ){
            return false;
        }
        List<_EL> _tels = listElements();
        for(int i=0;i<_els.size(); i++){
            if( !Objects.equals(_els.get(i), _tels.get(i))){
                return false;
            }
        }
        return true;
    }

    default _EL get(Predicate<_EL> matchFn){
        List<_EL> _els = this.listElements(matchFn);
        if( _els.isEmpty() ){
            return null;
        }
        return _els.get(0);
    }

    default _EL get(int index){
        return this.listElements().get(index);
    }

    default EL getAst(int index){
        return this.listAstElements().get(index);
    }

    default int indexOf( _EL target){
        return listElements().indexOf(target);
    }

    default int indexOf( EL target){
        return listAstElements().indexOf(target);
    }

    default boolean has(_EL target){
        return !listElements( el-> el.equals(target)).isEmpty();
    }

    default boolean has(Predicate<_EL> matchFn){
        return !listElements( matchFn).isEmpty();
    }

    default boolean has(EL target){
        return !listAstElements( el-> el.equals(target)).isEmpty();
    }

    default _NL add( EL... astElements ) {
        for( EL el : astElements ) {
            this.listAstElements().add(el);
        }
        return (_NL)this;
    }

    default _NL add( _EL... elements ) {
        for( _EL el : elements ) {
            this.listAstElements().add( (EL)el.ast());
        }
        return (_NL)this;
    }

    default _NL remove( _EL... _els ) {
        Arrays.stream( _els ).forEach(e -> this.listAstElements().remove( e.ast() ) );
        return (_NL)this;
    }

    default _NL remove( EL... els ) {
        Arrays.stream(els ).forEach( e -> this.listAstElements().remove( e ) );
        return (_NL)this;
    }

    default _NL remove( Predicate<_EL> _matchFn ) {
        this.listElements(_matchFn).stream().forEach( e-> remove(e) );
        return (_NL)this;
    }

    default _NL forEach(Predicate<_EL> matchFn, Consumer<_EL> consumer ){
        listElements(matchFn).forEach(consumer);
        return (_NL)this;
    }

    default _NL forEach(Consumer<_EL> consumer ){
        listElements().forEach(consumer);
        return (_NL)this;
    }
}
