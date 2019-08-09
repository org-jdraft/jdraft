package org.jdraft.diff;

import java.util.*;
import com.github.javaparser.ast.ImportDeclaration;

import org.jdraft.*;
import org.jdraft._import._imports;
import org.jdraft.diff._diff.*;

public class _importsDiff
        implements _differ<List<ImportDeclaration>, _node> {

    public static final _importsDiff INSTANCE = new _importsDiff();
    
    public boolean equivalent(List<ImportDeclaration> left, List<ImportDeclaration> right) {
        Set<ImportDeclaration> ls = new HashSet<>();
        Set<ImportDeclaration> rs = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);
        return Objects.equals(ls, rs);
    }

    public _diff diff( _type left, _type right){
        return diff( 
                _path.of(), 
                new _diffList(left, right),
                left,
                right, 
                left.astCompilationUnit().getImports(), 
                right.astCompilationUnit().getImports());
    }
    
    public _diff diff( _imports left, _imports right ){
        
        return diff( _path.of(), new _diffList(null, null),
                _java.type(left.astCompilationUnit), _java.type(right.astCompilationUnit), left.astCompilationUnit.getImports(), right.astCompilationUnit.getImports());
    }
    
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN leftParent, _PN rightParent, _type left, _type right) {
        if( left.isTopLevel() && right.isTopLevel() ){
            return diff( path, dt, leftParent, rightParent, left.astCompilationUnit().getImports(), right.astCompilationUnit().getImports() );
        }
        if( !left.isTopLevel() && !right.isTopLevel() ){
            return dt;
        }
        if( !left.isTopLevel() ){
            right.getImports().forEach( i -> dt.addDiff(new _rightOnly_import(
                path.in(_java.Component.IMPORT),
                (_type) leftParent,
                (_type) rightParent,
                i.astId )) );
            return dt;
        } 
        
        left.getImports().forEach( i -> dt.addDiff(new _leftOnly_import(
                path.in(_java.Component.IMPORT),
                (_type) leftParent,
                (_type) rightParent,
                i.astId )) );
            return dt;    
    }
    
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN leftParent, _PN rightParent, _imports left, _imports right) {
        return diff( path, dt, leftParent, rightParent, left.astCompilationUnit.getImports(), right.astCompilationUnit.getImports() );
    }
    
    @Override
    public <_PN extends _node> _diff diff(_path path, _build dt, _PN _leftParent, _PN _rightParent, List<ImportDeclaration> left, List<ImportDeclaration> right) {
        Set<ImportDeclaration> ls = new HashSet<>();
        Set<ImportDeclaration> rs = new HashSet<>();
        Set<ImportDeclaration> both = new HashSet<>();
        ls.addAll(left);
        rs.addAll(right);

        both.addAll(left);
        both.retainAll(right);

        ls.removeAll(both);
        rs.removeAll(both);

        ls.forEach(c -> dt.addDiff(new _leftOnly_import(
                path.in(_java.Component.IMPORT),
                (_type) _leftParent,
                (_type) _rightParent, c)));

        rs.forEach(c -> dt.addDiff(new _rightOnly_import(
                path.in(_java.Component.IMPORT),
                (_type) _leftParent,
                (_type) _rightParent, c)));

        return (_diff) dt;
    }

    public static class _rightOnly_import implements _diffNode<_type>, _diffNode._rightOnly<ImportDeclaration> {

        public _path path;
        public _type leftRoot;
        public _type rightRoot;
        public ImportDeclaration right;

        public _rightOnly_import(_path path, _type leftRoot, _type rightRoot, ImportDeclaration right) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.right = Ast.importDeclaration(right.toString());
        }

        @Override
        public _type leftParent() {
            return leftRoot;
        }

        @Override
        public _type rightParent() {
            return rightRoot;
        }

        @Override
        public void patchLeftToRight() {
            leftRoot.removeImports(right);
            rightRoot.removeImports(right);
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.imports(right);
            rightRoot.imports(right);
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public ImportDeclaration right() {
            return right;
        }

        @Override
        public String toString() {
            return "   + " + path;
        }

    }

    public static class _leftOnly_import implements _diffNode<_type>, _diffNode._leftOnly<ImportDeclaration> {

        public _path path;
        public _type leftRoot;
        public _type rightRoot;
        public ImportDeclaration left;

        public _leftOnly_import(_path path, _type leftRoot, _type rightRoot, ImportDeclaration left) {
            this.path = path;
            this.leftRoot = leftRoot;
            this.rightRoot = rightRoot;
            this.left = Ast.importDeclaration(left.toString());
        }

        @Override
        public _type leftParent() {
            return leftRoot;
        }

        @Override
        public _type rightParent() {
            return rightRoot;
        }

        @Override
        public void patchLeftToRight() {
            leftRoot.imports(left);
            rightRoot.imports(left);
        }

        @Override
        public void patchRightToLeft() {
            leftRoot.removeImports(left);
            rightRoot.removeImports(left);
        }

        @Override
        public _path path() {
            return path;
        }

        @Override
        public ImportDeclaration left() {
            return left;
        }

        @Override
        public String toString() {
            return "   - " + path;
        }
    }
}
