package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.nodeTypes.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

import static org.jdraft.Ast.*;

import org.jdraft._annos._withAnnos;
import org.jdraft._body._withBody;

import org.jdraft._modifiers.*;
import org.jdraft._constructor._withConstructors;
import org.jdraft._javadocComment._withJavadoc;
import org.jdraft._method._withMethods;
import org.jdraft._receiverParam._withReceiverParam;
import org.jdraft._initBlock._withInitBlocks;
import org.jdraft._throws._withThrows;
import org.jdraft._type._withExtends;
import org.jdraft._type._withImplements;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.walk.Walk;

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

    static char[] controlChars = new char[]{
            '\"', '\\', '.','<','>',',','.','*', '&', '@','!','~','%','^','(',')','-', '.', '=','+',':',';','?','/', '\t', ' ','|', '[', '{', '}', ']'};


    /**
     * Characters that are used in lexing the input

    enum ControlCharacter{

        LESS_THAN('<'),

        DOT('.');


        private final char c;

        ControlCharacter(char c){
            this.c = c;
        }
    }
     */
    /*
    enum Symbol{
        LESS_THAN("<", BinaryExpr.Operator.LESS),
        PLUS_EQUALS("+=", AssignExpr.Operator.PLUS),
        PLUS("+", BinaryExpr.Operator.PLUS, UnaryExpr.Operator.PLUS),
        IF_TERNARY( "?", "?");
        String representation;
        Object[] implementations;

        private Symbol(String representation, Object...implementations){
            this.representation = representation;
            this.implementations = implementations;
        }
    }
     */

    /**
     *

    enum _reservedWord{

    }
    */

    /**
     * Builds an appropriate
     * @param code
     * @return
     */
    public static _java._domain of(String...code){
        String all = Text.combine(code);

        if( all.trim().length() ==0){
            return _emptyStmt.of();
        }
        if( all.equals("true") || all.equals("false") ){
            return _booleanExpr.of(all);
        }
        if( all.equals("null") ){
            return _nullExpr.of(all);
        }
        if( all.startsWith("'") && all.endsWith("'") ){
            return _charExpr.of(all);
        }
        if( all.startsWith("@") ){
            if( all.startsWith("@interface ") ){
                return _annotation.of(all);
            }

            try {
                _annos _as = _annos.of(all);
                if (_as.size() == 1) {
                    return _as.getAt(0);
                }
                return _as;
            }catch(Exception e){ }
            //could be a field constructor or method with an annotation!!!
            try{ return _field.of(all); }catch(Exception e){}
            try{ return _method.of(all); }catch(Exception e){}
            try{ return _constructor.of(all); }catch(Exception e){}
            try{ return _constant.of(all); }catch(Exception e){}
            try{ return _receiverParam.of(all); }catch(Exception e){}
            try{ return _typeRef.of(all); }catch(Exception e){}
        }
        if( all.startsWith("(") ){
            if( all.endsWith(")")){//args or params or parethesizedExpr
                //try params... if not working try args
                try { return _params.of(all); }catch(Exception e){}
                try { return _parenthesizedExpr.of(all); }catch (Exception e){ }
                try { return _args.of(all); }catch (Exception e){ }
            } else{
                try { return _castExpr.of(all); }catch(Exception e){}
            }
            if( all.contains("->")){
                //lambda
                try{  return _lambdaExpr.of(all); }catch(Exception e){}
            }
        }
        if( all.endsWith(")") && all.contains("(")){
            if( all.contains("->") ){
                try { return _lambdaExpr.of(all);}catch(Exception e){} //lambda calling a method??
            }
            try { return _methodCallExpr.of(all);}catch(Exception e){}
            try { return _constant.of(all);}catch(Exception e){} //enum constant SPADES("spade")
        }

        if( all.startsWith("[") && all.endsWith("]")){
            try{ return _arrayDimension.of(all); }catch(Exception e){}
        }
        if( all.startsWith("{") && all.endsWith("}")) {//code block or arrayInit
            try { return _arrayInitExpr.of(all); } catch (Exception e) { }
            try { return _blockStmt.of(all); } catch (Exception e) { }
            //initBlock?
        }

        if( all.startsWith("/*") && all.endsWith("*/")){
            if( all.startsWith("/**") ){
                return _javadocComment.of(all);
            }
            return _blockComment.of(all);
        }
        if( all.startsWith("module ") && all.endsWith("}") ){
            return _moduleInfo.of(all);
        }
        if( all.startsWith("exports ") ){
            return _moduleExports.of(all);
        }
        if( all.startsWith("opens ") ){
            return _moduleOpens.of(all);
        }
        if( all.startsWith("uses ") ){
            return _moduleUses.of(all);
        }
        if( all.startsWith("requires ") ){
            return _moduleRequires.of(all);
        }
        if( all.startsWith("provides ") ){
            return _moduleProvides.of(all);
        }
        if( all.startsWith("if") ){
            try{ return _ifStmt.of(all);}catch(Exception e){}
        }
        if( all.startsWith("class ") ){
            try{ return _class.of(all);}catch(Exception e){}
        }
        if( all.startsWith("for") ){
            if( all.contains(":") ){
                try{ return _forEachStmt.of(all);}catch(Exception e){}
            }
            try{ return _forStmt.of(all);}catch(Exception e){}
        }

        if( all.startsWith("package ") && all.endsWith(";")){
            return _package.of(all);
        }
        if( all.startsWith("try") && all.endsWith("}")){
            return _tryStmt.of(all);
        }
        if( all.startsWith("static") ){
            if( all.endsWith("}") ){
                return _initBlock.of(all);
            }
            try{ return _field.of(all);}catch(Exception e){}
            try{ return _method.of(all);}catch(Exception e){}
            try{ return _class.of(all);}catch(Exception e){}
        }
        if( all.startsWith("throws ") ){
            return _throws.of(all);
        }
        if( all.startsWith("throw ") && all.endsWith(";") ){
            return _throwStmt.of(all);
        }
        if( all.startsWith("yield ") && all.endsWith(";") ){
            return _yieldStmt.of(all);
        }
        if( all.startsWith("while") && all.endsWith("}")){
            return _whileStmt.of(all);
        }
        if( all.startsWith("<") && all.endsWith(">")){
            return _typeParams.of(all);
            //return _typeArgs.of(all);
        }

        if( all.startsWith("import ") ){
            //might be a class, interface, enum with first thing being import
            _imports _is = _imports.of(all);
            if( _is.size() == 1 ){
                return _is.getAt(0);
            }
            return _is;
        }
        if( all.startsWith("assert ") ){
            //might be the first statment of many
            try { return _assertStmt.of(all); }catch(Exception e){}
        }
        if( all.startsWith("new ") ){
            if( all.contains("[") && all.contains("]")){
                return _newArrayExpr.of( all);
            }
            return _newExpr.of(all);
        }
        if( all.startsWith("break") ){
            try{ return _breakStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("continue")){
            try{ return _continueStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("super") && !all.contains("(") ){
            try{ return _superExpr.of(all); } catch(Exception e){}
        }
        if( (all.startsWith("this") || all.startsWith("super")) && all.contains("(") && all.endsWith(";") ){
            try{ return _constructorCallStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("case") || all.startsWith("default")){
            try{
                _switchCases _sc = _switchCases.of(all);
                if( _sc.size() == 1 ){
                    return _sc.getAt(0);
                }
                return _sc;
            } catch(Exception e){

            }
            try{ return _switchCase.of(all);}catch (Exception e){}
        }
        if( all.startsWith("catch") ){
            try{ return _catch.of(all); } catch(Exception e){}
        }
        if( (all.startsWith("do") || all.endsWith(";")) && all.contains("while") ){
            try{ return _doStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("enum") ){
            try{ return _enum.of(all); } catch(Exception e){}
        }
        if( all.startsWith("interface") ){
            try{ return _interface.of(all); } catch(Exception e){}
        }
        if( all.startsWith("//") ){
            try{ return _lineComment.of(all); } catch(Exception e){}
        }
        if( all.startsWith("return")){
            try{ return _returnStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("\"") ){
            if( all.startsWith("\"\"\"")) {
                try { return _textBlockExpr.of(all); } catch (Exception e) { }
            } else{
                return _stringExpr.of(all);
            }
        }
        if( all.startsWith("synchronized") ){
            try{ return _synchronizedStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("switch") ){
            if( all.contains("yield") ){
                try{ return _switchExpr.of(all); } catch(Exception e){}
            }
            try{ return _switchStmt.of(all); } catch(Exception e){}
        }
        if( all.startsWith("while") ){
            try{ return _whileStmt.of(all); } catch(Exception e){}
        }
        //ends with
        if( all.endsWith(".this") ){
            try{ return _thisExpr.of(all); } catch(Exception e){}
        }
        if( _modifier.of(all) != null){
            return _modifier.of(all);
        }
        if( Arrays.stream(_unaryExpr.PREFIX_OPERATORS).anyMatch(o -> all.startsWith(o.asString())) ){
            try{ return _unaryExpr.of(all); }catch(Exception e){}
        }

        //numbers
        if( Character.isDigit( all.charAt(0) ) || all.charAt(0) =='-' || all.charAt(0)=='+' ){
            if( all.contains(".") || all.endsWith("D") || all.endsWith("d") || all.endsWith("F") || all.endsWith("f") || all.contains("E") || all.contains("e")){
                try{ return _doubleExpr.of(all); }catch(Exception e){ }
            }
            if( ! (all.endsWith("l")|| all.endsWith("L") )){
                try{ return _intExpr.of(all); }catch(Exception e){ }
            }
            try{ return _longExpr.of(all); }catch(Exception e){ }
        }
        if( all.startsWith("0x") || all.startsWith("0X") || all.startsWith("0b") || all.startsWith("0B")){
            if( ! (all.endsWith("l")|| all.endsWith("L") )){
                try{ return _intExpr.of(all); }catch(Exception e){ }
            }
            try{ return _longExpr.of(all); }catch(Exception e){ }
        }
        if( all.startsWith(".")){
            try{ return _doubleExpr.of(all); }catch(Exception e){ }
        }

        //ends with
        if( Arrays.stream(_unaryExpr.POSTFIX_OPERATORS).anyMatch(o -> all.endsWith(o.asString())) ){
            return _unaryExpr.of(all);
        }
        if( all.endsWith(".class") ){
            try{ return _classExpr.of(all); }catch(Exception e){ }
        }
        if( all.endsWith("]")){
            try{ return _arrayAccessExpr.of(all); }catch(Exception e){ }
        }

        //statements
        if( all.endsWith(";")){
            try{ return _stmt.of(all); }catch(Exception e){ }
        }
        //methods/constructors
        if( all.endsWith("}") ){
            try{ return _method.of(all); }catch(Exception e){ }
            try{ return _constructor.of(all); }catch(Exception e){ }
        }


        //If we get here, just hail mary pass to expression
        //expression?
        _expr _e = _expr.of(all);
        return _e;
        /*
        if( all.contains("=") || all.contains("+")){

        }
         */
        //starts with any unaryExpr prefix operators
        //UnaryExpr.Operator.values()
        //ends with any unaryExpr postfix operators
        //UnaryExpr.Operator.values()

        //throw new _jdraftException("could not build a _node from code :"+System.lineSeparator()+all);
    }

    /**
     * Marker interface for ALL models and interfaces related to Java Language Constructs
     *
     * @see _tree._node a one-to-one mapping between an AST (Node) and a <CODE>_javaDomain</CODE> ( _method <--> MethodDeclaration )
     * @see _tree._orderedGroup a one-to 0 or more ordered NodeList instances of AST nodes ( _parameters <--> NodeList<Parameter> )
     * @see _tree._group a one-to 0 or more NodeList instances of AST nodes ( _modifiers <--> NodeList<Parameter> )
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
        if( nodeClass == _variable.class){
            return _variable.of(code).node();
        }
        if( nodeClass == _switchCase.class){
            return _switchCase.of(code).node();
        }
        if( nodeClass == _package.class){
            return _package.of(Text.combine( code)).node();
        }
        if( nodeClass == _typeParam.class){
            return _typeParam.of(Text.combine(code)).node();
        }
        if ( _stmt.class.isAssignableFrom(nodeClass)){
            return Stmt.of(code);
        }
        if ( _expr.class.isAssignableFrom(nodeClass)){
            return Expr.of(code);
        }
        if (_import.class == nodeClass) {
            return importDeclaration(Text.combine(code) );
        }
        if (_anno.class == nodeClass) {
            return annotationExpr(code);
        }
        if (_method.class == nodeClass) {
            return methodDeclaration(code);
        }
        if (_constructor.class == nodeClass) {
            return constructorDeclaration(code);
        }
        if (_typeRef.class == nodeClass) {
            return Types.of(Text.combine(code).trim());
        }
        if (_initBlock.class == nodeClass) {
            return staticBlock(code);
        }
        if (_type.class.isAssignableFrom(nodeClass)) {
            return Ast.typeDeclaration(code);
        }
        if (_param.class == nodeClass) {
            return parameter(code);
        }
        if (_receiverParam.class == nodeClass) {
            return receiverParameter(Text.combine(code));
        }
        /*
        if (_variable.class == nodeClass) {
            return field(code);
        }
         */
        if (_field.class == nodeClass) {
            return fieldDeclaration(code);
        }
        if (_constant.class == nodeClass) {
            return constantDeclaration(code);
        }
        if (_annoMember.class == nodeClass) {
            return annotationMemberDeclaration(code);
        }
        if (_method.class == nodeClass) {
            return methodDeclaration(code);
        }
        throw new _jdraftException("Could not parse Node " + nodeClass);
    }

    /**
     * return the _jdraft _node for the
     * @param node
     * @return
     */
    static _tree._node node(Node node){
        return (_tree._node)of( node);
    }

    /**
     * Builds the appropriate _node entities based on AST Nodes provided
     * <PRE>
     * handles:
     * all {@link _type}s:
     * {@link _annotation}, {@link _class}, {@link _enum}, {@link _interface}
     * {@link _anno}
     * {@link _annoMember}
     * {@link _constructor}
     * {@link _constant}
     * {@link _field}
     * {@link _javadocComment}
     * {@link _method}
     * {@link _param}
     * {@link _receiverParam}
     * {@link _initBlock}
     * {@link _typeParam}
     * {@link _typeRef}
     * </PRE>
     *
     * @param astNode the ast node
     * @return the _model entity
     */
    static _java._domain of(Node astNode) {
        if( astNode instanceof Expression ){
            return _expr.of( (Expression)astNode);
        }
        if( astNode instanceof Statement ){
            return _stmt.of( (Statement)astNode);
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
        /* is expr
        if (astNode instanceof AnnotationExpr) {
            return _annoExpr.of((AnnotationExpr) astNode);
        }
         */
        if( astNode instanceof PackageDeclaration ){
            return _package.of( (PackageDeclaration)astNode);
        }
        if (astNode instanceof AnnotationDeclaration) {
            return _annotation.of((AnnotationDeclaration) astNode);
        }
        if (astNode instanceof AnnotationMemberDeclaration) {
            return _annoMember.of((AnnotationMemberDeclaration) astNode);
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
            return _lambdaExpr.of((LambdaExpr) astNode);
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
        /*
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
        */
        if( astNode instanceof Comment){
            if (astNode instanceof JavadocComment) {
                JavadocComment jdc = (JavadocComment) astNode;
                return _javadocComment.of(jdc);
            }
            if (astNode instanceof LineComment) {
                LineComment c = (LineComment) astNode;
                return _lineComment.of(c);
            }
            BlockComment c = (BlockComment) astNode;
            return _blockComment.of(c);
            /*
            if (jdc.getParentNode().isPresent()) {
                Node parent =
                return _javadocComment.of((NodeWithJavadoc) jdc.getParentNode().get());
            }
            throw new _jdraftException("No Parent provided for JavadocComment");
             */
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
            return _switchCase.of( (SwitchEntry)astNode);
        }
        if( astNode instanceof com.github.javaparser.ast.Modifier){
            return _modifier.of( (Modifier)astNode);
        }
        if (astNode instanceof Parameter) {
            return _param.of((Parameter) astNode);
        }
        if (astNode instanceof InitializerDeclaration) {
            InitializerDeclaration id = (InitializerDeclaration) astNode;
            return _initBlock.of(id);
        }
        if (astNode instanceof ReceiverParameter) {
            ReceiverParameter rp = (ReceiverParameter) astNode;
            return _receiverParam.of(rp);
        }
        if (astNode instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) astNode;
            return _typeParam.of(tp);
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
        if( astNode instanceof ModuleDirective ){
            return _moduleDirective.of( (ModuleDirective)astNode);
        }
        if( astNode instanceof ModuleRequiresDirective ){
            return _moduleRequires.of( (ModuleRequiresDirective)astNode);
        }
        if( astNode instanceof MemberValuePair){
            return _annoEntryPair.of( (MemberValuePair)astNode);
        }
        throw new _jdraftException("Unable to create _java entity from " + astNode+" "+astNode.getClass());
    }


    /**
     * Mappings from JavaParser AST models (i.e. {@link AnnotationExpr}) 
     * ...to jdraft _java models (i.e. {@link _anno})
     */
    class Classes {

        /*
            Easy access to the draft _java classes and interfaces that represent
            entities... his allows convenient autocomplete when you do a _walk.in
            of _walk.list, etc.
            <PRE>
            Tree.list( _c, _java.THROWS );
            Tree.list( _c, _java.THROWS );
            </PRE>

            to make it similar (in feel) to Ast.* :
            Tree.in( _c, Ast.INITIALIZER_DECLARATION,
                id-> id.getBody().add(Stmt.of("System.out.println(1);"));
            Tree.list( _m, Ast.RETURN_STMT );
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
        Class<_annoMember> ELEMENT = _annoMember.class;

        Class<_body> BODY = _body.class;
        /** an annotation use i.e. @Deprecated */
        Class<_anno> ANNO = _anno.class;
        /** group of annotation usages  on a single entity */
        Class<_annos> ANNOS = _annos.class;
        Class<_import> IMPORT = _import.class;
        Class<_imports> IMPORTS = _imports.class;

        Class<_javadocComment> JAVADOC_COMMENT = _javadocComment.class;
        Class<_lineComment> LINE_COMMENT = _lineComment.class;
        Class<_blockComment> BLOCK_COMMENT = _blockComment.class;

        Class<_modifiers> MODIFIERS = _modifiers.class;
        Class<_param> PARAMETER = _param.class;
        Class<_params> PARAMETERS = _params.class;
        Class<_typeParam> TYPE_PARAMETER = _typeParam.class;
        Class<_typeParams> TYPE_PARAMETERS = _typeParams.class;
        Class<_receiverParam> RECEIVER_PARAMETER = _receiverParam.class;
        Class<_initBlock> STATIC_BLOCK = _initBlock.class;
        Class<_throws> THROWS = _throws.class;
        Class<_typeRef> TYPEREF = _typeRef.class;

        /**
         * The classes below are categorical interfaces that are applied to classes
         */
        Class<_declared> MEMBER = _declared.class;
        Class<_withName> NAMED = _withName.class;
        Class<_withNameType> NAMED_TYPE = _withNameType.class;

        Class<_withThrows> HAS_THROWS = _withThrows.class;
        Class<_withBody> HAS_BODY = _withBody.class;
        Class<_withAnnos> HAS_ANNOS = _withAnnos.class;
        Class<_withConstructors> HAS_CONSTRUCTORS = _withConstructors.class;
        Class<_withJavadoc> HAS_JAVADOC = _withJavadoc.class;
        Class<_withMethods> HAS_METHODS = _withMethods.class;
        Class<_withModifiers> HAS_MODIFIERS = _withModifiers.class;
        Class<_params._withParams> HAS_PARAMETERS = _params._withParams.class;
        Class<_withReceiverParam> HAS_RECEIVER_PARAMETER = _withReceiverParam.class;
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
    }

    /**
     * A {@link _member} defined within a {@link _type} (that is callable/referenceable/reachable) from the outside
     * it can be associated with a larger entity or context)
     * NOTE: each {@link _declared} maps directly to:
     * <UL>
     *     <LI>an AST representation {@link Node}
     * </UL>
     * <UL>
     * <LI>{@link _field} {@link FieldDeclaration}
     * <LI>{@link _constructor} {@link ConstructorDeclaration}
     * <LI>{@link _method} {@link MethodDeclaration}
     * <LI>{@link _constant} {@link EnumConstantDeclaration}
     * <LI>{@link _annoMember} {@link AnnotationMemberDeclaration}
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
    interface _declared<N extends Node, _D extends _withName & _withAnnos & _withJavadoc & _withComments>
            extends _member<N, _D>, _withName<_D>, _withAnnos<_D>, _withJavadoc<_D>, _withComments<N, _D>  {

        @Override
        default _javadocComment getJavadoc() {
            NodeWithJavadoc t = (NodeWithJavadoc) this.node();
            if( t.getJavadocComment().isPresent()){
                return _javadocComment.of( (JavadocComment)t.getJavadocComment().get() );
            }
            return null;
        }

        /**
         *
         * @return
         */
        default _D removeJavadoc() {
            ((NodeWithJavadoc) this.node()).removeJavaDocComment();
            return (_D) this;
        }

        @Override
        default boolean hasJavadoc() {
            return ((NodeWithJavadoc) this.node()).getJavadoc().isPresent();
        }
    }

    /**
     * A member within the body of a Class (something defined in the  { }) including {@link _initBlock}s.
     * All _{@link _member}s (they are represented by BOTH a meta-representation i.e. {@link _method},
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
     * <LI>{@link _annoMember} {@link AnnotationMemberDeclaration}
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
    interface _member<N extends Node, _N extends _withComments>
            extends _withComments<N, _N>, _tree._node<N, _N> {

        /**
         * Given the "container class" and a String representing the member, build and return
         * the type
         * @param containerClass
         * @param code
         * @return
         */
        static _member of( Class<? extends _member> containerClass, String code){
            if( containerClass.isAssignableFrom(_type.class)) {
                if (containerClass == _class.class) {
                    _class _c = _class.of("class UNKNOWN{ " + System.lineSeparator() + code + "}");
                    _member _m = _c.listMembers().get(0);
                    _m.node().remove();
                    return _m;
                }
                if (containerClass == _annotation.class) {
                    _enum _c = _enum.of("@interface UNKNOWN{ " + System.lineSeparator() + code + "}");
                    _member _m = _c.listMembers().get(0);
                    _m.node().remove();
                    return _m;
                }
                if (containerClass == _enum.class) {
                    _enum _c = _enum.of("enum UNKNOWN{ " + System.lineSeparator() + code + "}");
                    _member _m = _c.listMembers().get(0);
                    _m.node().remove();
                    return _m;
                }
                if (containerClass == _interface.class) {
                    _interface _c = _interface.of("interface UNKNOWN{ " + System.lineSeparator() + code + "}");
                    _member _m = _c.listMembers().get(0);
                    _m.node().remove();
                    return _m;
                }
            }

            if( containerClass == _constant.class){
                _constant _cs = _constant.of("A(){"+ System.lineSeparator()+ code + "}");
                _member _m = _cs.listMembers().get(0);
                _m.node().remove();
                return _m;
            }
            throw new _jdraftException("cannot create member for "+ containerClass );
            /*
            if( containerClass == _moduleInfo.class ){
                _moduleInfo _mi = _moduleInfo.of("UNKNOWN{"+ System.lineSeparator() + code + "}");
                ModuleDirective md = _mi.listAstModuleDirectives().get(0);
                _member _m = _mi.ast().listMembers().get(0);
                _m.ast().remove();
                return _m;
            }
             */
        }
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
                BodyDeclaration bd = Walk.first(Walk.PARENTS, fd, BodyDeclaration.class);
                if( bd != null ) {
                    return (_M) _java.of(bd);
                }
                return null; //we didnt find a parent that was a BodyDeclaration
            } else{
                BodyDeclaration bd = Walk.first(Walk.PARENTS, node(), BodyDeclaration.class);
                if( bd != null ) {
                    return (_M) _java.of(bd);
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
    interface _withComments <N extends Node, _N extends _tree._node & _withComments> extends _tree._node<N, _N> {

        default boolean hasComment(){
             return getComment() != null;
        }

        default boolean hasComment(boolean hasComment){
            if( hasComment ){
                return hasComment();
            }
            return !hasComment();
        }

        /**
         * removes the attributed comment
         * @return the modified _node
         */
        default _N removeComment(){
            node().removeComment();
            return (_N)this;
        }

        /**
         * sets the attributed comment to this _node and returns the modified node
         * @param comment the text of the comment
         * @return
         */
        default _N setComment(String...comment){
            N n = node();
            n.setComment((Comment)_comment.of(comment).node());
            return (_N)this;
        }

        /**
         * sets the attributed comment to this _node and returns the modified node
         * @param _c the
         * @return
         */
        default <_C extends _comment> _N setComment(_C _c){
            N n = node();
            n.setComment((Comment)_c.node());
            return (_N)this;
        }

        /**
         * Gets the "attributed" comment on the node (or null if there is no comment on this node)
         * @param <_C> the comment type
         * @return the attributed comment or null if there is no comment attributed to this node
         */
        default <_C extends _comment> _C getComment(){
            N n = node();
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
        default _N toAllComments(Consumer<_comment> actionFn){
            return toAllComments(t-> true, actionFn);
        }

        /**
         *
         * @param matchFn
         * @param actionFn
         * @return
         */
        default _N toAllComments(Predicate<_comment> matchFn, Consumer<_comment> actionFn){
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

        /**
         *
         * @return
         */
        default List<_lineComment> listAllLineComments(){
            return listAllComments(c-> c instanceof _lineComment).stream().map(c -> (_lineComment)c).collect(Collectors.toList());
        }

        /**
         *
         * @return
         */
        default List<_javadocComment> listAllJavadocComments(){
            return listAllComments(c-> c instanceof _javadocComment).stream().map(c -> (_javadocComment)c).collect(Collectors.toList());
        }
    }

    /**
     * a Node entity that "holds"/or contains textual data,
     *
     * this interface provides a common way to inspect, and manipulate the textual contents
     * of these nodes
     *
     * @see _stringExpr
     * @see _textBlockExpr
     * @see _comment
     * @see _lineComment
     * @see _blockComment
     * @see _javadocComment
     *
     * @param <_WT>
     */
    interface _withText<_WT extends _withText> extends _java._domain {

        /**
         * Gets the textual contents of the Node
         * @return
         */
        String getText();

        /**
         * Sets the text content
         * @param text the text to be set on the entity
         * @return
         */
        _WT setText( String text );

        /**
         * does the textual content of this node match the Predicate?
         * @param textMatchFn predicate function
         * @return true if the text contents of this node matches
         */
        default boolean isText(Predicate<String> textMatchFn ) {
            return textMatchFn.test(getText());
        }

        /**
         * Does the Text match the Stencil
         * @param stencil
         * @return
         */
        default boolean isText(Stencil stencil){
            return parseText(stencil) != null;
        }

        /**
         * Parses the text using the stencil and returns the Tokens (or null if the text cannot be parsed by the Stencil)
         * @param stencil the stencil
         * @return the Tokens parsed from the Stencil or null if the parse failed for the text
         */
        default Tokens parseText(Stencil stencil){
            return stencil.parse(getText());
        }

        /**
         *
         * @param text
         * @return
         */
        default boolean isText(String text){
            return Objects.equals( getText(), text);
        }

        /**
         * Does this entity with text contain this charSequence
         * @param containsString
         * @return
         */
        default boolean contains(CharSequence containsString){
            return getText().contains(containsString);
        }

        /**
         *
         * @param startsWith
         * @return
         */
        default boolean startsWith(String startsWith){
            return this.getText().startsWith(startsWith);
        }

        /**
         *
         * @param endsWith
         * @return
         */
        default boolean endsWith(String endsWith){
            return this.getText().endsWith(endsWith);
        }

        /**
         *
         */
        default _WT replace(char target, char replacement){
            this.setText( this.getText().replace(target, replacement) );
            return (_WT)this;
        }

        /**
         *
         * @param target
         * @param replacement
         * @return
         */
        default _WT replace(String target, String replacement){
            this.setText( this.getText().replace(target, replacement) );
            return (_WT)this;
        }

        /**
         * Look for matches to matchStencil in the contents of the comment and replace with the replaceStencil
         * for example:
         * <PRE>
         * _comment _c = _comment.of("//<code>System.out.println(getValue());</code>");
         * _c.matchReplace("<code>$content$</code>", "{@code $content$}");
         *
         * //verify we matched the old <code></code> tags to {@code}
         * assertEquals( "{@code System.out.println}", _c.getContents());
         * </PRE>
         * @param matchStencil stencil for matching input pattern
         * @param replaceStencil stencil for drafting the replacement
         * @return the modified _withText node
         * @see Stencil
         */
        default _WT matchReplace(String matchStencil, String replaceStencil){
            return matchReplace(Stencil.of(matchStencil), Stencil.of(replaceStencil));
        }

        /**
         * Look for matches to matchStencil in the contents of the comment and replace with the replaceStencil
         * for example:
         * <PRE>
         * Stencil matchStencil = Stencil.of("<code>$content$</code>");
         * Stencil replaceStencil = Stencil.of("{@code $content$}");
         * _comment _c = _comment.of("//<code>System.out.println(getValue());</code>");
         * _c.matchReplace(matchStencil, replaceStencil);
         *
         * //verify we matched the old <code></code> tags to {@code}
         * assertEquals( "{@code System.out.println}", _c.getContents());
         * </PRE>
         * @param matchStencil stencil for matching input pattern
         * @param replaceStencil stencil for drafting the replacement
         * @return the modified _withText node
         */
        default _WT matchReplace(Stencil matchStencil, Stencil replaceStencil){
            setText(Stencil.matchReplace( getText(), matchStencil, replaceStencil));
            return (_WT)this;
        }
    }

    /**
     * Named java entities
     * {@link _type} {@link _class} {@link _enum} {@link _interface} {@link _annotation}
     * {@link _method}
     * {@link _field}
     * {@link _param}
     * {@link _anno}
     * {@link _constant}
     * {@link _annoMember}
     * {@link _typeParam}
     *
     * {@link _methodCallExpr}
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
            Stencil st = Stencil.of(name);
            if( st.isFixedText() ){
                return Objects.equals(getName(), name);
            }
            return st.matches(getName());
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
     *     <LI>{@link _param}
     *     <LI>{@link _method}
     *     <LI>{@link _annoMember}
     *     <LI>{@link _receiverParam}
     *     <LI>{@link _variable}
     * </UL>
     * @param <_NT> the specialized entity that is a named TYPE
     */
    interface _withNameType<N extends Node, _NT extends _withNameType>
            extends _tree._node<N, _NT>, _withName<_NT>, _typeRef._withType<N,_NT> {
    }

    /**
     *
     * @param <N>
     * @param <_WS>
     *
     * @see _fieldAccessExpr
     * @see _methodCallExpr
     * @see _methodRefExpr
     * @see _newExpr
     */
    interface _withScope<N extends Node, _WS extends _tree._node> extends _tree._node<N, _WS> {

        default boolean hasScope(){
            return ((NodeWithOptionalScope) node()).getScope().isPresent();
        }

        default boolean isScope(String...expr){
            if( ((NodeWithOptionalScope) node()).getScope().isPresent()){
                return Expr.equal( (Expression)((NodeWithOptionalScope) node()).getScope().get(), Expr.of(expr));
            }
            return false;
        }

        default boolean isScope(Expression e){
            if( ((NodeWithOptionalScope) node()).getScope().isPresent()){
                return Expr.equal( (Expression) ((NodeWithOptionalScope) node()).getScope().get(), e);
            }
            return e == null;
        }

        default boolean isScope(_expr _e){
            if( ((NodeWithOptionalScope) node()).getScope().isPresent()){
                return Expr.equal( (Expression) ((NodeWithOptionalScope) node()).getScope().get(), _e.node());
            }
            return _e == null;
        }

        default _WS removeScope(){
            ((NodeWithOptionalScope) node()).removeScope();
            return (_WS)this;
        }

        default _WS setScope(String scope ){
            ((NodeWithOptionalScope) node()).setScope(Expr.of(scope));
            return (_WS)this;
        }

        default _WS setScope(_expr _e){
            ((NodeWithOptionalScope) node()).setScope(_e.node());
            return (_WS)this;
        }

        default _WS setScope(Expression e){
            ((NodeWithOptionalScope) node()).setScope(e);
            return (_WS)this;
        }

        default _WS setScope(String... scope){
            return setScope( Expr.of(scope));
        }

        default _expr getScope(){
            if( NodeWithScope.class.isAssignableFrom(node().getClass())){
                return _expr.of( ((NodeWithScope) node()).getScope());
            }
            if( ((NodeWithOptionalScope) node()).getScope().isPresent()){
                return _expr.of( (Expression)
                        ((NodeWithOptionalScope) node()).getScope().get());
            }
            return null;
        }
    }

    /**
     * A Java node containing a condition
     *
     * @param <N> the Ast Node type
     * @param <_WC> the java node type
     *
     * @see _ifStmt
     * @see _ternaryExpr
     * @see _doStmt
     * @see _whileStmt
     */
    interface _withCondition <N extends Node, _WC extends _tree._node> extends _tree._node<N, _WC> {

        /**
         * Is the condition expression an instance of this exprImplClass?
         * @param exprImplClass the expression implementation class
         * @param <_CE> the condition expression implementation class
         * @return
         */
        default <_CE extends _expr> boolean isCondition(Class<_CE> exprImplClass){
            _expr _e = getCondition();
            if( _e != null){
                return exprImplClass.isAssignableFrom(_e.getClass());
            }
            return false;
        }

        default <_CE extends _expr> boolean isCondition(Class<_CE> exprImplClass, Predicate<_CE> _matchfn){
            _expr _e = getCondition();
            if( _e != null){
                if( exprImplClass.isAssignableFrom(_e.getClass())){
                    return _matchfn.test( (_CE)_e);
                }
            }
            return false;
        }

        default boolean isCondition(String...expression){
            try{
                return isCondition(Expr.of(expression));
            }catch(Exception e){
                return false;
            }
        }

        default boolean isCondition(_expr _ex){
            return Expr.equal(  this.getCondition().node(), _ex.node());
        }

        default boolean isCondition(Expression ex){
            return Expr.equal( this.getCondition().node(), ex);
        }

        default boolean isCondition(Predicate<_expr> matchFn){
            return matchFn.test(getCondition());
        }

        default _WC setCondition(String...expression){
            return setCondition(Expr.of(expression));
        }

        default _WC setCondition(_expr e){
            return setCondition(e.node());
        }

        default _WC setCondition(Expression e){
            ((NodeWithExpression) node()).setExpression(e);
            return (_WC)this;
        }

        default _expr getCondition(){
            return _expr.of(((NodeWithExpression) node()).getExpression());
        }
    }

    /**
     * Container of an {@link Expression} / {@link _expr}
     * @param <N>
     * @param <_WE>
     */
    interface _withExpression <N extends Node, _WE extends _tree._node> extends _tree._node<N, _WE> {

        default boolean isExpression(String...expression){
            try{
                return isExpression(Expr.of(expression));
            }catch(Exception e){
                return false;
            }
        }

        default <_EI extends _expr> boolean isExpression(Class<_EI> exprImpl){
            if( this.getExpression() != null && exprImpl.isAssignableFrom(this.getExpression().getClass())){
                return true;
            }
            return false;
        }

        default <_EI extends _expr> boolean isExpression(Class<_EI> exprImpl, Predicate<_EI> matchFn){
            if( this.getExpression() != null && exprImpl.isAssignableFrom(this.getExpression().getClass())){
                return matchFn.test( (_EI)this.getExpression() );
            }
            return false;
        }

        default boolean isExpression(_expr _ex){
            return Objects.equals( getExpression(), _ex);
            //return Expr.equal( this.getExpression().ast(), _ex.ast());
        }

        default boolean isExpression(Expression ex){
            return Expr.equal( this.getExpression().node(), ex);
        }

        default boolean isExpression(Predicate<_expr> matchFn){
            return matchFn.test(getExpression());
        }

        default boolean isExpression( int i){
            return isExpression( Expr.of(i) );
        }

        default boolean isExpression( boolean b){
            return isExpression( Expr.of(b) );
        }

        default boolean isExpression( float f){
            return isExpression( Expr.of(f) );
        }

        default boolean isExpression( long l){
            return isExpression( Expr.of(l) );
        }

        default boolean isExpression( double d){
            return isExpression( Expr.of(d) );
        }

        default boolean isExpression( char c){
            return isExpression( Expr.of(c) );
        }

        default boolean isExpression( int... i){
            return isExpression( Expr.arrayInitializerExpr(i) );
        }

        default boolean isExpression( boolean... b){
            return isExpression( Expr.arrayInitializerExpr(b) );
        }

        default boolean isExpression( float... f){
            return isExpression( Expr.arrayInitializerExpr(f) );
        }

        default boolean isExpression( long... l){
            return isExpression( Expr.arrayInitializerExpr(l) );
        }

        default boolean isExpression( double... d){
            return isExpression( Expr.arrayInitializerExpr(d) );
        }

        default boolean isExpression( char... c){
            return isExpression( Expr.arrayInitializerExpr(c) );
        }

        default _WE setExpression(String...expression){
            return setExpression(Expr.of(expression));
        }

        default _WE setExpression(_expr e){
            return setExpression(e.node());
        }

        default _WE setExpression(Expression e){
            ((NodeWithExpression) node()).setExpression(e);
            return (_WE)this;
        }

        default _expr getExpression(){
            return _expr.of(((NodeWithExpression) node()).getExpression());
        }
    }

    /**
     * Sort the list of nodes by position and return the sorted list
     * NOTE: positions can lie, especially if the AST is modified after
     *
     * @param nodes
     */
    static List<_tree._node> sort(List<_tree._node> nodes){
        Collections.sort(nodes, new _nodeStartPositionComparator());
        return nodes;
    }

    /**
     * Comparator for Nodes within an AST node that organizes based on the
     * start position.
     */
    class _nodeStartPositionComparator implements Comparator<_tree._node> {

        @Override
        public int compare(_tree._node o1, _tree._node o2) {
            if (o1.node().getBegin().isPresent() && o2.node().getBegin().isPresent()) {
                int comp = o1.node().getBegin().get().compareTo(o2.node().getBegin().get());
                if( comp != 0 ){
                    return comp;
                }
                int comp2 = o1.node().getEnd().get().compareTo(o2.node().getEnd().get());
                return comp2;
            }
            //if one or the other doesnt have a begin
            // put the one WITHOUT a being BEFORE the other
            // if neither have a being, return
            if (!o1.node().getBegin().isPresent() && !o2.node().getBegin().isPresent()) {
                return 0;
            }
            if (o1.node().getBegin().isPresent()) {
                return -1;
            }
            return 1;
        }
    }

    interface _char extends _codeToken {
        char character();
    }

    enum _digit implements _char{
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9);

        private int val;

        _digit(int val){
            this.val = val;
        }

        public int value(){
            return val;
        }

        public char character(){
            return (char)(val+'0');
        }
    }

    enum _lowerAlpha implements _char{
        a('a'), b('b'), c('c'), d('d'), e('e'), f('f'), g('g'), h('h'), i('i'),
        j('j'), k('k'), l('l'), m('m'), n('n'), o('o'), p('p'), q('q'), r('r'),
        s('s'), t('t'), u('u'), v('v'), w('w'), x('x'), y('y'), z('z');

        private final char ch;

        _lowerAlpha(char ch){
            this.ch = ch;
        }

        public char character(){
            return ch;
        }

        public static _lowerAlpha get(char ch){
            Optional<_lowerAlpha> os = Stream.of(values()).filter(sc -> sc.ch == ch).findFirst();
            if( os.isPresent() ){
                return os.get();
            }
            return null;
        }
    }

    /*
    enum _upperAlpha implements _char{
        A('A'), B('B'), C('C'), D('D'), E('E'), F('F'), G('G'), H('H'), I('I'),
        J('J'), K('K'), L('L'), M('M'), N('N'), O('O'), P('P'), Q('Q'), R('R'),
        S('S'), T('T'), U('U'), V('V'), W('W'), X('X'), Y('Y'), Z('Z');

        private final char ch;

        _upperAlpha(char ch){
            this.ch = ch;
        }

        public char character(){
            return ch;
        }

        public static _upperAlpha get(char ch){
            Optional<_upperAlpha> os = Stream.of(values()).filter(sc -> sc.ch == ch).findFirst();
            if( os.isPresent() ){
                return os.get();
            }
            return null;
        }
    }
     */


    /**
     * When we read in the code String for tokenization/lexing,
     * we need an interface that roundly explains what KIND on code token we have
     *
     *
     * // a StringLiteralBuffer when encounter an open "
     * (I peek ahead to make sure its not a textBlockBuffer, also check last token to make sure
     * // it's not a \, it will know to end, when it encounters a ENDING " NOT preceded by a \)
     * // a TextBlockBuffer when I encounter the ends when I encounter """
     * // a Whitespace buffer contains contiguous whitespace
     * // a keywordBuffer : it will collect lowercase, it will
     * // a textBuffer :
     * specifically:
     * <UL>
     *     <LI>whitespace</LI>
     *     <LI></LI>
     * </UL>
     * <LI>
     *
     * </LI>
     */
    interface _codeToken {

    }


    /**
     * "Special" characters that can be used by the tokenizer in breaking up Strings / categorizing things
     */
    enum _specialChar implements _char, _codeToken {
        //the characters # _ $

        TILDE('~'), BANG('!'), AT('@'), PERCENT('%'), CARET('^'), AND('&'), STAR('*'),
        LPAREN('('), RPAREN(')'), DASH('-'), EQUAL('='), PLUS('+'), SLASH('\\'), PIPE('|'),
        QUOTE('\''), OBRACKET('{'), CBRACKET('}'), OBRACE('['), CBRACE(']'), SEMI(';'),COLON(':'),
        DBL_QUOTE('\"'), COMMA(','), LT('<'), GT('>'), BACKSLASH('/'), QUESTIONMARK('?');

        private final char c;

        _specialChar(char c){
            this.c = c;
        }

        public static _specialChar get(char c){
            Optional<_specialChar> os = Stream.of(values()).filter(sc -> sc.c == c).findFirst();
            if( os.isPresent() ){
                return os.get();
            }
            return null;
        }

        public char character(){
            return c;
        }
    }

    class _whitespace implements _codeToken {

        private final StringBuilder str = new StringBuilder();

        public _whitespace(){
        }

        private _whitespace(String s ){
            str.append(s);
        }

        public void add( char c){
            str.append(c);
        }

        public _whitespace drain(){
            if( str.length() == 0 ){
                return null;
            }
            String st = str.toString();
            str.delete(0, str.length());
            return new _whitespace(st);
        }
        public String toString(){
            return " ";
        }
    }

    //TriBuffer idea
    //
    //so we have:
    // (1) Tokens that contains codeTokens (this is where we write to when we switch between the (3) buffers)
    //
    // (3) buffers
    class _tokenizer{

        /**
         * TODO maintain a buffer of lowercase chars, & when you hit a whitespace or special character
         * interpret the buffer to see if the buffer represents a keyword, if so, add the keyword to the buffer
         * (rather than adding a separate pass)
         * @param str
         * @return
         */
        public static List<Object> tokenize(String str){
            List<Object>toks = new ArrayList<>();
            _whitespace whiteSpace = new _whitespace();
            String text = "";
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                switch(c){
                    case '~': case '!': case '@': case '%': case '^': case '&': case '*': case '(': case ')':
                    case '-': case '=': case '+': case '\\': case '|': case '\'': case '{': case '}': case '[':
                    case ']': case ';': case ':': case '\"': case ',': case '<': case '>': case '/': case '?':
                    {
                        _whitespace _ws  = whiteSpace.drain();
                        if(_ws != null ){
                            toks.add(_ws);
                        }
                        if(text.length() > 0){
                            toks.add(text);
                            text = "";
                        }
                        toks.add(_specialChar.get(c));
                        break;
                    }
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i':
                    case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p': case 'q': case 'r':
                    case 's': case 't': case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':{
                        _whitespace _ws  = whiteSpace.drain();
                        if(_ws != null ){
                            toks.add(_ws);
                        }
                        if( text.length() > 0){
                            toks.add(text);
                            text = "";
                        }
                        toks.add(_lowerAlpha.get(c));
                        break;
                    }
                    default: {
                        if( Character.isWhitespace(c)){
                            whiteSpace.add(c);
                        } else {
                            _whitespace _ws = whiteSpace.drain();
                            if (_ws != null) {
                                toks.add(_ws);
                            }
                            text = text + c;
                        }
                    }
                }
            }
            _whitespace _ws  = whiteSpace.drain();
            if(_ws != null ){
                toks.add(_ws);
            }
            if(text.length() > 0){
                toks.add(text);
            }
            return toks;
        }
    }

    /**
     * Reserved words in the Java language
     * these words have specific meaning and when parsed
     * @see _hintWord
     */
    enum _keyWord implements _codeToken {
        IF("if"), CLASS("class"), FOR("for"), PUBLIC("public"), PRIVATE("private"), PROTECTED("protected"),
        PACKAGE("package"), TRY("try"), STATIC("static"), THROWS("throws"), THROW("throw"), YIELD("yield"),
        WHILE("while"), IMPORT("import"), ASSERT("assert"), NEW("new"), BREAK("break"), THIS("this"),
        SUPER("super"), CASE("case"), CATCH("catch"), ABSTRACT("abstract"), BOOLEAN("boolean"),
        BYTE("byte"), CHAR("char"), CONST("const"), CONTINUE("continue"), DEFAULT("default"),
        DOUBLE("double"), DO("do"),ELSE("else"), ENUM("enum"),EXTENDS("extends"),FALSE("false"),
        FINAL("final"),FINALLY("finally"),FLOAT("float"),GOTO("goto"),IMPLEMENTS("implements"),
        INSTANCEOF("instanceof"),INT("int"),INTERFACE("interface"),LONG("long"), NATIVE("native"),
        NULL("null"), RETURN("return"),SHORT("short"),STRICTFP("strictfp"), SWITCH("switch"),
        SYNCHRONIZED("synchronized"), TRANSIENT("transient"),TRUE("true"),VOID("void"),VOLATILE("volatile");

        private final String text;

        //the sequence of loweralpha characters to match
        private final List<_lowerAlpha> sequence = new ArrayList<>();

        _keyWord(String s){
            this.text = s;
            for(int i=0;i<s.length();i++){
                sequence.add(_lowerAlpha.get(s.charAt(i)));
            }
        }

        public List<_lowerAlpha> getSequence(){
            return sequence;
        }


        public static List<Object> replace( List<Object> codeObjects){
            Stream.of( values() ).forEach( v -> v.replaceIn(codeObjects));
            return codeObjects;
        }

        /**
         *
         * @param str
         * @return
         */
        public static _keyWord get(String str){
            Optional<_keyWord> orw =
                    Stream.of(_keyWord.values()).filter(rw -> rw.text.equals(str)).findFirst();
            if( orw.isPresent() ){
                return orw.get();
            }
            return null;
        }

        public List<Object> replaceIn(List<Object> unbound){
            int idx = Collections.indexOfSubList(unbound, this.sequence);
            while( idx >= 0 ){
                for(int i=0; i< this.sequence.size(); i++){
                    unbound.remove(idx);
                }
                unbound.add(idx, this);
                idx = Collections.indexOfSubList(unbound, this.sequence);
            }
            return unbound;
        }
    }

    /**
     * NOT GLOBALLY enforced words like {@link _keyWord}
     * these words (when encountered) can give hints to the parser/lexer on what to do
     *
     * i.e.
     * i.e. you can do this (create a valid variable with the HintWord):
     * String String = "free";
     * String module = "blah";
     *
     * While with a keyword this wont work
     * String new = "blah";//wont compile
     * String int = "burger"; //wont compile
     *
     * @see _keyWord
     */
    enum _hintWord {
        MODULE("module"),
        EXPORTS("exports"),
        OPENS("opens"),
        USES("uses"),
        REQUIRES("requires"),
        PROVIDES("provides");

        private final String text;

        _hintWord(String s){
            this.text = s;
        }

        /**
         *
         * @param str
         * @return
         */
        public static _keyWord get(String str){
            Optional<_keyWord> orw =
                    Stream.of(_keyWord.values()).filter(rw -> rw.text.equals(str)).findFirst();
            if( orw.isPresent() ){
                return orw.get();
            }
            return null;
        }
    }
}
