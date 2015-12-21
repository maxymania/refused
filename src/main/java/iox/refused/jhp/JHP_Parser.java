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
package iox.refused.jhp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import bsh.EvalError;
import bsh.Interpreter;

public class JHP_Parser {

    private static final Pattern BSLASHES = Pattern.compile("\\\\", Pattern.DOTALL);
    private static final Pattern IMPORTS = Pattern.compile(
            "\\<templ\\:import (.+?)\\>[\\r\\n\\t ]+", Pattern.DOTALL);
    private static final Pattern BLOCK = Pattern.compile(
            "\\<templ\\:(insert ([^ ]+)|init)\\>[\\r\\n\\t ]+(.*?)\\<\\/templ\\:in(sert|it)\\>", Pattern.DOTALL);
    private static final Pattern TEXTLET = Pattern.compile(
            "(\\?\\>|^)(.*?)(\\<\\?jhp|$)", Pattern.DOTALL);

    public static Object compile(String src) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("import iox.refused.IRenderer;\n");
        buffer.append("import iox.refused.IMetaRenderer;\n");
        buffer.append("import iox.refused.IParameter;\n");
        buffer.append("import java.io.PrintWriter;\n");
        buffer.append("\n");
        src = compileImports(buffer, src);
        String init = null;
        List<String[]> lst = new ArrayList<String[]>();

        Matcher m = BLOCK.matcher(src);
        while (m.find()) {
            if ("init".equals(m.group(1))) {
                init = m.group(3);
            } else {
                lst.add(new String[]{m.group(2), m.group(3)});
            }
        }
        if (lst.isEmpty()) {
            if (init != null) {
                src = init;
            }
            buffer.append("void process(IParameter req,PrintWriter resp){\n");
            buffer.append("void echo(Object obj){resp.print(obj);}\n");
            compile(buffer, src);
            buffer.append("\n}\nreturn (IRenderer)this;");
        } else if (init == null) {
            buffer.append("void process(IParameter req,PrintWriter resp){\n");
            buffer.append("void echo(Object obj){resp.print(obj);}\n");
            int n = 0;
            for (String[] obj : lst) {
                if (n > 0) {
                    buffer.append("else ");
                }
                buffer.append(
                        "if(\"" + StringEscapeUtils.escapeJava(obj[0])
                        + "\".equals(req.part())){\n");
                compile(buffer, obj[1]);
                buffer.append("}\n");
                n++;
            }
            buffer.append("\n}\nreturn (IRenderer)this;");
        } else {
            buffer.append("IRenderer process(IParameter req){\n");
            buffer.append("void echo(Object obj){}\n");
            compile(buffer, init);
            buffer.append("return new IRenderer(){\n");
            buffer.append("void process(IParameter req,PrintWriter resp){\n");
            buffer.append("void echo(Object obj){resp.print(obj);}\n");
            int n = 0;
            for (String[] obj : lst) {
                if (n > 0) {
                    buffer.append("else ");
                }
                buffer.append(
                        "if(\"" + StringEscapeUtils.escapeJava(obj[0])
                        + "\".equals(req.part())){\n");
                compile(buffer, obj[1]);
                buffer.append("}\n");
                n++;
            }
            buffer.append("\n}\n};");
            buffer.append("\n}\nreturn (IMetaRenderer)this;");
        }
        Interpreter i = new Interpreter();
        try {
            return i.eval(buffer.toString());
        } catch (EvalError e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String compileImports(StringBuffer buffer, String src) {
        Matcher m = IMPORTS.matcher(src);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String imp = m.group(1);
            buffer.append("import ").append(imp).append(";\n");
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static void compile(StringBuffer sb, String src) {
        Matcher m = TEXTLET.matcher(src);
        while (m.find()) {
            String name = BSLASHES.matcher(StringEscapeUtils.escapeJava(m.group(2))).replaceAll("\\\\\\\\");
            m.appendReplacement(sb, "echo(\"" + name + "\");\n");
        }
        m.appendTail(sb);
    }
}
