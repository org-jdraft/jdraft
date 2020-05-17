package org.jdraft.diff;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;

import org.jdraft.*;
import org.jdraft._annoExprs._withAnnoExprs;
import org.jdraft._java.Component;
import org.jdraft.diff._diff.*;

/**
 * Diff for: {@link _annoExpr} {@link _annoExprs} and {@link _withAnnoExprs}
 *
 * @author Eric
 */
public final class _annoExprsDiff
    implements _differ<_annoExprs, _java._multiPart> {

    public static final _annoExprsDiff INSTANCE = new _annoExprsDiff();
    
     public _diff diff(_withAnnoExprs left, _withAnnoExprs right){
        return diff( 
                _nodePath.of(),
                new _diffList( (_java._multiPart)left, (_java._multiPart)right),
                (_java._multiPart)left,
                (_java._multiPart)right,
                left.getAnnoExprs(),
                right.getAnnoExprs());
    }
     
    @Override
    public <_PN extends _java._multiPart> _diff diff(_nodePath path, _build ds, _PN _leftParent, _PN _rightParent, _annoExprs left, _annoExprs right) {
        NodeList<AnnotationExpr> laes = left.astAnnNode.getAnnotations();
        NodeList<AnnotationExpr> raes = right.astAnnNode.getAnnotations();
        for (int i = 0; i < laes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) laes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!raes.stream().filter(a -> Exprs.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!raes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                
                ds.addDiff(new _leftOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_withAnnoExprs) _leftParent, (_withAnnoExprs) _rightParent, _annoExpr.of(e)));
            }
        }
        for (int i = 0; i < raes.size(); i++) {
            AnnotationExpr e = (AnnotationExpr) raes.get(i);
            //find a matching annotation in other, if one isnt found, then not equal
            
            if (!laes.stream().filter(a -> Exprs.equal(e, (AnnotationExpr) a)).findFirst().isPresent()) {
            //if (!laes.stream().filter(a -> Ast.annotationEqual(e, (AnnotationExpr) a)).findFirst().isPresent()) {
                ds.addDiff(new _rightOnly_anno( //in LEFT not in RIGHT means REMOVE
                        path.in(Component.ANNO, e.getNameAsString()), (_withAnnoExprs) _leftParent, (_withAnnoExprs) _rightParent, _annoExpr.of(e)));
            }
        }
        return ds;
    }

    public static class _leftOnly_anno
            implements _diffNode<_withAnnoExprs>, _diffNode._leftOnly<_annoExpr> {

        public _nodePath path;
        public _withAnnoExprs leftParent;
        public _withAnnoExprs rightParent;
        public _annoExpr left;

        public _leftOnly_anno(_nodePath p, _withAnnoExprs left, _withAnnoExprs right, _annoExpr toRemove) {
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
        public _annoExpr left() {
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

    public static class _rightOnly_anno implements _diffNode<_withAnnoExprs>, _diffNode._rightOnly<_annoExpr> {

        public _nodePath path;
        public _withAnnoExprs leftParent;
        public _withAnnoExprs rightParent;
        public _annoExpr right;

        public _rightOnly_anno(_nodePath p, _withAnnoExprs leftParent, _withAnnoExprs rightParent, _annoExpr right) {
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
        public _annoExpr right() {
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
