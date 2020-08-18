# Performance and Regression Automation Test Framework for Salesforce Apps.


# TO RUN TEST ON LOCAL ENVIRONMENT

* 1.- Install Required Software: 

	- Java 8 openjdk version "1.8.0_41". 
	- Maven
	- Chrome Browser (latest VA approved version)
	- Git

* 2.- CLONE repo and UPDATE config file generator script __properties-builder.sh__

	- SALESFORCE_USERNAME
	- SALESFORCE_PASSWORD
	
* 3.- Your IP address __must be white listed__ or Salesforce will prompt for verification code on every login attempt per test run.
 
	- To whitelist your IP address follow instructions on this link :
			- https://starrdata.com/whitelisting-your-ip-address-in-salesforce/
			
* 4.- To install dependencies and compile framework (from gitbash) :
	- mvn clean install will compile and run all unit tests.
	- mvn clean install -DskipTests : installs dependencies and compile framework skipping unit tests.
	- mvn clean package -Djavax.net.ssl.trustStore=$PWD\\security\\cacerts : compiles and runs all unit tests
	- mvn clean jacoco:prepare-agent package jacoco:report -Djavax.net.ssl.trustStore=$PWD\\security\\cacerts :
		Compiles, runs all unit tests and creates coverage report in jacoco default directory. See https://mkyong.com/maven/maven-jacoco-code-coverage-example/
	- mvn clean -Dtest=ParticipantSearchTest test : compiles and runs one particular test in this case the ParticipantSearchTest test.
	- mvn javadoc:javadoc generates latest javadoc documents in target/site/apidocs directory

* 5.- Run Test: (from gitbash, cmd or IDE terminal)
	- . run.sh 1022 
	- NOTE: 1022 is the Jira story number for eMPWR-VA Project.
	- This will run Test1022ParticipantSearch class which runs the PERFORMANCE & REGRESSION TESTS for this Jira issue.
	
* 6.- After performnace test is completed, check stat reports exist in the reports directory

* 7.- RUN aggregate_reports.sh script to create latest aggregate metrics report for a Jira issues that were run.
	- . aggregate_reports.sh

# TO RUN TEST Using concurrent MINIKUBE PODS

* 1.- Install Java 8 openjdk version "1.8.0_41", Maven. 

* 2.- Disable Hyper-V and Install and Launch Oracle's Virtual Box.

* 3.- Install and launch Minikube.
	- minikube start --vm-driver="virtualbox" --memory 4096 --disk-size 20g	

* 4.- Change your tests parameters in the __INSTALLATION/CONFIG FILE__: installMax.sh
	- Prototype to test
	- Participant SSN and search name
	- Transaction search strings for app, appName should be: eMPWR-VA
	- __Number of users per test__
	- __Number of tests iterations per user__
	- Implicit wait time in seconds
	- Environment: docker
			 
* 5.- Change your user credentials in the __Dockerfile__ : Dockerfile
	- ENV SALESFORCE_USERNAME=yourusername@va.gov
	- ENV SALESFORCE_PASSWORD=youruserpassword
	- ENV SALESFORCE_NAME=yourname 
		
* 6.- Your IP address __must be white listed__ or Salesforce will prompt for verification code on every login attempt. 
	- To whitelist your IP address follow instructions on this link:
			- https://starrdata.com/whitelisting-your-ip-address-in-salesforce/

* 7.- SCALE number of containers to launch in file kubernetes/deployment.json. This can also be scaled using K8 GUI after the first pod is deployed. 

* 8.- RUN build_deploy.sh from command line to deploy the docker containers (K8 pods) to Minikube.

* 9.- Wait for containers to finish tests. (depends on how many pods, users and iterations)

* 10.- RUN kubernetes/get_reports.sh to download performance metrics reports

* 11.- RUN kubernetes/aggregate_reports.sh to create latest aggregate metrics report.

* 12.- DELETE the minikube deployment.


# FOR MORE DETAIL INSTRUCTIONS DOWNLOAD DOCUMENT: 
* https://github.ec.va.gov/EPMO/empwr_qa_selenium/blob/EMPWR-887/Instructions.docx
