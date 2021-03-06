package org.jdraft.diff;

import java.util.Objects;
import org.jdraft.*;

import org.jdraft.diff._diff.*;

public final class _constructorDiff implements _differ<_constructor, _tree._node> {

    public static final _constructorDiff INSTANCE = new _constructorDiff();
    
    public boolean equivalent(_constructor left, _constructor right) {
        return Objects.equals(left, right);
    }

    public static String constructorSignatureDescription(_constructor ct) {
        StringBuilder sb = new StringBuilder();

        _params _pts = ct.getParams();
        sb.append(ct.getName());
        sb.append("(");
        for (int i = 0; i < _pts.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(_pts.getAt(i).getType());
            if (_pts.getAt(i).isVarArg()) {
                sb.append("...");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _constructor left, _constructor right) {
        _javadocCommentDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annosDiff.INSTANCE.diff(path, dt, left, right, left.getAnnos(), right.getAnnos());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveAstModifiersList(), right.getEffectiveAstModifiersList());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _receiverParamDiff.INSTANCE.diff(path, dt, left, right, left.getReceiverParam(), right.getReceiverParam());
        _paramsDiff.INSTANCE.diff(path, dt, left, right, left.getParams(), right.getParams());
        _typeParamsDiff.INSTANCE.diff(path, dt, left, right, left.getTypeParams(), right.getTypeParams());
        _throwsDiff.INSTANCE.diff(path, dt, left, right, left.getThrows(), right.getThrows());
        _bodyDiff.INSTANCE.diff(path, dt, left, right, left.getBody(), right.getBody());
        return dt;
    }
}
