package org.jdraft.proto;

import org.jdraft._javadoc;
import org.jdraft._java;
import org.jdraft._walk;
import org.jdraft.Ast;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.*;
import org.jdraft.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * prototype for different types of {@link com.github.javaparser.ast.comments.Comment}s
 *
 * @author Eric
 * @param <C> the underlying comment type
 */
public final class $comment <C extends Comment>
    implements $proto<C>, Template<C>, $constructor.$part, $method.$part, $field.$part {
    
    public static $comment<Comment> any(){
        return new $comment();
    }
    
    public static<C extends Comment> $comment<C> of( String pattern, Predicate<C> constraint ){
        return (($comment<C>) new $comment(Ast.comment(pattern))).and(constraint);
    }
    
    public static $comment<Comment> of( Predicate<Comment> constraint ){
        return any().and(constraint);
    }
    
    public static $comment<JavadocComment> javadocComment(){
        $comment $c = new $comment().omitBlockComments().omitLineComments();
        $c.contentsPattern = Stencil.of("$javadoc$");
        return $c;
    }

    public static $comment<JavadocComment> javadocComment( _javadoc _jc){
        return new $comment( _jc.ast()).omitBlockComments().omitLineComments();
    }
    
    public static $comment<JavadocComment> javadocComment(String...comment){
        return new $comment(Ast.javadocComment(comment)).omitBlockComments().omitLineComments();
    }
        
    public static $comment<JavadocComment> javadocComment(String pattern, Predicate<Comment> constraint){
        return new $comment(Ast.javadocComment(pattern)).omitBlockComments().omitLineComments()
            .and(constraint);
    }
    
    public static $comment<JavadocComment> javadocComment(Predicate<JavadocComment> constraint){
        return new $comment().omitBlockComments().omitLineComments()
            .and(constraint);
    }
        
    public static $comment<BlockComment> blockComment(){
        return new $comment().omitJavadocComments().omitLineComments();
    }

    public static $comment<BlockComment> blockComment(String...blockComment){
        return new $comment(Ast.blockComment(blockComment)).omitJavadocComments().omitLineComments();
    }
    
    public static $comment<BlockComment> blockComment(String pattern, Predicate<Comment> constraint){
        return new $comment(Ast.blockComment(pattern)).omitJavadocComments().omitLineComments()
            .and(constraint);
    }
    
    public static $comment<BlockComment> blockComment(Predicate<Comment> constraint){
        return new $comment().omitJavadocComments().omitLineComments()
            .and(constraint);
    }
    
    public static $comment<LineComment> lineComment(){
        return new $comment().omitBlockComments().omitJavadocComments();
    }

    public static $comment<LineComment> lineComment(String lineComment){
        return new $comment(Ast.lineComment(lineComment)).omitBlockComments().omitJavadocComments();
    }
    
    public static $comment<LineComment> lineComment(String pattern, Predicate<LineComment> constraint){
        return new $comment(Ast.lineComment( pattern))
                .omitBlockComments().omitJavadocComments()
                .and(constraint);
    }
    
    public static $comment<LineComment> lineComment(Predicate<LineComment> constraint){
        return new $comment().omitBlockComments().omitJavadocComments()
            .and(constraint);
    }
    
    public static <C extends Comment> $comment<C> of(C comment){
        return new $comment(comment);
    }
    
    public static <C extends Comment> $comment<C> of( String...comment ){
        return new $comment( Ast.comment(comment));
    }
    
    public static final Set<Class<? extends Comment>> ALL_COMMENT_CLASSES = new HashSet<>();
    
    static {
        ALL_COMMENT_CLASSES.add(BlockComment.class);
        ALL_COMMENT_CLASSES.add(LineComment.class);
        ALL_COMMENT_CLASSES.add(JavadocComment.class);
    }    
    
    /** the classes of comment classes that are matched against */
    public Set<Class<? extends Comment>> commentClasses = new HashSet<>();
    
    /** The pattern for the contents of the Comment */
    public Stencil contentsPattern = Stencil.of("$comment$");
    
    public Predicate<C> constraint = t -> true;
    
    /** this is the any() constructor private intentionally*/
    private $comment(){
        commentClasses.addAll(ALL_COMMENT_CLASSES);
        contentsPattern = Stencil.of("$comment$");        
    }
    
    /**
     * A $proto based on a particular kind of comment
     * @param <C>
     * @param astComment 
     */
    public <C extends Comment> $comment( C astComment ){
        this.commentClasses.add(astComment.getClass() );
        this.contentsPattern = Stencil.of(Ast.getContent(astComment) );        
    }
    
    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $comment hardcode$(Translator translator, Tokens kvs ) {
        this.contentsPattern = this.contentsPattern.hardcode$(translator, kvs);        
        return this;
    }
    
    public $comment and(Predicate<C> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $comment matchBlockComments(){
        this.commentClasses.add(BlockComment.class);
        return this;
    }
    
    public $comment matchLineComments(){
        this.commentClasses.add(LineComment.class);
        return this;
    }
    
    public $comment matchJavadocComments(){
        this.commentClasses.add(JavadocComment.class );
        return this;
    }
    
    public $comment omitBlockComments(){
        this.commentClasses.remove(BlockComment.class);
        return this;
    }
    
    public $comment omitLineComments(){
        this.commentClasses.remove(LineComment.class);
        return this;
    }
    
    public $comment omitJavadocComments(){
        this.commentClasses.remove(JavadocComment.class );
        return this;
    }
    
    public boolean matches( String...comment ){
        return matches( Ast.comment(comment));
    }
    
    public boolean matches( Comment astComment ){
        return select(astComment) != null;
    }

    /**
     *
     * @param comment
     * @return
     */
    public Select select( String...comment ){
        return select( Ast.comment(comment));
    }

    /**
     *
     * @param astComment
     * @return
     */
    public Select select( Comment astComment ){
        if(! this.commentClasses.contains(astComment.getClass())){
            return null;
        }
        if( !this.constraint.test( (C)astComment) ){
            return null;
        }        
        Tokens ts = this.contentsPattern.decompose(Ast.getContent(astComment));
        if( ts == null ){
            return null;
        }
        return new Select( astComment, $args.of( ts ));
    }

    /**
     *
     * @return
     */
    public boolean isMatchAny(){
        try{
            return 
                this.contentsPattern.isMatchAny() && this.constraint.test(null);
        }catch(Exception e){
            return false;
        }
    }

    /**
     *
     * @param comment
     * @param allTokens
     * @return
     */
    public Tokens decomposeTo( Comment comment, Tokens allTokens ){
        if(allTokens == null){
            return allTokens;
        }
        if( comment == null ){
            if( isMatchAny() ){
                return allTokens;
            } else{
                return null;
            }
        }
        Select sel = select(comment);
        if( sel != null ){
            if( allTokens.isConsistent(sel.args.asTokens()) ){
                allTokens.putAll(sel.args.asTokens());
                return allTokens;
            }
        }
        return null;
    }

    @Override
    public C firstIn(Node astStartNode, Predicate<C> commentMatchFn) {
        
        //this is extra work, but it "acts" like we want it to
        List<C> found = listIn(astStartNode, commentMatchFn);
        Ast.sortNodesByPosition(found);
        //Collections.sort( found, Ast.COMPARE_NODE_BY_LOCATION);
        if( found.isEmpty() ){
            return null;
        }
        return found.get(0);
    }

    @Override
    public Select selectFirstIn(Node astNode) {
        List<C> found = listIn(astNode );
        Ast.sortNodesByPosition(found);
        if( found.isEmpty() ){
            return null;
        }
        return select(found.get(0));        
    }

    /**
     *
     * @param astNode
     * @param selectConstraint
     * @return
     */
    public Select selectFirstIn(Node astNode, Predicate<Select> selectConstraint) {
        List<Select> found = listSelectedIn(astNode, selectConstraint);
        Collections.sort( found, (s1, s2)-> Ast.COMPARE_NODE_BY_LOCATION.compare(s1.comment, s2.comment));
        if( found.isEmpty() ){
            return null;
        }
        return found.get(0);        
    }

    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn(astNode, s -> found.add(s));
        return found;
    }

    /**
     *
     * @param astRootNode
     * @param selectConstraint
     * @return
     */
    public List<Select> listSelectedIn(Node astRootNode, Predicate<Select> selectConstraint) {
        List<Select> found = new ArrayList<>();
        forSelectedIn( astRootNode, selectConstraint, s -> found.add(s));
        return found;
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<C> commentMatchFn, Consumer<C> _nodeActionFn) {
        _walk.comments(astNode, c->{
            Select s = select(c);
            if( s != null && commentMatchFn.test( (C)s.comment) ) {
                _nodeActionFn.accept( (C)c);
            }
        });
        return astNode;
    }

    /**
     *
     * @param _j
     * @param selectActionFn
     * @param <_J>
     * @return
     */
    public <_J extends _java> _J forSelectedIn(_java _j, Consumer<Select> selectActionFn) {
        _walk.comments(_j, c->{
            Select s = select(c);
            if( s != null ){
                selectActionFn.accept(s);
            }
        });
        return (_J)_j;
    }
    
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        //_walk will organize by order
        _walk.comments(astRootNode, c->{
            Select s = select(c);
            if( s != null ){
                selectActionFn.accept(s);
            }
        });
        return astRootNode;
    }
    
    public <_J extends _java> _J forSelectedIn(_java _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        _walk.comments(_j, c->{
            Select s = select(c);
            if( s != null && selectConstraint.test(s)){
                selectActionFn.accept(s);
            }
        });
        return (_J)_j;
    }
    
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        _walk.comments(astRootNode, c->{
            Select s = select(c);
            if( s != null && selectConstraint.test(s)){
                selectActionFn.accept(s);
            }
        });
        return astRootNode;
    }
        
    @Override
    public <N extends Node> N removeIn(N astNode) {
        return forEachIn(astNode, n-> n.remove() );
    }

    public JavadocComment composeJavadocComment(Translator translator, Map<String, Object> keyValues) {
        String contents = this.contentsPattern.compose(translator, keyValues);
        if( contents.trim().length() == 0 ){
            return null;
        }
        if( contents.trim().equals( "null" ) ){
            return null;
        }
        return new JavadocComment( contents );
    }
    
    public BlockComment composeBlockComment(Translator translator, Map<String, Object> keyValues) {
        
        String contents = this.contentsPattern.compose(translator, keyValues);
        if( contents.trim().length() == 0 || contents.equals("null")){
            return null;
        }
        return new BlockComment( contents );
    }
    
    public LineComment composeLineComment(Translator translator, Map<String,Object> keyValues){
        String contents = this.contentsPattern.compose(translator, keyValues);
        if( contents.trim().length() == 0 || contents.equals("null")){
            return null;
        }
        return new LineComment( contents );
    }
    
    @Override
    public C compose(Translator translator, Map<String, Object> keyValues) {
        if( !keyValues.containsKey("comment")){
            keyValues.put("comment", "");
        }        
        if( !keyValues.containsKey("javadoc")){
            keyValues.put("javadoc", "");
        }      
        if( this.commentClasses.isEmpty()){
            return (C) composeBlockComment( translator, keyValues );
        }
        if( this.commentClasses.size() == 1 ){
            Class cc = this.commentClasses.toArray(new Class[0])[0];
            if( cc == JavadocComment.class){
                return (C) composeJavadocComment(translator, keyValues);
            }
            if( cc == BlockComment.class){
                return (C) composeBlockComment( translator, keyValues );
            }
            return (C) composeLineComment(translator, keyValues );
        }
        if( this.commentClasses.contains(JavadocComment.class)){
            return (C) composeJavadocComment(translator, keyValues);
        }
        if( this.commentClasses.contains(BlockComment.class)){
            return (C) composeJavadocComment(translator, keyValues);
        }
        return (C) composeLineComment(translator, keyValues);
    }

    @Override
    public $comment $(String target, String $Name) {
        this.contentsPattern = this.contentsPattern.$(target, $Name);
        return this;
    }

    @Override
    public List<String> list$() {
        return this.contentsPattern.list$();
    }

    @Override
    public List<String> list$Normalized() {
        return this.contentsPattern.list$Normalized();
    }

    public boolean match( Node n){
        if( n instanceof Comment ){
            return matches( (Comment) n);
        }
        return false;
    }

    /**
     * 
     * @param <C> 
     */
    public static class Select<C extends Comment> 
        implements $proto.selected,
            $proto.selectedAstNode<C> {

        public C comment;
        public $args args;
        
        public Select( C comment, $args args){
            this.comment = comment;
            this.args = args;
        }
        
        @Override
        public $args args() {
            return args;
        }

        @Override
        public C ast() {
            return comment;
        }
        
        public boolean isOrphan(){
            return comment.isOrphan();
        }
        
        public boolean isBlockComment(){
            return comment instanceof BlockComment;
        }
        
        public boolean isLineComment(){
            return comment instanceof LineComment;
        }
        
        public boolean isJavadocComment(){
            return comment instanceof JavadocComment;
        }
        
        public String getContent(){
            return Ast.getContent(comment);
        }
    }    
}
