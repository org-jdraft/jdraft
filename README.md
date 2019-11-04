## jdraft
[![Java 8+](https://img.shields.io/badge/java-8+-4c7e9f.svg)](http://www.oracle.com/technetwork/java/javase/downloads)
[![Apache License 2](https://img.shields.io/badge/license-APL2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

### *What* is it?
jdraft represents Java source code as `_draft` objects & has tools for 
analyzing, modifying, querying, diffing, running and testing `_draft` objects.

### *Who* is it for?
Java developers who manage lots of Java source code
(to analyze code, debug code, maintain code & evolve code over time.)

### *What* is it (technically)?
The core of jdraft is an API of stateless facade instances (`_class`, `_method`, `_field`,...) that delegate 
operations to stateful JavaParser Ast instances (`ClassOrInterfaceDeclaration`, `MethodDeclaration`, `FieldDeclaration`, ...) 

These `_draft` facades provides a "uniform interface" for tools to walk, analyze and manipulate 
the Ast naturally from a Java program. 
(Developers can use jdrafts' built-in tools, or write custom tools to do things with Java source code)
   
### *What* is the point? (Representation & using the "right tool for the job")   
Normally, developers are familiar with the String representation of Java code:
```java
String srcCode = "public class Point{ double x; double y = 1.0; }"   
``` 
...but code (as Strings is files) is hard to "use" in a programmatic sense (i.e. <I>the complexity of the program 
required to change the fields `x` &  `y` from `double` to `float` is non-trivial as the String has to be parsed FIRST 
(into an AST/or tree), then Nodes in the tree have to be crawled, and data changed etc. etc.</I>)

Using the jdraft Representation makes the tasks of writing this program (to modify Java code) easy:
```java
// convert the String to _class, modify both fields to be float, write back to a String 
srcCode = _class.of(srcCode).forFields(f-> f.type(float.class)).toString();
```
  
### Why?
developers should feel empowered
with a small tool do big things quickly and automatically on large scale codebases  
should be easy to learn and use 
**_"more improv, less batch job"_** 
Comparison tests for Tools
[Eclipse JDT]()
[Google Auto Value]()
[Google Error Prone]()
[IntelliJ PSI]()
[JavaParser]()
[JavaPoet]()
[RascalMPL]()
[RoslynSyntax]()
[Semmle]()
[Spoon]()
<OL>
<LI>Metaprogramming</LI>
<LI>Code Generation</LI> 
<LI>Lint Checking / Code Metrics</LI>
<LI>Code Querying</LI>
<LI>Code Evolution</LI>
</OL>

### *How* to setup and use jdraft
jdraft requires (2) things to compile/build/run:
1. a JDK 1.8+ (*not a ~~JRE~~*)
2. a current version of [javaparser-core](https://github.com/javaparser)
```xml
<dependency>
  <groupId>com.github.javaparser</groupId>
  <artifactId>javaparser-core</artifactId>
  <version>3.15.3</version>
</dependency>
```   
 
### *How* to build jdraft models 
1. build individual `_draft` models `_class(_c), _field(_x, _y), _method(_getX, _getY, _setX, _setY)` 
from Strings & compose them together: 
```java 
_class _c = _class.of("package graph;","public class Point{}");
_field _x = _field.of("public double x;");
_field _y = _field.of("public double y;");
_method _getX = _method.of("public double getX(){ return x; }");
_method _setX = _method.of("public void setX(double x){ this.x = x;}");
_method _getY = _method.of("public double getY(){ return y; }");
_method _setY = _method.of("public void setY(double y){ this.y = y;}");

// _draft models compose..add fields and methods to _c:
_c.fields(_x, _y).methods(_getX, _getY, _setX, _setY );

// toString() will return the source code 
System.out.println(_c);
```
><PRE>
>package graph;
>
>public class Point {
>    public double x;
>    public double y;
>    public double getX() { return this.x; }
>    public void setX(double x) { this.x = x; }
>    public double getY() { return y; }
>    public void setY(double y) {this.y = y; }
>}</PRE>   
2. build a `_draft` model from source of an existing class (`_class.of(Point.class)`)<BR/> 
<I>(NOTE: this more preferred mechanism to using Strings above, and it works for Top level Classes,
Nested Classes, and Local Classes)</I> :
```java
class Point{
    public double x;
    public double y;
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
} 
_class _c = _class.of(Point.class);
```
3. build `_draft` models from the source of an anonymous Object:
```java
_class _c = _class.of("graph.Point", new Object(){
    public double x;
    public double y;
    public double getX(){
        return x;
    }  
    public void setX(double x){
        this.x = x;
    }
    public double getY(){
        return y;
    }  
    public void setY(double y){
        this.y = y;
    }
});
```
4. build `_draft` models with *@macros* (`@_get` & `@_set` auto generate `getX()`,`getY()`,`setX()` & `setY()` methods)
```java 
_class _c = _class.of("graph.Point", 
    new @_get @_set Object(){ public double x,y;});
```
### *How* to run (compile, load, eval) the `_draft` models/source at runtime
```java
// the @_dto @macro will generate getX(),getY(),setX(),setY(), equals(), hashCode() & toString()
// methods as well as a no-arg constructor in the `_draft` model
_class _c = _class.of("graph.Point", new @_dto Object(){
     public double x,y;
     });
// add the distance method to `_point`
_point.method(new Object(){
    public double distanceTo( double x, double y ){
        return Math.sqrt((this.y - y) * (this.y - y) + (this.x - x) * (this.x - x));
    }
    @_remove double x, y;
});

//runtime
_runtime _r = _runtime.of(_c);
``` 
#### **_Query_** Java source code 