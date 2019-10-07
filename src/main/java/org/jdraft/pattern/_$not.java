package org.jdraft.pattern;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Runtime Marker interface to apply to BodyDeclaration/_member(s) to
 * be part of a $not pattern
 * <PRE>
 * for example:
 *
 * //$pattern to Match TestCase classes that have a "setUp()" method but no "tearDown()" method.
 * //    NOTE: we don't know (care or match on) the CONTENTS of the setUp and/or tearDown methods
 * //    we just want to check for existence and NON-existence
 * $class $setUpNoTearDown = $class.of( new TestCase(){
 *     public void setUp(){}
 *
 *     @_$not
 *     public void tearDown(){}
 * });
 * </PRE>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface _$not {
}
