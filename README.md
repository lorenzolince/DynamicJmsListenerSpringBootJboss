# DynamicJmsListenerSpringBootJboss
# test dynamic JmsListener spring boot in wildfly actveMQ

1. git clone https://github.com/lorenzolince/docker.git
2. cd docker/ORACLE-DATABASE
3. run ./dockerBuild.sh
4. run docker-compose up -d
5. cd docker/wildfly$
6. run ./dockerBuild.sh
7. run ./run.sh
8. git clone https://github.com/lorenzolince/DynamicJmsListenerSpringBootJboss.git
9. cd DynamicJmsListenerSpringBootJboss
10. mvn clean install spring-boot:run
11. to access  http://localhost:8088/liquibase/swagger-ui.html
