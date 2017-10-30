FROM jenkins/jenkins:2.86
VOLUME /p9_backups
ENV TERRAFORM_VERSION=0.9.11

USER root
#Init ScriptsE
COPY init/* /usr/share/jenkins/ref/init.groovy.d/
#Add All Plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY ./config/FIRSTRUN.txt $JENKINS_HOME/FIRSTRUN.txt

RUN apt-get -yqq update && \
    apt-get install -yqq jq sudo curl vim libltdl7 jq python python-dev python-pip && \
    echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers && \
    echo "2.0" > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state && \
    echo "2.0" > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion && \
    pip install awscli

RUN /usr/local/bin/install-plugins.sh $(cat /usr/share/jenkins/ref/plugins.txt | tr '\n' ' ')


RUN  cd /tmp && wget -q https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    cp terraform /usr/bin/terraform

USER jenkins
