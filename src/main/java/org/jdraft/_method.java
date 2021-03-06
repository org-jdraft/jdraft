package org.jdraft;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

import org.jdraft.macro._remove;
import org.jdraft.macro.macro;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

/**
 * Representation of the source code of a Java method (wraps a {@link MethodDeclaration} AST node)
 * i.e. <PRE>
 *     public static void main(String[] args){
 *         System.out.println("Hello World");
 *     }
 * </PRE>
 *
 * @author Eric
 */
public final class _method
        implements _javadocComment._withJavadoc<_method>, _annos._withAnnos<_method>,
        _java._withNameType<MethodDeclaration, _method>, _body._withBody<_method>, _throws._withThrows<_method>,
        _modifiers._withModifiers<_method>, _params._withParams<_method>,
        _typeParams._withTypeParams<_method>, _receiverParam._withReceiverParam<_method>,
        _modifiers._withStatic<_method>, _modifiers._withNative<_method>, _modifiers._withFinal<_method>,
        _modifiers._withAbstract<_method>, _modifiers._withSynchronized<_method>,
        _modifiers._withStrictFp<_method>, _java._declared<MethodDeclaration, _method> {

    public static final Function<String, _method> PARSER = s-> _method.of(s);

    public static _method of(String methodDecl) {
        return of(new String[]{methodDecl});
    }

    /**
     * Given the class name and the method name, find and return the
     * _method representation of the method
     * @param clazz the declaring class
     * @param methodName the name of the method
     * @return
     */
    public static _method of( Class clazz, String methodName){
        try {
            Method[] ms = clazz.getDeclaredMethods();
            List<Method> mls = Arrays.stream(ms).filter( m -> m.getName().equals(methodName)).collect(Collectors.toList());
            if( mls.size() == 0 ){
                throw new _jdraftException("No method with name "+ methodName+" on class "+clazz );
            }
            if( mls.size() == 1){
                return of( mls.get(0)); //theres only 1 method with this name
            }
            //I meant the NO-arg one
            Method m = clazz.getMethod(methodName, new Class[0]);
            return of( m );
        }catch(Exception e){
            throw new _jdraftException("Could not resolve "+clazz+" method "+ methodName, e);
        }
    }

    public static _method of( Class clazz, String methodName, Class<?>... parameterTypes){
        try {
            Method m = clazz.getMethod(methodName, parameterTypes);
            return of( m);
        }catch(Exception e){
            throw new _jdraftException("Could not resolve "+clazz+" method "+ methodName+" with "+parameterTypes, e);
        }
    }

    /**
     * Finds and returns the
     * @param m
     * @return
     */
    public static _method of( Method m ){
        Class declClass = m.getDeclaringClass();
        _class _c = _class.of(declClass);
        _method _mm =  _c.firstMethod(_m -> _m.getName().equals(m.getName()) && _m.hasParamsOf(m));
        if( _mm != null ){
            _mm.node().remove();
        }
        return _mm;
    }

    public static _method of( ){
        return of( new MethodDeclaration());
    }

    public static _method of(String... methodDecl) {

        //check for shortcut method
        String m = Text.combine(methodDecl);
        String[] ml = m.split(" ");
        if (ml.length == 1) {
            m = ml[0].trim();
            //"id"
            //"id<T>"
            //"id()"
            //"id();"
            //"id(){}"
            if (!m.endsWith(";") && !m.endsWith("}")) {
                //its a shortcut method (only providing NAME, no return TYPE)
                if (!m.endsWith(")")) {
                    m = m + "()";
                }
                return new _method(Ast.methodDeclaration("public void " + m + ";"));
            } else {
                return new _method(Ast.methodDeclaration("public void " + m));
            }
        }
        return new _method(Ast.methodDeclaration(methodDecl));
    }

    /**
     * Builds a _method from an anonymous Object BODY
     * <PRE>
     * _method _m = _method.of( new Object() {
     *    int x;
     *    public int getDiff(int x){
     *        return this.x - x;
     *    }
     * });
     *
     * //
     * assertEquals(_m,
     * _method.of("public int getDiff(int x){",
     *     "return this.x - x;",
     *     "}");
     * </PRE> NOTE: the method should be the only method declared in the BODY
     * -or- the only method that does NOT contain the @_remove annotation
     *
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static _method of(Object anonymousObjectContainingMethod) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        //removeIn all things that aren't METHODS or METHODS WITH @_remove
        bds.removeIf(b -> b.isAnnotationPresent(_remove.class) || (!(b instanceof MethodDeclaration)));
        //there should be only (1) method left, if > 1 take the first method
        MethodDeclaration md = (MethodDeclaration) bds.get(0);

        _method _m = _method.of(md);

        Method rm = null;
        Class anonClass = anonymousObjectContainingMethod.getClass();
        if( anonClass.getDeclaredMethods().length == 1 ){
            rm = anonClass.getDeclaredMethods()[0];
        } else {
            //Arrays.stream(anonymousObjectContainingMethod.getClass().getDeclaredMethods()).forEach(m -> System.out.println(m));
            rm = Arrays.stream(anonymousObjectContainingMethod.getClass().getDeclaredMethods()).filter(mm -> _m.hasParamsOf(mm)).findFirst().get();
        }

        macro.to(md, rm );
        _method _mm = _method.of(md);
        //call the macro on it
        //_method _mm = macro.to(anonymousObjectContainingMethod.getClass(), md); //of(md));

        //ensure we remove the parent reference to the method from the (old) code
        if( _mm.node().getParentNode().isPresent() ){
            _mm.node().getParentNode().get().remove( _mm.node() );
        }
        return _mm;
    }

    public static _method of(MethodDeclaration methodDecl) {
        return new _method(methodDecl);
    }

    /**
     * Build an return a main() method from the code within the lambda body
     * @param lambdaWithMainBody
     * @return
     */
    public static _method main(Consumer<String[]> lambdaWithMainBody){
        Statement st = Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]).getBody();
        _method _m = _method.of("public static void main(String[] args){}");
        if( st.isBlockStmt() ){
            _m.setBody( (BlockStmt)st);
        } else{
            _m.add(st);
        }
        return _m;
    }

    public _feature._features<_method> features(){
        return FEATURES;
    }

    public static _method get( String fieldDef){
        return get(_field.of(fieldDef));
    }

    /**
     * build and return a new getX(){} method
     * @param typeClass the type of the class
     * @param name the name of the property
     * @return a get method
     */
    public static _method get( Class typeClass, String name){
        return get(_typeRef.of(typeClass), name);
    }

    /**
     * build and return a new getX(){} method
     * @param _namedType instance of a _namedType with name and type
     * @return a get method
     */
    public static _method get( _java._withNameType _namedType ){
        return get(_namedType.getType(), _namedType.getName());
    }

    /**
     * build and return a new getX(){} method
     * @param _type the type of the class
     * @param name the name of the property
     * @return a get method
     */
    public static _method get( _typeRef _type, String name){
         if( _type.is("boolean")|| _type.is("Boolean")){
             return of("public "+_type.toString()+" is"+Character.toUpperCase( name.charAt(0))+name.substring(1)+"(){"+ System.lineSeparator()+
                     "    return this."+name+";"+
                     "}");
         }
         return of("public "+_type.toString()+" get"+Character.toUpperCase( name.charAt(0))+name.substring(1)+"(){"+ System.lineSeparator()+
                 "    return this."+name+";"+
                 "}");
    }

    /**
     *
     * @param fieldDef
     * @return
     */
    public static _method set( String fieldDef){
        return set(_field.of(fieldDef));
    }

    /**
     * build and return a new getX(){} method
     * @param typeClass the type of the class
     * @param name the name of the property
     * @return a get method
     */
    public static _method set( Class typeClass, String name){
        return set(_typeRef.of(typeClass), name);
    }

    /**
     * build and return a new getX(){} method
     * @param _namedType instance of a _namedType with name and type
     * @return a get method
     */
    public static _method set( _java._withNameType _namedType ){
        return set(_namedType.getType(), _namedType.getName());
    }

    public static _method set( _typeRef _type, String name){
        return of("public void set"+Character.toUpperCase( name.charAt(0)) + name.substring(1)+"("+_type.toString()+" "+ name+"){"+ System.lineSeparator()+
                    "    this."+name+" = "+name+";"+
                    "}");
    }

    @Override
    public _method setJavadoc(String... content) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _method setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.node()).setJavadocComment(astJavadocComment);
        return this;
    }

    private MethodDeclaration node;

    public _method(MethodDeclaration md) {
        this.node = md;
    }

    @Override
    public MethodDeclaration node() {
        return node;
    }

    @Override
    public MethodDeclaration nodeWithParameters(){
        return node;
    }

    /**
     * 
     * @param methodDecl
     * @return 
     */
    public boolean is(String... methodDecl) {
        String str = Text.combine(methodDecl);
        if( str.startsWith("$") && str.endsWith("$")){
            Stencil st = Stencil.of(str);
            if( st.isMatchAny() ){
                return true;
            }
        }
        try {
            _method _mm = of(methodDecl);

            //because we DONT know the context of the method (on interface, etc.)
            // lets add all of the implied modifiers to the _mm temp model
            NodeList<Modifier> mms = Modifiers.getImpliedModifiers(this.node);
            mms.forEach(mmm -> {
                _mm.node().addModifier(mmm.getKeyword());
            });
            return _mm.equals(this);
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isMain() {
        return IS_MAIN.test(this);
    }

    public _method copy() {
        return new _method(this.node.clone());
    }

    /**
     * Predicate to determine if a method is a main method
     */
    public static final Predicate<_method> IS_MAIN = m-> 
        m.isPublic() && m.isStatic() && m.getName().equals("main") && m.isVoid()
        && m.getParams().size() == 1 && m.getParam(0).isType(String[].class);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final _method other = (_method) obj;
        if (this.node == other.node) {
            return true; //two _method s pointing to the same MethodDeclaration
        }
        if( ! Expr.equalAnnos(node, other.node)){
            return false;
        }
        if (!Objects.equals(this.getBody(), other.getBody())) {
            return false;
        }
        if (this.hasJavadoc() != other.hasJavadoc()) {
            return false;
        }
        //if (this.hasJavadoc() && !Objects.equals(this.getJavadoc().getContent().trim(), other.getJavadoc().getContent().trim())) {
        //    return false;
        //}
        if (this.hasJavadoc() && !Objects.equals(this.getJavadoc().getText().trim(), other.getJavadoc().getText().trim())) {
            System.out.println("NOT EQUAL"+ System.lineSeparator()+this.getJavadoc().getText().trim()+" "+System.lineSeparator()+ " "+ other.getJavadoc().getText().trim());
            return false;
        }
        if (!Modifiers.modifiersEqual(this.node, other.node)) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getParams(), other.getParams())) {
            return false;
        }
        if (!Types.equal(node.getThrownExceptions(), other.node.getThrownExceptions())) {
            return false;
        }        
        if (!Objects.equals(this.getTypeParams(), other.getTypeParams())) {
            return false;
        }
        if (!Types.equal(node.getType(), other.node.getType())) {
            return false;
        }
        if (!Objects.equals(this.getReceiverParam(), other.getReceiverParam())) {
            return false;
        }
        return true;
    }

    public static _feature._one<_method, _javadocComment> JAVADOC = new _feature._one<>(_method.class, _javadocComment.class,
            _feature._id.JAVADOC,
            a -> a.getJavadoc(),
            (_method a, _javadocComment _jd) -> a.setJavadoc(_jd), PARSER);

    public static _feature._one<_method, _annos> ANNOS = new _feature._one<>(_method.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_method a, _annos _ta) -> a.setAnnos(_ta), PARSER);

    public static _feature._one<_method, _modifiers> MODIFIERS = new _feature._one<>(_method.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getModifiers(),
            (_method a, _modifiers _ms) -> a.setModifiers(_ms), PARSER);

    public static _feature._one<_method, _typeParams> TYPE_PARAMS = new _feature._one<>(_method.class, _typeParams.class,
            _feature._id.TYPE_PARAMS,
            a -> a.getTypeParams(),
            (_method a, _typeParams _tps) -> a.setTypeParams(_tps), PARSER);

    public static _feature._one<_method, _typeRef> TYPE = new _feature._one<>(_method.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_method a, _typeRef _tr) -> a.setType(_tr), PARSER);

    public static _feature._one<_method, String> NAME = new _feature._one<>(_method.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_method a, String s) -> a.setName(s), PARSER);

    public static _feature._one<_method, _receiverParam> RECEIVER_PARAM = new _feature._one<>(_method.class, _receiverParam.class,
            _feature._id.RECEIVER_PARAM,
            a -> a.getReceiverParam(),
            (_method a, _receiverParam _r) -> a.setReceiverParam(_r), PARSER);

    public static _feature._one<_method, _params> PARAMS = new _feature._one<>(_method.class, _params.class,
            _feature._id.PARAMS,
            a -> a.getParams(),
            (_method a, _params _p) -> a.setParams(_p), PARSER);

    public static _feature._one<_method, _throws> THROWS = new _feature._one<>(_method.class, _throws.class,
            _feature._id.THROWS,
            a -> a.getThrows(),
            (_method a, _throws _t) -> a.setThrows(_t), PARSER);

    public static _feature._one<_method, _body> BODY = new _feature._one<>(_method.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_method a, _body _b) -> a.setBody(_b), PARSER);

    public static _feature._features<_method> FEATURES = _feature._features.of(_method.class, PARSER,
            JAVADOC, ANNOS, MODIFIERS, TYPE_PARAMS, TYPE, NAME, RECEIVER_PARAM, PARAMS, THROWS, BODY );

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _method replace(MethodDeclaration replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        //the ORDER of the modifiers is unimportant
        Set<Modifier> modsSet = new HashSet<>();
        modsSet.addAll(this.getEffectiveAstModifiersList());

        hash = 23 * hash + Objects.hash(
                Expr.hashAnnos(node),
                this.getBody(),
                //this.getJavadoc(),
                modsSet, //this.getEffectiveModifiers(), //this.getModifiers(),
                this.getName(),
                this.getParams(),
                Types.hash(node.getThrownExceptions()),
                this.getTypeParams(),
                this.getReceiverParam(),
                Types.hash(node.getType()));
        if( this.node().hasJavaDocComment() ){

            hash = hash * getJavadoc().hashCode();
        }
        return hash;
    }
    
    @Override
    public _method setType(Type type) {
        this.node.setType(type);
        return this;
    }

    @Override
    public _typeRef getType() {
        return _typeRef.of(this.node.getType());
    }

    @Override
    public _method setName(String name) {
        this.node.setName(name);
        return this;
    }
   
    @Override
    public _throws getThrows() {
        return _throws.of(node);
    }

    /**
     * Match the reflective java.lang.reflect.Method to this _method's
     * parameters
     *
     * @param m the method
     * @return
     */
    public boolean hasParamsOf(java.lang.reflect.Method m) {
        java.lang.reflect.Type[] genericParameterTypes = m.getGenericParameterTypes();
        if( m.getParameterCount() > 0 ) {
        }
        List<_param> pl = this.listParams();
        if (genericParameterTypes.length != pl.size()) {
            return false;
        }
        for (int i = 0; i < genericParameterTypes.length; i++) {
            _typeRef _t = _typeRef.of(genericParameterTypes[i]);
            if (!pl.get(i).isType(_t)) {
                if (m.isVarArgs()
                    && //if last parameter and varargs
                    Types.equal(pl.get(i).getType().getElementType(),
                        _t.getElementType())) {
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Test to match a java reflect Method to a _method
     * @param _m the method to test
     * @param m the reflective method
     * @return true if they match, false otherwise
     */
    public static boolean match( _method _m, java.lang.reflect.Method m ){
        if( _m.getName().equals(m.getName()) && _m.hasParamsOf(m)){

            /** ARRG we have to handle Array Types returns because they return "[Z", "[B", ,"[S, "[F"... for class names*/
            //if( m.getGenericReturnType().)
            //System.out.println("GEN"+m.getGenericReturnType() );
            //System.out.println("GEN TYPE NANME "+m.getGenericReturnType().getTypeName() );
            //System.out.println("GEN"+m.getGenericReturnType(). );

            return _m.getType().is(m.getGenericReturnType());
        }
        return false;
    }

    @Override
    public _body getBody() {
        return _body.of(this.node);
    }

    @Override
    public _modifiers getModifiers() {
        return _modifiers.of(this.node);
    }

    @Override
    public NodeList<Modifier> getEffectiveAstModifiersList() {
        NodeList<Modifier> ims = Modifiers.getImpliedModifiers(this.node);

        if (ims == null) {
            return this.node.getModifiers();
        }
        NodeList<Modifier> mms = this.node.getModifiers();
        mms.forEach(m -> {
            if (!ims.contains(m)) {
                ims.add(m);
            }
        });
        return ims;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(node);
    }

    public SimpleName getNameNode() { return this.node.getName(); }

    @Override
    public String getName() {
        return node.getNameAsString();
    }
    
    @Override
    public _params getParams() {
        return _params.of(node);
    }
    
    @Override
    public boolean is(MethodDeclaration methodDeclaration) {
        try {
            return of(methodDeclaration).equals(this);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.node.toString();
    }

    public boolean isPackagePrivate(){
        NodeList<Modifier> mods = getEffectiveAstModifiersList();
        return !mods.contains(Modifier.publicModifier()) && !mods.contains(Modifier.privateModifier()) && !mods.contains(Modifier.protectedModifier());
    }

    public boolean isPublic() {
        return this.node.isPublic() || getEffectiveAstModifiersList().contains(Modifier.publicModifier());
    }

    public boolean isProtected() {
        return this.node.isProtected();
    }

    public boolean isPrivate() {
        return this.node.isPrivate();
    }

    public boolean isDefault() {
        return this.node.isDefault();
    }

    @Override
    public boolean isStatic() {
        return this.node.isStatic();
    }

    @Override
    public boolean isAbstract() {
        /**
         * Cant just look at the MODIFIERS, I also have to check if this method
         * lacks a BODY
         */
        if (!this.node.getBody().isPresent()) {
            return true;
        }
        return this.node.isAbstract();
    }
    
    @Override
    public boolean isFinal() {
        return this.node.isFinal();
    }

    public _method setPublic() {
        this.node.setPrivate(false);
        this.node.setProtected(false);
        this.node.setPublic(true);
        return this;
    }

    public _method setProtected() {
        this.node.setPrivate(false);
        this.node.setProtected(true);
        this.node.setPublic(false);
        return this;
    }

    public _method setPrivate() {
        this.node.setPrivate(true);
        this.node.setProtected(false);
        this.node.setPublic(false);
        return this;
    }

    public _method setPackagePrivate() {
        this.node.setPrivate(false);
        this.node.setProtected(false);
        this.node.setPublic(false);
        return this;
    }

    @Override
    public _method setAbstract() {
        this.node.removeBody();
        return setAbstract(true);
    }
    
    @Override
    public _method setAbstract(boolean toSet) {
        this.node.setAbstract(toSet);
        if (toSet) {
            this.node.removeBody();
        } else {
            this.node.createBody();
        }
        return this;
    }
    
    @Override
    public _method setFinal(boolean toSet) {
        this.node.setFinal(toSet);
        return this;
    }    

    @Override
    public _method setBody(BlockStmt body) {
        if (body == null) {
            this.node.removeBody();
        } else {
            this.node.setBody(body);
        }
        return this;
    }

    @Override
    public _method clearBody() {
        if (this.node.getBody().isPresent()) {
            this.node.setBody( new BlockStmt() );
        }
        return this;
    }

    @Override
    public _method add(Statement... statements) {        
        if (!this.node.getBody().isPresent()) {
            this.node.createBody();
        }
        for (Statement statement : statements) {
            this.node.getBody().get().addStatement(statement);
        }
        return this;
    }

    @Override
    public _method add(int startStatementIndex, Statement... statements) {
        if (!this.node.getBody().isPresent()) {
            this.node.createBody();
        }
        for (int i = 0; i < statements.length; i++) {
            this.node.getBody().get().addStatement(i + startStatementIndex, statements[i]);
        }
        return this;
    }

    /**
     *
     * @author Eric
     * @param <_WM>
     */
    public interface _withMethods<_WM extends _withMethods>
            extends _java._domain {

        List<_method> listMethods();

        default List<_method> listMethods(String name) {
            return listMethods().stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
        }

        default List<_method> listMethods(Predicate<_method> _methodMatchFn){
            return listMethods().stream().filter(_methodMatchFn).collect(Collectors.toList());
        }

        default boolean hasMethods() {
            return !listMethods().isEmpty();
        }

        default boolean hasMethod(String name){
            return !listMethods(name).isEmpty();
        }

        default boolean hasMethod( Predicate<_method> _matchFn){
            return !listMethods(_matchFn).isEmpty();
        }

        default boolean hasMethod( Class returnType){
            return !listMethods(m-> m.isType(returnType)).isEmpty();
        }

        default boolean hasMethod( Class returnType, String name){
            return !listMethods(m-> m.isNamed(name) && m.isType(returnType)).isEmpty();
        }

        default boolean hasMethod( Class returnType, String name, Class...parameterTypes){
            return getMethod(returnType, name, parameterTypes) != null;
        }

        default _method getMethod(String name){
            List<_method>_ms = listMethods(m-> m.isNamed(name) );
            if (_ms.isEmpty()){
                return null;
            }
            return _ms.get(0);
        }

        default _method getMethod( Class returnType, String name){
            List<_method>_ms = listMethods(m-> m.isNamed(name) && m.isType(returnType));
            if (_ms.isEmpty()){
                return null;
            }
            return _ms.get(0);
        }

        default _method getMethod( Class returnType, String name, Class...parameterTypes){
            List<_method>_ms = listMethods(m-> m.isNamed(name) && m.isType(returnType) && m.getParams().isTypes(parameterTypes));
            if (_ms.isEmpty()){
                return null;
            }
            return _ms.get(0);
        }

        /**
         * Check if all individual arg ({@link _method}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allMethods( Predicate<_method> matchFn){
            return listMethods().stream().allMatch(matchFn);
        }

        /**
         * Find and return the first method declared in the _withMethods with this name
         * @param methodName
         * @return
         */
        default _method firstMethodNamed(String methodName) {
            List<_method> lm = listMethods(methodName);
            if (lm.isEmpty()) {
                return null;
            }
            return lm.get(0);
        }

        default _method firstMethod(Predicate<_method> _methodMatchFn){
            List<_method> lm = listMethods(_methodMatchFn);
            if (lm.isEmpty()) {
                return null;
            }
            return lm.get(0);
        }

        /**
         * Apply this method Consumer to all methods and return the WithMethods container
         * @param _actionFn the Consumer to apply to all methods
         * @return the modified withMethods Consumer
         */
        default _WM toMethods(Consumer<_method> _actionFn) {
            return toMethods(m -> true, _actionFn);
        }

        /**
         * Apply this method Consumer to all methods that match the _matchFn and return the withMethods Container
         * @param _matchFn the predicate for matching which methods
         * @param _actionFn the consumer to apply to all methods
         * @return the modified withMethods container
         */
        default _WM toMethods(Predicate<_method> _matchFn, Consumer<_method> _actionFn) {
            listMethods(_matchFn).forEach(_actionFn);
            return (_WM) this;
        }

        /**
         * Apply this method Consumer to all methods and return the _methods that were consumed
         * @param _actionFn the Consumer to apply to all methods
         * @return the modified withMethods Consumer
         */
        default List<_method> forMethods(Consumer<_method> _actionFn) {
            return forMethods(m -> true, _actionFn);
        }

        /**
         * Apply this method Consumer to all methods that match the _matchFn and return the _methods that were consumed
         * @param _matchFn the predicate for matching which methods
         * @param _actionFn the consumer to apply to all methods
         * @return the modified withMethods container
         */
        default List<_method> forMethods(Predicate<_method> _matchFn, Consumer<_method> _actionFn) {
            List<_method> matching = listMethods(_matchFn);
            matching.forEach(_actionFn);
            return matching;
        }

        default _WM removeMethod(_method _m) {
            this.toMethods(m -> m.equals(_m), m-> m.node.removeForced() );
            return removeMethod( _m.node);
        }

        default _WM removeMethod(MethodDeclaration astM) {
            this.toMethods(m -> m.equals(_method.of(astM)), m-> m.node.removeForced() );
            return (_WM) this;
        }

        default _WM removeMethods(Predicate<_method> methodPredicate) {
            listMethods(methodPredicate).forEach(_m -> removeMethod(_m));
            return (_WM) this;
        }

        _WM addMethod(MethodDeclaration method);

        default _WM addMethod(String... method) {
            return addMethod(Ast.methodDeclaration(method));
        }

        default _WM addMethod(_method _m) {
            return addMethod(_m.node());
        }

        default _WM addMethods(_method... ms) {
            Arrays.stream(ms).forEach(m -> addMethod(m));
            return (_WM) this;
        }

        /**
         * Pass in the BODY of a main method and _1_build/add a
         *
         * public static void main(String[] args) {...} method
         *
         * @param mainMethodBody the BODY of the main method
         * @return the modified T
         */
        default _WM main(String... mainMethodBody) {
            _method _m = _method.of("public static void main(String[] args){ }");
            _m.add(mainMethodBody);
            return addMethod(_m);
        }

        /**
         * Build a public static void main(String[] args) {...} method with the
         * contents of the lambda
         *
         * @param body
         * @return
         */
        default _WM main(Expr.Command body) {
            LambdaExpr le = Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]);
            _method _m = _method.of("public static void main(String[] args){ }");
            if (le.getBody().isBlockStmt()) {
                _m.setBody(le.getBody().asBlockStmt());
            } else {
                _m.add(le.getBody());
            }
            //TODO? should I removeIn / replaceIn the main method if one is already found??
            return addMethod(_m);
        }

        /**
         * Build a public static void main(String[] args) {...} method with the
         * contents of the lambda
         *
         * @param body
         * @return
         */
        default _WM main(Consumer<String[]> body) {
            LambdaExpr le = Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]);
            _method _m = _method.of("public static void main(String[] args){ }");
            if (le.getBody().isBlockStmt()) {
                _m.setBody(le.getBody().asBlockStmt());
            } else {
                _m.add(le.getBody());
            }
            //TODO? should I removeIn / replaceIn the main method if one is already found??
            return addMethod(_m);
        }

        /**
         * 
         * @param methodDef
         * @return 
         */
        default _WM addMethod(String methodDef) {
            return addMethod(new String[]{methodDef});
        }

        /**
         * Adds a Draft method (with source declared in an anonymous class) to the 
         * type and returns the modified type
         * NOTE: ALSO adds any imports that exist on the public API to the type
         * 
         * @param anonymousObjectContainingMethod
         * @return 
         */
        default _WM addMethod(Object anonymousObjectContainingMethod) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            ObjectCreationExpr oce = Expr.newExpr(ste);
            if (oce == null || !oce.getAnonymousClassBody().isPresent()) {
                throw new _jdraftException("No anonymous Object containing a method provided ");
            }
            Optional<BodyDeclaration<?>> obd = oce.getAnonymousClassBody().get().stream()
                .filter(bd -> bd instanceof MethodDeclaration
                && !bd.asMethodDeclaration().getAnnotationByClass(_remove.class).isPresent()).findFirst();
            if (!obd.isPresent()) {
                throw new _jdraftException("Could not find Method in anonymous object body");
            }
            MethodDeclaration md = (MethodDeclaration) obd.get();
            //md.removeForced();

            Optional<CompilationUnit> oc = ((_tree._node)this).node().findCompilationUnit();
            if( oc.isPresent() ){
                CompilationUnit cu = oc.get();
                Set<Class> clazzes = _import.inferImportsFrom(anonymousObjectContainingMethod.getClass());    
                
                clazzes.forEach(c -> {
                    if( !c.isArray()){
                        cu.addImport(c);
                    } else{ //cant add import to array class, use component type instead 
                        cu.addImport(c.getComponentType());
                    }
                });
            }
            /*
            //reset the range to start
            if( md.getRange().isPresent() ) {
                Range r = md.getRange().get();
                int lineCount = r.getLineCount();
                System.out.println( "LINE COUNT "+ lineCount + md);
                md.setRange(md.getRange().get().withBeginLine(1).withEndLine(lineCount));
            }
             */
            //md.setRange( new Range(Position.HOME) );

            return addMethod( md ); //(MethodDeclaration) StaticJavaParser.parseBodyDeclaration( md.toString()) );
        }
    }

    public static String describeMethodSignature(_method _m) {
        StringBuilder sb = new StringBuilder();
        sb.append(_m.getName());
        sb.append("(");
        for (int i = 0; i < _m.getParams().size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(_m.getParam(i).getType());
            if (_m.getParam(i).isVarArg()) {
                sb.append("...");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
