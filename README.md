## Guidelines

### Step 1
> Install dependency

```shell script
cd component-common

mvn clean install
```

### Step 2
> Start Ping Service

- Edit Run/Debug Configuration on IDEA board and allow multi instance running
- Start Ping Service third times
- Open target folder (config in application.properties) and check files were written

### Step 3
> Start Pong Service

- Start Pong Service and check message consume by <font color="red">FileMessageHandler.java</font> and it log consume info in console

### Step 4
> Run Test Report

- Scripts (Ping Service)
```
cd mono-service/serivce-ping

mvn clean test
```
- check test report on target/site/jacoco/index.html


- Scripts (Pong Service)
```
cd mono-service/serivce-pong

mvn clean test
```
- check test report on target/site/jacoco/index.html
