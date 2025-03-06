## Tasks for java course on frct bsu

All the tasks are in one repository\
Each contains compile.sh which was used to compile these projects on linux

**Requirements (or what I've used)**:
 - openjdk23
 - maven 3.9.0
 - tomcat 11

### Running the apps
To run labs up to 8:\
`./compile.sh` to compile into app.jar (also creates bin dir)\
`java -jar app.jar` runs the app

To run labs 8 and 9:\
`mvn install` to compile the .war\
`./deploy.sh` to deploy the app\
`./undeploy.sh` to undeploy the app

It all assumes that you have tomcat installed on /opt/tomcat