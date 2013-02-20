/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.queryBuilder;

/**
 * A composite is an expression that contains multiple terms
 * @author Paul Wolfgang
 */
public interface Composite extends Expression {
    
    /**
     * Add a term
     * @param e the term to be added
     */
    void addTerm(Expression e);
    
}
