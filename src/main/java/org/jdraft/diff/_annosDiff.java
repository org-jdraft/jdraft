package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.jdraft.*;
import org.jdraft._annos._withAnnoExprs;
import org.jdraft.diff._diff.*;

/**
 * Diff for: {@link _anno} {@link _annos} and {@link _withAnnoExprs}
 *
 * @author Eric
 */
public final class _annosDiff
    implements _differ<_annos, _tree._node> {

    public static final _annosDiff INSTANCE = new _annosDiff();
    
     public _diff diff(_withAnnoExprs left, _withAnnoExprs right){
        return diff( 
                _nodePath.of(),
                new _diffList( (_tree._node)left, (_tree._node)right),
                (_tree._node)left,
                (_tree._node)right,
                left.getAnnoExprs(),
                right.getAnnoExprs());
    }
     
    @Override
    public <_PN extends _tree._node> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annos left, _annos right) {
        NodeList<AnnotationExpr> laes = left.astAnnNode.getAnnotations();
        NodeList<AnnotationExpr> raes = right.astAnnNode.getAnnotations();
        for (int i = 0; i < laes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) laes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!raes.stream().filter(a -> Expr.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!raes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                
                ds.addDiff(new _leftOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Feature.ANNO_EXPR, e.getNameAsString()), (_withAnnoExprs) _leftParent, (_withAnnoExprs) _rightParent, _anno.of(e)));
            }
        }
        for (int i = 0; i < raes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) raes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!laes.stream().filter(a -> Expr.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!laes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                ds.addDiff(new _rightOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Feature.ANNO_EXPR, e.getNameAsString()), (_withAnnoExprs) _leftParent, (_withAnnoExprs) _rightParent, _anno.of(e)));
            }
        }
        return ds;
    }

    public static class _leftOnly_anno
            implements _diffNode<_withAnnoExprs>, _diffNode._leftOnly<_anno> {

        public _nodePath path;
        public _withAnnoExprs leftParent;
        public _withAnnoExprs rightParent;
        public _anno left;

        public _leftOnly_anno(_nodePath p, _withAnnoExprs left, _withAnnoExprs right, _anno toRemove) {
            this.path = p;
            this.leftParent = left;
            this.rightParent = right;
            this.left = toRemove.copy();
        }

        @Override
        public _withAnnoExprs leftParent() {
            return leftParent;
        }

        @Override
        public _withAnnoExprs rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _anno left() {
            return left;
        }

        @Override
        public void patchRightToLeft() {
            leftParent.removeAnnoExpr(left.ast());
            rightParent.removeAnnoExpr(left.ast());
        }

        @Override
        public void patchLeftToRight() {
            
            leftParent.removeAnnoExpr(left.ast());
            leftParent.addAnnoExprs(left);
            rightParent.removeAnnoExpr(left.ast());
            rightParent.addAnnoExprs(left);
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }

    public static class _rightOnly_anno implements _diffNode<_withAnnoExprs>, _diffNode._rightOnly<_anno> {

        public _nodePath path;
        public _withAnnoExprs leftParent;
        public _withAnnoExprs rightParent;
        public _anno right;

        public _rightOnly_anno(_nodePath p, _withAnnoExprs leftParent, _withAnnoExprs rightParent, _anno right) {
            this.path = p;
            this.leftParent = leftParent;
            this.rightParent = rightParent;
            this.right = right.copy();
        }

        @Override
        public _withAnnoExprs leftParent() {
            return leftParent;
        }

        @Override
        public _withAnnoExprs rightParent() {
            return rightParent;
        }

        @Override
        public _nodePath path() {
            return path;
        }

        @Override
        public _anno right() {
            return right;
        }

        @Override
        public void patchRightToLeft() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeAnnoExpr(right.ast());
            leftParent.addAnnoExprs(right);
            rightParent.removeAnnoExpr(right.ast());
            rightParent.addAnnoExprs(right);
        }

        @Override
        public void patchLeftToRight() {
            //remove it before just so we dont mistakenly add it twice
            leftParent.removeAnnoExpr(right.ast());
            rightParent.removeAnnoExpr(right.ast());
        }

        @Override
        public String toString() {
            return "   + " + path;
        }
    }
}
