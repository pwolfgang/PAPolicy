package edu.temple.cla.papolicy.queryBuilder;

public class Between implements Expression {
    
    private String column;
    private String lowValue;
    private String hiValue;
    
    public Between(String column, String lowValue, String hiValue) {
        this.column = column;
        this.lowValue = lowValue;
        this.hiValue = hiValue;
    }
    
    @Override
    public String toString() {
        return column + " BETWEEN " + lowValue + " AND " + hiValue;
    }
    
    @Override
    public boolean isEmptyExpression() {
        return false;
    }

}
