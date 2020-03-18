package test.byexample;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.Walk;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$ex;
import org.jdraft.pattern.$stmt;

import java.util.List;

public class GitHubIssueTest extends TestCase {

    public void testF(){
        class SampleClass {
            public String concat(String s1, String s2) {
                return s1 + "_" + s2;
            }

            /*sample comment outside function*/

            public int product(int n1, int n2) {
                /*
                sample comment inside function
                */
                return n1 * n2;
            }


            public void main(String[] args) {
                System.out.println(product(3, 2));
                System.out.println(concat("My", "Name"));
            }
        }

        CompilationUnit cu = Ast.of(SampleClass.class);
        List<TypeDeclaration<?>> ts = cu.getTypes();
        for(int i=0;i<ts.size();i++){
            System.out.println(">>> "+i +  ts.get(i) );
        }
        cu.getAllContainedComments().forEach(c -> System.out.println("CONTAINED"+c ));

        //cu.getOrphanComments().forEach(c -> System.out.println("ORPHANED"+c ));
    }

    public void testMethodsWithName(){
        MethodDeclaration md = Ast.method(
                "        public static ContactComponent convertContactComponent(ContactComponent src) throws FHIRException {\n" +
                        "            if (src == null || src.isEmpty())\n" +
                        "                return null;\n" +
                        "            ContactComponent tgt = new ContactComponent();\n" +
                        "            VersionConvertor_10_40.copyElement(src, tgt);\n" +
                        "            for (CodeableConcept t : src.getRelationship()) tgt.addRelationship(VersionConvertor_10_40.convertCodeableConcept(t));\n" +
                        "            tgt.setName(VersionConvertor_10_40.convertHumanName(src.getName()));\n" +
                        "            for (ContactPoint t : src.getTelecom()) tgt.addTelecom(VersionConvertor_10_40.convertContactPoint(t));\n" +
                        "            tgt.setAddress(VersionConvertor_10_40.convertAddress(src.getAddress()));\n" +
                        "            tgt.setGender(VersionConvertor_10_40.convertAdministrativeGender(src.getGender()));\n" +
                        "            tgt.setOrganization(VersionConvertor_10_40.convertReference(src.getOrganization()));\n" +
                        "            tgt.setPeriod(VersionConvertor_10_40.convertPeriod(src.getPeriod()));\n" +
                        "            return tgt;\n" +
                        "        }");

        $stmt $target = $.expressionStmt("tgt.set$Name$($container$(src.get$Name$()));");
        $stmt $replacement = $.ifStmt("if(src.has$Name$()){ tgt.set$Name$($container$(src.get$Name$())); }");

        $target.replaceIn(md, $replacement);

        System.out.println( md );
    }
}
