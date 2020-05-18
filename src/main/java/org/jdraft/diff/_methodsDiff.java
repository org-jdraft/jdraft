package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import static org.jdraft._method.describeMethodSignature;

import org.jdraft.diff._diff.*;

public final class _methodsDiff
        implements _differ<List<_method>, _java._multiPart> {

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
                && m.getParams().hasParamsOfType(tm.getParams().types())).findFirst();
        if (om.isPresent()) {
            return om.get();
        }
        return null;
    }

     public _diff diff(_method._withMethods leftParent, _method._withMethods rightParent){
        return diff( 
                _nodePath.of(),
                new _diffList( (_java._multiPart)leftParent, (_java._multiPart)rightParent),
                (_java._multiPart)leftParent,
                (_java._multiPart)rightParent,
                leftParent.listMethods(),
                rightParent.listMethods());
    }
     
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_method> left, List<_method> right) {
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
                        _leftParent,
                        _rightParent,
                        m,
                        fm);
            } else {
                dt.addDiff(new _leftOnly_method(
                        path.in(_java.Feature.METHOD, describeMethodSignature(m)),
                        (_method._withMethods) _leftParent, (_method._withMethods) _rightParent, m));
            }
        });
        rs.forEach(m -> {
            dt.addDiff(new _rightOnly_method(
                    path.in(_java.Feature.METHOD, describeMethodSignature(m)),
                    (_method._withMethods) _leftParent, (_method._withMethods) _rightParent, m));
        });
        return dt;
    }

    public static class _leftOnly_method implements _diffNode<_method._withMethods>, _diffNode._leftOnly<_method> {

        public _nodePath path;
        public _method._withMethods rightParent;
        public _method._withMethods leftParent;
        public _method left;

        public _leftOnly_method(_nodePath path, _method._withMethods leftParent, _method._withMethods rightParent, _method left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _method.of(left.toString());
        }

        @Override
        public _method._withMethods leftParent() {
            return leftParent;
        }

        @Override
        public _method._withMethods rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _method left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeMethod(left);
            this.rightParent.removeMethod(left);
        }

        @Override
        public void patchLeftToRight() {
            this.leftParent.removeMethod(left); //dont double add
            this.leftParent.addMethod(left.copy());
            this.rightParent.removeMethod(left);
            this.rightParent.addMethod(left.copy());
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_method implements _diffNode<_method._withMethods>, _diffNode._rightOnly<_method> {

        public _nodePath path;
        public _method._withMethods rightParent;
        public _method._withMethods leftParent;
        public _method right;

        public _rightOnly_method(_nodePath path, _method._withMethods leftParent, _method._withMethods rightParent, _method right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _method.of(right.toString());
        }

        @Override
        public _method._withMethods leftParent() {
            return leftParent;
        }

        @Override
        public _method._withMethods rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _method right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            this.leftParent.removeMethod(right);
            this.leftParent.addMethod(right.copy());

            this.rightParent.removeMethod(right);
            this.rightParent.addMethod(right.copy());
        }

        @Override
        public void patchLeftToRight() {
            this.leftParent.removeMethod(right);
            this.rightParent.removeMethod(right);
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}

    
