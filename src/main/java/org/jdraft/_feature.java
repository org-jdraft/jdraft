package org.jdraft;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Specifically designate a named feature and tools to get or set the value in an automated fashion
 * in the order the features appear within the AST
 *
 * <P>FROM a "Ast {@link com.github.javaparser.ast.Node} or {@link _java._node}s perspective, defines all of the
 * underlying possible "feature" nodes "below" it that can be populated and tools for accessing
 * or setting these nodes.</P>
 *
 * <P>For example: we have many Nodes in an AST that are {@link com.github.javaparser.ast.expr.Name} type, but
 * features let you designate a specific {@link com.github.javaparser.ast.expr.Name}, (i.e. the name of a
 * {@link com.github.javaparser.ast.body.MethodDeclaration}) and a tool for identifying, categorizing, accessing
 * or updating specifically method name(s) {@link _method#NAME}.</P>
 *
 * So the Combination of the featureClass and the featureId can uniquely identify a feature.(i.e.
 * methodNames would be the pair : {_method.class, Feature.Id.NAME}, however there is a
 * public static final _feature field on _methodCall that will implement the feature, so we can just say:
 * _method.NAME... which provides the get() and set() methods appropriate for getting and setting the name
 *
 * TODO: add a lambda for converting between a String... and a _T (using the of() methods) maybe that is on each class
 * i.e. a PARSE Function that is on each class that calls the of(....) method to create an instance
 * _methodCallExpr.PARSE = (String...code)-> _methodCallExpr.of(code);
 * TODO: matches function takes in a String
 *
 * @param <_T> the target Type  (i.e. the "container of the feature")
 * @param <_F> the feature Type (the underlying feature... could be a single node or list, etc.)
 */
public interface _feature<_T, _F>{

    /**
     * The disambiguation of where the feature is present (i.e. identify ONLY names on {@link _method}s like {@link _method#NAME}
     * @return the target Class of the feature (i.e. {@link _method} for {@link _method#NAME}
     */
    Class<_T> getTargetClass();

    /**
     * All features associated with Names, FeatureName delineates all possible names
     * used (i.e. FeatureNames (i.e. FeatureName.NAME) are reused in different entities/contexts
     * @return the FeatureName
     */
    _id getFeatureId();

    /**
     * A getter function for retrieving the Feature from the target class instance
     * @return function for retrieving the feature instance from the target instance
     */
    Function<_T, _F> getter();

    /**
     * Setter function that will update the Feature (_F) instance value inside the Target (_T) instance
     * @return the setter function
     */
    BiConsumer<_T, _F> setter();

    /**
     * Return the parsers and creates an instance of the target (container) class of the feature from a String
     * @return a Parser for parsing a Target instance from a String
     */
    Function<String, _T> getTargetParser();

    /**
     * Base function for determining if these features are (semantically) the same
     *
     * @param left the left feature
     * @param right the right feature
     * @return true if these features are the same, false otherwise
     */
    default boolean equal(_F left, _F right){
        return Objects.equals(left, right);
    }

    /**
     *
     * @param _targetInstance the target instance to obtain the feature from
     * @return the feature from the targetInstance
     */
    _F get(_T _targetInstance);

    /**
     * Set the value of the feature on the targetInstance with the new featureValue
     * @param _targetInstance the instance to update
     * @param featureValue the value to set
     * @return the modified targetInstance
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

        /**{@link _annoExprs#ANNOS} */
        ANNO_EXPR("annoExpr"), /** i.e. @Deprecated */
        ANNO_EXPRS("annoExprs"), /** i.e. @Deprecated @NotNull */

        /** {@link _annoExpr#ENTRY_PAIRS} */
        ENTRY_PAIRS("entryPairs"),
        /** {@link _annoExpr#ENTRY_PAIRS} */
        ENTRY_PAIR("entryPair"),

        /*ANNOTATION("annotation"), /* the declaration of an ANNOTATION i.e. "@interface Closeable{}" */
        /* ANNOTATION_ENTRIES("annotationEntries"), /*Lists of Members of annotations */
        /* ANNOTATION_ENTRY("annotationEntry"), Lists of members in _annotations*/

        /** {@link _newExpr#ANONYMOUS_CLASS_BODY} */
        ANONYMOUS_CLASS_BODY("anonymousClassBody"),

        /** {@link _args#ARGS}, {@link _constant#ARGS},  {@link _methodCallExpr#ARGS} {@link _constructorCallStmt#ARGS}, {@link _newExpr#ARGS}*/
        ARGS("args"),
        /** {@link _args#ARGS} */
        ARG("arg"),

        /** {@link _arrayCreateExpr#DIMENSIONS} */
        ARRAY_DIMENSIONS("arrayDimensions"),
        /** {@link _arrayCreateExpr#DIMENSIONS} */
        ARRAY_DIMENSION("arrayDimension"),

        /** {@link _assignExpr#OPERATOR}
        ASSIGN_OPERATOR("assignOperator"),*/

        /** {@link _catch#BODY} {@link _constructor#BODY} {@link _doStmt#BODY}, {@link _forEachStmt#BODY},
         * {@link _forStmt#BODY}, {@link _initBlock#BODY}, {@link _lambdaExpr#BODY}, {@link _method#BODY},
         * {@link _synchronizedStmt#BODY}, {@link _whileStmt#BODY}
         */
        BODY("body"),

        /**{@link _localClassStmt#CLASS} */
        CLASS("class"),
        /*ENUM("enum"),
        INTERFACE("interface"),
        */


        /** {@link _tryStmt#CATCH_CLAUSES}*/
        CATCH_CLAUSES( "catchClauses"),
        /** {@link _tryStmt#CATCH_CLAUSES}*/
        CATCH_CLAUSE("catchClause"),

        /**{@link _switchEntry#CASE_EXPRESSIONS}*/
        CASE_EXPRESSIONS("caseExpressions"),


        /**{@link _assertStmt#CHECK}*/
        CHECK_EXPR("checkExpr"),

        /** {@link _project#CODE_UNITS}*/
        CODE_UNITS("codeUnits"), //project
        /** {@link _project#CODE_UNITS}*/
        CODE_UNIT("codeUnit"),


        /** {@link org.jdraft._java._withCondition}, {@link _doStmt#CONDITION}, {@link _ifStmt#CONDITION}, {@link _ternaryExpr#CONDITION}, {@link _whileStmt#CONDITION} */
        CONDITION_EXPR("conditionExpr"), //ternary

        /* CONSTANTS("constants"), */
        /* CONSTANT("constant"), */
        /* CONSTRUCTORS("constructors"), //class, _enum */
        /* CONSTRUCTOR("constructor"), */

        /** {@link _entry#DEFAULT} */
        DEFAULT_EXPR("defaultExpr"),

        /**{@link org.jdraft._java._withExpression} */
        EXPRESSION("expression"), //CastExpr

        /** {@link _typeParam#EXTENDS_TYPE_BOUND} */
        EXTENDS_TYPE_BOUNDS("extendsTypeBounds"), //typeParam

        /**{@link org.jdraft._type._withExtends} {@link _class#EXTENDS} {@link _interface#EXTENDS}*/
        EXTENDS_TYPES("extendsTypes"),

        /**{@link _tryStmt#FINALLY_BODY}*/
        FINALLY_BODY( "finallyBody"),

        /** {@link _class#MEMBERS}, {@link _enum#MEMBERS}, {@link _annotation#MEMBERS}, {@link _interface#MEMBERS}, {@link _constant#MEMBERS} */
        MEMBERS("members"),
        /** {@link _class#MEMBERS}, {@link _enum#MEMBERS}, {@link _annotation#MEMBERS}, {@link _interface#MEMBERS}, {@link _constant#MEMBERS} */
        MEMBER("member"),

        /** {@link _moduleInfo#IS_OPEN} */
        IS_OPEN("isOpen"),

        /** {@link _moduleInfo#MODULE_DIRECTIVES} */
        MODULE_DIRECTIVES("moduleDirectives"),
        /** {@link _moduleInfo#MODULE_DIRECTIVES} */
        MODULE_DIRECTIVE("moduleDirective"),

        /** {@link _moduleRequires#IS_TRANSITIVE} */
        IS_TRANSITIVE("isTransitive"),

        /** {@link _body#IS_IMPLEMENTED} */
        IS_IMPLEMENTED( "isImplemented"), //for implemented bodies (i.e. "void m(){}" IMPLEMENTED vs "void m();" NOT IMPLEMENTED)

        /** {@link _moduleProvides#MODULE_NAMES}, {@link _moduleOpens#MODULE_NAMES}, {@link _moduleExports#MODULE_NAMES} */
        MODULE_NAMES("moduleNames"),
        /**{@link _param#VAR_ARG_ANNO_EXPRS} */
        VAR_ARG_ANNO_EXPRS("varArgAnnoExprs"),

        /* IS_TOP_LEVEL("isTopLevel"), */
        /* MODULE_DECLARATION("moduleDeclaration"), */

        /** {@link _annotation#PACKAGE}, {@link _class#PACKAGE}, {@link _enum#PACKAGE}, {@link _interface#PACKAGE}, {@link _packageInfo#PACKAGE}*/
        PACKAGE("package"),

        //values like int, float, etc. are stored as string b/c "0b1" & "1" & "0x1"
        //are mean the same value but represented differently
        /**{@link _expr._literal} (int, double, boolean, string, char, float, textBlock)*/
        LITERAL_VALUE("literalValue"),

        /**{@link _comment} (lineComment, blockComment, javadocComment)*/
        TEXT("text"),

        /** {@link org.jdraft._modifiers._withModifiers} */
        MODIFIERS("modifiers"),
        /** {@link _modifiers#MODIFIERS}*/
        MODIFIER("modifier"),

        /** HEADER_COMMENT("header"), */

        /** {@link _annotation#JAVADOC}, {@link _class#JAVADOC}, {@link _enum#JAVADOC}, {@link _interface#JAVADOC},
         * {@link _constructor#JAVADOC} {@link _method#JAVADOC}
         */
        JAVADOC("javadoc"),

        /**{@link _assignExpr}, {@link _binaryExpr}, {@link _unaryExpr}*/
        OPERATOR("operator"),
        /*LINE_COMMENT("lineComment"), BLOCK_COMMENT("blockComment"),        */

        /**{@link _catch#PARAM} {@link _params#PARAMS} */
        PARAM("param"),
        /** {@link org.jdraft._params._withParams} {@link _params#PARAMS}, {@link _constructor#PARAMS}, {@link _lambdaExpr#PARAMS}, {@link _method#PARAMS} */
        PARAMS("params"),
        /** {@link org.jdraft._receiverParam._withReceiverParam} {@link _method#RECEIVER_PARAM} {@link _constructor#RECEIVER_PARAM} */
        RECEIVER_PARAM("receiverParam"),

        /** {@link _typeParams#TYPE_PARAMS} */
        TYPE_PARAM("typeParam"),

        /**{@link org.jdraft._typeParams._withTypeParams} */
        TYPE_PARAMS("typeParams"),

        /**{@link org.jdraft._throws._withThrows}, {@link _constructor#THROWS}, {@link _method#THROWS} */
        THROWS("throws"), //list of thrown exceptions (method, constructor)
        /**{@link _throws#THROWS} */
        THROW("throw"), //individual thrown exception type (method constructor)

        /**{@link org.jdraft._java._withName} ... */
        NAME("name"),

        /**{@link _type}, {@link _annotation}, {@link _class}, {@link _enum}, {@link _interface}, {@link _imports}*/
        IMPORTS("imports"),
        /**{@link _imports} */
        IMPORT("import"),

        /** {@link _import} {@link _initBlock} */
        IS_STATIC("isStatic"),

        /**{@link _import}*/
        IS_WILDCARD("isWildcard"),

        /**{@link org.jdraft._typeRef._withTypeRef} */
        TYPE_REF("typeRef"),

        /**{@link org.jdraft._type._withImplements}*/
        IMPLEMENTS_TYPES("implementsTypes"), //_class, _enum

        /*
        INIT_BLOCKS("initBlocks"), //class, _enum
        INIT_BLOCK("initBlock"), //class, _enum
        */

        INITS("inits"), //initializations made on an forStmt "for(int i=0, int j=1; ...)"

        /** {@link _variable#INIT} */
        INIT("init"), //field
        /** {@link _param#IS_FINAL} */
        IS_FINAL("isFinal"),
        /** {@link _param#IS_VAR_ARG}*/
        IS_VAR_ARG("isVarArg"),

        /*
        METHODS("methods"),
        METHOD("method"),
        */

        /* AST_TYPE("astType"), //typeRef */
        /* ARRAY_LEVEL("arrayLevel"), //_typeRef */
        /* ELEMENT_TYPE("elementType"), //array _typeRef */

        //new stuff for Statements and expressions
        /**{@link _tryStmt#TRY_BODY}*/
        TRY_BODY("tryBody"),

        /**{@link _tryStmt#WITH_RESOURCES}*/
        WITH_RESOURCES_EXPRS("withResourcesExprs"), //tryStmt

        /**{@link _blockStmt} {@link _body} {@link _switchEntry} */
        STATEMENTS("statements"),

        /* SWITCH_SELECTOR_EXPR("switchSelectorExpr"), */

        /**{@link _switchExpr}, {@link _switchStmt}*/
        SWITCH_ENTRIES("switchEntries"),
        /**{@link _switchExpr}, {@link _switchStmt}*/
        SWITCH_ENTRY("switchEntry"),

        /**{@link _switchEntry#BODY_TYPE}*/
        BODY_TYPE("bodyType"),
        /* SWITCH_LABEL_EXPRS("switchLabelExprs"), */

        /**{@link _arrayAccessExpr#INDEX}*/
        INDEX("index"),
        /* VALUE_EXPRS("valueExprs"), //ArrayInit */

        /**{@link _assignExpr#TARGET}*/
        TARGET("target"),

        VALUE_EXPR("valueExpr"), //assign
        LEFT_EXPR( "leftExpr"), //binaryExpr
        RIGHT_EXPR( "rightExpr"), //binaryExpr

        /* BINARY_OPERATOR( "binaryOperator"), /** {@link _binaryExpr}
        UNARY_OPERATOR( "unaryOperator"), //unaryExpr */

        /**{@link _ifStmt#THEN}, {@link _ternaryExpr#THEN}*/
        THEN("then"),
        /**{@link _ifStmt#THEN}, {@link _ternaryExpr#THEN}*/
        ELSE("else"),


        /**{@link org.jdraft._java._withScope}, {@link _fieldAccessExpr#SCOPE}, {@link _methodCallExpr#SCOPE}, {@link _methodRefExpr#SCOPE}, {@link _newExpr#SCOPE} */
        SCOPE("scope"),

        /**{@link org.jdraft._typeArgs._withTypeArgs}, {@link _constructorCallStmt}, {@link _fieldAccessExpr}, {@link _methodCallExpr}, {@link _methodRefExpr}, {@link _newExpr}, {@link _typeArgs} */
        TYPE_ARGS("typeArgs"),

        /**{@link _methodRefExpr}*/
        IDENTIFIER("identifier"),

        /**{@link _superExpr#TYPE_NAME} {@link _thisExpr#TYPE_NAME}*/
        TYPE_NAME("typeName"),

        VARIABLES("variables"), //VariableDeclarator.class),

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

        _id(String name){
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

        /** the top level container target class containing the feature (i.e. {@link _method} for {@link _method#NAME} */
        public final Class<_T> targetClass;

        /** the unifying element type (could be an interface, i.e. _java._member) */
        public final Class<_E> featureElementClass;

        /**
         * the implementation types of feature elements (i.e. {@link _method}, {@link _field},... for {@link _java._member})
         * this is filled in if the {@link #featureElementClass} is a base class or interface.
         */
        public Class[] featureImplementationClasses;

        public final _id featureId;
        public final _id featureElementId;
        public final Function<_T, List<_E>> getter;
        public final BiConsumer<_T, List<_E>> setter;

        public final Function<String, _T> targetParser;
        public final Function<String,_E>elementParser;

        public _many(Class<_T> targetClass, Class<_E>featureElementClass, _id featureId, _id featureElementId,
                     Function<_T,List<_E>> getter, BiConsumer<_T,List<_E>> setter, Function<String,_T> targetParser,
                     Function<String, _E>elementParser){
            this.targetClass = targetClass;
            this.featureElementClass = featureElementClass;
            this.featureId = featureId;
            this.featureElementId = featureElementId;
            this.getter = getter;
            this.setter = setter;
            this.targetParser = targetParser;
            this.elementParser = elementParser;
        }

        public _many featureImplementations(Class<? extends _E>... featureImplementationClass ){
            this.featureImplementationClasses = featureImplementationClasses;
            return this;
        }

        public Function<String, _T> getTargetParser(){ return this.targetParser; }

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
        public final Function<String,_T> targetParser;

        public _one(Class<_T> targetClass, Class<_F>featureClass, _id feature, Function<_T,_F> getter, BiConsumer<_T,_F> setter, Function<String, _T> targetParser){
            this.targetClass = targetClass;
            this.featureClass = featureClass;
            this.feature = feature;
            this.getter = getter;
            this.setter = setter;
            this.targetParser = targetParser;
        }

        public Class<_T> getTargetClass(){
            return targetClass;
        }

        public Class<_F> getFeatureClass(){
            return featureClass;
        }

        public Function<String, _T> getTargetParser() { return this.targetParser; }

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

        /** Class of the Container of the feature (i.e. {@link _method} class for the {@link _method#NAME} feature) */
        public final Class<_T> targetClass;

        /**
         * Note: this should be the features IN THE ORDER THEY APPEAR IN THE LANGUAGE
         * each feature MAY be null, a single entity, or a list of entities as defined by it's
         * {@link _feature} implementation
         */
        public final List<_feature<_T, ?>> featureList;

        /**
         * By default, the order of the features is exactly the order they are passed in
         * (THIS IS NOT the case for things like {@link _ternaryExpr}, because it is possible
         * for the "Operator" to be:
         * <UL>
         *     <LI>a prefix operator, as in "++a", "--a", "!a"
         *     (where the OPERATOR feature ("++", "--", "!") is BEFORE the Expression ("a") feature)
         *     <LI>a postfix operator, as in "a++", "a--"
         *     (where the OPERATOR feature ("++", "--") is AFTER the Expression ("a") feature
         * </UL>
         * @see _ternaryExpr
         */
        public BiFunction<_T, List<_feature<_T, ?>>, List<_feature<_T, ?>>> featureOrder =
                (_T instance, List<_feature<_T, ?>> baseOrder) -> baseOrder;

        private _meta(Class<_T> targetClass, _feature<_T, ?>... features ){
            this.targetClass = targetClass;
            this.featureList = Stream.of(features).collect(Collectors.toList());
        }

        /** get the target class for this specification */
        public Class<_T> getTargetClass(){
            return targetClass;
        }

        /**
         * Sets the feature order based on the instance
         * @param featureOrder
         * @return
         */
        public _meta<_T> setFeatureOrder(BiFunction<_T, List<_feature<_T, ?>>, List<_feature<_T, ?>>> featureOrder ){
            this.featureOrder = featureOrder;
            return this;
        }

        /**
         * List the appropriate featureOrder based on the _T instance
         * @param instance the instance of the container of features
         * @return the list of features in the order they appear
         */
        public List<_feature<_T, ?>> list(_T instance){
             return featureOrder.apply(instance, this.featureList);
        }

        /**
         * List the appropriate featureOrder based on the _T instance
         * @param instance the instance of the container of features
         * @return the list of features in the order they appear
         */
        public List<_feature<_T, ?>> list(_T instance, Predicate<_feature<_T,?>> matchFn){
            return featureOrder.apply(instance, this.featureList).stream().filter(matchFn).collect(Collectors.toList());
        }

        /** returns a (ordered logically in the token order appearance) list of all features for the targetClass
        public List<_feature<_T, ?>> list(){
            return featureList;
        }
        */

        /** returns an ordered list of features that map to the instances features that pass the matchFn
        public List<_feature<_T, ?>> list(Predicate<_feature<_T,?>> matchFn){
            return featureList.stream().filter(matchFn).collect(Collectors.toList());
        }
         */

        /**
         * Perform some action on all features that match the matchFn
         * @param matchFn matcher for features
         * @param actionFn the action to take with the matching features
         * @return the immutable meta<_T>
         */
        public _meta<_T> forEach(_T instance, Predicate<_feature<_T, ?>> matchFn, Consumer<_feature<_T,?>> actionFn){
            list(instance, matchFn).forEach(actionFn);
            return this;
        }

        /**
         *
         * @param actionFn
         * @return
         */
        public _meta<_T> forEach(_T instance, Consumer<_feature<_T,?>> actionFn){
            list(instance).forEach(actionFn);
            return this;
        }
    }
}
