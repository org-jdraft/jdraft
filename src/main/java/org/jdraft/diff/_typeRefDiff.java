package org.jdraft.diff;

import java.util.Objects;

import org.jdraft.*;

import org.jdraft.diff._diff.*;

public class _typeRefDiff
    implements _differ<_typeRef, _node> {
    
    public static final _typeRefDiff INSTANCE = new _typeRefDiff();

    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _typeRef left, _typeRef right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _change_type(path.in(_java.Component.TYPE), (_namedType) leftRoot, (_namedType) rightRoot));
        }
        return (_diff) dt;
    }
    
    /**
     * Both signifies a delta and provides a means to 
     * commit (via right()) 
     * or rollback( via left())
     */
    public static class _change_type 
            implements _diffNode, _diffNode._change<_typeRef>{
        _path path;
        _namedType left;
        _namedType right;
        _typeRef leftType;
        _typeRef rightType;
        
        public _change_type(_path _p, _namedType left, _namedType right ){
            this.path = _p;
            this.left = left;
            this.leftType = left.getType().copy();
            this.right = right;
            this.rightType = right.getType().copy();            
        }
        
        @Override
        public void patchLeftToRight(){
            left.type(leftType.copy());
            right.type(leftType.copy());
        }
        
        @Override
        public void patchRightToLeft(){
            left.type(rightType.copy());
            right.type(rightType.copy());
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
