### jdraft
#### What is it?
jdraft creates mutable object representations of Java source code & has tools for
analyzing, modifying, and running these models.
#### How to build jdraft models 
1) build & compose individual models:
```java 
_class _c = _class.of("package graph;","public class Point{}");
_field _x = _field.of("public double x;");
_method _getX = _method.of("public double getX(){ return x; }");
_method _setX = _method.of("public void setX(double x){ this.x = x;}");

// _draft models are compose:
_c.field(_x).methods(_getX, _getY);

```  