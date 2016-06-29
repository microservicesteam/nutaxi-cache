# nutaxi-cache

This repository contains all the cache related services, libraries that are used by the nutaxi application.


## Redis

Redis is an open source key-value store that functions as a data structure server.

### Local Redis Setup

*Prerequisites*

* Install [Docker](https://docs.docker.com/engine/installation/)

*Steps*

* Start Redis instance

```
 docker run --name nutaxi-redis -d redis
```

* Check that the instance has been started

```
docker ps

docker run --name nutaxi-redis -p 6379:6379  -d redis
```

* Connect to the instance via the default Redis port(6379)
