git clean -xfd  && \
git submodule foreach --recursive git clean -xfd && \
git reset --hard  && \
git submodule foreach --recursive git reset --hard && \
git submodule update --remote && \
git submodule update --init --recursive --remote && \
export MAVEN_OPTS="-Xmx3056m -XX:MaxPermSize=128m" && \
mkdir build  && \
cd build  && \
cmake ..  && \
make  && \
cd .. && \
cd cdap-ambari-service && \
./build.sh && \
cd .. && \
cd cdap && \
mvn clean install -DskipTests -Dcheckstyle.skip && \
cd .. && \
mvn install -DskipTests -Dcheckstyle.skip=true -B -am -pl cdap/cdap-api -P templates && \
mvn install -DskipTests -Dcheckstyle.skip=true -B -am -f cdap/cdap-app-templates -P templates 
