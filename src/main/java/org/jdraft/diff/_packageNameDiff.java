package org.jdraft.diff;

import java.util.Objects;

import org.jdraft._java.Feature;
import org.jdraft.*;

import org.jdraft.diff._diff.*;

/**
 * _differ for the package name
 */
public final class _packageNameDiff
        implements _differ<String, _java._node> {

    public static final _packageNameDiff INSTANCE = new _packageNameDiff();
    
    public _diff diff( _type left, _type right){
        return diff( _nodePath.of(),
                new _diffList(left, right),
                left, 
                right, 
                left.getPackageName(),
                right.getPackageName());
    }

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, String leftPackageName, String rightPackageName) {
        if (!Objects.equals(leftPackageName, rightPackageName)) {            
            return dt.addDiff(
                new _changePackageName(path.in(Feature.PACKAGE), (_type) _leftParent, (_type) _rightParent));
        }
        return dt;
    }

    public static class _changePackageName implements _diffNode<_type>, _diffNode._change<String> {

        public _type leftParent;
        public _type rightParent;
        public String leftPackageName;
        public String rightPackageName;
        public _nodePath path;

        public _changePackageName(_nodePath _p, _type leftParent, _type rightParent) {
            this.path = _p;
            this.leftParent = leftParent;
            if (leftParent != null) {
                this.leftPackageName = leftParent.getPackageName();
            }
            this.rightParent = rightParent;
            if (rightParent != null) {
                this.rightPackageName = rightParent.getPackageName();
            }
        }

        @Override
        public void patchLeftToRight() {
            if (leftPackageName == null) {                
                this.leftParent.removePackage();
                this.rightParent.removePackage();
                return;
            }
            this.leftParent.setPackage(leftPackageName);
            this.rightParent.setPackage(leftPackageName);
        }

        @Override
        public void patchRightToLeft() {
            if (rightPackageName == null) {
                this.leftParent.removePackage();
                this.rightParent.removePackage();
                return;
            }
            this.leftParent.setPackage(rightPackageName);
            this.rightParent.setPackage(rightPackageName);
        }

        @Override
        public String left() {
            return leftPackageName;
        }

        @Override
        public String right() {
            return rightPackageName;
        }

        @Override
        public _type leftParent() {
            return leftParent;
        }

        @Override
        public _type rightParent() {
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
