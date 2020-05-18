package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

public final class _typeRefDiff
    implements _differ<_typeRef, _java._multiPart> {
    
    public static final _typeRefDiff INSTANCE = new _typeRefDiff();

    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _typeRef left, _typeRef right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _change_type(path.in(_java.Feature.TYPE), (_java._withNameTypeRef) _leftParent, (_java._withNameTypeRef) _rightParent));
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
        _java._withNameTypeRef leftParent;
        _java._withNameTypeRef rightParent;
        _typeRef leftType;
        _typeRef rightType;
        
        public _change_type(_nodePath _p, _java._withNameTypeRef leftParent, _java._withNameTypeRef rightParent){
            this.path = _p;
            this.leftParent = leftParent;
            this.leftType = leftParent.getTypeRef().copy();
            this.rightParent = rightParent;
            this.rightType = rightParent.getTypeRef().copy();
        }
        
        @Override
        public void patchLeftToRight(){
            leftParent.setTypeRef(leftType.copy());
            rightParent.setTypeRef(leftType.copy());
        }
        
        @Override
        public void patchRightToLeft(){
            leftParent.setTypeRef(rightType.copy());
            rightParent.setTypeRef(rightType.copy());
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
