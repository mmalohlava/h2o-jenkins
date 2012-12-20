#!/bin/bash
# install.sh -- created 2012-12-20, <+NAME+>
# @Last Change: 24-Dez-2004.
# @Revision:    0.0

mvn install -Dmaven.test.skip=true

java -jar ./sandbox/jenkins-cli.jar -s http://23.23.182.217:8080 install-plugin ./target/h2o-dashboard.hpi
# vi: 
