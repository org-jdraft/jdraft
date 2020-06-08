package org.jdraft.diff;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _typeDiff implements _differ<_type, _tree._node> {

    public static final _typeDiff INSTANCE = new _typeDiff();

    /*
    public _java.Component getComponent(_type t) {
        if (t instanceof _class) {
            return _java.Component.CLASS;
        }
        if (t instanceof _interface) {
            return _java.Component.INTERFACE;
        }
        if (t instanceof _enum) {
            return _java.Component.ENUM;
        }
        return _java.Component.ANNOTATION;
    }
     */

    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _type left, _type right) {
        if (left instanceof _class) {
            return _classDiff.INSTANCE.diff(path.in(Feature.CLASS, left.getName()), dt, _leftParent, _rightParent, (_class) left, (_class) right);
        }
        if (left instanceof _interface) {
            return _interfaceDiff.INSTANCE.diff(path.in(Feature.INTERFACE, left.getName()), dt, _leftParent, _rightParent, (_interface) left, (_interface) right);
        }
        if (left instanceof _annotation) {
            return _annotationDiff.INSTANCE.diff(path.in(Feature.ANNOTATION, left.getName()), dt, _leftParent, _rightParent, (_annotation) left, (_annotation) right);
        }
        return _enumDiff.INSTANCE.diff(path.in(Feature.ENUM, left.getName()), dt, _leftParent, _rightParent, (_enum) left, (_enum) right);
    }
}
