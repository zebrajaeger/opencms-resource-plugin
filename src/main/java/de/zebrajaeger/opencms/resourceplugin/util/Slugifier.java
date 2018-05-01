package de.zebrajaeger.opencms.resourceplugin.util;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class Slugifier {

    public static final String DOT = "-_-dot-_-";

    private Slugifier() {
    }

    public static String nameFromUrl(String url) throws MalformedURLException {
        return nameFromUrl(new URL(url));
    }

    public static String nameFromUrl(URL url) {
        String name = FilenameUtils.getName(url.getFile());
        name = replaceLast(name, ".", DOT);

        Slugify slugify = new Slugify();
        String slugName = slugify.slugify(name);

        return slugName.replace(DOT, ".");
    }

    public static String replaceLast(String source, String searchFor, String replaceWith) {
        int lastIndex = source.lastIndexOf(searchFor);

        if (lastIndex == -1) {
            return source;
        }

        String beginString = source.substring(0, lastIndex);
        String endString = source.substring(lastIndex + searchFor.length());

        return beginString + replaceWith + endString;
    }
}
