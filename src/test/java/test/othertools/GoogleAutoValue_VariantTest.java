package test.othertools;

import com.github.javaparser.ast.body.TypeDeclaration;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._field;
import org.jdraft._type;
import org.jdraft.macro._final;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$field;
import org.jdraft.pattern.$method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

public class GoogleAutoValue_VariantTest extends TestCase {

    /**
     * There are a few other variants o AutoValue to cover:
     * https://github.com/google/auto/blob/master/value/userguide/builders-howto.md#-use-or-not-use-set-prefixes
     */
    public void testNoGetSetPrefixes(){

        @_dto_NoSetPrefix class C{
            int a;
            @_final int b;
        }
        _class _c = _class.of(C.class);
        System.out.println( _c);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.TYPE_USE})
    @interface _dto_NoSetPrefix{

        public static Consumer<TypeDeclaration> C = new Act();

        /*
        public static _macro<_type> $THIN = new _macro<_type>(){

            @Override
            public _type apply(_type type) {
                System.out.println( "Doing it");
                Act.act(type);
                return type;
            }
        };
         */

        class Act implements Consumer<TypeDeclaration>{


            public static final $method $setNoPrefix = $method.of("public void $name$($type$ $name$){ this.$name$ = $name$; }");
            public static final $field $setterFields = $field.of($.NOT_STATIC, $.NOT_FINAL);

            public Act( ){
                //System.out.println( "In ctor");
            }

            public Act(_dto_NoSetPrefix d){
                //
                //System.out.println( "In ctor");
            }

            @Override
            public void accept(TypeDeclaration typeDeclaration) {
                $setterFields.forEachIn(typeDeclaration, f-> typeDeclaration.addMember( $setNoPrefix.fill(f.getName(), f.getTypeRef()).ast() ) );
                //act( _java.type(typeDeclaration));

            }

            public static void act( _type _t ){
                //if( _t instanceof ){
                //_t.forFields( (_field f)-> f.isStatic(), (_field f)-> System.out.println(f));
                //_t.forFields( (f)-> !f.isStatic() && !f.isFinal(), f-> _t.add( $setNoPrefix.fill(f.getName(), f.getTypeRef())));
                $.field($.NOT_STATIC, $.NOT_FINAL).forEachIn(_t, f-> _t.add( $setNoPrefix.fill(f.getName(), f.getTypeRef())));

                //_t.listFields( (f) -> !((_field)f).isFinal() && !((_field)f).isStatic());
                //typeDeclaration.getFields().stream().filter(f-> !f.)
                //_toString.Act.to(typeDeclaration);
                //_equals.Act.to(typeDeclaration);
                //_hashCode.Act.to(typeDeclaration);
            }
        }
    }
}
