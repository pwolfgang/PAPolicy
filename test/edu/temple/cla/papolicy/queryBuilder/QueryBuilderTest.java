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
    
}
