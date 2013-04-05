/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A composite is an expression that contains multiple terms
 *
 * @author Paul Wolfgang
 */
public abstract class  Composite implements Expression {

    protected List<Expression> terms;

    /**
     * Add a term
     *
     * @param e the term to be added
     */
    public void addTerm(Expression e) {
        if (e != null) {
            if (terms == null) {
                terms = new ArrayList<>();
            }
            if (this.getClass() == e.getClass()) { // Both same kind of Composite
                Composite composite = (Composite) e;
                terms.addAll(composite.getTerms());
            } else {
                terms.add(e);
            }
        }
    }

    /**
     * Determine if composite is empty
     */
    public boolean isEmpty() {
        return terms == null || terms.isEmpty();
    }

    /**
     * Get the list of terms
     */
    public List<Expression> getTerms() {
        return terms;
    }
    
    public boolean isEmptyExpression() {
        return terms == null;
    }

    /**
     * Return a string representation. If there are no terms, return an empty
     * string If there is only one term, return that term Otherwise return the
     * terms separated by operator
     * @param The operator
     * @noParen if true, then surrounding parentheses are not applied.
     * @return
     */
    public String toString(String operator, boolean noParen) {
        if (terms == null) {
            return "";
        } else {
            for (Iterator<Expression> itr = terms.iterator(); itr.hasNext();) {
                Expression e = itr.next();
                if (e.isEmptyExpression()) {
                    itr.remove();
                }
            }
            if (terms.size() == 1) {
                return terms.get(0).toString();
            } else {
                StringBuilder sb = new StringBuilder();
                if (!noParen) {
                    sb.append("(");
                }
                sb.append(terms.get(0));
                for (int i = 1; i < terms.size(); i++) {
                    sb.append(operator);
                    sb.append(terms.get(i));
                }
                if (!noParen) {
                    sb.append(")");
                }
                return sb.toString();
            }
        }
    }
    
}
