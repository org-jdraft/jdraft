package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;

/**
 * Prototype for "ANY" expression implementation
 * (Can match against any expression... nice for when you know the stencil/pattern
 * but you dont know the exact Node type
 */
public class $s
	implements $bot.$node<Statement, _statement, $s>,
		$selector.$node<_statement, $s>,
		$statement<Statement, _statement, $s> {

	public static $s of(Stencil stencil ) {
		return new $s(stencil);
	}

	public static $s of(String code) {
		return new $s( code);
	}

	public static $s of(String... code) {
		return new $s( code);
	}

	public static $s of(Predicate<_statement> _matchFn) {
		return new $s(  ).$and(_matchFn);
	}

	public static $s of(String stencil, Class<? extends _statement> sClasses) {
		return of(stencil, new Class[]{ sClasses} );
	}

    /**
     * Build an $expression bot matching and expression with this stencil that implements any of the provided
	 * expressionClasses
	 * @param stencil
     * @param sClasses
     * @return
     */
	public static $s of(String stencil, Class<? extends _statement>...sClasses) {
		$s ee = new $s();
		ee.stencil = Stencil.of(stencil);
		ee.$and(sClasses);
		return ee;
	}

	public $s copy(){
		$s s = of( this.predicate );
		if( this.stencil != null ) {
			s.stencil = this.stencil.copy();
		}
		return s;
	}

	public $s $and(Class<? extends _statement>...sClasses) {
		Set<Class<? extends _statement>> _stmtClasses = new HashSet<>();
		Arrays.stream(sClasses).forEach(c -> _stmtClasses.add(c));
		Predicate<_statement> pe = (_e)->
				_stmtClasses.stream().anyMatch(c -> c.isAssignableFrom( _e.getClass() ));
		return $and( pe );
	}

	/**
	 * Matches ANY expression that is an instance of any of the statement classes
	 */
	public static $s of(Class<? extends _statement>...sClasses) {
		return new $s().$and(sClasses);
	}

	public static $s not(Predicate<_statement> _matchFn) {
		return new $s().$not(_matchFn);
	}

	public Predicate<_statement> getPredicate(){
		return this.predicate;
	}

	public $s setPredicate(Predicate<_statement> predicate){
		this.predicate = predicate;
		return this;
	}

	public Stencil stencil = null;

	public Predicate<_statement> predicate = p->true;

	public $s() {
	}
	/*
	public $e(Expression e) {
		this.stencil = Stencil.of(e.toString());
		this.predicate.and(n -> e.getClass().isAssignableFrom(n.getClass()));
	}

	public $e(_expression _e) {
		this.stencil = Stencil.of(_e.toString());
		this.predicate.and(n -> _e.ast().getClass().isAssignableFrom(n.getClass()));
	}
	 */

	public $s(Stencil st) {
		this.stencil = st;
	}

	public $s(String...ex) {
		this.stencil = Stencil.of((Object[])ex);
	}

	public boolean matches(Statement e){
		return select(e) != null;
	}

	public boolean matches(String s){
		return select(s) != null;
	}

	public boolean matches(String...str){
		return select(str) != null;
	}

	@Override
	public Selected select(Node n) {
		if( n == null ) {
			if( isMatchAny()) {
				return new Selected(null, new Tokens());
			}
			return null;
		}
		if( n instanceof Statement ) {
			_statement _e = _statement.of( (Statement)n);
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
			return "$s{}";
		}
		return "$s{"+ this.stencil +"}";
	}

	@Override
	public Selected select(String code) {
		try {
			Statement e = Stmt.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public Selected select(String... code) {
		try {
			Statement e = Stmt.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
		//return select(Ex.of(code));
	}

	@Override
	public Selected select(_statement candidate) {
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
	
	public $s $and(Predicate<_statement> matchFn) {
		this.predicate = this.predicate.and(matchFn);
		return this;
	}

	@Override
	public $s $not(Predicate<_statement> matchFn) {
		this.predicate = this.predicate.and(matchFn.negate());
		return this;
	}

	@Override
	public _statement draft(Translator translator, Map<String, Object> keyValues) {
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

			 _statement _e = _statement.of(drafted);
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
			 _statement _e = _statement.of(draftedCode);
             if (predicate.test(_e)) {
                 // run all the
                 return _e;
             }
         }
         return null;
	}

	@Override
	public $s $(String target, String $Name) {
		if( this.stencil != null ) {
			this.stencil = this.stencil.$(target, $Name);
		}
		return this; 
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
	
	/**
	 * This makes it easier to NOT have to do silly things with generics on the outside
	 */
	public static class Selected <_S extends _statement> extends Select<_S> {
		public Selected(_S _node, Tokens tokens) {
			super(_node, tokens);
		}		
	}	
}
