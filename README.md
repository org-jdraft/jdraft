### jdraft
#### *What* is it?
jdraft represents Java source code as `_draft` objects & has tools for 
analyzing, modifying, querying, and running of `_draft` objects.

#### *How* to setup and use jdraft
jdraft requires (2) things to compile/build or run:
1. a JDK (NOT JRE) that is 1.8 (Java 8) or later
2. the latest version of JavaParser (3.15.3)
*(until its up on MavenCentral)* download and compile to source, you'll only need (1) 
dependency the current version of JavaParser:
```xml
<dependency>
  <groupId>com.github.javaparser</groupId>
  <artifactId>javaparser-core</artifactId>
  <version>3.15.3</version>
</dependency>
```   
 
#### *How* to build jdraft models 
1. build models `_class(_c), _field(_x, _y), _method(_getX, _getY, _setX, _setY)` from Strings:  
```java 
_class _c = _class.of("package graph;","public class Point{}");
_field _x = _field.of("public double x;");
_field _y = _field.of("public double y;");
_method _getX = _method.of("public double getX(){ return x; }");
_method _setX = _method.of("public void setX(double x){ this.x = x;}");
_method _getY = _method.of("public double getY(){ return y; }");
_method _setY = _method.of("public void setY(double y){ this.y = y;}");

// _draft models compose:
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
>
>    public double getX() {
>        return x;
>    }
>
>    public void setX(double x) {
>        this.x = x;
>    }
>
>    public double getY() {
>        return y;
>    }
>
>    public void setY(double y) {
>        this.y = y;
>    }
>}   

2. build models from the source of an anonymous Object:
```java
_class _c = _class.of("graph.Point", new Object(){
    public double x, y;
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
3. build with Macros (`@_get` and `@_set` auto generate get() and set() methods)
```java 
_class _c = _class.of("graph.Point", 
    new @_get @_set Object(){ public double x,y;});
```
#### *Query* Java source code 