package com.example.learn.java;

import com.example.utils.storage.FileUtil;

import java.io.File;
import java.util.ArrayList;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Java方法测试类
 */
public class JavaTest {

    public static void main(String[] args) {
        // 执行下面两条测试语句，需要注释getLastFilePath那一行代码
        // FileUtil.writeFile("你好123sad", "app/src/main/java/com/example/learn/java/FileUtilTest.txt");
         System.out.println(FileUtil.readFile(new File("app/src/main/java/com/example/learn/java/FileUtilTest.txt")));

        // 正则表达式
        // System.out.println(RegexUtil.checkEmail("test@test.test"));
        // System.out.println(RegexUtil.checkEmail("test@test"));
        // System.out.println(RegexUtil.checkChinese("测试"));
        // System.out.println(RegexUtil.checkChinese("test测试"));

        // 字符串相关操作
        // String urlString = "sdcard/com.fqxyi.androidutils/files/test.txt";
        // System.out.print(urlString.substring(urlString.lastIndexOf(".") + 1));

        try {
            modifyClass();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        testArrayListAdd(false);
//        testArrayListAdd(true);

    }

    private static void testArrayListAdd(boolean enableEnsureCapacity) {
        ArrayList<Object> list = new ArrayList<Object>();
        final int N = 10000000;
        long startTime = System.currentTimeMillis();
        if (enableEnsureCapacity) {
            list.ensureCapacity(N);
        }
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        long endTime = System.currentTimeMillis();
        if (enableEnsureCapacity) {
            System.out.println("使用ensureCapacity耗时：" + (endTime - startTime));
        } else {
            System.out.println("未使用ensureCapacity耗时：" + (endTime - startTime));
        }
    }

    private static void modifyClass() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //设置目标类的路径
        pool.insertClassPath("C:\\Users\\Administrator\\Desktop\\hyphenatechat_3.4.2\\") ;
        //获得要修改的类
        CtClass cc =pool.get("com.hyphenate.chat.EMClient");//就是对Login.class的映射
        //得到方法
        CtMethod m = cc.getDeclaredMethod("loadLibrary");
        //可以在函数的开头插入新的代码
        //m.insertBefore("{return true;}") ;
        //也可以直接将verify函数的内容设为return true;至于功能你懂的
        m.setBody("{if (!libraryLoaded)\n" +
                "    {\n" +
                "      _loadLibrary(\"sqlite\");\n" +
                "      _loadLibrary(\"hyphenate_av\");\n" +
                "      _loadLibrary(\"hyphenate_av_recorder\");\n" +
                "      _loadLibrary(\"hyphenate\");\n" +
                "      libraryLoaded = true;\n" +
                "    }}");
        //保存到文件里,会在项目根目录下生成一个Login.class,并没有自动替换classes/Login.class，需要自己手动替换进去
        cc.writeFile() ;
    }

}
