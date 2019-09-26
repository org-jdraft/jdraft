package test.othertools;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import junit.framework.TestCase;
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
public class EclipseJDTTest extends TestCase {

    //read in/parse and cache some source code from a .jar file
    public static _code._cache _cc = _code._cache.of(
            _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar"));

    public void testReadAllPackagesAndMethods(){

        //print the distinct package names occurring in the code
        $.packageDecl().streamIn(_cc).map(p->p.getName()).distinct().forEach(e-> System.out.println( e ));

        //get the names of all the types along with the line count
        _cc.for_code(c-> System.out.println( c.getFullName() + ":" + c.astCompilationUnit().getRange().get().getLineCount() ));


        //print the name, signature and return type for all methods
        $.method().forEachIn(_cc, m->System.out.println(
                "Name:      " + m.getName()+System.lineSeparator()+
                "Signature: " + m.ast().getSignature().toString()+System.lineSeparator()+
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
        _c.getMethod("main").add( ()->System.out.println("Hello, World!") );
        //System.out.println( _c );
        assertEquals( Stmt.of( ()->System.out.println("Hello, World!")),
                _c.getMethod("main").getStatement(0) );
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
                _c.getMethod("main").getStatement(0));
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
        _class _c = _class.of("X").imports(List.class);
        _c.imports(Set.class);
    }

    /**
     * https://www.vogella.com/tutorials/EclipseJDT/article.html#writing-recorded-changes-to-disk
     */
    public void testWritingChangesToDisk(){
        //create a _class model
        _class _c = _class.of(EclipseJDTTest.class);
        //make changes
        _c.field("public static final int ID = 1234567;");
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
        Node n = Ast.at(EclipseJDTTest.class, 193, 10);
        System.out.println( n );// "Node"

        //here we can get a "member" (method, constructor, field) containing this location
        MethodDeclaration astMd = Ast.memberAt(EclipseJDTTest.class, 208, 10);
        assertEquals( "testGettingElementAtACertainLineNumber", astMd.getNameAsString() );
    }
}
