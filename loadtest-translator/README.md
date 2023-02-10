# Loadtest Translator

One part of _**dqtranslator**_ that is responsible for combining the modeling
with the mapping and produce a _**dqlang**_-load-test-configuration from it.

The mappings are stored in [src/main/resources/mapping](https://github.com/dqualizer/loadtest-translator-prototype/tree/main/loadtest-translator/src/main/resources/mapping).

The modeling will be imported and the created load test configuration will
be exported via RabbitMQ.