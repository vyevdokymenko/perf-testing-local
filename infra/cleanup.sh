#!/bin/bash
set -ex

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

sudo rm -rf $ROOT_DIR/grafana/grafana_data
sudo rm -rf $ROOT_DIR/grafana/.env_grafana

sudo rm -rf $ROOT_DIR/influxdb/influxdb_data
sudo rm -rf $ROOT_DIR/influxdb/.env_influxdb

sudo rm -rf $ROOT_DIR/jenkins/jenkins_home
sudo rm -rf $ROOT_DIR/jenkins/.env_jenkins

sudo rm -rf $ROOT_DIR/jmeter/jmeter_workspace
sudo rm -rf $ROOT_DIR/jmeter/.env_jmeter_agent

sudo rm -rf $ROOT_DIR/traefik/acme.json