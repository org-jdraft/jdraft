package test.othertools;

import junit.framework.TestCase;
import org.jdraft._constructor;
import org.jdraft._javadoc;
import org.jdraft._method;
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
public class IntelliJStructuralSearchTest extends TestCase {

    static _sources _SOURCE = _sources.of( _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java\\org\\jdraft\\diff") );

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

        //Annotation Declarations
        _( $annotation.of() );

        //Annotation
        _( $anno.of() );

        //Comments
        _( $comment.of() );

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

//Expressions

        all expressions of some type
        $ex.of($ex.LAMBDA)

        Array access
        $.arrayAccess()

        Assignments
        $.assign();

        Casts
        $.cast()

        Field Selections / Field Access
        $.fieldAccess()
        $.fieldAccess().name("url$Name$")

                instanceof
                $.instanceOf()

        lambdas
        $.lambda()

        method Calls
        $.methodCall()

        method calls to deprecated methods
//you got me here
//how about FIRST we find all deprecated methods
        $method $deprecatedMethods = $.method().hasAnno(Deprecated.class);

        find all static direct access, find all
//$.methodCall(mc->getName

        method references
        $.methodReference()

        new expressions
        $new.of()

//simple method invocation with constant
        $methodCall.of( $args.of(as-> as.size() == 1
                        && as.isArgument(0, a->a.isLiteral() )

                String concatenation with many operands
//TODO
                $binaryExpression $stringConcat =
                        $binaryExpression.ofPlus()
                                .$or( b-> b.getLeft()
                                        $.statement(s-> $binary)

                                        String literals
                                        $.string()

                                        DiamondOperators
                                        $.hasDiamondOperator()
                                        $.typeParameters(t.isDiamondOperator());

        Generic Cast
        $.cast( c-> c.getType().isGeneric())

        Generic Classes
        $class.of(c-> c.hasTypeParameters())
        $class.of(c->c.isGeneric())
        $class.of().$isGeneric()

        Generic Constructors
        $constructor.of(c-> c.hasTypeParameters());
        $constructor.of(c-> c.isGeneric());
        $constructor.of().$isGeneric()
//is not generic
        $constructor.of().$isGeneric(false);


        Generic Methods
        $method.of(m-> m.hasTypeParameters());
        $method.of(m-> m.isGeneric());
        $method.of().$isGeneric()
//not generic
        $method.of().$isGeneric(false);

//??
        $.genericType()
        $.genericClass()
        $.genericInterface()
        $.genericEnum()

        $.genericTypeRef()
        $.genericMethod()
        $.genericConsttructor()
        $.genericField();

??isDiamondOperator()

        Method returns bounded wildcard
//TODO
        $type().isWildcard() //<?
        $typeParameter.of().isWildcard()


        $method.of().$return($typeRef.of($typeParameter.of().isWildCard());
        Type<? extends $bound$>

        type var substitutions in instanceof with generic types
                instanceof with generic type
        $instanceOf.of().$typeRef(t-> t.isGeneric())

        typed symbol
        $typeRef.of(t-> t.ieGeneric)

        variables with generic types
        $variable.of( $typeRef.of(t->t.isGeneric()))


        boxed expressions?

        boxing in declarations
        $typeRef $boxed =
                $typeRef.or(Object.class, Integer.class, Boolean.class, Double.class, Long.class, Character.class, Short.class, Byte.class);

        $typeRef $primitive = $typeRef.or(int.class, boolean.class, long.class, char.class, short.class, byte.class);

        $expression $LITERALS = $literal.of().$not(_string.class, _null.class);

        $variable.of().$typeRef($primitive).$init($LITERALS)

        boxing in method calls
????

        fields variables with a given name
//i.e. a name that starts with url
        $var.of().$name("url$Any$")

        break to label
        $.breakStmt(b-> b.hasLabel())

        methods and constructors with final
        $.member( _method, _constructor).$modifiers($.FINAL);

        switch statements with few branches
        $.switchStmt(s-> s.listEntries().size() <= 4)

        try statements with resources and catch blocks
        $.tryStmt(t-> t.hasResources() && t.hasCatchBlocks() )

        unboxed expressions
        $varEx.of( $.PRIMITIVE_TYPE ).$init( $.WRAPPER_TYPE )

        unboxed method calls
                ???

        usage of derived type in cast
???

        Ejb interface
        $interface.of(i-> i.hasMethods()).$extends(EJBObject.class)

        EJB class
        $class.of().$implement(EntityBean.class)

        Servlet
        $class.of().$extends(HttpServlet.class)

        SessionEjb
        $class.of().$extends(SessionBean.class)

        Struts Action
        $class.of().$extends(Action.class)

        BeanInfo Class
        $class.of().$implements(BeanInfo.class)

        Cloneable class
//find all cloneable classes
        $class $class.of().$implements(Cloneable.class )
        $class.of().$extends( $type.of(

                TestCases classes
                $class.of().$extends(TestCase.class)

                find all implementers
                Serializable Classes
//recursive find all
//extends implement

                $class.of().$implements(Serializable.class)

//anyMatch (in nodeList)
//allMatch (in nodeList)

//singletons
                $class.of(c-> c.hasConstructors()
                        && c->c.hasConstructor(ct-> ct.isFinal()))

                assertStatement without descriptions
                $.assertStmt(a-> !a.hasMessage())

                forEachLoops
                $.forEach()

                ifs
                $if.of()

                logging without if
        $methodCall.of("LOG.debug($args$)") ??

        switch
        $switchStmt.of()

        try
        $tryStmt.of();

 */
    }

    public void _($pattern $p){
        $p.countIn(_SOURCE);
    }



}
