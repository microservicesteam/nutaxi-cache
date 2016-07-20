# nutaxi-cache

[![Travis Status for microservicesteam/nutaxi-cache](https://travis-ci.org/microservicesteam/nutaxi-cache.svg?branch=master)](https://travis-ci.org/microservicesteam/nutaxi-cache) [![Codeship Status for microservicesteam/nutaxi-cache](https://codeship.com/projects/4b8004c0-2b53-0134-d199-667bd7267f75/status?branch=master)](https://codeship.com/projects/163122)  [![Code Coverage Status for microservicesteam/nutaxi-cache](https://codecov.io/gh/microservicesteam/nutaxi-cache/branch/master/graph/badge.svg)](https://codecov.io/gh/microservicesteam/nutaxi-cache) [![Dependency Status for microservicesteam/nutaxi-cache](https://www.versioneye.com/user/projects/578fca8413b4e100531e915d/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/578690816edb08003f20750f)

This repository contains all the cache related services, libraries that are used by the nutaxi application.


## Redis

Redis is an open source key-value store that functions as a data structure server.

### Local Redis Setup

*Prerequisites*

* Install [Docker](https://docs.docker.com/engine/installation/)

*Steps*

* Start Redis instance

```
docker run --name nutaxi-redis -p 6379:6379  -d redis
```

* Check that the instance has been started

```
docker ps

```

* Connect to the instance via the default Redis port(6379)

### Redis cluster on Amazon ElastiCache

*Prerequisites*

* AWS account

*Steps*

* Login to Amazon account
* Go to ElastiCache home page (https://console.aws.amazon.com/elasticache/home)
* Click on Launch Cache Cluster button
* Select Redis as an engine
* Enter the necessary settings on Specify Cluster Details page
* Select the default VPC in Cache Subnet Group
* Select the appropriate VPC Security groups (this will be needed later)
* Review the settings and click on Launch Replication Group button
* The started cluster can be viewed under Cache Clusters window
* Expand cluster details to see the endpoint where you can connect to
* Access to the cluster can be managed by the security group, the correct port (6379 by default) needs to opened (Service / EC2 / Security groups / Select security group / Inbound tab at the bottom / Edit button)

Based on ElastiCache [Getting started guide](http://docs.aws.amazon.com/AmazonElastiCache/latest/UserGuide/GettingStarted.html)

### Redis cluster with Docker Cloud

* TODO
