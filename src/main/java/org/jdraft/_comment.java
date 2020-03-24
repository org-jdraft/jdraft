package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import org.jdraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @see _javadoc for a javadoc attributed to a AST node (i.e. must start with / * * end with * / AND
 * be
 */
public interface _comment<C extends Comment, _C extends _comment> extends _java._node<C, _C> {

    static <_C extends _comment> _C of( String... comm ){
        String str = Text.combine(comm);
        if( str.startsWith("//")){
            return (_C)_lineComment.of(str);
        }
        if( str.startsWith("/**") ){
            return (_C)_blockComment.of(str);
        }
        return (_C)_javadocComment.of(str);
    }

    static <_C extends _comment> _C of( Comment comment ){
        if( comment instanceof JavadocComment ){
            return (_C)_javadocComment.of( (JavadocComment)comment);
        }
        if( comment instanceof LineComment ){
            return (_C)_lineComment.of( (LineComment)comment);
        }
        return (_C)_blockComment.of( (BlockComment)comment);
    }

    /**
     * _node that is attributed to this comment
     * i.e.
     *  class C{
     *     void m(){
     *         // print just in case
     *         System.out.println("id "+ id);
     *     }
     * }
     *
     * $comment.of("// print just in case").
     *
     * @return
     */
    _java._domain getCommentedNode();

    /**
     * Is there attribution for this node
     * @return
     */
    default boolean isAttributed(){
        return getCommentedNode() != null;
    }

    /**
     * Gets the contents of the Javadoc as a single String
     * @return
     */
    public String getContents();

    /**
     * Because _blockComments/ _javadocComments can span multiple lines
     *  / *
     *    * contents
     *    * second line
     *    * /
     *  the "actual contents" within the block contents should "really" only be (2) lines :
     *  [0] "contents"
     *  [1] "second line"
     *
     *  (or a single String "contents\nsecond line", because the prefix spaces, tabs and *'s are
     *   not really relevant contents of the comment)
     *
     *   alternatively, just "getContents()" returns:
     *   [0] ""
     *   [1] "   * contents"
     *   [2] "   * second line"
     *   [3] "  "
     *
     * ...this will return what is meant to be Content
     * (and not extraneous spaces, tabs and *s to signify a comment line)
     * @return
     */
    default String getNormalizedContents(){
        return Comments.getContent(ast());
    }

    /**
     * Returns the content as a list of Strings (one for each line)
     * @return
     */
    default List<String> listContents(){
        return Text.lines( getContents() );
    }

    /**
     *
     * @return
     */
    default List<String> listNormalizedContents(){
        return Text.lines( getNormalizedContents() );
    }

    /**
     * Sets the entire contents of the comment
     * @param contents
     * @return
     */
    public _C setContents( String...contents );

    default boolean isOrphaned(){
        return this.ast().isOrphan();
    }

    /**
     * Is this comment attributed to a Member
     * _method, _field, _class, _constructor?
     */
    default boolean isOnMember(){
        return !this.isOrphaned() && this.getCommentedNode() instanceof _java._member;
    }

    /**
     * Is the comment attributed to any of these _node classes
     * @param nodeClasses
     * @return true if the comment is attributed to
     */
    default boolean isOn( Class<? extends _java._node>... nodeClasses){
        if( this.getCommentedNode() != null){
            Arrays.stream( nodeClasses ).anyMatch( c-> c.isAssignableFrom(this.getCommentedNode().getClass()));
        }
        return false;
    }

    /**
     * Is this comment attributed to a body element (i.e. Statement or Expression)
     * or an orphaned comment within the body of a member? (not ATTRIBUTED to a member)
     *
     * @return

    default boolean isInBody(Node containerOfComment){
        if(!this.ast().getRange().isPresent() ){
            th
        }
        if( this.isOrphaned() ){
            AtomicBoolean ab = new AtomicBoolean();
            containerOfComment.stream().anyMatch( n -> {
                if( n instanceof NodeWithOptionalBlockStmt){
                    NodeWithBlockStmt nwbs = (NodeWithBlockStmt)n;
                    if( nwbs.getBody().getRange().isPresent()){
                        return nwbs.getBody().getRange().get().contains()
                    }
                }
                if( n instanceof NodeWithBlockStmt){
            } );
        }
        if( this.getCommentedNode() != null){

            Node cn = ((_java._node)getCommentedNode()).ast();

            Arrays.stream( nodeClasses ).anyMatch( c-> c.isAssignableFrom(this.getCommentedNode().getClass()));
        }
        return false;
    }
    */

    /**
     * list all comments within this astRootNode (including the comment applied
     * to the astRootNode if the AstRootNode is an instance of {@link NodeWithJavadoc}
     *
     * @param astRootNode the root node to look through
     * @return a list of all comments on or underneath the node

    static List<com.github.javaparser.ast.comments.Comment> listComments(Node astRootNode) {
        return Comments.list(astRootNode);
    }
    */

    /**
     * @param <C>                the comment class
     * @param astRootNode        the root node to start the search
     * @param commentTargetClass the TYPE of comment ({@link com.github.javaparser.ast.comments.Comment},
     *                           {@link LineComment}, {@link JavadocComment}, {@link BlockComment})
     * @param commentMatchFn     predicate for selecting comments
     * @return a list of matching comments

    static <C extends com.github.javaparser.ast.comments.Comment> List<C> listComments(
            Node astRootNode, Class<C> commentTargetClass, Predicate<C> commentMatchFn) {
        return Comments.list(astRootNode, commentTargetClass, commentMatchFn);
    }
    */

    /**
     * list all comments within this astRootNode that match the predicate
     * (including the comment applied to the astRootNode if the AstRootNode is
     * an instance of {@link NodeWithJavadoc})
     *
     * @param astRootNode    the root node to look through
     * @param commentMatchFn matching function for comments
     * @return a list of all comments on or underneath the node

    static List<com.github.javaparser.ast.comments.Comment> listComments(Node astRootNode, Predicate<com.github.javaparser.ast.comments.Comment> commentMatchFn) {
        return Comments.list(astRootNode, commentMatchFn);
    }
    */

    /**
     *
     * @param <C>
     * @param <_J>
     * @param _j
     * @param commentTargetClass
     * @param commentMatchFn
     * @return

    static <C extends com.github.javaparser.ast.comments.Comment, _J extends _java._domain> List<C> listComments(
            _J _j, Class<C> commentTargetClass, Predicate<C> commentMatchFn){

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Comments.list( ((_codeUnit) _j).astCompilationUnit(), commentTargetClass, commentMatchFn );
            }
            else{
                return Comments.list( ((_type) _j).ast(), commentTargetClass, commentMatchFn );
            }
        } else{
            return Comments.list(  ((_java._multiPart) _j).ast(), commentTargetClass, commentMatchFn);
        }
    }
    */

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @return

    static <_J extends _java._domain> List<com.github.javaparser.ast.comments.Comment> listComments(_J _j, Predicate<com.github.javaparser.ast.comments.Comment> commentMatchFn){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Comments.list( ((_codeUnit) _j).astCompilationUnit(), commentMatchFn );
            }
            else{
                return Comments.list( ((_type) _j).ast(), commentMatchFn);
            }
        } else{
            return Comments.list(  ((_java._multiPart) _j).ast(), commentMatchFn );
        }
    }
    */

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @param commentActionFn

    static <_J extends _java._domain> void forComments(_J _j, Predicate<com.github.javaparser.ast.comments.Comment> commentMatchFn, Consumer<com.github.javaparser.ast.comments.Comment> commentActionFn ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Comments.forEachIn( ((_codeUnit) _j).astCompilationUnit(), commentMatchFn, commentActionFn);
            }
            else{
                Comments.forEachIn( ((_type) _j).ast(), commentMatchFn, commentActionFn);
            }
        } else{
            Comments.forEachIn(  ((_java._multiPart) _j).ast(), commentMatchFn, commentActionFn );
        }
    }
    */

    /**
     *
     * @param <C>
     * @param <_J>
     * @param _j
     * @param commentClass
     * @param commentMatchFn
     * @param commentActionFn

    static <C extends com.github.javaparser.ast.comments.Comment, _J extends _java._domain> void forComments(_J _j, Class<C> commentClass, Predicate<C> commentMatchFn, Consumer<C> commentActionFn ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Comments.forEachIn( ((_codeUnit) _j).astCompilationUnit(), commentClass, commentMatchFn, commentActionFn);
            }
            else{
                Comments.forEachIn( ((_type) _j).ast(),  commentClass, commentMatchFn, commentActionFn);
            }
        } else{
            Comments.forEachIn(  ((_java._multiPart) _j).ast(),  commentClass, commentMatchFn, commentActionFn );
        }
    }
    */

    /**
     *
     * @param <_J>
     * @param _j
     * @return

    static <_J extends _java._domain> List<com.github.javaparser.ast.comments.Comment> listComments(_J _j){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Comments.list( ((_codeUnit) _j).astCompilationUnit() );
            }
            else{
                return Comments.list( ((_type) _j).ast() );
            }
        } else{
            return Comments.list(  ((_java._multiPart) _j).ast() );
        }
    }
    */

    /**
     *
     * @param _j
     * @param commentActionFn

    static void forComments(_java._domain _j, Consumer<com.github.javaparser.ast.comments.Comment> commentActionFn){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Comments.forEachIn( ((_codeUnit) _j).astCompilationUnit(), commentActionFn);
            }
            else{
                Comments.forEachIn( ((_type) _j).ast(), commentActionFn);
            }
        } else{
            Comments.forEachIn(  ((_java._multiPart)_j).ast(), commentActionFn );
        }
    }
    */
}
