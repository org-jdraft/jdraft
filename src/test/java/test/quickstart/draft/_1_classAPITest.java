package test.quickstart.draft;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._interface;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class _1_classAPITest extends TestCase {

    public void test_classBuildAndUse_fromStrings(){
        // a String way of building a class:
        _class _c = _class.of("C")
                .setHeaderComment("Some License Here")
                .setPackage("math.entity")
                .imports("java.util.*", "java.io.*;")
                .javadoc("This is the class",
                        "javadoc")
                .anno("@Deprecated")
                .setFinal()
                .extend("BaseClass")
                .implement("java.io.Serializable")
                .typeParameters("<T extends Serializable>")
                .staticBlock("System.out.println(\"static block \" + UUID.randomUUID().toString());")
                .fields("public int i;", "public UUID uuid;")
                .constructor("public C(int i){",
                        "   this.i = i;",
                        "}")
                .method("public UUID getUUID(){ return this.uuid; }")
                //add a nested class (here an interface)
                .nest( _interface.of("Describable")
                        .method("String describe();") );
        String theSourceCode = _c.toString();

        //verify the _draft _class _c is equal to the source code
        assertTrue(_c.is(theSourceCode));

        System.out.println(_c);

        //we can ask simple questions about the _draft  we can ask things about the _class
        assertTrue( _c.getName().equals("C"));
        assertTrue( _c.isInPackage("math.entity"));
        assertTrue( _c.hasImport(Map.class));
        assertTrue( _c.getJavadoc().contains("This is the class"));
        assertTrue( _c.hasAnno("@Deprecated"));
        assertTrue(_c.isFinal());
        assertTrue(_c.isExtends("BaseClass"));
        assertTrue(_c.isImplements(Serializable.class));
        assertTrue(_c.getTypeParameters().is("<T extends Serializable>"));
        assertTrue(_c.hasInitBlocks());
        assertTrue(_c.getInitBlock(0).is("static{System.out.println(\"static block \" + UUID.randomUUID().toString());}"));
        assertTrue( _c.getField("i").is("public int i") );
        assertTrue( _c.getField("uuid").is("public UUID uuid") );
        assertTrue( _c.getConstructor(0).is("public C(int i){ this.i = i; }"));
        assertTrue(_c.getMethod("getUUID").is("public UUID getUUID(){ return this.uuid; }"));
        assertTrue( _c.getNest("Describable") instanceof _interface);

        //iterate over members/properties
        //properties
        _c.forImports(i-> System.out.println(i));
        _c.forAnnos(a-> System.out.println(a));
        //members
        _c.forInitBlocks(i-> System.out.println(i));
        //members/declared
        _c.forConstructors(c-> System.out.println(c));
        _c.forFields(f-> System.out.println(f));
        _c.forMethods(m-> System.out.println(m));
        //nests
        _c.forNests(n -> System.out.println(n));

        //iterate over all members / declared
        _c.forMembers(m-> System.out.println(m)); //members are all _class entities (including init/staticBlocks)
        _c.forDeclared(d-> System.out.println(d)); //declared are members with names (no init/staticBlocks)

        //selectively iterate over members/properties
        _c.forImports(i-> i.isWildcard(), i-> System.out.println(i));
        _c.forAnnos( a-> a.isInstance(Deprecated.class), a->System.out.println(a) );
        //members
        _c.forInitBlocks(i-> i.isStatic(), i-> System.out.println(i));
        //members/declared
        _c.forConstructors(c-> c.isVarArg(), c-> System.out.println(c));
        _c.forFields(f-> f.isPublic(), f-> System.out.println(f));
        _c.forMethods(m-> m.isType(UUID.class), m-> System.out.println(m));
        //nests
        _c.forNests(n-> n instanceof _interface, n-> System.out.println(n));
    }
}
