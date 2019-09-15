package org.jdraft.diff;

import org.jdraft._class;
import org.jdraft._interface;
import org.jdraft._java;
import java.lang.reflect.Modifier;
import java.util.*;
import junit.framework.TestCase;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import org.jdraft.diff._diff;

public class TextDiff_v_Java_DiffTest extends TestCase {
    
    public interface A1 {
        int a = 1;
        int b = 2;
    }
    
    public interface A2 
    {
        
        public static final int b
            = 2;
        
        public static final int a
            = 1;
        
    }
    
    /**
     * The contents of interface A1 and A2 are syntactically different, 
     * but they are semantically the same...(because the fields a, and b in A1
     * have "inferred modifiers" "public static final" and the order in which a 
     * and b are declared is not
     * 
     * ... i.e. they are explicitly marked with no
     * modifiers, but because of their context: 
     * (a field on a method, with an initial value set)
     * 
     * at runtime they are considered "public static final"
     * 
     * If we do a naive diff 
     */
    public void testInferredModifiersOnInterface() throws NoSuchFieldException{
        // A1.a has "implied" modifiers "public static final" based on the context
        int inferredMods = A1.class.getField("a").getModifiers();
        assertTrue( Modifier.isPublic(inferredMods)); 
        assertTrue( Modifier.isStatic(inferredMods));
        assertTrue( Modifier.isFinal(inferredMods));
        
        // A2.a has "explicit" modifiers "public static final"
        inferredMods = A2.class.getField("a").getModifiers();
        assertTrue( Modifier.isPublic(inferredMods));
        assertTrue( Modifier.isStatic(inferredMods));
        assertTrue( Modifier.isFinal(inferredMods));
        
        
        {   //naive textual diff between A1 and A2        
            String A1Text = _interface.of(A1.class).toString();
            String A2Text = _interface.of(A2.class).toString();
        
            diff_match_patch dmp = new diff_match_patch();        
            LinkedList<diff_match_patch.Diff> naiveTextDiffResults 
                = dmp.diff_main(A1Text, A2Text);
            
            //there are many diffs internal 
            
            //the only parts that are EQUAL when we diff between A1 and A2

            Set<String> expectEqualPartsTrim = new HashSet<>();
            expectEqualPartsTrim.add("public interface A");
            expectEqualPartsTrim.add("{");
            expectEqualPartsTrim.add("int");
            expectEqualPartsTrim.add("=");
            expectEqualPartsTrim.add(";");
            expectEqualPartsTrim.add(";"+System.lineSeparator()+"}" );

            Set<String> capturedUnequalPartsTrim = new HashSet<>();
            Set<String> capturedEqualPartsTrim = new HashSet<>();
            naiveTextDiffResults.stream().filter(d -> d.operation == Operation.EQUAL)
                .forEach(d -> capturedEqualPartsTrim.add(d.text.trim()));
            
            naiveTextDiffResults.stream().filter(d -> d.operation != Operation.EQUAL)
                .forEach(d -> capturedUnequalPartsTrim.add(d.text.trim()));
            
            assertEquals(capturedEqualPartsTrim, expectEqualPartsTrim);
        }
        {    //java.diff between A1 and A2
            _diff _dl = _diff.of(_interface.of(A1.class), _interface.of(A2.class) );
            assertTrue(_dl.size() == 1 ); //ONLY 1 diff (name A1 vs A2)
            assertTrue(_dl.firstAt(_java.Component.NAME).isChange()); //is name change
        }        
    }
    
    
    ///Diff  for many types _type._hasTypes...
    
    //In general: its HARD for a program to make sense of what the textual diffs
    // mean, because the TEXTUAL DIFFS are only SYNTAX and there is a great deal 
    // of semantic meaning MISSING.... and the Diffs are "line number based" and 
    // based on the order (of Java members) within the file which doesnt always
    // lend to intuitive
    
    // Alternatively: if we can differentiate WHAT KIND of changes there are
    // i.e. Ohh here we added a field, edited the code within a constructor or 
    // method, or removed an annotation... it is MUCH easier for a program to
    // take action are have a better analysis of what mioght be causing some 
    // symptom
    
}
