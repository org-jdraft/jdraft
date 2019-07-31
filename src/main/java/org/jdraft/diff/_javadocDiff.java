package org.jdraft.diff;

import java.util.Objects;

import com.github.javaparser.ast.comments.JavadocComment;

import org.jdraft.*;
import org.jdraft._javadoc._hasJavadoc;

import org.jdraft.diff._diff.*;

public class _javadocDiff implements _differ<_javadoc, _node> {

    public static final _javadocDiff INSTANCE = new _javadocDiff();

    public boolean equivalent(_javadoc left, _javadoc right) {
        return Objects.equals(left, right);
    }

    public _diff diff( _hasJavadoc left, _hasJavadoc right){
        return diff( _path.of(), 
            new _diff._mutableDiffList(), 
            (_node)left, 
            (_node)right, 
            left.getJavadoc(), 
            right.getJavadoc());
    }

    @Override
    public <R extends _node> _diff diff(_path path, _build dt, R leftRoot, R rightRoot, _javadoc left, _javadoc right) {
        if (!equivalent(left, right)) {
            dt.addDiff(new _changeJavadoc(path.in(_java.Component.JAVADOC), (_javadoc._hasJavadoc) leftRoot, (_javadoc._hasJavadoc) rightRoot));
        }
        return (_diff) dt;
    }

    /**
     * Both signifies a delta and provides a means to commit (via right()) or
     * rollback( via left())
     */
    public static class _changeJavadoc
            implements _diffNode, _diffNode._change<JavadocComment> {

        public _path path;
        public _javadoc._hasJavadoc left;
        public _javadoc._hasJavadoc right;
        public JavadocComment leftJavadoc;
        public JavadocComment rightJavadoc;

        public _changeJavadoc(_path _p, _javadoc._hasJavadoc left, _javadoc._hasJavadoc right) {
            this.path = _p;
            this.left = left;
            if (left.hasJavadoc()) {
                this.leftJavadoc = left.getJavadoc().ast().clone();
            }
            this.right = right;
            if (right.hasJavadoc()) {
                this.rightJavadoc = right.getJavadoc().ast().clone();
            }
        }

        @Override
        public void patchLeftToRight() {
            if( leftJavadoc != null ){
                System.out.println("Setting on "+ left.getClass() );
                System.out.println("Setting on "+ right.getClass() );
                left.javadoc(leftJavadoc.clone());
                right.javadoc(leftJavadoc.clone());
                System.out.println("LEFT JAVADOC"+left.getJavadoc() );
                System.out.println("RIGHT JAVADOC"+right.getJavadoc() );
                System.out.println( "ARE THEY EQUAL " + left.getJavadoc().equals( right.getJavadoc() )); 
            } else{
                left.removeJavadoc();
                right.removeJavadoc();
            }
        }

        @Override
        public void patchRightToLeft() {
            left.javadoc(rightJavadoc);
            right.javadoc(rightJavadoc);
        }

        @Override
        public JavadocComment left() {
            return leftJavadoc;
        }

        @Override
        public JavadocComment right() {
            return rightJavadoc;
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
        public String toString() {
            return "   ~ " + path;
        }
    }
}
