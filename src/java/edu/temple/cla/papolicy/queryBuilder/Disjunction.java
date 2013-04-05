package edu.temple.cla.papolicy.queryBuilder;

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
        return toString(" OR ", false);
    }
    
    /**
     * Disjunctions always need to be surrounded with parentheses
     * @return The same result as toString.
     */
    @Override
    public String toStringNoParen() {
        return toString();
    }

}
