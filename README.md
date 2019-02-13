



# ConfigApplication
ConfigApplication project is a library for dynamic configuration records.The purpose of the project is to have AppKeys stored in files such as web.config and app.config accessible via a common and dynamic structure and to make updates without requiring deployment or restart/recycle.



## Getting Started

### Prerequisites

Apache Maven is required as build automation tool since this application is a maven based project. It can be easily installed from the website https://maven.apache.org/install.html. 

SQLite is a file based relational database, and it does not require a database server. In this project, SQLite is used as storage, and configuration records are read from the database file that is located on project resources path, but in order to update, delete or insert records, this db file can be opened by SQLite shell tool (http://www.sqlitetutorial.net/download-install-sqlite/), Firefox SQLite Manager extention, or many other way. 

This configuration records are read from SQLite database, and hold in a map data structure. These records are also hold in Memcached that is an in-memory key-value store as caching system. Values can be optionally obtained from Memcached with a different function call. It can be easily install from its website https://memcached.org/. 

### Install

After completed prerequisites, **sqlite-jdbc** and **spymemcached** java libraries(jars) are added as dependency to maven project pom.xml file to use SQLite and Memcached. Also **junit** library is used for Unit tests.

In order to create library(jar) and use it in other applications, config application can be build with basic maven commands.
```
mvn clean: clean project cached output
mvn compile: compile source code of project
mvn test: run test code with suitable test framework
mvn package: package compile code as Jar
mvn install: install package into repo for uses as dependency on other projects.
```
**mvn clean install** command executes the clean build life cycle and the install build phase in the default build life cycle, and it contains all phases of maven operations.

## Configuration Library Functions

### Application Initialization
```
new ConfigurationReader(applicationName,connectionString,refreshTimerIntervalInMs);
```
Application initialize with 3 parameters:
- ApplicationName: Name of the application worked on
- connectionString: connection information of the storage
- refreshTimerIntervalInMs: storage control period

### Application Methods

#### Methods used internally

- `selectAll()`: It reads records from database file specific to application, and it stores these records in a Map data structure, also
  stores in Memcached in-memory database.
- `asyncServiceMethod()`: This method runs asyncronously, it calls selectAll function and sleep with refreshTimerIntervalInMs time     
  period in infinite loop.

#### Methods used from external applications
- `<T> T GetValue(String key)`: This function takes configuration name as parameter, and returns the value of this configuration records 
  with any data type.
- `<T> T GetValueFromCache(String key)`: This function can be also used for read value of a record, but it return values from Memcached   database. 

## JUnit Tests

Before initialization of ConfigurationReader Class of the application, setUpBeforeClass method sets predefined parameters with **@BeforeClass** annotation, and ConfigurationReaderTest which is the constructor of Test Class initialize the Application with these parameters. 

After initialization, testGetValue and testGetValueFromCache methods with **@Test** annotation tests the methods which will be used from external applications.

# Deployment

JAR File can be also needed to install in the remote repository, and to do this **mvn deploy** command can be used. This command can be implemented in this website https://maven.apache.org/guides/mini/guide-3rd-party-jars-remote.html.


