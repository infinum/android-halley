package com.infinum.halley.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class HalleyPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit =
        with(project) {
            addDependencies(this)
        }

    private fun addDependencies(project: Project) {
        with(project) {
            dependencies.add("implementation", "com.infinum.halley:halley-core:$halleyVersion")

            if (pluginManager.hasPlugin("org.jetbrains.kotlin.plugin.serialization").not()) {
                pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            }
        }
    }
}
