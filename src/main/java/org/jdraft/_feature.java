package org.jdraft;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Specifically designate a named feature and tools to get or set the value in an automated fashion
 * in the order the features appear within the AST
 *
 * <P>FROM a "Ast {@link com.github.javaparser.ast.Node} or {@link _java._node}s perspective, defines all of the
 * exactly the underlying possible "feature" nodes "below" it that can be populated and tools for accessing
 * or setting these nodes.</P>
 *
 * <P>For example: we have many Nodes in an AST that are {@link com.github.javaparser.ast.expr.Name} type, but
 * features let you designate a specific {@link com.github.javaparser.ast.expr.Name}, (i.e. the name of a
 * {@link com.github.javaparser.ast.body.MethodDeclaration}) and a tool for identifying, categorizing, accessing
 * or updating specifically method name(s).</P>
 *
 * So the Combination of the featureClass and the featureId can uniquely identify a feature.(i.e.
 * methodNames would be the pair : {_method.class, Feature.Id.NAME}, however there is a
 * public static final _feature field on _methodCall that will implement the feature, so we can just say:
 * _method.NAME... which provides the get() and set() methods appropriate for getting and setting the name
 *
 * @param <_T> the target Type  (i.e. the "container of the feature")
 * @param <_F> the feature Type (the underlying feature... could be a single node or list, etc.)
 */
public interface _feature<_T, _F>{

    /**
     * All features associated with Names, FeatureName delineates all possible names
     * used (i.e. FeatureNames (i.e. FeatureName.NAME) are reused in different entities/contexts
     * @return the FeatureName
     */
    _id getFeatureId();

    /**
     * The disambiguation of where the feature is present (i.e. a Name Feature can exist on a particular Entity)
     * @return
     */
    Class<_T> getTargetClass();

    /**
     * A getter function for retrieving the Feature from the instance
     * @return
     */
    Function<_T, _F> getter();

    /**
     * Setter function that will update the Feature (_F) instance value inside the Target (_T) instance
     * @return the setter function
     */
    BiConsumer<_T, _F> setter();

    /**
     *
     * @param _targetInstance the target instance to obtain the feature from
     * @return the feature from the targetInstance
     */
    _F get(_T _targetInstance);

    /**
     * Set the value of the feature with the new featureValue
     * @param _targetInstance
     * @param featureValue
     * @return
     */
    _T set(_T _targetInstance, _F featureValue);

    /**
     * A "concrete" way of identifying/addressing elements and properties that are the components
     * of a Java program. A way to consistently name things when we construct and deconstruct
     * Components of Java programs (external tools for building & matching &
     * diffing can be more valuable having the opportunity to compare like for
     * like (by componentizing things out and comparing or matching on a part by
     * part basis)
     */
    enum _id {

        ANNO_EXPR("annoExpr"), /** i.e. @Deprecated */
        ANNO_EXPRS("annoExprs"), /** i.e. @Deprecated @NotNull */
        ANNO_EXPR_ENTRY_PAIR("annoExprEntryPair"), //anno
        ANNO_EXPR_ENTRY_PAIRS("annoExprEntryPairs"), //anno

        ANNOTATION("annotation"),
        ANNOTATION_ENTRY("annotationEntry"),
        ANNOTATION_ENTRIES("annotationEntries"),

        ANONYMOUS_CLASS_BODY("anonymousClassBody"),//_new

        ARG_EXPR("arg"), //_enum._constant
        ARGS_EXPRS("args"), //_enum._constant

        ARRAY_DIMENSION("arrayDimension"),
        ARRAY_DIMENSIONS("arrayDimensions"), //arrayCreate
        ARRAY_NAME("arrayName"), //arrayAccess

        ASSIGN_OPERATOR("assignOperator"),

        CODE_UNITS("codeUnits"), //project
        CODE_UNIT("codeUnit"),

        MEMBERS("members"), //for types (_class, _interface, _enum, _annotation)
        MEMBER("member"),

        IS_OPEN("isOpen"), //module-info

        MODULE_DIRECTIVES("moduleDirectives"),
        MODULE_DIRECTIVE("moduleDirective"),
        IS_TRANSITIVE("isTransitive"), //moduleRequires
        NAMES("names"), //moduleProvides, moduleOpens, moduleExports

        VAR_ARG_ANNO_EXPRS("varArgAnnoExprs"),//annotations places on parameter var args

        IS_TOP_LEVEL("isTopLevel"),
        MODULE_DECLARATION("moduleDeclaration"),
        PACKAGE("package"),

        CATCH_CLAUSES( "catchClauses"), //tryStmt
        CATCH("catch"), //individual catch clause
        CASE_EXPRESSIONS("caseExpressions"), // switchEntry i.e. 'a' and 'b' in : case 'A', 'B':

        //values like int, float, etc. are stored as string b/c "0b1" & "1" & "0x1"
        //are mean the same value but represented differently
        LITERAL_VALUE("literalValue"),

        TEXT("text"),
        CLASS("class"),
        ENUM("enum"),
        INTERFACE("interface"),

        BODY("body"),
        MODIFIER("modifier"),
        MODIFIERS("modifiers"), //List.class, Modifier.class),
        HEADER_COMMENT("header"),
        JAVADOC("javadoc"),
        LINE_COMMENT("lineComment"),
        BLOCK_COMMENT("blockComment"),
        PARAM("param"),
        PARAMS("params"),
        RECEIVER_PARAM("receiverParam"),
        TYPE_PARAM("typeParam"), //_typeParam.class
        EXTENDS_TYPE_BOUNDS("extendsTypeBounds"), //typeParam
        TYPE_PARAMS("typeParams"),
        THROWS("throws"), //list of thrown exceptions (method, constructor)
        THROW("throw"), //individual thrown exception type (method constructor)
        NAME("name"),

        IMPORT("import"),
        IMPORTS("imports"),

        IS_STATIC("isStatic"),
        IS_WILDCARD("isWildcard"),


        FIELD("field"),
        FIELDS("fields"),
        INNER_TYPE("innerType"),
        INNER_TYPES("innerTypes"),

        COMPANION_TYPE( "companionType"),
        COMPANION_TYPES( "companionTypes"),

        TYPE("type"),
        DEFAULT_EXPR("defaultExpr"),

        EXTENDS_TYPES("extendsTypes"),
        IMPLEMENTS_TYPES("implementsTypes"), //_class, _enum

        INIT_BLOCK("initBlock"), //class, _enum
        INIT_BLOCKS("initBlocks"), //class, _enum

        CONSTRUCTOR("constructor"),
        CONSTRUCTORS("constructors"), //class, _enum

        METHOD("method"),
        METHODS("methods"),

        CONSTANT("constant"),
        CONSTANTS("constants"),



        INITS("inits"), //initializations made on an forStmt "for(int i=0, int j=1; ...)"
        INIT("init"), //field
        IS_FINAL("isFinal"), //_parameter
        IS_VAR_ARG("isVarArg"), //parameter

        AST_TYPE("astType"), //typeRef
        ARRAY_LEVEL("arrayLevel"), //_typeRef
        ELEMENT_TYPE("elementType"), //array _typeRef

        //new stuff for Statements and expressions
        TRY_BODY("tryBody"),


        FINALLY_BODY( "finallyBody"),
        WITH_RESOURCES_EXPRS("withResourcesExpr"), //tryStmt

        STATEMENTS("statements"), //statements of a switch entry
        SWITCH_SELECTOR_EXPR("switchSelectorExpr"),
        SWITCH_ENTRIES("switchEntries"), // lists of "cases" within a switchSmt switchExpr
        SWITCH_ENTRY("switchEntry"), // individual "case"
        SWITCH_BODY_TYPE("switchBodyType"),
        SWITCH_LABEL_EXPRS("switchLabelExprs"),

        INDEX_EXPR("indexExpr"), //arrayAccess
        VALUE_EXPRS("valueExprs"), //ArrayInit
        TARGET_EXPR("targetExpr"), //assign
        VALUE_EXPR("valueExpr"), //assign
        LEFT_EXPR( "leftExpr"), //binaryExpr
        RIGHT_EXPR( "rightExpr"), //binaryExpr
        BINARY_OPERATOR( "binaryOperator"), //binaryExpr
        UNARY_OPERATOR( "unaryOperator"), //unaryExpr
        EXPRESSION("expression"), //CastExpr
        CONDITION_EXPR("conditionExpr"), //ternary
        THEN("then"),
        //THEN_EXPR("thenExpr"),    //ternary
        ELSE("else"),
        //ELSE_EXPR("else"),   //ternary
        INNER_EXPR("innerExpr"), //parenthesizedExpr
        SCOPE_EXPR("scopeExpr"), //fieldAccessExpr
        TYPE_ARGS("typeArgs"), //methodCall
        IDENTIFIER("identifier"),  //methodReference

        TYPE_NAME("typeName"), //_super superExpr
        VARIABLES("variables"), //VariableDeclarator.class),
        CHECK_EXPR("checkExpr"), //assertStmt
        MESSAGE_EXPR("messageExpr"), //assertStmt
        LABEL("label"), //breakStmt, labeledStmt
        IS_THIS_CALL("isThisCall"), //constructorCallStmt
        IS_SUPER_CALL("isSuperCall"), //constructorCallStmt
        ITERABLE_EXPR("iterableExpr"), //forEachStmt
        VARIABLE("variable"), //forEachStmt
        INIT_EXPR("initExpr"), //forStmt
        UPDATES_EXPRS("updateExprs"), //forStmt
        UPDATE_EXPR("updateExpr"),
        COMPARE_EXPR("compareExpr"),
        STATEMENT("statement"), //labeledStatment
        IS_PARENTHESIZED_PARAMS("isParenthesizedParams"),

        IS_ENCLOSED_PARAMS( "isEnclosedParams"),
        LITERAL("literal"), //typeRef, textBlock
        ;

        public final String name;

        private _id(String name){
            this.name = name;
        }
    }

    //TODO multiOrdered
    //    get(int index)
    //    set(int index)
    //    replace(old, new)

    //TODO multiBag
    //     replace(old, new)
    /**
     *
     * @param <_T>
     * @param <_E>
     */
    class _many<_T, _E> implements _feature<_T, List<_E>> {
        public final Class<_T> targetClass;
        public final Class<_E> featureElementClass;

        public final _id featureId;
        public final _id featureElementId;
        public final Function<_T, List<_E>> getter;
        public final BiConsumer<_T, List<_E>> setter;

        public _many(Class<_T> targetClass, Class<_E>featureElementClass, _id featureId, _id featureElementId,
                     Function<_T,List<_E>> getter, BiConsumer<_T,List<_E>> setter){
            this.targetClass = targetClass;
            this.featureElementClass = featureElementClass;
            this.featureId = featureId;
            this.featureElementId = featureElementId;
            this.getter = getter;
            this.setter = setter;
        }

        public Class<_T> getTargetClass(){
            return targetClass;
        }

        public Class<_E> getFeatureElementClass(){
            return featureElementClass;
        }

        public _id getFeatureId(){
            return this.featureId;
        }

        public Function<_T, List<_E>> getter(){
            return this.getter;
        }

        public List<_E> get(_T _a){
            return this.getter.apply(_a);
        }

        public int size(_T _a){
            List l = get(_a);
            if( l != null ) {
                return l.size();
            }
            return 0;
        }

        public Iterator<_E> iterator(_T _a){
            List l = get(_a);
            if( l == null ){
                return Collections.emptyIterator();
            }
            return l.iterator();
        }

        public _T set(_T _targetInstance, List<_E> featureValue){
            this.setter.accept(_targetInstance, featureValue);
            return _targetInstance;
        }

        //add
        //remove
        //set/replace target index value

        public BiConsumer<_T, List<_E>> setter(){
            return this.setter;
        }

        public String toString(){
            return this.targetClass.getSimpleName()+"."+featureId;
        }
    }

    /**
     * A tool for operating on a specific feature that is a single entity (i.e. a
     * within an instance of the targetClass
     *
     * @param <_T>
     * @param <_F>
     */
    class _one<_T, _F> implements _feature<_T, _F> {
        public final Class<_T> targetClass;
        public final Class<_F> featureClass;

        public final _id feature;
        public final Function<_T, _F> getter;
        public final BiConsumer<_T, _F> setter;

        public _one(Class<_T> targetClass, Class<_F>featureClass, _id feature, Function<_T,_F> getter, BiConsumer<_T,_F> setter){
            this.targetClass = targetClass;
            this.featureClass = featureClass;
            this.feature = feature;
            this.getter = getter;
            this.setter = setter;
        }

        public Class<_T> getTargetClass(){
            return targetClass;
        }

        public Class<_F> getFeatureClass(){
            return featureClass;
        }

        public _id getFeatureId(){
            return this.feature;
        }

        public Function<_T, _F> getter(){
            return this.getter;
        }

        public _F get(_T _a){
            return this.getter.apply(_a);
        }

        public _T set(_T _targetInstance, _F featureValue){
            this.setter.accept(_targetInstance, featureValue);
            return _targetInstance;
        }

        public BiConsumer<_T, _F> setter(){
            return this.setter;
        }

        public String toString(){
            return targetClass.getSimpleName()+"."+ getFeatureId();
        }
    }

    /**
     * Immutable instance for all {@link _java._node} types: describes the ordered features (i.e. edges to child nodes)
     * that can be get or set for this node within the AST (this allows programs to be written to automated tree
     * traversal on each node encounter)
     *
     * for example an {@link _annoExpr} has
     *
     * @see _annoExpr#META
     *
     * @param <_T>
     */
    class _meta<_T>{

        public static <_T extends Object> _meta<_T> of(Class<_T> targetClass, _feature<_T,?>...features){
            return new _meta<>(targetClass, features);
        }

        public final Class<_T> targetClass;

        /** Note: this should be the features IN THE ORDER THEY APPEAR IN THE LANGUAGE */
        public final List<_feature<_T, ?>> featureList;

        private _meta(Class<_T> targetClass, _feature<_T, ?>... features ){
            this.targetClass = targetClass;
            this.featureList = Stream.of(features).collect(Collectors.toList());
        }

        //get the target class for this specification
        public Class<_T> getTargetClass(){
            return targetClass;
        }

        //returns a (ordered logically in the token order appearance) list of all features for the targetClass
        public List<_feature<_T, ?>> list(){
            return featureList;
        }

        /** returns an ordered list of features that map to the instances features that pass the matchFn */
        public List<_feature<_T, ?>> list(Predicate<_feature<_T,?>> matchFn){
            return featureList.stream().filter(matchFn).collect(Collectors.toList());
        }

        /**
         * Perform some action on all features that match the matchFn
         * @param matchFn matcher for features
         * @param actionFn the action to take with the matching features
         * @return the immutable meta<_T>
         */
        public _meta<_T> forEach(Predicate<_feature<_T, ?>> matchFn, Consumer<_feature<_T,?>> actionFn){
            list(matchFn).forEach(actionFn);
            return this;
        }

        public _meta<_T> forEach(Consumer<_feature<_T,?>> consumerFn){
            this.featureList.forEach(consumerFn);
            return this;
        }
    }
}
