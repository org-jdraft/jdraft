package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import static org.jdraft.Ast.*;

import org.jdraft._annos._withAnnos;
import org.jdraft._annotation._entry;
import org.jdraft._body._hasBody;

import org.jdraft._modifiers.*;
import org.jdraft._constructor._withConstructors;
import org.jdraft._javadoc._withJavadoc;
import org.jdraft._method._withMethods;
import org.jdraft._receiverParameter._withReceiverParameter;
import org.jdraft._initBlock._withInitBlocks;
import org.jdraft._throws._withThrows;
import org.jdraft._type._withExtends;
import org.jdraft._type._withImplements;
import org.jdraft.text.Text;

/**
 * <P>A "Meta-Model" implementation/view of entities for representing java source code
 * in a concrete way that sits "above" that AST implementation
 * API adapting the access & manipulation of an interconnected nodes of an AST
 * as an intuitive logical model.
 *
 * <P>Logical Facade instances store NO DATA, but references(s) into
 * {@link com.github.javaparser.ast.Node}s of the AST, and
 * provide a simple API to query or manipulate the AST as is it were
 * a simple Dto or VALUE object.
 *
 * <P>To put it another way, _java models that DO HAVE STATE STORED IN THE UNDERLYING AST,
 * but that state is (so each entity has a pointer to an AST Node that can be operated on)
 *
 * The purpose of these inner interfaces is to define the relationship between
 * the AST Node(s) and the Logical entities... and trying to provide consistency
 * for how the logical API presents operations for adding, removing, and
 * replacing the underlying AST Nodes.
 *
 * The _model HIDES certain details that exist in the AST
 * (for instance all of the Position details for line and column numbers where the text of a component is
 * declared in the source file)
 *
 * The _model also simplifies ways of interacting with entities (in a logical fashion)
 * (for instance, we can extend a _type by passing in a Runtime Class, and this class is
 * converted into a AST component and both added to the extensions, AND the class is added
 * as an AST import if required)
 * 
 * Translates between AST {@link Node} entities to {@link _java} runtime
 * entities deals with Runtime Java Classes.
 *
 * This attempts to hel[p unify the many AST /_java models and model types so we
 * can convert/ translate between :
 * <UL>
 * <LI>a Plain Text String representation of a Java Method
 * <LI>a runtime Reflective entity (java.lang.reflect.Method)
 * <LI>an AST entity( com.github.javaparser.ast.body.MethodDeclaration)
 * <LI>a draft _java (draft.java._method) representation
 * </UL>
 *
 * @author Eric
 */
public interface _java {

    /**
     * Marker interface for ALL models and interfaces related to Java Language Constructs
     *
     * @see _multiPart a one-to-one mapping between an AST (Node) and a <CODE>_javaDomain</CODE> ( _method <--> MethodDeclaration )
     * @see _list a one-to 0 or more NodeList instances of AST nodes ( _parameters <--> NodeList<Parameter> )
     */
    interface _domain { }

    /**
     * Parse and return the appropriate node based on the Node class (the Node
     * class can be a _model, or Ast Node class
     *
     * @param nodeClass the class of the node (implementation class)
     * must extend {@link _domain} or {@link Node}
     * @param code the java source code representation
     * @return the node implementation of the code
     */
    static Node node(Class nodeClass, String... code) {
        
        if (! _domain.class.isAssignableFrom(nodeClass)) {
            return Ast.nodeOf(nodeClass, code);
        }
        if (_import.class == nodeClass) {
            return importDeclaration(Text.combine(code) );
        }
        if (_anno.class == nodeClass) {
            return anno(code);
        }
        if (_method.class == nodeClass) {
            return method(code);
        }
        if (_constructor.class == nodeClass) {
            return constructor(code);
        }
        if (_typeRef.class == nodeClass) {
            return typeRef(Text.combine(code).trim());
        }
        if (_initBlock.class == nodeClass) {
            return staticBlock(code);
        }
        if (_type.class.isAssignableFrom(nodeClass)) {
            return Ast.typeDecl(code);
        }
        if (_parameter.class == nodeClass) {
            return parameter(code);
        }
        if (_receiverParameter.class == nodeClass) {
            return receiverParameter(Text.combine(code));
        }
        /*
        if (_variable.class == nodeClass) {
            return field(code);
        }
         */
        if (_field.class == nodeClass) {
            return field(code);
        }
        if (_constant.class == nodeClass) {
            return constantDecl(code);
        }
        if (_entry.class == nodeClass) {
            return annotationMemberDecl(code);
        }
        if (_method.class == nodeClass) {
            return method(code);
        }
        throw new _jdraftException("Could not parse Node " + nodeClass);
    }

    /**
     * Builds the appropriate _model entities based on AST Nodes provided (Note:
     * since there are no logical entities for
     * {@link com.github.javaparser.ast.expr.Expression}, or
     * {@link com.github.javaparser.ast.stmt.Statement} Node implementations,
     * this will fail if these are passed in the input
     * <PRE>
     * handles:
     * all {@link _type}s:
     * {@link _annotation}, {@link _class}, {@link _enum}, {@link _interface}
     * {@link _anno}
     * {@link _entry}
     * {@link _constructor}
     * {@link _constant}
     * {@link _field}
     * {@link _javadoc}
     * {@link _method}
     * {@link _parameter}
     * {@link _receiverParameter}
     * {@link _initBlock}
     * {@link _typeParameter}
     * {@link _typeRef}
     * </PRE>
     *
     * @param astNode the ast node
     * @return the _model entity
     */
    static _domain of(Node astNode) {
        if( astNode instanceof Expression ){
            return _expression.of( (Expression)astNode);
        }
        if( astNode instanceof Statement ){
            return _statement.of( (Statement)astNode);
        }
        if( astNode instanceof Comment ){
            return _comment.of( (Comment)astNode);
        }
        if( astNode instanceof ArrayCreationLevel){
            return _arrayDimension.of( (ArrayCreationLevel)astNode);
        }
        if (astNode instanceof ImportDeclaration ){
            return _import.of((ImportDeclaration) astNode);
        }
        if (astNode instanceof AnnotationExpr) {
            return _anno.of((AnnotationExpr) astNode);
        }
        if( astNode instanceof PackageDeclaration ){
            return _package.of( (PackageDeclaration)astNode);
        }
        if (astNode instanceof AnnotationDeclaration) {
            return _annotation.of((AnnotationDeclaration) astNode);
        }
        if (astNode instanceof AnnotationMemberDeclaration) {
            return _entry.of((AnnotationMemberDeclaration) astNode);
        }
        if (astNode instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration cois = (ClassOrInterfaceDeclaration) astNode;
            if (cois.isInterface()) {
                return _interface.of(cois);
            }
            return _class.of(cois);
        }
        if (astNode instanceof ConstructorDeclaration) {
            return _constructor.of((ConstructorDeclaration) astNode);
        }
        if (astNode instanceof EnumDeclaration) {
            return _enum.of((EnumDeclaration) astNode);
        }
        if (astNode instanceof EnumConstantDeclaration) {
            return _constant.of((EnumConstantDeclaration) astNode);
        }
        if (astNode instanceof LambdaExpr) {
            return _lambda.of((LambdaExpr) astNode);
        }
        if (astNode instanceof VariableDeclarator) {

            VariableDeclarator vd = (VariableDeclarator) astNode;
            if( vd.getParentNode().isPresent()){
                //System.out.println("PARENT "+vd.getParentNode().get().getClass());
                if( vd.getParentNode().get() instanceof FieldDeclaration){
                    return _field.of(vd);
                }
            }
            return _variable.of(vd);
            //return _field.of(vd);
        }
        if (astNode instanceof FieldDeclaration) {
            FieldDeclaration fd = (FieldDeclaration) astNode;
            if (fd.getVariables().size() > 1) {
                throw new _jdraftException(
                        "Ambiguious node for FieldDeclaration " + fd + "pass in VariableDeclarator instead " + fd);
            }
            return _field.of(fd.getVariable(0));
        }
        if (astNode instanceof BlockStmt) {
            if (astNode.getParentNode().isPresent()) {
                if (astNode.getParentNode().get() instanceof NodeWithBlockStmt) {
                    return _body.of((NodeWithBlockStmt) astNode.getParentNode().get());
                }
                if (astNode.getParentNode().get() instanceof NodeWithOptionalBlockStmt) {
                    return _body.of((NodeWithOptionalBlockStmt) astNode.getParentNode().get());
                }
            }
            throw new _jdraftException("Unable to return draft _java node for BlockStmt without NodeWithBlockStmt parent");
        }
        if (astNode instanceof JavadocComment) {
            JavadocComment jdc = (JavadocComment) astNode;
            if (jdc.getParentNode().isPresent()) {
                return _javadoc.of((NodeWithJavadoc) jdc.getParentNode().get());
            }
            throw new _jdraftException("No Parent provided for JavadocComment");
        }
        if (astNode instanceof MethodDeclaration) {
            MethodDeclaration md = (MethodDeclaration) astNode;
            return _method.of(md);
        }
        if( astNode instanceof Name){
            return _name.of( (Name)astNode);
        }
        if( astNode instanceof SimpleName){
            return _name.of( (SimpleName)astNode);
        }
        if( astNode instanceof SwitchEntry){
            return _switchEntry.of( (SwitchEntry)astNode);
        }
        if( astNode instanceof Modifier){
            return _modifier.of( (Modifier)astNode);
        }
        if (astNode instanceof Parameter) {
            return _parameter.of((Parameter) astNode);
        }
        if (astNode instanceof InitializerDeclaration) {
            InitializerDeclaration id = (InitializerDeclaration) astNode;
            return _initBlock.of(id);
        }
        if (astNode instanceof ReceiverParameter) {
            ReceiverParameter rp = (ReceiverParameter) astNode;
            return _receiverParameter.of(rp);
        }
        if (astNode instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) astNode;
            return _typeParameter.of(tp);
        }
        if (astNode instanceof Type) {
            return _typeRef.of((Type) astNode);
        }
        if( astNode instanceof CatchClause ){
            return _catch.of( (CatchClause)astNode);
        }
        if (astNode instanceof CompilationUnit) {
            return _codeUnit.of((CompilationUnit) astNode);
        }
        if( astNode instanceof ModuleDeclaration ){
            return _moduleInfo.of( (ModuleDeclaration)astNode);
        }
        throw new _jdraftException("Unable to create _java entity from " + astNode+" "+astNode.getClass());
    }

    /**
     * A "concrete" way of identifying/addressing elements and properties that are the components
     * of a Java program. A way to consistently name things when we construct and deconstruct
     * Components of Java programs (external tools for building & matching &
     * diffing can be more valuable having the opportunity to compare like for
     * like (by componentizing things out and comparing or matching on a part by
     * part basis)
     */
    enum Component {
        MODULE_DECLARATION("moduleDeclaration", ModuleDeclaration.class),
        PACKAGE("package", PackageDeclaration.class),
        /** i.e. @Deprecated @NotNull */
        ANNOS("annos", _annos.class),
        /** i.e. @Deprecated */
        ANNO("anno", _anno.class),
        CLASS("class", _class.class),
        ENUM("enum", _enum.class),
        INTERFACE("interface", _interface.class),
        ANNOTATION("annotation", _annotation.class),
        BODY("body", _body.class),

        MODIFIERS("modifiers", _modifiers.class), //List.class, Modifier.class),
        MODIFIER("modifier", _modifier.class),

        HEADER_COMMENT("header", Comments.class),
        JAVADOC("javadoc", _javadoc.class),
        PARAMETERS("parameters", _parameters.class),
        PARAMETER("parameter", _parameter.class),
        RECEIVER_PARAMETER("receiverParameter", _receiverParameter.class),
        TYPE_PARAMETERS("typeParameters", _typeParameters.class),
        TYPE_PARAMETER("typeParameter", TypeParameter.class), //_typeParameter.class
        THROWS("throws", _throws.class),
        NAME("name", String.class),

        KEY_VALUES("keyValues", List.class, MemberValuePair.class), //anno
        KEY_VALUE("keyValue", MemberValuePair.class), //anno

        IMPORTS("imports", _imports.class),
        IMPORT("import", _import.class), //todo change to _import

        STATIC("static", Boolean.class),
        WILDCARD("wildcard", Boolean.class),
        ELEMENTS("elements", List.class, _entry.class), //_annotation
        ELEMENT("element", _entry.class), //annotation
        FIELDS("fields", List.class, _field.class),
        FIELD("field", _field.class),
        INNER_TYPES("innerType", List.class, _type.class),
        INNER_TYPE("innerType", _type.class),

        COMPANION_TYPES( "companionTypes", List.class, _type.class),
        COMPANION_TYPE( "companionType", _type.class),

        TYPE("type", _typeRef.class), //annotation.element
        DEFAULT("default", Expression.class), //_annotation.element

        EXTENDS("extends", List.class, ClassOrInterfaceType.class), //_class, //_interface
        IMPLEMENTS("implements", List.class, ClassOrInterfaceType.class), //_class, _enum
        INIT_BLOCKS("initBlocks", List.class, _initBlock.class), //class, _enum
        INIT_BLOCK("initBlocks", _initBlock.class), //class, _enum
        CONSTRUCTORS("constructors", List.class, _constructor.class), //class, _enum
        CONSTRUCTOR("constructor", _constructor.class),
        METHODS("methods", List.class, _method.class), //class, _enum, _interface, _enum._constant
        METHOD("method", _method.class),
        CONSTANTS("constants", List.class, _constant.class),
        CONSTANT("constant", _constant.class), //_enum

        ARGUMENT("argument", Expression.class), //_enum._constant
        ARGUMENTS("arguments", List.class, Expression.class), //_enum._constant

        INIT("init", Expression.class), //field
        FINAL("final", Boolean.class), //_parameter
        VAR_ARG("varArg", Boolean.class), //parameter

        AST_TYPE("astType", Type.class), //typeRef
        ARRAY_LEVEL("arrayLevel", Integer.class), //_typeRef
        ELEMENT_TYPE("elementType", Type.class), //array _typeRef

        //new stuff for Statements and expressions
        TRY_BODY("tryBody", BlockStmt.class),
        CATCH_CLAUSES( "catchClauses", List.class, CatchClause.class), //tryStmt
        FINALLY_BODY( "finallyBody", BlockStmt.class),
        WITH_RESOURCES("withResources", List.class, Expression.class), //tryStmt

        STATEMENTS("statements", List.class, Statement.class), //statements of a switch entry
        SWITCH_SELECTOR("switchSelector", Expression.class),
        SWITCH_ENTRIES("switchEntries", List.class, SwitchEntry.class), //TODO change to _switchEntry
        SWITCH_BODY_TYPE("switchBodyType", com.github.javaparser.ast.stmt.SwitchEntry.Type.class),
        SWITCH_LABELS("switchLabels", List.class, Expression.class),
        ARRAY_NAME("arrayName", Expression.class), //arrayAccess
        INDEX("index", Expression.class), //arrayAccess
        VALUES("values", List.class, Expression.class), //ArrayInit
        TARGET("target", Expression.class), //assign
        VALUE("value", Expression.class), //assign
        LEFT( "left", Expression.class), //binaryExpression
        RIGHT( "right", Expression.class), //binaryExpression
        BINARY_OPERATOR( "binaryOperator", BinaryExpr.Operator.class), //binaryExpression
        UNARY_OPERATOR( "unaryOperator", UnaryExpr.Operator.class), //unaryExpression
        EXPRESSION("expression", Expression.class), //CastExpr
        CONDITION("condition", Expression.class), //ternary
        THEN("then", Expression.class),    //ternary
        ELSE("else", Expression.class),   //ternary
        INNER("inner", Expression.class), //enclosedExpr
        SCOPE("scope", Expression.class), //fieldAccessExpr
        TYPE_ARGUMENTS("typeArguments", List.class, Type.class), //methodCall
        IDENTIFIER("identifier", String.class),  //methodReference
        ANONYMOUS_CLASS_BODY("anonymousClassBody", List.class, BodyDeclaration.class),//_new
        TYPE_NAME("typeName", String.class), //_super superExpr
        VARIABLES("variables", List.class, _variable.class), //VariableDeclarator.class),
        CHECK("check", Expression.class), //assertStmt
        MESSAGE("message", Expression.class), //assertStmt
        LABEL("label", String.class), //breakStmt, labeledStmt
        THIS_CALL("thisCall", Boolean.class), //constructorCallStmt
        SUPER_CALL("superCall", Boolean.class), //constructorCallStmt
        ITERABLE("iterable", Expression.class), //forEachStmt
        VARIABLE("variable", VariableDeclarationExpr.class), //forEachStmt
        INITIALIZATION("initialization", List.class, Expression.class), //forStmt
        UPDATE("update", List.class, Expression.class),
        COMPARE("compare", Expression.class),
        STATEMENT("statement", Statement.class), //labeledStatment
        ARRAY_DIMENSIONS("arrayDimensions", List.class, ArrayCreationLevel.class), //arrayCreate
        ARRAY_DIMENSION("arrayDimension", Expression.class),
        ENCLOSED_PARAMETERS( "enclosedParameters", Boolean.class),
        LITERAL("literal", Object.class), //typeRef, textBlock
        ASSIGN_OPERATOR("assignOperator", AssignExpr.Operator.class);


        public final String name;
        public final Class implementationClass;
        public final Class elementClass;

        Component(String name, Class implementationClass) {
            this.name = name;
            this.implementationClass = implementationClass;
            this.elementClass = null;
        }

        Component(String name, Class containerClass, Class elementClass) {
            this.name = name;
            this.implementationClass = containerClass;
            this.elementClass = elementClass;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public static Component of(String name) {
            Optional<Component> op = Arrays.stream(Component.values()).filter(p -> p.name.equals(name)).findFirst();
            if (op.isPresent()) {
                return op.get();
            }
            return null;
        }

        public static <_N extends _multiPart> Component of(Class<_N> nodeClass) {
            Optional<Component> op = Arrays.stream(Component.values()).filter(p -> p.implementationClass.equals(nodeClass)).findFirst();
            if (op.isPresent()) {
                return op.get();
            }
            return null;
        }

        /**
         * Returns the appropriate Component based on the _type
         *
         * @param t the type instance
         * @return the Component
         */
        public static Component getComponent(_type t) {
            if (t instanceof _class) {
                return Component.CLASS;
            }
            if (t instanceof _interface) {
                return Component.INTERFACE;
            }
            if (t instanceof _enum) {
                return Component.ENUM;
            }
            return Component.ANNOTATION;
        }
    }

    /**
     * Mappings from JavaParser AST models (i.e. {@link AnnotationExpr}) 
     * ...to jdraft _java models (i.e. {@link _anno})
     */
    class Model {

        /*
            Easy access to the draft _java classes and interfaces that represent
            entities... his allows convenient autocomplete when you do a _walk.in
            of _walk.list, etc.
            <PRE>
            _walk.list( _c, _java.THROWS );
            _walk.list( _c, _java.THROWS );
            </PRE>

            to make it similar (in feel) to Ast.* :
            _walk.in( _c, Ast.INITIALIZER_DECLARATION,
                id-> id.getBody().add(Stmt.of("System.out.println(1);"));
            _walk.list( _m, Ast.RETURN_STMT );
          */
        Class<_codeUnit> CODE = _codeUnit.class;

        Class<_packageInfo> PACKAGE_INFO = _packageInfo.class;
        Class<_moduleInfo> MODULE_INFO = _moduleInfo.class;

        Class<_type> TYPE = _type.class;

        Class<_class> CLASS = _class.class;
        Class<_enum> ENUM = _enum.class;
        Class<_interface> INTERFACE = _interface.class;
        Class<_annotation> ANNOTATION = _annotation.class;

        Class<_method> METHOD = _method.class;
        Class<_field> FIELD = _field.class;
        Class<_constructor> CONSTRUCTOR = _constructor.class;

        /** ENUM constant i.e. enum E { CONSTANT; }*/
        Class<_constant> CONSTANT = _constant.class;

        /** Annotation Element i.e. @interface A{ int element(); }*/
        Class<_entry> ELEMENT = _entry.class;

        Class<_body> BODY = _body.class;
        /** an annotation use i.e. @Deprecated */
        Class<_anno> ANNO = _anno.class;
        /** group of annotation usages  on a single entity */
        Class<_annos> ANNOS = _annos.class;
        Class<_import> IMPORT = _import.class;
        Class<_imports> IMPORTS = _imports.class;
        Class<_javadoc> JAVADOC = _javadoc.class;

        Class<_modifiers> MODIFIERS = _modifiers.class;
        Class<_parameter> PARAMETER = _parameter.class;
        Class<_parameters> PARAMETERS = _parameters.class;
        Class<_typeParameter> TYPE_PARAMETER = _typeParameter.class;
        Class<_typeParameters> TYPE_PARAMETERS = _typeParameters.class;
        Class<_receiverParameter> RECEIVER_PARAMETER = _receiverParameter.class;
        Class<_initBlock> STATIC_BLOCK = _initBlock.class;
        Class<_throws> THROWS = _throws.class;
        Class<_typeRef> TYPEREF = _typeRef.class;

        /**
         * The classes below are categorical interfaces that are applied to classes
         */
        Class<_multiPart> NODE = _multiPart.class;
        Class<_declared> MEMBER = _declared.class;
        Class<_withName> NAMED = _withName.class;
        Class<_withNameTypeRef> NAMED_TYPE = _withNameTypeRef.class;

        Class<_withThrows> HAS_THROWS = _withThrows.class;
        Class<_hasBody> HAS_BODY = _hasBody.class;
        Class<_withAnnos> HAS_ANNOS = _withAnnos.class;
        Class<_withConstructors> HAS_CONSTRUCTORS = _withConstructors.class;
        Class<_withJavadoc> HAS_JAVADOC = _withJavadoc.class;
        Class<_withMethods> HAS_METHODS = _withMethods.class;
        Class<_withModifiers> HAS_MODIFIERS = _withModifiers.class;
        Class<_parameters._withParameters> HAS_PARAMETERS = _parameters._withParameters.class;
        Class<_withReceiverParameter> HAS_RECEIVER_PARAMETER = _withReceiverParameter.class;
        Class<_withInitBlocks> HAS_STATIC_BLOCKS = _withInitBlocks.class;
        Class<_withExtends> HAS_EXTENDS = _withExtends.class;
        Class<_withImplements> HAS_IMPLEMENTS = _withImplements.class;

        Class<_withFinal> HAS_FINAL = _withFinal.class;
        Class<_withAbstract> HAS_ABSTRACT = _withAbstract.class;
        Class<_withNative> HAS_NATIVE = _withNative.class;
        Class<_withStatic> HAS_STATIC = _withStatic.class;
        Class<_withStrictFp> HAS_STRICTFP = _withStrictFp.class;
        Class<_withSynchronized> HAS_SYNCHRONIZED = _withSynchronized.class;
        Class<_withTransient> HAS_TRANSIENT = _withTransient.class;
        Class<_withVolatile> HAS_VOLATILE = _withVolatile.class;

        /**
         * Map from the _java classes to the Ast Node equivalent
         */
        public static final Map<Class<? extends _multiPart>, Class<? extends Node>> _JAVA_TO_AST_NODE_CLASSES = new HashMap<>();

        static {
            _JAVA_TO_AST_NODE_CLASSES.put(_import.class, ImportDeclaration.class);
            
            _JAVA_TO_AST_NODE_CLASSES.put(_anno.class, AnnotationExpr.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_entry.class, AnnotationMemberDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_constant.class, EnumConstantDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_constructor.class, ConstructorDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_field.class, VariableDeclarator.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_method.class, MethodDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_parameter.class, Parameter.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_receiverParameter.class, ReceiverParameter.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_initBlock.class, InitializerDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_typeParameter.class, TypeParameter.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_typeRef.class, Type.class);

            _JAVA_TO_AST_NODE_CLASSES.put(_type.class, TypeDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_annotation.class, AnnotationDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_class.class, ClassOrInterfaceDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_interface.class, ClassOrInterfaceDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_enum.class, EnumDeclaration.class);
        }

        /**
         * Map from the {@link _multiPart} classes to the Ast
         * {@link com.github.javaparser.ast.Node} equivalent
         */
        public static final Map<Class<? extends Node>, Class<? extends _multiPart>> AST_NODE_TO_JAVA_CLASSES = new HashMap<>();

        static {
            AST_NODE_TO_JAVA_CLASSES.put(ImportDeclaration.class, _import.class); //base
            
            AST_NODE_TO_JAVA_CLASSES.put(AnnotationExpr.class, _anno.class); //base
            AST_NODE_TO_JAVA_CLASSES.put(NormalAnnotationExpr.class, _anno.class); //impl
            AST_NODE_TO_JAVA_CLASSES.put(MarkerAnnotationExpr.class, _anno.class);
            AST_NODE_TO_JAVA_CLASSES.put(SingleMemberAnnotationExpr.class, _anno.class);

            AST_NODE_TO_JAVA_CLASSES.put(AnnotationMemberDeclaration.class, _entry.class);
            AST_NODE_TO_JAVA_CLASSES.put(EnumConstantDeclaration.class, _constant.class);
            AST_NODE_TO_JAVA_CLASSES.put(ConstructorDeclaration.class, _constructor.class);

            AST_NODE_TO_JAVA_CLASSES.put(VariableDeclarator.class, _field.class);
            AST_NODE_TO_JAVA_CLASSES.put(FieldDeclaration.class, _field.class);

            AST_NODE_TO_JAVA_CLASSES.put(MethodDeclaration.class, _method.class);
            AST_NODE_TO_JAVA_CLASSES.put(Parameter.class, _parameter.class);
            AST_NODE_TO_JAVA_CLASSES.put(ReceiverParameter.class, _receiverParameter.class);
            AST_NODE_TO_JAVA_CLASSES.put(InitializerDeclaration.class, _initBlock.class);
            AST_NODE_TO_JAVA_CLASSES.put(TypeParameter.class, _typeParameter.class);

            AST_NODE_TO_JAVA_CLASSES.put(Type.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(ArrayType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(ClassOrInterfaceType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(IntersectionType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(PrimitiveType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(ReferenceType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(UnionType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(VarType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(VoidType.class, _typeRef.class);
            AST_NODE_TO_JAVA_CLASSES.put(WildcardType.class, _typeRef.class);

            AST_NODE_TO_JAVA_CLASSES.put(TypeDeclaration.class, _type.class);
            AST_NODE_TO_JAVA_CLASSES.put(ClassOrInterfaceDeclaration.class, _type.class);
            AST_NODE_TO_JAVA_CLASSES.put(EnumDeclaration.class, _enum.class);
            AST_NODE_TO_JAVA_CLASSES.put(AnnotationDeclaration.class, _annotation.class);
        }        
    }

    /**
     * A {@link _member} defined within a {@link _type} (that is callable/referenceable/reachable) from the outside
     * it can be associated with a larger entity or context)
     * NOTE: each {@link _declared} maps directly to:
     * <UL>
     *     <LI>an AST representation {@link Node}
     *     <LI></LI>a meta-representation {@link _multiPart}
     * </UL>
     * <UL>
     * <LI>{@link _field} {@link FieldDeclaration}
     * <LI>{@link _constructor} {@link ConstructorDeclaration}
     * <LI>{@link _method} {@link MethodDeclaration}
     * <LI>{@link _constant} {@link EnumConstantDeclaration}
     * <LI>{@link _entry} {@link AnnotationMemberDeclaration}
     * <LI>{@link _type} {@link TypeDeclaration}
     * <LI>{@link _class} {@link ClassOrInterfaceDeclaration}
     * <LI>{@link _enum} {@link EnumDeclaration}
     * <LI>{@link _interface} {@link ClassOrInterfaceDeclaration}
     * <LI>{@link _annotation} {@link AnnotationDeclaration}
     * </UL>
     *
     * NOTE:
     * <LI>{@link _initBlock} {@link InitializerDeclaration}
     * is a {@link _member} but is NOT {@link _declared} (primarily because it is not
     * callable/referenceable/accessible outside of the Class where it is defined and does
     * not satisfy the {@link _withName} {@link _withAnnos} or {@link _withJavadoc} interfaces
     * (Not available via reflection at runtime)
     *
     * @param <N> the AST node type (i.e. {@link MethodDeclaration})
     * @param <_D> the meta-representation declaration type (i.e. {@link _method})
     */
    interface _declared<N extends Node, _D extends _multiPart & _withName & _withAnnos & _withJavadoc>
            extends _member<N, _D>, _withName<_D>, _withAnnos<_D>, _withJavadoc<_D>, _withComments<N, _D>  {

        @Override
        default _javadoc getJavadoc() {
            return _javadoc.of((NodeWithJavadoc) this.ast());
        }

        default _D removeJavadoc() {
            ((NodeWithJavadoc) this.ast()).removeJavaDocComment();
            return (_D) this;
        }

        @Override
        default boolean hasJavadoc() {
            return ((NodeWithJavadoc) this.ast()).getJavadoc().isPresent();
        }
    }

    /**
     * A member within the body of a Class (something defined in the  { }) including {@link _initBlock}s.
     * All _{@link _member}s are {@link _multiPart}s (they are represented by BOTH a meta-representation i.e. {@link _method},
     * and an AST representation {@link MethodDeclaration}.
     *
     * {@link _initBlock} IS a {@link _member}, BUT IS NOT a {@link _declared}, because even though
     * {@link _initBlock} is defined within the context of a Class, it is not named/reachable/callable or "declared"
     * and referenced outside of the class where it is defined.
     * <UL>
     * <LI>{@link _initBlock} {@link InitializerDeclaration}
     * <LI>{@link _field} {@link FieldDeclaration}
     * <LI>{@link _constructor} {@link ConstructorDeclaration}
     * <LI>{@link _method} {@link MethodDeclaration}
     * <LI>{@link _constant} {@link EnumConstantDeclaration}
     * <LI>{@link _entry} {@link AnnotationMemberDeclaration}
     * <LI>{@link _type} {@link TypeDeclaration}
     * <LI>{@link _class} {@link ClassOrInterfaceDeclaration}
     * <LI>{@link _enum} {@link EnumDeclaration}
     * <LI>{@link _interface} {@link ClassOrInterfaceDeclaration}
     * <LI>{@link _annotation} {@link AnnotationDeclaration}
     * </UL>
     *
     * @param <N> the Ast Node instance type
     * @param <_N> the _draft instance type
     * @see _declared (an EXTENSION of {@link _member}s that are also {@link _withName}...(all {@link _member}s are
     * {@link _declared}s, ACCEPT {@link _initBlock} which is ONLY a {@link _member}
     */
    interface _member<N extends Node, _N extends _multiPart>
            extends _multiPart<N, _N>, _withComments<N, _N> {

        /**
         * Returns the parent _member for this _member (if it exists)
         * (traverses up through the parents to the
         *
         * for example:
         * _class _c = _class.of("C", new Object(){
         *     int x,y;
         * });
         *
         * assertEquals(_c, _c.getField(i).getParentMember());
         *
         * @param <_M>
         * @return
         */
        default <_M extends _member> _M getParentMember(){
            if(this instanceof _field){
                _field _f = (_field)this;
                FieldDeclaration fd = _f.getFieldDeclaration();
                if( fd == null ){
                    return null;
                }
                BodyDeclaration bd = Tree.first(Tree.PARENTS, fd, BodyDeclaration.class);
                if( bd != null ) {
                    return (_M) of(bd);
                }
                return null; //we didnt find a parent that was a BodyDeclaration
            } else{
                BodyDeclaration bd = Tree.first(Tree.PARENTS, ast(), BodyDeclaration.class);
                if( bd != null ) {
                    return (_M) of(bd);
                }
                return null; //we didnt find a parent that was a BodyDeclaration
            }
        }
    }

    /**
     * Node entities that may have comments attributed to them and contain nodes that may have comments
     * @param <N> the Ast node type
     * @param <_N> the _node type
     */
    interface _withComments <N extends Node, _N extends _node> extends _node<N, _N> {

        /**
         * Gets the "attributed" comment on the node (or null if there is no comment on this node)
         * @param <_C> the comment type
         * @return the attributed comment or null if there is no comment attributed to this node
         */
        default <_C extends _comment> _C getComment(){
            N n = ast();
            if( n.getComment().isPresent() ){
                return _comment.of( n.getComment().get() );
            }
            return null;
        }

        /**
         * Lists all comments Attributed to OR contained within the positional range (either attributed or orphaned)
         * of this node, i.e. the line comment AND block comment below:
         * <CODE><PRE>
         *  //line comment
         *  void m(){
         *      /* block comment * /
         *      int i = 0;
         *  }
         * </PRE></CODE>
         * @return a list of all comments attributed or contained within the node
         */
        default List<_comment> listAllComments(){
            return listAllComments(t->true);
        }

        /**
         * Lists the comments that abide by the matchFn predicate
         * @param matchFn match function to select nodes in the list
         * @return a list of _comment
         */
        default List<_comment> listAllComments(Predicate<_comment> matchFn){
            Node basedNode = null;
            if( this instanceof _codeUnit && ((_codeUnit)this).isTopLevel() ){
                basedNode = ((_codeUnit)this).astCompilationUnit();
            }
            List<_comment> _cs = new ArrayList<>();
            basedNode.getAllContainedComments().stream()
                    .map( c-> _comment.of(c))
                    .filter( c-> matchFn.test( (_comment)c))
                    .forEach( c -> _cs.add((_comment)c));
            return _cs;
        }

        /**
         * Apply a function to all comments (any comment attributed to this node, and within the token range of the node)
         * @param actionFn function to apply to the _comment
         * @return the _N node
         */
        default _N forAllComments(Consumer<_comment> actionFn){
            return forAllComments(t-> true, actionFn);
        }

        default _N forAllComments(Predicate<_comment> matchFn, Consumer<_comment> actionFn){
            Node basedNode = null;
            if( this instanceof _codeUnit && ((_codeUnit)this).isTopLevel() ){
                basedNode = ((_codeUnit)this).astCompilationUnit();
            }
            basedNode.getAllContainedComments().stream()
                    .map( c-> _comment.of(c))
                    .filter( c-> matchFn.test( (_comment)c) )
                    .forEach( c-> actionFn.accept( (_comment)c) );
            return (_N)this;
        }

        /**
         * lists all block comments that may be attributed to the node itself
         * @return
         */
        default List<_blockComment> listAllBlockComments(){
            return listAllComments(c-> c instanceof _blockComment).stream().map(c -> (_blockComment)c).collect(Collectors.toList());
        }

        default List<_lineComment> listAllLineComments(){
            return listAllComments(c-> c instanceof _lineComment).stream().map(c -> (_lineComment)c).collect(Collectors.toList());
        }

        default List<_javadocComment> listAllJavadocComments(){
            return listAllComments(c-> c instanceof _javadocComment).stream().map(c -> (_javadocComment)c).collect(Collectors.toList());
        }
    }

    /**
     * A _domain entity that maps 1-to-1 to an Ast (Syntax) entity in JavaParser
     *
     * @param <N> the Ast type
     * @param <_N> the _domain type
     */
    interface _node<N extends Node, _N extends _node> extends _domain {

        /**
         * Build and return an (independent) copy of this _node entity
         * @return an independent mutable copy
         */
        _N copy();

        /**
         * @return the underlying AST Node instance being manipulated
         * NOTE: the AST node contains physical information (i.e. location in
         * the file (line numbers) and syntax related parent/child relationships
         */
        N ast();

        /**
         * Replace this ast node (wherever it resides in the Ast TREE) with the ASt node
         * and return the replacement Ast node instance
         * @param n the ast nod to replace this node in the tree
         * @param <N> the type of Ast node to replace with
         * @return the replacement node
         */
        default <N extends Node> N replace(N n){
            boolean rep = this.ast().replace(n);
            if( rep ) {
                return n;
            }
            throw new _jdraftException("unable to replace "+ this+" with "+ n);
        }

        /**
         * Replace this ast node (wherever it resides in the Ast TREE) with the ASt node
         * and return the replacement Ast node instance
         * @param _n the astNode to replace this node in the tree
         * @param <_N> the type of _astNode node to replace with
         * @return the replacement _astNode implementation
         */
        default <_N extends _node> _N replace(_N _n){
            replace(_n.ast());
            return _n;
        }

        /**
         * Pass in the AST Pretty Printer configuration which will determine how the code
         * is formatted and return the formatted String representing the code.
         *
         * @see com.github.javaparser.printer.PrettyPrintVisitor the original visitor for walking and printing nodes in AST
         * @see Print.PrintNoAnnotations a configurable subclass of PrettyPrintVisitor that will not print ANNOTATIONS
         * @see PrettyPrinterConfiguration the configurations for spaces, Tabs, for printing
         * @see Print#PRINT_NO_ANNOTATIONS_OR_COMMENTS a premade implementation for
         * @see Print#PRINT_NO_COMMENTS
         *
         * @param codeFormat the details on how the code will be formatted (for this element and all sub ELEMENTS)
         * @return A String representing the .java code
         */
        default String toString(PrettyPrinterConfiguration codeFormat) {
            return ast().toString(codeFormat);
        }

        /**
         * Pass a match function to verify based on a lambda predicate
         * @param matchFn a function to match against the entity
         * @return true if the match function is verified, false otherwise
         */
        default boolean is(Predicate<_N> matchFn){
            return matchFn.test((_N)this);
        }

        /**
         * is the String representation equal to the _node entity
         * (i.e. if we parse the string, does it create an AST entity that
         * is equal to the node?)
         *
         * @param stringRep the string representation of the node
         * (parsed as an AST and compared to this entity to see if equal)
         * @return true if the Parsed String represents the entity
         */
        boolean is(String... stringRep);

        /**
         * Is the AST node representation equal to the underlying entity
         * @param astNode the astNode to compare against
         * @return true if they represent the same _node, false otherwise
         */
        default boolean is(N astNode){
            return Objects.equals(ast(), astNode);
        }
    }

    /**
     * Simple Atomic node of code that cannot be "broken down" into smaller components
     * (Types that are ALWAYS Leaf nodes on the AST)
     *
     * In general it is some Ast component that has 0 or 1 parts associated with it
     *
     * @see _nameExpression
     * @see _typeExpression
     * @see _arrayDimension
     * @see _modifier
     * @see _super
     * @see _this
     * @see _expression._literal
     *     @see _null
     *     @see _int
     *     @see _double
     *     @see _char
     *     @see _boolean
     *     @see _long
     *     @see _string
     *     @see _textBlock
     * @see _emptyStmt
     * @see _continueStmt
     * @see _breakStmt
     * @see _classExpression
     *
     * @param <N> the AST node type
     * @param <_UP> the _domain type
     * @see _multiPart for an ast node type that contains multiple walkable child entities
     */
    interface _uniPart<N extends Node, _UP extends _uniPart> extends _node<N, _UP> { }

    /**
     * {@link _multiPart} entity (having more than one possible child) that maps directly to an AST {@link Node}
     * for example:
     * <UL>
     * <LI>{@link _declared}s</LI>
     * <UL>
     *     <LI>{@link _type} {@link TypeDeclaration}</LI>
     *     <LI>{@link _annotation} {@link AnnotationDeclaration}
     *     <LI>{@link _interface} {@link ClassOrInterfaceDeclaration}</LI>
     *     <LI>{@link _class} {@link ClassOrInterfaceDeclaration}</LI>
     *     <LI>{@link _enum} {@link EnumDeclaration}</LI>
     *     <LI>{@link _constant} {@link EnumConstantDeclaration}</LI>
     *     <LI>{@link _entry} {@link AnnotationMemberDeclaration}
     *     <LI>{@link _method} {@link MethodDeclaration}</LI>
     *     <LI>{@link _constructor} {@link ConstructorDeclaration}</LI>
     *     <LI>{@link _field} {@link FieldDeclaration}</LI>
     * </UL>
     * <LI>{@link _member}s</LI>
     * <UL>
     *         <LI>{@link _initBlock} {@link InitializerDeclaration}</LI>
     * </UL>
     * //adornment, property
     * <LI>{@link _anno} {@link AnnotationExpr}
     * <LI>{@link _parameter} {@link Parameter}</LI>
     * <LI>{@link _receiverParameter} {@link ReceiverParameter}</LI>
     * <LI>{@link _typeParameter} {@link TypeParameter}</LI>
     * <LI>{@link _typeRef} {@link Type}</LI>
     * </UL>
     * @see _java for mappings
     * @param <_MP> the jdraft _node type {@link _method}, {@link _field}
     * @param <N> ast node {@link MethodDeclaration}, {@link FieldDeclaration}
     */
    interface _multiPart<N extends Node, _MP extends _multiPart> extends _node<N, _MP> {

        /**
         * Decompose the entity into key-VALUE pairs where the key is the Component
         * @return a map of key values
         */
        Map<Component, Object> components();

        /**
         * Decompose the entity into smaller named tokens
         * returning a mapping between the name and the constituent part
         * @return a Map with the names mapped to the corresponding components
         */
        default Map<String, Object> tokenize() {
            Map<Component, Object> parts = components();
            Map<String, Object> mdd = new HashMap<>();
            parts.forEach((p, o) -> {
                mdd.put(p.name, o);
            });
            return mdd;
        }
    }

    /**
     * A grouping of monotonic (same type) entities where the order doesnt matter
     * to the semantics of the set.  While they may may be stored in NodeList<N>
     * this represents the syntax, however in the semantic side, this:
     * <CODE>
     * void m() throws A, B
     * </CODE>
     *
     * ...is semantically equivalent to this:
     * <CODE>
     *  void m() throws B, A
     * </CODE>
     *
     * @see _annos< AnnotationExpr,_anno>
     * @see _imports< ImportDeclaration,_import>
     * @see _modifiers <com.github.javaparser.ast.Modifier,_modifier>
     * @see _typeParameters< TypeParameter,_typeParameter>
     * @see _throws< ReferenceType,_typeRef>
     *
     * @param <EL>
     * @param <_EL>
     * @param <_S>
     */
    interface _set<EL extends Node, _EL extends _node, _S extends _set> extends _domain{

        _S copy();

        default boolean isEmpty(){
            return size() == 0;
        }

        default int size(){
            return listAstElements().size();
        }

        List<_EL> list();

        /**
         * NOTE this returns a Mutable reference to the Ast elements
         *
         * meaning:
         * IF YOU ADD ELEMENTS TO THE LIST THEY ARE DIRECTLY ADDED TO THE AST
         * (this is NOT true if you add elements to the list returned in list())
         * @return a MUTABLE reference to the Asts List & elements
         */
        NodeList<EL> listAstElements();

        default List<_EL> list(Predicate<_EL> matchFn){
            return list().stream().filter(matchFn).collect(Collectors.toList());
        }

        default List<EL> listAstElements(Predicate<EL> matchFn){
            return listAstElements().stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean anyMatch(Predicate<_EL> _matchFn){
            return list().stream().anyMatch(_matchFn);
        }

        default boolean allMatch(Predicate<_EL> _matchFn){
            return list().stream().allMatch(_matchFn);
        }

        default boolean is(_EL... _els){
            Set<_EL> _arr = new HashSet<>();
            Arrays.stream(_els).forEach(n -> _arr.add(n));
            return is(_arr);
        }

        default boolean is(List<_EL> _els){
            Set<_EL> hs = new HashSet<>();
            hs.addAll(_els);
            return is(hs);
        }

        default boolean is(Set<_EL> _els){
            if( this.size() != _els.size() ){
                return false;
            }
            Set<_EL> _tels = new HashSet<>();
            _tels.addAll( list() );

            return Objects.equals( _tels, _els);
            /*
            for(int i=0;i<_els.size(); i++){
                if( !Objects.equals(_els.get(i), _tels.get(i))){
                    return false;
                }
            }
            return true;
             */
        }


        default _EL get(Predicate<_EL> matchFn){
            List<_EL> _els = this.list(matchFn);
            if( _els.isEmpty() ){
                return null;
            }
            return _els.get(0);
        }

        default _EL getAt(int index){
            return this.list().get(index);
        }

        //default EL getAst(int index){
        //   return this.listAstElements().get(index);
        //}

        default int indexOf(_EL target){
            return list().indexOf(target);
        }

        default int indexOf(EL target){
            return listAstElements().indexOf(target);
        }

        default boolean has(_EL target){
            return !list( el-> el.equals(target)).isEmpty();
        }

        default boolean has(Predicate<_EL> matchFn){
            return !list( matchFn).isEmpty();
        }

        default boolean has(EL target){
            return !listAstElements( el-> el.equals(target)).isEmpty();
        }

        default _S add(EL... astElements) {
            for( EL el : astElements ) {
                this.listAstElements().add(el);
            }
            return (_S)this;
        }

        default _S add(_EL... elements) {
            for( _EL el : elements ) {
                this.listAstElements().add( (EL)el.ast());
            }
            return (_S)this;
        }

        default _S remove(_EL... _els) {
            Arrays.stream( _els ).forEach(e -> this.listAstElements().remove( e.ast() ) );
            return (_S)this;
        }

        default _S remove(EL... els) {
            Arrays.stream(els ).forEach( e -> this.listAstElements().remove( e ) );
            return (_S)this;
        }

        default _S remove(Predicate<_EL> _matchFn) {
            this.list(_matchFn).stream().forEach( e-> remove(e) );
            return (_S)this;
        }

        default _S forEach(Predicate<_EL> matchFn, Consumer<_EL> consumer){
            list(matchFn).forEach(consumer);
            return (_S)this;
        }

        default _S forEach(Consumer<_EL> consumer){
            list().forEach(consumer);
            return (_S)this;
        }
    }

    /**
     * ORDERED/ ORDER MATTERS TO THE SEMANTICS GROUP
     * Sometimes we have groupings of entities that do not
     * map to a specific Ast entity but a grouping of AST entities
     *
     * @see _parameters (parameters are ordered)
     * @see _arrayCreate (the dimensions of the array are in an ordered list)
     * @see _arrayInitialize (the elements located in the array are ordered)
     */
    interface _list<EL extends Node, _EL extends _node, _L extends _list> extends _set<EL, _EL, _L>, _domain {

        default boolean is(_EL... _els){
            List<_EL> _arr = new ArrayList<>();
            Arrays.stream(_els).forEach(n -> _arr.add(n));
            return is(_arr);
        }

        default boolean is(List<_EL> _els){
            if( this.size() != _els.size() ){
                return false;
            }
            List<_EL> _tels = list();
            for(int i=0;i<_els.size(); i++){
                if( !Objects.equals(_els.get(i), _tels.get(i))){
                    return false;
                }
            }
            return true;
        }

        /** remove the argument at this index */
        default _L removeAt(int index){
            this.listAstElements().remove(index);
            return (_L)this;
        }

        default _L setAt( int index, EL element){
            this.listAstElements().set(index, element);
            return (_L)this;
        }

        default _L setAt( int index, _EL _element){
            this.listAstElements().set(index, (EL)_element.ast());
            return (_L)this;
        }

        default boolean isAt( int index, _EL _element){
            if( index >= this.size()){
                return false;
            }
            return getAt(index).equals(_element);
        }

        /**
         * does the argument at this index match any of the classes provided?
         * @param index the argument index
         * @param classes the _expression classes for matching the argument
         * @return true if the argument matches any of these classes, false otherwise
         */
        default boolean isAt( int index, Class<? extends _expression>...classes) {
            if( index >= this.size()){
                return false;
            }
            return Arrays.stream(classes).anyMatch( c-> c.isAssignableFrom( getAt(index).getClass() ));
        }

        default boolean isAt( int index, Predicate<_EL> matchFn) {
            if( index >= this.size()){
                return false;
            }
            return matchFn.test( getAt(index));
        }
    }

    /**
     * Named java entities
     * {@link _type} {@link _class} {@link _enum} {@link _interface} {@link _annotation}
     * {@link _method}
     * {@link _field}
     * {@link _parameter}
     * {@link _anno}
     * {@link _constant}
     * {@link _entry}
     * {@link _typeRef}
     * {@link _typeParameter}
     *
     * {@link _methodCall}
     *
     * @author Eric
     * @param <_WN>
     */
    interface _withName<_WN extends _withName> extends _domain {

        /**
         * @param name set the name on the entity and return the modified entity
         * @return the modified entity
         */
        _WN setName(String name);

        /**
         * gets the name of the entity
         * @return the name of the entity
         */
        String getName();

        /**
         * returns the name node (a {@link SimpleName} or a {@link Name})
         * @return
         */
        Node getNameNode();

        /**
         *
         * @param name
         * @return
         */
        default boolean isNamed(String name) {
            return Objects.equals(getName(), name);
        }

        /**
         * determine if the name matches the Predicate
         * @param matchFn
         * @return
         */
        default  boolean isNamed(Predicate<String> matchFn){
            return matchFn.test(getName());
        }

    }

    /**
     * Entity with TYPE/NAME pair
     * <UL>
     *     <LI>{@link _field}
     *     <LI>{@link _parameter}
     *     <LI>{@link _method}
     *     <LI>{@link _annotation._entry}
     *     <LI>{@link _receiverParameter}
     *     <LI>{@link _variable}
     * </UL>
     * @param <_NT> the specialized entity that is a named TYPE
     */
    interface _withNameTypeRef<N extends Node, _NT extends _withNameTypeRef> extends _withName<_NT>, _typeRef._withTypeRef<N,_NT> {

    }

    interface _withScope<N extends Node, _WS extends _node> extends _node<N, _WS> {

        default boolean hasScope(){
            return ((NodeWithOptionalScope)ast()).getScope().isPresent();
        }

        default boolean isScope(String...expr){
            if( ((NodeWithOptionalScope)ast()).getScope().isPresent()){
                return Ex.equivalent( (Expression)((NodeWithOptionalScope)ast()).getScope().get(), Ex.of(expr));
            }
            return false;
        }

        default boolean isScope(Expression e){
            if( ((NodeWithOptionalScope)ast()).getScope().isPresent()){
                return Ex.equivalent( (Expression) ((NodeWithOptionalScope)ast()).getScope().get(), e);
            }
            return e == null;
        }

        default boolean isScope(_expression _e){
            if( ((NodeWithOptionalScope)ast()).getScope().isPresent()){
                return Ex.equivalent( (Expression) ((NodeWithOptionalScope)ast()).getScope().get(), _e.ast());
            }
            return _e == null;
        }

        default _WS removeScope(){
            ((NodeWithOptionalScope)ast()).removeScope();
            return (_WS)this;
        }

        default _WS setScope(String scope ){
            ((NodeWithOptionalScope)ast()).setScope(Ex.of(scope));
            return (_WS)this;
        }

        default _WS setScope(_expression _e){
            ((NodeWithOptionalScope)ast()).setScope(_e.ast());
            return (_WS)this;
        }

        default _WS setScope(Expression e){
            ((NodeWithOptionalScope)ast()).setScope(e);
            return (_WS)this;
        }

        default _WS setScope(String... scope){
            return setScope( Ex.of(scope));
        }

        default _expression getScope(){
            if( ((NodeWithOptionalScope)ast()).getScope().isPresent()){
                return _expression.of( (Expression)
                        ((NodeWithOptionalScope)ast()).getScope().get());
            }
            return null;
        }
    }


    interface _withCondition <N extends Node, _WC extends _node> extends _node<N, _WC> {

        default boolean isCondition(String...expression){
            try{
                return isCondition(Ex.of(expression));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isCondition(_expression _ex){
            return Ex.equivalent(  this.getCondition().ast(), _ex.ast());
        }

        default boolean isCondition(Expression ex){
            return Ex.equivalent( this.getCondition().ast(), ex);
        }

        default boolean isCondition(Predicate<_expression> matchFn){
            return matchFn.test(getCondition());
        }

        default _WC setCondition(String...expression){
            return setCondition(Ex.of(expression));
        }

        default _WC setCondition(_expression e){
            return setCondition(e.ast());
        }

        default _WC setCondition(Expression e){
            ((NodeWithExpression)ast()).setExpression(e);
            return (_WC)this;
        }

        default _expression getCondition(){
            return _expression.of(((NodeWithExpression)ast()).getExpression());
        }
    }

    interface _withExpression <N extends Node, _WE extends _node> extends _node<N, _WE> {

        default boolean isExpression(String...expression){
            try{
                return isExpression(Ex.of(expression));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isExpression(_expression _ex){
            return Ex.equivalent( this.getExpression().ast(), _ex.ast());
        }

        default boolean isExpression(Expression ex){
            return Ex.equivalent( this.getExpression().ast(), ex);
        }

        default boolean isExpression(Predicate<_expression> matchFn){
            return matchFn.test(getExpression());
        }

        default boolean isExpression( int i){
            return isExpression( Ex.of(i) );
        }

        default boolean isExpression( boolean b){
            return isExpression( Ex.of(b) );
        }

        default boolean isExpression( float f){
            return isExpression( Ex.of(f) );
        }

        default boolean isExpression( long l){
            return isExpression( Ex.of(l) );
        }

        default boolean isExpression( double d){
            return isExpression( Ex.of(d) );
        }

        default boolean isExpression( char c){
            return isExpression( Ex.of(c) );
        }

        default _WE setExpression(String...expression){
            return setExpression(Ex.of(expression));
        }

        default _WE setExpression(_expression e){
            return setExpression(e.ast());
        }

        default _WE setExpression(Expression e){
            ((NodeWithExpression)ast()).setExpression(e);
            return (_WE)this;
        }

        default _expression getExpression(){
            return _expression.of(((NodeWithExpression)ast()).getExpression());
        }
    }
}
