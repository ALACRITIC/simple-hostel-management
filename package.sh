#!/usr/bin/env bash

# Exit on non zero value
set -e

# Compile and create jar
mvn compile
mvn package

# package in zip
mkdir -p releases/booking-system

cd releases/
cp ../target/bookingsystem-1.0.jar   booking-system/
cp ../launch-linux.sh                booking-system/
cp ../launch-windows.bat             booking-system/
cp ../README.md                      booking-system/

7z a book-me-latest.zip booking-system

rm -rf booking-system