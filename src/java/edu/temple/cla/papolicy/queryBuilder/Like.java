package edu.temple.cla.papolicy.queryBuilder;

public class Like implements Expression {
    
    private String column;
    private int code;
    
    public Like(String column, int code) {
        this.column = column;
        this.code = code;
    }
    
    public String toString() {
        return column + " LIKE('" + code + "__')";
    }
    
    public boolean isEmptyExpression() {return false;}

}
