#!/bin/sh

echo "sensor_id=t1; value=30" | netcat -c -u localhost 3344
echo "sensor_id=t1; value=100" | netcat -c -u localhost 3344

echo "sensor_id=h1; value=40" | netcat -c -u localhost 3355
echo "sensor_id=h1; value=99" | netcat -c -u localhost 3355