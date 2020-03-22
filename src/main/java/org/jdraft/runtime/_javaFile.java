package org.jdraft.runtime;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.Objects;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

import org.jdraft.*;

/**
 * Adapts the {@link _codeUnit} or {@link CompilationUnit} so it can be used as the
 * input to the (in memory javac tool in AdHoc)
 *
 * the adaptation occurs by calling functions and interpreting the contents of
 * the CompilationUnit and determining the fully qualified class name / the
 * relative file name where the file should exist
 *
 * @param <C> the type of _code object (i.e. {@code _javaFile<_class>})
 * @see _runtime
 * 
 * @author Eric
 */
public class _javaFile<C extends _codeUnit>
    implements JavaFileObject  {

    /**
     * Build a JavaFile from String source code
     * @param code
     * @return 
     */
    public static _javaFile of( String...code ){
        return new _javaFile( (_type) _type.of(code) );
    }
    
    /**
     * Build a _codeFile from the _code _c
     * @param <C>
     * @param _c the _type or other _code entity
     * @return the _codeFile
     */
    public static <C extends _codeUnit> _javaFile of(C _c){
        return new _javaFile( _c );        
    }
    
    /**
     * 
     * @param astCu
     * @return 
     */
    public static _javaFile of( CompilationUnit astCu ){
        return new _javaFile( astCu );        
    }
    
    /**
     * Builds a _codeFile by reading the .java contents at the Path and
     * 
     * @param javaSourceFilePath
     * @return 
     */
    public static _javaFile of( Path javaSourceFilePath ){
        _codeUnit _c = _codeUnit.of(javaSourceFilePath);
        _javaFile _cf = of( _c );
        String fullName = _c.getFullName();
        
        fullName = fullName.replace(".", "/")+".java";
        Path relPath = Paths.get(fullName);
        
        if( javaSourceFilePath.endsWith(relPath)){
            Path resPath = javaSourceFilePath;
            for(int i=0;i<relPath.getNameCount(); i++){
                resPath = resPath.getParent();
            } 
            _cf.setSourcePath(resPath);
        }
        return _cf;          
    }
    
    /**
     * Build a 
     * @param sourceRoot
     * @param javaSourceFilePath
     * @return a codeFile
     */
    public static _javaFile of( Path sourceRoot, Path javaSourceFilePath ){
        _codeUnit _c = _codeUnit.of(javaSourceFilePath);
        _javaFile _cf = of( _c );
        _cf.setSourcePath(sourceRoot);
        return _cf;
    }    
    
    /**
     * Prints the source code within the CompilationUnit/AST
     */
    static Function<_codeUnit, String> toStringFn = cu -> cu.toString();

    /**
     * for the time being, just return the default 0L.... NOW you COULD create a
     * listener or something that would be registered for events but you dont
     * HAVE to.
     */
    static Function<_codeUnit, Long> lastUpdatedFn = cu -> 0L;

    /** 
     * (Optional) The source root directory ...i.e. prior to the package path 
     * i.e. if we have a project with source at:
     * myproject/src/main/java
     * myproject/src/test/java
     * 
     * and within theses directories I have 2 classes
     * com.myproject.AAAA and
     * com.myproject.AAAATest
     * 
     * the (2) codeModelFiles will have different sourcePaths
     * so that I can differentiate the source code from the test code
     */
    private Path sourcePath;
    
    public C codeModel;
    
    /**
     * Build a _code instance to wrap an AST CompilationUnit
     *
     * @param ast
     */
    public _javaFile(CompilationUnit ast) {
        this((C) _codeUnit.of(ast) );
    }

    public void setSourcePath(Path sourcePath){
        this.sourcePath = sourcePath;
    }
     
    public Path getSourcePath(){
        return this.sourcePath;
    }
     
    /**
     * This implementation returns the result of the lastModified function.
     * Subclasses can change this behavior as long as the contract of 
     * {@link javax.tools.FileObject} is
     * obeyed.
     *
     * @return {@code 0L}
     */
    @Override
    public long getLastModified() {
        return 0L;
    }
       
    /** @return Gets the underlying AST code object */
    public C getCode(){
        return codeModel;
    }
    
    /**
     * a "custom constructor" which allows you to pass in the functions
     *
     * @param _c
     */
    public _javaFile(C _c) {
        this.codeModel = _c;
    }
    
     /**
     * Wraps the result of {@linkplain #getCharContent} in a Reader.
     * Subclasses can change this behavior as long as the contract of
     * {@link javax.tools.FileObject} is obeyed.
     *
     * @param  ignoreEncodingErrors {@inheritDoc}
     * @return a Reader wrapping the result of getCharContent
     * @throws IllegalStateException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        CharSequence charContent = getCharContent(ignoreEncodingErrors);
        if (charContent == null)
            throw new UnsupportedOperationException();
        if (charContent instanceof CharBuffer) {
            CharBuffer buffer = (CharBuffer)charContent;
            if (buffer.hasArray())
                return new CharArrayReader(buffer.array());
        }
        return new StringReader(charContent.toString());
    }
    
     /**
     * Wraps the result of openOutputStream in a Writer.  Subclasses
     * can change this behavior as long as the contract of {@link
     * javax.tools.FileObject} is obeyed.
     *
     * @return a Writer wrapping the result of openOutputStream
     * @throws IllegalStateException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutputStream());
    }
    
    /**
     * This implementation always throws {@linkplain
     * UnsupportedOperationException}.Subclasses can change this
     * behavior as long as the contract of {@link javax.tools.FileObject} is
     * obeyed.
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream( this.getCharContent(true).toString().getBytes() );        
    }

    /**
     * This implementation always throws {@linkplain
     * UnsupportedOperationException}.Subclasses can change this
     * behavior as long as the contract of {@link javax.tools.FileObject} is obeyed.
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }
    
     @Override
    public CharSequence getCharContent( boolean ignoreEncodingErrors) 
        throws IOException {
        return this.codeModel.toString();
    }    

    /**
     * A Function that resolves the Fully qualified Class Name (i.e.
     * "java.util.Map" to be used for the file path) from the {@link _codeUnit}
     * instance (using the {@link CompilationUnit}).
     *
     * <P>
     * NOTE: we attempt to use the most up to date information (i.e. the
     * PackageDeclaration and Find the First/Only Public TypeDeclaration, and then
     * "fall back" to checking the Storage on the CompilationUnit because the
     * aspects of the AST COULD have changed since the AST was read in from the
     * storage...(i.e. we could read a .java file in from "aaaa\bbbb\C.java" and
     * then change the packageDeclaration to be "package xxxx.yyyy;"... So the
     * class name should resolve to "xxxx.yyyy.C"</P>
     */
    public static class ClassNameFrom_codeFn
            implements Function<_codeUnit, String> {

        public static final ClassNameFrom_codeFn INSTANCE
                = new ClassNameFrom_codeFn();

        private ClassNameFrom_codeFn() {
        }

        /**
         *
         * @param ast the ast compilation unit
         * @return the simple name
         */
        public String findSimpleName(CompilationUnit ast) {
            if (ast.getPrimaryTypeName().isPresent()) {
                //if we have a designated primary type
                return ast.getPrimaryTypeName().get();
            }
            if (ast.getTypes().size() == 1) {
                //if only one type
                return ast.getType(0).getNameAsString();
            }
            //find the first public type
            Optional<TypeDeclaration<?>> otd
                    = ast.getTypes().stream().filter(t -> t.isPublic()).findFirst();
            if (otd.isPresent()) {
                return otd.get().getNameAsString();
            }
            //could be a package-info, module_info, or multiple package private types
            if (ast.getModule().isPresent()) {
                return "module-info";
            }
            if (ast.getStorage().isPresent()) {
                CompilationUnit.Storage st = ast.getStorage().get();
                if (st.getSourceRoot().endsWith("package-info.java")) {
                    return "package-info";
                }
                if (st.getSourceRoot().endsWith("module-info.java")) {
                    return "module-info";
                }
                String simpleName = st.getSourceRoot().getName(st.getSourceRoot().getNameCount() - 1).toString();
                //remove the .java from it
                return simpleName.substring(0, simpleName.length() - ".java".length());
            }
            //we don't have the storage present... if we have 0 types
            // and a package declaration... it's a package-info
            if (ast.getPackageDeclaration().isPresent() && ast.getTypes().isEmpty()) {
                return "package-info";
            }
            return ast.getTypes().get(0).getNameAsString();
        }

        @Override
        public String apply(_codeUnit code) {
            if (code instanceof _type) {
                return ((_type) code).getFullName();
            }
            //a packge-info, module-info
            CompilationUnit ast = code.astCompilationUnit();
            String simpleName = findSimpleName(ast);
            String packageName = null;
            if (ast.getPackageDeclaration().isPresent()) {
                //trust the package first, because it could have changed after 
                //reading in the initial file
                packageName = ast.getPackageDeclaration().get().getNameAsString();
            } else if (ast.getStorage().isPresent()) {
                //Fall back to the 
                //storage is present, so we can find the 
                CompilationUnit.Storage st = ast.getStorage().get();
                try {
                    Path jPath = st.getPath().relativize(st.getSourceRoot());

                    String pth = jPath.toString().replace("\\", ".");
                    packageName = pth.substring(0, pth.lastIndexOf("\\"));
                } catch (Exception e) {

                }
            }
            if (packageName != null) {
                return packageName + "." + simpleName;
            }
            return simpleName;
        }
    }

    /**
     * This implementation returns {@code null}.  Subclasses can
     * change this behavior as long as the contract of
     * {@link JavaFileObject} is obeyed.
     */
    @Override
    public NestingKind getNestingKind(){ return null; }

    /**
     * This implementation returns {@code null}.  Subclasses can
     * change this behavior as long as the contract of
     * {@link JavaFileObject} is obeyed.
     */
    @Override
    public Modifier getAccessLevel(){ return null; }
    
    /**
     * builds the fully qualified Class name for the source code
     * (NOTE: this supports "xxx.package-info" models as well)
     * @return the fully qualified class name based on the model
     */
    public String getFullyQualifiedClassName(){
        return this.codeModel.getFullName();
    }
    
    /**
     * gets the package name 
     * @return 
     */
    public String getPackageName(){
        String str = getFullyQualifiedClassName();
        int idx = str.lastIndexOf(".");
        if( idx >= 0 ){
            return str.substring(0, idx);
        }
        return "";
    }
    
    /** @return the simple class name of the Class, (i.e. "Map" for "java.util.Map") */
    public String getSimpleName(){
        String fullyQualifiedClassName = getFullyQualifiedClassName();
        if( fullyQualifiedClassName.contains(".")){
            return fullyQualifiedClassName.substring( fullyQualifiedClassName.lastIndexOf(".")+1);
        }
        return fullyQualifiedClassName;
    }
    
    @Override
    public String getName() {
        return toUri().getPath();
    }
    
     /**
     * This implementation compares the path of its URI to the given
     * simple name.  This method returns true if the given kind is
     * equal to the kind of this object, and if the path is equal to
     * {@code simpleName + kind.extension} or if it ends with {@code
     * "/" + simpleName + kind.extension}.
     *
     * <p>This method calls {@link #getKind} and {@link #toUri} and
     * does not access the fields {@link #toUri()}  and {@link #getKind()}
     * directly.
     *
     * <p>Subclasses can change this behavior as long as the contract
     * of {@link JavaFileObject} is obeyed.
     */
    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        String baseName = simpleName + kind.extension;
        return kind.equals(getKind())
            && (baseName.equals(toUri().getPath())
                || toUri().getPath().endsWith("/" + baseName));
    }
    
    /**
     * This implementation does nothing.  Subclasses can change this
     * behavior as long as the contract of {@link javax.tools.FileObject} is
     * obeyed.
     *
     * @return {@code false}
     */
    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Kind getKind() {
        return Kind.SOURCE;
    }
    
    public static final ClassNameFrom_codeFn CODE_NAME_FN = new ClassNameFrom_codeFn();
    
    @Override
    public URI toUri(){
        return URI.create("string:///" + CODE_NAME_FN.apply(codeModel).replace('.', '/') + Kind.SOURCE.extension);
    }    
    
    @Override
    public String toString() {
        return getClass().getName() + "[" + toUri() + "]";
    }
    
    @Override
    public int hashCode(){
        return Objects.hash( 
            this.toUri(),            
            this.getLastModified(),
            codeModel );            
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
        final _javaFile<?> other = (_javaFile<?>) obj;
        if (!Objects.equals(this.codeModel, other.codeModel)) {
            return false;
        }
        return true;
    }
}
