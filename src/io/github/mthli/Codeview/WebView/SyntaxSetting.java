package io.github.mthli.Codeview.WebView;

import java.io.*;

public class SyntaxSetting {
    public static final String base_url = "file:///asset/google-code-prettify/";

    public static String setCodeAsHtml(String path) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html>\n");
        builder.append("<head>\n");
        builder.append("<meta charset=\"utf-8\">\n");
        builder.append("<link rel=\"stylesheet\" href=\"github.css\" type=\"text/css\"></link>\n");
        builder.append("<script src=\"run_prettify.js\" type=\"text/javascript\"></script>\n");
        builder.append("</head>\n");
        builder.append("<body onload=\"prettyPrint()\">\n");
        builder.append("<pre class=\"prettyprint\">");
        builder.append(getFileContent(path));
        builder.append("</pre>");
        builder.append("</body>\n");
        builder.append("</html>");
        return builder.toString();
    }

    private static String getFileContent(String path) {
        StringBuilder builder = new StringBuilder();
        File file = new File(path);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException f) {
            return f.getMessage();
        }
        char[] buf = new char[1024];
        int num;
        try {
            while ((num = reader.read(buf)) != -1) {
                String data = String.valueOf(buf, 0, num);
                builder.append(data);
            }
        } catch (IOException i) {
            return i.getMessage();
        } finally {
            try {
                reader.close();
            } catch (IOException i) {
                /* Do nothing */
            }
        }
        return builder.toString();
    }
}