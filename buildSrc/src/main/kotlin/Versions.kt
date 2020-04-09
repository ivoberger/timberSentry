/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val com_android_tools_build_gradle: String = "3.6.2"

    const val lint_gradle: String = "26.6.2"

    const val com_github_dcendents_android_maven_gradle_plugin: String = "2.1"

    const val timber: String = "4.7.1"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.7.0"

    const val sentry_android: String = "1.7.21"

    const val org_jetbrains_kotlin: String = "1.3.71"

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "6.3"

        const val currentVersion: String = "6.3"

        const val nightlyVersion: String = "6.5-20200408220925+0000"

        const val releaseCandidate: String = ""
    }
}
