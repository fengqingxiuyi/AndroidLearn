package com.example.lintjar

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class LogDetectorTest : LintDetectorTest() {
    fun testBasic() {
        lint().files(
            java("""
                    package com.example.learn.lint;
                    import android.util.Log;
                    public class LintTestJava {
                        private void testLint() {
                            Log.i("tag", "msg");
                            System.out.println("sout");
                        }
                    }
                    """
            ).indented(),
            kotlin("""
                    package com.example.learn.lint
                    import android.util.Log
                    class LintTestKotlin {
                        private fun testLint() {
                            Log.i("tag", "msg")
                            println("sout")
                        }
                    }
                    """
            ).indented())
            .run()
            .expect("""
                    src/com/example/learn/lint/LintTestJava.java:5: Warning: 使用LogUtil类替换原生Log [LogUsage]
                            Log.i("tag", "msg");
                            ~~~~~~~~~~~~~~~~~~~
                    src/com/example/learn/lint/LintTestJava.java:6: Warning: 使用LogUtil类替换System.out.print [LogUsage]
                            System.out.println("sout");
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/example/learn/lint/LintTestKotlin.kt:5: Warning: 使用LogUtil类替换原生Log [LogUsage]
                            Log.i("tag", "msg")
                            ~~~~~~~~~~~~~~~~~~~
                    src/com/example/learn/lint/LintTestKotlin.kt:6: Warning: 使用LogUtil类替换System.out.print [LogUsage]
                            println("sout")
                            ~~~~~~~~~~~~~~~
                    0 errors, 4 warnings
                    """
            )
    }

    override fun getDetector(): Detector {
        return LogDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(LogDetector.ISSUE)
    }
}