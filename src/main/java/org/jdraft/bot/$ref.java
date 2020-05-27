package org.jdraft.bot;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Bot useful for looking for "references" to a specific Class or group of classes.
 *
 * this is useful because sometimes (the underlying AST implementation of a "reference" to some entity
 * (like a class) name is handled using different models:
 * <PRE>
 * for example we might:
 * 1) import this class
 *  import aaaa.bbbb.C;
 *
 * 2) use a class as a simple type a field declaration
 * (in a fully qualified way):
 *  aaaa.bbbb.C c;
 *
 * (in an unqualified way):
 * C c;
 *
 * 3) use the type within a Generic
 * (in a fully qualified way):
 * Optional<aaaa.bbbb.C> c;
 * (in an unqualified way):
 * Optional<C> c;
 *
 * 4) the class may be an annotation
 * (in a fully qualified way):
 *  @aaaa.bbbb.C(key=1)
 *
 * (in an unqualified way):
 * @C(key=1)
 *
 * 5)the class could be represented in a thrown clause
 * (in a fully qualified way):
 * void m() throws aaaa.bbbb.C{...}
 *
 * (in an unqualified way)
 * void m() throws
 *
 * 6) the class can be referenced in a methodReference
 * (fully qualified)
 * Consumer<P> aConsumer = aaaa.bbbb.C::doSomething;
 *
 * (unqualified)
 * Consumer<P> aConsumer = C::doSomething;
 *
 * ...(and other situations)
 * for each of the above cases the implementation make it difficult to unify these references
 * (sometimes the reference is implemented as a Name in the AST, sometimes a SimpleName, sometimes
 * a {@link Type}, and sometimes as a String field.
 *
 * $ref unifies this so that semantically we have a bot that can match and "understand" what
 * situations we encounter within an AST are really references we might want to query, inspect, and modify
 * </PRE>
 *
 */
public class $ref implements $bot<_java._node, $ref>,
        $selector<_java._node, $ref>, Template<_java._node>{

    public static final _name.Use ANNO_ENTRY_PAIR_NAME = _name.Use.ANNO_ENTRY_PAIR_NAME;
    public static final _name.Use ANNO_EXPR_NAME = _name.Use.ANNO_EXPR_NAME;
    public static final _name.Use ANNOTATION_ENTRY_NAME = _name.Use.ANNOTATION_ENTRY_NAME;
    public static final _name.Use ANNOTATION_DECLARATION_NAME = _name.Use.ANNOTATION_DECLARATION_NAME;
    public static final _name.Use BREAK_LABEL_NAME = _name.Use.BREAK_LABEL_NAME;
    public static final _name.Use CLASS_DECLARATION_NAME = _name.Use.CLASS_DECLARATION_NAME;
    public static final _name.Use CONSTRUCTOR_NAME = _name.Use.CONSTRUCTOR_NAME;
    public static final _name.Use CONTINUE_LABEL_NAME = _name.Use.CONTINUE_LABEL_NAME;
    public static final _name.Use ENUM_DECLARATION_NAME = _name.Use.ENUM_DECLARATION_NAME;
    public static final _name.Use ENUM_CONSTANT_NAME = _name.Use.ENUM_CONSTANT_NAME;
    public static final _name.Use IMPORT_NAME = _name.Use.IMPORT_NAME;
    public static final _name.Use INTERFACE_DECLARATION_NAME = _name.Use.INTERFACE_DECLARATION_NAME;
    public static final _name.Use LABELED_STATEMENT_LABEL_NAME = _name.Use.LABELED_STATEMENT_LABEL_NAME;
    public static final _name.Use METHOD_NAME = _name.Use.METHOD_NAME;
    public static final _name.Use METHOD_REFERENCE_NAME = _name.Use.METHOD_REFERENCE_NAME;
    public static final _name.Use PACKAGE_NAME = _name.Use.PACKAGE_NAME;
    public static final _name.Use PARAM_NAME = _name.Use.PARAM_NAME;
    public static final _name.Use TYPE_PARAM_NAME = _name.Use.TYPE_PARAM_NAME;
    public static final _name.Use TYPE_REF_NAME = _name.Use.TYPE_REF_NAME;

    public static final _name.Use VARIABLE_NAME = _name.Use.VARIABLE_NAME;

    public static $ref of(){
        return new $ref();
    }

    /**
     * A reference to a specific Class
     * @param clazz
     * @return
     */
    public static $ref of(Class clazz){
        //I need to find fully qualified references to a given class
        return new Or( $ref.of(clazz.getCanonicalName()), //ANNOTATION_NAME, ENUM_NAME, INTERFACE_NAME, CLASS_NAME, TYPE_REF_NAME, METHOD_REFERENCE_NAME, IMPORT_NAME, ANNO_NAME, CONSTRUCTOR_NAME),
                //I need to match Simple Name references of entities that import the class
                $ref.of(clazz.getSimpleName()).$isInCodeUnit(cu-> cu.hasImport(clazz)));
    }

    public static $ref startsWith(String namePrefix){
        return of(namePrefix+"$after$");
    }

    public static $ref startsWith(String namePrefix, _name.Use...nameUses){
        return startsWith(namePrefix).$and(nameUses);
    }

    public static $ref endsWith(String namePostfix){
        return of("$before$"+namePostfix);
    }

    public static $ref endsWith(String namePostfix, _name.Use...nameUses){
        return endsWith(namePostfix).$and(nameUses);
    }

    public static $ref contains(String nameContains){
        return of("$before$"+nameContains+"$after$");
    }

    public static $ref contains(String nameContains, _name.Use...nameUses){
        return of("$before$"+nameContains+"$after$").$and(nameUses);
    }

    public static $ref of(Node n){
        if( n instanceof Name) {
            return new $ref(n.toString());
        }
        if( n instanceof ImportDeclaration ){
            ImportDeclaration id = (ImportDeclaration)n;
            return new $ref(id.getNameAsString());
        }
        if( n instanceof ClassOrInterfaceType){
            return new $ref( n.toString() );
        }
        if( n instanceof MethodDeclaration ){
            return new $ref(((MethodDeclaration) n).asMethodDeclaration().getTypeAsString());
        }
        throw new _jdraftException("Could not create ref from node type "+n.getClass());
    }

    public static $ref of(String stencil){
        return new $ref(stencil);
    }

    public static $ref of(_name.Use...nameUses){
        return of().$and(nameUses);
    }

    public static $ref of(String stencil, _name.Use...nameUses){
        return of(stencil).$and(nameUses);
    }

    public static $ref of(Stencil stencil ){
        return new $ref(stencil);
    }

    /** the pattern of the name*/
    public Stencil stencil = null;

    /** situations where the name is used in an excluded context */
    public Set<_name.Use> excludedUses = new HashSet<>();

    /**  */
    public Predicate<_java._node> predicate = t -> true;

    public $ref(){}

    public $ref(String stencil){
        this.stencil = Stencil.of( stencil);
    }

    public $ref(Stencil stencil){
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
    public $ref copy(){
        $ref copy = of().$and(this.predicate.and(t->true));
        if(this.stencil != null ){
            copy.stencil = Stencil.of( this.stencil );
        }
        return copy;
    }

    public Predicate<_java._node> getPredicate(){
        return this.predicate;
    }

    @Override
    public $ref setPredicate(Predicate<_java._node> predicate) {
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
                //        && this.matchTypeRefNames && this.matchVariableNames;
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $ref $and(Predicate<_java._node> matchFn) {
        this.predicate = predicate.and(matchFn);
        return this;
    }

    public $ref $and( _name.Use...nameUses){
        this.excludedUses = _name.Use.allExcept(nameUses);
        return this;
    }

    public $ref $not( $ref... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    public boolean matches( Node n){
        return select(n) != null;
    }

    public boolean matches(String str ){
        return select((_java._node)_java.of(Types.of(str))) != null;
    }

    public Select<_java._node> select(String s){
        return select( (_java._node)_java.of(Types.of(s)));
    }

    public Select<_java._node> select( String...str){
        return select( _name.of(str));
    }

    public Select<_java._node> select( Node n ){
        if( n instanceof Name || n instanceof SimpleName || n instanceof Type || n instanceof ImportDeclaration){ //|| n instanceof MethodReferenceExpr
            return select( (_java._node)_java.of(n) );
        }
        return null;
    }

    @Override
    public Select<_java._node> selectFirstIn(Node astNode, Predicate<Select<_java._node>> predicate) {
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

    /** Is this name being used as "part" or a whole _annotation/AnnotationDeclaration name i.e. "A" within "@interface A{}" */
    public $ref $matchAnnotationNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNOTATION_DECLARATION_NAME);
        } else{
            excludedUses.add(_name.Use.ANNOTATION_DECLARATION_NAME);
        }
        return this;
    }

    /** Is this name being used as "part" or a whole _annotation._element/AnnotationMemberDeclaration name */
    public $ref $matchAnnotationElementNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNOTATION_ENTRY_NAME);
        } else{
            excludedUses.add(_name.Use.ANNOTATION_ENTRY_NAME);
        }
        return this;
    }

    public $ref $matchAnnoMemberValueNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNO_ENTRY_PAIR_NAME);
        } else{
            excludedUses.add(_name.Use.ANNO_ENTRY_PAIR_NAME);
        }
        return this;
    }

    public $ref $matchAnnoNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ANNO_EXPR_NAME);
        } else{
            excludedUses.add(_name.Use.ANNO_EXPR_NAME);
        }
        return this;
    }

    /** Is this name being used as "part" or a whole _class/ClassOrInterfaceDeclaration name i.e. "C" within "class C{}" */
    public $ref $matchClassNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CLASS_DECLARATION_NAME);
        } else{
            excludedUses.add(_name.Use.CLASS_DECLARATION_NAME);
        }
        return this;
    }

    public $ref $matchConstructorNames( boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CONSTRUCTOR_NAME);
        } else{
            excludedUses.add(_name.Use.CONSTRUCTOR_NAME);
        }
        return this;
    }

    /** Is this name being used as "part" or a whole _enum/EnumDeclaration name i.e. "E" within "enum E{ ; }" */
    public $ref $matchEnumNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ENUM_DECLARATION_NAME);
        } else{
            excludedUses.add(_name.Use.ENUM_DECLARATION_NAME);
        }
        return this;
    }

    public $ref $matchMethodNames( boolean b){
        if( b ){
            excludedUses.remove(_name.Use.METHOD_NAME);
        } else{
            excludedUses.add(_name.Use.METHOD_NAME);
        }
        return this;
    }

    public $ref $matchLabels(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.BREAK_LABEL_NAME);
            excludedUses.remove(_name.Use.CONTINUE_LABEL_NAME);
            excludedUses.remove(_name.Use.LABELED_STATEMENT_LABEL_NAME);
        } else{
            excludedUses.add(_name.Use.BREAK_LABEL_NAME);
            excludedUses.add(CONTINUE_LABEL_NAME);
            excludedUses.add(LABELED_STATEMENT_LABEL_NAME);
        }
        return this;
    }

    public $ref $matchContinueLabels(boolean b){
        if( b ){
            excludedUses.remove(CONTINUE_LABEL_NAME);
        } else{
            excludedUses.add(CONTINUE_LABEL_NAME);
        }
        return this;
    }

    public $ref $matchBreakLabels(boolean b){
        if( b ){
            excludedUses.remove(BREAK_LABEL_NAME);
        } else{
            excludedUses.add(BREAK_LABEL_NAME);
        }
        return this;
    }

    public $ref $matchLabeledStatementLabels(boolean b){
        if( b ){
            excludedUses.remove(LABELED_STATEMENT_LABEL_NAME);
        } else{
            excludedUses.add(LABELED_STATEMENT_LABEL_NAME);
        }
        return this;
    }

    public $ref $matchImports(boolean b){
        if( b ){
            excludedUses.remove(IMPORT_NAME);
        } else{
            excludedUses.add(IMPORT_NAME);
        }
        return this;
    }

    public $ref $matchMethodReferences( boolean b){
        if( b ){
            excludedUses.remove(METHOD_REFERENCE_NAME);
        } else{
            excludedUses.add(METHOD_REFERENCE_NAME);
        }
        return this;
    }

    public $ref $matchTypeParameters( boolean b){
        if( b ){
            excludedUses.remove(TYPE_PARAM_NAME);
        } else{
            excludedUses.add(TYPE_PARAM_NAME);
        }
        return this;
    }

    /** Is this name being used as "part" or a whole _interface/ClassOrInterfaceDeclaration name i.e. "I" within "interface I{}" */
    public $ref $matchInterfaceNames(boolean b){
        if( b ){
            excludedUses.remove(INTERFACE_DECLARATION_NAME);
        } else{
            excludedUses.add(INTERFACE_DECLARATION_NAME);
        }
        return this;
    }

    /** Is the name being used as an Enum Constant (i.e. "CLUBS" in "enum Suit{ CLUBS, HEARTS, DIAMONDS, SPADES; }" */

    public $ref $matchEnumConstantNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.ENUM_CONSTANT_NAME);
        } else{
            excludedUses.add(_name.Use.ENUM_CONSTANT_NAME);
        }
        return this;
    }

    public $ref $matchParameterNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.PARAM_NAME);
        } else{
            excludedUses.add(_name.Use.PARAM_NAME);
        }
        return this;
    }

    public $ref $matchPackageNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.PACKAGE_NAME);
        } else{
            excludedUses.add(_name.Use.PACKAGE_NAME);
        }
        return this;
    }

    public $ref $matchVariableNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.VARIABLE_NAME);
        } else{
            excludedUses.add(_name.Use.VARIABLE_NAME);
        }
        return this;
    }

    public $ref $matchTypeDeclarationNames(boolean b){
        if( b ){
            excludedUses.remove(_name.Use.CLASS_DECLARATION_NAME);
            excludedUses.remove(_name.Use.ENUM_DECLARATION_NAME);
            excludedUses.remove(_name.Use.INTERFACE_DECLARATION_NAME);
            excludedUses.remove(_name.Use.ANNOTATION_DECLARATION_NAME);
        } else{
            excludedUses.add(_name.Use.CLASS_DECLARATION_NAME);
            excludedUses.add(_name.Use.ENUM_DECLARATION_NAME);
            excludedUses.add(_name.Use.INTERFACE_DECLARATION_NAME);
            excludedUses.add(_name.Use.ANNOTATION_DECLARATION_NAME);
        }
        return this;
    }

    public $ref $matchTypeRefNames(boolean b){
        if( b ){
            excludedUses.remove( _name.Use.TYPE_REF_NAME );
        } else{
            excludedUses.add( _name.Use.TYPE_REF_NAME );
        }
        return this;
    }

    /**
     *
     * @param candidate
     * @return
     */
    protected boolean useCheck( _java._node candidate ){
        //we only match "top level" parents names *NOT* name nodes nested within name nodes
        if( candidate.ast().getParentNode().isPresent() ){
            Node parent = candidate.ast().getParentNode().get();
            if( parent.getClass() == Name.class){
                //System.out.println( "EXCLUDED BY PARENT IS NAME");
                return false;
            }

            if( parent instanceof ClassOrInterfaceType){
                ClassOrInterfaceType coit = (ClassOrInterfaceType)parent;
                if( !coit.getTypeArguments().isPresent()){
                    //System.out.println( "NO TYPE ARGUMENTS");
                    return false;
                } else{
                    //System.out.println( "TYPE ARGUMENTS" + candidate+" "+ parent);
                }
            }

            //If we have
            // "class C<Y>{}"
            // it has a TypeParameter Y which has a Nested SimpleName Y
            // "class C<Y>{}" ClassOrInterfaceDeclaration
            //  ├─"C" SimpleName
            //  └─"Y" TypeParameter
            //    └─"Y" SimpleName
            // here we only want to "count" the "Y" as a single $ref,
            // therefore we only use the TypeParameter (top-level) and exclude the nested "Y" SimpleName
            if( parent instanceof TypeParameter && candidate.ast() instanceof SimpleName){
                if( parent.toString().equals( candidate.toString() ) ){
                    return false;
                }
            }
            if( parent instanceof ImportDeclaration){
                return false;
            }
        } else{
            //System.out.println("NO PARENT "+ candidate);
        }
        //System.out.println( "NOT EXCLUDED BY DEFAULT");
        return !this.excludedUses.stream().anyMatch(e-> {
            //System.out.println("Checking "+ e+" with "+candidate.ast()+System.lineSeparator()+ new ASCIITreePrinter().output(candidate.ast().getParentNode().get()) );
            boolean match = e.is(candidate.ast());
            //System.out.println( "MATCHED "+ match);
            return match;
        });
    }

    public Select<_java._node > select(_java._node candidate){
        if( isMatchAny()){
            if(useCheck(candidate)) {
                return new Select<>(candidate, new Tokens());
            }
            return null;
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
            if( candidate.ast() instanceof Name){
                //System.out.println( " "+ candidate.name);
                Name nm = (Name) candidate.ast();
                ts = this.stencil.parse(nm.getIdentifier());
                if( ts != null ){ //DONT match is the parent matches
                    if( nm.getParentNode().isPresent() && matches(nm.getParentNode().get()) ){
                        return null;
                    }
                }
            }// else if( candidate.ast() instanceof MethodReferenceExpr){
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
               // MethodReferenceExpr mre = (MethodReferenceExpr) candidate.ast();
                //ts = this.stencil.parse( mre.getIdentifier());
            //}
            else if( candidate.ast() instanceof ClassOrInterfaceType){
                ClassOrInterfaceType mre = (ClassOrInterfaceType) candidate.ast();
                ts = this.stencil.parse( mre.toString());
            }
            else if( candidate instanceof _import){
                ts = this.stencil.parse( ((_import)candidate).getName());
            }
            if( ts != null ){
                return new Select<>(candidate, ts);
            }
        }
        return null;
    }

    /**
     * Exclude matching names that are used in these contexts
     * (NOTE: by default no uses are excluded)
     * @param usages uses that are forbidden to be matched
     * @return the modified $name bot
     */
    public $ref $exclude(_name.Use... usages ){
        Arrays.stream(usages).forEach(e -> this.excludedUses.add( e ) );
        return this;
    }

    /**
     * Include matching names that are used in these contexts
     * (NOTE: by default ALL uses are included, this will only change )
     * @param usages uses that are forbidden to be matched
     * @return the modified $name bot
     */
    public $ref $include(_name.Use... usages ){
        Arrays.stream(usages).forEach(e -> this.excludedUses.remove( e ) );
        return this;
    }

    @Override
    public _name draft(Translator translator, Map<String, Object> keyValues) {
        if( this.stencil == null){
            Object nm = keyValues.get("$ref");
            if( nm == null ){
                throw new _jdraftException( "cannot draft $ref with no Template/Stencil ");
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
    public $ref $(String target, String $Name) {
        if( this.stencil != null ){
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public $ref $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
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

    public $ref $hardcode(Translator translator, Tokens kvs) {
        if( this.stencil != null ){
            this.stencil.$hardcode(translator, kvs);
        }
        return this;
    }

    /**
     * An Or entity that can match against any of the $ref instances
     */
    public static class Or extends $ref {

        public List<$ref> $refBots = new ArrayList<>();

        private Or($ref...nms){
            Arrays.stream(nms).forEach(n-> $refBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public $ref.Or copy(){
            $ref.Or or = new $ref.Or();
            or.$and( this.predicate );

            this.$refBots.forEach( e-> or.$refBots.add( e.copy() ) );
            if( this.stencil != null ) {
                or.stencil = this.stencil.copy();
            }
            this.excludedUses.forEach(e-> or.excludedUses.add(e));
            return or;
        }

        @Override
        public Select<_java._node> select(_java._node _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $ref $whichBot = whichMatch(_candidate);
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
        public $ref whichMatch(_java._node candidate){
            Optional<$ref> orsel  = $refBots.stream().filter($p-> $p.matches( candidate ) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
