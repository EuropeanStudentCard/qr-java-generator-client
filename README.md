# QRClientFactory

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

`QRClientFactory` is a Java client designed to interact with the European Student Card Router application (ESC-R) API. It provides functionality to retrieve QR codes for European Student Card Numbers (ESCNs), supporting different orientations, color schemes, and sizes.

## Usage

### Creating an Instance of QRClientFactory
You can create an instance of `QRClientFactory` with either the default host or a custom host URL.

#### Default Host
```java
QRClientFactory factory = QRClientFactory.create();
```

#### Custom Host
```java
QRClientFactory factory = QRClientFactory.create("http://your-custom-host.com");
```

### Retrieving a QR Code for a Specific ESCN
To retrieve a QR code for a European Student Card Number (ESCN), use the `getQRCode` method. This method requires the ESCN, orientation, colors, and size of the QR code.

#### Example Request:
```java
QRClientFactory factory = QRClientFactory.create();
String escn = "your-escn-number";

String qrCodeSvg = factory.getQRCode(
    escn,                          // The ESCN number
    QRClientFactory.Orientation.VERTICAL,  // Orientation: VERTICAL or HORIZONTAL
    QRClientFactory.Colours.NORMAL,        // Colours: NORMAL or INVERTED
    QRClientFactory.Size.M                 // Size: XS, S, or M
);
```

The `getQRCode` method will return the QR code as an SVG string, which can be directly used or saved.

### Parameters

- `escn`: The European Student Card Number (ESCN) for which the QR code is generated.
- `orientation`: Defines the orientation of the QR code. Available values:
    - `VERTICAL`
    - `HORIZONTAL`

- `colours`: Defines the color scheme of the QR code. Available values:
    - `NORMAL` (Standard colors)
    - `INVERTED` (Inverted color scheme)

- `size`: Defines the size of the QR code. Available values:
    - `XS` (41x41px)
    - `S` (61.5x61.5px)
    - `M` (164x164px)

### Handling Errors
If the request fails due to I/O or URL-related issues, an `IOException` or `URISyntaxException` will be thrown, allowing you to handle the error accordingly.

```java
try {
    String qrCodeSvg = factory.getQRCode(escn, Orientation.HORIZONTAL, Colours.INVERTED, Size.S);
} catch (IOException | URISyntaxException e) {
    System.err.println("Error retrieving QR code: " + e.getMessage());
}
```

## Enum Types

The `QRClientFactory` class uses the following enum types:

### `Orientation`
Defines the orientation of the QR code:
- `VERTICAL`
- `HORIZONTAL`

### `Colours`
Defines the color scheme of the QR code:
- `NORMAL`
- `INVERTED`

### `Size`
Defines the size of the QR code:
- `XS` - 41x41px
- `S`  - 61.5x61.5px
- `M`  - 164x164px

## Class Documentation

### `QRClientFactory`
A factory class for interacting with the European Student Card Router API.

#### Methods:
- `static QRClientFactory create()`: Creates a new instance of `QRClientFactory` with the default host URL.
- `static QRClientFactory create(String host)`: Creates a new instance of `QRClientFactory` with a custom host URL.
- `String getQRCode(String escn, Orientation orientation, Colours colours, Size size)`: Retrieves the QR code as an SVG string based on the provided parameters.