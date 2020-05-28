package test.othertools;

import org.jdraft.*;
import org.jdraft.io._path;
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

    static _project _SOURCE = _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java\\org\\jdraft\\diff").load();

    public void testJavadocPatterns(){
        //Comments
        __( $comment.of() );

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
        __( $field.of() );

        //All fields within ANY class (i.e. not enums, interfaces, annotations)
        __( $.field().$isParent($class.of()) );

        //All inner classes (only _class es)
        __( $class.of(c-> !c.isTopLevel()) );

        //All inner types (all class, interface, annotation, enums)
        __( $type.of(t-> !t.isTopLevel()) );

        //All methods
        __( $method.of() );

        //all AnonymousClasses
        __( $ex.newEx(n-> n.isAnonymous()) );

        //$new.of(n-> n.hasBody());

        //Any initializer
        __( $.initBlock() );

        //Class constructors
        __( $.constructor().$isParent($class.of() ) );

        //ALL constructors
        __( $.constructor() );

        //Class implementing (exactly 2) interfaces
        __( $class.of(c-> c.listAstImplements().size() ==2) );

        //All Classes
        __( $class.of() );

        //ALL types (Classes interfaces enums, annotations)
        __( $type.of() );

        //Classes with type parameterized constructors
        __( $class.of(c-> c.hasTypeParams()) );

        //Methods and constructors
        __( $.or($method.of(), $constructor.of() ) );

        //Deprecated methods
        __( $method.of( $annoRef.of(Deprecated.class)) );

        //Direct Subclasses
        __( $class.of(c -> c.hasExtends()) );

        //Subclass of a specific type
        __( $class.of(c -> c.isExtends("baseClassName")) );

        //Enums
        __( $enum.of() );

        //Fields of a class
        __( $field.of().$isParent( $class.of("className") ) );

        //All classes that implement interfaces
        __( $class.of(c-> c.hasImplements()) );

        //All classes that implement a specific interface
        __( $class.of(c-> c.isImplements(Serializable.class)) );

        //Instance fields of a class
        //$.instanceField().of($.CLASS);
        //$.instanceMethod().of($.CLASS);
        //$.instanceInit()

        //non static fields on classes
        __( $field.of().$not($.STATIC).$isParent($class.of()) );

        //instance fields of a specific class
        __( $field.of().$not($.STATIC).$isParent($class.of("specificClass")) );

        //Instance initializers
        __( $.initBlock(i-> !i.isStatic()) );

        //Interface not implemented or extended
        //$interface.of( i-> !i.hasExtends() && !i.hasImplements() )

        //Interfaces
        __( $interface.of() );

        //Package-private fields
        __( $field.of(f-> f.isPackagePrivate() ) ); //isPackagePrivate())

        //Static fields that are not final
        __( $field.of($.STATIC).$not($.FINAL) );

        //StaticInitializer
        //$.staticInit()
        __( $.initBlock(i-> i.isStatic()) );

        //Annotated Classes
        __( $class.of(c-> c.hasAnnoExprs() ) );

        //Annotated Fields
        __( $field.of(f->f.hasAnnoExprs() ) );

        //Annotated Methods
        __( $method.of(m->m.hasAnnoExprs() ) );

        //Annotation Type Declarations
        __( $annotation.of() );

        //Annotation
        __( $annoRef.of() );

        //Expressions

        //all expressions of some type
        __( $ex.of(_lambdaExpr.class) );

        //all lambdas
        __( $.lambda() );


        //Array access
        __( $.arrayAccess() );

        //Assignments
        __( $.assign() );

        //Casts
        __( $.cast() );

        //Field Selections / Field Access
        __( $.fieldAccessExpr() );

        //$.fieldAccessExpr().name("url$Name$")

         //instanceof
        __( $.instanceOf() );

        // method Calls
        __( $.methodCall() );

        //method calls to deprecated methods
        //you got me here
        //how about FIRST we find all deprecated methods
        //$method $deprecatedMethods = $.method().$anno(Deprecated.class);

        //find all static direct access, find all
//$.methodCall(mc->getName

        //method references
        __( $.methodReference() );

        //new expressions
        __( $.newExpr() );

//simple method invocation with constant
        //method calls that use a single parameter literal
        __( $.methodCall(m -> m.countArgs()  == 1
                        && m.getArgs().isAt(0, a -> a.isLiteral() ) ) );

        //String concatenation with many operands
        __( $.binaryExpr(b-> b.isPlus() &&
                b.isLeft( _stringExpr.class ) || b.isRight( _stringExpr.class )) );

        //String literals
        __( $.stringLiteral() );

        //DiamondOperators
        //$.methodCall(m-> m.isUsingDiamondOperator());
        //$.constructorCallStmt( c -> c.isUsingDiamondOperator() );
        //$.newExpr(n-> n.isUsingDiamondOperator());

        //Diamond Operators
        __( $.or( $.methodCall(m-> m.isUsingDiamondOperator()),
                $.constructorCallStmt( c -> c.isUsingDiamondOperator() ),
                $.newExpr(n-> n.isUsingDiamondOperator()) ) );
        //$.thisCallStmt( t-> t.isUsingDi)
        //_( $.hasDiamondOperator()
        //$.typeParameters(t.isDiamondOperator());

        //Generic Cast
        __( $.cast(c-> c.getTypeRef().isGenericType() ) );

        //Generic Classes
        __( $class.of(c-> c.hasTypeParams()) );
        //$class.of(c->c.isGeneric())
        //$class.of().$isGeneric()

        //Generic Constructors
        $constructor.of(c-> c.hasTypeParams());
        //is not generic
        $constructor.of(c-> !c.hasTypeParams());


        //Generic Methods
        $method.of(m-> m.hasTypeParams());
        //not generic
        $method.of(m-> !m.hasTypeParams());


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
        __( $.literal() );

        //all primitives
        __( $typeRef.of(t-> t.isPrimitiveType() ) );
        //$typeRef $primitive = $typeRef.or(int.class, boolean.class, long.class, double.class, float.class, char.class, short.class, byte.class);

        // boxed (primitive) types
        __( $typeRef.or(Integer.class, Boolean.class, Long.class, Double.class,  Float.class, Character.class, Short.class, Byte.class) );


        //literals (accept null, string)
        __( $.literal().$not(_stringExpr.class, _nullExpr.class) );


        //$.variable( v-> v. ).$typeRef($primitive).$init($LITERALS)

        //boxing in method calls
        //????

        //fields variables with a given name
//i.e. a name that starts with url
        __( $var.of().$name("url$Any$") );

        //break to label
        __( $.breakStmt(b-> b.hasLabel()) );

        //final methods and constructors
        __( $.or( $method.of($.FINAL), $constructor.of($.FINAL)) );

        //switch statements with few branches
        __( $.switchStmt(s -> s.countSwitchEntries() <= 4) );

        //try statements with resources and 1 or more catch blocks
        __( $.tryStmt(t-> t.hasWithResources() && t.hasCatch() ) );

        //unboxed expressions
        //_( $varEx.of( $.PRIMITIVE_TYPE ).$init( $.WRAPPER_TYPE ) )
        /* TODO I NEED TO FIX pattern var
        _( $.variable( v-> v.get$.typeRef(t->t.isPrimitive() ).$init( $.WRAPPER_TYPE ) )
        unboxed method calls
                ???

        usage of derived type in cast
???     */

        //Ejb interface
        __( $interface.of(i-> i.hasMethods()).$extend("EJBObject") );

        //EJB class
        __( $class.of().$implements("EntityBean") );

        //Servlet
        __( $class.of().$extends( "HttpServlet") );

        //SessionEjb
        __( $class.of().$extends("SessionBean") );

        //Struts Action
        __( $class.of().$extends("Action") );

        //BeanInfo Class
        __( $class.of().$implements("BeanInfo") );

        //Cloneable class
        //find all cloneable classes
        __( $class.of().$implements(Cloneable.class ) );

        //all direct testcases
        __( $class.of().$extends( junit.framework.TestCase.class ) );

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
        __( $class.of(c-> c.allConstructors(ct-> ct.isFinal()) ) );
        __( $class.of(c-> c.allMethods(m-> m.isFinal()) ) );
        __( $class.of(c-> c.allFields(f-> f.isFinal()) ) );
        __( $class.of(c-> c.allInitBlocks(i-> i.isStatic() ) ) );
        __( $class.of(c-> c.allTypeParams(tp-> !tp.hasTypeBound() ) ) );
        __( $class.of(c-> c.isAllAnnoExprs(a-> !a.isNamed("todo") ) ) );

        //Singletons
        // OR classes that HAVE defined constructors that are ALL private
        __( $class.of(c-> c.hasConstructors()
                && c.allConstructors(ct-> ct.isPrivate())) );

        //assertStatement without descriptions/messages
        __( $.assertStmt(a-> !a.hasMessage()) );

        //forEachLoops
        __( $.forEachStmt() );

        //ifs
        __( $.ifStmt() );

        //logging without if
        //$.methodCall("$LOG$.debug($args$)").$isParentNot($.ifStmt("if( $LOG$.isDebugEnabled())")) ??

        //switch statement
        __($.switchStmt());
        __($.switchExpr());

        //try
        __( $.tryStmt() );
    }

    public void __($pattern $p){
        $p.countIn(_SOURCE);
    }



}
