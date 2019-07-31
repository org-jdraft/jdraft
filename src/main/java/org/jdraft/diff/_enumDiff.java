package org.jdraft.diff;

import java.util.*;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;

import org.jdraft.*;

import static org.jdraft._java.Component.*;
import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _enumDiff implements _differ<_enum, _node> {

    public static final _enumDiff INSTANCE = new _enumDiff();
    
    public boolean equivalent(_enum left, _enum right) {
        return Objects.equals(left, right);
    }

    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _enum left, _enum right) {
        _packageNameDiff.INSTANCE.diff(path, dt, left, right, left.getPackage(), right.getPackage());
        _importsDiff.INSTANCE.diff(path, dt, left, right, left, right);
        _annosDiff.INSTANCE.diff(path, dt, left, right, left.getAnnos(), right.getAnnos());

        _implementsDiff.INSTANCE.diff(path, dt, left, right, left.listImplements(), right.listImplements());
        _javadocDiff.INSTANCE.diff(path, dt, left, right, left.getJavadoc(), right.getJavadoc());
        _staticBlocksDiff.INSTANCE.diff(path, dt, left, right, left.listStaticBlocks(), right.listStaticBlocks());
        _namedDiff.INSTANCE.diff(path, dt, left, right, left.getName(), right.getName());
        _modifiersDiff.INSTANCE.diff(path, dt, left, right, left.getEffectiveModifiers(), right.getEffectiveModifiers());
        _constructorsDiff.INSTANCE.diff(path, dt, left, right, left.listConstructors(), right.listConstructors());
        _methodsDiff.INSTANCE.diff(path, dt, left, right, left.listMethods(), right.listMethods());
        _fieldsDiff.INSTANCE.diff(path, dt, left, right, left.listFields(), right.listFields());
        ENUM_CONSTANTS_DIFF.diff(path, dt, left, right, left.listConstants(), right.listConstants());
        _nestsDiff.INSTANCE.diff(path, dt, left, right, left.listNests(), right.listNests()); //.INSPECT_NESTS.diff(path, dt, leftRoot, rightRoot, left.listNests(), right.listNests());

        _companionTypeDiff.INSTANCE.diff(path, dt, left, right, left.listCompanionTypes(), right.listCompanionTypes()); //.INSPECT_NESTS.diff(path, dt, leftRoot, rightRoot, left.listNests(), right.listNests());
        return (_diff) dt;
    }
      
    public static _enumConstantDiff ENUM_CONSTANT_DIFF = new _enumConstantDiff();

    public static class _enumConstantDiff
            implements _differ<_enum._constant, _node> {

        public boolean equivalent(_enum._constant left, _enum._constant right) {
            return Objects.equals(left, right);
        }

        @Override
        public _diff diff(_enum._constant left, _enum._constant right) {
            _path _p = new _path();
            _diff._mutableDiffList ds = new _diff._mutableDiffList();
            return diff( _p, ds, left, right, left, right);
        }
        
        @Override
        public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _enum._constant left, _enum._constant right) {

            _path _p = path.in(CONSTANT, left.getName());

            _annosDiff.INSTANCE.diff(_p, dt, left, right, left.getAnnos(), right.getAnnos());
            _javadocDiff.INSTANCE.diff(_p, dt, left, right, left.getJavadoc(), right.getJavadoc());
            _namedDiff.INSTANCE.diff(_p, dt, left, right, left.getName(), right.getName());            
            ARGUMENTS_DIFF.diff(_p, dt, left, right, left.listArguments(), right.listArguments());
            _methodsDiff.INSTANCE.diff(_p, dt, left, right, left.listMethods(), right.listMethods());
            _fieldsDiff.INSTANCE.diff(_p, dt, left, right, left.listFields(), right.listFields());
            return (_diff) dt;
        }
    }

    public static _enumConstantsDiff ENUM_CONSTANTS_DIFF = new _enumConstantsDiff();

    public static class _enumConstantsDiff
            implements _differ<List<_enum._constant>, _node> {

        public boolean equivalent(List<_enum._constant> left, List<_enum._constant> right) {
            Set<_enum._constant> ls = new HashSet<>();
            Set<_enum._constant> rs = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            return Objects.equals(ls, rs);
        }

        public _enum._constant sameName(_enum._constant target, Set<_enum._constant> source) {
            Optional<_enum._constant> ec
                    = source.stream().filter(c -> c.getName().equals(target.getName())).findFirst();
            if (ec.isPresent()) {
                return ec.get();
            }
            return null;
        }

        @Override
        public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, List<_enum._constant> left, List<_enum._constant> right) {
            Set<_enum._constant> ls = new HashSet<>();
            Set<_enum._constant> rs = new HashSet<>();
            Set<_enum._constant> both = new HashSet<>();
            ls.addAll(left);
            rs.addAll(right);
            both.addAll(ls);
            both.retainAll(rs);

            ls.removeAll(both);
            rs.removeAll(both);
            ls.forEach(f -> {
                _enum._constant cc = sameName(f, rs);
                if (cc != null) {
                    rs.remove(cc);
                    ENUM_CONSTANT_DIFF.diff(path, dt, leftRoot, rightRoot, f, cc);
                } else {
                    dt.addDiff(new _leftOnly_enum_constant(path.in(CONSTANT, f.getName()), (_enum) leftRoot, (_enum) rightRoot, f));
                }
            });
            rs.forEach(f -> {
                dt.addDiff(new _rightOnly_enum_constant(path.in(CONSTANT, f.getName()), (_enum) leftRoot, (_enum) rightRoot, f));
            });
            return (_diff) dt;
        }

        public static class _rightOnly_enum_constant implements _diffNode<_enum>, _diffNode._rightOnly<_enum._constant> {

            public _path path;
            public _enum leftRoot;
            public _enum rightRoot;
            public _enum._constant right;

            public _rightOnly_enum_constant(_path path, _enum leftRoot, _enum rightRoot, _enum._constant right) {
                this.path = path;
                this.leftRoot = leftRoot;
                this.rightRoot = rightRoot;
                this.right = _enum._constant.of(right.toString());
            }

            @Override
            public _enum leftRoot() {
                return leftRoot;
            }

            @Override
            public _enum rightRoot() {
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
            public _path path() {
                return path;
            }

            @Override
            public _enum._constant right() {
                return this.right;
            }

            @Override
            public String toString() {
                return "   + " + path;
            }
        }

        public static class _leftOnly_enum_constant implements _diffNode<_enum>, _diffNode._leftOnly<_enum._constant> {

            public _path path;
            public _enum leftRoot;
            public _enum rightRoot;
            public _enum._constant left;

            public _leftOnly_enum_constant(_path path, _enum leftRoot, _enum rightRoot, _enum._constant left) {
                this.path = path;
                this.leftRoot = leftRoot;
                this.rightRoot = rightRoot;
                this.left = _enum._constant.of(left.toString());
            }

            @Override
            public _enum leftRoot() {
                return leftRoot;
            }

            @Override
            public _enum rightRoot() {
                return rightRoot;
            }

            @Override
            public void patchLeftToRight() {
                
                leftRoot.removeConstant(left);
                leftRoot.add(left);

                rightRoot.removeConstant(left);
                rightRoot.add(left);
            }

            @Override
            public void patchRightToLeft() {
                leftRoot.removeConstant(left);
                rightRoot.removeConstant(left);
            }

            @Override
            public _path path() {
                return path;
            }

            @Override
            public _enum._constant left() {
                return this.left;
            }

            @Override
            public String toString() {
                return "   - " + path;
            }
        }
    }

    public static final ArgsInspect ARGUMENTS_DIFF = new ArgsInspect();

    public static class ArgsInspect implements _differ<List<Expression>, _node> {

        public boolean equivalent(List<Expression> left, List<Expression> right) {
            return Objects.equals(left, right);
        }

        @Override
        public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, List<Expression> left, List<Expression> right) {
            if (!Objects.equals(left, right)) {
                dt.addDiff(new _changeArguments(path.in(ARGUMENTS), (_enum._constant) leftRoot, (_enum._constant) rightRoot));
            }
            return (_diff) dt;
        }

        public static class _changeArguments
                implements _diffNode<_enum._constant>, _diffNode._change<List<Expression>> {

            _path path;
            _enum._constant leftRoot;
            _enum._constant rightRoot;
            NodeList<Expression> leftArguments;
            NodeList<Expression> rightArguments;

            public _changeArguments(_path path, _enum._constant left, _enum._constant right) {
                this.path = path;
                this.leftRoot = left;
                this.rightRoot = right;

                //these are effectively clones
                leftArguments = new NodeList<>();
                rightArguments = new NodeList<>();
                leftArguments.addAll(leftRoot.astConstant.getArguments());
                rightArguments.addAll(rightRoot.astConstant.getArguments());
            }

            @Override
            public _enum._constant leftRoot() {
                return leftRoot;
            }

            @Override
            public _enum._constant rightRoot() {
                return rightRoot;
            }

            @Override
            public _path path() {
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
                this.leftRoot.setArguments(rightArguments);
                this.rightRoot.setArguments(rightArguments);
            }

            @Override
            public void patchLeftToRight() {
                this.leftRoot.setArguments(leftArguments);
                this.rightRoot.setArguments(leftArguments);
            }

            @Override
            public String toString() {
                return "   ~ " + path;
            }
        }
    }
}
