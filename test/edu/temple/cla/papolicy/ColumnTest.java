/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
@RunWith(value=Parameterized.class)
public class ColumnTest {

    private String expected;
    private String textColumn;
    private String freeText;

    @Parameters
    public static Collection<String[]> getTextParameters() {
        return Arrays.asList(new String[][] {
            {"(text LIKE(\"%foo%\"))", "text", "foo"},
            {"(text LIKE(\"%foo%\") AND text LIKE(\"%bar%\"))", "text", "foo bar"},
            {"(text LIKE(\"%foo%\") AND text LIKE(\"%bar%\"))", "text", "foo and bar"},
            {"(text LIKE(\"%foo%\") OR text LIKE(\"%bar%\"))", "text", "foo or bar"},
            {"(text LIKE(\"%foo bar%\"))", "text", "\"foo bar\""},
            {"(text LIKE(\"%foo bar%\") OR text LIKE(\"%baz%\"))", "text", "\"foo bar\" or baz"}
        });
    }

    public ColumnTest(String expected, String textColumn, String freeText) {
        this.expected = expected;
        this.textColumn = textColumn;
        this.freeText = freeText;
    }

    @Test
    public void testSingleToken() {
        Column column = new Column();
        String result = column.parseFreeText(textColumn, freeText);
        assertEquals(expected, result);
    }
}