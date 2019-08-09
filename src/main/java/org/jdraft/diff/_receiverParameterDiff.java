package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.body.ReceiverParameter;
import org.jdraft.*;
import org.jdraft._receiverParameter._hasReceiverParameter;

import org.jdraft.diff._diff.*;

public class _receiverParameterDiff
        implements  _differ<_receiverParameter, _node> {

    public static final _receiverParameterDiff INSTANCE = new _receiverParameterDiff();
    
    public _diff diff( _hasReceiverParameter left, _hasReceiverParameter right){
        return diff( _path.of(), 
                new _diffList( (_node)left, (_node)right),
                (_node)left, 
                (_node)right, 
                left.getReceiverParameter(), 
                right.getReceiverParameter());
    }
    
    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, _receiverParameter left, _receiverParameter right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new change_receiverParameter(path.in(_java.Component.RECEIVER_PARAMETER), (_receiverParameter._hasReceiverParameter) _leftParent, (_receiverParameter._hasReceiverParameter) _rightParent));
        }
        return (_diff) dt;
    }

    public static class change_receiverParameter 
            implements _diffNode<_receiverParameter._hasReceiverParameter>, _diffNode._change<ReceiverParameter> {

        public _path path;
        public _receiverParameter._hasReceiverParameter leftParent;
        public _receiverParameter._hasReceiverParameter rightParent;
        public ReceiverParameter left;
        public ReceiverParameter right;

        public change_receiverParameter(_path path, _receiverParameter._hasReceiverParameter leftParent, _receiverParameter._hasReceiverParameter rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            if (leftParent.hasReceiverParameter()) {
                this.left = leftParent.getReceiverParameter().astReceiverParam.clone();
            }
            if (rightParent.hasReceiverParameter()) {
                this.right = rightParent.getReceiverParameter().astReceiverParam.clone();
            }
        }

        @Override
        public _receiverParameter._hasReceiverParameter leftParent() {
            return leftParent;
        }

        @Override
        public _receiverParameter._hasReceiverParameter rightParent() {
            return rightParent;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public ReceiverParameter left() {
            return left;
        }

        @Override
        public ReceiverParameter right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.receiverParameter(left);
            rightParent.receiverParameter(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.receiverParameter(right);
            rightParent.receiverParameter(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
