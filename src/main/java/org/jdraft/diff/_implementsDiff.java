package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import org.jdraft.*;
import static org.jdraft.Ast.typesEqual;
import org.jdraft._java.Component;
import org.jdraft._type._hasImplements;

import org.jdraft.diff._diff.*;

public class _implementsDiff implements
        _differ<List<ClassOrInterfaceType>, _node> {

    public static final _implementsDiff INSTANCE = new _implementsDiff();

    public boolean equivalent(List<ClassOrInterfaceType> left, List<ClassOrInterfaceType> right) {
        Set<ClassOrInterfaceType> ls = new HashSet<>();
        Set<ClassOrInterfaceType> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        return Objects.equals(ls, rs);
    }

    public _diff diff(_type _t1, _type _t2){
        List<ClassOrInterfaceType> impls1 = new ArrayList<>();
        List<ClassOrInterfaceType> impls2 = new ArrayList<>();
        if( _t1 instanceof _hasImplements ){
            impls1 = ((_hasImplements)_t1).listImplements();
        }
        if( _t1 instanceof _hasImplements ){
            impls2 = ((_hasImplements)_t2).listImplements();
        }
        return diff( impls1, impls2);
    }
    
    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, List<ClassOrInterfaceType> left, List<ClassOrInterfaceType> right) {
        
        //probably the best/easiest wasy is to put something in _type
        
        //List<ClassOrInterfaceType>addRight = new ArrayList<>();
        //List<ClassOrInterfaceType>addLeft = new ArrayList<>();
        for (int i = 0; i < left.size(); i++) {
            ClassOrInterfaceType cit = left.get(i);
            if (!right.stream().filter(c -> typesEqual(c, cit)).findFirst().isPresent()) {
                //addRight.add(cit);
                dt.addDiff(new _leftOnly_implements(path.in(Component.IMPLEMENTS), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }
        for (int i = 0; i < right.size(); i++) {
            ClassOrInterfaceType cit = right.get(i);
            if (!left.stream().filter(c -> typesEqual(c, cit)).findFirst().isPresent()) {
                //addLeft.add(cit);
                dt.addDiff(new _rightOnly_implements(path.in(Component.IMPLEMENTS), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }
        return (_diff) dt;
    }

    public static class _rightOnly_implements
            implements _diffNode<_type>, _diffNode._rightOnly<ClassOrInterfaceType> {

        public _path path;
        public _type leftRoot;
        public _type rightRoot;
        public ClassOrInterfaceType right;

        public _rightOnly_implements(_path path, _type leftRoot, _type rightRoot, ClassOrInterfaceType right) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.right = right.clone();
        }

        @Override
        public _type leftParent() {
            return leftRoot;
        }

        @Override
        public _type rightParent() {
            return rightRoot;
        }

        @Override
        public void patchLeftToRight() {
            ((_type._hasImplements) leftRoot).removeImplements(right);
            ((_type._hasImplements) rightRoot).removeImplements(right);
        }

        @Override
        public void patchRightToLeft() {
            ((_type._hasImplements) leftRoot).removeImplements(right);
            ((_type._hasImplements) leftRoot).implement(right);
            ((_type._hasImplements) rightRoot).removeImplements(right);
            ((_type._hasImplements) rightRoot).implement(right);
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public ClassOrInterfaceType right() {
            return this.right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }

    public static class _leftOnly_implements
            implements _diffNode<_type>, _diffNode._leftOnly<ClassOrInterfaceType> {

        public _path path;
        public _type leftRoot;
        public _type rightRoot;
        public ClassOrInterfaceType left;

        public _leftOnly_implements(_path path, _type leftRoot, _type rightRoot, ClassOrInterfaceType left) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = left.clone();
        }

        @Override
        public _type leftParent() {
            return leftRoot;
        }

        @Override
        public _type rightParent() {
            return rightRoot;
        }

        @Override
        public void patchLeftToRight() {
            ((_type._hasImplements) leftRoot).removeImplements(left);
            ((_type._hasImplements) leftRoot).implement(left);
            ((_type._hasImplements) rightRoot).removeImplements(left);
            ((_type._hasImplements) rightRoot).implement(left);
        }

        @Override
        public void patchRightToLeft() {
            ((_type._hasImplements) leftRoot).removeImplements(left);
            ((_type._hasImplements) rightRoot).removeImplements(left);
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public ClassOrInterfaceType left() {
            return this.left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
