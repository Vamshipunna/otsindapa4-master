 node{     
	 stage('Checkout') {         
         	 git url:'https://github.com/Vamshipunna/otsindapa4-master.git',branch: 'master'  
	 }
	 stage('build')  {
        	  sh 'cd ndap-eureka-server' 
		 sh 'ls'
		 sh 'pwd'
	 	 sh  'mvn -f ndap-eureka-server/pom.xml clean install'
	 }
}
