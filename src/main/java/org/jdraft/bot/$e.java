package org.jdraft.bot;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LiteralExpr;
import org.jdraft._jdraftException;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;
import org.jdraft.Ex;
import org.jdraft._expression;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;

/**
 * Prototype for "ANY" expression implementation
 * (Can match against 
 */
public class $e 
	implements $bot.$node<Expression,_expression, $e>,
		$selector.$node<_expression, $e>,
		$expr<Expression, _expression, $e>{
	
	public static $e of() {
		return new $e();
	}
		
	public static $e of( _expression<?,?> _e) {
		return new $e( _e );
	}
		
	public static<$E extends $expr> $E of(Expression ile) {
		if( ile instanceof ArrayAccessExpr ){
			return ($E)$arrayAccess.of( (ArrayAccessExpr)ile);
		}
		if( ile instanceof LiteralExpr ){
			if( ile instanceof IntegerLiteralExpr){
				return ($E) $int.of( (IntegerLiteralExpr) ile);
			}
		}
		return ($E)new $e( _expression.of(ile) );
	}
		
	public static $expr of( Stencil stencil ) {
		return new $e(stencil);
	}
		
	public static $expr of(String... code) {
		return of( Ex.of(code));
	}
		
	public static $expr of(Predicate<_expression> _matchFn) {
		return new $e(  ).$and(_matchFn);
	}
	
	/**
	 * Matches ANY expression that is an instance of any of the expression classes
	 */
	public static $expr of(Class<? extends _expression>...expressionClasses) {
		Set<Class<? extends _expression>> exprClasses = new HashSet<>();			
		Arrays.stream(expressionClasses).forEach(c -> exprClasses.add(c));
		Predicate<_expression> pe = (_e)-> 
			exprClasses.stream().anyMatch(c -> c.isAssignableFrom( _e.getClass() ));			
		$expr e = of(pe);
		return e;
	}

	public Predicate<_expression> getPredicate(){
		return this.predicate;
	}

	public $e setPredicate( Predicate<_expression> predicate){
		this.predicate = predicate;
		return this;
	}

	public Stencil stencil = null;
	
	public Predicate<_expression> predicate = p->true;
	
	public $e() {
	}
	
	public $e(Expression e) {
		this.stencil = Stencil.of(e.toString());
		this.predicate.and(n -> e.getClass().isAssignableFrom(n.getClass()));
	}

	public $e(_expression _e) {
		this.stencil = Stencil.of(_e.toString());
		this.predicate.and(n -> _e.ast().getClass().isAssignableFrom(n.getClass()));
	}

	public $e(Stencil st) {
		this.stencil = st;			
	}
	
	public $e(String...ex) {
		this.stencil = Stencil.of((Object[])ex);
	}
	
	@Override
	public Selected select(Node n) {
		if( n == null ) {
			if( isMatchAny()) {
				return new Selected(null, new Tokens());
			}
			return null;
		}
		if( n instanceof Expression ) {
			_expression _e = _expression.of( (Expression)n);
			if( this.predicate.test(_e )) {
				//if( this.expressionClassSet.con)
				if(this.stencil == null ) {
					return new Selected( _e, new Tokens());
				}
				else { //check if it passes the stencil too
					Tokens ts = this.stencil.parse(_e.toString());
					if( ts != null ) {
						return new Selected(_e, ts);
					}
				}
			}				
		}
		return null;
	}

	public String toString(){
		if( this.stencil == null ){
			return "$e{}";
		}
		return "$e{"+ this.stencil +"}";
	}

	@Override
	public Selected select(String code) {
		
		try {
			Expression e = Ex.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public Selected select(String... code) {
		try {
			Expression e = Ex.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
		//return select(Ex.of(code));
	}

	@Override
	public Selected select(_expression candidate) {
		if( candidate == null) {
			if( isMatchAny() ) {
				return new Selected( null, new Tokens());
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
	
	public $e $and(Predicate<_expression> matchFn) {
		this.predicate = this.predicate.and(matchFn);
		return this;
	}

	@Override
	public $e $not(Predicate<_expression> matchFn) {
		this.predicate = this.predicate.and(matchFn.negate());
		return this;
	}

	@Override
	public _expression draft(Translator translator, Map<String, Object> keyValues) {
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
             
             _expression _e = _expression.of(drafted);
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
             _expression _e = _expression.of(draftedCode);
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
	public List<String> list$() {
		if( this.stencil != null ) {
			return this.stencil.list$();
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<String> list$Normalized() {
		if( this.stencil != null ) {
			return this.stencil.list$Normalized();
		}
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * This makes it easier to NOT have to do silly things with generics on the outside
	 */
	public static class Selected <_E extends _expression> extends Select<_E> {
		public Selected(_E _node, Tokens tokens) {
			super(_node, tokens);
		}		
	}	
}
