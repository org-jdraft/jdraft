package org.jdraft.diff;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

/**
 * 
 */
public final class _interfaceDiff
        implements _differ<_interface, _tree._node> {

    public static final _interfaceDiff INSTANCE = new _interfaceDiff();
    
    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _interface left, _interface right) {
        _packageNameDiff.INSTANCE.diff(path, dt, left, right, left.getPackageName(), right.getPackageName());
        _importsDiff.INSTANCE.diff(path, dt, left, right, left, right);
        
        _annosDiff.INSTANCE.diff(path, dt, left, right, left.getAnnos(), right.getAnnos());
        _extendsDiff.INSTANCE.diff(path, dt, left, right, left.listAstExtends(), right.listAstExtends());
        _javadocCommentDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _typeParamsDiff.INSTANCE.diff(path, dt, left, right, left.getTypeParams(), right.getTypeParams());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveAstModifiersList(), right.getEffectiveAstModifiersList());
        _methodsDiff.INSTANCE.diff(path, dt, left, right, left.listMethods(), right.listMethods());
        _fieldsDiff.INSTANCE.diff(path, dt, left, right, left.listFields(), right.listFields());
        _innerTypesDiff.INSTANCE.diff(path, dt, left, right, left.listInnerTypes(), right.listInnerTypes());

        _companionTypeDiff.INSTANCE.diff(path, dt, left, right, left.listCompanionTypes(), right.listCompanionTypes());
        return dt;
    }
}
