package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._staticBlock._hasStaticBlocks;

import org.jdraft.diff._diff.*;

public class _staticBlocksDiff
        implements _differ<List<_staticBlock>, _node> {

    public static final _staticBlocksDiff INSTANCE = new _staticBlocksDiff();
    
    public boolean equivalent(List<_staticBlock> left, List<_staticBlock> right) {
        Set<_staticBlock> ls = new HashSet<>();
        Set<_staticBlock> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        return Objects.equals(ls, rs);
    }

    public _diff diff( _hasStaticBlocks left, _hasStaticBlocks right){
        return diff( _path.of(), new _mutableDiffList(), (_node)left, (_node)right, left.listStaticBlocks(), right.listStaticBlocks());
    }
    
    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, List<_staticBlock> left, List<_staticBlock> right) {
        Set<_staticBlock> ls = new HashSet<>();
        Set<_staticBlock> rs = new HashSet<>();
        Set<_staticBlock> both = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        both.addAll(left);
        both.retainAll(right);

        ls.removeAll(both);
        rs.removeAll(both);

        ls.forEach(s -> dt.addDiff(new _leftOnly_staticBlock(
                path.in(_java.Component.STATIC_BLOCK),
                (_staticBlock._hasStaticBlocks) leftRoot, (_staticBlock._hasStaticBlocks) rightRoot, s)));
        rs.forEach(s -> dt.addDiff(new _rightOnly_staticBlock(
                path.in(_java.Component.STATIC_BLOCK),
                (_staticBlock._hasStaticBlocks) leftRoot, (_staticBlock._hasStaticBlocks) rightRoot, s)));
        return (_diff) dt;
    }

    public static class _rightOnly_staticBlock implements _diffNode<_staticBlock._hasStaticBlocks>, _diffNode._rightOnly<_staticBlock> {

        public _path path;
        public _staticBlock._hasStaticBlocks leftRoot;
        public _staticBlock._hasStaticBlocks rightRoot;
        public _staticBlock right;
        //TODO? leftMemberIndex, rightMemberIndex so I add the static Block in the right place???

        public _rightOnly_staticBlock(_path path, _staticBlock._hasStaticBlocks leftRoot, _staticBlock._hasStaticBlocks rightRoot, _staticBlock right) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.right = _staticBlock.of(right.astStaticInit.clone());
        }

        @Override
        public _staticBlock._hasStaticBlocks leftRoot() {
            return leftRoot;
        }

        @Override
        public _staticBlock._hasStaticBlocks rightRoot() {
            return rightRoot;
        }

        @Override
        public void patchLeftToRight() {
            this.leftRoot.removeStaticBlock(right);
            this.rightRoot.removeStaticBlock(right);
        }

        @Override
        public void patchRightToLeft() {
            this.leftRoot.removeStaticBlock(right);
            this.leftRoot.staticBlock(right);

            this.rightRoot.removeStaticBlock(right);
            this.rightRoot.staticBlock(right);
        }

        @Override
        public _path path() {
            return this.path;
        }

        @Override
        public _staticBlock right() {
            return this.right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }

    public static class _leftOnly_staticBlock implements _diffNode<_staticBlock._hasStaticBlocks>, _diffNode._leftOnly<_staticBlock> {

        public _path path;
        public _staticBlock._hasStaticBlocks leftRoot;
        public _staticBlock._hasStaticBlocks rightRoot;
        public _staticBlock left;

        public _leftOnly_staticBlock(_path path, _staticBlock._hasStaticBlocks leftRoot, _staticBlock._hasStaticBlocks rightRoot, _staticBlock left) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = _staticBlock.of(left.astStaticInit.clone());
        }

        @Override
        public _staticBlock._hasStaticBlocks leftRoot() {
            return leftRoot;
        }

        @Override
        public _staticBlock._hasStaticBlocks rightRoot() {
            return rightRoot;
        }

        @Override
        public void patchLeftToRight() {
            this.leftRoot.removeStaticBlock(left);
            this.leftRoot.staticBlock(left);

            this.rightRoot.removeStaticBlock(left);
            this.rightRoot.staticBlock(left);
        }

        @Override
        public void patchRightToLeft() {

            this.leftRoot.removeStaticBlock(left);
            this.rightRoot.removeStaticBlock(left);

        }

        @Override
        public _path path() {
            return this.path;
        }

        @Override
        public _staticBlock left() {
            return this.left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
