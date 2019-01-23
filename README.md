# Akka Cluster Playground
Tinkering with Akka Cluster

This is a toy application developed to play a bit with Akka Cluster.
It is based on the [sample repo](https://github.com/akka/akka-sample-cluster-docker-compose-scala) provided by Akka, and on the [official documentation](https://doc.akka.io/docs/akka/2.5/cluster-usage.html). Do you want to play with it? Feel free to contribute! :smile:

## Running
The project is "docker-first", so we need to create the docker images to run using [sbt Native Packager](https://www.scala-sbt.org/sbt-native-packager/). Run the command `sbt docker:publishLocal`, it will create the image `elleflorio/akka-cluster-playground`. 

Once you have the image, type `docker-compose up`. Docker compose will run a `seed` node along with `node1` and `node2`. 

## API
I integrated [Akka HTTP](https://doc.akka.io/docs/akka-http/current/), so you can interact with the cluster using the following REST API.
The port for the nodes are the following:
* seed: `8000`
* node1: `8001`
* node2: `8002`

### Health 
Check if the nodes are up and running calling a `/health` endpoint:
```
GET http://localhost:{{port}}/health
```
Response:
```
OK
```


### Members
Check the members inside the cluster using the `/status/members` endpoint:
```
GET http://localhost:{{port}}/status/members
```
Response:
```
[
    "akka.tcp://cluster-playground@node1:1600",
    "akka.tcp://cluster-playground@node2:1600",
    "akka.tcp://cluster-playground@seed:2552"
]
```

### Fibonacci
Ask for the computation of Fibonacci number `n` with the `/process/fibonacci/n` endpoint:
```
GET http://localhost:{{port}}/process/fibonacci/10
```
Response:
```
{
    "nodeId": "seed",
    "result": 55
}
```
The `nodeId` represents the node where the processor 
that executed the computation is running.

## Cluster Aware Routers
The cluster uses [Cluster Aware Routers](https://doc.akka.io/docs/akka/2.5/cluster-routing.html) for load balancing, with a round-robin strategy. You can verify this asking for the Fibonacci number computation to a random node. Everytime the `nodeId` in the response will be different, following a round-robin strategy.

That's it! Enjoy! :grin:
