package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._initBlock._withInitBlocks;

import org.jdraft.diff._diff.*;

public final class _initBlocksDiff
        implements _differ<List<_initBlock>, _java._multiPart> {

    public static final _initBlocksDiff INSTANCE = new _initBlocksDiff();
    
    public boolean equivalent(List<_initBlock> left, List<_initBlock> right) {
        Set<_initBlock> ls = new HashSet<>();
        Set<_initBlock> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        return Objects.equals(ls, rs);
    }

    public _diff diff(_withInitBlocks left, _withInitBlocks right){
        return diff( _nodePath.of(), new _diffList( (_java._multiPart)left, (_java._multiPart)right), (_java._multiPart)left, (_java._multiPart)right, left.listInitBlocks(), right.listInitBlocks());
    }
    
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_initBlock> left, List<_initBlock> right) {
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
                path.in(_java.Component.INIT_BLOCK),
                (_withInitBlocks) _leftParent, (_withInitBlocks) _rightParent, s)));
        rs.forEach(s -> dt.addDiff(new _rightOnly_initBlock(
                path.in(_java.Component.INIT_BLOCK),
                (_withInitBlocks) _leftParent, (_withInitBlocks) _rightParent, s)));
        return dt;
    }

    public static class _rightOnly_initBlock implements _diffNode<_withInitBlocks>, _diffNode._rightOnly<_initBlock> {

        public _nodePath path;
        public _withInitBlocks leftParent;
        public _withInitBlocks rightParent;
        public _initBlock right;
        //TODO? leftMemberIndex, rightMemberIndex so I add the static Block in the right place???

        public _rightOnly_initBlock(_nodePath path, _withInitBlocks leftParent, _withInitBlocks rightParent, _initBlock right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _initBlock.of(right.astInit.clone());
        }

        @Override
        public _withInitBlocks leftParent() {
            return leftParent;
        }

        @Override
        public _withInitBlocks rightParent() {
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
            this.leftParent.addInitBlock(right);

            this.rightParent.removeInitBlock(right);
            this.rightParent.addInitBlock(right);
        }

        @Override
        public _nodePath path() {
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

    public static class _leftOnly_initBlock implements _diffNode<_withInitBlocks>, _diffNode._leftOnly<_initBlock> {

        public _nodePath path;
        public _withInitBlocks leftParent;
        public _withInitBlocks rightParent;
        public _initBlock left;

        public _leftOnly_initBlock(_nodePath path, _withInitBlocks leftParent, _withInitBlocks rightParent, _initBlock left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _initBlock.of(left.astInit.clone());
        }

        @Override
        public _withInitBlocks leftParent() {
            return leftParent;
        }

        @Override
        public _withInitBlocks rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            this.leftParent.removeInitBlock(left);
            this.leftParent.addInitBlock(left);

            this.rightParent.removeInitBlock(left);
            this.rightParent.addInitBlock(left);
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeInitBlock(left);
            this.rightParent.removeInitBlock(left);
        }

        @Override
        public _nodePath path() {
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
