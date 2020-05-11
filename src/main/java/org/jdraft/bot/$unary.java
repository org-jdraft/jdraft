package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import org.jdraft._expression;
import org.jdraft._java._domain;
import org.jdraft._jdraftException;
import org.jdraft._unary;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * $bot for matching/selecting/inspecting and modifying {@link _unary}s/ {@link UnaryExpr}s
 *
 * @author Eric
 */
public class $unary implements $bot.$node<UnaryExpr, _unary, $unary>,
        $selector.$node<_unary, $unary>,
        $expression<UnaryExpr, _unary, $unary> {

    public static $unary of() {
        return new $unary();
    }

    public static $unary of(_unary _i) {
        return new $unary(_i);
    }

    public static $unary of(UnaryExpr ile) {
        return new $unary(_unary.of(ile));
    }

    /**
     * Matches all binaryExpressions using EXACTLY this operator
     * @param operator the operator
     * @return the $binaryExpression bot to match
     */
    public static $unary of(UnaryExpr.Operator operator){
        return of().$operators(operator);
    }

    public static $unary not( UnaryExpr.Operator... operators){
        return of().$not(operators);
    }

    public static $unary bitwiseComplement(){
        return of( _unary.BITWISE_COMPLEMENT);
    }
    public static $unary logicalComplement(){
        return of( _unary.LOGICAL_COMPLEMENT );
    }
    public static $unary minus(){
        return of( _unary.MINUS );
    }
    public static $unary plus(){
        return of( _unary.PLUS );
    }
    public static $unary postDecrement(){
        return of( _unary.POST_DECREMENT );
    }
    public static $unary postIncrement(){
        return of( _unary.POST_INCREMENT );
    }
    public static $unary preDecrement(){
        return of( _unary.PRE_DECREMENT );
    }
    public static $unary preIncrement(){
        return of( _unary.PRE_INCREMENT );
    }

    /**
     * Matches all binaryExpressions using ANY ONE of the provided operators
     * @param operators
     * @return
     */
    public static $unary of(UnaryExpr.Operator... operators){
        return of().$operators(operators);
    }

    public static $unary of(String... code) {
        return of(_unary.of(code));
    }

    public static $unary.Or or($unary...$bs){
        return new $unary.Or($bs);
    }

    public Predicate<_unary> getPredicate(){
        return this.predicate;
    }

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $unary copy(){
        $unary $copy = of( ).$and(this.predicate.and(t->true) );
        $copy.$and(this.predicate);
        $copy.excludedOperators.addAll( this.excludedOperators );
        $copy.expression = this.expression.copy();
        return $copy;
    }

    /**
     * ONLY match _binaryExpressions that use one of the provided operators
     * @param ops the provided operators to match against
     * @return the modified $binaryExpression
     */
    public $unary $operators(UnaryExpr.Operator... ops){
        this.excludedOperators.addAll(ALL_OPERATORS);
        Arrays.stream(ops).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    @Override
    public $unary $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        return this;
    }

    public $unary setPredicate(Predicate<_unary> predicate){
        this.predicate = predicate;
        return this;
    }

    public $unary $and(Predicate<_unary> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $unary $and(UnaryExpr.Operator...operators) {
        Arrays.stream(operators).forEach( op -> this.excludedOperators.remove(op));
        return this;
    }

    public $unary $not(UnaryExpr.Operator...operators) {
        Arrays.stream(operators).forEach( op -> this.excludedOperators.add(op));
        return this;
    }

    public $unary $not(Predicate<_unary> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public $unary $expression(String... expression ){
        this.expression.setSelector( $expression.of(expression) );
        return this;
    }

    public $unary $expression(_expression _e){
        this.expression.setSelector( $expression.of(_e) );
        return this;
    }

    public $unary $expression($expression $e){
        this.expression.setSelector( $e );
        return this;
    }

    public $unary $expression(Class<? extends _expression>... implementationClasses ){
        this.expression.setSelector( $expression.of(implementationClasses) );
        return this;
    }


    public Select<_unary> select(String code) {
        try {
            return select(_unary.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_unary> select(String... code) {
        try {
            return select(_unary.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_unary> select(Node n) {
        if (n instanceof UnaryExpr) {
            return select(_unary.of((UnaryExpr) n));
        }
        return null;
    }

    public Select<_unary> select(Expression e) {
        if (e instanceof UnaryExpr) {
            return select(_unary.of((UnaryExpr) e));
        }
        return null;
    }

    public Select<_unary> select(_domain _n) {
        if (_n instanceof _unary) {
            return select((_unary) _n);
        }
        return null;
    }

    public Select<_unary> select(_expression<?, ?> _e) {
        if (_e instanceof _unary) {
            return select((_unary) _e);
        }
        return null;
    }

    public Select<_unary> select(_unary _i) {
        if (predicate.test(_i) ) {// && !excludedOperators.contains(_i.getOperator())) {
            Tokens ts = Tokens.selectTokens(_i, this.expression, this.operator);
            if (ts != null) {
                return new Select<>(_i, ts);
            }
            return null;
        }
        return null;
    }

    public _unary instance(String... str) {
        return _unary.of(str);
    }

    @Override
    public _unary draft(Translator translator, Map<String, Object> keyValues) {
        _expression _l = this.expression.draft(translator, keyValues);

        if( this.excludedOperators.size() == ALL_OPERATORS.size() - 1){
            //only (1) available operator
            Optional<UnaryExpr.Operator> oo = ALL_OPERATORS.stream().filter(o -> !this.excludedOperators.contains(o)).findFirst();
            return _unary.of(_l, oo.get());
        }
        Object o = keyValues.get("$operator");

        if( o == null ){
            throw new _jdraftException("no \"$operator\" (BinaryExpr Operator) specified in $binaryExpression or in "+ keyValues);
        }
        if( o instanceof UnaryExpr.Operator){
            return _unary.of(_l, (UnaryExpr.Operator)o );
        }
        else{
            UnaryExpr.Operator bo = UnaryExpr.Operator.valueOf( o.toString() );
            if( bo == null ){
                throw new _jdraftException("invalid \"$operator\" (BinaryExpr Operator) "+ o);
            }
            return _unary.of(_l, bo);
        }
    }

    @Override
    public $unary $(String target, String $Name) {
        this.expression.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> all = new ArrayList<>();
        all.addAll( this.expression.$list() );
        return new ArrayList<>();
    }

    @Override
    public List<String> $listNormalized() {
        List<String>norm = new ArrayList<>();
        norm.addAll( this.expression.$listNormalized());
        return norm.stream().distinct().collect(Collectors.toList());
    }

    public boolean isMatchAny() {
        if (this.excludedOperators.isEmpty() && this.expression.isMatchAny()) {
            try {
                return this.predicate.test(null);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static final Set<UnaryExpr.Operator> ALL_OPERATORS = Arrays.stream(UnaryExpr.Operator.values()).collect(Collectors.toSet());

    public String toString() {

        Set<UnaryExpr.Operator> ops = new HashSet<>();
        ops.addAll(ALL_OPERATORS);
        ops.removeAll(this.excludedOperators);
        return "$unary{" + System.lineSeparator() +
                Text.indent( this.expression.toString()) + System.lineSeparator() +
                Text.indent( ops.toString() )+ System.lineSeparator()
                +"}";
    }

    public Predicate<_unary> predicate = d -> true;

    public $featureSelector<_unary, _expression> expression =
            $featureSelector.of( _unary.class, _expression.class, "expression", u-> u.getExpression());

    public Set<UnaryExpr.Operator> excludedOperators = new HashSet<>();

    public $featureSelector<_unary, UnaryExpr.Operator> operator =
            $featureSelector.of( _unary.class, UnaryExpr.Operator.class, "operator", b-> b.getOperator())
                    .setSelector( o -> {
                        if( excludedOperators.contains(o)){
                            return null;
                        }
                        return new Tokens();
                    });

    public $unary() {
    }

    public $unary(_unary _i) {
        this.expression.setSelector( $expression.of(_i.getExpression()) );
        Arrays.stream (UnaryExpr.Operator.values()).forEach(o-> excludedOperators.add(o) );
        excludedOperators.remove( _i.getOperator());
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $unary {

        public List<$unary> $unaryExpressions = new ArrayList<>();

        private Or($unary...bots){
            Arrays.stream(bots).forEach(n-> $unaryExpressions.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Or copy(){
            Or $copy = $unary.or(new $unary[0]);
            $copy.$and(this.predicate);
            this.$unaryExpressions.forEach( ($b) -> $copy.$unaryExpressions.add($b.copy()));
            $copy.excludedOperators.addAll( this.excludedOperators );
            $copy.expression = this.expression.copy();

            return $copy;
        }

        @Override
        public Select<_unary> select(_unary _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $unary $whichBot = whichMatch(_candidate);
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
        public $unary whichMatch(_unary _candidate){
            Optional<$unary> orsel  = this.$unaryExpressions.stream().filter($p-> $p.matches(_candidate) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
