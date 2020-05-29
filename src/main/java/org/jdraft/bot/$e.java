package org.jdraft.bot;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.javaparser.ast.expr.*;
import org.jdraft._jdraftException;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;
import org.jdraft.Expr;
import org.jdraft._expr;

import com.github.javaparser.ast.Node;

/**
 * Prototype for "ANY" expression implementation
 * (Can match against any expression... nice for when you know the stencil/pattern
 * but you dont know the exact Node type
 */
public class $e 
	implements $bot.$node<Expression, _expr, $e>,
		$selector.$node<_expr, $e>,
		$expr<Expression, _expr, $e> {

	public static $e of(){
		return new $e();
	}

	public static $e of(Stencil stencil ) {
		return new $e(stencil);
	}

	public static $e of(String code) {
		return new $e( code);
	}

	public static $e of(String... code) {
		return new $e( code);
	}

	public static $e of(String stencil, Class<? extends _expr> expressionClasses) {
		return of(stencil, new Class[]{ expressionClasses} );
	}

	/**
	 * Build an $expression bot matching and expression with this stencil that implements any of the provided
	 * expressionClasses
	 * @param stencil
	 * @param expressionClasses
	 * @return
	 */
	public static $e of(String stencil, Class<? extends _expr>...expressionClasses) {
		$e ee = new $e();
		ee.stencil = Stencil.of(stencil);
		ee.$and(expressionClasses);
		return ee;
	}

	public $e $and( Class<? extends _expr>...expressionClasses) {
		Set<Class<? extends _expr>> exprClasses = new HashSet<>();
		Arrays.stream(expressionClasses).forEach(c -> exprClasses.add(c));
		Predicate<_expr> pe = (_e)->
				exprClasses.stream().anyMatch(c -> c.isAssignableFrom( _e.getClass() ));
		return $and( pe );
	}

	/**
	 * Matches ANY expression that is an instance of any of the expression classes
	 */
	public static $e of(Class<? extends _expr>...expressionClasses) {
		return new $e().$and(expressionClasses);
	}

	public static $e not(Predicate<_expr> _matchFn) {
		return new $e().$not(_matchFn);
	}

	public Predicate<_expr> getPredicate(){
		return this.predicate;
	}

	public $e setPredicate( Predicate<_expr> predicate){
		this.predicate = predicate;
		return this;
	}

	public Stencil stencil = null;
	
	public Predicate<_expr> predicate = p->true;
	
	public $e() {
	}

	public $e copy(){
		$e copy = of().$and(this.predicate.and(t->true));
		if( this.stencil != null ) {
			copy.stencil = this.stencil.copy();
		}
		return copy;
	}

	public $e(Stencil st) {
		this.stencil = st;			
	}
	
	public $e(String...ex) {
		this.stencil = Stencil.of((Object[])ex);
	}

	public $e $not( $e... $sels ){
		return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
	}

	public boolean matches(Expression e){
		return select(e) != null;
	}

	public boolean matches(String s){
		return select(s) != null;
	}

	public boolean matches(String...str){
		return select(str) != null;
	}

	@Override
	public Select<_expr> select(Node n) {
		if( n == null ) {
			if( isMatchAny()) {
				return new Select<>(null, new Tokens());
			}
			return null;
		}
		if( n instanceof Expression ) {
			_expr _e = _expr.of( (Expression)n);
			if( this.predicate.test(_e )) {
				//if( this.expressionClassSet.con)
				if(this.stencil == null ) {
					return new Select<>( _e, new Tokens());
				}
				else { //check if it passes the stencil too
					Tokens ts = this.stencil.parse(_e.toString());
					if( ts != null ) {
						return new Select<>(_e, ts);
					}
				}
			}				
		}
		return null;
	}

	public String toString(){
		if( this.stencil == null ){
			return "$e{ MATCH ANY }";
		}
		return "$e{ \""+ this.stencil +"\" }";
	}

	@Override
	public Select<_expr> select(String code) {
		try {
			Expression e = Expr.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public Select<_expr> select(String... code) {
		try {
			Expression e = Expr.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
		//return select(Ex.of(code));
	}

	@Override
	public Select<_expr> select(_expr candidate) {
		if( candidate == null) {
			if( isMatchAny() ) {
				return new Select<>( null, new Tokens());
			}
			return null;
		}
		return select(candidate.ast());
	}

	public boolean isMatchAny() {
		try{
			if( this.predicate.test(null) ) {
				return this.stencil == null || this.stencil.isMatchAny();
			}
		}catch(Exception e) {}
		return false;
	}
	
	public $e $and(Predicate<_expr> matchFn) {
		this.predicate = this.predicate.and(matchFn);
		return this;
	}

	public $e $hardcode(Translator tr, Tokens ts){
		if( this.stencil != null ){
			this.stencil =  this.stencil.$hardcode(tr, ts);
		}
		return this;
	}

	@Override
	public $e $not(Predicate<_expr> matchFn) {
		this.predicate = this.predicate.and(matchFn.negate());
		return this;
	}

	@Override
	public _expr draft(Translator translator, Map<String, Object> keyValues) {
		 if (this.stencil == null) {
             // I might NOT set the
             String overrideName = this.getClass().getSimpleName();
             Object override = keyValues.get(overrideName);
             if (override == null) {
                 throw new _jdraftException("no stencil specified for " + this + " ...and no override Stencil/String \"" + overrideName + "\" provided");
             }
             Stencil stencil = null;
             if (override instanceof String) {
                 stencil = Stencil.of((String) override);
             } else if (override instanceof Stencil) {
                 stencil = (Stencil) override;
             } else {
                 stencil = Stencil.of(override.toString());
             }
             String drafted = stencil.draft(translator, keyValues);
             
             _expr _e = _expr.of(drafted);
             if( predicate.test(_e)) {
            	 return _e;
             }
             return null;
         // return null; //we can draft a prototype with no syntax
         }
         // build the code based on the normalized syntaxPattern
         String draftedCode = stencil.draft(translator, keyValues);
         if (draftedCode != null) {
             // if the drafted code is built
             // create an instance of the object based on the drafted code
             _expr _e = _expr.of(draftedCode);
             if (predicate.test(_e)) {
                 // run all the
                 return _e;
             }
         }
         return null;
	}

	@Override
	public $e $(String target, String $Name) {
		if( this.stencil != null ) {
			this.stencil = this.stencil.$(target, $Name);
		}
		return this; 
	}

	@Override
	public Template<_expr> $hardcode(Translator translator, Map<String, Object> keyValues) {
		return null;
	}

	@Override
	public List<String> $list() {
		if( this.stencil != null ) {
			return this.stencil.$list();
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<String> $listNormalized() {
		if( this.stencil != null ) {
			return this.stencil.$listNormalized();
		}
		return Collections.EMPTY_LIST;
	}
}
