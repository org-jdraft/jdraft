### jdraft

#### *What* is it?
jdraft represents Java source code as `_draft` objects & has tools for 
analyzing, modifying, querying, diffing, running and testing `_draft` objects.

#### *What* is it used for?
jdraft was built specifically to help developers write simple programs 
that can generate (& test) new Java code or modify (& test) existing Java code.

**_"more improv, less batch job"_** 
<OL>
<LI>Metaprogramming</LI>
<LI>Code Generation</LI> 
<LI>Lint Checking / Code Metrics</LI>
<LI>Code Querying</LI>
<LI>Code Evolution</LI>
</OL>

#### *How* to setup and use jdraft
jdraft requires (2) things to compile/build/run:
1. a JDK 1.8+ (*not a ~~JRE~~*)
2. a current version of [JavaParser](https://github.com/javaparser)

```xml
<dependency>
  <groupId>com.github.javaparser</groupId>
  <artifactId>javaparser-core</artifactId>
  <version>3.15.3</version>
</dependency>
```   
 
#### *How* to build jdraft models 
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
>    public double getX() { return this.x; }
>    public void setX(double x) { this.x = x; }
>    public double getY() { return y; }
>    public void setY(double y) {this.y = y; }
>}</PRE>   
2. build a `_draft` model from source of an existing class (`_class.of(Point.class)`) :
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
#### **_Query_** Java source code 