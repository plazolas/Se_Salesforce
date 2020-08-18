#!/bin/bash
xvfb-run -a java -cp $CLASSPATH  gov.va.salesforceTests.TestApp > out.log 2>&1
tail -f /dev/null