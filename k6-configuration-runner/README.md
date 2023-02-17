# k6 Load Testing - Java Example

A component that transforms adapted load test configuration into k6 scripts.
These scripts will be executed. The test results will be exported to InfluxDB(v2) and
can be viewed in Grafana.

The k6 configuration will be imported via RabbitMQ.

---
## Application properties

- api.host (default: 127.0.0.1) (Should be an IP-address)
- influx.host (default: localhost)

--- 
### xk6-output-influxdb

By default, it is not possible to export k6 result metrics to an InfluxDB v2.
Therefore, the [xk6-output-influxdb](https://github.com/grafana/xk6-output-influxdb)-extension 
has to be installed.

> The installation is only necessary when the k6-configuration-runner is **executed locally**.
> The Dockerfile already installs the k6-extension.

---
### More Information about k6

- k6 in general: https://k6.io/docs/
- k6 options: https://k6.io/docs/using-k6/k6-options/reference/
- k6 metrics: https://k6.io/docs/using-k6/metrics/
- k6 params: https://k6.io/docs/javascript-api/k6-http/params/
- k6 responses: https://k6.io/docs/javascript-api/k6-http/response/