package org.jdraft.diff;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

public final class _classDiff implements _differ<_class, _java._multiPart> {

    public static _classDiff INSTANCE = new _classDiff();
    
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _class left, _class right) {
        if( left.hasPackage() || right.hasPackage() ){
            _packageNameDiff.INSTANCE.diff(path, ds, left, right, left.getPackage(), right.getPackage());
        }
        //_importsDiff.INSTANCE.diff(path, ds, left, right, left.listAstImports(), right.listAstImports());
        _importsDiff.INSTANCE.diff(path, ds, left, right, left, right);
        _javadocCommentDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
        _annosDiff.INSTANCE.diff(path, ds, left, right, left.getAnnos(), right.getAnnos());
        _modifiersDiff.INSTANCE.diff(path, ds, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
        _typeParametersDiff.INSTANCE.diff(path, ds, left, right, left.getTypeParameters(), right.getTypeParameters());
        _extendsDiff.INSTANCE.diff(path, ds, left, right, left.listExtends(), right.listExtends());
        _implementsDiff.INSTANCE.diff(path, ds, left, right, left.listImplements(), right.listImplements());
        _initBlocksDiff.INSTANCE.diff(path, ds, left, right, left.listInitBlocks(), right.listInitBlocks());
        _constructorsDiff.INSTANCE.diff(path, ds, left, right, left.listConstructors(), right.listConstructors());
        _methodsDiff.INSTANCE.diff(path, ds, left, right, left.listMethods(), right.listMethods());
        _fieldsDiff.INSTANCE.diff(path, ds, left, right, left.listFields(), right.listFields());
        _innerTypesDiff.INSTANCE.diff(path, ds, left, right, left.listInnerTypes(), right.listInnerTypes());

        _companionTypeDiff.INSTANCE.diff(path, ds, left, right, left.listCompanionTypes(), right.listCompanionTypes());

        return ds;
    }
}
