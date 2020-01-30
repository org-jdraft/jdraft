package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

public class _typeRefDiff
    implements _differ<_typeRef, _node> {
    
    public static final _typeRefDiff INSTANCE = new _typeRefDiff();

    @Override
    public <_PN extends _node> _diff diff(_nodePath path, _build dt, _PN _leftParent, _PN _rightParent, _typeRef left, _typeRef right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _change_type(path.in(_java.Component.TYPE), (_namedType) _leftParent, (_namedType) _rightParent));
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
        _namedType leftParent;
        _namedType rightParent;
        _typeRef leftType;
        _typeRef rightType;
        
        public _change_type(_nodePath _p, _namedType leftParent, _namedType rightParent){
            this.path = _p;
            this.leftParent = leftParent;
            this.leftType = leftParent.getType().copy();
            this.rightParent = rightParent;
            this.rightType = rightParent.getType().copy();
        }
        
        @Override
        public void patchLeftToRight(){
            leftParent.type(leftType.copy());
            rightParent.type(leftType.copy());
        }
        
        @Override
        public void patchRightToLeft(){
            leftParent.type(rightType.copy());
            rightParent.type(rightType.copy());
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
        public _mrJava leftParent() {
            return leftParent;
        }

        @Override
        public _mrJava rightParent() {
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
