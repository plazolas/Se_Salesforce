#!/bin/bash

##### create local.properties config file
cat > $SALESFORCE_CONFIG_FILE <<EOF
#User
user.name=$SALESFORCE_NAME
user.username=$SALESFORCE_USERNAME
user.password=$SALESFORCE_PASSWORD

# WebDriver Settings
webdriver.type=chrome
webdriver.path.firefox=/usr/local/bin/geckodriver
webdriver.path.chrome=/usr/local/bin/chromedriver

# Participant
participant.ssn=137124444
participant.searchname=Martha D

# Transactions
transaction.search.MuleSoft=06A
transaction.search.ORDS=06A

# Tests
test.prototype=ORDS
test.platform=linux
test.environment=docker
test.number_of_tests=2
test.number_of_users=2
test.implicitWait=200
test.url=https://va--fasaoa2dev.my.salesforce.com
test.logout=https://va--fasaoa2dev.lightning.force.com/secur/logout.jsp
test.reportsPath=/salesforce/
test.output_log=/salesforce/out.log
test.transaction_url=https://va--fasaoa2dev.lightning.force.com/lightning/r/Transaction__c/aCrr000000076aBCAQ/view
test.tran_xpath_ORDS=//a[@title = '06A-{0002}']
test.tran_xpath_MuleSoft=//div[@class='slds-truncate']

# Selenium Properties

selenium.revision=3.141.59
geckodriver.revision=0.26.0
chromedriver.revision=2.33
iedriver.revision=11.0

firefox.revision=57.0
chrome.revision=62.0
ie.revision=11.0

gecko.driver.windows.path=geckodriver-v0.26.0-win64/geckodriver.exe
chrome.driver.windows.path=drivers/chromedriver.exe
ie.driver.windows.path=drivers/IEDriverServer.exe

#Logging
logging.level.root=ERROR

EOF

browser=`cat $SALESFORCE_CONFIG_FILE | grep type | grep webdriver | sed 's/webdriver\.type=//'`

#### install selenium libs
wget https://selenium-release.storage.googleapis.com/3.141/selenium-java-3.141.59.zip
unzip selenium-java-3.141.59.zip -d selenium-java
chmod -R 755 selenium-java
rm -f selenium-java-3.141.59.zip

if [ $browser == "firefox" ] ; then
    #### install geckodriver
    wget https://github.com/mozilla/geckodriver/releases/download/v0.26.0/geckodriver-v0.26.0-linux64.tar.gz
    tar xvzf geckodriver-v0.26.0-linux64.tar.gz -C /usr/local/bin/
    rm -f geckodriver-v0.26.0-linux64.tar.gz
    chown root:root /usr/local/bin/geckodriver
    chmod 755 /usr/local/bin/geckodriver

    ##### install Firefox & its plugins
    yum install -y firefox
fi
if [ $browser == "chrome" ] ; then
    #### install chromedriver
     Install ChromeDriver.
    wget https://chromedriver.storage.googleapis.com/2.35/chromedriver_linux64.zip
    unzip chromedriver_linux64.zip
    rm -f chromedriver_linux64.zip
    mv -f chromedriver /usr/local/bin/chromedriver
    chown root:root /usr/bin/local/chromedriver
    chmod 755 /usr/bin/local/chromedrive

    ##### install Chrome
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm
    yum install -y google-chrome-stable_current_x86_64.rpm
    rm -f google-chrome-stable_current_x86_64.rpm
fi
yum install -y http://linuxdownload.adobe.com/linux/x86_64/adobe-release-x86_64-1.0-1.noarch.rpm
wget http://vault.centos.org/6.2/os/x86_64/Packages/xorg-x11-server-Xvfb-1.10.4-6.el6.x86_64.rpm
yum install -y xorg-x11-server-Xvfb-1.10.4-6.el6.x86_64.rpm
rm -f xorg-x11-server-Xvfb-1.10.4-6.el6.x86_64.rpm
yum install -y xauth
yum update -y

mkdir /root/.cache
echo "created .cache dir"

mkdir /root/.cache/dconf
echo "created .cache/dconf dir"

chmod 777 /root/.cache
chmod 777 /root/.cache/dconf


