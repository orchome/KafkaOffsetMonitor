Kafka Offset Monitor
===========

[![Build Status](https://travis-ci.org/Morningstar/kafka-offset-monitor.svg?branch=master)](https://travis-ci.org/Morningstar/kafka-offset-monitor)

This is an app to monitor your kafka consumers and their position (offset) in the log.

You can see the current consumer groups, for each group the topics that they are consuming and the position of the group in each topic log. This is useful to understand how quick you are consuming from a log and how fast the log is growing. It allows for debuging kafka producers and consumers or just to have an idea of what is going on in your system.

Here are a few screenshots:

List of Consumer Groups
-----------------------

![Consumer Groups](https://github.com/orchome/KafkaOffsetMonitor/blob/main/img/groups.png)

List of Topics for a Group
--------------------------

![Topic List](https://github.com/orchome/KafkaOffsetMonitor/blob/main/img/topics.png)

Supported Kafka version
===========

- kafka raft >= 2.8

Building It
===========

The command below will build a fat-jar in the target/scala_${SCALA_VERSION} directory which can be run by following the command in the "Running It" section.

```bash
mvn -Dmaven.test.skip=true -U package
```

Download It
===========
<a href="https://www.orchome.com/file/KafkaOffsetMonitor-1.0.jar.zip" rel="nofollow">KafkaOffsetMonitor-1.0.jar.zip</a>

Running It
===========

This is a small web app, you can run it locally or on a server, as long as you have access to the Kafka broker(s) storing kafka data.

```
java -jar KafkaOffsetMonitor-1.0.jar \
     --broker-list=localhost:9092 \
     --server.port=8080
```

The arguments are:

- **broker-list** comma-separated list of Kafka broker hosts (ex. "host1:port,host2:port').  Required only when using offsetStorage "kafka".
- **server.port** the port on which the app will be made available

Contributing
============

The KafkaOffsetMonitor is released under the Apache License and we **welcome any contributions** within this license. Any pull request is welcome and will be reviewed and merged as quickly as possible.
