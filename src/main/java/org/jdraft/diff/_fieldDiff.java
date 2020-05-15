package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft._java.Component;
import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _fieldDiff implements _differ<_field, _java._multiPart> {

    public static final _fieldDiff INSTANCE = new _fieldDiff();
    
    public boolean equivalent(_field left, _field right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _field left, _field right) {

        _nodePath p = path.in(Component.FIELD, left != null ? left.getName() : right.getName());

        _namedDiff.INSTANCE.diff(p, dt, left, right, left.getName(), right.getName());
        _typeRefDiff.INSTANCE.diff(p, dt, left, right, left.getTypeRef(), right.getTypeRef());
        _modifiersDiff.INSTANCE.diff(p, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _javadocCommentDiff.INSTANCE.diff(p, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annoExprsDiff.INSTANCE.diff(p, dt, left, right, left.getAnnoRefs(), right.getAnnoRefs());

        if (!Objects.equals(left.getInit(), right.getInit())) {
            dt.addDiff(new _changeInit(p.in(Component.INIT), left, right));
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
                this.leftExpression = leftParent.getInit().clone();
            }
            this.rightParent = rightParent;
            if (rightParent.hasInit()) {
                this.rightExpression = rightParent.getInit().clone();
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
