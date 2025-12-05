# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Install Xvfb, necessary libraries, and wget for downloading dependencies
RUN apt-get update && apt-get install -y \
    xvfb \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    libfontconfig1 \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Create lib directory and download dependencies
RUN mkdir -p lib && \
    wget -q -O lib/sqlite-jdbc-3.45.1.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar && \
    wget -q -O lib/slf4j-api-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar && \
    wget -q -O lib/slf4j-simple-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar

# Copy the source code into the container
COPY src/ /app/src/
COPY src/credentials.txt /app/src/credentials.txt

# Compile all Java files with dependencies
RUN mkdir -p out && \
    find src -name "*.java" > sources.txt && \
    javac -cp "lib/*" -d out @sources.txt

# Expose the port the application runs on (not actually used by Swing app)
EXPOSE 8080

# Run the application with Xvfb for headless GUI support
CMD Xvfb :99 -screen 0 1024x768x16 & export DISPLAY=:99 && java -Djava.awt.headless=false -cp "out:lib/*" StudentInfoApp