#!/bin/bash
# Install jenkins plugins

set -e

/usr/bin/jenkins-plugin-cli --plugins \
configuration-as-code \
matrix-auth \
credentials \
git \
workflow-aggregator \
role-strategy \
job-dsl

mkdir -p ~/.ssh && ssh-keyscan github.com > ~/.ssh/known_hosts
     


