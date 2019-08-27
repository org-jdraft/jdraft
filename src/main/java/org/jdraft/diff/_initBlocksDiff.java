package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._initBlock._hasInitBlocks;

import org.jdraft.diff._diff.*;

public class _initBlocksDiff
        implements _differ<List<_initBlock>, _node> {

    public static final _initBlocksDiff INSTANCE = new _initBlocksDiff();
    
    public boolean equivalent(List<_initBlock> left, List<_initBlock> right) {
        Set<_initBlock> ls = new HashSet<>();
        Set<_initBlock> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        return Objects.equals(ls, rs);
    }

    public _diff diff(_hasInitBlocks left, _hasInitBlocks right){
        return diff( _path.of(), new _diffList( (_node)left, (_node)right), (_node)left, (_node)right, left.listInitBlocks(), right.listInitBlocks());
    }
    
    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, List<_initBlock> left, List<_initBlock> right) {
        Set<_initBlock> ls = new HashSet<>();
        Set<_initBlock> rs = new HashSet<>();
        Set<_initBlock> both = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        both.addAll(left);
        both.retainAll(right);

        ls.removeAll(both);
        rs.removeAll(both);

        ls.forEach(s -> dt.addDiff(new _leftOnly_initBlock(
                path.in(_java.Component.STATIC_BLOCK),
                (_hasInitBlocks) _leftParent, (_hasInitBlocks) _rightParent, s)));
        rs.forEach(s -> dt.addDiff(new _rightOnly_initBlock(
                path.in(_java.Component.STATIC_BLOCK),
                (_hasInitBlocks) _leftParent, (_hasInitBlocks) _rightParent, s)));
        return dt;
    }

    public static class _rightOnly_initBlock implements _diffNode<_hasInitBlocks>, _diffNode._rightOnly<_initBlock> {

        public _path path;
        public _hasInitBlocks leftParent;
        public _hasInitBlocks rightParent;
        public _initBlock right;
        //TODO? leftMemberIndex, rightMemberIndex so I add the static Block in the right place???

        public _rightOnly_initBlock(_path path, _hasInitBlocks leftParent, _hasInitBlocks rightParent, _initBlock right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _initBlock.of(right.astInit.clone());
        }

        @Override
        public _hasInitBlocks leftParent() {
            return leftParent;
        }

        @Override
        public _hasInitBlocks rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            this.leftParent.removeInitBlock(right);
            this.rightParent.removeInitBlock(right);
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeInitBlock(right);
            this.leftParent.initBlock(right);

            this.rightParent.removeInitBlock(right);
            this.rightParent.initBlock(right);
        }

        @Override
        public _path path() {
            return this.path;
        }

        @Override
        public _initBlock right() {
            return this.right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }

    public static class _leftOnly_initBlock implements _diffNode<_hasInitBlocks>, _diffNode._leftOnly<_initBlock> {

        public _path path;
        public _hasInitBlocks leftParent;
        public _hasInitBlocks rightParent;
        public _initBlock left;

        public _leftOnly_initBlock(_path path, _hasInitBlocks leftParent, _hasInitBlocks rightParent, _initBlock left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _initBlock.of(left.astInit.clone());
        }

        @Override
        public _hasInitBlocks leftParent() {
            return leftParent;
        }

        @Override
        public _hasInitBlocks rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            this.leftParent.removeInitBlock(left);
            this.leftParent.initBlock(left);

            this.rightParent.removeInitBlock(left);
            this.rightParent.initBlock(left);
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeInitBlock(left);
            this.rightParent.removeInitBlock(left);
        }

        @Override
        public _path path() {
            return this.path;
        }

        @Override
        public _initBlock left() {
            return this.left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
