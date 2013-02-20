package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A conjunction is a composite whose terms are separated by AND
 * @author Paul Wolfgang
 */
public class Conjunction implements Composite {
    
    private List<Expression> terms;
    
    /**
     * Add a term
     * @param e the term to be added
     */
    @Override
    public void addTerm(Expression e) {
        if (terms == null) {
            terms = new ArrayList<>();
        }
        terms.add(e);
    }
    
    /**
     * Return a string representation.
     * If there are no terms, return an empty string
     * If there is only one term, return that term
     * Otherwise return the terms separated by OR
     * @return 
     */
    @Override
    public String toString() {
        if (terms == null) {
            return "";
        } else if (terms.size() == 1) {
            return terms.get(0).toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(terms.get(0));
            for (int i = 1; i < terms.size(); i++) {
                sb.append(" AND ");
                sb.append(terms.get(i));
            }
            sb.append(")");
            return sb.toString();
        }
    }

}
