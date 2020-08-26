package com.example.ui.toast;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ui.BuildConfig;
import com.example.ui.R;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 自定义Toast，控制Toast的显示
 */
public class ToastUtil {

    public interface ToastListener {
        Activity getLastAliveActivity();
        boolean isForeground();
    }

    private static ToastListener sToastListener;
    private static Handler handler;

    private static ToastDialog dialog = null;

    private final static int MSG_NORMAL = 0;
    private final static int MSG_REMOVE = 1;

    // 动画时间 ToastDialog显示隐藏动画合计为400，故写400
    private static final int ANIMATION_DURATION = 400;
    // 消失时间
    private static final long DEFAULT_DELAY_MILLIS = 2000;
    private static long DELAY_MILLIS = DEFAULT_DELAY_MILLIS;

    private static BlockingQueue<String> queue = new LinkedBlockingDeque<>();
    private static HashMap<String, Runnable> activityRunnableMap = new HashMap<>();

    private static String splitStr = "【ToastUtil】";

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_REMOVE:
                        try {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            if (queue.size() > 0) {
                                String message = queue.remove();
                                String[] objArr = message.split(splitStr);
                                show(objArr[0], Integer.parseInt(objArr[1]));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MSG_NORMAL:
                    default:
                        try {
                            String message = (String) msg.obj;
                            if (dialog != null && dialog.isShowing()) {
                                //先remove再add，是为了保证已存在的消息放在最后一个位置
                                queue.remove(message);
                                queue.add(message);
                            } else {
                                String[] objArr = message.split(splitStr);
                                show(objArr[0], Integer.parseInt(objArr[1]));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void init(ToastListener toastListener) {
        sToastListener = toastListener;
        handler = new MyHandler();
    }

    public static void toast(String msg) {
        toast(msg, ToastType.TYPE_NORMAL);
    }

    public static void toast(String msg, long delayMillis) {
        toast(msg, delayMillis, ToastType.TYPE_NORMAL);
    }

    public static void toast(String msg, int type) {
        if (BuildConfig.DEBUG) {
            toast(msg, DEFAULT_DELAY_MILLIS * 2, type);
        } else {
            toast(msg, DEFAULT_DELAY_MILLIS, type);
        }
    }

    public static void toast(String msg, long delayMillis, int type) {
        if (TextUtils.isEmpty(msg) || !sToastListener.isForeground()) {
            return;
        }
        DELAY_MILLIS = delayMillis;
        Message message = Message.obtain();
        message.what = MSG_NORMAL;
        message.obj = msg + splitStr + type;
        handler.sendMessage(message);
    }

    private static void show(String msg, int type) {
        if (null != sToastListener) {
            show(sToastListener.getLastAliveActivity(), msg, type);
        }
    }

    private static void show(Activity activity, String msg, final int type) {
        try {
            if (activity == null || !sToastListener.isForeground() || (dialog != null && dialog.isShowing())) {
                return;
            }
            if (dialog == null || dialog.getActivity() != activity) {
                dialog = new ToastDialog(activity);
                dialog.setContentView(R.layout.uikit_toast_view);
            }
            //set flag
            ImageView toastFlag = (ImageView) dialog.findViewById(R.id.toast_flag);
            if (type == ToastType.TYPE_SUCCESS) {
                toastFlag.setVisibility(View.VISIBLE);
                toastFlag.setImageResource(R.drawable.toast_success);
            } else if (type == ToastType.TYPE_ERROR) {
                toastFlag.setVisibility(View.VISIBLE);
                toastFlag.setImageResource(R.drawable.toast_error);
            } else {
                toastFlag.setVisibility(View.GONE);
            }
            //set text
            TextView toastMsg = (TextView) dialog.findViewById(R.id.toast_msg);
            if (!TextUtils.isEmpty(msg)) {
                toastMsg.setText(msg.trim());
            }
            //显示
            dialog.show();
            //隐藏
            if (DELAY_MILLIS < ANIMATION_DURATION) {
                DELAY_MILLIS = DEFAULT_DELAY_MILLIS;
            }
            Runnable runnable = activityRunnableMap.get(getActivityName(activity));
            if (runnable == null) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = MSG_REMOVE;
                        handler.sendMessage(message);
                    }
                };
                activityRunnableMap.put(getActivityName(activity), runnable);
            }
            handler.postDelayed(runnable, DELAY_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onDestroy(Activity activity) {
        try {
            if (dialog != null && dialog.getActivity() != activity) {
                return;
            }
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            Runnable runnable = null;
            if (activityRunnableMap != null) {
                runnable = activityRunnableMap.get(getActivityName(activity));
            }
            if (runnable != null && handler != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onAppExit() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            if (queue != null) {
                queue.clear();
            }
            if (activityRunnableMap != null) {
                activityRunnableMap.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getActivityName(Activity activity) {
        String name = "unknown";
        try {
            name = activity.getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
