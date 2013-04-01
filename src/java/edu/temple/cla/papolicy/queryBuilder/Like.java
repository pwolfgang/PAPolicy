package edu.temple.cla.papolicy.queryBuilder;

public class Like implements Expression {
    
    private String column;
    private String what;
    private boolean not;
    
    public Like(String column, int code, boolean not) {
        this.column = column;
        this.what = code + "__";
        this.not = not;
    }
    
    public Like(String column, int code) {
        this(column, code, false);
    }
    
    public Like(String column, String what, boolean not) {
        this.column = column;
        this.what = what;
        this.not = not;
    }
    
    public Like(String column, String what) {
        this(column, what, false);
    }
    
    public String toString() {
        return column + (not ? " NOT ": " ") + "LIKE('" + what + "')";
    }
    
    public boolean isEmptyExpression() {return false;}

}
