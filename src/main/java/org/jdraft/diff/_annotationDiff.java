package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

public class _annotationDiff implements _differ<_annotation, _node> {

    public static final _annotationDiff INSTANCE = new _annotationDiff();

    @Override
    public <R extends _node> _diff diff(_path path, _build ds, R leftRoot, R rightRoot, _annotation left, _annotation right) {
        _packageNameDiff.INSTANCE.diff(path, ds, left, right, left.getPackage(), right.getPackage());
        _importsDiff.INSTANCE.diff(path, ds, left, right, left, right);
        _annosDiff.INSTANCE.diff(path, ds, left, right, left.getAnnos(), right.getAnnos());
        _javadocDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
        _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, ds, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _fieldsDiff.INSTANCE.diff(path, ds, left, right, left.listFields(), right.listFields());
        ANNOTATION_ELEMENTS_DIFF.diff(path, ds, left, right, left.listElements(), right.listElements());
        _nestsDiff.INSTANCE.diff(path, ds, left, right, left.listNests(), right.listNests());

        _companionTypeDiff.INSTANCE.diff(path, ds, left, right, left.listCompanionTypes(), right.listCompanionTypes());
        return ds;
    }

    public static _annotationElementsDiff ANNOTATION_ELEMENTS_DIFF = new _annotationElementsDiff();

    public static class _annotationElementsDiff implements
            _differ<List<_annotation._element>, _node> {

        public boolean equivalent(List<_annotation._element> left, List<_annotation._element> right) {
            Set<_annotation._element> ls = new HashSet<>();
            Set<_annotation._element> rs = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            return Objects.equals(ls, rs);
        }

        public _annotation._element sameName(_annotation._element target, Set<_annotation._element> source) {
            Optional<_annotation._element> ec
                    = source.stream().filter(c -> c.getName().equals(target.getName())).findFirst();
            if (ec.isPresent()) {
                return ec.get();
            }
            return null;
        }

        public _diff diff( _annotation left, _annotation right ){
            return diff( _path.of(), new _mutableDiffList(), left, right, left.listElements(), right.listElements());
        }
        
        @Override
        public <R extends _node> _diff diff(_path path, _build ds, R leftRoot, R rightRoot, List<_annotation._element> left, List<_annotation._element> right) {
            Set<_annotation._element> ls = new HashSet<>();
            Set<_annotation._element> rs = new HashSet<>();
            Set<_annotation._element> both = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            both.addAll(ls);
            both.retainAll(rs);

            ls.removeAll(both);
            rs.removeAll(both);
            
            ls.forEach(f -> {
                _annotation._element cc = sameName(f, rs);
                if (cc != null) {
                    rs.remove(cc);
                    ANNOTATION_ELEMENT_DIFF.diff(path.in(_java.Component.ELEMENT, f.getName()), ds, leftRoot, rightRoot, f, cc);
                } else {
                    ds.addDiff(new _leftOnly_element(path.in(_java.Component.ELEMENT, f.getName()), (_annotation) leftRoot, (_annotation) rightRoot, f));
                }
            });

            rs.forEach(f -> {
                ds.addDiff(new _rightOnly_element(path.in(_java.Component.ELEMENT, f.getName()), (_annotation) leftRoot, (_annotation) rightRoot, f));
            });
            return ds;
        }

        public static class _rightOnly_element implements _diffNode<_annotation>, _diffNode._rightOnly<_annotation._element> {

            public _path path;
            public _annotation leftRoot;
            public _annotation rightRoot;
            public _annotation._element right;

            public _rightOnly_element(_path path, _annotation leftRoot, _annotation rightRoot, _annotation._element right) {
                this.path = path;
                this.leftRoot = leftRoot;
                this.rightRoot = rightRoot;
                this.right = right;
            }

            @Override
            public _annotation leftRoot() {
                return leftRoot;
            }

            @Override
            public _annotation rightRoot() {
                return rightRoot;
            }

            @Override
            public void patchLeftToRight() {
                leftRoot.removeElement(right);
                rightRoot.removeElement(right);
            }

            @Override
            public void patchRightToLeft() {
                leftRoot.removeElement(right);
                rightRoot.removeElement(right);
                leftRoot.element(right);
                rightRoot.element(right);
            }

            @Override
            public _path path() {
                return path;
            }

            @Override
            public _annotation._element right() {
                return right;
            }

            @Override
            public String toString() {
                return "   + " + path;
            }
        }

        public static class _leftOnly_element implements _diffNode<_annotation>, _diffNode._leftOnly<_annotation._element> {

            public _path path;
            public _annotation leftRoot;
            public _annotation rightRoot;
            public _annotation._element left;

            public _leftOnly_element(_path path, _annotation leftRoot, _annotation rightRoot, _annotation._element left) {
                this.path = path;
                this.leftRoot = leftRoot;
                this.rightRoot = rightRoot;
                this.left = _annotation._element.of(left.toString());
            }

            @Override
            public _annotation leftRoot() {
                return leftRoot;
            }

            @Override
            public _annotation rightRoot() {
                return rightRoot;
            }

            @Override
            public void patchLeftToRight() {
                leftRoot.removeElement(left);
                rightRoot.removeElement(left);
            }

            @Override
            public void patchRightToLeft() {
                leftRoot.removeElement(left);
                rightRoot.removeElement(left);
                leftRoot.element(left);
                rightRoot.element(left);
            }

            @Override
            public _path path() {
                return path;
            }

            @Override
            public _annotation._element left() {
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
            _differ<_annotation._element, _node> {

        @Override
        public <R extends _node> _diff diff(_path path, _build ds, R leftRoot, R rightRoot, _annotation._element left, _annotation._element right) {
            _javadocDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
            _annosDiff.INSTANCE.diff(path, ds, left, right, left.getAnnos(), right.getAnnos());
            _typeRefDiff.INSTANCE.diff(path, ds, left, right, left.getType(), right.getType());
            _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
            DEFAULT_VALUE_DIFF.diff(path, ds, left, right, left.getDefaultValue(), right.getDefaultValue());
            return (_diff) ds;
        }
    }

    public static final DefaultValueDiff DEFAULT_VALUE_DIFF = new DefaultValueDiff();

    public static class DefaultValueDiff implements _differ<Expression, _annotation._element> {

        @Override
        public <R extends _node> _diff diff(_path path, _build ds, R leftRoot, R rightRoot, Expression left, Expression right) {
            if (!Objects.equals(left, right)) {
                ds.addDiff(new _changeDefault(path.in(_java.Component.DEFAULT), (_annotation._element) leftRoot, (_annotation._element) rightRoot));
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

        _path path;
        _annotation._element leftRoot;
        _annotation._element rightRoot;
        Expression leftExpression;
        Expression rightExpression;

        public _changeDefault(_path _p, _annotation._element leftRoot, _annotation._element rightRoot) {
            this.path = _p;
            this.leftRoot = leftRoot;
            if (leftRoot.hasDefaultValue()) {
                this.leftExpression = leftRoot.getDefaultValue().clone();
            }
            this.rightRoot = rightRoot;
            if (rightRoot.hasDefaultValue()) {
                this.rightExpression = rightRoot.getDefaultValue().clone();
            }
        }

        @Override
        public void patchLeftToRight() {
            leftRoot.setDefaultValue(leftExpression.clone());
            rightRoot.setDefaultValue(leftExpression.clone());
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.setDefaultValue(rightExpression.clone());
            rightRoot.setDefaultValue(rightExpression.clone());
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
        public _java leftRoot() {
            return leftRoot;
        }

        @Override
        public _java rightRoot() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }
    }
}
