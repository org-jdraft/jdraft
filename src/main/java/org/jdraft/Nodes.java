package org.jdraft;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumerates mapping from the JavaParser {@link Node}
 * to an underlying {@link _java._node}
 *
 * (includes some interfaces/abstract classes)
 *
 */
public enum Nodes {

      //_anno implementations are a single class in _jdraft, multiple implementation classes in JavaParser
      ANNO(_annoExpr.class, AnnotationExpr.class),
         ANNO_MARKER(_annoExpr.class, MarkerAnnotationExpr.class),
         ANNO_SINGLE(_annoExpr.class, SingleMemberAnnotationExpr.class),
         ANNO_KEYVALUE(_annoExpr.class, NormalAnnotationExpr.class),
      ANNO_ENTRY(_annotation._entry.class, AnnotationMemberDeclaration.class),
      ANNOTATION(_annotation.class, AnnotationDeclaration.class),
      ARRAY_ACCESS(_arrayAccessExpr.class, ArrayAccessExpr.class),
      ARRAY_CREATE(_arrayCreateExpr.class, ArrayCreationExpr.class),
      ARRAY_DIMENSION(_arrayDimension.class, ArrayCreationLevel.class),
      ARRAY_INITIALIZE(_arrayInitExpr.class, ArrayInitializerExpr.class),
      ASSERT_STATEMENT(_assertStmt.class, AssertStmt.class),
      ASSIGN(_assignExpr.class, AssignExpr.class),
      BINARY_EXPRESSION(_binaryExpr.class, BinaryExpr.class),
      BLOCK_COMMENT(_blockComment.class, BlockComment.class),
      BLOCK_STMT(_blockStmt.class, BlockStmt.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_body.class, VIRTUAL (a VIEW) the presence or absence of a code body
       BOOLEAN(_booleanExpr.class, BooleanLiteralExpr.class),
       BREAK_STMT(_breakStmt.class, BreakStmt.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_caseGroup.class, VIRTUAL (a VIEW) all cases and the corresponding code block
       CAST(_castExpr.class, CastExpr.class),
       CATCH(_catch.class, CatchClause.class),
       CHAR(_charExpr.class, CharLiteralExpr.class),
       CLASS(_class.class, ClassOrInterfaceDeclaration.class),
       CLASS_EXPRESSION(_classExpr.class, ClassExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_codeUnit.class, CompilationUnit.class); Hidden
       COMMENT(_comment.class, Comment.class), //INTERFACE
       CONDITIONAL_EXPRESSION(_ternaryExpr.class, ConditionalExpr.class),
       CONSTANT(_constant.class, EnumConstantDeclaration.class),
       CONSTRUCTOR(_constructor.class, ConstructorDeclaration.class),
       CONSTRUCTOR_CALL(_constructorCallStmt.class, ExplicitConstructorInvocationStmt.class),
       CONTINUE_STATEMENT(_continueStmt.class, ContinueStmt.class),
       DO_STATEMENT(_doStmt.class, DoStmt.class),
       DOUBLE(_doubleExpr.class, DoubleLiteralExpr.class),
       EMPTY_STATEMENT(_emptyStmt.class, EmptyStmt.class),
       ENCLOSED_EXPRESSION(_parenthesizedExpr.class, EnclosedExpr.class),

       ENUMERATION(_enum.class, EnumDeclaration.class),
       FIELD(_field.class, FieldDeclaration.class), //**
       FIELD_ACCESS(_fieldAccessExpr.class, FieldAccessExpr.class),
       FOR_EACH_STATEMENT(_forEachStmt.class, ForEachStmt.class),
       FOR_STATEMENT(_forStmt.class, ForStmt.class),
       IF_STATEMENT(_ifStmt.class, IfStmt.class),
       IMPORT(_import.class, ImportDeclaration.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_imports.class, ImportDeclaration.class);
       INIT_BLOCK(_initBlock.class, InitializerDeclaration.class),
       INSTANCE_OF(_instanceOfExpr.class, InstanceOfExpr.class),
       INT(_intExpr.class, IntegerLiteralExpr.class),

       INTERFACE(_interface.class, ClassOrInterfaceDeclaration.class),
       JAVADOC_COMMENT(_javadocComment.class, JavadocComment.class),
       LABELED_STATEMENT(_labeledStmt.class, LabeledStmt.class),
       LAMBDA(_lambdaExpr.class, LambdaExpr.class),
       LINE_COMMENT(_lineComment.class, LineComment.class),
       LOCAL_CLASS_STATEMENT(_localClassStmt.class, LocalClassDeclarationStmt.class),
       LOCAL_VARIABLES(_variablesExpr.class, VariableDeclarationExpr.class),
       LONG(_longExpr.class, LongLiteralExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_method.class
        METHOD(_method.class, MethodDeclaration.class),
        METHOD_CALL(_methodCallExpr.class, MethodCallExpr.class),
        METHOD_REFERENCE(_methodRefExpr.class, MethodReferenceExpr.class),
        MODIFIER(_modifier.class, Modifier .class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_modifiers.class
        MODULE_INFO(_moduleInfo.class, ModuleDeclaration.class),
        NAME(_name.class, SimpleName.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_name.class, SimpleName.class); ******
        NAME_EXPRESSION(_nameExpr.class, NameExpr.class),
        NEW(_newExpr.class, ObjectCreationExpr.class),
        NULL(_nullExpr.class, NullLiteralExpr.class),
        PACKAGE(_package.class, PackageDeclaration.class),
        PACKAGE_INFO(_packageInfo.class, CompilationUnit.class), /***/
        PARAMETER(_param.class, Parameter.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_parameters.class, Name.class);
        QUALIFIED_NAME(_qualifiedName.class, Name.class), /** VIRTUAL */
        RECEIVER_PARAMETER(_receiverParam.class, ReceiverParameter.class),
        RETURN_STMT(_returnStmt.class, ReturnStmt.class),
        STRING(_stringExpr.class, StringLiteralExpr.class),
        SUPER(_superExpr.class, SuperExpr.class),
        SWITCH_ENTRY(_switchEntry.class, SwitchEntry.class),
        SWITCH_EXPRESSION(_switchExpr.class, SwitchExpr.class),
        SWITCH_STATEMENT(_switchStmt.class, SwitchStmt.class),
        SYNCHRONIZED_STATEMENT(_synchronizedStmt.class, SynchronizedStmt.class),
        TEXT_BLOCK(_textBlockExpr.class, TextBlockLiteralExpr.class),
        THIS(_thisExpr.class, ThisExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_throws.class, /** VIRTUAL */

        THROW_STATEMENT(_throwStmt.class, ThrowStmt.class),
        TRY_STATEMENT(_tryStmt.class, TryStmt.class),
        TYPE(_type.class, TypeDeclaration.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_typeArguments.class VIRTUAL
         TYPE_EXPRESSION(_typeExpr.class, TypeExpr.class),
         TYPE_PARAMETER(_typeParam.class, TypeParameter.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_typeParameters.class VIRTUAL
         TYPE_REF(_typeRef.class, Type.class),
            ARRAY_TYPE(_typeRef.class, ArrayType.class),
            CLASS_OR_INTERFACE_TYPE(_typeRef.class, ClassOrInterfaceType.class),
            INTERSECTION_TYPE(_typeRef.class, IntersectionType.class),
            PRIMITIVE_TYPE(_typeRef.class, PrimitiveType.class),
            REFERENCE_TYPE(_typeRef.class, ReferenceType.class),
            UNION_TYPE(_typeRef.class, UnionType.class),
            VAR_TYPE(_typeRef.class, VarType.class),
            VOID_TYPE(_typeRef.class, VoidType.class),
            WILDCARD_TYPE(_typeRef.class, WildcardType.class),

         UNARY(_unaryExpr.class, UnaryExpr.class),
         VARIABLE(_variable.class, VariableDeclarator.class),
         WHILE_STATEMENT(_whileStmt.class, WhileStmt.class),
         YIELD_STATEMENT(_yieldStmt.class, YieldStmt.class);

    //virtual types / quasi virtual types
    //_body
    //_caseGroup
    //_name
    //_qualifiedName

    //_annos
    //_arguments
    //_imports
    //_modifiers
    //_parameters
    //_throws
    //_typeArguments
    //_typeParameters

    public final Class<? extends _java._node> _n;
    public final Class<? extends Node> n;

    Nodes(Class<? extends _java._node> _n, Class<? extends Node> n){
        this._n = _n;
        this.n = n;
    }


    public static Nodes of(Class clazz){
        Optional<Nodes> ncm = Arrays.stream(Nodes.values()).filter(n -> n._n == clazz || n.n == clazz).findFirst();
        if( ncm.isPresent() ){
            return ncm.get();
        }
        return null;
    }

    public static boolean contains( Class clazz ){
        return of( clazz) != null;
    }

    public static boolean is_node( Class clazz){
        return Arrays.stream(Nodes.values()).filter(n -> n._n == clazz).findFirst().isPresent();
    }

    public static boolean isNode( Class clazz){
        return Arrays.stream(Nodes.values()).filter(n -> n.n == clazz).findFirst().isPresent();
    }

    public static Class<? extends Node> node (Class _nodeClass){
        Optional<Nodes> ncm = Arrays.stream(Nodes.values()).filter(n -> n._n == _nodeClass).findFirst();
        if( ncm.isPresent() ){
            return ncm.get().n;
        }
        return null;
    }

    public static Class<? extends _java._node> _node(Class _nodeClass){
        Optional<Nodes> ncm = Arrays.stream(Nodes.values()).filter(n -> n.n == _nodeClass).findFirst();
        if( ncm.isPresent() ){
            return ncm.get()._n;
        }
        return null;
    }

}