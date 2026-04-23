#!/bin/bash

echo "Trying to get Jenkins agent secret..."

while [ -z "$JENKINS_AGENT_SECRET" ]; do
  JENKINS_AGENT_SECRET=$(curl -s -u "$JENKINS_ADMIN_USERNAME:$JENKINS_ADMIN_PASSWORD" \
    "$JENKINS_URL/computer/$JENKINS_AGENT_NAME/slave-agent.jnlp" | \
    grep -oP '(?<=<argument>)[a-f0-9]{64}(?=</argument>)')

  if [ -z "$JENKINS_AGENT_SECRET" ]; then
    echo "Secret not available yet. Retrying in 3 seconds..."
    sleep 3
  fi
done

echo "Got Jenkins agent secret: $JENKINS_AGENT_SECRET"

echo "Getting an agent from  Jenkins host..."
curl -L -o agent.jar ${JENKINS_URL}/jnlpJars/agent.jar

echo "Connecting an agent to the Jenkins..."
java -jar ./agent.jar -url ${JENKINS_URL} -secret ${JENKINS_AGENT_SECRET} -name ${JENKINS_AGENT_NAME} -workDir ${JENKINS_AGENT_WORKSPACE} -webSocket