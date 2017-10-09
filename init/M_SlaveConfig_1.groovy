import java.util.Arrays
import java.util.logging.Logger
import com.cloudbees.jenkins.plugins.amazonecs.ECSTaskTemplate
import jenkins.model.*
import com.cloudbees.jenkins.plugins.amazonecs.ECSCloud

Logger logger = Logger.getLogger("ecs-cluster")

String accountId = System.getenv("AWS_ACCOUNTT_ID")?:null;
String ecsClusterName = System.getenv("ECS_CLUSTER")?:null;
String jenkins_ip = System.getenv("JEKNINS_IP")?:null;
if (!accountId || !ecsClusterName || !jenkins_ip){
    logger.info("Not configuring Cloud")
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
  name="name",
  templates=Arrays.asList(ecsTemplate),
  credentialsId=null,
  cluster="arn:aws:ecs:eu-west-1:${AWS_ACCOUNTT_ID}:cluster/${ecsClusterName}",
  regionName="us-east-1",
  jenkinsUrl="http://${jenkins_ip}:8080/jenkins",
  slaveTimoutInSeconds=60
)

logger.info("Gettings clouds")
def clouds = instance.clouds
clouds.add(ecsCloud)
logger.info("Saving jenkins")
instance.save()

