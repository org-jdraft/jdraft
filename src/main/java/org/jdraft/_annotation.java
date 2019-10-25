package org.jdraft;

import java.io.InputStream;
import java.lang.annotation.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.Type;

import com.github.javaparser.utils.Log;
import org.jdraft._anno.*;
import org.jdraft.io._in;
import org.jdraft.macro.macro;

/**
 * Logical Mutable Model of the source code representing a Java annotation.<BR>
 *
 * Implemented as a "Facade" on top of an AST ({@link AnnotationDeclaration})
 * All state is stored within the AST, this facade supplies access to
 *
 * @author Eric
 */
public final class _annotation
        implements _type<AnnotationDeclaration, _annotation> {

    public static _annotation of( Class<? extends Annotation> clazz ){
        TypeDeclaration n = Ast.typeDecl( clazz );
        //not a compilation
        Set<Class> imps = _import.inferImportsFrom(clazz);
        _annotation _a = of( (AnnotationDeclaration)n);
        imps.forEach(i -> _a.imports(i) );
        return macro.to(clazz, n); //_a);
    }

    public static _annotation of( CompilationUnit cu ){
        NodeList<TypeDeclaration<?>> tds = cu.getTypes();
        if( tds.size() == 1 ){
            return of( (AnnotationDeclaration) tds.get(0) );
        }
        if( cu.getTypes().size() > 1 ){
            //I want to class
            Optional<TypeDeclaration<?>> coid = cu.getTypes().stream().filter(
                    t-> t instanceof AnnotationDeclaration).findFirst();
            if( coid.isPresent() ){
                return of( (AnnotationDeclaration)coid.get());
            }
        }
        if( cu.getPrimaryType().isPresent() ){
            return of( (AnnotationDeclaration)( cu.getPrimaryType().get() ) );
        }
        throw new _jdraftException("Unable to locate primary TYPE in "+ cu);
    }

    /**
     * USED MOSTLY INTERNALLY
     * builds an annotation
     * @param signature the simple signature (name:"A" / package & name: "aaaa.bbbb.A")
     * @param anonymousClassBody
     * @param ste the stackTraceElement
     * @return
     */
    public static _annotation of( String signature, Object anonymousClassBody, StackTraceElement ste) {
        _annotation _a = of( signature );
        ObjectCreationExpr oce = Ex.anonymousObjectEx(ste);

        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();

        //each (non-static) field REALLY represents an annotation.element with a possible default
        bds.stream().filter( bd-> bd instanceof FieldDeclaration && !bd.asFieldDeclaration().isStatic() ).forEach( f-> {
            VariableDeclarator vd = ((FieldDeclaration) f).getVariable(0);
            _annotation._element _ae = null;
            if( vd.getInitializer().isPresent()){
                _ae = _annotation._element.of(vd.getType(), vd.getNameAsString(), vd.getInitializer().get());
            } else {
                _ae = _annotation._element.of(vd.getType(), vd.getNameAsString());
            }
            if( ((FieldDeclaration) f).getJavadocComment().isPresent()){
                _ae.javadoc( ((FieldDeclaration) f).getJavadocComment().get());
            }
            if( !f.getAnnotations().isEmpty()){
                _ae.anno( f.getAnnotations());
            }
            _a.element(_ae);
        });

        //static fields are static fields on the annotation
        bds.stream().filter( bd-> bd instanceof FieldDeclaration && bd.asFieldDeclaration().isStatic() ).forEach( f-> {
            ((FieldDeclaration) f).getVariables().forEach( v-> _a.field(v));
        });
        return _a;
    }

    /**
     *
     * NOTE: default values are set to initial values
     *
     * NOTE: macros are not run on the Anonymous Object BODY
     * @param signature
     * @param anonymousObjectBody
     * @return
     */
    public static _annotation of( String signature, Object anonymousObjectBody ){
        return of(signature, anonymousObjectBody, Thread.currentThread().getStackTrace()[2]);
    }

    public _annotation retentionPolicyRuntime(){
        this.imports(Retention.class, RetentionPolicy.class);
        this.removeAnnos(Retention.class); //remove if one already exists
        anno( "Retention(RetentionPolicy.RUNTIME)");
        return this;
    }

    public _annotation retentionPolicyClass(){
        this.imports(Retention.class, RetentionPolicy.class);
        this.removeAnnos(Retention.class);
        anno( "Retention(RetentionPolicy.CLASS)");
        return this;
    }

    public _annotation retentionPolicySource(){
        this.imports(Retention.class, RetentionPolicy.class);
        this.removeAnnos(Retention.class);
        anno( "Retention(RetentionPolicy.SOURCE)");
        return this;
    }

    /**
     * Set the target element types for this annotation
     * (with an annotation)
     * @param elementTypes the element types this annotation should target
     * @return the modified annotation
     */
    public _annotation targets( ElementType...elementTypes ){        
        if( elementTypes.length == 0 ){
            this.removeAnnos( Target.class);
        }
        this.imports(Target.class, ElementType.class); 
        if( elementTypes.length == 1 ){
            this.removeAnnos( Target.class);
            return anno("Target(ElementType."+elementTypes[0].name()+")" );
        }                
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<elementTypes.length; i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append("ElementType.").append(elementTypes[i].name() );
        }
        return anno("Target({"+sb.toString()+"})");
    }
    
    public _annotation targetMethod(){
        this.imports( Target.class, ElementType.class );        
        removeAnnos(Target.class);
        anno("Target(ElementType.METHOD)");
        return this;
    }

    public _annotation targetParameter(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.PARAMETER)");
        return this;
    }

    public _annotation targetTypeUse(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.TYPE_USE)");
        return this;
    }

    public _annotation targetType(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.TYPE)");
        return this;
    }

    public _annotation targetTypeParameter(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.TYPE_PARAMETER)");
        return this;
    }

    public _annotation targetLocalVariable(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.LOCAL_VARIABLE)");
        return this;
    }

    public _annotation targetAnnotationType(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.ANNOTATION_TYPE)");
        return this;
    }
    
    public _annotation targetPackage(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.PACKAGE)");
        return this;
    }

    public _annotation targetConstructor(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.CONSTRUCTOR)");
        return this;
    }

    public _annotation targetField(){
        this.imports( Target.class, ElementType.class );
        removeAnnos(Target.class);
        anno("Target(ElementType.FIELD)");
        return this;
    }

    public static _annotation of( String...classDef ) {
        if (classDef.length == 1) {
            String[] strs = classDef[0].split(" ");
            if (strs.length == 1) {
                //shortcut classes
                String shortcutClass = strs[0];
                String packageName = null;
                int lastDotIndex = shortcutClass.lastIndexOf('.');
                if (lastDotIndex > 0) {
                    packageName = shortcutClass.substring(0, lastDotIndex);
                    shortcutClass = shortcutClass.substring(lastDotIndex + 1);
                    if (!shortcutClass.endsWith("}")) {
                        shortcutClass = shortcutClass + "{}";
                    }
                    return of(Ast.of("package " + packageName + ";" + System.lineSeparator() +
                            "public @interface " + shortcutClass));
                }
                if (!shortcutClass.endsWith("}")) {
                    shortcutClass = shortcutClass + "{}";
                }
                return of(Ast.of("public @interface " + shortcutClass));
            }
        }
        String cc = Text.combine(classDef);
        int atInterfaceIndex = cc.indexOf(" @interface ");
        int privateIndex = cc.indexOf( "private ");
        if( privateIndex >= 0 && privateIndex < atInterfaceIndex ){
            cc = cc.substring(0, privateIndex)+ cc.substring(privateIndex + "private ".length());
            _annotation _i = of( Ast.of( cc ));
            _i.setPrivate();
            return _i;
        }
        return of(Ast.of(classDef));
    }

    public static _annotation of( AnnotationDeclaration astClass ){
        return new _annotation( astClass );
    }

    public static _annotation of(InputStream is){
        return of( StaticJavaParser.parse(is) );
    }

    public static _annotation of( _in in ){
        return of( in.getInputStream());
    }
    
    public _annotation( AnnotationDeclaration astClass ){
        this.astAnnotation = astClass;
    }

    /**
     * the AST storing the state of the _class
     * the _class is simply a facade into the state astClass
     *
     * the _class facade is a "Logical View" of the _class state stored in the
     * AST and can interpret or manipulate the AST without:
     * having to deal with syntax issues
     */
    private final AnnotationDeclaration astAnnotation;

    @Override
    public boolean isTopLevel(){
        return ast().isTopLevelType();
    }
    
    @Override
    public CompilationUnit astCompilationUnit(){
        if( this.ast().findCompilationUnit().isPresent() ){
            return this.ast().findCompilationUnit().get();
        }
        return null;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.astAnnotation );
    }

    @Override
    public String toString(){
        if( this.ast().isTopLevelType() ){
            return this.astCompilationUnit().toString();            
        }
        return this.astAnnotation.toString();        
    }

    public boolean hasElements(){
        return !listElements().isEmpty();
    }

    public _annotation element( String... elementDeclaration ){
        return element( Ast.annotationMemberDecl( elementDeclaration ));
    }

    public _annotation element( _element _p){
        this.astAnnotation.addMember( _p.astAnnMember );
        return this;
    }

    public _annotation element( AnnotationMemberDeclaration annotationProperty){
        this.astAnnotation.addMember( annotationProperty );
        return this;
    }

    public _element getElement(Predicate<_annotation._element> _ae){
        List<_element> lps = listElements(_ae );
        if( lps.isEmpty() ){
            return null;
        }
        return lps.get(0);
    }

    public _element getElement( String name ){
        List<_element> lps = listElements(p -> p.getName().equals( name ) );
        if( lps.isEmpty() ){
            return null;
        }
        return lps.get(0);
    }

    public List<_element> listElements(){
        NodeList<BodyDeclaration<?>> nb  = this.astAnnotation.getMembers();
        List<_element> ps = new ArrayList<>();
        nb.stream().filter( b -> b instanceof AnnotationMemberDeclaration )
                .forEach( am -> ps.add( _element.of( (AnnotationMemberDeclaration)am) ));
        return ps;
    }

    public List<_element> listElements(Predicate<_element> _elementMatchFn ){
        return listElements().stream().filter( _elementMatchFn ).collect(Collectors.toList());
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final _annotation other = (_annotation)obj;
        if( this.astAnnotation == other.astAnnotation){
            return true; //short circuit... two _annotations refer to the same AnnotationDeclaration
        }
        if( !Objects.equals( this.getPackage(), other.getPackage())){
            return false;
        }
        if( !Ast.modifiersEqual(astAnnotation, astAnnotation) ){
            return false;
        }
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc()) ){
            return false;
        }
        if( !Ex.equivalentAnnos(astAnnotation, astAnnotation)){
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName()) ){
            return false;
        }
        if( ! Ast.importsEqual( this.astAnnotation,other.astAnnotation ) ){
            return false;
        }
        Set<_field> tf = new HashSet<>();
        Set<_field> of = new HashSet<>();
        tf.addAll( this.listFields() );
        of.addAll( other.listFields() );

        if( !Objects.equals( tf, of)){
            return false;
        }

        Set<_element> tp = new HashSet<>();
        Set<_element> op = new HashSet<>();
        tp.addAll(this.listElements() );
        op.addAll(other.listElements() );

        if( !Objects.equals( tp, op)){
            return false;
        }

        Set<_type> tn = new HashSet<>();
        Set<_type> on = new HashSet<>();
        tn.addAll( this.listNests() );
        on.addAll( other.listNests() );

        if( !Objects.equals( tn, on)){
            return false;
        }

        Set<_type> ctn = new HashSet<>();
        Set<_type> con = new HashSet<>();
        ctn.addAll( this.listCompanionTypes() );
        con.addAll( other.listCompanionTypes() );

        if( !Objects.equals( ctn, con)){
            return false;
        }
        return true;
    }

    public _annotation forElements( Consumer<_element> elementConsumer ){
        listElements().forEach(elementConsumer);
        return this;
    }

    public _annotation forElements( Predicate<_element> elementMatchFn, Consumer<_element> elementConsumer ){
        listElements(elementMatchFn).forEach(elementConsumer);
        return this;
    }

    public _annotation removeElement( String elementName ){
        _element _e = this.getElement(elementName );
        if( _e != null ) {
            this.astAnnotation.remove(_e.astAnnMember);
        }
        return this;
    }

    public _annotation removeElement( _element _e ){
        listElements(e -> e.equals(_e)).forEach(e-> e.ast().removeForced() );
        return this;
    }

    public _annotation removeElements( Predicate<_element> _pe ){
        listElements(_pe).forEach( e -> removeElement(e));
        return this;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.HEADER_COMMENT, this.getHeaderComment() );
        parts.put( _java.Component.PACKAGE, this.getPackage() );
        parts.put( _java.Component.IMPORTS, this.getImports().list() );
        parts.put( _java.Component.ANNOS, this.listAnnos() );
        parts.put( _java.Component.JAVADOC, this.getJavadoc() );
        parts.put( _java.Component.NAME, this.getName() );
        parts.put( _java.Component.MODIFIERS, this.getModifiers() );
        parts.put( _java.Component.ELEMENTS, this.listElements() );
        parts.put( _java.Component.FIELDS, this.listFields() );
        parts.put( _java.Component.NESTS, this.listNests() );
        parts.put( _java.Component.COMPANION_TYPES, this.listCompanionTypes() );
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        
        hash = 13 * hash + Objects.hashCode( this.getPackage() );
        hash = 13 * hash + Ast.importsHash( astAnnotation  );

        hash = 13 * hash + Objects.hashCode( this.getEffectiveModifiers() );

        hash = 13 * hash + Objects.hashCode( this.getJavadoc() );
        hash = 13 * hash + Ex.hashAnnos(astAnnotation);

        hash = 13 * hash + Objects.hashCode( this.getName() );

        //organize
        Set<_field> fields = new HashSet<>();
        fields.addAll( this.listFields() );
        hash = 13 * hash + Objects.hashCode( fields );

        Set<_element> elements = new HashSet<>();
        elements.addAll(this.listElements() );
        hash = 13 * hash + Objects.hashCode( elements );

        Set<_type> nests = new HashSet<>();
        nests.addAll(  this.listNests() );
        hash = 13 * hash + Objects.hashCode( nests );

        Set<_type> companionTypes = new HashSet<>();
        companionTypes.addAll(  this.listCompanionTypes() );
        hash = 13 * hash + Objects.hashCode( companionTypes );

        return hash;
    }

    @Override
    public _annotation field( VariableDeclarator field ) {
        if(! field.getParentNode().isPresent()){
            throw new _jdraftException("cannot add Var without parent FieldDeclaration");
        }
        FieldDeclaration fd = (FieldDeclaration)field.getParentNode().get();
        //we already added it to the parent
        if( this.astAnnotation.getFields().contains( fd ) ){
            if( !fd.containsWithinRange( field ) ){
                fd.addVariable( field );
            }
            return this;
        }
        this.astAnnotation.addMember( fd );
        return this;
    }

    @Override
    public boolean is( String...annotationDeclaration){
        try {
            return is(Ast.annotationDecl(annotationDeclaration));
        }catch(Exception e){
            return false;
        }
    }

    /** 
     * is the AnnotationDeclaration equal this class
     * @param ad 
     * @return true if these annotations are equivalent
     */
    @Override
    public boolean is( AnnotationDeclaration ad ){
        try{
            _annotation _a = of( ad);
            return Objects.equals(this, _a);
        } catch(Exception e){
            return false;
        }
    }
    
    @Override
    public AnnotationDeclaration ast() {
        return this.astAnnotation;
    }
    
    /**
     * a property element added to an annotation
     * <PRE>
     * // VALUE is an element of the Speed annotation
     * public @interface Speed{
     *     int VALUE() default 0;
     * }
     * </PRE>
     * NOTE: we called this an ELEMENT and NOT a member, because we use the
     * term "member" to be any member implementation (_field, _method, etc.)
     * of a type (as it is documented in Java), so we devised the term _element
     * to mean (specifically) a property of an _annotation
     * (it is also a _member) and maps to an AnnotationMemberDeclaration
     */
    public static class _element implements _javadoc._hasJavadoc<_element>,
            _anno._hasAnnos<_element>, _namedType<_element>,
            _declared<AnnotationMemberDeclaration, _element> {

        public static _element of( AnnotationMemberDeclaration am){
            return new _element( am );
        }

        public static _element of( String...code ){
            return new _element( Ast.annotationMemberDecl( code ) );
        }

        public static _element of( Type type, String name ){
            AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
            amd.setName(name);
            amd.setType(type);
            return of( amd );
        }

        public static _element of( Type type, String name, Expression defaultValue ){
            AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
            amd.setName(name);
            amd.setType(type);
            amd.setDefaultValue(defaultValue);
            return of( amd );
        }
        
        private final AnnotationMemberDeclaration astAnnMember;

        public _element( AnnotationMemberDeclaration astAnnMember ){
            this.astAnnMember = astAnnMember;
        }

        @Override
        public _element copy(){
            return of( this.astAnnMember.toString() );
        }
        
        @Override
        public boolean is(String...stringRep){
            return is(Ast.annotationMemberDecl(stringRep));
        }
        
        @Override
        public boolean is(AnnotationMemberDeclaration amd ){
            return _element.of(amd).equals(this);
        }
        
        @Override
        public _element name(String name){
            this.astAnnMember.setName( name );
            return this;
        }

        @Override
        public AnnotationMemberDeclaration ast(){
            return this.astAnnMember;
        }
        
        @Override
        public _element type( Type t){
            this.astAnnMember.setType( t );
            return this;
        }

        @Override
        public String getName(){
            return this.astAnnMember.getNameAsString();
        }

        @Override
        public _typeRef getType(){
            return _typeRef.of(this.astAnnMember.getType());
        }

        public boolean hasDefaultValue(){
            return this.astAnnMember.getDefaultValue().isPresent();
        }

        public _element removeDefaultValue(){
            this.astAnnMember.removeDefaultValue();
            return this;
        }
        
        public _element setDefaultValue(int intValue ){
            this.astAnnMember.setDefaultValue( Ex.of( intValue ) );
            return this;
        }
        
        public _element setDefaultValue(long longValue ){
            this.astAnnMember.setDefaultValue( Ex.of( longValue ) );
            return this;
        }
        
        public _element setDefaultValue(char charValue ){
            this.astAnnMember.setDefaultValue( Ex.of( charValue ) );
            return this;
        }
        
        public _element setDefaultValue(boolean booleanValue ){
            this.astAnnMember.setDefaultValue( Ex.of( booleanValue ) );
            return this;
        }
        
        public _element setDefaultValueNull(){
            this.astAnnMember.setDefaultValue( Ex.nullEx() );
            return this;
        }
        
        public _element setDefaultValue(float floatValue ){
            this.astAnnMember.setDefaultValue( Ex.of( floatValue ) );
            return this;
        }
        
        public _element setDefaultValue(double doubleValue ){
            this.astAnnMember.setDefaultValue( Ex.of( doubleValue ) );
            return this;
        }
        
        public _element setDefaultValue( String defaultValueExpression){
            this.astAnnMember.setDefaultValue( Ast.ex( defaultValueExpression) );
            return this;
        }

        public _element setDefaultValue( Expression e){
            this.astAnnMember.setDefaultValue( e );
            return this;
        }

        public Expression getDefaultValue(){
            if( this.astAnnMember.getDefaultValue().isPresent()){
                return this.astAnnMember.getDefaultValue().get();
            }
            return null;
        }

        @Override
        public _annos getAnnos() {
            return _annos.of(this.astAnnMember );
        }

        @Override
        public boolean equals( Object obj ) {
            if( this == obj ) {
                return true;
            }
            if( obj == null ) {
                return false;
            }
            if( getClass() != obj.getClass() ) {
                return false;
            }
            final _element other = (_element)obj;
            if( this.astAnnMember == other.astAnnMember){
                return true; //two _element instances pointing to same AstMemberDeclaration
            }
            if( ! Ex.equivalentAnnos(this.astAnnMember, other.astAnnMember)){
                return false;
            }
            if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
                Log.trace("expected javadoc %s got %s", this::getJavadoc, other::getJavadoc);
                return false;
            }
            if( !Objects.equals( this.getName(), other.getName() ) ) {
                Log.trace("expected name %s got %s", this::getName, other::getName);
                return false;
            }
            if( !Ast.typesEqual( astAnnMember.getType(), other.astAnnMember.getType())){
                Log.trace("expected type %s got %s", astAnnMember::getType, other.astAnnMember::getType);
                return false;
            }
            if( !Objects.equals( this.getDefaultValue(), other.getDefaultValue() ) ) {
                Log.trace("expected name %s got %s", this::getDefaultValue, other::getDefaultValue);
                return false;
            }
            return true;
        }

        @Override
        public Map<_java.Component, Object> components( ) {
            Map<_java.Component, Object> parts = new HashMap<>();
            parts.put( _java.Component.ANNOS, this.listAnnos() );
            parts.put( _java.Component.JAVADOC, this.getJavadoc() );
            parts.put( _java.Component.NAME, this.getName() );
            parts.put( _java.Component.TYPE, this.getType() );
            parts.put( _java.Component.DEFAULT, this.getDefaultValue() );
            return parts;
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hash(
                    Ex.hashAnnos(this.astAnnMember),
                    this.getJavadoc(),
                    this.getName(),
                    Ast.typeHash(this.astAnnMember.getType()),
                    this.getDefaultValue() );
            return hash;
        }

        @Override
        public String toString(){
            return this.astAnnMember.toString();
        }              
    }
}
