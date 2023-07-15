FROM openjdk:17-jdk-slim-buster
MAINTAINER qrowned.dev

WORKDIR /app

COPY target/license-application/license/lib lib/

ENTRYPOINT ["java", "-Xms128M", "-Xmx3072M", "-cp", ".:lib/*", "dev.qrowned.license.server.LicenseServerApplication"]