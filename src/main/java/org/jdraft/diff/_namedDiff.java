package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;
import org.jdraft._java.Component;

import org.jdraft.diff._diff.*;

/**
 * Differ for a {@link _named}
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
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, String left, String right) {
        if (!Objects.equals(left, right)) {
            return dt.addDiff(new _changeName(path.in(component), (_named) _leftParent, (_named) _rightParent));
        }
        return dt;
    }
    
    public static class _changeName implements _diffNode, _diffNode._change<String> {
        _named leftParent;
        _named rightParent;
        String leftName;
        String rightName;
        _path path;
        
         public _changeName(_path _p, _named leftParent, _named rightParent){
            this.path = _p;
            this.leftParent = leftParent;
            this.leftName = leftParent.getName();
            this.rightParent = rightParent;
            this.rightName = rightParent.getName();
        }
         
        @Override
        public void patchLeftToRight(){
            leftParent.name(leftName);
            rightParent.name(leftName);
        }
        
        @Override
        public void patchRightToLeft(){
            leftParent.name(rightName);
            rightParent.name(rightName);
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
        public _meta_model leftParent() {
            return leftParent;
        }

        @Override
        public _meta_model rightParent() {
            return rightParent;
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
