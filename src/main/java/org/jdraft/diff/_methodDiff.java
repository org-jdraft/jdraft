package org.jdraft.diff;

import java.util.Objects;
import org.jdraft.*;
import static org.jdraft._method.describeMethodSignature;

import org.jdraft.diff._diff.*;

public final class _methodDiff implements _differ<_method, _java._node> {

    public static final _methodDiff INSTANCE = new _methodDiff();

    public boolean equivalent(_method left, _method right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _method left, _method right) {
        _nodePath p = path.in(Feature.METHOD, describeMethodSignature(left));

        _javadocCommentDiff.INSTANCE.diff(p, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annoExprsDiff.INSTANCE.diff(p, dt, left, right, left.getAnnoExprs(), right.getAnnoExprs());
        _typeRefDiff.INSTANCE.diff(p, dt, left, right, left.getType(), right.getType());
        _modifiersDiff.INSTANCE.diff(p, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _namedDiff.INSTANCE.diff(p, dt, left, right, left.getName(), right.getName());
        _typeParamsDiff.INSTANCE.diff(p, dt, left, right, left.getTypeParams(), right.getTypeParams());
        _receiverParamDiff.INSTANCE.diff(p, dt, left, right, left.getReceiverParam(), right.getReceiverParam());
        _paramsDiff.INSTANCE.diff(p, dt, left, right, left.getParams(), right.getParams());
        _throwsDiff.INSTANCE.diff(p, dt, left, right, left.getThrows(), right.getThrows());
        _bodyDiff.INSTANCE.diff(p, dt, left, right, left.getBody(), right.getBody());
        return dt;
    }
}
