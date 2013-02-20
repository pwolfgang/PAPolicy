/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite is an expression that contains multiple terms
 * @author Paul Wolfgang
 */
public class Composite implements Expression {
    
    protected List<Expression> terms;

    
    /**
     * Add a term
     * @param e the term to be added
     */
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
    public String toString(String operator) {
        if (terms == null) {
            return "";
        } else if (terms.size() == 1) {
            return terms.get(0).toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(terms.get(0));
            for (int i = 1; i < terms.size(); i++) {
                sb.append(operator);
                sb.append(terms.get(i));
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
