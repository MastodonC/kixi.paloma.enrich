#!/usr/bin/env bash
set -o errexit
set -o nounset
set -o xtrace

JAR_LOCATION=${1:-$JAR_LOCATION}

exec java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp -XX:ErrorFile=/tmp/hs_err_pid_%p.log -XX:+UseG1GC -Xloggc:/tmp/gc_%p.log -XX:+PrintGCCause -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=3 -XX:GCLogFileSize=2M -XX:+PrintGCDateStamps ${JAVA_OPTS:-} -cp $JAR_LOCATION kixi.paloma.enrich >&1
