package org.jdraft.runtime;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.tools.SimpleJavaFileObject;

/**
 * <P>Compiled Java bytecode adapted to a {@link javax.tools.JavaFileObject} 
 * (containing the bytecode as an outputStream and the fully qualified class name)</P>
 * 
 * NOTE: used internally (by the {@link javax.tools.JavaCompiler})
 * 
 * @author Eric
 */
public class _classFile extends SimpleJavaFileObject {
    
     /**
     * <PRE>
     * Read in a bytecodeFile from a path (and resolve the classPath)
     * if we pass in a path like Paths.get("C:\\A.class")
     * we will resolve the class "A.class" and set the classPath to be "C:\\"
     * 
     * if we pass in a path like : "C:\\temp\\classes\\com\\myproj\\GG.class"
     * we might determine (from the bytecode) that the fully qualified class name
     * is : "com.myproj.GG"
     * and we need to calculate the "classPath" as "C:\\temp\\classes"
     * </PRE>
     * @param filePath the path to the file on disk
     * @return a BytecodeFile with the data and classpath populated
     */
    public static _classFile of(Path filePath ){
        try{
            byte[] bytecode = Files.readAllBytes(filePath);
            String className = resolveClassNameFromBytecode(bytecode);
            _classFile bc = new _classFile(className);
            bc.openOutputStream().write(bytecode); //write the bytecode
            // find the delta between the filePath and the path to the class
            // to determine the classPath
            try{
                Path locPath = Paths.get( bc.toUri().getPath() );
                Path classPath = filePath;
                for(int i=0;i<locPath.getNameCount(); i++){
                    classPath = classPath.getParent();
                }
                bc.setClassPath(classPath);
            }catch(Exception e){
                //failure to set classPath NOT critical
            }            
            return bc;
        } catch(IOException ioe){
            throw new _runtimeException("IOException reading bytecode from "+filePath, ioe);
        }
    }
    
    /**
     * Builds and returns the URI from a fullyQualifiedClassName
     * @param fullyQualifiedClassName
     * @return 
     */
    public static URI uri(String fullyQualifiedClassName){
        return URI.create("file:///" + fullyQualifiedClassName.replace('.', '/') + Kind.CLASS.extension);        
    }
    
    /**
     * Looks into the bytes of the bytecode to read the fully qualified className 
     * @param _cf the classFile
     * @return the fully qualified className of the class represented in the bytecode
     */
    public static String resolveClassNameFromBytecode(_classFile _cf){
        return resolveClassNameFromBytecode(_cf.getBytecode());
    }
    
    /**
     * Looks into the bytes of the bytecode to read the fully qualified className 
     * @param bytecode the class' bytecode
     * @return the fully qualified className of the class represented in the bytecode
     */
    public static String resolveClassNameFromBytecode(byte[] bytecode) {
        return resolveClassNameFromBytecode( new ByteArrayInputStream(bytecode));
    }
    
    /**
     * reads the bytes from the class bytecode file and returns the 
     * class name
     * ripped from:
     * <A HREF="https://stackoverflow.com/questions/1649674/resolve-class-name-from-bytecode">
     * Resolve the class name from bytecode</A>
     * @param byteCodeInputStream an input stream to 
     * @return the fully qualified class name
     */
    public static String resolveClassNameFromBytecode(InputStream byteCodeInputStream) {
        
        try{
            DataInputStream dis = new DataInputStream(byteCodeInputStream);
            dis.readLong(); // skip header and class version
            int cpcnt = (dis.readShort()&0xffff)-1;
            int[] classes = new int[cpcnt];
            String[] strings = new String[cpcnt];
            for(int i=0; i<cpcnt; i++) {
                int t = dis.read();
                if(t==7) classes[i] = dis.readShort()&0xffff;
                else if(t==1) strings[i] = dis.readUTF();
                else if(t==5 || t==6) { dis.readLong(); i++; }
                else if(t==8) dis.readShort();
                else dis.readInt();
            }
            dis.readShort(); // skip access flags
            return strings[classes[(dis.readShort()&0xffff)-1]-1].replace('/', '.');
        }catch(IOException e){
            throw new _runtimeException("unable to read bytecode",e);
        }
    }

    /** storage for the bytecodes as a byte[] */
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    /** the fully qualified class name (i.e. "java.util.Map") */
    private final String fullyQualifiedClassName;

    /** 
     * the "root" of the path... i.e. if we have a class
     * "com.myproject.XXX.class" written to a file at the path:
     * "C:\\temp\\classes\\com\\myproject\\XXX.class"
     * then the "rootPath" will be:
     * "C:\\temp\\classes"
     */
    private Path classPath = null;

    /**
     * build a _byteCodeFile based on a className
     * @param fullyQualifiedClassName
     */
    public _classFile(String fullyQualifiedClassName) {
        super(uri(fullyQualifiedClassName), Kind.CLASS);
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }
    
    public void setClassPath(Path classPath){
        this.classPath = classPath;
    }
    
    public Path getClassPath(){
        return this.classPath;
    }
    
    public String getFullyQualifiedClassName(){
        return fullyQualifiedClassName;
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
    
    /** @return the simple name of the Class */
    public String getSimpleName(){
        if( fullyQualifiedClassName.contains(".")){
            return fullyQualifiedClassName.substring( fullyQualifiedClassName.lastIndexOf(".")+1);
        }
        return fullyQualifiedClassName;
    }
    
    @Override
    public OutputStream openOutputStream() throws IOException {
        return baos;
    }
    
    @Override
    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream( baos.toByteArray() );
    }

    public byte[] getBytecode() {
        return baos.toByteArray();
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(this.toUri(), this.getBytecode() );
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
        final _classFile other = (_classFile) obj;
        if (!Objects.equals(this.toUri(), other.toUri())) {
            return false;
        }
        if (!Arrays.equals(this.getBytecode(), other.getBytecode())) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String toString(){
        return "_bytecodeFile["+this.uri+"]";
    }

    /**
     * Writes the .class file to the path (according to it's fully qualified class name
     * in the directory) 
     * @param classPath the classpath (i.e. "C:\\dec\\myproject\\classes\\") base to write to
     * NOTE: the actual path to the file will be based on the fu
     */
    public void write( Path classPath ){
        Path filePath = Paths.get(classPath.toString(), this.fullyQualifiedClassName.replace(".","/")+".class" );
        try{            
            filePath.getParent().toFile().mkdirs(); //create the parent directories if neccessary
            Files.write(filePath, this.getBytecode() );
        }catch(IOException ioe){
            throw new _runtimeException("unable to write .class file to "+filePath, ioe);
        }
    }    
}
