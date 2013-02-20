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
    public String toString();
    
}
