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

Run

```shell
docker compose up
```

Once everything is spun up you could send some data of both sensors

```shell
sh push-test-data.sh
```

They will be handled and at the logs of

```shell
docker logs environment-monitoring-task-central-service-1 | grep ALARM
```

there will be something similar to

```
2025-05-13 10:05:37,122 WARN [c.s.e.m.c.h.SensorAlertEventHandler] - ALARM! Sensor 't1' of type 'TEMPERATURE' threshold '35.00' is exceed with value '100' at '2025-05-13T10:05:37.058948210Z'
2025-05-13 10:05:37,130 WARN [c.s.e.m.c.h.SensorAlertEventHandler] - ALARM! Sensor 'h1' of type 'HUMIDITY' threshold '50.00' is exceed with value '99' at '2025-05-13T10:05:37.081538671Z'
```

# Comments to the Implementation

## Warehouse-service

As it is crucial not to lose any sensor data, it is written with Spring Reactor in non-blocking/reactive mode.
Its only goal is to receive a UPD packet, parses it and stores to the broker.
I didn't do the load testing, however, it is quite interesting how much input data it could handle.
Of course, a single instance could listen for multiple ports but much better from the scalability perspective to have it
configurable, so two instances of the warehouse-service are run, see the `docker-compose.yaml` file, please.

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
  their thresholds
- the thresholds are assumed just one per sensor, `exceeding the threshold` means `>=` its value, but there might be
  multiple thresholds and various operations like `<` and `>`
- there is no API to manage the sensors and their thresholds and thus no cache evictions
- each threshold crossing raises an alert, there is no deduplication logic even I am familiar
  with [IBM Tivoli Netcool/OMNIbus](https://www.ibm.com/docs/en/netcoolomnibus/8.1.0?topic=overview-introduction-tivoli-netcoolomnibus)

## Testing

- I don't believe much in the mocking testing at all
- this is a mostly integration project, the only "logic" is basically a comparison a value with a threshold, so I was
  focused on the integration tests using TestContainers
- I didn't add jacoco/sonar etc., but IDEA shows the test coverage close to `100%` (not 100% sharp as there are
  `@SneakyThrows` and some exception handling which is hard to reach)

