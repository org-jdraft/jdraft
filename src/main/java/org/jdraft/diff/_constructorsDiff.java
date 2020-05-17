package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._constructor._withConstructors;
import org.jdraft._java.Component;

import org.jdraft.diff._diff.*;

public final class _constructorsDiff implements
        _differ<List<_constructor>, _java._multiPart> {

    public static final _constructorsDiff INSTANCE = new _constructorsDiff();
    
    public static _constructor sameNameAndParameterTypes(_constructor ct, Set<_constructor> lcs) {
        _params _pts = ct.getParams();
        _typeRef[] _trs = new _typeRef[_pts.size()];
        for (int i = 0; i < _pts.size(); i++) {
            _trs[i] = _pts.getAt(i).getTypeRef();
        }
        Optional<_constructor> oc
            = lcs.stream().filter(
                c -> c.getName().equals(ct.getName())
                && c.getParams().hasParamsOfType(_trs)).findFirst();
        if (oc.isPresent()) {
            return oc.get();
        }
        return null;
    }

    public boolean equivalent(List<_constructor> left, List<_constructor> right) {
        Set<_constructor> ls = new HashSet<>();
        Set<_constructor> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        return Objects.equals(ls, rs);
    }

    public _diff diff(_withConstructors left, _withConstructors right) {
        return diff( _nodePath.of(),
            new _diffList((_java._multiPart)left, (_java._multiPart)right), (_type)left, (_type)right, left.listConstructors(), right.listConstructors());
    }
    
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_constructor> left, List<_constructor> right) {
        Set<_constructor> ls = new HashSet<>();
        Set<_constructor> rs = new HashSet<>();
        Set<_constructor> both = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        both.addAll(left);
        both.retainAll(right);

        ls.removeAll(both);
        rs.removeAll(both);
        ls.forEach(c -> {
            _constructor _rct = sameNameAndParameterTypes(c, rs);
            if (_rct != null) {
                rs.remove(_rct);
                _constructorDiff.INSTANCE.diff(
                        path.in(Component.CONSTRUCTOR, _constructorDiff.constructorSignatureDescription(c)),
                        dt,
                        _leftParent,
                        _rightParent,
                        c, 
                        _rct);
            } else {
                dt.addDiff(new _leftOnly_constructor(path.in(Component.CONSTRUCTOR,
                        _constructorDiff.constructorSignatureDescription(c)),
                        (_withConstructors) _leftParent,
                        (_withConstructors) _rightParent,
                        c));
            }
        });
        rs.forEach(c -> {
            dt.addDiff( new _rightOnly_constructor(
                path.in(Component.CONSTRUCTOR,
                        _constructorDiff.constructorSignatureDescription(c)),
                    (_withConstructors) _leftParent,
                    (_withConstructors) _rightParent,
                    c));
        });
        //ok, lets check if there is an inferred constructor diff, meaning
        
        return dt;
    }

    public static class _rightOnly_constructor implements _diffNode<_withConstructors>, _diffNode._rightOnly<_constructor> {

        public _nodePath path;
        public _withConstructors leftParent;
        public _withConstructors rightParent;
        public _constructor right;

        public _rightOnly_constructor(_nodePath path, _withConstructors leftParent, _withConstructors rightParent, _constructor right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _constructor.of(right.toString());
        }

        @Override
        public _withConstructors leftParent() {
            return this.leftParent;
        }

        @Override
        public _withConstructors rightParent() {
            return this.rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.removeConstructor(right);
            rightParent.removeConstructor(right);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeConstructor(right);
            leftParent.addConstructor(right);
            rightParent.removeConstructor(right);
            rightParent.addConstructor(right);
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _constructor right() {
            return right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }

    public static class _leftOnly_constructor 
            implements _diffNode<_withConstructors>, _diffNode._leftOnly<_constructor> {

        public _nodePath path;
        public _withConstructors leftParent;
        public _withConstructors rightParent;
        public _constructor left;

        public _leftOnly_constructor(_nodePath path, _withConstructors leftParent, _withConstructors rightParent, _constructor left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _constructor.of(left.toString());
        }

        @Override
        public _withConstructors leftParent() {
            return this.leftParent;
        }

        @Override
        public _withConstructors rightParent() {
            return this.rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.removeConstructor(left);
            leftParent.addConstructor(left);
            rightParent.removeConstructor(left);
            rightParent.addConstructor(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeConstructor(left);
            rightParent.removeConstructor(left);
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _constructor left() {
            return left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
