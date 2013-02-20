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
public class ConjunctionTest {
    
    public ConjunctionTest() {
    }

    @Test
    public void emptyConjunctionReturnsEmptyString() {
        Expression e = new Conjunction();
        assertEquals("", e.toString());
    }
    
    @Test
    public void singleTermReturnTerm() {
        Composite c = new Conjunction();
        c.addTerm(new Comparison("foo", "<>", "0"));
        assertEquals("foo<>0", c.toString());
    }
    
    @Test
    public void multipleTerms() {
        Composite c = new Conjunction();
        c.addTerm(new Comparison("foo", "<>", "0"));
        c.addTerm(new Comparison("bar", "<>", "0"));
        c.addTerm(new Comparison("baz", "<>", "0"));
        assertEquals("(foo<>0 AND bar<>0 AND baz<>0)", c.toString());
    }
    
        

    
}
