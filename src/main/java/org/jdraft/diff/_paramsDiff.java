package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _paramsDiff
        implements _differ<_params, _tree._node> {

    public static final _paramsDiff INSTANCE = new _paramsDiff();
    
    public boolean equivalent(_params left, _params right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _params left, _params right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _changeParams(path.in(Feature.PARAMS), (_params._withParams) _leftParent, (_params._withParams) _rightParent));
        }
        return dt;
    }

    public static class _changeParams
            implements _diffNode<_params._withParams>, _diffNode._change<_params> {

        public _nodePath path;
        public _params._withParams leftParent;
        public _params._withParams rightParent;
        public _params left;
        public _params right;

        public _changeParams(_nodePath path, _params._withParams leftParent, _params._withParams rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = leftParent.getParams().copy();
            this.right = rightParent.getParams().copy();
        }

        @Override
        public _params._withParams leftParent() {
            return leftParent;
        }

        @Override
        public _params._withParams rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _params left() {
            return left;
        }

        @Override
        public _params right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setParams(left);
            rightParent.setParams(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setParams(right);
            rightParent.setParams(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }

    }
}
