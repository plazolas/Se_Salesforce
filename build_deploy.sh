#!/bin/bash
docker rm $(docker ps -a -q)
docker rmi $(docker images -a -q)

mvn clean package

if [ $? -ne 0 ] ; then
  echo "Compile Error"
  return 1
fi

docker build --no-cache -t localhost:5000/salesforcetests:latest .
docker push localhost:5000/salesforcetests:latest
docker pull localhost:5000/salesforcetests:latest

kubectl apply -f ./kubernetes/deployment.json
