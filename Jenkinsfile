#!/usr/bin/env groovy
@Library('jenkins_lib')_
pipeline {
  agent {label 'slave'}
  environment { 
   	DEB_COMPONENT = 'cdap'
	DEB_ARCH = 'amd64'
	DEB_POOL = 'gvs-dev-debian/pool/c'
	ARTIFACT_SRC1 = './cdap/**/target'
	ARTIFACT_SRC2 = './cdap-ambari-service/target'
	ARTIFACT_DEST1 = 'gvs-dev-debian/pool/c'
	}
  stages {
    stage("Define Release version"){
      steps {
      script {
        versionDefine()
        }
      }
    }
    stage("Build"){
    steps{
    script{
         buildsteps = load 'raf_cb.groovy'
    }}}
  }
	
post {
       always {
          reports_alerts('target/checkstyle-result.xml', 'target/surefire-reports/*.xml', '**/target/site/cobertura/coverage.xml', 'allure-report/', 'index.html')
     	  slackalert('jenkins-cdap-alerts')
       }
    }

}
