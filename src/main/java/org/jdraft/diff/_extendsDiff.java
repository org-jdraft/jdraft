package org.jdraft.diff;

import java.util.*;

import com.github.javaparser.ast.type.ClassOrInterfaceType;

import org.jdraft.*;
import static org.jdraft.Ast.typesEqual;
import org.jdraft._java.Component;
import org.jdraft._type._hasExtends;

import org.jdraft.diff._diff._build;


import org.jdraft.diff._diff._differ;

public class _extendsDiff implements
        _differ<List<ClassOrInterfaceType>, _java._compound> {

    public static final _extendsDiff INSTANCE = new _extendsDiff();
    
    public boolean equivalent(List<ClassOrInterfaceType> left, List<ClassOrInterfaceType> right) {
        Set<ClassOrInterfaceType> ls = new HashSet<>();
        Set<ClassOrInterfaceType> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        return Objects.equals(ls, rs);
    }

    public _diff diff(_type left, _type right){
        List<ClassOrInterfaceType> lts = new ArrayList<>();
        List<ClassOrInterfaceType> rts = new ArrayList<>();
        
        if( left instanceof _hasExtends){
            lts = ((_hasExtends)left).listExtends();
        }
        if( right instanceof _hasExtends){
            rts = ((_hasExtends)right).listExtends();
        }
        return diff( lts, rts);
    }
    
    @Override
    public <_PN extends _java._compound> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<ClassOrInterfaceType> left, List<ClassOrInterfaceType> right) {
        
        for (int i = 0; i < left.size(); i++) {
            ClassOrInterfaceType cit = left.get(i);
            if (!right.stream().filter(c -> typesEqual(c, cit)).findFirst().isPresent()) {
                dt.addDiff(new _leftOnly_extends(path.in(Component.EXTENDS), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }
        for (int i = 0; i < right.size(); i++) {
            ClassOrInterfaceType cit = right.get(i);
            if (!left.stream().filter(c -> typesEqual(c, cit)).findFirst().isPresent()) {
                dt.addDiff(new _rightOnly_extends(path.in(Component.EXTENDS), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }        
        return dt;
    }

    public static class _rightOnly_extends
            implements _diffNode<_type>, _diffNode._rightOnly<ClassOrInterfaceType> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public ClassOrInterfaceType right;

        public _rightOnly_extends(_nodePath path, _type leftParent, _type rightParent, ClassOrInterfaceType right) {
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
            ((_type._hasExtends) leftParent).removeExtends(right);
            ((_type._hasExtends) rightParent).removeExtends(right);
        }

        @Override
        public void patchRightToLeft() {
            ((_type._hasExtends) leftParent).removeExtends(right);
            ((_type._hasExtends) leftParent).addExtend(right);
            ((_type._hasExtends) rightParent).removeExtends(right);
            ((_type._hasExtends) rightParent).addExtend(right);
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

    public static class _leftOnly_extends
            implements _diffNode<_type>, _diffNode._leftOnly<ClassOrInterfaceType> {

        public _nodePath path;
        public _type leftParent;
        public _type rightParent;
        public ClassOrInterfaceType left;

        public _leftOnly_extends(_nodePath path, _type leftParent, _type rightParent, ClassOrInterfaceType left) {
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
            ((_type._hasExtends) leftParent).removeExtends(left);
            ((_type._hasExtends) leftParent).addExtend(left);
            ((_type._hasExtends) rightParent).removeExtends(left);
            ((_type._hasExtends) rightParent).addExtend(left);
        }

        @Override
        public void patchRightToLeft() {
            ((_type._hasExtends) leftParent).removeExtends(left);
            ((_type._hasExtends) rightParent).removeExtends(left);
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
