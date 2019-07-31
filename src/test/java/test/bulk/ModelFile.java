package test.bulk;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javax.tools.FileObject;

/**
 * A File implementation that can return or accept 
 * a model representation of the file 
 * 
 * @author Eric
 * @param <M> the model type (i.e. Document, CompilationUnit, etc.)
 */
public interface ModelFile<M> extends FileObject {
    
    /** 
     * @return a Object Model representation of this File (i.e. an AST)
     */
    public abstract M getModel();
    
    /** 
     * Sets the model representation of this File
     * NOTE: by changing the model, you MAY change the Relative Path
     * (i.e. if you change the package of a .java source code file, 
     * it will change the relative path to the file)
     * @param model sets the new Model for this file 
     * @return the modified file 
     */
    public abstract ModelFile<M> setModel(M model);
    
    public static URI fileUri( Path path ){
        return URI.create("file:////" + path.toString().replace("\\", "/"));
    }
    
    /**
     * builds and returns a lambda Path Predicate that matches file filePaths that
     * end with one or more file endings
     * 
     * @param fileNameEnding
     * @return 
     */
    static Predicate<Path> pathEndsWith(final String... fileNameEnding) {
        Arrays.asList(fileNameEnding);
        return p-> {
            String s = p.toString();
            return Arrays.stream(fileNameEnding).filter( fe -> s.endsWith(fe) ).findFirst().isPresent();
        };
    }
    
    /**
     * returns a Path predicate that the path contains the string provided
     * @param fileNameContains
     * @return 
     */
    static Predicate<Path> pathContains(final String... fileNameContains){
        Arrays.asList(fileNameContains);
        return p-> {
            String s = p.toString();
            return Arrays.stream(fileNameContains).filter(cont -> s.contains(cont)).findFirst().isPresent();
        };
    }
    
    /**
     * Combine a String spread across multiple lines to a single String
     * (no line feed at the end)
     * @param lines lines of a String
     * @return a Single String representing the lines (each separated by a line feed)
     */
    static String combine(String...lines){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<lines.length; i++){
            sb.append( lines[i]);
            if( i < lines.length -1 ){
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param filePath
     * @return 
     */
    static String readString( Path filePath ){
        try{
            return new String(Files.readAllBytes(filePath));
        }catch(IOException e){
            throw new RuntimeException("unable to read file "+ filePath, e);
        }
    }
    
    /**
     * 
     * @param filePath
     * @return 
     */
    static List<String> readLines( Path filePath ){
        try{
            return Files.readAllLines(filePath);
        }catch(IOException e){
            throw new RuntimeException("unable to read file "+ filePath, e);
        }
    }
    
    /**
     * Optionally creates the directories needed and writes a file with the contents
     * @param filePath the path to the file
     * @param lines the content (in lines)
     */
    static void write(Path filePath, String... lines){
        write(filePath, combine(lines).getBytes());
    }
    
    /**
     * 
     * @param filePath
     * @param bytes 
     */
    static void write(Path filePath, byte[] bytes){
        filePath.getParent().toFile().mkdirs();
        try{
            Files.write(filePath, bytes); 
        } catch(IOException e){
            throw new RuntimeException("Unable to write file "+ filePath, e);
        }
    }
    
    /**
     * Moves a file from a source to target destination optionally overwriting the 
     * file at the target destination 
     * @param sourcePath
     * @param targetPath 
     */
    static void moveFile( Path sourcePath, Path targetPath ){
        targetPath.getParent().toFile().mkdirs();
        try{        
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING );
        } catch(IOException ioe){
            throw new RuntimeException("unable to move file from "+ sourcePath+" to "+ targetPath, ioe);
        }   
    }
}
