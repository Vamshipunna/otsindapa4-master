 node{     
	 stage('Checkout') {         
         	 git url:'https://github.com/Vamshipunna/otsindapa4-master.git',branch: 'master'  
	 }
	 stage('build')  {
        	  sh 'cd ndap-eureka-server'    
	 	 sh  'mvn clean install'
	 }
}
