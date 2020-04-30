package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.Type;
import org.jdraft._jdraftException;
import org.jdraft._name;
import org.jdraft.text.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class $name implements $bot<Node, _name, $name>,
        $selector<_name, $name>, Template<_name>, $methodCall.$part {

    public static $name of() {
        return new $name();
    }

    public static $name of(Expression e) {
        return new $name(e.toString());
    }

    public static $name of(String stencil) {
        return new $name(stencil);
    }

    /*
    public static $name of(Class clazz) {
        return new Or($name.of(clazz.getCanonicalName()),
                        //I need to match Simple Name references of entities that import the class
                $name.of(clazz.getSimpleName()).$isInCodeUnit(cu -> cu.hasImport(clazz)));
    }
     */


    public static $name of( _name.Use...nameUses ){
        $name $n = of();
        $n.excludedUses.addAll(_name.Use.all());
        $n.excludedUses.removeAll( Arrays.stream(nameUses).collect(Collectors.toList()) );
        return $n;
    }

    public static $name startsWith(String text ){
        return of( text+"$after$");
    }

    public static $name endsWith(String text){
        return of( "$before$"+text);
    }

    public static $name contains( String text ){
        return of("$before$"+text+"$after$");
    }

    public static $name of( Stencil stencil ){
        return new $name(stencil);
    }

    /** the pattern of the name*/
    public Stencil stencil = null;

    /**
     * Rather than tracking When the can be matched, we track only when we want to exclude matching based on use,
     * by default we ALWAYS MATCH regardless of use, here we can define when we DONT MATCH by explicitly saying.
     */
    public Set<_name.Use> excludedUses = new HashSet<>();

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

    /**
     * Exclude matching names that are used in these contexts
     * (NOTE: by default no uses are excluded)
     * @param usages uses that are forbidden to be matched
     * @return the modified $name bot
     */
    public $name $exclude(_name.Use... usages ){
        Arrays.stream(usages).forEach(e -> this.excludedUses.add( e ) );
        return this;
    }

    /**
     * Include matching names that are used in these contexts
     * (NOTE: by default ALL uses are included, this will only change )
     * @param usages uses that are forbidden to be matched
     * @return the modified $name bot
     */
    public $name $include(_name.Use... usages ){
        Arrays.stream(usages).forEach(e -> this.excludedUses.remove( e ) );
        return this;
    }

    /** build another mutable copy of this bot */
    public $name copy(){
        $name copy = of().$and(this.predicate.and(t->true));
        if(this.stencil != null ){
            copy.stencil = Stencil.of( this.stencil );
        }
        this.excludedUses.forEach(e -> copy.excludedUses.add(e));
        return copy;
    }

    @Override
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
                return this.excludedUses.isEmpty() && predicate.test(null);
                //this.matchAnnoNames && this.matchAnnoMemberValueNames
                //        && this.matchImports && this.matchMethodNames && this.matchMethodReferences
                //        && this.matchPackageNames && this.matchParameterNames && this.matchTypeDeclarationNames
                //        && this.matchTypeRefNames && this.matchVariableNames && matchEnumConstantNames;
            } catch(Exception e){
                System.out.println( "Failed predicate test ");
                return false;
            }
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

    //public Set<_name.Use> excludedUses = new HashSet<>();

    /** Is this name being used as "part" or a whole _annotation/AnnotationDeclaration name i.e. "A" within "@interface A{}" */
    //public boolean matchAnnotationNames = true;

    public $name $matchAnnotationNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNOTATION_NAME);
        } else{
            excludedUses.add(_name.Use.ANNOTATION_NAME);
        }
        return this;
    }

    /** Is this name being used as "part" or a whole _annotation._element/AnnotationMemberDeclaration name */
    //public boolean matchAnnotationElementNames = true;

    public $name $matchAnnotationElementNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNOTATION_ELEMENT_NAME);
        } else{
            excludedUses.add(_name.Use.ANNOTATION_ELEMENT_NAME);
        }
        return this;
        //this.matchAnnotationElementNames = b;
        //return this;
    }

    //public boolean matchAnnoMemberValueNames = true;

    public $name $matchAnnoMemberValueNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNO_MEMBER_VALUE_NAME);
        } else{
            excludedUses.add(_name.Use.ANNO_MEMBER_VALUE_NAME);
        }
        return this;
        //this.matchAnnoMemberValueNames = b;
        //return this;
    }

    //public boolean matchAnnoNames = true;

    public $name $matchAnnoNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNO_NAME);
        } else{
            excludedUses.add(_name.Use.ANNO_NAME);
        }
        return this;
        //this.matchAnnoNames = b;
        //return this;
    }

    /** Is this name being used as "part" or a whole _class/ClassOrInterfaceDeclaration name i.e. "C" within "class C{}" */
    //public boolean matchClassNames = true;

    public $name $matchClassNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CLASS_NAME);
        } else{
            excludedUses.add(_name.Use.CLASS_NAME);
        }
        return this;

        //this.matchClassNames = b;
        //return this;
    }

    //public boolean matchConstructorNames = true;

    public $name $matchConstructorNames( boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CONSTRUCTOR_NAME);
        } else{
            excludedUses.add(_name.Use.CONSTRUCTOR_NAME);
        }
        return this;
    }

    /** Is this name being used as "part" or a whole _enum/EnumDeclaration name i.e. "E" within "enum E{ ; }" */
    //public boolean matchEnumNames = true;

    public $name $matchEnumNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ENUM_NAME);
        } else{
            excludedUses.add(_name.Use.ENUM_NAME);
        }
        return this;
        //this.matchEnumNames = b;
        //return this;
    }

    //public boolean matchMethodNames = true;

    public $name $matchMethodNames( boolean b){
        if( b ){
            excludedUses.remove(_name.Use.METHOD_NAME);
        } else{
            excludedUses.add(_name.Use.METHOD_NAME);
        }
        return this;
        //this.matchMethodNames = b;
        //return this;
    }

    //public boolean matchLabels = true;

    public $name $matchLabels(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.BREAK_LABEL_NAME);
            excludedUses.remove(_name.Use.CONTINUE_LABEL_NAME);
            excludedUses.remove(_name.Use.LABELED_STATEMENT_LABEL_NAME);
        } else{
            excludedUses.add(_name.Use.BREAK_LABEL_NAME);
            excludedUses.add(_name.Use.CONTINUE_LABEL_NAME);
            excludedUses.add(_name.Use.LABELED_STATEMENT_LABEL_NAME);
        }
        return this;
        //this.matchLabels = b;
        //return this;
    }

    //public boolean matchContinueLabels = true;

    public $name $matchContinueLabels(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CONTINUE_LABEL_NAME);
        } else{
            excludedUses.add(_name.Use.CONTINUE_LABEL_NAME);
        }
        return this;
        //this.matchContinueLabels = b;
        //return this;
    }

    //public boolean matchBreakLabels = true;

    public $name $matchBreakLabels(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.BREAK_LABEL_NAME);
        } else{
            excludedUses.add(_name.Use.BREAK_LABEL_NAME);
        }
        return this;

        //this.matchBreakLabels = b;
        //return this;
    }

    //public boolean matchLabeledStatementLabels = true;

    public $name $matchLabeledStatementLabels(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.LABELED_STATEMENT_LABEL_NAME);
        } else{
            excludedUses.add(_name.Use.LABELED_STATEMENT_LABEL_NAME);
        }
        return this;

        //this.matchLabeledStatementLabels = b;
        //return this;
    }

    //public boolean matchImports = true;

    public $name $matchImports(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.IMPORT_NAME);
        } else{
            excludedUses.add(_name.Use.IMPORT_NAME);
        }
        return this;
        //this.matchImports = b;
        //return this;
    }

    //public boolean matchMethodReferences = true;

    public $name $matchMethodReferences( boolean b){
        if( b ){
            excludedUses.remove(_name.Use.METHOD_REFERENCE_NAME);
        } else{
            excludedUses.add(_name.Use.METHOD_REFERENCE_NAME);
        }
        return this;

        //this.matchMethodReferences = b;
        //return this;
    }

    /** Is this name being used as "part" or a whole _interface/ClassOrInterfaceDeclaration name i.e. "I" within "interface I{}" */
    //public boolean matchInterfaceNames = true;

    public $name $matchInterfaceNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.INTERFACE_NAME);
        } else{
            excludedUses.add(_name.Use.INTERFACE_NAME);
        }
        return this;
        //this.matchInterfaceNames = b;
        //return this;
    }

    /** Is the name being used as an Enum Constant (i.e. "CLUBS" in "enum Suit{ CLUBS, HEARTS, DIAMONDS, SPADES; }" */
    //public boolean matchEnumConstantNames = true;
    //public boolean matchParameterNames = true;
    //public boolean matchPackageNames = true;
    //public boolean matchVariableNames = true;
    //public boolean matchTypeDeclarationNames = true;
    //public boolean matchTypeRefNames = true;

    public $name $matchEnumConstantNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ENUM_CONSTANT_NAME);
        } else{
            excludedUses.add(_name.Use.ENUM_CONSTANT_NAME);
        }
        return this;
        //this.matchEnumConstantNames = b;
        //return this;
    }

    public $name $matchParameterNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.PARAMETER_NAME);
        } else{
            excludedUses.add(_name.Use.PARAMETER_NAME);
        }
        return this;
        //this.matchParameterNames = b;
        //return this;
    }

    public $name $matchPackageNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.PACKAGE_NAME);
        } else{
            excludedUses.add(_name.Use.PACKAGE_NAME);
        }
        return this;
        //this.matchPackageNames = b;
        //return this;
    }

    public $name $matchVariableNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.VARIABLE_NAME);
        } else{
            excludedUses.add(_name.Use.VARIABLE_NAME);
        }
        return this;
        //this.matchVariableNames = b;
        //return this;
    }

    public $name $matchTypeDeclarationNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CLASS_NAME);
            excludedUses.remove(_name.Use.INTERFACE_NAME);
            excludedUses.remove(_name.Use.ENUM_NAME);
            excludedUses.remove(_name.Use.ANNOTATION_NAME);
        } else{
            excludedUses.add(_name.Use.CLASS_NAME);
            excludedUses.add(_name.Use.INTERFACE_NAME);
            excludedUses.add(_name.Use.ENUM_NAME);
            excludedUses.add(_name.Use.ANNOTATION_NAME);
        }
        return this;
        //this.matchTypeDeclarationNames = b;
        //return this;
    }

    /*
    public $name $matchTypeRefNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.TYPE_REF_NAME);
        } else{
            excludedUses.add(_name.Use.TYPE_REF_NAME);
        }
        return this;
        //this.matchTypeRefNames = b;
        //return this;
    }
     */

    /**
     *
     * @param candidate
     * @return
     */
    private boolean useCheck( _name candidate ){
        //BECAUSE we dont want overlap matches for "nested names" like:
        //"package base.sub.end;" PackageDeclaration : (-)
        //  └─"base.sub.end" Name : (1,1)-(1,12)
        //    └─"base.sub" Name : (1,1)-(1,8)
        //      └─"base" Name : (1,1)-(1,4)
        //...when we might have a $name like $name.of("base$any$") we only want to match the TOP level MATCH
        // (i.e. the "base.sub.end" name)


        if( candidate.ast().getParentNode().isPresent() ){
            Node parent = candidate.ast().getParentNode().get();
            if( parent.getClass() == Name.class ){
                return false;
            }
            if( parent instanceof Type){
                return false;
            }
        }

        /*
        if( candidate.ast().getParentNode().isPresent() ){

        }

         */
        /*
        if( !(candidate.ast() instanceof MethodReferenceExpr) ){
            boolean hasTypeExpressionParent = candidate.ast().getParentNode().get().stream(Node.TreeTraversal.PARENTS).anyMatch(n-> n instanceof TypeExpr);
            return !hasTypeExpressionParent;

            if( !hasTypeExpressionParent ) {
                System.out.println("NOT HAS TYPE EXPRESSION"+candidate.ast().getClass() );
                boolean hasTypeParents = candidate.ast().getParentNode().get().stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof Type);
                System.out.println("HAS TYPE PARENTS"+candidate.ast().getClass() );
                return !hasTypeParents;
            }

        }
         */
        return !this.excludedUses.stream().anyMatch(e-> {
            //System.out.println("Checking "+ e+" with "+candidate.ast()+System.lineSeparator()+ new ASCIITreePrinter().output(candidate.ast().getParentNode().get()) );
            boolean match = e.is(candidate.ast());
            //System.out.println( "MATCHED "+ match);
            return match;
        });
        /*
        //we only match "top level" parents names

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
        if( !matchTypeDeclarationNames && candidate.isTypeName()){
            return false;
        }
        if( ! matchTypeRefNames && candidate.isTypeRefName() ){
            return false;
        }
        return true;
         */
    }

    public Select<_name> select(_name candidate){
        if( isMatchAny()){
            return new Select<>(candidate, new Tokens());
        }
        if( this.stencil == null ){
            //System.out.println( "STENCIL IS NULL" );
            if( useCheck(candidate) && this.predicate.test(candidate) ){
                return new Select<>(candidate, new Tokens());
            }
            return null;
        }
        //System.out.println( "STENCIL IS \""+ this.stencil +"\"");
        if( useCheck(candidate) && this.predicate.test(candidate) ){

            String str = candidate.toString();
            //System.out.println("TRYING TO MATCH " +str);

            Tokens ts = this.stencil.parse(str);
            //System.out.println( "HERE>> "+candidate+ " "+ ts);

            if( ts != null ) {
                return new Select<>(candidate, ts);
            }
            if( candidate.name instanceof com.github.javaparser.ast.expr.Name){
                //System.out.println( " "+ candidate.name);
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
     * Or bot to inspect/match/select based on a few independent $name $bot instances
     */
    public static class Or extends $name {

        public List<$name> $nameBots = new ArrayList<>();

        private Or($name...nms){
            Arrays.stream(nms).forEach(n-> $nameBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public $name.Or copy(){

            $name.Or or = new $name.Or();
            $nameBots.forEach($nb -> or.$nameBots.add($nb.copy()));
            or.$and( this.predicate.and(t->true) );
            if( this.stencil != null ) {
                or.stencil = this.stencil.copy();
            }
            this.excludedUses.forEach( e-> or.excludedUses.add(e));
            /*
            //port over the common preferences
            or.matchImports = this.matchImports;
            or.matchMethodNames = this.matchMethodNames;
            or.matchMethodReferences = this.matchMethodReferences;
            or.matchPackageNames = this.matchPackageNames;
            or.matchParameterNames = this.matchParameterNames;
            or.matchTypeDeclarationNames = this.matchTypeDeclarationNames;
            or.matchTypeRefNames = this.matchTypeRefNames;
            or.matchVariableNames = this.matchVariableNames;
            or.matchConstructorNames = this.matchConstructorNames;
            or.matchAnnoNames = this.matchAnnoNames;
            or.matchAnnoMemberValueNames = this.matchAnnoMemberValueNames;
            */
            return or;
        }

        @Override
        public Select<_name> select(_name _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $name $whichBot = whichMatch(_candidate);
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
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param candidate
         * @return
         */
        public $name whichMatch(_name candidate){
            Optional<$name> orsel  = $nameBots.stream().filter($p-> $p.matches( candidate ) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public List<$name> listBots(){
            return this.$nameBots;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$name.Or{").append(System.lineSeparator());
            for(int i = 0; i< listBots().size(); i++){
                sb.append( Text.indent( this.listBots().get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }

}
