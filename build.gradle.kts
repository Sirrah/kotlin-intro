plugins {
    kotlin("jvm") version "1.3.10"
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.11")
}