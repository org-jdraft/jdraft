package org.jdraft.diff;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._annos._withAnnoExprs;
import org.jdraft._body._withBody;
import org.jdraft._constructor._withConstructors;
import org.jdraft._field._withFields;
import org.jdraft._javadocComment._withJavadoc;
import org.jdraft._method._withMethods;
import org.jdraft._modifiers._withModifiers;
import org.jdraft._receiverParam._withReceiverParam;
import org.jdraft._throws._withThrows;
import org.jdraft._typeParams._withTypeParams;
import org.jdraft._initBlock._withInitBlocks;

import org.jdraft.diff._diffNode._edit;

/**
 * <P>
 * Provides a mechanism to _walk and difference (at each "component") for a draft
 * entities.
 *
 * NOTE: this is MUCH different than testing AST Nodes for syntactical equality
 * because of
 * <UL>
 * <LI>Inferred Modifiers:
 * <PRE>
 * (i.e. a method with no modifiers on an interface is inferred to be public,
 * but not syntactically equal to an interface with an IMPLICIT marking of public)
 *
 *  (not syntactically equal, but equivalent in meaning)
 * interface i {int p;}  =/= interface i{ public int p;}
 * </PRE>
 * </LI>
 * <LI>Ordering: the order of many things (i.e. thrown exceptions,
 * typeParameters) matters from a readability perspective (we dont want to
 * change the syntactic order in which exceptions are thrown in the code). But
 * the underlying semantics are the same regardless if your throws clause has A,
 * then B or B, then A
 *
 * (not syntactically equal, but equivalent in meaning) void m() throws A, B;
 * =/= void m() throws B, A
 * </LI>
 * <LI>Fully Qualified Types vs "Bare" when comparing types if they have the
 * same name and are from an explicitly different packages, they should not be
 * equivalent. But the following ARE equivalent (specifiying the fully qualified
 * type on one hand should
 *
 * java.util.Map<java.net.URL,String> map; =/= Map<URL,String> map;
 *
 * public interface Semantic<A> {
 *
 * Are two entities "semantically" equivalent? Note: we need this level
 * difference between equality/equivalency precisely because we often times
 * cannot look at syntactical equality
 *
 * here are a few examples that are syntactically NOT EQUAL but semantically
 * EQUIVALENT
 *
 * (although not syntactically the same, semantically they MEAN the same thing)
 * interface i{ int x(); } interface i{ public int x(); }
 *
 * <H3>Semantic (verses Syntactic) Diff</H3>
 * <P>
 * This diff avoids a naive "syntactic diff"s and opts for semantic differences
 * where possible.</P>
 *
 * <P>
 * For instance, these two interfaces:
 * <PRE>
 * interface A{
 *     int a = 100;
 * }
 *
 * interface B{
 *     public static final int a=100;
 * }
 * </PRE>
 * <P>
 * ...ARE syntactically different, but semantically the same (the field a on the
 * interface above has "implied" modifiers "public static final")... So if we
 * call _diff on the two Classes above they would be the SAME assertTrue(
 * _diff.fieldsOf(A.class, B.class).isEmpty() );
 *
 *
 * @author Eric
 */
public interface _diff {


    /**
     * Build _type models and diff the two types
     *
     * @param clazz
     * @param clazz2
     * @return
     */
    static _diff of(Class clazz, Class clazz2) {
        return of((_type) _type.of(clazz), _type.of(clazz2));
    }

    static _diff of(_type _left, _type _right) {
        return _typeDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_class _left, _class _right) {
        return _classDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_enum _left, _enum _right) {
        return _enumDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_interface _left, _interface _right) {
        return _interfaceDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_annotation _left, _annotation _right) {
        return _annotationDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_field _left, _field _right) {
        return _fieldDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_method _left, _method _right) {
        return _methodDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_constructor _left, _constructor _right) {
        return _constructorDiff.INSTANCE.diff(_left, _right);
    }

    static _diff of(_constant _left, _constant _right) {
        return _enumDiff.ENUM_CONSTANT_DIFF.diff(_left, _right);
    }

    static _diff of(_annoMember _left, _annoMember _right) {
        return _annotationDiff.ANNOTATION_ELEMENT_DIFF.diff(_left, _right);
    }

    static _diff extendsOf(_type left, _type right) {
        return _extendsDiff.INSTANCE.diff(left, right);
    }

    static _diff implementsOf(_type left, _type right) {
        return _implementsDiff.INSTANCE.diff(left, right);
    }

    static _diff importsOf(_type left, _type right) {
        return _importsDiff.INSTANCE.diff(left, right);
    }

    static _diff constantsOf(_enum left, _enum right) {
        return _enumDiff.ENUM_CONSTANTS_DIFF.diff(left.listConstants(), right.listConstants());
    }

    static _diff elementsOf(_annotation left, _annotation right) {
        return _annotationDiff.ANNOTATION_ELEMENTS_DIFF.diff(left.listEntries(), right.listEntries());
    }

    //so if a _hasConstructor has a no arg construstor with no body, it should be 
    // NOT a diff (I should post-process remove a diff
    static _diff constructorsOf(_withConstructors left, _withConstructors right) {
        return _constructorsDiff.INSTANCE.diff(left, right);
    }

    static _diff throwsOf(_withThrows left, _withThrows right) {
        return _throwsDiff.INSTANCE.diff(left, right);
    }

    static _diff fieldsOf(_withFields left, _withFields right) {
        return _fieldsDiff.INSTANCE.diff(left, right);
    }

    static _diff methodsOf(_withMethods left, _withMethods right) {
        return _methodsDiff.INSTANCE.diff(left, right);
    }

    static _diff annosOf(_withAnnoExprs left, _withAnnoExprs right) {
        return _annosDiff.INSTANCE.diff(left, right);
    }

    static _diff javadocOf(_withJavadoc left, _withJavadoc right) {
        return _javadocCommentDiff.INSTANCE.diff(left, right);
    }

    static _diff bodyOf(_withBody left, _withBody right) {
        return _bodyDiff.INSTANCE.diff(left, right);
    }

    static _diff modifiersOf(_withModifiers left, _withModifiers right) {
        return _modifiersDiff.INSTANCE.diff(left, right);
    }

    static _diff nestsOf(_type left, _type right) {
        return _innerTypesDiff.INSTANCE.diff(left, right);
    }

    static _diff packageNameOf(_type left, _type right) {
        return _packageNameDiff.INSTANCE.diff(left, right);
    }

    static _diff typeParametersOf(_withTypeParams left, _withTypeParams right) {
        return _typeParamsDiff.INSTANCE.diff(left, right);
    }

    static _diff staticBlocksOf(_withInitBlocks left, _withInitBlocks right) {
        return _initBlocksDiff.INSTANCE.diff(left, right);
    }

    static _diff receiverParameterOf(_withReceiverParam left, _withReceiverParam right) {
        return _receiverParamDiff.INSTANCE.diff(left, right);
    }

    /**
     * Signifies that the collection of Diffs is mutable (we can add diffsNodes
     * to the List)
     */
    interface _build extends _diff {
        _build addDiff(_diffNode d);
    }

    /**
     * @return a list of all diff nodes
     */
    List<_diffNode> list();

    /** gets the left root node of the diff (could be null for localized diffs) */
    _tree._node leftRoot();

    /** gets the right root node of the diff (could be null for localized diffs) */
    _tree._node rightRoot();

    /**
     * @return number of diffs
     */
    default int size() {
        return list().size();
    }

    /**
     *
     * @return true if there are no known diffs between the (2) entities
     */
    default boolean isEmpty() {
        return list().isEmpty();
    }

    /**
     * List all filePaths that have diffs
     *
     * @return
     */
    default List<_nodePath> paths() {
        return list().stream().map(d -> d.path()).collect(Collectors.toList());
    }

    /**
     * Return the _diffNode at the path, (or null if no diff exists at this
     * path)
     *
     * @param _p the underlying _path
     * @return the _diffNode at this path, or null if not found
     */
    default _diffNode at(_nodePath _p) {
        return first(d -> d.path().equals(_p));
    }
    
    default _diffNode on(Class<? extends _tree._node> clazz){
        return first( d-> d.on(clazz));
    }
    
    default _diffNode on(Class<? extends _tree._node> clazz, String id){
        return first( d-> d.on(clazz, id));
    }
    
    default _diffNode on(Feature feature){
        return first( d-> d.on(feature));
    }
    
    default _diffNode on(Feature feature, String id){
        return first( d-> d.on(feature, id));
    }
    
    default _diffNode leftOnlyAt(_nodePath _p){
        return first(d -> d.isLeftOnly() && d.path().equals(_p));
    }
    
    default <_N extends _tree._node> _diffNode leftOnlyAt(Class<_N> nodeClass){
        return first(d -> d.isLeftOnly() && d.at(Feature.of(nodeClass)));
    }
    
    default <_N extends _tree._node> _diffNode leftOnlyAt(Class<_N> nodeClass, String id){
        return first(d -> d.isLeftOnly() && d.at(Feature.of(nodeClass), id));
    }
    
    default _diffNode leftOnlyAt(String id){
        return first(d -> d.isLeftOnly() && d.at(id));
    }
    
    default _diffNode leftOnlyAt(Feature feature){
        return first(d -> d.isLeftOnly() && d.at(feature));
    }
    
    default _diffNode leftOnlyAt(Feature feature, String id){
        return first(d -> d.isLeftOnly() && d.at(feature, id));
    }
    
    default <_N extends _tree._node> _diffNode leftOnlyOn(Class<_N> nodeClass){
        return first( d-> d.isLeftOnly() && d.on( Feature.of(nodeClass) ) );
    }
    
    default _diffNode leftOnlyOn(String id){
        return first( d-> d.isLeftOnly() && d.on( id ) );
    }
    
    default <_N extends _tree._node>_diffNode leftOnlyOn(Class<_N> nodeClass, String id){
        return first( d-> d.isLeftOnly() && d.on( Feature.of(nodeClass), id ) );
    }
    
    default _diffNode leftOnlyOn(Feature feature){
        return first( d-> d.isLeftOnly() && d.on(feature) );
    }
    
    default _diffNode leftOnlyOn(Feature feature, String id){
        return first( d-> d.isLeftOnly() && d.on(feature, id) );
    }
    
    default _diffNode rightOnlyAt(_nodePath _p){
        return first(d -> d.isRightOnly() && d.path().equals(_p));
    }
    
    default <_N extends _tree._node> _diffNode rightOnlyAt(Class<_N> nodeClass){
        return first(d -> d.isRightOnly() && d.at(Feature.of(nodeClass)));
    }
    
    default <_N extends _tree._node> _diffNode rightOnlyAt(Class<_N> nodeClass, String id){
        return first(d -> d.isRightOnly() && d.at(Feature.of(nodeClass), id));
    }
    
    default _diffNode rightOnlyAt(String id){
        return first(d -> d.isRightOnly() && d.at(id));
    }
    
    default _diffNode rightOnlyAt(Feature feature){
        return first(d -> d.isRightOnly() && d.at(feature));
    }
    
    default _diffNode rightOnlyAt(Feature feature, String id){
        return first(d -> d.isRightOnly() && d.at(feature, id));
    }
    
    default <_N extends _tree._node> _diffNode rightOnlyOn(Class<_N> nodeClass){
        return first( d-> d.isRightOnly() && d.on( Feature.of(nodeClass) ) );
    }

    default _diffNode rightOnlyOn(String id){
        return first( d-> d.isRightOnly() && d.on( id ) );
    }
    
    default <_N extends _tree._node>_diffNode rightOnlyOn(Class<_N> nodeClass, String id){
        return first( d-> d.isRightOnly() && d.on( Feature.of(nodeClass), id ) );
    }
    
    default _diffNode rightOnlyOn(Feature feature){
        return first( d-> d.isRightOnly() && d.on(feature) );
    }
    
    default _diffNode rightOnlyOn(Feature feature, String id){
        return first( d-> d.isRightOnly() && d.on(feature, id) );
    }
    
    /** change */
    
    /**
     * return the first change at this exact path (or null if none found)
     * @param _p
     * @return 
     */
    default _diffNode changeAt(_nodePath _p){
        return first(d -> d.isChange() && d.path().equals(_p));
    }
    
    default <_N extends _tree._node> _diffNode changeAt(Class<_N> nodeClass){
        return first(d -> d.isChange() && d.at(Feature.of(nodeClass)));
    }

    default  _diffNode changeAt(String id){
        return first(d -> d.isChange() && d.at(id));
    }
    
    default <_N extends _tree._node> _diffNode changeAt(Class<_N> nodeClass, String id){
        return first(d -> d.isChange() && d.at(Feature.of(nodeClass), id));
    }
    
    default _diffNode changeAt(Feature feature){
        return first(d -> d.isChange() && d.at(feature));
    }
    
    default _diffNode changeAt(Feature feature, String id){
        return first(d -> d.isChange() && d.at(feature, id));
    }
    
    default <_N extends _tree._node> _diffNode changeOn(Class<_N> nodeClass){
        return first( d-> d.isChange() && d.on( Feature.of(nodeClass) ) );
    }
    
    default _diffNode changeOn(String id){
        return first( d-> d.isChange() && d.on( id ) );
    }
    
    default <_N extends _tree._node>_diffNode changeOn(Class<_N> nodeClass, String id){
        return first( d-> d.isChange() && d.on( Feature.of(nodeClass), id ) );
    }
    
    default _diffNode changeOn(Feature feature){
        return first( d-> d.isChange() && d.on(feature) );
    }
    
    default _diffNode changeOn(Feature feature, String id){
        return first( d-> d.isChange() && d.on(feature, id) );
    }
    
    /** edit */
        /** change */
    
    /**
     * return the first edit that exists at this exact path (or null if none found)
     * @param _p
     * @return 
     */
    default _diffNode editAt(_nodePath _p){
        return first(d -> d.isEdit() && d.path().equals(_p));
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode editAt(Class<_N> nodeClass){
        return first(d -> d.isEdit() && d.at(Feature.of(nodeClass)));
    }

    /**
     *
     * @param id
     * @return
     */
    default _diffNode editAt(String id){
        return first(d -> d.isEdit() && d.at(id));
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode editAt(Class<_N> nodeClass, String id){
        return first(d -> d.isEdit() && d.at(Feature.of(nodeClass), id));
    }

    /**
     *
     * @param feature
     * @return
     */
    default _diffNode editAt(Feature feature){
        return first(d -> d.isEdit() && d.at(feature));
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default _diffNode editAt(Feature feature, String id){
        return first(d -> d.isEdit() && d.at(feature, id));
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode editOn(Class<_N> nodeClass){
        return first( d-> d.isEdit() && d.on( Feature.of(nodeClass) ) );
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node>_diffNode editOn(Class<_N> nodeClass, String id){
        return first( d-> d.isEdit() && d.on( Feature.of(nodeClass), id ) );
    }

    /**
     *
     * @param id
     * @return
     */
    default _diffNode editOn(String id){
        return first( d-> d.isEdit() && d.on( id ) );
    }

    /**
     *
     * @param feature
     * @return
     */
    default _diffNode editOn(Feature feature){
        return first( d-> d.isEdit() && d.on(feature) );
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default _edit editOn(Feature feature, String id){
        return (_edit) first( d-> d.isEdit() && d.on(feature, id) );
    }
    
    /**
     * returns the DiffNode that can be found at this (Full) path
     *
     * @param pathAsTokens the tokens that make up the path
     * @return the _diffNode found at this path or null if not found
     */
    default _diffNode at(Object... pathAsTokens) {
        return at(_nodePath.of(pathAsTokens));
    }

    /**
     * IS there a Diff Node at the specified Component type?
     *
     * @param feature the component type
     * @return true if there is a diff node at this type
     */
    default boolean isAt(Feature feature) {
        return first(d -> d.at(feature)) != null;
    }

    /**
     *
     * @param id
     * @return
     */
    default boolean isAt(String id){
        return first(d -> d.at(id)) != null;
    }
    
    /**
     * 
     * @param <_N>
     * @param c
     * @return 
     */
    default <_N extends _tree._node> boolean isAt(Class<_N> c ){
        return isAt( Feature.of(c) );
    }    
    
    /**
     * Is there a Diff Node at (i.e. the LAST part of the path) the specified
     * Component type
     *
     * @param feature the component type
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default boolean isAt(Feature feature, String id) {
        return first(d -> d.at(feature) && d.at(id)) != null;
    }

    /**
     * Is there a Diff Node at (i.e.the LAST part of the path) the specified
     * Component type
     *
     * @param <_N> the _java node class
     * @param node the node class (i.e. _method.class, _field.class)
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default <_N extends _tree._node> boolean isAt(Class<_N>node, String id) {
        return isAt(Feature.of(node),id);
    }
    
    /**
     * IS there a Diff Node at the specified Component type?
     *
     * @param feature the component type
     * @return true if there is a diff node at this type
     */
    default boolean isOn(Feature feature) {
        return first(d -> d.on(feature)) != null;
    }

    /**
     * 
     * @param <_N>
     * @param c
     * @return 
     */
    default <_N extends _tree._node> boolean isOn(Class<_N> c ){
        return isOn( Feature.of(c) );
    }    

    /**
     * Is there a Diff Node at (i.e. the LAST part of the path) the specified
     * Component type
     *
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default boolean isOn(String id) {
        return first(d -> d.on(id)) != null;
    }
    
    /**
     * Is there a Diff Node at (i.e. the LAST part of the path) the specified
     * Component type
     *
     * @param feature the component type
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default boolean isOn(Feature feature, String id) {
        return first(d -> d.on(feature,id)) != null;
    }

    /**
     * Is there a Diff Node at (i.e.the LAST part of the path) the specified
     * Component type
     *
     * @param <_N> the _java node class
     * @param node the node class (i.e. _method.class, _field.class)
     * @param id the identifier for the node
     * @return true if a Diff was found at this Node, false otherwise
     */
    default <_N extends _tree._node> boolean isOn(Class<_N>node, String id) {
        return isOn(Feature.of(node),id);
    }
    
    /**
     * Is there any Object that is either added, removed, or changed that is
     * equal to this object
     *
     * @param obj
     * @return
     */
    default boolean isDiffOf(Object obj) {
        return first(d -> {
            if (d.isRightOnly()) {
                return d.asRightOnly().right().equals(obj);
            }
            if (d.isLeftOnly()) {
                return d.asLeftOnly().left().equals(obj);
            }
            if (d.isChange()) {
                return d.asChange().left().equals(obj)
                        || d.asChange().right().equals(obj);
            }
            return false;
        }) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasRightOnly() {
        return first(d -> d.isRightOnly()) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasChange() {
        return first(d -> d.isChange()) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasLeftOnly() {
        return first(d -> d.isLeftOnly()) != null;
    }

    /**
     *
     * @return
     */
    default boolean hasEdit() {
        return first(d -> d.isEdit()) != null;
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasRightOnlyAt(Feature feature) {
        return first(d -> d.at(feature) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasRightOnlyAt(Class<_N> memClass) {
        return hasRightOnlyAt( Feature.of(memClass) );
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasLeftOnlyAt(Feature feature) {
        return first(d -> d.at(feature) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasLeftOnlyAt(Class<_N> memClass) {
        return hasLeftOnlyAt( Feature.of(memClass) );
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasChangeAt(Feature feature) {
        return first(d -> d.at(feature) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasChangeAt(Class<_N> memClass) {
        return hasChangeAt( Feature.of(memClass) );
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasEditAt(Feature feature) {
        return first(d -> d.at(feature) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasEditAt(Class<_N> memClass) {
        return hasEditAt( Feature.of(memClass) );
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasRightOnlyAt(Feature feature, String id) {
        return first(d -> d.at(feature, id) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasRightOnlyAt(Class<_N> memClass, String id) {
        return hasRightOnlyAt( Feature.of(memClass) ,id);
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasLeftOnlyAt(Feature feature, String id) {
        return first(d -> d.at(feature, id) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasLeftOnlyAt(Class<_N> memClass, String id) {
        return hasLeftOnlyAt( Feature.of(memClass) ,id);
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasChangeAt(Feature feature, String id) {
        return first(d -> d.at(feature, id) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasChangeAt(Class<_N> memClass, String id) {
        return hasChangeAt( Feature.of(memClass) ,id);
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasEditAt(Feature feature, String id) {
        return first(d -> d.at(feature, id) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasEditAt(Class<_N> memClass, String id) {
        return hasRightOnlyAt( Feature.of(memClass) ,id);
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasRightOnlyOn(Feature feature) {
        return first(d -> d.on(feature) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasRightOnlyOn(Class<_N> memClass) {
        return hasRightOnlyOn( Feature.of(memClass));
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasLeftOnlyOn(Feature feature) {
        return first(d -> d.on(feature) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasLeftOnlyOn(Class<_N> memClass) {
        return hasLeftOnlyOn( Feature.of(memClass));
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasChangeOn(Feature feature) {
        return first(d -> d.on(feature) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasChangeOn(Class<_N> memClass) {
        return hasChangeOn( Feature.of(memClass));
    }

    /**
     *
     * @param feature
     * @return
     */
    default boolean hasEditOn(Feature feature) {
        return first(d -> d.on(feature) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasEditOn(Class<_N> memClass) {
        return hasEditOn( Feature.of(memClass));
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasRightOnlyOn(Feature feature, String id) {
        return first(d -> d.on(feature, id) && d.isRightOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasRightOnlyOn(Class<_N> memClass, String id) {
        return hasRightOnlyOn( Feature.of(memClass), id);
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasLeftOnlyOn(Feature feature, String id) {
        return first(d -> d.on(feature, id) && d.isLeftOnly()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasLeftOnlyOn(Class<_N> memClass, String id) {
        return hasLeftOnlyOn( Feature.of(memClass), id);
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasChangeOn(Feature feature, String id) {
        return first(d -> d.on(feature, id) && d.isChange()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasChangeOn(Class<_N> memClass, String id) {
        return hasChangeOn( Feature.of(memClass), id);
    }

    /**
     *
     * @param feature
     * @param id
     * @return
     */
    default boolean hasEditOn(Feature feature, String id) {
        return first(d -> d.on(feature, id) && d.isEdit()) != null;
    }

    /**
     *
     * @param memClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> boolean hasEditOn(Class<_N> memClass, String id) {
        return hasEditOn( Feature.of(memClass), id);
    }

    /**
     * For each of the matching DiffNodes in the Tree, perform some action
     *
     * @param _nodeMatchFn function for matching specific _diffNodes
     * @param _nodeActionFn consumer action function on selected _diffNodes
     * @return the potentially modified) _diffTree
     */
    default _diff forEach(Predicate<_diffNode> _nodeMatchFn, Consumer<_diffNode> _nodeActionFn) {
        list(_nodeMatchFn).forEach(_nodeActionFn);
        return this;
    }

    /**
     * For each diff _node use run this _nodeActionFn
     *
     * @param _nodeActionFn action to take on each node
     * @return the potentially modified _diff
     */
    default _diff forEach(Consumer<_diffNode> _nodeActionFn) {
        list().forEach(_nodeActionFn);
        return this;
    }

    /**
     * For all diffs that are ADD (meaning they are ABSENT in LEFT and PRESENT
     * in RIGHT) check if they match the _addNodeMatchFn and, if so, execute the
     * _addNodeActionFn
     *
     * @param _rightOnlyMatchFn matching function for Add nodes
     * @param _rightOnlyActionFn the action for adds that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forRightOnlys(Predicate<_diffNode._rightOnly> _rightOnlyMatchFn, Consumer<_diffNode._rightOnly> _rightOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._rightOnly && _rightOnlyMatchFn.test((_diffNode._rightOnly) d));
        List<_diffNode._rightOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._rightOnly) n));
        ans.forEach(_rightOnlyActionFn);
        return this;
    }

    /**
     * For all diff _nodes that are ADD (meaning they are ABSENT in LEFT and
     * PRESENT in RIGHT) execute the _addNodeActionFn
     *
     * @param _rightOnlyActionFn the action for adds that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forRightOnlys(Consumer<_diffNode._rightOnly> _rightOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._rightOnly);
        List<_diffNode._rightOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._rightOnly) n));
        ans.forEach(_rightOnlyActionFn);
        return this;
    }

    /**
     * For all diffs that are REMOVE (meaning they are PRESENT in LEFT and
     * ABSENT in RIGHT) check if they match the _removeNodeMatchFn and, if so,
     * execute the _removeNodeActionFn
     *
     * @param leftOnlyMatchFn matching function for Remove nodes
     * @param leftOnlyActionFn the action for removes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forLeftOnlys(Predicate<_diffNode._leftOnly> leftOnlyMatchFn, Consumer<_diffNode._leftOnly> leftOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._leftOnly && leftOnlyMatchFn.test((_diffNode._leftOnly) d));
        List<_diffNode._leftOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._leftOnly) n));
        ans.forEach(leftOnlyActionFn);
        return this;
    }

    /**
     * For all diffs that are REMOVE (meaning they are PRESENT in LEFT and
     * ABSENT in RIGHT) execute the _removeNodeActionFn
     *
     * @param leftOnlyActionFn the action for removes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forLeftOnlys(Consumer<_diffNode._leftOnly> leftOnlyActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._leftOnly);
        List<_diffNode._leftOnly> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._leftOnly) n));
        ans.forEach(leftOnlyActionFn);
        return this;
    }

    /**
     * For all diffs that are CHANGE (meaning they are PRESENT in LEFT & RIGHT
     * but different) check if they match the _changeNodeMatchFn and, if so,
     * execute the _changeNodeActionFn
     *
     * @param changeMatchFn matching function for change nodes
     * @param changeActionFn the action for changes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forChanges(Predicate<_diffNode._change> changeMatchFn, Consumer<_diffNode._change> changeActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._change && changeMatchFn.test((_diffNode._change) d));
        List<_diffNode._change> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._change) n));
        ans.forEach(changeActionFn);
        return this;
    }

    /**
     * For all diffs that are CHANGE (meaning they are PRESENT in LEFT & RIGHT
     * but different) check if they match the _changeNodeMatchFn and, if so,
     * execute the _changeNodeActionFn
     *
     * @param changeActionFn the action for changes that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forChanges(Consumer<_diffNode._change> changeActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._change);
        List<_diffNode._change> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._change) n));
        ans.forEach(changeActionFn);
        return this;
    }

    /**
     * For all diffs that are EDITs (meaning they are PRESENT in LEFT & RIGHT
     * but different on a textual level... this means the underlying code is put
     * through a diff) execute the _editNodeActionFn
     *
     * @param editActionFn the action for edits that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forEdits(Consumer<_diffNode._edit> editActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._edit);
        List<_diffNode._edit> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._edit) n));
        ans.forEach(editActionFn);
        return this;
    }

    /**
     * For all diffs that are EDITs (meaning they are PRESENT in LEFT & RIGHT
     * but different on a textual level... this means the underlying code is put
     * through a diff) check if they match the _editNodeMatchFn and, if so,
     * execute the _editNodeActionFn
     *
     * @param editMatchFn matching function for edit nodes
     * @param editActionFn the action for edits that pass the matchFn
     * @return the potentially modified _diff
     */
    default _diff forEdits(Predicate<_diffNode._edit> editMatchFn, Consumer<_diffNode._edit> editActionFn) {
        List<_diffNode> ns = list(d -> d instanceof _diffNode._edit && editMatchFn.test((_diffNode._edit) d));
        List<_diffNode._edit> ans = new ArrayList<>();
        ns.forEach(n -> ans.add((_diffNode._edit) n));
        ans.forEach(editActionFn);
        return this;
    }

    /**
     * List all add nodes that comply with the _addNodeActionFn
     *
     * @param rightOnlyMatchFn match function for selecting diff nodes
     * @return a list of _addNodes
     */
    default List<_diffNode._rightOnly> listRightOnlys(Predicate<_diffNode._rightOnly> rightOnlyMatchFn) {
        List<_diffNode._rightOnly> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._rightOnly && rightOnlyMatchFn.test((_diffNode._rightOnly) d))
                .forEach(e -> ens.add((_diffNode._rightOnly) e));
        return ens;
    }

    /**
     * Lists the diffs that are add Nodes (not found in the left, but found in
     * the right)
     *
     * @see _diffNode._rightOnly
     * @return a list of _addNode that are FOUND on the RIGHT and NOT FOUND on
     * the LEFT
     */
    default List<_diffNode._rightOnly> listRightOnlys() {
        List<_diffNode._rightOnly> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._rightOnly).forEach(n -> tds.add((_diffNode._rightOnly) n));
        return tds;
    }

    /**
     * List all remove nodes that comply with the _removeNodeActionFn
     *
     * @param leftOnlyMatchFn match function for selecting _removeNode s
     * @return a list of _removeNodes that match the _removeNodeMatchFn
     */
    default List<_diffNode._leftOnly> listLeftOnlys(Predicate<_diffNode._leftOnly> leftOnlyMatchFn) {
        List<_diffNode._leftOnly> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._leftOnly && leftOnlyMatchFn.test((_diffNode._leftOnly) d))
                .forEach(e -> ens.add((_diffNode._leftOnly) e));
        return ens;
    }

    /**
     * Lists the diffs that are _removeNodes (Found on the left, but NOT FOUND
     * on the right)
     *
     * @return a list of _removeNode that have removes (Found on left, not on
     * right)
     */
    default List<_diffNode._leftOnly> listLeftOnlys() {
        List<_diffNode._leftOnly> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._leftOnly).forEach(n -> tds.add((_diffNode._leftOnly) n));
        return tds;
    }

    /**
     * List all change nodes that comply with the _changeNodeActionFn
     *
     * @param _changeNodeMatchFn match function for selecting _changeNode s
     * @return a list of _changeNodes that match the _changeNodeMatchFn
     */
    default List<_diffNode._change> listChanges(Predicate<_diffNode._change> _changeNodeMatchFn) {
        List<_diffNode._change> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._change && _changeNodeMatchFn.test((_diffNode._change) d))
                .forEach(e -> ens.add((_diffNode._change) e));
        return ens;
    }

    /**
     * Lists the diffs that are _changeNodes Objects matched (API-wise) on both
     * LEFT and RIGHT but changed (i.e. Modifiers, Annotations, etc.)
     *
     * @return a list of _changeNode that have changes (Found on left & right
     * but different)
     */
    default List<_diffNode._change> listChanges() {
        List<_diffNode._change> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._change).forEach(n -> tds.add((_diffNode._change) n));
        return tds;
    }

    /**
     * List all edit nodes that comply with the _editNodeActionFn
     *
     * @param _editNodeMatchFn match function for selecting diff nodes
     * @return a list of _editNodes
     */
    default List<_diffNode._edit> listEdits(Predicate<_diffNode._edit> _editNodeMatchFn) {
        List<_diffNode._edit> ens = new ArrayList<>();
        list(d -> d instanceof _diffNode._edit && _editNodeMatchFn.test((_diffNode._edit) d))
                .forEach(e -> ens.add((_diffNode._edit) e));
        return ens;
    }

    /**
     * Lists the diffs that are edit "text diffs" (usually diffs for some body
     * of code)
     *
     * these involve the edit distance algorithm (more low level involved for
     * determining edits needed to be applied to the left text to create the
     * right text
     *
     * @return a list of _editNode that have edits (usually in code bodies)
     */
    default List<_diffNode._edit> listEdits() {
        List<_diffNode._edit> tds = new ArrayList<>();
        list(d -> d instanceof _diffNode._edit).forEach(n -> tds.add((_diffNode._edit) n));
        return tds;
    }

    /**
     * Find the first diff that is at (the last component in the path is) the
     * component type
     *
     * @param id the
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstOn(String id) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.on(id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode firstOn(Class<_N> nodeClass) {
        return firstOn( Feature.of(nodeClass));
    }
    
    /**
     * Find the first diff that is at (the last component in the path is) the
     * component type
     *
     * @param componet the last component in the path
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstOn(Feature componet) {
        Optional<_diffNode> first
            = list().stream().filter(d -> d.on(componet)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode firstOn(Class<_N> nodeClass, String id) {
        return firstOn( Feature.of(nodeClass), id);
    }
    
    /**
     * Find the first diff that has this part (Component and id) anywahere on
     * the path
     *
     * @param componet the last component in the path
     * @param id
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstOn(Feature componet, String id) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.on(componet, id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode firstAt(Class<_N> nodeClass) {
        return firstAt( Feature.of(nodeClass));
    }

    /**
     *
     * @param id
     * @return
     */
    default _diffNode firstAt(String id) {
        Optional<_diffNode> odn = 
            list().stream().filter(d -> d.at(id)).findFirst();        
        if( odn.isPresent()){
            return odn.get();
        }
        return null;
    }
    
    /**
     * Find the first diff that is at (the last component in the path is) the
     * component type
     *
     * @param feature the last component in the path
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstAt(Feature feature) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.at(feature)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param id
     * @param <_N>
     * @return
     */
    default <_N extends _tree._node> _diffNode firstAt(Class<_N> nodeClass, String id) {
        return firstAt( Feature.of(nodeClass), id);
    }
    
    /**
     * Find the first diff that is at (the LAST part of the path) the component
     * type with this id
     *
     * @param feature the last component in the path
     * @param id the last id in the path to the component
     * @return the first diff node found at this path or null if none found
     */
    default _diffNode firstAt(Feature feature, String id) {
        Optional<_diffNode> first
                = list().stream().filter(d -> d.at(feature, id)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     * find the first node that matches the predicate or return null
     *
     * @param _nodeMatchFn
     * @return the first node that matches the function or null if none found
     */
    default _diffNode first(Predicate<_diffNode> _nodeMatchFn) {
        Optional<_diffNode> first
                = list().stream().filter(_nodeMatchFn).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param id the leaf (last) id in the path (i.e. "0", "m(String)")
     * @return
     */
    default List<_diffNode> listAt(String id) {
        return list(d -> d.at(id));
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param feature the leaf component to look for (i.e. METHOD, FIELD)
     * @return
     */
    default List<_diffNode> listAt(Feature feature) {
        return list(d -> d.at(feature));
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param feature the leaf component to look for (i.e. METHOD, FIELD)
     * @param id
     * @return
     */
    default List<_diffNode> listAt(Feature feature, String id) {
        return list(d -> d.at(feature, id));
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @return
     */
    default <_N extends _tree._node> List<_diffNode> listAt(Class<_N> nodeClass) {
        return listAt( Feature.of(nodeClass) );
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @param id
     * @return
     */
    default <_N extends _tree._node> List<_diffNode> listAt(Class<_N> nodeClass, String id) {
        return listAt( Feature.of(nodeClass), id);
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param id the leaf (last) id in the path (i.e. "0", "m(String)")
     * @return
     */
    default List<_diffNode> listOn(String id) {
        return list(d -> d.on(id));
    }

    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param feature the leaf component to look for (i.e. METHOD, FIELD)
     * @return
     */
    default List<_diffNode> listOn(Feature feature) {
        return list(d -> d.on(feature));
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @return
     */
    default <_N extends _tree._node> List<_diffNode> listOn(Class<_N> nodeClass) {
        return listOn( Feature.of(nodeClass) );
    }

    /**
     * List all _diffNode s that have this component & id part ANYWHERE on the
     * path
     *
     * @param feature the component to look for (i.e. METHOD, FIELD)
     * @param id the id to look for ("myMethod", "x")
     * @return a list of _diffNodes that occur on or
     */
    default List<_diffNode> listOn(Feature feature, String id) {
        return list(d -> d.on(feature, id));
    }
    
    /**
     * List all diff _nodes that have this component as the last component in
     * the path
     *
     * @param <_N>
     * @param nodeClass
     * @param id
     * @return
     */
    default <_N extends _tree._node> List<_diffNode> listOn(Class<_N> nodeClass, String id) {
        return listOn(Feature.of(nodeClass), id);
    }

    /**
     * Lists nodes matching a matchFn
     *
     * @param _nodeMatchFn matches diff nodes for retrieval
     * @return list of diff nodes that match the nodeMatchFn
     */
    default List<_diffNode> list(Predicate<_diffNode> _nodeMatchFn) {
        return list().stream().filter(_nodeMatchFn).collect(Collectors.toList());
    }

    /**
     * For all diffs, Accept all changes from the RIGHT object and 
     * apply/patching them in on the LEFT object
     *
     * @return
     */
    default _diff patchRightToLeft() {
        forEach(d -> d.patchRightToLeft());
        return this;
    }

    /**
     * For all diffs, Accept all changes from the LEFT object and 
     * apply/patching them in on the RIGHT object
     *
     * @return
     */
    default _diff patchLeftToRight() {
        forEach(d -> d.patchLeftToRight());
        return this;
    }

    /**
     * collection of diff _nodes that represent the deep differences between two
     * domain objects (two _class es, _methods, _fields)
     */
    class _diffList<_RN extends _tree._node>
            implements _diff, _diff._build {

        /**
         * the underlying diffs collected
         */
        public final List<_diffNode> diffNodeList;

        /** the left root node of the diff (may be null for local diffs) */
        public final _RN leftRoot;

        /** the right root node of the diff (may be null for local diffs) */
        public final _RN rightRoot;

        public _diffList(_RN leftRoot, _RN rightRoot) {
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            diffNodeList = new ArrayList<>();
        }

        public _diffList(_RN leftRoot, _RN rightRoot, List<_diffNode> diffs) {
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.diffNodeList = diffs;
        }

        /**
         * The starting/root left node where the diff occurs (could be null for local diffs)
         * @return
         */
        public _RN leftRoot(){
            return this.leftRoot;
        }

        /**
         * The starting/root right node where the diff occurs (could be null for local diffs)
         * @return
         */
        public _RN rightRoot(){
            return this.rightRoot;
        }

        @Override
        public List<_diffNode> list() {
            return diffNodeList;
        }

        @Override
        public String toString() {
            if (list().isEmpty()) {
                return "-- (0) differences --";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(diffNodeList.size()).append(") diffs").append(System.lineSeparator());
            this.diffNodeList.forEach(d -> sb.append(d));
            return sb.toString();
        }

        @Override
        public _build addDiff(_diffNode d) {
            diffNodeList.add(d);
            return this;
        }
    }

    /**
     *
     *
     * @author Eric
     * @param <T> the diff node type
     * @param <_RN> the root node type
     */
    interface _differ<T, _RN extends _tree._node> {

        /**
         *
         * @param left
         * @param right
         * @return
         */
        default _diff diff(T left, T right) {
            _nodePath _p = new _nodePath();

            //we dont know if
            _diffList md = null;

            if( left instanceof _tree._node && right instanceof _tree._node) {
                md = new _diffList((_tree._node)left, (_tree._node) right);
            } else{
                md = new _diffList(null, null);
            }
            diff(_p, md, null, null, left, right);
            return md;
        }

        default _diff diff(_nodePath path, _RN leftRoot, _RN rightRoot) {
            _diffList md = new _diffList(leftRoot, rightRoot);
            diff(path, md, leftRoot, rightRoot);
            return md;
        }

        //launch a diff using the left and right roots as the main roots to diff
        default _diff diff(_nodePath path, _build dt, _RN leftRoot, _RN rightRoot) {
            return diff(path, dt, leftRoot, rightRoot, (T) leftRoot, (T) rightRoot);
        }

        /**
         * diff the left and right nodes where leftParent and rightParent are the containing nodes
         *
         * @param <_PN> the parent _node type
         * @param path the current path to the entity
         * @param ds the mutable _difflist containing the current _diffNode s
         * @param _leftParent the parent of the left node
         * @param _rightParent the parent of the right node
         * @param left the left node/element/property
         * @param right the right node/element/property
         * @return the diffList with all diffs between the left and right nodes
         */
        <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, T left, T right);
    }

}
