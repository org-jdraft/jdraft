package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _fieldDiff implements _differ<_field, _tree._node> {

    public static final _fieldDiff INSTANCE = new _fieldDiff();
    
    public boolean equivalent(_field left, _field right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _field left, _field right) {

        _nodePath p = path.in(Feature.FIELD, left != null ? left.getName() : right.getName());

        _namedDiff.INSTANCE.diff(p, dt, left, right, left.getName(), right.getName());
        _typeRefDiff.INSTANCE.diff(p, dt, left, right, left.getType(), right.getType());
        _modifiersDiff.INSTANCE.diff(p, dt, left, right, left.getEffectiveAstModifiersList(), right.getEffectiveAstModifiersList());
        _javadocCommentDiff.INSTANCE.diff(p, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annosDiff.INSTANCE.diff(p, dt, left, right, left.getAnnos(), right.getAnnos());

        if (!Objects.equals(left.getInitNode(), right.getInitNode())) {
            dt.addDiff(new _changeInit(p.in(Feature.INIT), left, right));
        }
        return dt;
    }

    /**
     * Both signifies a delta and provides a means to commit (via right()) or
     * rollback( via left())
     */
    public static class _changeInit
            implements _diffNode<_field>, _diffNode._change<Expression> {

        _nodePath path;
        _field leftParent;
        _field rightParent;
        Expression leftExpression;
        Expression rightExpression;

        public _changeInit(_nodePath _p, _field leftParent, _field rightParent) {
            this.path = _p;
            this.leftParent = leftParent;
            if (leftParent.hasInit()) {
                this.leftExpression = leftParent.getInitNode().clone();
            }
            this.rightParent = rightParent;
            if (rightParent.hasInit()) {
                this.rightExpression = rightParent.getInitNode().clone();
            }
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setInit(leftExpression);
            rightParent.setInit(leftExpression);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setInit(rightExpression);
            rightParent.setInit(rightExpression);
        }

        @Override
        public Expression left() {
            return leftExpression;
        }

        @Override
        public Expression right() {
            return rightExpression;
        }

        @Override
        public _field leftParent() {
            return leftParent;
        }

        @Override
        public _field rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }
        
        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
