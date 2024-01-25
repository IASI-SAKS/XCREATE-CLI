# XCREATE-CLI
Simple CLI for the tool X-CREATE.

This repository uses X-CREATE version 1.2 which is shipped with the JAR included in the folder [lib](lib).
The [POM](pom.xml) explicitly links to this library.

For more information about X-CREATE see at:
 * [X-CREATE Official Page](http://labsedc.isti.cnr.it/tools/xcreate) at [LabSEDC ISTI-CNR](http://labsedc.isti.cnr.it)
 * [Said Daoudagh's Space](https://github.com/saiddao/X-CREATE) on GitHub

## CONFIGURATION

X-CREATE need the access to a DB hosted on an instance of mySQL. Configure its access in the file:
 * [xcreate.properties](src/main/resources/xcreate.properties)
 
## RUN IT
1. Add a set of given XACML policies in the DB on mySQL
     * the CLI will look for XML files in the folder specified as its only mandatory argument
     * the CLI will process all the files trying to add all the XACML policies. If a XACML policies is already present in the DB, it will be skipped.
     * the generation of random values is always enabled by the CLI
     * adding is performed by means of the profile ``addPolicies`` in the [POM](pom.xml). Use the following command: ``mvn clean compile -P addPolicies exec:java -Dexec.args="/tmp/xacmlPolicies"``
1. Generate the XACML reuqest for a given set of XACML policies already loaded in the DB
     * the CLI will look for XML files in the folder specified as its only mandatory argument
     * the CLI will process all the files in the given directory. If a XACML policies is not present in the DB, it will be skipped.
     * the results will be stored in the forlder ``Temp_X-CREATE`` by policy
     * **the generated requests may result overwitten** across several executions (if a policy with a given name has been already processed)
     * only one strategy is accesiible from the CLI: **Multiple Combinatorial**
     * generation is performed by means of the profile ``generateRequests`` in the [POM](pom.xml). Use a command like the following one: ``mvn clean compile -P generateRequests exec:java -Dexec.args="/tmp/xacmlPolicies"``
1. Delete a XACML policy from the DB
     * this feature has not been implemented yet!
        
