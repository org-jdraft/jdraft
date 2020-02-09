package org.jdraft.diff;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

/**
 * 
 */
public class _interfaceDiff
        implements _differ<_interface, _java._compound> {

    public static final _interfaceDiff INSTANCE = new _interfaceDiff();
    
    @Override
    public <_PN extends _java._compound> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _interface left, _interface right) {
        _packageNameDiff.INSTANCE.diff(path, dt, left, right, left.getPackage(), right.getPackage());
        _importsDiff.INSTANCE.diff(path, dt, left, right, left, right);
        
        _annosDiff.INSTANCE.diff(path, dt, left, right, left.getAnnos(), right.getAnnos());
        _extendsDiff.INSTANCE.diff(path, dt, left, right, left.listExtends(), right.listExtends());
        _javadocDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _typeParametersDiff.INSTANCE.diff(path, dt, left, right, left.getTypeParameters(), right.getTypeParameters());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _methodsDiff.INSTANCE.diff(path, dt, left, right, left.listMethods(), right.listMethods());
        _fieldsDiff.INSTANCE.diff(path, dt, left, right, left.listFields(), right.listFields());
        _nestsDiff.INSTANCE.diff(path, dt, left, right, left.listNests(), right.listNests());

        _companionTypeDiff.INSTANCE.diff(path, dt, left, right, left.listCompanionTypes(), right.listCompanionTypes());
        return dt;
    }
}
