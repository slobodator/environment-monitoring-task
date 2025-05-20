# Preconditions

- modern `jdk 21+`, `maven`, `docker`
- `netcat` installed (for testing)

> [!CAUTION]
> [Colima](https://github.com/abiosoft/colima) seems to have [an issue](https://github.com/abiosoft/colima/issues/708)
> with forwarding UDP ports, so I tested with [OrbStack](https://orbstack.dev/) which is fine.

# Building

```shell
git clone git@github.com:slobodator/environment-monitoring-task.git
cd environment-monitoring-task
mvn clean install
```

> [!WARNING]
> It could take a while as it uses TestContainers for testing and fetches their images

# Running

Make sure that a valid Docker context/environment is chosen

```shell
docker context ls
```

Run (outdated)

```shell
docker compose up
```

**UPDATE 1.1** As the code is changed and **the cache eviction is not implemented**, please, run

```shell
docker compose down
docker compose up --build
```

Once everything is spun up you could send some data of both sensors

```shell
sh push-test-data.sh
```

They will be handled and at the logs of

```shell
docker logs environment-monitoring-task-central-service-1 | grep SensorAlertEventHandler
```

there will be something similar to (outdated)

```
2025-05-13 10:05:37,122 WARN [c.s.e.m.c.h.SensorAlertEventHandler] - ALARM! Sensor 't1' of type 'TEMPERATURE' threshold '35.00' is exceed with value '100' at '2025-05-13T10:05:37.058948210Z'
2025-05-13 10:05:37,130 WARN [c.s.e.m.c.h.SensorAlertEventHandler] - ALARM! Sensor 'h1' of type 'HUMIDITY' threshold '50.00' is exceed with value '99' at '2025-05-13T10:05:37.081538671Z'
```

**UPDATE 1.1**

new output is like

```
2025-05-20 10:14:33,399 INFO [c.s.e.m.c.h.SensorAlertEventHandler] - OK! Sensor 'h1' of type 'HUMIDITY' value '40' at '2025-05-20T10:14:32.437622362Z', which is ok
2025-05-20 10:14:33,402 INFO [c.s.e.m.c.h.SensorAlertEventHandler] - OK! Sensor 't1' of type 'TEMPERATURE' value '30' at '2025-05-20T10:14:32.437616362Z', which is ok
2025-05-20 10:14:33,408 WARN [c.s.e.m.c.h.SensorAlertEventHandler] - CRITICAL! Sensor 't1' of type 'TEMPERATURE' threshold '35.00' is exceed with value '100' at '2025-05-20T10:14:32.504772071Z'
2025-05-20 10:14:33,409 WARN [c.s.e.m.c.h.SensorAlertEventHandler] - CRITICAL! Sensor 'h1' of type 'HUMIDITY' threshold '50.00' is exceed with value '99' at '2025-05-20T10:14:32.614949336Z'
```

# Comments to the Implementation

## Warehouse-service

As it is crucial not to lose any sensor data, it is written with Spring Reactor in non-blocking/reactive mode.
Its only goal is to receive a UPD packet, parses it and stores to the broker.
I didn't do the load testing, however, it is quite interesting how much input data it could handle.
Of course, a single instance could listen for multiple ports but much better from the scalability perspective to have it
configurable, so two instances of the warehouse-service are run, see the `docker-compose.yaml` file, please.

**UPDATE 1.1**

- I solved the issue with multiple UDP ports
- Unfortunately, Netty UDP server doesn't support multiple ports, so I had to run numerous instances and blocking the
  last one not to let the JVM quit
- a configuration would be like `UDP_PORTS: temperature:3344, humidity:3355`

## Broker

Kafka is used, but the `EventPublisher` could be adopted to any other.

## Central-service

This time I used a regular Spring Boot "blocking" approach as the operations against the broker are simple and scalable.
It receives events from the warehouse-services, checks thresholds and publish **sensor-alert** events to allow handle
them separately. However, for simplification it consumes them itself.
The configuration is stored in Postgres, there are predefined sensors `t1` and `h1` from the example.
Before the DB there is a Redis cache.

## Simplifications

- `SensorAlertEventHandler` literally writes the message to the log, even could be enriched with more complex actions
- `sensors` are stored in a single table, but at the real system it is worth to separate the sensors themselves and
  their thresholds (**done as of 1.1**)
- the thresholds are assumed just one per sensor, `exceeding the threshold` means `>=` its value, but there might be
  multiple thresholds and various operations like `<` and `>`
- there is no API to manage the sensors and their thresholds and thus no cache evictions
- each threshold crossing raises an alert, there is no deduplication logic even I am familiar
  with [IBM Tivoli Netcool/OMNIbus](https://www.ibm.com/docs/en/netcoolomnibus/8.1.0?topic=overview-introduction-tivoli-netcoolomnibus)

**UPDATE 1.1**

- I added a many-to-many relationship of the sensors and thresholds
- I didn't choose either a threshold could be applied for numerous sensors or it is a dependant to the sensor
- still, the relationship could support both
- existing threshold were **migrated** to the new structure, see the liquibase scripts, please

## Testing

- I don't believe much in the mocking testing at all
- this is a mostly integration project, the only "logic" is basically a comparison a value with a threshold, so I was
  focused on the integration tests using TestContainers
- I didn't add jacoco/sonar etc., but IDEA shows the test coverage close to `100%` (not 100% sharp as there are
  `@SneakyThrows` and some exception handling which is hard to reach)

