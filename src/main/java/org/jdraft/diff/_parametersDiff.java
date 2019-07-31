package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _parametersDiff
        implements _differ<_parameter._parameters, _node> {

    public static final _parametersDiff INSTANCE = new _parametersDiff();
    
    public boolean equivalent(_parameter._parameters left, _parameter._parameters right) {
        return Objects.equals(left, right);
    }

    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _parameter._parameters left, _parameter._parameters right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _changeParameters(path.in(_java.Component.PARAMETERS), (_parameter._hasParameters) leftRoot, (_parameter._hasParameters) rightRoot));
        }
        return (_diff) dt;
    }

    public static class _changeParameters
            implements _diffNode<_parameter._hasParameters>, _diffNode._change<_parameter._parameters> {

        public _path path;
        public _parameter._hasParameters leftRoot;
        public _parameter._hasParameters rightRoot;
        public _parameter._parameters left;
        public _parameter._parameters right;

        public _changeParameters(_path path, _parameter._hasParameters leftRoot, _parameter._hasParameters rightRoot) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = leftRoot.getParameters().copy();
            this.right = rightRoot.getParameters().copy();
        }

        @Override
        public _parameter._hasParameters leftRoot() {
            return leftRoot;
        }

        @Override
        public _parameter._hasParameters rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _parameter._parameters left() {
            return left;
        }

        @Override
        public _parameter._parameters right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftRoot.setParameters(left);
            rightRoot.setParameters(left);
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.setParameters(right);
            rightRoot.setParameters(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }

    }
}
