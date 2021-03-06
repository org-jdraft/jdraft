package org.jdraft;

import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Specifically designate a named feature and tools to get or set the value in an automated fashion
 * in the order the features appear within the AST
 *
 * <P>FROM a "Ast {@link com.github.javaparser.ast.Node} or {@link _tree._node}s perspective, defines all of the
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
     * for a valid entity, is this feature allowed to be null?
     * @return whether this feature is allowed to be null
     */
    boolean isNullable();

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

        /**{@link _annos._withAnnos} {@link _annos#ANNOS} {@link _annotation#ANNOS}
         * {@link _class#ANNOS} {@link _constructor#ANNOS}, {@link _field#ANNOS} {@link _interface#ANNOS}
         * {@link _method#ANNOS} {@link _param#ANNOS} {@link _receiverParam#ANNOS} {@link _variablesExpr#ANNOS}... */
        ANNOS("annos", _annos._withAnnos.class, 0b00000000000000000000000000000001), /** i.e. @Deprecated @NotNull */
        /**{@link _annos#ANNOS} */
        ANNO_EXPR("annoExpr",0b00000000000000000000000000000010), /** i.e. @Deprecated */

        /** {@link _newExpr#ANONYMOUS_BODY_MEMBERS} */
        ANONYMOUS_BODY_MEMBERS("anonymousBodyMembers",0b00000000000000000000000000000011),

        /** {@link _args#ARGS}, {@link _constant#ARGS},  {@link _methodCallExpr#ARGS}
         *  {@link _constructorCallStmt#ARGS}, {@link _newExpr#ARGS}*/
        ARGS("args", _args._withArgs.class, 0b00000000000000000000000000000100),
        /** {@link _args#ARGS} */
        ARG("arg", 0b00000000000000000000000000000101),

        /** {@link _newArrayExpr#DIMENSIONS} */
        ARRAY_DIMENSIONS("arrayDimensions", 0b00000000000000000000000000000110),
        /** {@link _newArrayExpr#DIMENSIONS} */
        ARRAY_DIMENSION("arrayDimension", 0b00000000000000000000000000000111),

        /**
         * {@link _catch#BODY} {@link _constructor#BODY} {@link _doStmt#BODY}, {@link _forEachStmt#BODY},
         * {@link _forStmt#BODY}, {@link _initBlock#BODY}, {@link _lambdaExpr#BODY}, {@link _method#BODY},
         * {@link _synchronizedStmt#BODY}, {@link _whileStmt#BODY}
         */
        BODY("body", _body._withBody.class, 0b00000000000000000000000000001000),

        /**{@link _switchCase#BODY_TYPE}*/
        BODY_TYPE("bodyType", 0b00000000000000000000000000001001),

        /**{@link _switchCase#CASE_EXPRESSIONS}*/
        CASE_EXPRESSIONS("caseExpressions", 0b00000000000000000000000000001010),
        /**{@link _switchCase#CASE_EXPRESSIONS}*/
        CASE_EXPRESSION("caseExpression", 0b00000000000000000000000000001011),

        /** {@link _tryStmt#CATCH_CLAUSES}*/
        CATCH_CLAUSES( "catchClauses",0b00000000000000000000000000001100),
        /** {@link _tryStmt#CATCH_CLAUSES}*/
        CATCH_CLAUSE("catchClause",0b00000000000000000000000000001101),

        /**{@link _assertStmt#CHECK}*/
        CHECK("check",0b00000000000000000000000000001110),

        /**{@link _localClassStmt#CLASS} */
        CLASS("class",0b00000000000000000000000000001111),

        /** {@link _project#CODE_UNITS}*/
        CODE_UNITS("codeUnits", 0b00000000000000000000000000010000), //project
        /** {@link _project#CODE_UNITS}*/
        CODE_UNIT("codeUnit",0b00000000000000000000000000010001),

        /**{@link _forStmt#COMPARE}*/
        COMPARE("compare",0b00000000000000000000000000010010),

        /** {@link org.jdraft._java._withCondition}, {@link _doStmt#CONDITION}, {@link _ifStmt#CONDITION},
         * {@link _ternaryExpr#CONDITION}, {@link _whileStmt#CONDITION} */
        CONDITION("condition", _java._withCondition.class, 0b00000000000000000000000000010011),

        /** {@link _annoMember#DEFAULT} */
        DEFAULT("default",0b00000000000000000000000000010100),

        /**{@link _ifStmt#ELSE}, {@link _ternaryExpr#ELSE}*/
        ELSE("else",0b00000000000000000000000000010101),

        /** {@link _anno#ENTRY_PAIRS} */
        ENTRY_PAIRS("entryPairs",0b00000000000000000000000000010110),
        /** {@link _anno#ENTRY_PAIRS} */
        ENTRY_PAIR("entryPair",0b00000000000000000000000000010111),

        /**
         * {@link org.jdraft._java._withExpression} {@link _arrayDimension#EXPRESSION}, {@link _castExpr#EXPRESSION},
         * {@link _exprStmt#EXPRESSION}, {@link _instanceOfExpr#EXPRESSION}, {@link _parenthesizedExpr#EXPRESSION},
         * {@link _returnStmt#EXPRESSION}, {@link _synchronizedStmt#EXPRESSION}, {@link _throwStmt#EXPRESSION},
         * {@link _unaryExpr#EXPRESSION}, {@link _yieldStmt#EXPRESSION}
         */
        EXPRESSION("expression", _java._withExpression.class,0b00000000000000000000000000011000),

        /** {@link _typeParam#EXTENDS_TYPE_BOUND} */
        EXTENDS_TYPE_BOUNDS("extendsTypeBounds",0b00000000000000000000000000011001),
        EXTENDS_TYPE_BOUND("extendsTypeBound",0b00000000000000000000000000011010),

        /**{@link _class#EXTENDS} {@link _interface#EXTENDS}*/
        EXTENDS("extends", _type._withExtends.class,0b00000000000000000000000000011011),
        /**{@link _class#EXTENDS} {@link _interface#EXTENDS}*/
        EXTEND("extend",0b00000000000000000000000000011100),

        /**{@link _tryStmt#FINALLY_BODY}*/
        FINALLY_BODY( "finallyBody",0b00000000000000000000000000011101),

        /**{@link _methodRefExpr#IDENTIFIER}*/
        IDENTIFIER("identifier",0b00000000000000000000000000011110),

        /**{@link _arrayAccessExpr#INDEX}*/
        INDEX("index",0b00000000000000000000000000011111),

        /**{@link org.jdraft._type._withImplements}, {@link _class#IMPLEMENTS}, {@link _enum#IMPLEMENTS}*/
        IMPLEMENTS("implements", _type._withImplements.class, 0b00000000000000000000000000100000),
        IMPLEMENT("implement", 0b00000000000000000000000000100001),

        /**{@link _annotation#IMPORTS}, {@link _class#IMPORTS}, {@link _enum#IMPORTS},{@link _interface#IMPORTS},
         * {@link _packageInfo#IMPORTS},{@link _moduleInfo#IMPORTS} {@link _imports#IMPORTS}*/
        IMPORTS("imports",0b00000000000000000000000000100010),
        /**{@link _imports#IMPORTS} */
        IMPORT("import",0b00000000000000000000000000100011),

        /**{@link _arrayInitExpr#INITS}*/
        INITS("inits",0b00000000000000000000000000100100),
        /** {@link _variable#INIT} {@link _newArrayExpr#INIT}, {@link _arrayInitExpr#INITS},
         * {@link _field#INIT}, {@link _forStmt#INIT}*/
        INIT("init", 0b00000000000000000000000000100101),

        /** {@link _param#IS_FINAL} */
        IS_FINAL("isFinal",0b00000000000000000000000000100110),

        /**
         * is the body implemented, or (abstract/NOT implemented)
         * <CODE>void m(){}</CODE> has an IMPLEMENTED BODY because its an empty block statement
         * -vs-
         * <CODE>void m();</CODE> has a NOT IMPLEMENTED BODY because ";" )
         * {@link _body#IS_IMPLEMENTED} */
        IS_IMPLEMENTED( "isImplemented",0b00000000000000000000000000100111),

        /** {@link _moduleInfo#IS_OPEN} */
        IS_OPEN("isOpen", 0b00000000000000000000000000101000),

        /**{@link _lambdaExpr#IS_PARAM_PARENTHESIZED}*/
        IS_PARAM_PARENTHESIZED("isParamParenthesized",0b00000000000000000000000000101001),

        /** {@link _import#IS_STATIC} {@link _initBlock#IS_STATIC} */
        IS_STATIC("isStatic",0b00000000000000000000000000101010),

        /**{@link _constructorCallStmt#IS_SUPER} */
        IS_SUPER("isSuper",0b00000000000000000000000000101011),

        /**{@link _constructorCallStmt#IS_THIS} */
        IS_THIS("isThis",0b00000000000000000000000000101100),

        /** {@link _moduleRequires#IS_TRANSITIVE} */
        IS_TRANSITIVE("isTransitive", 0b00000000000000000000000000101101),

        /**{@link _import#IS_WILDCARD}*/
        IS_WILDCARD("isWildcard", 0b00000000000000000000000000101110),

        /** {@link _param#IS_VAR_ARG}*/
        IS_VAR_ARG("isVarArg",0b00000000000000000000000000101111),

        /**{@link _forEachStmt#ITERABLE}*/
        ITERABLE("iterable", 0b00000000000000000000000000110000),

        /**
         * {@link org.jdraft._javadocComment._withJavadoc}
         * {@link _annotation#JAVADOC}, {@link _class#JAVADOC}, {@link _enum#JAVADOC}, {@link _interface#JAVADOC},
         * {@link _constructor#JAVADOC} {@link _method#JAVADOC}
         */
        JAVADOC("javadoc", _javadocComment._withJavadoc.class, 0b00000000000000000000000000110000),

        /**{@link _breakStmt#LABEL}, {@link _labeledStmt#LABEL}, {@link _continueStmt#LABEL} */
        LABEL("label",0b00000000000000000000000000110001),

        /**{@link _binaryExpr}*/
        LEFT( "left",0b00000000000000000000000000110010),

        //values like int, float, etc. are stored as string b/c "0b1" & "1" & "0x1"
        //are mean the same value but represented differently
        /**{@link _expr._literal} {@link _intExpr#LITERAL_VALUE}, {@link _doubleExpr#LITERAL_VALUE},
         * {@link _booleanExpr#LITERAL_VALUE}, {@link _stringExpr#LITERAL_VALUE},{@link _charExpr#LITERAL_VALUE},
         * {@link _textBlockExpr#LITERAL_VALUE) */
        LITERAL_VALUE("literalValue", 0b00000000000000000000000000110011),

        /** {@link _class#MEMBERS}, {@link _enum#MEMBERS}, {@link _annotation#MEMBERS}, {@link _interface#MEMBERS}, {@link _constant#MEMBERS} */
        MEMBERS("members", 0b00000000000000000000000000110100),
        /** {@link _class#MEMBERS}, {@link _enum#MEMBERS}, {@link _annotation#MEMBERS}, {@link _interface#MEMBERS}, {@link _constant#MEMBERS} */
        MEMBER("member",0b00000000000000000000000000110101),

        /**{@link _assertStmt#MESSAGE}*/
        MESSAGE("message",0b00000000000000000000000000110110),

        /**
         * {@link _annotation#MODIFIERS}, {@link _class#MODIFIERS},{@link _constructor#MODIFIERS},
         * {@link _annoMember#MODIFIERS}, {@link _enum#MODIFIERS}, {@link _field#MODIFIERS},{@link _interface#MODIFIERS},
         * {@link _method#MODIFIERS}, {@link _modifiers#MODIFIERS}, {@link _variablesExpr#MODIFIERS}
         */
        MODIFIERS("modifiers", _modifiers._withModifiers.class, 0b00000000000000000000000000110111),
        /** {@link _modifiers#MODIFIERS}*/
        MODIFIER("modifier", 0b00000000000000000000000000111000),

        /** {@link _moduleInfo#MODULE_DIRECTIVES} */
        MODULE_DIRECTIVES("moduleDirectives", 0b00000000000000000000000000111001),
        /** {@link _moduleInfo#MODULE_DIRECTIVES} */
        MODULE_DIRECTIVE("moduleDirective", 0b00000000000000000000000000111010),

        /** {@link _moduleProvides#MODULE_NAMES}, {@link _moduleOpens#MODULE_NAMES}, {@link _moduleExports#MODULE_NAMES} */
        MODULE_NAMES("moduleNames", 0b00000000000000000000000000111011),
        /**{@link _moduleExports#MODULE_NAME}, {@link _moduleInfo#MODULE_NAME}, {@link _moduleOpens#MODULE_NAME}, {@link _moduleProvides#MODULE_NAME},
         * {@link _moduleRequires#MODULE_NAME}*/
        MODULE_NAME("moduleName", 0b00000000000000000000000000111100),

        /**{@link _anno#NAME}, {@link _arrayAccessExpr#EXPRESSION}, {@link _class#NAME}, {@link _constant#NAME},
         * {@link _constructor#NAME}, {@link _annoMember#NAME}, {@link _annoEntryPair#NAME}, {@link _enum#NAME},
         * {@link _field#NAME}, {@link _fieldAccessExpr#NAME}, {@link _import#NAME}, {@link _interface#NAME},
         * {@link _method#NAME}, {@link _methodCallExpr#NAME}, {@link _name#NAME}, {@link _nameExpr#NAME},
         * {@link _package#NAME}, {@link _param#NAME}, {@link _receiverParam#NAME}, {@link _variable#NAME}
         */
        NAME("name", _java._withName.class, 0b00000000000000000000000000111101),

        /**{@link _assignExpr#OPERATOR}, {@link _binaryExpr#OPERATOR}, {@link _unaryExpr#OPERATOR}*/
        OPERATOR("operator",0b00000000000000000000000000111110),

        /** {@link _annotation#PACKAGE}, {@link _class#PACKAGE}, {@link _enum#PACKAGE}, {@link _interface#PACKAGE},
         * {@link _packageInfo#PACKAGE}*/
        PACKAGE("package",0b00000000000000000000000000111111),

        /** {@link _params#PARAMS}, {@link _constructor#PARAMS},{@link _lambdaExpr#PARAMS}, {@link _method#PARAMS} */
        PARAMS("params", _params._withParams.class, 0b00000000000000000000000001000000),
        /**{@link _catch#PARAM} {@link _params#PARAMS} */
        PARAM("param", 0b00000000000000000000000001000001),

        /** {@link _method#RECEIVER_PARAM} {@link _constructor#RECEIVER_PARAM} */
        RECEIVER_PARAM("receiverParam", _receiverParam._withReceiverParam.class, 0b00000000000000000000000001000010),

        /**{@link _binaryExpr#RIGHT}*/
        RIGHT("right", 0b00000000000000000000000001000011),

        /**{@link _fieldAccessExpr#SCOPE}, {@link _methodCallExpr#SCOPE}, {@link _methodRefExpr#SCOPE}, {@link _newExpr#SCOPE} */
        SCOPE("scope", _java._withScope.class, 0b00000000000000000000000001000100),

        /**{@link _blockStmt#STATEMENTS} {@link _body#STATEMENTS} {@link _switchCase#STATEMENTS} */
        STATEMENTS("statements", 0b00000000000000000000000001000101),
        /**{@link _blockStmt#STATEMENTS}, {@link _body#STATEMENTS}, {@link _labeledStmt#STATEMENT}, {@link _switchCase#STATEMENTS} */
        STATEMENT("statement", 0b00000000000000000000000001000110),

        /**{@link _switchExpr#SWITCH_ENTRIES}, {@link _switchStmt#SWITCH_ENTRIES}*/
        SWITCH_ENTRIES("switchEntries", 0b00000000000000000000000001000111),
        /**{@link _switchExpr#SWITCH_ENTRIES}, {@link _switchStmt#SWITCH_ENTRIES}*/
        SWITCH_ENTRY("switchEntry",0b00000000000000000000000001001000),

        /**{@link _switchExpr#SELECTOR}, {@link _switchStmt#SELECTOR}*/
        SELECTOR("selector", 0b00000000000000000000000001001001),

        /**{@link _assignExpr#TARGET}*/
        TARGET("target",0b00000000000000000000000001001010),

        /**{@link _lineComment#TEXT}, {@link _blockComment#TEXT}, {@link _javadocComment#TEXT} */
        TEXT("text", _comment.class, 0b00000000000000000000000001001011),

        /**{@link _ifStmt#THEN}, {@link _ternaryExpr#THEN}*/
        THEN("then", 0b00000000000000000000000001001100),

        /**{@link _constructor#THROWS}, {@link _method#THROWS} */
        THROWS("throws", _throws._withThrows.class, 0b00000000000000000000000001001101),
        /**{@link _throws#THROWS} */
        THROW("throw",0b00000000000000000000000001001110),

        /**{@link _tryStmt#TRY_BODY}*/
        TRY_BODY("tryBody", 0b00000000000000000000000001001111),

        /**{@link _newArrayExpr#TYPE}, {@link _castExpr#TYPE} {@link _classExpr#TYPE}, {@link _annoMember#TYPE},
         * {@link _field#TYPE}, {@link _instanceOfExpr#TYPE},{@link _method#TYPE}, {@link _newExpr#TYPE},
         * {@link _param#TYPE}, {@link _receiverParam#TYPE},{@link _typeExpr#TYPE}, {@link _typeRef#TYPE},
         * {@link _variable#TYPE}...*/
        TYPE("typeRef", _typeRef._withType.class, 0b00000000000000000000000001010000),

        /** {@link _class}, {@link _constructor}, {@link _interface}, {@link _method}, {@link _typeParams} */
        TYPE_PARAMS("typeParams", _typeParams._withTypeParams.class, 0b00000000000000000000000001010001),
        /** {@link _typeParams#TYPE_PARAMS} */
        TYPE_PARAM("typeParam", 0b00000000000000000000000001010010),

        /**{@link _constructorCallStmt#TYPE_ARGS}, {@link _fieldAccessExpr#TYPE_ARGS}, {@link _methodCallExpr#TYPE_ARGS},
         * {@link _methodRefExpr#TYPE_ARGS}, {@link _newExpr#TYPE_ARGS}, {@link _typeArgs#TYPE_ARGS} */
        TYPE_ARGS("typeArgs", _typeArgs._withTypeArgs.class, 0b00000000000000000000000001010011),
        TYPE_ARG("typeArg", 0b00000000000000000000000001010100),

        /**{@link _superExpr#TYPE_NAME} {@link _thisExpr#TYPE_NAME}*/
        TYPE_NAME("typeName", 0b00000000000000000000000001010101),

        /**{@link _forStmt#UPDATES}*/
        UPDATES("updates", 0b00000000000000000000000001010110),
        /**{@link _forStmt#UPDATES}*/
        UPDATE("update", 0b00000000000000000000000001010111),

        /**{@link _assignExpr#VALUE}, {@link _annoEntryPair#VALUE}*/
        VALUE("value", 0b00000000000000000000000001011000),

        /**{@link _forEachStmt#VARIABLES} {@link _variablesExpr#VARIABLES} */
        VARIABLES("variables",0b00000000000000000000000001011001),
        /**{@link _variablesExpr#VARIABLES}*/
        VARIABLE("variable",0b00000000000000000000000001011010),

        /**{@link _param#VAR_ARG_ANNO_EXPRS} */
        VAR_ARG_ANNO_EXPRS("varArgAnnoExprs",0b00000000000000000000000001011011),

        /**{@link _tryStmt#WITH_RESOURCES}*/
        WITH_RESOURCES("withResources", 0b00000000000000000000000001011100),
        /**{@link _tryStmt#WITH_RESOURCES}*/
        WITH_RESOURCE("withResource",0b00000000000000000000000001011101);

        /** name used to identify this feature */
        public final String name;

        /** Nullable type that is associated with this feature */
        public final Class categoryType;

        public final int id;

        _id(String name, int id){
            this.name = name;
            this.id = id;
            this.categoryType = null;
        }
        _id(String name, Class categoryType, int id){
            this.name = name;
            this.id = id;
            this.categoryType = categoryType;
        }

        public String toString(){
            if( this.categoryType == null ){
                return this.name;
            }
            return this.categoryType.getSimpleName()+"."+this.name;
        }
    }

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
        public Class<? extends _E> [] featureImplementationClasses;

        /**
         * Does the order matter to the semantics (i.e. to test for equality)
         * this is TRUE for things like {@link _params}, {@link _args}
         */
        public Boolean isOrdered = false;

        /** Is this feature allowed to be null (and the target be valid)?*/
        public Boolean nullable = false;

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

        /**
         * Are the many items in the feature set of the same implementation type?
         * @return
         */
        public boolean isHomogenous(){
            return this.featureImplementationClasses == null || this.featureImplementationClasses.length == 0;
        }

        /**
         * the gamut of possible types that can be the Feature Implementation
         * (NULL or empty if the set is HOMOGENOUS)
         * @param featureImplementationClass
         * @return
         */
        public _many<_T, _E> featureImplementations(Class<? extends _E>... featureImplementationClass ){
            this.featureImplementationClasses = featureImplementationClasses;
            return this;
        }

        /**
         * Does the order have (semantic) meaning
         * @param isOrdered
         * @return
         */
        public _many<_T, _E> setOrdered(Boolean isOrdered){
            this.isOrdered = isOrdered;
            return this;
        }

        public _many<_T, _E> setNullable(Boolean isNullable){
            this.nullable = isNullable;
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

        @Override
        public boolean isNullable() {
            return this.nullable;
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

        public BiConsumer<_T, List<_E>> setter(){
            return this.setter;
        }

        public String toString(){
            return this.targetClass.getSimpleName()+"."+featureId;
        }
    }

    /**
     * A tool for operating on a specific feature that is a single entity (i.e. a {@link _tree._node})
     * within an instance of the targetClass
     *
     * @param <_T>
     * @param <_F>
     */
    class _one<_T, _F> implements _feature<_T, _F> {
        /** the concrete target ({@link _tree._node}, {@link _tree._group}, {@link _tree._orderedGroup}) implementation */
        public final Class<_T> targetClass;

        /** the class or interface type of the feature */
        public final Class<_F> featureClass;

        /** concrete implementation classes that can be used for this feature (i.e. if the featureClass is {@link _expr} */
        public Class <? extends _F>[] featureImplementationClasses;

        public final _id feature;
        public final Function<_T, _F> getter;
        public final BiConsumer<_T, _F> setter;
        public final Function<String,_T> targetParser;
        public Boolean nullable;

        public _one(Class<_T> targetClass, Class<_F>featureClass, _id feature, Function<_T,_F> getter, BiConsumer<_T,_F> setter, Function<String, _T> targetParser){
            this.targetClass = targetClass;
            this.featureClass = featureClass;
            this.feature = feature;
            this.getter = getter;
            this.setter = setter;
            this.targetParser = targetParser;
        }

        public boolean isNullable(){
            return this.nullable;
        }

        public _one<_T, _F> setNullable(Boolean isNullable){
            this.nullable = isNullable;
            return this;
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
     * Immutable instance for all {@link _tree._node} types: describes the ordered features (i.e. edges to child nodes)
     * that can be get or set for this node within the AST (this allows programs to be written to automated tree
     * traversal on each node encounter)
     *
     * for example an {@link _anno} has
     *
     * @see _anno#FEATURES
     *
     * @param <_T>
     */
    class _features<_T>{

        public static <_T> _features<_T> of(Class<_T> targetClass, Function<String, _T> parser, _feature<_T,?>...features){
            return new _features<>(targetClass, parser, features);
        }

        /** Class of the Container of the feature (i.e. {@link _method} class for the {@link _method#NAME} feature) */
        public final Class<_T> targetClass;

        /** A Parser that can be used to convert from a String to a model instance */
        public final Function<String, _T> parser;

        /**
         * Note: this should be the features IN THE ORDER THEY APPEAR IN THE LANGUAGE
         * each feature MAY be null, a single entity, or a list of entities as defined by it's
         * {@link _feature} implementation
         */
        public final List<_feature<_T, ?>> featureList;

        /**
         * Are the order of the features tokens strictly ordered, i.e.
         * each feature contained in the _META MUST appear in a certain order
         * (This is ALMOST ALWAYS true, except for {@link _param#FEATURES}, where
         * you can interchange the final operator, and the annotations )
         */
        public boolean isStrictlyOrdered = true;

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

        private _features(Class<_T> targetClass, Function<String,_T> parser, _feature<_T, ?>... features ){
            this.targetClass = targetClass;
            this.parser = parser;
            this.featureList = Stream.of(features).collect(Collectors.toList());
        }

        /** get the target class for this specification */
        public Class<_T> getTargetClass(){
            return targetClass;
        }

        public Function<String, _T> getParser(){
            return parser;
        }

        /**
         * Parse the string into a _T instance
         * @param code the code representing the _features entity
         * @return a new instance of the _features entity
         */
        public _T parse(String...code){
            return parser.apply( Text.combine(code));
        }

        /**
         * Sets the feature order based on the instance
         * @param featureOrder
         * @return
         */
        public _features<_T> setFeatureOrder(BiFunction<_T, List<_feature<_T, ?>>, List<_feature<_T, ?>>> featureOrder ){
            this.featureOrder = featureOrder;
            return this;
        }

        /**
         * The tokens/features of the META are strictly ordered when they MUST
         * occur in the same arrangement, otherwise they are loosely ordered when
         * there can be some variation in the order of features/tokens
         *
         * @param isStrictlyOrdered
         * @return
         */
        public _features<_T> setStrictlyOrdered(Boolean isStrictlyOrdered){
            this.isStrictlyOrdered = isStrictlyOrdered;
            return this;
        }

        /**
         * Build a map with the key as a feature and the value as the feature value from instance t
         * @param targetType the targetType to "scrape" for features to put in the map
         * @return the featureMap
         */
        public Map<_feature<_T, ?>, Object> featureMap(_T targetType){
            Map<_feature<_T, ?>, Object> instanceMap = new HashMap<>();
            list(targetType).forEach(f-> instanceMap.put( f, f.get(targetType)));
            return instanceMap;
        }

        /**
         * The size (number of features)
         * @return the size (or number of features)
         */
        public int size(){
            return this.featureList.size();
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

        /**
         * Perform some action on all features that match the matchFn
         * @param matchFn matcher for features
         * @param actionFn the action to take with the matching features
         * @return the immutable meta<_T>
         */
        public _features<_T> forEach(_T instance, Predicate<_feature<_T, ?>> matchFn, Consumer<_feature<_T,?>> actionFn){
            list(instance, matchFn).forEach(actionFn);
            return this;
        }

        /**
         *
         * @param actionFn
         * @return
         */
        public _features<_T> forEach(_T instance, Consumer<_feature<_T,?>> actionFn){
            list(instance).forEach(actionFn);
            return this;
        }
    }
}
