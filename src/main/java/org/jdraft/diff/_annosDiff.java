package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.jdraft.*;
import org.jdraft._anno._hasAnnos;
import org.jdraft._java.Component;
import org.jdraft.diff._diff.*;

/**
 * Diff for: {@link org.jdraft._anno} {@link org.jdraft._anno._annos} and {@link org.jdraft._anno._hasAnnos}
 *
 * @author Eric
 */
public class _annosDiff
    implements _differ<_anno._annos, _node> {

    public static final _annosDiff INSTANCE = new _annosDiff();
    
     public _diff diff( _hasAnnos left, _hasAnnos right){
        return diff( 
                _path.of(), 
                new _diffList( (_node)left, (_node)right),
                (_node)left, 
                (_node)right, 
                left.getAnnos(), 
                right.getAnnos());
    }
     
    @Override
    public <_PN extends _node> _diff diff(_path path, _build ds, _PN _leftParent, _PN _rightParent, _anno._annos left, _anno._annos right) {
        NodeList<AnnotationExpr> laes = left.astAnnNode.getAnnotations();
        NodeList<AnnotationExpr> raes = right.astAnnNode.getAnnotations();
        for (int i = 0; i < laes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) laes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!raes.stream().filter(a -> Expr.equivalent(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!raes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                
                ds.addDiff(new _leftOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_anno._hasAnnos) _leftParent, (_anno._hasAnnos) _rightParent, _anno.of(e)));
            }
        }
        for (int i = 0; i < raes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) raes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!laes.stream().filter(a -> Expr.equivalent(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!laes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                ds.addDiff(new _rightOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_anno._hasAnnos) _leftParent, (_anno._hasAnnos) _rightParent, _anno.of(e)));
            }
        }
        return ds;
    }

    public static class _leftOnly_anno
            implements _diffNode<_anno._hasAnnos>, _diffNode._leftOnly<_anno> {

        public _path path;
        public _anno._hasAnnos leftRoot;
        public _anno._hasAnnos rightRoot;
        public _anno left;

        public _leftOnly_anno(_path p, _anno._hasAnnos left, _anno._hasAnnos right, _anno toRemove) {
            this.path = p;
            this.leftRoot = left;
            this.rightRoot = right;
            this.left = toRemove.copy();
        }

        @Override
        public _anno._hasAnnos leftParent() {
            return leftRoot;
        }

        @Override
        public _anno._hasAnnos rightParent() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _anno left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.removeAnno(left.ast());
            rightRoot.removeAnno(left.ast());
        }

        @Override
        public void patchLeftToRight() {
            
            leftRoot.removeAnno(left.ast());
            leftRoot.annotate(left);
            rightRoot.removeAnno(left.ast());
            rightRoot.annotate(left);
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_anno implements _diffNode<_anno._hasAnnos>, _diffNode._rightOnly<_anno> {

        public _path path;
        public _anno._hasAnnos leftRoot;
        public _anno._hasAnnos rightRoot;
        public _anno right;

        public _rightOnly_anno(_path p, _anno._hasAnnos leftRoot, _anno._hasAnnos rightRoot, _anno right) {
            this.path = p;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.right = right.copy();
        }

        @Override
        public _anno._hasAnnos leftParent() {
            return leftRoot;
        }

        @Override
        public _anno._hasAnnos rightParent() {
            return rightRoot;
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public _anno right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            //remove it before just so we dont mistakenly add it twice
            leftRoot.removeAnno(right.ast());
            leftRoot.annotate(right);
            rightRoot.removeAnno(right.ast());
            rightRoot.annotate(right);
        }

        @Override
        public void patchLeftToRight() {
            //remove it before just so we dont mistakenly add it twice
            leftRoot.removeAnno(right.ast());
            rightRoot.removeAnno(right.ast());
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}
