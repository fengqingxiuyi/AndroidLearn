package com.example.utils.encrypt;

public class UnicodeUtils {

  /**
   * utf-8 转unicode
   *
   * @param str
   * @return String
   */
  public static String utf8ToUnicode(String str) {
    str = (str == null ? "" : str);
    String tmp;
    StringBuffer sb = new StringBuffer(1000);
    char c;
    int i, j;
    sb.setLength(0);
    for (i = 0; i < str.length(); i++) {
      c = str.charAt(i);
      sb.append("\\u");
      j = (c >>> 8); //取出高8位
      tmp = Integer.toHexString(j);
      if (tmp.length() == 1)
        sb.append("0");
      sb.append(tmp);
      j = (c & 0xFF); //取出低8位
      tmp = Integer.toHexString(j);
      if (tmp.length() == 1)
        sb.append("0");
      sb.append(tmp);

    }
    return (new String(sb));
  }

  /**
   * unicode 转 utf-8
   * @param theString
   * @return
   */
  public static String unicodeToUtf8(String theString) {
    char aChar;
    int len = theString.length();
    StringBuffer outBuffer = new StringBuffer(len);
    for (int x = 0; x < len; ) {
      aChar = theString.charAt(x++);
      if (aChar == '\\') {
        aChar = theString.charAt(x++);
        if (aChar == 'u') {
          // Read the xxxx
          int value = 0;
          for (int i = 0; i < 4; i++) {
            aChar = theString.charAt(x++);
            switch (aChar) {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + aChar - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + aChar - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + aChar - 'A';
                break;
              default:
                throw new IllegalArgumentException(
                    "Malformed   \\uxxxx   encoding.");
            }

          }
          outBuffer.append((char) value);
        } else {
          if (aChar == 't')
            aChar = '\t';
          else if (aChar == 'r')
            aChar = '\r';
          else if (aChar == 'n')
            aChar = '\n';
          else if (aChar == 'f')
            aChar = '\f';
          outBuffer.append(aChar);
        }
      } else
        outBuffer.append(aChar);
    }
    return outBuffer.toString();
  }

}
