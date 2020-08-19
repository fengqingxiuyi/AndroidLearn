package com.example.lintjar

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.getContainingUFile
import org.jetbrains.uast.kotlin.KotlinUFunctionCallExpression

/**
 * @author fqxyi
 * @date 2020/8/19
 * 避免使用Log / System.out.println，提醒使用LogUtil
 */
@Suppress("UnstableApiUsage")
class LogDetector : Detector(), Detector.UastScanner {

    private val logMethodNameList = listOf("v", "d", "i", "w", "e", "println")

    override fun getApplicableUastTypes(): List<Class<out UElement?>>? {
        return listOf(UClass::class.java, UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            //获取整个文件的字符串数据
            var fileString: String? = null

            override fun visitClass(node: UClass) {
                fileString = node.getContainingUFile()?.asSourceString()
            }

            override fun visitCallExpression(node: UCallExpression) {
                //过滤文件
                if (fileString?.contains("static final fun main() : void") == true) {
                    return
                }
                //
                val sourceString = node.asSourceString()
                val sourceParentString = node.uastParent?.asSourceString() ?: ""
                val methodName = node.methodName ?: ""
                val includeMethodName = logMethodNameList.contains(methodName)
                //判断文件类型
                when (node) {
                    is KotlinUFunctionCallExpression -> { //kotlin
                        //判断System.out.print
                        if (sourceString.startsWith("System.out.print") || sourceString.startsWith("print")) {
                            //message会在鼠标悬浮时显示
                            context.report(ISSUE, node, context.getLocation(node),
                                "使用LogUtil类替换System.out.print")
                        }
                        //判断Log
                        if (includeMethodName && (
                                    sourceParentString.startsWith("android.util.Log.$methodName") ||
                                    (fileString?.contains("import android.util.Log") == true && sourceParentString.startsWith("Log.$methodName")) ||
                                    (fileString?.contains("import android.util.Log.$methodName") == true && sourceString.startsWith(methodName))
                                    )
                        ) {
                            //message会在鼠标悬浮时显示
                            context.report(ISSUE, node, context.getLocation(node),
                                "使用LogUtil类替换原生Log")
                        }
                    }
                    else -> { //java
                        //判断System.out.print
                        if (sourceString.startsWith("System.out.print")) {
                            //message会在鼠标悬浮时显示
                            context.report(ISSUE, node, context.getLocation(node),
                                "使用LogUtil类替换System.out.print")
                        }
                        //判断Log
                        if (includeMethodName && (
                                    sourceParentString.startsWith("android.util.Log.$methodName") ||
                                    (fileString?.contains("import android.util.Log") == true && sourceParentString.startsWith("Log.$methodName")) ||
                                    (fileString?.contains("import static android.util.Log.$methodName") == true && sourceString.startsWith(methodName))
                                    )
                        ) {
                            //message会在鼠标悬浮时显示
                            context.report(ISSUE, node, context.getLocation(node),
                                "使用LogUtil类替换原生Log")
                        }
                    }
                }
            }
        }
    }

    companion object {
        /** Issue describing the problem and pointing to the detector implementation */
        @JvmField
        val ISSUE: Issue = Issue.create(
            //Lint Report文档Overview选项ID，同时也可以以`@SuppressLint("LogUsage")`的形式用于压制警告
            id = "LogUsage",
            //Lint Report文档自定义的选项标题
            briefDescription = "Log Mentions",
            //鼠标悬浮时，会显示一个弹框，弹框右侧有三个点的图标，点击后再选择Show Inspection Description会显示
            explanation = """
                    请使用LogUtil替换Log和System.out提供的打印函数，避免在正式包中打印Log
                    """,
            category = Category.SECURITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                LogDetector::class.java,
                Scope.JAVA_FILE_SCOPE)
        )
    }

}