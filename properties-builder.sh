#!/bin/bash

export SALESFORCE_NAME=$SALESFORCE_NAME
export SALESFORCE_USERNAME=$SALESFORCE_USERNAME
export SALESFORCE_PASSWORD=$SALESFORCE_PASSWORD

if [[ "$OSTYPE" == "msys" ]]; then
  export RUN=local
  export PLATFORM=windows
  export ENVIRONMENT=dev
  export LOGGING_LEVEL=DEBUG
  export MY_PWD=$(echo "$PWD" | sed 's/^\///' | sed 's/\//\\/g' | sed 's/^./\0:/' | sed 's/\\/\\\\/g' | sed 's/c/C/')
  export WORK_DIR=$MY_PWD
else
  export RUN=linux
  export PLATFORM=linux
  export ENVIRONMENT=uat
  export LOGGING_LEVEL=INFO
  export WORK_DIR=/salesforce
fi

case $ENVIRONMENT in
	dev)
		export SALESFORCE_URL=https://va--fasaoa2dev.my.salesforce.com
		;;
	uat)
		export SALESFORCE_URL=https://va--fasaoa1uat.my.salesforce.com
		;;
  prod)
		export SALESFORCE_URL=https://empwr.salesforce.com
		;;
	*)
		echo "Sorry, No valid running env"
		exit
		;;
  esac

export SALESFORCE_CONFIG_FILE="$WORK_DIR\\\\local.properties"
export SALESFORCE_REPORTS_DIR="$WORK_DIR\\\\reports\\\\"
export TRUST_STORE="$WORK_DIR\\\\security\\\\cacerts"
export CHROME_LOG_FILE="$WORK_DIR\\\\chrome.log"
export OUTPUT_LOG="$WORK_DIR\\\\out.log"
export JAVA_CLASSPATH="$WORK_DIR\target\classes;$WORK_DIR\selenium-java\guava-25.0-jre.jar;$WORK_DIR\selenium-java\okhttp-3.11.0.jar;$WORK_DIR\selenium-java\okio-1.14.0.jar;$WORK_DIR\selenium-java\commons-exec-1.3.jar;$WORK_DIR\selenium-java\selenium-api-3.141.59.jar;$WORK_DIR\selenium-java\selenium-firefox-driver-3.141.59.jar;$WORK_DIR\selenium-java\selenium-chrome-driver-3.141.59.jar;$WORK_DIR\selenium-java\selenium-remote-driver-3.141.59.jar;$WORK_DIR\selenium-java\commons-codec-1.14.jar;$WORK_DIR\selenium-java\selenium-support-3.10.0.jar;$HOMEDRIVE$HOMEPATH\.m2\repository\com\google\code\gson\gson\2.8.2\gson-2.8.2.jar;"

cat > local.properties <<EOF
#User
user.name=$SALESFORCE_NAME
user.username=$SALESFORCE_USERNAME
user.password=$SALESFORCE_PASSWORD

# platform options: linux, windows
test.platform=$PLATFORM
# run options: local, linux, kubernetes
test.run=$RUN
# env options: dev, uat, prod
test.env=$ENVIRONMENT

test.appName=eMPWR-VA
test.homepage=https://va--fasaoa2dev.lightning.force.com/
test.homepageTitle1=Salesforce

test.reportsPath=$SALESFORCE_REPORTS_DIR
test.output_log=$OUTPUT_LOG
test.WORK_DIR=$WORK_DIR

test.url=$SALESFORCE_URL
test.logoutURL=$SALESFORCE_URL/secur/logout.jsp
test.getAuthTokenUrl=$SALESFORCE_URL/services/oauth2/authorize?
test.getAccessTokenUrl=$SALESFORCE_URL/services/oauth2/token
test.redirect_uri=https://va--fasaoa2dev.lightning.force.com/lightning/page/home
test.getAccountDataUrl=$SALESFORCE_URL/services/data/v20.0/sobjects/Account/
test.response_type=code
test.client_id=3MVG9Zdl7Yn6QDKOdmJFkJHLLCtql9a6jk_tzzV2VWXsFBePaKI_KwudTDw.YBbeytXdMLCBmTNsw6A3YjI3g
test.client_secret=AFA63D2342F7F81CEADDD07A708E6528EDB81D7FAAF962FEB1724A778B090B73
test.grant_type=authorization_code
test.truststore=$TRUST_STORE

########### WebDriver Settings ###########
webdriver.type=chrome
webdriver.mode=headless
test.number_of_tests=1
test.number_of_users=1
test.implicitWait=90
test.polling=50
##########################################

# Selenium WebDriver Location for local runs
gecko.driver.windows.path=webdrivers/geckodriver-v0.26.0-win64/geckodriver.exe
chrome.driver.windows.path=webdrivers/chromedriver_win32_83.0.4103.39/chromedriver.exe
ie.driver.windows.path=webdrivers/IEDriverServer.exe

# Selenium WebDriver location for minikube runs
webdriver.path.firefox=drivers/geckodriver-v0.26.0-win64/geckodriver.exe
webdriver.path.chrome=drivers/chromedriver_win32_81.0.4044.113/chromedriver.exe
webdriver.path.ie=drivers/IEDriverServer_x64_3.150.1/IEDriverServer.exe

#Logging
logging.level.root=$LOGGING_LEVEL

EOF

echo JAVA_CLASSPATH
echo "$JAVA_CLASSPATH"
echo TRUST_STORE
echo $TRUST_STORE
echo WORK_DIR
echo $WORK_DIR
echo SALESFORCE_CONFIG_FILE
echo $SALESFORCE_CONFIG_FILE

echo "
===========================================
Properties file created ./local.properties
===========================================
"
