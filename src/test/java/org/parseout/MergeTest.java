package org.parseout;

import junit.framework.TestCase;
import org.parseout.java.IdentifierRun;

import java.util.BitSet;

public class MergeTest extends TestCase {

    public void testF(){

    }

    public static class CompositeContentType extends ContentType {
        BitSet openCharset = new BitSet();
        BitSet contentCharset = new BitSet();
        BitSet closeCharset = new BitSet();

        char[] openChars;
        char[] contentChars;
        char[] closeChars;

        CompositeContentType(String name, char[] chars){
            super(name);
            this.openChars = chars;
            this.contentChars = chars;
            this.closeChars = chars;

            for(int i=0;i<chars.length; i++){
                this.openCharset.set( this.openChars[i]);
            }
            for(int i=0;i<chars.length; i++){
                this.contentCharset.set( this.contentChars[i]);
            }
            for(int i=0;i<chars.length; i++){
                this.closeCharset.set( this.closeChars[i]);
            }
        }

        CompositeContentType(String name, char[] openChars, char[] contentChars, char[] closeChars){
            super(name);
            this.openChars = openChars;
            this.contentChars = contentChars;
            this.closeChars = closeChars;

            for(int i=0;i<openChars.length; i++){
                this.openCharset.set( this.openChars[i]);
            }
            for(int i=0;i<contentChars.length; i++){
                this.contentCharset.set( this.contentChars[i]);
            }
            for(int i=0;i<closeChars.length; i++){
                this.closeCharset.set( this.closeChars[i]);
            }
        }
    }

    /*
    public static final MergingContentType SIGNED_NUMBER = new MergingContentType(
            '+', '-',

    )
     */



    public static final CompositeContentType IDENTIFIER = new CompositeContentType( "Identifier",
            IdentifierRun.CHARS, //open chars
            new char[]{
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '_','$','0','1','2','3','4','5','6','7','8','9'},
            new char[]{
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '_','$', '0','1','2','3','4','5','6','7','8','9'});

    /*
    public long encodeTerm( State s, ContentType ct, int length, int trailingSpace){

    }

    public void testMergeIdentifier(){

    }
     */
}
