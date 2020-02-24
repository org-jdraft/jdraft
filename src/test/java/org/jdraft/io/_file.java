package org.jdraft.io;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public class _file {

    private _file(){
    }

    public static URI uri(Path path ){
        return URI.create("file:////" + path.toString().replace("\\", "/"));
    }

    /**
     * builds and returns a lambda Path Predicate that matches file filePaths that
     * end with one or more file endings
     *
     * @param fileNameEnding
     * @return
     */
    public static Predicate<Path> pathEndsWith(final String... fileNameEnding) {
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
    public static Predicate<Path> pathContains(final String... fileNameContains){
        Arrays.asList(fileNameContains);
        return p-> {
            String s = p.toString();
            return Arrays.stream(fileNameContains).filter(cont -> s.contains(cont)).findFirst().isPresent();
        };
    }

    /**
     *
     * @param filePath
     * @return
     */
    public static String readString( Path filePath ){
        try{
            return new String(Files.readAllBytes(filePath));
        }catch(IOException e){
            throw new _ioException("unable to read file "+ filePath, e);
        }
    }

    /**
     *
     * @param filePath
     * @return
     */
    static List<String> readLines(Path filePath ){
        try{
            return Files.readAllLines(filePath);
        }catch(IOException e){
            throw new _ioException("unable to read file "+ filePath, e);
        }
    }

    /**
     * Optionally creates the directories needed and writes a file with the contents
     * @param filePath the path to the file
     * @param lines the content (in lines)
     */
    public static Path write(Path filePath, String... lines){
        return write(filePath, combine(lines).getBytes());
    }

    /**
     *
     * @param filePath
     * @param bytes
     * @return Path the Path written to
     */
    public static Path write(Path filePath, byte[] bytes){
        filePath.getParent().toFile().mkdirs();
        try{
            return Files.write(filePath, bytes);
        } catch(IOException e){
            throw new _ioException("Unable to write file "+ filePath, e);
        }
    }

    /**
     * Combine a String spread across multiple lines to a single String
     * (no line feed at the end)
     * @param lines lines of a String
     * @return a Single String representing the lines (each separated by a line feed)
     */
    public static String combine(String...lines){
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
     * Moves a file from a source to target destination optionally overwriting the
     * file at the target destination
     * @param sourcePath
     * @param targetPath
     */
    public static Path moveFile( Path sourcePath, Path targetPath ){
        targetPath.getParent().toFile().mkdirs();
        try{
            return Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING );
        } catch(IOException ioe){
            throw new _ioException("unable to move file from "+ sourcePath+" to "+ targetPath, ioe);
        }
    }
}
