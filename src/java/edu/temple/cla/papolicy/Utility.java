/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;

/**
 * Class to hold utility methods
 * @author Paul Wolfgang
 */
public class Utility {

    /**
     * Method to compress and encode a string to be used as a get parameter.
     * The string is first comprssed using the zip algorithm and then encoded
     * using the base64 code
     * @param theString the string to be compressed and encoded.
     * @return the compressed and encoded string.
     */
    public static String compressAndEncode(String theString) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            GZIPOutputStream zipped = new GZIPOutputStream(bytes);
            OutputStreamWriter out = new OutputStreamWriter(zipped);
            out.write(theString);
            out.close();
            String codedString = Base64.encodeBase64URLSafeString(bytes.toByteArray());
            return codedString;
        } catch (Exception ex) {
            return theString;
        }
    }

    /**
     * Method to decode and uncompress a string. The string is first decoded
     * into a byte array and then uncompressed using the zip algorithm.
     * @param theString A string created by compressAndEncode
     * @return The original string
     */
     public static String decodeAndDecompress(String theString) {
         try {
             char[] chars = theString.toCharArray();
             for (int i = 0; i < chars.length; i++) {
                 if (chars[i] == ' ') chars[i] = '+';
             }
             theString = new String(chars);
             byte[] bytes = Base64.decodeBase64(theString);
             Reader r = 
                     new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(bytes)));
             StringWriter w = new StringWriter();
             int c;
             while ((c = r.read()) != -1) {
                 w.write(c);
             }
             return w.toString();
         } catch (Exception ex) {
             return theString;
         }
     }
}
