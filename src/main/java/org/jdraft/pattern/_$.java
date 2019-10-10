package org.jdraft.pattern;

import org.jdraft._draftException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that contains "key value" data for performing parameterizations {@link $pattern#$(String, String)}
 * {@link $method}
 * {@link $constructor}
 * {@link $class}
 * {@link $enum}
 * {@link $interface}
 * {@link $annotation}
 *
 * This is a "specific" macro annotation that we DOES NOT run through
 * the normal macro process, but rather post-parameterizes a model
 * after construction (the OUTPUT is a $pattern implementation and not
 * a _meta_model)
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
public @interface _$ {

    String[] value();
}
