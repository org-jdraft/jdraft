package org.jdraft.bot;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.jdraft.*;
import org.jdraft.pattern.$;
import org.jdraft.text.Stencil;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Entity capable of selection which entails both
 * <UL>
 *     <LI>determining if a "node" meets a criteria</LI>
 *     <LI>extracting parameters within the text of the {@link _java._node}</LI>
 * </UL>
 * @param <_S>
 * @param <$S>
 */
public interface $selector<_S, $S> {

    /** matching function to match against a candidate (usually defaults to (t)->true to match all) */
    Predicate<_S> getPredicate();

    /**
     * REPLACES the existing predicate/matching function, note although this is available
     * most of the time you'll want to mutate the existing predicate with $and() or $not()
     * @see #$and(Predicate)
     * @see #$not(Predicate)
     */
    $S setPredicate( Predicate<_S> predicate);

    /**
     * Return a Select if the candidate matches
     * (including the candidate and tokens extracted)
     *
     * @param candidate
     * @return
     */
    Select<_S> select(_S candidate);

    /**
     * Does this textual representation match the normalized AST version?
     * @param candidate
     * @return
     */
    boolean matches(String candidate);

    /**
     *
     * @param candidate
     * @return
     */
    default boolean matches(_S candidate){
        return select(candidate) != null;
    }

    /**
     * Does this match ANY input (INCLUDING NULL or no input at all)
     * in other words if an entity was found that PASSED the isMatchAny function it can be considered
     * ENTIRELY OPTIONAL (and or even null)
     * @return
     */
    boolean isMatchAny();

    /**
     * --Predicate Updater -- (i.e. updates/adds a new Predicate constraint to the existing predicate constraints)
     *
     * Update the matchFn with a new MatchFn that will be "ANDED" to the existing matchFn
     * @param matchFn a new functional matching constraint to the prototype
     * @return the modified $selector
     */
    default $S $and(Predicate<_S> matchFn){
        setPredicate( getPredicate().and(matchFn));
        return ($S)this;
    }

    /**
     * --Constraint updater -- (i.e. updates constraints on the prototype and returns the modified prototype)
     *
     * Add a (NOT) matching constraint to add to the $prototype
     * @param matchFn a constraint to be negated and added (via and) to the constraint
     * @return the modified $selector
     */
    default $S $not(Predicate<_S> matchFn) {
        return $and( matchFn.negate() );
    }


    /**
     * Is the range of this Node
     * @param _n
     * @return
     */
    default $S $isInRange(_java._node _n ){
        return $isInRange(_n.ast());
    }

    /**
     * Is the range of this Node
     * @param n
     * @return
     */
    default $S $isInRange(Node n){ ;
        if( n.getRange().isPresent() ){
            return $isInRange(n.getRange().get());
        }
        throw new _jdraftException("Node "+n+" does not have a range");
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param range
     * @return
     */
    default $S $isInRange(final Range range ){
        Predicate<_S> pp = n -> {
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
    default $S $isInRange(int beginLine, int beginColumn, int endLine, int endColumn){
        return $isInRange(Range.range(beginLine,beginColumn,endLine, endColumn));
    }

    /**
     * Adds a constraint that the instance occurs will be within the Range
     * @param beginLine
     * @param endLine
     * @return
     */
    default $S $isInRange(int beginLine, int endLine){
        return $isInRange(Range.range(beginLine,0,endLine, Integer.MAX_VALUE -10000));
    }

    /**
     * Verifies that the ENTIRE matching pattern exists on this specific line in the code
     * @param line the line expected
     * @return the modified $pattern
     */
    default $S $isAtLine( int line ){
        return $isInRange(Range.range(line,0,line, Integer.MAX_VALUE -10000));
    }




    /**
     * does the candidate match the name of the package?
     * NOTE: supports $variables$ i.e. .$isInPackage("org.jdraft.$any$")
     * @param packageNameStencil a specific name (i.e. "java.util" or a parameterizedName "$any$.daos")
     * @return
     */
    default $S $isInPackage(String packageNameStencil){
        //this will "normalize" the stencil
        _package _pkg = _package.of(packageNameStencil);
        String normalizedPackageName = _pkg.toString().trim();

        //lets make this a Stencil so we can match via a stencil (if the stencil has no parameters (is fixed text) it'll match as well)
        Stencil st = Stencil.of(normalizedPackageName);
        return $isInPackage( p-> st.matches( p.toString().trim()));
    }

    /**
     *
     * @param packageMatchFn
     * @return
     */
    default $S $isInPackage(Predicate<_package> packageMatchFn){
        return $and(n -> {
            Optional<CompilationUnit> ocu = ((_java._node)n).ast().findCompilationUnit();
            if( ocu.isPresent() && ocu.get().getPackageDeclaration().isPresent() ){
                _package _p = _package.of(ocu.get().getPackageDeclaration().get());
                return packageMatchFn.test(_p);
            }
            return false;
        });
    }

    /**
     * candidates must have imported the given classes
     * @param classNames
     * @return
     */
    default $S $isImports( String...classNames){
        return $isImports( is-> is.hasImports(classNames));
    }

    /**
     * candidates must have imported the given classes
     * @param clazzes
     * @return
     */
    default $S $isImports( Class...clazzes){
        return $isImports( is-> is.hasImports(clazzes));
    }

    /**
     * Adds a constraint for matching candidates who are defined in compilationUnits
     * that have matching _imports
     *
     * in simple terms: Does this Node/thing exist in a Class file that matches these imports
     *
     * @param _importsMatchFn function to match imports
     * @return the modified $bot instance with the constraint added
     */
    default $S $isImports(Predicate<_imports> _importsMatchFn){
        return $and(n -> {
            Optional<CompilationUnit> ocu = ((_java._node)n).ast().findCompilationUnit();
            if( ocu.isPresent() ){
                _imports _is = _imports.of(ocu.get());
                return _importsMatchFn.test(_is);
            }
            return false;
        });
    }

    /**
     * Does the candidate's most immediate "container" or {@link _java._member} match the
     *
     * $isInLambda()
     * $isInAnonymousClass()
     *
     * @param _memberMatchFn
     * @return
     */
    default $S $isInMember(Predicate<_java._member> _memberMatchFn){
        return $and(n -> {
            Node node = ((_java._node)n).ast();
            Optional<Node> containingMember = node.stream(Tree.PARENTS).filter(p-> p instanceof BodyDeclaration
                    && p.getParentNode().isPresent()
                    && !(p.getParentNode().get() instanceof ObjectCreationExpr)).findFirst();

            if( !containingMember.isPresent()){
                return false;
            }
            _java._member _m = (_java._member)_java.of((BodyDeclaration)containingMember.get());
            return _memberMatchFn.test(_m);
        });
    }

    default $S $isInMember(Class<? extends _java._member>..._memberClasses){
        return $isInMember( _tp -> Arrays.stream(_memberClasses).anyMatch(_tc -> _tc.isAssignableFrom(_tp.getClass())));
    }

    /**
     * Verifies that
     * @param _typeClasses
     * @return
     */
    default $S $isInType(Class<? extends _type>..._typeClasses ){
        return $isInType( _tp -> Arrays.stream(_typeClasses).anyMatch( _tc -> _tc.isAssignableFrom(_tp.getClass())));
    }

    /**
     * Gets the containing {@link _type} ({@link _class}, {@link _interface}, {@link _enum}, {@link _annotation})
     * of the candidate and checks if it matches the _typeMatchFn
     * NOTE: for anonymous classes, i.e. tests the parent of the anonymous Class
     * @param _typeMatchFn matches against the _type the candidate is contained within
     * @return the modified $bot
     */
    default $S $isInType(Predicate<_type> _typeMatchFn){
        return $and(n -> {
            Node node = ((_java._node)n).ast();
            Optional<Node> containingType = node.stream(Tree.PARENTS).filter( p-> p instanceof TypeDeclaration
                    && p.getParentNode().isPresent()
                    && !(p.getParentNode().get() instanceof ObjectCreationExpr)).findFirst();

            if( !containingType.isPresent()){
                return false;
            }
            _type _t = _type.of((TypeDeclaration)containingType.get());
            return _typeMatchFn.test(_t);
        });
    }



    default $S $isParent(Class... parentClassTypes ){
        return $and(n -> {
            if (n instanceof Node) {
                return Tree.isParent( (Node)n, parentClassTypes);
            } else if (n instanceof _java._node) {
                return Tree.isParent( ((_java._node)n).ast(), parentClassTypes);
            } else if (n instanceof _body) {
                return Tree.isParent( ((_body)n).ast(), parentClassTypes);
            } else if( n instanceof _variable){
                // I NEED _java.isParent()
                return Tree.isParent( ((_variable)n).ast(), parentClassTypes);
            }
            //NEED TO MANUALLY IMPLEMENT FOR:
            // $parameters, $annos, $snip, $throws, $typeParameters
            // if( n instanceof List ){
            //    List l = (List)n;
            //    l.forEach();
            // }
            throw new _jdraftException("Not implemented yet for type : " + n.getClass());

        });
    }

    /**
     * Adds a constraint to test that the PARENT does NOT MATCH ANY of the bots provided
     * <PRE>
     *     class
     * </PRE>
     *
     * @param $bs
     * @return
     */
    default $S $isParentNot($bot.$node ... $bs){
        for(int i=0;i<$bs.length; i++) {
            $bot $b = $bs[i];
            Predicate<_S> pp = n -> {
                if (n instanceof Node) {
                    return Tree.isParent((Node) n, c -> $b.matches(c));
                } else if (n instanceof _java._node) {
                    return Tree.isParent(((_java._node) n).ast(), c -> $b.matches(c));
                } else if (n instanceof _body) {
                    return Tree.isParent(((_body) n).ast(), c -> $b.matches(c));
                }
                return false;
            };
            $not(pp);
        }
        return ($S)this;
    }

    /**
     * Check if the parent of the candidate matches the _parentMatchFn
     * @param _parentMatchFn
     * @return
     */
    default $S $isParent(Predicate<_java._node> _parentMatchFn){
        return $and(n -> {
            if (n instanceof _java._node) {
                if( ((_java._node)n).ast().getParentNode().isPresent()){
                    return _parentMatchFn.test( (_java._node)_java.of(((_java._node)n).ast().getParentNode().get()) );
                }
                return false;
            } else if (n instanceof _body) {
                _body _b = (_body)n;
                return _parentMatchFn.test( (_java._node)_java.of(_b.ast().getParentNode().get()) );
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
    }

    /**
     * Adds a constraint to test that the parent of the instance Is this node the child of a ?
     * @param $bs the prototypes to match against
     * @return
     */
    default $S $isParent($selector... $bs ){
        return $and(n -> {
            if (n instanceof Node) {
                return Tree.isParent( (Node)n, c-> Arrays.stream($bs).anyMatch($b->$b.matches(c)) );
            } else if (n instanceof _java._node) {
                return Tree.isParent( ((_java._node)n).ast(), c->Arrays.stream($bs).anyMatch($b->$b.matches(c)) );
            } else if (n instanceof _body) {
                return Tree.isParent( ((_body)n).ast(), c->Arrays.stream($bs).anyMatch($b->$b.matches(c)) );
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

    /**
     * Does this class have an ancestor that is any one of these _classes (includes _interfaces, like
     * {@link _statement._controlFlow})
     *
     * @param _nodeClasses
     * @return
     */
    default $S $hasAncestor( int levels, Class<? extends _java._domain>... _nodeClasses){
        return $hasAncestor( levels, _tp -> Arrays.stream(_nodeClasses).anyMatch( _tc -> _tc.isAssignableFrom(_tp.getClass())));
    }


    /**
     * Does this class have an ancestor that is any one of these _classes (includes _interfaces, like
     * {@link _statement._controlFlow})
     *
     * @param _nodeClasses
     * @return
     */
    default $S $hasAncestor( Class<? extends _java._domain>... _nodeClasses){
        return $hasAncestor( _tp -> Arrays.stream(_nodeClasses).anyMatch( _tc -> _tc.isAssignableFrom(_tp.getClass())));
    }

    /**
     *
     * @param ancestorMatchFn
     * @return
     */
    default $S $hasAncestor( Predicate<_java._node> ancestorMatchFn ){
        return $hasAncestor(Integer.MAX_VALUE -100, ancestorMatchFn);
    }

    /**
     *
     * @param levels
     * @param _ancestorMatchFn
     * @return
     */
    default $S $hasAncestor( int levels, Predicate<_java._node> _ancestorMatchFn){
        return $and(n-> {
            if (n instanceof _java._node) {
                return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch(c-> _ancestorMatchFn.test( (_java._node)_java.of(c) ) );
            } else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }

    /**
     * If I walk "UP" the AST of the candidate can I find an ancestor that matches one of the
     * $bots provided.
     *
     * @param levels how many parent node levels to walk before stopping
     * @param $selectors bots that may match against ancestors
     * @return the modified bot
     */
    default $S $hasAncestor( int levels, $selector... $selectors ){
        return $and(n-> {
            if (n instanceof _java._node) {
                return ((_java._node)n).ast().stream($.PARENTS).limit(levels).anyMatch(c-> Arrays.stream($selectors).anyMatch($b ->$b.matches(c)));
            } else if (n instanceof _body) {
                return ((_body)n).ast().stream($.PARENTS).limit(levels).anyMatch( c-> Arrays.stream($selectors).anyMatch($b ->$b.matches(c)));
            } else{
                //NEED TO MANUALLY IMPLEMENT FOR:
                // $parameters, $annos, $throws, $typeParameters
                // if( n instanceof List ){
                //    List l = (List)n;
                //    l.forEach();
                // }
                throw new _jdraftException("Not implemented yet for type : "+ n.getClass());
            }
        } );
    }


    /**
     * A Selector that selects a single AST _node type of thing
     *
     * @param <_N>
     * @param <$N>
     */
    interface $node<_N, $N> extends $selector<_N, $N> {

        Select<_N> select(String... code);

        default Select<_N> select(String code){
            return select( new String[]{code});
        }

        Select<_N> select(Node n);

        default Select<_N> select(_java._node _jn ){
            try{
                return select( (_N) _jn );
            }catch(Exception e){
                return null;
            }
        }

        default boolean matches(String...code){
           return select(code) != null;
        }

        default boolean matches(String code){
            return select(code) != null;
        }

        default boolean matches(Node n) {
            return select(n) != null;
        }
    }

}
