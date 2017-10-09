FROM jenkins/jenkins
ENV DOCKER_COMPOSE_VERSION=1.15.0
ENV TERRAFORM_VERSION=0.9.11
ENV ITEM_ROOTDIR=/tmp/
USER root
#Init Scripts
COPY init/*.groovy /usr/share/jenkins/ref/init.groovy.d/
#Add All Plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY ./config/FIRSTRUN.txt $JENKINS_HOME/FIRSTRUN.txt



RUN apt-get update && \
    apt-get install -y sudo curl vim libltdl7 && \
    echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers && \
    apt-get install -y python python-dev python-pip && \
    pip install awscli && \
    /usr/local/bin/install-plugins.sh $(cat /usr/share/jenkins/ref/plugins.txt | tr '\n' ' ') && \
    cd /tmp && \
    wget -q https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip && \
    cp terraform /usr/bin/terraform && \
    echo "2.0" > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state && \
    echo "2.0" > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion

USER jenkins
