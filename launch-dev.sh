#!/usr/bin/env bash

mvn compile && mvn package && java -Djava.awt.headless=false -jar target/bookingsystem-1.0.jar
