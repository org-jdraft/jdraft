package org.jdraft;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

import org.jdraft._parameter.*;
import org.jdraft._anno.*;
import org.jdraft.macro._macro;
import org.jdraft.macro._remove;
import org.jdraft.macro.macro;

/**
 * Model of a Java method (wraps a {@link MethodDeclaration} AST node
 *
 * @author Eric
 */
public final class _method
        implements _javadoc._hasJavadoc<_method>, _anno._hasAnnos<_method>,
        _namedType<_method>, _body._hasBody<_method>, _throws._hasThrows<_method>,
        _modifiers._hasModifiers<_method>, _parameter._hasParameters<_method>,
        _typeParameter._hasTypeParameters<_method>, _receiverParameter._hasReceiverParameter<_method>,
        _modifiers._hasStatic<_method>, _modifiers._hasNative<_method>, _modifiers._hasFinal<_method>,
        _modifiers._hasAbstract<_method>, _modifiers._hasSynchronized<_method>,
        _modifiers._hasStrictFp<_method>, _member<MethodDeclaration, _method> {

    public static _method of(String methodDecl) {
        return of(new String[]{methodDecl});
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
                return new _method(Ast.method("public void " + m + ";"));
            } else {
                return new _method(Ast.method("public void " + m));
            }
        }
        return new _method(Ast.method(methodDecl));
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
        ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        //removeIn all things that aren't METHODS or METHODS WITH @_remove
        bds.removeIf(b -> b.isAnnotationPresent(_remove.class) || (!(b instanceof MethodDeclaration)));
        //there should be only (1) method left, if > 1 take the first method
        MethodDeclaration md = (MethodDeclaration) bds.get(0);

        _method _m = _method.of(md);

        Method rm = Arrays.stream(anonymousObjectContainingMethod.getClass().getDeclaredMethods()).filter(mm ->_m.hasParametersOf(mm)).findFirst().get();

        macro.to(md, rm );
        _method _mm = _method.of(md);
        //call the macro on it
        //_method _mm = macro.to(anonymousObjectContainingMethod.getClass(), md); //of(md));

        //ensure we remove the parent reference to the method from the (old) code
        if( _mm.ast().getParentNode().isPresent() ){
            _mm.ast().getParentNode().get().remove( _mm.ast() );
        }
        return _mm;
    }

    /*
    public static _method fromSignature(String signature) {
        String[] toks = signature.split(" ");
        if (toks.length == 1) {
            //single token shortcut... make it "public void"
            signature = "public void " + signature;
        }
        if (!signature.contains("(")) {
            signature = signature + "()";
        }
        signature = signature + "{}";
        return of(signature);
    }
     */

    public static _method of(MethodDeclaration methodDecl) {
        return new _method(methodDecl);
    }

    private final MethodDeclaration astMethod;

    public _method(MethodDeclaration md) {
        this.astMethod = md;
    }

    @Override
    public MethodDeclaration ast() {
        return astMethod;
    }

    /**
     * 
     * @param methodDecl
     * @return 
     */
    public boolean is(String... methodDecl) {
        try {
            _method _mm = of(methodDecl);

            //because we DONT know the context of the method (on interface, etc.)
            // lets add all of the implied modifiers to the _mm temp model
            NodeList<Modifier> mms = Ast.getImpliedModifiers(this.astMethod);
            mms.forEach(mmm -> {
                _mm.ast().addModifier(mmm.getKeyword());
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
        return new _method(this.astMethod.clone());
    }

    /**
     * Predicate to determine if a method is a main method
     */
    public static final Predicate<_method> IS_MAIN = m-> 
        m.isPublic() && m.isStatic() && m.getName().equals("main") && m.isVoid()
        && m.getParameters().size() == 1 && m.getParameter(0).isType(String[].class);

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
        if (this.astMethod == other.astMethod) {
            return true; //two _method s pointing to the same MethodDeclaration
        }
        if( ! Ex.equivalentAnnos(astMethod, other.astMethod)){
            return false;
        }
        if (!Objects.equals(this.getBody(), other.getBody())) {
            return false;
        }
        if (this.hasJavadoc() != other.hasJavadoc()) {
            return false;
        }
        if (this.hasJavadoc() && !Objects.equals(this.getJavadoc().getContent().trim(), other.getJavadoc().getContent().trim())) {
            return false;
        }
        if (!Ast.modifiersEqual(this.astMethod, other.astMethod)) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getParameters(), other.getParameters())) {
            return false;
        }
        if (!Ast.typesEqual(astMethod.getThrownExceptions(), other.astMethod.getThrownExceptions())) {
            return false;
        }        
        if (!Objects.equals(this.getTypeParameters(), other.getTypeParameters())) {
            return false;
        }
        if (!Ast.typesEqual(astMethod.getType(), other.astMethod.getType())) {
            return false;
        }
        if (!Objects.equals(this.getReceiverParameter(), other.getReceiverParameter())) {
            return false;
        }
        return true;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put(_java.Component.ANNOS, getAnnos());
        parts.put(_java.Component.BODY, getBody());
        parts.put(_java.Component.TYPE, getType());
        parts.put(_java.Component.PARAMETERS, getParameters());
        parts.put(_java.Component.MODIFIERS, getEffectiveModifiers());
        parts.put(_java.Component.JAVADOC, getJavadoc());
        parts.put(_java.Component.RECEIVER_PARAMETER, getReceiverParameter());
        parts.put(_java.Component.TYPE_PARAMETERS, getTypeParameters());
        parts.put(_java.Component.THROWS, getThrows());
        parts.put(_java.Component.NAME, getName());
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        //the ORDER of the modifiers is unimportant
        Set<Modifier> modsSet = new HashSet<>();
        modsSet.addAll(this.getEffectiveModifiers());

        hash = 23 * hash + Objects.hash(
                Ex.hashAnnos(astMethod),
                this.getBody(),
                this.getJavadoc(),
                modsSet, //this.getEffectiveModifiers(), //this.getModifiers(),
                this.getName(),
                this.getParameters(),
                Ast.typesHashCode(astMethod.getThrownExceptions()),
                this.getTypeParameters(),
                this.getReceiverParameter(),
                Ast.typeHash(astMethod.getType()));
        return hash;
    }
    
    @Override
    public _method type(Type type) {
        this.astMethod.setType(type);
        return this;
    }

    @Override
    public _typeRef getType() {
        return _typeRef.of(this.astMethod.getType());
    }

    @Override
    public _method name(String name) {
        this.astMethod.setName(name);
        return this;
    }
   
    @Override
    public _throws getThrows() {
        return _throws.of(astMethod);
    }

    /**
     * Match the reflective java.lang.reflect.Method to this _method's
     * parameters
     *
     * @param m the method
     * @return
     */
    public boolean hasParametersOf(java.lang.reflect.Method m) {
        java.lang.reflect.Type[] genericParameterTypes = m.getGenericParameterTypes();
        if( m.getParameterCount() > 0 ) {
        }
        List<_parameter> pl = this.listParameters();
        if (genericParameterTypes.length != pl.size()) {
            return false;
        }
        for (int i = 0; i < genericParameterTypes.length; i++) {
            _typeRef _t = _typeRef.of(genericParameterTypes[i]);
            if (!pl.get(i).isType(_t)) {
                if (m.isVarArgs()
                    && //if last parameter and varargs
                    Ast.typesEqual(pl.get(i).getType().getElementType(),
                        _t.getElementType())) {
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public _body getBody() {
        return _body.of(this.astMethod);
    }

    @Override
    public _modifiers getModifiers() {
        return _modifiers.of(this.astMethod);
    }

    @Override
    public NodeList<Modifier> getEffectiveModifiers() {
        NodeList<Modifier> ims = Ast.getImpliedModifiers(this.astMethod);

        if (ims == null) {
            return this.astMethod.getModifiers();
        }
        NodeList<Modifier> mms = this.astMethod.getModifiers();
        mms.forEach(m -> {
            if (!ims.contains(m)) {
                ims.add(m);
            }
        });
        return ims;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(astMethod);
    }

    @Override
    public String getName() {
        return astMethod.getNameAsString();
    }
    
    @Override
    public _parameters getParameters() {
        return _parameters.of(astMethod);
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
        return this.astMethod.toString();
    }

    public boolean isPackagePrivate(){
        NodeList<Modifier> mods = getEffectiveModifiers();
        return !mods.contains(Modifier.publicModifier()) && !mods.contains(Modifier.privateModifier()) && !mods.contains(Modifier.protectedModifier());
    }

    public boolean isPublic() {
        return this.astMethod.isPublic() || getEffectiveModifiers().contains(Modifier.publicModifier());
    }

    public boolean isProtected() {
        return this.astMethod.isProtected();
    }

    public boolean isPrivate() {
        return this.astMethod.isPrivate();
    }

    public boolean isDefault() {
        return this.astMethod.isDefault();
    }

    @Override
    public boolean isStatic() {
        return this.astMethod.isStatic();
    }

    @Override
    public boolean isAbstract() {
        /**
         * Cant just look at the MODIFIERS, I also have to check if this method
         * lacks a BODY
         */
        if (!this.astMethod.getBody().isPresent()) {
            return true;
        }
        return this.astMethod.isAbstract();
    }
    
    @Override
    public boolean isFinal() {
        return this.astMethod.isFinal();
    }

    public _method setPublic() {
        this.astMethod.setPrivate(false);
        this.astMethod.setProtected(false);
        this.astMethod.setPublic(true);
        return this;
    }

    public _method setProtected() {
        this.astMethod.setPrivate(false);
        this.astMethod.setProtected(true);
        this.astMethod.setPublic(false);
        return this;
    }

    public _method setPrivate() {
        this.astMethod.setPrivate(true);
        this.astMethod.setProtected(false);
        this.astMethod.setPublic(false);
        return this;
    }

    public _method setPackagePrivate() {
        this.astMethod.setPrivate(false);
        this.astMethod.setProtected(false);
        this.astMethod.setPublic(false);
        return this;
    }

    @Override
    public _method setAbstract() {
        this.astMethod.removeBody();
        return setAbstract(true);
    }
    
    @Override
    public _method setAbstract(boolean toSet) {
        this.astMethod.setAbstract(toSet);
        if (toSet) {
            this.astMethod.removeBody();
        } else {
            this.astMethod.createBody();
        }
        return this;
    }
    
    @Override
    public _method setFinal(boolean toSet) {
        this.astMethod.setFinal(toSet);
        return this;
    }    

    @Override
    public _method setBody(BlockStmt body) {
        if (body == null) {
            this.astMethod.removeBody();
        } else {
            this.astMethod.setBody(body);
        }
        return this;
    }

    @Override
    public _method clearBody() {
        if (this.astMethod.getBody().isPresent()) {
            this.astMethod.setBody( new BlockStmt() );
        }
        return this;
    }

    @Override
    public _method add(Statement... statements) {        
        if (!this.astMethod.getBody().isPresent()) {
            this.astMethod.createBody();
        }
        for (Statement statement : statements) {
            this.astMethod.getBody().get().addStatement(statement);
        }
        return this;
    }

    @Override
    public _method add(int startStatementIndex, Statement... statements) {
        if (!this.astMethod.getBody().isPresent()) {
            this.astMethod.createBody();
        }
        for (int i = 0; i < statements.length; i++) {
            this.astMethod.getBody().get().addStatement(i + startStatementIndex, statements[i]);
        }
        return this;
    }

    /**
     *
     * @author Eric
     * @param <_HM>
     */
    public interface _hasMethods<_HM extends _hasMethods>
            extends _java {

        List<_method> listMethods();

        default boolean hasMethods() {
            return !listMethods().isEmpty();
        }

        default _method getMethod(String name) {
            List<_method> lm = listMethods(name);
            if (lm.isEmpty()) {
                return null;
            }
            return lm.get(0);
        }

        default List<_method> listMethods(String name) {
            return listMethods().stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
        }

        List<_method> listMethods(Predicate<_method> _methodMatchFn);

        default _HM forMethods(Consumer<_method> methodConsumer) {
            return forMethods(m -> true, methodConsumer);
        }

        default _HM forMethods(Predicate<_method> methodMatchFn,
                               Consumer<_method> methodConsumer) {
            listMethods(methodMatchFn).forEach(methodConsumer);
            return (_HM) this;
        }

        default _HM removeMethod(_method _m) {
            this.forMethods(m -> m.equals(_m), m-> m.astMethod.removeForced() );
            return removeMethod( _m.astMethod );
        }

        default _HM removeMethod(MethodDeclaration astM) {
            this.forMethods(m -> m.equals(_method.of(astM)), m-> m.astMethod.removeForced() );
            return (_HM) this;
        }

        default _HM removeMethods(Predicate<_method> methodPredicate) {
            listMethods(methodPredicate).forEach(_m -> removeMethod(_m));
            return (_HM) this;
        }

        _HM method(MethodDeclaration method);

        default _HM method(String... method) {
            return method(Ast.method(method));
        }

        default _HM method(_method _m) {
            return method(_m.ast());
        }

        default _HM methods(_method... ms) {
            Arrays.stream(ms).forEach(m -> method(m));
            return (_HM) this;
        }

        /**
         * Pass in the BODY of a main method and _1_build/add a
         *
         * public static void main(String[] args) {...} method
         *
         * @param mainMethodBody the BODY of the main method
         * @return the modified T
         */
        default _HM main(String... mainMethodBody) {
            _method _m = _method.of("public static void main(String[] args){ }");
            _m.add(mainMethodBody);
            return method(_m);
        }

        /**
         * Build a public static void main(String[] args) {...} method with the
         * contents of the lambda
         *
         * @param body
         * @return
         */
        default _HM main(Ex.Command body) {
            LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
            _method _m = _method.of("public static void main(String[] args){ }");
            if (le.getBody().isBlockStmt()) {
                _m.setBody(le.getBody().asBlockStmt());
            } else {
                _m.add(le.getBody());
            }
            //TODO? should I removeIn / replaceIn the main method if one is already found??
            return method(_m);
        }

        /**
         * Build a public static void main(String[] args) {...} method with the
         * contents of the lambda
         *
         * @param body
         * @return
         */
        default _HM main(Consumer<String[]> body) {
            LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
            _method _m = _method.of("public static void main(String[] args){ }");
            if (le.getBody().isBlockStmt()) {
                _m.setBody(le.getBody().asBlockStmt());
            } else {
                _m.add(le.getBody());
            }
            //TODO? should I removeIn / replaceIn the main method if one is already found??
            return method(_m);
        }

        /**
         * 
         * @param methodDef
         * @return 
         */
        default _HM method(String methodDef) {
            return method(new String[]{methodDef});
        }

        /**
         * Adds a Draft method (with source declared in an anonymous class) to the 
         * type and returns the modified type
         * NOTE: ALSO adds any imports that exist on the public API to the type
         * 
         * @param anonymousObjectContainingMethod
         * @return 
         */
        default _HM method(Object anonymousObjectContainingMethod) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
            if (oce == null || !oce.getAnonymousClassBody().isPresent()) {
                throw new _draftException("No anonymous Object containing a method provided ");
            }
            Optional<BodyDeclaration<?>> obd = oce.getAnonymousClassBody().get().stream()
                .filter(bd -> bd instanceof MethodDeclaration
                && !bd.asMethodDeclaration().getAnnotationByClass(_remove.class).isPresent()).findFirst();
            if (!obd.isPresent()) {
                throw new _draftException("Could not find Method in anonymous object body");
            }
            MethodDeclaration md = (MethodDeclaration) obd.get();
            
            Optional<CompilationUnit> oc = ((_node)this).ast().findCompilationUnit();
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
            return method(md);
        }
    }

    public static String describeMethodSignature(_method _m) {
        StringBuilder sb = new StringBuilder();
        sb.append(_m.getName());
        sb.append("(");
        for (int i = 0; i < _m.getParameters().size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(_m.getParameter(i).getType());
            if (_m.getParameter(i).isVarArg()) {
                sb.append("...");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
