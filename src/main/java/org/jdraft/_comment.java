package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @see _javadoc for a javadoc attributed to a AST node (i.e. must start with / * * end with * / AND
 * be
 */
public interface _comment {

    /**
     * list all comments within this astRootNode (including the comment applied
     * to the astRootNode if the AstRootNode is an instance of {@link NodeWithJavadoc}
     *
     * @param astRootNode the root node to look through
     * @return a list of all comments on or underneath the node
     */
    static List<Comment> listComments(Node astRootNode) {
        return Ast.listComments(astRootNode);
    }

    /**
     * @param <C>                the comment class
     * @param astRootNode        the root node to start the search
     * @param commentTargetClass the TYPE of comment ({@link Comment},
     *                           {@link LineComment}, {@link JavadocComment}, {@link BlockComment})
     * @param commentMatchFn     predicate for selecting comments
     * @return a list of matching comments
     */
    static <C extends Comment> List<C> listComments(
            Node astRootNode, Class<C> commentTargetClass, Predicate<C> commentMatchFn) {
        return Ast.listComments(astRootNode, commentTargetClass, commentMatchFn);
    }

    /**
     * list all comments within this astRootNode that match the predicate
     * (including the comment applied to the astRootNode if the AstRootNode is
     * an instance of {@link NodeWithJavadoc})
     *
     * @param astRootNode    the root node to look through
     * @param commentMatchFn matching function for comments
     * @return a list of all comments on or underneath the node
     */
    static List<Comment> listComments(Node astRootNode, Predicate<Comment> commentMatchFn) {
        return Ast.listComments(astRootNode, commentMatchFn);
    }

    /**
     *
     * @param <C>
     * @param <_J>
     * @param _j
     * @param commentTargetClass
     * @param commentMatchFn
     * @return
     */
    static <C extends Comment, _J extends _java._domain> List<C> listComments(
            _J _j, Class<C> commentTargetClass, Predicate<C> commentMatchFn){

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Ast.listComments( ((_codeUnit) _j).astCompilationUnit(), commentTargetClass, commentMatchFn );
            }
            else{
                return Ast.listComments( ((_type) _j).ast(), commentTargetClass, commentMatchFn );
            }
        } else{
            return Ast.listComments(  ((_java._multiPart) _j).ast(), commentTargetClass, commentMatchFn);
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @return
     */
    static <_J extends _java._domain> List<Comment> listComments(_J _j, Predicate<Comment> commentMatchFn){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Ast.listComments( ((_codeUnit) _j).astCompilationUnit(), commentMatchFn );
            }
            else{
                return Ast.listComments( ((_type) _j).ast(), commentMatchFn);
            }
        } else{
            return Ast.listComments(  ((_java._multiPart) _j).ast(), commentMatchFn );
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @param commentActionFn
     */
    static <_J extends _java._domain> void forComments(_J _j, Predicate<Comment> commentMatchFn, Consumer<Comment> commentActionFn ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Ast.forComments( ((_codeUnit) _j).astCompilationUnit(), commentMatchFn, commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(), commentMatchFn, commentActionFn);
            }
        } else{
            Ast.forComments(  ((_java._multiPart) _j).ast(), commentMatchFn, commentActionFn );
        }
    }

    /**
     *
     * @param <C>
     * @param <_J>
     * @param _j
     * @param commentClass
     * @param commentMatchFn
     * @param commentActionFn
     */
    static <C extends Comment, _J extends _java._domain> void forComments(_J _j, Class<C> commentClass, Predicate<C> commentMatchFn, Consumer<C> commentActionFn ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Ast.forComments( ((_codeUnit) _j).astCompilationUnit(), commentClass, commentMatchFn, commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(),  commentClass, commentMatchFn, commentActionFn);
            }
        } else{
            Ast.forComments(  ((_java._multiPart) _j).ast(),  commentClass, commentMatchFn, commentActionFn );
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @return
     */
    static <_J extends _java._domain> List<Comment> listComments(_J _j){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Ast.listComments( ((_codeUnit) _j).astCompilationUnit() );
            }
            else{
                return Ast.listComments( ((_type) _j).ast() );
            }
        } else{
            return Ast.listComments(  ((_java._multiPart) _j).ast() );
        }
    }

    /**
     *
     * @param _j
     * @param commentActionFn
     */
    static void forComments(_java._domain _j, Consumer<Comment> commentActionFn){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Ast.forComments( ((_codeUnit) _j).astCompilationUnit(), commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(), commentActionFn);
            }
        } else{
            Ast.forComments(  ((_java._multiPart)_j).ast(), commentActionFn );
        }
    }
}
