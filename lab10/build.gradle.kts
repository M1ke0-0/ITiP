import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.ByteArrayOutputStream

plugins {
    id("java")
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.example.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":string-utils"))
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.9")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes(Pair("Main-Class", "org.example.Main"))
    }
}

// Task 5: PrintInfoTask
abstract class PrintInfoTask : DefaultTask() {
    @TaskAction
    fun printInfo() {
        println("======================================")
        println("Это моя первая пользовательская задача!")
        println("Проект: ${project.name}")
        println("Версия Gradle: ${project.gradle.gradleVersion}")
        println("======================================")
    }
}

tasks.register<PrintInfoTask>("printInfo") {
    group = "Custom"
    description = "Выводит информацию о проекте"
}

// Task 5 & 7: GenerateBuildInfoTask
abstract class GenerateBuildInfoTask : DefaultTask() {

    @get:Input
    abstract val gitCommitHash: Property<String>

    @TaskAction
    fun generate() {
        val buildDir = project.layout.buildDirectory.dir("generated/resources/main").get().asFile
        buildDir.mkdirs()
        val file = File(buildDir, "build-passport.properties")

        var buildNumber = 1
        val props = java.util.Properties()
        if (file.exists()) {
            file.inputStream().use { props.load(it) }
            val prev = props.getProperty("build.number", "0").toIntOrNull() ?: 0
            buildNumber = prev + 1
        }

        val userName = System.getenv("USERNAME") ?: System.getenv("USER") ?: "Unknown"
        val osName = System.getProperty("os.name")
        val javaVersion = System.getProperty("java.version")
        val gradleVersion = project.gradle.gradleVersion
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        file.writeText("""
            build.user=$userName
            build.os=$osName
            build.java.version=$javaVersion
            build.gradle.version=$gradleVersion
            build.date=$dateTime
            build.message=Hello from generated build info!
            build.number=$buildNumber
            build.git.commit=${gitCommitHash.get()}
        """.trimIndent())
    }
}

tasks.register<GenerateBuildInfoTask>("generateBuildInfo") {
    group = "Custom"
    description = "Generates build passport properties"
    
    val hash = try {
        val stdout = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
            isIgnoreExitValue = true
        }
        stdout.toString().trim().takeIf { it.isNotEmpty() } ?: "unknown"
    } catch (e: Exception) {
        "unknown"
    }
    
    gitCommitHash.set(hash)
}

sourceSets {
    main {
        resources {
            srcDir(project.layout.buildDirectory.dir("generated/resources/main"))
        }
    }
}

tasks.named("processResources") {
    dependsOn("generateBuildInfo")
}
