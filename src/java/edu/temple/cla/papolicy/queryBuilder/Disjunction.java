package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A disjunction is a composite whose terms are separated by OR
 * @author Paul Wolfgang
 */
public class Disjunction extends Composite {

    /**
     * Return a string representation.
     * If there are no terms, return an empty string
     * If there is only one term, return that term
     * Otherwise return the terms separated by OR
     * @return 
     */
    @Override
    public String toString() {
        return toString(" OR ");
    }

}
