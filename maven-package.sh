#!/usr/bin/env bash

mvn package && 7z a book-me.7z . -x="*.7z"