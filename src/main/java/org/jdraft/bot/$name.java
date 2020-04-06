package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import org.jdraft._jdraftException;
import org.jdraft._name;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class $name implements $bot<Node, _name, $name>,
        $selector<_name, $name>, Template<_name>, $methodCall.$part {

    public static $name of(){
        return new $name();
    }

    public static $name of( Expression e){
        return new $name(e.toString());
    }

    public static $name of( String stencil){
        return new $name(stencil);
    }

    public static $name of( Stencil stencil ){
        return new $name(stencil);
    }

    /*
    public static $name of( Predicate<_name> matchFn){
        return new $name().$and(matchFn);
    }
     */

    /** the pattern of the name*/
    public Stencil stencil = null;

    /**  */
    public Predicate<_name> predicate = t -> true;

    public $name(){}

    public $name(String stencil){
        this.stencil = Stencil.of( stencil);
    }

    public $name(Stencil stencil){
        this.stencil = stencil;
    }

    /**
     * Does the stencil explicitly have a . (for a qualified name)
     * @return
     */
    public boolean isQualified(){
        return stencil != null &&
                stencil.getTextForm().getFixedText().indexOf('.') >= 0;
    }

    /** build another mutable copy of this bot */
    public $name copy(){
        $name copy = of().$and(this.predicate.and(t->true));
        if(this.stencil != null ){
            copy.stencil = Stencil.of( this.stencil );
        }
        return copy;
    }

    public Predicate<_name> getPredicate(){
        return this.predicate;
    }

    @Override
    public $name setPredicate(Predicate<_name> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public boolean isMatchAny() {
        if( stencil == null ){
            try{
                return predicate.test(null) && this.matchAnnoNames && this.matchAnnoMemberValueNames
                        && this.matchImports && this.matchMethodNames && this.matchMethodReferences
                        && this.matchPackageNames && this.matchParameterNames && this.matchTypeDeclarationNames
                        && this.matchTypeRefNames && this.matchVariableNames;
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $name $and(Predicate<_name> matchFn) {
        this.predicate = predicate.and(matchFn);
        return this;
    }

    public boolean matches( Node n){
        return select(n) != null;
    }

    public boolean matches(String str ){
        return select(_name.of(str)) != null;
    }

    public Select<_name> select(String s){
        return select( _name.of(s));
    }

    public Select<_name> select( String...str){
        return select( _name.of(str));
    }

    public Select<_name> select( Node n ){
        if( n instanceof Name || n instanceof SimpleName || n instanceof MethodReferenceExpr){
            return select( _name.of(n) );
        }
        return null;
    }

    @Override
    public Select<_name> selectFirstIn(Node astNode, Predicate<Select<_name>> predicate) {
        Optional<Node> on = astNode.stream().filter( n ->{
            Select sel = select( n);
            if( sel != null ){
                return predicate.test(sel);
            }
            return false;
        }).findFirst();
        if( !on.isPresent() ){
            return null;
        }
        return select( on.get());
    }

    //matchTypeArguments
    //matchTypeParameters
    //matchThrows
    //matchAnnos
    
    public boolean matchConstructorNames = true;

    public $name $matchConstructorNames( boolean b){
        this.matchConstructorNames = b;
        return this;
    }

    public boolean matchMethodNames = true;

    public $name $matchMethodNames( boolean b){
        this.matchMethodNames = b;
        return this;
    }
    public boolean matchImports = true;

    public $name $matchImports(boolean b){
        this.matchImports = b;
        return this;
    }

    public boolean matchMethodReferences = true;

    public $name $matchMethodReferences( boolean b){
        this.matchMethodReferences = b;
        return this;
    }

    public boolean matchAnnoMemberValueNames = true;

    public $name $matchAnnoMemberValueNames(boolean b){
        this.matchAnnoMemberValueNames = b;
        return this;
    }

    public boolean matchAnnoNames = true;

    public $name $matchAnnoNames(boolean b){
        this.matchAnnoNames = b;
        return this;
    }

    public boolean matchParameterNames = true;

    public $name $matchParameterNames(boolean b){
        this.matchParameterNames = b;
        return this;
    }

    public boolean matchPackageNames = true;

    public $name $matchPackageNames(boolean b){
        this.matchPackageNames = b;
        return this;
    }

    boolean matchVariableNames = true;

    public $name $matchVariableNames(boolean b){
        this.matchVariableNames = b;
        return this;
    }

    boolean matchTypeDeclarationNames = true;

    public $name $matchTypeDeclarationNames(boolean b){
        this.matchTypeDeclarationNames = b;
        return this;
    }

    boolean matchTypeRefNames = true;

    public $name $matchTypeRefNames(boolean b){
        this.matchTypeRefNames = b;
        return this;
    }

    /**
     *
     * @param candidate
     * @return
     */
    private boolean useCheck( _name candidate ){
        if( ! matchAnnoMemberValueNames && candidate.isAnnoMemberValueName() ){
            return false;
        }
        if( ! matchAnnoNames && candidate.isAnnoName()){
            return false;
        }
        if( !matchConstructorNames && candidate.isConstructorName() ){
            return false;
        }
        if( ! matchImports && candidate.isImportName() ){
            return false;
        }
        if( ! matchMethodNames && candidate.isMethodName()){
            return false;
        }
        if( !matchPackageNames && candidate.isPackageName()){
            return false;
        }
        if( !matchParameterNames && candidate.isParameterName() ){
            return false;
        }
        if( !matchVariableNames && candidate.isVariableName()){
            return false;
        }
        if( ! matchMethodReferences && candidate.isMethodReference() ){
            return false;
        }
        if( !matchTypeDeclarationNames && candidate.isTypeDeclarationName()){
            return false;
        }
        if( ! matchTypeRefNames && candidate.isTypeRefName() ){
            return false;
        }
        return true;
    }

    public Select<_name> select(_name candidate){
        if( isMatchAny()){
            return new Select<>(candidate, new Tokens());
        }
        if( this.stencil == null ){
            if( useCheck(candidate) && this.predicate.test(candidate) ){
                return new Select<>(candidate, new Tokens());
            }
            return null;
        }
        if( useCheck(candidate) && this.predicate.test(candidate) ){

            String str = candidate.toString();
            //System.out.println("TRYING TO MATCH " +str);

            Tokens ts = this.stencil.parse(str);
            //System.out.println( "HERE>> "+candidate+ " "+ ts);

            if( ts != null ) {
                return new Select<>(candidate, ts);
            }
            if( candidate.name instanceof com.github.javaparser.ast.expr.Name){
                System.out.println( " "+ candidate.name);
                Name nm = (Name) candidate.name;
                ts = this.stencil.parse(nm.getIdentifier());
                if( ts != null ){ //DONT match is the parent matches
                    if( nm.getParentNode().isPresent() && matches(nm.getParentNode().get()) ){
                        return null;
                    }
                }
            } else if( candidate.name instanceof MethodReferenceExpr){
                //we only identify the ID part of the MethodReferenceExpr
                //because the underlying "scope" expression can be a SimpleName
                // for example
                // A::B
                // A could be a SimpleName node
                // B is a String identifier (i.e. NOT a Node)
                // REALLY, if we walk the nodes:
                //
                //com.github.javaparser.ast.expr.MethodReferenceExpr "A::B"
                //com.github.javaparser.ast.expr.TypeExpr "A"
                //com.github.javaparser.ast.type.ClassOrInterfaceType "A"
                //com.github.javaparser.ast.expr.SimpleName "A"
                // notice the String B is NOT
                MethodReferenceExpr mre = (MethodReferenceExpr) candidate.name;
                ts = this.stencil.parse( mre.getIdentifier());
            }
            if( ts != null ){
                return new Select<>(candidate, ts);
            }
        }
        return null;
    }

    @Override
    public _name draft(Translator translator, Map<String, Object> keyValues) {
        if( this.stencil == null){
            Object nm = keyValues.get("$name");
            if( nm == null ){
                throw new _jdraftException( "cannot draft $name with no Template/Stencil ");
            }
            if( nm instanceof Stencil ){
                return _name.of(((Stencil)nm).draft(translator, keyValues));
            } else{
                return _name.of(nm.toString());
            }
        }
        return _name.of(this.stencil.draft(translator, keyValues));
    }

    @Override
    public $name $(String target, String $Name) {
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

    public $name $hardcode(Translator translator, Tokens kvs) {
        if( this.stencil != null ){
            this.stencil.$hardcode(translator, kvs);
        }
        return this;
    }

    /**
     * A "ready-made" consumer that can be used to "respond" to the
     * different types of Nodes containing names that are represented by the _name
     * interface and matches with $name.
     *
     * for example:
     *
     */
    public static class $nameConsumer implements Consumer<_name> {

        public Consumer<SimpleName> simpleNameConsumer = t->{};
        public Consumer<Name> nameConsumer = t->{};
        /** this on a MethodReferenceExpr specifically on the "Id" String field stored */
        public Consumer<MethodReferenceExpr> methodReferenceConsumer = t->{};

        public $nameConsumer(){}

        @Override
        public void accept(_name _n) {
            if(_n.name instanceof SimpleName){
                simpleNameConsumer.accept( (SimpleName)_n.name);
                return;
            }
            if(_n.name instanceof Name){
                nameConsumer.accept( (Name)_n.name);
                return;
            }
            if(_n.name instanceof MethodReferenceExpr){
                methodReferenceConsumer.accept( (MethodReferenceExpr) _n.name);
                return;
            }
            throw new _jdraftException("Found a name that is not Name, SimpleName, or MethodReference");
        }

        public $nameConsumer onSimpleName(Consumer<SimpleName> snc){
            this.simpleNameConsumer = snc;
            return this;
        }

        public $nameConsumer onName( Consumer<Name> nc){
            this.nameConsumer = nc;
            return this;
        }

        public $nameConsumer onMethodReference( Consumer<MethodReferenceExpr> mrc){
            this.methodReferenceConsumer = mrc;
            return this;
        }
    }

    /**
     * An Or entity that can match against any of the $name instances
     */
    public static class Or extends $name { //implements $selector<String, $name>, $methodCall.$part{

        public Predicate<_name> predicate = p-> true;

        public List<$name> $names = new ArrayList<>();

        private Or($name...nms){
            Arrays.stream(nms).forEach(n-> $names.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Predicate<_name> getPredicate(){
            return this.predicate;
        }

        public $name setPredicate( Predicate<_name> predicate){
            this.predicate = predicate;
            return this;
        }

        @Override
        public Select<_name> select(_name candidate) {
            if( predicate.test(candidate) ) {
                Optional<$name> on = $names.stream().filter(n -> n.matches(candidate)).findFirst();
                if (on.isPresent()) {
                    return on.get().select(candidate);
                }
            }
            return null;
        }

        @Override
        public $name $and(Predicate<_name> matchFn) {
            this.predicate = this.predicate.and(matchFn);
            return null;
        }
    }

}
