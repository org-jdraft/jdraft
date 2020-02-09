package org.jdraft.text;

import org.jdraft._jdraftException;
import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.prototype.$null;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Utility functions for handling Textual data
 *
 * @author Eric
 */
public final class Text {

    /**
     * Read in the file from the filePath, then replace they target values with the replacements
     * ie.<PRE>
     * String replaced =
     *         Text.replace(Paths.get("C:\\temp\\bill.xml"),
     *             //target         replacement
     *             "Eric DeFazio", "Craig Thurston",
     *             "$100.00",      "$0.00",
     *             "$0.00",        "$123.45");
     * </PRE>
     * If the original file "C:\\temp\\bill.xml" had the following contents:
     * <bill>
     *     <name>Eric DeFazio</name>
     *     <balance>$100.00</balance>
     *     <credit>$0.00</credit>
     * </bill>
     *
     * ...the output String returned will be:
     * <bill>
     *     <name>Craig Thurston</name>
     *     <balance>$0.00</balance>
     *     <credit>$123.45</credit>
     * </bill>
     *
     * @param filePath
     * @param keyValuePairs
     * @return
     */
    public static String replace(Path filePath, String...keyValuePairs){
        //this is quick and dirty,
        //https://github.com/robert-bor/aho-corasick
        try {
            String source = new String(Files.readAllBytes(_io.in($null.class).getPath()));
            for(int i=0;i<keyValuePairs.length; i+=2){
                source = source.replaceAll(keyValuePairs[i], keyValuePairs[i+1]);
            }
            return source;
        }catch(IOException e){
            throw new _ioException("Unable to read source at "+filePath, e);
        }
    }

    /**
     * Read in the file from the filePath, then replace they target values with the replacements
     * ie.<PRE><CODE>
     * Map<String,String>replaceMap = new HashMap<>();</>
     * replaceMap.put("Eric DeFazio", "Craig Thurston");
     * replaceMap.put("$100.00", "$0.00");
     * replaceMap.put("$0.00", "$123.45");
     *
     * String replaced =
     *         Text.replace(Paths.get("C:\\temp\\bill.xml"), replaceMap);
     * </CODE></PRE>
     * If the original file "C:\\temp\\bill.xml" had the following contents:
     * <bill>
     *     <name>Eric DeFazio</name>
     *     <balance>$100.00</balance>
     *     <credit>$0.00</credit>
     * </bill>
     *
     * ...the output String returned will be:
     * <bill>
     *     <name>Craig Thurston</name>
     *     <balance>$0.00</balance>
     *     <credit>$123.45</credit>
     * </bill>
     *
     * @param filePath
     * @param keyValuePairs
     * @return
     */
    public static String replace(Path filePath, Map<String,String> keyValuePairs){
        try {
            String source = new String(Files.readAllBytes(filePath));
            String[] keys = keyValuePairs.keySet().toArray(new String[0]);
            for(int i=0;i<keys.length; i+=2){
                source = source.replaceAll(keys[i], keyValuePairs.get(keys[i]));
            }
            return source;
        }catch(IOException e){
            throw new _ioException("Unable to read source at "+filePath, e);
        }
    }

    private Text(){}
    
    /**
     * Look through the sourceString for any instances of the target string.
     * IF the target String is identified within the source String
     * 
     * @param sourceString the entire text to look through for 
     * @param target the (unpadded) target string to find
     * @return the FIRST instance of the target String with all of the (whitespace padding)
     * that occurs within sourceString (or null if target is not matched within sourceString)
     */
    public static String matchFirstPaddedTarget( String sourceString, String target ){
        return matchNextPaddedTarget(sourceString, target, 0);
    }
    
    /**
     * Look through the sourceString for any instances of the target string.
     * IF the target String is identified within the source String
     * 
     * @param sourceString the entire text to look through for 
     * @param target the (unpadded) target string to find
     * @param index the starting index to search afterwards
     * @return the FIRST instance of the target String with all of the (whitespace padding)
     * that occurs within sourceString (or null if target is not matched within sourceString)
     */
    public static String matchNextPaddedTarget( String sourceString, String target, int index ){    
        int idx = sourceString.indexOf(target, index);
        if( idx < 0 ){
            return "";
        }
        String pre = Text.getLeadingSpaces(sourceString, idx);
        String post = Text.getTrailingSpaces(sourceString, idx + (target.length() -1) );
        return pre+target+post;
    }
    
    /**
     * 
     * @param fixedText
     * @param ind
     * @return 
     */
    public static String getLeadingSpaces( String fixedText, int ind ){
        if( fixedText == null || fixedText.length() == 0 || ind == 0){
            return "";
        }
        int start = ind;
        while( start > 0 && Character.isWhitespace( fixedText.charAt(start-1) ) ){
            start--;
        }
        return fixedText.substring(start, ind);
    }
    
    /**
     * 
     * @param fixedText
     * @param ind
     * @return 
     */
    public static String getTrailingSpaces( String fixedText, int ind ){
        if( fixedText == null || fixedText.length() == 0 || ind >= fixedText.length() -1){
            return "";
        }
        ind++;
        int end = ind;
        while( end < fixedText.length() && Character.isWhitespace( fixedText.charAt(end) ) ){            
            end++; 
        }
        return fixedText.substring(ind, end);        
    }
    
    /**
     * Create a single String (with LineSeparators between each entry) based on an Array of Strings
     * @param code the strings (each entry is on it's own line)
     * @return a Single String representing the Strings
     */
    public static String combine( String... code ) {
        if( code.length == 0 ){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<code.length-1;i++){
            sb.append( code[i] );
            sb.append( System.lineSeparator() );
        }
        sb.append(code[code.length-1]);
        return sb.toString();
        /*
        for( String code1 : code ) {
            sb.append( code1 );
            sb.append( System.lineSeparator() );
        }
        return sb.toString();

         */
    }

    /**
     * Create a single String (with LineSeparators between each entry) based on an Array of Strings
     * @param code the strings (each entry is on it's own line)
     * @return a Single String representing the Strings
     */
    public static String combineTrim( String... code ) {
        StringBuilder sb = new StringBuilder();
        for( String code1 : code ) {
            sb.append( code1 );
            sb.append( System.lineSeparator() );
        }
        return sb.toString().trim();
    }

    /**
     *
     * @param string an un-indented String with multiple lines
     * @return a Single string where each line is indented 4 spaces
     */
    public static String indent( String string ) {
        return indent( string, "    ");        
    }
    
    /**
     *
     * @param string an un-indented String with multiple lines
     * @param prefix the prefix to add to each line
     * @return a Single string where each line is indented 4 spaces
     */
    public static String indent( String string, String prefix ) {
        List<String> ls = lines( string );

        StringBuilder sb = new StringBuilder();

        for (String l : ls) {
            sb.append(prefix);
            sb.append(l);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Breaks the single String into an array of Lines
     * @param string a single String
     * @return
     */
    public static List<String> lines( String string ) {
        if( string == null ) {
            return Collections.emptyList();
        }
        BufferedReader br = new BufferedReader(
                new StringReader( string ) );

        List<String> strLine = new ArrayList<>();

        try {
            String line = br.readLine();
            while( line != null ) {
                strLine.add( line );
                line = br.readLine();
            }
            return strLine;
        }
        catch( IOException e ) {
            throw new _jdraftException( "Error formatting Lines" );
        }
    }
}
