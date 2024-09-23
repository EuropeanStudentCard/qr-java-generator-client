package eu.europeanstudentcard.generator.client.exceptions;

public class QRClientException extends RuntimeException {

    public QRClientException(String message) {
        super(message);
    }

    public QRClientException(Exception e) {
        super(e);
    }

}
