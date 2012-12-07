APP_PROPS=app.properties
LOG_PROPS=log4j.properties
license_path=$HOME/terracotta-license.key
logs_home=/tmp
ehcacheconfig=ehcache.xml
params=$*
execClass="org.terracotta.utils.perftester.sampleclient.launcher.Launcher"

JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseParallelOldGC -XX:+UseCompressedOops -XX:MaxDirectMemorySize=10G"
JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=file:log4j.properties -verbose:gc -Xloggc:$logs_home/run.gc.log -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintGCTimeStamps"
#JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=7070 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

java $JAVA_OPTS -Dapp.config.path=file:$APP_PROPS -Dlog4j.configuration=file:$LOG_PROPS -Dehcache.config.path=$ehcacheconfig -Dorg.terracotta.license.path=$license_path -cp "libs/*" $execClass $params | tee $logs_home/run.log