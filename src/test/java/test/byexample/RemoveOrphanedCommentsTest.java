package test.byexample;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.proto.$;
import org.jdraft.proto.$expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveOrphanedCommentsTest extends TestCase {

    @interface MyAnnotation {

    }

    public void testRemoveOrphanComments() {
        class MyClass {

            // field comment line1
            // field comment line2
            // field comment line3
            @MyAnnotation()
            public String field1;

            // method comment line1
            // method comment line2
            // method comment line3
            @MyAnnotation()
            public void method1() {

            }

            // some other orphan comments here
        }

        CompilationUnit cu = Ast.of(MyClass.class);
        MethodDeclaration md = cu.getType(0).getMethods().get(0);
        FieldDeclaration fd = cu.getType(0).getFields().get(0);

        removeByAnnotation( cu, MyAnnotation.class);
        //removeCompletely(md);
        //removeCompletely(fd );


        System.out.println( cu );
    }

    public static void removeByAnnotation(Node top, Class annotationClass ){
        List<Node> toRemove = new ArrayList<>();
        top.walk( c-> {
            if( c instanceof NodeWithAnnotations  && ((NodeWithAnnotations)c).getAnnotationByClass(annotationClass).isPresent() ){
                toRemove.add( c );
            }
        });
        toRemove.forEach( n -> removeCompletely(n));
    }

    public static void removeCompletely( Node n ){
        if( n instanceof NodeWithJavadoc){
            removeComments( (NodeWithJavadoc)n);
        } else{
            try {
                n.removeComment();
            }catch(Exception e){
                //hmm this shouldnt happen
            }
        }
        boolean isRemoved = n.remove();
        if( !isRemoved ){
            //forcefully removing
            n.removeForced();
        }
    }

    public static void removeComments( NodeWithJavadoc nwj ){
        List<Comment> pocs = listPrecedingOrphanComments(nwj);
        Node n = (Node)nwj;
        n.removeComment();
        nwj.removeJavaDocComment();
        pocs.forEach(c -> c.remove());
    }

    public static List<Comment> listPrecedingOrphanComments(NodeWithJavadoc nwj) {
        Node n = (Node) nwj;


        if (!n.getParentNode().isPresent()) { //no parent... break
            return Collections.EMPTY_LIST;
        }
        Node parent = n.getParentNode().get();

        //these are the positions between which where orphaned comments are to be removed
        Position startPosition = new Position(0, 0); //this will be the previous sibling node
        Position endPosition = ((Node) nwj).getRange().get().begin; //before the target Node

        List<Node> siblings = parent.getChildNodes().stream().collect(Collectors.toList());

        //lets sort the siblings by position
        siblings.sort(Ast.COMPARE_NODE_BY_LOCATION);
        boolean done = false;

        for (int i = 0; i < siblings.size(); i++) {
            Node sib = siblings.get(i);
            if ( sib == nwj) { //as soon as I find the node, we dont need to check nodes after
                done = true;
            } else if( ! done &&  !(sib instanceof Comment)){ //set the start position be the end of the
                startPosition = siblings.get(i).getRange().get().end;
            }
        }
        //need to make this final for lambda
        final Position orgStart = startPosition;


        List<Comment> orphansToRemove =
                parent.getOrphanComments().stream().filter(oc -> oc.getBegin().get().isAfter(orgStart) && oc.getBegin().get().isBefore(endPosition)).collect(Collectors.toList());
        return orphansToRemove;
    }
}
