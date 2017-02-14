#!/usr/bin/env bash

rm book-me.zip || true
mvn compile && mvn package && 7z a book-me.zip .
