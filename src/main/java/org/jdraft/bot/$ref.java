package org.jdraft.bot;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;

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
public class $ref implements $bot<Node, _java._node, $ref>,
        $selector<_java._node, $ref>, Template<_java._node>{

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
        return new Or( $ref.of(clazz.getCanonicalName()),
                //I need to match Simple Name references of entities that import the class
                $ref.of(clazz.getSimpleName()).$isInCodeUnit(cu-> cu.hasImport(clazz)));
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

    public static $ref of(Stencil stencil ){
        return new $ref(stencil);
    }

    /** the pattern of the name*/
    public Stencil stencil = null;

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
                return predicate.test(null) && this.matchAnnoNames && this.matchAnnoMemberValueNames
                        && this.matchImports && this.matchMethodNames && this.matchMethodReferences
                        && this.matchPackageNames && this.matchParameterNames && this.matchTypeDeclarationNames
                        && this.matchTypeRefNames && this.matchVariableNames;
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $ref $and(Predicate<_java._node> matchFn) {
        this.predicate = predicate.and(matchFn);
        return this;
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

    //these need to go into $ref
    //matchTypeArguments
    //matchTypeParameters
    //matchThrows
    
    public Boolean matchConstructorNames = true;

    public $ref $matchConstructorNames(Boolean b){
        this.matchConstructorNames = b;
        return this;
    }

    public Boolean matchMethodNames = true;

    public $ref $matchMethodNames(Boolean b){
        this.matchMethodNames = b;
        return this;
    }

    public Boolean matchImports = true;

    public $ref $matchImports(Boolean b){
        this.matchImports = b;
        return this;
    }

    public Boolean matchMethodReferences = true;

    public $ref $matchMethodReferences(Boolean b){
        this.matchMethodReferences = b;
        return this;
    }

    public Boolean matchAnnoMemberValueNames = true;

    public $ref $matchAnnoMemberValueNames(Boolean b){
        this.matchAnnoMemberValueNames = b;
        return this;
    }

    public Boolean matchAnnoNames = true;

    public $ref $matchAnnoNames(Boolean b){
        this.matchAnnoNames = b;
        return this;
    }

    public Boolean matchParameterNames = true;

    public $ref $matchParameterNames(Boolean b){
        this.matchParameterNames = b;
        return this;
    }

    public Boolean matchPackageNames = true;

    public $ref $matchPackageNames(Boolean b){
        this.matchPackageNames = b;
        return this;
    }

    public Boolean matchVariableNames = true;

    public $ref $matchVariableNames(Boolean b){
        this.matchVariableNames = b;
        return this;
    }

    public Boolean matchTypeDeclarationNames = true;

    public $ref $matchTypeDeclarationNames(Boolean b){
        this.matchTypeDeclarationNames = b;
        return this;
    }

    public Boolean matchTypeRefNames = true;

    public $ref $matchTypeRefNames(Boolean b){
        this.matchTypeRefNames = b;
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

            //System.out.println( "PARENT " + parent+" "+ parent.getClass()+" "+parent.getClass().getCanonicalName());
            //Print.tree( candidate.ast().getParentNode().get() );
            if( parent.getClass() == Name.class){
                return false;
            }
            if( parent instanceof ClassOrInterfaceType){
                ClassOrInterfaceType coit = (ClassOrInterfaceType)parent;
                if( !coit.getTypeArguments().isPresent()){
                    return false;
                }
                //System.out.println( "Its a Type Argument");
            }
            if( parent instanceof ImportDeclaration){
                return false;
            }
        }
        if( ! matchAnnoMemberValueNames && isAnnoMemberValueName(candidate.ast()) ){
            return false;
        }
        if( ! matchAnnoNames && isAnnoName(candidate.ast())){
            return false;
        }
        if( !matchConstructorNames && isConstructorName(candidate.ast()) ){
            return false;
        }
        if( isImportName(candidate.ast()) ){

            if( !(this.matchImports) ){
                return false;
            }
        }
        if( ! matchMethodNames && isMethodName(candidate.ast())){
            return false;
        }
        if( !matchPackageNames && isPackageName(candidate.ast())){
            return false;
        }
        if( !matchParameterNames && isParameterName(candidate.ast()) ){
            return false;
        }
        if( !matchVariableNames && isVariableName(candidate.ast())){
            return false;
        }
        if( ! matchMethodReferences && isMethodReference(candidate.ast()) ){
            return false;
        }
        if( !matchTypeDeclarationNames && isTypeDeclarationName(candidate.ast())){
            return false;
        }
        if( ! matchTypeRefNames && isTypeRefName(candidate.ast()) ){
            return false;
        }
        return true;
    }

    public Select<_java._node > select(_java._node candidate){
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

    public static boolean isAnnoName(Node name){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof AnnotationExpr;
    }

    public static boolean isAnnoMemberValueName(Node name){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof MemberValuePair;
    }

    /** Is this "name" type one that is being used in the context of a package name? */
    public static boolean isPackageName(Node name){
        return name.getParentNode().isPresent() && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof PackageDeclaration);
    }

    public static boolean isMethodName(Node name){
        return name.getParentNode().isPresent()
                &&
                (name.getParentNode().get() instanceof MethodDeclaration
                        || name.getParentNode().get() instanceof MethodCallExpr);
    }

    public static boolean isConstructorName(Node name){
        return name.getParentNode().isPresent()
                &&
                (name.getParentNode().get() instanceof ConstructorDeclaration
                        || name.getParentNode().get() instanceof ObjectCreationExpr); //new
    }

    /** Is this "name" type one that is being used in the context of a variable name? */
    public static boolean isVariableName(Node name){
        return name instanceof SimpleName && name.getParentNode().isPresent() && name.getParentNode().get() instanceof VariableDeclarator;
    }

    /** Is this "name" type one that is being used in a parameter */
    public static boolean isParameterName(Node name){
        return name instanceof SimpleName && name.getParentNode().isPresent() && name.getParentNode().get() instanceof Parameter;
    }

    public static boolean isTypeRefName(Node name){
        return name instanceof Type || name.getParentNode().isPresent()
                && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof Type);
    }

    /** Is this name being used as "part" or a whole type name*/
    public static boolean isTypeDeclarationName(Node name){
        return name.getParentNode().isPresent() && name.getParentNode().get() instanceof TypeDeclaration;
    }

    /** Is this name a "part" of a MethodReference? */
    public boolean isMethodReference(Node name){
        return name instanceof MethodReferenceExpr || name.getParentNode().isPresent() &&
                name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof MethodReferenceExpr);
    }

    /** Is this name being used as "part" or a whole import name? */
    public static boolean isImportName(Node name){
        boolean is = name instanceof ImportDeclaration ||
                (name.getParentNode().isPresent() && name.stream(Node.TreeTraversal.PARENTS).anyMatch(n -> n instanceof ImportDeclaration) );
        return is;
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
    public $ref $(String target, String $Name) {
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
            or.matchAnnoMemberValueNames = this.matchAnnoMemberValueNames;
            or.matchAnnoNames = this.matchAnnoNames;
            or.matchConstructorNames = this.matchConstructorNames;
            or.matchImports = this.matchImports;
            or.matchMethodNames = this.matchMethodNames;
            or.matchMethodReferences = this.matchMethodReferences;
            or.matchPackageNames = this.matchPackageNames;
            or.matchParameterNames = this.matchParameterNames;
            or.matchTypeDeclarationNames = this.matchTypeDeclarationNames;
            or.matchTypeRefNames = this.matchTypeRefNames;
            or.matchVariableNames = this.matchVariableNames;

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
