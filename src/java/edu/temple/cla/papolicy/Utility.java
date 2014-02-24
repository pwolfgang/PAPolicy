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
import org.apache.log4j.Logger;


/**
 * Class to hold utility methods
 * @author Paul Wolfgang
 */
public class Utility {

    private static final Logger logger = Logger.getLogger(Utility.class);

    /**
     * Method to compress and encode a string to be used as a get parameter.
     * The string is first compressed using the zip algorithm and then encoded
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
            logger.error(ex);
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
             logger.error(ex);
             return theString;
         }
     }
     
     /**
      * Method to format hyperlinks in the drilldown.  A hyperlink that has been
      * created in MS Access has the form <i>text</i>#<i>ref</i># where the 
      * <i>text</i> is optional. This is translated to 
      * %lt:a href=%qt;<i>ref</i>%qt;%gt <i>text</i>%lt;/a%gt;. If the <i>text</i>
      * is empty, then the <i>ref</i> is used. If the hyperlink contains no #
      * characters, then it is assumed not to be a  hyperlink and is unchanged.
      * @param hyperlink The hyperlink string
      * @return The html string to be inserted into the table cell
      */
     public static String reformatHyperlink(String hyperlink) {
         int firstSharp = hyperlink.indexOf("#");
         if (firstSharp == -1) {
             return hyperlink;
         }
         int secondSharp = hyperlink.indexOf("#", firstSharp + 1);
         if (secondSharp == -1) secondSharp = hyperlink.length();
         String text = hyperlink.substring(0, firstSharp);
         String ref = hyperlink.substring(firstSharp + 1, secondSharp);
         if (text.isEmpty()) {
             return "<a href=\"" + ref + "\">" + ref + "</a>";
         } else {
             return "<a href=\"" + ref + "\">" + text + "</a>";
         }
     }
}
