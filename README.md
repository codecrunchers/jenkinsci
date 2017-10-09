# Jenkins Build Server

AWS Aligned Jenkins for use with Planet 9 [P9 AWS Pipeline] (aws-pipeline-v2)

## To Build 
* `docker build -t planet9/jenkins .` or `docker build --no-cache -t planet9/jenkins .`

* Build lightweight devel image   `docker build  -t planet9/jenkins --file ./DockerfileLite .`

## To Run
* `docker run -d --env GIT_REPO=<to create an initial build job, pass a repo with a Jenkinsfile> \
--env GITHUB_APP_CLIENT_ID=<git client id>  \
--env GITHUB_APP_CLIENT_SECRET=<git app secret> \
--env AWS_ACCOUNT_ID = <aws #> \
--env  JENKINS_IP=  <jenkis url for this, is sent to slaves> \
--env  AWS_REGION=   \
--env  ECS_CLUSTER=  <name of ecs cluster hosting jenkins/slave farm> \
-p 18080:8080 -p 50000:50000 planet9/jenkins \`


## To Test
### Linux

Install python etc locally - or use a docker image to not pollute your local env
* `apt install -y python3-pip && /usr/bin/pip3 install --upgrade pip && /usr/bin/pip3 install pytest`
* `python3 -m pytest spec`

Leave 
* GITHUB_APP_CLIENT_ID &&   
* GITHUB_APP_CLIENT_SECRET blank for local access, using admin/admin

# Env Vars
* `GITHUB_APP_CLIENT_ID`
Use this for guithub authentication
* `GITHUB_APP_CLIENT_SECRET`
See above
* `ECS_CLUSTER`
The name of the ECS Cluster used for Slaves
* `AWS_ACCOUNT_ID`
So we can build the cluster name
* `JENKINS_IP`
IP for Slave->Master connection
* `AWS_REGION`
defaults to "eu-west-1";



