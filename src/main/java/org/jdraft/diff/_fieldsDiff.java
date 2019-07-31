package org.jdraft.diff;

import java.util.*;

import org.jdraft.*;
import org.jdraft._field._hasFields;
import org.jdraft._java.Component;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _fieldsDiff implements _differ<List<_field>, _node> {

    public static final _fieldsDiff INSTANCE = new _fieldsDiff();
    
    public boolean equivalent(List<_field> left, List<_field> right) {
        Set<_field> lf = new HashSet<>();
        Set<_field> rf = new HashSet<>();
        lf.addAll(left);
        rf.addAll(right);
        return Objects.equals(lf, rf);
    }

    public _field getFieldNamed(Set<_field> _fs, String name) {
        Optional<_field> _f
                = _fs.stream().filter(f -> f.getName().equals(name)).findFirst();
        if (_f.isPresent()) {
            return _f.get();
        }
        return null;
    }

    public _diff diff( _hasFields left, _hasFields right){
        return diff( _path.of(), new _mutableDiffList(), (_node)left, (_node)right, left.listFields(), right.listFields());
    }
    
    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, List<_field> left, List<_field> right) {
        Set<_field> lf = new HashSet<>();
        Set<_field> rf = new HashSet<>();
        lf.addAll(left);
        rf.addAll(right);
        Set<_field> both = new HashSet<>();
        both.addAll(left);
        both.retainAll(right);

        //organize fields that have the same name as edits
        lf.removeAll(both);
        rf.removeAll(both);

        
        lf.forEach(f -> {
            _path p = path.in(Component.FIELD, f.getName());
            _field match = getFieldNamed(rf, f.getName());
            if (match != null) {
                _fieldDiff.INSTANCE.diff(path, dt, leftRoot, rightRoot, f, match);
                rf.remove(match);
            } else {
                dt.addDiff(new _leftOnly_field(p, (_field._hasFields) leftRoot, (_field._hasFields) rightRoot, f.copy()));
            }
        });
        rf.forEach(f -> {
            _path p = path.in(Component.FIELD, f.getName());
            dt.addDiff(new _rightOnly_field(p, (_field._hasFields) leftRoot, (_field._hasFields) rightRoot, f.copy()));
        });

        return (_diff) dt;
    }
    
    
    public static class _leftOnly_field
        implements _diffNode<_field._hasFields>, _diffNode._leftOnly<_field> {

        public _path path;
        public _field._hasFields leftRoot;
        public _field._hasFields rightRoot;
        public _field left;

        public _leftOnly_field( _path p, _field._hasFields leftRoot, _field._hasFields rightRoot, _field left) {
            this.path = p;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = _field.of(left.toString());
        }

        @Override
        public _field._hasFields leftRoot() {
            return leftRoot;
        }

        @Override
        public _field._hasFields rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _field left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            //remove the anno
            leftRoot.removeField(left);
            rightRoot.removeField(left);
        }

        @Override
        public void patchLeftToRight() {
            //remove it IN CASE so we dont mistakenly add it twice
            leftRoot.removeField(left);
            leftRoot.field(left.copy());
            rightRoot.removeField(left);
            rightRoot.field(left.copy());
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_field implements _diffNode<_field._hasFields>, _diffNode._rightOnly<_field> {

        public _path path;
        public _field._hasFields leftRoot;
        public _field._hasFields rightRoot;
        public _field right;

        public _rightOnly_field(_path p, _field._hasFields leftRoot, _field._hasFields rightRoot, _field right) {
            this.path = p;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.right = _field.of(right.toString() );
        }

        @Override
        public _field._hasFields leftRoot() {
            return leftRoot;
        }

        @Override
        public _field._hasFields rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _field right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            //remove it before just so we dont mistakenly add it twice
            leftRoot.removeField(right);
            leftRoot.field(right.copy());
            rightRoot.removeField(right);
            rightRoot.field(right.copy());
        }

        @Override
        public void patchLeftToRight() {
            //remove it before just so we dont mistakenly add it twice
            leftRoot.removeField(right);
            rightRoot.removeField(right);
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}
