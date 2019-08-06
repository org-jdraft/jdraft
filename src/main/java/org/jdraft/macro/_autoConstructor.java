package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithConstructors;
import org.jdraft.*;

import java.lang.annotation.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Builds a constructor based on the final, non_static FIELDS without initializers
 * Works on {@link _class} and {@link org.jdraft._enum} {@link _type}s
 *
 * NOTE: this DOES NOT look into the super class to see if there are final FIELDS that
 * are not initialized
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface _autoConstructor {

    Macro $ = new Macro();

    /** picks _fields of the _class or enum that are required in the constructor */
    Predicate<_field> CTOR_REQUIRED_FIELD = _f -> !_f.isStatic() && _f.isFinal() && !_f.hasInit();

    class Macro implements _macro<_type> {

        public _type apply( _type _t ){
            return to( _t );
        }
        
        @Override
        public String toString(){
           return "macro[autoConstructor]"; 
        }

        public static <T extends _type> T to( T t  ){
            List<_field> _fs = t.listFields(CTOR_REQUIRED_FIELD);
            _constructor _ct = _constructor.of(t.getName() + "(){}");
            if (t instanceof _class) {
                _ct.setPublic(); //assume class CONSTRUCTORS is public
            }
            _fs.forEach(f -> {
                _ct.addParameter(f.getType(), f.getName());
                _ct.add("this." + f.getName() + " = " + f.getName() + ";");
            });
            ((_constructor._hasConstructors) t).constructor(_ct); //add the annotation to the TYPE
            return t;
        }
    }
    class Act implements Consumer<Node> {

        @Override
        public void accept(Node node) {
            if( node instanceof NodeWithConstructors && node instanceof TypeDeclaration){
                NodeWithConstructors nwcs = (NodeWithConstructors)node;
                TypeDeclaration td = (TypeDeclaration)node;
                List<_field> _fs = _field.of( nwcs.getFields() );
                _fs = _fs.stream().filter( CTOR_REQUIRED_FIELD ).collect(Collectors.toList());
                //_constructor _ct = _constructor.of( td.getName() + "(){}");
                ConstructorDeclaration cd = null;
                if (node instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)node).isInterface()) {
                    cd = nwcs.addConstructor(Modifier.Keyword.PUBLIC);
                    //_ct.setPublic(); //assume class CONSTRUCTORS are public
                } else{
                    cd = nwcs.addConstructor();
                }
                _constructor _ct = _constructor.of( cd );
                _fs.forEach(f -> {
                    _ct.addParameter(f.getType(), f.getName());
                    _ct.add("this." + f.getName() + " = " + f.getName() + ";");
                });
                _macro.removeAnnotation(node, _autoConstructor.class);
            }
            else{
                throw new _jDraftException("cannot add a constructor ");
            }
        }
    }
}
