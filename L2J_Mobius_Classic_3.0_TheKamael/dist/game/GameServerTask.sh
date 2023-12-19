#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

# Check for JAVA_HOME and set java path
JAVA_PATH=$JAVA_HOME
if [ -z "$JAVA_PATH" ]; then
    echo "Could not find JAVA_HOME environment variable!"
    exit 1
else
    JAVA_PATH="$JAVA_PATH/bin/"
fi

# Load java.cfg parameters
if [ ! -f java.cfg ]; then
    echo "java.cfg file does not exist!"
    exit 1
fi
PARAMETERS=$(cat "java.cfg")

# Check for GUI configuration
WINDOW_MODE=1
if grep -q "enablegui=true" config/Interface.ini; then
    WINDOW_MODE=0
fi

# Generate command
COMMAND="$JAVA_PATH/java $PARAMETERS -jar ../libs/GameServer.jar"

# Run the server in a loop
ERR=1
until [ $ERR -eq 0 ]; do
    [ -f log/java0.log.0 ] && mv log/java0.log.0 "log/$(date +%Y-%m-%d_%H-%M-%S)_java.log"
    [ -f log/stdout.log ] && mv log/stdout.log "log/$(date +%Y-%m-%d_%H-%M-%S)_stdout.log"

    if [ $WINDOW_MODE -eq 1 ]; then
        $COMMAND > log/stdout.log 2>&1
    else
        # If GUI is enabled, you might want to run without redirecting to stdout.log
        $COMMAND
    fi

    ERR=$?
    if [ $ERR -ne 2 ]; then
        echo "Game Server terminated abnormally!"
    fi
    sleep 10
done
