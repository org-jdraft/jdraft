package org.jdraft.diff;

import java.util.Objects;
import org.jdraft.*;

import org.jdraft.diff._diff.*;

public class _constructorDiff implements _differ<_constructor, _node> {

    public static final _constructorDiff INSTANCE = new _constructorDiff();
    
    public boolean equivalent(_constructor left, _constructor right) {
        return Objects.equals(left, right);
    }

    public static String constructorSignatureDescription(_constructor ct) {
        StringBuilder sb = new StringBuilder();

        _parameter._parameters _pts = ct.getParameters();
        sb.append(ct.getName());
        sb.append("(");
        for (int i = 0; i < _pts.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(_pts.get(i).getType());
            if (_pts.get(i).isVarArg()) {
                sb.append("...");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, _constructor left, _constructor right) {
        _javadocDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annosDiff.INSTANCE.diff(path, dt, left, right, left.getAnnos(), right.getAnnos());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _receiverParameterDiff.INSTANCE.diff(path, dt, left, right, left.getReceiverParameter(), right.getReceiverParameter());
        _parametersDiff.INSTANCE.diff(path, dt, left, right, left.getParameters(), right.getParameters());
        _typeParametersDiff.INSTANCE.diff(path, dt, left, right, left.getTypeParameters(), right.getTypeParameters());
        _throwsDiff.INSTANCE.diff(path, dt, left, right, left.getThrows(), right.getThrows());
        _bodyDiff.INSTANCE.diff(path, dt, left, right, left.getBody(), right.getBody());
        return (_diff) dt;
    }
}
