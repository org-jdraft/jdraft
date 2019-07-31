package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;
import org.jdraft._java.Component;

import org.jdraft.diff._diff.*;

/**
 * Inspect for a Named
 */
public class _namedDiff implements _differ<String, _node> {

    public static final _namedDiff INSTANCE = new _namedDiff();
    
    public Component component = Component.NAME;

    public _namedDiff() {
        this(Component.NAME);
    }
    
    public _namedDiff(Component component) {
        this.component = component;
    }

    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, String left, String right) {
        if (!Objects.equals(left, right)) {
            return (_diff) dt.addDiff(new _changeName(path.in(component), (_named) leftRoot, (_named) rightRoot));
        }
        return (_diff) dt;
    }
    
    public static class _changeName implements _diffNode, _diffNode._change<String> {
        _named left;
        _named right;
        String leftName;
        String rightName;
        _path path;
        
         public _changeName(_path _p, _named left, _named right ){
            this.path = _p;
            this.left = left;
            this.leftName = left.getName();
            this.right = right;
            this.rightName = right.getName();            
        }
         
        @Override
        public void patchLeftToRight(){
            left.name(leftName);
            right.name(leftName);
        }
        
        @Override
        public void patchRightToLeft(){
            left.name(rightName);
            right.name(rightName);
        }
        
        @Override
        public String left(){
            return leftName;
        }
        
        @Override
        public String right(){
            return rightName;
        }
        
        
        @Override
        public _java leftRoot() {
            return left;
        }

        @Override
        public _java rightRoot() {
            return right;
        }

        @Override
        public _path path() {
            return path;
        }
        
        @Override
        public String toString(){
            return "   ~ "+path;
        }
    } 
}