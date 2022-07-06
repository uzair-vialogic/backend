pipeline {
    environment{
        dockerimagename = "vialogic/backend-support-test"
        dockerImage = ""
    }

    agent any 
    
    stages {
        stage('Build Image') { 
            steps {
                script{
                    dockerImage = docker.build dockerimagename
                }
            }
                
        }
    
        stage('Pushing Image') { 
            environment {
                registryCredential = 'dockerhublogin'
            }
            steps{
                script{
                    docker.withRegistry('https://registry.hub.docker.com',registryCredential){
                        dockerImage.push("latest")
                    }
                }
            }  
        }
        stage('Deploy') { 
            steps {
                echo"Deploying Successfull "
            }
        }
        
        
        
        
    }    
}

