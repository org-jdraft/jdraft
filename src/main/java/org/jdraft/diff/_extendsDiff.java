package org.jdraft.diff;

import java.util.*;

import com.github.javaparser.ast.type.ClassOrInterfaceType;

import org.jdraft.*;
import static org.jdraft.Types.equal;
import org.jdraft._java.Feature;
import org.jdraft._type._withExtends;

import org.jdraft.diff._diff._build;


import org.jdraft.diff._diff._differ;

public final class _extendsDiff implements
        _differ<List<ClassOrInterfaceType>, _java._node> {

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
        
        if( left instanceof _type._withExtends){
            lts = ((_withExtends)left).listExtends();
        }
        if( right instanceof _type._withExtends){
            rts = ((_withExtends)right).listExtends();
        }
        return diff( lts, rts);
    }
    
    @Override
    public <_PN extends _java._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, List<ClassOrInterfaceType> left, List<ClassOrInterfaceType> right) {
        
        for (int i = 0; i < left.size(); i++) {
            ClassOrInterfaceType cit = left.get(i);
            if (!right.stream().filter(c -> Types.equal(c, cit)).findFirst().isPresent()) {
                dt.addDiff(new _leftOnly_extends(path.in(Feature.EXTENDS_TYPES), (_type) _leftParent, (_type) _rightParent, cit));
            }
        }
        for (int i = 0; i < right.size(); i++) {
            ClassOrInterfaceType cit = right.get(i);
            if (!left.stream().filter(c -> Types.equal(c, cit)).findFirst().isPresent()) {
                dt.addDiff(new _rightOnly_extends(path.in(Feature.EXTENDS_TYPES), (_type) _leftParent, (_type) _rightParent, cit));
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
            ((_withExtends) leftParent).removeExtends(right);
            ((_withExtends) rightParent).removeExtends(right);
        }

        @Override
        public void patchRightToLeft() {
            ((_withExtends) leftParent).removeExtends(right);
            ((_withExtends) leftParent).addExtend(right);
            ((_withExtends) rightParent).removeExtends(right);
            ((_withExtends) rightParent).addExtend(right);
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
            ((_withExtends) leftParent).removeExtends(left);
            ((_withExtends) leftParent).addExtend(left);
            ((_withExtends) rightParent).removeExtends(left);
            ((_withExtends) rightParent).addExtend(left);
        }

        @Override
        public void patchRightToLeft() {
            ((_withExtends) leftParent).removeExtends(left);
            ((_withExtends) rightParent).removeExtends(left);
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
