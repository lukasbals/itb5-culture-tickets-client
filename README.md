# Culture Tickets client

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BakaBoing_itb5-culture-tickets-client&metric=alert_status)](https://sonarcloud.io/dashboard?id=BakaBoing_itb5-culture-tickets-client)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BakaBoing_itb5-culture-tickets-client&metric=bugs)](https://sonarcloud.io/dashboard?id=BakaBoing_itb5-culture-tickets-client)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BakaBoing_itb5-culture-tickets-client&metric=code_smells)](https://sonarcloud.io/dashboard?id=BakaBoing_itb5-culture-tickets-client)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BakaBoing_itb5-culture-tickets-client&metric=coverage)](https://sonarcloud.io/dashboard?id=BakaBoing_itb5-culture-tickets-client)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BakaBoing_itb5-culture-tickets-client&metric=security_rating)](https://sonarcloud.io/dashboard?id=BakaBoing_itb5-culture-tickets-client)

Culture tickets is a software, to help event managers to sell tickets for their events.
This repository represents the client of the application.

## Story definitions

### Definition of ready

* AC's
* Estimated
* Discussion in the team until anyone understands the topics
* UI mock

### Definition of done

* \> 1 approving code reviews
* Documented
* Tested
* AC's are fulfilled

## Test

Run the unit tests with the following commands:

```shell script
mvn clean test
```

## Build

Build the app with the following commands:

```shell script
mvn clean -U package
```

## Run

Run the app with the following commands:

```shell script
java -jar itb5-culture-tickets-client-1.0-SNAPSHOT.jar
```

If you want to specify the host and connection type manually:
```shell script
java -jar itb5-culture-tickets-client-1.0-SNAPSHOT.jar [ip/localhost] [rmi/ejb]
```

default: 
```shell script
java -jar itb5-culture-tickets-client-1.0-SNAPSHOT.jar 10.0.51.93 rmi
```