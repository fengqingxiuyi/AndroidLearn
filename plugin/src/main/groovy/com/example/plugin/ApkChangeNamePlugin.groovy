package com.example.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * APK文件名替换插件
 */
class ApkChangeNamePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (!project.android) {
            throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!');
        }
        project.android.applicationVariants.all {
            variant ->
                variant.outputs.all {
                    outputFileName = "${variant.name}-${variant.versionName}.apk"
                }
        }
    }
}
