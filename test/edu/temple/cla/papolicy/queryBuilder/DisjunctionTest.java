/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.queryBuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class DisjunctionTest {
    
    public DisjunctionTest() {
    }

    @Test
    public void emptyDisjunctionReturnsEmptyString() {
        Expression e = new Disjunction();
        assertEquals("", e.toString());
    }
    
    @Test
    public void singleTermReturnTerm() {
        Composite c = new Disjunction();
        c.addTerm(new Comparison("foo", "<>", "0"));
        assertEquals("foo<>0", c.toString());
    }
    
    @Test
    public void multipleTerms() {
        Composite d = new Disjunction();
        d.addTerm(new Comparison("foo", "<>", "0"));
        d.addTerm(new Comparison("bar", "<>", "0"));
        d.addTerm(new Comparison("baz", "<>", "0"));
        assertEquals("(foo<>0 OR bar<>0 OR baz<>0)", d.toString());
    }
    
    @Test
    public void nestedExpressions() {
        Composite d = new Disjunction();
        Composite c1 = new Conjunction();
        Composite c2 = new Conjunction();
        c1.addTerm(new Comparison("foo", "<>", "0"));
        c1.addTerm(new Comparison("bar", "=", "0"));
        c2.addTerm(new Comparison("foo", "=", "0"));
        c2.addTerm(new Comparison("bar", "<>", "0"));
        d.addTerm(c1);
        d.addTerm(c2);
        assertEquals("((foo<>0 AND bar=0) OR (foo=0 AND bar<>0))", d.toString());
    }    
}
