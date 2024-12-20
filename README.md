# Flow Log Records App

Small command-line app that processes flow log record files described in https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html.

**Note:** Only v2 records are supported.

## Generate an executable jar file:
```shell
./gradlew bootjar
```

The executable will be created under `./build/libs`

## Execute the application:
### Option1: With wrapper gradle task 
```
./gradlew bootRun --args="<mappgins_file> <flow_log_file> <output_file>"
```

Example:
```shell
./gradlew bootRun --args="/Users/russell/Workspace/tag_mappings.csv /Users/russell/Workspace/flow_log_big.txt /Users/russell/Workspace/output.txt"
```

### Option2: Using the generated executable jar
```
java -jar ./build/libs/flowlog-0.0.1.jar <mappgins_file> <flow_log_file> <output_file>
```

Example:
```shell
java -jar ./build/libs/flowlog-0.0.1.jar /Users/russell/Workspace/tag_mappings.csv /Users/russell/Workspace/flow_log_big.txt /Users/russell/Workspace/output.txt
```