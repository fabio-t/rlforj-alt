plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.fabio-t"
version = "0.4.0"
description = "Roguelike Library For Java (Alternative version)"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        // the codebase predates javadoc's strict lint; don't fail the build on it
        addBooleanOption("Xdoclint:none", true)
        addStringOption("-quiet")
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name = "Roguelike Library For Java (Alternative version)"
                description = "FOV, LOS and pathfinding algorithms for roguelikes and other grid-based games"
                url = "https://github.com/fabio-t/rlforj-alt"

                licenses {
                    license {
                        name = "BSD-3-Clause"
                        url = "https://spdx.org/licenses/BSD-3-Clause.html"
                    }
                }

                developers {
                    developer {
                        id = "fabio-t"
                        name = "Fabio Ticconi"
                        email = "fabio.ticconi@gmail.com"
                    }
                }

                scm {
                    connection = "scm:git:https://github.com/fabio-t/rlforj-alt.git"
                    developerConnection = "scm:git:git@github.com:fabio-t/rlforj-alt.git"
                    url = "https://github.com/fabio-t/rlforj-alt"
                }
            }
        }
    }

    repositories {
        // publish releases to GitHub Packages: ./gradlew publish
        // (requires GITHUB_ACTOR/GITHUB_TOKEN, e.g. from a GitHub Actions run)
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fabio-t/rlforj-alt")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.key") as String?
            }
        }
    }
}

tasks.register<JavaExec>("demo") {
    group = "application"
    description = "Runs the ASCII demo (FOV, cone FOV and LOS examples)"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "rlforj.examples.Demo"
}
