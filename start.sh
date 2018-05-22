#########################################################################
# File Name: start.sh
#########################################################################
#!/bin/bash

git pull

mvn clean package -Dmaven.test.skip

pkill -9 -f library

nohup java -jar target/library.jar &
