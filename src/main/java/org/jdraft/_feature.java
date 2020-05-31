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

        /**{@link org.jdraft._annoExprs._withAnnoExprs}... */
        ANNO_EXPRS("annoExprs", _annoExprs._withAnnoExprs.class), /** i.e. @Deprecated @NotNull */
        /**{@link _annoExprs#ANNOS} */
        ANNO_EXPR("annoExpr"), /** i.e. @Deprecated */

        /** {@link _newExpr#ANONYMOUS_CLASS_BODY} */
        ANONYMOUS_CLASS_BODY("anonymousClassBody"),

        /** {@link org.jdraft._args._withArgs} {@link _args#ARGS}, {@link _constant#ARGS},  {@link _methodCallExpr#ARGS}
         *  {@link _constructorCallStmt#ARGS}, {@link _newExpr#ARGS}*/
        ARGS("args", _args._withArgs.class),
        /** {@link _args#ARGS} */
        ARG("arg"),

        /** {@link _arrayCreateExpr#DIMENSIONS} */
        ARRAY_DIMENSIONS("arrayDimensions"),
        /** {@link _arrayCreateExpr#DIMENSIONS} */
        ARRAY_DIMENSION("arrayDimension"),

        /**
         * {@link org.jdraft._body._withBody}
         * {@link _catch#BODY} {@link _constructor#BODY} {@link _doStmt#BODY}, {@link _forEachStmt#BODY},
         * {@link _forStmt#BODY}, {@link _initBlock#BODY}, {@link _lambdaExpr#BODY}, {@link _method#BODY},
         * {@link _synchronizedStmt#BODY}, {@link _whileStmt#BODY}
         */
        BODY("body", _body._withBody.class),

        /**{@link _switchEntry#BODY_TYPE}*/
        BODY_TYPE("bodyType"),

        /**{@link _switchEntry#CASE_EXPRESSIONS}*/
        CASE_EXPRESSIONS("caseExpressions"),
        /**{@link _switchEntry#CASE_EXPRESSIONS}*/
        CASE_EXPRESSION("caseExpression"),

        /** {@link _tryStmt#CATCH_CLAUSES}*/
        CATCH_CLAUSES( "catchClauses"),
        /** {@link _tryStmt#CATCH_CLAUSES}*/
        CATCH_CLAUSE("catchClause"),

        /**{@link _assertStmt#CHECK}*/
        CHECK("check"),

        /**{@link _localClassStmt#CLASS} */
        CLASS("class"),

        /** {@link _project#CODE_UNITS}*/
        CODE_UNITS("codeUnits"), //project
        /** {@link _project#CODE_UNITS}*/
        CODE_UNIT("codeUnit"),

        /**{@link _forStmt#COMPARE}*/
        COMPARE("compare"),

        /** {@link org.jdraft._java._withCondition}, {@link _doStmt#CONDITION}, {@link _ifStmt#CONDITION},
         * {@link _ternaryExpr#CONDITION}, {@link _whileStmt#CONDITION} */
        CONDITION("condition", _java._withCondition.class),

        /** {@link _entry#DEFAULT} */
        DEFAULT("default"),

        /**{@link _ifStmt#ELSE}, {@link _ternaryExpr#ELSE}*/
        ELSE("else"),

        /** {@link _annoExpr#ENTRY_PAIRS} */
        ENTRY_PAIRS("entryPairs"),
        /** {@link _annoExpr#ENTRY_PAIRS} */
        ENTRY_PAIR("entryPair"),

        /**
         * {@link org.jdraft._java._withExpression} {@link _arrayDimension#EXPRESSION}, {@link _castExpr#EXPRESSION},
         * {@link _exprStmt#EXPRESSION}, {@link _instanceOfExpr#EXPRESSION}, {@link _parenthesizedExpr#EXPRESSION},
         * {@link _returnStmt#EXPRESSION}, {@link _synchronizedStmt#EXPRESSION}, {@link _throwStmt#EXPRESSION},
         * {@link _unaryExpr#EXPRESSION}, {@link _yieldStmt#EXPRESSION}
         */
        EXPRESSION("expression", _java._withExpression.class),

        /** {@link _typeParam#EXTENDS_TYPE_BOUND} */
        EXTENDS_TYPE_BOUNDS("extendsTypeBounds"),

        /**{@link _class#EXTENDS} {@link _interface#EXTENDS}*/
        EXTENDS("extends", _type._withExtends.class),

        /**{@link _tryStmt#FINALLY_BODY}*/
        FINALLY_BODY( "finallyBody"),

        /**{@link _methodRefExpr#IDENTIFIER}*/
        IDENTIFIER("identifier"),

        /**{@link org.jdraft._type._withImplements}, {@link _class#IMPLEMENTS}, {@link _enum#IMPLEMENTS}*/
        IMPLEMENTS("implements", _type._withImplements.class), //_class, _enum

        /**{@link _annotation#IMPORTS}, {@link _class#IMPORTS}, {@link _enum#IMPORTS},{@link _interface#IMPORTS},
         * {@link _packageInfo#IMPORTS},{@link _moduleInfo#IMPORTS} {@link _imports#IMPORTS}*/
        IMPORTS("imports"),
        /**{@link _imports#IMPORTS} */
        IMPORT("import"),

        /**{@link _arrayAccessExpr#INDEX}*/
        INDEX("index"),

        /**{@link _forStmt#INITS}, {@link _arrayInitExpr#INITS}*/
        INITS("inits"),
        /** {@link _variable#INIT} {@link _arrayCreateExpr#INIT}, {@link _arrayInitExpr#INITS},
         * {@link _field#INIT}, {@link _forStmt#INITS}*/
        INIT("init"),

        /** {@link _param#IS_FINAL} */
        IS_FINAL("isFinal"),

        /**
         * is the body implemented, or (abstract/NOT implemented)
         * <CODE>void m(){}</CODE> has an IMPLEMENTED BODY because its an empty block statement
         * -vs-
         * <CODE>void m();</CODE> has a NOT IMPLEMENTED BODY because ";" )
         * {@link _body#IS_IMPLEMENTED} */
        IS_IMPLEMENTED( "isImplemented"),

        /** {@link _moduleInfo#IS_OPEN} */
        IS_OPEN("isOpen"),

        /**{@link _lambdaExpr#IS_PARAM_PARENTHESIZED}*/
        IS_PARAM_PARENTHESIZED("isParamParenthesized"),

        /** {@link _import#IS_STATIC} {@link _initBlock#IS_STATIC} */
        IS_STATIC("isStatic"),

        /**{@link _constructorCallStmt#IS_SUPER} */
        IS_SUPER("isSuper"),

        /**{@link _constructorCallStmt#IS_THIS} */
        IS_THIS("isThis"),

        /** {@link _moduleRequires#IS_TRANSITIVE} */
        IS_TRANSITIVE("isTransitive"),

        /**{@link _import#IS_WILDCARD}*/
        IS_WILDCARD("isWildcard"),

        /** {@link _param#IS_VAR_ARG}*/
        IS_VAR_ARG("isVarArg"),

        /**{@link _forEachStmt#ITERABLE}*/
        ITERABLE("iterable"),

        /**
         * {@link org.jdraft._javadocComment._withJavadoc}
         * {@link _annotation#JAVADOC}, {@link _class#JAVADOC}, {@link _enum#JAVADOC}, {@link _interface#JAVADOC},
         * {@link _constructor#JAVADOC} {@link _method#JAVADOC}
         */
        JAVADOC("javadoc", _javadocComment._withJavadoc.class),

        /**{@link _breakStmt#LABEL}, {@link _labeledStmt#LABEL}, {@link _continueStmt#LABEL} */
        LABEL("label"),

        /**{@link _binaryExpr}*/
        LEFT( "left"),

        //values like int, float, etc. are stored as string b/c "0b1" & "1" & "0x1"
        //are mean the same value but represented differently
        /**{@link _expr._literal} {@link _intExpr#LITERAL_VALUE}, {@link _doubleExpr#LITERAL_VALUE},
         * {@link _booleanExpr#LITERAL_VALUE}, {@link _stringExpr#LITERAL_VALUE},{@link _charExpr#LITERAL_VALUE},
         * {@link _textBlockExpr#LITERAL_VALUE) */
        LITERAL_VALUE("literalValue"),

        /** {@link _class#MEMBERS}, {@link _enum#MEMBERS}, {@link _annotation#MEMBERS}, {@link _interface#MEMBERS}, {@link _constant#MEMBERS} */
        MEMBERS("members"),
        /** {@link _class#MEMBERS}, {@link _enum#MEMBERS}, {@link _annotation#MEMBERS}, {@link _interface#MEMBERS}, {@link _constant#MEMBERS} */
        MEMBER("member"),

        /**{@link _assertStmt#MESSAGE}*/
        MESSAGE("message"),

        /** {@link org.jdraft._modifiers._withModifiers} */
        MODIFIERS("modifiers", _modifiers._withModifiers.class),
        /** {@link _modifiers#MODIFIERS}*/
        MODIFIER("modifier"),

        /** {@link _moduleInfo#MODULE_DIRECTIVES} */
        MODULE_DIRECTIVES("moduleDirectives"),
        /** {@link _moduleInfo#MODULE_DIRECTIVES} */
        MODULE_DIRECTIVE("moduleDirective"),

        /** {@link _moduleProvides#MODULE_NAMES}, {@link _moduleOpens#MODULE_NAMES}, {@link _moduleExports#MODULE_NAMES} */
        MODULE_NAMES("moduleNames"),

        /**{@link org.jdraft._java._withName} ... */
        NAME("name", _java._withName.class),

        /**{@link _assignExpr#OPERATOR}, {@link _binaryExpr#OPERATOR}, {@link _unaryExpr#OPERATOR}*/
        OPERATOR("operator"),

        /** {@link _annotation#PACKAGE}, {@link _class#PACKAGE}, {@link _enum#PACKAGE}, {@link _interface#PACKAGE},
         * {@link _packageInfo#PACKAGE}*/
        PACKAGE("package"),

        /** {@link org.jdraft._params._withParams} {@link _params#PARAMS}, {@link _constructor#PARAMS}, {@link _lambdaExpr#PARAMS}, {@link _method#PARAMS} */
        PARAMS("params", _params._withParams.class),
        /**{@link _catch#PARAM} {@link _params#PARAMS} */
        PARAM("param"),

        /** {@link org.jdraft._receiverParam._withReceiverParam} {@link _method#RECEIVER_PARAM} {@link _constructor#RECEIVER_PARAM} */
        RECEIVER_PARAM("receiverParam", _receiverParam._withReceiverParam.class),

        /**{@link _binaryExpr#RIGHT}*/
        RIGHT( "right"),

        /**{@link org.jdraft._java._withScope}, {@link _fieldAccessExpr#SCOPE}, {@link _methodCallExpr#SCOPE}, {@link _methodRefExpr#SCOPE}, {@link _newExpr#SCOPE} */
        SCOPE("scope", _java._withScope.class),

        /**{@link _blockStmt#STATEMENTS} {@link _body#STATEMENTS} {@link _switchEntry#STATEMENTS} */
        STATEMENTS("statements"),
        /**{@link _blockStmt#STATEMENTS}, {@link _body#STATEMENTS}, {@link _labeledStmt#STATEMENT}, {@link _switchEntry#STATEMENTS} */
        STATEMENT("statement"),

        /**{@link _switchExpr#SWITCH_ENTRIES}, {@link _switchStmt#SWITCH_ENTRIES}*/
        SWITCH_ENTRIES("switchEntries"),
        /**{@link _switchExpr#SWITCH_ENTRIES}, {@link _switchStmt#SWITCH_ENTRIES}*/
        SWITCH_ENTRY("switchEntry"),

        /**{@link _switchExpr#SELECTOR}, {@link _switchStmt#SELECTOR}*/
        SELECTOR("selector"),

        /**{@link _assignExpr#TARGET}*/
        TARGET("target"),

        /**{@link _lineComment#TEXT}, {@link _blockComment#TEXT}, {@link _javadocComment#TEXT} */
        TEXT("text", _comment.class),

        /**{@link _ifStmt#THEN}, {@link _ternaryExpr#THEN}*/
        THEN("then"),

        /**{@link org.jdraft._throws._withThrows}, {@link _constructor#THROWS}, {@link _method#THROWS} */
        THROWS("throws", _throws._withThrows.class),
        /**{@link _throws#THROWS} */
        THROW("throw"),

        /**{@link _tryStmt#TRY_BODY}*/
        TRY_BODY("tryBody"),

        /**{@link org.jdraft._typeRef._withTypeRef} ...*/
        TYPE_REF("typeRef", _typeRef._withTypeRef.class),

        /**{@link org.jdraft._typeParams._withTypeParams} ... */
        TYPE_PARAMS("typeParams", _typeParams._withTypeParams.class),
        /** {@link _typeParams#TYPE_PARAMS} */
        TYPE_PARAM("typeParam"),

        /**{@link _constructorCallStmt#TYPE_ARGS}, {@link _fieldAccessExpr#TYPE_ARGS}, {@link _methodCallExpr#TYPE_ARGS},
         * {@link _methodRefExpr#TYPE_ARGS}, {@link _newExpr#TYPE_ARGS}, {@link _typeArgs#TYPE_ARGS} */
        TYPE_ARGS("typeArgs", _typeArgs._withTypeArgs.class),

        /**{@link _superExpr#TYPE_NAME} {@link _thisExpr#TYPE_NAME}*/
        TYPE_NAME("typeName"),

        /**{@link _forStmt#UPDATES}*/
        UPDATES("updates"),
        /**{@link _forStmt#UPDATES}*/
        UPDATE("update"),

        /**{@link _assignExpr#VALUE}, {@link _entryPair#VALUE}*/
        VALUE("value"),

        /**{@link _forEachStmt#VARIABLES} {@link _variablesExpr#VARIABLES} */
        VARIABLES("variables"),
        /**{@link _variablesExpr#VARIABLES}*/
        VARIABLE("variable"), //forEachStmt

        /**{@link _param#VAR_ARG_ANNO_EXPRS} */
        VAR_ARG_ANNO_EXPRS("varArgAnnoExprs"),

        /**{@link _tryStmt#WITH_RESOURCES}*/
        WITH_RESOURCES("withResources"),
        /**{@link _tryStmt#WITH_RESOURCES}*/
        WITH_RESOURCE("withResource")
        ;

        public final String name;
        public final Class categoryType;

        _id(String name){
            this.name = name;
            this.categoryType = null;
        }
        _id(String name, Class categoryType){
            this.name = name;
            this.categoryType = categoryType;
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