package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

public final class _annotationDiff implements _differ<_annotation, _java._node> {

    public static final _annotationDiff INSTANCE = new _annotationDiff();

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annotation left, _annotation right) {
        _packageNameDiff.INSTANCE.diff(path, ds, left, right, left.getPackageName(), right.getPackageName());
        _importsDiff.INSTANCE.diff(path, ds, left, right, left, right);
        _annoExprsDiff.INSTANCE.diff(path, ds, left, right, left.getAnnoExprs(), right.getAnnoExprs());
        _javadocCommentDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
        _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, ds, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _fieldsDiff.INSTANCE.diff(path, ds, left, right, left.listFields(), right.listFields());
        ANNOTATION_ELEMENTS_DIFF.diff(path, ds, left, right, left.listElements(), right.listElements());
        _innerTypesDiff.INSTANCE.diff(path, ds, left, right, left.listInnerTypes(), right.listInnerTypes());

        _companionTypeDiff.INSTANCE.diff(path, ds, left, right, left.listCompanionTypes(), right.listCompanionTypes());
        return ds;
    }

    public static _annotationElementsDiff ANNOTATION_ELEMENTS_DIFF = new _annotationElementsDiff();

    public static class _annotationElementsDiff implements
            _differ<List<_annotation._entry>, _java._node> {

        public boolean equivalent(List<_annotation._entry> left, List<_annotation._entry> right) {
            Set<_annotation._entry> ls = new HashSet<>();
            Set<_annotation._entry> rs = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            return Objects.equals(ls, rs);
        }

        public _annotation._entry sameName(_annotation._entry target, Set<_annotation._entry> source) {
            Optional<_annotation._entry> ec
                    = source.stream().filter(c -> c.getName().equals(target.getName())).findFirst();
            if (ec.isPresent()) {
                return ec.get();
            }
            return null;
        }

        public _diff diff( _annotation left, _annotation right ){
            return diff( _nodePath.of(), new _diffList(left, right), left, right, left.listElements(), right.listElements());
        }
        
        @Override
        public <_PN extends _java._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, List<_annotation._entry> left, List<_annotation._entry> right) {
            Set<_annotation._entry> ls = new HashSet<>();
            Set<_annotation._entry> rs = new HashSet<>();
            Set<_annotation._entry> both = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            both.addAll(ls);
            both.retainAll(rs);

            ls.removeAll(both);
            rs.removeAll(both);
            
            ls.forEach(f -> {
                _annotation._entry cc = sameName(f, rs);
                if (cc != null) {
                    rs.remove(cc);
                    ANNOTATION_ELEMENT_DIFF.diff(path.in(_java.Feature.ANNOTATION_ENTRY, f.getName()), ds, _leftParent, _rightParent, f, cc);
                } else {
                    ds.addDiff(new _leftOnly_element(path.in(_java.Feature.ANNOTATION_ENTRY, f.getName()), (_annotation) _leftParent, (_annotation) _rightParent, f));
                }
            });

            rs.forEach(f -> {
                ds.addDiff(new _rightOnly_element(path.in(_java.Feature.ANNOTATION_ENTRY, f.getName()), (_annotation) _leftParent, (_annotation) _rightParent, f));
            });
            return ds;
        }

        public static class _rightOnly_element implements _diffNode<_annotation>, _diffNode._rightOnly<_annotation._entry> {

            public _nodePath path;
            public _annotation leftParent;
            public _annotation rightParent;
            public _annotation._entry right;

            public _rightOnly_element(_nodePath path, _annotation leftParent, _annotation rightParent, _annotation._entry right) {
                this.path = path;
                this.leftParent = leftParent;
                this.rightParent = rightParent;
                this.right = right;
            }

            @Override
            public _annotation leftParent() {
                return leftParent;
            }

            @Override
            public _annotation rightParent() {
                return rightParent;
            }

            @Override
            public void patchLeftToRight() {
                leftParent.removeEntry(right);
                rightParent.removeEntry(right);
            }

            @Override
            public void patchRightToLeft() {
                leftParent.removeEntry(right);
                rightParent.removeEntry(right);
                leftParent.addEntry(right);
                rightParent.addEntry(right);
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public _annotation._entry right() {
                return right;
            }

            @Override
            public String toString() {
                return "   + " + path;
            }
        }

        public static class _leftOnly_element implements _diffNode<_annotation>, _diffNode._leftOnly<_annotation._entry> {

            public _nodePath path;
            public _annotation leftParent;
            public _annotation rightParent;
            public _annotation._entry left;

            public _leftOnly_element(_nodePath path, _annotation leftParent, _annotation rightParent, _annotation._entry left) {
                this.path = path;
                this.leftParent = leftParent;
                this.rightParent = rightParent;
                this.left = _annotation._entry.of(left.toString());
            }

            @Override
            public _annotation leftParent() {
                return leftParent;
            }

            @Override
            public _annotation rightParent() {
                return rightParent;
            }

            @Override
            public void patchLeftToRight() {
                leftParent.removeEntry(left);
                rightParent.removeEntry(left);
            }

            @Override
            public void patchRightToLeft() {
                leftParent.removeEntry(left);
                rightParent.removeEntry(left);
                leftParent.addEntry(left);
                rightParent.addEntry(left);
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public _annotation._entry left() {
                return left;
            }

            @Override
            public String toString() {
                return "   - " + path;
            }
        }
    }

    public static _annotationElementDiff ANNOTATION_ELEMENT_DIFF
            = new _annotationElementDiff();

    public static class _annotationElementDiff implements
            _differ<_annotation._entry, _java._node> {

        @Override
        public <_PN extends _java._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annotation._entry left, _annotation._entry right) {
            _javadocCommentDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
            _annoExprsDiff.INSTANCE.diff(path, ds, left, right, left.getAnnoExprs(), right.getAnnoExprs());
            _typeRefDiff.INSTANCE.diff(path, ds, left, right, left.getTypeRef(), right.getTypeRef());
            _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
            DEFAULT_VALUE_DIFF.diff(path, ds, left, right, left.getDefaultValue(), right.getDefaultValue());
            return ds;
        }
    }

    public static final DefaultValueDiff DEFAULT_VALUE_DIFF = new DefaultValueDiff();

    public static class DefaultValueDiff implements _differ<Expression, _annotation._entry> {

        @Override
        public <_PN extends _java._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, Expression left, Expression right) {
            if (!Objects.equals(left, right)) {
                ds.addDiff(new _changeDefault(path.in(_java.Feature.DEFAULT_EXPR), (_annotation._entry) _leftParent, (_annotation._entry) _rightParent));
            }
            return (_diff) ds;
        }
    }

    /**
     * Both signifies a delta and provides a means to commit (via right()) or
     * rollback( via left())
     */
    public static class _changeDefault
            implements _diffNode, _diffNode._change<Expression> {

        _nodePath path;
        _annotation._entry leftParent;
        _annotation._entry rightParent;
        Expression leftExpression;
        Expression rightExpression;

        public _changeDefault(_nodePath _p, _annotation._entry leftParent, _annotation._entry rightParent) {
            this.path = _p;
            this.leftParent = leftParent;
            if (leftParent.hasDefaultValue()) {
                this.leftExpression = leftParent.getDefaultValue().clone();
            }
            this.rightParent = rightParent;
            if (rightParent.hasDefaultValue()) {
                this.rightExpression = rightParent.getDefaultValue().clone();
            }
        }

        @Override
        public void patchLeftToRight() {
            leftParent.setDefaultValue(leftExpression.clone());
            rightParent.setDefaultValue(leftExpression.clone());
        }

        @Override
        public void patchRightToLeft() {
            leftParent.setDefaultValue(rightExpression.clone());
            rightParent.setDefaultValue(rightExpression.clone());
        }

        @Override
        public Expression left() {
            return leftExpression;
        }

        @Override
        public Expression right() {
            return rightExpression;
        }

        @Override
        public _java._domain leftParent() {
            return leftParent;
        }

        @Override
        public _java._domain rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }
    }
}
