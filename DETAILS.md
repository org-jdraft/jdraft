## jdraft 
#### for analyzing, modifying and running .java source code at runtime
```java
/* jdraft is tool to help programmers write code that analyzes or modifes java source code.  
   to use jdraft, one must take .java code (as text from a String, File, or in a Jar)
   and convert it into a jdraft model so the code can be analyzed or modified.
 */

/* make a model (_m) from a String representing a method */
_method _m = _method.of("public double m(){ return x; }");

/* the model (_m) is easy to analyze */
assertEquals( "m", _m.getName() );
assertTrue( _m.isType(double.class) );
assertFalse( _m.hasThrows() );
 
/* each model (_m) can be built of other models (_ms, _t) */
_modifiers _ms = _m.getModifiers();
assertTrue( _ms.isPublic() );

_typeRef _t = _m.getType();
assertTrue(_t.is(int.class));

/* models (_m, _ms) are mutable */
_m.name("getX")            //change method name to getX()
  .anno(Deprecated.class)  //add Deprecated annotation

/* internally jdraft models (_m, _t) are ad-hoc wrappers for JavaParser Ast instances (mAst, tAst) */
MethodDeclaration mAst = _m.ast();
Type tAst = _t.ast();

/* if we modify the AST(mAst) the _jdraft model(_m) will reflect the change */
mAst.setFinal(true);

assertTrue( mAst.isFinal());
assertTrue( _m.isFinal());

/* retrieving / writing the source of a jdraft model(_m, _ms, _t) is simple */
System.out.println( _m );        
Strings modifiers = _ms.toString();

/* a _class (_c) can contain the _method model (_m) inside */
_class _c = _class.of("graph.Point")
    .method(_m); //add the method to the class

/* we can add fields the _class(_c) */ 
_c.field("public double x=0;"); //_method (_m) needs an int field x
_c.field("public double y=0;"); // add a field for the y coordinate

/* jdraft has "shortcuts" for building & modifying models with runtime code */

// create a new _method getY() with the SOURCE CODE of the Anonymous Object's getY method
// ...then add the _method to the _class (_c)
_c.method( new Object(){
    public double getY(){
        return this.y;
    }
    @_remove double y; //this field is NOT added to the _class _c
});
```

## jdraft Macros
```java
/* jdraft has built in macros that can be applied via runtime @Annotations */

// here we apply the @_final annotation macro to the fields x and y
// ...then we apply the @_dto annotation macro to build: 
//    the appropriate constructor for _c
//    getter methods for _c
//    setter methods for _c
//    an equals method for _c
//    a hashCode method for _c
//    a toString method for _c
_c = _class.of("graph.Point", new @_dto Object(){ @_final double x, y; });

System.out.println( _c);

// heres the output:
package graph;

public class Point {

    final double x, y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Point test = (Point) o;
        boolean eq = true;
        eq = eq && Double.compare(this.x, test.x) == 0;
        eq = eq && Double.compare(this.y, test.y) == 0;
        return eq;
    }

    public int hashCode() {
        int hash = 101;
        int prime = 197;
        hash = hash * prime + (int) (Double.doubleToLongBits(x) ^ (Double.doubleToLongBits(x) >>> 32));
        hash = hash * prime + (int) (Double.doubleToLongBits(y) ^ (Double.doubleToLongBits(y) >>> 32));
        return hash;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Point").append("{");
        sb.append(System.lineSeparator());
        sb.append(" x: ").append(x).append(System.lineSeparator());
        sb.append(" y: ").append(y).append(System.lineSeparator());
        sb.append("}");
        return sb.toString();
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
```