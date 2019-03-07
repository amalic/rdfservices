pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'docker build --rm -f "Dockerfile" -t servicefromdata:latest .'
      }
    }
    stage('Run') {
      steps {
        sh '''docker kill service-from-data
docker run -d --rm --name service-from-data -p 85:8080 --link graphdb:graphdb -e ENDPOINT="http://graphdb:7200/repositories/ncats-red-kg" servicefromdata'''
      }
    }
  }
}