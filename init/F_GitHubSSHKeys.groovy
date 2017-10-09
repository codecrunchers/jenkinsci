println("-------> Retreiving SSH keys from s3")

def home = System.getProperty('user.home')
def keysFolder = '.ssh'
def copyS3Content = "aws s3 cp s3://pipeline-creds ${home}/${keysFolder} --recursive"

def execution = copyS3Content.execute()
execution.waitFor()

if (execution.exitValue() != 0) {
  println "Error: Unable to retreive ssh keys from s3"
  println "${execution.err.text}"
//  System.exit(1)
}

// change the perms
"chmod 400 ${home}/${keysFolder}/id_rsa".execute()

println "${execution.text}"
println "ssh keys downloaded from s3"
