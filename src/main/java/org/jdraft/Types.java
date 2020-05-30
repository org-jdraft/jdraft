package org.jdraft;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.type.*;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Functionality related to Ast {@link Type} entities
 * (for building hashcodes, testing equality, parsing, etc.)
 */
public final class Types {
    private Types(){}

    public static class Classes {

        /**
         * i.e. "void m(){}"
         */
        public static Class<VoidType> VOID = VoidType.class;

        /**
         * Unknown type, as used in lambda expressions like:
         * "(x)->doThing(x);"
         * where we have the parameter x that does not explicitly provide the
         */
        public static Class<UnknownType> UNKNOWN = UnknownType.class;

        public static Class<PrimitiveType> PRIMITIVE = PrimitiveType.class;

        public static PrimitiveType BOOLEAN = PrimitiveType.booleanType();
        public static PrimitiveType BYTE = PrimitiveType.byteType();
        public static PrimitiveType SHORT = PrimitiveType.shortType();
        public static PrimitiveType CHAR = PrimitiveType.charType();
        public static PrimitiveType INT = PrimitiveType.intType();
        public static PrimitiveType FLOAT = PrimitiveType.floatType();
        public static PrimitiveType DOUBLE = PrimitiveType.doubleType();
        public static PrimitiveType LONG = PrimitiveType.longType();
    }
    /**
     * When we create an anonymous Local Class and ask for it's name, it will
     * have this weird "$#$" qualifier, where # is some number... Here is an
     * example:
     * <PRE>
     * draft.java._classTest$1$Hoverboard
     * </PRE> ...well we want to identify these patterns and convert them into
     * dots draft.java._classTest.Hoverboard
     */
    public static final String LOCAL_CLASS_NAME_PACKAGE_PATTERN = "\\$?\\d+\\$";

    /**
     * A Pattern used to identify anonymous local class so as to decide when
     * we encounter one when referencing code
     */
    public static final Pattern PATTERN_LOCAL_CLASS = Pattern.compile(LOCAL_CLASS_NAME_PACKAGE_PATTERN);

    /**
     *
     * @param param the string representation of the typeParameter
     * @return
     */
    public static TypeParameter typeParam(String param) {
        param = param.trim();
        if (param.length() == 0) {
            return null;
        }
        if (!param.startsWith("<")) {
            param = "<" + param;
            param = param + ">";
        }
        //if (!param.endsWith(">")) {
        //    param = param + ">";
        //}
        MethodDeclaration md = Ast.methodDeclaration(param + " void a(){}");

        TypeParameter tp = md.getTypeParameters().get(0);
        tp.removeForced(); //DISCONNECT
        return tp;
    }

    /**
     *
     * @param code the string representation of the typeParameter
     * @return
     */
    public static NodeList<TypeParameter> typeParams(String code) {
        code = code.trim();
        if (code.length() == 0) {
            return new NodeList<>();
        }
        if (!code.startsWith("<")) {
            code = "<" + code;
        }
        if (!code.endsWith(">")) {
            code = code + ">";
        }
        MethodDeclaration md = Ast.methodDeclaration(code + " void a(){}");

        NodeList<TypeParameter> ntp = md.getTypeParameters();
        NodeList<TypeParameter> cpy = new NodeList<>(); //Disconnected copy
        ntp.forEach(tp -> cpy.add(tp));
        return cpy;
    }

    public static Type of(AnnotatedType at) {
        return of(at.getType().toString());
    }

    public static Type of(java.lang.reflect.Type t) {
        String str = t.getTypeName();
        if (PATTERN_LOCAL_CLASS.matcher(str).find()) {
            //lets remove all the local stuff... return a type without package
            str = str.replaceAll(LOCAL_CLASS_NAME_PACKAGE_PATTERN, ".");
            return of(str.substring(str.lastIndexOf('.') + 1));
        }
        return of(str);
    }

    public static Type of(Class clazz) {
        if (clazz.isArray()) {
            Class<?> cl = clazz;
            //int dimensions = 0;
            StringBuilder sb = new StringBuilder();
            while (cl.isArray()) {
                //dimensions++;
                sb.append("[]");
                cl = cl.getComponentType();
            }
            String tr = cl.getCanonicalName() + sb.toString();
            return of(tr);
        }
        return of(clazz.getCanonicalName());
    }

    public static ClassOrInterfaceType classOrInterfaceType( String code){
        return (ClassOrInterfaceType)of( code);
    }

    public static Type of(String code) {

        if (code.contains("|")) { //Could only be a Union Type i.e. from a catch clause
            code = "catch(" + code + " e ) {}";
            CatchClause cc = Ast.catchClause(code);
            List<UnionType> ut = new ArrayList<>();
            cc.getParameter().walk(UnionType.class, u -> ut.add(u));
            return ut.get(0);
        }
        if( code.startsWith("?")){
            //need to parse a WildcardType
            try {
                Type t = of("List<" + code + ">");
                Optional<WildcardType> on = t.findFirst(WildcardType.class);
                if( on.isPresent()){
                    return on.get();
                }
            }catch(Exception e){ }
            throw new _jdraftException("Unable to parse \""+ code+"\" as wildcard type");
        }
        if (PATTERN_LOCAL_CLASS.matcher(code).find()) {
            //lets remove all the local stuff... return a type without package
            code = code.replaceAll(LOCAL_CLASS_NAME_PACKAGE_PATTERN, ".");
            return of(code.substring(code.lastIndexOf('.') + 1));
        }
        ParseResult<Type> prt = Ast.JAVAPARSER.parseType(code);
        if( prt.isSuccessful() ){
            return prt.getResult().get();
        }
        throw new _jdraftException("Unable to parse type :\""+code+"\""+System.lineSeparator()+prt.getProblems());
        /*
        try {

            return StaticJavaParser.parseType(code);
        }catch(Exception e){
            throw new _jdraftException("Unable to parse type :\""+code+"\"", e);
        }
         */
    }

    public static List<String> normalizeTypeParam(TypeParameter tp) {
        List<String> tw = new ArrayList<>();
        Tree.directChildren(tp, Node.class, t -> true, t -> {
            if (t instanceof Type) {
                List<String> toks = tokenize((Type) t);
                toks.forEach(e -> {
                    if (e.contains(".")) {
                        tw.add(e.substring(e.lastIndexOf(".") + 1));
                    } else {
                        tw.add(e);
                    }
                });
            } else {
                tw.add(t.toString());
            }
        });
        return tw;
    }

    public static List<String> tokenize(Type type) {
        return tokenize(type.toString());
    }

    public static List<String> tokenize(String type) {
        type = type.trim();
        List<String> toks = new ArrayList<>();
        String build = new String();
        for (int i = 0; i < type.length(); i++) {
            switch (type.charAt(i)) {
                case '<':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add("<");
                    break;
                case '>':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add(">");
                    break;
                case '|':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add("|");
                    break;
                case '&':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    toks.add("&");
                    break;
                case ',':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    break;
                case ' ':
                    if (build.length() > 0) {
                        toks.add(build);
                        build = "";
                    }
                    break;
                default:
                    build += type.charAt(i);
            }
        }
        if (build.length() > 0) {
            toks.add(build);
        }
        return toks;
    }

    /**
     * Builds a hashcode for a set of types (i.e. no order)
     * @param ts
     * @return
     */
    public static int hash(List<? extends Type> ts) {
        Set<Integer> thc = new HashSet<>();
        for (int i = 0; i < ts.size(); i++) {
            thc.add(hash(ts.get(i)));
        }
        return thc.hashCode();
    }

    /**
     * Check if the contents of the lists (NOT THE ORDER) is equal This is
     * useful for checking a list of types where one list may contain some fully
     * qualified types (java.util.Map) and the other may not(Map) (and for
     * generics, etc.)
     * <PRE>
     * this is good for checking if two classes implement the same interfaces in this scenario:
     * _class _a = _class.of("A").implement("fully.qualified.B", "fully.qualified.C");
     * _class _b = _class.of("B").implement("C", "B");
     * //thiw will work / return true... even though order, and types fully qualified or not
     * assertTrue( Ast.typesEqual ( _a.listImplements(), _b.listImplements() ) );
     * </PRE>
     *
     * @param <T> the Ast Type
     * @param lt1
     * @param lt2
     * @return
     */
    public static <T extends Type> boolean equal(List<T> lt1, List<T> lt2) {
        if (lt1.size() != lt2.size()) {
            return false;
        }
        for (int i = 0; i < lt1.size(); i++) {
            Type cit = lt1.get(i);
            if (!lt2.stream().filter(c -> equal(c, cit)).findFirst().isPresent()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param t
     * @return
     */
    public static int hash(Type t) {
        if (t == null) {
            return 0;
        }
        List<String> toks = tokenize(t.asString());
        List<Integer> hashes = new ArrayList<>();
        for (int i = 0; i < toks.size(); i++) {
            int idx = toks.get(i).lastIndexOf(".");
            if (idx > 0) {
                hashes.add(Objects.hashCode(toks.get(i).substring(idx + 1)));
            } else {
                hashes.add(Objects.hashCode(toks.get(i)));
            }
        }
        return hashes.hashCode();
    }

    /**
     * Verify that the referenceTypes are equal irregardless of package
     *
     * in situations where I am testing equality i.e. assertTrue(
     * Ast.typesEqual( Ast.typeRef("java.lang.String"), Ast.typeRef("String")));
     *
     * TODO: this doesnt work for matching crazy fully qualified Annotations and TypeBounds
     * FIXME: I need to probably walk/extract the Annotations separately (because they could be out of order
     * and partially/fully qualified, also I SHOULD match TypeParameter typeBounds with a more
     * robust matching (to ensure fully qualified names "java.util.Map" always match "Map")
     *
     * This (however) should work for the vast majority of cases
     * @param r1 the first reference TYPE
     * @param r2 the second reference TYPE
     * @return io
     */
    public static boolean equal(Type r1, Type r2) {
        if (Objects.equals(r1, r2)) {
            return true; //if they are ALREADY equal, return true
        }
        if (r1 == null || r2 == null) {
            //System.out.println( "ONE NULL" );
            return false;
        }
        if( r1.getClass() != r2.getClass() ){
            return false;
        }
        if( r1.isUnionType() && r2.isUnionType() ){ //UnionTypes "A | B" == "B | A"
            UnionType ut1 = r1.asUnionType();
            UnionType ut2 = r2.asUnionType();
            NodeList<ReferenceType> rts1 = ut1.getElements();
            NodeList<ReferenceType> rts2 = ut2.getElements();
            if( rts1.size() != rts2.size() ){
                return false;
            }
            return rts1.stream().allMatch( r-> rts2.stream().anyMatch(a-> equal(a, r)));
        }
        //if ONE or the OTHER is fully
        boolean r1FullyQualified = r1.asString().contains(".");
        boolean r2FullyQualified = r2.asString().contains(".");

        //OK, what I have to do is tokenize based on < > , space
        //ok, really... what I have to do is "build" tokens
        //XOR, if ONE or the OTHER (NOT BOTH or NEITHER) are fully qualified
        if ((r1FullyQualified || r2FullyQualified) && !(r1FullyQualified && r2FullyQualified)) {
            List<String> r1Toks = tokenize(r1.asString());
            List<String> r2Toks = tokenize(r2.asString());
            if (r1Toks.size() != r2Toks.size()) {
                return false;
            }
            for (int i = 0; i < r1Toks.size(); i++) {
                if (!r1Toks.get(i).equals(r2Toks.get(i))) {
                    r1FullyQualified = r1Toks.get(i).contains(".");
                    r2FullyQualified = r2Toks.get(i).contains(".");

                    if ((r1FullyQualified || r2FullyQualified) && !(r1FullyQualified && r2FullyQualified)) {
                        String s1 = r1Toks.get(i);
                        if (s1.contains(".")) {
                            s1 = s1.substring(s1.lastIndexOf(".") + 1);
                        }
                        String s2 = r2Toks.get(i);
                        if (s2.contains(".")) {
                            s2 = s2.substring(s2.lastIndexOf(".") + 1);
                        }
                        //
                        if (!s1.equals(s2)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        //here we could have both types that have '.'s but one is from an inner class
        // i.e.
        // _typeRef _t1 = _typeRef.of("_enum._constant");
        // _typeRef _t2 = _typeRef.of("org.jdraft._enum._constant");
        //ok... lets parse out the first

        List<String> t1s = tokenize(r1);
        List<String> t2s = tokenize(r2);
        if (t1s.size() != t2s.size()) {
            return false;
        }
        String s1n  = t1s.get(0); //get the first (type) token
        String s2n  = t2s.get(0); //get the second (type) token
        int dotIndex1 = s1n.lastIndexOf('.');
        int dotIndex2 = s2n.lastIndexOf('.');
        if( dotIndex1 > 0 ){ //this means BOTH must be qualified (partially or otherwise)
            //Log.info( "BOTH PARTIALLY QUALIFIED");
            if( s1n.contains(s2n) || s2n.contains(s1n) ){ //compare that either one contains the other JUST THE NAME NOT GENERICS
                //String normalizedName = s1n.substring(dotIndex);
                String t1 = r1.asString().substring(dotIndex1+1);
                String t2 = r2.asString().substring(dotIndex2+1);
                //call types equal on the SIMPLE type name and (potentially) any generics after it
                return equal( of(t1), of(t2));
            }
        }
        return false;
    }
}
