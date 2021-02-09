## Spring Boot Gremlin GraphDB Example

#### Preparation
1. If you want to go on with spring-gremlin-data, you should download apache-tinkerpop-gremlin-server 3.2.4;
Otherwise, you can go on with any version.
2. `cd apache-tinkerpop-gremlin-server`
3. `./bin/gremlin-server.sh ./conf/gremlin-server.yaml start`
4. Then check port number: `lsof -nP +c 15 | grep LISTEN`
