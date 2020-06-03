package org.jdraft;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;

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

    public static final Function<String, _annotation> PARSER = s-> _annotation.of(s);

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
        TypeDeclaration n = Ast.typeDeclaration( javaParser, clazz );
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
        ObjectCreationExpr oce = Expr.newExpr(ste);

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
                _ae.addAnnoExprs( f.getAnnotations());
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
        this.removeAnnoExprs(Retention.class); //remove if one already exists
        addAnnoExprs( "Retention(RetentionPolicy.RUNTIME)");
        return this;
    }

    public _annotation setRetentionPolicyClass(){
        this.addImports(Retention.class, RetentionPolicy.class);
        this.removeAnnoExprs(Retention.class);
        addAnnoExprs( "Retention(RetentionPolicy.CLASS)");
        return this;
    }

    public _annotation setRetentionPolicySource(){
        this.addImports(Retention.class, RetentionPolicy.class);
        this.removeAnnoExprs(Retention.class);
        addAnnoExprs( "Retention(RetentionPolicy.SOURCE)");
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
            this.removeAnnoExprs( Target.class);
        }
        this.addImports(Target.class, ElementType.class);
        if( elementTypes.length == 1 ){
            this.removeAnnoExprs( Target.class);
            return addAnnoExprs("Target(ElementType."+elementTypes[0].name()+")" );
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<elementTypes.length; i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append("ElementType.").append(elementTypes[i].name() );
        }
        return addAnnoExprs("Target({"+sb.toString()+"})");
    }

    public _annotation setTargetMethod(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.METHOD)");
        return this;
    }

    public _annotation setTargetParameter(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.PARAMETER)");
        return this;
    }

    public _annotation setTargetTypeUse(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.TYPE_USE)");
        return this;
    }

    public _annotation setTargetType(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.TYPE)");
        return this;
    }

    public _annotation setTargetTypeParameter(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.TYPE_PARAMETER)");
        return this;
    }

    public _annotation setTargetLocalVariable(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.LOCAL_VARIABLE)");
        return this;
    }

    public _annotation setTargetAnnotationType(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.ANNOTATION_TYPE)");
        return this;
    }

    public _annotation setTargetPackage(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.PACKAGE)");
        return this;
    }

    public _annotation setTargetConstructor(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.CONSTRUCTOR)");
        return this;
    }

    public _annotation setTargetField(){
        this.addImports( Target.class, ElementType.class );
        removeAnnoExprs(Target.class);
        addAnnoExprs("Target(ElementType.FIELD)");
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
    public _annoExprs getAnnoExprs() {
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
        return !listEntries().isEmpty();
    }

    public _annotation addEntry(){
        return addEntry( new AnnotationMemberDeclaration( ));
    }
    public _annotation addEntry(String... entryDeclaration ){
        String str = Text.combine(entryDeclaration );
        if( !str.endsWith(";")){
            str = str + ";";
        }
        return addEntry( Ast.annotationMemberDeclaration( str ));
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
        List<_entry> lps = listEntries(_ae );
        if( lps.isEmpty() ){
            return null;
        }
        return lps.get(0);
    }

    public _entry getEntry(String name ){
        List<_entry> lps = listEntries(p -> p.getName().equals( name ) );
        if( lps.isEmpty() ){
            return null;
        }
        return lps.get(0);
    }

    public List<_entry> listEntries(){
        NodeList<BodyDeclaration<?>> nb  = this.astAnnotation.getMembers();
        List<_entry> ps = new ArrayList<>();
        nb.stream().filter( b -> b instanceof AnnotationMemberDeclaration )
                .forEach( am -> ps.add( _entry.of( (AnnotationMemberDeclaration)am) ));
        return ps;
    }

    public List<_entry> listEntries(Predicate<_entry> _elementMatchFn ){
        return listEntries().stream().filter( _elementMatchFn ).collect(Collectors.toList());
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
        if( !Expr.equalAnnos(astAnnotation, astAnnotation)){
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
        tp.addAll(this.listEntries() );
        op.addAll(other.listEntries() );

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
        listEntries().forEach(entryConsumer);
        return this;
    }

    public _annotation forEntries(Predicate<_entry> entryMatchFn, Consumer<_entry> entryConsumer ){
        listEntries(entryMatchFn).forEach(entryConsumer);
        return this;
    }

    public _annotation setEntries(List<_entry> entries){
        this.astAnnotation.getMembers().removeIf(m -> m instanceof AnnotationMemberDeclaration);
        entries.forEach( e-> this.addEntry(e.ast()) );
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
        listEntries(e -> e.equals(_e)).forEach(e-> e.ast().removeForced() );
        return this;
    }

    public _annotation removeEntries(Predicate<_entry> _pe ){
        listEntries(_pe).forEach(e -> removeEntry(e));
        return this;
    }

    /** could be a single statement, or a block stmt */
    public static _feature._one<_annotation, _imports> IMPORTS = new _feature._one<>(_annotation.class, _imports.class,
            _feature._id.IMPORTS,
            a -> a.getImports(),
            (_annotation a, _imports b) -> a.setImports(b), PARSER);

    public static _feature._one<_annotation, _package> PACKAGE = new _feature._one<>(_annotation.class, _package.class,
            _feature._id.PACKAGE,
            a -> a.getPackage(),
            (_annotation a, _package b) -> a.setPackage(b), PARSER)
            .setNullable(true);

    public static _feature._one<_annotation, _annoExprs> ANNOS = new _feature._one<>(_annotation.class, _annoExprs.class,
            _feature._id.ANNOS,
            a -> a.getAnnoExprs(),
            (_annotation a, _annoExprs b) -> a.setAnnoExprs(b), PARSER);

    public static _feature._one<_annotation, _javadocComment> JAVADOC = new _feature._one<>(_annotation.class, _javadocComment.class,
            _feature._id.JAVADOC,
            a -> a.getJavadoc(),
            (_annotation a, _javadocComment b) -> a.setJavadoc(b), PARSER)
            .setNullable(true);

    public static _feature._one<_annotation, _modifiers> MODIFIERS = new _feature._one<>(_annotation.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getModifiers(),
            (_annotation a, _modifiers b) -> a.setModifiers(b), PARSER);

    public static _feature._many<_annotation, _java._member> MEMBERS = new _feature._many<>(_annotation.class, _java._member.class,
            _feature._id.MEMBERS,
            _feature._id.MEMBER,
            a -> a.listMembers(),
            (_annotation a, List<_java._member>mems) -> a.setMembers(mems), PARSER, s-> _java._member.of(_annotation.class, s))
            .featureImplementations(_entry.class, _field.class, /*inner type*/_class.class, _enum.class, _annotation.class, _interface.class)
            .setOrdered(false);/** for the most part, the order of declarations doesnt matter */

    public static _feature._features<_annotation> FEATURES = _feature._features.of(_annotation.class,
            PACKAGE, IMPORTS, ANNOS, JAVADOC, MODIFIERS, MEMBERS);

    /*
    public static _feature._many<_annotation, _annotation._entry> ANNOTATION_ENTRIES = new _feature._many<>(_annotation.class, _annotation._entry.class,
            _feature._id.ANNOTATION_ENTRIES,
            _feature._id.ANNOTATION_ENTRY,
            a -> a.listEntries(),
            (_annotation a, List<_entry>les) -> a.setEntries(les));

    public static _feature._many<_annotation, _field> FIELDS = new _feature._many<>(_annotation.class, _field.class,
            _feature._id.FIELDS,
            _feature._id.FIELD,
            a -> a.listFields(),
            (_annotation a, List<_field>les) -> a.setFields(les));

    public static _feature._many<_annotation, _type> INNER_TYPES = new _feature._many<>(_annotation.class, _type.class,
            _feature._id.INNER_TYPES,
            _feature._id.INNER_TYPE,
            a -> a.listInnerTypes(),
            (_annotation a, List<_type>lit) -> a.setInnerTypes(lit));

    public static _feature._many<_annotation, _type> COMPANION_TYPES = new _feature._many<>(_annotation.class, _type.class,
            _feature._id.COMPANION_TYPES,
            _feature._id.COMPANION_TYPE,
            a -> a.listCompanionTypes(),
            (_annotation a, List<_type>lit) -> a.setCompanionTypes(lit));

    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.HEADER_COMMENT, this.getHeaderComment() );
        parts.put( _java.Feature.PACKAGE, this.getPackage() );
        parts.put( _java.Feature.IMPORTS, this.getImports().list() );
        parts.put( _java.Feature.ANNO_EXPRS, this.listAnnoExprs() );
        parts.put( _java.Feature.JAVADOC, this.getJavadoc() );
        parts.put( _java.Feature.MODIFIERS, this.getModifiers() );
        parts.put( _java.Feature.NAME, this.getName() );
        parts.put( _java.Feature.ANNOTATION_ENTRIES, this.listEntries() );
        parts.put( _java.Feature.FIELDS, this.listFields() );
        parts.put( _java.Feature.INNER_TYPES, this.listInnerTypes() );
        parts.put( _java.Feature.COMPANION_TYPES, this.listCompanionTypes() );
        return parts;
    }
    */

    @Override
    public int hashCode() {
        int hash = 5;
        
        hash = 13 * hash + Objects.hashCode( this.getPackage() );
        hash = 13 * hash + _imports.Compare.importsHash( astAnnotation  );

        hash = 13 * hash + Objects.hashCode( this.getEffectiveModifiers() );

        hash = 13 * hash + Objects.hashCode( this.getJavadoc() );
        hash = 13 * hash + Expr.hashAnnos(astAnnotation);

        hash = 13 * hash + Objects.hashCode( this.getName() );

        //organize
        Set<_field> fields = new HashSet<>();
        fields.addAll( this.listFields() );
        hash = 13 * hash + Objects.hashCode( fields );

        Set<_entry> elements = new HashSet<>();
        elements.addAll(this.listEntries() );
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
    public _annotation setFields(List<_field> fields) {
        this.astAnnotation.getMembers().removeIf( m -> m instanceof FieldDeclaration );
        fields.forEach(f-> addField(f));
        return this;
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
            return is( (AnnotationDeclaration)Ast.typeDeclaration(annotationDeclaration));
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

}
