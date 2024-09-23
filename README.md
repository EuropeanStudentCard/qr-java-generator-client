# ESCN Validator Client

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

`EscnClientFactory` is a Java client designed to interact with the European Student Card Router application (ESC-R) API.
It provides functionality to generate ESCN numbers, either as single or in a list using a RESTful API endpoint. This class is part of a larger library aimed at simplifying integration with ESC services.. The ESCNs are UUIDs formatted according to the RFC 4122 standard. 

## Usage
### Creating an Instance of EscnClientFactory
You can create an instance of EscnClientFactory with the default host or a specified host.

#### Default Host
```java
EscnClientFactory factory = EscnClientFactory.create();
```
#### Specified Host
```java
EscnClientFactory factory = EscnClientFactory.create("http://your-custom-host.com");
```
### Generating a Single ESCN
To generate a single ESCN, use the getEscn method with the required parameters:

```java
  EscnClientFactory clientFactory = EscnClientFactory.create();
  String escn = clientFactory.getEscn(123, "participantCode");
```

### Generating Multiple ESCNs
To generate a list of ESCNs, use the getEscn method with additional parameters:

```java
  EscnClientFactory clientFactory = EscnClientFactory.create();
  List<String> escns = clientFactory.getEscn(123, "participantCode", 5);
```

## Class Documentation
### EscnClientFactory
Factory class for creating instances of ESCN Client.

##### Methods
* `create()`: Creates a new instance of EscnClientFactory with the default host URL.
* `create(String host)`: Creates a new instance of EscnClientFactory with a specified host URL.
* `String getEscn(Integer prefix, String pic)`: Generates a single ESCN using the provided prefix and participant identification code.
* `List<String> getEscn(Integer prefix, String pic, Integer numberOfESCN)`: Generates a list of ESCNs based on the provided parameters.