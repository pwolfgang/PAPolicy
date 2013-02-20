package edu.temple.cla.papolicy.queryBuilder;

public class Comparison implements Expression {
    
    private String lhs;
    private String operator;
    private String rhs;
    
    public Comparison(String lhs, String operator, String rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }
    
    @Override
    public String toString() {
        return lhs+operator+rhs;
    }

}
