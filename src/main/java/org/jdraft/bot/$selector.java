package org.jdraft.bot;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Tokens;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Entity capable of selection which entails both
 * <UL>
 *     <LI>determining if a "node" meets a criteria (usually via a {@link Predicate})</LI>
 *     <LI>extracting parameters within the text of the {@link _tree._node} via a {@link Stencil}</LI>
 * </UL>
 * @param <_S> the candidate type being selected (i.e. a {@link $methodCallExpr} -> {@link _methodCallExpr}
 * @param <$S> the underlying selector type  to "return itself modified" (i.e. {@link $methodCallExpr} returns {@link $methodCallExpr}
 */
public interface $selector<_S, $S> extends Function<_S, Tokens> {

    /** return a copy*/
    $S copy();

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

    @Override
    default Tokens apply(_S sel) {
        Select s = select(sel);
        if( s != null ){
            return s.tokens;
        }
        return null;
    }

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
     * @param predicate a new functional matching constraint to the prototype
     * @return the modified $selector
     */
    default $S $and(Predicate<_S> predicate){
        setPredicate( getPredicate().and(predicate));
        return ($S)this;
    }

    /**
     * --Predicate updater -- (i.e. updates constraints on the prototype and returns the modified prototype)
     *
     * Add a (NOT) matching constraint to add to the $prototype
     * @param predicate a constraint to be negated and added (via and) to the constraint
     * @return the modified $selector
     */
    default $S $not(Predicate<_S> predicate) {
        return $and( predicate.negate() );
    }

    /**
     * Specify a like-kind of $S (selector) that will exclude selections within the predicate
     * i.e.
     * <PRE>
     * //match all types except "void"
     * $typeRef.of().$not($typeRef.VOID);
     * </PRE>
     *
     * @param $sel a $S instance selector for describing matches to NOT be selected
     * @return the modified $S

    $S $not( $S $sel );
    */

    /**
     * Specify many like-kind of $S (selectors) that to exclude from selection
     * i.e.
     * <PRE>
     * //match all types except "void" and Double
     * $typeRef.of().$not($typeRef.VOID, $typeRef.of(Double.class) );
     * </PRE>
     *
     * @param $sels a list of $S instance selector for describing matches to be excluded
     * @return the modified $S
     */
    public $S $not( $S... $sels );
    //{
    //    return $not( t-> Stream.of($sels).anyMatch( $s -> (($bot)$s).matches(t) ) );
    //}

    /*--------------------------- Position/Range(Row, Column) Aware Criteria-----------------------------------------*/

    /**
     * Is the textual range of this _node
     * @param _n
     * @return
     */
    default $S $isInRange(_tree._node _n ){
        return $isInRange(_n.ast());
    }

    /**
     * Is the range of this Ast Node
     * @param n
     * @return
     */
    default $S $isInRange(Node n){
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
            } else if (n instanceof _tree._node) {
                Node node = ((_tree._node)n).ast();
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
    default $S $isInLineRange(int beginLine, int endLine){
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

    /*------------------------------------- AST Tree Aware Criteria-----------------------------------------*/
    /**
     *
     * @param _codeUnitMatchFn
     * @return
     */
    default $S $isInCodeUnit( Predicate<_codeUnit> _codeUnitMatchFn){
        return $and( _n -> {
            Optional<CompilationUnit> oc = ((_tree._node)_n).ast().findCompilationUnit();
            if( !oc.isPresent() ){
                return false;
            }
            return _codeUnitMatchFn.test( _codeUnit.of(oc.get()) );
        } );
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
            Optional<CompilationUnit> ocu = ((_tree._node)n).ast().findCompilationUnit();
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
            Optional<CompilationUnit> ocu = ((_tree._node)n).ast().findCompilationUnit();
            if( ocu.isPresent() ){
                _imports _is = _imports.of(ocu.get());
                return _importsMatchFn.test(_is);
            }
            return false;
        });
    }

    /**
     * Verifies that the candidates' containing type matches one of the provided $typeSelectors
     * @param $typeSelectors selector for the type
     * @return the modified $selector
     */
    default $S $isInType($selector.$node...$typeSelectors ){
        return $isInType( _tp -> Arrays.stream($typeSelectors).anyMatch( $ts -> $ts.matches(_tp)));
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
            Node node = ((_tree._node)n).ast();
            Optional<Node> containingType = node.stream(Walk.PARENTS).filter(p-> p instanceof TypeDeclaration
                    && p.getParentNode().isPresent()
                    && !(p.getParentNode().get() instanceof ObjectCreationExpr)).findFirst();

            if( !containingType.isPresent()){
                return false;
            }
            _type _t = _type.of((TypeDeclaration)containingType.get());
            return _typeMatchFn.test(_t);
        });
    }

    /**
     *
     * @param _memberClasses
     * @return
     */
    default $S $isInMember(Class<? extends _java._member>..._memberClasses){
        return $isInMember( _tp -> Arrays.stream(_memberClasses).anyMatch(_tc -> _tc.isAssignableFrom(_tp.getClass())));
    }

    /**
     * Provide a $selectors ($method, $constructor, $field, ...) to test against the containing member of the candidate
     *
     * @param $selectors the selectors to match against the containing member
     * @return
     * @see _java._member
     */
    default $S $isInMember($selector.$node... $selectors){
        return $isInMember(m-> Arrays.stream($selectors).anyMatch($es-> $es.matches(m)));
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
            Node node = ((_tree._node)n).ast();
            Optional<Node> containingMember = node.stream(Walk.PARENTS).filter(p-> p instanceof BodyDeclaration
                    && p.getParentNode().isPresent()
                    && !(p.getParentNode().get() instanceof ObjectCreationExpr)).findFirst();

            if( !containingMember.isPresent()){
                return false;
            }
            _java._member _m = (_java._member)_java.of((BodyDeclaration)containingMember.get());
            return _memberMatchFn.test(_m);
        });
    }

    default $S $isParent(Class... parentClassTypes ){
        return $and(n -> {
            if (n instanceof Node) {
                return Walk.isParent( (Node)n, parentClassTypes);
            } else if (n instanceof _tree._node) {
                return Walk.isParent( ((_tree._node)n).ast(), parentClassTypes);
            } else if (n instanceof _body) {
                return Walk.isParent( ((_body)n).ast(), parentClassTypes);
            } else if( n instanceof _variable){
                // I NEED _java.isParent()
                return Walk.isParent( ((_variable)n).ast(), parentClassTypes);
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
     * @param $selectors
     * @return
     */
    default $S $isParentNot($selector.$node ... $selectors){
        for(int i=0;i<$selectors.length; i++) {
            $selector $b = $selectors[i];
            Predicate<_S> pp = n -> {
                if (n instanceof Node) {
                    return Walk.isParent((Node) n, c -> $b.matches(c));
                } else if (n instanceof _tree._node) {
                    return Walk.isParent(((_tree._node) n).ast(), c -> $b.matches(c));
                } else if (n instanceof _body) {
                    return Walk.isParent(((_body) n).ast(), c -> $b.matches(c));
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
    default $S $isParent(Predicate<_tree._node> _parentMatchFn){
        return $and(n -> {
            if (n instanceof _tree._node) {
                if( ((_tree._node)n).ast().getParentNode().isPresent()){
                    return _parentMatchFn.test( (_tree._node)_java.of(((_tree._node)n).ast().getParentNode().get()) );
                }
                return false;
            } else if (n instanceof _body) {
                _body _b = (_body)n;
                return _parentMatchFn.test( (_tree._node)_java.of(_b.ast().getParentNode().get()) );
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
     * Adds a constraint to test that the parent of the candidate matches any one of the $selectors
     * @param $selectors the $seelctors to match against the candidates parent node
     * @return
     */
    default $S $isParent($selector.$node... $selectors ){
        return $and(n -> {
            if (n instanceof Node) {
                return Walk.isParent( (Node)n, c-> Arrays.stream($selectors).anyMatch($b->$b.matches(c)) );
            } else if (n instanceof _tree._node) {
                return Walk.isParent( ((_tree._node)n).ast(), c->Arrays.stream($selectors).anyMatch($b->$b.matches(c)) );
            } else if (n instanceof _body) {
                return Walk.isParent( ((_body)n).ast(), c->Arrays.stream($selectors).anyMatch($b->$b.matches(c)) );
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
     * Does the candidate have an ancestor (UP the AST tree) that is any one of these _classes
     * (includes interfaces, like {@link _stmt._controlFlow})
     *
     * @param _nodeClasses
     * @return
     */
    default $S  $hasAncestor( int levels, Class<? extends _java._domain>... _nodeClasses){
        return $hasAncestor( levels, _tp -> Arrays.stream(_nodeClasses).anyMatch( _tc -> _tc.isAssignableFrom(_tp.getClass())));
    }

    /**
     * Does this class have an ancestor that is any one of these _classes (includes _interfaces, like
     * {@link _stmt._controlFlow})
     *
     * @param _nodeClasses
     * @return
     */
    default $S $hasAncestor( Class<? extends _java._domain>... _nodeClasses){
        return $hasAncestor( _tp -> Arrays.stream(_nodeClasses).anyMatch( _tc -> _tc.isAssignableFrom(_tp.getClass())));
    }

    /**
     *
     * @param _ancestorMatchFn
     * @return
     */
    default $S $hasAncestor( Predicate<_tree._node> _ancestorMatchFn ){
        return $hasAncestor(Integer.MAX_VALUE -100, _ancestorMatchFn);
    }

    /**
     *
     * @param levels
     * @param _ancestorMatchFn
     * @return
     */
    default $S $hasAncestor( int levels, Predicate<_tree._node> _ancestorMatchFn){
        return $and(n-> {
            if (n instanceof _tree._node) {
                return ((_tree._node)n).ast().stream(Walk.PARENTS).limit(levels).anyMatch(c-> _ancestorMatchFn.test( (_tree._node)_java.of(c) ) );
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
    default $S $hasAncestor( int levels, $selector.$node... $selectors ){
        return $and(n-> {
            if (n instanceof _tree._node) {
                return ((_tree._node)n).ast().stream(Walk.PARENTS).limit(levels).anyMatch(c-> Arrays.stream($selectors).anyMatch($b ->$b.matches(c)));
            } else if (n instanceof _body) {
                return ((_body)n).ast().stream(Walk.PARENTS).limit(levels).anyMatch(c-> Arrays.stream($selectors).anyMatch($b ->$b.matches(c)));
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

    default $S $hasDescendant( Class<? extends _java._domain>...domainClasses ){
        return $hasDescendant( n-> Arrays.stream(domainClasses).anyMatch(c-> c.isAssignableFrom(n.getClass())));
    }

    /**
     *
     * @param _descendantMatchFn
     * @return
     */
    default $S $hasDescendant( Predicate<_tree._node> _descendantMatchFn){
        return $and(n-> {
            if (n instanceof _tree._node) {
                return ((_tree._node)n).ast().stream(Walk.PRE_ORDER).skip(1).anyMatch(d-> _descendantMatchFn.test( (_tree._node)_java.of(d)) );
            } else if (n instanceof _body) {
                return ((_body)n).ast().stream(Walk.PRE_ORDER).skip(1).anyMatch(d-> _descendantMatchFn.test( (_tree._node)_java.of(d)) );
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
     * If I walk "DOWN" the AST of the candidate can I find a descendant (child, grandchild,...)
     * that matches ANY one of the $selectors provided.
     *
     * @param $selectors bots that may match against ancestors
     * @return the modified bot
     */
    default $S $hasDescendant( $selector.$node... $selectors ){
        return $and(n-> {
            if (n instanceof _tree._node) {                        //skip yourself (only children)
                return ((_tree._node)n).ast().stream(Walk.PRE_ORDER).skip(1).anyMatch(c-> Arrays.stream($selectors).anyMatch($b ->$b.matches( c)) );
            } else if (n instanceof _body) {                   //skip yourself (only children)
                return ((_body)n).ast().stream(Walk.PRE_ORDER).skip(1).anyMatch(c-> Arrays.stream($selectors).anyMatch($b ->$b.matches(c)));
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
     * @param <_S>
     * @param <$S>
     */
    interface $node<_S, $S> extends $selector<_S, $S>, Function<_S, Tokens> {

        Select<_S> select(String... code);

        default Select<_S> select(String code){
            return select( new String[]{code});
        }

        Select<_S> select(Node n);

        default Select<_S> select(_tree._node _jn ){
            try{
                return select( (_S) _jn );
            }catch(Exception e){
                return null;
            }
        }

        default Tokens apply(_S _toSelect){
            Select<_S> s = select(_toSelect);
            if( s == null ){
                return null;
            }
            return s.tokens;
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


    /**
     *
     */
    interface $orSelect<$S extends $selector, $O extends $orSelect> {

        /**
         * lists the individual Or {@link $bot}s that make up the $orBot
         * @return
         */
        List<$S> $listOrSelectors();

        default $O forOrSelectors(Consumer<$S> $botActionFn ){
            $listOrSelectors().forEach($botActionFn);
            return ($O) this;
        }
    }
}
