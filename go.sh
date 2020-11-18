#!/bin/bash

echo "Hallo"


dbuser="dbadmin"
rootpw="supercoolpw12@5f"
hostname="dietstory_database"
staticmem="4096mb"

pwd=$(pwd)

datapath="$pwd/sql"

echo $datapath

export MYSQL_DB_USER=$dbuser
export MYSQL_ROOT_PASSWORD=$rootpw
export MYSQL_HOST_NAME=$hostname
export JAVA_STATIC_MEM=$staticmem
export DIETSTORY_PATH=$pwd
export DATA_PATH=$datapath
export SERVER_HOST="devemaplestoryserver.westeurope.cloudapp.azure.com"

# docker run --rm -e BUILD_SOURCE=local -v $pwd:/mnt benjixd/dietstory-build:java_8



# docker build --build-arg MYSQL_HOST_NAME=$hostname --build-arg MYSQL_ROOT_PASSWORD=$rootpw -t dietstory-populate-db -f docker/populate_database/Dockerfile .

docker-compose -f docker/dev_compose/docker-compose.yml up --build