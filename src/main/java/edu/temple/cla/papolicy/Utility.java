/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


/**
 * Class to hold utility methods
 * @author Paul Wolfgang
 */
public class Utility {

    private static final Pattern HYPERLINK = Pattern.compile("<a\\s+href=\\\".+\\\".*>.*</a>");
    private static final Logger LOGGER = Logger.getLogger(Utility.class);

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
        } catch (IOException ex) { // Cannot happen
            LOGGER.error("IO Exception writing to internal string", ex);
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
         } catch (IOException ex) {
             LOGGER.error(ex);
             return theString;
         }
     }
     
     /**
      * Method to format hyperlinks in the drilldown.  A hyperlink that has been
      * created in MS Access has the form <i>text</i>#<i>ref</i># where the 
      * <i>text</i> is optional. This is translated to 
      * &lt;a href=&quot;<i>ref</i>&quot;&gt;<i>text</i>&lt;/a&gt;. If the <i>text</i>
      * is empty, then the <i>ref</i> is used. If the hyperlink contains no #
      * characters, then the string is checked to see if it is the form 
      * &lt;a href=&quot;<i>ref</i>&quot;&gt;<i>text</i>&lt;/a&gt;. If not, the string
      * is presumed to contain the ref and is reformatted as described above.
      * @param hyperlink The hyperlink string
      * @return The html string to be inserted into the table cell
      */
     public static String reformatHyperlink(String hyperlink) {
         int firstSharp = hyperlink.indexOf("#");
         if (firstSharp == -1) {
             if (HYPERLINK.matcher(hyperlink).matches()) {
                 return hyperlink;
             } else {
                 return ("<a href=\"" + hyperlink + "\">" + hyperlink + "</a>");
             }
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
