package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

import java.util.Arrays;
import java.util.List;

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
    default String getContents(){
        return Comments.getContent(ast());
    }

    default boolean contains( CharSequence content ){
        return getContents().contains(content);
    }

    /**
     * Can we find an instanceof the the stencil pattern anywhere in the code?
     * @param stencil the stencil to match within the javadoc contents
     * @return true if this Stencil pattern is found, false otherwise
     */
    default boolean contains( Stencil stencil ){
        return parseFirst(stencil) != null;
    }

    /**
     * create a Stencil from the stencilText, then look for the Stencil inside the text matching
     * and extracting the first instance found into tokens and returning them.
     * Return null if no instance of the text is found
     *
     * @param stencilText text to build a Stencil to match against
     * @return Tokens of the first Stencil match, or null if no match
     */
    default Tokens parseFirst(String stencilText ){
        return parseFirst(Stencil.of(stencilText));
    }

    /**
     *
     * @param stencil
     * @return
     */
    default Tokens parseFirst(Stencil stencil ){
        return stencil.parseFirst(getContents());
    }

    /**
     * Returns the content as a list of Strings (one for each line)
     * @return
     */
    default List<String> listContents(){
        return Text.lines( getContents() );
    }

    /**
     * Is this comment NOT attributed to a given Ast Node?
     * @return whether this comment is orphaned( Not attributed to an AST node)
     */
    default boolean isOrphaned(){
        return this.ast().isOrphan();
    }

    /**
     * Is this comment attributed to a Member
     * _method, _field, _class, _constructor?

    default boolean isOnMember(){
        return !this.isOrphaned() && this.getCommentedNode() instanceof _java._member;
    }
     */

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
     * Sets the entire contents of the comment
     * @param contents
     * @return
     */
    default _C setContents( String...contents ){
        return setContents( Text.combine(contents) );
    }

    /**
     * Sets the contents of the comment
     * @param contents
     * @return
     */
    default _C setContents(String contents){
        return setContents( _blockComment.STANDARD_STYLE, contents);
    }

    /**
     *
     * @param style
     * @param contents
     * @return
     */
    default _C setContents(_style style, String...contents){
        return setContents( style, Text.combine(contents) );
    }

    /**
     * Because {@link _blockComment}s/ {@link _javadocComment}s can span multiple lines
     *  / **
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
    default _C setContents(_style style, String contents){
        if( this.ast().getRange().isPresent()){
            //it's already attached, So I need to infer what indentation I need
            int column = Math.max( (this.ast().getRange().get().begin.column) -1, 0);

            String indent = "";
            for(int i=0;i<column;i++){
                indent += " "; //or tabs??
            }
            String cont = _comment.formatContents(style, indent, contents);
            ast().setContent( cont );
            return (_C)this;
        }
        ast().setContent( _comment.formatContents(style, "", contents) );
        return (_C)this;
    }

    /*
     * multi
     * line
     * content
     */
    _style STANDARD_STYLE = new _style();

    /* multi
     * line
     * content
     */
    _style FIRST_LINE_STYLE =
            new _style(false, 1, true, true);

    /*
      multi
      line
      comment
     */
    _style OPEN_STYLE =
            new _style(true, 1, false, true);

    /* multi
       line
       comment */
    _style COMPACT_OPEN_STYLE =
            new _style(false, 1, false, false);

    /*multi
      line
      comment*/
    _style ULTRA_COMPACT_STYLE =
            new _style(false, 0, false, false);

    /**
     * A Style to apply to the raw string contents of comments
     * (i.e. typically applied to multi-line {@link _blockComment} and {@link _javadocComment})
     *
     * this is never represented in the actual AST, but rather exists to add some Quality of Life
     * improvements to updating comments within the AST
     */
    class _style {

        /** IF the comment contents is more than one line, does the contensts start on the first line */
        public boolean skipContentsOnFirstLine = true;

        /** The number of spaces between the star and content / *_    _* / (the _'s here represent 1 space) */
        public int starPadding = 1;

        /** Prefix multiple line contents with the "*" ? */
        public boolean starPrefixAllLines = true;

        /**
         * does the closing * / tag appear on separate line (aligned in the left gutter)?
         */
        public boolean alignCloseTagOnLeft = true; //

        public _style(){}

        public _style(boolean skipContentsOnFirstLine, int starPadding,
                      boolean starPrefixAllLines, boolean alignCloseTagOnLeft){
            this.skipContentsOnFirstLine = skipContentsOnFirstLine;
            this.starPadding = starPadding;
            this.starPrefixAllLines = starPrefixAllLines;
            this.alignCloseTagOnLeft = alignCloseTagOnLeft;
        }
    }

    /**
     *
     * @param style
     * @param contents
     * @return
     */
    static String formatContents(_style style, String contents){
        return formatContents(style, "", contents);
    }

    /**
     * remove the preceding / * and the ending * / and create the contents
     * @param style
     * @param indent the indention of the tag (this indention is only applied
     * @param rawContents
     * @return
     */
    static String formatContents(_style style, String indent, String rawContents){
        List<String> ls = Text.lines(rawContents);
        if( ls.size() == 1 ){
            String trim = rawContents.trim();
            if( trim.startsWith("/*") ){
                rawContents = trim.substring(2).trim();
            }
            if( rawContents.endsWith("*/")){
                rawContents = rawContents.substring(0, rawContents.length() -2);
            }
            for(int i=0;i<style.starPadding; i++){
                rawContents = " "+rawContents+" ";
            }

            return rawContents;
        }
        //there are more than 1 line, here we need to "play" with the indent
        StringBuilder sb = new StringBuilder();
        if( style.skipContentsOnFirstLine ){
            ls.add(0, "");
        }
        for(int i=0;i<ls.size();i++){
            if( i == 0 ){
                for(int j = 0; j<style.starPadding; j++){
                    sb.append(" ");
                }
                sb.append(ls.get(0)).append(System.lineSeparator());
            }
            else {
                sb.append(indent);
                if( style.starPrefixAllLines){
                    sb.append(" *");
                }
                else{
                    sb.append("  ");
                }
                for(int j = 0; j<style.starPadding; j++){
                    sb.append(" ");
                }

            }
            if( i == ls.size() - 1 ){ //last line
                //check if the last line
                if( style.alignCloseTagOnLeft ){
                    if( ls.get(i).equals(System.lineSeparator()) ){
                        //the last line is a blank line, so DONT add it
                        sb.append(System.lineSeparator()); //keep the blank line
                        sb.append(indent);//add an indent
                    } else{
                        sb.append(ls.get(i)).append(System.lineSeparator());
                        sb.append(indent);
                    }
                } else{
                    if( i > 0 ) {
                        sb.append(ls.get(i)); //.append(System.lineSeparator());
                        for(int j = 0; j<style.starPadding; j++){
                            sb.append(" ");
                        }
                    }
                }
            } else{ //just
                if( i > 0 ) {
                    sb.append(ls.get(i)).append(System.lineSeparator());
                }
            }
        }
        if( style.alignCloseTagOnLeft ){
            sb.append(" "); //because the start tag has a / * (the slash before the *) the close tag on the last line needs indent 1
        }
        return sb.toString();
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
