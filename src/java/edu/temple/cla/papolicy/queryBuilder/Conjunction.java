package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A conjunction is a composite whose terms are separated by AND
 * @author Paul Wolfgang
 */
public class Conjunction extends Composite {
        
    /**
     * Return a string representation.
     * If there are no terms, return an empty string
     * If there is only one term, return that term
     * Otherwise return the terms separated by OR
     * @return 
     */
    @Override
    public String toString() {
        return toString(" AND ");
    }

}
