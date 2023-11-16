#!/bin/bash

# Set the paths and filenames
SOURCE_DIR="./src"
BUILD_DIR="./bin"
JAR_FILE="kidpaint.jar"
MANIFEST_FILE="Manifest.mf"

# Clean the build directory
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

# Compile Java source files
find "$SOURCE_DIR" -name "*.java" > sources.txt
javac -d "$BUILD_DIR" "@sources.txt"
rm sources.txt

# Build the JAR file using the existing manifest file
jar cfm "$JAR_FILE" "$MANIFEST_FILE" -C "$BUILD_DIR" .

echo "Build completed successfully. JAR file created: $JAR_FILE"