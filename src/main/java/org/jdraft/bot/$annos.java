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
public class $annos
        extends $botEnsemble<_annos, $annos>
        implements $bot<_annos, $annos>{

    public static $annos of(){
        return new $annos();
    }

    public static $annos of($anno $ar){
        return new $annos($ar);
    }

    public static $annos of($anno...$ars){
        return new $annos($ars);
    }

    public static $annos of(Class<? extends Annotation> annoClass){
        return of( _anno.of(annoClass));
    }

    public static $annos of(Class<? extends Annotation>... annoClasses){
        _annos _ars = _annos.of();
        for(int i=0;i<annoClasses.length;i++){
            _ars.add(_anno.of( annoClasses[i]));
        }
        return of(_ars);
    }

    public static $annos of(String annos){
        return of(_annos.of(annos) );
    }

    public static $annos of(String... annos){
        return of(_annos.of(annos) );
    }

    public static $annos of(_anno _ar){
        return of( _annos.of(_ar));
    }

    public static $annos of(_anno... _ars){
        return of( _annos.of(_ars));
    }

    public static $annos of(_annos _annos){
        $anno[] $ars = new $anno[_annos.size()];
        for(int i = 0; i< _annos.size(); i++){
            $ars[i]= $anno.of(_annos.getAt(i));
        }
        return of($ars);
    }

    public static $annos of(NodeWithAnnotations nwa){
        return of( _annos.of(nwa));
    }

    /**
     *
     * @param anonymousObjectWithAnnotations
     * @return
     */
    public static $annos of(Object anonymousObjectWithAnnotations ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr( ste );
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( _annos.of(bd) );
    }

    public static $annos as(NodeWithAnnotations nwa){
        return as( _annos.of(nwa));
    }

    public static $annos as(_annos _annos){
        $anno[] $ars = new $anno[_annos.size()];
        for(int i = 0; i< _annos.size(); i++){
            $ars[i]= $anno.as(_annos.getAt(i));
        }
        return as($ars);
    }

    public static $annos as(String annos){
        return as(_annos.of(annos) );
    }

    public static $annos as(String... annos){
        return as(_annos.of(annos) );
    }
    /**
     *
     * @param anonymousObjectWitAnnotations
     * @return
     */
    public static $annos as(Object anonymousObjectWitAnnotations ){
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
    public static $annos as($anno...$ars){
        return of($ars).$and(as-> as.size() == $ars.length);
    }

    public static $annos.Or or($annos...$as ){
        return new $annos.Or($as);
    }

    public $featureBotList<_annos, _anno, $anno> annosList =
            $featureBotList.of(_annos.ANNOS);

    public $annos($anno...$ars){
        Arrays.stream($ars).forEach($a -> annosList.add($a));
    }

    @Override
    public $annos copy() {
        $annos copy = of();
        copy.annosList = annosList.copy();
        copy.predicate = predicate.and(t->true);
        return copy;
    }

    public $annos $not($annos... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    @Override
    public Select<_annos> selectFirstIn(Node astNode, Predicate<Select<_annos>> predicate) {
        Optional<Node>on = astNode.stream().filter( nwa -> {
            if(nwa instanceof NodeWithAnnotations) {
                Select s = select(_annos.of(nwa));
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
    public Select<_annos> select(Node n) {
        if( n instanceof NodeWithAnnotations ){
            return select( _annos.of( (NodeWithAnnotations)n));
        }
        return null;
    }

    public Select<_annos> select(NodeWithAnnotations nwa) {
        return select( _annos.of( nwa));
    }

    public Select<_annos> select(String...candidate){
        try {
            return select(_annos.of(candidate));
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean matches(String candidate) {
        return select(candidate) != null;
    }

    @Override
    public List<$feature<_annos, ?, ?>> $listFeatures() {
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
    public _annos draft(Translator translator, Map<String, Object> keyValues) {
        List<_anno> _as = annosList.draft(translator, keyValues);
        _annos _ars = _annos.of( _as.toArray(new _anno[0]));

        if( this.predicate.test(_ars)){
            return _ars;
        }
        return null;
    }

    /**
     * An Or entity that can match against any of the $bot instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $annos {

        final List<$annos> $annosBots = new ArrayList<>();

        public Or($annos...$as){
            super();
            Arrays.stream($as).forEach($a -> $annosBots.add($a) );
        }

        /**
         * Build and return a copy of this or bot
         * @return
         */
        public Or copy(){
            List<$annos> copyBots = new ArrayList<>();
            this.$annosBots.forEach(a-> copyBots.add(a.copy()));
            Or theCopy = new Or( copyBots.toArray(new $annos[0]) );

            //now copy the predicate and all underlying bots on the baseBot
            theCopy.predicate = this.predicate.and(t->true);
            return theCopy;
        }

        @Override
        public List<String> $list(){
            return $annosBots.stream().map($a ->$a.$list() ).flatMap(Collection::stream).collect(Collectors.toList());
        }

        @Override
        public List<String> $listNormalized(){
            return $annosBots.stream().map($a ->$a.$listNormalized() ).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }

        @Override
        public $annos.Or $hardcode(Translator translator, Tokens tokens){
            $annosBots.stream().forEach($a -> $a.$hardcode(translator, tokens));
            return this;
        }

        @Override
        public _annos draft(Translator tr, Map<String,Object> map){
            throw new _jdraftException("Cannot draft "+getClass()+" pattern"+ this );
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$annoExprs.Or{");
            sb.append(System.lineSeparator());
            $annosBots.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_annos> select(_annos _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $annos $whichBot = whichMatch(_a);
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

        public List<$annos> $listOrSelectors() {
            return this.$annosBots;
        }

        public $annos whichMatch(_annos _a){
            if( !this.predicate.test(_a ) ){
                return null;
            }
            Optional<$annos> orsel  = this.$annosBots.stream().filter($p-> $p.matches(_a) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
