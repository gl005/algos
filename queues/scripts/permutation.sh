#!/usr/bin/env bash

# It's currently a bit tedious to read from StdIn from an IntelliJ run configuration
javac -cp ../lib/algs4.jar:. -d ../target *.java;
cd ../target && time java -cp ../lib/algs4.jar:. Permutation "$@";