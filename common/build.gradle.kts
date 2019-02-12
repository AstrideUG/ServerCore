/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

dependencies {
    "jarLibs"("org.mongodb", "mongodb-driver-async", extra["versions.mongodb"].toString())
    "jarLibs"("com.google.inject", "guice", extra["versions.guice"].toString())
    "jarLibs"("com.google.code.gson", "gson", extra["versions.gson"].toString())
}

