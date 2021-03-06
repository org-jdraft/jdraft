package test.othertools;

import com.github.javaparser.ast.CompilationUnit;

import static junit.framework.TestCase.*;
import org.jdraft.*;
import org.jdraft.io._archive;
import org.jdraft.io._io;
import org.jdraft.macro._static;
import org.jdraft.pattern.$;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Eclipse JDT core
 * https://www.vogella.com/tutorials/EclipseJDT/article.html#example-accessing-your-java-projects-with-the-jdt-java-model
 */

public class EclipseJDTTest{ //extends TestCase

    //read in/parse and cache some source code from a .jar file
    //public static _sources _cc = _sources.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");
            //_archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar"));


    //public static _archive _cc = //_sources.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");
    //    _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");

    public static _project _cc =
            _project.of( _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar"));

    /*
    public void testFile() throws FileNotFoundException {
        StaticJavaParser.parse( new FileInputStream("C:\\temp\\AnnotationUtils.java") );
    }
     */

    public void testAnn(){
        /**
         * @@since 5.0.5
         */
         class F{}
         _class _c = _class.of(F.class);
    }

    public void testE(){
        /**
         * or if it should be looked up via @{@link java.lang.annotation.Repeatable}
         */
         class F{

         }
         _class _c = _class.of(F.class);
         System.out.println( _c );
    }
    public void testR(){
        _archive _cc = _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");
        _cc.forEachFileAsBytes( (p, bs)-> {
            try{
                String s = new String(bs);

                Path pp = p;
                if( !p.toString().endsWith(".html") ) {
                    if( !p.toString().endsWith("package-info.java") && !p.toString().endsWith(".kt")) {
                        //
                        //System.out.println( ">START "+ p.toString());
                        try {
                            _type _t = _type.of(s);
                            //System.out.println( "<DONE "+ p.toString());
                        }catch(Exception ee){
                            //System.out.println( "EXCEPTION IN "+pp);
                        }
                    }
                }
            } catch(Exception e){
                System.out.println( "error on "+ p);
                e.printStackTrace();
            }
        });
    }

    public void testReadJavadocWithString(){

        _archive _cc = _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");
        _cc.forEachFileAsBytes( (p, bs)-> {
            try{
                String s = new String(bs);


                Path pp = p;
                if( !p.toString().endsWith(".html") ) {
                    if( !p.toString().endsWith("package-info.java") && !p.toString().endsWith(".kt")) {
                        System.out.println( "parsin "+ p.toString());
                        try {
                            _type _t = _type.of(s);
                        }catch(Exception ee){
                            System.out.println( "EXCEPTION IN "+pp);
                        }
                    }
                }
            } catch(Exception e){
               System.out.println( "error on "+ p);
               e.printStackTrace();
            }
        });
        /*
        _cc.for_code( c-> System.out.println( c.getFullName()));

        $comment $c = $comment.of((Predicate<Comment>) c -> ((Comment)c).getContent().contains("JAVADOC"));
        _cc.for_code( t-> $c.printIn(t) );
        //List<_type> _ts = _cc.list_types(t-> $c.printIn(t) );
        //_ts.forEach(t-> System.out.println( t.getFullName()));

         */
    }

    public void testReadAllPackagesAndMethods(){

        //print the distinct package names occurring in the code
        $.packageDecl().streamIn(_cc).map(p->p.getName()).distinct().forEach(e-> System.out.println( e ));

        //get the names of all the types along with the line count
        _cc.forEach(c-> System.out.println( c.getFullName() + ":" + c.astCompilationUnit().getRange().get().getLineCount() ));


        //print the name, signature and return type for all methods
        $.method().forEachIn(_cc, m->System.out.println(
                "Name:      " + m.getName()+System.lineSeparator()+
                "Signature: " + m.node().getSignature().toString()+System.lineSeparator()+
                "ReturnType: "+ m.getType()+System.lineSeparator() ));

    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#adding-a-hello-world-statement-using-the-ast
     *
     * String source = String.join("\n",
     *         "public class HelloWorld {",
     *         "    public static void main(String[] args) {",
     *                   // Insert the following statement.
     *                   // System.out.println("Hello, World");
     *         "    }",
     *         "}");
     *
     * ASTParser parser = ASTParser.newParser(AST.JLS8);
     * parser.setSource(source.toCharArray());
     * parser.setKind(ASTParser.K_COMPILATION_UNIT);
     *
     * CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
     *
     * unit.accept(new ASTVisitor() {
     *
     *     @SuppressWarnings("unchecked")
     *     public boolean visit(MethodDeclaration node) {
     *         AST ast = node.getAST();
     *
     *         MethodInvocation methodInvocation = ast.newMethodInvocation();
     *
     *         // System.out.println("Hello, World")
     *         QualifiedName qName =
     *                    ast.newQualifiedName(
     *                             ast.newSimpleName("System"),
     *                             ast.newSimpleName("out"));
     *         methodInvocation.setExpression(qName);
     *         methodInvocation.setName(ast.newSimpleName("println"));
     *
     *         StringLiteral literal = ast.newStringLiteral();
     *         literal.setLiteralValue("Hello, World");
     *         methodInvocation.arguments().add(literal);
     *
     *         // Append the statement
     *         node.getBody().statements().add(ast.newExpressionStatement(methodInvocation));
     *
     *         return super.visit(node);
     *     }
     * });
     */
    public void testAddSystemOutPrintln(){
        _class _c = _class.of("HelloWorld", new Object(){
            public @_static void main(String[] args){
            }
        });
        _c.firstMethodNamed("main").add( ()->System.out.println("Hello, World!") );
        //System.out.println( _c );
        assertEquals( Stmt.of( ()->System.out.println("Hello, World!")),
                _c.firstMethodNamed("main").getAstStatement(0) );
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#finding-all-elements-of-type-methoddeclaration
     */
    public void testFindAllMethods(){
        List<_method> _ms = $.method().listIn(_cc);
    }

    /**
     * A variant on the above, often you want to do more with the entities than just list them
     * here we return a stream then map to get method names then distinct, then limit and finally return
     *
     * we could add more "stages" to the stream, (i.e. additional filters, etc.) but it is just an
     * illustration
     */
    public void testFindFirst20DistinctMethodNames() {
        List<String> names = $.method().streamIn(_cc).map(_method::getName).distinct().limit(20).collect(Collectors.toList());
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#add-hello-world-statement-with-jdt-using-methoddeclarationfinder
     * String source = String.join("\n",
     *         "public class HelloWorld {",
     *         "    public static void main(String[] args) {",
     *                   // Insert the following statement.
     *                   // System.out.println("Hello, World");
     *         "    }",
     *         "}");
     *
     * ASTParser parser = ASTParser.newParser(AST.JLS8);
     * parser.setSource(source.toCharArray());
     * parser.setKind(ASTParser.K_COMPILATION_UNIT);
     *
     * CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
     * AST ast = unit.getAST();
     *
     * List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(unit);
     * for (MethodDeclaration methodDeclaration : methodDeclarations) {
     *     MethodInvocation methodInvocation = ast.newMethodInvocation();
     *
     *     // System.out.println("Hello, World")
     *     QualifiedName qName = ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
     *     methodInvocation.setExpression(qName);
     *     methodInvocation.setName(ast.newSimpleName("println"));
     *
     *     StringLiteral literal = ast.newStringLiteral();
     *     literal.setLiteralValue("Hello, World");
     *     methodInvocation.arguments().add(literal);
     *
     *     // Append the statement
     *     methodDeclaration.getBody().statements().add(ast.newExpressionStatement(methodInvocation));
     * }
     */
    public void testAddSystemOutPrintlnVia$method(){
        class HelloWorld{
            public @_static void main(String[] args){
                //Insert the following statement
                //System.out.println( "Hello, World");
            }
        }
        _class _c = _class.of(HelloWorld.class);
        //System.out.println(_c);
        //find the first method named "main" in the class & add the statement
        $.method($.name("main")).firstIn(_c)
                .add(()->System.out.println("Hello, World!"));

        //System.out.println(_c);
        assertEquals( Stmt.of( ()->System.out.println("Hello, World!")),
                _c.firstMethodNamed("main").getAstStatement(0));
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#creating-a-compilationunit-ast-from-file-in-workspace
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#creating-compilationunit-ast-from-file-on-disk
     */
    public void testCreateCompilationUnityFromFileInWorkspace(){
        CompilationUnit fromFileInWorkspace = Ast.of(EclipseJDTTest.class);
        CompilationUnit fromFileOnDisk = Ast.of(Paths.get("C:\\temp\\Point.java"));
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#adding-imports-with-astrewrite
     */
    public void testCreateAndAddImports(){
        _class _c = _class.of("X").addImports(List.class);
        _c.addImports(Set.class);
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#writing-recorded-changes-to-disk
     */
    public void testWritingChangesToDisk(){
        //create a _class model
        _class _c = _class.of(EclipseJDTTest.class);
        //make changes
        _c.addField("public static final int ID = 1234567;");
        //write source to disk
        Path filePath = _io.out("C:\\temp", _c);

        //lets verify the path that was written (i.e. were packages reflected?)
        System.out.println(filePath); //"C:\temp\test\spoon\EclipseJDTTest.java"
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#getting-an-element-at-a-certain-line-number
     */
    public void testGettingElementAtACertainLineNumber(){
        //_type _t = _java.type(EclipseJDTTest.class);
        //here we can get the most specific node (it should be a Node)
        /* removed temporarily causing issues
        Node n = At.at(EclipseJDTTest.class, 193, 10);
        System.out.println( n );// "Node"

        //here we can get a "member" (method, constructor, field) containing this location
        MethodDeclaration astMd = At.memberAt(EclipseJDTTest.class, 297, 10);
        assertEquals( "testGettingElementAtACertainLineNumber", astMd.getNameAsString() );

         */
    }
}
