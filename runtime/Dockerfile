FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    python3 python3-pip \
    openjdk-17-jdk \
    g++ \
    nodejs npm \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
