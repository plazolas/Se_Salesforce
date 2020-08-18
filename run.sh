#!/bin/bash
source properties-builder.sh
java  -Djavax.net.ssl.trustStore=$TRUST_STORE \
      -classpath $JAVA_CLASSPATH \
      gov.va.salesforceTests.TestApp $1
