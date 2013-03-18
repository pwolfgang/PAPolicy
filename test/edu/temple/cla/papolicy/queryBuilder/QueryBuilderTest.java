/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.queryBuilder;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class QueryBuilderTest {
    
    private QueryBuilder qb;
    
    public QueryBuilderTest() {
    }
    
    @Before
    public void setUp() {
        qb = new QueryBuilder();
        qb.setDbName("BooBar");
    }
    
    @Test
    public void shouldSelectFromDatabase() {
        assertEquals("SELECT * FROM BooBar", qb.build());
    }
    
    @Test
    public void shouldSelectNamedColumn() {
        qb.addColumn("a");
        assertEquals("SELECT a FROM BooBar", qb.build());
    }
    
    @Test
    public void shouldSelectSeveralColumns() {
        qb.addColumn("a");
        qb.addColumn("b");
        qb.addColumn("c");
        assertEquals("SELECT a, b, c FROM BooBar", qb.build());
    }
    
    @Test
    public void shouldSelectBasedOnCriteria() {
        qb.addColumn("a");
        Expression criteria = new Comparison("a", "=", "5");
        qb.addToSelectCriteria(criteria);
        assertEquals("SELECT a FROM BooBar WHERE a=5", qb.build());
    }
    
    @Test
    public void shouldSelectBasedOnFilter() {
        qb.addColumn("a");
        Expression filter = new Comparison("a", "=", "5");
        qb.addFilter(filter);
        assertEquals("SELECT a FROM BooBar WHERE a=5", qb.build());
    }

    @Test
    public void shouldSelectBasedOnCriteriaAndFilter() {
        qb.addColumn("a");
        qb.addColumn("b");
        Expression criteria = new Comparison("a", "=", "5");
        qb.addToSelectCriteria(criteria);        
        Expression filter = new Comparison("b", "<>", "5");
        qb.addFilter(filter);
        assertEquals("SELECT a, b FROM BooBar WHERE a=5 AND b<>5", qb.build());
    }
    
    @Test
    public void shouldSelectBasedOnCriteriaAndFilterGroupBy() {
        qb.addColumn("a");
        qb.addColumn("b");
        Expression criteria = new Comparison("a", "=", "5");
        qb.addToSelectCriteria(criteria);        
        Expression filter = new Comparison("b", "<>", "5");
        qb.addFilter(filter);
        qb.setGroupBy("a");
        qb.setOrderBy("a");
        assertEquals("SELECT a, b FROM BooBar WHERE a=5 AND b<>5 GROUP BY a ORDER BY a", qb.build());
    }
    
    @Test
    public void shouldSelectSeveralColumnsGroupBy() {
        qb.addColumn("a");
        qb.addColumn("b");
        qb.addColumn("c");
        qb.setGroupBy("a");
        qb.setOrderBy("a");
       assertEquals("SELECT a, b, c FROM BooBar GROUP BY a ORDER BY a", qb.build());
    }

    @Test
    public void shouldSelectBasedOnCriteriaAndFilterAndBetweenGroupBy() {
        qb.addColumn("a");
        qb.addColumn("b");
        Expression criteria = new Comparison("a", "=", "5");
        qb.addToSelectCriteria(criteria);        
        Expression filter = new Comparison("b", "<>", "5");
        qb.addFilter(filter);
        qb.setBetween(new Between("a","3","7"));
        qb.setGroupBy("a");
        qb.setOrderBy("a");
        assertEquals("SELECT a, b FROM BooBar WHERE a=5 AND b<>7 AND a BETWEEN 3 AND 5 GROUP BY a ORDER BY a", qb.build());
    }
    
}
