package org.jdraft.diff;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;

import org.jdraft.*;
import org.jdraft._body._hasBody;
import org.jdraft.diff._diff.*;
import name.fraser.neil.plaintext.diff_match_patch;


/**
 *
 * @author Eric
 */
public class _bodyDiff
        implements _differ<_body, _node> {

    public static final _bodyDiff INSTANCE = new _bodyDiff();
    
    private static final diff_match_patch BODY_TEXT_DIFF = new diff_match_patch();

    public boolean equivalent(_body left, _body right) {
        return Objects.equals(left, right);
    }

    public _diff diff( _hasBody left, _hasBody right){
        return diff( _path.of(), 
                new _diff._mutableDiffList(), 
                (_node)left, 
                (_node)right, 
                left.getBody(), 
                right.getBody());
    }

    @Override
    public <R extends _node> _diff diff(_path path, _build ds, R leftRoot, R rightRoot, _body left, _body right) {
        if (left == right) {
            return (_diff) ds;
        }
        String leftSer = left.toString(Ast.PRINT_NO_COMMENTS);
        String rightSer = right.toString(Ast.PRINT_NO_COMMENTS);

        if (!Objects.equals(leftSer, rightSer)) {
            //ok. we know at least one diff (other than comments) are in the text
            // lets diff the originals WITH comments

            // NOTE: we treat code DIFFERENTLY than other objects that are diffedbecause 
            // diffs in code can cross object boundaries and it's not as "clean"
            // as simply having members with properties because of the nature of code
            // instead we have a _textDiff which encapsulates the apparent changes
            // the text has to undergo to get from LEFT, to RIGHT and incorporates               
            // the Edit Distance between two bodies of text
            LinkedList<diff_match_patch.Diff> diffs = BODY_TEXT_DIFF.diff_main(left.toString(), right.toString());

            //_path path, _hasBody _leftRoot, _hasBody _rightRoot, LinkedList<Diff> diffs ){
            ds.addDiff(new _bodyEditNode(path.in(_java.Component.BODY), (_hasBody) leftRoot, (_hasBody) rightRoot, diffs));

            //dt.addEdit(path.in(_java.Component.BODY), diffs, left, right);
            //_textDiff td = new _textDiff(diffs);
            //dt.add(path.in(_java.Component.BODY), td, td);                
        }
        return (_diff) ds;
    }
    
    
    public static class _bodyEditNode implements _diffNode<_hasBody>, _diffNode._edit{
        final _hasBody _leftRoot;
        final _hasBody _rightRoot;
        final LinkedList<diff_match_patch.Diff> diffs;
        final _path path;
        
        public _bodyEditNode( _path path, _hasBody _leftRoot, _hasBody _rightRoot, LinkedList<diff_match_patch.Diff> diffs ){
            this._leftRoot = _leftRoot;
            this._rightRoot = _rightRoot;
            this.path = path;
            this.diffs = diffs;
        }
        
         @Override
        public _hasBody leftRoot(){
            return _leftRoot;
        }
        
        @Override
        public _hasBody rightRoot(){
            return _rightRoot;
        }
        
        @Override
        public _path path(){
            return path;
        }
        
        @Override
        public LinkedList<diff_match_patch.Diff> listDiffs(){
            return this.diffs;
        }
        
        @Override
        public String toString(){
            return "  E " + path.toString() + System.lineSeparator();
        }
        
        
        @Override
        public _bodyEditNode forEach( Consumer<diff_match_patch.Diff> diffActionFn ){
            listDiffs().forEach(diffActionFn);
            return this;
        }
        
        @Override
        public void patchLeftToRight(){
            StringBuilder sb = new StringBuilder();
            forEach( d -> {
                //DELETE, INSERT, EQUAL
                if( d.operation == diff_match_patch.Operation.EQUAL || d.operation == diff_match_patch.Operation.DELETE ){
                    sb.append(d.text);
                }
            });
            this.leftRoot().setBody(sb.toString());
            this.rightRoot().setBody(sb.toString());
        }
        
        @Override
        public void patchRightToLeft(){
            StringBuilder sb = new StringBuilder();
            forEach( d -> {
                //DELETE, INSERT, EQUAL
                if( d.operation == diff_match_patch.Operation.EQUAL || d.operation == diff_match_patch.Operation.INSERT ){
                    sb.append(d.text);
                }
            });
            this.leftRoot().setBody(sb.toString());
            this.rightRoot().setBody(sb.toString());
        }
        
        /**
         * For all instances where the text on the right needs to be added to 
         * the text on the left
         * @param addActionFn
         * @return 
         */
        @Override
        public _bodyEditNode forAdds( Consumer<diff_match_patch.Diff> addActionFn ){
            listDiffs().stream().filter(d -> d.operation == diff_match_patch.Operation.INSERT).forEach(addActionFn);
            return this;
        }
        
        /**
         * For all instances where the text on the left is to be removed to 
         * create the text on the right
         * @param removeActionFn
         * @return 
         */
        @Override
        public _bodyEditNode forRemoves( Consumer<diff_match_patch.Diff> removeActionFn ){
            listDiffs().stream().filter(d -> d.operation == diff_match_patch.Operation.DELETE).forEach(removeActionFn);
            return this;
        }
        
        /**
         * For all instances where the text is the same between left and right
         * @param retainActionFn
         * @return 
         */
        @Override
        public _bodyEditNode forRetains( Consumer<diff_match_patch.Diff> retainActionFn ){
            listDiffs().stream().filter(d -> d.operation == diff_match_patch.Operation.EQUAL).forEach(retainActionFn);
            return this;
        }
    }    
}