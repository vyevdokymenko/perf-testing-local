FROM eclipse-temurin:21-jdk-jammy

RUN apt-get update && apt-get install -y git curl unzip nano && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN useradd --create-home jmeter_agent --shell /bin/bash
RUN mkdir /home/jmeter_agent/jenkins_workspace && chmod 777 /home/jmeter_agent/jenkins_workspace

RUN cd /home/jmeter_agent/ && \
curl https://dlcdn.apache.org/jmeter/binaries/apache-jmeter-5.6.3.zip -o apache-jmeter-5.6.3.zip && \
unzip apache-jmeter-5.6.3.zip && \
rm apache-jmeter-5.6.3.zip

COPY eu-sign-1.3.184.jar /home/jmeter_agent/apache-jmeter-5.6.3/lib/
COPY eu-sign-1.3.184.jar /home/jmeter_agent/apache-jmeter-5.6.3/lib/ext/

WORKDIR /home/jmeter_agent/

RUN mkdir -p .ssh && ssh-keyscan github.com >> .ssh/known_hosts
RUN chown -R jmeter_agent:jmeter_agent /home/jmeter_agent/

USER jmeter_agent
