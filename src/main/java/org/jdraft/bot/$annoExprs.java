package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * as() exactly these match
 * of() these match (can be other annotations)
 */
public class $annoExprs
        extends $botEnsemble<_annoExprs, $annoExprs>
        implements $bot<_annoExprs, $annoExprs>{

    public static $annoExprs of(){
        return new $annoExprs();
    }

    public static $annoExprs of($annoExpr $ar){
        return new $annoExprs($ar);
    }

    public static $annoExprs of($annoExpr...$ars){
        return new $annoExprs($ars);
    }

    public static $annoExprs of(Class<? extends Annotation> annoClass){
        return of( _annoExpr.of(annoClass));
    }

    public static $annoExprs of(Class<? extends Annotation>... annoClasses){
        _annoExprs _ars = _annoExprs.of();
        for(int i=0;i<annoClasses.length;i++){
            _ars.add(_annoExpr.of( annoClasses[i]));
        }
        return of(_ars);
    }

    public static $annoExprs of(String annos){
        return of(_annoExprs.of(annos) );
    }

    public static $annoExprs of(String... annos){
        return of(_annoExprs.of(annos) );
    }

    public static $annoExprs of(_annoExpr _ar){
        return of( _annoExprs.of(_ar));
    }

    public static $annoExprs of(_annoExpr... _ars){
        return of( _annoExprs.of(_ars));
    }

    public static $annoExprs of(_annoExprs _annoExprs){
        $annoExpr[] $ars = new $annoExpr[_annoExprs.size()];
        for(int i = 0; i< _annoExprs.size(); i++){
            $ars[i]= $annoExpr.of(_annoExprs.getAt(i));
        }
        return of($ars);
    }

    public static $annoExprs of(NodeWithAnnotations nwa){
        return of( _annoExprs.of(nwa));
    }

    /**
     *
     * @param anonymousObjectWithAnnotations
     * @return
     */
    public static $annoExprs of(Object anonymousObjectWithAnnotations ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _annoExprs.of(bd) );
    }

    public static $annoExprs as(NodeWithAnnotations nwa){
        return as( _annoExprs.of(nwa));
    }

    public static $annoExprs as(_annoExprs _annoExprs){
        $annoExpr[] $ars = new $annoExpr[_annoExprs.size()];
        for(int i = 0; i< _annoExprs.size(); i++){
            $ars[i]= $annoExpr.as(_annoExprs.getAt(i));
        }
        return as($ars);
    }

    public static $annoExprs as(String annos){
        return as(_annoExprs.of(annos) );
    }

    public static $annoExprs as(String... annos){
        return as(_annoExprs.of(annos) );
    }
    /**
     *
     * @param anonymousObjectWitAnnotations
     * @return
     */
    public static $annoExprs as(Object anonymousObjectWitAnnotations ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return as( bd );
    }

    /**
     * I want EXACTLY these annotations (no more, no less)
     * @param $ars
     * @return
     */
    public static $annoExprs as($annoExpr...$ars){
        return of($ars).$and(as-> as.size() == $ars.length);
    }

    public static $annoExprs.Or or($annoExprs...$as ){
        return new $annoExprs.Or($as);
    }

    public $featureBotList<_annoExprs, _annoExpr, $annoExpr> annosList =
            $featureBotList.of(_annoExprs.ANNOS);

    public $annoExprs($annoExpr...$ars){
        Arrays.stream($ars).forEach($a -> annosList.add($a));
    }

    @Override
    public $annoExprs copy() {
        $annoExprs copy = of();
        copy.annosList = annosList.copy();
        copy.predicate = predicate.and(t->true);
        return copy;
    }

    public $annoExprs $not( $annoExprs... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    @Override
    public Select<_annoExprs> selectFirstIn(Node astNode, Predicate<Select<_annoExprs>> predicate) {
        Optional<Node>on = astNode.stream().filter( nwa -> {
            if(nwa instanceof NodeWithAnnotations) {
                Select s = select(_annoExprs.of(nwa));
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
    public Select<_annoExprs> select(Node n) {
        if( n instanceof NodeWithAnnotations ){
            return select( _annoExprs.of( (NodeWithAnnotations)n));
        }
        return null;
    }

    public Select<_annoExprs> select(NodeWithAnnotations nwa) {
        return select( _annoExprs.of( nwa));
    }

    public Select<_annoExprs> select(String...candidate){
        try {
            return select(_annoExprs.of(candidate));
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean matches(String candidate) {
        return select(candidate) != null;
    }

    @Override
    public List<$feature<_annoExprs, ?, ?>> $listFeatures() {
        return Stream.of(this.annosList).collect(Collectors.toList());
    }

    @Override
    public boolean isMatchAny() {
        try {
            return this.annosList.isMatchAny() && this.predicate.test(null);
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public _annoExprs draft(Translator translator, Map<String, Object> keyValues) {
        List<_annoExpr> _as = annosList.draft(translator, keyValues);
        _annoExprs _ars = _annoExprs.of( _as.toArray(new _annoExpr[0]));

        if( this.predicate.test(_ars)){
            return _ars;
        }
        return null;
    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annoExprs {

        final List<$annoExprs> $annoExprsBots = new ArrayList<>();

        public Or($annoExprs...$as){
            super();
            Arrays.stream($as).forEach($a -> $annoExprsBots.add($a) );
        }

        /**
         * Build and return a copy of this or bot
         * @return
         */
        public Or copy(){
            List<$annoExprs> copyBots = new ArrayList<>();
            this.$annoExprsBots.forEach(a-> copyBots.add(a.copy()));
            Or theCopy = new Or( copyBots.toArray(new $annoExprs[0]) );

            //now copy the predicate and all underlying bots on the baseBot
            theCopy.predicate = this.predicate.and(t->true);
            return theCopy;
        }

        @Override
        public List<String> $list(){
            return $annoExprsBots.stream().map($a ->$a.$list() ).flatMap(Collection::stream).collect(Collectors.toList());
        }

        @Override
        public List<String> $listNormalized(){
            return $annoExprsBots.stream().map($a ->$a.$listNormalized() ).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }

        @Override
        public $annoExprs.Or $hardcode(Translator translator, Tokens tokens){
            $annoExprsBots.stream().forEach($a -> $a.$hardcode(translator, tokens));
            return this;
        }

        @Override
        public _annoExprs draft(Translator tr, Map<String,Object> map){
            throw new _jdraftException("Cannot draft "+getClass()+" pattern"+ this );
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$annoExprs.Or{");
            sb.append(System.lineSeparator());
            $annoExprsBots.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_annoExprs> select(_annoExprs _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $annoExprs $whichBot = whichMatch(_a);
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

        public List<$annoExprs> $listOrSelectors() {
            return this.$annoExprsBots;
        }

        public $annoExprs whichMatch(_annoExprs _a){
            if( !this.predicate.test(_a ) ){
                return null;
            }
            Optional<$annoExprs> orsel  = this.$annoExprsBots.stream().filter($p-> $p.matches(_a) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
