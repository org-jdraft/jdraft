package org.jdraft.diff;

import java.util.*;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;

import static org.jdraft.diff.Feature.*;
import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public final class _enumDiff implements _differ<_enum, _java._node> {

    public static final _enumDiff INSTANCE = new _enumDiff();
    
    public boolean equivalent(_enum left, _enum right) {
        return Objects.equals(left, right);
    }

    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _enum left, _enum right) {
        _packageNameDiff.INSTANCE.diff(path, dt, left, right, left.getPackageName(), right.getPackageName());
        _importsDiff.INSTANCE.diff(path, dt, left, right, left, right);
        _annoExprsDiff.INSTANCE.diff(path, dt, left, right, left.getAnnoExprs(), right.getAnnoExprs());

        _implementsDiff.INSTANCE.diff(path, dt, left, right, left.listAstImplements(), right.listAstImplements());
        _javadocCommentDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _initBlocksDiff.INSTANCE.diff(path, dt, left, right, left.listInitBlocks(), right.listInitBlocks());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _constructorsDiff.INSTANCE.diff(path, dt, left, right, left.listConstructors(), right.listConstructors());
        _methodsDiff.INSTANCE.diff(path, dt, left, right, left.listMethods(), right.listMethods());
        _fieldsDiff.INSTANCE.diff(path, dt, left, right, left.listFields(), right.listFields());
        ENUM_CONSTANTS_DIFF.diff(path, dt, left, right, left.listConstants(), right.listConstants());
        _innerTypesDiff.INSTANCE.diff(path, dt, left, right, left.listInnerTypes(), right.listInnerTypes()); //.INSPECT_NESTS.diff(path, dt, leftRoot, rightRoot, left.listNests(), right.listNests());

        _companionTypeDiff.INSTANCE.diff(path, dt, left, right, left.listCompanionTypes(), right.listCompanionTypes()); //.INSPECT_NESTS.diff(path, dt, leftRoot, rightRoot, left.listNests(), right.listNests());
        return (_diff) dt;
    }
      
    public static _enumConstantDiff ENUM_CONSTANT_DIFF = new _enumConstantDiff();

    public static class _enumConstantDiff
            implements _differ<_constant, _java._node> {

        public boolean equivalent(_constant left,_constant right) {
            return Objects.equals(left, right);
        }

        @Override
        public _diff diff(_constant left, _constant right) {
            _nodePath _p = new _nodePath();
            _diffList ds = new _diffList(left, right);
            return diff( _p, ds, left, right, left, right);
        }
        
        @Override
        public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _constant left, _constant right) {

            _nodePath _p = path.in(CONSTANT, left.getName());

            _annoExprsDiff.INSTANCE.diff(_p, dt, left, right, left.getAnnoExprs(), right.getAnnoExprs());
            _javadocCommentDiff.INSTANCE.diff(_p, dt, left, right, left.getJavadoc(), right.getJavadoc());
            _namedDiff.INSTANCE.diff(_p, dt, left, right, left.getName(), right.getName());            
            ARGUMENTS_DIFF.diff(_p, dt, left, right, left.listArgs(), right.listArgs());
            _methodsDiff.INSTANCE.diff(_p, dt, left, right, left.listMethods(), right.listMethods());
            _fieldsDiff.INSTANCE.diff(_p, dt, left, right, left.listFields(), right.listFields());
            return dt;
        }
    }

    public static _enumConstantsDiff ENUM_CONSTANTS_DIFF = new _enumConstantsDiff();

    public static class _enumConstantsDiff
            implements _differ<List<_constant>, _java._node> {

        public boolean equivalent(List<_constant> left, List<_constant> right) {
            Set<_constant> ls = new HashSet<>();
            Set<_constant> rs = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            return Objects.equals(ls, rs);
        }

        public _constant sameName(_constant target, Set<_constant> source) {
            Optional<_constant> ec
                    = source.stream().filter(c -> c.getName().equals(target.getName())).findFirst();
            if (ec.isPresent()) {
                return ec.get();
            }
            return null;
        }

        @Override
        public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_constant> left, List<_constant> right) {
            Set<_constant> ls = new HashSet<>();
            Set<_constant> rs = new HashSet<>();
            Set<_constant> both = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            both.addAll(ls);
            both.retainAll(rs);

            ls.removeAll(both);
            rs.removeAll(both);
            ls.forEach(f -> {
                _constant cc = sameName(f, rs);
                if (cc != null) {
                    rs.remove(cc);
                    ENUM_CONSTANT_DIFF.diff(path, dt, _leftParent, _rightParent, f, cc);
                } else {
                    dt.addDiff(new _leftOnly_enum_constant(path.in(CONSTANT, f.getName()), (_enum) _leftParent, (_enum) _rightParent, f));
                }
            });
            rs.forEach(f -> {
                dt.addDiff(new _rightOnly_enum_constant(path.in(CONSTANT, f.getName()), (_enum) _leftParent, (_enum) _rightParent, f));
            });
            return dt;
        }

        public static class _rightOnly_enum_constant implements _diffNode<_enum>, _diffNode._rightOnly<_constant> {

            public _nodePath path;
            public _enum leftRoot;
            public _enum rightRoot;
            public _constant right;

            public _rightOnly_enum_constant(_nodePath path, _enum leftRoot, _enum rightRoot, _constant right) {
                this.path = path;
                this.leftRoot = leftRoot;
                this.rightRoot = rightRoot;
                this.right = _constant.of(right.toString());
            }

            @Override
            public _enum leftParent() {
                return leftRoot;
            }

            @Override
            public _enum rightParent() {
                return rightRoot;
            }

            @Override
            public void patchLeftToRight() {
                leftRoot.removeConstant(right);
                rightRoot.removeConstant(right);
            }

            @Override
            public void patchRightToLeft() {
                leftRoot.removeConstant(right);
                leftRoot.add(right);

                rightRoot.removeConstant(right);
                rightRoot.add(right);
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public _constant right() {
                return this.right;
            }

            @Override
            public String toString() {
                return "   + " + path;
            }
        }

        public static class _leftOnly_enum_constant implements _diffNode<_enum>, _diffNode._leftOnly<_constant> {

            public _nodePath path;
            public _enum leftParent;
            public _enum rightParent;
            public _constant left;

            public _leftOnly_enum_constant(_nodePath path, _enum leftParent, _enum rightParent, _constant left) {
                this.path = path;
                this.leftParent = leftParent;
                this.rightParent = rightParent;
                this.left = _constant.of(left.toString());
            }

            @Override
            public _enum leftParent() {
                return leftParent;
            }

            @Override
            public _enum rightParent() {
                return rightParent;
            }

            @Override
            public void patchLeftToRight() {
                
                leftParent.removeConstant(left);
                leftParent.add(left);

                rightParent.removeConstant(left);
                rightParent.add(left);
            }

            @Override
            public void patchRightToLeft() {
                leftParent.removeConstant(left);
                rightParent.removeConstant(left);
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public _constant left() {
                return this.left;
            }

            @Override
            public String toString() {
                return "   - " + path;
            }
        }
    }

    public static final ArgsInspect ARGUMENTS_DIFF = new ArgsInspect();

    //public static class ArgsInspect implements _differ<List<Expression>, _java._multiPart> {
    public static class ArgsInspect implements _differ<List<_expr>, _java._node> {

        public boolean equivalent(List<Expression> left, List<Expression> right) {
            return Objects.equals(left, right);
        }

        @Override
        //public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<Expression> left, List<Expression> right) {
        public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<_expr> left, List<_expr> right) {
            if (!Objects.equals(left, right)) {
                dt.addDiff(new _changeArguments(path.in(ARGS_EXPRS), (_constant) _leftParent, (_constant) _rightParent));
            }
            return (_diff) dt;
        }

        public static class _changeArguments
                implements _diffNode<_constant>, _diffNode._change<List<Expression>> {

            _nodePath path;
            _constant leftParent;
            _constant rightParent;
            NodeList<Expression> leftArguments;
            NodeList<Expression> rightArguments;

            public _changeArguments(_nodePath path, _constant left, _constant right) {
                this.path = path;
                this.leftParent = left;
                this.rightParent = right;

                //these are effectively clones
                leftArguments = new NodeList<>();
                rightArguments = new NodeList<>();
                leftArguments.addAll(leftParent.astConstant.getArguments());
                rightArguments.addAll(rightParent.astConstant.getArguments());
            }

            @Override
            public _constant leftParent() {
                return leftParent;
            }

            @Override
            public _constant rightParent() {
                return rightParent;
            }

            @Override
            public _nodePath path() {
                return path;
            }

            @Override
            public List<Expression> left() {
                return leftArguments;
            }

            @Override
            public List<Expression> right() {
                return rightArguments;
            }

            @Override
            public void patchRightToLeft() {
                this.leftParent.setArgs(rightArguments);
                this.rightParent.setArgs(rightArguments);
            }

            @Override
            public void patchLeftToRight() {
                this.leftParent.setArgs(leftArguments);
                this.rightParent.setArgs(leftArguments);
            }

            @Override
            public String toString() {
                return "   ~ " + path;
            }
        }
    }
}
