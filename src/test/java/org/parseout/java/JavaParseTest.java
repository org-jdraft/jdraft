package org.parseout.java;

import junit.framework.TestCase;
import org.jdraft._class;
import org.parseout.ContentType;
import org.parseout.State;
import org.parseout.binary.Bitwise;
import org.parseout.java.outline.Element;
import org.parseout.run.Run;

public class  JavaParseTest extends TestCase {

    public static class JavaRunListener extends State.LineColumnTrackListener{
        @Override
        public void onRun(State state, ContentType runContentType, int runLength, boolean trailingSpace) {
            switch(runContentType.getCombinedRunBits() ){
                case ContentType.ALPHABET_LOWERCASE: {
                    //could be a keyword
                    int add = trailingSpace ? 1 : 0;
                    String word = state.content.substring(state.cursor, state.cursor+runLength-add);
                    Element.Term t = Element.Term.keyword(word);
                    if( t != null ){
                        System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS A keyword "+ t);
                    } else{
                        System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS IDENTIFIER ");
                    }
                    break;
                }
                case ContentType.ANY_IDENTIFIER: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS IDENTIFIER ");
                    break;
                }
                case ContentType.WHITESPACE:{
                    System.out.println(" WHITESPACE ");
                    break;
                }
                case ContentType.DIGITS: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS DIGITS ");
                    break;
                }
                case ContentType.ANY_NUMERIC:{
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS NUMERIC ");
                    break;
                }
                case ContentType.DOUBLE_QUOTED: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS DOUBLE QUOTED ");
                    break;
                }
                case ContentType.QUOTED: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS QUOTED ");
                    break;
                }
                case ContentType.COMMENT: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS COMMENT ");
                    break;
                }
                case ContentType.OPERATOR: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS OPERATOR ");
                    break;
                }
                case ContentType.SEPARATOR: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS SEPARATOR ");
                    break;
                }
                case ContentType.PUNCTUATION_OPEN: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS PUNCT OPEN ");
                    break;
                }
                case ContentType.PUNCTUATION_CLOSE: {
                    System.out.println(state.content.substring(state.cursor, state.cursor+runLength)+" IS PUNCT CLOSE ");
                    break;
                }
                default :{
                    Bitwise.print(runContentType.getCombinedRunBits());
                    break;
                }
            }
            super.updateLineAndColumn(state, runLength);
        }
    }

    public void testJP(){
        JavaRunListener jrl = new JavaRunListener();
        class C{
            //Line Comment
            String s = "A String ! @ with all kinds of \" internal punct ?> <doesnt :; class get evaluated";
            char c = 'c';

            int[] i = {0, 1_000, -1, 0xDEAD_BEEF, 0B10100101, 0123_4567 };
            double [] d = {.1d, -1.0d};
        }
        _class _c = _class.of(C.class);
        State s = JavaParse.parse(_c.toString(), jrl );
    }

    public void testJ(){
        State s = JavaParse.INSTANCE.next("public class A{}");
        System.out.println( s.getLine()+ " "+s.getColumn() );
        s = JavaParse.INSTANCE.next(s);
        System.out.println( s.getLine()+ " "+s.getColumn() );
        s = JavaParse.INSTANCE.next(s);
        System.out.println( s.getLine()+ " "+s.getColumn() );
        s = JavaParse.INSTANCE.next(s);
        System.out.println( s.getLine()+ " "+s.getColumn() );
        s = JavaParse.INSTANCE.next(s);
        System.out.println( s.getLine()+ " "+s.getColumn() );
        s = JavaParse.INSTANCE.next(s);
        System.out.println( s.getLine()+ " "+s.getColumn() );

        State st = JavaParse.parse("public class A{}");
    }

    public void testAlphabetPlus(){
        State st = JavaParse.parse("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$");

    }

    public void  testParseClass(){
        //parse class

        State st = JavaParse.parse("class A{}"); //minimal
        st = JavaParse.parse("public class A{}");


        //st = JavaParse.parse("public static final @_ANN(1,2) @a2(\"quote\") class $_Name_32<E extends Map<A,B>>{}");

        st = JavaParse.parse("{ System.out.println(1); }"); //block, initBlock,
        st = JavaParse.parse("static { System.out.println(1); }"); //static Block
    }


    public void testCustomRunListener(){
        JavaParse jp = new JavaParse();

    }

    //qualifiedNameGrouper alphabetic,".",alphabetic
    //symbolGrouper        Symbol + Symbol
    //vararg grouper       "..."
    //identifier grouper   alphabetic "_" "$" alphabetic

    //keywordResolver



}
