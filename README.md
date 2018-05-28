# java-code-guru


## install java

	$ sudo vi /etc/profile.d/jdk1.8.sh

		export JAVA_HOME=/usr/local/lib/java/jdk1.8.0_152
		export JRE_HOME=$JAVA_HOME/jre
		export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
		export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin


## install maven

	$ sudo vi /etc/profile.d/maven3.3.sh 
	
		export M2_HOME=/usr/local/lib/maven/apache-maven-3.3.3
		export MAVEN_OPTS="-Xms256m -Xmx512m -Dfile.encoding=UTF-8"
		export PATH=$PATH:$M2_HOME/bin


## build and generate jar

	$ mvn clean compile package
