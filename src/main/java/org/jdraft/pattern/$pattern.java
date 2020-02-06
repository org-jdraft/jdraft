package org.jdraft.pattern;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * $pattern is similar to the new Pattern matching Switch features is Java 13/14:
 * "Patterns fuses a test, conditional extraction, and binding to variables"
 * <A HREF="https://www.youtube.com/watch?time_continue=9&v=GAa54jXKbn4v">Brian Goetz -- Java a Look into the Future</A>
 *
 * In addition, the $pattern implementations "know" how to walk (an AST) to seek and find matches
 * within one an AST to find matches, and $pattern instances provide mechanisms to perform some action
 * (collect, modify code) within the AST they are found.
 *
 * Model of a <A HREF="https://en.wikipedia.org/wiki/Query_by_Example">query-by-example</A>,
 * (a buildable/mutable query object that has the structure of the AST entity being queried
 * and contains a hierarchical structure)
 *
 * $pattern objects define a mechanism to walk the AST and query/modify Java code
 * matching against grammar entries via the _node model 
 *
 * @param <P> the type of node being queried for {@link _java._node}
 * @param <$P> the pattern type
 * {@link com.github.javaparser.ast.Node} or 
 * {@link _java._node})
 */
public interface $pattern<P, $P extends $pattern>{


    /**
     * Will this prototype match ANY instance of the type (or null)?
     * <PRE>
     *     //i.e. matches null
     *     assertTrue($method.of().matches(null));
     *     //matches any method with any properties
     *     assertTrue($method.of().matches("public void m(){}"));
     *
     *     //WILL NOT match String that is NOT a method
     *     assertTrue($method.of().matches("int i=0;"));
     * </PRE>
     * @return true if the instance will match ANY instance of the type (or null)
     */
    boolean isMatchAny();

    /**
     * Parameterizes (2) targets and parameters
     *
     * @param target1
     * @param $paramName1
     * @param target2
     * @param $paramName2
     * @return
     */
    default $P $(String target1, String $paramName1, String target2, String $paramName2){
        return ($P)$(target1,$paramName1).$(target2, $paramName2);
    }

    /**
     * Parameterizes (3) targets and parameters
     *
     * @param target1
     * @param $paramName1
     * @param target2
     * @param $paramName2
     * @return
     */
    default $P $(String target1, String $paramName1, String target2, String $paramName2, String target3, String $paramName3){
        return ($P)$(target1,$paramName1).$(target2, $paramName2).$(target3, $paramName3);
    }

    /**
     * Parameterize the target String with the $paramName
     * @param target
     * @param $paramName
     * @return
     */
    $P $(String target, String $paramName);

    /**
     * Add an (AND) matching constraint for matching the $proto against an instance of P
     * @param constraint a constraint on the instance of P
     * @return the modified $P ($proto)
     */
    $P $and(Predicate<P> constraint );

    /**
     * Add a (NOT) matching constraint to add to the proto
     * @param constraint a constraint to be negated and added (via and) to the constraint
     * @return the modified prototype
     */
    default $P $not(Predicate<P> constraint){
        return $and( constraint.negate() );
    }

    /**
     * Finds the parent "member" (a {@link BodyDeclaration}) and checks that it matches one of these
     * $member {@link $pattern}s
     * @param members the member $patterns to match against the parent member
     * @return the modified $P (after adding the constraint)
     */
    default $P $isParentMember( $member... members){
        Predicate<P> pp = n -> {
            if (n instanceof Node) {
                Node node = (Node)n;
                //System.out.println( "NODE "+ node );
                return Ast.isParentMember(node, nn->Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent() );
                //return Arrays.stream(members).filter( $m -> $m.match(node)).findFirst().isPresent();
            } else if (n instanceof _java._node) {
                //for Fields (_field) I need to skip one (since I go from the varDeclarator to FieldDeclaration)
                Node node = null;
                if( n instanceof _field ){
                    node = ((_field)n).getFieldDeclaration();
                } else {
                    node = ((_java._node) n).ast();
                }
                //System.out.println( "_NODE "+ node );
                return Ast.isParentMember(node, nn->Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent() );
                //return Arrays.stream(members).filter( $m -> $m.match(node)).findFirst().isPresent();
            } else if( n instanceof _body ){
                return Ast.isParentMember( ((_body)n).ast(), nn->Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent() );
            }
            return false;
        };
        return $and( pp );
    }

    /**
     * Finds the parent "member" (a {@link BodyDeclaration}) and checks that it matches one of these
     * $member {@link $pattern}s (IF so returns false/ does not match)
     * @param members the member $patterns to match against the parent member
     * @return the modified $P (after adding the constraint)
     */
    default $P $isNotParentMember( $member... members){
        Predicate<P> pp = n -> {
            if (n instanceof Node) {
                Node node = (Node)n;
                return Ast.isParentMember(node, nn->Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent() );
            } else if (n instanceof _java._node) {
                Node node = ((_java._node)n).ast();
                return Ast.isParentMember(node, nn->Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent() );
            } else if (n instanceof _body) {
                Node node = ((_body)n).ast();
                return Ast.isParentMember(node, nn->Arrays.stream(members).filter($m ->$m.match(nn)).findFirst().isPresent() );
            }
            return false;
        };
        return $not( pp );
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param range
     * @return
     */
    default $P $isInRange(final Range range ){
        Predicate<P> pp = n -> {
            if (n instanceof Node) {
                return ((Node)n).getRange().isPresent() && range.strictlyContains( ((Node) n).getRange().get());
            } else if (n instanceof _java._node) {
                Node node = ((_java._node)n).ast();
                return node.getRange().isPresent() && range.strictlyContains(node.getRange().get());
            }else if (n instanceof _body) {
                Node node = ((_body)n).ast();
                return node.getRange().isPresent() && range.strictlyContains(node.getRange().get());
            }
            return false;
        };
        return $and( pp );
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param beginLine
     * @param beginColumn
     * @param endLine
     * @param endColumn
     * @return
     */
    default $P $isInRange(int beginLine, int beginColumn, int endLine, int endColumn){
        return $isInRange(Range.range(beginLine,beginColumn,endLine, endColumn));
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param beginLine
     * @param endLine
     * @return
     */
    default $P $isInRange(int beginLine, int endLine){
        return $isInRange(Range.range(beginLine,0,endLine, Integer.MAX_VALUE -10000));
    }

    /**
     * Verifies that the ENTIRE matching pattern exists on this specific line in the code
     * @param line the line expected
     * @return the modified $pattern
     */
    default $P $atLine(int line ){
        return $isInRange(Range.range(line,0,line, Integer.MAX_VALUE -10000));
    }

    /**
     *
     * @param map
     * @return
     */
    default $P hardcode$( Map map ){
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of(map) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param kvs the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    default $P hardcode$( Tokens kvs ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, kvs );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    default $P hardcode$( Object... keyValues ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    default $P hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator
     * @param kvs
     * @return
     */
    $P hardcode$( Translator translator, Tokens kvs );

    /**
     * Adds a constraint to test that the PARENT does NOT MATCH ANY of the prototypes provided
     * <PRE>
     *     class
     * </PRE>
     *
     * @param $ps
     * @return
     */
    default $P $isParentNot($pattern... $ps){
        for(int i=0;i<$ps.length; i++) {
            $pattern $p = $ps[i];
            Predicate<P> pp = n -> {
                if (n instanceof Node) {
                    return Ast.isParent((Node) n, c -> $p.match(c));
                } else if (n instanceof _java._node) {
                    return Ast.isParent(((_java._node) n).ast(), c -> $p.match(c));
                } else if (n instanceof _body) {
                    return Ast.isParent(((_body) n).ast(), c -> $p.match(c));
                }
                return false;
            };
            $not(pp);
        }
        return ($P)this;
    }

    /**
     * Adds a constraint to test that the parent of the instance Is this node the child of a a proto?
     * @param $ps the prototypes to match against
     * @return
     */
    default $P $isParent($pattern... $ps ){
        return $and(n -> {
            if (n instanceof Node) {
                return Ast.isParent( (Node)n, c->Arrays.stream($ps).anyMatch( $p->$p.match(c)) );
            } else if (n instanceof _java._node) {
                return Ast.isParent( ((_java._node)n).ast(), c->Arrays.stream($ps).anyMatch($p->$p.match(c)) );
            } else if (n instanceof _body) {
                return Ast.isParent( ((_body)n).ast(), c->Arrays.stream($ps).anyMatch( $p->$p.match(c)) );
            } else {
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : " + n.getClass());
            }
        });
        //return and( (n)-> Ast.isParent( (Node)n, e-> proto.match(e) ) );
    }

    default <N extends Node> $P $isParentNot(Class<N> parentClass, Predicate<N> parentMatchFn){
        return $not(n -> Ast.isParent( (Node)n, parentClass, parentMatchFn) );
    }

    default <N extends Node> $P $isParent(Class<N> parentClass, Predicate<N> parentMatchFn){
        return $and(n -> Ast.isParent( (Node)n, parentClass, parentMatchFn) );
    }

    default $P $isParentNot(Predicate<Node> parentMatchFn ){
        return $not(n -> Ast.isParent((Node)n, parentMatchFn) );
    }

    default $P $isParent(Predicate<Node> parentMatchFn ){
        return $and(n -> {
            if( n instanceof _java._domain){
                return Ast.isParent( ((_java._node)n).ast(), parentMatchFn);
            }
            return Ast.isParent((Node) n, parentMatchFn);
        });
    }

    default $P $isParentNot(Class... parentClassTypes ){
        return $not(n -> {
            if (n instanceof Node) {
                return Ast.isParent( (Node)n, parentClassTypes);
            } else if (n instanceof _java._node) {
                return Ast.isParent( ((_java._node)n).ast(), parentClassTypes);
            }else if (n instanceof _body) {
                return Ast.isParent( ((_body)n).ast(), parentClassTypes);
            }
            return false;
        });
    }

    default $P $isParent(Class... parentClassTypes ){
        return $and(n -> {
                    if (n instanceof Node) {
                        return Ast.isParent( (Node)n, parentClassTypes);
                    } else if (n instanceof _java._node) {
                        return Ast.isParent( ((_java._node)n).ast(), parentClassTypes);
                    } else if (n instanceof _body) {
                        return Ast.isParent( ((_body)n).ast(), parentClassTypes);
                    }  else {
                        //NEED TO MANUALLY IMPLEMENT FOR:
                        // $parameters, $annos, $snip, $throws, $typeParameters
                        // if( n instanceof List ){
                        //    List l = (List)n;
                        //    l.forEach();
                        // }
                        throw new _jdraftException("Not implemented yet for type : " + n.getClass());
                    }
                });
    }

    default $P $hasAncestor(Class...classes){
        return $hasAncestor(Integer.MAX_VALUE -100, n-> Ast.isNodeOfType(n, classes ));
    }

    /**
     *
     * @param ancestorMatchFn
     * @return
     */
    default $P $hasAncestor( Predicate<Node> ancestorMatchFn ){
        return $hasAncestor(Integer.MAX_VALUE -100, ancestorMatchFn);
    }

    default $P $hasNoAncestor( int levels, Predicate<Node>ancestorMatchFn){
        return $not(n-> {
            if( n instanceof Node ) {
                return ((Node)n).stream($.PARENTS).limit(levels).anyMatch( ancestorMatchFn  );
            }else if (n instanceof _java._node) {
                return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch( ancestorMatchFn );
            } else if (n instanceof _body) {
                return ((_body)n).ast().stream($.PARENTS).limit(levels).anyMatch( ancestorMatchFn );
            }else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    /**
     *
     * @param levels
     * @param ancestorMatchFn
     * @return
     */
    default $P $hasAncestor( int levels, Predicate<Node> ancestorMatchFn ){
        return $and(n-> {
            if( n instanceof Node ) {
                return ((Node)n).stream($.PARENTS).limit(levels).anyMatch( ancestorMatchFn  );
            }else if (n instanceof _java._node) {
                return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch( ancestorMatchFn );
            }else if (n instanceof _body) {
                return ((_body)n).ast().stream($.PARENTS).limit(levels).anyMatch( ancestorMatchFn );
            }  else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    /**
     * Constraint to verify a matching entity has one of these protoypes as ancestor
     * @param $ps
     * @return
     */
    default $P $hasAncestor( $pattern... $ps ) {
        return $hasAncestor(Integer.MAX_VALUE -100, $ps);
    }

    default $P $hasNoAncestor( $pattern... $ps){
        return $hasNoAncestor(Integer.MAX_VALUE -100, $ps);
    }

    /**
     * Adds a Not constraint that traverses UP(through parents, grandparents...) {@param levels} to see if
     * any ancestors match any of the prototypes
     * @param levels the maximum number of ancestors to check against
     * @param $ps the prototypes to test against
     * @return the modified prototype
     */
    default $P $hasNoAncestor( int levels, $pattern...$ps ){
        return $not(n-> {
            if( n instanceof Node ) {
                return ((Node)n).stream($.PARENTS).limit(levels).anyMatch( c-> Arrays.stream($ps).anyMatch($p ->$p.match(c)));
            }else if (n instanceof _java._node) {
                return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch(c-> Arrays.stream($ps).anyMatch($p ->$p.match(c)));
            }else if (n instanceof _body) {
                return ((_body)n).ast().stream($.PARENTS).limit(levels).anyMatch( c-> Arrays.stream($ps).anyMatch($p ->$p.match(c)));
            }  else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    default $P $hasAncestor( int levels, $pattern... $ps ){
        return $and(n-> {
            if( n instanceof Node ) {
                return ((Node)n).stream($.PARENTS).limit(levels).anyMatch( c-> Arrays.stream($ps).anyMatch($p ->$p.match(c)));
            }else if (n instanceof _java._node) {
                 return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch(c-> Arrays.stream($ps).anyMatch($p ->$p.match(c)));
            } else if (n instanceof _body) {
                return ((_body)n).ast().stream($.PARENTS).limit(levels).anyMatch( c-> Arrays.stream($ps).anyMatch($p ->$p.match(c)));
            } else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $snip, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    default $P $hasNoChild( $pattern...$ps){
        return $not(n-> {
            if( n instanceof Node ){
                return ((Node)n).getChildNodes().stream().anyMatch(c -> Arrays.stream($ps).anyMatch( $p->$p.match(c)));
            } else if( n instanceof _java._node){
                return ((_java._node)n).ast().getChildNodes().stream().anyMatch(c -> Arrays.stream($ps).anyMatch($p->$p.match(c)));
            } else if( n instanceof _body){
                return ((_body)n).ast().getChildNodes().stream().anyMatch(c -> Arrays.stream($ps).anyMatch( $p->$p.match(c)));
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    /**
     * Adds a constraint and
     * @param $ps
     * @return
     */
    default $P $hasChild( $pattern... $ps ){

        return $and(n-> {
            if( n instanceof Node ){
                return ((Node)n).getChildNodes().stream().anyMatch(c -> Arrays.stream($ps).anyMatch( $p->$p.match(c)));
            } else if( n instanceof _java._node){
                return ((_java._node)n).ast().getChildNodes().stream().anyMatch(c -> Arrays.stream($ps).anyMatch($p->$p.match(c)));
            } else if( n instanceof _body){
                return ((_body)n).ast().getChildNodes().stream().anyMatch(c -> Arrays.stream($ps).anyMatch( $p->$p.match(c)));
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    default $P $hasChild( Class... childClassTypes ){
        return $and(n-> {
            if( n instanceof Node ){
                return ((Node)n).getChildNodes().stream().anyMatch(c -> Ast.isNodeOfType(c, childClassTypes) );
            } else if( n instanceof _java._node){
                return ((_java._node)n).ast().getChildNodes().stream().anyMatch(c -> Ast.isNodeOfType(c, childClassTypes) );
            } else if( n instanceof _body){
                return ((_body)n).ast().getChildNodes().stream().anyMatch(c -> Ast.isNodeOfType(c, childClassTypes) );
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    default $P $hasNoChild( Class... childClassTypes ){
        return $not(n-> {
            if( n instanceof Node ){
                return ((Node)n).getChildNodes().stream().anyMatch(c -> Ast.isNodeOfType(c, childClassTypes) );
            } else if( n instanceof _java._node){
                return ((_java._node)n).ast().getChildNodes().stream().anyMatch(c -> Ast.isNodeOfType(c, childClassTypes) );
            } else if( n instanceof _body){
                return ((_body)n).ast().getChildNodes().stream().anyMatch(c -> Ast.isNodeOfType(c, childClassTypes) );
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    default $P $hasChild( Predicate<Node> childMatchFn ){
        return $and(n-> {
            if( n instanceof Node ){
                return ((Node)n).getChildNodes().stream().anyMatch(c -> childMatchFn.test(c) );
            } else if( n instanceof _java._node){
                return ((_java._node)n).ast().getChildNodes().stream().anyMatch(c -> childMatchFn.test(c) );
            } else if( n instanceof _body){
                return ((_body)n).ast().getChildNodes().stream().anyMatch(c -> childMatchFn.test(c) );
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    default $P $hasNoChild( Predicate<Node> childMatchFn ){
        return $not(n-> {
            if( n instanceof Node ){
                return ((Node)n).getChildNodes().stream().anyMatch(c -> childMatchFn.test(c) );
            } else if( n instanceof _java._node){
                return ((_java._node)n).ast().getChildNodes().stream().anyMatch(c -> childMatchFn.test(c) );
            } else if( n instanceof _body){
                return ((_body)n).ast().getChildNodes().stream().anyMatch(c -> childMatchFn.test(c) );
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    /**
     *
     * @param descendantClassTypes
     * @return
     */
    default $P $hasDescendant( Class... descendantClassTypes ){
        return $hasDescendant(Integer.MAX_VALUE -100, descendantClassTypes);
    }

    /**
     *
     * @param descendantClassTypes
     * @return
     */
    default $P $hasNoDescendant( Class... descendantClassTypes ){
        return $hasNoDescendant(Integer.MAX_VALUE -100, descendantClassTypes);
    }

    /**
     *
     * @param depth
     * @param descendantClassTypes
     * @return
     */
    default $P $hasDescendant( int depth, Class... descendantClassTypes ){
        return $hasDescendant(depth, c-> Ast.isNodeOfType(c, descendantClassTypes));
    }

    /**
     *
     * @param depth
     * @param descendantClassTypes
     * @return
     */
    default $P $hasNoDescendant( int depth, Class... descendantClassTypes ){
        return $hasNoDescendant(depth, c-> Ast.isNodeOfType(c, descendantClassTypes));
    }

    /**
     *
     * @param descendantMatchFn
     * @return
     */
    default $P $hasDescendant( Predicate<Node> descendantMatchFn ){
        return $hasDescendant(Integer.MAX_VALUE -100, descendantMatchFn);
    }

    /**
     *
     * @param descendantMatchFn
     * @return
     */
    default $P $hasNoDescendant( Predicate<Node> descendantMatchFn ){
        return $hasNoDescendant(Integer.MAX_VALUE -100, descendantMatchFn);
    }

    /**
     *
     * @param depth
     * @param descendantMatchFn
     * @return
     */
    default $P $hasDescendant( int depth, Predicate<Node> descendantMatchFn ){
        return $and(n-> {
            if( n instanceof Node ){
                return Ast.matchDescendant( (Node)n, depth, descendantMatchFn);
            } else if( n instanceof _java._node){
                return Ast.matchDescendant( ((_java._node)n).ast(), depth, descendantMatchFn);
            } else if( n instanceof _body){
                return Ast.matchDescendant( ((_body)n).ast(), depth, descendantMatchFn);
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    /**
     *
     * @param depth
     * @param descendantMatchFn
     * @return
     */
    default $P $hasNoDescendant( int depth, Predicate<Node> descendantMatchFn ){
        return $not(n-> {
            if( n instanceof Node ){
                return Ast.matchDescendant( (Node)n, depth, descendantMatchFn);
            } else if( n instanceof _java._node){
                return Ast.matchDescendant( ((_java._node)n).ast(), depth, descendantMatchFn);
            } else if( n instanceof _body){
                return Ast.matchDescendant( ((_body)n).ast(), depth, descendantMatchFn);
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }


    /**
     * check that any descendants match the descendantProto
     * i.e.
     *
     * //find any method that contains a synchronized statement
     * <PRE>{@code </PRE>$method.of().$hasDescendant( $.synchronizedStmt() ); }</PRE>
     *
     * @param $ps a prototype to match against the descendants
     * @return the modified $P
     */
    default $P $hasDescendant( $pattern... $ps ){
        return $hasDescendant(Integer.MAX_VALUE -100, $ps);
    }

    default $P $hasNoDescendant( $pattern... $ps ){
        return $hasNoDescendant(Integer.MAX_VALUE -100, $ps);
    }

    /**
     * check that any descendants match the descendantProto
     * i.e.
     *
     * //find any method that contains a synchronized statement within (4) levels of descendants
     * // (we might specify level(4) if the method contains a Local Class that we DONT want to match against)
     * <PRE>{@code </PRE>$method.of().$hasDescendant( 4, $.synchronizedStmt() ); }</PRE>
     *
     * @param depth how many levels deep to look for descendants
     * @param $ps a prototype to match against
     * @return the modified
     */
    default $P $hasDescendant( int depth, $pattern... $ps ){
        if( depth <= 0 ){
            return ($P)this;
        }
        return $and(n->{
            if( n instanceof Node ){
                return Ast.matchDescendant( (Node)n, depth, c->Arrays.stream($ps).anyMatch( $p-> $p.match(c) ));
            }else if( n instanceof _java._node){
                return Ast.matchDescendant( ((_java._node)n).ast(), depth, c->Arrays.stream($ps).anyMatch($p->$p.match(c)));
            } else if( n instanceof _body){
                return Ast.matchDescendant( ((_body)n).ast(), depth, c->Arrays.stream($ps).anyMatch( $p->$p.match(c)));
            }else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        });
    }

    default $P $hasNoDescendant( int depth, $pattern... $ps ){
        if( depth <= 0 ){
            return ($P)this;
        }
        return $not(n->{
            if( n instanceof Node ){
                return Ast.matchDescendant( (Node)n, depth, c->Arrays.stream($ps).anyMatch( $p-> $p.match(c) ));
            }else if( n instanceof _java._node){
                return Ast.matchDescendant( ((_java._node)n).ast(), depth, c->Arrays.stream($ps).anyMatch($p->$p.match(c)));
            } else{
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        });
    }

    /**
     * does this prototype match this ast candidate node?
     * @param candidate an ast candidate node
     * @return
     */
    boolean match( Node candidate );

    /**
     * does this prototype match this _java node?
     * @param _j a _java entity
     * @return true if the
     */
    default boolean match( _java._domain _j ){
        if( _j instanceof _field ){ //I think this'll work??

            /** TODO THIS SEEMS WRONG */


            return match( ((_field)_j).getFieldDeclaration());
        }
        if( _j instanceof _java._node){
            return match( ((_java._node)_j).ast());
        }
        return false;
    }

    /**
     * Find and return the first matching instance given the code provider
     * @param _codeProvider
     * @return
     */
    default boolean isIn( _compilationUnit._provider _codeProvider ){
        return firstIn(_codeProvider, t->true) != null;
    }

    /**
     *
     * @param clazz
     * @return
     */
    default boolean isIn(Class clazz){
        return firstIn((_type)_java.type(clazz) ) != null;
    }

    /**
     * Find the first in the collection
     * @param codeCollection a collection of code
     * @param <_J> the _code type ( _type, _class, _enum, etc.)
     * @return the first instance found within the code collection
     */
    default <_J extends _java._domain> boolean isIn(Collection<_J> codeCollection){
        return firstIn(codeCollection) != null;
    }

    /**
     *
     * @param clazz
     * @param nodeMatchFn
     * @return
     */
    default boolean isIn( Class clazz, Predicate<P> nodeMatchFn){
        return firstIn(clazz, nodeMatchFn) != null;
    }

    /**
     *
     * @param _codeProvider
     * @param matchFn
     * @return
     */
    default boolean isIn(_compilationUnit._provider _codeProvider, Predicate<P> matchFn){
        return firstIn(_codeProvider, matchFn) != null;
    }

    /**
     * Find the first instance matching the prototype instance within the node
     * @param _j the the _model node
     * @return  the first matching instance or null if none is found
     */
    default <_J extends _java._domain> boolean isIn(_J _j){
        return firstIn( _j ) != null;
    }

    /**
     *
     * @param _j
     * @param nodeMatchFn
     * @return
     */
    default boolean isIn(_java._domain _j, Predicate<P> nodeMatchFn){
        return firstIn(_j, nodeMatchFn) != null;
    }

    /**
     *
     * @param astStartNode
     * @return the first matching instance or null
     */
    default boolean isIn(Node astStartNode){
        return firstIn(astStartNode) != null;
    }

    /**
     *
     * @param astStartNode
     * @param nodeMatchFn
     * @return
     */
    default boolean isIn(Node astStartNode, Predicate<P> nodeMatchFn){
        return firstIn(astStartNode, nodeMatchFn) != null;
    }


    /**
     * Find and return the first matching instance given the code provider
     * @param _codeProvider
     * @return
     */
    default P firstIn( _compilationUnit._provider _codeProvider ){
       return firstIn(_codeProvider, t->true);
    }

    /**
     * 
     * @param clazz
     * @return 
     */
    default P firstIn(Class clazz){
        return firstIn((_type)_java.type(clazz) );
    }

    /**
     *
     * @param clazzes
     * @return
     */
    default P firstIn(Class... clazzes){
        for( int i=0; i< clazzes.length; i++){
            P p = firstIn( (_type)_java.type(clazzes[i]) );
            if( p != null ){
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param _js
     * @return
     */
    default <_J extends _java._domain> P firstIn(_J... _js){
        for( int i=0; i< _js.length; i++){
            P p = firstIn( _js[i] );
            if( p != null ){
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param astNodes
     * @return
     */
    default P firstIn(Node... astNodes){
        for( int i=0; i< astNodes.length; i++){
            P p = firstIn( astNodes[i] );
            if( p != null ){
                return p;
            }
        }
        return null;
    }

    /**
     * Find the first in the collection
     * @param codeCollection a collection of code
     * @param <_J> the _code type ( _type, _class, _enum, etc.)
     * @return the first instance found within the code collection
     */
    default <_J extends _java._domain> P firstIn(Collection<_J> codeCollection){
        List<_J> lc = codeCollection.stream().collect(Collectors.toList());
        for( int i=0; i< lc.size(); i++){
            P p = firstIn( lc.get(i) );
            if( p != null ){
                return p;
            }
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param nodeMatchFn
     * @return 
     */
    default P firstIn( Class clazz, Predicate<P> nodeMatchFn){
        return firstIn(_java.type(clazz).astCompilationUnit(), nodeMatchFn);
    }

    /**
     *
     * @param _codeProvider
     * @param matchFn
     * @return
     */
    default P firstIn(_compilationUnit._provider _codeProvider, Predicate<P> matchFn){
        P found = null;
        List<_compilationUnit> _lc = _codeProvider.list_code();
        for(int i=0;i<_lc.size();i++){
            found = firstIn(_lc.get(i));
            if( found != null && matchFn.test(found)){
                return found;
            }
        }
        return null;
    }
    
    /**
     * Find the first instance matching the prototype instance within the node
     * @param _j the the _model node
     * @return  the first matching instance or null if none is found
     */
    default <_J extends _java._domain> P firstIn(_J _j){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return firstIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j;
            return firstIn(_t.ast());
        }
        if( _j instanceof _body){
            _body _b = (_body)_j;
            return firstIn(_b.ast());
        }
        return firstIn( ((_java._node)_j).ast());
    }
    
    /**
     * 
     * @param _j
     * @param nodeMatchFn
     * @return 
     */
    default P firstIn(_java._domain _j, Predicate<P> nodeMatchFn){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return firstIn(_c.astCompilationUnit(), nodeMatchFn);
            }
            _type _t = (_type)_j; //only possible
            return firstIn(_t.ast(), nodeMatchFn);
        }
        if( _j instanceof _java._node) {
            return firstIn(((_java._node) _j).ast());
        }
        if( _j instanceof _body ){
            return firstIn(((_body) _j).ast());
        }
        throw new _jdraftException("Not implemented for type "+_j.getClass() );
    }
    
    /**
     * 
     * @param astStartNode
     * @return the first matching instance or null
     */
    default P firstIn(Node astStartNode){
        return firstIn(astStartNode, t->true);
    }
    
    /**
     * 
     * @param astStartNode
     * @param nodeMatchFn
     * @return 
     */
    P firstIn(Node astStartNode, Predicate<P> nodeMatchFn);

    /**
     *
     * @param instance
     * @param <S>
     * @return
     */
    <S extends selected> S select(P instance);

    /**
     * 
     * @param <S>
     * @param clazz
     * @return 
     */
    default <S extends selected> S selectFirstIn( Class clazz ){
        return selectFirstIn( (_type)_java.type(clazz));
    }

    /**
     *
     * @param <S>
     * @param classes
     * @return
     */
    default <S extends selected> S selectFirstIn( Class... classes ){
        for(int i=0;i<classes.length; i++){
            S s = selectFirstIn( (_type)_java.type(classes[i]) );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param <S>
     * @param _codeProvider
     * @return
     */
    default <S extends selected> S selectFirstIn(_compilationUnit._provider _codeProvider ){
        List<_compilationUnit> _cs = _codeProvider.list_code();
        for(int i=0;i<_cs.size(); i++){
            S s = selectFirstIn( _cs.get(i) );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param <S>
     * @param _js
     * @return
     */
    default <S extends selected, _J extends _java._domain> S selectFirstIn(_J... _js ){
        for(int i=0;i<_js.length; i++){
            S s = selectFirstIn( _js[i] );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param <S>
     * @param _jc
     * @return
     */
    default <S extends selected, _J extends _java._domain> S selectFirstIn(Collection<_J> _jc ){
        List<_J> l = _jc.stream().collect(Collectors.toList());
        for(int i=0;i<l.size(); i++){
            S s = selectFirstIn( l.get(i) );
            if( s != null ){
                return s;
            }
        }
        return null;
    }

    /**
     * 
     * @param <S>
     * @param _j
     * @return 
     */
    default <S extends selected> S selectFirstIn( _java._domain _j ){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast());
        }
        if( _j instanceof _java._node) {
            return selectFirstIn(((_java._node) _j).ast());
        }
        if( _j instanceof _body ){
            return selectFirstIn(((_body) _j).ast());
        }
        throw new _jdraftException("Not implemented for type "+_j.getClass() );
    }

    /**
     *
     * @param astStartNodes
     * @param <S>
     * @return
     */
    default <S extends selected> S selectFirstIn( Node... astStartNodes ){
        for(int i=0;i<astStartNodes.length; i++){
            S s = selectFirstIn( astStartNodes[i] );
            if( s != null){
                return s;
            }
        }
        return null;
    }

    /**
     * Selects the first instance
     * @param <S>
     * @param astNode
     * @return 
     */
    <S extends selected> S selectFirstIn( Node astNode );
        
    /**
     * Find and return a List of all matching the prototype within clazz
     *
     * @param clazz the runtime class (MUST HAVE JAVA SOURCE AVAILABLE IN CLASSPATH)
     * @return a List of P that match the query
     */
    default List<P> listIn(Class clazz){
        return listIn( (_type)_java.type(clazz));
    }

    default Stream<P> streamIn(Class clazz){
        return listIn(clazz).stream();
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param classes all of the runtime classes (MUST HAVE SOURCE AVAILABLE ON CLASSPATH)
     * @return a List of P that match the query
     */
    default List<P> listIn( Class...classes ){
        List<P> found = new ArrayList<>();
        Arrays.stream(classes).forEach(c -> found.addAll( listIn(c) ));
        return found;
    }

    default Stream<P> streamIn(Class...classes){
        return listIn(classes).stream();
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param _codeProvider the provider of _code instances
     * @return a List of P that match the query
     */
    default List<P> listIn( _compilationUnit._provider _codeProvider ){
        List<P> found = new ArrayList<>();
        _codeProvider.for_code(c -> found.addAll( listIn(c)));
        return found;
    }

    default Stream<P> streamIn( _compilationUnit._provider _codeProvider ){
        return listIn(_codeProvider).stream();
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param _js any collection of _code entities( _class, _enum, ...etc)
     * @param <_J> the underlying _code type (_code, _type, _packageInfo, etc.)
     * @return list of matching P for the query
     */
    default <_J extends _java._domain> List<P> listIn(Collection<_J> _js){
        List<P> found = new ArrayList<>();
        _js.forEach(c -> found.addAll( listIn(c) ));
        return found;
    }

    default <_J extends _java._domain> Stream<P> streamIn(Collection<_J> _js){
        return listIn(_js).stream();
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param _js any collection of _code entities( _class, _enum, ...etc)
     * @param nodeMatchFn additional function predicate for matching
     * @param <_J> the underlying _code type (_code, _type, _packageInfo, etc.)
     * @return list of matching P for the query
     */
    default <_J extends _java._domain> List<P> listIn(Collection<_J> _js, Predicate<P> nodeMatchFn){
        List<P> found = new ArrayList<>();
        _js.forEach(c -> found.addAll( listIn(c, nodeMatchFn) ));
        return found;
    }

    /**
     *
     * @param _js
     * @param nodeMatchFn
     * @param <_J>
     * @return
     */
    default <_J extends _java._domain> Stream<P> streamIn(Collection<_J> _js, Predicate<P> nodeMatchFn){
        return listIn(_js, nodeMatchFn).stream();
    }

    /**
     * Find and return a list of all matching prototypes within the clazz
     *
     * @param _codeProvider any collection of _code entities( _class, _enum, ...etc)
     * @param nodeMatchFn additional function predicate for matching
     * @param <_J> the underlying _code type (_code, _type, _packageInfo, etc.)
     * @return list of matching P for the query
     */
    default <_J extends _java._domain> List<P> listIn(_compilationUnit._provider _codeProvider, Predicate<P> nodeMatchFn){
        List<P> found = new ArrayList<>();
        _codeProvider.for_code(c -> found.addAll( listIn(c, nodeMatchFn) ));
        return found;
    }

    /**
     *
     * @param _codeProvider
     * @param nodeMatchFn
     * @param <_J>
     * @return
     */
    default <_J extends _java._domain> Stream<P> streamIn(_compilationUnit._provider _codeProvider, Predicate<P> nodeMatchFn){
        return listIn(_codeProvider, nodeMatchFn).stream();
    }

    /**
     * 
     * @param clazz
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(Class clazz, Predicate<P> nodeMatchFn){
        return listIn( (_type)_java.type(clazz), nodeMatchFn);
    }

    default Stream<P> streamIn(Class clazz, Predicate<P> nodeMatchFn){
        return listIn(clazz, nodeMatchFn).stream();
    }

    /**
     *
     * @param clazzes the list of classes to search
     * @param nodeMatchFn
     * @return
     */
    default List<P> listIn(Class[] clazzes, Predicate<P> nodeMatchFn){
        List<P> found = new ArrayList<>();
        Arrays.stream(clazzes).forEach( c-> found.addAll(listIn(c, nodeMatchFn)) );
        return found;
    }

    default Stream<P> streamIn(Class[] clazzes, Predicate<P> nodeMatchFn){
        return listIn(clazzes, nodeMatchFn).stream();
    }

    /**
     *
     * @param _js
     * @return
     */
    default List<P> listIn(_java._domain..._js){
        List<P> found = new ArrayList<>();
        Arrays.stream(_js).forEach( j -> found.addAll( listIn(j)) );
        return found;
    }

    default Stream<P> streamIn(_java._domain..._js){
        return listIn(_js).stream();
    }

    /**
     * Find and return a List of all matching node types within _n
     *
     * @param _j the root _java model node to start the search (i.e. _class,
     * _method, _packageInfo)
     * @return a List of Q that match the query
     */
    default List<P> listIn(_java._domain _j) {
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j; //only possible
            return listIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        if( _j instanceof _java._node) {
            return listIn(((_java._node) _j).ast() );
        }
        if( _j instanceof _body ){
            return listIn(((_body) _j).ast() );
        }
        throw new _jdraftException("Not implemented for type "+_j.getClass() );
        //return listIn( ((_node)_j).ast() );
    }

    default Stream<P> streamIn(_java._domain _j) {
        return listIn(_j).stream();
    }
    
    /**
     * 
     * @param _j the _java model
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(_java._domain _j, Predicate<P>nodeMatchFn){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return listIn(_c.astCompilationUnit(), nodeMatchFn);
            }
            _type _t = (_type)_j; //only possible
            return listIn(_t.ast(), nodeMatchFn); //return the TypeDeclaration, not the CompilationUnit
        }
        if( _j instanceof _java._node) {
            return listIn(((_java._node) _j).ast(), nodeMatchFn);
        }
        if( _j instanceof _body ){
            return listIn(((_body) _j).ast(), nodeMatchFn);
        }
        throw new _jdraftException("Not implemented for type "+_j.getClass() );
        //return listIn(((_node)_j).ast(),nodeMatchFn);
    }

    default Stream<P> streamIn(_java._domain _j, Predicate<P>nodeMatchFn){
        return listIn(_j, nodeMatchFn).stream();
    }

    /**
     *
     * @param astNodes
     * @return
     */
    default List<P> listIn( Node...astNodes){
        ArrayList<P> ap = new ArrayList<>();
        Arrays.stream(astNodes).forEach( n -> ap.addAll( listIn(n) ));
        return ap;
    }

    default Stream<P> streamIn( Node...astNodes){
        return listIn(astNodes).stream();
    }

    /**
     *
     * @param astNode the root AST node to start the search
     * @return a List of Q matching the query
     */
    default List<P> listIn(Node astNode){
        return listIn( astNode, t->true);
    }

    default Stream<P> streamIn(Node astNode){
        return listIn(astNode).stream();
    }

    /**
     * 
     * @param astStartNode
     * @param nodeMatchFn
     * @return 
     */
    default List<P> listIn(Node astStartNode, Predicate<P> nodeMatchFn){
        List<P> found = new ArrayList<>();
        forEachIn(astStartNode, nodeMatchFn, b-> found.add(b));
        return found;    
    }

    default Stream<P> streamIn(Node astStartNode, Predicate<P> nodeMatchFn){
        return listIn(astStartNode, nodeMatchFn).stream();
    }

    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param clazz runtime class (MUST HAVE .java source code in CLASSPATH)
     * @return the selected
     */
    default <S extends selected> List<S> listSelectedIn(Class clazz){
        return listSelectedIn( (_type)_java.type(clazz));
    }

    /**
     *
     * @param clazzes
     * @param <S>
     * @return
     */
    default <S extends selected> List<S> listSelectedIn(Class... clazzes){
        List<S> sel = new ArrayList<>();
        Arrays.stream(clazzes).forEach( c -> sel.addAll(listSelectedIn(c)));
        return sel;
    }

    /**
     *
     * @param _codeProvider
     * @param <S>
     * @return
     */
    default <S extends selected> List<S> listSelectedIn(_compilationUnit._provider _codeProvider){
        List<S> sel = new ArrayList<>();
        _codeProvider.for_code(_j -> sel.addAll( listSelectedIn( _j )) );
        return sel;
    }

    /**
     *
     * @param _js
     * @param <S>
     * @param <_J>
     * @return
     */
    default <S extends selected, _J extends _java._domain> List<S> listSelectedIn(Collection<_J> _js){
        List<S> sel = new ArrayList<>();
        _js.forEach(_j -> sel.addAll( listSelectedIn( _j )) );
        return sel;
    }
    
    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the _j
     *
     * @param _j the java entity (_type, _method, etc.) where to start the
     * search
     * @return a list of the selected
     */
    default <S extends selected> List<S> listSelectedIn(_java._domain _j){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit)_j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit());
            }
            _type _t = (_type)_j; //only possible
            return listSelectedIn(_t.ast()); //return the TypeDeclaration, not the CompilationUnit
        }
        if( _j instanceof _java._node) {
            return listSelectedIn(((_java._node) _j).ast());
        }
        if( _j instanceof _body ){
            return listSelectedIn(((_body) _j).ast() );
        }
        throw new _jdraftException("Not implemented for type "+_j.getClass() );
        //return listSelectedIn( ((_node)_j).ast());
    }
    
    /**
     * return the selections (containing the node and deconstructed parts) of
     * all matching entities within the astRootNode
     *
     * @param astNode the node to start the search (TypeDeclaration,
     * MethodDeclaration)
     * @return the selected
     */
    <S extends selected> List<S> listSelectedIn(Node astNode);
    
    /**
     * 
     * @param clazz the runtime Class (.java source must be on the classpath)
     * @param nodeActionFn what to do with each entity matching the prototype
     * @return the (potentially modified) _type 
     */
    default <_CT extends _type> _CT forEachIn(Class clazz, Consumer<P>nodeActionFn ){
        return forEachIn( (_CT)_java.type(clazz), nodeActionFn);
    }

    /**
     * Look through the models for all of these {@link _type}s and when we encounter a matching one execute the nodeActionFn
     * @param clazzes an array of loaded classes
     * @param nodeActionFn some action to take on the matches
     * @return the _types for all of the classes (we had to create them so might as well return em)
     * NOTE: since we pass Class references in, we know they must be _type implementations
     * (_class, _enum, _interface, _annotation) because we cant reference package-info.class or module-info.class
     */
    default List<_type> forEachIn(Class[] clazzes, Consumer<P>nodeActionFn ){
        List<_type> types = new ArrayList<>();
        Arrays.stream(clazzes).forEach( c -> types.add( forEachIn(c, nodeActionFn)));
        return types;
    }

    /**
     *
     * @param _codeProvider
     * @param nodeActionFn
     * @return
     */
    default List<_compilationUnit> forEachIn(_compilationUnit._provider _codeProvider, Consumer<P> nodeActionFn ){
        List<_compilationUnit> ts = new ArrayList<>();
        _codeProvider.for_code( j-> ts.add( forEachIn( j, nodeActionFn) ) );
        return ts;
    }

    /**
     *
     * @param _js
     * @param nodeActionFn
     * @return
     */
    default <_J extends _java._domain> List<_J> forEachIn(Collection<_J> _js, Consumer<P>nodeActionFn ){
        List<_J> ts = new ArrayList<>();
        _js.stream().forEach( j-> ts.add( forEachIn( j, nodeActionFn) ) );
        return ts;
    }

    /**
     *
     * @param _js
     * @param nodeMatchFn
     * @param nodeActionFn
     * @return
     */
    default <_J extends _java._domain> List<_J> forEachIn(Collection<_J> _js, Predicate<P> nodeMatchFn, Consumer<P>nodeActionFn ){
        List<_J> ts = new ArrayList<>();
        _js.stream().forEach( j-> ts.add( forEachIn( j, nodeMatchFn, nodeActionFn) ) );
        return ts;
    }

    /**
     * Find and execute a function on all of the matching occurrences within
     * astRootNode
     *
     * @param <_J>
     * @param _j the java node to start the walk
     * @param nodeActionFn the function to run on all matching entities
     * @return the modified _java node
     */
    default <_J extends _java._domain> _J forEachIn(_J _j, Consumer<P> nodeActionFn){
        return forEachIn(_j, t->true, nodeActionFn);
    }
    
    /**
     * Find and execute a function on all of the matching occurrences that
     * satisfy the _nodeMatchFn within the _node _n
     *
     * @param <_J>
     * @param _j the node to search through (_type, _method, etc.)
     * @param nodeMatchFn matching function to filter which nodes to operate on
     * @param nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    default <_J extends _java._domain> _J forEachIn(_J _j, Predicate<P> nodeMatchFn, Consumer<P> nodeActionFn){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                forEachIn(_c.astCompilationUnit(), nodeMatchFn, nodeActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forEachIn(_t.ast(), nodeMatchFn, nodeActionFn); //return the TypeDeclaration, not the CompilationUnit
            return _j;
        }
        if( _j instanceof _java._node) {
            forEachIn(((_java._node) _j).ast(), nodeMatchFn, nodeActionFn);
            return _j;
        }
        if( _j instanceof _body ){
            forEachIn(((_body) _j).ast(), nodeMatchFn, nodeActionFn);
            return _j;
        }
        throw new _jdraftException("Not implemented for type "+_j.getClass() );
    }
    
    /**
     * Find and execute a function on all of the matching occurrences within
     * astRootNode
     *
     * @param <N>
     * @param astNode the node to search through (CompilationUnit,
     * MethodDeclaration, etc.)
     * @param _nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    default <N extends Node> N forEachIn(N astNode, Consumer<P> _nodeActionFn){
        return forEachIn(astNode, t->true, _nodeActionFn);
    }

    /**
     * Find and execute a function on all of the matching occurrences that
     * satisfy the nodeMatchFn within the Node astRootNode
     *
     * @param <N>
     * @param astNode the node to search through (CompilationUnit,
     * MethodDeclaration, etc.)
     * @param nodeMatchFn matching function to filter which nodes to operate on
     * @param nodeActionFn the function to run upon each encounter with a
     * matching node
     * @return the modified astRootNode
     */
    <N extends Node> N forEachIn(N astNode, Predicate<P> nodeMatchFn, Consumer<P> nodeActionFn);
    
    /**
     *
     * @param clazz
     * @return
     */
    default int count( Class clazz ){
        return count( (_type)_java.type(clazz));
    }

    /**
     * Count the number of occurrences within the collection of code
     * @param _codeProvider the collection to search through
     * @return
     */
    default int count( _compilationUnit._provider _codeProvider){
        AtomicInteger ai = new AtomicInteger();
        _codeProvider.for_code(c-> ai.addAndGet(count(c)));
        return ai.get();
    }

    /**
     * Count the number of occurrences within the collection of code
     * @param cs the collection to search through
     * @param <_C>
     * @return
     */
    default <_C extends _compilationUnit> int count(Collection<_C> cs){
        AtomicInteger ai = new AtomicInteger();
        cs.forEach( c -> ai.addAndGet( count(c)));
        return ai.get();
    }

    /**
     *
     * @param classes
     * @return
     */
    default int count( Class... classes ){
        int count = 0;
        for(int i=0;i<classes.length;i++) {
            count +=count((_type) _java.type(classes[i]));
        }
        return count;
    }

    /**
     * Determines the count found in all of the Ast nodes
     * @param astNodes AST nodes (TypeDeclaration, MethodDeclaration, CompilationUnit)
     * @param <N> the underlying Node type
     * @return the count of instances found
     */
    default <N extends Node> int count( N... astNodes ){
        int count = 0;
        for(int i=0;i<astNodes.length;i++) {
            count +=count(astNodes[i]);
        }
        return count;
    }

    /**
     *
     * @param <N>
     * @param astNode
     * @return
     */
    default <N extends Node> int count( N astNode ){
        AtomicInteger ai = new AtomicInteger(0);
        forEachIn( astNode, e -> ai.incrementAndGet() );
        return ai.get();
    }

    /**
     *
     * @param <_J>
     * @param _j
     * @return
     */
    default <_J extends _java._domain> int count(_J _j ){
        AtomicInteger ai = new AtomicInteger(0);
        forEachIn(_j, e -> ai.incrementAndGet() );
        return ai.get();
    }

    /**
     * print each occurrence of the proto found within the class
     * @param clazz
     * @return
     */
    default void printIn( Class clazz ){
        printIn( (_type)_java.type(clazz));
    }

    /**
     * print each occurrence of the proto found within the code providers code
     * @param _codeProvider the collection to search through
     * @return
     */
    default void printIn( _compilationUnit._provider _codeProvider){
        _codeProvider.for_code(c-> printIn(c));
    }

    /**
     * print each occurrence of the proto found within the code collection
     * @param cs the collection to search through
     * @param <_C>
     * @return
     */
    default <_C extends _compilationUnit> void printIn(Collection<_C> cs){
        cs.forEach( c -> printIn(c));
    }

    /**
     * print each occurrence of the proto found within the Classes provided
     * @param classes
     * @return
     */
    default void printIn( Class... classes ){
        for(int i=0;i<classes.length;i++) {
            printIn((_type) _java.type(classes[i]));
        }
    }

    /**
     * print each occurrence of the proto found within the code providers code
     * @param astNodes AST nodes (TypeDeclaration, MethodDeclaration, CompilationUnit)
     * @param <N> the underlying Node type
     * @return the count of instances found
     */
    default <N extends Node> void printIn( N... astNodes ){
        for(int i=0;i<astNodes.length;i++) {
            printIn( astNodes[i]);
        }
    }

    /**
     * print each occurrence of the proto found within the ast node
     * @param <N>
     * @param astNode
     * @return
     */
    default <N extends Node> void printIn( N astNode ){
        forEachIn( astNode, e -> System.out.println( e ) );
    }

    /**
     * print each occurrence of the proto found within the _java entity
     * @param <_J>
     * @param _j
     * @return
     */
    default <_J extends _java._domain> void printIn(_J _j ){
        forEachIn(_j, e -> System.out.println( e ) );
    }

    /**
     *
     * @param _cp
     * @return
     */
    default List<_compilationUnit> removeIn(_compilationUnit._provider _cp ){
        return _cp.for_code(c-> removeIn(c) );
    }

    /**
     *
     * @param _js
     * @param <_J>
     * @return
     */
    default <_J extends _java._domain> Collection<_J> removeIn(Collection<_J> _js ){
        _js.forEach( _j -> removeIn(_j) );
        return _js;
    }

    /**
     *
     * @param _js
     * @param <_J>
     * @return
     */
    default <_J extends _java._domain> Collection<_J> removeIn(Collection<_J> _js, Predicate<P> nodeMatchFn){
        _js.forEach( _j -> removeIn(_j, nodeMatchFn) );
        return _js;
    }

    /**
     *
     * @param _cp
     * @return
     */
    default List<_compilationUnit> removeIn(_compilationUnit._provider _cp, Predicate<P>nodeMatchFn){
        return _cp.for_code(c-> removeIn(c, nodeMatchFn) );
    }

    /**
     * 
     * @param clazz the runtime _type (MUST have .java SOURCE in the classpath) 
     * @return the _type with all entities matching the prototype (& constraint) removed
     */
    default <_CT extends _type> _CT removeIn(Class clazz){
        return (_CT)removeIn( (_type)_java.type(clazz));
    } 
    
    /**
     * 
     * @param clazz the runtime _type (MUST have .java SOURCE in the classpath) 
     * @param nodeMatchFn 
     * @return the _type with all entities matching the prototype (& constraint) removed
     */
    default <_CT extends _type> _CT removeIn(Class clazz, Predicate<P> nodeMatchFn){
        return (_CT)removeIn( (_type)_java.type(clazz), nodeMatchFn);
    } 
    
    /**
     *
     * @param _j the root java node to start from (_type, _method, etc.)
     * @param <_J> the TYPE of model node
     * @return the modified model node
     */
    default <_J extends _java._domain> _J removeIn(_J _j){
        removeIn(_j, t->true);
        return _j;
    }
    
    /**
     * 
     * @param <_J> the TYPE of _model
     * @param _j the root _java node to start from (_type, _method, etc.)
     * @param nodeMatchFn
     * @return the modified model node
     */
    default <_J extends _java._domain> _J removeIn(_J _j, Predicate<P> nodeMatchFn){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                removeIn(_c.astCompilationUnit(), nodeMatchFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            removeIn(_t.ast(), nodeMatchFn); //return the TypeDeclaration, not the CompilationUnit
            return _j;
        }
        removeIn(((_java._node) _j).ast(), nodeMatchFn);
        return _j;
    }
    
    /**
     * Remove all matching occurrences of the template in the node and return
     * the modified node
     *
     * @param astNode the root node to start search
     * @param <N> the input node TYPE
     * @return the modified node
     */
    default <N extends Node> N removeIn(N astNode){
        return removeIn(astNode, t->true);
    }

    /**
     * Remove all matching occurrences of the template in the node and return
     * the modified node
     * @param <N> the input node TYPE
     * @param astNode the root node to start search
     * @param nodeMatchFn function to match nodes to remove
     * @return the modified node
     */
    default <N extends Node> N removeIn(N astNode, Predicate<P> nodeMatchFn){
        return forEachIn(astNode, s-> {
            if( nodeMatchFn.test( s ) ){
                if( s instanceof Node ){
                    ((Node) s).remove();
                } else{
                    if(s instanceof _java._node){
                        ((_java._node)s).ast().remove();
                    } else{
                        /* yeah this is kinda a mess
                        if( s instanceof _throws ){
                            //for throws... ONLY remove the ones we found (NOT ALL THROWS)
                            $throws $ts = ($throws)this;
                            _throws _th = (_throws)s;
                            _th.forEach(t -> {
                                if($ts.matches(t)){
                                    t.remove()
                                }
                            });
                        }
                        */
                        //throws, typeRef
                    }

                }
            }            
        });
    }

    /**
     * Common functionality present in $patterns that are related to
     * AST elements /searches
     * @param <A> the Ast type
     */
    interface $ast<A extends Node, $P extends $pattern> extends $pattern<A, $P> {

        /** The Java Ast type associated with this pattern */
        Class<A> astType();
    }

    /**
     * Common functionality present in $patterns that are related to AST elements /searches
     * @param <_J> the java _model type associated with this pattern
     */
    interface $java<_J extends _java._domain, $P extends $pattern> extends $pattern<_J, $P> {

        /** The Java _model type that is represented by this $pattern */
        Class<_J> _modelType();

        default <_CT extends _type> _CT replaceIn( Class clazz, $P $protoReplace ){
            return (_CT)replaceIn((_type)_java.type(clazz), $protoReplace);
        }

        default List<_compilationUnit> replaceIn(_compilationUnit._provider _codeProvider, $P $protoReplace ){
            return _codeProvider.for_code( c -> replaceIn(c, $protoReplace));
        }

        default <_J extends _java._domain> _J replaceIn(_J _j , $P $protoReplace ){
            Walk.in(_j, _modelType(), e-> {
                $pattern.select_java<_J> sel = select(e);
                if( sel != null ){
                    _java._node _n = (_java._node)sel._node();
                    Template<_J> tj = (Template<_J>) $protoReplace;
                    _java._node rep = (_java._node)tj.draft( sel.tokens() );
                    _n.ast().replace( rep.ast() );
                }
            });
            return _j;
        }

        /**
         *
         * @param <N>
         * @param astNode
         * @param $protoReplace
         * @return
         */
        default <N extends Node> N replaceIn(N astNode, $P $protoReplace ){
            Walk.in( astNode, _modelType(), t->true, e-> {
                $pattern.selected sel = select( (_J)e );
                //$proto.select_java<P> sel = select((P)e);
                if( sel != null ){
                    $pattern.select_java<_J> found = ($pattern.select_java<_J>) sel;
                    //build the replacement

                    Template<_J> $rp = (Template<_J>)$protoReplace;
                    _J replacement = $rp.draft(sel.tokens());

                    //replace the replacement
                    ((_java._node)found._node()).ast().replace( ((_java._node)replacement).ast() );
                }
                //sel._ann.ast().replace($annoReplacement.draft(sel.tokens).ast() );
                //}
            });
            return astNode;
        }

    }

    /**
     * An extra layer on top of a Tokens that is specifically
     * for holding value data that COULD be Expressions, Statements and the 
     * like
     */
    class $tokens implements Map<String, Object> {

        /**
         *
         */
        private Tokens tokens;

        public static $tokens of(){
            return new $tokens( Tokens.of() );
        }
        
        public static $tokens of(Tokens ts) {
            if (ts == null) {
                return null;
            }
            return new $tokens(ts);
        }

        public $tokens(){
            this.tokens = new Tokens();
        }

        public $tokens(Tokens ts) {
            this.tokens = ts;
        }

        public Object get(String $name) {
            return tokens.get($name);
        }

        public Tokens asTokens() {
            return tokens;
        }

        public Expression expr(String $name) {
            Object obj = get($name);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            }
            return Ex.of(obj.toString());
        }

        public Statement stmt(String $name) {
            Object obj = get($name);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            }
            return Stmt.of(obj.toString());
        }

        public _typeRef type(String $name) {
            Object obj = get($name);
            if (obj == null || obj.toString().trim().length() == 0) {
                return null;
            }
            return _typeRef.of(obj.toString());
        }

        /**
         * Reads the data from the $nameValues and parse & return the data as
         * a List of Statements
         * @param $name
         * @return 
         */
        public List<Statement> stmts(String $name) {
            Object obj = get($name);
            if (obj == null) {
                return null;
            }
            if (obj.toString().trim().length() == 0) {
                return Collections.EMPTY_LIST;
            }
            return Stmt.blockStmt(obj.toString()).getStatements();
        }

        /**
         *
         * $tokens.to(...) will short circuit
         * IF "tokens" is null: return null (without running the lambda)
         * IF "tokens" is not null : run the {@param supplier} lambda and derive "newTokens" of Map<String,Object>
         * IF "newTokens" is null : return null (this means that this particular match failed)
         * IF "newTokens" is not null : check that the "tokens" are consistent with "newTokens"
         * IF "tokens"/"newTokens" ARE NOT consistent (i.e. at least one var is assigned (2) distinct values) : return null
         * IF "tokens"/"newTokens" ARE consistent : return the "composite" tokens list (the union of "tokens" & "newTokens")
         *
         */
        public static $tokens to( $tokens tokens, Supplier<Map<String,Object>>supplier){
            if( tokens == null ){
                return null; //short circuit (no need to run the check, tokens already null)
            }
            Map<String,Object> newTokens = supplier.get(); //run the lambda to find new tokens
            if( tokens.isConsistent(newTokens)){
                tokens.putAll(newTokens);
                return tokens;
            } else{
                //System.out.println( "Consistency failure at "+ newTokens+" "+ tokens);
            }
            return null;
        }

        public <M extends Map<String,Object>> boolean isConsistent(M tokens){
            return this.tokens.isConsistent(tokens);
        }

        /**
         * is the clause with the key equal to the Type?
         *
         * @param $name
         * @param astType
         * @return true if
         */
        public boolean is(String $name, Type astType) {
            return is($name, _typeRef.of(astType));
        }

        /**
         * 
         * @param $name
         * @param _t
         * @return 
         */
        public boolean is(String $name, _typeRef _t) {
            return type($name).equals(_t);
        }

        /**
         * is the clause with the key equal to the expression?
         *
         * @param $name
         * @param exp
         * @return true if
         */
        public boolean is(String $name, Expression exp) {
            Expression ex = expr($name);
            return exp.equals(ex);
        }

        /**
         * is the clause with the key equal to the expression?
         *
         * @param $name
         * @param st
         * @return true if
         */
        public boolean is(String $name, Statement st) {
            Statement stmt = stmt($name);
            return stmt.toString(Ast.PRINT_NO_COMMENTS).equals(st.toString(Ast.PRINT_NO_COMMENTS));
        }

        public boolean is($tokens $nvs) {
            return this.equals($nvs);
        }

        public boolean is(Tokens tks) {
            return this.equals($tokens.of(tks));
        }

        public boolean is(String $name, Class clazz ){
            Object o = get($name);
            if( o != null ){
                return _typeRef.of( o.toString() ).equals(_typeRef.of(clazz));
            }
            return false;            
        }
        
        
        /**
         * 
         * @param $name
         * @param expectedValue
         * @return 
         */
        public boolean is(String $name, Object expectedValue) {
            //this matches nullExpr or simply not there
            Object o = get($name);

            if (expectedValue == null || expectedValue instanceof NullLiteralExpr) {
                if (!(o == null || o instanceof NullLiteralExpr || o.equals("null"))) {
                    return false;
                }
                return true;
            }
            if (expectedValue instanceof String && o instanceof String) {
                String v = (String) expectedValue;
                String s = (String) o;

                if (s.startsWith("\"") && s.endsWith("\"")) {
                    s = s.substring(1, s.length() - 1);
                }
                if (v.startsWith("\"") && v.endsWith("\"")) {
                    v = v.substring(1, v.length() - 1);
                }
                return s.equals(v);
            }
            if (expectedValue instanceof Expression) {
                return Ex.equivalent((Expression) expectedValue, get($name));
            } else if (expectedValue instanceof String) {
                try {
                    return Ex.equivalent(Ex.of((String) expectedValue), o);
                } catch (Exception e) {

                }
                return Objects.equals(expectedValue, get($name));
            } else if (o.getClass().equals(expectedValue.getClass())) {
                return o.equals(expectedValue);
            }
            return expectedValue.toString().equals(o);
        }

        /**
         *
         * @param $nvs the name values
         * @return
         */
        public boolean is(Object... $nvs) {
            if ($nvs.length % 2 == 1) {
                throw new _jdraftException("Expected an even number of key values, got (" + $nvs.length + ")");
            }
            for (int i = 0; i < $nvs.length; i += 2) {
                String key = $nvs[i].toString();
                if ( !is(key, get(key) ) ) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !o.getClass().equals($tokens.class)) {
                return false;
            }
            $tokens co = ($tokens) o;
            return Objects.equals(co.tokens, tokens);
        }

        @Override
        public int hashCode() {
            return this.tokens.hashCode();
        }

        @Override
        public String toString() {
            return this.tokens.toString();
        }

        @Override
        public int size() {
            return tokens.size();
        }

        @Override
        public boolean isEmpty() {
            return tokens.isEmpty();
        }

        @Override
        public boolean containsKey(Object $name) {
            return tokens.containsKey($name);
        }

        @Override
        public boolean containsValue(Object value) {
            return tokens.containsValue(value);
        }

        @Override
        public Object get(Object $name) {
            return tokens.get($name);
        }

        @Override
        public Object put(String $name, Object value) {
            return tokens.put($name, value);
        }

        @Override
        public Object remove(Object $name) {
            return tokens.remove($name);
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> $nvs) {
            tokens.putAll($nvs);
        }

        @Override
        public void clear() {
            tokens.clear();
        }

        @Override
        public Set<String> keySet() {
            return tokens.keySet();
        }

        @Override
        public Collection<Object> values() {
            return tokens.values();
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return tokens.entrySet();
        }
    }

    /**
     * a selected entity from a prototype query
     *
     */
    interface selected {

        /** return the parsed tokens from the selection */
        $tokens tokens();

        /** Get the value of this $param$ via the name */
        default Object get(String $name ){
            return tokens().get($name);
        }
        
        default boolean is(Object... $nameValues) {
            return tokens().is($nameValues);
        }

        default boolean is(String $name, String value) {
            return tokens().is($name, value);
        }

        default boolean is(String $name, Expression value) {
            return tokens().is($name, value);
        }

        default boolean is(String $name, Statement value) {
            return tokens().is($name, value);
        }

        default boolean is(String $name, Type value) {
            return tokens().is($name, value);
        }                
    }

    /**
     * The entity that is Selected is/has an AST node Representation
     *
     * @param <N> the specific type of AST Node that is selected
     */
    interface selectAst<N extends Node> extends selected {

        /**
         * @return the selected AST Node (i.e. Expression, Statement,
         * VariableDeclarator)
         */
        N ast();
    }

    /**
     * The entity that is selected is/has a draft _node Representation (which
     * wraps the underlying Ast Node/Nodes)
     *
     * @param <_J> the jDraft _java representation
     */
    interface select_java<_J extends _java._domain> extends selected {

        /**
         * @return the selected _java node (i.e. _method for a MethodDeclaration)
         */
        _J _node();
    }


    /**
     * Methods to simplify matching code WITHIN the same BODY
     * for example if if have a method:
     * <PRE>
     * class C{
     *     void b4(){
     *         System.out.println(0);
     *     }
     *     void m(){
     *         System.out.println(1);
     *         System.out.println(2);
     *         {
     *             System.out.println(3);
     *             System.out.println(4);
     *             if( true ){
     *                 System.out.println( 5 );
     *             }
     *             HERE : System.out.println(6); //<--- at this Statement
     *             if(1==1){
     *                 System.out.println(7);
     *             }
     *             System.out.println(8);
     *         }
     *         System.out.println(9);
     *     }
     *     void after(){
     *         System.out.println(10);
     *     }
     * }
     * </PRE>
     * <PRE>
     *
     * Statement here = $.labeledStmt().firstIn(C.class); //get the HERE: (labeledStatement)
     * $stmt $anyPrintln = $.stmt.of("System.out.println($any$);");
     *
     * // we find the code BEFORE but in the same Body
     * List<Node> allBefore = $pattern.BodyScope.listAllBefore( here, $anyPrintln);
     * // allBefore contains System.out.println(1); System.out.println(2); System.out.println(3); System.out.println(4); System.out.println(5);
     *
     * Statement previous = $pattern.BodyScope.findPrevious( here, $anyPrintln);
     * //previous = System.out.println(5);
     *
     * //find the code AFTER but within the same block
     * List<Node> allAfter = $pattern.BodyScope.listAllAfter( here, $anyPrintln);
     * //allAfter contains System.out.println(7); System.out.println(8); System.out.println(9);
     *
     * Statement next = $pattern.BodyScope.findNext( here, $anyPrintln);
     * //next = System.out.println(7);
     * </PRE>
     *
     * @see BodyScope
     */
    interface BodyScope {

        /**
         *
         * @param firstNode the "terminal" node, (ONLY NODES AFTER THIS NODE WILL BE TESTED)
         * @param $matchPatterns one or more $patterns to match within the
         * @param <N> the type of node we expect
         * @return
         */
        public static <N extends Node> N findNext(Node firstNode, $pattern...$matchPatterns ){
            List<Node> nodesAfter = listNodesAfter( firstNode );

            Optional<Node> on =
                    nodesAfter.stream().filter(bn -> Arrays.stream($matchPatterns).anyMatch($n -> $n.match(bn) ) ).findFirst();
            if( on.isPresent() ){
                return (N)on.get();
            }
            return null;
        }

        /**
         *
         * @param lastNode the "terminal" node, (ONLY NODES BEFORE THIS WILL BE TESTED)
         * @param $matchPatterns one or more $patterns to match within the
         * @param <N>
         * @return
         */
        public static <N extends Node> N findPrevious(Node lastNode, $pattern...$matchPatterns ){
            List<Node> nodesBefore = listNodesBefore( lastNode );
            //Since we want to find the most previous one closes to the lastNode, we need to
            //reverse the order of the nodesBefore before
            // i.e. if we had :
            // int m(){
            //     i=1;                  //<-- we SHOULD NOT return this statement... it's furthest away
            //     System.out.println(i);
            //     i=2;                  //<-- we should return THIS statement... as it's CLOSEST
            //     System.out.println(i);
            //     assert(true);         //<-- IF this is the lastNode
            // }
            // $stmt $matchIAssignment = $stmt.of("i = $any$;"); //if this is the ONLY matchPattern
            // Statement st = findPrevious(lastNode, $matchIAssignment); //should return "i=2;"

            // THIS IS WHY we reverse the nodesBefore, we want to identify/break as soon as we reach the FIRST match
            Collections.reverse(nodesBefore);
            Optional<Node> on =
                    nodesBefore.stream().filter(bn -> Arrays.stream($matchPatterns).anyMatch($n -> $n.match(bn) ) ).findFirst();
            if( on.isPresent() ){
                return (N)on.get();
            }
            return null;
        }

        /**
         *
         * @param startNode
         * @param $ps
         * @return
         */
        public static List<Node> listAllBefore(Node startNode, $pattern... $ps ){
            List<Node> before = listNodesBefore(startNode);
            List<Node> allBefore =
                    before.stream().filter(bn -> Arrays.stream($ps).anyMatch($n -> $n.match(bn) ) ).collect(Collectors.toList());
            return allBefore;
        }

        /**
         *
         * @param startNode
         * @param $ps
         * @return
         */
        public static List<Node> listAllAfter(Node startNode, $pattern... $ps ){
            List<Node> after = listNodesAfter(startNode);
            List<Node> allAfter =
                    after.stream().filter(bn -> Arrays.stream($ps).anyMatch($n -> $n.match(bn) ) ).collect(Collectors.toList());
            return allAfter;
        }

        /**
         *
         * @param node
         * @return
         */
        public static Node getParentBody(Node node ){
            Optional<Node> on = node.stream(Node.TreeTraversal.PARENTS).filter(n-> n instanceof BodyDeclaration).findFirst();
            if( on.isPresent() ) {
                return on.get();
            }
            return null;
        }

        /**
         *
         * @param n
         * @return
         */
        public static List<Node> listNodesBefore( Node n ){
            Node  parentNode = getParentBody(n);

            List<Node> nodesBefore = new ArrayList<>();
            if( parentNode == null ){
                return nodesBefore;
            }
            AtomicBoolean reached = new AtomicBoolean(false);

            //walk "Down" until we reach a node that is reached
            parentNode.walk(nn -> {
                if(!reached.get() && nn == n){
                    reached.set(true);
                }
                if( !reached.get()){
                    if(!nn.containsWithinRange(n)) { //dont include "parent" nodes of this node
                        nodesBefore.add(nn);
                    }
                }
            });
            return nodesBefore;
        }

        /**
         *
         * @param n
         * @return
         */
        public static List<Node> listNodesAfter(Node n ){
            Node  parentNode = getParentBody(n);
            List<Node> nodesAfter = new ArrayList<>();
            if( parentNode == null ){
                return nodesAfter;
            }
            AtomicBoolean reached = new AtomicBoolean(false);

            //walk "Down" until we reach a node that is reached, use postorder traversal
            parentNode.walk(Node.TreeTraversal.POSTORDER, nn -> {
                if( reached.get() && nn != n ){
                    if(!nn.containsWithinRange(n)) { //dont include "parent" nodes of this node
                        nodesAfter.add(nn);
                    }
                }
                if(!reached.get() && nn == n){
                    reached.set(true);
                }
            });
            return nodesAfter;
        }

    }

    /**
     * Methods to simplify matching code WITHIN the same BLOCK
     * for example if if have a method:
     * <PRE>
     * class C{
     *     void b4(){
     *         System.out.println(0);
     *     }
     *     void m(){
     *         System.out.println(1);
     *         System.out.println(2);
     *         {
     *             System.out.println(3);
     *             System.out.println(4);
     *             if( true ){
     *                 System.out.println( 5 );
     *             }
     *             HERE : System.out.println(6); //<--- at this Statement
     *             if(1==1){
     *                 System.out.println(7);
     *             }
     *             System.out.println(8);
     *         }
     *         System.out.println(9);
     *     }
     *     void after(){
     *         System.out.println(10);
     *     }
     * }
     * </PRE>
     * <PRE>
     *
     * Statement here = $.labeledStmt().firstIn(C.class); //get the HERE: (labeledStatement)
     * $stmt $anyPrintln = $.stmt.of("System.out.println($any$);");
     *
     * // we find the code BEFORE but in the same Block
     * List<Node> allBefore = $pattern.BlockScope.listAllBefore( here, $anyPrintln);
     * // allBefore contains System.out.println(3); System.out.println(4); System.out.println(5);
     *
     * Statement previous = $pattern.BlockScope.findPrevious( here, $anyPrintln);
     * //previous = System.out.println(5);
     *
     * //find the code AFTER but within the same block
     * List<Node> allAfter = $pattern.BlockScope.listAllAfter( here, $anyPrintln);
     * //allAfter contains System.out.println(7); System.out.println(8);
     *
     * Statement next = $pattern.BlockScope.findNext( here, $anyPrintln);
     * //next = System.out.println(7);
     * </PRE>
     *
     * @see BodyScope
     */
    interface BlockScope {

        /**
         *
         * @param firstNode the "terminal" node, (ONLY NODES AFTER THIS NODE WILL BE TESTED)
         * @param $matchPatterns one or more $patterns to match within the
         * @param <N> the type of node we expect
         * @return
         */
        public static <N extends Node> N findNext(Node firstNode, $pattern...$matchPatterns ){
            List<Node> nodesAfter = listNodesAfter( firstNode );

            Optional<Node> on =
                    nodesAfter.stream().filter(bn -> Arrays.stream($matchPatterns).anyMatch($n -> $n.match(bn) ) ).findFirst();
            if( on.isPresent() ){
                return (N)on.get();
            }
            return null;
        }

        /**
         *
         * @param lastNode the "terminal" node, (ONLY NODES BEFORE THIS WILL BE TESTED)
         * @param $matchPatterns one or more $patterns to match within the
         * @param <N>
         * @return
         */
        public static <N extends Node> N findPrevious(Node lastNode, $pattern...$matchPatterns ){
            List<Node> nodesBefore = listNodesBefore( lastNode );
            //Since we want to find the most previous one closes to the lastNode, we need to
            //reverse the order of the nodesBefore before
            // i.e. if we had :
            // int m(){
            //     i=1;                  //<-- we SHOULD NOT return this statement... it's furthest away
            //     System.out.println(i);
            //     i=2;                  //<-- we should return THIS statement... as it's CLOSEST
            //     System.out.println(i);
            //     assert(true);         //<-- IF this is the lastNode
            // }
            // $stmt $matchIAssignment = $stmt.of("i = $any$;"); //if this is the ONLY matchPattern
            // Statement st = findPrevious(lastNode, $matchIAssignment); //should return "i=2;"

            // THIS IS WHY we reverse the nodesBefore, we want to identify/break as soon as we reach the FIRST match
            Collections.reverse(nodesBefore);
            Optional<Node> on =
                    nodesBefore.stream().filter(bn -> Arrays.stream($matchPatterns).anyMatch($n -> $n.match(bn) ) ).findFirst();
            if( on.isPresent() ){
                return (N)on.get();
            }
            return null;
        }

        /**
         *
         * @param startNode
         * @param $ps
         * @return
         */
        public static List<Node> listAllBefore(Node startNode, $pattern... $ps ) {
            return listAllBefore( startNode, Node.class, $ps);
        }

        public static <N extends Node> List<N> listAllBefore(Node startNode, Class<N> nodeType, $pattern... $ps ){
            List<N> before = listNodesBefore(nodeType, startNode);
            List<N> allBefore =
                    before.stream().filter(bn -> Arrays.stream($ps).anyMatch($n -> $n.match(bn) ) ).collect(Collectors.toList());
            return allBefore;
        }

        /**
         *
         * @param startNode
         * @param $ps
         * @return
         */
        public static List<Node> listAllAfter(Node startNode, $pattern... $ps ) {
            return listAllAfter(startNode, Node.class, $ps);
        }

        public static <N extends Node> List<N> listAllAfter(Node startNode, Class<N> nodeType, $pattern...$ps){
            List<N> after = listNodesAfter(startNode, nodeType);
            List<N> allAfter =
                    after.stream().filter(bn -> Arrays.stream($ps).anyMatch($n -> $n.match(bn) ) ).collect(Collectors.toList());
            return allAfter;
        }

        /**
         *
         * @param node
         * @return
         */
        public static Node getParentBlock(Node node ){
            //List<Node> matched = new ArrayList<>();
            Optional<Node> on = node.stream(Node.TreeTraversal.PARENTS).filter(n-> n instanceof NodeWithStatements).findFirst();
            if( on.isPresent() ) {
                return on.get();
            }
            return null;
        }

        /**
         *
         * @param n
         * @return
         */
        public static List<Node> listNodesBefore( Node n ) {
            return listNodesBefore( Node.class, n);
        }

        public static<N extends Node> List<N> listNodesBefore( Class<N> nodeType, Node n){
            Node parentBlock = getParentBlock(n);

            List<N> nodesBefore = new ArrayList<>();
            if( parentBlock == null ){
                return nodesBefore;
            }
            AtomicBoolean reached = new AtomicBoolean(false);

            //walk "Down" until we reach a node that is reached
            parentBlock.walk(nn -> {
                if(!reached.get() && nn == n){
                    reached.set(true);
                }
                if( !reached.get()){
                    if(!nn.containsWithinRange(n)) { //dont include "parent" nodes of this node
                        if( nodeType.isAssignableFrom(nn.getClass())) {
                            nodesBefore.add( (N)nn);
                        }
                    }
                }
            });
            return nodesBefore;
        }

        /**
         *
         * @param n
         * @return
         */
        public static List<Node> listNodesAfter(Node n ) {
            return listNodesAfter(n, Node.class);
        }

        public static <N extends Node> List<N> listNodesAfter(Node n, Class<N>nodeType){
            Node parentBlock = getParentBlock(n);
            List<N> nodesAfter = new ArrayList<>();
            if( parentBlock == null ){
                return nodesAfter;
            }
            AtomicBoolean reached = new AtomicBoolean(false);

            //walk "Down" until we reach a node that is reached
            parentBlock.walk(Node.TreeTraversal.POSTORDER, nn -> {
                if( nn == n ){
                    //System.out.println( "FOUND AT "+ nn );
                    reached.set(true);
                }
                else if( reached.get() && !nn.containsWithinRange(n)) { //dont include "parent" nodes of this node
                    //System.out.println( "ADDING "+ nn );
                    if(nodeType.isAssignableFrom(nn.getClass() )) {
                        nodesAfter.add((N)nn);
                    }
                }
            });
            return nodesAfter;
        }
    }

    /**
     *
     */
    public static class $exception extends _jdraftException {
        public $exception(String message, Throwable throwable){
            super(message, throwable);
        }

        public $exception(String message){
            super(message);
        }

        public $exception(Throwable throwable){
            super(throwable);
        }
    }

}
