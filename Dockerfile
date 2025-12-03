# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Install Xvfb and necessary libraries
RUN apt-get update && apt-get install -y \
    xvfb \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    libfontconfig1 \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the Java application
RUN javac -d out src/*.java

# Expose the port the application runs on
EXPOSE 8080

# Run the application with Xvfb
CMD Xvfb :99 -screen 0 1024x768x16 & export DISPLAY=:99 && java -Djava.awt.headless=false -cp out StudentInfoApp