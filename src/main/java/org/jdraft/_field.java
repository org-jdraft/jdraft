package org.jdraft;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.*;

import org.jdraft._anno.*;
import org.jdraft.macro._remove;
import org.jdraft.macro.macro;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Model of a Java Field (A Facade linked to a {@link VariableDeclarator} and
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
 *
 */
public final class _field
        implements _javadoc._hasJavadoc<_field>, _anno._hasAnnos<_field>, _modifiers._hasModifiers<_field>,
        _modifiers._hasFinal<_field>, _modifiers._hasStatic<_field>, _modifiers._hasTransient<_field>,
        _modifiers._hasVolatile<_field>, _namedType<_field>,
        _declared<VariableDeclarator, _field> {

    private final VariableDeclarator astVar;

    public static _field of(String singleString) {
        return of(new String[]{singleString});
    }

    public static _field of(Object anonymousObjectWithField) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);
        FieldDeclaration fd = (FieldDeclaration) oce.getAnonymousClassBody().get().stream().filter(bd -> bd instanceof FieldDeclaration
                && !bd.getAnnotationByClass(_remove.class).isPresent()).findFirst().get();

        //add the field to a class so I can run
        _class _c = _class.of("Temp").add(_field.of(fd.clone().getVariable(0)));
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
     * @param f
     * @return
     */
    public static List<_field> of(FieldDeclaration f) {
        List<_field> fs = new ArrayList<>();
        for (int i = 0; i < f.getVariables().size(); i++) {
            fs.add(_field.of(f.getVariable(i)));
        }
        return fs;
    }

    public static _field of(VariableDeclarator v) {
        return new _field(v);
    }

    public static _field of(String... fieldDecl) {
        String str = Text.combine(fieldDecl);
        FieldDeclaration fd = Ast.field(str);
        if (fd.getVariables().size() != 1) {
            throw new _draftException("unable to create a singular field from " + str);
        }
        return new _field(fd.getVariable(0));
    }

    /**
     * Gets the (effective modifiers) which is the union of the explicit
     * modifiers (returned from {@link #getModifiers()} and the implied
     * modifiers, that are derived from the context
     *
     * @return an enumSet of the modifiers that are effective on this _field
     */
    @Override
    public NodeList<Modifier> getEffectiveModifiers() {
        if (this.getFieldDeclaration() == null) {
            return new NodeList<>();
        }

        NodeList<Modifier> implied = Ast.getImpliedModifiers(getFieldDeclaration());
        if (implied == null) {
            return getFieldDeclaration().getModifiers();
        }
        //implied.addAll(getFieldDeclaration().getModifiers());
        implied = Ast.merge(implied, getFieldDeclaration().getModifiers());
        return implied;
    }

    public boolean is(VariableDeclarator vd) {
        return of(vd).equals(this);
    }

    public boolean is(String... fieldDeclaration) {
        try {
            return of(fieldDeclaration).equals(this);
        } catch (Exception e) {
            //System.err.println("AN EXCEPTION " + e);
            //System.out.println( Text.combine(fieldDeclaration)+" not a valid field ");
            return false;
        }
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
        this.astVar.removeInitializer();
        return this;
    }

    public boolean isInit(Predicate<Expression> expressionPredicate) {
        return this.hasInit() && expressionPredicate.test(this.getInit());
    }

    public boolean isInit(String... initExpression) {
        if( this.hasInit() ) {
            try {
                Expression e = Ex.of(initExpression);
                return this.getInit().equals(e);
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }
    
    public boolean isInit(Expression e) {
        return Objects.equals(this.getInit(), e);
    }

    public boolean isInit(boolean b) {
        return Objects.equals(this.getInit(), Ex.of(b));
    }

    public boolean isInit(byte b) {
        return Objects.equals(this.getInit(), Ex.of(b));
    }

    public boolean isInit(short s) {
        return Objects.equals(this.getInit(), Ex.of(s));
    }

    public boolean isInit(int i) {
        return Objects.equals(this.getInit(), Ex.of(i));
    }

    public boolean isInit(char c) {
        return Objects.equals(this.getInit(), Ex.of(c));
    }

    public boolean isInit(float f) {
        return Objects.equals(this.getInit(), Ex.of(f));
    }

    public boolean isInit(double d) {
        return Objects.equals(this.getInit(), Ex.of(d));
    }

    public boolean isInit(long l) {
        return Objects.equals(this.getInit(), Ex.of(l));
    }

    public boolean isInit(String init) {
        return Objects.equals(this.getInit(), Ex.stringLiteralEx(init));
    }
    
    public boolean hasInit() {
        return this.astVar.getInitializer().isPresent();
    }

    public _field(VariableDeclarator astVar) {
        this.astVar = astVar;
    }

    @Override
    public VariableDeclarator ast() {
        return astVar;
    }
    
    public Expression getInit() {
        if (astVar.getInitializer().isPresent()) {
            return astVar.getInitializer().get();
        }
        return null;
    }

    //it IS possible to set the parents FieldDeclaration to be null
    // in this case, the FieldDeclaration is null
    // it happens often enough  to want to shoot yourself
    public FieldDeclaration getFieldDeclaration() {

        Optional<Node> on = astVar.stream(_walk.PARENTS).filter( n-> n instanceof FieldDeclaration).findFirst();
        if( on.isPresent() ){
            return (FieldDeclaration) on.get();
        }
        return null;
    }

    @Override
    public _annos getAnnos() {

        if( this.getFieldDeclaration() != null && this.astVar != null && this.astVar.getParentNode().isPresent()) {

            return _annos.of(getFieldDeclaration());
        }
        //FIELDS are a pain this avoids issues if the FieldDeclaration if removed and the errant VarDeclarator
        //exists (not knowing it has been effectively deleted /removed from the model)
        // you SHOULDNT EVER HAVE a VarDeclarator w/o a FieldDeclaration, but (in practice) this
        // saves trying to double removeIn when the parent was removed
        return _annos.of();
    }

    @Override
    public String getName() {
        return astVar.getNameAsString();
    }

    @Override
    public boolean hasJavadoc() {
        if( getFieldDeclaration() != null ){
            return getFieldDeclaration().getJavadocComment().isPresent();
        }
        return false;        
    }

    @Override
    public _field javadoc(String... contents) {
        if( getFieldDeclaration() != null ){
            getFieldDeclaration().setJavadocComment(Text.combine(contents));
        }
        return this;
    }

    @Override
    public _field javadoc(JavadocComment astJavadocComment) {
        if (this.getFieldDeclaration() != null) {
            this.getFieldDeclaration().setJavadocComment(astJavadocComment);
        }
        return this;
    }

    @Override
    public _javadoc getJavadoc() {
        if( this.getFieldDeclaration() != null){
            return _javadoc.of(getFieldDeclaration());
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
            sb.append(this.getInit());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public _field name(String name) {
        this.astVar.setName(name);
        return this;
    }

    @Override
    public _field type(Type t) {
        this.astVar.setType(t);
        return this;
    }

    @Override
    public _typeRef getType() {
        return _typeRef.of(astVar.getType());
    }

    @Override
    public _modifiers getModifiers() {
        return _modifiers.of(getFieldDeclaration());
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
        if (this.astVar == other.astVar) {
            return true; //two _field s pointing to the same VariableDeclarator
        }
        if (!Objects.equals(getName(), other.getName())) {
            return false;
        }
        if (!Ast.typesEqual(this.astVar.getType(), other.astVar.getType())) {
            return false;
        }
        if( !Ex.equivalent(getInit(), other.getInit())) {
            return false;
        }        
        if (!Objects.equals(getJavadoc(), other.getJavadoc())) {
            return false;
        }
        if(this.getFieldDeclaration() != null) {
            if( other.getFieldDeclaration() != null ){
                if (!Ast.modifiersEqual(getFieldDeclaration(), other.getFieldDeclaration())) {
                    return false;
                }
                if(! Ex.equivalentAnnos(getFieldDeclaration(), other.getFieldDeclaration()) ){
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
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put(_java.Component.NAME, getName());
        parts.put(_java.Component.TYPE, getType());
        parts.put(_java.Component.MODIFIERS, getModifiers());
        parts.put(_java.Component.JAVADOC, getJavadoc());
        parts.put(_java.Component.ANNOS, getAnnos());
        parts.put(_java.Component.INIT, getInit());
        return parts;
    }

    @Override
    public int hashCode() {
        Set<Modifier> ms = new HashSet<>();
        ms.addAll(getEffectiveModifiers());
        return Objects.hash(getName(), Ast.typeHash(astVar.getType()),
                ms, //getModifiers(),
                Ex.hashAnnos(getFieldDeclaration()),
                getJavadoc(), 
                Ex.hash(getInit()));
    }

    @Override
    public _field copy() {
        FieldDeclaration fd = getFieldDeclaration().clone();
        return new _field(fd.getVariable(fd.getVariables().indexOf(this.astVar)));
    }

    public boolean isPublic() {
        return this.getFieldDeclaration() != null && 
                getFieldDeclaration().isPublic() || getEffectiveModifiers().contains(Modifier.publicModifier());
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

    public boolean isPrivate() {
        return this.getFieldDeclaration() != null && this.getFieldDeclaration().isPrivate();
    }

    @Override
    public boolean isStatic() {
        return this.getFieldDeclaration() != null &&  this.getFieldDeclaration().isStatic() || getEffectiveModifiers().contains(Modifier.staticModifier());
    }

    @Override
    public boolean isFinal() {
        return this.getFieldDeclaration() != null && this.getFieldDeclaration().isFinal() || getEffectiveModifiers().contains(Modifier.finalModifier());
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
        return this.astVar.getType().isPrimitiveType();
    }

    public boolean isArray() {
        return this.astVar.getType().isArrayType();
    }

    public boolean isReferenceType() {
        return this.astVar.getType().isReferenceType();
    }

    public _field init(boolean b) {
        this.astVar.setInitializer(Ex.of(b));
        return this;
    }

    public _field init(byte b) {
        this.astVar.setInitializer(Ex.of(b));
        return this;
    }

    public _field init(short s) {
        this.astVar.setInitializer(Ex.of(s));
        return this;
    }

    public _field init(int i) {
        this.astVar.setInitializer(Ex.of(i));
        return this;
    }

    public _field init(char c) {
        this.astVar.setInitializer(Ex.of(c));
        return this;
    }

    public _field init(float f) {
        this.astVar.setInitializer(Ex.of(f));
        return this;
    }

    public _field init(double d) {
        this.astVar.setInitializer(Ex.of(d));
        return this;
    }

    public _field init(long l) {
        this.astVar.setInitializer(Ex.of(l));
        return this;
    }

    public _field init(String init) {
        this.astVar.setInitializer(init);
        return this;
    }

    public _field init(Supplier supplier) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr sup = Ex.lambdaEx(ste);
        return init(sup.getExpressionBody().get());
    }

    public _field init(Expression expr) {
        this.astVar.setInitializer(expr);
        return this;
    }
    
    /**
     * Components that have fields (_class, _interface, _enum, _annotation, _enum._constant)
     * 
     * @author Eric
     * @param <_HF>
     */
    public interface _hasFields<_HF extends _hasFields>
            extends _model {

        List<_field> listFields();

        default boolean hasFields() {
            return !listFields().isEmpty();
        }

        default List<_field> listFields(Predicate<_field> _fieldMatchFn) {
            return listFields().stream().filter(_fieldMatchFn).collect(Collectors.toList());
        }

        default _HF forFields(Consumer<_field> fieldConsumer) {
            return forFields(f -> true, fieldConsumer);
        }

        default _HF forFields(Predicate<_field> fieldMatchFn,
                              Consumer<_field> fieldConsumer) {
            listFields(fieldMatchFn).forEach(fieldConsumer);
            return (_HF) this;
        }

        default _HF removeField(String fieldName){
            this.listFields(f-> f.getName().equals(fieldName)).forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.astVar.removeForced();
                }            
                });
            return (_HF)this;
        }

        default _HF removeField(_field _f){
            this.listFields(f-> f.equals(_f)).forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.astVar.removeForced();
                }            
                });
            return (_HF)this;
        }

        default _HF removeFields(Predicate<_field> fieldPredicate){
            this.listFields(fieldPredicate).forEach(f-> {
                if(f.getFieldDeclaration().getVariables().size() == 1){
                    f.getFieldDeclaration().removeForced();
                } else {
                    f.astVar.removeForced();
                }            
                });
            return (_HF)this;
        }

        default _field getField(String name) {
            return getField(f -> f.getName().equals(name));
        }

        default _field getField(int index) {
            return listFields().get(index);
        }

        default _field getField(Predicate<_field> _fieldMatchFn) {
            Optional<_field> of = listFields().stream().filter(_fieldMatchFn).findFirst();
            if (of.isPresent()) {
                return of.get();
            }
            return null;
        }

        _HF field(VariableDeclarator field);

        default _HF field(String... field) {
            return field(Ast.field(field).getVariable(0));
        }

        default _HF field(_field _f) {
            return field(_f.astVar);
        }

        default _HF fields(FieldDeclaration fds) {
            fds.getVariables().forEach(v -> field(v));
            return (_HF) this;
        }

        default _HF fields(String... fieldDeclarations) {
            List<FieldDeclaration> fs = Ast.fields(fieldDeclarations);
            fs.forEach(f -> fields(f));
            return (_HF) this;
        }

        default _HF fields(_field... fs) {
            Arrays.stream(fs).forEach(f -> field(f));
            return (_HF) this;
        }
    }
}
