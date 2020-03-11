package test.othertools;

import static junit.framework.TestCase.*;
import org.jdraft.*;
import org.jdraft.io._path;
import org.jdraft.io._sources;
import org.jdraft.pattern.*;

import java.io.Serializable;

/**
 * Looking at the integrated IntelliJ Structured Source Query
 *
 * https://www.jetbrains.com/help/idea/structural-search-and-replace.html
 *
 * describe what Structural Search is and how to do it in IntelliJ (in video form)
 * https://www.youtube.com/watch?v=fIPr_ANBpFk
 *
 *
 */
public class IntelliJStructuralSearchTest{

    static _sources _SOURCE = _sources.of( _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java\\org\\jdraft\\diff") );

    public void testJavadocPatterns(){
        //Comments
        _( $comment.of() );

        /*
        //_field _f = _field.of("/** @deprecatedint i;"); */
        //System.out.println( _f);
        //assertEquals(1, $comment.javadocComment("@deprecated").countIn(_f));
        //TODO look for SPECIFIC javadoc tags
        //_( $.javadoc().$withTagName("deprecated") );

        //Javadoc annotated class
        //_( $class.of( $.javadoc(j -> j."@$tag$ $tagValue$")) );
/*
        _javadoc.of();
        //Javadoc annotated fields
        $field.of( $javadoc.of().with("@$tag$ $tagValue$"))

        Javadoc annotated methods
        $method.of( $javadoc.of().with("@$tag$ $tagValue$"))

        Javadoc annotated constructor
        $constructor.of( $javadoc.of().with("@$tag$ $tagValue$"))

        Javadoc tags
        $javadoc.of().with(

                Not annotated method
                $method.of(m-> !m.hasAnnos())

                XDoclet tag
                $.javadoc().withTag("@see"))
        $.javadoc().withTag("@ejb.$type$"))
*/
    }

    /**
     * Emulate 90% of the existing templates that exist in IntelliJ
     */
    public void testExistingTemplates(){
        //all fields
        _( $field.of() );

        //All fields within ANY class (i.e. not enums, interfaces, annotations)
        _( $.field().$isParent($class.of()) );

        //All inner classes (only _class es)
        _( $class.of(c-> !c.isTopLevel()) );

        //All inner types (all class, interface, annotation, enums)
        _( $type.of(t-> !t.isTopLevel()) );

        //All methods
        _( $method.of() );

        //all AnonymousClasses
        _( $ex.newEx(n-> n.isAnonymous()) );

        //$new.of(n-> n.hasBody());

        //Any initializer
        _( $.initBlock() );

        //Class constructors
        _( $.constructor().$isParent($class.of() ) );

        //ALL constructors
        _( $.constructor() );

        //Class implementing (exactly 2) interfaces
        _( $class.of(c-> c.listImplements().size() ==2) );

        //All Classes
        _ ( $class.of() );

        //ALL types (Classes interfaces enums, annotations)
        _( $type.of() );

        //Classes with type parameterized constructors
        _( $class.of(c-> c.hasTypeParameters()) );

        //Methods and constructors
        _( $.or($method.of(), $constructor.of() ) );

        //Deprecated methods
        _( $method.of( $anno.of(Deprecated.class)) );

        //Direct Subclasses
        _( $class.of( c -> c.hasExtends()) );

        //Subclass of a specific type
        _( $class.of( c -> c.isExtends("baseClassName")) );

        //Enums
        _( $enum.of() );

        //Fields of a class
        _( $field.of().$isParent( $class.of("className") ) );

        //All classes that implement interfaces
        _( $class.of(c-> c.hasImplements()) );

        //All classes that implement a specific interface
        _( $class.of(c-> c.isImplements(Serializable.class)) );

        //Instance fields of a class
        //$.instanceField().of($.CLASS);
        //$.instanceMethod().of($.CLASS);
        //$.instanceInit()

        //non static fields on classes
        _( $field.of().$not($.STATIC).$isParent($class.of()) );

        //instance fields of a specific class
        _( $field.of().$not($.STATIC).$isParent($class.of("specificClass")) );

        //Instance initializers
        _( $.initBlock(i-> !i.isStatic()) );

        //Interface not implemented or extended
        //$interface.of( i-> !i.hasExtends() && !i.hasImplements() )

        //Interfaces
        _( $interface.of() );

        //Package-private fields
        _( $field.of(f-> f.isPackagePrivate() ) ); //isPackagePrivate())

        //Static fields that are not final
        _( $field.of($.STATIC).$not($.FINAL) );

        //StaticInitializer
        //$.staticInit()
        _( $.initBlock(i-> i.isStatic()) );

        //Annotated Classes
        _( $class.of( c-> c.hasAnnos() ) );

        //Annotated Fields
        _( $field.of( f->f.hasAnnos() ) );

        //Annotated Methods
        _( $method.of( m->m.hasAnnos() ) );

        //Annotation Type Declarations
        _( $annotation.of() );

        //Annotation
        _( $anno.of() );

        //Expressions

        //all expressions of some type
        _( $ex.of(_lambda.class) );

        //all lambdas
        _( $.lambda() );


        //Array access
        _( $.arrayAccess() );

        //Assignments
        _( $.assign() );

        //Casts
        _( $.cast() );

        //Field Selections / Field Access
        _( $.fieldAccessExpr() );

        //$.fieldAccessExpr().name("url$Name$")

         //instanceof
        _( $.instanceOf() );

        // method Calls
        _( $.methodCall() );

        //method calls to deprecated methods
        //you got me here
        //how about FIRST we find all deprecated methods
        //$method $deprecatedMethods = $.method().$anno(Deprecated.class);

        //find all static direct access, find all
//$.methodCall(mc->getName

        //method references
        _( $.methodReference() );

        //new expressions
        _( $.newExpr() );

//simple method invocation with constant
        //method calls that use a single parameter literal
        _( $.methodCall( m -> m.countArguments()  == 1
                        && m.getArguments().isAt(0, a -> a.isLiteral() ) ) );

        //String concatenation with many operands
        _( $.binaryExpr( b-> b.isPlus() &&
                b.isLeft( _string.class ) || b.isRight( _string.class )) );

        //String literals
        _( $.stringLiteral() );

        //DiamondOperators
        //$.methodCall(m-> m.isUsingDiamondOperator());
        //$.constructorCallStmt( c -> c.isUsingDiamondOperator() );
        //$.newExpr(n-> n.isUsingDiamondOperator());

        //Diamond Operators
        _( $.or( $.methodCall(m-> m.isUsingDiamondOperator()),
                $.constructorCallStmt( c -> c.isUsingDiamondOperator() ),
                $.newExpr(n-> n.isUsingDiamondOperator()) ) );
        //$.thisCallStmt( t-> t.isUsingDi)
        //_( $.hasDiamondOperator()
        //$.typeParameters(t.isDiamondOperator());

        //Generic Cast
        _( $.cast( c-> c.getTypeRef().isGenericType() ) );

        //Generic Classes
        _( $class.of(c-> c.hasTypeParameters()) );
        //$class.of(c->c.isGeneric())
        //$class.of().$isGeneric()

        //Generic Constructors
        $constructor.of(c-> c.hasTypeParameters());
        //is not generic
        $constructor.of(c-> !c.hasTypeParameters());


        //Generic Methods
        $method.of(m-> m.hasTypeParameters());
        //not generic
        $method.of(m-> !m.hasTypeParameters());


        //Method returns bounded wildcard
        //TODO
        //$type().isWildcard() //<?
        //$typeParameter.of().isWildcard()

        //TODO check/fix this
        //$method.of(m -> m.isTypeRef(t-> t.isGenericType() && t.getTypeArguments()getGt.isWildcard()));
        //Type<? extends $bound$>

        //type var substitutions in instanceof with generic types
        //instanceof with generic type
        $.instanceOf(i-> i.isTypeRef(t-> t.isGenericType()));

        //typed symbol
        $typeRef.of(t-> t.isGenericType());

        //variables with generic types
        //wont work with existing pattern impl, wait for prototype
        //$.variable().$
        //$.variable( $typeRef.of(t->t.isGeneric()))

        //all literals
        //
        _( $.literal() );

        //all primitives
        _( $typeRef.of( t-> t.isPrimitive() ) );
        //$typeRef $primitive = $typeRef.or(int.class, boolean.class, long.class, double.class, float.class, char.class, short.class, byte.class);

        // boxed (primitive) types
        _( $typeRef.or(Integer.class, Boolean.class, Long.class, Double.class,  Float.class, Character.class, Short.class, Byte.class) );


        //literals (accept null, string)
        _( $.literal().$not(_string.class, _null.class) );


        //$.variable( v-> v. ).$typeRef($primitive).$init($LITERALS)

        //boxing in method calls
        //????

        //fields variables with a given name
//i.e. a name that starts with url
        _( $var.of().$name("url$Any$") );

        //break to label
        _( $.breakStmt(b-> b.hasLabel()) );

        //final methods and constructors
        _( $.or( $method.of($.FINAL), $constructor.of($.FINAL)) );

        //switch statements with few branches
        _( $.switchStmt(s -> s.countSwitchEntries() <= 4) );

        //try statements with resources and 1 or more catch blocks
        _( $.tryStmt(t-> t.hasWithResources() && t.hasCatch() ) );

        //unboxed expressions
        //_( $varEx.of( $.PRIMITIVE_TYPE ).$init( $.WRAPPER_TYPE ) )
        /* TODO I NEED TO FIX pattern var
        _( $.variable( v-> v.get$.typeRef(t->t.isPrimitive() ).$init( $.WRAPPER_TYPE ) )
        unboxed method calls
                ???

        usage of derived type in cast
???     */

        //Ejb interface
        _( $interface.of(i-> i.hasMethods()).$extend("EJBObject") );

        //EJB class
        _( $class.of().$implements("EntityBean") );

        //Servlet
        _( $class.of().$extends( "HttpServlet") );

        //SessionEjb
        _( $class.of().$extends("SessionBean") );

        //Struts Action
        _( $class.of().$extends("Action") );

        //BeanInfo Class
        _( $class.of().$implements("BeanInfo") );

        //Cloneable class
        //find all cloneable classes
        _( $class.of().$implements(Cloneable.class ) );

        //all direct testcases
        _( $class.of().$extends( junit.framework.TestCase.class ) );

        //I could search for n levels deep
        //this is another tool (outside of pattern)
        //_typeTree _tt = _typeTree.of(_archive.of("C:\\temp\\mycode.jar"));
        //List<_typeTree._typeNode> _tn = _tt.listAllDescendants(TestCase.class);
        //_tn.get(0).fullyQualifiedClassName;

         //find DIRECT implementers
         $class.of().$implements(Serializable.class);

         //find ALL implementers
         //_typeTree _tt = _typeTree.of(_archive.of("C:\\temp\\mycode.jar"));
         //List<_typeTree._typeNode> _tn = _tt.listAllDescendants(TestCase.class);
         //_tn.get(0).fullyQualifiedClassName;

//recursive find all
//extends implement


//anyMatch (in nodeList)
//allMatch (in nodeList)

//singletons
        //methods
        //constructors
        //anyMatch
        //allMatch
        //_class _c = _class.of("C");
        //$class.of(  _c.listConstructors().stream().allMatch(c-> c.isFinal() ) );
        _( $class.of( c-> c.allConstructors( ct-> ct.isFinal()) ) );
        _( $class.of( c-> c.allMethods( m-> m.isFinal()) ) );
        _( $class.of( c-> c.allFields( f-> f.isFinal()) ) );
        _( $class.of( c-> c.allInitBlocks( i-> i.isStatic() ) ) );
        _( $class.of( c-> c.allTypeParameters( tp-> !tp.hasTypeBound() ) ) );
        _( $class.of( c-> c.allAnnos( a-> !a.isNamed("todo") ) ) );

        //Singletons
        // OR classes that HAVE defined constructors that are ALL private
        _( $class.of(c-> c.hasConstructors()
                && c.allConstructors(ct-> ct.isPrivate())) );

        //assertStatement without descriptions/messages
        _( $.assertStmt(a-> !a.hasMessage()) );

        //forEachLoops
        _( $.forEachStmt() );

        //ifs
        _( $.ifStmt() );

        //logging without if
        //$.methodCall("$LOG$.debug($args$)").$isParentNot($.ifStmt("if( $LOG$.isDebugEnabled())")) ??

        //switch statement
        _($.switchStmt());
        _($.switchExpr());

        //try
        _( $.tryStmt() );
    }

    public void _($pattern $p){
        $p.countIn(_SOURCE);
    }



}
