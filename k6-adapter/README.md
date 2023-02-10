# k6-Adapter

A component used to adapt a _**dqlang**_-load-test-configuration into a configuration
better suited for the load testing tool [k6](https://k6.io/docs/).

Constant values provided by the modeling will be converted to actual values.
These values are stored and can be definded in [src/main/resources/constant](https://github.com/dqualizer/loadtest-translator-prototype/tree/main/k6-adapter/src/main/resources/constant).

The _**dqlang**_-load-test-configuration will be imported and the k6
configuration will be exported via RabbitMQ.