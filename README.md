# Jenkins Build Server

[https://jenkins.io/solutions/pipeline/

## To Build and Run that image
* `docker build -t planet9/jenkins .` or `docker build --no-cache -t planet9/jenkins .`

* Build lightweight devel image   `docker build  -t planet9/jenkins --file ./DockerfileLite .`


* `docker run -d 
	--env GIT_REPO=<git@github.com:codecrunchers/helloworld-npm.git>
	--env GITHUB_APP_CLIENT_ID=<git client id> 
	--env GITHUB_APP_CLIENT_SECRET=<git app secret>
	-p 18080:8080 planet9/jenkins`


## To Test
### Linux
Install python etc locally - or use a docker image to not pollute your local env
* `apt install -y python3-pip && /usr/bin/pip3 install --upgrade pip && /usr/bin/pip3 install pytest`
* `python3 -m pytest spec`

Leave GITHUB_APP_CLIENT_ID && GITHUB_APP_CLIENT_SECRET blank for local access, using admin/admin
 

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

*
*
*
 



