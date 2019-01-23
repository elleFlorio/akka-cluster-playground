# Akka Cluster Playground
Tinkering with Akka Cluster

This is a toy application developed to play a bit with Akka Cluster.
It is based on the [sample repo](https://github.com/akka/akka-sample-cluster-docker-compose-scala) provided by Akka, and on the [official documentation](https://doc.akka.io/docs/akka/2.5/cluster-usage.html). Do you want to play with it? Feel free to contribute! :smile:

## Running
The project is "docker-first", so we need to create the docker images to run using [sbt Native Packager](https://www.scala-sbt.org/sbt-native-packager/). Run the command `sbt docker:publishLocal`, it will create the image `elleflorio/akka-cluster-playground`. 

Once you have the image, type `docker-compose up`. Docker compose will run a `seed` node along with `node1` and `node2`. Right now the nodes do nothing interesting, but since I integrated [Akka HTTP](https://doc.akka.io/docs/akka-http/current/) you can check if the nodes are up and running calling a `/healt` endpoint:
* seed: `http://localhost:8000/health`
* node1: `http://localhost:8001/health`
* node2: `http://localhost:8002/health`

Or you can check the members inside the cluster using the `/status/members` endpoint:
* seed: `http://localhost:8000/status/members`
* node1: `http://localhost:8001/status/members`
* node2: `http://localhost:8002/status/members`

That's it! Enjoy! :grin:
