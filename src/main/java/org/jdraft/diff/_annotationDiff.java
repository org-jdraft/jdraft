package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;
import org.jdraft.diff._diff.*;

public final class _annotationDiff implements _differ<_annotation, _tree._node> {

    public static final _annotationDiff INSTANCE = new _annotationDiff();

    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annotation left, _annotation right) {
        _packageNameDiff.INSTANCE.diff(path, ds, left, right, left.getPackageName(), right.getPackageName());
        _importsDiff.INSTANCE.diff(path, ds, left, right, left, right);
        _annosDiff.INSTANCE.diff(path, ds, left, right, left.getAnnos(), right.getAnnos());
        _javadocCommentDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
        _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, ds, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _fieldsDiff.INSTANCE.diff(path, ds, left, right, left.listFields(), right.listFields());
        ANNOTATION_ELEMENTS_DIFF.diff(path, ds, left, right, left.listAnnoMembers(), right.listAnnoMembers());
        _innerTypesDiff.INSTANCE.diff(path, ds, left, right, left.listInnerTypes(), right.listInnerTypes());

        _companionTypeDiff.INSTANCE.diff(path, ds, left, right, left.listCompanionTypes(), right.listCompanionTypes());
        return ds;
    }

    public static _annotationElementsDiff ANNOTATION_ELEMENTS_DIFF = new _annotationElementsDiff();

    public static class _annotationElementsDiff implements
            _differ<List<_annoMember>, _tree._node> {

        public boolean equivalent(List<_annoMember> left, List<_annoMember> right) {
            Set<_annoMember> ls = new HashSet<>();
            Set<_annoMember> rs = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            return Objects.equals(ls, rs);
        }

        public _annoMember sameName(_annoMember target, Set<_annoMember> source) {
            Optional<_annoMember> ec
                    = source.stream().filter(c -> c.getName().equals(target.getName())).findFirst();
            if (ec.isPresent()) {
                return ec.get();
            }
            return null;
        }

        public _diff diff( _annotation left, _annotation right ){
            return diff( _nodePath.of(), new _diffList(left, right), left, right, left.listAnnoMembers(), right.listAnnoMembers());
        }
        
        @Override
        public <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, List<_annoMember> left, List<_annoMember> right) {
            Set<_annoMember> ls = new HashSet<>();
            Set<_annoMember> rs = new HashSet<>();
            Set<_annoMember> both = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            both.addAll(ls);
            both.retainAll(rs);

            ls.removeAll(both);
            rs.removeAll(both);
            
            ls.forEach(f -> {
                _annoMember cc = sameName(f, rs);
                if (cc != null) {
                    rs.remove(cc);
                    ANNOTATION_ELEMENT_DIFF.diff(path.in(Feature.ANNOTATION_ENTRY, f.getName()), ds, _leftParent, _rightParent, f, cc);
                } else {
                    ds.addDiff(new _leftOnly_element(path.in(Feature.ANNOTATION_ENTRY, f.getName()), (_annotation) _leftParent, (_annotation) _rightParent, f));
                }
            });

            rs.forEach(f -> {
                ds.addDiff(new _rightOnly_element(path.in(Feature.ANNOTATION_ENTRY, f.getName()), (_annotation) _leftParent, (_annotation) _rightParent, f));
            });
            return ds;
        }

        public static class _rightOnly_element implements _diffNode<_annotation>, _diffNode._rightOnly<_annoMember> {

            public _nodePath path;
            public _annotation leftParent;
            public _annotation rightParent;
            public _annoMember right;

            public _rightOnly_element(_nodePath path, _annotation leftParent, _annotation rightParent, _annoMember right) {
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
                leftParent.removeAnnoMember(right);
                rightParent.removeAnnoMember(right);
            }

            @Override
            public void patchRightToLeft() {
                leftParent.removeAnnoMember(right);
                rightParent.removeAnnoMember(right);
                leftParent.addAnnoMember(right);
                rightParent.addAnnoMember(right);
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public _annoMember right() {
                return right;
            }

            @Override
            public String toString() {
                return "   + " + path;
            }
        }

        public static class _leftOnly_element implements _diffNode<_annotation>, _diffNode._leftOnly<_annoMember> {

            public _nodePath path;
            public _annotation leftParent;
            public _annotation rightParent;
            public _annoMember left;

            public _leftOnly_element(_nodePath path, _annotation leftParent, _annotation rightParent, _annoMember left) {
                this.path = path;
                this.leftParent = leftParent;
                this.rightParent = rightParent;
                this.left = _annoMember.of(left.toString());
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
                leftParent.removeAnnoMember(left);
                rightParent.removeAnnoMember(left);
            }

            @Override
            public void patchRightToLeft() {
                leftParent.removeAnnoMember(left);
                rightParent.removeAnnoMember(left);
                leftParent.addAnnoMember(left);
                rightParent.addAnnoMember(left);
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public _annoMember left() {
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
            _differ<_annoMember, _tree._node> {

        @Override
        public <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annoMember left, _annoMember right) {
            _javadocCommentDiff.INSTANCE.diff(path, ds, left, right, left.getJavadoc(), right.getJavadoc());
            _annosDiff.INSTANCE.diff(path, ds, left, right, left.getAnnos(), right.getAnnos());
            _typeRefDiff.INSTANCE.diff(path, ds, left, right, left.getType(), right.getType());
            _namedDiff.INSTANCE.diff(path, ds, left, right, left.getName(), right.getName());
            DEFAULT_VALUE_DIFF.diff(path, ds, left, right, left.getDefaultAstValue(), right.getDefaultAstValue());
            return ds;
        }
    }

    public static final DefaultValueDiff DEFAULT_VALUE_DIFF = new DefaultValueDiff();

    public static class DefaultValueDiff implements _differ<Expression, _annoMember> {

        @Override
        public <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, Expression left, Expression right) {
            if (!Objects.equals(left, right)) {
                ds.addDiff(new _changeDefault(path.in(Feature.DEFAULT_EXPR), (_annoMember) _leftParent, (_annoMember) _rightParent));
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
        _annoMember leftParent;
        _annoMember rightParent;
        Expression leftExpression;
        Expression rightExpression;

        public _changeDefault(_nodePath _p, _annoMember leftParent, _annoMember rightParent) {
            this.path = _p;
            this.leftParent = leftParent;
            if (leftParent.hasDefaultValue()) {
                this.leftExpression = leftParent.getDefaultAstValue().clone();
            }
            this.rightParent = rightParent;
            if (rightParent.hasDefaultValue()) {
                this.rightExpression = rightParent.getDefaultAstValue().clone();
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
