package maxz.monitor.services.callers.utils;

import org.jetbrains.annotations.NotNull;

public class StringUtils {

    public static String getAfterLast(@NotNull String source, @NotNull String delimiter) {
        int i = source.lastIndexOf(delimiter);
        if(i !=- 1 && i < source.length()-1) {
            return source.substring(i+1);
        }
        return source;
    }

}
