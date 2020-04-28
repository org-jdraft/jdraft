package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * $bot for inspecting and mutating {@link _assertStmt}s / {@link AssertStmt}s
 */
public class $assertStmt implements $bot.$node<AssertStmt, _assertStmt, $assertStmt>,
        $selector.$node<_assertStmt, $assertStmt>,
        $bot.$withComment<$assertStmt>,
        $statement<AssertStmt, _assertStmt, $assertStmt> {

    public interface $part{}

    public static $assertStmt of(String name ){
        return of( new String[]{name} );
    }

    public static $assertStmt of() {
        return new $assertStmt();
    }

    public static $assertStmt of(_assertStmt _i) {
        return new $assertStmt(_i);
    }

    public static $assertStmt of(AssertStmt ile) {
        return new $assertStmt(_assertStmt.of(ile));
    }

    public static $assertStmt of(String... code) {
        return of(_assertStmt.of(code));
    }

    public static $assertStmt of(Expressions.Command lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Consumer<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Supplier<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(BiConsumer<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Expressions.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Expressions.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Function<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(BiFunction<? extends Object, ? extends Object,? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Expressions.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $assertStmt of(Expressions.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    private static $assertStmt from(StackTraceElement ste ){
        return of( _assertStmt.from( ste) );
    }

    public static $assertStmt.Or or($assertStmt...$aas){
        return new $assertStmt.Or($aas);
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     *
     * NOTE: COMMUTATIVE UPDATE
     * the or is (itself) an $assertStmt AND it contains multiple individual $assertStmts
     * which allows us to define common matching/selecting in the "base" Or,
     * and individual matching and selecting in each one of the entities in the $assertStmtList
     *
     * //build a or with the $commOr
     * $assertStmt $baseOr = $assertStmt.or($assertStmt.of().$check("true"), $assertStmt.of().$check("false") );
     * //HERE I CAN UPDATE THE COMMUTATIVE PREDICATE on the $baseOr, which adds constraints to ALL of the or ($bots)
     * $baseOr.$and(a -> a.hasComment());
     *
     * //it is easier to write this ( $and() is applied to the $baseOr & "shared" by all of the individual (or) $bots):
     * $assertStmt $baseOr = $assertStmt.or(
     *      $assertStmt.of().$check("true"),
     *      $assertStmt.of().$check("false")).$and(a->a.hasComment());
     *
     * //but logically equivalent to applying the same $and() condition to each :
     * $assertStmt $baseOr = $assertStmt.or(
     *     $assertStmt.of().$check("true").$and(a->a.hasComment()),
     *     $assertStmt.of().$check("false").$and(a->a.hasComment()) );
     *
     *
     */
    public static class Or extends $assertStmt{

        /**
         * A list of individual bots that are used to match or select against
         * NOTE: one of these MUST match or select for the matching or selecting to pass
         */
        public List<$assertStmt> $assertStmtBots = new ArrayList<>();

        private Or($assertStmt...nms){
            Arrays.stream(nms).forEach(n-> $assertStmtBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public $assertStmt.Or copy(){
            $assertStmt.Or $copy = $assertStmt.or();
            $copy.$and(this.predicate);
            $copy.check = ($expression)this.check.copy();
            $copy.message = ($expression)this.message.copy();
            this.$assertStmtBots.forEach( ($a) -> $copy.$assertStmtBots.add($a.copy()));
            return $copy;
        }

        /**
         * Return the underlying $arrayAccess that matches the _arrayAccess
         * (or null if none of the $arrayAccess match the candidate _arrayAccess)
         * @param ae
         * @return
         */
        public $assertStmt whichMatch(_assertStmt ae){
            if( !this.predicate.test(ae ) ){
                return null;
            }
            Optional<$assertStmt> orsel  = this.$assertStmtBots.stream().filter($p-> $p.matches(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        @Override
        public Select<_assertStmt> select(_assertStmt candidate) {
            //check the common selection based on the base properties
            Select commonSelect = super.select(candidate);
            if( commonSelect == null ){
                return null; // the base common Select for the Or did not match
            }
            //find the first matching individual (or) $bot that matches
            $assertStmt $whichBot = whichMatch(candidate);
            if( $whichBot == null ){ // no matching individual (or) $bot, so NONE of the or $bots
                return null;
            }
            Select iSel = $whichBot.select(candidate); //iSel should always be NON-NULL (we just found it via whichMatch())
            if(! iSel.tokens.isConsistent(commonSelect.tokens) ){ //no inconsistency between the bot tokens & base or tokens
                return null;
            }
            iSel.tokens.putAll(commonSelect.tokens);
            return iSel;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$assertStmt.Or{").append(System.lineSeparator());
            for(int i = 0; i<this.$assertStmtBots.size(); i++){
                sb.append( Text.indent( this.$assertStmtBots.get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }/* Or */

    public Predicate<_assertStmt> predicate = d -> true;

    public $expression check = $expression.of();
    public $expression message = $expression.of();
    public $comment comment = null;

    public $assertStmt() { }

    @Override
    public $assertStmt $hardcode(Translator translator, Tokens kvs) {
        this.check.$hardcode(translator, kvs);
        this.message.$hardcode(translator, kvs);
        return this;
    }

    public $assertStmt(_assertStmt _r){
        if( _r.hasMessage() ){
            this.message = $expression.of( _r.getMessage());
        }
        this.check = $expression.of(_r.getCheck());
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $assertStmt copy(){
        $assertStmt $r = of( ).$and(this.predicate.and(t->true) );
        $r.check = ($expression)this.check.copy();
        $r.message = ($expression)this.message.copy();
        return $r;
    }

    public Predicate<_assertStmt> getPredicate(){
        return this.predicate;
    }

    public $comment get$Comment(){
        return this.comment;
    }

    public $assertStmt $hasComment($comment $com){
        this.comment = $com;
        return this;
    }

    public $assertStmt setPredicate(Predicate<_assertStmt> predicate){
        this.predicate = predicate;
        return this;
    }

    public boolean isMatchAny(){
        if( (this.comment == null || this.comment.isMatchAny())
                && this.check.isMatchAny() &&  this.check instanceof $e && this.message.isMatchAny() && this.message instanceof $e){
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    public Select<_assertStmt> select(String... code) {
        try {
            return select(_assertStmt.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_assertStmt> select(Node n) {
        if (n instanceof AssertStmt) {
            return select(_assertStmt.of((AssertStmt) n));
        }
        return null;
    }

    public Select<_assertStmt> select(Statement e) {
        if (e instanceof AssertStmt) {
            return select(_assertStmt.of((AssertStmt) e));
        }
        return null;
    }

    public Select<_assertStmt> select(_java._domain _n) {
        if (_n instanceof _assertStmt) {
            return select((_assertStmt) _n);
        }
        return null;
    }

    public Select<_assertStmt> select(_statement<?, ?> _e) {
        if (_e instanceof _assertStmt) {
            return select((_assertStmt) _e);
        }
        return null;
    }

    public Select<_assertStmt> select(_assertStmt _r){

        if( ! this.predicate.test(_r)){
            return null;
        }
        Tokens ts = Tokens.of( );
        if( this.comment != null ){
            try{
                Select s = this.comment.select( (_comment)_r.getComment() );
                if( s == null ){
                    return null;
                }
                else {
                    ts.putAll( s.tokens );
                }
            } catch(Exception e){
                //comments match failed, dont throw just return null
                return null;
            }
        }
        Select scs  = this.check.select( _r.getCheck() );
        if( scs == null || !ts.isConsistent(scs.tokens) ){
            return null;
        }
        ts.putAll(scs.tokens);
        scs  = this.message.select( _r.getMessage() );
        if( scs == null || !ts.isConsistent(scs.tokens) ){
            return null;
        }
        ts.putAll(scs.tokens);
        return new Select<>(_r, ts);
    }

    public _assertStmt draft(Translator tr, Map<String,Object> keyValues){
        _assertStmt _es = _assertStmt.of();
        if( !this.check.isMatchAny() ){
            _es.setCheck( (_expression)this.check.draft(tr, keyValues) );
        }
        if( !this.message.isMatchAny() ){
            _es.setMessage( (_expression)this.message.draft(tr, keyValues) );
        }
        if( !this.predicate.test(_es) ){
            throw new _jdraftException("Drafted $assertStmt failed predicate");
        }
        return _es;
    }

    @Override
    public $assertStmt $(String target, String $Name) {
        this.check.$(target, $Name);
        this.message.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.check.$list());
        ps.addAll( this.message.$list());
        return ps;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.check.$listNormalized());
        ps.addAll( this.message.$listNormalized());
        return ps.stream().distinct().collect(Collectors.toList());
    }

    //$withExpression interface
    public $expression get$check(){
        return this.check;
    }

    public $assertStmt $check( ){
        this.check = $expression.of();
        return this;
    }

    public $assertStmt $check($expression $e ){
        this.check = $e;
        return this;
    }

    public $assertStmt $check(Predicate<_expression> matchFn){
        this.check.$and(matchFn);
        return this;
    }

    public $assertStmt $check(Class<? extends _expression>...expressionClasses){
        this.check = $expression.of(expressionClasses);
        return this;
    }

    public $assertStmt $check(String expression){
        this.check = $expression.of(expression);
        return this;
    }

    public $assertStmt $check(Expression e){
        this.check = $expression.of(e);
        return this;
    }

    public $assertStmt $check(_expression _e) {
        this.check = $expression.of(_e);
        return this;
    }


    //$withExpression interface
    public $expression get$message(){
        return this.message;
    }

    public $assertStmt $message( ){
        this.message = $expression.of();
        return this;
    }

    public $assertStmt $message($expression $e ){
        this.message = $e;
        return this;
    }

    public $assertStmt $message(Predicate<_expression> matchFn){
        this.message.$and(matchFn);
        return this;
    }

    public $assertStmt $message(Class<? extends _expression>...expressionClasses){
        this.message = $expression.of(expressionClasses);
        return this;
    }

    public $assertStmt $message(String expression){
        this.message = $expression.of(expression);
        return this;
    }

    public $assertStmt $message(Expression e){
        this.message = $expression.of(e);
        return this;
    }

    public $assertStmt $message(_expression _e) {
        this.message = $expression.of(_e);
        return this;
    }
}
