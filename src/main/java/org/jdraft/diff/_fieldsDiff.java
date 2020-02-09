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
public class _fieldsDiff implements _differ<List<_field>, _java._compound> {

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
        return diff( _nodePath.of(), new _diffList((_java._compound)left, (_java._compound)right), (_java._compound)left, (_java._compound)right, left.listFields(), right.listFields());
    }
    
    @Override
    public <_PN extends _java._compound> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_field> left, List<_field> right) {
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
            _nodePath p = path.in(Component.FIELD, f.getName());
            _field match = getFieldNamed(rf, f.getName());
            if (match != null) {
                _fieldDiff.INSTANCE.diff(path, dt, _leftParent, _rightParent, f, match);
                rf.remove(match);
            } else {
                dt.addDiff(new _leftOnly_field(p, (_field._hasFields) _leftParent, (_field._hasFields) _rightParent, f.copy()));
            }
        });
        rf.forEach(f -> {
            _nodePath p = path.in(Component.FIELD, f.getName());
            dt.addDiff(new _rightOnly_field(p, (_field._hasFields) _leftParent, (_field._hasFields) _rightParent, f.copy()));
        });

        return (_diff) dt;
    }
    
    
    public static class _leftOnly_field
        implements _diffNode<_field._hasFields>, _diffNode._leftOnly<_field> {

        public _nodePath path;
        public _field._hasFields leftParent;
        public _field._hasFields rightParent;
        public _field left;

        public _leftOnly_field(_nodePath p, _field._hasFields leftParent, _field._hasFields rightParent, _field left) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = _field.of(left.toString());
        }

        @Override
        public _field._hasFields leftParent() {
            return leftParent;
        }

        @Override
        public _field._hasFields rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _field left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            //remove the anno
            leftParent.removeField(left);
            rightParent.removeField(left);
        }

        @Override
        public void patchLeftToRight() {
            //remove it IN CASE so we dont mistakenly add it twice
            leftParent.removeField(left);
            leftParent.field(left.copy());
            rightParent.removeField(left);
            rightParent.field(left.copy());
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_field implements _diffNode<_field._hasFields>, _diffNode._rightOnly<_field> {

        public _nodePath path;
        public _field._hasFields leftParent;
        public _field._hasFields rightParent;
        public _field right;

        public _rightOnly_field(_nodePath p, _field._hasFields leftParent, _field._hasFields rightParent, _field right) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = _field.of(right.toString() );
        }

        @Override
        public _field._hasFields leftParent() {
            return leftParent;
        }

        @Override
        public _field._hasFields rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _field right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeField(right);
            leftParent.field(right.copy());
            rightParent.removeField(right);
            rightParent.field(right.copy());
        }

        @Override
        public void patchLeftToRight() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeField(right);
            rightParent.removeField(right);
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}
