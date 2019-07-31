package org.jdraft;

import java.io.*;
import java.util.*;

/**
 * Utility functions for handling Textual data
 *
 * @author Eric
 */
public final class Text {

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
            return Collections.EMPTY_LIST;
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
            throw new _jDraftException( "Error formatting Lines" );
        }
    }
}
