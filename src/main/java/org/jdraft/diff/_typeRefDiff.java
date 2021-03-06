package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

public final class _typeRefDiff
    implements _differ<_typeRef, _tree._node> {
    
    public static final _typeRefDiff INSTANCE = new _typeRefDiff();

    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _typeRef left, _typeRef right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _change_type(path.in(Feature.TYPE), (_java._withNameType) _leftParent, (_java._withNameType) _rightParent));
        }
        return  dt;
    }
    
    /**
     * Both signifies a delta and provides a means to 
     * commit (via right()) 
     * or rollback( via left())
     */
    public static class _change_type 
            implements _diffNode, _diffNode._change<_typeRef>{
        _nodePath path;
        _java._withNameType leftParent;
        _java._withNameType rightParent;
        _typeRef leftType;
        _typeRef rightType;
        
        public _change_type(_nodePath _p, _java._withNameType leftParent, _java._withNameType rightParent){
            this.path = _p;
            this.leftParent = leftParent;
            this.leftType = leftParent.getType().copy();
            this.rightParent = rightParent;
            this.rightType = rightParent.getType().copy();
        }
        
        @Override
        public void patchLeftToRight(){
            leftParent.setType(leftType.copy());
            rightParent.setType(leftType.copy());
        }
        
        @Override
        public void patchRightToLeft(){
            leftParent.setType(rightType.copy());
            rightParent.setType(rightType.copy());
        }
        
        @Override
        public _typeRef left(){
            return leftType;
        }
        
        @Override
        public _typeRef right(){
            return rightType;
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
