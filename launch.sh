#!/usr/bin/env bash

mvn package && java -Djava.awt.headless=false -jar target/bookingsystem-1.0.jar
