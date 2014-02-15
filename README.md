## Introduction
Log4janalyser is a simple web application to visualise log4j files. It is particularly useful in that it allows tailing of logs and filtering of logs.


## Building
Build the package as follows:
```
mvn package
```

## Usage
1. Deploy the WAR to your favourite application server. or run:
   ``` 
   mvn jetty:run-war
   ```
2. Run the application.
3. Enter the path to the (local) file and the log4j format used.


Have fun!
