#!/bin/bash

__directory="$(readlink -f "$(dirname "${BASH_SOURCE[0]}")")"

java -jar ${__directory}/quarkus-run.jar