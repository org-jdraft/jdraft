package org.jdraft;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.*;

import org.jdraft.macro._remove;
import org.jdraft.macro.macro;
import org.jdraft.text.Text;
import org.jdraft.walk.Walk;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Representation of the Java source of a Field (A Facade linked to a {@link VariableDeclarator} and
 * its parent {@link FieldDeclaration} )
 *
 * NOTE: many variables can be declared in a single {@link FieldDeclaration}:
 * <PRE>
 * int x,y,z;
 * </PRE> each of the above will be (3) separate {@link _field} (s) each with
 * the same {@link FieldDeclaration} root, and with (3) separate
 * {@link VariableDeclarator}
 *
 * thus, each {@link _field} is a reference to the {@link VariableDeclarator}
 * and it can "lookup" it's parent {@link FieldDeclaration}
 * quite often, there is a 1-to-1 relationship between _field and {@link FieldDeclaration}
 *
 * @author Eric
 */
public final class _field implements _javadocComment._withJavadoc<_field>, _annos._withAnnos<_field>,
        _modifiers._withModifiers<_field>, _modifiers._withFinal<_field>, _modifiers._withStatic<_field>,
        _modifiers._withTransient<_field>, _modifiers._withVolatile<_field>,
        _java._withNameType<VariableDeclarator, _field>, _java._declared<VariableDeclarator, _field> {

    public static final Function<String, _field> PARSER = s-> _field.of(s);

    private VariableDeclarator node;

    public static _field of( Class clazz, String name){
        VariableDeclarator vd = new VariableDeclarator(Types.of(clazz), name);
        return new _field(vd);
    }

    public static _field of(String fieldDeclaration) {
        return of(new String[]{fieldDeclaration});
    }

    public static _field of(Object anonymousObjectWithField) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        FieldDeclaration fd = (FieldDeclaration) oce.getAnonymousClassBody().get().stream().filter(bd -> bd instanceof FieldDeclaration
                && !bd.getAnnotationByClass(_remove.class).isPresent()).findFirst().get();

        //add the field to a class so I can run
        _class _c = _class.of("UNKNOWN").add(_field.of(fd.clone().getVariable(0)));
        macro.to(anonymousObjectWithField.getClass(), _c);

        //I NEED TO DISSOCIATE THE FIELD FROM THE OTHER ??? (I think clone does that)
        return _c.getField(0);
    }

    /**
     * Builds
     * @param fds
     * @return
     */
    public static List<_field> of( List<FieldDeclaration> fds ){
        List<_field> fields = new ArrayList<>();
        fds.stream().forEach(f-> f.getVariables().forEach(v-> fields.add( of(v)) ) );
        return fields;
    }

    /**
     * We can define a {@link FieldDeclaration} that represents multiple
     * distinct {@link _field}s:
     *
     * FieldDeclaration fd = Ast.field("int x,y,z;"); List<_field> _fs =
     * _fields.of(fd); //_fs is a list of (3) _fields
     *
     * we need to return a list
     *
     * @param astField
     * @return
     */
    public static List<_field> of(FieldDeclaration astField) {
        List<_field> fs = new ArrayList<>();
        for (int i = 0; i < astField.getVariables().size(); i++) {
            fs.add(_field.of(astField.getVariable(i)));
        }
        return fs;
    }

    public static _field of(VariableDeclarator astVariable) {
        return new _field(astVariable);
    }

    /**
     * Builds an empty/incomplete field to be modified
     * @return an empty/incomplete field
     */
    public static _field of(){
        //we HAVE to create a Field parent because a variable's modifiers are on the parent
        FieldDeclaration fd = new FieldDeclaration();
        VariableDeclarator vd = new VariableDeclarator();
        fd.addVariable(vd);
        return of( vd );
    }

    public static _field of(String... fieldDeclaration) {
        String str = Text.combine(fieldDeclaration);
        FieldDeclaration fd = Ast.fieldDeclaration(str);
        if (fd.getVariables().size() != 1) {
            throw new _jdraftException("unable to create a singular field from " + str);
        }
        return new _field(fd.getVariable(0));
    }

    public static _feature._one<_field, _annos> ANNOS = new _feature._one<>(_field.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_field a, _annos _e) -> a.setAnnos(_e), PARSER);


    public static _feature._one<_field, _modifiers> MODIFIERS = new _feature._one<>(_field.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getEffectiveModifiers(),
            (_field a, _modifiers _e) -> a.setModifiers(_e), PARSER);

    public static _feature._one<_field, _typeRef> TYPE = new _feature._one<>(_field.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_field a, _typeRef _e) -> a.setType(_e), PARSER);

    public static _feature._one<_field, String> NAME = new _feature._one<>(_field.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_field a, String s) -> a.setName(s), PARSER);

    public static _feature._one<_field, _expr> INIT = new _feature._one<>(_field.class, _expr.class,
            _feature._id.INIT,
            a -> a.getInit(),
            (_field a, _expr _e) -> a.setInit(_e), PARSER);


    public static _feature._features<_field> FEATURES = _feature._features.of(_field.class, PARSER, ANNOS, MODIFIERS, TYPE, NAME, INIT );

    public _feature._features<_field> features(){
        return FEATURES;
    }

    /**
     * Gets the (effective modifiers) which is the union of the explicit
     * modifiers (returned from {@link #getModifiers()} and the implied
     * modifiers, that are derived from the context
     *
     * @return an enumSet of the modifiers that are effective on this _field
     */
    @Override
    public NodeList<Modifier> getEffectiveAstModifiersList() {
        if (this.getFieldDeclaration() == null) {
            return new NodeList<>();
        }

        NodeList<Modifier> implied = Modifiers.getImpliedModifiers(getFieldDeclaration());
        if (implied == null) {
            return getFieldDeclaration().getModifiers();
        }
        //implied.addAll(getFieldDeclaration().getModifiers());
        implied = Modifiers.merge(implied, getFieldDeclaration().getModifiers());
        return implied;
    }

    public boolean is(VariableDeclarator vd) {
        return of(vd).equals(this);
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _field replace(VariableDeclarator replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean is(FieldDeclaration fd) {
        return of(fd.getVariable(0))
                .equals(this);
    }

    /**
     * remove the initial value declaration of the field
     * @return the modified field
     */
    public _field removeInit() {
        this.node.removeInitializer();
        return this;
    }

    public boolean isInit(Predicate<Expression> expressionPredicate) {
        return this.hasInit() && expressionPredicate.test(this.getInitNode());
    }

    public boolean isInit(String... initExpression) {
        if( this.hasInit() ) {
            try {
                Expression e = Expr.of(initExpression);
                return this.getInitNode().equals(e);
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }
    
    public boolean isInit(Expression e) {
        return Objects.equals(this.getInitNode(), e);
    }

    public boolean isInit(boolean b) {
        return Objects.equals(this.getInitNode(), Expr.of(b));
    }

    public boolean isInit(byte b) {
        return Objects.equals(this.getInitNode(), Expr.of(b));
    }

    public boolean isInit(short s) {
        return Objects.equals(this.getInitNode(), Expr.of(s));
    }

    public boolean isInit(int i) {
        return Objects.equals(this.getInitNode(), Expr.of(i));
    }

    public boolean isInit(char c) {
        return Objects.equals(this.getInitNode(), Expr.of(c));
    }

    public boolean isInit(float f) {
        return Objects.equals(this.getInitNode(), Expr.of(f));
    }

    public boolean isInit(double d) {
        return Objects.equals(this.getInitNode(), Expr.of(d));
    }

    public boolean isInit(long l) {
        return Objects.equals(this.getInitNode(), Expr.of(l));
    }

    public boolean isInit(String init) {
        return Objects.equals(this.getInitNode(), Expr.stringLiteralExpr(init));
    }
    
    public boolean hasInit() {
        return this.node.getInitializer().isPresent();
    }

    public _field(VariableDeclarator node) {
        this.node = node;
        if( !node.getParentNode().isPresent()){
            FieldDeclaration fd = new FieldDeclaration();
            fd.addVariable(node);
        }
    }

    @Override
    public VariableDeclarator node() {
        return node;
    }

    public _expr getInit(){
        if (node.getInitializer().isPresent()) {
            return _expr.of( node.getInitializer().get());
        }
        return null;
    }

    public Expression getInitNode() {
        if (node.getInitializer().isPresent()) {
            return node.getInitializer().get();
        }
        return null;
    }

    //it IS possible to set the parents FieldDeclaration to be null
    // in this case, the FieldDeclaration is null
    // it happens often enough  to want to shoot yourself
    public FieldDeclaration getFieldDeclaration() {

        Optional<Node> on = node.stream(Walk.PARENTS).filter(n-> n instanceof FieldDeclaration).findFirst();
        if( on.isPresent() ){
            return (FieldDeclaration) on.get();
        }
        return null;
    }

    @Override
    public _annos getAnnos() {

        if( this.getFieldDeclaration() != null && this.node != null && this.node.getParentNode().isPresent()) {

            return _annos.of(getFieldDeclaration());
        }
        //FIELDS are a pain this avoids issues if the FieldDeclaration if removed and the errant VarDeclarator
        //exists (not knowing it has been effectively deleted /removed from the model)
        // you SHOULDNT EVER HAVE a VarDeclarator w/o a FieldDeclaration, but (in practice) this
        // saves trying to double removeIn when the parent was removed
        return _annos.of();
    }

    public SimpleName getNameNode() { return this.node.getName(); }

    @Override
    public String getName() {
        return node.getNameAsString();
    }

    @Override
    public boolean hasJavadoc() {
        if( getFieldDeclaration() != null ){
            return getFieldDeclaration().getJavadocComment().isPresent();
        }
        return false;        
    }

    @Override
    public _field setJavadoc(String... contents) {
        if( getFieldDeclaration() != null ){
            getFieldDeclaration().setJavadocComment(Text.combine(contents));
        }
        return this;
    }

    @Override
    public _field setJavadoc(JavadocComment astJavadocComment) {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setJavadocComment(astJavadocComment);
        }
        return this;
    }

    @Override
    public _javadocComment getJavadoc() {
        if( this.getFieldDeclaration() != null && this.getFieldDeclaration().getJavadocComment().isPresent() ){
            return _javadocComment.of( this.getFieldDeclaration().getJavadocComment().get());
        }
        return null;
    }

    @Override
    public _field removeJavadoc() {
        if( getFieldDeclaration() != null ){
            getFieldDeclaration().removeJavaDocComment();
        }
        return this;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.hasJavadoc()) {
            sb.append(this.getJavadoc());
        }
        if (this.hasAnnos()) {
            sb.append(this.getAnnos());
        }
        String mods = this.getModifiers().toString();
        if (mods.trim().length() > 0) {
            sb.append(mods);
            sb.append(" ");
        }
        sb.append(this.getType());
        sb.append(" ");
        sb.append(this.getName());
        if (this.hasInit()) {
            sb.append(" = ");
            sb.append(this.getInitNode());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public _field setName(String name) {
        this.node.setName(name);
        return this;
    }

    @Override
    public _field setType(Type t) {
        this.node.setType(t);
        return this;
    }

    @Override
    public _typeRef getType() {
        return _typeRef.of(node.getType());
    }

    @Override
    public _modifiers getModifiers() {
        FieldDeclaration fd = getFieldDeclaration();
        if( fd == null ){
            return _modifiers.of();
        }
        return _modifiers.of(fd);
    }

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
        final _field other = (_field) obj;
        if (this.node == other.node) {
            return true; //two _field s pointing to the same VariableDeclarator
        }
        if (!Objects.equals(getName(), other.getName())) {
            return false;
        }
        if (!Types.equal(this.node.getType(), other.node.getType())) {
            return false;
        }
        if( !Expr.equal(getInitNode(), other.getInitNode())) {
            return false;
        }        
        if (!Objects.equals(getJavadoc(), other.getJavadoc())) {
            return false;
        }
        if(this.getFieldDeclaration() != null) {
            if( other.getFieldDeclaration() != null ){
                if (!Modifiers.modifiersEqual(getFieldDeclaration(), other.getFieldDeclaration())) {
                    return false;
                }
                if(! Expr.equalAnnos(getFieldDeclaration(), other.getFieldDeclaration()) ){
                    return false;
                }                
            }
        } else{
            //if this is ONLY a var
            return other.getFieldDeclaration() == null || 
                    other.getAnnos().isEmpty() && other.getModifiers().ast().isEmpty();
        }       
        return true;
    }

    @Override
    public int hashCode() {
        Set<Modifier> ms = new HashSet<>();
        ms.addAll(getEffectiveAstModifiersList());
        return Objects.hash(getName(), Types.hash(node.getType()),
                ms, //getModifiers(),
                Expr.hashAnnos(getFieldDeclaration()),
                getJavadoc(), 
                Expr.hash(getInitNode()));
    }

    @Override
    public _field copy() {
        FieldDeclaration fd = getFieldDeclaration().clone();
        return new _field(fd.getVariable(fd.getVariables().indexOf(this.node)));
    }

    public boolean isPublic() {
        return this.getFieldDeclaration() != null && 
                getFieldDeclaration().isPublic() || getEffectiveAstModifiersList().contains(Modifier.publicModifier());
    }

    public boolean isDefaultAccess() {
        return this.getFieldDeclaration() != null 
                && !this.getFieldDeclaration().isPublic()
                && !this.getFieldDeclaration().isPrivate()
                && !this.getFieldDeclaration().isProtected();
    }

    public boolean isProtected() {
        return this.getFieldDeclaration() != null && this.getFieldDeclaration().isProtected();
    }

    public boolean isPackagePrivate(){
        return this.getFieldDeclaration() != null &&
                !this.getFieldDeclaration().isPrivate() &&
                !this.getFieldDeclaration().isPublic() &&
                !this.getFieldDeclaration().isProtected();
    }

    public boolean isPrivate() {
        return this.getFieldDeclaration() != null && this.getFieldDeclaration().isPrivate();
    }

    @Override
    public boolean isStatic() {
        return this.getFieldDeclaration() != null &&  this.getFieldDeclaration().isStatic() || getEffectiveAstModifiersList().contains(Modifier.staticModifier());
    }

    @Override
    public boolean isFinal() {
        return this.getFieldDeclaration() != null && this.getFieldDeclaration().isFinal() || getEffectiveAstModifiersList().contains(Modifier.finalModifier());
    }

    @Override
    public boolean isVolatile() {
        if(this.getFieldDeclaration() != null) {
            return this.getFieldDeclaration().isVolatile();
        }
        return false;
    }

    @Override
    public boolean isTransient() {
        if(this.getFieldDeclaration() != null) {
            return this.getFieldDeclaration().isTransient();
        }
        return false;
    }

    public _field setPublic() {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setPrivate(false);
            this.getFieldDeclaration().setProtected(false);
            this.getFieldDeclaration().setPublic(true);
        }
        return this;
    }

    public _field setProtected() {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setPrivate(false);
            this.getFieldDeclaration().setProtected(true);
            this.getFieldDeclaration().setPublic(false);
        }
        return this;
    }

    public _field setPrivate() {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setPrivate(true);
            this.getFieldDeclaration().setProtected(false);
            this.getFieldDeclaration().setPublic(false);
        }
        return this;
    }

    public _field setDefaultAccess() {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setPrivate(false);
            this.getFieldDeclaration().setProtected(false);
            this.getFieldDeclaration().setPublic(false);
        }
        return this;
    }

    @Override
    public _field setTransient(boolean toSet) {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setTransient(toSet);
        }
        return this;
    }

    @Override
    public _field setVolatile(boolean toSet) {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setVolatile(toSet);
        }
        return this;
    }

    @Override
    public _field setStatic(boolean toSet) {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setStatic(toSet);
        }
        return this;
    }

    @Override
    public _field setFinal(boolean toSet) {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setFinal(toSet);
        }
        return this;
    }

    public boolean isPrimitive() {
        return this.node.getType().isPrimitiveType();
    }

    public boolean isArray() {
        return this.node.getType().isArrayType();
    }

    public boolean isReferenceType() {
        return this.node.getType().isReferenceType();
    }

    public _field setInit(boolean b) {
        this.node.setInitializer(Expr.of(b));
        return this;
    }

    public _field setInit(byte b) {
        this.node.setInitializer(Expr.of(b));
        return this;
    }

    public _field setInit(short s) {
        this.node.setInitializer(Expr.of(s));
        return this;
    }

    public _field setInit(int i) {
        this.node.setInitializer(Expr.of(i));
        return this;
    }

    public _field setInit(char c) {
        this.node.setInitializer(Expr.of(c));
        return this;
    }

    public _field setInit(float f) {
        this.node.setInitializer(Expr.of(f));
        return this;
    }

    public _field setInit(double d) {
        this.node.setInitializer(Expr.of(d));
        return this;
    }

    public _field setInit(long l) {
        this.node.setInitializer(Expr.of(l));
        return this;
    }

    public _field setInit(String init) {
        this.node.setInitializer(init);
        return this;
    }

    public _field setInit(Supplier supplier) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr sup = Expr.lambdaExpr(ste);
        return setInit(sup.getExpressionBody().get());
    }

    public _field setInit(_expr _e ){
        return setInit(_e.node());
    }

    public _field setInit(Expression expr) {
        this.node.setInitializer(expr);
        return this;
    }
    
    /**
     * Components that have fields (_class, _interface, _enum, _annotation, _enum._constant)
     * 
     * @author Eric
     * @param <_WF>
     */
    public interface _withFields<_WF extends _withFields>
            extends _java._domain {

        List<_field> listFields();

        /**
         * Check if all individual arg ({@link _field}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allFields( Predicate<_field> matchFn){
            return listFields().stream().allMatch(matchFn);
        }

        default boolean hasFields() {
            return !listFields().isEmpty();
        }

        default boolean hasField(Predicate<_field> _matchFn){
            return !listFields(_matchFn).isEmpty();
        }

        default boolean hasField(String fieldName){
            return !listFields(f-> f.isNamed(fieldName)).isEmpty();
        }

        default boolean hasField(Class fieldType){
            return !listFields(f-> f.isType(fieldType)).isEmpty();
        }

        default List<_field> listFields(Predicate<_field> _fieldMatchFn) {
            return listFields().stream().filter(_fieldMatchFn).collect(Collectors.toList());
        }

        default _WF toFields(Consumer<_field> fieldConsumer) {
            return toFields(f -> true, fieldConsumer);
        }

        default _WF toFields(Predicate<_field> fieldMatchFn,
                             Consumer<_field> fieldConsumer) {
            listFields(fieldMatchFn).forEach(fieldConsumer);
            return (_WF) this;
        }

        default List<_field> forFields(Consumer<_field> fieldConsumer) {
            return forFields(f -> true, fieldConsumer);
        }

        default List<_field> forFields(Predicate<_field> fieldMatchFn,
                             Consumer<_field> fieldConsumer) {
            List<_field> _fs = listFields(fieldMatchFn);
            _fs.forEach(fieldConsumer);
            return _fs;
        }

        default _WF removeField(String fieldName){
            this.listFields(f-> f.getName().equals(fieldName)).forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.node.removeForced();
                }            
                });
            return (_WF)this;
        }

        default _WF removeField(_field _f){
            this.listFields(f-> f.equals(_f)).forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.node.removeForced();
                }            
                });
            return (_WF)this;
        }

        default _WF removeFields(Predicate<_field> fieldPredicate){
            this.listFields(fieldPredicate).forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.node.removeForced();
                }            
                });
            return (_WF)this;
        }

        default List<_field> removeFieldIf( Predicate<_field> _matchFn){
            List<_field> _fs = this.listFields(_matchFn);
            _fs.forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.node.removeForced();
                }
            });
            return _fs;
        }

        default _field getField(String name) {
            return firstField(f -> f.getName().equals(name));
        }

        default _field getField(int index) {
            return listFields().get(index);
        }

        default _field firstField(Predicate<_field> _fieldMatchFn) {
            Optional<_field> of = listFields().stream().filter(_fieldMatchFn).findFirst();
            if (of.isPresent()) {
                return of.get();
            }
            return null;
        }

        _WF setFields(List<_field> fields);

        _WF addField(VariableDeclarator field);

        default _WF addField(String... field) {
            return addField(Ast.fieldDeclaration(field).getVariable(0));
        }

        default _WF addField(_field _f) {
            return addField(_f.node);
        }

        default _WF addFields(FieldDeclaration fds) {
            fds.getVariables().forEach(v -> addField(v));
            return (_WF) this;
        }

        default _WF addFields(String... fieldDeclarations) {
            List<FieldDeclaration> fs = Ast.fieldDeclarations(fieldDeclarations);
            fs.forEach(f -> addFields(f));
            return (_WF) this;
        }

        default _WF addFields(_field... fs) {
            Arrays.stream(fs).forEach(f -> addField(f));
            return (_WF) this;
        }
    }
}
