echo "Building.."
sh"""git clean -xfd  && \
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
mvn install -DskipTests -Dcheckstyle.skip=true -B -am -f cdap/cdap-app-templates -P templates && \
rm -rf ${env.WORKSPACE}/cdap/*/target/*.rpm  && \
rm -rf ${env.WORKSPACE}/ansible_rpm/*.rpm  && \
mvn package -P examples,templates,dist,release,rpm-prepare,rpm,deb-prepare,deb \
-DskipTests \
-Dcheckstyle.skip=true \
-Dadditional.artifacts.dir=${env.WORKSPACE}/app-artifacts \
-Dsecurity.extensions.dir=${env.WORKSPACE}/security-extensions -DbuildNumber=${env.RELEASE}"""


def scannerHome = tool 'sonar';
withSonarQubeEnv('sonar') {
echo "sonar"
sh 'cd ${WORKSPACE}/cdap && mvn sonar:sonar'
//timeout(time: 1, unit: 'HOURS') {
//def qg = waitForQualityGate()
//if (qg.status != 'OK') {
//error "Pipeline aborted due to quality gate failure: ${qg.status}"
//}}
}

rpm_push( env.buildType, '${WORKSPACE}/cdap/**/target', 'ggn-dev-rpms/cdap-build' )
rpm_push( env.buildType, '${WORKSPACE}/cdap-ambari-service/target', 'ggn-dev-rpms/cdap-build' )
rpm_push( env.buildType, '${WORKSPACE}', 'ggn-dev-rpms/cdap-build' )
deb_push(env.buildType, 'cdap/**/target', 'gvs-dev-debian/pool/c' )
deb_push(env.buildType, 'cdap-ambari-service/target', 'gvs-dev-debian/pool/c' ) 
