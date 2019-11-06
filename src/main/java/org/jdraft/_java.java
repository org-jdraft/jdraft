package org.jdraft;

import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
import org.jdraft._initBlock._hasInitBlocks;
import org.jdraft._throws._hasThrows;
import org.jdraft._type._hasExtends;
import org.jdraft._type._hasImplements;
import org.jdraft._typeParameter._typeParameters;
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.macro.macro;

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
     * Shortcut for checking if an ast has a parent of a particular class that complies with a particular Predicate
     * @param _j the _java entity
     * @param parentNodeClass the node class expected of the parent node
     * @param parentMatchFn predicate for matching the parent
     * @param <_J> the expected _java node type
     * @return true if the parent node exists, is of a particular type and complies with the predicate
     */
    static <_J extends _draft> boolean isParent(_draft _j, Class<_J> parentNodeClass, Predicate<_J> parentMatchFn){
        if( _j instanceof _node ){
            AtomicBoolean ans = new AtomicBoolean(false);
            _walk.in_java(Node.TreeTraversal.PARENTS, 1, ((_node)_j).ast(), parentNodeClass, parentMatchFn, (t)-> ans.set(true) );
            return ans.get();
        }
        //need to handle _typeParameters, _parameters, _annos
        if( _j instanceof _typeParameters ){
            _typeParameters _tps = (_typeParameters)_j;
            _node _n = (_node)_java.of( (Node)_tps.astHolder());
            return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
        }
        if( _j instanceof _body){
            _body _tps = (_body)_j;
            Object par = _tps.astParentNode();
            if( par != null ){
                _node _n = (_node)_java.of( (Node)par );
                return parentNodeClass.isAssignableFrom(_n.getClass()) && parentMatchFn.test( (_J)_n);
            }
        }
        if( _j instanceof _parameters){
            _parameters _tps = (_parameters)_j;
            _node _n = (_node)_java.of( (Node)_tps.astHolder());
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
    static <T> boolean hasAncestor(_draft _j, Class<T> type, Predicate<T> matchFn){
        return _walk.first(Node.TreeTraversal.PARENTS, _j, type, matchFn) != null;
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
    static <T> boolean hasDescendant(_draft _j, Class<T> type, Predicate<T> matchFn) {
        return _walk.first(Node.TreeTraversal.POSTORDER,_j, type, matchFn) != null;
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
    static <T> boolean hasChild(_draft _j, Class<T> type, Predicate<T> matchFn) {
        return _walk.first(Node.TreeTraversal.DIRECT_CHILDREN,_j, type, matchFn) != null;
    }

    /**
     *
     * @param _j
     */
    static void describe( _draft _j ){
        if( _j instanceof _code && ((_code) _j).isTopLevel() ){
            Ast.describe( ((_code) _j).astCompilationUnit());
        }
        Ast.describe ( ((_node)_j).ast() );
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
        _code _c = _java.code(is);
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
        _code _c = _java.code(path);
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
    static _code code(Path javaSourceFilePath) throws _jdraftException {
        return code(Ast.of(javaSourceFilePath));
    }

    /**
     * Read and return the appropriate _code model based on the .java source
     * within the javaSourceInputStream
     *
     * @param javaSourceInputStream
     * @return
     */
    static _code code(InputStream javaSourceInputStream) throws _jdraftException {
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
    static _code code(File javaSourceFile) throws _jdraftException {
        return code(Ast.of(javaSourceFile));
    }

    /**
     * build and return the _code wrapper to encapsulate the AST representation
     * of the .java source code stored in the javaSourceReader
     *
     * @param javaSourceReader reader containing .java source code
     * @return the _code model instance representing the source
     */
    static _code code(Reader javaSourceReader) throws _jdraftException {
        return code(Ast.of(javaSourceReader));
    }

    /**
     * build the appropriate draft wrapper object to encapsulate the AST
     * compilationUnit
     *
     * @param astRoot the AST
     * @return a _code wrapper implementation that wraps the AST
     */
    static _code code(CompilationUnit astRoot) {
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
     * must extend {@link _draft} or {@link Node}
     * @param code the java source code representation
     * @return the node implementation of the code
     */
    static Node node(Class nodeClass, String... code) {
        
        if (! _draft.class.isAssignableFrom(nodeClass)) {
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
        if (_enum._constant.class == nodeClass) {
            return constantDecl(code);
        }
        if (_annotation._element.class == nodeClass) {
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
     * {@link _annotation._element}
     * {@link _constructor}
     * {@link _enum._constant}
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
    static _draft of(Node astNode) {
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
            return _annotation._element.of((AnnotationMemberDeclaration) astNode);
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
            return _enum._constant.of((EnumConstantDeclaration) astNode);
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
    static void flattenLabel(_draft _j, String labelName){
        if( _j instanceof _node ){
            Ast.flattenLabel( ((_node)_j).ast(), labelName);
            return;
        }
        throw new _jdraftException("cannot flatten a label :"+labelName+" from "+ _j.getClass());
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _enum._constant
     * @see _annotation._element
     *
     * @param clazz
     * @param line
     * @param column
     * @param <_M>
     * @return
     */
    static <_M extends _member> _M  memberAt(Class clazz, int line, int column) {
        return memberAt( Ast.of(clazz), Math.max( line, 1), column);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @param model
     * @param line
     * @param column
     * @param <_M>
     * @return
     */
    static <_M extends _member> _M  memberAt(_member model, int line, int column) {
        return memberAt(model.ast(), line, column);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _enum._constant
     * @see _annotation._element
     *
     * @param top the top node
     * @param line a 1-based line number (we start with line 1)
     * @param column the column within the file
     * @param <_M> a _member implementation
     * @return
     */
    static <_M extends _member> _M  memberAt(Node top, int line, int column) {
        BodyDeclaration astM = Ast.memberAt(top, line, column);
        if( astM == null ){
            return null;
        }
        return (_M)_java.of(astM);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _enum._constant
     * @see _annotation._element
     *
     * @param clazz the runtime Class
     * @param line a 1-based line number (we start with line 1)
     * @param <_M> a _member implementation
     * @return
     */
    static <_M extends _member> _M memberAt(Class clazz, int line ) {
        return memberAt( Ast.of(clazz), line);
    }

    /**
     * return the most specific member ({@link _method}, {@link _field}, {@link _constructor},
     * {@link _initBlock}...) that is at of or contains the line number
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _enum._constant
     * @see _annotation._element
     *
     * @param _mem
     * @param line
     * @param <_M>
     * @return
     */
    static <_M extends _member> _M  memberAt(_member _mem, int line ) {
        return memberAt(_mem.ast(), line );
    }

    /**
     * finds the closest/most specific _java _member CONTAINING this position and
     * returns it (or null if the position is outside for range)
     *
     * @see _type
     * @see _enum
     * @see _class
     * @see _interface
     * @see _annotation
     * @see _method
     * @see _field
     * @see _initBlock
     * @see _enum._constant
     * @see _annotation._element
     *
     * @param top the top node
     * @param line a 1-based line number (we start with line 1)
     * @param <_M> a _member implementation
     * @return an instance of a BodyDeclaration AST Node (or null if not found)
     */
    static <_M extends _member> _M memberAt(Node top, int line ) {
        BodyDeclaration astM = Ast.memberAt(top, line);
        if( astM == null ){
            return null;
        }
        return (_M)_java.of(astM);
    }

    /**
     * Breaking down a larger thing into constituent things
     * often we need to look at properties (like name, modifiers)
     * or sub components (like methods, fields) to understand a thing...
     * Also decomposing things (for diffing, etc.) is useful in analysis
     *
     * @see _packageInfo
     * @see _moduleInfo
     * @see _type
     * @see _node
     * @see _declared
     */
    interface _componentized{

        /**
         * Decompose the entity into key-VALUE pairs where the key is the Component
         * @return a map of key values
         */
        Map<Component, Object> components();
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
        PARAMETERS("parameters", _parameters.class),
        //parameter
        PARAMETER("parameter", _parameter.class),
        RECEIVER_PARAMETER("receiverParameter", _receiverParameter.class),
        TYPE_PARAMETERS("typeParameters", _typeParameter._typeParameters.class),
        TYPE_PARAMETER("typeParameter", TypeParameter.class), //_typeParameter.class
        THROWS("throws", _throws.class),
        NAME("name", String.class),

        KEY_VALUES("keyValues", List.class, MemberValuePair.class), //anno
        KEY_VALUE("keyValue", MemberValuePair.class), //anno

        IMPORTS("imports", List.class, _import.class),
        IMPORT("import", ImportDeclaration.class),
        STATIC("static", Boolean.class),
        WILDCARD("wildcard", Boolean.class),
        ELEMENTS("elements", List.class, _annotation._element.class), //_annotation
        ELEMENT("element", _annotation._element.class), //annotation
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
        CONSTANTS("constants", List.class, _enum._constant.class),
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

        public static <_N extends _node> Component of(Class<_N> nodeClass) {
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
    static <C extends Comment, _J extends _draft> List<C> listComments(
            _J _j, Class<C> commentTargetClass, Predicate<C> commentMatchFn){

        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                return Ast.listComments( ((_code) _j).astCompilationUnit(), commentTargetClass, commentMatchFn );
            }
            else{
                return Ast.listComments( ((_type) _j).ast(), commentTargetClass, commentMatchFn );
            }
        } else{
            return Ast.listComments(  ((_node) _j).ast(), commentTargetClass, commentMatchFn);
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @return
     */
    static <_J extends _draft> List<Comment> listComments(_J _j, Predicate<Comment> commentMatchFn){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                return Ast.listComments( ((_code) _j).astCompilationUnit(), commentMatchFn );
            }
            else{
                return Ast.listComments( ((_type) _j).ast(), commentMatchFn);
            }
        } else{
            return Ast.listComments(  ((_node) _j).ast(), commentMatchFn );
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @param commentMatchFn
     * @param commentActionFn
     */
    static <_J extends _draft> void forComments(_J _j, Predicate<Comment> commentMatchFn, Consumer<Comment> commentActionFn ){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                Ast.forComments( ((_code) _j).astCompilationUnit(), commentMatchFn, commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(), commentMatchFn, commentActionFn);
            }
        } else{
            Ast.forComments(  ((_node) _j).ast(), commentMatchFn, commentActionFn );
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
    static <C extends Comment, _J extends _draft> void forComments(_J _j, Class<C> commentClass, Predicate<C> commentMatchFn, Consumer<C> commentActionFn ){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                Ast.forComments( ((_code) _j).astCompilationUnit(), commentClass, commentMatchFn, commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(),  commentClass, commentMatchFn, commentActionFn);
            }
        } else{
            Ast.forComments(  ((_node) _j).ast(),  commentClass, commentMatchFn, commentActionFn );
        }
    }

    /**
     *
     * @param _j
     * @param commentActionFn
     */
    static void forComments(_draft _j, Consumer<Comment> commentActionFn){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                Ast.forComments( ((_code) _j).astCompilationUnit(), commentActionFn);
            }
            else{
                Ast.forComments( ((_type) _j).ast(), commentActionFn);
            }
        } else{
            Ast.forComments(  ((_node)_j).ast(), commentActionFn );
        }
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @return
     */
    static <_J extends _draft> List<Comment> listComments(_J _j){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel() ){
                return Ast.listComments( ((_code) _j).astCompilationUnit() );
            }
            else{
                return Ast.listComments( ((_type) _j).ast() );
            }
        } else{
            return Ast.listComments(  ((_node) _j).ast() );
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
        Class<_code> CODE = _code.class;

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
        Class<_element> ELEMENT = _element.class;

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
        Class<_node> NODE = _node.class;
        Class<_declared> MEMBER = _declared.class;
        Class<_named> NAMED = _named.class;
        Class<_namedType> NAMED_TYPE = _namedType.class;

        Class<_hasThrows> HAS_THROWS = _hasThrows.class;
        Class<_hasBody> HAS_BODY = _hasBody.class;
        Class<_hasAnnos> HAS_ANNOS = _hasAnnos.class;
        Class<_hasConstructors> HAS_CONSTRUCTORS = _hasConstructors.class;
        Class<_hasJavadoc> HAS_JAVADOC = _hasJavadoc.class;
        Class<_hasMethods> HAS_METHODS = _hasMethods.class;
        Class<_hasModifiers> HAS_MODIFIERS = _hasModifiers.class;
        Class<_parameter._hasParameters> HAS_PARAMETERS = _parameter._hasParameters.class;
        Class<_hasReceiverParameter> HAS_RECEIVER_PARAMETER = _hasReceiverParameter.class;
        Class<_hasInitBlocks> HAS_STATIC_BLOCKS = _hasInitBlocks.class;
        Class<_hasExtends> HAS_EXTENDS = _hasExtends.class;
        Class<_hasImplements> HAS_IMPLEMENTS = _hasImplements.class;

        Class<_hasFinal> HAS_FINAL = _hasFinal.class;
        Class<_hasAbstract> HAS_ABSTRACT = _hasAbstract.class;
        Class<_hasNative> HAS_NATIVE = _hasNative.class;
        Class<_hasStatic> HAS_STATIC = _hasStatic.class;
        Class<_hasStrictFp> HAS_STRICTFP = _hasStrictFp.class;
        Class<_hasSynchronized> HAS_SYNCHRONIZED = _hasSynchronized.class;
        Class<_hasTransient> HAS_TRANSIENT = _hasTransient.class;
        Class<_hasVolatile> HAS_VOLATILE = _hasVolatile.class;

        /**
         * Map from the _java classes to the Ast Node equivalent
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
}
