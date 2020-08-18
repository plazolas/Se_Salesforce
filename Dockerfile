FROM nimmis/java-centos:openjdk-8-jdk

MAINTAINER Oswaldo Plazola

WORKDIR /salesforce

ADD target /salesforce/target

COPY installMax.sh /salesforce/
COPY runLinuxTests.sh /salesforce/
COPY alias.sh /salesforce/

RUN yum install -y vim && \
    yum install -y wget && \
    yum install -y tar && \
    yum install -y unzip && \
    yum install -y gzip

# Your Salesforce Credentials here: 
ENV SALESFORCE_USERNAME=oswaldo.plazola@va.gov
ENV SALESFORCE_PASSWORD=A1s2d3f4g5h6J7@
ENV SALESFORCE_NAME=Oswaldo
# ###
ENV SALESFORCE_CONFIG_FILE=/salesforce/local.properties
ENV CLASSPATH="/salesforce/target/classes:/salesforce/selenium-java/client-combined-3.141.59.jar:/salesforce/selenium-java/libs/guava-25.0-jre.jar:/salesforce/selenium-java/libs/okhttp-3.11.0.jar:/salesforce/selenium-java/libs/okio-1.14.0.jar:/salesforce/selenium-java/libs/commons-exec-1.3.jar"
    
RUN chmod 755 /salesforce/installMax.sh && \
    chmod 755 /salesforce/runLinuxTests.sh

RUN sh /salesforce/installMax.sh

ENTRYPOINT [ "/usr/bin/bash", "/salesforce/runLinuxTests.sh" ]
    
