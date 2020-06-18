package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Prototype for "ANY" expression implementation
 * (Can match against any expression... nice for when you know the stencil/pattern
 * but you dont know the exact Node type
 */
public class $s
	implements $bot.$node<Statement, _stmt, $s>,
		$selector.$node<_stmt, $s>,
        $stmt<Statement, _stmt, $s> {

	public static $s of(Stencil stencil ) {
		return new $s(stencil);
	}

	public static $s of(String code) {
		return new $s( code);
	}

	public static $s of(String... code) {
		return new $s( code);
	}

	public static $s of(String stencil, Class<? extends _stmt> sClasses) {
		return of(stencil, new Class[]{ sClasses} );
	}

    /**
     * Build an $expression bot matching and expression with this stencil that implements any of the provided
	 * expressionClasses
	 * @param stencil
     * @param sClasses
     * @return
     */
	public static $s of(String stencil, Class<? extends _stmt>...sClasses) {
		$s ee = new $s();
		ee.stencil = Stencil.of(stencil);
		ee.$and(sClasses);
		return ee;
	}

	public static $s of(){
		return new $s();
	}

	public $s copy(){
		$s s = of( ).$and(this.predicate );
		if( this.stencil != null ) {
			s.stencil = this.stencil.copy();
		}
		return s;
	}

	@Override
	public $s $hardcode(Translator translator, Tokens kvs) {
		if( this.stencil != null ) {
			this.stencil = this.stencil.$hardcode(translator, kvs);
		}
		return this;
	}

	public $s $not( $s... $sels ){
		return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
	}

	public $s $and(Class<? extends _stmt>...sClasses) {
		Set<Class<? extends _stmt>> _stmtClasses = new HashSet<>();
		Arrays.stream(sClasses).forEach(c -> _stmtClasses.add(c));
		Predicate<_stmt> pe = (_e)->
				_stmtClasses.stream().anyMatch(c -> c.isAssignableFrom( _e.getClass() ));
		return $and( pe );
	}

	/**
	 * Matches ANY expression that is an instance of any of the statement classes
	 */
	public static $s of(Class<? extends _stmt>...sClasses) {
		return new $s().$and(sClasses);
	}

	public static $s not(Predicate<_stmt> _matchFn) {
		return new $s().$not(_matchFn);
	}

	public Predicate<_stmt> getPredicate(){
		return this.predicate;
	}

	public $s setPredicate(Predicate<_stmt> predicate){
		this.predicate = predicate;
		return this;
	}

	public Stencil stencil = null;

	public Predicate<_stmt> predicate = p->true;

	public $s() {
	}

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
	public Select<_stmt> select(Node n) {
		if( n == null ) {
			if( isMatchAny()) {
				return new Select<>(null, new Tokens());
			}
			return null;
		}
		if( n instanceof Statement ) {
			_stmt _e = _stmt.of( (Statement)n);
			if( this.predicate.test(_e )) {
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
			return "$s{}";
		}
		return "$s{"+ this.stencil +"}";
	}

	@Override
	public Select<_stmt> select(String code) {
		try {
			Statement e = Stmt.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public Select<_stmt> select(String... code) {
		try {
			Statement e = Stmt.of(code);
			return select(e);
		}catch(Exception e) {
			return null;
		}
		//return select(Ex.of(code));
	}

	@Override
	public Select<_stmt> select(_stmt candidate) {
		if( candidate == null) {
			if( isMatchAny() ) {
				return new Select<>( null, new Tokens());
			}
			return null;
		}
		return select(candidate.node());
	}

	public boolean isMatchAny() {
		try{
			if( this.predicate.test(null) ) {
				return this.stencil == null || this.stencil.isMatchAny();
			}
		}catch(Exception e) {}
		return false;
	}
	
	public $s $and(Predicate<_stmt> matchFn) {
		this.predicate = this.predicate.and(matchFn);
		return this;
	}

	@Override
	public $s $not(Predicate<_stmt> matchFn) {
		this.predicate = this.predicate.and(matchFn.negate());
		return this;
	}

	@Override
	public _stmt draft(Translator translator, Map<String, Object> keyValues) {
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

			 _stmt _e = _stmt.of(drafted);
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
			 _stmt _e = _stmt.of(draftedCode);
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
	public $s $hardcode(Translator translator, Map<String, Object> keyValues) {
		return this.$hardcode(translator, Tokens.of(keyValues));
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
