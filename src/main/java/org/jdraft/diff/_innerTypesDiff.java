package org.jdraft.diff;

import java.util.*;
import org.jdraft.*;
import org.jdraft.diff._diff.*;

/**
 * Differ for inner {@link _type}s
 *
 * @author Eric
 */
public final class _innerTypesDiff implements _differ<List<_type>, _java._node> {

    public static _innerTypesDiff INSTANCE = new _innerTypesDiff();
    
    public boolean equivalent(List<_type> left, List<_type> right) {
        Set<_type> ls = new HashSet<>();
        Set<_type> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        return Objects.equals(ls, right);
    }

    private static _type sameNameAndType(_type _t, Set<_type> target) {
        Optional<_type> ot = target.stream().filter(t -> t.getName().equals(_t.getName())
                && _t.getClass().isAssignableFrom(t.getClass())).findFirst();
        if (ot.isPresent()) {
            return ot.get();
        }
        return null;
    }

    public _diff diff( _type left, _type right){
        return diff( _nodePath.of(),
                new _diffList(left, right),
                left, 
                right, 
                left.listInnerTypes(),
                right.listInnerTypes());
    }

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_type> left, List<_type> right) {
        Set<_type> ls = new HashSet<>();
        Set<_type> rs = new HashSet<>();
        Set<_type> both = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        both.addAll(ls);
        both.retainAll(rs);

        ls.removeAll(both);
        rs.removeAll(both);
        
        ls.forEach(f -> {
            //System.out.println( ">>>>>>>>>>>>>>>>>> CHECKING FOR NEST "+ f.getName());
            _type cc = sameNameAndType(f, rs);
            
            if (cc != null) {
                //System.out.println( ">>>>>>>>>>>>>>>>>> FOUND MATCH "+ f);
                _typeDiff.INSTANCE.diff(path.in(_java.Feature.INNER_TYPE), dt, _leftParent, _rightParent, f, cc);
                rs.remove(cc);
            } else {
                //System.out.println( "<<<<<<<<<<<<< NOT FOUND MATCH "+ cc);
                if (f instanceof _class) {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.CLASS, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                } else if (f instanceof _interface) {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.INTERFACE, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                } else if (f instanceof _enum) {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.ENUM, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                } else {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.ANNOTATION, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                }
            }
        });

        rs.forEach(f -> {
            if (f instanceof _class) {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.CLASS, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            } else if (f instanceof _interface) {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.INTERFACE, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            } else if (f instanceof _enum) {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.ENUM, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            } else {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Feature.INNER_TYPE).in(_java.Feature.ANNOTATION, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            }
        });
        return dt;
    }

    public static class _rightOnly_nest
            implements _diffNode<_type>, _diffNode._rightOnly<_type> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public _type right;

        public _rightOnly_nest(_nodePath path, _type leftParent, _type rightParent, _type right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _type.of(right.toString());
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
        public void patchLeftToRight() {
            leftParent.removeInnerType(right);
            rightParent.removeInnerType(right);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeInnerType(right);
            leftParent.addInner(right);
            rightParent.removeInnerType(right);
            rightParent.addInner(right);
        }

        @Override
        public _nodePath path() {
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

    public static class _leftOnly_nest
            implements _diffNode<_type>, _diffNode._leftOnly<_type> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public _type left;

        public _leftOnly_nest(_nodePath path, _type leftParent, _type rightParent, _type left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _type.of(left.toString());
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
        public void patchLeftToRight() {
            leftParent.removeInnerType(left);
            leftParent.addInner(left);
            rightParent.removeInnerType(left);
            rightParent.addInner(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeInnerType(left);
            rightParent.removeInnerType(left);
        }

        @Override
        public _nodePath path() {
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
