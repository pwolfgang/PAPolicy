/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class UtilityTest {

    public UtilityTest() {
    }


    @Test
    public void testRoundTrip() {
        System.out.println("compressAndEncode");
        String theString = "The quick brown fox jumps over the lazy dog";
        String compressed = Utility.compressAndEncode(theString);
        String uncompressed = Utility.decodeAndDecompress(compressed);
        assertEquals(theString, uncompressed);
    }

}