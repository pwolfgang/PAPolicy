package edu.temple.cla.papolicy.queryBuilder;
/**
 * The Between class represents a range.  It generates the
 * expression BETWEEN lowValue AND hiValue
 * @author Paul Wolfgang
 */
public class Between implements Expression {
    
    private String column;
    private String lowValue;
    private String hiValue;
    
    /**
     * Create a Between instance
     * @param column the column to be examined.
     * @param lowValue the minimum value
     * @param hiValue the maximum value
     */
    public Between(String column, int lowValue, int hiValue) {
        this.column = column;
        this.lowValue = Integer.toString(lowValue);
        this.hiValue = Integer.toString(hiValue);
    }
    
    /**
     * return a String representation
     * @return column BETWEEN lowValue AND hiValue
     */
    @Override
    public String toString() {
        return column + " BETWEEN " + lowValue + " AND " + hiValue;
    }
    
    /**
     * Return a String representation without parentheses
     * @return column BETWEEN lowValue and hiValue
     */
    @Override
    public String toStringNoParen() {
        return toString();
    }
    
    /**
     * Indicate whether this expression is empty
     * @return false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

}
