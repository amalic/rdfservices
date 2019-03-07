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
        sh 'docker run -it --rm -p 80:8080 servicefromdata'
      }
    }
  }
}