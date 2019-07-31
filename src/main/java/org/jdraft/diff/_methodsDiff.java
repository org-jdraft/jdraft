package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import static org.jdraft._method.describeMethodSignature;

import org.jdraft.diff._diff.*;

public class _methodsDiff
        implements _differ<List<_method>, _node> {

    public static final _methodsDiff INSTANCE = new _methodsDiff();
    
    static class _matchedMethods {

        _method left;
        _method right;

        public _matchedMethods(_method left, _method right) {
            this.left = left;
            this.right = right;
        }
    }

    public boolean equivalent(List<_method> left, List<_method> right) {
        Set<_method> ls = new HashSet<>();
        ls.addAll(left);
        Set<_method> rs = new HashSet<>();
        rs.addAll(right);
        return Objects.equals(ls, rs);
    }

    public static _method findSameNameAndParameters(_method tm, Set<_method> targets) {
        Optional<_method> om = targets.stream().filter(m -> m.getName().equals(tm.getName())
                //&& m.getType().equals(tm.getType())
                && m.getParameters().hasParametersOfType(tm.getParameters().types())).findFirst();
        if (om.isPresent()) {
            return om.get();
        }
        return null;
    }

     public _diff diff( _method._hasMethods left, _method._hasMethods right){
        return diff( 
                _path.of(), 
                new _diff._mutableDiffList(), 
                (_node)left, 
                (_node)right, 
                left.listMethods(), 
                right.listMethods());
    }
     
    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, List<_method> left, List<_method> right) {
        Set<_method> ls = new HashSet<>();
        ls.addAll(left);
        Set<_method> rs = new HashSet<>();
        rs.addAll(right);

        Set<_method> both = new HashSet<>();
        both.addAll(left);
        both.retainAll(right);

        ls.removeAll(right);
        rs.removeAll(left);

        ls.forEach(m -> {
            _method fm = findSameNameAndParameters(m, rs);
            if (fm != null) {
                rs.remove(fm);
                _methodDiff.INSTANCE.diff(
                        path,
                        dt,
                        leftRoot,
                        rightRoot,
                        m,
                        fm);
            } else {
                dt.addDiff(new _leftOnly_method(
                        path.in(_java.Component.METHOD, describeMethodSignature(m)),
                        (_method._hasMethods) leftRoot, (_method._hasMethods) rightRoot, m));
            }
        });
        rs.forEach(m -> {
            dt.addDiff(new _rightOnly_method(
                    path.in(_java.Component.METHOD, describeMethodSignature(m)),
                    (_method._hasMethods) leftRoot, (_method._hasMethods) rightRoot, m));
        });
        return (_diff) dt;
    }

    public static class _leftOnly_method implements _diffNode<_method._hasMethods>, _diffNode._leftOnly<_method> {

        public _path path;
        public _method._hasMethods rightRoot;
        public _method._hasMethods leftRoot;
        public _method left;

        public _leftOnly_method(_path path, _method._hasMethods leftRoot, _method._hasMethods rightRoot, _method left) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = _method.of(left.toString());
        }

        @Override
        public _method._hasMethods leftRoot() {
            return leftRoot;
        }

        @Override
        public _method._hasMethods rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _method left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            this.leftRoot.removeMethod(left);
            this.rightRoot.removeMethod(left);
        }

        @Override
        public void patchLeftToRight() {
            this.leftRoot.removeMethod(left); //dont double add
            this.leftRoot.method(left.copy());
            this.rightRoot.removeMethod(left);
            this.rightRoot.method(left.copy());
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_method implements _diffNode<_method._hasMethods>, _diffNode._rightOnly<_method> {

        public _path path;
        public _method._hasMethods rightRoot;
        public _method._hasMethods leftRoot;
        public _method right;

        public _rightOnly_method(_path path, _method._hasMethods leftRoot, _method._hasMethods rightRoot, _method right) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.right = _method.of(right.toString());
        }

        @Override
        public _method._hasMethods leftRoot() {
            return leftRoot;
        }

        @Override
        public _method._hasMethods rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _method right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            this.leftRoot.removeMethod(right);
            this.leftRoot.method(right.copy());

            this.rightRoot.removeMethod(right);
            this.rightRoot.method(right.copy());
        }

        @Override
        public void patchLeftToRight() {
            this.leftRoot.removeMethod(right);
            this.rightRoot.removeMethod(right);
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}

    
