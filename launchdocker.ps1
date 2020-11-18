$dbuser = "dbadmin"
$rootpw = "supercoolpw12@5f"
$hostname = "dietstory_database"
$staticmem = "4096mb"

Write-Host $PSScriptRoot

$Env:MYSQL_DB_USER = $dbuser
$Env:MYSQL_ROOT_PASSWORD = $rootpw
$Env:MYSQL_HOST_NAME = $hostname
$Env:JAVA_STATIC_MEM = $staticmem
$Env:DIETSTORY_PATH = $PSScriptRoot
$Env:DATA_PATH = Join-Path -Path $PSScriptRoot "sql"
$Env:SERVER_HOST = "devedse.softether.net"


& docker run --rm -e BUILD_SOURCE=local -v ${PSScriptRoot}:/mnt benjixd/dietstory-build:java_8

# & docker run --rm -it -v ${PSScriptRoot}:/mnt -p 7575:7575 -p 7576:7576 -p 7577:7577 -p 8484:8484 -p 8485:8485 benjixd/dietstory


#$test = & docker images -q devemazegeneratorconsoleapp:a

# & docker build --build-arg MYSQL_HOST_NAME=$hostname --build-arg MYSQL_ROOT_PASSWORD=$rootpw -t dietstory-populate-db -f docker/populate_database/Dockerfile .

& docker-compose -f docker/dev_compose/docker-compose.yml up --build

# & docker-compose -f docker/dev_compose/docker-compose.yml down