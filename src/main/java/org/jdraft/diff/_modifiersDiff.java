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

    public _diff diff( _hasModifiers left, _hasModifiers right){
        return diff( _path.of(), 
                new _diff._mutableDiffList(), 
                (_node)left, 
                (_node)right, 
                left.getEffectiveModifiers(), 
                right.getEffectiveModifiers());
    }
    
    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, NodeList<Modifier> left, NodeList<Modifier> right) {
        if (!Objects.equals(left, right)) {
            dt.addDiff(new _changeModifiers(path.in(_java.Component.MODIFIERS), (_modifiers._hasModifiers) leftRoot, (_modifiers._hasModifiers) rightRoot));
        }
        return (_diff) dt;
    }

    public static class _changeModifiers
            implements _diffNode<_modifiers._hasModifiers>, _diffNode._change<NodeList<Modifier>> {

        public _path path;
        public _modifiers._hasModifiers leftRoot;
        public _modifiers._hasModifiers rightRoot;
        public NodeList<Modifier> left;
        public NodeList<Modifier> right;

        public _changeModifiers(_path path, _modifiers._hasModifiers leftRoot, _modifiers._hasModifiers rightRoot) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = leftRoot.getEffectiveModifiers();
            this.right = rightRoot.getEffectiveModifiers();
        }

        @Override
        public _modifiers._hasModifiers leftRoot() {
            return leftRoot;
        }

        @Override
        public _modifiers._hasModifiers rightRoot() {
            return rightRoot;
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
            leftRoot.modifiers(left);
            rightRoot.modifiers(left);
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.modifiers(right);
            rightRoot.modifiers(right);
        }

        @Override
        public String toString() {
            return "   ~ " + path;
        }
    }
}
