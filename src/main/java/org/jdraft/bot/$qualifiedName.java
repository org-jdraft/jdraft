package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import org.jdraft._jdraftException;
import org.jdraft._qualifiedName;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class $qualifiedName implements $bot<Node, _qualifiedName, $qualifiedName>,
        $selector<_qualifiedName, $qualifiedName>{

    public static $qualifiedName of( _qualifiedName _qn){
        return new $qualifiedName(_qn);
    }

    public static $qualifiedName of( Predicate<_qualifiedName> pqn ){
        return new $qualifiedName().$and(pqn);
    }

    Stencil stencil = null;
    Predicate<_qualifiedName> predicate = t-> true;

    private $qualifiedName( ){ }

    public $qualifiedName( _qualifiedName _qn){
        stencil = Stencil.of(_qn.getNameString());
    }

    @Override
    public $qualifiedName $hardcode(Translator translator, Tokens kvs) {
        this.stencil = this.stencil.$hardcode(translator, kvs);
        return this;
    }

    @Override
    public Select<_qualifiedName> select(Node n) {
        if( n instanceof NodeWithSimpleName || n instanceof NodeWithName){
            try{
                return select( _qualifiedName.of(n));
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }

    public $qualifiedName copy(){
        $qualifiedName $qn = of( this.predicate.and(t->true) );
        if( this.stencil != null ){
            $qn.stencil = this.stencil.copy();
        }
        return $qn;
    }

    @Override
    public Select<_qualifiedName> selectFirstIn(Node astNode, Predicate<Select<_qualifiedName>> predicate) {
        return null;
    }

    @Override
    public Predicate<_qualifiedName> getPredicate() {
        return this.predicate;
    }

    @Override
    public $qualifiedName setPredicate(Predicate<_qualifiedName> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public Select<_qualifiedName> select(_qualifiedName candidate) {
        if( this.predicate.test(candidate) ) {
            if( this.stencil == null ){
                return new Select<_qualifiedName>(candidate, new Tokens());
            }
            Tokens ts = this.stencil.parse(candidate.getNameString());
            if( ts != null ){
                return new Select<_qualifiedName>(candidate, ts);
            }
        }
        return null;
    }

    @Override
    public boolean matches(String candidate) {
        return false;
    }

    @Override
    public boolean isMatchAny() {
        if(stencil == null || stencil.isMatchAny()){
            try {
                return this.predicate.test(null);
            }catch(Exception e){}
        }
        return false;
    }

    @Override
    public _qualifiedName draft(Translator translator, Map<String, Object> keyValues) {
        throw new _jdraftException("unable to draft");
    }

    @Override
    public $qualifiedName $(String target, String $Name) {
        if( this.stencil != null ){
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> $list() {
        if( this.stencil != null ){
            return this.stencil.$list();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> $listNormalized() {
        if( this.stencil != null ){
            return this.stencil.$listNormalized();
        }
        return Collections.emptyList();
    }
}
