### jdraft
#### *What* is it?
jdraft represents Java source code as `_draft` objects & has tools for 
analyzing, modifying, and (compiling, loading) running of `_draft` objects.
#### *How* to 
#### *How* to build jdraft models 
1. build models `_class (_c), _field (_x), _method (_getX, _setX)` from Strings:  
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
```  
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