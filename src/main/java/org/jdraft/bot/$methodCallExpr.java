package org.jdraft.bot;

import org.jdraft.*;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * $bot for inspecting, drafting and mutating {@link _methodCallExpr}s / {@link MethodCallExpr}s
 */
public class $methodCallExpr implements $bot.$node<MethodCallExpr, _methodCallExpr, $methodCallExpr>,
        $selector.$node<_methodCallExpr, $methodCallExpr>,
        $expr<MethodCallExpr, _methodCallExpr, $methodCallExpr> {

    public interface $part{}

    public static $methodCallExpr of(String name, $part...parts){
        $methodCallExpr $mc = of( ($part)$name.of(name));
        return addParts($mc, parts);
    }

    public static $methodCallExpr of($part...parts){
        $methodCallExpr $mc = of();
        return addParts($mc, parts);
    }

    public static $methodCallExpr of(Expr.Command lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Consumer<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Supplier<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(BiConsumer<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Expr.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Expr.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Function<? extends Object, ? extends Object> lambdaWithMethodCall ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        if( lambdaWithMethodCall instanceof $part ){
            return of( ($part)lambdaWithMethodCall );
        }
        return from( ste );
    }

    public static $methodCallExpr of(BiFunction<? extends Object, ? extends Object,? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Expr.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $methodCallExpr of(Expr.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    private static $methodCallExpr from(StackTraceElement ste ){
        return of( _methodCallExpr.from( ste) );
    }

    public static $methodCallExpr of(String name ){
        return of( new String[]{name} );
    }


    public static $methodCallExpr of() {
        return new $methodCallExpr();
    }

    public static $methodCallExpr of(_methodCallExpr _i) {
        return new $methodCallExpr(_i);
    }

    public static $methodCallExpr of(MethodCallExpr ile) {
        return new $methodCallExpr(_methodCallExpr.of(ile));
    }

    public static $methodCallExpr of(String... code) {
        if( code.length == 1 //singe string
                && code[0].split(" ").length == 1 //single token
                && !code[0].contains("(") ) { //no open parenthesis
                return of().$name(code[0]);
        }
        return of(_methodCallExpr.of(code));
    }

    public static $methodCallExpr.Or or($methodCallExpr... $methodCallExprs){
        return new Or($methodCallExprs);
    }

    /**
     * the internals of what an instance of $methodCall.Or is (by example):
     *
     * <PRE>
     * //define (2) instances of $methodCall bots
     * $methodCall $a = $methodCall.of().$isInCodeUnit(c-> c instanceof _class);
     * $methodCall $b = $methodCall.of().$isInCodeUnit(c-> c instanceof _interface);
     *
     * // build an $methodCall.Or instance with the (2) $methodCall bots {$a,$b}
     * $methodCall.Or $or = $methodCall.or($a, $b);
     *
     * //NOTE: the $or instance IS A $methodCall itself, this is done because there may be
     * // some instance that expects a $methodCall, and this will satisfy the requirement
     * $methodCall $mc = ($methodCall) $mcor;
     *
     * //here we modify the "base instance", of the $or, we add a constraint that applies physically to
     * //the underlying $or instance (which again IS a $methodCall), so we update it's
     * //predicate, but it can be "thought of" LOGICALLY as applying this constraint to BOTH
     * //individual bots {$a, $b}
     * $or.$isInCodeUnit(c-> c.importsClass(IOException.class));
     *
     * //When we try to select, we ALWAYS FIRST check the base "$or" instances select/match function
     * //here the match/select returns FALSE, because the base constraint (import IOException)
     * //is NOT met, even though one of the individual bots ($a) DOES match all of its constraints
     * assertFalse($or.isIn("class C{ long t = System.getTimeMillis(); }"));
     *
     * //here the match/select DOES work, because the base constrains (imports IOException) are met,
     * // AND one of the OR constraints match
     * (here specifically $a, which looks for ANY methodCall that is defined within a _class)
     * assertTrue($or.IsIn("import java.io.IOException;","class C{ long t = System.getTimeMillis(); }");
     * </PRE>
     */
    public static class Or extends $methodCallExpr {

         public List<$methodCallExpr> $methodCallExprBots = new ArrayList<>();

         public Or($methodCallExpr...$mcs){
             Arrays.stream($mcs).forEach(m -> $methodCallExprBots.add(m));
         }

        public List<$methodCallExpr> $listOrSelectors() {
            return $methodCallExprBots;
        }

         public $methodCallExpr.Or copy(){
             Or or = new Or($listOrSelectors().toArray(new $methodCallExpr[0]));
             or.$and( this.predicate.and(t->true) );
             //I need to port over all of the common things
             or.scope = ($expr)this.scope.copy();
             or.typeArgs = this.typeArgs.copy();
             or.name = this.name.copy();
             or.args = this.args.copy();
             return or;
         }

         public Select<_methodCallExpr> select(_methodCallExpr _candidate){
             Select commonSelect = super.select(_candidate);
             if(  commonSelect == null){
                 return null;
             }
             $methodCallExpr $whichBot = whichMatch(_candidate);
             if( $whichBot == null ){
                 return null;
             }
             Select whichSelect = $whichBot.select(_candidate);
             if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                 return null;
             }
             whichSelect.tokens.putAll(commonSelect.tokens);
             return whichSelect;
         }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param candidate
         * @return
         */
        public $methodCallExpr whichMatch(_methodCallExpr candidate){
            Optional<$methodCallExpr> orsel  = this.$methodCallExprBots.stream().filter($p-> $p.matches( candidate ) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$methodCall.Or{").append(System.lineSeparator());
            for(int i = 0; i< $listOrSelectors().size(); i++){
                sb.append( Text.indent( this.$listOrSelectors().get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }

    public $methodCallExpr $not( $methodCallExpr... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    private static $methodCallExpr addParts($methodCallExpr $mc, $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $name){
                $mc.name = ($name)parts[i];
            }
            if( parts[i] instanceof $expr){
                $mc.scope = ($expr)parts[i];
            }
            if( parts[i] instanceof $args){
                $mc.args = ($args)parts[i];
            }
            if( parts[i] instanceof $typeArgs){
                $mc.typeArgs = ($typeArgs)parts[i];
            }
        }
        return $mc;
    }

    public List<$bot> $listBots(){
        List<$bot> bots = new ArrayList();
        bots.add( this.scope );
        bots.add( this.typeArgs);
        bots.add( this.name );
        bots.add( this.args);
        return bots;
    }

    public Predicate<_methodCallExpr> predicate = d -> true;

    public $expr scope = $expr.of();
    public $typeArgs typeArgs = $typeArgs.of();
    public $name name = $name.of();
    public $args args = $args.of();

    public $methodCallExpr() { }

    public $methodCallExpr(_methodCallExpr _mc){
        name = $name.of(_mc.getName());
        if( _mc.hasScope()){
            scope = $expr.of(_mc.getScope());
        }
        if( _mc.hasArgs() ){
            args = $args.as(_mc.getArgs());
        }
        if( _mc.hasTypeArgs() ){
            typeArgs = $typeArgs.of( _mc.getTypeArgs() );
        }
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $methodCallExpr copy(){
        $methodCallExpr $mc = of().$and( this.predicate.and(t->true) );
        $mc.typeArgs = this.typeArgs.copy();
        $mc.args = this.args.copy();
        $mc.name = this.name.copy();
        $mc.scope = ($expr)this.scope.copy();
        return $mc;
    }

    @Override
    public $methodCallExpr $hardcode(Translator translator, Tokens kvs) {
        typeArgs.$hardcode(translator, kvs);
        args.$hardcode(translator, kvs);
        scope.$hardcode(translator, kvs);
        name.$hardcode(translator, kvs);
        return this;
    }

    public Predicate<_methodCallExpr> getPredicate(){
        return this.predicate;
    }

    public $methodCallExpr setPredicate(Predicate<_methodCallExpr> predicate){
        this.predicate = predicate;
        return this;
    }

    public boolean isMatchAny(){
        if( this.name.isMatchAny() && this.scope.isMatchAny() && args.isMatchAny() && typeArgs.isMatchAny()){
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    public Select<_methodCallExpr> select(String... code) {
        try {
            return select(_methodCallExpr.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_methodCallExpr> select(Node n) {
        if (n instanceof MethodCallExpr) {
            return select(_methodCallExpr.of((MethodCallExpr) n));
        }
        return null;
    }

    public Select<_methodCallExpr> select(Expression e) {
        if (e instanceof MethodCallExpr) {
            return select(_methodCallExpr.of((MethodCallExpr) e));
        }
        return null;
    }

    public Select<_methodCallExpr> select(_java._domain _n) {
        if (_n instanceof _methodCallExpr) {
            return select((_methodCallExpr) _n);
        }
        return null;
    }

    public Select<_methodCallExpr> select(_expr<?, ?> _e) {
        if (_e instanceof _methodCallExpr) {
            return select((_methodCallExpr) _e);
        }
        return null;
    }

    public Select<_methodCallExpr> select(_methodCallExpr _mc){
        if( !_mc.hasScope() && !this.scope.isMatchAny()){
            return null;
        }
        if( ! this.predicate.test(_mc)){
            return null;
        }
        Select s = this.scope.select( _mc.getScope() );
        if( s == null ){
            return null;
        }
        Tokens ts = s.tokens;

        s = this.name.select(_mc.getName());
        if( s == null || !ts.isConsistent(s.tokens)){
            return null;
        }
        ts.putAll(s.tokens);

        s = this.args.select(_mc.getArgs() );
        if( s == null || !ts.isConsistent(s.tokens)){
            return null;
        }
        ts.putAll(s.tokens);

        s = this.typeArgs.select(_mc.getTypeArgs() );
        if( s == null || !ts.isConsistent(s.tokens)){
            return null;
        }
        ts.putAll(s.tokens);
        return new Select<>(_mc, ts);
    }

    public _methodCallExpr draft(Translator tr, Map<String,Object> keyValues){
        _methodCallExpr _mc = _methodCallExpr.of();
        _mc.setName(this.name.draft(tr, keyValues).node.toString());
        if( !this.scope.isMatchAny() ){
            _mc.setScope( (_expr)this.scope.draft(tr, keyValues));
        }
        _mc.setArgs( this.args.draft(tr, keyValues));
        try{
            _mc.setTypeArgs( this.typeArgs.draft(tr, keyValues));
        } catch(Exception e){
            if( !typeArgs.isMatchAny() ){
                throw new _jdraftException("Unable to set type arguments ", e);
            }
        }
        if( !this.predicate.test(_mc) ){
            throw new _jdraftException("Drafted _methodCall failed bot predicate");
        }
        return _mc;
    }

    @Override
    public $methodCallExpr $(String target, String $Name) {
        this.name.$(target, $Name);
        this.scope.$(target, $Name);
        this.args.$(target, $Name);
        this.typeArgs.$(target, $Name);
        return this;
    }

    @Override
    public $methodCallExpr $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
    }

    @Override
    public List<String> $list() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.scope.$list());
        ps.addAll( this.typeArgs.$list());
        ps.addAll( this.name.$list());
        ps.addAll(this.args.$list());
        return ps;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.scope.$listNormalized());
        ps.addAll( this.typeArgs.$listNormalized());
        ps.addAll( this.name.$listNormalized());
        ps.addAll( this.args.$listNormalized());
        return ps.stream().distinct().collect(Collectors.toList());
    }

    /*
    //$withTypeArguments
    public $typeArgs get$typeArguments(){
        return this.typeArgs;
    }
     */

    public $methodCallExpr $typeArgs(){
        this.typeArgs = $typeArgs.of();
        return this;
    }

    public $methodCallExpr $typeArgs(Predicate<_typeArgs> predicate){
        this.typeArgs.$and(predicate);
        return this;
    }

    public $methodCallExpr $typeArgs($typeArgs $as){
        this.typeArgs = $as;
        return this;
    }

    public $methodCallExpr $typeArgs(String ts){
        this.typeArgs = $typeArgs.of(ts);
        return this;
    }

    public $methodCallExpr $typeArgs(String... ts){
        this.typeArgs = $typeArgs.of(ts);
        return this;
    }

    public $methodCallExpr $typeArgs($typeRef...tas){
        this.typeArgs = $typeArgs.of(tas);
        return this;
    }

    public $methodCallExpr $typeArgs(_typeRef...args){
        this.typeArgs = $typeArgs.of(args);
        return this;
    }

    /*
    //$withArguments
    public $args get$args(){
        return this.args;
    }
     */

    public $methodCallExpr $args(){
        this.args = $args.of();
        return this;
    }

    public $methodCallExpr $args(Predicate<_args> predicate){
        this.args.$and(predicate);
        return this;
    }

    public $methodCallExpr $args(String args){
        this.args = $args.as(args);
        return this;
    }

    public $methodCallExpr $args(String...args){
        this.args = $args.as(args);
        return this;
    }

    public $methodCallExpr $args($args $as){
        this.args = $as;
        return this;
    }

    public $methodCallExpr $args($expr...args){
        this.args = $args.as(args);
        return this;
    }

    public $methodCallExpr $args(_expr...args){
        this.args = $args.as(args);
        return this;
    }

    /*
    //$withName
    public $name get$name(){
        return this.name;
    }
     */

    public $methodCallExpr $name(){
        this.name = $name.of();
        return this;
    }

    public $methodCallExpr $name(Predicate<String> matchFn){
        this.name.$and(n -> matchFn.test( n.toString()) );
        return this;
    }

    public $methodCallExpr $name(String name){
        this.name = $name.of(name);
        return this;
    }

    public $methodCallExpr $name($name $n){
        this.name = $n;
        return this;
    }

    /*
    //$withScope interface
    public $expr get$scope(){
        return this.scope;
    }
     */

    public $methodCallExpr $scope( ){
        this.scope = $expr.of();
        return this;
    }

    public $methodCallExpr $scope($expr $e ){
        this.scope = $e;
        return this;
    }

    public $methodCallExpr $scope(Predicate<_expr> matchFn){
        this.scope.$and(matchFn);
        return this;
    }

    public $methodCallExpr $scope(Class<? extends _expr>...expressionClasses){
        this.scope = $expr.of(expressionClasses);
        return this;
    }

    public $methodCallExpr $scope(String expression){
        this.scope = $expr.of(expression);
        return this;
    }

    public $methodCallExpr $scope(Expression e){
        this.scope = $expr.of(e);
        return this;
    }

    public $methodCallExpr $scope(_expr _e){
        this.scope = $expr.of(_e);
        return this;
    }

}
