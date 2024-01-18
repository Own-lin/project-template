mvn clean install -Dskiped test
if [ -f ./bin ]; then
    rm -rf ./bin
fi
mkdir ./bin
cp ./core/target/core-*.jar ./bin/active.jar
nohup java -jar ./bin/active.jar > ./bin/running.log 2>&1 &
