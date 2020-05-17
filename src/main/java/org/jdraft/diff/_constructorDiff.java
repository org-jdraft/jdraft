package org.jdraft.diff;

import java.util.Objects;
import org.jdraft.*;

import org.jdraft.diff._diff.*;

public final class _constructorDiff implements _differ<_constructor, _java._multiPart> {

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
            sb.append(_pts.getAt(i).getTypeRef());
            if (_pts.getAt(i).isVarArg()) {
                sb.append("...");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _constructor left, _constructor right) {
        _javadocCommentDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annoExprsDiff.INSTANCE.diff(path, dt, left, right, left.getAnnoExprs(), right.getAnnoExprs());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _receiverParamDiff.INSTANCE.diff(path, dt, left, right, left.getReceiverParam(), right.getReceiverParam());
        _paramsDiff.INSTANCE.diff(path, dt, left, right, left.getParams(), right.getParams());
        _typeParamsDiff.INSTANCE.diff(path, dt, left, right, left.getTypeParams(), right.getTypeParams());
        _throwsDiff.INSTANCE.diff(path, dt, left, right, left.getThrows(), right.getThrows());
        _bodyDiff.INSTANCE.diff(path, dt, left, right, left.getBody(), right.getBody());
        return dt;
    }
}
