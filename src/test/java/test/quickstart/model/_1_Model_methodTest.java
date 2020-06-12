package test.quickstart.model;

import com.github.javaparser.ast.body.MethodDeclaration;
import junit.framework.TestCase;
import org.jdraft.*;

/**
 * Building and using jdraft models (_method (_m) from String)
 * Simple Accessor and mutator methods on the _method (_m)
 * Accessing and modifying the underlying JavaParser MethodDeclaration (astMd)
 * ...Other jdraft model types (_type, _class, _constructor, _field, _typeRef, _initBlock...)
 */
public class _1_Model_methodTest extends TestCase {

    public void testM(){
        // here we might want to do some metaprogramming to modify some source code
        // that represents a Java method. If the source code is a String representation, (like in
        // the variable "methodAsString") even if we KNOW the source code represents a valid Java method,
        // to write a (meta)program that can ask about the method or manipulate it, is difficult, because
        // we'd have to either manually parse it or engage in some kind of crazy regular expressions to try
        // and identify the parts of the method
        String methodAsString = "public int setX(int x){ this.x = x; }";

        // with jdraft, we just pass in this String to a new jdraft _method... it will create for us
        // a model that will simplify writing some meta-program that analyzes or modifies the source code
        _method _m = _method.of( "public int setX(int x){ this.x = x; }" );

        // we can get simple properties and ask simple questions about the _m instance
        assertEquals("setX", _m.getName() );
        assertTrue( _m.isPublic() );
        assertTrue( _m.isImplemented() );
        assertTrue( _m.hasParams() );
        assertTrue( _m.isType( int.class ) );

        // other simple questions you can ask
            assertFalse( _m.hasJavadoc() );
            assertFalse( _m.hasThrows() );
            assertFalse( _m.hasAnnos() );

        // the _method can also return other _models that are also part of the _method
        _modifiers _ms = _m.getModifiers();
        _params _ps = _m.getParams();
        _typeRef _t = _m.getType();

        // other _method _models
            _typeParams _tps = _m.getTypeParams();
            _throws _ts = _m.getThrows();
            _annos _as = _m.getAnnos();
            _javadocComment _jd = _m.getJavadoc();
            _receiverParam _rp = _m.getReceiverParam();

        // what IS the _method?
        // the _method is actually a stateless wrapper pointing to a JavaParser AST
        // we can get the JavaParser AST MethodDeclaration by calling the ast() method
        MethodDeclaration md = _m.ast();

        // ...if we call any "mutator" type methods on the _m instance, the changes will also be reflected in
        // the AST and by:
        assertFalse( md.isStatic() ); // the _m is NOT static
        assertFalse( _m.isStatic() ); // the md is NOT static

        _m.setStatic( true );         // set the _method to be static

        assertTrue( md.isStatic() );  // will update the state of the MethodDeclaration Ast
        assertTrue( _m.isStatic() );  // the _m will also be set to static (indirectly)
                                      // when we call isStatic() on the _m instance, what we are REALLY doing
                                      // is checking the state of the underlying MethodDeclaration
                                      // so the _method and MethodDeclaration are NEVER "out of synch"
                                      // since the instance of _m can only ask or update it's MethodDeclaration
                                      // (it has no state of its own)

        // also, we note that if we manually update the MethodDeclaration(md), it's changes will be reflected in the
        // _method(_m) instance:
        assertFalse( md.isFinal() );
        assertFalse( _m.isFinal() );

        md.setFinal( true ); //update the underlying MethodDeclaration(md) AST

        assertTrue( md.isFinal() );  // verify the MethodDeclaration was changed
        assertTrue( _m.isFinal() );  // the _method(_m) also reflects the change

        // overall it should be pointed out that the AST entities (like MethodDeclaration) are "more important";
        // they are the source of record... and the _model entities (like _method) are just wrappers that
        // can be created and destroyed on demand, basically they exist to adapt the JavaParser API to make it easier
        // for programs with less internal information about the AST/low level Syntax to easily write programs
        // that modify the AST

        // when we have completed making changes to the _method(_m)/MethodDeclaration(md) we can always get the
        // source code back by calling toString():
        System.out.println( _m.toString() );

        Class<? extends _java._domain>[] _MODEL_TYPES = new Class[]{
            _type.class, _class.class, _interface.class, _enum.class, _annotation.class,
            _field.class, _constructor.class, _initBlock.class, _constant.class, _annoMember.class,
            _param.class, _params.class, _typeRef.class, _modifiers.class, _anno.class,
            _annos.class, _body.class, _import.class, _throws.class, _typeParam.class,
            _typeParams.class};
    }
}
