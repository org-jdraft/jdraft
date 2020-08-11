package org.parseout.java;

import junit.framework.TestCase;
import org.parseout.State;

public class JavaParseTest extends TestCase {

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



    //qualifiedNameGrouper alphabetic,".",alphabetic
    //symbolGrouper        Symbol + Symbol
    //vararg grouper       "..."
    //identifier grouper   alphabetic "_" "$" alphabetic

    //keywordResolver



}
