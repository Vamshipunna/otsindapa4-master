 node{     
	 stage('Checkout') {         
         	 git url:'https://github.com/Vamshipunna/otsindapa4-master.git',branch: 'master'  
	 }
	 stage('build')  {
        	 sh  'mvn -f ndap-eureka-server/pom.xml clean install'
		 sh  'mvn -f ndap-user-service/pom.xml clean install'
		 sh  'mvn -f ndap-auth-service/pom.xml clean install'
		 sh  'mvn -f ndap-zuul-proxy/pom.xml clean install'
	 }
}
