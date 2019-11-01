package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._constructor._hasConstructors;
import org.jdraft._java.Component;

import org.jdraft.diff._diff.*;

public class _constructorsDiff implements
        _differ<List<_constructor>, _node> {

    public static final _constructorsDiff INSTANCE = new _constructorsDiff();
    
    public static _constructor sameNameAndParameterTypes(_constructor ct, Set<_constructor> lcs) {
        _parameter._parameters _pts = ct.getParameters();
        _typeRef[] _trs = new _typeRef[_pts.size()];
        for (int i = 0; i < _pts.size(); i++) {
            _trs[i] = _pts.get(i).getType();
        }
        Optional<_constructor> oc
            = lcs.stream().filter(
                c -> c.getName().equals(ct.getName())
                && c.getParameters().hasParametersOfType(_trs)).findFirst();
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

    public _diff diff(_hasConstructors left, _hasConstructors right) {
        return diff( _nodePath.of(),
            new _diffList((_node)left, (_node)right), (_type)left, (_type)right, left.listConstructors(), right.listConstructors());
    }
    
    @Override
    public <_PN extends _node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_constructor> left, List<_constructor> right) {
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
                        (_constructor._hasConstructors) _leftParent,
                        (_constructor._hasConstructors) _rightParent,
                        c));
            }
        });
        rs.forEach(c -> {
            dt.addDiff( new _rightOnly_constructor(
                path.in(Component.CONSTRUCTOR,
                        _constructorDiff.constructorSignatureDescription(c)),
                    (_constructor._hasConstructors) _leftParent,
                    (_constructor._hasConstructors) _rightParent,
                    c));
        });
        //ok, lets check if there is an inferred constructor diff, meaning
        
        return dt;
    }

    public static class _rightOnly_constructor implements _diffNode<_constructor._hasConstructors>, _diffNode._rightOnly<_constructor> {

        public _nodePath path;
        public _constructor._hasConstructors leftParent;
        public _constructor._hasConstructors rightParent;
        public _constructor right;

        public _rightOnly_constructor(_nodePath path, _constructor._hasConstructors leftParent, _constructor._hasConstructors rightParent, _constructor right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _constructor.of(right.toString());
        }

        @Override
        public _constructor._hasConstructors leftParent() {
            return this.leftParent;
        }

        @Override
        public _constructor._hasConstructors rightParent() {
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
            leftParent.constructor(right);
            rightParent.removeConstructor(right);
            rightParent.constructor(right);
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
            implements _diffNode<_constructor._hasConstructors>, _diffNode._leftOnly<_constructor> {

        public _nodePath path;
        public _constructor._hasConstructors leftParent;
        public _constructor._hasConstructors rightParent;
        public _constructor left;

        public _leftOnly_constructor(_nodePath path, _constructor._hasConstructors leftParent, _constructor._hasConstructors rightParent, _constructor left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _constructor.of(left.toString());
        }

        @Override
        public _constructor._hasConstructors leftParent() {
            return this.leftParent;
        }

        @Override
        public _constructor._hasConstructors rightParent() {
            return this.rightParent;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.removeConstructor(left);
            leftParent.constructor(left);
            rightParent.removeConstructor(left);
            rightParent.constructor(left);
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
