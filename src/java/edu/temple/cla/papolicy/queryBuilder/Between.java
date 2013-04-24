package edu.temple.cla.papolicy.queryBuilder;

public class Between implements Expression {
    
    private String column;
    private String lowValue;
    private String hiValue;
    
    public Between(String column, int lowValue, int hiValue) {
        this.column = column;
        this.lowValue = Integer.toString(lowValue);
        this.hiValue = Integer.toString(hiValue);
    }
    
    @Override
    public String toString() {
        return column + " BETWEEN " + lowValue + " AND " + hiValue;
    }
    
    @Override
    public String toStringNoParen() {
        return toString();
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }

}
