rootProject.name = "ServerCore"
rootProject.buildFileName = "build.gradle"

val subs = arrayOf(
    //    "api",
    "spigot",
    "modules",
//    "sponge",
//    "bungee",
    "common"//,
//    "velocity"
)

include(*subs)

subs.forEach {
    findProject(":$it")?.name = "${rootProject.name}-${it.capitalize()}"
}