package org.jdraft;

import junit.framework.TestCase;

import java.io.*;
import java.net.URISyntaxException;

public class _featureTest extends TestCase {

    public void testFeatureInstanceMap(){
        assertEquals( _anno.FEATURES.size(), _anno.FEATURES.featureMap(_anno.of("@A(k=1,v=2)")).size());
        assertEquals( _annos.FEATURES.size(), _annos.FEATURES.featureMap( _annos.of("@A @B @C @D")).size());
        assertEquals( _annotation.FEATURES.size(), _annotation.FEATURES.featureMap( _annotation.of("A").addAnnoMember("int val()").addInnerType(_class.of("B")) ).size());
        assertEquals( _args.FEATURES.size(), _args.FEATURES.featureMap(_args.of("1,'c'")).size());
        assertEquals( _newArrayExpr.FEATURES.size(), _newArrayExpr.FEATURES.featureMap(_newArrayExpr.of("int [1][2][3]")).size());
        assertEquals( _arrayInitExpr.FEATURES.size(), _arrayInitExpr.FEATURES.featureMap(_arrayInitExpr.of("{1, 2}")).size());
        assertEquals( _blockStmt.FEATURES.size(), _blockStmt.FEATURES.featureMap(_blockStmt.of("{int i; int j; int k;}")).size());
        assertEquals( _body.FEATURES.size(), _body.FEATURES.featureMap(_body.of("{int i; int j;}")).size());

        assertEquals( _class.FEATURES.size(), _class.FEATURES.featureMap(_class.of("class A{ int a; void m(){} }")).size());
        assertEquals( _class.FEATURES.size(), _class.FEATURES.featureMap(_class.of("class A implements B, C{}")).size());
        assertEquals( _constant.FEATURES.size(), _constant.FEATURES.featureMap(_constant.of("A{ int a; void m(){} }")).size());

        assertEquals( _enum.FEATURES.size(), _enum.FEATURES.featureMap(_enum.of("enum E implements B, C{}")).size());
        assertEquals( _enum.FEATURES.size(), _enum.FEATURES.featureMap(_enum.of("enum E{ A,B,C,D; }")).size());

        assertEquals( _forStmt.FEATURES.size(), _forStmt.FEATURES.featureMap(_forStmt.of("for(;;a++,b++)")).size());
        assertEquals(_imports.FEATURES.size(), _imports.FEATURES.featureMap(_imports.of("aaaa.A", "bbbb.B")).size());

        assertEquals(_interface.FEATURES.size(), _interface.FEATURES.featureMap( _interface.of("A").addExtend("B").addExtend("C")).size());
        assertEquals(_interface.FEATURES.size(), _interface.FEATURES.featureMap( _interface.of("A").addFields("int i, j;")).size());
        assertEquals(_modifiers.FEATURES.size(), _modifiers.FEATURES.featureMap(_modifiers.of("public static final")).size());
        assertEquals(_moduleExports.FEATURES.size(), _moduleExports.FEATURES.featureMap(_moduleExports.of("exports R.S to T1.U1, T2.U2;")).size());
        assertEquals(_moduleInfo.FEATURES.size(), _moduleInfo.FEATURES.featureMap(_moduleInfo.of("module M{ exports R.S to T1.U1, T2.U2; opens F; }")).size());
        assertEquals( _moduleOpens.FEATURES.size(), _moduleOpens.FEATURES.featureMap(_moduleOpens.of("opens R.S to T1.U1, T2.U2;")).size());

        assertEquals(_moduleProvides.FEATURES.size(), _moduleProvides.FEATURES.featureMap(_moduleProvides.of("provides R.S with T1.U1, T2.U2;")).size());

        assertEquals(_newExpr.FEATURES.size(), _newExpr.FEATURES.featureMap(_newExpr.of("new A(){ int i, j; }")).size());

        assertEquals( _params.FEATURES.size(), _params.FEATURES.featureMap(_params.of("(@A int i, final String...vars)")).size());

        assertEquals( _project.FEATURES.size(), _project.FEATURES.featureMap( _project.of(_class.of("A"), _interface.of("I"))).size());

        assertEquals( _switchCase.FEATURES.size(), _switchCase.FEATURES.featureMap(_switchCase.of("case 1, 2: break;")).size());
        assertEquals( _switchCase.FEATURES.size(), _switchCase.FEATURES.featureMap(_switchCase.of("case 1: i=0; break;")).size());

        assertEquals( _switchExpr.FEATURES.size(), _switchExpr.FEATURES.featureMap(_switchExpr.of("switch(a){ case 1: yield 1; case 2: yield 2;}")).size());
        assertEquals( _switchExpr.FEATURES.size(), _switchStmt.FEATURES.featureMap(_switchStmt.of("switch(a){ case 1: return 1; case 2: return 2;}")).size());

        assertEquals( _throws.FEATURES.size(), _throws.FEATURES.featureMap(_throws.of(FileNotFoundException.class, URISyntaxException.class)).size());
        assertEquals( _tryStmt.FEATURES.size(), _tryStmt.FEATURES.featureMap( _tryStmt.of("try{ a(); }catch(A a){} catch(B b){}")).size());
        assertEquals( _tryStmt.FEATURES.size(), _tryStmt.FEATURES.featureMap( _tryStmt.of("try( AC f = new AC(); AC t = new AC()) { }")).size());

        assertEquals(_typeArgs.FEATURES.size(), _typeArgs.FEATURES.featureMap(_typeArgs.of("A,B")).size());
        assertEquals(_typeParams.FEATURES.size(), _typeParams.FEATURES.featureMap(_typeParams.of("A extends B, C")).size());

        assertEquals( _variablesExpr.FEATURES.size(), _variablesExpr.FEATURES.featureMap(_variablesExpr.of("int i,j")).size());
    }

    //here we go through all the "many" features and make sure the getters work
    public void testManyGet(){
        assertEquals( 2, _anno.ENTRY_PAIRS.get(_anno.of("@A(k=1,v=2)")).size());
        assertEquals( 4, _annos.ANNOS.get( _annos.of("@A @B @C @D")).size());
        assertEquals( 2, _annotation.MEMBERS.get( _annotation.of("A").addAnnoMember("int val()").addInnerType(_class.of("B")) ).size());
        assertEquals( 2, _args.ARGS.get(_args.of("1,'c'")).size());
        assertEquals( 3, _newArrayExpr.DIMENSIONS.get(_newArrayExpr.of("int [1][2][3]")).size());
        assertEquals( 2, _arrayInitExpr.INITS.get(_arrayInitExpr.of("{1, 2}")).size());
        assertEquals( 3, _blockStmt.STATEMENTS.get(_blockStmt.of("{int i; int j; int k;}")).size());
        assertEquals( 2, _body.STATEMENTS.get(_body.of("{int i; int j;}")).size());

        assertEquals( 2, _class.MEMBERS.get(_class.of("class A{ int a; void m(){} }")).size());
        assertEquals( 2, _class.IMPLEMENTS.get(_class.of("class A implements B, C{}")).size());
        assertEquals( 2, _constant.MEMBERS.get(_constant.of("A{ int a; void m(){} }")).size());

        assertEquals( 2, _enum.IMPLEMENTS.get(_enum.of("enum E implements B, C{}")).size());
        assertEquals( 4, _enum.MEMBERS.get(_enum.of("enum E{ A,B,C,D; }")).size());

        assertEquals( 2, _forStmt.UPDATES.get(_forStmt.of("for(;;a++,b++)")).size());
        assertEquals(2, _imports.IMPORTS.get(_imports.of("aaaa.A", "bbbb.B")).size());

        assertEquals(2, _interface.EXTENDS.get( _interface.of("A").addExtend("B").addExtend("C")).size());
        assertEquals(2, _interface.MEMBERS.get( _interface.of("A").addFields("int i, j;")).size());
        assertEquals(3, _modifiers.MODIFIERS.get(_modifiers.of("public static final")).size());
        assertEquals(2, _moduleExports.MODULE_NAMES.get(_moduleExports.of("exports R.S to T1.U1, T2.U2;")).size());
        assertEquals(2, _moduleInfo.MODULE_DIRECTIVES.get(_moduleInfo.of("module M{ exports R.S to T1.U1, T2.U2; opens F; }")).size());
        assertEquals( 2, _moduleOpens.MODULE_NAMES.get(_moduleOpens.of("opens R.S to T1.U1, T2.U2;")).size());

        assertEquals(2, _moduleProvides.MODULE_NAMES.get(_moduleProvides.of("provides R.S with T1.U1, T2.U2;")).size());

        assertEquals(2, _newExpr.ANONYMOUS_BODY_MEMBERS.get(_newExpr.of("new A(){ int i, j; }")).size());

        assertEquals( 2, _params.PARAMS.get(_params.of("(@A int i, final String...vars)")).size());

        assertEquals( 2, _project.CODE_UNITS.get( _project.of(_class.of("A"), _interface.of("I"))).size());

        assertEquals( 2, _switchCase.CASE_EXPRESSIONS.get(_switchCase.of("case 1, 2: break;")).size());
        assertEquals( 2, _switchCase.STATEMENTS.get(_switchCase.of("case 1: i=0; break;")).size());

        assertEquals( 2, _switchExpr.SWITCH_ENTRIES.get(_switchExpr.of("switch(a){ case 1: yield 1; case 2: yield 2;}")).size());
        assertEquals( 2, _switchStmt.SWITCH_ENTRIES.get(_switchStmt.of("switch(a){ case 1: return 1; case 2: return 2;}")).size());

        assertEquals( 2, _throws.THROWS.get(_throws.of(FileNotFoundException.class, URISyntaxException.class)).size());
        assertEquals( 2, _tryStmt.CATCH_CLAUSES.get( _tryStmt.of("try{ a(); }catch(A a){} catch(B b){}")).size());
        assertEquals( 2, _tryStmt.WITH_RESOURCES.get( _tryStmt.of("try( AC f = new AC(); AC t = new AC()) { }")).size());

        assertEquals(2, _typeArgs.TYPE_ARGS.get(_typeArgs.of("A,B")).size());
        assertEquals(2, _typeParams.TYPE_PARAMS.get(_typeParams.of("A extends B, C")).size());

        assertEquals( 2, _variablesExpr.VARIABLES.get(_variablesExpr.of("int i,j")).size());

    }

    /*
    public static void main(String[] args){
        try(FileInputStream f = new FileInputStream("A"); FileInputStream f2 = new FileInputStream("A");){

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}
