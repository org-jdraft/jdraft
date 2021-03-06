package org.jdraft.bot;

import org.jdraft.*;
import org.jdraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public @interface _$ {
    String[] value();

    /**
     * given any member component with an @$_ annotation, will return the source
     * with the annotations parameterized
     * <p>
     * i.e.
     * _field _f = _field.of( new Object(){
     *
     * @_$("x", "name", "int", "type")
     * public int x = 100;
     * }
     * String parameterized = _$.Parameterize.toString( _f);
     * //parameterized String:
     * public $type$ $name$ = 100;
     */
    class Parameterize {


        public static <C extends _java._member & _annos._withAnnos> C update(C _c) {
            List<_anno> _ps = _c.listAnnosByName("_$");
            if (_ps != null) {
                C _cl = (C) _c.copy(); //make a copy as to not modify the original
                _cl.removeAnnos((a) -> ((_anno) a).getName().equals("_$")); //remove the @_$ annotations from the clone

                //create me the full source as a String (after removing the annos)
                String sourceString = _cl.toString();

                for (int i = 0; i < _ps.size(); i++) {
                    _anno _a = _ps.get(i);
                    _arrayInitExpr _ai = _arrayInitExpr.of(_a.getEntryValueExpression("value").asArrayInitializerExpr());
                    //keyValues
                    List<String> ls = new ArrayList<>();
                    _ai.toEach(a -> ls.add(a.node().asStringLiteralExpr().getValue()));

                    if (ls.size() % 2 != 0) {
                        throw new _jdraftException("Expected an EVEN number of keyValue pairs for templating");
                    }
                    //i need to change every other
                    for (int j = 1; j < ls.size(); j += 2) {
                        ls.set(j, "$" + ls.get(j) + "$");
                    }

                    //now I have keyValue Strings in the list ls, parameterize the sourceString
                    sourceString = Text.replace(sourceString, ls.toArray(new String[0]));

                }

                if( _c instanceof _field){
                    _field _v = (_field)_c;
                    return (C) _field.of(sourceString);
                } else {
                    return (C) _java.of(_java.node(_c.node().getClass(), sourceString));
                }
            }
            return _c;
        }

        public static <C extends _java._member & _annos._withAnnos> String toString(C _c) {
            List<_anno> _ps = _c.listAnnosByName("_$");
            if (_ps != null) {
                C _cl = (C) _c.copy(); //make a copy as to not modify the original
                _cl.removeAnnos((a) -> ((_anno) a).getName().equals("_$")); //remove the @_$ annotations from the clone

                //create me the full source as a String (after removing the annos)
                String sourceString = _cl.toString();

                for (int i = 0; i < _ps.size(); i++) {
                    _anno _a = _ps.get(i);
                    _arrayInitExpr _ai = _arrayInitExpr.of(_a.getEntryValueExpression("value").asArrayInitializerExpr());
                    //keyValues
                    List<String> ls = new ArrayList<>();
                    _ai.toEach(a -> ls.add(a.node().asStringLiteralExpr().getValue()));

                    if (ls.size() % 2 != 0) {
                        throw new _jdraftException("Expected an EVEN number of keyValue pairs for templating");
                    }
                    //i need to change every other
                    for (int j = 1; j < ls.size(); j += 2) {
                        ls.set(j, "$" + ls.get(j) + "$");
                    }

                    //now I have keyValue Strings in the list ls, parameterize the sourceString
                    sourceString = Text.replace(sourceString, ls.toArray(new String[0]));
                }
                //System.out.println( sourceString );

                return sourceString;
            }
            return _c.toString();
        }
    }//Parameterize
}
