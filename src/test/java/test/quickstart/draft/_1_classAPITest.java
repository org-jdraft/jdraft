package test.quickstart.draft;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._interface;
import org.jdraft.diff._diff;
import org.jdraft.macro._imports;
import org.jdraft.macro._non_static;
import org.jdraft.macro._package;

import java.beans.Encoder;
import java.io.Serializable;
import java.util.UUID;

public class _1_classAPITest extends TestCase {

    /** Naive way of creating a _class (with String based components) */
    static _class _FROM_STRING_PARTS = _class.of("C").setHeaderComment("Some License Here")
            .setPackage("math.entity")
            .addImports("java.util.UUID", "java.io.Serializable;", "java.beans.Encoder")
            .setJavadoc("This is the class", "javadoc")
            .addAnnos("@Deprecated")
            .setFinal()
            .addExtend("java.beans.Encoder")
            .implement("java.io.Serializable")
            .typeParameters("<T extends Serializable>")
            .staticBlock("System.out.println(\"static block \" + UUID.randomUUID().toString());")
            .addFields("public int i;", "public UUID uuid;")
            .addConstructor("public C(int i){ this.i = i;}")
            .addMethod("public UUID getUUID(){ return this.uuid; }")
            .nest( _interface.of("Describable")  //add a nested class (here an interface)
                  .addMethod("String describe();") );

    /** We can build a _class from a single String */
    static _class _FROM_ONE_STRING = _class.of(_FROM_STRING_PARTS.toString());

    /** Creating or modifying a _class (with String/Class/Lambda based components) */
    static _class _FROM_RUNTIME_PARTS = _class.of("math.entity.C")
            //.setPackage("math.entity") //we can set the name as compound to set the package name
            .setHeaderComment("Some License Here")
            .addImports(UUID.class, Serializable.class, Encoder.class)
            .setJavadoc("This is the class", "javadoc")
            .addAnnos(Deprecated.class)
            .setFinal()
            .typeParameters("<T extends Serializable>")
            .addExtend(java.beans.Encoder.class)
            .implement(Serializable.class)
            .staticBlock(()->System.out.println("static block " + UUID.randomUUID().toString()))
            .body( new Object(){
                public int i;
                public UUID uuid;
                public UUID getUUID(){ return this.uuid; }
            })
            //.fields("public int i;", "public UUID uuid;") //already added in .body()
            .addConstructor("public C(int i){ this.i = i;}")
            //.method("public UUID getUUID(){ return this.uuid; }") //already added in .body()
            .nest( _interface.of("Describable")  //add a nested class (here an interface)
                    .addMethod("String describe();") );

    /**
     * This is the class
     * javadoc
     */
    @Deprecated
    @_package("math.entity")
    @_imports({Serializable.class, Encoder.class, UUID.class})
    public @_non_static static final class C<T extends Serializable> extends Encoder implements Serializable{
        static{ System.out.println("static block " + UUID.randomUUID().toString()); }
        public int i;
        public UUID uuid;
        public UUID getUUID(){ return this.uuid; }

        public C(int i){
            this.i = i;
        }

        public interface Describable{
            public String describe();
        }
    }

    static _class _FROM_CLASS = _class.of(C.class);

    public void testEquality(){
        // we can get the .java source code from the model:
        String stringPartsJavaCode = _FROM_STRING_PARTS.toString();
        String runtimePartsJavaCode = _FROM_RUNTIME_PARTS.toString();
        String classJavaCode = _FROM_CLASS.toString();

        System.out.println( _diff.of(_FROM_STRING_PARTS, _FROM_CLASS ) );
        System.out.println( _diff.of(_FROM_STRING_PARTS, _FROM_RUNTIME_PARTS ) );

        //...and even though the String code is NOT the same (because the order things are declared in is different)
        assertFalse( stringPartsJavaCode.equals(runtimePartsJavaCode) );

        //...the _class models of the source code ARE EQUAL
        assertTrue(_FROM_RUNTIME_PARTS.equals( _FROM_STRING_PARTS ));

        //...we can check "model equality" which is a "deep check" against
        //verify the _draft _classes _c are equal to the source code
        assertTrue(_FROM_STRING_PARTS.is( stringPartsJavaCode ));
        assertTrue(_FROM_RUNTIME_PARTS.is( stringPartsJavaCode ));

        //we can verify their hashcodes are equal
        assertEquals(_FROM_RUNTIME_PARTS.hashCode(), _FROM_STRING_PARTS.hashCode());

        //if there are differences we can use the _diff function (empty diff means equals)
        assertTrue( _diff.of(_FROM_STRING_PARTS, _FROM_STRING_PARTS).isEmpty());

        //System.out.println( _FROM_RUNTIME_PARTS );
        //System.out.println( _diff.of(_FROM_STRING_PARTS, _FROM_RUNTIME_PARTS ) );
         //is(theSourceCode));

        System.out.println(_FROM_STRING_PARTS);
    }

    public void test_class_GetIsHas_API() {
        //we can ask simple questions about the _draft  we can ask things about the _class
        assertTrue(_FROM_STRING_PARTS.getName().equals("C"));
        assertTrue(_FROM_STRING_PARTS.isInPackage("math.entity"));
        assertTrue(_FROM_STRING_PARTS.hasImport(UUID.class));
        assertTrue(_FROM_STRING_PARTS.getJavadoc().contains("This is the class"));
        assertTrue(_FROM_STRING_PARTS.hasAnno("@Deprecated"));
        assertTrue(_FROM_STRING_PARTS.isFinal());
        assertTrue(_FROM_STRING_PARTS.isExtends("Encoder"));
        assertTrue(_FROM_STRING_PARTS.isImplements(Serializable.class));
        assertTrue(_FROM_STRING_PARTS.getTypeParameters().is("<T extends Serializable>"));
        assertTrue(_FROM_STRING_PARTS.hasInitBlocks());
        assertTrue(_FROM_STRING_PARTS.getInitBlock(0).is("static{System.out.println(\"static block \" + UUID.randomUUID().toString());}"));
        assertTrue(_FROM_STRING_PARTS.getField("i").is("public int i"));
        assertTrue(_FROM_STRING_PARTS.getField("uuid").is("public UUID uuid"));
        assertTrue(_FROM_STRING_PARTS.getConstructor(0).is("public C(int i){ this.i = i; }"));
        assertTrue(_FROM_STRING_PARTS.getMethod("getUUID").is("public UUID getUUID(){ return this.uuid; }"));
        assertTrue(_FROM_STRING_PARTS.getNest("Describable") instanceof _interface);
    }

    public void test_iterate_for(){
        //iterate over members/properties
        //properties
        _FROM_STRING_PARTS.forImports(i-> System.out.println(i));
        _FROM_STRING_PARTS.forAnnos(a-> System.out.println(a));
        //members
        _FROM_STRING_PARTS.forInitBlocks(i-> System.out.println(i));
        //members/declared
        _FROM_STRING_PARTS.forConstructors(c-> System.out.println(c));
        _FROM_STRING_PARTS.forFields(f-> System.out.println(f));
        _FROM_STRING_PARTS.forMethods(m-> System.out.println(m));
        //nests
        _FROM_STRING_PARTS.forNests(n -> System.out.println(n));

        //iterate over all members / declared
        _FROM_STRING_PARTS.forMembers(m-> System.out.println(m)); //members are all _class entities (including init/staticBlocks)
        _FROM_STRING_PARTS.forDeclared(d-> System.out.println(d)); //declared are members with names (no init/staticBlocks)

        //selectively iterate over members/properties
        _FROM_STRING_PARTS.forImports(i-> i.isWildcard(), i-> System.out.println(i));
        _FROM_STRING_PARTS.forAnnos(a-> a.isInstance(Deprecated.class), a->System.out.println(a) );
        //members
        _FROM_STRING_PARTS.forInitBlocks(i-> i.isStatic(), i-> System.out.println(i));
        //members/declared
        _FROM_STRING_PARTS.forConstructors(c-> c.isVarArg(), c-> System.out.println(c));
        _FROM_STRING_PARTS.forFields(f-> f.isPublic(), f-> System.out.println(f));
        _FROM_STRING_PARTS.forMethods(m-> m.isType(UUID.class), m-> System.out.println(m));
        //nests
        _FROM_STRING_PARTS.forNests(n-> n instanceof _interface, n-> System.out.println(n));
    }
}
