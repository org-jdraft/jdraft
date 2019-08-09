package org.jdraft.diff;

import java.util.*;
import org.jdraft.*;
import org.jdraft.diff._diff.*;

/**
 * Differ for nested {@link _type}s
 *
 * @author Eric
 */
public class _nestsDiff implements _differ<List<_type>, _node> {

    public static _nestsDiff INSTANCE = new _nestsDiff();
    
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
        return diff( _path.of(), 
                new _diffList(left, right),
                left, 
                right, 
                left.listNests(), 
                right.listNests());
    }

    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, List<_type> left, List<_type> right) {
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
                _typeDiff.INSTANCE.diff(path.in(_java.Component.NEST), dt, _leftParent, _rightParent, f, cc);
                rs.remove(cc);
            } else {
                //System.out.println( "<<<<<<<<<<<<< NOT FOUND MATCH "+ cc);
                if (f instanceof _class) {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Component.NEST).in(_java.Component.CLASS, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                } else if (f instanceof _interface) {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Component.NEST).in(_java.Component.INTERFACE, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                } else if (f instanceof _enum) {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Component.NEST).in(_java.Component.ENUM, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                } else {
                    dt.addDiff(new _leftOnly_nest(path.in(_java.Component.NEST).in(_java.Component.ANNOTATION, f.getName()),
                            (_type) _leftParent, (_type) _rightParent, f));
                }
            }
        });

        rs.forEach(f -> {
            if (f instanceof _class) {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Component.NEST).in(_java.Component.CLASS, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            } else if (f instanceof _interface) {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Component.NEST).in(_java.Component.INTERFACE, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            } else if (f instanceof _enum) {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Component.NEST).in(_java.Component.ENUM, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            } else {
                dt.addDiff(new _rightOnly_nest(path.in(_java.Component.NEST).in(_java.Component.ANNOTATION, f.getName()),
                        (_type) _leftParent, (_type) _rightParent, f));
            }
        });
        return dt;
    }

    public static class _rightOnly_nest
            implements _diffNode<_type>, _diffNode._rightOnly<_type> {

        public _path path;
        public _type leftParent;
        public _type rightParent;
        public _type right;

        public _rightOnly_nest(_path path, _type leftParent, _type rightParent, _type right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _java.type(right.toString());
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
            leftParent.removeNest(right);
            rightParent.removeNest(right);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeNest(right);
            leftParent.nest(right);
            rightParent.removeNest(right);
            rightParent.nest(right);
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

    public static class _leftOnly_nest
            implements _diffNode<_type>, _diffNode._leftOnly<_type> {

        public _path path;
        public _type leftParent;
        public _type rightParent;
        public _type left;

        public _leftOnly_nest(_path path, _type leftParent, _type rightParent, _type left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _java.type(left.toString());
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
            leftParent.removeNest(left);
            leftParent.nest(left);
            rightParent.removeNest(left);
            rightParent.nest(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeNest(left);
            rightParent.removeNest(left);
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
