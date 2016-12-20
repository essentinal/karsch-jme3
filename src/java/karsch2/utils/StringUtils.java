package karsch2.utils;

public class StringUtils {
  public static String fixEmpty(final String s) {
    if (s != null && s.isEmpty()) {
      return null;
    }
    return s;
  }

  public static String fixNull(final String s) {
    if (s == null) {
      return "";
    }
    return s;
  }

}
