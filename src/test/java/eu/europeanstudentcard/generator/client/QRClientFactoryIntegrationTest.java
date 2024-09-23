package eu.europeanstudentcard.generator.client;

import eu.europeanstudentcard.generator.client.exceptions.QRClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QRClientFactoryIntegrationTest {

    @Test
    public void getQRCodeShouldThrowAQRExceptionWhenNoCardIsFound() {
        Assertions.assertThrows(QRClientException.class, () -> QRClientFactory.create().getQRCode("0c615bc5-605a-11ef-83b5-errorUUID",
                QRClientFactory.Orientation.HORIZONTAL, QRClientFactory.Colours.NORMAL, QRClientFactory.Size.S));
    }

    @Test
    public void getQRCodeShouldThrowAQRExceptionWhenHostIsInvalid() {
        Assertions.assertThrows(QRClientException.class, () -> QRClientFactory.create("http://wrong.host").getQRCode("0c615bc5-605a-11ef-83b5-60a44c40ce5c",
                QRClientFactory.Orientation.HORIZONTAL, QRClientFactory.Colours.NORMAL, QRClientFactory.Size.S));
    }
}