package org.jdraft.diff;

import java.util.Objects;
import org.jdraft.*;
import static org.jdraft._method.describeMethodSignature;

import org.jdraft.diff._diff.*;

public class _methodDiff implements _differ<_method, _node> {

    public static final _methodDiff INSTANCE = new _methodDiff();

    public boolean equivalent(_method left, _method right) {
        return Objects.equals(left, right);
    }

    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _method left, _method right) {
        _path p = path.in(_java.Component.METHOD, describeMethodSignature(left));

        _javadocDiff.INSTANCE.diff(p, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annosDiff.INSTANCE.diff(p, dt, left, right, left.getAnnos(), right.getAnnos());
        _typeRefDiff.INSTANCE.diff(p, dt, left, right, left.getType(), right.getType());
        _modifiersDiff.INSTANCE.diff(p, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _namedDiff.INSTANCE.diff(p, dt, left, right, left.getName(), right.getName());
        _typeParametersDiff.INSTANCE.diff(p, dt, left, right, left.getTypeParameters(), right.getTypeParameters());
        _receiverParameterDiff.INSTANCE.diff(p, dt, left, right, left.getReceiverParameter(), right.getReceiverParameter());
        _parametersDiff.INSTANCE.diff(p, dt, left, right, left.getParameters(), right.getParameters());
        _throwsDiff.INSTANCE.diff(p, dt, left, right, left.getThrows(), right.getThrows());
        _bodyDiff.INSTANCE.diff(p, dt, left, right, left.getBody(), right.getBody());
        return (_diff) dt;
    }
}