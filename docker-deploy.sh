#!/bin/bash
if [ "$#" -ne 2 ]; then
	echo
  	echo "Invalid number of arguments."
	echo
	echo "Usage:  $0 <spring profile> <docker image/container name>"
	echo
	echo "e.g.        $0 default compreface-relay"
	echo
  	exit 1
else
	echo
	echo "Deploying $2 Docker container with Spring profile: " $1
	echo
fi
#
echo
echo "#"
echo "# Stage 1: Remove docker container"
echo "#"
echo
docker stop $2
docker rm $(docker ps -aqf ancestor=$2)
#
echo
echo "#"
echo "# Stage 2: Remove docker image"
echo "#"
echo
docker image rm $(docker images -q $2)
#
echo "#"
echo "# Stage 3: Build Docker image from JAR file"
echo "#"
if ls target/$2*.jar 1> /dev/null 2>&1; then
	echo "Build docker image using JAR file: " `ls target/$2*.jar`
    echo
else
	echo "The $2 JAR file cannot be found - build stopped" 
	echo "Re-run the script with the '$2.jar' JAR file placed in the directory: " `pwd`/target
	echo
	exit
fi
docker build . -f Dockerfile -t $2 --build-arg SPRING_PROFILE=$1
echo
#
echo
echo "#"
echo "# Stage 4: Start and run the container in detached mode"
echo "#"
echo
docker compose -f docker-compose.yaml up -d
#
echo
echo "#"
echo "# Stage 5: Display latest Docker container and tail the log (after 10 seconds) to check all is good"
echo "#"
docker ps -l
echo
sleep 10
docker logs $2
echo
