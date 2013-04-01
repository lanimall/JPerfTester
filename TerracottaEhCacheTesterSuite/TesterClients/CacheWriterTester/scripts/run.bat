@echo off

set APP_PROPS=app.properties
set TC_HOME=
set LOG_PROPS=log4j.properties
set license_path=$HOME/terracotta-license.key
set logs_home=/tmp
set ehcacheconfig=ehcache.xml
set params=
set execClass=org.terracotta.utils.perftester.cache.cachewritertester.Launcher

set JAVA_OPTS=-Xms512m -Xmx512m -XX:+UseParallelOldGC -XX:+UseCompressedOops -XX:MaxDirectMemorySize=10G
set JAVA_OPTS=%JAVA_OPTS% -Dlog4j.configuration=file:log4j.properties -verbose:gc -Xloggc:$logs_home/run.gc.log -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintGCTimeStamps
rem set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

"%JAVA_HOME%\bin\java" %JAVA_OPTS% -Dapp.config.path=file:%APP_PROPS% -Dlog4j.configuration=file:%LOG_PROPS% -Dehcache.config.path=%ehcacheconfig% -Dorg.terracotta.license.path=%license_path% -cp "libs/*;%TC_HOME%/common/*;%TC_HOME%/ehcache/lib/*" %execClass% %params%