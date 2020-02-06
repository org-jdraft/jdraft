package lut;

import org.jdraft._class;
import org.jdraft._method;
import org.jdraft.pattern.$switchEntry;
import org.jdraft.pattern.$method;
import org.jdraft.runtime._runtime;
import org.jdraft.text.Stencil;
import org.jdraft.text.Translator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Given a Map, an interface (defining the lookup parameters) builds a Class that implements a lookup on the Map
 * using a switch statement
 *
 * Lut should work with keys & values of Type:
 * <PRE>String,int, long, char, float, double,boolean,</PRE>
 *
 * @author Eric
 */
public class LutBuilder {



    //NOTE: we EXPECT the interface to be PUBLIC to have a "lookup" method where $valueType$ and $keyType$ are defined
    // the $keyType$ and $valueType$ should be one of: (String, int, float, double, boolean, short, byte)
    public static final $method $LUT_METHOD = $method.of(
            "public $valueType$ lookup( $keyType$ key ){}");

    public static final $switchEntry $LUT_CASE = $switchEntry.of("case $key$: return $value$;");

    //I need a new Translator that treats input Strings as \" \" (Strings as LITERALS) when I write them
    // i.e. I want "case "Str": ..."
    //      not    "case Str: ..."
    public static final Translator StringLiteralTranslate = new Translator() {
        @Override
        public Object translate(Object source) {
            if( source instanceof String) {
                return "\""+source.toString()+"\"";
            }
            return Translator.DEFAULT_TRANSLATOR.translate(source);
        }
    };

    public static final Stencil $SWITCH_STMT = Stencil.of(
            "switch(key){",
            "    $cases$ ",
            "    default : throw new RuntimeException(\"invalid key: \" + key);",
            "}");

    /**
     * Build
     * @param className
     * @param keyType
     * @param valueType
     * @param keyValues
     * @return
     */
    public static _class buildLutClass(String className, Class keyType, Class valueType, Map keyValues ) {
        _method _m = $LUT_METHOD.fill(valueType, keyType);

        StringBuilder caseStatements = new StringBuilder();
        keyValues.forEach( (k,v)->caseStatements.append(
                $LUT_CASE.fill(StringLiteralTranslate, k, v).toString() ) );
        String code = $SWITCH_STMT.draft("cases", caseStatements );
        _m.setBody(code); //NOTE: if the generated code is not parseable, it'll fail here
        return _class.of(className).method( _m );
    }

    public static Object runtimeLut( String className, Class lutInterface, Map keyValues){
        return runtimeLut( className,
                Arrays.stream(lutInterface.getDeclaredMethods()).filter(m -> m.getName().equals("lookup")).findFirst().get(),
                keyValues);
    }

    public static Object runtimeLut( String className, Method lutMethod, Map keyValues){
        _class _c = buildLutClass(className, lutMethod.getParameterTypes()[0],lutMethod.getReturnType(),keyValues);
        _c.implement(lutMethod.getDeclaringClass()); //
        //System.out.println( _c ); //we could print out the source code here
        return _runtime.instanceOf(_c);
    }
}
