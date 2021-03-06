package org.jdraft;

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
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.nio.file.Paths;

import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.io._in;
import org.jdraft.text.Text;
import org.jdraft.walk.Walk;

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
     * Build a JavaParser configuration that includes "Post processing" negative numbers usually stored as:
     *  <PRE>
     *       ...
     *        └─"-10000" UnaryExpr : (1,18)-(1,25)
     *          └─"10000" IntegerLiteralExpr : (1,21)-(1,25)
     *  </PRE>
     *  will be converted to:
     *  <PRE>
     *       └─"-10000" IntegerLiteralExpr : (1,18)-(1,25)
     *  </PRE>
     *  (we replace the UnaryExpr parent with an IntegerLiteralExpr containing a negative number literal)
     */
    private static final ParserConfiguration JAVAPARSER_CONFIG = new ParserConfiguration().setLanguageLevel( LanguageLevel.BLEEDING_EDGE);
    static{
        JAVAPARSER_CONFIG.getPostProcessors().add(new NegativeLiteralNumberPostProcessor());
    }

    /**
     * A static reference to a JavaParser (used to replace StaticJavaParser, in
     * order to properly handle module-info.java files and other "bleeding edge")
     * features
     */    
    public static final JavaParser JAVAPARSER = 
        new JavaParser(JAVAPARSER_CONFIG);

    public static class Classes{
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
    public static final Class<Comments> COMMENT = Comments.class;
    public static final Class<BlockComment> BLOCK_COMMENT = BlockComment.class;
    public static final Class<JavadocComment> JAVADOC_COMMENT = JavadocComment.class;
    public static final Class<LineComment> LINE_COMMENT = LineComment.class;

    /* -------------------------- EXPRESSIONS -----------------------------------*/
    /**
     * See also {@link Expr}
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
     * Yield statement "yield 6;"
     */
    public static final Class<YieldStmt> YIELD_STMT = YieldStmt.class;

    /**
     * case 1:
     *    return 'a';
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
    public static final Class<SwitchExpr> SWITCH_EXPR = SwitchExpr.class;
    
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
    }

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
    public static CompilationUnit reparse( CompilationUnit astCu) {
        return reparse(JAVAPARSER, astCu);
    }

    /**
     *
     * @param javaParser
     * @param astCu
     * @return
     */
    public static CompilationUnit reparse( JavaParser javaParser, CompilationUnit astCu) {
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
    public static CompilationUnit reparse( CompilationUnit astCu, PrettyPrinterConfiguration ppv ) {
        return reparse(JAVAPARSER, astCu, ppv);
    }

    /**
     *
     * @param javaParser
     * @param astCu
     * @param ppv
     * @return
     */
    public static CompilationUnit reparse( JavaParser javaParser, CompilationUnit astCu, PrettyPrinterConfiguration ppv ) {
        return of(javaParser, astCu.toString(ppv));
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
     * Parse and return the appropriate {@link Node} implementation based on the 
     * nodeClass and code
     *
     * @param nodeClass the class of the node (implementation class)
     * @param code the code for the AST to parse
     * @return the node implementation of the code
     */
    public static Node nodeOf(Class nodeClass, String... code) {
        if (nodeClass.isAssignableFrom( AnnotationExpr.class)) {
            return annotationExpr(code);
        }
        if (ImportDeclaration.class == nodeClass) {
            return importDeclaration(Text.combine(code));
        }
        if (Statement.class.isAssignableFrom(nodeClass)) {
            return Stmt.of(code);
        }
        if (Expression.class.isAssignableFrom(nodeClass)) {
            return Expr.of(code);
        }
        if (CatchClause.class.isAssignableFrom(nodeClass)) {
            return catchClause(code);
        }
        if (MethodDeclaration.class == nodeClass) {
            return methodDeclaration(code);
        }
        if (ConstructorDeclaration.class == nodeClass) {
            return constructorDeclaration(code);
        }        
        if (InitializerDeclaration.class == nodeClass) {
            return staticBlock(code);
        }
        if (TypeDeclaration.class.isAssignableFrom(nodeClass)) {
            return typeDeclaration(code);
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
            return variableDeclarator(code);
        }
        if (FieldDeclaration.class == nodeClass) {
            return fieldDeclaration(code);
        }
        if (EnumConstantDeclaration.class == nodeClass) {
            return constantDeclaration(code);
        }
        if (CompilationUnit.class == nodeClass) {
            return of(code);
        }
        if (Comment.class.isAssignableFrom(nodeClass)) {
            return comment(code);
        }
        if (AnnotationMemberDeclaration.class == nodeClass) {
            return annotationMemberDeclaration(code);
        }
        if (MethodDeclaration.class == nodeClass) {
            return methodDeclaration(code);
        }
        if( SimpleName.class == nodeClass){
            return new SimpleName(Text.combine(code) );
        }
        if( Name.class == nodeClass){
            return new Name( Text.combine(code));
        }
        if( Type.class.isAssignableFrom(nodeClass )){
            return Types.of( Text.combine(code));
        }
        if( Modifier.class == nodeClass ){
            return Modifiers.MODS_KEYWORD_TO_ENUM_MAP.get(Text.combine(code) );
        }        
        throw new _jdraftException("Could not parse Node of class " + nodeClass);
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
    public static TypeDeclaration typeDeclaration(Class clazz) {
        return Ast.typeDeclaration(clazz, _io.IN_DEFAULT);
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
    public static TypeDeclaration typeDeclaration(JavaParser javaParser, Class clazz) {
        return Ast.typeDeclaration(javaParser, clazz, _io.IN_DEFAULT);
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
    public static TypeDeclaration typeDeclaration(Class clazz, _in._resolver resolver) {
        return typeDeclaration(JAVAPARSER, clazz, resolver);
    }

    /**
     * Returns a TypeDeclaration, or a LocalClassDeclaration given the class and
     * resolver
     * @param javaParser the javaParser for the job
     * @param clazz the runtime class to lookup the source code and return AST
     * for
     * @param resolver how to look up the source code
     * @return the typeDeclaration or an exception
     */
    public static TypeDeclaration typeDeclaration(JavaParser javaParser, Class clazz, _in._resolver resolver) {
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
            CompilationUnit cu = parse(javaParser, _i.getInputStream());
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

            TypeDeclaration td = typeDeclaration( _i );
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
            //System.out.println( " LOCALS" + localCand);
            if (localCand.size() == 1) {
                ClassOrInterfaceDeclaration coid = localCand.get(0).getClassDeclaration();
                //System.out.println ("ORGI COID"+ coid);

                coid.setPublic(true);
                //System.out.println ("ORGI COID 2"+ coid.clone());
                if (localCand.get(0).getComment().isPresent()) {
                    coid.setComment(localCand.get(0).getComment().get());
                }

                //promote Local classes to a new Compilation Unit before caching
                //CompilationUnit cu = new CompilationUnit( ); //coid.toString() );
                String str = coid.toString();
                //System.out.println("COID STRING"+ str );
                CompilationUnit cu = parse( javaParser, str );
                cu.setStorage(_i.getPath());
                //cu.addType(coid);

                //System.out.println( "COMP UNIT "+ cu);
                //this seems to be getting rid of
                cu = Ast.reparse(javaParser, cu); //reparse to ensure the line numbers and positions are re evaluated
                coid = (ClassOrInterfaceDeclaration)cu.getType(0);
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
                //cu = Ast.reparse(cu);
                if (localCand.get(0).getComment().isPresent()) {
                    coid.setComment(localCand.get(0).getComment().get());
                }
                cu = Ast.reparse(cu); //reparse to ensure the line numbers and positions are re evaluated
                coid = (ClassOrInterfaceDeclaration) cu.getType(0);
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

        CompilationUnit cu = parse(javaParser, _i.getInputStream());
        cu.setStorage(_i.getPath());
        List<TypeDeclaration> tds
                = Walk.list(cu, TypeDeclaration.class, td -> td.getNameAsString().equals(clazz.getSimpleName())
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
        if(!str.startsWith("package")) {
            str = "package " + str;
        }
        if(!str.endsWith(";")){
            str = str + ";";
        }
        CompilationUnit cu = of(str); //+" \n class Nothing{}");

        PackageDeclaration pd = cu.getPackageDeclaration().get();
        cu.removePackageDeclaration();
        return pd;
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
        ParseResult<ImportDeclaration> pid = Ast.JAVAPARSER.parseImport(code);
        if( pid.isSuccessful() ) {
            return pid.getResult().get();
        }
        throw new _jdraftException("Could not parse import from \""+code+"\"");
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
        return of(JAVAPARSER, javaSourceCode);
    }

    /**
     * Creates an AST {@link CompilationUnit} from the code (CompilationUnit is
     * the top level entity for AST (representing a Java file), or throws a
     * _jDraftException if there is a problem readingor parsing the file
     *
     * @param javaParser the parser for the task
     * @param javaSourceCode
     * @return an AST CompilationUnit from
     */
    public static CompilationUnit of(JavaParser javaParser, String... javaSourceCode) throws _jdraftException {
        String str = Text.combine(javaSourceCode);
        return parse(javaParser, str);
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
        return of( JAVAPARSER, javaSourceFile);
    }

    /**
     * Creates an AST {@link CompilationUnit} from the data in the javaSourceFile
     * and returns it, or throws a _jDraftException if there is a problem reading
     * or parsing the file
     * @param javaParser the parser for the task
     * @param javaSourceFile a File containing the .java source contents to
     * build a _code model for (including Classes, and package-info.java & module-info.java)
     * @return the Ast CompilationUnit
     */
    public static CompilationUnit of(JavaParser javaParser, File javaSourceFile) throws _jdraftException {
        return parse(javaParser, javaSourceFile);
    }
    
    /**
     * Creates an AST {@link CompilationUnit} from the data in the inputStream
     * the top level
     * @param javaSourceInputStream the input stream containing .java source code
     * @return the CompilationUnit
     */
    public static CompilationUnit of(InputStream javaSourceInputStream) throws _jdraftException {
        return of(JAVAPARSER, javaSourceInputStream);
    }

    /**
     * Creates an AST {@link CompilationUnit} from the data in the inputStream
     * the top level
     * @param javaParser the parser for the task
     * @param javaSourceInputStream the input stream containing .java source code
     * @return the CompilationUnit
     */
    public static CompilationUnit of(JavaParser javaParser, InputStream javaSourceInputStream) throws _jdraftException {
        return parse(javaParser, javaSourceInputStream);
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
        return of(JAVAPARSER, clazz);
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
    public static CompilationUnit of(JavaParser javaParser, Class clazz) throws _jdraftException {
        Node n = typeDeclaration(javaParser, clazz);
        if (n instanceof CompilationUnit) {
            return (CompilationUnit) n;
        }
        //System.out.println("BEFORE >> "+ n );
        if( clazz.isMemberClass() || clazz.isLocalClass() || clazz.isAnonymousClass() ){
            CompilationUnit cu = of( n.toString() );
            //CompilationUnit cu = new CompilationUnit();
            //System.out.println("REFRESHED MEM >> "+cu);
            //cu.addType( (TypeDeclaration)n);
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
        return of(JAVAPARSER, javaSourceCodeReader);
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
    public static CompilationUnit of(JavaParser javaParser, Reader javaSourceCodeReader) throws _jdraftException {
        CompilationUnit cu = parse(javaParser, javaSourceCodeReader);
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
        return of(JAVAPARSER, pathToJavaSourceCode);
    }

    /**
     * parses the contents contained in the file at the path and returns an
     * AST CompilationUnit (or throws a _jDraftException if the .java source code
     * is invalid)
     * @param pathToJavaSourceCode the local path to a .java source file
     * @return an AST compilationUnit representing the source code
     * @throws _jdraftException if there is an issue reading or parsing the source code
     */
    public static CompilationUnit of(JavaParser javaParser, Path pathToJavaSourceCode) throws _jdraftException {
        CompilationUnit cu = parse(javaParser, pathToJavaSourceCode);
        cu.setStorage(pathToJavaSourceCode);
        return cu;        
    }

    /**
     * parse and return a CompilationUnit based on the .java source contents
     * that were read from the javaSourceReader
     * @param javaSourceReader the reader for the source
     * @return an AST CompilationUnit
     */
    protected static CompilationUnit parse(Reader javaSourceReader) {
        return parse(JAVAPARSER, javaSourceReader);
    }

    /**
     *
     * @param javaParser
     * @param javaSourceReader
     * @return
     */
    protected static CompilationUnit parse(JavaParser javaParser, Reader javaSourceReader) {
        ParseResult<CompilationUnit> pr = javaParser.parse(javaSourceReader);
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
    protected static CompilationUnit parse( File javaSourceFile) {
        return parse(JAVAPARSER, javaSourceFile);
    }

    protected static CompilationUnit parse( JavaParser javaParser, File javaSourceFile) {
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
     * Given an array of lines representing some .java source code, parse it and
     * return the AST CompilationUnit based on the source
     * @param linesOfJavaSourceCode source code lines
     * @return an AST CompilationUnit
     */
    protected static CompilationUnit parse(String... linesOfJavaSourceCode) {
        return parse(JAVAPARSER, linesOfJavaSourceCode);
    }

    /**
     * Given an array of lines representing some .java source code, parse it and
     * return the AST CompilationUnit based on the source
     * @param javaParser the configured JavaParser instance
     * @param linesOfJavaSourceCode source code lines
     * @return an AST CompilationUnit
     */
    protected static CompilationUnit parse(JavaParser javaParser, String... linesOfJavaSourceCode) {
        String str = Text.combine(linesOfJavaSourceCode);
        ParseResult<CompilationUnit> pr = javaParser.parse(str);
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
                    pr = javaParser.parse(str);
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
    protected static CompilationUnit parse(Path pathToJavaSourceCode ) {
        return parse(JAVAPARSER, pathToJavaSourceCode);
    }

    /**
     *
     * @param javaParser the configured JavaParser instance
     * @param pathToJavaSourceCode
     * @return
     */
    protected static CompilationUnit parse(JavaParser javaParser, Path pathToJavaSourceCode ) {
        ParseResult<CompilationUnit> pr;
        try {
            pr = javaParser.parse(pathToJavaSourceCode);
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
    protected static CompilationUnit parse(InputStream javaSourceInputStream) {
        return parse(JAVAPARSER, javaSourceInputStream);
    }

    /**
     * read and parse the inputStream containing .java source code and return
     * the AST {@link CompilationUnit} representation of the .java source
     * @param javaParser the configured JavaParser instance
     * @param javaSourceInputStream input Stream containing .java source
     * @return an AST CompilationUnit representation of the source
     */
    protected static CompilationUnit parse(JavaParser javaParser, InputStream javaSourceInputStream) {
        ParseResult<CompilationUnit> pr = javaParser.parse(javaSourceInputStream, StandardCharsets.UTF_8);
        if( !pr.isSuccessful() ){
            throw new _jdraftException("Unable to parse text in inputStream :"+pr.getProblems());
        }
        return pr.getResult().get();
    }

    public static TypeDeclaration typeDeclaration(_in in ){
        TypeDeclaration td = typeDeclaration(in.getInputStream() );
        if( td.findCompilationUnit().isPresent() ){
            td.findCompilationUnit().get().setStorage(in.getPath() );
        }
        return td;
    }
    
    /**
     * @param is the input Stream walk
     * @return the Top Level Ast Node (CompilationUnit, TypeDeclaration)
     */
    public static TypeDeclaration typeDeclaration(InputStream is) {
        CompilationUnit cu = parse(is);
        if( cu.getPrimaryType().isPresent()){
            return cu.getPrimaryType().get();
        }
        //List<TypeDeclaration> tds = listAll(cu, TypeDeclaration.class);
        List<TypeDeclaration> tds = Walk.list(cu, TypeDeclaration.class);
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
    public static BodyDeclaration bodyDeclaration(String... code) {
        String allCode = Text.combine(code);
        ParseResult<BodyDeclaration> pbd = JAVAPARSER.parseBodyDeclaration(allCode );
        if( pbd.isSuccessful() ){
            return pbd.getResult().get();
        }
        throw new _jdraftException("Invalid BodyDeclaration : " + System.lineSeparator()
                + Text.indent(allCode) + System.lineSeparator()
                + pbd.getProblems());
    }

    /**
     * Builds a {@link MethodDeclaration} from the code and returns it
     *
     * @param code the code making up the methods (may have Javadoc comments &
     * annotations)
     * @return the JavaParser {@link MethodDeclaration}
     */
    public static MethodDeclaration methodDeclaration(String... code) {
        MethodDeclaration md = (MethodDeclaration) bodyDeclaration(code);

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
    public static ConstructorDeclaration constructorDeclaration(String... code) {

        ConstructorDeclaration cd = (ConstructorDeclaration) bodyDeclaration(code);

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
    public static ExplicitConstructorInvocationStmt constructorCallStmt(String... code ) {
        return Stmt.constructorCallStmt(  code );
    }

    /**
     * i.e."this(100,2900);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt thisCallStmt(String... code ) {
        return Stmt.constructorCallStmt(  code );
    }

    /**
     * i.e. "super('c', 123);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt superCallStmt(String... code ) {
        return Stmt.constructorCallStmt(  code );
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

    public static MethodCallExpr methodCallExpr(String... code) {
        return Expr.methodCallExpr(code);
    }

    public static Name name(String code) {
        ParseResult<Name> pn = JAVAPARSER.parseName(code);
        if( pn.isSuccessful() ){
            return pn.getResult().get();
        }
        throw new _jdraftException("Unable to parse Name \""+ code+ "\""+System.lineSeparator()+ pn.getProblems());
    }

    public static SimpleName simpleName(String code) {
        ParseResult<SimpleName> pn = JAVAPARSER.parseSimpleName(code);
        if( pn.isSuccessful() ){
            return pn.getResult().get();
        }
        throw new _jdraftException("Unable to parse SimpleName \""+ code+ "\""+System.lineSeparator()+ pn.getProblems());
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
        LambdaExpr le = Expr.lambdaExpr( pa +"->{return 1;}");

        NodeList<Parameter> nps = le.getParameters();
        if (nps.getParentNode().isPresent()) {
            nps.getParentNode().get().removeForced();
        }
        return le.getParameters();
    }

    public static Parameter parameter(String... code) {
        String allCode = Text.combine(code);
        ParseResult<Parameter> pn = JAVAPARSER.parseParameter(allCode);
        if( pn.isSuccessful() ){
            return pn.getResult().get();
        }
        throw new _jdraftException("Unable to parse Parameter \""+ allCode+ "\""+System.lineSeparator()+ pn.getProblems());
    }

    /**
     *
     * @param code the lines of code
     * @return a List of FieldDeclarations
     */
    public static List<FieldDeclaration> fieldDeclarations(String... code) {

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
    public static FieldDeclaration fieldDeclaration(String... code) {
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
    public static VariableDeclarationExpr variableDeclarationExpr(String...code ){
        return Expr.variablesExpr(code);
    }

    /**
     * Parses and returns a {@link VariableDeclarator} based on the code provided
     * (the underlying class for Both member fields {@link FieldDeclaration} & local
     * {@link VariableDeclarationExpr} variables defined with code bodies
     *
     *
     * @see #variableDeclarationExpr(String...)
     * @see #fieldDeclaration(String...)
     *
     * @param code
     * @return
     */
    public static VariableDeclarator variableDeclarator(String... code) {
        VariableDeclarator vd = fieldDeclaration(code).getVariable(0);
        vd.removeForced();
        return vd;
    }

    /**
     * Returns the annotation Member (with ANNOTATIONS and Javadocs)
     *
     * @param code
     * @return
     */
    public static AnnotationMemberDeclaration annotationMemberDeclaration(String... code) {
        String str = Text.combine(code);
        //if( !str.endsWith("();") ){
        //    str = str+"();";
       // }
        if( !str.endsWith(";") ){
            str = str+";";
        }
        str = "@interface UNKNOWN{" + System.lineSeparator() + str + System.lineSeparator() + "}";
        AnnotationDeclaration ad = (AnnotationDeclaration)Ast.typeDeclaration(str);
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
    public static TypeDeclaration typeDeclaration(String... code) {
        return typeDeclaration( Ast.JAVAPARSER, code);
    }

    /**
     *
     * @param javaParser
     * @param code
     * @return
     */
    public static TypeDeclaration typeDeclaration(JavaParser javaParser, String... code) {
        CompilationUnit cu = Ast.of(javaParser, code);
        
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

    public static NullLiteralExpr nullLiteralExpr() {
        return Expr.nullExpr();
    }

    /**
     * convert the String code into a single AST Expression nod
     *
     * @param code
     * @return
     */
    public static Expression expression(String... code) {
        return Expr.of(code);
    }

    public static IntegerLiteralExpr intLiteralExpr(int intValue) {
        return Expr.of(intValue);
    }

    public static BooleanLiteralExpr booleanLiteralExpr(boolean booleanValue) {
        return Expr.of(booleanValue);
    }

    public static CharLiteralExpr charLiteralExpr(char charValue) {
        return Expr.of(charValue);
    }

    public static LongLiteralExpr longLiteralExpr(long longValue) {
        return Expr.of(longValue);
    }

    public static DoubleLiteralExpr doubleLiteralExpr(float floatValue) {
        return Expr.of(floatValue);
    }

    public static DoubleLiteralExpr doubleLiteralExpr(double doubleValue) {
        return Expr.of(doubleValue);
    }

    public static JavaParser PARSER = new JavaParser( new ParserConfiguration().setLanguageLevel(LanguageLevel.BLEEDING_EDGE));

    /**
     * Java 12+ switch Expression
     * 
     * @param switchExpr
     * @return 
     */
    public static SwitchExpr switchExpr(String... switchExpr ){
        return (SwitchExpr)PARSER.parseExpression( Text.combine(switchExpr) ).getResult().get();
    }
    
    /**
     * Found in Java 12+ switch expressions
     * 
     * @param caseExpr
     * @return 
     */
    public static SwitchEntry caseEntry(String...caseExpr ){
        SwitchExpr se = (SwitchExpr)(PARSER.parseExpression("switch(x) { " +  Text.combine(caseExpr) + "};")).getResult().get();
        return se.getEntry(0);
    }

    /**
     * Builds a JavaParser AST model of an Anonymous Class (an {@link ObjectCreationExpr} based on the
     * real code passed in:
     * i.e.<PRE>
     *     ObjectCreationExpr oce = Ast.anonymousClass( new Object(){
     *        public int a;
     *        public void SomeMethod(){
     *            System.out.println(1);
     *        }
     *     });
     * </PRE>
     * @param anonymousClassImplementation
     * @return the ObjectCreationExpr model instance of the runtime instance
     */
    public static ObjectCreationExpr anonymousClassExpr(Object anonymousClassImplementation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return Expr.newExpr(ste, _io.IN_DEFAULT);
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
        ParseResult<Statement> pn = JAVAPARSER.parseStatement("switch (a){" + se + System.lineSeparator() + "}");
        if( pn.isSuccessful() ){
            SwitchEntry ses = pn.getResult().get().asSwitchStmt().getEntry(0);
            ses.remove();
            return ses;
        }
        throw new _jdraftException("Unable to parse SwitchEntry \""+ se+ "\""+System.lineSeparator()+ pn.getProblems());
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
            Statement st = statement(TRY_HARDCODED + res);
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
    public static Statement statement(String... code) {
        return Stmt.of(code);
    }

    /**
     * Parse the code as (one or more) AST {@link AnnotationExpr}s
     *
     * @param code the code that is an annotation
     * @return NodeList of {@link AnnotationExpr}s
     */
    public static NodeList<AnnotationExpr> annotationExprs(String... code) {
        String f = Text.combine(code) + System.lineSeparator() + "int f;";
        FieldDeclaration fd = fieldDeclaration(f);
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
    public static AnnotationExpr annotationExpr(String... code) {
        String s = Text.combine(code);

        if(!s.startsWith("@")) {
            s = "@"+s;
        }

        ParseResult<AnnotationExpr> pn = JAVAPARSER.parseAnnotation(s);
        if( pn.isSuccessful() ){
            return pn.getResult().get();
        }
        throw new _jdraftException("Unable to parse AnnotationExpr \""+ s+ "\""+System.lineSeparator()+ pn.getProblems());
    }

    /**
     * Parses and returns a new EnumConstantDeclaration
     *
     * @param code
     * @return
     */
    public static EnumConstantDeclaration constantDeclaration(String... code) {
        String comb = Text.combine(code).trim();
        if (comb.endsWith(";")) {
            EnumDeclaration ed = (EnumDeclaration)Ast.typeDeclaration("enum E{ " + System.lineSeparator() + comb + System.lineSeparator() + " }");
            EnumConstantDeclaration ecd = ed.getEntry(0);
            ecd.removeForced(); //disconnect
            return ecd;
        }
        if (comb.endsWith(",")) {
            EnumDeclaration ed = (EnumDeclaration)Ast.typeDeclaration("enum E{ " + System.lineSeparator() + comb.substring(0, comb.length() - 1) + ";" + System.lineSeparator() + " }");
            EnumConstantDeclaration ecd = ed.getEntry(0);
            ecd.removeForced(); //disconnet
            return ecd;
        }
        EnumDeclaration ed = (EnumDeclaration)Ast.typeDeclaration("enum E{ " + System.lineSeparator() + comb + ";" + System.lineSeparator() + " }");
        EnumConstantDeclaration ecd = ed.getEntry(0);
        ecd.removeForced(); //disconnet
        return ecd;
    }

    public static ReceiverParameter receiverParameter(String str) {
        String rp = "void blah(" + str + "){}";
        MethodDeclaration md = methodDeclaration(rp);
        ReceiverParameter rep = md.getReceiverParameter().get();
        rep.removeForced(); //disconnect
        return rep;
    }

    public static InitializerDeclaration initializerDeclaration(String...code ){
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
        InitializerDeclaration id = (InitializerDeclaration) Ast.typeDeclaration(str).getMembers().get(0);
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
        InitializerDeclaration id = initializerDeclaration(code);
        id.setStatic(true);
        return id;
    }
}
