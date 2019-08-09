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
public class _fieldDiff implements _differ<_field, _node> {

    public static final _fieldDiff INSTANCE = new _fieldDiff();
    
    public boolean equivalent(_field left, _field right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, _field left, _field right) {

        _path p = path.in(Component.FIELD, left != null ? left.getName() : right.getName());

        _namedDiff.INSTANCE.diff(p, dt, left, right, left.getName(), right.getName());
        _typeRefDiff.INSTANCE.diff(p, dt, left, right, left.getType(), right.getType());
        _modifiersDiff.INSTANCE.diff(p, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _javadocDiff.INSTANCE.diff(p, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _annosDiff.INSTANCE.diff(p, dt, left, right, left.getAnnos(), right.getAnnos());

        if (!Objects.equals(left.getInit(), right.getInit())) {
            dt.addDiff(new _changeInit(p.in(Component.INIT), left, right));
        }
        return (_diff) dt;
    }

    /**
     * Both signifies a delta and provides a means to commit (via right()) or
     * rollback( via left())
     */
    public static class _changeInit
            implements _diffNode<_field>, _diffNode._change<Expression> {

        _path path;
        _field left;
        _field right;
        Expression leftExpression;
        Expression rightExpression;

        public _changeInit(_path _p, _field left, _field right) {
            this.path = _p;
            this.left = left;
            if (left.hasInit()) {
                this.leftExpression = left.getInit().clone();
            }
            this.right = right;
            if (right.hasInit()) {
                this.rightExpression = right.getInit().clone();
            }
        }

        @Override
        public void patchLeftToRight() {
            left.init(leftExpression);
            right.init(leftExpression);
        }

        @Override
        public void patchRightToLeft() {
            left.init(rightExpression);
            right.init(rightExpression);
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
            return left;
        }

        @Override
        public _field rightParent() {
            return right;
        }

        @Override
        public _path path() {
            return path;
        }
        
        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
