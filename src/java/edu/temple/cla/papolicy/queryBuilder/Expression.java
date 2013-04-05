/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.queryBuilder;

/**
 * An expression is a logical expression used in the WHERE clause
 * @author Paul Wolfgang
 */
public interface Expression {
    
    /**
     * Return a string representation of the expression
     * @return a string representation of the expression
     */
    @Override
    String toString();
    
    /**
     * Return a string representation of the expression without surrounding
     * parentheses
     * @return A string representation without surrounding parentheses.
     */
    String toStringNoParen();
    
    boolean isEmptyExpression();
    
}
