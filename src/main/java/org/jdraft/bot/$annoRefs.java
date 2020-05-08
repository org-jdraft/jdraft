package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.Expressions;
import org.jdraft._annoRef;
import org.jdraft._annoRefs;
import org.jdraft._jdraftException;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * as() exactly these match
 * of() these match (can be other annotations)
 */
public class $annoRefs
        implements $bot<NodeWithAnnotations, _annoRefs, $annoRefs>{

    public static $annoRefs of(){
        return new $annoRefs();
    }

    public static $annoRefs of($annoRef...$ars){
        return new $annoRefs($ars);
    }

    public static $annoRefs of( _annoRefs _annoRefs){
        $annoRef[] $ars = new $annoRef[_annoRefs.size()];
        for(int i=0;i<_annoRefs.size(); i++){
            $ars[i]= $annoRef.of(_annoRefs.getAt(i));
        }
        return of($ars);
    }

    public static $annoRefs of( NodeWithAnnotations nwa){
        return of( _annoRefs.of(nwa));
    }

    public static $annoRefs as( NodeWithAnnotations nwa){
        return as( _annoRefs.of(nwa));
    }

    public static $annoRefs as( _annoRefs _annoRefs){
        $annoRef[] $ars = new $annoRef[_annoRefs.size()];
        for(int i=0;i<_annoRefs.size(); i++){
            $ars[i]= $annoRef.as(_annoRefs.getAt(i));
        }
        return as($ars);
    }

    /**
     *
     * @param anonymousObjectWithAnnotations
     * @return
     */
    public static $annoRefs of(Object anonymousObjectWithAnnotations ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expressions.newEx( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _annoRefs.of(bd) );
    }

    /**
     *
     * @param anonymousObjectWitAnnotations
     * @return
     */
    public static $annoRefs as( Object anonymousObjectWitAnnotations ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expressions.newEx( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return as( bd );
    }

    public static $annoRefs.Or or($annoRefs...$as ){
        return new $annoRefs.Or($as);
    }

    /**
     * I want EXACTLY these annotations (no more, no less)
     * @param $ars
     * @return
     */
    public static $annoRefs as($annoRef...$ars){
        return of($ars).$and(as-> as.size() == $ars.length);
    }

    public Predicate<_annoRefs> predicate = t-> true;

    public Set<$annoRef> $annos = new HashSet<>();

    public $annoRefs($annoRef...$ars){
        Arrays.stream($ars).forEach($a -> $annos.add($a));
    }

    @Override
    public $annoRefs copy() {
        $annoRefs copy = of();
        this.$annos.forEach($a -> copy.$annos.add($a.copy()));
        copy.predicate = predicate.and(t->true);
        return copy;
    }

    @Override
    public $annoRefs $hardcode(Translator translator, Tokens kvs) {
        this.$annos.forEach($a -> $a.$hardcode(translator, kvs));
        return this;
    }

    @Override
    public Select<_annoRefs> selectFirstIn(Node astNode, Predicate<Select<_annoRefs>> predicate) {
        Optional<Node>on = astNode.stream().filter( nwa -> {
            if(nwa instanceof NodeWithAnnotations) {
                Select s = select(_annoRefs.of(nwa));
                return s != null;
            }
            return false;
        }).findFirst();
        if( on.isPresent() ){
            return select( (NodeWithAnnotations)on.get() );
        }
        return null;
    }

    @Override
    public Predicate<_annoRefs> getPredicate() {
        return this.predicate;
    }

    @Override
    public $annoRefs setPredicate(Predicate<_annoRefs> predicate) {
        this.predicate = predicate;
        return this;
    }


    @Override
    public Select<_annoRefs> select(Node n) {
        if( n instanceof NodeWithAnnotations ){
            return select( _annoRefs.of( (NodeWithAnnotations)n));
        }
        return null;
    }

    public Select<_annoRefs> select(NodeWithAnnotations nwa) {
        return select( _annoRefs.of( nwa));
    }

    @Override
    public Select<_annoRefs> select(_annoRefs _anns) {

        if( !this.predicate.test(_anns) ) {
            return null;
        }
        if( this.$annos.size() > _anns.size() ){
             return null;
        }
        List<_annoRef> annosLeft = new ArrayList<>();
        annosLeft.addAll(_anns.list());
        Tokens tokens = new Tokens();
        List<$annoRef> $ars = this.$annos.stream().collect(Collectors.toList());
        for(int i=0;i<$ars.size();i++){

            $annoRef $a = $ars.get(i);
            Optional<_annoRef> oa = annosLeft.stream().filter(a-> $a.matches(a)).findFirst();
            if( !oa.isPresent() ){
                //System.out.println("NO MATCHING "+ $a);
                return null; //didnf find a matching anno
            }
            _annoRef got = oa.get();
            annosLeft.remove(got);
            Select $as = $a.select(oa.get());
            if( tokens.isConsistent($as.tokens)){ //args are consistent
                tokens.putAll($as.tokens);
            } else{
                return null;
            }
        }
        return new Select(_anns, tokens);
        /*
        List<_annoRef> _as = candidate.list();
        Tokens ts = Tokens.of();

        top: for(int i=0; i< $as.size();i++){
            $annoRef $a = $as.get(i);
            for(int j=0; j< _as.size(); j++ ){
                _annoRef _ar = _as.get(j);
                Select s = $a.select(_ar);
                if( s != null ){
                    _as.remove(_ar);
                    if( ts.isConsistent(s.tokens) ){
                        ts.putAll( s.tokens);
                    } else{
                        return null;
                    }
                    break top;
                }
            }
            Optional<_annoRef> f = _as.stream().filter(_a -> $a.matches( _a)).findFirst();
            if( !f.isPresent() ){
                return null;
            }
            Tokens each = $a.select(f.get()).tokens;

            if( !ts.isConsistent(each)){
                return null;
            }
            _as.remove(f);
            ts.putAll(each);
        }
        this.$annos.forEach($a -> );
        Map<$annoRef, _annoRef> annoMap = new HashMap<>();
        */

    }

    public Select<_annoRefs> select(String...candidate){
        try {
            return select(_annoRefs.of(candidate));
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean matches(String candidate) {
        return select(candidate) != null;
    }

    @Override
    public boolean isMatchAny() {
        try {
            return this.$annos.isEmpty() && this.predicate.test(null);
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public _annoRefs draft(Translator translator, Map<String, Object> keyValues) {
        _annoRefs _ars = _annoRefs.of();
        $annoRef[] arr = this.$annos.toArray(new $annoRef[0]);
        for(int i=0;i<arr.length; i++){
            _ars.add( arr[i].draftToString(translator, keyValues));
        }
        if( this.predicate.test(_ars)){
            return _ars;
        }
        return null;
    }

    @Override
    public $annoRefs $(String target, String $Name) {
        this.$annos.forEach( $a -> $a.$(target, $Name));
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> params = new ArrayList<>();
        this.$annos.forEach( $a -> params.addAll($a.$list() ));
        return params;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> params = new ArrayList<>();
        this.$annos.forEach( $a -> params.addAll($a.$listNormalized() ));
        return params.stream().distinct().collect(Collectors.toList());
    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annoRefs {

        final List<$annoRefs> $annoRefsBots = new ArrayList<>();

        public Or($annoRefs...$as){
            super();
            Arrays.stream($as).forEach($a -> $annoRefsBots.add($a) );
        }

        /**
         * Build and return a copy of this or bot
         * @return
         */
        public Or copy(){
            List<$annoRefs> copyBots = new ArrayList<>();
            this.$annoRefsBots.forEach(a-> copyBots.add(a.copy()));
            Or theCopy = new Or( copyBots.toArray(new $annoRefs[0]) );

            //now copy the predicate and all underlying bots on the baseBot
            theCopy.predicate = this.predicate.and(t->true);
            return theCopy;
        }

        @Override
        public List<String> $list(){
            return $annoRefsBots.stream().map($a ->$a.$list() ).flatMap(Collection::stream).collect(Collectors.toList());
        }

        @Override
        public List<String> $listNormalized(){
            return $annoRefsBots.stream().map($a ->$a.$listNormalized() ).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }

        @Override
        public _annoRefs draft(Translator tr, Map<String,Object> map){
            throw new _jdraftException("Cannot draft "+getClass()+" pattern"+ this );
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$annoRefs.Or{");
            sb.append(System.lineSeparator());
            $annoRefsBots.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_annoRefs> select(_annoRefs _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $annoRefs $whichBot = whichMatch(_a);
            if( $whichBot == null ){
                return null;
            }
            Select whichSelect = $whichBot.select(_a);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        public boolean isMatchAny(){
            return false;
        }

        public List<$annoRefs> $listOrSelectors() {
            return this.$annoRefsBots;
        }

        public $annoRefs whichMatch(_annoRefs _a){
            if( !this.predicate.test(_a ) ){
                return null;
            }
            Optional<$annoRefs> orsel  = this.$annoRefsBots.stream().filter($p-> $p.matches(_a) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
