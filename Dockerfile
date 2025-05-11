FROM mcr.microsoft.com/playwright/java:v1.41.0-jammy

WORKDIR /usr/src/app
COPY . .

RUN apt-get update && apt-get install -y wget unzip \
    && wget https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.29.0/allure-commandline-2.29.0.zip \
    && unzip allure-commandline-2.29.0.zip -d /opt \
    && ln -s /opt/allure-2.29.0/bin/allure /usr/local/bin/allure \
    && rm allure-commandline-2.29.0.zip

RUN mvn clean compile -DskipTests

ENTRYPOINT ["mvn"]
CMD ["test"]
