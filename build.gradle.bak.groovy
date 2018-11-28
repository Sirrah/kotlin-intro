plugins {
    id 'java'
    id 'org.jetbrains.kotlin.jvm' version '1.3.10'
}

repositories {
    jcenter()
}

dependencies {
    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10"
    testCompile group: 'junit', name: 'junit', version: '4.11'
}
