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
         ANNO_EXPR_MARKER(_annoExpr.class, MarkerAnnotationExpr.class),
         ANNO_EXPR_SINGLE(_annoExpr.class, SingleMemberAnnotationExpr.class),
         ANNO_EXPR_KEYVALUE(_annoExpr.class, NormalAnnotationExpr.class),
      ANNO_ENTRY(_entry.class, AnnotationMemberDeclaration.class),
      ANNOTATION(_annotation.class, AnnotationDeclaration.class),
      ARRAY_ACCESS_EXPR(_arrayAccessExpr.class, ArrayAccessExpr.class),
      ARRAY_CREATE_EXPR(_arrayCreateExpr.class, ArrayCreationExpr.class),
      ARRAY_DIMENSION(_arrayDimension.class, ArrayCreationLevel.class),
      ARRAY_INIT_EXPR(_arrayInitExpr.class, ArrayInitializerExpr.class),
      ASSERT_STMT(_assertStmt.class, AssertStmt.class),
      ASSIGN_EXPR(_assignExpr.class, AssignExpr.class),
      BINARY_EXPR(_binaryExpr.class, BinaryExpr.class),
      BLOCK_COMMENT(_blockComment.class, BlockComment.class),
      BLOCK_STMT(_blockStmt.class, BlockStmt.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_body.class, VIRTUAL (a VIEW) the presence or absence of a code body
       BOOLEAN_EXPR(_booleanExpr.class, BooleanLiteralExpr.class),
       BREAK_STMT(_breakStmt.class, BreakStmt.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_caseGroup.class, VIRTUAL (a VIEW) all cases and the corresponding code block
       CAST_EXPR(_castExpr.class, CastExpr.class),
       CATCH(_catch.class, CatchClause.class),
       CHAR_EXPR(_charExpr.class, CharLiteralExpr.class),
       CLASS(_class.class, ClassOrInterfaceDeclaration.class),
       CLASS_EXPR(_classExpr.class, ClassExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_codeUnit.class, CompilationUnit.class); Hidden
       COMMENT(_comment.class, Comment.class), //INTERFACE

       CONSTANT(_constant.class, EnumConstantDeclaration.class),
       CONSTRUCTOR(_constructor.class, ConstructorDeclaration.class),
       CONSTRUCTOR_CALL_STMT(_constructorCallStmt.class, ExplicitConstructorInvocationStmt.class),
       CONTINUE_STMT(_continueStmt.class, ContinueStmt.class),
       DO_STMT(_doStmt.class, DoStmt.class),
       DOUBLE_EXPR(_doubleExpr.class, DoubleLiteralExpr.class),
       EMPTY_STMT(_emptyStmt.class, EmptyStmt.class),


       ENUM(_enum.class, EnumDeclaration.class),
       FIELD(_field.class, FieldDeclaration.class), //**
       FIELD_ACCESS_EXPR(_fieldAccessExpr.class, FieldAccessExpr.class),
       FOR_EACH_STMT(_forEachStmt.class, ForEachStmt.class),
       FOR_STMT(_forStmt.class, ForStmt.class),
       IF_STMT(_ifStmt.class, IfStmt.class),
       IMPORT(_import.class, ImportDeclaration.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_imports.class, ImportDeclaration.class);
       INIT_BLOCK(_initBlock.class, InitializerDeclaration.class),
       INSTANCE_OF_EXPR(_instanceOfExpr.class, InstanceOfExpr.class),
       INT_EXPR(_intExpr.class, IntegerLiteralExpr.class),

       INTERFACE(_interface.class, ClassOrInterfaceDeclaration.class),
       JAVADOC_COMMENT(_javadocComment.class, JavadocComment.class),
       LABELED_STATEMENT(_labeledStmt.class, LabeledStmt.class),
       LAMBDA_EXPR(_lambdaExpr.class, LambdaExpr.class),
       LINE_COMMENT(_lineComment.class, LineComment.class),
       LOCAL_CLASS_STATEMENT(_localClassStmt.class, LocalClassDeclarationStmt.class),

       LONG_EXPR(_longExpr.class, LongLiteralExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_method.class
        METHOD(_method.class, MethodDeclaration.class),
        METHOD_CALL_EXPR(_methodCallExpr.class, MethodCallExpr.class),
        METHOD_REF_EXPR(_methodRefExpr.class, MethodReferenceExpr.class),
        MODIFIER(_modifier.class, Modifier .class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_modifiers.class
        MODULE_INFO(_moduleInfo.class, ModuleDeclaration.class),
        NAME(_name.class, SimpleName.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_name.class, SimpleName.class); ******
        NAME_EXPR(_nameExpr.class, NameExpr.class),
        NEW_EXPR(_newExpr.class, ObjectCreationExpr.class),
        NULL_EXPR(_nullExpr.class, NullLiteralExpr.class),
        PACKAGE(_package.class, PackageDeclaration.class),
        PACKAGE_INFO(_packageInfo.class, CompilationUnit.class), /***/
        PARAM(_param.class, Parameter.class),
        PARENTHESIZED_EXPRESSION(_parenthesizedExpr.class, EnclosedExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_parameters.class, Name.class);
        QUALIFIED_NAME(_qualifiedName.class, Name.class), /** VIRTUAL */
        RECEIVER_PARAM(_receiverParam.class, ReceiverParameter.class),
        RETURN_STMT(_returnStmt.class, ReturnStmt.class),
        STRING_EXPR(_stringExpr.class, StringLiteralExpr.class),
        SUPER_EXPR(_superExpr.class, SuperExpr.class),
        SWITCH_ENTRY(_switchEntry.class, SwitchEntry.class),
        SWITCH_EXPR(_switchExpr.class, SwitchExpr.class),
        SWITCH_STMT(_switchStmt.class, SwitchStmt.class),
        SYNCHRONIZED_STMT(_synchronizedStmt.class, SynchronizedStmt.class),
        TERNARY_EXPRESSION(_ternaryExpr.class, ConditionalExpr.class),
        TEXT_BLOCK_EXPR(_textBlockExpr.class, TextBlockLiteralExpr.class),
        THIS_EXPR(_thisExpr.class, ThisExpr.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_throws.class, /** VIRTUAL */

        THROW_STMT(_throwStmt.class, ThrowStmt.class),
        TRY_STMT(_tryStmt.class, TryStmt.class),
        TYPE(_type.class, TypeDeclaration.class),
    //_JAVA_TO_AST_NODE_CLASSES.put(_typeArguments.class VIRTUAL
         TYPE_EXPR(_typeExpr.class, TypeExpr.class),
         TYPE_PARAM(_typeParam.class, TypeParameter.class),
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

         UNARY_EXPR(_unaryExpr.class, UnaryExpr.class),
         VARIABLE(_variable.class, VariableDeclarator.class),
         VARIABLES_EXPR(_variablesExpr.class, VariableDeclarationExpr.class),
         WHILE_STMT(_whileStmt.class, WhileStmt.class),
         YIELD_STMT(_yieldStmt.class, YieldStmt.class);

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
