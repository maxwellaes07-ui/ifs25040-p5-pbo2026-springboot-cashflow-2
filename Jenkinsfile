pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        MAVEN_VERSION = '3.9.9'
        JDK_MAJOR     = '21'
        TOOLS_DIR     = "${env.JENKINS_HOME ?: '/var/jenkins_home'}/.cashflow-tools"
        MAVEN_OPTS    = '-Dmaven.repo.local=${WORKSPACE}/.m2/repository -Xmx512m'
    }

    stages {
        stage('Setup Tools') {
            steps {
                // Server Jenkins tidak punya Maven (exit code 127: mvn not found),
                // jadi Maven & JDK 21 disiapkan otomatis bila belum tersedia.
                sh '''#!/bin/sh
set -e
mkdir -p "$TOOLS_DIR"

fetch() { # url output
    if command -v curl >/dev/null 2>&1; then curl -fsSL --retry 3 -o "$2" "$1";
    elif command -v wget >/dev/null 2>&1; then wget -q -O "$2" "$1";
    else echo "curl/wget tidak tersedia di Jenkins"; exit 1; fi
}

# ---------- JDK 21 ----------
need_jdk=1
if command -v java >/dev/null 2>&1; then
    v=$(java -version 2>&1 | sed -n '1s/.*"\\([0-9]*\\)\\..*/\\1/p;1s/.*"\\([0-9]*\\)".*/\\1/p' | head -1)
    [ -n "$v" ] && [ "$v" -ge "$JDK_MAJOR" ] && need_jdk=0
fi
if [ "$need_jdk" = "1" ]; then
    if [ ! -x "$TOOLS_DIR/jdk/bin/java" ]; then
        case "$(uname -m)" in aarch64|arm64) arch=aarch64;; *) arch=x64;; esac
        echo ">> Mengunduh JDK $JDK_MAJOR ($arch)"
        fetch "https://api.adoptium.net/v3/binary/latest/$JDK_MAJOR/ga/linux/$arch/jdk/hotspot/normal/eclipse" "$TOOLS_DIR/jdk.tgz"
        rm -rf "$TOOLS_DIR/jdk" "$TOOLS_DIR/jdk-tmp"; mkdir -p "$TOOLS_DIR/jdk-tmp"
        tar xzf "$TOOLS_DIR/jdk.tgz" -C "$TOOLS_DIR/jdk-tmp" --strip-components=1
        mv "$TOOLS_DIR/jdk-tmp" "$TOOLS_DIR/jdk"; rm -f "$TOOLS_DIR/jdk.tgz"
    fi
fi

# ---------- Maven ----------
if ! command -v mvn >/dev/null 2>&1; then
    if [ ! -x "$TOOLS_DIR/maven/bin/mvn" ]; then
        echo ">> Mengunduh Maven $MAVEN_VERSION"
        fetch "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.tar.gz" "$TOOLS_DIR/maven.tgz"
        rm -rf "$TOOLS_DIR/maven" "$TOOLS_DIR/maven-tmp"; mkdir -p "$TOOLS_DIR/maven-tmp"
        tar xzf "$TOOLS_DIR/maven.tgz" -C "$TOOLS_DIR/maven-tmp" --strip-components=1
        mv "$TOOLS_DIR/maven-tmp" "$TOOLS_DIR/maven"; rm -f "$TOOLS_DIR/maven.tgz"
    fi
fi
'''
            }
        }

        stage('Build & Test') {
            steps {
                sh '''#!/bin/sh
set -e
[ -x "$TOOLS_DIR/jdk/bin/java" ] && { export JAVA_HOME="$TOOLS_DIR/jdk"; export PATH="$JAVA_HOME/bin:$PATH"; }
[ -x "$TOOLS_DIR/maven/bin/mvn" ] && export PATH="$TOOLS_DIR/maven/bin:$PATH"
java -version
mvn -v
mvn -B -ntp clean verify
'''
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh '''#!/bin/sh
set -e
[ -x "$TOOLS_DIR/jdk/bin/java" ] && { export JAVA_HOME="$TOOLS_DIR/jdk"; export PATH="$JAVA_HOME/bin:$PATH"; }
[ -x "$TOOLS_DIR/maven/bin/mvn" ] && export PATH="$TOOLS_DIR/maven/bin:$PATH"
mvn -B -ntp package -Pproduction -DskipTests
'''
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: false
            }
        }
    }

    post {
        success { echo 'BUILD SUKSES: target/*.jar siap dideploy.' }
        failure { echo 'BUILD GAGAL: cek log di atas.' }
    }
}
