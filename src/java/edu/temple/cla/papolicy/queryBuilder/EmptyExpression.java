package edu.temple.cla.papolicy.queryBuilder;

/**
 * Represents the empty expression
 * @author Paul Wolfgang
 */
public class EmptyExpression implements Expression {
    
    /**
     * Return an empty string;
     * @return an empty string
     */
    @Override
    public String toString() {return "";}
    
    @Override
    public String toStringNoParen() {return "";}
    
    @Override
    public boolean isEmpty() {return true;}

}
