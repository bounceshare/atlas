package com.bounce.atlas.utils;

import com.bounce.utils.BounceUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

public class FreemarkerUtils {

    public static final int VERSION = 1;

    public static String getContent(String filename) {
        StringBuilder content = new StringBuilder();
        try {
            InputStream inputStream = FreemarkerUtils.class.getClassLoader().getResourceAsStream(filename);
            if (inputStream != null) {
                content.append(IOUtils.toString(inputStream));
            }
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String getFreemarkerString(String filename, Map<String, Object> templateData) {
        StringBuilder content = new StringBuilder();

        try {
            Version version = new Version(2, 3, 20);
            Configuration cfg = new Configuration(version);
            cfg.setClassForTemplateLoading(FreemarkerUtils.class, "/");
            cfg.setIncompatibleImprovements(new Version(2, 3, 20));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setLocale(Locale.US);

            StringWriter stringWriter = new StringWriter();

            Template template = cfg.getTemplate(filename);
            template.process(templateData, stringWriter);

            content.append(stringWriter.toString());
        } catch (Exception e) {
            BounceUtils.logError(e);
            e.printStackTrace();
        }

        return content.toString();
    }

}
