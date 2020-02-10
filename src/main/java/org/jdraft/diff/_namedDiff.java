package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;
import org.jdraft._java.Component;

import org.jdraft.diff._diff.*;

/**
 * Differ for a {@link _java._named}
 */
public class _namedDiff implements _differ<String, _java._compoundNode> {

    public static final _namedDiff INSTANCE = new _namedDiff();
    
    public Component component = Component.NAME;

    public _namedDiff() {
        this(Component.NAME);
    }
    
    public _namedDiff(Component component) {
        this.component = component;
    }

    @Override
    public <_PN extends _java._compoundNode> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, String left, String right) {
        if (!Objects.equals(left, right)) {
            return dt.addDiff(new _changeName(path.in(component), (_java._named) _leftParent, (_java._named) _rightParent));
        }
        return dt;
    }
    
    public static class _changeName implements _diffNode, _diffNode._change<String> {
        _java._named leftParent;
        _java._named rightParent;
        String leftName;
        String rightName;
        _nodePath path;
        
         public _changeName(_nodePath _p, _java._named leftParent, _java._named rightParent){
            this.path = _p;
            this.leftParent = leftParent;
            this.leftName = leftParent.getName();
            this.rightParent = rightParent;
            this.rightName = rightParent.getName();
        }
         
        @Override
        public void patchLeftToRight(){
            leftParent.setName(leftName);
            rightParent.setName(leftName);
        }
        
        @Override
        public void patchRightToLeft(){
            leftParent.setName(rightName);
            rightParent.setName(rightName);
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
        
        @Override
        public String toString(){
            return "   ~ "+path;
        }
    } 
}
