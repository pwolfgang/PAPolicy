package edu.temple.cla.papolicy.queryBuilder;

/**
 * The Like expression determines if a value matches or does not match
 * a pattern. It is used to match a major code to one or more minor codes
 * or in the key-word search.
 * @author Paul Wolfgang
 */
public class Like implements Expression {
    
    private String column;
    private String what;
    private boolean not;
    
    /**
     * Construct a Like expression
     * @param column The column to be tested
     * @param code The major code
     * @param not True if NOT should be preappended.
     */
    public Like(String column, int code, boolean not) {
        this.column = column;
        this.what = code + "__";
        this.not = not;
    }
    
    /**
     * Construct a Like expression with not set to false
     * @param column The column to be tested
     * @param code The major code
     */
    public Like(String column, int code) {
        this(column, code, false);
    }
    
    /**
     * Construct a general Like expression
     * @param column The column to be tested
     * @param what The pattern to be matched
     * @param not True if NOT should be preapppended
     */
    public Like(String column, String what, boolean not) {
        this.column = column;
        this.what = what;
        this.not = not;
    }
    
    /**
     * Construct a general Like expression with not set to false
     * @param column The column to be tested
     * @param what The pattern to be matched
     */
    public Like(String column, String what) {
        this(column, what, false);
    }
    
    /**
     * Return the expression as a string
     * @return [NOT] LIKE('what')
     */
    @Override
    public String toString() {
        return column + (not ? " NOT ": " ") + "LIKE('" + what + "')";
    }
    
    /**
     * Return the expression as a string without parentheses.
     * @return [NOT] LIKE('what')
     */
    @Override
    public String toStringNoParen() {
        return toString();
    }
    
    /**
     * Indicate whether this expression is empty.
     * @return false
     */
    @Override
    public boolean isEmpty() {return false;}

}
