#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

# --- InfluxDB ---
cd "$ROOT_DIR/influxdb"
[ -f .env_influxdb ] || cp .env_influxdb_example .env_influxdb
mkdir -p influxdb_data

# --- Grafana ---
cd "$ROOT_DIR/grafana"
[ -f .env_grafana ] || cp .env_grafana_example .env_grafana
mkdir -p grafana_data

# --- Jenkins ---
cd "$ROOT_DIR/jenkins"
[ -f .env_jenkins ] || cp .env_jenkins_example .env_jenkins
mkdir -p jenkins_home
chmod +x jenkins_setup.sh

# --- JMeter ---
cd "$ROOT_DIR/jmeter"
[ -f .env_jmeter_agent ] || cp .env_jmeter_agent_example .env_jmeter_agent
mkdir -p jmeter_workspace
chmod +x jmeter_agent.sh

cd "$ROOT_DIR"

podman build -f jmeter/jmeter.Dockerfile -t jmeter_agent_5.6.3:local jmeter

podman compose up -d