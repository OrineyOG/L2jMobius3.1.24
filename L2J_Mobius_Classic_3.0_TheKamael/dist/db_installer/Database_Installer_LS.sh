#!/bin/bash

# Check for JAVA_HOME and set java path
JAVA_PATH=$JAVA_HOME
if [ -z "$JAVA_PATH" ]; then
    echo "Could not find JAVA_HOME environment variable!"
    exit 1
else
    JAVA_PATH="$JAVA_PATH/bin/"
fi

# Run the installer
"$JAVA_PATH"java -jar Database_Installer_LS.jar
