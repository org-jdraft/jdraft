package test.othertools;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._addImports;
import org.jdraft.macro._static;
import org.jdraft.pattern.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * I just wanted to compare and contrast how Roslyn (and C#) does
 * Source code querying.
 */
public class RoslynSyntaxTest extends TestCase {
    /**
     * https://github.com/dotnet/roslyn/wiki/Getting-Started-C%23-Syntax-Analysis#example---manually-traversing-the-tree
     */
    public void testParseAndQuery(){
        //granted this isnt the same language, I just wanted to show how a like thing is done in
        //jdraft (Java) vs C#
        _class _c = _class.of("HellowWorld", new @_addImports( Collections.class) Object(){
            @_static void main (String[] args){
                System.out.println( "Hello World");
            }
        });

        //examine the members (of the class)
        _c.forDeclared(m -> System.out.println(m));

        CompilationUnit cu = _c.astCompilationUnit();

        //list imports
        List<_import> li = _c.listImports();

        //gets the AST
        ClassOrInterfaceDeclaration programDeclaration = _c.ast();

        //get the _method
        _method _main = _c.firstMethodNamed("main");

        _param _p = _main.getParam(0);
    }

    /**
     * https://github.com/dotnet/roslyn/wiki/Getting-Started-C%23-Syntax-Analysis#example---using-query-methods
     *
     * // find the first parameter on
     * var firstParameters = from methodDeclaration in root.DescendantNodes()
     *                                                     .OfType<MethodDeclarationSyntax>()
     *                       where methodDeclaration.Identifier.ValueText == "Main"
     *                       select methodDeclaration.ParameterList.Parameters.First();
     */
    public void testQueryMethods(){
        @_addImports(Collection.class) class HelloWorld{
            public @_static void main(String[] args){
                System.out.println("Hello World");
            }
        }
        _class _c = _class.of(HelloWorld.class);

        //traditional (direct) walk walk the first method for the first parameter
        _c.getMethod(0).getParam(0);
        //find the main method and get the first parameter
        _c.firstMethodNamed("main").getParam(0);

        //protoype walk
        //get the first method named "main" in the type and get it['s first parameter
        $method.of($.name("main")).firstIn(HelloWorld.class).getParam(0);
    }

    public void testSyntaxWalkers(){
        @_addImports({Collection.class})
        class TopLevel{
            class Child1{
                class Foo{}
            }
            class Child2{
                class Bar{}
            }
        }
        //Java doesnt really have "namespaces" of usings statements but for
    }

    /**
     * https://docs.microsoft.com/en-us/dotnet/csharp/roslyn-sdk/get-started/semantic-analysis#querying-symbols
     *
     * https://docs.microsoft.com/en-us/dotnet/csharp/roslyn-sdk/get-started/semantic-analysis#binding-an-expression
     */
    public void testCreate(){
        class CCC{
            public String str = "THT";
            int i = 1 + 2;
        }
        $.literal().printIn(CCC.class);
    }

    /**
     * Roslyn
     * You can also build the full query using the LINQ query syntax, and then display all the method names in the console:
     *
     * foreach (string name in (from method in stringTypeSymbol
     *                          .GetMembers().OfType<IMethodSymbol>()
     *                          where method.ReturnType.Equals(stringTypeSymbol) &&
     *                          method.DeclaredAccessibility == Accessibility.Public
     *                          select method.Name).Distinct())
     * {
     *     Console.WriteLine(name);
     * }
     */
    public void testQueryForUniqueNames(){
        //all public methods that return a String
        $method $m = $method.of($.PUBLIC, $typeRef.of(String.class));
        class SimpleClass{
            public String trim(){ return "a"; }
            public String trim(int i){ return "b"; }
            public String compose(int i){ return "c"; }
            public String parse(int i){ return "b"; }

        }
        class AnotherClass{

        }
        $m.listIn(SimpleClass.class, AnotherClass.class).stream().map(_method::getName).distinct().forEach(System.out::println);

        //examples of returning streams that support chaining additional features (mapping, sorting, distinct)
        $m.streamIn(SimpleClass.class, AnotherClass.class).map(_method::getName).distinct().limit(5).forEach(System.out::println);

        //add sorting
        $m.streamIn(SimpleClass.class, AnotherClass.class).map(_method::getName).distinct().limit(5).sorted().forEach(System.out::println);
        $m.streamIn(SimpleClass.class, AnotherClass.class).map(_method::getName).distinct().limit(5).forEach(System.out::println);
    }
}
