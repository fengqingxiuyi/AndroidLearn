package com.example.utils;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author fqxyi
 * @date 2020/8/19
 * 所有log都通过这个类打印
 */
public class LogUtil {

    private static String TAG = "LogUtil";

    private static boolean LOG_DEBUG = BuildConfig.DEBUG;

    private static int maxLength = 3500;

    private static final String SUFFIX = ".java";

    private static ErrorListener errorListener;
    interface ErrorListener {
        void error(Throwable e);
        void error(String error);
    }
    public static void setErrorListener(ErrorListener errorListener) {
        LogUtil.errorListener = errorListener;
    }

    private static LogListener logListener;
    interface LogListener {
        void log(Throwable e);
        void log(String error);
    }
    public static void setLogListener(LogListener logListener) {
        LogUtil.logListener = logListener;
    }

    public static void i(String msg) {
        i(msg, true);
    }

    public static void i(String msg, boolean lineBreak) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return;
            String header = getHeaderInfo();
            int index = 0;
            int countOfSub = msg.length() / maxLength;
            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    Log.i(TAG, header + sub);
                    index += maxLength;
                }
                Log.i(TAG, header + msg.substring(index, msg.length()));
            } else {
                Log.i(TAG, header + msg);
            }
            // 回调
            if (null != logListener) {
                logListener.log(header + msg);
            }
        }
    }

    private static boolean isEmpty(String msg, boolean lineBreak) {
        try {
            if (TextUtils.isEmpty(msg)) {
                return true;
            }
        } catch (Exception e) { //在非Android环境中运行会报错
            if (lineBreak) {
                System.out.println(msg);
            } else {
                System.out.print(msg);
            }
            return true;
        }
        return false;
    }

    public static void i(Object... messages) {
        if (LOG_DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object msg : messages) {
                if (null == msg) {
                    continue;
                }
                stringBuilder.append(msg.toString());
                stringBuilder.append(" ");
            }
            if (stringBuilder.length() > 0) {
                LogUtil.i(stringBuilder.toString());
            }
        }
    }

    public static void d(String msg) {
        d(msg, true);
    }

    public static void d(String msg, boolean lineBreak) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return;
            String header = getHeaderInfo();
            int index = 0;
            int countOfSub = msg.length() / maxLength;
            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    Log.d(TAG, header + sub);
                    index += maxLength;
                }
                Log.d(TAG, header + msg.substring(index, msg.length()));
            } else {
                Log.d(TAG, header + msg);
            }
            // 回调
            if (null != logListener) {
                logListener.log(header + msg);
            }
        }
    }

    public static void d(Object... messages) {
        if (LOG_DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object msg : messages) {
                if (null == msg) {
                    continue;
                }
                stringBuilder.append(msg.toString());
                stringBuilder.append(" ");
            }
            if (stringBuilder.length() > 0) {
                LogUtil.d(stringBuilder.toString());
            }
        }
    }

    public static void e(String msg) {
        e(msg, true);
    }

    public static void e(String msg, boolean lineBreak) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return;
            int index = 0;
            int countOfSub = msg.length() / maxLength;
            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    Log.e(TAG, sub);
                    index += maxLength;
                }
                Log.e(TAG, msg.substring(index, msg.length()));
            } else {
                Log.e(TAG, msg);
            }
        }
        // 回调
        if (null != errorListener) {
            errorListener.error(msg);
        }
    }

    public static void e(String... messages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String msg : messages) {
            stringBuilder.append(msg);
            stringBuilder.append("\n");
        }
        if (LOG_DEBUG) {
            e(stringBuilder.toString());
        } else {
            if (null != errorListener) {
                errorListener.error(stringBuilder.toString());
            }
        }
    }

    public static void e(Throwable throwable) {
        if (LOG_DEBUG) {
            Log.e(TAG, getFullHeaderInfo(), throwable);
        }
        // 回调
        if (null != errorListener) {
            errorListener.error(throwable);
        }
    }

    private static String readThrowable(Throwable ex) {
        Writer writer = new StringWriter();
        try {
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private static String getFullHeaderInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (null == stackTrace) {
            return "";
        }
        StringBuilder headInfo = new StringBuilder();
        for (StackTraceElement traceElement : stackTrace) {
            headInfo.append(traceElement.toString());
        }
        return headInfo.toString();
    }

    private static String getHeaderInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (null == stackTrace) {
            return "";
        }
        StackTraceElement destStackTraceElement = null;
        boolean next = true;
        for (StackTraceElement traceElement : stackTrace) {
            if (traceElement.getClassName().equals(LogUtil.class.getName())) {
                next = false;
            } else {
                if (next) {
                    continue;
                } else {
                    destStackTraceElement = traceElement;
                    break;
                }
            }
        }
        if (null == destStackTraceElement) {
            return "";
        }
        String className = destStackTraceElement.getClassName();
        String methodName = destStackTraceElement.getMethodName();
        int lineNumber = destStackTraceElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String threadInfo = "Thread: " + Thread.currentThread().getName();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            threadInfo += "(UI线程), ";
        } else {
            threadInfo += "(Work线程), ";
        }
        String classAndMethodInfo = "[(" + className + SUFFIX + ":" + lineNumber + ")#" + methodName + "]\n";
        return threadInfo + classAndMethodInfo;
    }

}
