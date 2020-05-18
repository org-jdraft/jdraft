package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;
import org.jdraft._java.Feature;

import org.jdraft.diff._diff.*;

/**
 * Differ for a {@link _java._withName}
 */
public final class _namedDiff implements _differ<String, _java._multiPart> {

    public static final _namedDiff INSTANCE = new _namedDiff();
    
    public Feature feature = Feature.NAME;

    public _namedDiff() {
        this(Feature.NAME);
    }
    
    public _namedDiff(Feature feature) {
        this.feature = feature;
    }

    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, String left, String right) {
        if (!Objects.equals(left, right)) {
            return dt.addDiff(new _changeName(path.in(feature), (_java._withName) _leftParent, (_java._withName) _rightParent));
        }
        return dt;
    }
    
    public static class _changeName implements _diffNode, _diffNode._change<String> {
        _java._withName leftParent;
        _java._withName rightParent;
        String leftName;
        String rightName;
        _nodePath path;
        
         public _changeName(_nodePath _p, _java._withName leftParent, _java._withName rightParent){
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
