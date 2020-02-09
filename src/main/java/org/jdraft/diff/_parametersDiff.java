package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _parametersDiff
        implements _differ<_parameters, _java._compound> {

    public static final _parametersDiff INSTANCE = new _parametersDiff();
    
    public boolean equivalent(_parameters left, _parameters right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _java._compound> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _parameters left, _parameters right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _changeParameters(path.in(_java.Component.PARAMETERS), (_parameter._hasParameters) _leftParent, (_parameter._hasParameters) _rightParent));
        }
        return dt;
    }

    public static class _changeParameters
            implements _diffNode<_parameter._hasParameters>, _diffNode._change<_parameters> {

        public _nodePath path;
        public _parameter._hasParameters leftParent;
        public _parameter._hasParameters rightParent;
        public _parameters left;
        public _parameters right;

        public _changeParameters(_nodePath path, _parameter._hasParameters leftParent, _parameter._hasParameters rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = leftParent.getParameters().copy();
            this.right = rightParent.getParameters().copy();
        }

        @Override
        public _parameter._hasParameters leftParent() {
            return leftParent;
        }

        @Override
        public _parameter._hasParameters rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _parameters left() {
            return left;
        }

        @Override
        public _parameters right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setParameters(left);
            rightParent.setParameters(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setParameters(right);
            rightParent.setParameters(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }

    }
}
