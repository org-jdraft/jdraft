package org.jdraft;

import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
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

import org.jdraft._anno._withAnnos;
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
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.macro.macro;
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

    /**
     * Marker interface for ALL models and interfaces related to Java Language Constructs
     *
     * @see _multiPart a one-to-one mapping between an AST (Node) and a <CODE>_javaDomain</CODE> ( _method <--> MethodDeclaration )
     * @see _list a one-to 0 or more NodeList instances of AST nodes ( _parameters <--> NodeList<Parameter> )
     */
    interface _domain { }

    /**
     * Shortcut for checking if an ast has a parent of a particular class that complies with a particular Predicate
     * @param _j the _java entity
     * @param parentNodeClass the node class expected of the parent node
     * @param parentMatchFn predicate for matching the parent
     * @param <_J> the expected _java node type
     * @return true if the parent node exists, is of a particular type and complies with the predicate
     */
    static <_J extends _domain> boolean isParent(_domain _j, Class<_J> parentNodeClass, Predicate<_J> parentMatchFn){
        if( _j instanceof _multiPart){
            AtomicBoolean ans = new AtomicBoolean(false);
            Walk.in_java(Node.TreeTraversal.PARENTS, 1, ((_multiPart)_j).ast(), parentNodeClass, parentMatchFn, (t)-> ans.set(true) );
            return ans.get();
        }
        //need to handle _typeParameters, _parameters, _annos
        if( _j instanceof _typeParameters ){
            _typeParameters _tps = (_typeParameters)_j;
            _multiPart _n = (_multiPart)_java.of( (Node)_tps.astHolder());
            return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
        }
        if( _j instanceof _body){
            _body _tps = (_body)_j;
            Object par = _tps.astParentNode();
            if( par != null ){
                _multiPart _n = (_multiPart)_java.of( (Node)par );
                return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
            }
        }
        if( _j instanceof _parameters){
            _parameters _tps = (_parameters)_j;
            _multiPart _n = (_multiPart)_java.of( (Node)_tps.astHolder());
            return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
        }
        return false;
    }

    /**
     * Check to see if this java entity has an Ancestor(parents, grandparents...) that
     * matches the type and matchFn
     * @param _j the _java entity to check
     * @param type the target type
     * @param matchFn matching lambda function
     * @param <T> the match type
     * @return true if
     */
    static <T> boolean hasAncestor(_domain _j, Class<T> type, Predicate<T> matchFn){
        return Walk.first(Node.TreeTraversal.PARENTS, _j, type, matchFn) != null;
    }

    /**
     * Check to see if this java entity has an Descendant(child, grandchild...) that
     * matches the type and matchFn
     *
     * @param _j the _java entity to check
     * @param type the target type
     * @param matchFn matching lambda function
     * @param <T> the match type
     * @return
     */
    static <T> boolean hasDescendant(_domain _j, Class<T> type, Predicate<T> matchFn) {
        return Walk.first(Node.TreeTraversal.POSTORDER,_j, type, matchFn) != null;
    }

    /**
     * Check to see if this java entity has a Child that matches the type and matchFn
     *
     * @param _j the _java entity to check
     * @param type the target type
     * @param matchFn matching lambda function
     * @param <T> the match type
     * @return
     */
    static <T> boolean hasChild(_domain _j, Class<T> type, Predicate<T> matchFn) {
        return Walk.first(Node.TreeTraversal.DIRECT_CHILDREN,_j, type, matchFn) != null;
    }

    /**
     *
     * @param _j
     */
    static void describe( _domain _j ){
        if( _j instanceof _codeUnit && ((_codeUnit) _j).isTopLevel() ){
            Ast.describe( ((_codeUnit) _j).astCompilationUnit());
        }
        else { //if( _j instanceof _java._astNode){
            Ast.describe ( ((_astNode)_j).ast() );
        }
    }

    /**
     * Describes the Ast node (i.e. the class and content)
     * @param astNode
     */
    static void describe( Node astNode ){
        Ast.describe(astNode);
    }

    /**
     * Read in a .java file from the InputStream and return the _type(_class, _enum, _interface, _annotation)
     * @param is
     * @return
     */
    static <T extends _type> T type(InputStream is) {
        _codeUnit _c = _java.code(is);
        if (_c instanceof _type) {
            return (T) _c;
        }
        throw new _jdraftException("_code in inputStream " + is + " does not represent a _type");
    }

    /**
     * Read in a .java file from the Path and return the _type(_class, _enum, _interface, _annotation)
     * @param path
     * @return
     */
    static <T extends _type> T type(Path path) {
        _codeUnit _c = _java.code(path);
        if (_c instanceof _type) {
            return (T) _c;
        }
        throw new _jdraftException("_code at " + path + " does not represent a _type");
    }

    /**
     * build and return a new _type based on the code provided
     *
     * @param code the code for the _type
     * @return the _type
     * {@link _class} {@link _enum} {@link _interface}, {@link _annotation}
     */
    static <T extends _type> T type(String... code) {
        return type(Ast.typeDecl(code));
    }

    /**
     * Build and return the appropriate _type based on the CompilationUnit
     * (whichever the primary TYPE is)
     *
     * @param astRoot the root AST node containing the top level TYPE
     * @return the _model _type
     */
    static <T extends _type> T type(CompilationUnit astRoot) {
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
            throw new _jdraftException("cannot create _type from CompilationUnit with no TypeDeclaration");
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
                throw new _jdraftException("cannot create a _type out of a package-info.java");
            }
            if (p.endsWith("module-info.java")) {
                throw new _jdraftException("cannot create a _type out of a module-info.java");
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
    static <T extends _type> T type(TypeDeclaration td) {

        /*** MED
        if (td.isTopLevelType()) {
            return type(td.findCompilationUnit().get(), td);
        }
        */
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if (coid.isInterface()) {
                return (T)_interface.of(coid);
            }
            return (T)_class.of(coid);
        }
        if (td instanceof EnumDeclaration) {
            return (T)_enum.of((EnumDeclaration) td);
        }
        return (T)_annotation.of((AnnotationDeclaration) td);
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
    static <T extends _type> T type(CompilationUnit astRoot, TypeDeclaration td) {
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) td;
            if (coid.isInterface()) {
                return (T)_interface.of(coid);
            }
            return (T)_class.of(coid);
        }
        if (td instanceof EnumDeclaration) {
            return (T)_enum.of((EnumDeclaration) td);
        }
        return (T)_annotation.of((AnnotationDeclaration) td);
    }

    /**
     * given a Class, return the draft model of the source
     * @param clazz
     * @param resolver
     * @return
     */
    static <_T extends _type> _T type(Class clazz, _in._resolver resolver ){
        Node n = Ast.typeDecl( clazz, resolver );
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
                return (_T)macro.to(clazz, _interface.of(coid));
            }
            return (_T)macro.to(clazz,  _class.of(coid) );
        }else if( td instanceof EnumDeclaration){
            return (_T)macro.to(clazz, _enum.of( (EnumDeclaration)td));
        }
        return (_T)macro.to(clazz, _annotation.of( (AnnotationDeclaration)td));
    }
    
    /**
     * Locate the source code for the runtime class (clazz)
     *
     * @see _io#IN_DEFAULT the configuration for where to look for the source code of the class
     * @param clazz the class
     * @return
     */
    static <T extends _type> T type(Class clazz) {
        return type(clazz, _io.IN_DEFAULT);
    }

    /**
     * Read and return a _code from the .java source code file at
     * javaSourceFilePath
     *
     * @param javaSourceFilePath the path to the local Java source code
     * @return the _code instance
     */
    static _codeUnit code(Path javaSourceFilePath) throws _jdraftException {
        return code(Ast.of(javaSourceFilePath));
    }

    /**
     * Read and return the appropriate _code model based on the .java source
     * within the javaSourceInputStream
     *
     * @param javaSourceInputStream
     * @return
     */
    static _codeUnit code(InputStream javaSourceInputStream) throws _jdraftException {
        return code(Ast.of(javaSourceInputStream));
    }

    /**
     * Read and return the appropriate _code model based on the .java source
     * within the javaSourceFile
     *
     * @param javaSourceFile
     * @return
     * @throws _jdraftException
     */
    static _codeUnit code(File javaSourceFile) throws _jdraftException {
        return code(Ast.of(javaSourceFile));
    }

    /**
     * build and return the _code wrapper to encapsulate the AST representation
     * of the .java source code stored in the javaSourceReader
     *
     * @param javaSourceReader reader containing .java source code
     * @return the _code model instance representing the source
     */
    static _codeUnit code(Reader javaSourceReader) throws _jdraftException {
        return code(Ast.of(javaSourceReader));
    }

    /**
     * build the appropriate draft wrapper object to encapsulate the AST
     * compilationUnit
     *
     * @param astRoot the AST
     * @return a _code wrapper implementation that wraps the AST
     */
    static _codeUnit code(CompilationUnit astRoot) {
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
        if (astNode instanceof ImportDeclaration ){
            return _import.of((ImportDeclaration) astNode);
        }
        if (astNode instanceof AnnotationExpr) {
            return _anno.of((AnnotationExpr) astNode);
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
            return _field.of(vd);
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
        if (astNode instanceof CompilationUnit) {
            return code((CompilationUnit) astNode);
        }
        throw new _jdraftException("Unable to create _java entity from " + astNode);
    }

    /**
     * Find all labeled statements that match the label "labelName"
     * and flatten the label (inlining the code within the label)
     * for example: <PRE>{@code
     * class c{
     *     void m(){
     *         label: System.out.println(1);
     *     }
     * }
     * _class _c = _class.of(c.class)
     * _java.flattenLabel( _c, "label" );
     * }</PRE>
     * //will make c:
     * <PRE>{@code
     * class c{
     *     void m(){
     *         System.out.println(1);
     *     }
     * }
     * }</PRE>
     *
     * <PRE>{@code
     * class c{
     *     void m(){
     *         label: {
     *             System.out.println(1);
     *             System.out.println(2);
     *             }
     *     }
     * }
     * _class _c = _class.of(c.class)
     * _java.flattenLabel( _c, "label" );
     * }</PRE>
     * //will make c:
     * <PRE>{@code
     * class c{
     *     void m(){
     *         System.out.println(1);
     *         System.out.println(2);
     *     }
     * }
     * }</PRE>
     * @param _j
     * @param labelName
     */
    static void flattenLabel(_domain _j, String labelName){
        if( _j instanceof _multiPart){
            Ast.flattenLabel( ((_multiPart)_j).ast(), labelName);
            return;
        }
        throw new _jdraftException("cannot flatten a label :"+labelName+" from "+ _j.getClass());
    }

    /**
     * Breaking down a larger thing into constituent things
     * often we need to look at properties (like name, modifiers)
     * or sub components (like methods, fields) to understand a thing...
     * Also decomposing things (for diffing, etc.) is useful in analysis
     *
     * @see _multiPart

    interface _componentized{

    }
    */

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

        //MODIFIERS("modifiers", List.class, Modifier.class),
        //MODIFIER("modifier", Modifier.class),
        HEADER_COMMENT("header", Comment.class),
        JAVADOC("javadoc", _javadoc.class),
        PARAMETERS("parameters", _parameters.class),
        //parameter
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

        //IMPORTS("imports", List.class, _import.class),
        //IMPORT("import", ImportDeclaration.class), //todo change to _import
        STATIC("static", Boolean.class),
        WILDCARD("wildcard", Boolean.class),
        ELEMENTS("elements", List.class, _entry.class), //_annotation
        ELEMENT("element", _entry.class), //annotation
        FIELDS("fields", List.class, _field.class),
        FIELD("field", _field.class),
        NESTS("nests", List.class, _type.class),
        NEST("nest", _type.class),

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
        VARIABLES("variables", List.class, VariableDeclarator.class),
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

    // ------------------ COMMENTS --------------------------

    /**
     * list all comments within this astRootNode (including the comment applied
     * to the astRootNode if the AstRootNode is an instance of {@link NodeWithJavadoc}
     *
     * @param astRootNode the root node to look through
     * @return a list of all comments on or underneath the node
     */
    static List<Comment> listComments(Node astRootNode) {
        return Ast.listComments(astRootNode);
    }

    /**
     * @param <C>                the comment class
     * @param astRootNode        the root node to start the search
     * @param commentTargetClass the TYPE of comment ({@link Comment},
     *                           {@link LineComment}, {@link JavadocComment}, {@link BlockComment})
     * @param commentMatchFn     predicate for selecting comments
     * @return a list of matching comments
     */
    static <C extends Comment> List<C> listComments(
            Node astRootNode, Class<C> commentTargetClass, Predicate<C> commentMatchFn) {
        return Ast.listComments(astRootNode, commentTargetClass, commentMatchFn);
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
    static List<Comment> listComments(Node astRootNode, Predicate<Comment> commentMatchFn) {
        return Ast.listComments(astRootNode, commentMatchFn);
    }

    /**
     *
     * @param <C>
     * @param <_J>
     * @param _j
     * @param commentTargetClass
     * @param commentMatchFn
     * @return
     */
    static <C extends Comment, _J extends _domain> List<C> listComments(
            _J _j, Class<C> commentTargetClass, Predicate<C> commentMatchFn){

        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Ast.listComments( ((_codeUnit) _j).astCompilationUnit(), commentTargetClass, commentMatchFn );
            }
            else{
                return Ast.listComments( ((_type) _j).ast(), commentTargetClass, commentMatchFn );
            }
        } else{
            return Ast.listComments(  ((_multiPart) _j).ast(), commentTargetClass, commentMatchFn);
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @return
     */
    static <_J extends _domain> List<Comment> listComments(_J _j, Predicate<Comment> commentMatchFn){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Ast.listComments( ((_codeUnit) _j).astCompilationUnit(), commentMatchFn );
            }
            else{
                return Ast.listComments( ((_type) _j).ast(), commentMatchFn);
            }
        } else{
            return Ast.listComments(  ((_multiPart) _j).ast(), commentMatchFn );
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @param commentActionFn
     */
    static <_J extends _domain> void forComments(_J _j, Predicate<Comment> commentMatchFn, Consumer<Comment> commentActionFn ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Ast.forComments( ((_codeUnit) _j).astCompilationUnit(), commentMatchFn, commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(), commentMatchFn, commentActionFn);
            }
        } else{
            Ast.forComments(  ((_multiPart) _j).ast(), commentMatchFn, commentActionFn );
        }
    }

    /**
     *
     * @param <C>
     * @param <_J>
     * @param _j
     * @param commentClass
     * @param commentMatchFn
     * @param commentActionFn
     */
    static <C extends Comment, _J extends _domain> void forComments(_J _j, Class<C> commentClass, Predicate<C> commentMatchFn, Consumer<C> commentActionFn ){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Ast.forComments( ((_codeUnit) _j).astCompilationUnit(), commentClass, commentMatchFn, commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(),  commentClass, commentMatchFn, commentActionFn);
            }
        } else{
            Ast.forComments(  ((_multiPart) _j).ast(),  commentClass, commentMatchFn, commentActionFn );
        }
    }

    /**
     *
     * @param _j
     * @param commentActionFn
     */
    static void forComments(_domain _j, Consumer<Comment> commentActionFn){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                Ast.forComments( ((_codeUnit) _j).astCompilationUnit(), commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(), commentActionFn);
            }
        } else{
            Ast.forComments(  ((_multiPart)_j).ast(), commentActionFn );
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @return
     */
    static <_J extends _domain> List<Comment> listComments(_J _j){
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel() ){
                return Ast.listComments( ((_codeUnit) _j).astCompilationUnit() );
            }
            else{
                return Ast.listComments( ((_type) _j).ast() );
            }
        } else{
            return Ast.listComments(  ((_multiPart) _j).ast() );
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
        Class<_declaredBodyPart> MEMBER = _declaredBodyPart.class;
        Class<_withName> NAMED = _withName.class;
        Class<_withNameType> NAMED_TYPE = _withNameType.class;

        Class<_withThrows> HAS_THROWS = _withThrows.class;
        Class<_hasBody> HAS_BODY = _hasBody.class;
        Class<_withAnnos> HAS_ANNOS = _withAnnos.class;
        Class<_withConstructors> HAS_CONSTRUCTORS = _withConstructors.class;
        Class<_withJavadoc> HAS_JAVADOC = _withJavadoc.class;
        Class<_withMethods> HAS_METHODS = _withMethods.class;
        Class<_withModifiers> HAS_MODIFIERS = _withModifiers.class;
        Class<_parameter._hasParameters> HAS_PARAMETERS = _parameter._hasParameters.class;
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
     * A {@link _memberBodyPart} defined within a {@link _type} (that is callable/referenceable/reachable) from the outside
     * it can be associated with a larger entity or context)
     * NOTE: each {@link _declaredBodyPart} maps directly to:
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
     * is a {@link _memberBodyPart} but is NOT {@link _declaredBodyPart} (primarily because it is not
     * callable/referenceable/accessible outside of the Class where it is defined and does
     * not satisfy the {@link _withName} {@link _withAnnos} or {@link _withJavadoc} interfaces
     * (Not available via reflection at runtime)
     *
     * @param <N> the AST node type (i.e. {@link MethodDeclaration})
     * @param <_D> the meta-representation declaration type (i.e. {@link _method})
     */
    interface _declaredBodyPart<N extends Node, _D extends _multiPart & _withName & _withAnnos & _withJavadoc>
            extends _memberBodyPart<N, _D>, _withName<_D>, _withAnnos<_D>, _withJavadoc<_D> {

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
     * All _{@link _memberBodyPart}s are {@link _multiPart}s (they are represented by BOTH a meta-representation i.e. {@link _method},
     * and an AST representation {@link MethodDeclaration}.
     *
     * {@link _initBlock} IS a {@link _memberBodyPart}, BUT IS NOT a {@link _declaredBodyPart}, because even though
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
     * @see _declaredBodyPart (an EXTENSION of {@link _memberBodyPart}s that are also {@link _withName}...(all {@link _memberBodyPart}s are
     * {@link _declaredBodyPart}s, ACCEPT {@link _initBlock} which is ONLY a {@link _memberBodyPart}
     */
    interface _memberBodyPart<N extends Node, _N extends _multiPart>
            extends _multiPart<N, _N> {

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
        default <_M extends _memberBodyPart> _M getParentMember(){
            if(this instanceof _field){
                _field _f = (_field)this;
                FieldDeclaration fd = _f.getFieldDeclaration();
                if( fd == null ){
                    return null;
                }
                BodyDeclaration bd = Walk.first(Walk.PARENTS, fd, BodyDeclaration.class);
                if( bd != null ) {
                    return (_M) of(bd);
                }
                return null; //we didnt find a parent that was a BodyDeclaration
            } else{
                BodyDeclaration bd = Walk.first(Walk.PARENTS, ast(), BodyDeclaration.class);
                if( bd != null ) {
                    return (_M) of(bd);
                }
                return null; //we didnt find a parent that was a BodyDeclaration
            }
        }
    }

    /**
     * A _domain entity that maps 1-to-1 to an Ast (Syntax) entity
     *
     * @param <N> the Ast type
     * @param <_N> the _domain type
     */
    interface _astNode <N extends Node, _N extends _astNode> extends _domain {

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
         * Pass in the AST Pretty Printer configuration which will determine how the code
         * is formatted and return the formatted String representing the code.
         *
         * @see com.github.javaparser.printer.PrettyPrintVisitor the original visitor for walking and printing nodes in AST
         * @see PrintNoAnnotations a configurable subclass of PrettyPrintVisitor that will not print ANNOTATIONS
         * @see PrettyPrinterConfiguration the configurations for spaces, Tabs, for printing
         * @see Ast#PRINT_NO_ANNOTATIONS_OR_COMMENTS a premade implementation for
         * @see Ast#PRINT_NO_COMMENTS
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
     * @param <_N> the _domain type
     * @see _multiPart for an ast node type that contains multiple walkable child entities
     */
    interface _uniPart<N extends Node, _N extends _uniPart> extends _astNode<N, _N> { }

    /**
     * {@link _multiPart} entity (having more than one possible child) that maps directly to an AST {@link Node}
     * for example:
     * <UL>
     * <LI>{@link _declaredBodyPart}s</LI>
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
     * <LI>{@link _memberBodyPart}s</LI>
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
     * @param <_N> the jdraft _node type {@link _method}, {@link _field}
     * @param <N> ast node {@link MethodDeclaration}, {@link FieldDeclaration}
     */
    interface _multiPart<N extends Node, _N extends _multiPart> extends _astNode<N, _N> {

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
     * @param <_NL>
     */
    interface _set<EL extends Node, _EL extends _astNode, _NL extends _set> extends _domain{

        _NL copy();

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

        default _EL get(int index){
            return this.list().get(index);
        }

        default EL getAst(int index){
            return this.listAstElements().get(index);
        }

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

        default _NL add(EL... astElements) {
            for( EL el : astElements ) {
                this.listAstElements().add(el);
            }
            return (_NL)this;
        }

        default _NL add(_EL... elements) {
            for( _EL el : elements ) {
                this.listAstElements().add( (EL)el.ast());
            }
            return (_NL)this;
        }

        default _NL remove(_EL... _els) {
            Arrays.stream( _els ).forEach(e -> this.listAstElements().remove( e.ast() ) );
            return (_NL)this;
        }

        default _NL remove(EL... els) {
            Arrays.stream(els ).forEach( e -> this.listAstElements().remove( e ) );
            return (_NL)this;
        }

        default _NL remove(Predicate<_EL> _matchFn) {
            this.list(_matchFn).stream().forEach( e-> remove(e) );
            return (_NL)this;
        }

        default _NL forEach(Predicate<_EL> matchFn, Consumer<_EL> consumer){
            list(matchFn).forEach(consumer);
            return (_NL)this;
        }

        default _NL forEach(Consumer<_EL> consumer){
            list().forEach(consumer);
            return (_NL)this;
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
    interface _list<EL extends Node, _EL extends _astNode, _NL extends _list> extends _set<EL, _EL, _NL>, _domain {

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

        default boolean isAt( int index, EL element){
            return get(index).equals(element);
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
     * @param <_N>
     */
    interface _withName<_N extends _withName> extends _domain {

        /**
         * @param name set the name on the entity and return the modified entity
         * @return the modified entity
         */
        _N setName(String name);

        /**
         * gets the name of the entity
         * @return the name of the entity
         */
        String getName();

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
     *     <LI>{@link _field},
     *     <LI>{@link _parameter},
     *     <LI>{@link _method},
     *     <LI>{@link _entry},
     *     <LI>{@link _receiverParameter}
     * </UL>
     * @param <_NT> the specialized entity that is a named TYPE
     */
    interface _withNameType<N extends Node, _NT extends _withNameType> extends _withName<_NT>, _withType<N,_NT>  {

    }

    interface _withType<N extends Node, _WT extends _astNode> extends _astNode<N, _WT> {

        /**
         * @return they type
         */
        default _typeRef getType(){
            return _typeRef.of(((NodeWithType)ast()).getType());
        }

        /**
         * Gets the element type of the type (i.e. if the type is an Array type)
         * @return the array element type
         */
        default _typeRef getElementType() {
            return _typeRef.of(getType().ast().getElementType());
        }

        /**
         * Set the TYPE and return the modified entity
         * @param t the _typeRef object
         * @return the modified entity after setting the TYPE
         */
        default _WT setType(Type t){
            ((NodeWithType)ast()).setType(t);
            return (_WT)this;
        }

        default boolean isArrayType(){
            return getType().isArrayType();
        }

        /**
         * set the TYPE and return
         * @param _tr
         * @return
         */
        default _WT setType(_typeRef _tr) {
            return setType(_tr.ast());
        }

        /**
         * Set the TYPE and return the modified entity
         * @param typeRef the String representation of the TYPE
         * @return the modified entity after setting the TYPE
         */
        default _WT setType(String typeRef) {
            return setType(typeRef(typeRef));
        }

        /**
         * Set the TYPE and return the modified entity
         * @param clazz the class of the TYPE to set
         * @return the modified entity after setting the TYPE
         */
        default _WT setType(Class clazz) {
            return setType(typeRef(clazz.getCanonicalName()));
        }

        /**
         * is the type void
         * @return true if void
         */
        default boolean isVoid() {
            return getType().ast().isVoidType();
        }

        /**
         * Is the TYPE of this entity the same as represented where the _typeRef TYPE
         * @param type the _typeRef representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(Type type) {
            return Ast.typesEqual( getType().ast(), type);
        }

        /**
         * Is the TYPE of this entity the same as represented where the _typeRef TYPE
         * @param type the _typeRef representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(_typeRef type) {
            return getType().equals(type);
        }

        /**
         * Is the TYPE of this entity the same as represented where the _typeRef TYPE
         * @param clazz the Class representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(Class clazz) {
            try {
                return isType(clazz.getCanonicalName()) || isType(clazz.getSimpleName());
            } catch (Exception e) {
            }
            return false;
        }

        /**
         * Does the type of this named typed comply with the lambda
         * @param typeMatchFn lambda matcher function
         * @return true if the type
         */
        default boolean isType(Predicate<_typeRef> typeMatchFn) {
            return typeMatchFn.test(getType());
        }

        /**
         * Is the TYPE of this entity the same as represented where the String TYPE
         * @param type the String representation of the TYPE
         * @return true if the TYPE is the same
         */
        default boolean isType(String type) {
            try {
                return isType(typeRef(type));
            } catch (Exception e) {
            }
            return false;
        }
    }

    interface _withScope<N extends Node, _TA extends _astNode> extends _astNode<N, _TA> {

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

        default _TA removeScope(){
            ((NodeWithOptionalScope)ast()).removeScope();
            return (_TA)this;
        }

        default _TA setScope( String scope ){
            ((NodeWithOptionalScope)ast()).setScope(Ex.of(scope));
            return (_TA)this;
        }

        default _TA setScope( _expression _e){
            ((NodeWithOptionalScope)ast()).setScope(_e.ast());
            return (_TA)this;
        }

        default _TA setScope( Expression e){
            ((NodeWithOptionalScope)ast()).setScope(e);
            return (_TA)this;
        }

        default _TA setScope(String... scope){
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

    interface _withArguments<N extends Node, _TA extends _astNode> extends _astNode<N, _TA> {

        default _expression getArgument( int index){
            return _expression.of( ((NodeWithArguments)ast()).getArgument(index) );
        }

        default _TA removeArgument( int index ){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_TA)this;
        }

        default _TA setArgument(int index, _expression _e){
            ((NodeWithArguments)ast()).getArguments().set(index, _e.ast());
            return (_TA)this;
        }

        default _TA setArgument(int index, Expression e){
            ((NodeWithArguments)ast()).getArguments().set(index, e);
            return (_TA)this;
        }

        default _TA setArgument( int index, boolean b){
            return setArgument(index, Ex.of(b));
        }

        default _TA setArgument( int index, int i){
            return setArgument(index, Ex.of(i));
        }

        default _TA setArgument( int index, char c){
            return setArgument(index, Ex.of(c));
        }

        default _TA setArgument( int index, float f){
            return setArgument(index, Ex.of(f));
        }

        default _TA setArgument( int index, long l){
            return setArgument(index, Ex.of(l));
        }

        default _TA setArgument(int index, double d){
            return setArgument(index, Ex.of(d));
        }

        default _TA setArguments(_expression ... _es){
            NodeList<Expression> nle = new NodeList<>();
            Arrays.stream(_es).forEach(n -> nle.add(n.ast()));
            ((NodeWithArguments)ast()).setArguments(nle);
            return (_TA)this;
        }

        default _TA setArguments(Expression ... es){
            NodeList<Expression> nle = new NodeList<>();
            Arrays.stream(es).forEach(n -> nle.add(n));
            ((NodeWithArguments)ast()).setArguments(nle);
            return (_TA)this;
        }

        default boolean hasArguments(){
            return ((NodeWithArguments)ast()).getArguments().size() > 0 ;
        }

        default int countArguments(){
            return ((NodeWithArguments)ast()).getArguments().size();
        }

        default int countArguments( Predicate<_expression> matchFn){
            return listArguments(matchFn).size();
        }

        default List<_expression> listArguments(){
            List<_expression> args = new ArrayList<>();
            ((NodeWithArguments)ast()).getArguments().forEach(a -> args.add(_expression.of( (Expression)a)));
            return args;
        }

        default List<_expression> listArguments(Predicate<_expression> matchFn){
            return listArguments().stream().filter(matchFn).collect(Collectors.toList());
        }

        default boolean isArguments(String...es){
            _expression[] _es = new _expression[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expression.of(es[i]);
            }
            return isArguments(_es);
        }

        default boolean isArguments(Expression...es){
            _expression[] _es = new _expression[es.length];
            for(int i=0;i<es.length;i++){
                _es[i] = _expression.of(es[i]);
            }
            return isArguments(_es);
        }

        default boolean isArguments(_expression..._es){
            List<_expression> _tes = listArguments();
            if(_es.length == _tes.size()){
                for(int i=0;i<_es.length;i++){
                    if( ! Ex.equivalent(  _es[i].ast(), _tes.get(i).ast() ) ){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isArguments( Predicate<List<_expression>> matchFn){
            return matchFn.test( listArguments() );
        }

        default boolean isArgument( int index, boolean b){
            return isArgument(index, Ex.of(b));
        }

        default boolean isArgument( int index, int i){
            return isArgument(index, Ex.of(i));
        }

        default boolean isArgument( int index, char c){
            return isArgument(index, Ex.of(c));
        }

        default boolean isArgument( int index, float f){
            return isArgument(index, Ex.of(f));
        }

        default boolean isArgument( int index, long l){
            return isArgument(index, Ex.of(l));
        }

        default boolean isArgument(int index, double d){
            return isArgument(index, Ex.of(d));
        }

        default boolean isArgument( int index, String exprString){
            try {
                return Ex.equivalent( getArgument(index).ast(), Ex.of(exprString));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isArgument( int index, Expression e){
            try {
                return Ex.equivalent( getArgument(index).ast(), e);
            }catch(Exception ex){
                return false;
            }
        }

        default boolean isArgument( int index, _expression _e){
            try {
                return Ex.equivalent( getArgument(index).ast(), _e.ast());
            }catch(Exception e){
                return false;
            }
        }


        default _TA addArgument( int i){
            return addArgument( Ex.of(i) );
        }

        default _TA addArgument( boolean b){
            return addArgument( Ex.of(b) );
        }

        default _TA addArgument( float f){
            return addArgument( Ex.of(f) );
        }

        default _TA addArgument( long l){
            return addArgument( Ex.of(l) );
        }

        default _TA addArgument( double d){
            return addArgument( Ex.of(d) );
        }

        default _TA addArgument( char c){
            return addArgument( Ex.of(c) );
        }

        default _TA addArgument( Expression e ){
            return addArguments( e );
        }

        default _TA addArguments(String...es){
            Arrays.stream(es).forEach(e -> ((NodeWithArguments)ast()).addArgument(e));
            return (_TA)this;
        }

        default _TA addArguments(Expression...es){
            Arrays.stream(es).forEach(e -> ((NodeWithArguments)ast()).addArgument(e));
            return (_TA)this;
        }

        default _TA addArguments(_expression..._es){
            Arrays.stream(_es).forEach(_e -> ((NodeWithArguments)ast()).addArgument(_e.ast()));
            return (_TA)this;
        }

        default _TA removeArguments(){
            ((NodeWithArguments)ast()).getArguments().removeIf( t->true);
            return (_TA)this;
        }

        default _TA removeArguments(int index){
            ((NodeWithArguments)ast()).getArguments().remove(index);
            return (_TA)this;
        }
        default _TA removeArguments(Predicate<_expression> matchFn ){
            ((NodeWithArguments)ast()).getArguments().removeIf(matchFn);
            return (_TA)this;
        }

        default _TA removeArguments(_expression...es ){
            for(int i=0;i<es.length;i++){
                ((NodeWithArguments)ast()).getArguments().remove(es[i].ast());
            }
            return (_TA)this;
        }

        default _TA removeArguments(Expression...es ){
            for(int i=0;i<es.length;i++){
                ((NodeWithArguments)ast()).getArguments().remove(es[i]);
            }
            return (_TA)this;
        }

        default _TA forArguments(Consumer<_expression> argFn){
            ((NodeWithArguments)ast()).getArguments().stream().map( a-> _expression.of( (Expression)a))
                    .forEach(e->  argFn.accept( (_expression)e) );
            return (_TA)this;
        }

        default _TA forArguments(Predicate<_expression> expressionMatchFn, Consumer<_expression> argFn){
            ((NodeWithArguments)ast()).getArguments().stream()
                    .map( a-> _expression.of( (Expression)a))
                    .filter(expressionMatchFn).forEach(e->  argFn.accept( (_expression)e) );
            return (_TA)this;
        }
    }

    interface _withCondition <N extends Node, _WE extends _astNode> extends _astNode<N, _WE> {

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

        default _WE setCondition(String...expression){
            return setCondition(Ex.of(expression));
        }

        default _WE setCondition(_expression e){
            return setCondition(e.ast());
        }

        default _WE setCondition(Expression e){
            ((NodeWithExpression)ast()).setExpression(e);
            return (_WE)this;
        }

        default _expression getCondition(){
            return _expression.of(((NodeWithExpression)ast()).getExpression());
        }
    }

    interface _withExpression <N extends Node, _WE extends _astNode> extends _astNode<N, _WE> {

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

    /**
     *
     * @param <N>
     * @param <_TA>
     */
    interface _withTypeArguments<N extends Node, _TA extends _astNode> extends _astNode<N, _TA> {

        default boolean hasTypeArguments(){
            return ((NodeWithTypeArguments)ast()).getTypeArguments().isPresent();
        }

        /**
         * are we using the diamond operator &lt;>
         * @return
         */
        default boolean isUsingDiamondOperator(){
            return ((NodeWithTypeArguments)ast()).isUsingDiamondOperator();
        }

        /**
         * Set the TypeArguments to be using the Diamond Operator
         * @return
         */
        default _TA setUseDiamondOperator(){
            ((NodeWithTypeArguments)ast()).setDiamondOperator();
            return (_TA)this;
        }

        default int typeArgumentCount(){
            if( hasTypeArguments() ){
                NodeList<Type> nlt =(NodeList<Type>) ((NodeWithTypeArguments)ast()).getTypeArguments().get();
                return nlt.size();
            }
            return ((NodeWithArguments)ast()).getArguments().size();
        }

        default _TA setTypeArguments( Type...ts ){
            ((NodeWithTypeArguments)ast()).setTypeArguments(ts);
            return (_TA)this;
        }

        default _TA setTypeArguments( _typeRef...tr){
            NodeList<Type> tas = new NodeList<>();
            Arrays.stream(tr).forEach( t -> tas.add(t.ast()));
            ((NodeWithTypeArguments)ast()).setTypeArguments(tas);
            return (_TA)this;
        }

        default List<_typeRef> listTypeArguments(){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            if( nwta.getTypeArguments().isPresent() ){
                NodeList<Type> nlt = (NodeList<Type>)nwta.getTypeArguments().get();
                return nlt.stream().map(n -> _typeRef.of(n)).collect(Collectors.toList());
            }
            return Collections.EMPTY_LIST;
        }

        default _typeRef getTypeArgument( int index ){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            if( nwta.getTypeArguments().isPresent() ) {
                NodeList<Type> nlt = (NodeList<Type>) nwta.getTypeArguments().get();
                Type t = nlt.get(index);
                return _typeRef.of(t);
            }
            throw new _jdraftException("No type arguments");
        }

        /**
         * Returns a list of Type arguments based on the predicate
         * if there are any or an empty list if there are none
         * @return
         */
        default List<_typeRef> listTypeArguments(Predicate<_typeRef> matchFn){
            return listTypeArguments().stream().filter(matchFn).collect(Collectors.toList());
        }

        default _TA forTypeArguments(Consumer<_typeRef> typeArgFn){
            listTypeArguments().stream().forEach(typeArgFn);
            return (_TA)this;
        }

        default _TA forTypeArguments(Predicate<_typeRef> matchFn, Consumer<_typeRef> typeArgFn){
            listTypeArguments().stream().filter(matchFn).forEach(typeArgFn);
            return (_TA)this;
        }

        default _TA removeTypeArgument(int index){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            NodeList<Type> nt = (NodeList<Type>)nwta.getTypeArguments().get();
            nt.remove(index);
            return (_TA)this;
        }
        /**
         * Remove ALL type Arguments
         * @return
         */
        default _TA removeTypeArguments(){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            nwta.removeTypeArguments();
            return (_TA)this;
        }

        default _TA removeTypeArguments( String...tas ){
            Type[] ts = new Type[tas.length];
            for(int i=0;i<tas.length;i++){
                ts[i] = Ast.typeRef(tas[i]);
            }
            return removeTypeArguments(ts);
        }

        /**
         * removes these specific type arguments if they are found in the TypeArgs
         * @param tas
         * @return
         */
        default _TA removeTypeArguments( Type...tas ){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            NodeList<Type> nt = null;

            if( nwta.getTypeArguments().isPresent() ){
                nt =(NodeList<Type>) nwta.getTypeArguments().get();
                for(int i=0;i<tas.length;i++){
                    nt.remove(tas[i]);
                }
            }
            return (_TA) this;
        }

        /**
         * removes these specific type arguments if they are found in the TypeArgs
         * @param tas
         * @return
         */
        default _TA removeTypeArguments( _typeRef...tas ){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            NodeList<Type> nt = null;

            if( nwta.getTypeArguments().isPresent() ){
                nt =(NodeList<Type>) nwta.getTypeArguments().get();
                for(int i=0;i<tas.length;i++){
                    nt.remove(tas[i].ast());
                }
            }
            return (_TA) this;
        }

        /**
         * removes these specific type arguments if they are found in the TypeArgs
         * @param matchFn
         * @return
         */
        default _TA removeTypeArguments( Predicate<_typeRef> matchFn ){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            NodeList<Type> nt = null;


            if( nwta.getTypeArguments().isPresent() ){
                nt =(NodeList<Type>) nwta.getTypeArguments().get();
                List<Type> toRemove =
                        nt.stream().filter(ta -> matchFn.test( _typeRef.of(ta))).collect(Collectors.toList());
                return removeTypeArguments(toRemove.toArray(new Type[0]));
            }
            return (_TA) this;
        }

        default _TA setTypeArgument(int index, Type t ){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            NodeList<Type> nt = null;

            if( nwta.getTypeArguments().isPresent() ){
                NodeList<Type> nta = (NodeList<Type>) nwta.getTypeArguments().get();
                nta.set(index, t);
            }
            return (_TA)this;
        }

        default _TA setTypeArgument(int index, _typeRef _t ){
            return setTypeArgument(index, _t.ast());
        }

        default _TA setTypeArgument(int index, String type){
            return setTypeArgument(index, Ast.typeRef(type));
        }

        default boolean isTypeArguments(_typeRef..._typeArguments){
            List<_typeRef> tas = listTypeArguments();
            if( _typeArguments.length != tas.size()){
                return false;
            }
            for(int i=0;i<tas.size();i++){
                if( !tas.get(i).equals( _typeArguments[i])){
                    return false;
                }
            }
            return true;
        }

        default _TA addTypeArguments(String...ts){
            Type[] tys = new Type[ts.length];
            for(int i=0;i<ts.length;i++){
                tys[i] = Ast.typeRef(ts[i]);
            }
            return addTypeArguments(tys);
        }

        default _TA addTypeArguments(Class...cs){
            Type[] ts = new Type[cs.length];
            for(int i=0;i<cs.length;i++){
                ts[i] = Ast.typeRef(cs[i]);
            }
            return addTypeArguments(ts);
        }

        default _TA addTypeArguments(Type...ts){
            NodeWithTypeArguments nwta = (NodeWithTypeArguments)ast();
            NodeList<Type> nt = null;
            if( nwta.getTypeArguments().isPresent() ){
                nt =(NodeList<Type>) nwta.getTypeArguments().get();
            } else{
                nt = new NodeList<Type>();
                nwta.setTypeArguments(nt);
            }
            for(int i=0;i<ts.length;i++){
                nt.add( ts[i]);
            }
            return (_TA)this;
        }

        default _TA addTypeArguments(_typeRef..._es){
            List<Type> lt = Arrays.stream(_es).map(_e -> _e.ast()).collect(Collectors.toList());
            return addTypeArguments( lt.toArray(new Type[0]));
        }
    }
}
