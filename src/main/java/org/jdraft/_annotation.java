package org.jdraft;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.type.Type;

import com.github.javaparser.utils.Log;
import org.jdraft.io._in;
import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.macro.macro;
import org.jdraft.text.Text;

/**
 * Model of the Java source code representing <B>Declaration</B> of an Annotation
 * (i.e. @interface A{} ) (a {@link _type}) <BR>
 *
 * Implemented as a "Facade" on top of an AST ({@link AnnotationDeclaration})
 *
 * @author Eric
 */
public final class _annotation
        implements _type<AnnotationDeclaration, _annotation> {

    public static _annotation of( Path p) {
        return of( Ast.JAVAPARSER, p);
    }
    public static _annotation of(JavaParser javaParser, Path p) {
        return of(javaParser, _io.inFile(p));
    }

    public static _annotation of(URL url){
        return of(Ast.JAVAPARSER, url);
    }
    public static _annotation of(JavaParser javaParser, URL url){
        try {
            InputStream inStream = url.openStream();
            return of(javaParser, inStream);
        }catch(IOException ioe){
            throw new _ioException("invalid input url \""+url.toString()+"\"", ioe);
        }
    }

    public static _annotation of( Class<? extends Annotation> clazz ){
        return of(Ast.JAVAPARSER, clazz);
    }

    public static _annotation of( JavaParser javaParser, Class<? extends Annotation> clazz ){
        TypeDeclaration n = Ast.typeDecl( javaParser, clazz );
        //not a compilation
        Set<Class> imps = _import.inferImportsFrom(clazz);
        _annotation _a = of( (AnnotationDeclaration)n);
        imps.forEach(i -> _a.addImports(i) );
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
        ObjectCreationExpr oce = Exprs.newEx(ste);

        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();

        //each (non-static) field REALLY represents an annotation.element with a possible default
        bds.stream().filter( bd-> bd instanceof FieldDeclaration && !bd.asFieldDeclaration().isStatic() ).forEach( f-> {
            VariableDeclarator vd = ((FieldDeclaration) f).getVariable(0);
            _entry _ae = null;
            if( vd.getInitializer().isPresent()){
                _ae = _entry.of(vd.getType(), vd.getNameAsString(), vd.getInitializer().get());
            } else {
                _ae = _entry.of(vd.getType(), vd.getNameAsString());
            }
            if( ((FieldDeclaration) f).getJavadocComment().isPresent()){
                _ae.setJavadoc( ((FieldDeclaration) f).getJavadocComment().get());
            }
            if( !f.getAnnotations().isEmpty()){
                _ae.addAnnoRefs( f.getAnnotations());
            }
            _a.addEntry(_ae);
        });

        //static fields are static fields on the annotation
        bds.stream().filter( bd-> bd instanceof FieldDeclaration && bd.asFieldDeclaration().isStatic() ).forEach( f-> {
            ((FieldDeclaration) f).getVariables().forEach( v-> _a.addField(v));
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

    public static _annotation of(){
        return of( new AnnotationDeclaration() );
    }

    public static _annotation of( String...classDef ) {
        return of(Ast.JAVAPARSER, classDef);
    }

    public static _annotation of( String annotationDef) {
        return of(Ast.JAVAPARSER, new String[]{annotationDef});
    }

    public static _annotation of( String annotationDef1, String annotationDef2) {
        return of(Ast.JAVAPARSER, new String[]{annotationDef1, annotationDef2});
    }

    public static _annotation of( JavaParser javaParser, String...classDef ) {
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
                    return of(Ast.of(javaParser,"package " + packageName + ";" + System.lineSeparator() +
                            "public @interface " + shortcutClass));
                }
                if (!shortcutClass.endsWith("}")) {
                    shortcutClass = shortcutClass + "{}";
                }
                return of(Ast.of(javaParser,"public @interface " + shortcutClass));
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
        return of(Ast.of(javaParser, classDef));
    }

    public static _annotation of(InputStream is) {
        return of(Ast.JAVAPARSER, is);
    }

    public static _annotation of(JavaParser javaParser, InputStream is) {
        return of( Ast.of(javaParser, is) );
    }

    public static _annotation of( _in in ) {
        return of(Ast.JAVAPARSER, in);
    }

    public static _annotation of(JavaParser javaParser, _in in ) {
        return of( javaParser, in.getInputStream());
    }

    public static _annotation of( AnnotationDeclaration astClass ){
        return new _annotation( astClass );
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

    public _annotation setRetentionPolicyRuntime(){
        this.addImports(Retention.class, RetentionPolicy.class);
        this.removeAnnoRefs(Retention.class); //remove if one already exists
        addAnnoRefs( "Retention(RetentionPolicy.RUNTIME)");
        return this;
    }

    public _annotation setRetentionPolicyClass(){
        this.addImports(Retention.class, RetentionPolicy.class);
        this.removeAnnoRefs(Retention.class);
        addAnnoRefs( "Retention(RetentionPolicy.CLASS)");
        return this;
    }

    public _annotation setRetentionPolicySource(){
        this.addImports(Retention.class, RetentionPolicy.class);
        this.removeAnnoRefs(Retention.class);
        addAnnoRefs( "Retention(RetentionPolicy.SOURCE)");
        return this;
    }

    /**
     * Set the target element types for this annotation
     * (with an annotation)
     * @param elementTypes the element types this annotation should target
     * @return the modified annotation
     */
    public _annotation setTargets(ElementType...elementTypes ){
        if( elementTypes.length == 0 ){
            this.removeAnnoRefs( Target.class);
        }
        this.addImports(Target.class, ElementType.class);
        if( elementTypes.length == 1 ){
            this.removeAnnoRefs( Target.class);
            return addAnnoRefs("Target(ElementType."+elementTypes[0].name()+")" );
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<elementTypes.length; i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append("ElementType.").append(elementTypes[i].name() );
        }
        return addAnnoRefs("Target({"+sb.toString()+"})");
    }

    public _annotation setTargetMethod(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.METHOD)");
        return this;
    }

    public _annotation setTargetParameter(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.PARAMETER)");
        return this;
    }

    public _annotation setTargetTypeUse(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.TYPE_USE)");
        return this;
    }

    public _annotation setTargetType(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.TYPE)");
        return this;
    }

    public _annotation setTargetTypeParameter(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.TYPE_PARAMETER)");
        return this;
    }

    public _annotation setTargetLocalVariable(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.LOCAL_VARIABLE)");
        return this;
    }

    public _annotation setTargetAnnotationType(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.ANNOTATION_TYPE)");
        return this;
    }

    public _annotation setTargetPackage(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.PACKAGE)");
        return this;
    }

    public _annotation setTargetConstructor(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.CONSTRUCTOR)");
        return this;
    }

    public _annotation setTargetField(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoRefs(Target.class);
        addAnnoRefs("Target(ElementType.FIELD)");
        return this;
    }

    @Override
    public CompilationUnit astCompilationUnit(){
        if( this.ast().findCompilationUnit().isPresent() ){
            return this.ast().findCompilationUnit().get();
        }
        return null;
    }

    @Override
    public _annotation setJavadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return this;
    }

    @Override
    public _annotation setJavadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return this;
    }

    @Override
    public _annoExprs getAnnoRefs() {
        return _annoExprs.of(this.astAnnotation );
    }

    @Override
    public String toString(){
        if( this.ast().isTopLevelType() ){
            return this.astCompilationUnit().toString();            
        }
        return this.astAnnotation.toString();        
    }

    public boolean hasEntries(){
        return !listElements().isEmpty();
    }

    public _annotation addEntry(){
        return addEntry( new AnnotationMemberDeclaration( ));
    }
    public _annotation addEntry(String... entryDeclaration ){
        return addEntry( Ast.annotationMemberDecl( entryDeclaration ));
    }

    public _annotation addEntry(_entry _ae){
        this.astAnnotation.addMember( _ae.astAnnMember );
        return this;
    }

    public _annotation addEntry(AnnotationMemberDeclaration annotationEntry){
        this.astAnnotation.addMember( annotationEntry );
        return this;
    }

    public _entry getEntry(Predicate<_entry> _ae){
        List<_entry> lps = listElements(_ae );
        if( lps.isEmpty() ){
            return null;
        }
        return lps.get(0);
    }

    public _entry getEntry(String name ){
        List<_entry> lps = listElements(p -> p.getName().equals( name ) );
        if( lps.isEmpty() ){
            return null;
        }
        return lps.get(0);
    }

    public List<_entry> listElements(){
        NodeList<BodyDeclaration<?>> nb  = this.astAnnotation.getMembers();
        List<_entry> ps = new ArrayList<>();
        nb.stream().filter( b -> b instanceof AnnotationMemberDeclaration )
                .forEach( am -> ps.add( _entry.of( (AnnotationMemberDeclaration)am) ));
        return ps;
    }

    public List<_entry> listElements(Predicate<_entry> _elementMatchFn ){
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
        if( !Modifiers.modifiersEqual(astAnnotation, astAnnotation) ){
            return false;
        }
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc()) ){
            return false;
        }
        if( !Exprs.equalAnnos(astAnnotation, astAnnotation)){
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName()) ){
            return false;
        }
        if( ! _imports.Compare.importsEqual( this.astAnnotation,other.astAnnotation ) ){
            return false;
        }
        Set<_field> tf = new HashSet<>();
        Set<_field> of = new HashSet<>();
        tf.addAll( this.listFields() );
        of.addAll( other.listFields() );

        if( !Objects.equals( tf, of)){
            return false;
        }

        Set<_entry> tp = new HashSet<>();
        Set<_entry> op = new HashSet<>();
        tp.addAll(this.listElements() );
        op.addAll(other.listElements() );

        if( !Objects.equals( tp, op)){
            return false;
        }

        Set<_type> tn = new HashSet<>();
        Set<_type> on = new HashSet<>();
        tn.addAll( this.listInnerTypes() );
        on.addAll( other.listInnerTypes() );

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

    public _annotation forEntries(Consumer<_entry> entryConsumer ){
        listElements().forEach(entryConsumer);
        return this;
    }

    public _annotation forEntries(Predicate<_entry> entryMatchFn, Consumer<_entry> entryConsumer ){
        listElements(entryMatchFn).forEach(entryConsumer);
        return this;
    }

    public _annotation removeEntry(String entryName ){
        _entry _e = this.getEntry(entryName );
        if( _e != null ) {
            this.astAnnotation.remove(_e.astAnnMember);
        }
        return this;
    }

    public _annotation removeEntry(_entry _e ){
        listElements(e -> e.equals(_e)).forEach(e-> e.ast().removeForced() );
        return this;
    }

    public _annotation removeEntries(Predicate<_entry> _pe ){
        listElements(_pe).forEach( e -> removeEntry(e));
        return this;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.HEADER_COMMENT, this.getHeaderComment() );
        parts.put( _java.Component.PACKAGE, this.getPackage() );
        parts.put( _java.Component.IMPORTS, this.getImports().list() );
        parts.put( _java.Component.ANNOS, this.listAnnoRefs() );
        parts.put( _java.Component.JAVADOC, this.getJavadoc() );
        parts.put( _java.Component.NAME, this.getName() );
        parts.put( _java.Component.MODIFIERS, this.getModifiers() );
        parts.put( _java.Component.ELEMENTS, this.listElements() );
        parts.put( _java.Component.FIELDS, this.listFields() );
        parts.put( _java.Component.INNER_TYPES, this.listInnerTypes() );
        parts.put( _java.Component.COMPANION_TYPES, this.listCompanionTypes() );
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        
        hash = 13 * hash + Objects.hashCode( this.getPackage() );
        hash = 13 * hash + _imports.Compare.importsHash( astAnnotation  );

        hash = 13 * hash + Objects.hashCode( this.getEffectiveModifiers() );

        hash = 13 * hash + Objects.hashCode( this.getJavadoc() );
        hash = 13 * hash + Exprs.hashAnnos(astAnnotation);

        hash = 13 * hash + Objects.hashCode( this.getName() );

        //organize
        Set<_field> fields = new HashSet<>();
        fields.addAll( this.listFields() );
        hash = 13 * hash + Objects.hashCode( fields );

        Set<_entry> elements = new HashSet<>();
        elements.addAll(this.listElements() );
        hash = 13 * hash + Objects.hashCode( elements );

        Set<_type> inners = new HashSet<>();
        inners.addAll(  this.listInnerTypes() );
        hash = 13 * hash + Objects.hashCode( inners );

        Set<_type> companionTypes = new HashSet<>();
        companionTypes.addAll(  this.listCompanionTypes() );
        hash = 13 * hash + Objects.hashCode( companionTypes );

        return hash;
    }

    @Override
    public _annotation addField(VariableDeclarator field ) {
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
            return is( (AnnotationDeclaration)Ast.typeDecl(annotationDeclaration));
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
     * a property entry added to an annotation
     * <PRE>
     * // VALUE is an entry of the Speed annotation
     * public @interface Speed{
     *     int VALUE() default 0;
     * }
     * </PRE>
     * NOTE: we called this an entry and NOT a member, because we use the
     * term "member" to be any member implementation (_field, _method, etc.)
     * of a type (as it is documented in Java), so we devised the term _element
     * to mean (specifically) a property of an _annotation
     * (it is also a _member) and maps to an AnnotationMemberDeclaration
     */
    public static class _entry implements _javadocComment._withJavadoc<_entry>,
            _annoExprs._withAnnoExprs<_entry>, _java._withNameTypeRef<AnnotationMemberDeclaration,_entry>,
            _java._declared<AnnotationMemberDeclaration, _entry> {

        public static _entry of(AnnotationMemberDeclaration astEntry){
            return new _entry( astEntry );
        }

        public static _entry of(String...code ){
            return new _entry( Ast.annotationMemberDecl( code ) );
        }

        public static _entry of(Type type, String name ){
            AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
            amd.setName(name);
            amd.setType(type);
            return of( amd );
        }

        public static _entry of(Type type, String name, Expression defaultValue ){
            AnnotationMemberDeclaration amd = new AnnotationMemberDeclaration();
            amd.setName(name);
            amd.setType(type);
            amd.setDefaultValue(defaultValue);
            return of( amd );
        }
        
        private final AnnotationMemberDeclaration astAnnMember;

        public _entry(AnnotationMemberDeclaration astAnnMember ){
            this.astAnnMember = astAnnMember;
        }

        @Override
        public _entry copy(){
            return of( this.astAnnMember.toString() );
        }
        
        @Override
        public boolean is(String...stringRep){
            return is(Ast.annotationMemberDecl(stringRep));
        }
        
        @Override
        public boolean is(AnnotationMemberDeclaration amd ){
            return _entry.of(amd).equals(this);
        }

        public Node getNameNode() { return this.astAnnMember.getName(); }

        @Override
        public _entry setName(String name){
            this.astAnnMember.setName( name );
            return this;
        }

        @Override
        public AnnotationMemberDeclaration ast(){
            return this.astAnnMember;
        }
        
        @Override
        public _entry setTypeRef(Type t){
            this.astAnnMember.setType( t );
            return this;
        }

        @Override
        public _entry setJavadoc(String... content) {
            ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
            return this;
        }

        @Override
        public _entry setJavadoc(JavadocComment astJavadocComment) {
            ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
            return this;
        }

        @Override
        public String getName(){
            return this.astAnnMember.getNameAsString();
        }

        @Override
        public _typeRef getTypeRef(){
            return _typeRef.of(this.astAnnMember.getType());
        }

        public boolean hasDefaultValue(){
            return this.astAnnMember.getDefaultValue().isPresent();
        }

        public _entry removeDefaultValue(){
            this.astAnnMember.removeDefaultValue();
            return this;
        }
        
        public _entry setDefaultValue(int intValue ){
            this.astAnnMember.setDefaultValue( Exprs.of( intValue ) );
            return this;
        }
        
        public _entry setDefaultValue(long longValue ){
            this.astAnnMember.setDefaultValue( Exprs.of( longValue ) );
            return this;
        }
        
        public _entry setDefaultValue(char charValue ){
            this.astAnnMember.setDefaultValue( Exprs.of( charValue ) );
            return this;
        }
        
        public _entry setDefaultValue(boolean booleanValue ){
            this.astAnnMember.setDefaultValue( Exprs.of( booleanValue ) );
            return this;
        }
        
        public _entry setDefaultValueNull(){
            this.astAnnMember.setDefaultValue( Exprs.nullEx() );
            return this;
        }
        
        public _entry setDefaultValue(float floatValue ){
            this.astAnnMember.setDefaultValue( Exprs.of( floatValue ) );
            return this;
        }
        
        public _entry setDefaultValue(double doubleValue ){
            this.astAnnMember.setDefaultValue( Exprs.of( doubleValue ) );
            return this;
        }
        
        public _entry setDefaultValue(String defaultValueExpression){
            this.astAnnMember.setDefaultValue( Ast.expression( defaultValueExpression) );
            return this;
        }

        public _entry setDefaultValue(Expression e){
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
        public _annoExprs getAnnoRefs() {
            return _annoExprs.of(this.astAnnMember );
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
            final _entry other = (_entry)obj;
            if( this.astAnnMember == other.astAnnMember){
                return true; //two _element instances pointing to same AstMemberDeclaration
            }
            if( ! Exprs.equalAnnos(this.astAnnMember, other.astAnnMember)){
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
            if( !Types.equal( astAnnMember.getType(), other.astAnnMember.getType())){
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
            parts.put( _java.Component.ANNOS, this.listAnnoRefs() );
            parts.put( _java.Component.JAVADOC, this.getJavadoc() );
            parts.put( _java.Component.NAME, this.getName() );
            parts.put( _java.Component.TYPE, this.getTypeRef() );
            parts.put( _java.Component.DEFAULT, this.getDefaultValue() );
            return parts;
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hash(
                    Exprs.hashAnnos(this.astAnnMember),
                    this.getJavadoc(),
                    this.getName(),
                    Types.hash(this.astAnnMember.getType()),
                    this.getDefaultValue() );
            return hash;
        }

        @Override
        public String toString(){
            return this.astAnnMember.toString();
        }              
    }
}
