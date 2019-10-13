plugins {
    idea
    jacoco
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
    id("org.jetbrains.kotlin.kapt") version "1.3.50"
    id("org.springframework.boot") version "2.1.9.RELEASE" apply false
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.50" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.50" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
}

allprojects {
    repositories {
        gradlePluginPortal()
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
        }

        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
        }

        withType<Test> {
            useTestNG()
        }

        val codeCoverageReport by creating(JacocoReport::class) {
            executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

            subprojects.onEach {
                sourceSets(it.sourceSets["main"])
            }

            reports {
                xml.isEnabled = true
                xml.destination = File("$buildDir/reports/jacoco/report.xml")
                html.isEnabled = false
                csv.isEnabled = false
            }

            dependsOn("test")
        }
    }

    repositories {
        gradlePluginPortal()
        jcenter()
    }
}
