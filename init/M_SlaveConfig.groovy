import java.util.Arrays
import java.util.logging.Logger
import com.cloudbees.jenkins.plugins.amazonecs.ECSTaskTemplate
import jenkins.model.*
import com.cloudbees.jenkins.plugins.amazonecs.ECSCloud

Logger logger = Logger.getLogger("ecs-cluster")

String accountId = System.getenv("AWS_ACCOUNT_ID")?:null;
String ecsClusterName = System.getenv("ECS_CLUSTER")?:null;
String jenkinsIP = System.getenv("JENKINS_IP")?:null;
String awsRegion = System.getenv("AWS_REGION")?:"eu-west-1";

if (!accountId || !ecsClusterName || !jenkinsIP){
    logger.info("Not configuring Cloud, env vars missing")
    return
}

logger.info("Loading Jenkins")
instance = Jenkins.getInstance()

logger.info("Creating template")
def ecsTemplate = new ECSTaskTemplate(
  templateName="node-slave",
  label="node-slave",
  image="codecrunchers/jenkins-node-slave:latest",
  remoteFSRoot="/home/node/work",
  memory=0,
  memoryReservation=2048,
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
  jenkinsUrl="${jenkinsIP}",
  slaveTimoutInSeconds=60
)

logger.info("Gettings clouds")
def clouds = instance.clouds
clouds.add(ecsCloud)
logger.info("Saving jenkins")
instance.save()

