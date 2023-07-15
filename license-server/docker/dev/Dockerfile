FROM openjdk:17-jdk-slim-buster

WORKDIR /app

ENTRYPOINT java -Xms128M -Xmx3072M -cp .:lib/* dev.qrowned.license.server.LicenseServerApplication