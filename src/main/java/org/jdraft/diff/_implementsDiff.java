package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import org.jdraft.*;
import static org.jdraft.Types.equal;
import org.jdraft._java.Component;
import org.jdraft._type._withImplements;

import org.jdraft.diff._diff.*;

public final class _implementsDiff implements
        _differ<List<ClassOrInterfaceType>, _java._multiPart> {

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
        if( _t1 instanceof _type._withImplements){
            impls1 = ((_withImplements)_t1).listImplements();
        }
        if( _t1 instanceof _type._withImplements){
            impls2 = ((_withImplements)_t2).listImplements();
        }
        return diff( impls1, impls2);
    }
    
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<ClassOrInterfaceType> left, List<ClassOrInterfaceType> right) {
        
        //probably the best/easiest wasy is to put something in _type
        
        //List<ClassOrInterfaceType>addRight = new ArrayList<>();
        //List<ClassOrInterfaceType>addLeft = new ArrayList<>();
        for (int i = 0; i < left.size(); i++) {
            ClassOrInterfaceType cit = left.get(i);
            if (!right.stream().filter(c -> Types.equal(c, cit)).findFirst().isPresent()) {
                //addRight.add(cit);
                dt.addDiff(new _leftOnly_implements(path.in(Component.IMPLEMENTS), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }
        for (int i = 0; i < right.size(); i++) {
            ClassOrInterfaceType cit = right.get(i);
            if (!left.stream().filter(c -> Types.equal(c, cit)).findFirst().isPresent()) {
                //addLeft.add(cit);
                dt.addDiff(new _rightOnly_implements(path.in(Component.IMPLEMENTS), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }
        return (_diff) dt;
    }

    public static class _rightOnly_implements
            implements _diffNode<_type>, _diffNode._rightOnly<ClassOrInterfaceType> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public ClassOrInterfaceType right;

        public _rightOnly_implements(_nodePath path, _type leftParent, _type rightParent, ClassOrInterfaceType right) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = right.clone();
        }

        @Override
        public _type leftParent() {
            return leftParent;
        }

        @Override
        public _type rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            ((_withImplements) leftParent).removeImplements(right);
            ((_withImplements) rightParent).removeImplements(right);
        }

        @Override
        public void patchRightToLeft() {
            ((_withImplements) leftParent).removeImplements(right);
            ((_withImplements) leftParent).addImplements(right);
            ((_withImplements) rightParent).removeImplements(right);
            ((_withImplements) rightParent).addImplements(right);
        }

        @Override
        public _nodePath path() {
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

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public ClassOrInterfaceType left;

        public _leftOnly_implements(_nodePath path, _type leftParent, _type rightParent, ClassOrInterfaceType left) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = left.clone();
        }

        @Override
        public _type leftParent() {
            return leftParent;
        }

        @Override
        public _type rightParent() {
            return rightParent;
        }

        @Override
        public void patchLeftToRight() {
            ((_withImplements) leftParent).removeImplements(left);
            ((_withImplements) leftParent).addImplements(left);
            ((_withImplements) rightParent).removeImplements(left);
            ((_withImplements) rightParent).addImplements(left);
        }

        @Override
        public void patchRightToLeft() {
            ((_withImplements) leftParent).removeImplements(left);
            ((_withImplements) rightParent).removeImplements(left);
        }

        @Override
        public _nodePath path() {
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
