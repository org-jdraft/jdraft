package org.jdraft;

import com.github.javaparser.utils.Log;

import com.github.javaparser.*;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.nodeTypes.modifiers.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import static com.github.javaparser.utils.Utils.normalizeEolInTextBlock;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.io._in;

/**
 * Translator for Converting from a String or Java reflective classes to AST
 * Nodes like {@link Statement} and {@link Expression} and {@link CatchClause} {@link MethodDeclaration}
 * {@link ConstructorDeclaration} (using the wonderful JavaParser API)
 *
 * As with many integration projects, the code below is subject to change (it
 * was writen more as a first pass to "git er done", but we shall try to not
 * change the API of this as this is a heavily interally and externally user
 * facing API... sufficive to say features/ methods may be added and the
 * internals may change to be more efficient or better at reporting errors, etc.
 * but the existing API should be stable
 *
 * <PRE>
 * you__________
 *   |          |
 *   |    ----draft----------------
 *   |   /      |       \          \
 *   |  /------AST       \          \
 *   | /        |         \          \
 *   |----------|----------|----------|
 * .java     [javac]     .class     [JVM]
 * </PRE>
 *
 * @author Eric
 */
public enum Ast {
    ;

    /**
     * A static reference to a JavaParser (used to replace StaticJavaParser, in
     * order to properly handle module-info.java files and other "bleeding edge")
     * features
     */    
    public static final JavaParser JAVAPARSER = 
        new JavaParser( new ParserConfiguration().setLanguageLevel(LanguageLevel.BLEEDING_EDGE) );
    
    /*---------------------------------------------------------
       the point of having all of these JavaParser Classes in a
       single place is to simplify / flatten the access to the JavaParser
       API Classes when being used walking the AST.  Since, what we want to be able
       to do is to _walk through the AST implementation easily and it
       is easier to have the IDE autocomplete the NAME of a class when
       it is in one spot...(without remembering the class package).
       i.e. I can look through:
       _walk.list( Ast.WALK<<autocomplete>>, node, Ast.<<autocomplete>>, ... );
                  ------------------------        --------------------
                   tree traversal strategy        node class / interface

       NOTE: if you know the TYPE of component is a Statement or Expression
       you can also use :
       _walk.list( node, Expr.<<autocomplete>>, ... );
                             ----------------
                             expression class

       _walk.list( node, Stmt.<<autocomplete>>, ... );
                             ----------------
                             statement class
       ...this will

       ...which will have less completions to weed through
     */


    /* ------------------NODES----------------------------*/
 /* ( in com.github.javaparser.ast ) */
    public static final Class<Node> NODE = Node.class;

    /**
     * NOTE: Modifier is NOT a Node
     */
    //public static final Class<Modifier> MODIFIER = Modifier.class;
    public static final Class<PackageDeclaration> PACKAGE_DECLARATION = PackageDeclaration.class;
    public static final Class<ImportDeclaration> IMPORT_DECLARATION = ImportDeclaration.class;
    public static final Class<CompilationUnit> COMPILATION_UNIT = CompilationUnit.class;

    /* ----------------------------- BODY ------------------------------------ */
 /* ( in com.github.javaparser.ast.BODY ) */
    public static final Class<BodyDeclaration> BODY_DECLARATION = BodyDeclaration.class;
    public static final Class<AnnotationDeclaration> ANNOTATION = AnnotationDeclaration.class;
    public static final Class<AnnotationMemberDeclaration> ANNOTATION_MEMBER = AnnotationMemberDeclaration.class;
    public static final Class<CallableDeclaration> CALLABLE_DECLARATION = CallableDeclaration.class;
    public static final Class<ClassOrInterfaceDeclaration> CLASS_OR_INTERFACE_DECLARATION = ClassOrInterfaceDeclaration.class;
    public static final Class<ConstructorDeclaration> CONSTRUCTOR_DECLARATION = ConstructorDeclaration.class;
    public static final Class<EnumConstantDeclaration> ENUM_CONSTANT_DECLARATION = EnumConstantDeclaration.class;
    public static final Class<EnumDeclaration> ENUM_DECLARATION = EnumDeclaration.class;
    public static final Class<FieldDeclaration> FIELD_DECLARATION = FieldDeclaration.class;
    public static final Class<InitializerDeclaration> INITIALIZER_DECLARATION = InitializerDeclaration.class;
    public static final Class<MethodDeclaration> METHOD_DECLARATION = MethodDeclaration.class;
    public static final Class<Parameter> PARAMETER = Parameter.class;
    public static final Class<ReceiverParameter> RECEIVER_PARAMETER = ReceiverParameter.class;
    public static final Class<TypeDeclaration> TYPE_DECLARATION = TypeDeclaration.class;
    public static final Class<VariableDeclarator> VARIABLE_DECLARATOR = VariableDeclarator.class;

    /*-----------------------COMMENTS-----------------------------------------*/
    /* ( in com.github.javaparser.ast.comments ) */
    public static final Class<Comment> COMMENT = Comment.class;
    public static final Class<BlockComment> BLOCK_COMMENT = BlockComment.class;
    public static final Class<JavadocComment> JAVADOC_COMMENT = JavadocComment.class;
    public static final Class<LineComment> LINE_COMMENT = LineComment.class;

    /* -------------------------- EXPRESSIONS -----------------------------------*/
    /**
     * See also {@link Ex}
     */
    /* ( in com.github.javaparser.ast.expr ) */
    public static final Class<Expression> EXPR = Expression.class;

    /**
     * i.e. "@Deprecated" "@Target(RUNTIME)"
     */
    public static final Class<AnnotationExpr> ANNOTATION_EXPR = AnnotationExpr.class;

    /**
     * i.e. "arr[0]"
     */
    public static final Class<ArrayAccessExpr> ARRAY_ACCESS_EXPR = ArrayAccessExpr.class; //of("$var$[$index$]");

    /**
     * "new int[20]"
     */
    public static final Class<ArrayCreationExpr> ARRAY_CREATION_EXPR = ArrayCreationExpr.class;

    /**
     * "{1,2,3,4,5}"
     */
    public static final Class<ArrayInitializerExpr> ARRAY_INITIALIZER_EXPR = ArrayInitializerExpr.class;

    /**
     * "a = b", "a = 100"
     */
    public static final Class<AssignExpr> ASSIGN_EXPR = AssignExpr.class;

    /**
     * "a||b "
     */
    public static final Class<BinaryExpr> BINARY_EXPR = BinaryExpr.class;

    /**
     * "true", "false"
     */
    public static final Class<BooleanLiteralExpr> BOOLEAN_LITERAL_EXPR = BooleanLiteralExpr.class;

    /**
     * "(String)s"
     */
    public static final Class<CastExpr> CAST_EXPR = CastExpr.class;

    /**
     * 'c'
     */
    public static final Class<CharLiteralExpr> CHAR_LITERAL_EXPR = CharLiteralExpr.class;

    /**
     * "Listener.class"
     */
    public static final Class<ClassExpr> CLASS_EXPR = ClassExpr.class;

    /**
     * "a > 100 ? 1 : 2"
     */
    public static final Class<ConditionalExpr> CONDITIONAL_EXPR = ConditionalExpr.class;

    /**
     * "100.0d"
     */
    public static final Class<DoubleLiteralExpr> DOUBLE_LITERAL_EXPR = DoubleLiteralExpr.class;

    /**
     * "(100 + 1)" (anything in Parenthesis)
     */
    public static final Class<EnclosedExpr> ENCLOSED_EXPR = EnclosedExpr.class;

    /**
     * "a.b"
     */
    public static final Class<FieldAccessExpr> FIELD_ACCESS_EXPR = FieldAccessExpr.class;

    /**
     * "e instanceof String"
     */
    public static final Class<InstanceOfExpr> INSTANCE_OF_EXPR = InstanceOfExpr.class;

    /**
     * "3"
     */
    public static final Class<IntegerLiteralExpr> INT_LITERAL_EXPR = IntegerLiteralExpr.class;

    /**
     * "a-> true"
     */
    public static final Class<LambdaExpr> LAMBDA_EXPR = LambdaExpr.class;

    /**
     * "23467L"
     */
    public static final Class<LongLiteralExpr> LONG_LITERAL_EXPR = LongLiteralExpr.class;

    /**
     * "null"
     */
    public static final Class<NullLiteralExpr> NULL_EXPR = NullLiteralExpr.class;

    /**
     * "a()"
     */
    public static final Class<MethodCallExpr> METHOD_CALL_EXPR = MethodCallExpr.class;

    /**
     * "String::getName"
     */
    public static final Class<MethodReferenceExpr> METHOD_REFERENCE_EXPR = MethodReferenceExpr.class;
    //AST.Expr.methodReference("String::getName");

    /**
     * "new String()"
     */
    public static final Class<ObjectCreationExpr> OBJECT_CREATION_EXPR = ObjectCreationExpr.class;

    /**
     * "NAME"
     */
    public static final Class<NameExpr> NAME_EXPR = NameExpr.class;

    /**
     * "\"String\""
     */
    public static final Class<StringLiteralExpr> STRING_LITERAL_EXPR = StringLiteralExpr.class;

    /**
     * "super"
     */
    public static final Class<SuperExpr> SUPER_EXPR = SuperExpr.class;

    /**
     * "this"
     */
    public static final Class<ThisExpr> THIS_EXPR = ThisExpr.class;

    /**
     * "!a"
     */
    public static final Class<UnaryExpr> UNARY_EXPR = UnaryExpr.class;

    /**
     * "int i = 1"
     */
    public static final Class<VariableDeclarationExpr> VAR_DECLARATION_EXPR = VariableDeclarationExpr.class;

    public static final Class<Name> NAME = Name.class;

    public static final Class<SimpleName> SIMPLE_NAME = SimpleName.class;

    /**
     * Any literal Expression
     * <UL>
     * <LI>INT_LITERAL_EXPR
     * <LI>LONG LITERAL_EXPR
     * <LI>FLOAT_LITERAL_EXPR
     * <LI>STRING_LITERAL
     * <LI>NULL_LITERAL_EXPR
     * <LI>DOUBLE_LITERAL_EXPR
     * <LI>BOOLEAN_LITERAL
     * <LI>CHAR_LITERAL
     * </UL>
     */
    public static final Class<LiteralExpr> LITERAL_EXPR = LiteralExpr.class;

    /* ---------------------------------NODE TYPES ---------------------------------- */
 /* ( in com.github.javaparser.ast.nodetypes ) */

 /*Interfaces which define or categorize AST nodes based on the properties they contain*/
    public static final Class<NodeWithAnnotations> NODE_WITH_ANNOTATIONS = NodeWithAnnotations.class;
    public static final Class<NodeWithArguments> NODE_WITH_ARGUMENTS = NodeWithArguments.class;
    public static final Class<NodeWithBlockStmt> NODE_WITH_BLOCK_STMT = NodeWithBlockStmt.class;
    public static final Class<NodeWithBody> NODE_WITH_BODY = NodeWithBody.class;
    public static final Class<NodeWithCondition> NODE_WITH_CONDITION = NodeWithCondition.class;
    //public static final Class<NodeWithConstructors> NODE_WITH_CONSTRUCTORS = NodeWithConstructors.class;
    public static final Class<NodeWithDeclaration> NODE_WITH_DECLARATION = NodeWithDeclaration.class;
    public static final Class<NodeWithExpression> NODE_WITH_EXPRESSION = NodeWithExpression.class;
    public static final Class<NodeWithExtends> NODE_WITH_EXTENDS = NodeWithExtends.class;
    public static final Class<NodeWithIdentifier> NODE_WITH_IDENTIFIER = NodeWithIdentifier.class;
    public static final Class<NodeWithImplements> NODE_WITH_IMPLEMENTS = NodeWithImplements.class;
    public static final Class<NodeWithJavadoc> NODE_WITH_JAVADOC = NodeWithJavadoc.class;
    public static final Class<NodeWithMembers> NODE_WITH_MEMBERS = NodeWithMembers.class;
    public static final Class<NodeWithModifiers> NODE_WITH_MODIFIERS = NodeWithModifiers.class;
    public static final Class<NodeWithName> NODE_WITH_NAME = NodeWithName.class;
    public static final Class<NodeWithOptionalBlockStmt> NODE_WITH_OPTIONAL_BLOCK_STMT = NodeWithOptionalBlockStmt.class;
    public static final Class<NodeWithOptionalLabel> NODE_WITH_OPTIONAL_LABEL = NodeWithOptionalLabel.class;
    public static final Class<NodeWithOptionalScope> NODE_WITH_OPTIONAL_SCOPE = NodeWithOptionalScope.class;
    public static final Class<NodeWithParameters> NODE_WITH_PARAMETERS = NodeWithParameters.class;
    public static final Class<NodeWithRange> NODE_WITH_RANGE = NodeWithRange.class;
    public static final Class<NodeWithScope> NODE_WITH_SCOPE = NodeWithScope.class;
    public static final Class<NodeWithSimpleName> NODE_WITH_SIMPLE_NAME = NodeWithSimpleName.class;
    public static final Class<NodeWithStatements> NODE_WITH_STATEMENTS = NodeWithStatements.class;
    public static final Class<NodeWithThrownExceptions> NODE_WITH_THROWN_EXCEPTIONS = NodeWithThrownExceptions.class;
    public static final Class<NodeWithTokenRange> NODE_WITH_TOKEN_RANGE = NodeWithTokenRange.class;
    public static final Class<NodeWithTraversableScope> NODE_WITH_TRAVERSABLE_SCOPE = NodeWithTraversableScope.class;
    public static final Class<NodeWithType> NODE_WITH_TYPE = NodeWithType.class;
    public static final Class<NodeWithTypeArguments> NODE_WITH_TYPE_ARGUMENTS = NodeWithTypeArguments.class;
    public static final Class<NodeWithTypeParameters> NODE_WITH_TYPE_PARAMETERS = NodeWithTypeParameters.class;
    public static final Class<NodeWithVariables> NODE_WITH_VARIABLES = NodeWithVariables.class;

    /* ---------------------------------NODE TYPES w/MODIFIERS ---------------------------------- */
 /* ( in com.github.javaparser.ast.nodetypes.MODIFIERS ) */
    public static final Class<NodeWithAbstractModifier> NODE_WITH_ABSTRACT_MOD = NodeWithAbstractModifier.class;
    public static final Class<NodeWithAccessModifiers> NODE_WITH_ACCESS_MODS = NodeWithAccessModifiers.class;
    public static final Class<NodeWithFinalModifier> NODE_WITH_FINAL_MOD = NodeWithFinalModifier.class;
    public static final Class<NodeWithPrivateModifier> NODE_WITH_PRIVATE_MOD = NodeWithPrivateModifier.class;
    public static final Class<NodeWithProtectedModifier> NODE_WITH_PROTECTED_MOD = NodeWithProtectedModifier.class;
    public static final Class<NodeWithPublicModifier> NODE_WITH_PUBLIC_MOD = NodeWithPublicModifier.class;
    public static final Class<NodeWithStaticModifier> NODE_WITH_STATIC_MOD = NodeWithStaticModifier.class;
    public static final Class<NodeWithStrictfpModifier> NODE_WITH_STRICTFP_MOD = NodeWithStrictfpModifier.class;

    /* ------------------STATEMENTS ----------------------------*/
    /**
     * See also {@link Stmt}
     */
    /* ( in com.github.javaparser.ast.stmt ) */
    public static final Class<Statement> STMT = Statement.class;

    /**
     * Assert Stmt i.e. "assert true;" "assert 1==b : "Not Equal";
     */
    public static final Class<AssertStmt> ASSERT_STMT = AssertStmt.class;

    /**
     * Break Stmt i.e. "break;" or "break outer;"
     */
    public static final Class<BreakStmt> BREAK_STMT = BreakStmt.class;

    /**
     * Block Stmt i.e. "{ assert(1); System.out.println(i); }"
     */
    public static final Class<BlockStmt> BLOCK_STMT = BlockStmt.class;

    /**
     * Continue Stmt i.e. "continue;" or "continue outer;"
     */
    public static final Class<ContinueStmt> CONTINUE_STMT = ContinueStmt.class;

    /**
     * Do Stmt "do{...}while(count < size)"
     */
    public static final Class<DoStmt> DO_STMT = DoStmt.class;

    /**
     * Constructor Invocation Stmt i.e. "this(1, 3);"
     */
    public static final Class<ExplicitConstructorInvocationStmt> CTOR_INVOCATION_STMT
            = ExplicitConstructorInvocationStmt.class;

    /**
     * Expression Stmt i.e. "i += 1;"
     */
    public static final Class<ExpressionStmt> EXPRESSION_STMT = ExpressionStmt.class;

    /**
     * For Stmt i.e. "for(int i=0;i<count;i++){ System.out.println(i);}"
     */
    public static final Class<ForStmt> FOR_STMT = ForStmt.class;

    /**
     * For Each Stmt i.e. "for( int i : arr) {...}"
     */
    public static final Class<ForEachStmt> FOR_EACH_STMT = ForEachStmt.class;

    /**
     * If Stmt
     */
    public static final Class<IfStmt> IF_STMT = IfStmt.class;

    /**
     * Labeled Stmt i.e. "outer: return 5;"
     */
    public static final Class<LabeledStmt> LABELED_STMT = LabeledStmt.class;

    /**
     * Local Class Decl Stmt (i.e. "class L{ int x, y; }"
     */
    public static final Class<LocalClassDeclarationStmt> LOCAL_CLASS_DECLARATION_STMT
            = LocalClassDeclarationStmt.class;

    /**
     * Return Stmt (i.e. "return true;")
     */
    public static final Class<ReturnStmt> RETURN_STMT = ReturnStmt.class;

    /**
     * Switch Entry Stmt (i.e. case 'a' : break;)
     *
     * public static final Class<SwitchEntryStmt> SWITCH_ENTRY_STMT =
     * SwitchEntryStmt.class;
     */
    /**
     * Switch Stmt (i.e. "switch( NAME.charAt(i))" )
     */
    public static final Class<SwitchStmt> SWITCH_STMT = SwitchStmt.class;

    /**
     * Synchronized Stmt i.e. "synchronized(key) {...}"
     */
    public static final Class<SynchronizedStmt> SYNCHRONIZED_STMT = SynchronizedStmt.class;

    /**
     * Throw Stmt i.e. "throw new IOException(e);"
     */
    public static final Class<ThrowStmt> THROW_STMT = ThrowStmt.class;

    /**
     * Try Stmt "try{ ... }"
     */
    public static final Class<TryStmt> TRY_STMT = TryStmt.class;

    /**
     * While Stmt i.e. "while(isOpen) {...}"
     */
    public static final Class<WhileStmt> WHILE_STMT = WhileStmt.class;

    /**
     *
     */
    public static final Class<SwitchEntry> SWITCH_CASE = SwitchEntry.class;
    
    /**
     * <h2>Java 12-</h2>
     * Like {@link com.github.javaparser.ast.stmt.SwitchStmt},
     * but can also be used as an expression.
     * <br/>
     * <br/><code>int a = switch(x) { case 5,6 -> 20; case 9 -> 30; default -> 40; };</code>
     * <br/><code>int a = switch(x) { case 5,6: break 20; default: break 5+5; };</code>
     */
    public static final Class<SwitchExpr> SWITCH_EX = SwitchExpr.class;
    
    /**
     *
     * NOTE: this IS NOT an implementer of Statement
     */
    public static final Class<CatchClause> CATCH_CLAUSE = CatchClause.class;

    /* ------------------ TYPE ----------------------------*/
 /* ( in com.github.javaparser.ast.TYPE ) */
    public static final Class<Type> TYPE = Type.class;
    public static final Class<ArrayType> ARRAY_TYPE = ArrayType.class;
    public static final Class<ClassOrInterfaceType> CLASS_OR_INTERFACE_TYPE = ClassOrInterfaceType.class;
    public static final Class<IntersectionType> INTERSECTION_TYPE = IntersectionType.class;
    public static final Class<PrimitiveType> PRIMITIVE_TYPE = PrimitiveType.class;
    public static final Class<ReferenceType> REFERENCE_TYPE = ReferenceType.class;

    public static final Class<TypeParameter> TYPE_PARAMETER = TypeParameter.class;
    public static final Class<UnionType> UNION_TYPE = UnionType.class;
    public static final Class<UnknownType> UNKNOWN_TYPE = UnknownType.class;

    public static final Class<VarType> VAR_TYPE = VarType.class;
    public static final Class<VoidType> VOID_TYPE = VoidType.class;
    public static final Class<WildcardType> WILDCARD_TYPE = WildcardType.class;

    /**
     * cache for ASTs based on a Class (could be a top level class, an Inner
     * class, or a local class)
     * <p>
     * types are cached when they apply the {@link cache} marker annotation
     * <p>
     * useful if there are many embedded classes, _1_build, Lambdas, etc. that
     * will be looked up on the same class (no need to find a .java file for the
     * .class, read it, parse it to AST and return each time) just _1_build the
     * AST once and return mutable clones each time we ask for it
     *
     * @see cache
     */
    public static final Map<Class, TypeDeclaration> AST_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * An annotation to apply to any Type (Class, Interface, Enum, Annotation)
     * that will CACHE the Ast in a static Map {@link #AST_CACHE_MAP} (based on
     * the TYPE) and when asked for the AST will return a clone
     * <p>
     * (Avoids having to lookup the file, and parse it multiple times)
     *
     * @see #AST_CACHE_MAP
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface cache {
    }

    /*--------------------- LOCATION-BASED RESOLVING IN AST ---------------------------*/
    /**
     * Given a class and a line/column find the "most specific" source code node at this
     * (line:column) cursor position
     *
     * @param clazz the runtime class
     * @param line the line of the source code
     * @return an AST node or null if the position is not within the Clazzes source code
     */
    public static <N extends Node> N at(Class clazz, int line) {
        return at( of(clazz), line);
    }

    /**
     * Given a class and a line/column find the "most specific" node at this (line) cursor position
     *
     * @param top the top node to search through
     * @param line the line of the source code
     * @return an AST node or null if the position is not within the Node
     */
    public static <N extends Node> N at(Node top, int line){
        final int l = Math.max( Math.abs(line), 1);
        Range r = new Range(new Position(l, 0), new Position(l, Integer.MAX_VALUE -1000));

        List<Node> ns = top.findAll(Node.class, (Node n)-> n.getRange().isPresent()
                        && (n.getRange().get().overlapsWith( r ) || r.overlapsWith( n.getRange().get() )) );
        if( ns.isEmpty() ){
            Log.error("None found at : %s ", ()-> l);
            return null;
        }
        //the last node is the "most specific node", but lets climb up (parents)
        //to find a parent that is on the start of the line that contains the small node

        Node theLast = ns.get(ns.size() -1);
        Node theFullLineNode  = theLast;
        boolean done = false;

        while( theFullLineNode.getParentNode().isPresent() && ! done ){
            Node par = theFullLineNode.getParentNode().get();
            if( par.getRange().isPresent() && (! (par instanceof CompilationUnit ))){
                if( par.getRange().get().begin.line == line && par.getRange().get().end.line == line){
                    theFullLineNode = par;
                } else{
                    done = true;
                }
            } else{
                done = true;
            }
        }
        return (N)theFullLineNode;
    }

    /**
     * Given a class and a line/column find the "most specific" source code node at this (line:column)
     * cursor position
     *
     * @param clazz the runtime class
     * @param line the line of the source code
     * @param column the column cursor position within the source code
     * @return an AST node or null if the position is not within the Clazzes source code
     */
    public static <N extends Node> N at(Class clazz, int line, int column) {
        line = Math.max( Math.abs(line), 1);
        return at( of(clazz), line, column);
    }

    /**
     * Given top level node to return the most "specific" node that contains the (line:column)
     * cursor position
     * @param top the Top level Ast node to look through
     * @param line the line cursor position
     * @param column the column cursor position
     * @return an AST node or null if the position is not within the top node
     */
    public static <N extends Node> N at(Node top, int line, int column){
        Position p = new Position(Math.max( Math.abs(line), 1), column);
        List<Node> found = new ArrayList<>();
        top.walk( n-> {
            if( n.getRange().isPresent() && n.getRange().get().contains(p)){
                if( found.isEmpty() ){
                    found.add( n );
                } else{
                    if( found.get(0).containsWithinRange(n) ){
                        found.clear(); //remove the old one
                        found.add(n); //add the new one
                    }
                }
            }
        } );
        if( found.isEmpty() ){
            return null;
        }
        return (N)found.get(0);
    }

    /**
     * return a member (method, field, constructor, staticBlock)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param clazz
     * @param line
     * @param column
     * @param <M>
     * @return
     */
    public static <M extends BodyDeclaration> M memberAt(Class clazz, int line, int column) {
        line = Math.max( Math.abs(line), 1); //make sure non negative line number (also non zero)
        return memberAt( Ast.of(clazz), Math.max( line, 1), column);
    }

    /**
     * Finds the most specific {@link BodyDeclaration} member containing this
     * position and returns it (or null if the position is outside for range)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param top
     * @param line
     * @param column
     * @param <M>
     * @return
     */
    public static <M extends BodyDeclaration> M memberAt(Node top, int line, int column) {
        line = Math.max( Math.abs(line), 1);
        Node n = at( top, line, column);
        if( n == null ){
            return null;
        }
        if( n instanceof BodyDeclaration) {
            return (M)n;
        }
        return (M) n.stream(Node.TreeTraversal.PARENTS).filter( p -> p instanceof BodyDeclaration).findFirst().get();
    }

    /**
     * Get the parent "member" (not JUST the parent) for some Node
     * we need this because sometimes nodes are "deeply nested" inside code, i.e.:
     * <PRE>
     *     void m(){
     *         if(true){
     *             here: int i = 0;
     *         }
     *     }
     * </PRE>
     * ...the "parent" of the labeled statement "here: int i=0;" is the ifStatement;
     * what we want to return is rather the method m()
     *
     * @param n the node to look for parent member
     * @param <M> the (expected) member type
     * @return the first Parent member of the node or null
     */
    public static <M extends BodyDeclaration> M parentMemberOf(Node n) {

        Optional<Node> bd =
                n.stream(Node.TreeTraversal.PARENTS).filter(p -> p instanceof BodyDeclaration ).findFirst();
        if( bd.isPresent() ){
            return (M)bd.get();
        }
        return null;
    }

    /**
     * Finds the most specific {@link BodyDeclaration} member containing this
     * position and returns it (or null if the position is outside for range)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param clazz
     * @param line
     * @param <M>
     * @return
     */
    public static <M extends BodyDeclaration> M memberAt(Class clazz, int line ) {
        return memberAt( Ast.of(clazz), line);
    }

    /**
     * Finds the most specific {@link BodyDeclaration} member containing this
     * line position and returns it (or null if the position is outside for range)
     *
     * @see TypeDeclaration
     * @see EnumDeclaration
     * @see ClassOrInterfaceDeclaration
     * @see AnnotationDeclaration
     * @see MethodDeclaration
     * @see FieldDeclaration
     * @see InitializerDeclaration
     * @see EnumConstantDeclaration
     * @see AnnotationMemberDeclaration
     *
     * @param top the top node
     * @param line a 1-based line number (we start with line 1)
     * @param <M> a BodyDeclaration (Member)
     * @return an instance of a BodyDeclaration AST Node (or null if not found)
     */
    public static <M extends BodyDeclaration> M memberAt(Node top, int line ) {
        //we cant have negative or 0 line numbers
        final int l = Math.max( Math.abs(line), 1);//if it's 0, we "really mean" 1
        Node n = at( top, line);
        if( n == null ){
            Log.info("Line number : %s is outside of member %s ", ()->l, ()->top.getClass() );
            return null;
        }
        if( n instanceof BodyDeclaration) {
            Log.info("Found member %s containing node %s at line : %s ", ()->n.getClass(), ()->n.getClass(), ()->l );
            return (M)n;
        }
        Optional<Node>on = n.stream(Node.TreeTraversal.PARENTS).filter( p -> p instanceof BodyDeclaration).findFirst();
        if( on.isPresent() ){
            Log.info("Found member %s containing node %s at line : %s ", ()->on.get().getClass(), ()->n.getClass(), ()->l );
            return (M) on.get();
        }
        Log.info("No Parent Member found containing Node %s at line : %s ", ()->n.getClass(), ()->l );
        return null;
    }

    /**
     * Describe the node and it's contents by walking
     * @param astNode the node to describe
     */
    public static void describe( Node astNode ){
        astNode.walk(n-> System.out.println( n.getClass().getCanonicalName()+" \""+n+"\"") );
    }

    /**
     * parse and return a CompilationUnit based on the .java source contents
     * that were read from the javaSourceReader
     * @param javaSourceReader the reader for the source
     * @return an AST CompilationUnit
     */
    protected static CompilationUnit parse(Reader javaSourceReader){
        ParseResult<CompilationUnit> pr = JAVAPARSER.parse(javaSourceReader);
        if( !pr.isSuccessful() ){
            throw new _jdraftException("Unable to parse reader "+pr.getProblems());
        }
        return pr.getResult().get();
    }
    
    /**
     * 
     * @param javaSourceFile
     * @return 
     */
    protected static CompilationUnit parse( File javaSourceFile){
        try{
            ParseResult<CompilationUnit> pr = JAVAPARSER.parse(javaSourceFile);
            if( !pr.isSuccessful() ){
                throw new _jdraftException("Unable to parse reader "+pr.getProblems());
            }            
            CompilationUnit cu = pr.getResult().get();
            cu.setStorage(Paths.get( javaSourceFile.getAbsolutePath()));
            return cu;
        } catch(FileNotFoundException fnfe){
            throw new _jdraftException("Unable to find file "+ javaSourceFile.toString() );
        }
    }
    
    /**
     * Organize imports alphabetically
     * 
     * **NOTE: during this process, the underlying {@link ImportDeclaration}s
     * will be reorganized internally in the NodeList, 
     * <B>but the positions of the tokens will not be updated!</B>, 
     * the side effect of this is if you ask to get the Position of a node
     * (i.e. check if one node is before another based on Begin or End Position)
     * it will have not been updated after the sorting of the imports)
     * 
     * HOWEVER: when the JavaParser writes out the AST, the positions used
     * in the Tokens (i.e. the Position/Range) are disregarded and the order 
     * (within the NodeList) is used
     * 
     * @see Ast#reparse(com.github.javaparser.ast.CompilationUnit) 
     * @param ast the ast compilationUnit
     */
    public static void organizeImports(CompilationUnit ast){        
         ast.getImports().sort((ImportDeclaration o1, ImportDeclaration o2) 
            -> o1.getNameAsString().compareTo(o2.getNameAsString()));         
    }
    
    /**
     * Reparse the CompilationUnit to ensure the internal positions stored for
     * the AST tokens are valid. (i.e. these positions are set when the original
     * Ast is parsed, but NOT maintained as the Ast is modified... i.e. if I read
     * in 
     * <PRE>
     * CompilationUnit cu = Ast.of("public class C{}");
     * </PRE>
     * the positions of the tokens  are correct, but as soon as I modify the 
     * Ast:
     * <PRE>
     * cu.addImport(Map.class);
     * </PRE>
     * ..the token positions WILL NOT be updated, i.e. even though the Ast 
     * ImportDeclaration for "import java.util.Map" occurs in the AST BEFORE
     * the TypeDeclaration of C, the position of the TypeDeclaration "C" is 
     * not updated
     * 
     * *** 
     * NOTE: this will modify the code formatting in an effort to "fix"
     * the positions of the internal Ast tokens. **
     * 
     * @param astCu the original CompilationUnit
     * @return a new CompilationUnit with correct Positions of AST tokens
     */
    public static CompilationUnit reparse( CompilationUnit astCu){
        return reparse( astCu, new PrettyPrinterConfiguration());
    }
    
    /**
     * Reparse the CompilationUnit to ensure the internal positions stored for
     * the AST tokens are valid. (i.e. these positions are set when the original
     * Ast is parsed, but NOT maintained as the Ast is modified... i.e. if I read
     * in 
     * <PRE>
     * CompilationUnit cu = Ast.of("public class C{}");
     * </PRE>
     * the positions of the tokens  are correct, but as soon as I modify the 
     * Ast:
     * <PRE>
     * cu.addImport(Map.class);
     * </PRE>
     * ..the token positions WILL NOT be updated, i.e. even though the Ast 
     * ImportDeclaration for "import java.util.Map" occurs in the AST BEFORE
     * the TypeDeclaration of C, the position of the TypeDeclaration "C" is 
     * not updated
     * 
     * *** 
     * NOTE: this will modify the code formatting in an effort to "fix"
     * the positions of the internal Ast tokens. **
     * 
     * @param astCu
     * @param ppv the configuration for how the AST will be written
     * @return the NEW CompilationUnit with correct Postions and AST tokens
     */
    public static CompilationUnit reparse( CompilationUnit astCu, PrettyPrinterConfiguration ppv ){
        return of(astCu.toString(ppv));
    }

    /**
     * Determine if the Node os one of the expected types
     * @param astNode
     * @param classTypes
     * @return true if the node is one of the types
     */
    public static boolean isNodeOfType( Node astNode, Class...classTypes ){
        return Arrays.stream(classTypes).anyMatch(
                nt -> nt.isAssignableFrom(astNode.getClass()));
    }

    /**
     * Shortcut for checking if the (direct) parent exists and matches the predicate
     * @param node
     * @param parentMatchFn
     * @return
     */
    public static boolean isParent( Node node, Predicate<Node> parentMatchFn){
        if( node.getParentNode().isPresent()){
            return parentMatchFn.test( node.getParentNode().get() );
        }
        return false;
    }

    /**
     * Shortcut for checking if the parent is one of the potential node types
     * @param node the ast node to check
     * @param parentNodeTypes array of node
     * @return
     */
    public static <N extends Node> boolean isParent( Node node, Class<N>... parentNodeTypes){
        if( node.getParentNode().isPresent()){
            return Ast.isNodeOfType( node.getParentNode().get(), parentNodeTypes);
        }
        return false;
    }

    /**
     * Shortcut for checking if an ast has a parent of a particular class that complies with a particular Predicate
     * @param node the ast node starting point
     * @param parentNodeClass the node class expected of the parent node
     * @param parentMatchFn predicate for matching the parent
     * @param <N> the expected parent node type
     * @return true if the parent node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean isParent( Node node, Class<N> parentNodeClass, Predicate<N> parentMatchFn){
        if( node.getParentNode().isPresent()){
            Node parent = node.getParentNode().get();
            if( parentNodeClass.isAssignableFrom(parent.getClass()) ) {
                return parentMatchFn.test( (N)node.getParentNode().get());
            }
        }
        return false;
    }

    /**
     * Finds the Parent "member" {@link BodyDeclaration} (the member "containing" the $pattern match)
     * and test that it matches the memberMatchFn
     * <PRE>
     * i.e.
     * class FF{
     *     @Deprecated
     *     public int getF(){
     *         int i = 0;
     *         int j = 1;
     *         return 2;
     *     }
     * }
     * //...if we start at the int literal 0:
     * Node oneLiteral = $.of(0).firstIn(FF.class);
     * //we might want to know something about it's containing member, (the method getF())
     *
     * // here we test that the literal is not contained in a parent member/BodyDeclaration
     * // that has the @Deprecated annotation
     * assertTrue( Ast.isParentMember(oneLiteral, bd-> bd.getAnnotation(Deprecated.class) == null) )
     *
     * </PRE>
     *
     * @param node
     * @param parentMemberMatchFn
     * @return
     */
    public static boolean isParentMember( Node node, Predicate<BodyDeclaration> parentMemberMatchFn){
        if( node.getParentNode().isPresent()){
            Node parent = node.getParentNode().get();
            if( parent instanceof BodyDeclaration ){
                return parentMemberMatchFn.test( (BodyDeclaration)parent);
            }
            //recurse to next parent until we find a BodyDeclaration to test of no parent
            return isParentMember(parent, parentMemberMatchFn);
        }
        return false;
    }

    /**
     * List all ancestors of the node
     * @param node
     * @return
     */
    public static List<Node> listAncestors( Node node ){
        List<Node>ancestors = new ArrayList<>();
        node.walk(Node.TreeTraversal.PARENTS, a -> ancestors.add(a));
        return ancestors;
    }

    /**
     * find and return the First Common Ancestor of the left and right nodes or null if there is no common ancestor
     * @param left
     * @param right
     * @return
     */
    public static Node commonAncestor( Node left, Node right ){
        //1)collect all ancestors for right in a set
        Set<Node> rightAncestors = new HashSet<>();
        right.walk(Node.TreeTraversal.PARENTS, p -> rightAncestors.add(p));

        //2) walk ancestors of left until I find an ancestor that exists in the rightAncestorSet
        Node leftAncestor = left;
        while (leftAncestor != null ) {
            if (rightAncestors.contains(leftAncestor)) {
                return leftAncestor;
            }
            if( leftAncestor.getParentNode().isPresent()){
                leftAncestor = leftAncestor.getParentNode().get();
            } else{
                leftAncestor = null;
            }
        }
        return null; //no common ancestor
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param ancestorMatchFn the node class expected of the parent node
     * @return true if the ancestor node exists, is of a particular type and complies with the predicate
     */
    public static boolean hasAncestor( Node node, Predicate<Node> ancestorMatchFn){
        return hasAncestor( node, Node.class, ancestorMatchFn);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param ancestorNodeClass the node class expected of the parent node
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean hasAncestor( Node node, Class<N> ancestorNodeClass){
        return hasAncestor( node, ancestorNodeClass, t->true);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class that complies with a particular Predicate
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param ancestorNodeClass the node class expected of the parent node
     * @param ancestorMatchFn predicate for matching the parent
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean hasAncestor( Node node, Class<N> ancestorNodeClass, Predicate<N> ancestorMatchFn){
        return ancestor( node, ancestorNodeClass, ancestorMatchFn ) != null;
    }

    /**
     * Find and return the first ancestor of the node that matches the class and ancestor matchFn (or return null)
     * @param node the start node
     * @param ancestorNodeClass
     * @param ancestorMatchFn
     * @param <N>
     * @return
     * 
     * @see #hasAncestor(Node, Class, Predicate) 
     */
    public static  <N extends Node> N ancestor( Node node, Class<N> ancestorNodeClass, Predicate<N> ancestorMatchFn){
        if( node.getParentNode().isPresent()){
            Node parent = node.getParentNode().get();
            if( ancestorNodeClass.isAssignableFrom(parent.getClass()) ) {
                N nn = (N)node.getParentNode().get();
                boolean match = ancestorMatchFn.test(nn);
                if( match ){
                    return nn;
                }
                return ancestor(parent, ancestorNodeClass, ancestorMatchFn);
            }
        }
        return null;
    }

    public static boolean hasChild( Node n, Predicate<Node> nodeMatchFn){
         return n.getChildNodes().stream().anyMatch(nodeMatchFn);
    }


    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param DescendantMatchFn the node class expected of the parent node
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static boolean hasDescendant( Node node, Predicate<Node> DescendantMatchFn){
        return hasDescendant( node, Node.class, DescendantMatchFn);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param DescendantNodeClass the node class expected of the parent node
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particualr type and complies with the predicate
     */
    public static <N extends Node> boolean hasDescendant( Node node, Class<N> DescendantNodeClass){
        return hasDescendant( node, DescendantNodeClass, t->true);
    }

    /**
     * Shortcut for checking if an ast has any ancestor of a particular class that complies with a particular Predicate
     * (walks up until the root)
     *
     * @param node the ast node starting point
     * @param descendantNodeClass the node class expected of the parent node
     * @param descendantMatchFn predicate for matching the parent
     * @param <N> the expected ancestor node type
     * @return true if the ancestor node exists, is of a particular type and complies with the predicate
     */
    public static <N extends Node> boolean hasDescendant( Node node, Class<N> descendantNodeClass, Predicate<N> descendantMatchFn){
        //return node.stream(Node.TreeTraversal.PREORDER).anyMatch(a -> descendantNodeClass.isAssignableFrom(a.getClass()) && descendantMatchFn.test( (N)a ));
        //return hasDescendant(node, Class)
        return matchDescendant(node, descendantNodeClass,Integer.MAX_VALUE -100, descendantMatchFn );
    }

    public static <N extends Node> boolean hasDescendant( Node node, int depth, Class<N> descendantNodeClass, Predicate<N> descendantMatchFn){
        return matchDescendant(node, descendantNodeClass,depth, descendantMatchFn );
    }

    /**
     * Recursive descent-level search that returns if the node or any of it's children
     * up to (depth) levels deep
     *
     * @param node
     * @param depth
     * @param nodeMatchFn
     * @return
     */
    public static boolean matchDescendant(Node node, int depth, Predicate<Node> nodeMatchFn ) {
        return matchDescendant(node, Node.class, depth, nodeMatchFn);
    }

    public static <N extends Node> boolean  matchDescendant(Node node, Class<N>nodeClass, int depth, Predicate<N>nodeMatchFn){
        if( depth < 0 ){
            return false;
        }
        if( nodeClass.isAssignableFrom(node.getClass()) && nodeMatchFn.test((N)node) ){
            //System.out.println( "FOUND "+node+" with "+ depth );
            return true;
        }
        List<Node> children = node.getChildNodes();
        //final int lvl = levels-1;
        for(int i=0;i<children.size();i++){
            if(matchDescendant(children.get(i),nodeClass, depth-1, nodeMatchFn)){
                return true;
            }
        }
        return false;
    }

    /**
     * Find and return the ast root node for this node
     * (NOTE: the "root" is NOT always the {@link CompilationUnit} because
     * we may have individually created/parsed disconnected nodes ({@link MethodDeclaration},
     * {@link InitializerDeclaration}, etc. )
     *
     * @param astNode an ast node
     * @return the top level parent/ancestor for this node
     */
    public static Node root(Node astNode){
        Node root = astNode;
        while(root.getParentNode().isPresent()){
            root = root.getParentNode().get();
        }
        return root;
    }

    /**
     * Given an array of lines representing some .java source code, parse it and
     * return the AST CompilationUnit based on the source
     * @param linesOfJavaSourceCode source code lines
     * @return an AST CompilationUnit
     */
    protected static CompilationUnit parse(String... linesOfJavaSourceCode){
        String str = Text.combine(linesOfJavaSourceCode);
        ParseResult<CompilationUnit> pr = JAVAPARSER.parse(str);        
        if( !pr.isSuccessful() ){
            if( pr.getProblem(0).getMessage().contains("'static'") ){
                Problem prb = pr.getProblem(0);
                
                //we have a disagreement over whether we can 
                if( prb.getLocation().isPresent() ){
                    TokenRange tr = prb.getLocation().get();
                    String withStatic = tr.toString();
                    //int stindex = withStatic.indexOf(" static ");
                    String withoutStatic = tr.toString().replaceFirst(" static ", " ");

                    str = str.replace(withStatic, withoutStatic);
                    pr = JAVAPARSER.parse(str);      
                    if( pr.isSuccessful() ){
                        CompilationUnit cu = pr.getResult().get();
                        //now I have to 
                        cu.getType(0).setStatic(true);
                        return cu;
                    }
                }
                if( prb.getCause().isPresent() ){
                    System.err.println("Cause"+ prb.getCause().get());
                }
            }
            throw new _jdraftException("ErrorParsing :"+pr.getProblems());
        }
        return pr.getResult().get();
    }
    
    /**
     * 
     * @param pathToJavaSourceCode
     * @return 
     */
    protected static CompilationUnit parse(Path pathToJavaSourceCode ){
        ParseResult<CompilationUnit> pr;
        try {
            pr = JAVAPARSER.parse(pathToJavaSourceCode);
        } catch (IOException ex) {
            throw new _ioException("Unable to read file at \""+pathToJavaSourceCode+"\"", ex);
        }
        if( !pr.isSuccessful() ){
            throw new _jdraftException("Unable to parse file at \""+pathToJavaSourceCode+"\""+pr.getProblems());
        }
        CompilationUnit cu = pr.getResult().get();
        cu.setStorage(pathToJavaSourceCode);
        return cu;
    }
    
    /**
     * read and parse the inputStream containing .java source code and return 
     * the AST {@link CompilationUnit} representation of the .java source
     * @param javaSourceInputStream input Stream containing .java source
     * @return an AST CompilationUnit representation of the source
     */
    protected static CompilationUnit parse(InputStream javaSourceInputStream){
        ParseResult<CompilationUnit> pr = JAVAPARSER.parse(javaSourceInputStream);
        if( !pr.isSuccessful() ){
            throw new _jdraftException("Unable to parse text in inputStream :"+pr.getProblems());
        }
        return pr.getResult().get();
    }    
    
    /**
     * Parse and return the appropriate {@link Node} implementation based on the 
     * nodeClass and code
     *
     * @param nodeClass the class of the node (implementation class)
     * @param code the code for the AST to parse
     * @return the node implementation of the code
     */
    public static Node nodeOf(Class nodeClass, String... code) {
        if (nodeClass.isAssignableFrom( AnnotationExpr.class)) {
            return anno(code);
        }
        if (ImportDeclaration.class == nodeClass) {
            return importDeclaration(Text.combine(code));
        }
        if (Statement.class.isAssignableFrom(nodeClass)) {
            return Stmt.of(code);
        }
        if (Expression.class.isAssignableFrom(nodeClass)) {
            return Ex.of(code);
        }
        if (CatchClause.class.isAssignableFrom(nodeClass)) {
            return catchClause(code);
        }
        if (MethodDeclaration.class == nodeClass) {
            return method(code);
        }
        if (ConstructorDeclaration.class == nodeClass) {
            return constructor(code);
        }        
        if (InitializerDeclaration.class == nodeClass) {
            return staticBlock(code);
        }
        if (TypeDeclaration.class.isAssignableFrom(nodeClass)) {
            return typeDecl(code);
        }
        if (Parameter.class == nodeClass) {
            return parameter(code);
        }
        if (ReceiverParameter.class == nodeClass) {
            return receiverParameter(Text.combine(code));
        }
        if (PackageDeclaration.class == nodeClass) {
            return packageDeclaration(Text.combine(code));
        }
        if (VariableDeclarator.class == nodeClass) {
            return varDecl(code);
        }
        if (FieldDeclaration.class == nodeClass) {
            return field(code);
        }
        if (EnumConstantDeclaration.class == nodeClass) {
            return constantDecl(code);
        }
        if (CompilationUnit.class == nodeClass) {
            return of(code);
        }
        if (Comment.class.isAssignableFrom(nodeClass)) {
            return comment(code);
        }
        if (AnnotationMemberDeclaration.class == nodeClass) {
            return annotationMemberDecl(code);
        }
        if (MethodDeclaration.class == nodeClass) {
            return method(code);
        }
        if( SimpleName.class == nodeClass){
            return new SimpleName(Text.combine(code) );
        }
        if( Name.class == nodeClass){
            return new Name( Text.combine(code));
        }
        if( Type.class.isAssignableFrom(nodeClass )){
            return typeRef( Text.combine(code));
        }
        if( Modifier.class == nodeClass ){
            return MODS_KEYWORD_TO_ENUM_MAP.get(Text.combine(code) );
        }        
        throw new _jdraftException("Could not parse Node of class " + nodeClass);
    }

    /* FOR MODIFIERS translate between a String, an int (used in the runtime modifiers bitmask) and an Ast Enum */
    public static final Map<String, Integer> MODS_KEYWORD_TO_BIT_MAP = new HashMap<String, Integer>();
    public static final Map<Integer, String> MODS_BIT_TO_KEYWORD_MAP = new HashMap<Integer, String>();
    public static final Map<Integer, com.github.javaparser.ast.Modifier> MODS_BIT_TO_ENUM_MAP = new HashMap<>();
    public static final Map<String, com.github.javaparser.ast.Modifier> MODS_KEYWORD_TO_ENUM_MAP = new HashMap<>();
    public static final Map<com.github.javaparser.ast.Modifier, String> MODS_ENUM_TO_KEYWORD_MAP = new HashMap<>();

    static {
        MODS_KEYWORD_TO_BIT_MAP.put( "public", java.lang.reflect.Modifier.PUBLIC );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PUBLIC, "public" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PUBLIC, com.github.javaparser.ast.Modifier.publicModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.publicModifier(), "public" );
        MODS_KEYWORD_TO_ENUM_MAP.put( "public", com.github.javaparser.ast.Modifier.publicModifier() );

        MODS_KEYWORD_TO_BIT_MAP.put( "protected", java.lang.reflect.Modifier.PROTECTED );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PROTECTED, "protected" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PROTECTED, com.github.javaparser.ast.Modifier.protectedModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.protectedModifier(), "protected" );
        MODS_KEYWORD_TO_ENUM_MAP.put( "protected", com.github.javaparser.ast.Modifier.protectedModifier() );

        MODS_KEYWORD_TO_BIT_MAP.put( "private", java.lang.reflect.Modifier.PRIVATE );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.PRIVATE, "private" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.PRIVATE, com.github.javaparser.ast.Modifier.privateModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "private", com.github.javaparser.ast.Modifier.privateModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.privateModifier(), "private" );

        MODS_KEYWORD_TO_BIT_MAP.put( "static", java.lang.reflect.Modifier.STATIC );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.STATIC, "static" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.STATIC, com.github.javaparser.ast.Modifier.staticModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "static", com.github.javaparser.ast.Modifier.staticModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.staticModifier(), "static" );

        MODS_KEYWORD_TO_BIT_MAP.put( "synchronized", java.lang.reflect.Modifier.SYNCHRONIZED );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.SYNCHRONIZED, "synchronized" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.SYNCHRONIZED, com.github.javaparser.ast.Modifier.synchronizedModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "synchronized", com.github.javaparser.ast.Modifier.synchronizedModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.synchronizedModifier(), "synchronized" );

        MODS_KEYWORD_TO_BIT_MAP.put( "abstract", java.lang.reflect.Modifier.ABSTRACT );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.ABSTRACT, "abstract" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.ABSTRACT, com.github.javaparser.ast.Modifier.abstractModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "abstract", com.github.javaparser.ast.Modifier.abstractModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.abstractModifier(), "abstract" );

        MODS_KEYWORD_TO_BIT_MAP.put( "final", java.lang.reflect.Modifier.FINAL );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.FINAL, "final" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.FINAL, com.github.javaparser.ast.Modifier.finalModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "final", com.github.javaparser.ast.Modifier.finalModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.finalModifier(), "final" );

        MODS_KEYWORD_TO_BIT_MAP.put( "native", java.lang.reflect.Modifier.NATIVE );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.NATIVE, "native" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.NATIVE, com.github.javaparser.ast.Modifier.nativeModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "native", com.github.javaparser.ast.Modifier.nativeModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.nativeModifier(), "native" );

        MODS_KEYWORD_TO_BIT_MAP.put( "transient", java.lang.reflect.Modifier.TRANSIENT );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.TRANSIENT, "transient" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.TRANSIENT, com.github.javaparser.ast.Modifier.transientModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "transient", com.github.javaparser.ast.Modifier.transientModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.transientModifier(), "transient" );

        MODS_KEYWORD_TO_BIT_MAP.put( "volatile", java.lang.reflect.Modifier.VOLATILE );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.VOLATILE, "volatile" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.VOLATILE, com.github.javaparser.ast.Modifier.volatileModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "volatile", com.github.javaparser.ast.Modifier.volatileModifier() );

        MODS_KEYWORD_TO_BIT_MAP.put( "strictfp", java.lang.reflect.Modifier.STRICT );
        MODS_BIT_TO_KEYWORD_MAP.put( java.lang.reflect.Modifier.STRICT, "strictfp" );
        MODS_BIT_TO_ENUM_MAP.put( java.lang.reflect.Modifier.STRICT, com.github.javaparser.ast.Modifier.strictfpModifier() );
        MODS_KEYWORD_TO_ENUM_MAP.put( "strictfp", com.github.javaparser.ast.Modifier.strictfpModifier() );
        MODS_ENUM_TO_KEYWORD_MAP.put( com.github.javaparser.ast.Modifier.strictfpModifier(), "strictfp" );

        //ANY_FOR DEFAULT INTERFACES
        MODS_KEYWORD_TO_BIT_MAP.put( "default", 1 << 12 );
        MODS_BIT_TO_KEYWORD_MAP.put( 1 << 12, "default" );
        //BIT_TO_ENUM_MAP.put( 1 << 12, com.github.javaparser.ast.Modifier );
        //KEYWORD_TO_ENUM_MAP.put( "default", com.github.javaparser.ast.Modifier.DEFAULT );
    }
    
    /**
     * Build and return a JavaParser Ast from the runtime clazz and return the
     * top level typeDeclaration.
     *
     * @see Ast#of(Class) to do the same and return the top
     * {@link CompilationUnit}
     * @param clazz the runtime class to acquire the
     * @return
     */
    public static TypeDeclaration typeDecl(Class clazz) {
        return Ast.typeDecl(clazz, _io.IN_DEFAULT);
    }

    /**
     * Returns a TypeDeclaration, or a LocalClassDeclaration given the class and
     * resolver
     *
     * @param clazz the runtime class to lookup the source code and return AST
     * for
     * @param resolver how to look up the source code
     * @return the typeDeclaration or an exception
     */
    public static TypeDeclaration typeDecl(Class clazz, _in._resolver resolver) {
        TypeDeclaration cached = AST_CACHE_MAP.get(clazz);
        if (cached != null) {
            return cached.clone();
        }
        if (!clazz.isLocalClass() && !clazz.isAnonymousClass() && !clazz.isSynthetic() && !clazz.getName().contains("$")) {
            _in _i = resolver.resolve(clazz);            
            if (_i == null) {
                throw new _ioException("could not find .java source for: " + clazz.getCanonicalName() + System.lineSeparator() + resolver.describe());
            }
            
            /** */
            CompilationUnit cu = parse(_i.getInputStream());
            cu.setStorage(_i.getPath());
            Optional<TypeDeclaration<?>> ot = 
                cu.getTypes().stream().filter(t -> t.getNameAsString().equals(clazz.getSimpleName()) ).findFirst();            
            if( !ot.isPresent() ){
                throw new _jdraftException("Unable to in source of type "+clazz.getSimpleName()+" in inputStream ");
            }
            //manually set the storage path on the cu
            if( _i.getPath() != null ){
                cu.setStorage(_i.getPath());
            }
            //ok, here             
            TypeDeclaration tdd = ot.get();       
            
            /* */
            if (tdd.getAnnotationByClass(cache.class).isPresent()) {
                tdd.getAnnotations().remove(tdd.getAnnotationByClass(cache.class).get());
                AST_CACHE_MAP.put(clazz, tdd);
            }
            return tdd;
        }
        if (clazz.isLocalClass()) {
            Class topClass = getTopClass(clazz);
            _in _i = resolver.resolve(topClass);
            if (_i == null) {
                throw new _ioException("no .java source for: " + topClass + " containing " + clazz.getCanonicalName()
                    + System.lineSeparator() + resolver.describe());
            }

            //get the enclosing class
            List<TypeDeclaration> tds = new ArrayList<>();

            TypeDeclaration td = typeDecl( _i );
            td.walk(TypeDeclaration.class, t -> {
                if (t.getName().asString().equals(clazz.getEnclosingClass().getSimpleName())) {
                    tds.add(t);
                }
            });

            td = tds.get(0);

            List<LocalClassDeclarationStmt> localCand = new ArrayList<>();
            Constructor ct = clazz.getEnclosingConstructor();
            if (ct != null) {
                td.walk(ConstructorDeclaration.class, cd -> 
                    cd.getBody().walk(
                        LocalClassDeclarationStmt.class,
                        (LocalClassDeclarationStmt lc) -> {
                            if (lc.getClassDeclaration().getNameAsString().equals(clazz.getName())) {
                                localCand.add(lc);
                            }
                    })
                );
            }
            Method md = clazz.getEnclosingMethod();

            if (md != null) {
                td.walk(MethodDeclaration.class, m -> {
                    if (m.getBody().isPresent() && m.getNameAsString().equals(md.getName())) {
                        m.walk(LocalClassDeclarationStmt.class, l -> {
                            if (l.getClassDeclaration().getNameAsString().equals(clazz.getSimpleName())) {
                                localCand.add(l);
                            }
                        });
                    }
                });
            }
            if (localCand.size() == 1) {
                ClassOrInterfaceDeclaration coid = localCand.get(0).getClassDeclaration();
                coid.setPublic(true);

                //promote Local classes to a new Compilation Unit before caching
                CompilationUnit cu = new CompilationUnit();
                cu.setStorage(_i.getPath());
                cu.addType(coid);
                if (localCand.get(0).getComment().isPresent()) {
                    coid.setComment(localCand.get(0).getComment().get());
                }
                if (coid.getAnnotationByClass(cache.class).isPresent()) {
                    coid.getAnnotations().remove(coid.getAnnotationByClass(cache.class).get());
                    AST_CACHE_MAP.put(clazz, coid);
                }
                return coid;
            } else {
                ClassOrInterfaceDeclaration coid = localCand.get(0).getClassDeclaration();
                coid.setPublic(true);

                CompilationUnit cu = new CompilationUnit();
                cu.setStorage(_i.getPath());
                cu.addType(coid);
                if (localCand.get(0).getComment().isPresent()) {
                    coid.setComment(localCand.get(0).getComment().get());
                }
                if (coid.getAnnotationByClass(cache.class).isPresent()) {
                    coid.getAnnotations().remove(coid.getAnnotationByClass(cache.class).get());
                    AST_CACHE_MAP.put(clazz, coid);
                }
                return coid;
            }
        }
        Class topClass = getTopClass(clazz);

        _in _i = resolver.resolve(topClass);
        if (_i == null) {
            throw new _ioException("couldn't find .java source for: " + topClass + " containing " + clazz.getCanonicalName()
                + System.lineSeparator() + resolver.describe());
        }

        CompilationUnit cu = parse(_i.getInputStream());
        cu.setStorage(_i.getPath());
        List<TypeDeclaration> tds
                = _walk.list(cu, TypeDeclaration.class, td -> td.getNameAsString().equals(clazz.getSimpleName())
                && td.getParentNode().isPresent()
                && !(td.getParentNode().get() instanceof LocalClassDeclarationStmt));//dont miz inner with Local classes
        if (tds.size() == 1) {
            TypeDeclaration td = tds.get(0);
            if (td.getAnnotationByClass(cache.class).isPresent()) {
                td.getAnnotations().remove(td.getAnnotationByClass(cache.class).get());
                AST_CACHE_MAP.put(clazz, td);
            }

            return td;
        } else if (tds.isEmpty()) {
            throw new _jdraftException("No source for inner class " + clazz
                    + System.lineSeparator() + resolver.describe());
        }
        throw new _jdraftException("Multiple inner classes share the same NAME for " + clazz);
    }

    /**
     * Creates / returns a packageDeclaration AST Node from a String i.e.
     * "package aaaa.bbbb.cccc;" NOTE: the "package " prefix and trailing ";"
     * are optional
     *
     * @param str the string representation
     * @return
     */
    public static PackageDeclaration packageDeclaration(String str) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        if (str.startsWith("package")) {
            str = str.substring("package".length());
        }
        if (str.endsWith(";")) {
            str = str.substring(0, str.length() - 1);
        }
        return new PackageDeclaration().setName(str);
    }

    /**
     * Creates the appropriate ImportDeclaration to statically import the method
     *
     * @param m the reflective method
     * @return the ImportDeclaration
     */
    public static ImportDeclaration importDeclaration(Method m) {
        if (!java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
            throw new _jdraftException("Cannot statically import a non-static method");
        }
        ImportDeclaration id = new ImportDeclaration(
            m.getDeclaringClass().getCanonicalName() + "." + m.getName(), true, false);
        return id;
    }

    /**
     * build a single import declaration for this runtime Class
     *
     * @param clazz the class to import
     * @return ImportDeclaration
     */
    public static ImportDeclaration importDeclaration(Class clazz) {
        if (clazz == null
            || clazz.isPrimitive()
            || clazz.isArray() && clazz.getComponentType().isPrimitive()) {
            return null;
        }
        if (clazz.isArray()) {
            String s = clazz.getCanonicalName();
            return new ImportDeclaration(s.substring(0, s.indexOf('[')), false, false);
        }
        return new ImportDeclaration(clazz.getCanonicalName(), false, false);
    }

    /**
     * Parse the import declaration from the string code
     *
     * @param code text representing an import statement
     * @return the ImportDeclaration
     */
    public static ImportDeclaration importDeclaration(String code) {
        code = code.trim();
        if (code.length() == 0) {
            return null;
        }
        int openParenIndex = code.indexOf("(");
        if( openParenIndex > 0 ){
            code = code.substring(0, openParenIndex).trim();
        }
        int idx = code.indexOf('['); //if it's an array remove the things
        if (idx > 0) {
            code = code.substring(0, idx);
        }
        if (!code.startsWith("import ")) {
            code = "import " + code;
        }
        if (!code.endsWith(";")) {
            code = code + ";";
        }
        return StaticJavaParser.parseImport(code);
    }

    /**
     * Builda a block comment based on the contents of the string / * * /
     * <p>
     * / *
     * *
     * * /
     *
     * @param str
     * @return
     */
    public static BlockComment blockComment(String... str) {

        if (str[0].trim().startsWith("/*")) {
            str[0] = str[0].substring(str[0].indexOf("/*") + 2);
        }
        if (str[0].trim().endsWith("*/")) {
            str[0] = str[0].substring(0, str[0].lastIndexOf("*/"));
            return new BlockComment(str[0]);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str[0]);
        if (str.length > 1) {
            for (int i = 1; i < str.length; i++) {
                sb.append(System.lineSeparator());
                if (str[i].trim().endsWith("*/")) {
                    str[i] = str[i].substring(0, str[i].lastIndexOf("*/"));
                    sb.append(str[i]);
                    return new BlockComment(sb.toString());
                }
                sb.append(str[i]);
            }
        }
        return new BlockComment(sb.toString());
    }

    /**
     * Parses a JavaDoc comment from the str and returns it
     *
     * @param str
     * @return
     */
    public static JavadocComment javadocComment(String... str) {

        if (str.length == 1) {
            String st = str[0].trim();
            if (st.startsWith("/**")) {
                st = st.substring("/**".length());
            }
            if (st.endsWith("*/")) {
                st = st.substring(0, st.length() - "*/".length());
            }
            return new JavadocComment(st);
        }
        StringBuilder sb = new StringBuilder();
        if (str[0].trim().startsWith("/**")) {
            str[0] = str[0].substring(str[0].indexOf("/**") + "/**".length());
        }
        for (int i = 0; i < str.length; i++) {
            if (i > 0) {
                sb.append(System.lineSeparator());
            }
            if (str[i].trim().endsWith("*/")) {
                str[i] = str[i].substring(0, str[i].lastIndexOf("*/"));

                sb.append(str[i]);
            }
            sb.append(str[i]);
        }
        return new JavadocComment(sb.toString());
    }

    /**
     * parses and returns a line comment, i.e."//comment" and returns it
     *
     * @param str
     * @return
     */
    public static LineComment lineComment(String... str) {
        StringBuilder sb = new StringBuilder();
        //System.out.println( "In line comment ");
        for(int i=0;i<str.length;i++){
            String st = str[i];
            if( i > 0 ){
                sb.append(" ");
            }
            if (st.trim().startsWith("//")) {
                sb.append(st.substring(st.indexOf("//") + 2));
            } else {
                sb.append(st);
            }            
        }
        LineComment lc = new LineComment();
        lc.setContent(sb.toString());
        return lc;
    }

    /**
     * Parses the comment and returns the appropriate variant
     *
     * @param str
     * @return
     */
    public static Comment comment(String... str) {
        if (str[0].trim().startsWith("//")) {
            return lineComment(str);
        } else if (str[0].trim().startsWith("/**")) {
            return javadocComment(str);
        } else {
            return blockComment(str);
        }
    }

    /**
     * Creates an AST {@link CompilationUnit} from the code (CompilationUnit is
     * the top level entity for AST (representing a Java file), or throws a 
     * _jDraftException if there is a problem readingor parsing the file
     *
     * @param javaSourceCode
     * @return an AST CompilationUnit from
     */
    public static CompilationUnit of(String... javaSourceCode) throws _jdraftException {
        String str = Text.combine(javaSourceCode);
        return parse(str);
    }
    
    /**
     * Creates an AST {@link CompilationUnit} from the data in the javaSourceFile
     * and returns it, or throws a _jDraftException if there is a problem reading
     * or parsing the file
     * @param javaSourceFile a File containing the .java source contents to 
     * build a _code model for (including Classes, and package-info.java & module-info.java)
     * @return the Ast CompilationUnit
     */
    public static CompilationUnit of(File javaSourceFile) throws _jdraftException {
        return parse(javaSourceFile);
    }
    
    /**
     * Creates an AST {@link CompilationUnit} from the data in the inputStream
     * the top level
     * @param javaSourceInputStream the input stream containing .java source code
     * @return the CompilationUnit
     */
    public static CompilationUnit of(InputStream javaSourceInputStream) throws _jdraftException {
        return parse(javaSourceInputStream);
    }

    /**
     * Creates an AST {@link CompilationUnit} from the source code of the
     * runtime clazz (CompilationUnit is the top level entity for AST
     * (representing a Java file)
     *
     * @param clazz the runtime class to find .java source and create the
     * CompilationUnit
     * @return the compilationUnit
     */
    public static CompilationUnit of(Class clazz) throws _jdraftException {
        Node n = typeDecl(clazz);
        if (n instanceof CompilationUnit) {
            return (CompilationUnit) n;
        }
        if( clazz.isMemberClass() || clazz.isLocalClass() || clazz.isAnonymousClass() ){
            CompilationUnit cu = new CompilationUnit();
            cu.addType( (TypeDeclaration)n);
            return cu;
        }
        if (n.findCompilationUnit().isPresent()) {

            return n.findCompilationUnit().get();
        }
        throw new _jdraftException("No .java source for " + clazz + System.lineSeparator() + _io.describe());
    }

    /**
     * parses the contents contained in the javaSourceCodeReader and returns an
     * AST CompilationUnit (or throws a _jDraftException if the .java source code
     * is invalid)
     * 
     * @param javaSourceCodeReader a reader containing .java source code
     * @return an Ast CompilationUnit
     * @throws _jdraftException if there is an error reading or parsing the input
     */
    public static CompilationUnit of(Reader javaSourceCodeReader) throws _jdraftException {
        CompilationUnit cu = parse(javaSourceCodeReader);        
        return cu;        
    }
    
    /**
     * parses the contents contained in the file at the path and returns an
     * AST CompilationUnit (or throws a _jDraftException if the .java source code
     * is invalid)
     * @param pathToJavaSourceCode the local path to a .java source file
     * @return an AST compilationUnit representing the source code
     * @throws _jdraftException if there is an issue reading or parsing the source code
     */
    public static CompilationUnit of(Path pathToJavaSourceCode) throws _jdraftException {
        CompilationUnit cu = parse(pathToJavaSourceCode);
        cu.setStorage(pathToJavaSourceCode);
        return cu;        
    }

    public static TypeDeclaration typeDecl(_in in ){
        TypeDeclaration td = typeDecl(in.getInputStream() );
        if( td.findCompilationUnit().isPresent() ){
            td.findCompilationUnit().get().setStorage(in.getPath() );
        }
        return td;
    }
    
    /**
     * @param is the input Stream walk
     * @return the Top Level Ast Node (CompilationUnit, TypeDeclaration)
     */
    public static TypeDeclaration typeDecl(InputStream is) {
        CompilationUnit cu = parse(is);
        if( cu.getPrimaryType().isPresent()){
            return cu.getPrimaryType().get();
        }
        //List<TypeDeclaration> tds = listAll(cu, TypeDeclaration.class);
        List<TypeDeclaration> tds = _walk.list(cu, TypeDeclaration.class);
        if (tds.size() == 1) {
            return tds.get(0);
        } else if (tds.isEmpty()) {
            throw new _jdraftException("Unable to find primary type in "+cu);
        }
        return tds.stream().filter(t -> t.isTopLevelType()).findFirst().get();
    }

    /**
     * Finds the top "compilationUnit" (i.e. .java file) that contains the class
     * regardless if the class is a: Member class i.e. a static inner class or a
     * Local Class (local to a ctor or method)
     * <p>
     * NOTE: NOT TESTED FOR Anonymous classes
     *
     * @param c
     * @return
     */
    public static Class getTopClass(Class c) {
        //System.out.println( "getting top class from "+ c);
        Class ret = c;
        try {
            while (ret.getEnclosingClass() != null) {
                ret = getTopClass(ret.getEnclosingClass());
            }
        } catch (Exception e) {

        }
        return ret;
    }

    /**
     * Create the BodyDeclaration implementation (MethodDeclaration,
     * FieldDeclaration etc)
     *
     * @param code the code of the declaration
     * @return the BodyDeclaration
     */
    public static BodyDeclaration declaration(String... code) {
        try {
            return StaticJavaParser.parseBodyDeclaration(Text.combine(code));
        } catch (Exception e) {
            throw new _jdraftException("Invalid Body Definition : "
                    + System.lineSeparator() + Text.indent(Text.combine(code)));
        }
    }

    /**
     *
     * @param param the string representation of the typeParameter
     * @return
     */
    public static TypeParameter typeParameter(String param) {
        param = param.trim();
        if (param.length() == 0) {
            return null;
        }
        if (!param.startsWith("<")) {
            param = "<" + param;
        }
        if (!param.endsWith(">")) {
            param = param + ">";
        }
        MethodDeclaration md = method(param + " void a(){}");

        TypeParameter tp = md.getTypeParameters().get(0);
        tp.removeForced(); //DISCONNECT
        return tp;
    }

    /**
     *
     * @param code the string representation of the typeParameter
     * @return
     */
    public static NodeList<TypeParameter> typeParameters(String code) {
        code = code.trim();
        if (code.length() == 0) {
            return new NodeList<>();
        }
        if (!code.startsWith("<")) {
            code = "<" + code;
        }
        if (!code.endsWith(">")) {
            code = code + ">";
        }
        MethodDeclaration md = method(code + " void a(){}");

        NodeList<TypeParameter> ntp = md.getTypeParameters();
        NodeList<TypeParameter> cpy = new NodeList<>(); //Disconnected copy
        ntp.forEach(tp -> cpy.add(tp));
        return cpy;
    }

    /**
     * Builds a {@link MethodDeclaration} from the code and returns it
     *
     * @param code the code making up the methods (may have Javadoc comments &
     * annotations)
     * @return the JavaParser {@link MethodDeclaration}
     */
    public static MethodDeclaration method(String... code) {
        MethodDeclaration md = (MethodDeclaration) declaration(code);

        if (md.getType().getBegin().isPresent()) {
            //now I need to reconcile the Javadoc
            Position beforeMethodType = md.getType().getBegin().get();
            //basically I need to manually SET the Javadoc comment
            //by searching through all Comments, finding ones that are Javadoc Comments
            // and BEFORE the position of the TYPE, then manually
            // setting the JavadocComment on the
            List<JavadocComment> jc = new ArrayList<>();
            md.getAllContainedComments().forEach(
                j -> {
                    if (j instanceof JavadocComment) {
                        JavadocComment com = (JavadocComment) j;
                        if (com.getBegin().get().isBefore(beforeMethodType)) {
                            j.remove();
                            jc.add(com);
                        }
                    }
                });
            if (!jc.isEmpty()) {
                md.setJavadocComment(jc.get(0));
            }
        }
        return md;
    }

    /**
     * At the moment only works for NON-ENUM CONSTRUCTORS
     *
     * @param code
     * @return
     */
    public static ConstructorDeclaration constructor(String... code) {

        ConstructorDeclaration cd = (ConstructorDeclaration) declaration(code);

        if (cd.getName().getBegin().isPresent()) {
            //now I need to reconcile the Javadoc
            Position beforeMethodType = cd.getName().getBegin().get();
            //basically I need to manually SET the Javadoc comment
            //by searching through all Comments, finding ones that are Javadoc Comments
            // and BEFORE the position of the TYPE, then manually
            // setting the JavadocComment on the
            List<JavadocComment> jc = new ArrayList<>();
            cd.getAllContainedComments().forEach(
                j -> {
                    if (j instanceof JavadocComment) {
                        JavadocComment com = (JavadocComment) j;
                        if (com.getBegin().get().isBefore(beforeMethodType)) {
                            j.remove();
                            jc.add(com);
                        }
                    }
                });
            if (!jc.isEmpty()) {
                cd.setJavadocComment(jc.get(0));
            }
        }
        return cd;
    }

    /**
     *
     * @param code
     * @return
     */
    public static AssertStmt assertStmt(String... code ){ return Stmt.assertStmt(code); }

    /**
     * convert the String code into a single Stmt AST BlockStmt
     *
     * @param code
     * @return the BlockStmt
     */
    public static BlockStmt blockStmt(String... code) {
        return Stmt.blockStmt(code);
    }

    /**
     * Build and return a BlockStmt given a sequence of Statements
     *
     * @param stmts
     * @return
     */
    public static BlockStmt blockStmt(Statement... stmts) {
        BlockStmt bs = new BlockStmt();
        Arrays.stream(stmts).forEach(s -> bs.addStatement(s));
        return bs;
    }

    /**
     * i.e."break;" or "break outer;"
     * @param code String representing the break of
     * @return the breakStmt
     */
    public static BreakStmt breakStmt(String... code ) {
        return Stmt.breakStmt( code );
    }

    /**
     * i.e."continue outer;"
     *
     * @param code
     * @return
     */
    public static ContinueStmt continueStmt(String... code ) {
        return Stmt.continueStmt( code );
    }

    /**
     *  i.e."do{ System.out.println(1); }while( a < 100 );"
     * @param code
     * @return
     */
    public static DoStmt doStmt(String... code ) {
        return Stmt.doStmt(  code );
    }

    /**
     * i.e."this(100,2900);" "super('c', 123);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt thisOrSuperCallStmt(String... code ) {
        return Stmt.thisOrSuperCallStmt(  code );
    }

    /**
     * i.e."this(100,2900);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt thisCallStmt(String... code ) {
        return Stmt.thisOrSuperCallStmt(  code );
    }

    /**
     * i.e. "super('c', 123);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt superCallStmt(String... code ) {
        return Stmt.thisOrSuperCallStmt(  code );
    }

    /**
     * i.e."s += t;"
     *
     * @param code
     * @return
     */
    public static ExpressionStmt expressionStmt( String... code ) {
        return Stmt.expressionStmt(code);
    }

    /**
     * i.e."for(int i=0; i<100;i++) {...}"
     * @param code
     * @return
     */
    public static ForStmt forStmt( String... code ) {
        return Stmt.forStmt( code );
    }

    /**
     * i.e."for(String element:arr){...}"
     *
     * @param code
     * @return
     */
    public static ForEachStmt forEachStmt( String... code ) {
        return Stmt.forEachStmt( code );
    }

    /**
     * i.e."if(a==1){...}"
     * @param code
     * @return
     */
    public static IfStmt ifStmt( String... code ) {
        return Stmt.ifStmt(  code );
    }

    /**
     * i.e."outer:   start = getValue();"
     * @param code
     * @return
     */
    public static LabeledStmt labeledStmt( String... code ) {
        return Stmt.labeledStmt( code );
    }

    /**
     * i.e."return VALUE;"
     *
     * @param code
     * @return
     */
    public static ReturnStmt returnStmt( String... code ) {
        return Stmt.returnStmt(  code );
    }

    /**
     * i.e."switch(a) { case 1: break; default : doMethod(a); }"
     *
     * @param code
     * @return
     */
    public static SwitchStmt switchStmt( String... code ) {
        return Stmt.switchStmt(code);
    }

    /**
     * i.e. "synchronized(e) { ...}"
     * @param code
     * @return
     */
    public static SynchronizedStmt synchronizedStmt( String... code ) {
        return Stmt.synchronizedStmt( code );
    }

    /**
     * i.e."throw new RuntimeException("SHOOT");"
     *
     * @param code
     * @return
     */
    public static ThrowStmt throwStmt( String... code ) {
        return Stmt.throwStmt(  code );
    }

    /**
     * i.e. "try{ clazz.getMethod("fieldName"); }"
     * @param code
     * @return
     */
    public static TryStmt tryStmt( String... code ) {
        return Stmt.tryStmt(  code );
    }

    /**
     * i.e."while(i< 1) { ...}"
     *
     * @param code
     * @return
     */
    public static WhileStmt whileStmt( String... code ) {
        return Stmt.whileStmt(  code );
    }

    public static MethodCallExpr methodCallEx(String... code) {
        return Ex.methodCallEx(code);
    }

    public static Name name(String code) {
        return StaticJavaParser.parseName(code);
    }

    public static SimpleName simpleName(String code) {
        return StaticJavaParser.parseSimpleName(code);
    }

    public static Type typeRef(AnnotatedType at) {
        return typeRef(at.getType().toString());
    }

    /**
     * When we create an anonymous Local Class and ask for it's name, it will 
     * have this weird "$#$" qualifier, where # is some number... Here is an 
     * example:
     * <PRE>
     * draft.java._classTest$1$Hoverboard
     * </PRE> ...well we want to identify these patterns and convert them into
     * dots draft.java._classTest.Hoverboard
     */
    public static final String LOCAL_CLASS_NAME_PACKAGE_PATTERN = "\\$?\\d+\\$";

    public static final Pattern PATTERN_LOCAL_CLASS = Pattern.compile(LOCAL_CLASS_NAME_PACKAGE_PATTERN);

    public static Type typeRef(java.lang.reflect.Type t) {
        String str = t.getTypeName();
        if (PATTERN_LOCAL_CLASS.matcher(str).find()) {
            //lets remove all the local stuff... return a type without package
            str = str.replaceAll(LOCAL_CLASS_NAME_PACKAGE_PATTERN, ".");
            return typeRef(str.substring(str.lastIndexOf('.') + 1));
        }
        return typeRef(str);
    }

    public static Type typeRef(Class clazz) {
        if (clazz.isArray()) {
            Class<?> cl = clazz;
            int dimensions = 0;
            StringBuilder sb = new StringBuilder();
            while (cl.isArray()) {
                dimensions++;
                sb.append("[]");
                cl = cl.getComponentType();
            }
            String tr = cl.getCanonicalName() + sb.toString();
            return typeRef(tr);
        }
        return typeRef(clazz.getCanonicalName());
    }

    //public static Class<PrimitiveType> PRIMITIVE_TYPE = PrimitiveType.class;

    public static PrimitiveType BOOLEAN_TYPE = PrimitiveType.booleanType();
    public static PrimitiveType BYTE_TYPE = PrimitiveType.byteType();
    public static PrimitiveType SHORT_TYPE = PrimitiveType.shortType();
    public static PrimitiveType CHAR_TYPE = PrimitiveType.charType();
    public static PrimitiveType INT_TYPE = PrimitiveType.intType();
    public static PrimitiveType FLOAT_TYPE = PrimitiveType.floatType();
    public static PrimitiveType DOUBLE_TYPE = PrimitiveType.doubleType();
    public static PrimitiveType LONG_TYPE = PrimitiveType.longType();

    public static Type typeRef(String code) {

        if (code.contains("|")) { //Could only be a Union Type i.e. from a catch clause
            code = "catch(" + code + " e ) {}";
            CatchClause cc = catchClause(code);
            List<UnionType> ut = new ArrayList<>();
            cc.getParameter().walk(UnionType.class, u -> ut.add(u));
            return ut.get(0);
        }
        if (PATTERN_LOCAL_CLASS.matcher(code).find()) {
            //lets remove all the local stuff... return a type without package
            code = code.replaceAll(LOCAL_CLASS_NAME_PACKAGE_PATTERN, ".");
            return typeRef(code.substring(code.lastIndexOf('.') + 1));
        }
        try {
            return StaticJavaParser.parseType(code);
        }catch(Exception e){
            throw new _jdraftException("Unable to parse type :\""+code+"\"", e);
        }
    }

    public static NodeList<Parameter> parameters(String... code) {
        //we need a holder for the Nodes
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < code.length; i++) {
            if (i > 0 && !(params.charAt(params.length() - 1) == ',')) {
                params.append(',');
            }
            params.append(code[i]);
        }
        String pa = params.toString().trim();
        if (!pa.startsWith("(")) {
            pa = "(" + pa;
        }
        if (!pa.endsWith(")")) {
            pa = pa + ")";
        }
        MethodDeclaration md = Ast.method("void $$" + pa + ";");

        NodeList<Parameter> nps = md.getParameters();
        if (nps.getParentNode().isPresent()) {
            nps.getParentNode().get().removeForced();
        }
        return nps;
    }

    public static Parameter parameter(String... code) {
        return StaticJavaParser.parseParameter(Text.combine(code));
    }

    /**
     *
     * @param code the lines of code
     * @return a List of FieldDeclarations
     */
    public static List<FieldDeclaration> fields(String... code) {

        String str = Text.combine(code);
        if (str.length() == 0) {
            return Collections.EMPTY_LIST;
        }
        if (!str.endsWith(";")) {
            str = str + ";";
        }
        ClassOrInterfaceDeclaration cd = Ast.of("public class $$$$Y{" + str + "}")
            .getClassByName("$$$$Y").get();
        List<FieldDeclaration> fds = cd.getFields();
        fds.forEach(f -> cd.remove(f)); //disconnect from
        return fds;
    }

    /**
     * This is a hack to retain the JAVADOC and ANNOTATIONS when we define a
     * field (basically create a dummy wrapper class, and return it)
     *
     * @param code
     * @return
     */
    public static FieldDeclaration field(String... code) {
        String str = Text.combine(code);
        if (!str.endsWith(";")) {
            str = str + ";";
        }
        String cl = "public class $$$$Y{"
                + System.lineSeparator() + str + System.lineSeparator() + "}";

        //System.out.println( cl );


        ClassOrInterfaceDeclaration cd = Ast.of(cl).getClassByName("$$$$Y").get();

        //ClassOrInterfaceDeclaration cd = Ast.of("public class $$$$Y{"
        //    + System.lineSeparator() + str + System.lineSeparator() + "}")
        //    .getClassByName("$$$$Y").get();
        FieldDeclaration fd = cd.getFields().get(0);
        if (cd.getAllContainedComments().size() > 0) {
            fd.setJavadocComment((JavadocComment) cd.getAllContainedComments().get(0));
        }
        cd.remove(fd); //HERE... WE MAKE SURE WE DISCONNECT THE FIELD FROM THE AST( its "disconnected")
        return fd;
    }

    /**
     * A variable that occurs within the body of a method, constructor, staticBlock
     * @param code the code comprising the variable
     * @return
     */
    public static VariableDeclarationExpr varLocalEx(String...code ){
        return Ex.varLocalEx(code);
    }

    /**
     * Parses and returns a {@link VariableDeclarator} based on the code provided
     * (the underlying class for Both member fields {@link FieldDeclaration} & local
     * {@link VariableDeclarationExpr} variables defined with code bodies
     *
     *
     * @see #varLocalEx(String...)
     * @see #field(String...)
     *
     * @param code
     * @return
     */
    public static VariableDeclarator varDecl(String... code) {
        VariableDeclarator vd = field(code).getVariable(0);
        vd.removeForced();
        return vd;
    }

    /**
     * Returns the annotation Member (with ANNOTATIONS and Javadocs)
     *
     * @param code
     * @return
     */
    public static AnnotationMemberDeclaration annotationMemberDecl(String... code) {
        String str = Text.combine(code);
        str = "@interface $$$$$${" + System.lineSeparator() + str + System.lineSeparator() + "}";
        AnnotationDeclaration ad = annotationDecl(str);
        AnnotationMemberDeclaration amd = (AnnotationMemberDeclaration) ad.getMember(0);
        ad.remove(amd); //disconnect
        return amd;
    }

    /**
     * returns a TypeDeclaration given the code (including the optional
     * Annotations and Javadoc Comment associated with it)
     *
     * @param code
     * @return
     */
    public static TypeDeclaration typeDecl(String... code) {
        CompilationUnit cu = Ast.of(code);
        
        List<Comment> jdc = new ArrayList<>();

        if (cu.getTypes().size() == 1) {
            TypeDeclaration td = cu.getType(0);

            List<Comment> cs = cu.getAllContainedComments();
            cs.addAll(cu.getComments());
            cs.addAll(cu.getOrphanComments());

            cs.forEach(c -> {
                if (c instanceof Comment) {
                    if (c.getBegin().get().isBefore((Position) td.getNameAsExpression().getBegin().get())) {
                        jdc.add((Comment) c);
                    }
                }
            });
            if (jdc.size() > 0) {
                td.setComment(jdc.get(0));
            }
            return td;
        }

        Optional<TypeDeclaration<?>> td = cu.getPrimaryType();
        if (td.isPresent()) {
            TypeDeclaration atd = td.get();
            if (jdc.size() > 0) {
                atd.setComment(jdc.get(0));
            }
            atd.removeForced(); //disconnect
            return atd;
        }
        return td.orElseGet(() -> {
            TypeDeclaration tdd = cu.getType(0);
            return tdd;
        });
    }

    public static EnumDeclaration enumDecl(Class clazz) {
        return (EnumDeclaration) Ast.typeDecl(clazz);
    }

    public static EnumDeclaration enumDecl(String... code) {
        return (EnumDeclaration) typeDecl(code);
    }

    public static ClassOrInterfaceDeclaration classDecl(Class clazz) {
        return (ClassOrInterfaceDeclaration) Ast.typeDecl(clazz);
    }

    public static ClassOrInterfaceDeclaration classDecl(String... code) {
        return (ClassOrInterfaceDeclaration) typeDecl(code);
    }

    public static ClassOrInterfaceDeclaration interfaceDecl(Class clazz) {
        return (ClassOrInterfaceDeclaration) Ast.typeDecl(clazz);
    }

    public static ClassOrInterfaceDeclaration interfaceDecl(String... code) {
        return (ClassOrInterfaceDeclaration) typeDecl(code);
    }

    public static AnnotationDeclaration annotationDecl(Class clazz) {
        return (AnnotationDeclaration) Ast.typeDecl(clazz);
    }

    public static AnnotationDeclaration annotationDecl(String... code) {
        return (AnnotationDeclaration) typeDecl(code);
    }

    /**
     * convert the String code into a single AST Expression nod
     *
     * @param code
     * @return
     */
    public static Expression ex(String... code) {
        return Ex.of(code);
    }

    public static NullLiteralExpr nullEx() {
        return Ex.nullEx();
    }

    public static IntegerLiteralExpr ex(int intValue) {
        return Ex.of(intValue);
    }

    public static BooleanLiteralExpr ex(boolean booleanValue) {
        return Ex.of(booleanValue);
    }

    public static CharLiteralExpr ex(char charValue) {
        return Ex.of(charValue);
    }

    public static LongLiteralExpr ex(long longValue) {
        return Ex.of(longValue);
    }

    public static DoubleLiteralExpr ex(float floatValue) {
        return Ex.of(floatValue);
    }

    public static DoubleLiteralExpr ex(double doubleValue) {
        return Ex.of(doubleValue);
    }

    /**
     * Java 12+ switch Expression
     * 
     * @param switchExpr
     * @return 
     */
    public static SwitchExpr switchEx(String... switchExpr ){
        
        return StaticJavaParser.parseExpression( Text.combine(switchExpr) );
    }
    
    /**
     * Found in Java 12+ switch expressions
     * 
     * @param caseExpr
     * @return 
     */
    public static SwitchEntry caseEntry(String...caseExpr ){
        SwitchExpr se = (SwitchExpr)StaticJavaParser.parseExpression("switch(x) { " +  Text.combine(caseExpr) + "};");
        return se.getEntry(0);
    }
    
    
    /**
     * 
     * @param switchCase
     * @return 
     */
    public static SwitchEntry caseStmt( String...switchCase ){
        return switchEntry(switchCase);
    }
    
    /**
     * A "case" (or default) within a Switch statement
     * i.e.
     * switch(a){
     *     case 1: break;
     *     case 2: System.out.println("Even"); break;
     *     case 3: throw RuntimeException();
     *     default: System.out.println("default");
     * }
     * @param switchEntry
     * @return 
     */
    public static SwitchEntry switchEntry(String...switchEntry){
        String se = Text.combine(switchEntry);
        if( !se.startsWith("case") && !(se.startsWith("default"))){
            se = "case "+se;
        }
        try {
            SwitchStmt ss = (SwitchStmt) StaticJavaParser.parseStatement(
                    "switch (a){" + se + System.lineSeparator() + "}");
            SwitchEntry swe = ss.getEntry(0);
            ss.remove(swe);
            return swe;
        } catch(Exception e){
            throw new _jdraftException("could not parse \""+se+"\"" );
        }
    }
    
    private static final String TRY_HARDCODED = "try{ assert(true); }";

    /**
     * parses and returns a {@link CatchClause} i.e. CatchClause cc =
     * AST.catchClause("catch(Exception e)", "{}");
     *
     * @param catchClause
     * @return
     */
    public static CatchClause catchClause(String... catchClause) {

        String res = Text.combine(catchClause).trim();
        if (!res.startsWith("catch")) {
            res = "catch " + res;
        }
        if (!res.endsWith("}")) {
            res = res + "}";
        }
        try {
            Statement st = stmt(TRY_HARDCODED + res);
            CatchClause cc = (CatchClause) st.getChildNodes().get(1); //getType ONLY the Catch
            cc.removeForced(); //Disconnect
            return cc;
        }catch(Exception e){
            throw new _jdraftException("String \""+res+"\" does not represent a valid CatchClause",e);
        }
    }

    /**
     * Convert the String code into a single Statement
     *
     * @param code
     * @return a Stmt
     */
    public static Statement stmt(String... code) {
        return Stmt.of(code);
    }

    /**
     * Parse the code as (one or more) AST {@link AnnotationExpr}s
     *
     * @param code the code that is an annotation
     * @return NodeList of {@link AnnotationExpr}s
     */
    public static NodeList<AnnotationExpr> annos(String... code) {
        String f = Text.combine(code) + System.lineSeparator() + "int f;";
        FieldDeclaration fd = field(f);
        NodeList<AnnotationExpr> anns = fd.getAnnotations();
        NodeList<AnnotationExpr> cpy = new NodeList<>();
        anns.forEach(a -> cpy.add(a));
        return cpy;
    }

    /**
     * parse the code as an and return the AnnotationExpr
     *
     * @param code
     * @return
     */
    public static AnnotationExpr anno(String... code) {
        String s = Text.combine(code);
        if (s.startsWith("@")) {
            return StaticJavaParser.parseAnnotation(s);
        }
        return StaticJavaParser.parseAnnotation("@" + s);
    }

    /**
     * Parses and returns a new EnumConstantDeclaration
     *
     * @param code
     * @return
     */
    public static EnumConstantDeclaration constantDecl(String... code) {
        String comb = Text.combine(code).trim();
        if (comb.endsWith(";")) {
            EnumDeclaration ed = enumDecl("enum E{ " + System.lineSeparator() + comb + System.lineSeparator() + " }");
            EnumConstantDeclaration ecd = ed.getEntry(0);
            ecd.removeForced(); //disconnect
            return ecd;
        }
        if (comb.endsWith(",")) {
            EnumDeclaration ed = enumDecl("enum E{ " + System.lineSeparator() + comb.substring(0, comb.length() - 1) + ";" + System.lineSeparator() + " }");
            EnumConstantDeclaration ecd = ed.getEntry(0);
            ecd.removeForced(); //disconnet
            return ecd;
        }
        EnumDeclaration ed = enumDecl("enum E{ " + System.lineSeparator() + comb + ";" + System.lineSeparator() + " }");
        EnumConstantDeclaration ecd = ed.getEntry(0);
        ecd.removeForced(); //disconnet
        return ecd;
    }

    public static ReceiverParameter receiverParameter(String str) {
        String rp = "void blah(" + str + "){}";
        MethodDeclaration md = method(rp);
        ReceiverParameter rep = md.getReceiverParameter().get();
        rep.removeForced(); //disconnect
        return rep;
    }

    public static InitializerDeclaration initBlock(String...code ){
        String st = Text.combine(code);
        String str = "";
        if( st.startsWith("/*") ){ //if they start with a comment... then they MUST provide the static{} or {} braces
            str = "class C{" + System.lineSeparator() + st + System.lineSeparator() + "}";
        }
        else if( st.startsWith("static")  || st.startsWith("{") ) {
            str = "class C{" + System.lineSeparator() + st + System.lineSeparator() + "}";
        } else{
            str = "class C{ " + System.lineSeparator()
                    +"{" +System.lineSeparator()
                    + st
                    + System.lineSeparator() + "} }";
        }
        InitializerDeclaration id = (InitializerDeclaration) classDecl(str).getMembers().get(0);
        id.removeForced(); //disconnect
        return id;
    }

    /**
     * Creates a static block {@link InitializerDeclaration}, tries to account
     * for missing "static" missing "{" missing "}"
     * <p>
     * also tries to maintain the Javadoc associated with the Initializer
     *
     * @param code
     * @return
     */
    public static InitializerDeclaration staticBlock(String... code) {
        InitializerDeclaration id = initBlock(code);
        id.setStatic(true);
        return id;
        /*
        //_1_build me a class to "wrap the static block, then parse it out
        String st = Text.combine(code);

        int openIndex = st.indexOf('{');
        if (openIndex < 0) {
            st = "{" + st;
            openIndex = 0;
        }
        if (!st.endsWith("}")) {
            st = st + "}";
        }
        int stIndex = st.indexOf("static");
        if (stIndex < 0 || stIndex > openIndex) {
            st = "static " + st;
        }
        String str = "class C{" + System.lineSeparator() + st + System.lineSeparator() + "}";
        InitializerDeclaration id = (InitializerDeclaration) classDeclaration(str).getMembers().get(0);
        id.removeForced(); //disconnect
        return id;
         */
    }

    /**
     * Looks for LabeledStmts within code and removes the labels while retaining the code within the labels
     * fore example take the code with (2) labeledStmts with the label "lablel":
     * <PRE>
     *      class C {
     *             public void m() {
     *                 label: System.out.println( 1 );
     *                 if(System.getProperty("A") != null){
     *                     label: {
     *                     System.out.println(2);
     *                     System.out.println(3);
     *                     }
     *                 }
     *             }
     *         }
     * </PRE>
     *     If we use the code above and call flatten the label, "label":
     *     <PRE>
     *      _class _c = _class.of( C.class);
     *      Ast.flattenLabel(_c.astCompilationUnit(), "label");
     *      System.out.println( _c );
     *     </PRE>
     * ...produces:
     * <PRE>
     *     public class C {
     *         public void m() {
     *             System.out.println(1);
     *             if (System.getProperty("A") != null) {
     *                 System.out.println(2);
     *                 System.out.println(3);
     *             }
     *         }
     *     }
     * </PRE>
     * @param node
     * @param labelName
     * @param <N>
     * @return
     */
    public static <N extends Node> N flattenLabel(N node, String labelName) {
        //if( !isImplemented() ){
        //    throw new _jDraftException("No label : "+labelName+" in non-implemented body");
        //}
        Optional<LabeledStmt> ols
                = node.findFirst(LabeledStmt.class, ls -> ls.getLabel().toString().equals(labelName));
        while (ols.isPresent()) {
            LabeledStmt ls = ols.get();
            if (ls.getStatement().isBlockStmt()) {
                BlockStmt bs = ls.getStatement().asBlockStmt();
                NodeList<Statement> stmts = bs.getStatements();
                if (stmts.isEmpty()) {
                    ls.remove(); //we have label:{}... just remove it entirely
                    //(ls.getParentNode().get()).remove(); //replace(ls, new EmptyStmt());
                    //(ls.getParentNode().get()).replace(ls, new EmptyStmt());
                } else if (stmts.size() == 1) {
                    if( stmts.get(0).isEmptyStmt()){ //we have label:{;}, remove it entirely
                        ls.remove();
                    } else {
                        ls.getParentNode().get().replace(ls, stmts.get(0));
                    }
                } else {
                    Node parent = ls.getParentNode().get();
                    NodeWithStatements parentNode = (NodeWithStatements) parent;
                    int stmtIndex = parentNode.getStatements().indexOf(ls);
                    for (int i = 0; i < stmts.size(); i++) {
                        parentNode.addStatement(stmtIndex + i, stmts.get(i));
                    }
                    parent.remove(ls);
                }
            } else {
                ls.getParentNode().get().replace(ls, ls.getStatement().clone());
            }
            //check if there is another to be flattened (NOTE: can't be at the same scope
            //but in a smaller scope or another scope (i.e. in a different constructr/method)
            //if we pass in a TypeDeclaration/CompilationUnit
            ols = node.findFirst(LabeledStmt.class, lbs -> lbs.getLabel().toString().equals(labelName));
        }
        return node;
    }

    public static final PrettyPrinterConfiguration PRINT_NO_TYPE_PARAMETERS = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false).setVisitorFactory(PrintNoTypeParameters::new);

    /**
     * Dont print TypeParameters or Annotations... this is for dealing with {@link ClassOrInterfaceType} types
     * which MAY have "TYPE_UYSE" annotations AND erased generics in source code and we want to look at
     * ONLY the stripped/raw type:<PRE>
     *
     * for example:
     * Type t = Ast.typeRef("@ann aaaa.bbbb.MyType<T extends Blah>
     * assertEquals( "aaaa.bbbb.MyType", t.toString( PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS ) );
     *
     * if we want to compare the type (proper) to another type without annotations and Generics
     * </PRE>
     */
    public static final PrettyPrinterConfiguration PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS
            = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false).setVisitorFactory(PrintNoAnnotationsOrTypeParameters::new);

    /**
     * An instance of a {@link com.github.javaparser.ast.visitor.VoidVisitor}
     * used to print out entities, will specifically Avoid printing ANNOTATIONS
     * and TypeParameters by simply overriding specific annotation METHODS with no-ops:
     *
     * @see #PRINT_NO_ANNOTATIONS_OR_COMMENTS
     *
     * This is also an example of an implementation of a Visitor for generating
     * the formatted .java source code, for the more comprehensive example:
     * @see com.github.javaparser.printer.PrettyPrintVisitor
     */
    public static class PrintNoTypeParameters extends PrettyPrintVisitor {

        public PrintNoTypeParameters(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }


        @Override
        public void visit(final ClassOrInterfaceType n, final Void arg) {
            if (!n.getAnnotations().isEmpty()) {
                NodeList<AnnotationExpr> annotations = n.getAnnotations();
                //System.out.println( "PRINTING ANNOS"+annotations);
                for (AnnotationExpr annotation : annotations) {
                    annotation.accept(this, arg);
                    printer.print(" ");
                }
                printer.print(" ");
            }

            if (n.getScope().isPresent()) {
                n.getScope().get().accept(this, arg);
                printer.print(".");
            }
            n.getName().accept(this, arg);
            /* manual print annotations
            super.printAnnotations(n.getAnnotations(), false, arg);
            */

            //

        }

        @Override
        public void visit(final MarkerAnnotationExpr n, final Void arg) {
            //System.out.println("PRINTING VISITING ANNOTATION"+n);
            super.visit(n, arg);
            /*
            printOrphanCommentsBeforeThisChildNode(n);
            printComment(n.getComment(), arg);
            printer.print("@");
            n.getName().accept(this, arg);

             */
        }

        public void visit(TypeParameter tp, Void arg){ }
    }

    /**
     * An instance of a {@link com.github.javaparser.ast.visitor.VoidVisitor}
     * used to print out entities, will specifically Avoid printing ANNOTATIONS
     * and TypeParameters by simply overriding specific annotation METHODS with no-ops:
     *
     * @see #PRINT_NO_ANNOTATIONS_OR_COMMENTS
     *
     * This is also an example of an implementation of a Visitor for generating
     * the formatted .java source code, for the more comprehensive example:
     * @see com.github.javaparser.printer.PrettyPrintVisitor
     */
    public static class PrintNoAnnotationsOrTypeParameters extends PrettyPrintVisitor {

        public PrintNoAnnotationsOrTypeParameters(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final ClassOrInterfaceType n, final Void arg) {
            if (n.getScope().isPresent()) {
                n.getScope().get().accept(this, arg);
                printer.print(".");
            }
            n.getName().accept(this, arg);
        }

        public void visit(TypeParameter tp, Void arg){ }

        @Override
        public void visit(MarkerAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(SingleMemberAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(NormalAnnotationExpr n, Void arg) {
        }
    }

    /**
     * An instance of a {@link com.github.javaparser.ast.visitor.VoidVisitor}
     * used to print out entities, will specifically Avoid printing ANNOTATIONS
     * by simply overriding specific annotation METHODS with no-ops:
     *
     * @see #PRINT_NO_ANNOTATIONS_OR_COMMENTS
     *
     * This is also an example of an implementation of a Visitor for generating
     * the formatted .java source code, for the more comprehensive example:
     * @see com.github.javaparser.printer.PrettyPrintVisitor
     */
    public static class PrintNoAnnotations extends PrettyPrintVisitor {

        public PrintNoAnnotations(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        private void printMemberAnnotations(NodeList<AnnotationExpr> annotations, Void arg) {
        }

        private void printAnnotations(NodeList<AnnotationExpr> annotations, boolean prefixWithASpace, Void arg) {
        }

        @Override
        public void visit(MarkerAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(SingleMemberAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(NormalAnnotationExpr n, Void arg) {
        }
    }

    public static final PrettyPrinterConfiguration PRINT_NO_COMMENTS = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false);

    public static class PrintRawComments extends PrettyPrintVisitor {

        public PrintRawComments(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final LineComment n, final Void arg) {
            if (configuration.isIgnoreComments()) {
                return;
            }
            printer.println(normalizeEolInTextBlock(n.getContent(), "").trim());
        }

        @Override
        public void visit(final BlockComment n, final Void arg) {
            if (configuration.isIgnoreComments()) {
                return;
            }
            String[] lines = n.getContent().split("\\r?\\n");
            StringBuilder sb = new StringBuilder();
            Arrays.stream(lines).forEach(con -> {
                String tr = con.trim();
                if (tr.startsWith("* ")) {
                    tr = tr.substring(2).trim();
                    sb.append(tr).append(System.lineSeparator());
                } else {
                    sb.append(con.trim()).append(System.lineSeparator());
                }
            });
            String con = sb.toString().trim();
            printer.print(con);
        }

        @Override
        public void visit(final JavadocComment n, final Void arg) {
            if (configuration.isPrintComments() && configuration.isPrintJavadoc()) {
                String[] lines = n.getContent().split("\\r?\\n");

                StringBuilder sb = new StringBuilder();
                Arrays.stream(lines).forEach(con -> {
                    String tr = con.trim();
                    if (tr.startsWith("* ")) {
                        tr = tr.substring(2).trim();
                        sb.append(tr).append(System.lineSeparator());
                    } else {
                        sb.append(tr).append(System.lineSeparator());
                    }
                });
                String con = sb.toString().trim(); //n.getContent();
                printer.print(con);
            }
        }
    }

    public static final PrettyPrinterConfiguration PRINT_RAW_COMMENTS = new PrettyPrinterConfiguration()
            .setVisitorFactory(PrintRawComments::new);
    
    /**
     * a PrettyPrinterConfiguration designed to
     * <PRE>
     *  usage examples:
     *  _class _c = _class.of("C", new Object(){ @Deprecated int f; });
     *  _c.toString( Ast.PRINT_NO_ANNOTATIONS_OR_COMMENTS );
     * </PRE>
     */
    public static final PrettyPrinterConfiguration PRINT_NO_ANNOTATIONS_OR_COMMENTS
            = new PrettyPrinterConfiguration()
                    .setPrintComments(false).setPrintJavadoc(false).setVisitorFactory(PrintNoAnnotations::new);

    /**
     * Get the content inside the comments... meaning no prefixes // line
     * comments are easy, just remove the // prefix /* multi- line block
     * comments and JAVADOC comments * can be difficult becuase they start with
     * * on subsequent lines * /
     *
     * @param astComment
     * @return
     */
    public static String getContent(Comment astComment) {
        return astComment.toString(PRINT_RAW_COMMENTS);
    }

    public static List<String> normalizeTypeParameter(TypeParameter tp) {
        List<String> tw = new ArrayList<>();
        _walk.directChildren(tp, Node.class, t -> true, t -> {
            if (t instanceof Type) {
                List<String> toks = Ast.tokenizeType((Type) t);
                toks.forEach(e -> {
                    if (e.contains(".")) {
                        tw.add(e.substring(e.lastIndexOf(".") + 1));
                    } else {
                        tw.add(e);
                    }
                });
            } else {
                tw.add(t.toString());
            }
        });
        return tw;
    }

    public static List<String> tokenizeType(Type type) {
        return tokenizeType(type.toString());
    }

    public static List<String> tokenizeType(String type) {
        type = type.trim();
        List<String> toks = new ArrayList<>();
        String build = new String();
        for (int i = 0; i < type.length(); i++) {
            switch (type.charAt(i)) {
                case '<':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add("<");
                    break;
                case '>':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add(">");
                    break;
                case '|':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add("|");
                    break;
                case '&':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add("&");
                    break;
                case ',':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    break;
                case ' ':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    break;
                default:
                    build += type.charAt(i);
            }
        }
        if (build.length() > 0) {
            toks.add(build);
        }
        return toks;
    }

    /**
     * Builds a hashcode for a set of types (i.e. no order)
     * @param ts
     * @return
     */
    public static int typesHashCode(List<? extends Type> ts) {
        Set<Integer> thc = new HashSet<>();
        for (int i = 0; i < ts.size(); i++) {
            thc.add(typeHash(ts.get(i)));
        }
        return thc.hashCode();
    }

    /**
     * Check if the contents of the lists (NOT THE ORDER) is equal This is
     * useful for checking a list of types where one list may contain some fully
     * qualified types (java.util.Map) and the other may not(Map) (and for
     * generics, etc.)
     * <PRE>
     * this is good for checking if two classes implement the same interfaces in this scenario:
     * _class _a = _class.of("A").implement("fully.qualified.B", "fully.qualified.C");
     * _class _b = _class.of("B").implement("C", "B");
     * //thiw will work / return true... even though order, and types fully qualified or not
     * assertTrue( Ast.typesEqual ( _a.listImplements(), _b.listImplements() ) );
     * </PRE>
     *
     * @param <T> the Ast Type
     * @param lt1
     * @param lt2
     * @return
     */
    public static <T extends Type> boolean typesEqual(List<T> lt1, List<T> lt2) {
        if (lt1.size() != lt2.size()) {
            return false;
        }
        for (int i = 0; i < lt1.size(); i++) {
            Type cit = lt1.get(i);
            if (!lt2.stream().filter(c -> typesEqual(c, cit)).findFirst().isPresent()) {
                return false;
            }
        }
        return true;
    }

    public static boolean importsEqual(TypeDeclaration left, TypeDeclaration right) {
        if (left.isTopLevelType()) {
            if (right.isTopLevelType()) {
                //both left and right are compilationUnits
                return importsEqual(left.findCompilationUnit().get(), right.findCompilationUnit().get());
            }
            return left.findCompilationUnit().get().getImports().isEmpty();
        }
        if (!right.isTopLevelType()) {
            return true;
        }
        return right.findCompilationUnit().isPresent()
                && right.findCompilationUnit().get().getImports().isEmpty();
    }

    public static boolean importsEqual(CompilationUnit left, CompilationUnit right) {
        if (left == null) {
            return right == null;
        }
        if (right == null) {
            return false;
        }
        if (left == right) {
            return true;
        }
        return importsEqual(left.getImports(), right.getImports());
    }

    public static boolean importsEqual(List<ImportDeclaration> li1, List<ImportDeclaration> li2) {
        Set<ImportDeclaration> ti = new HashSet<>();
        Set<ImportDeclaration> to = new HashSet<>();
        ti.addAll(li1);
        to.addAll(li2);
        if (!Objects.equals(ti, to)) {
            return false;
        }
        return true;
    }

    public static NodeList<Modifier> getImpliedModifiers(NodeWithModifiers nwm ){
        if( nwm instanceof VariableDeclarator){
            NodeList<Modifier> nlm = getImpliedModifiers( (VariableDeclarator)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof FieldDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (FieldDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof AnnotationDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (AnnotationDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof ClassOrInterfaceDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (ClassOrInterfaceDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof ConstructorDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (ConstructorDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof EnumDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (EnumDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof MethodDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (MethodDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof TypeDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (TypeDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        if( nwm instanceof AnnotationMemberDeclaration){
            NodeList<Modifier> nlm = getImpliedModifiers( (AnnotationMemberDeclaration)nwm);
            if( nlm == null ){
                return new NodeList<Modifier>();
            }
            return nlm;
        }
        //Parameter?
        throw new _jdraftException("Unable to get implied modifiers for "+ nwm+ " of "+nwm.getClass());
    }

    public static NodeList<Modifier> getImpliedModifiers(Parameter p){
        return new NodeList<Modifier>();
    }

    public static NodeList<Modifier> getImpliedModifiers(AnnotationMemberDeclaration amd){
        return new NodeList<Modifier>();
    }

    public static NodeList<Modifier> getImpliedModifiers(VariableDeclarator vd) {
        if (!vd.getParentNode().isPresent()) {
            return null;
        }
        return getImpliedModifiers((FieldDeclaration) vd.getParentNode().get());
    }

    public static NodeList<Modifier> getImpliedModifiers(FieldDeclaration fd) {
        if (fd == null) {
            return null;
        }
        if (!fd.getParentNode().isPresent()) {
            return null;
        }
        Node parent = fd.getParentNode().get();
        if (parent instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) parent;
            if (coid.isInterface()) {
                //check if initilized ??
                return NodeList.nodeList(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier());
            }
            return new NodeList<>();
        }
        if (parent instanceof AnnotationDeclaration) {
            AnnotationDeclaration ad = (AnnotationDeclaration) parent;
            return NodeList.nodeList(Modifier.publicModifier(), Modifier.staticModifier(), Modifier.finalModifier());
        }
        return new NodeList<>();
    }

    /**
     * Add modifiers from a to b (avoiding duplicates)
     *
     * @param target the nodelist to be merged to
     * @param source the nodelist to add to the target
     * @return the merged nodelist (target) with all of the added Modifier s
     */
    public static NodeList<Modifier> merge(NodeList<Modifier> target, NodeList<Modifier> source) {
        if( source != null ){
            source.forEach(m -> {
                if (!target.contains(m)) {
                    target.add(m);
                }
            });
        }
        return target;
    }

    /**
     *
     * @param left
     * @param right
     * @return
     */
    public static boolean equals(NodeList<Modifier> left, NodeList<Modifier> right) {
        return left.size() == right.size()
                && right.containsAll(left);
    }

    public static boolean modifiersEqual(NodeWithModifiers left, NodeWithModifiers right) {
        if (left == null) {
            return right == null;
        }
        if (right == null) {
            return false;
        }
        //return modifiersEqual(left.getModifiers(), right.getModifiers());
        //I dont know why I'd be doing this.... but I have to look at IMPLIED modifiers
        // and if they arent the same AST type... then WHY?... but ok lets just check modifiers
        // straight up
        if (left.getClass() != right.getClass()) {
            return Objects.equals(left, right);
        }

        // If they are both the same class, I need to derive the Implied Modifiers
        // based on the "container type" (i.e. an initialized  Field on a Interface like this:
        // interface I { int x=100;}
        // ...is REALLY equal to this:
        // interface I { public static final int x = 100; }
        // so it's IMPLIED Modifiers are "public static final"
        // because (even though they dont exist syntactically, they are implied by the context)
        /// TODO what about access modifiers (i need to make sure I dont do public & private when I merge the EnumSets
        // ...so I derive the implied modifiers
        NodeList<Modifier> leftImplied = null;
        NodeList<Modifier> rightImplied = null;
        //the following types need to take into account Implied Modifiers
        // we need to look them up so we can compare like for like
        if (left instanceof FieldDeclaration) {
            leftImplied = getImpliedModifiers((FieldDeclaration) left);
            rightImplied = getImpliedModifiers((FieldDeclaration) right);
        } else if (left instanceof MethodDeclaration) {
            leftImplied = getImpliedModifiers((MethodDeclaration) left);
            rightImplied = getImpliedModifiers((MethodDeclaration) right);
        } else if (left instanceof TypeDeclaration) {
            leftImplied = getImpliedModifiers((TypeDeclaration) left);
            rightImplied = getImpliedModifiers((TypeDeclaration) right);
        } else if (left instanceof ConstructorDeclaration) {
            leftImplied = getImpliedModifiers((ConstructorDeclaration) left);
            rightImplied = getImpliedModifiers((ConstructorDeclaration) right);
        }
        //
        if (leftImplied != null && rightImplied != null) { //Both are "connected" fields (not dangling without a parent node)
            leftImplied = merge(leftImplied, left.getModifiers());
            rightImplied = merge(rightImplied, right.getModifiers());
            return equals(leftImplied, rightImplied);
        }
        if (leftImplied == null && rightImplied == null) { //BOTH are dangling (without parents)
            return equals(left.getModifiers(), right.getModifiers());
        }
        if (leftImplied == null) { //if one or the other is null, then add the implied to BOTH
            leftImplied = NodeList.nodeList(rightImplied);
            leftImplied = merge(leftImplied, left.getModifiers());
            rightImplied = merge(rightImplied, right.getModifiers());
            return equals(leftImplied, rightImplied);
        }
        rightImplied = NodeList.nodeList(leftImplied);
        rightImplied = merge(rightImplied, right.getModifiers());

        leftImplied = merge(leftImplied, left.getModifiers());        
        return equals(leftImplied, rightImplied);
    }

    public static NodeList<Modifier> getImpliedModifiers(TypeDeclaration td) {
        if (td instanceof ClassOrInterfaceDeclaration) {
            return getImpliedModifiers((ClassOrInterfaceDeclaration) td);
        }
        if (td instanceof EnumDeclaration) {
            return getImpliedModifiers((EnumDeclaration) td);
        }
        return getImpliedModifiers((AnnotationDeclaration) td);
    }

    public static NodeList<Modifier> getImpliedModifiers(ClassOrInterfaceDeclaration coid) {
        if (coid.isInterface()) {
            return NodeList.nodeList(Modifier.abstractModifier());
        }
        return new NodeList<>();
    }

    public static NodeList<Modifier> getImpliedModifiers(EnumDeclaration ed) {
        return NodeList.nodeList(Modifier.staticModifier(), Modifier.finalModifier());
    }

    public static NodeList<Modifier> getImpliedModifiers(AnnotationDeclaration ad) {
        return NodeList.nodeList(Modifier.abstractModifier(), Modifier.staticModifier());
    }

    public static NodeList<Modifier> getImpliedModifiers(MethodDeclaration md) {
        if (md == null) {
            return null;
        }
        if (!md.getParentNode().isPresent()) {
            // we have a single rule that applies for a method with no parent 
            // ...in the absense of a method body i.e. "void m();" (body is NULL)
            // it is implied that the method is public & abstract
            if (!md.getBody().isPresent()) {
                return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
            }
            return new NodeList();
        }
        Node parent = md.getParentNode().get();
        if (parent instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) parent;
            if (coid.isInterface()) {
                if (!md.getBody().isPresent()) { //no body & on an interface
                    return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
                }
                //if it's a static or default method on an interface its public by default
                return NodeList.nodeList(Modifier.publicModifier());
            }

            if (!md.getBody().isPresent()) {
                return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
            }
            return new NodeList<>();
        }
        if (parent instanceof AnnotationDeclaration) { //annotation methods 
            return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
        }
        if (!md.getBody().isPresent()) { //No body, must be public abstract
            return NodeList.nodeList(Modifier.publicModifier(), Modifier.abstractModifier());
        }
        return new NodeList<>();
    }

    public static NodeList<Modifier> getImpliedModifiers(ConstructorDeclaration cd) {
        if (cd == null) {
            return null;
        }
        if (!cd.getParentNode().isPresent()) {
            return null;
        }
        Node parent = cd.getParentNode().get();
        if (parent instanceof EnumDeclaration) {
            return NodeList.nodeList(Modifier.privateModifier());
        }
        return new NodeList<>();
    }

    public static int importsHash(TypeDeclaration td) {
        if (!td.isTopLevelType()) {
            return 0;
        }
        if (!td.findCompilationUnit().isPresent()) {
            return 0;
        }
        return importsHash(td.findCompilationUnit().get());
    }

    public static int importsHash(CompilationUnit cu) {
        Set<Integer> is = new HashSet<>();
        cu.getImports().forEach(i -> is.add(i.hashCode()));
        return is.hashCode();
    }
    
    /**
     *
     * @param t
     * @return
     */
    public static int typeHash(Type t) {
        if (t == null) {
            return 0;
        }
        List<String> toks = tokenizeType(t.asString());
        List<Integer> hashes = new ArrayList<>();
        for (int i = 0; i < toks.size(); i++) {
            int idx = toks.get(i).lastIndexOf(".");
            if (idx > 0) {
                hashes.add(Objects.hashCode(toks.get(i).substring(idx + 1)));
            } else {
                hashes.add(Objects.hashCode(toks.get(i)));
            }
        }
        return hashes.hashCode();
    }

    /**
     * Verify that the referenceTypes are equal irregardless of package
     *
     * in situations where I am testing equality i.e. assertTrue(
     * Ast.typesEqual( Ast.typeRef("java.lang.String"), Ast.typeRef("String")));
     *
     * TODO: this doesnt work for matching crazy fully qualified Annotations and TypeBounds
     * FIXME: I need to probably walk/extract the Annotations separately (because they could be out of order
     * and partially/fully qualified, also I SHOULD match TypeParameter typeBounds with a more
     * robust matching (to ensure fully qualified names "java.util.Map" always match "Map")
     *
     * This (however) should work for the vast majority of cases
     * @param r1 the first reference TYPE
     * @param r2 the second reference TYPE
     * @return io
     */
    public static boolean typesEqual(Type r1, Type r2) {
        if (Objects.equals(r1, r2)) {
            return true; //if they are ALREADY equal, return true
        }
        if (r1 == null || r2 == null) {
            //System.out.println( "ONE NULL" );
            return false;
        }
        if( r1.getClass() != r2.getClass() ){
            return false;
        }
        //if ONE or the OTHER is fully
        boolean r1FullyQualified = r1.asString().contains(".");
        boolean r2FullyQualified = r2.asString().contains(".");

        //OK, what I have to do is tokenize based on < > , space
        //ok, really... what I have to do is "build" tokens
        //XOR, if ONE or the OTHER (NOT BOTH or NEITHER) are fully qualified
        if ((r1FullyQualified || r2FullyQualified) && !(r1FullyQualified && r2FullyQualified)) {
            List<String> r1Toks = tokenizeType(r1.asString());
            List<String> r2Toks = tokenizeType(r2.asString());
            if (r1Toks.size() != r2Toks.size()) {
                return false;
            }
            for (int i = 0; i < r1Toks.size(); i++) {
                if (!r1Toks.get(i).equals(r2Toks.get(i))) {
                    r1FullyQualified = r1Toks.get(i).contains(".");
                    r2FullyQualified = r2Toks.get(i).contains(".");

                    if ((r1FullyQualified || r2FullyQualified) && !(r1FullyQualified && r2FullyQualified)) {
                        String s1 = r1Toks.get(i);
                        if (s1.contains(".")) {
                            s1 = s1.substring(s1.lastIndexOf(".") + 1);
                        }
                        String s2 = r2Toks.get(i);
                        if (s2.contains(".")) {
                            s2 = s2.substring(s2.lastIndexOf(".") + 1);
                        }
                        //
                        if (!s1.equals(s2)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        //here we could have both types that have '.'s but one is from an inner class
        // i.e.
        // _typeRef _t1 = _typeRef.of("_enum._constant");
        // _typeRef _t2 = _typeRef.of("org.jdraft._enum._constant");
        //ok... lets parse out the first

        List<String> t1s = tokenizeType(r1);
        List<String> t2s = tokenizeType(r2);
        if (t1s.size() != t2s.size()) {
            return false;
        }
        String s1n  = t1s.get(0); //get the first (type) token
        String s2n  = t2s.get(0); //get the second (type) token
        int dotIndex1 = s1n.lastIndexOf('.');
        int dotIndex2 = s2n.lastIndexOf('.');
        if( dotIndex1 > 0 ){ //this means BOTH must be qualified (partially or otherwise)
            //Log.info( "BOTH PARTIALLY QUALIFIED");
            if( s1n.contains(s2n) || s2n.contains(s1n) ){ //compare that either one contains the other JUST THE NAME NOT GENERICS
                //String normalizedName = s1n.substring(dotIndex);
                String t1 = r1.asString().substring(dotIndex1+1);
                String t2 = r2.asString().substring(dotIndex2+1);
                //call types equal on the SIMPLE type name and (potentially) any generics after it
                return typesEqual( Ast.typeRef(t1), Ast.typeRef(t2));
            }
        }
        return false;
    }
    
    /**
     * Singleton instance for comparing AST nodes by
     */
    public static final NodeStartPositionComparator COMPARE_NODE_BY_LOCATION = 
        new NodeStartPositionComparator();
    
    /**
     * Sorts a list of nodes by the position:
     * HUGE CAVEAT: Asts that have been modified often will have nodes out of 
     * order, in this case it is best to serialize the AST to a String then read it
     * back in to ensure the node positions are parsed and maintained.
     * 
     * @param <N>
     * @param unsorted
     * @return 
     */
    public static <N extends Node> List<N> sortNodesByPosition( List<N> unsorted){        
        Collections.sort(unsorted, COMPARE_NODE_BY_LOCATION);
        return unsorted;
    }
    
    /**
     * Comparator for Nodes within an AST node that organizes based on the
     * start position.
     */
    public static class NodeStartPositionComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            if (o1.getBegin().isPresent() && o2.getBegin().isPresent()) {
                int comp = o1.getBegin().get().compareTo(o2.getBegin().get());
                if( comp != 0 ){
                    return comp;
                }
                int comp2 = o1.getEnd().get().compareTo(o2.getEnd().get());
                return comp2;                
            }
            //if one or the other doesnt have a begin
            // put the one WITHOUT a being BEFORE the other
            // if neither have a being, return
            if (!o1.getBegin().isPresent() && !o2.getBegin().isPresent()) {
                return 0;
            }
            if (o1.getBegin().isPresent()) {
                return -1;
            }
            return 1;
        }
    }

    /**
     * Given an AST node, walk check its comments (in order)
     *
     * @param astRootNode
     * @param commentActionFn
     */
    public static void forComments(Node astRootNode, Consumer<Comment> commentActionFn) {
        forComments(astRootNode, t -> true, commentActionFn);
    }

    /**
     *
     * @param <C>
     * @param astRootNode
     * @param commentClass
     * @param commentMatchFn
     * @param commentActionFn
     */
    public static <C extends Comment> void forComments(
            Node astRootNode, Class<C> commentClass, Predicate<C> commentMatchFn, Consumer<C> commentActionFn) {
        if (astRootNode instanceof NodeWithJavadoc && commentClass == JavadocComment.class) {
            NodeWithJavadoc nwj = (NodeWithJavadoc) astRootNode;
            if (nwj.getJavadocComment().isPresent()) {
                if (commentMatchFn.test((C) nwj.getJavadocComment().get())) {
                    commentActionFn.accept((C) nwj.getJavadocComment().get());
                }
            }
        }
        List<Comment> acs = astRootNode.getAllContainedComments();

        Collections.sort(acs, new CommentPositionComparator());
        LinkedHashSet<Comment> lhs = new LinkedHashSet<>();
        lhs.addAll(acs);

        lhs.stream().filter(c ->
                commentClass.isAssignableFrom(c.getClass())
                        && commentMatchFn.test((C) c))
                .forEach(c -> commentActionFn.accept((C) c));
    }

    /**
     * @param astRootNode
     * @param commentMatchFn
     * @param commentActionFn
     */
    public static void forComments(
            Node astRootNode, Predicate<Comment> commentMatchFn, Consumer<Comment> commentActionFn) {

        if (astRootNode == null) {
            return;
        }
        if (astRootNode instanceof NodeWithJavadoc) {
            NodeWithJavadoc nwj = (NodeWithJavadoc) astRootNode;
            if (nwj.getJavadocComment().isPresent()) {
                if (commentMatchFn.test((Comment) nwj.getJavadocComment().get())) {
                    commentActionFn.accept((Comment) nwj.getJavadocComment().get());
                }
            }
        }
        List<Comment> acs = astRootNode.getAllContainedComments();
        List<Comment> ocs = astRootNode.getOrphanComments();
        acs.addAll(ocs);

        Collections.sort(acs, new CommentPositionComparator());
        LinkedHashSet<Comment> lhs = new LinkedHashSet<>();
        lhs.addAll(acs);
        lhs.stream()
                .filter(commentMatchFn).forEach(commentActionFn);
    }


    /**
     * list all comments within this astRootNode (including the comment applied
     * to the astRootNode if the AstRootNode is an instance of {@link NodeWithJavadoc}
     *
     * @param astRootNode the root node to look through
     * @return a list of all comments on or underneath the node
     */
    public static List<Comment> listComments(Node astRootNode) {
        List<Comment> found = new ArrayList<>();
        forComments(astRootNode, c -> found.add(c));
        return found;
    }

    /**
     * list all comments within this astRootNode that match the predicate
     * (including the comment applied to the astRootNode if the AstRootNode is
     * an instance of {@link NodeWithJavadoc})
     *
     * @param astRootNode    the root node to look through
     * @param commentMatchFn matching function for comments
     * @return a list of all comments on or underneath the node
     */
    public static List<Comment> listComments(Node astRootNode, Predicate<Comment> commentMatchFn) {
        List<Comment> found = new ArrayList<>();
        forComments(astRootNode, c -> {
            if (commentMatchFn.test(c)) {
                found.add(c);
            }
        });
        return found;
    }

    /**
     * @param <C>                the comment class
     * @param astRootNode        the root node to start the search
     * @param commentTargetClass the TYPE of comment ({@link Comment},
     *                           {@link LineComment}, {@link JavadocComment}, {@link BlockComment})
     * @param commentMatchFn     predicate for selecting comments
     * @return a list of matching comments
     */
    public static <C extends Comment> List<C> listComments(
            Node astRootNode, Class<C> commentTargetClass, Predicate<C> commentMatchFn) {

        List<C> found = new ArrayList<>();
        forComments(astRootNode, c -> {
            if (commentTargetClass.isAssignableFrom(c.getClass())) {
                C cc = (C) c;
                if (commentMatchFn.test(cc)) {
                    found.add(cc);
                }
            }
        });
        return found;
    }

    /**
     * Comparator for Comments within an AST node that organizes based on the
     * start position
     */
    public static class CommentPositionComparator implements Comparator<Comment> {

        @Override
        public int compare(Comment o1, Comment o2) {
            if (o1.getBegin().isPresent() && o2.getBegin().isPresent()) {
                return o1.getBegin().get().compareTo(o2.getBegin().get());
            }
            //if one or the other doesnt have a begin
            // put the one WITHOUT a being BEFORE the other
            // if neither have a being, return
            if (!o1.getBegin().isPresent() && !o2.getBegin().isPresent()) {
                return 0;
            }
            if (o1.getBegin().isPresent()) {
                return -1;
            }
            return 1;
        }
    }
}
