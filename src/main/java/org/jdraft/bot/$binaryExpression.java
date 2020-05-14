package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft._binaryExpression;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $bot for matching/selecting/inspecting and modifying {@link _binaryExpression}s/ {@link BinaryExpr}s
 *
 * @author Eric
 */
public class $binaryExpression implements $bot.$node<BinaryExpr, _binaryExpression, $binaryExpression>,
        $selector.$node<_binaryExpression, $binaryExpression>,
        $expression<BinaryExpr, _binaryExpression, $binaryExpression> {

    public static $binaryExpression of() {
        return new $binaryExpression();
    }

    public static $binaryExpression of(_binaryExpression _i) {
        return new $binaryExpression(_i);
    }

    public static $binaryExpression of(BinaryExpr ile) {
        return new $binaryExpression(_binaryExpression.of(ile));
    }

    /**
     * Matches all binaryExpressions using EXACTLY this operator
     * @param operator the operator
     * @return the $binaryExpression bot to match
     */
    public static $binaryExpression of( BinaryExpr.Operator operator){
        return of().$operators(operator);
    }

    public static $binaryExpression and(){
        return of( _binaryExpression.AND );
    }

    public static $binaryExpression binaryAnd(){
        return of( _binaryExpression.BINARY_AND );
    }

    public static $binaryExpression binaryOr(){
        return of( _binaryExpression.BINARY_OR );
    }

    public static $binaryExpression divide(){
        return of( _binaryExpression.DIVIDE );
    }

    public static $binaryExpression equals(){
        return of( _binaryExpression.EQUALS);
    }
    public static $binaryExpression greater(){
        return of( _binaryExpression.GREATER );
    }
    public static $binaryExpression greaterEquals(){
        return of( _binaryExpression.GREATER_EQUALS );
    }
    public static $binaryExpression leftShift(){
        return of( _binaryExpression.LEFT_SHIFT );
    }
    public static $binaryExpression less(){
        return of( _binaryExpression.LESS );
    }
    public static $binaryExpression lessEquals(){
        return of( _binaryExpression.LESS_EQUALS );
    }
    public static $binaryExpression minus(){
        return of( _binaryExpression.MINUS );
    }
    public static $binaryExpression multiply(){
        return of( _binaryExpression.MULTIPLY );
    }
    public static $binaryExpression notEquals(){
        return of( _binaryExpression.NOT_EQUALS );
    }
    public static $binaryExpression or(){
        return of( _binaryExpression.OR );
    }
    public static $binaryExpression plus(){
        return of( _binaryExpression.PLUS);
    }
    public static $binaryExpression rem(){
        return of( _binaryExpression.REMAINDER);
    }
    public static $binaryExpression signedRightShift(){
        return of( _binaryExpression.SIGNED_RIGHT_SHIFT);
    }
    public static $binaryExpression unsignedRightShift(){
        return of( _binaryExpression.UNSIGNED_RIGHT_SHIFT);
    }
    public static $binaryExpression xor(){
        return of( _binaryExpression.XOR);
    }

    /**
     * Matches all binaryExpressions using ANY ONE of the provided operators
     * @param operators
     * @return
     */
    public static $binaryExpression of( BinaryExpr.Operator... operators){
        return of().$operators(operators);
    }

    public static $binaryExpression not( BinaryExpr.Operator... operators){
        return of().$not(operators);
    }

    public static $binaryExpression of(String... code) {
        return of(_binaryExpression.of(code));
    }

    public static $binaryExpression.Or or($binaryExpression...$bs){
        return new $binaryExpression.Or($bs);
    }

    public Predicate<_binaryExpression> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $binaryExpression copy(){
        $binaryExpression $copy = of( ).$and(this.predicate.and(t->true) );
        $copy.$and(this.predicate);
        $copy.excludedOperators.addAll( this.excludedOperators );
        $copy.left = this.left.copy();
        $copy.right = this.right.copy();
        return $copy;
    }

    /**
     * ONLY match _binaryExpressions that use one of the provided operators
     * @param ops the provided operators to match against
     * @return the modified $binaryExpression
     */
    public $binaryExpression $operators( BinaryExpr.Operator... ops){
        this.excludedOperators.addAll(ALL_OPERATORS);
        Arrays.stream(ops).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    @Override
    public $binaryExpression $hardcode(Translator translator, Tokens kvs) {
        this.left.$hardcode(translator, kvs);
        this.right.$hardcode(translator, kvs);
        return this;
    }

    public $binaryExpression setPredicate(Predicate<_binaryExpression> predicate){
        this.predicate = predicate;
        return this;
    }

    public $binaryExpression $and(Predicate<_binaryExpression> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $binaryExpression $and(BinaryExpr.Operator...operators) {
        Arrays.stream(operators).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    public $binaryExpression $not(BinaryExpr.Operator...operators) {
        Arrays.stream(operators).forEach( op -> this.excludedOperators.add(op));
        return this;
    }

    public $binaryExpression $not(Predicate<_binaryExpression> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $binaryExpression $left( String... expression ){
        this.left.setSelector( $expression.of(expression) );
        return this;
    }

    public $binaryExpression $left( _expression _e){
        this.left.setSelector( $expression.of(_e) );
        return this;
    }

    public $binaryExpression $left( $expression $e){
        this.left.setSelector( $e );
        return this;
    }

    public $binaryExpression $left( Class<? extends _expression>... implementationClasses ){
        this.left.setSelector( $expression.of(implementationClasses) );
        return this;
    }

    public $binaryExpression $right( Class<? extends _expression>... implementationClasses ){
        this.right.setSelector( $expression.of(implementationClasses) );
        return this;
    }

    public $binaryExpression $right( String... expression ){
        this.right.setSelector( $expression.of(expression) );
        return this;
    }

    public $binaryExpression $right( _expression _e){
        this.right.setSelector( $expression.of(_e) );
        return this;
    }

    public $binaryExpression $right( $expression $e){
        this.right.setSelector( $e );
        return this;
    }

    public Select<_binaryExpression> select(String code) {
        try {
            return select(_binaryExpression.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_binaryExpression> select(String... code) {
        try {
            return select(_binaryExpression.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_binaryExpression> select(Node n) {
        if (n instanceof BinaryExpr) {
            return select(_binaryExpression.of((BinaryExpr) n));
        }
        return null;
    }

    public Select<_binaryExpression> select(Expression e) {
        if (e instanceof BinaryExpr) {
            return select(_binaryExpression.of((BinaryExpr) e));
        }
        return null;
    }

    public Select<_binaryExpression> select(_domain _n) {
        if (_n instanceof _binaryExpression) {
            return select((_binaryExpression) _n);
        }
        return null;
    }

    public Select<_binaryExpression> select(_expression<?, ?> _e) {
        if (_e instanceof _binaryExpression) {
            return select((_binaryExpression) _e);
        }
        return null;
    }

    public Select<_binaryExpression> select(_binaryExpression _i) {
        if (predicate.test(_i) ) {// && !excludedOperators.contains(_i.getOperator())) {
            Tokens ts = Tokens.selectTokens(_i, this.left, this.right, this.operator);
            if (ts != null) {
                return new Select<>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public _binaryExpression instance(String... str) {
        return _binaryExpression.of(str);
    }

    @Override
    public _binaryExpression draft(Translator translator, Map<String, Object> keyValues) {
        _expression _l = this.left.draft(translator, keyValues);
        _expression _r = this.right.draft(translator, keyValues);
        if( this.excludedOperators.size() == ALL_OPERATORS.size() - 1){
            //only (1) available operator
            Optional<BinaryExpr.Operator> oo = ALL_OPERATORS.stream().filter(o -> !this.excludedOperators.contains(o)).findFirst();
            return _binaryExpression.of(_l, oo.get(), _r);
        }
        Object o = keyValues.get("$operator");

        if( o == null ){
            throw new _jdraftException("no \"$operator\" (BinaryExpr Operator) specified in $binaryExpression or in "+ keyValues);
        }
        if( o instanceof BinaryExpr.Operator){
            return _binaryExpression.of(_l, (BinaryExpr.Operator)o, _r);
        }
        else{
            BinaryExpr.Operator bo = BinaryExpr.Operator.valueOf( o.toString() );
            if( bo == null ){
                throw new _jdraftException("invalid \"$operator\" (BinaryExpr Operator) "+ o);
            }
            return _binaryExpression.of(_l, bo, _r);
        }
    }

    @Override
    public $binaryExpression $(String target, String $Name) {
        this.left.$(target, $Name);
        this.right.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll( this.left.$list() );
        all.addAll( this.right.$list() );
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        List<String>norm = new ArrayList<>();
        norm.addAll( this.left.$listNormalized());
        norm.addAll( this.right.$listNormalized());
        return norm.stream().distinct().collect(Collectors.toList());
    }

    public boolean isMatchAny() {
        if (this.excludedOperators.isEmpty() && this.left.isMatchAny() && this.right.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static final Set<BinaryExpr.Operator> ALL_OPERATORS = Arrays.stream(BinaryExpr.Operator.values()).collect(Collectors.toSet());

    public String toString() {

        Set<BinaryExpr.Operator> ops = new HashSet<>();
        ops.addAll(ALL_OPERATORS);
        ops.removeAll(this.excludedOperators);
        return "$binaryExpression{" + System.lineSeparator() +
                Text.indent( this.left.toString()) + System.lineSeparator() +
                Text.indent( ops.toString() )+ System.lineSeparator()+
                Text.indent( this.right.toString()) + System.lineSeparator()
                +"}";
    }

    public Predicate<_binaryExpression> predicate = d -> true;

    public Select.$feature<_binaryExpression, _expression> left =
            Select.$feature.of( _binaryExpression.class, _expression.class, "left", b-> b.getLeft());

    public Select.$feature<_binaryExpression, _expression> right =
            Select.$feature.of( _binaryExpression.class, _expression.class, "right", b-> b.getRight());

    public Set<BinaryExpr.Operator> excludedOperators = new HashSet<>();

    public Select.$feature<_binaryExpression, BinaryExpr.Operator> operator =
            Select.$feature.of( _binaryExpression.class, BinaryExpr.Operator.class, "operator", b-> b.getOperator())
                    .setSelector( o -> {
                        if( excludedOperators.contains(o)){
                            return null;
                        }
                        return new Tokens();
                    });

    public $binaryExpression() {
    }

    public $binaryExpression(_binaryExpression _i) {
        this.left.setSelector( $expression.of(_i.getLeft()) );
        this.right.setSelector( $expression.of(_i.getRight()) );
        Arrays.stream (BinaryExpr.Operator.values()).forEach(o-> excludedOperators.add(o) );
        excludedOperators.remove( _i.getOperator());
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $binaryExpression {

        public List<$binaryExpression> $binaryExpressions = new ArrayList<>();

        private Or($binaryExpression...bots){
            Arrays.stream(bots).forEach(n-> $binaryExpressions.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Or copy(){
            Or $copy = $binaryExpression.or(new $binaryExpression[0]);
            $copy.$and(this.predicate);
            this.$binaryExpressions.forEach( ($b) -> $copy.$binaryExpressions.add($b.copy()));
            $copy.excludedOperators.addAll( this.excludedOperators );
            $copy.left = this.left.copy();
            $copy.right = this.right.copy();

            return $copy;
        }

        @Override
        public Select<_binaryExpression> select(_binaryExpression _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $binaryExpression $whichBot = whichMatch(_candidate);
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
         * Return the underlying $arrayAccess that matches the _arrayAccess
         * (or null if none of the $arrayAccess match the candidate _arrayAccess)
         * @param _candidate
         * @return
         */
        public $binaryExpression whichMatch(_binaryExpression _candidate){
            Optional<$binaryExpression> orsel  = this.$binaryExpressions.stream().filter($p-> $p.matches(_candidate) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
