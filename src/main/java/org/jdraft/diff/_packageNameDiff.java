package org.jdraft.diff;

import java.util.Objects;

import org.jdraft._java.Component;
import org.jdraft.*;

import org.jdraft.diff._diff.*;

/**
 * _differ for the package name
 */
public class _packageNameDiff
        implements _differ<String, _node> {

    public static final _packageNameDiff INSTANCE = new _packageNameDiff();
    
    public _diff diff( _type left, _type right){
        return diff( _path.of(), 
                new _diffList(left, right),
                left, 
                right, 
                left.getPackage(), 
                right.getPackage());
    }

    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, String leftPackageName, String rightPackageName) {
        if (!Objects.equals(leftPackageName, rightPackageName)) {            
            return dt.addDiff(
                new _changePackageName(path.in(Component.PACKAGE_NAME), (_type) _leftParent, (_type) _rightParent));
        }
        return dt;
    }

    public static class _changePackageName implements _diffNode<_type>, _diffNode._change<String> {

        public _type leftRoot;
        public _type rightRoot;
        public String leftPackageName;
        public String rightPackageName;
        public _path path;

        public _changePackageName(_path _p, _type leftRoot, _type rightRoot) {
            this.path = _p;
            this.leftRoot = leftRoot;
            if (leftRoot != null) {
                this.leftPackageName = leftRoot.getPackage();
            }
            this.rightRoot = rightRoot;
            if (rightRoot != null) {
                this.rightPackageName = rightRoot.getPackage();
            }
        }

        @Override
        public void patchLeftToRight() {
            if (leftPackageName == null) {                
                this.leftRoot.removePackage();
                this.rightRoot.removePackage();
                return;
            }
            this.leftRoot.setPackage(leftPackageName);
            this.rightRoot.setPackage(leftPackageName);            
        }

        @Override
        public void patchRightToLeft() {
            if (rightPackageName == null) {
                this.leftRoot.removePackage();
                this.rightRoot.removePackage();
                return;
            }
            this.leftRoot.setPackage(rightPackageName);
            this.rightRoot.setPackage(rightPackageName);
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
            return leftRoot;
        }

        @Override
        public _type rightParent() {
            return rightRoot;
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
