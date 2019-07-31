package org.jdraft;

import java.util.Map;

/**
 * An entity made of constituent "parts" that can be broken down into
 * individual components.
 * 
 * NOTE: 
 * <PRE>
 * for example: 
 * {@link draft.java._class} which can "deconstructed" into
 * {@link draft.java._field}s, {@link draft.java._method}, ... etc. 
 * </PRE>
 * 
 * @see draft.java._class#deconstruct() for an example 
 * @author Eric
 */
public interface Composite {
    
   /**
    * build a Map consisting of the Named components that make up the Composite
    * @return a map containing the named components of the composite
    */ 
   Map<String,Object> deconstruct();
   
}
