/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val com_android_tools_build_gradle: String = "3.5.0-alpha04"

    const val lint_gradle: String = "26.5.0-alpha04"

    const val timber: String = "4.7.1"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2"

    const val sentry_android: String = "1.7.20" // available: "1.7.20"

    const val org_jetbrains_kotlin: String = "1.3.21"

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.2.1"

        const val currentVersion: String = "5.2.1"

        const val nightlyVersion: String = "5.3-20190211022529+0000"

        const val releaseCandidate: String = ""
    }
}
