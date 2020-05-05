## jdraft
[![Java 8+](https://img.shields.io/badge/java-8+-4c7e9f.svg)](http://www.oracle.com/technetwork/java/javase/downloads)
[![Apache License 2](https://img.shields.io/badge/license-APL2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

### *What* is it?
jdraft represents Java code as model objects & has tools for 
*analyzing*, *modifying*, *querying*, *diffing*, *running* and *testing* these models.

```java
_null.of();              // a null literal
_int.of(1);              // a literal int 1
_expression.of("1 + 2"); // binary expression "1 + 2"
_statement.of("System.out.println(\"x=\"+x);");
_method.of("public void getX(){ return this.x; }");
_class.of("public class Point{ int x, y; }");

//model all of the java sources within a jar file
_archive.of("C:\\Users\\Eric\\Downloads\\guava-28.1-jre-sources.jar");

//model all of the java sources from a github project (head) & maven central sources
_project.of(
    _githubProject.of("https://github.com/org-jdraft/jdraft"),
    _mavenCentral.of("com.github.javaparser", "javaparser-core", "3.15.21")
);
```

### *Why?*   
Java code is represented as a String:
```java
String srcCode = "public class Point{ double x; double y = 1.0; }"   
``` 
...but code (as a String within one or more files) is hard to *"use"* in a programmatic sense: 
(i.e. <I>it is HARD to write a program to *modify* the fields `x` &  `y` from `double` to `float` 
as the String has to be parsed FIRST (into an AST/or tree), then "nodes" in the tree have to be 
crawled, and data changed etc. etc.</I>)

Using the jdraft models as an intermediatry makes this programming task (to modify Java code) easy to write _AND_ read:
```java
// convert the String to _class, modify both fields to be float, write back to a String 
srcCode = _class.of(srcCode).forFields(f-> f.setType(float.class)).toString();
```
  
### Why?
developers should feel empowered with a small tool do big things quickly and automatically on large scale codebases  
should be easy to learn and use for tasks like 
<OL>
<LI>Metaprogramming</LI>
<LI>Code Generation</LI> 
<LI>Code Inspection</LI>
<LI>Code Querying</LI>
<LI>Code Evolution</LI>
</OL>
**_"more improv, less batch job"_** 

### Comparison tests for related tools:
 - [Eclipse JDT](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/EclipseJDTTest.java)
 - [Google Auto Value](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/GoogleAutoValueTest.java)
 - [Google Error Prone](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/GoogleErrorProneTest.java)
 - [IntelliJ PSI](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/IntelliJPSIExample.java)
 - [JavaParser](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/JavaParserWebsiteTest.java)
 - [JavaPoet](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/JavaPoetTest.java)
 - [Manifold](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/ManifoldTest.java)
 - [RascalMPL](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/RascalMPLTest.java)
 - [RoslynSyntax](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/RoslynSyntaxTest.java)
 - [Semmle](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/SemmleLGTMQueryTests.java)
 - [Spoon](https://github.com/org-jdraft/jdraft/blob/master/src/test/java/test/othertools/SpoolAnalysisTest.java)


### *How* to setup and use jdraft
jdraft requires (2) things to compile/build/run:
1. a JDK 1.8+ (*not a ~~JRE~~*)
2. a current version of [javaparser-core](https://github.com/javaparser)
```xml
<dependency>
  <groupId>com.github.javaparser</groupId>
  <artifactId>javaparser-core</artifactId>
  <version>3.15.21</version>
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
