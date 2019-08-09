package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._java.Component;
import org.jdraft.diff._diff.*;

/**
 * Diff a any implementation of {@link _type._hasTypes} containing {@link _type}s
 * @author Eric
 */
public class _typesDiff implements _differ<_types, _node> {

    public static _typesDiff INSTANCE = new _typesDiff();
    
    public boolean equivalent(_types leftParent, _types rightParent) {
        Set<_type> ls = new HashSet<>();
        Set<_type> rs = new HashSet<>();
        ls.addAll(leftParent.list());
        rs.addAll(rightParent.list());
        return Objects.equals(ls, rs);
    }

    private static _type sameNameAndType(_type _t, Set<_type> target) {
        Optional<_type> ot = target.stream().filter(t -> t.getFullName().equals(_t.getFullName())
                && _t.getClass().isAssignableFrom(t.getClass())).findFirst();
        if (ot.isPresent()) {
            return ot.get();
        }
        return null;
    }

    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, _types left, _types right) {
        Set<_type> ls = new HashSet<>();
        Set<_type> rs = new HashSet<>();
        Set<_type> both = new HashSet<>();
        ls.addAll(left.list());
        rs.addAll(right.list());
        both.addAll(ls);
        both.retainAll(rs);

        ls.removeAll(both);
        rs.removeAll(both);
        ls.forEach(f -> {
            _type cc = sameNameAndType(f, rs);
            if (cc != null) {
                _typeDiff.INSTANCE.diff(path.in(Component.getComponent(f), f.getFullName()), dt, _leftParent, _rightParent, f, cc);
                rs.remove(cc);
            } else {
                dt.addDiff(new _leftOnly_type(path.in(Component.getComponent(f), f.getFullName()),left, right, f));               
            }
        });

        rs.forEach(f -> {
            dt.addDiff(new _rightOnly_type(path.in(Component.getComponent(f), f.getFullName()),left, right, f));
        });      
        return dt;
    }

    public static class _rightOnly_type
            implements _diffNode<_types>, _diffNode._rightOnly<_type> {

        public _path path;
        public _types leftParent;
        public _types rightParent;
        public _type right;

        public _rightOnly_type(_path path, _types leftParent, _types rightParent, _type right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _java.type(right.toString());
        }

        @Override
        public _types leftParent() {
            return leftParent;
        }

        @Override
        public _types rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.remove(right);
            rightParent.remove(right);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.remove(right);
            leftParent.add(right);
            rightParent.remove(right);
            rightParent.add(right);
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _type right() {
            return this.right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }

    public static class _leftOnly_type
            implements _diffNode<_types>, _diffNode._leftOnly<_type> {

        public _path path;
        public _types leftParent;
        public _types rightParent;
        public _type left;

        public _leftOnly_type(_path path, _types leftParent, _types rightParent, _type left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _java.type(left.toString());
        }

        @Override
        public _types leftParent() {
            return leftParent;
        }

        @Override
        public _types rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.remove(left);
            leftParent.add(left);
            rightParent.remove(left);
            rightParent.add(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.remove(left);
            rightParent.remove(left);
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _type left() {
            return this.left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
