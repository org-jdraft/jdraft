package org.jdraft.diff;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A "concrete" way of identifying/addressing elements and properties that are the components
 * of a Java program. A way to consistently name things when we construct and deconstruct
 * Components of Java programs (external tools for building & matching &
 * diffing can be more valuable having the opportunity to compare like for
 * like (by componentizing things out and comparing or matching on a part by
 * part basis)
 */
public enum Feature {
    MODULE_DECLARATION("moduleDeclaration", ModuleDeclaration.class),
    PACKAGE("package", _package.class),
    /** i.e. @Deprecated */
    ANNO_EXPR("annoExpr", _anno.class),
    /** i.e. @Deprecated @NotNull */
    ANNO_EXPRS("annoExprs", _annos.class),

    CLASS("class", _class.class),
    ENUM("enum", _enum.class),
    INTERFACE("interface", _interface.class),
    ANNOTATION("annotation", _annotation.class),
    BODY("body", _body.class),

    MODIFIER("modifier", _modifier.class),
    MODIFIERS("modifiers", _modifiers.class), //List.class, Modifier.class),

    HEADER_COMMENT("header", Comments.class),
    JAVADOC("javadoc", _javadocComment.class),

    LINE_COMMENT("lineComment", _lineComment.class),
    BLOCK_COMMENT("blockComment", _blockComment.class),

    PARAM("param", _param.class),
    PARAMS("params", _params.class),

    RECEIVER_PARAM("receiverParam", _receiverParam.class),
    TYPE_PARAM("typeParam", _typeParam.class), //_typeParameter.class
    TYPE_PARAMS("typeParams", _typeParams.class),
    THROWS("throws", _throws.class),
    NAME("name", String.class),

    ANNO_EXPR_ENTRY_PAIR("annoExprEntryPair", MemberValuePair.class), //anno
    ANNO_EXPR_ENTRY_PAIRS("annoExprEntryPairs", List.class, MemberValuePair.class), //anno

    IMPORT("import", _import.class),
    IMPORTS("imports", _imports.class),

    IS_STATIC("isStatic", Boolean.class),
    IS_WILDCARD("isWildcard", Boolean.class),

    ANNOTATION_ENTRY("annotationEntry", _annoMember.class), //annotation
    ANNOTATION_ENTRIES("annotationEntries", List.class, _annoMember.class), //_annotation

    FIELD("field", _field.class),
    FIELDS("fields", List.class, _field.class),

    INNER_TYPE("innerType", _type.class),
    INNER_TYPES("innerTypes", List.class, _type.class),

    COMPANION_TYPE( "companionType", _type.class),
    COMPANION_TYPES( "companionTypes", List.class, _type.class),

    TYPE("type", _typeRef.class), //annotation.element
    DEFAULT_EXPR("defaultExpr", Expression.class), //_annotation.element

    EXTENDS_TYPES("extendsTypes", List.class, ClassOrInterfaceType.class), //_class, //_interface
    IMPLEMENTS_TYPES("implementsTypes", List.class, ClassOrInterfaceType.class), //_class, _enum

    INIT_BLOCK("initBlock", _initBlock.class), //class, _enum
    INIT_BLOCKS("initBlocks", List.class, _initBlock.class), //class, _enum

    CONSTRUCTOR("constructor", _constructor.class),
    CONSTRUCTORS("constructors", List.class, _constructor.class), //class, _enum

    METHOD("method", _method.class),
    METHODS("methods", List.class, _method.class), //class, _enum, _interface, _enum._constant

    CONSTANT("constant", _constant.class), //_enum
    CONSTANTS("constants", List.class, _constant.class),

    ARG_EXPR("arg", Expression.class), //_enum._constant
    ARGS_EXPRS("args", List.class, Expression.class), //_enum._constant

    INIT("init", Expression.class), //field
    IS_FINAL("isFinal", Boolean.class), //_parameter
    IS_VAR_ARG("isVarArg", Boolean.class), //parameter

    AST_TYPE("astType", Type.class), //typeRef
    ARRAY_LEVEL("arrayLevel", Integer.class), //_typeRef
    ELEMENT_TYPE("elementType", Type.class), //array _typeRef

    //new stuff for Statements and expressions
    TRY_BODY("tryBody", BlockStmt.class),
    CATCH_CLAUSES( "catchClauses", List.class, CatchClause.class), //tryStmt
    FINALLY_BODY( "finallyBody", BlockStmt.class),
    WITH_RESOURCES_EXPRS("withResourcesExpr", List.class, Expression.class), //tryStmt

    STATEMENTS("statements", List.class, Statement.class), //statements of a switch entry
    SWITCH_SELECTOR_EXPR("switchSelectorExpr", Expression.class),
    SWITCH_ENTRIES("switchEntries", List.class, SwitchEntry.class), //TODO change to _switchEntry
    SWITCH_BODY_TYPE("switchBodyType", SwitchEntry.Type.class),
    SWITCH_LABEL_EXPRS("switchLabelExprs", List.class, Expression.class),
    ARRAY_NAME("arrayName", Expression.class), //arrayAccess
    INDEX_EXPR("indexExpr", Expression.class), //arrayAccess
    VALUE_EXPRS("valueExprs", List.class, Expression.class), //ArrayInit
    TARGET_EXPR("targetExpr", Expression.class), //assign
    VALUE_EXPR("valueExpr", Expression.class), //assign
    LEFT_EXPR( "leftExpr", Expression.class), //binaryExpr
    RIGHT_EXPR( "rightExpr", Expression.class), //binaryExpr
    BINARY_OPERATOR( "binaryOperator", BinaryExpr.Operator.class), //binaryExpr
    UNARY_OPERATOR( "unaryOperator", UnaryExpr.Operator.class), //unaryExpr
    EXPRESSION("expression", Expression.class), //CastExpr
    CONDITION_EXPR("conditionExpr", Expression.class), //ternary
    THEN_EXPR("thenExpr", Expression.class),    //ternary
    ELSE_EXPR("else", Expression.class),   //ternary
    INNER_EXPR("innerExpr", Expression.class), //parenthesizedExpr
    SCOPE_EXPR("scopeExpr", Expression.class), //fieldAccessExpr
    TYPE_ARGS("typeArgs", List.class, Type.class), //methodCall
    IDENTIFIER("identifier", String.class),  //methodReference
    ANONYMOUS_CLASS_BODY("anonymousClassBody", List.class, BodyDeclaration.class),//_new
    TYPE_NAME("typeName", String.class), //_super superExpr
    VARIABLES("variables", List.class, _variable.class), //VariableDeclarator.class),
    CHECK_EXPR("checkExpr", Expression.class), //assertStmt
    MESSAGE_EXPR("messageExpr", Expression.class), //assertStmt
    LABEL("label", String.class), //breakStmt, labeledStmt
    IS_THIS_CALL("isThisCall", Boolean.class), //constructorCallStmt
    IS_SUPER_CALL("isSuperCall", Boolean.class), //constructorCallStmt
    ITERABLE_EXPR("iterableExpr", Expression.class), //forEachStmt
    VARIABLE("variable", VariableDeclarationExpr.class), //forEachStmt
    INIT_EXPR("initExpr", List.class, Expression.class), //forStmt
    UPDATE_EXPR("updateExpr", List.class, Expression.class),
    COMPARE_EXPR("compareExpr", Expression.class),
    STATEMENT("statement", Statement.class), //labeledStatment
    ARRAY_DIMENSION("arrayDimension", Expression.class),
    ARRAY_DIMENSIONS("arrayDimensions", List.class, ArrayCreationLevel.class), //arrayCreate
    IS_ENCLOSED_PARAMS( "isEnclosedParams", Boolean.class),
    LITERAL("literal", Object.class), //typeRef, textBlock
    ASSIGN_OPERATOR("assignOperator", AssignExpr.Operator.class);

    public final String name;
    public final Class implementationClass;
    public final Class elementClass;

    Feature(String name, Class implementationClass) {
        this.name = name;
        this.implementationClass = implementationClass;
        this.elementClass = null;
    }

    Feature(String name, Class containerClass, Class elementClass) {
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

    public static Feature of(String name) {
        Optional<Feature> op = Arrays.stream(Feature.values()).filter(p -> p.name.equals(name)).findFirst();
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     *
     * @param nodeClass
     * @param <_N>
     * @return
     */
    public static <_N extends _tree._node> Feature of(Class<_N> nodeClass) {
        Optional<Feature> op = Arrays.stream(Feature.values()).filter(p -> p.implementationClass.equals(nodeClass)).findFirst();
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }
}
