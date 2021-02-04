## Spring Boot Gremlin GraphDB Example

#### Preparation
1. Download apache-tinkerpop-gremlin-server 3.2.4
2. `cd apache-tinkerpop-gremlin-server`
3. `./bin/gremlin-server.sh ./conf/gremlin-server.yaml start`
4. Then check port number: `lsof -nP +c 15 | grep LISTEN`
