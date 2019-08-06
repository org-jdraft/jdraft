package org.jdraft;

import java.util.*;
import java.io.*;
import java.nio.file.Path;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.*;

import static org.jdraft.Ast.*;
import org.jdraft._anno._annos;
import org.jdraft._anno._hasAnnos;
import org.jdraft._annotation._element;
import org.jdraft._body._hasBody;
import org.jdraft._constructor._hasConstructors;
import org.jdraft._enum._constant;
import org.jdraft._import._imports;
import org.jdraft._javadoc._hasJavadoc;
import org.jdraft._method._hasMethods;
import org.jdraft._modifiers.*;
import org.jdraft._receiverParameter._hasReceiverParameter;
import org.jdraft._staticBlock._hasStaticBlocks;
import org.jdraft._throws._hasThrows;
import org.jdraft._type._hasExtends;
import org.jdraft._type._hasImplements;
import org.jdraft._typeParameter._typeParameters;
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.macro._macro;

/**
 * API adapting the access & manipulation of an interconnected nodes of an AST
 * as an intuitive logical model
 *
 * Logical Facade instances store NO DATA, but references(s) into
 * {@link com.github.javaparser.ast.Node}s of the AST, and
 * provide a simple API to query or manipulate the AST as is it were
 * a simple Dto or VALUE object.
 *
 * The purpose of these nested interfaces is to define the relationship between
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
    public static final Class<_code> CODE = _code.class;

    public static final Class<_packageInfo> PACKAGE_INFO = _packageInfo.class;
    public static final Class<_moduleInfo> MODULE_INFO = _moduleInfo.class;

    public static final Class<_type> TYPE = _type.class;

    public static final Class<_class> CLASS = _class.class;
    public static final Class<_enum> ENUM = _enum.class;
    public static final Class<_interface> INTERFACE = _interface.class;
    public static final Class<_annotation> ANNOTATION = _annotation.class;

    public static final Class<_method> METHOD = _method.class;
    public static final Class<_field> FIELD = _field.class;
    public static final Class<_constructor> CONSTRUCTOR = _constructor.class;

    /** ENUM constant i.e. enum E { CONSTANT; }*/
    public static final Class<_constant> CONSTANT = _constant.class;

    /** Annotation Element i.e. @interface A{ int element(); }*/
    public static final Class<_element> ELEMENT = _element.class;

    public static final Class<_body> BODY = _body.class;
    public static final Class<_anno> ANNO = _anno.class;
    public static final Class<_annos> ANNOS = _annos.class;
    public static final Class<_import> IMPORT = _import.class;
    public static final Class<_imports> IMPORTS = _imports.class;
    public static final Class<_javadoc> JAVADOC = _javadoc.class;
    public static final Class<_modifiers> MODIFIERS = _modifiers.class;
    public static final Class<_parameter> PARAMETER = _parameter.class;
    public static final Class<_parameter._parameters> PARAMETERS = _parameter._parameters.class;
    public static final Class<_typeParameter> TYPE_PARAMETER = _typeParameter.class;
    public static final Class<_typeParameters> TYPE_PARAMETERS = _typeParameters.class;
    public static final Class<_receiverParameter> RECEIVER_PARAMETER = _receiverParameter.class;
    public static final Class<_staticBlock> STATIC_BLOCK = _staticBlock.class;
    public static final Class<_throws> THROWS = _throws.class;
    public static final Class<_typeRef> TYPEREF = _typeRef.class;

    /**
     * The classes below are categorical interfaces that are applied to classes
     */
    public static final Class<_node> NODE = _node.class;
    public static final Class<_member> MEMBER = _member.class;
    public static final Class<_named> NAMED = _named.class;
    public static final Class<_namedType> NAMED_TYPE = _namedType.class;

    public static final Class<_hasThrows> HAS_THROWS = _hasThrows.class;
    public static final Class<_hasBody> HAS_BODY = _hasBody.class;
    public static final Class<_hasAnnos> HAS_ANNOS = _hasAnnos.class;
    public static final Class<_hasConstructors> HAS_CONSTRUCTORS = _hasConstructors.class;
    public static final Class<_hasJavadoc> HAS_JAVADOC = _hasJavadoc.class;
    public static final Class<_hasMethods> HAS_METHODS = _hasMethods.class;
    public static final Class<_hasModifiers> HAS_MODIFIERS = _hasModifiers.class;
    public static final Class<_parameter._hasParameters> HAS_PARAMETERS = _parameter._hasParameters.class;
    public static final Class<_hasReceiverParameter> HAS_RECEIVER_PARAMETER = _hasReceiverParameter.class;
    public static final Class<_hasStaticBlocks> HAS_STATIC_BLOCKS = _hasStaticBlocks.class;
    public static final Class<_hasExtends> HAS_EXTENDS = _hasExtends.class;
    public static final Class<_hasImplements> HAS_IMPLEMENTS = _hasImplements.class;

    public static final Class<_hasFinal> HAS_FINAL = _hasFinal.class;
    public static final Class<_hasAbstract> HAS_ABSTRACT = _hasAbstract.class;
    public static final Class<_hasNative> HAS_NATIVE = _hasNative.class;
    public static final Class<_hasStatic> HAS_STATIC = _hasStatic.class;
    public static final Class<_hasStrictFp> HAS_STRICTFP = _hasStrictFp.class;
    public static final Class<_hasSynchronized> HAS_SYNCHRONIZED = _hasSynchronized.class;
    public static final Class<_hasTransient> HAS_TRANSIENT = _hasTransient.class;
    public static final Class<_hasVolatile> HAS_VOLATILE = _hasVolatile.class;

    /**
     *
     * @param is
     * @return
     */
    public static _type type(InputStream is) {
        _code _c = _java.of(is);
        if (_c instanceof _type) {
            return (_type) _c;
        }
        throw new _draftException("_code at " + is + " does not represent a _type");
    }

    public static _type type(Path path) {
        _code _c = _java.of(path);
        if (_c instanceof _type) {
            return (_type) _c;
        }
        throw new _draftException("_code at " + path + " does not represent a _type");
    }

    /**
     * build and return a new _type based on the code provided
     *
     * @param code the code for the _type
     * @return the _type
     * {@link _class} {@link _enum} {@link _interface}, {@link _annotation}
     */
    public static _type type(String... code) {
        return type(Ast.type(code));
    }

    /**
     * Build and return the appropriate _type based on the CompilationUnit
     * (whichever the primary TYPE is)
     *
     * @param astRoot the root AST node containing the top level TYPE
     * @return the _model _type
     */
    public static _type type(CompilationUnit astRoot) {
        //if only 1 type, it's the top type
        if (astRoot.getTypes().size() == 1) {
            return type(astRoot, astRoot.getType(0));
        }
        //if multiple types find the first public type
        Optional<TypeDeclaration<?>> otd
                = astRoot.getTypes().stream().filter(t -> t.isPublic()).findFirst();
        if (otd.isPresent()) {
            return type(astRoot, otd.get());
        }
        //if there is marked a primary type (via storage) then it's that
        if (astRoot.getPrimaryType().isPresent()) {
            return type(astRoot, astRoot.getPrimaryType().get());
        }
        if (astRoot.getTypes().isEmpty()) {
            throw new _draftException("cannot create _type from CompilationUnit with no TypeDeclaration");
        }
        //if we have the storage (and potentially multiple package private types)
        //check the storage to determine if one of them is the right one
        if (astRoot.getStorage().isPresent()) {
            CompilationUnit.Storage st = astRoot.getStorage().get();
            Path p = st.getPath();

            //the storage says it was saved before
            Optional<TypeDeclaration<?>> ott
                    = astRoot.getTypes().stream().filter(t -> p.endsWith(t.getNameAsString() + ".java")).findFirst();
            if (ott.isPresent()) {
                return type(astRoot, ott.get());
            }

            if (p.endsWith("package-info.java")) {
                throw new _draftException("cannot create a _type out of a package-info.java");
            }
            if (p.endsWith("module-info.java")) {
                throw new _draftException("cannot create a _type out of a module-info.java");
            }

            //ok, well, this is dangerous, but shouldnt be a common occurrence
            // basically we have a compilationUnit with > 1 TypeDeclaration, but
            // none of the TypeDeclarations are public, and a PrimaryType is not 
            //defined in the storage, so we just choose the first typeDeclaration
            return type(astRoot, astRoot.getType(0));
        }
        return type(astRoot, astRoot.getType(0));
    }

    /**
     * Return the appropriate _type given the AST TypeDeclaration (also, insure
     * that if it is a Top Level _type,
     *
     * @param td
     * @return
     */
    public static _type type(TypeDeclaration td) {

        /*** MED
        if (td.isTopLevelType()) {
            return type(td.findCompilationUnit().get(), td);
        }
        */
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if (coid.isInterface()) {
                return _interface.of(coid);
            }
            return _class.of(coid);
        }
        if (td instanceof EnumDeclaration) {
            return _enum.of((EnumDeclaration) td);
        }
        return _annotation.of((AnnotationDeclaration) td);
    }

    /**
     * Builds the appropriate _model _type ({@link _class}, {@link _enum},
     * {@link _interface}, {@link _annotation})
     *
     * @param astRoot the compilationUnit
     * @param td the primary TYPE declaration within the CompilationUnit
     * @return the appropriate _model _type (_class, _enum, _interface,
     * _annotation)
     */
    public static _type type(CompilationUnit astRoot, TypeDeclaration td) {
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if (coid.isInterface()) {
                return _interface.of(coid);
            }
            return _class.of(coid);
        }
        if (td instanceof EnumDeclaration) {
            return _enum.of((EnumDeclaration) td);
        }
        return _annotation.of((AnnotationDeclaration) td);
    }

    /**
     * given a Class, return the draft model of the source
     * @param clazz
     * @param resolver
     * @return
     */
    public static <T extends _type> T type( Class clazz, _in._resolver resolver ){
        Node n = Ast.type( clazz, resolver );
        TypeDeclaration td = null;
        if( n instanceof CompilationUnit) { //top level TYPE
            CompilationUnit cu = (CompilationUnit) n;
            if (cu.getTypes().size() == 1) {
                td = cu.getType(0);
            } else {                
                td = cu.getPrimaryType().get();
            }            
        }else {
            td = (TypeDeclaration) n;
        }
        if( td instanceof ClassOrInterfaceDeclaration ){
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)td;
            if( coid.isInterface() ){
                return (T)_macro.to(clazz, _interface.of(coid));
            }
            return (T)_macro.to(clazz,  _class.of(coid) );
        }else if( td instanceof EnumDeclaration){
            return (T)_macro.to(clazz, _enum.of( (EnumDeclaration)td));
        }
        return (T)_macro.to(clazz, _annotation.of( (AnnotationDeclaration)td));
    }
    
    /**
     *
     * @param clazz
     * @return
     */
    public static _type  type(Class clazz) {
        return type(clazz, _io.IN_DEFAULT);
    }

    /**
     * Read and return a _code from the .java source code file at
     * javaSourceFilePath
     *
     * @param javaSourceFilePath the path to the local Java source code
     * @return the _code instance
     */
    public static _code of(Path javaSourceFilePath) throws _draftException {
        return of(Ast.of(javaSourceFilePath));
    }

    /**
     * Read and return the appropriate _code model based on the .java source
     * within the javaSourceInputStream
     *
     * @param javaSourceInputStream
     * @return
     */
    public static _code of(InputStream javaSourceInputStream) throws _draftException {
        return of(Ast.of(javaSourceInputStream));
    }

    /**
     * Read and return the appropriate _code model based on the .java source
     * within the javaSourceFile
     *
     * @param javaSourceFile
     * @return
     * @throws _draftException
     */
    public static _code of(File javaSourceFile) throws _draftException {
        return of(Ast.of(javaSourceFile));
    }

    /**
     * build and return the _code wrapper to encapsulate the AST representation
     * of the .java source code stored in the javaSourceReader
     *
     * @param javaSourceReader reader containing .java source code
     * @return the _code model instance representing the source
     */
    public static _code of(Reader javaSourceReader) throws _draftException {
        return of(Ast.of(javaSourceReader));
    }

    /**
     * build the appropriate draft wrapper object to encapsulate the AST
     * compilationUnit
     *
     * @param astRoot the AST
     * @return a _code wrapper implementation that wraps the AST
     */
    public static _code of(CompilationUnit astRoot) {
        if (astRoot.getModule().isPresent()) {
            return _moduleInfo.of(astRoot);
        }
        if (astRoot.getTypes().isEmpty()) {
            return _packageInfo.of(astRoot);
        }
        if (astRoot.getTypes().size() == 1) { //only one type
            return type(astRoot, astRoot.getTypes().get(0));
        }
        //the first public type
        Optional<TypeDeclaration<?>> otd
                = astRoot.getTypes().stream().filter(t -> t.isPublic()).findFirst();
        if (otd.isPresent()) {
            return type(astRoot, otd.get());
        }
        //the primary type
        if (astRoot.getPrimaryType().isPresent()) {
            return type(astRoot, astRoot.getPrimaryType().get());
        }
        return type(astRoot, astRoot.getType(0));
    }

    /**
     * Parse and return the appropriate node based on the Node class (the Node
     * class can be a _model, or Ast Node class
     *
     * @param nodeClass the class of the node (implementation class)
     * must extend {@link _java} or {@link Node}
     * @param code the java source code representation
     * @return the node implementation of the code
     */
    public static Node of(Class nodeClass, String... code) {
        
        if (!_java.class.isAssignableFrom(nodeClass)) {
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
            return ctor(code);
        }
        if (_typeRef.class == nodeClass) {
            return typeRef(Text.combine(code).trim());
        }
        if (_staticBlock.class == nodeClass) {
            return staticBlock(code);
        }
        if (_type.class.isAssignableFrom(nodeClass)) {
            return Ast.type(code);
        }
        if (_parameter.class == nodeClass) {
            return parameter(code);
        }
        if (_receiverParameter.class == nodeClass) {
            return receiverParameter(Text.combine(code));
        }
        if (_field.class == nodeClass) {
            return field(code);
        }
        if (_enum._constant.class == nodeClass) {
            return constant(code);
        }
        if (_annotation._element.class == nodeClass) {
            return annotationMember(code);
        }
        if (_method.class == nodeClass) {
            return method(code);
        }
        throw new _draftException("Could not parse Node " + nodeClass);
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
     * {@link _annotation._element}
     * {@link _constructor}
     * {@link _enum._constant}
     * {@link _field}
     * {@link _javadoc}
     * {@link _method}
     * {@link _parameter}
     * {@link _receiverParameter}
     * {@link _staticBlock}
     * {@link _typeParameter}
     * {@link _typeRef}
     * </PRE>
     *
     * @param node the ast node
     * @return the _model entity
     */
    public static _java of(Node node) {
        if (node instanceof ImportDeclaration ){
            return _import.of((ImportDeclaration) node);
        }
        if (node instanceof AnnotationExpr) {
            return _anno.of((AnnotationExpr) node);
        }        
        if (node instanceof AnnotationDeclaration) {
            return _annotation.of((AnnotationDeclaration) node);
        }
        if (node instanceof AnnotationMemberDeclaration) {
            return _annotation._element.of((AnnotationMemberDeclaration) node);
        }
        if (node instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration cois = (ClassOrInterfaceDeclaration) node;
            if (cois.isInterface()) {
                return _interface.of(cois);
            }
            return _class.of(cois);
        }
        if (node instanceof ConstructorDeclaration) {
            return _constructor.of((ConstructorDeclaration) node);
        }
        if (node instanceof EnumDeclaration) {
            return _enum.of((EnumDeclaration) node);
        }
        if (node instanceof EnumConstantDeclaration) {
            return _enum._constant.of((EnumConstantDeclaration) node);
        }
        if (node instanceof LambdaExpr) {
            return _lambda.of((LambdaExpr) node);
        }
        if (node instanceof VariableDeclarator) {
            VariableDeclarator vd = (VariableDeclarator) node;
            return _field.of(vd);
        }
        if (node instanceof FieldDeclaration) {
            FieldDeclaration fd = (FieldDeclaration) node;
            if (fd.getVariables().size() > 1) {
                throw new _draftException(
                        "Ambiguious node for FieldDeclaration " + fd + "pass in VariableDeclarator instead " + fd);
            }
            return _field.of(fd.getVariable(0));
        }
        if (node instanceof BlockStmt) {
            if (node.getParentNode().isPresent()) {
                if (node.getParentNode().get() instanceof NodeWithBlockStmt) {
                    return _body.of((NodeWithBlockStmt) node.getParentNode().get());
                }
                if (node.getParentNode().get() instanceof NodeWithOptionalBlockStmt) {
                    return _body.of((NodeWithOptionalBlockStmt) node.getParentNode().get());
                }
            }
            throw new _draftException("Unable to return draft _java node for BlockStmt without NodeWithBlockStmt parent");
        }
        if (node instanceof JavadocComment) {
            JavadocComment jdc = (JavadocComment) node;
            if (jdc.getParentNode().isPresent()) {
                return _javadoc.of((NodeWithJavadoc) jdc.getParentNode().get());
            }
            throw new _draftException("No Parent provided for JavadocComment");
        }
        if (node instanceof MethodDeclaration) {
            MethodDeclaration md = (MethodDeclaration) node;
            return _method.of(md);
        }
        if (node instanceof Parameter) {
            return _parameter.of((Parameter) node);
        }
        if (node instanceof InitializerDeclaration) {
            InitializerDeclaration id = (InitializerDeclaration) node;
            return _staticBlock.of(id);
        }
        if (node instanceof ReceiverParameter) {
            ReceiverParameter rp = (ReceiverParameter) node;
            return _receiverParameter.of(rp);
        }
        if (node instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) node;
            return _typeParameter.of(tp);
        }
        if (node instanceof Type) {
            return _typeRef.of((Type) node);
        }
        if (node instanceof CompilationUnit) {
            return of((CompilationUnit) node);
        }
        throw new _draftException("Unable to create _java entity from " + node);
    }

    /**
     * A Way to consistently name things when we construct and deconstruct
     * Components of Java programs (external tools for building & matching &
     * diffing can be more valuable having the opportunity to compare like for
     * like (by componentizing things out and comparing or matching on a part by
     * part basis)
     */
    public enum Component {
        MODULE_DECLARATION("moduleDeclaration", ModuleDeclaration.class),
        /** i.e. @Deprecated @NotNull */
        ANNOS("annos", _anno._annos.class),
        /** i.e. @Deprecated */
        ANNO("anno", _anno.class),
        CLASS("class", _class.class),
        ENUM("enum", _enum.class),
        INTERFACE("interface", _interface.class),
        ANNOTATION("annotation", _annotation.class),
        BODY("body", _body.class),
        MODIFIERS("modifiers", List.class, Modifier.class),
        MODIFIER("modifier", Modifier.class),
        HEADER_COMMENT("header", Comment.class),
        JAVADOC("javadoc", _javadoc.class),
        PARAMETERS("parameters", _parameter._parameters.class),
        //parameter
        PARAMETER("parameter", _parameter.class),
        RECEIVER_PARAMETER("receiverParameter", _receiverParameter.class),
        TYPE_PARAMETERS("typeParameters", _typeParameter._typeParameters.class),
        TYPE_PARAMETER("typeParameter", TypeParameter.class), //_typeParameter.class
        THROWS("throws", _throws.class),
        NAME("name", String.class),
        KEY_VALUES("keyValues", List.class, MemberValuePair.class), //anno
        //KeyValue
        KEY_VALUE("keyValue", MemberValuePair.class), //anno

        //VALUE("value", Expression.class), //anno
        PACKAGE_NAME("package", PackageDeclaration.class),
        IMPORTS("imports", List.class, _import.class),
        //IMPORT
        IMPORT("import", ImportDeclaration.class),
        STATIC("static", Boolean.class),
        WILDCARD("wildcard", Boolean.class),
        ELEMENTS("elements", List.class, _annotation._element.class), //_annotation
        //ELEMENT
        ELEMENT("element", _annotation._element.class), //annotation
        FIELDS("fields", List.class, _field.class),
        //FIELD
        FIELD("field", _field.class),
        NESTS("nests", List.class, _type.class),
        NEST("nest", _type.class),

        COMPANION_TYPES( "companionTypes", List.class, _type.class),
        COMPANION_TYPE( "companionType", _type.class),

        TYPE("type", _typeRef.class), //annotation.element
        DEFAULT("default", Expression.class), //_annotation.element

        EXTENDS("extends", List.class, ClassOrInterfaceType.class), //_class, //_interface
        IMPLEMENTS("implements", List.class, ClassOrInterfaceType.class), //_class, _enum
        STATIC_BLOCKS("staticBlocks", List.class, _staticBlock.class), //class, _enum
        STATIC_BLOCK("staticBlocks", _staticBlock.class), //class, _enum
        CONSTRUCTORS("constructors", List.class, _constructor.class), //class, _enum
        //CONSTRUCTOR
        CONSTRUCTOR("constructor", _constructor.class),
        METHODS("methods", List.class, _method.class), //class, _enum, _interface, _enum._constant
        //METHOD
        METHOD("method", _method.class),
        CONSTANTS("constants", List.class, _enum._constant.class),
        //CONSTANT
        CONSTANT("constant", _enum._constant.class), //_enum

        ARGUMENT("argument", Expression.class), //_enum._constant
        ARGUMENTS("arguments", List.class, Expression.class), //_enum._constant

        INIT("init", Expression.class), //field
        FINAL("final", Boolean.class), //_parameter
        VAR_ARG("varArg", Boolean.class), //parameter

        AST_TYPE("astType", Type.class), //typeRef
        ARRAY_LEVEL("arrayLevel", Integer.class), //_typeRef
        ELEMENT_TYPE("elementType", Type.class); //typeRef

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

        public static <N extends _node> Component of(Class<N> nodeClass) {
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
     * ...to draft _java models (i.e. {@link _anno})
     */
    public static class ModelMap {

        /**
         * Map from the _model._node classes to the Ast Node equivalent
         */
        public static final Map<Class<? extends _node>, Class<? extends Node>> _JAVA_TO_AST_NODE_CLASSES = new HashMap<>();

        static {
            _JAVA_TO_AST_NODE_CLASSES.put(_import.class, ImportDeclaration.class);
            
            _JAVA_TO_AST_NODE_CLASSES.put(_anno.class, AnnotationExpr.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_annotation._element.class, AnnotationMemberDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_enum._constant.class, EnumConstantDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_constructor.class, ConstructorDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_field.class, VariableDeclarator.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_method.class, MethodDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_parameter.class, Parameter.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_receiverParameter.class, ReceiverParameter.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_staticBlock.class, InitializerDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_typeParameter.class, TypeParameter.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_typeRef.class, Type.class);

            _JAVA_TO_AST_NODE_CLASSES.put(_type.class, TypeDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_annotation.class, AnnotationDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_class.class, ClassOrInterfaceDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_interface.class, ClassOrInterfaceDeclaration.class);
            _JAVA_TO_AST_NODE_CLASSES.put(_enum.class, EnumDeclaration.class);
        }

        /**
         * Map from the {@link _node} classes to the Ast
         * {@link com.github.javaparser.ast.Node} equivalent
         */
        public static final Map<Class<? extends Node>, Class<? extends _node>> AST_NODE_TO_JAVA_CLASSES = new HashMap<>();

        static {
            AST_NODE_TO_JAVA_CLASSES.put(ImportDeclaration.class, _import.class); //base
            
            AST_NODE_TO_JAVA_CLASSES.put(AnnotationExpr.class, _anno.class); //base
            AST_NODE_TO_JAVA_CLASSES.put(NormalAnnotationExpr.class, _anno.class); //impl
            AST_NODE_TO_JAVA_CLASSES.put(MarkerAnnotationExpr.class, _anno.class);
            AST_NODE_TO_JAVA_CLASSES.put(SingleMemberAnnotationExpr.class, _anno.class);

            AST_NODE_TO_JAVA_CLASSES.put(AnnotationMemberDeclaration.class, _annotation._element.class);
            AST_NODE_TO_JAVA_CLASSES.put(EnumConstantDeclaration.class, _enum._constant.class);
            AST_NODE_TO_JAVA_CLASSES.put(ConstructorDeclaration.class, _constructor.class);

            AST_NODE_TO_JAVA_CLASSES.put(VariableDeclarator.class, _field.class);
            AST_NODE_TO_JAVA_CLASSES.put(FieldDeclaration.class, _field.class);

            AST_NODE_TO_JAVA_CLASSES.put(MethodDeclaration.class, _method.class);
            AST_NODE_TO_JAVA_CLASSES.put(Parameter.class, _parameter.class);
            AST_NODE_TO_JAVA_CLASSES.put(ReceiverParameter.class, _receiverParameter.class);
            AST_NODE_TO_JAVA_CLASSES.put(InitializerDeclaration.class, _staticBlock.class);
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
}
