/*
   Copyright 2014 Simon Schmidt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package iox.refused.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

public class TextUtil {

    private static Charset CS = Charset.forName("utf8");
    private static Logger logger = Logger.getLogger("iox.refused.util.TextUtil");

    public static String loadfile(ServletContext context, String filename) {
        InputStream is = context.getResourceAsStream(filename);
        if (is == null) {
            logger.log(Level.SEVERE, "<loadfile> unable to load " + filename);
            return "";
        }
        String data;
        try {
            data = IOUtils.toString(is, CS);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "<loadfile> unable to load " + filename, e);
            data = "";
        }
        IOUtils.closeQuietly(is);
        return data;
    }
}
