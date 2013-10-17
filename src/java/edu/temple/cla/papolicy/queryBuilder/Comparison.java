package edu.temple.cla.papolicy.queryBuilder;

/**
 * A Comparison compares two values.
 * @author Paul Wolfgang
 */
public class Comparison implements Expression {
    
    private String lhs;
    private String operator;
    private String rhs;
    
    /**
     * Construct a Comparison
     * @param lhs The left hand value
     * @param operator The operator
     * @param rhs The right hand value
     */
    public Comparison(String lhs, String operator, String rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }
    
    /**
     * Return the expression as a string
     * @return lhs operator rhs
     */
    @Override
    public String toString() {
        return lhs+operator+rhs;
    }
    
    /**
     * Return the expression without parentheses
     * @return lhs operator rhs
     */
    @Override
    public String toStringNoParen() {
        return toString();
    }

    /**
     * Indicate if this expression is empty
     * @return false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }
}
