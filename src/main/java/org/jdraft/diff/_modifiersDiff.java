package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;

import org.jdraft.*;
import org.jdraft._modifiers._hasModifiers;

import org.jdraft.diff._diff.*;

/**
 *
 * @author Eric
 */
public class _modifiersDiff implements
        _differ<NodeList<Modifier>, _node> {

    public static final _modifiersDiff INSTANCE = new _modifiersDiff();
    
    public boolean equivalent(_modifiers left, _modifiers right) {
        return Objects.equals(left, right);
    }

    public _diff diff( _hasModifiers leftParent, _hasModifiers rightParent){
        return diff( _path.of(), 
                new _diffList((_node)leftParent, (_node)rightParent),
                (_node)leftParent,
                (_node)rightParent,
                leftParent.getEffectiveModifiers(),
                rightParent.getEffectiveModifiers());
    }
    
    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, NodeList<Modifier> left, NodeList<Modifier> right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _changeModifiers(path.in(_java.Component.MODIFIERS), (_modifiers._hasModifiers) _leftParent, (_modifiers._hasModifiers) _rightParent));
        }
        return dt;
    }

    public static class _changeModifiers
            implements _diffNode<_modifiers._hasModifiers>, _diffNode._change<NodeList<Modifier>> {

        public _path path;
        public _modifiers._hasModifiers leftParent;
        public _modifiers._hasModifiers rightParent;
        public NodeList<Modifier> left;
        public NodeList<Modifier> right;

        public _changeModifiers(_path path, _modifiers._hasModifiers leftParent, _modifiers._hasModifiers rightParent) {
            this.path = path;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.left = leftParent.getEffectiveModifiers();
            this.right = rightParent.getEffectiveModifiers();
        }

        @Override
        public _modifiers._hasModifiers leftParent() {
            return leftParent;
        }

        @Override
        public _modifiers._hasModifiers rightParent() {
            return rightParent;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public NodeList<Modifier> left() {
            return left;
        }

        @Override
        public NodeList<Modifier> right() {
            return right;
        }

        @Override
        public void patchLeftToRight() {
            leftParent.modifiers(left);
            rightParent.modifiers(left);
        }

        @Override
        public void patchRightToLeft() {
            leftParent.modifiers(right);
            rightParent.modifiers(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
