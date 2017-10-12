import java.util.Arrays
import java.util.logging.Logger
import com.cloudbees.jenkins.plugins.amazonecs.ECSTaskTemplate
import jenkins.model.*
import com.cloudbees.jenkins.plugins.amazonecs.ECSCloud

Logger logger = Logger.getLogger("ecs-cluster")
logger.info("Loading Jenkins")
instance = Jenkins.getInstance()
def clouds = instance.clouds

if(clouds.size() > 0){
    logger.info("Cloud in Place")
    return
}


String accountId = System.getenv("AWS_ACCOUNT_ID")?:null;
String ecsClusterName = System.getenv("ECS_CLUSTER")?:null;
String jenkinsIP = new URL("http://169.254.169.254/latest/meta-data/local-ipv4").getText()?:null
String awsRegion = System.getenv("AWS_REGION")?:"eu-west-1";

if (!accountId || !ecsClusterName || !jenkinsIP){
    logger.info("Not configuring Cloud, env vars missing")
    return
}


logger.info("Creating template")
def ecsTemplate = new ECSTaskTemplate(
  templateName="nodejs_slave",
  label="nodejs_slave",
  image="codecrunchers/jenkins-node-slave:latest",
  remoteFSRoot="/home/node/work",
  memory=0,
  memoryReservation=512,
  cpu=512,
  privileged=false,
  logDriverOptions=null,
  environments=null,
  extraHosts=null,
  mountPoints=null
)


logger.info("Retrieving ecs cloud config by descriptor")
def ecsCloud = new ECSCloud(
  name="ecs_slave_runner",
  templates=Arrays.asList(ecsTemplate),
  credentialsId=null,
  cluster="arn:aws:ecs:${awsRegion}:${accountId}:cluster/${ecsClusterName}",
  regionName="${awsRegion}",
  jenkinsUrl="http://${jenkinsIP}:8080/jenkins/", //TODO: until I sort out consul
  slaveTimoutInSeconds=60
)

logger.info("Gettings clouds")
def clouds = instance.clouds
clouds.add(ecsCloud)
logger.info("Saving jenkins")
instance.save()

