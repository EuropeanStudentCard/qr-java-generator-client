package eu.europeanstudentcard.generator.client;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.europeanstudentcard.generator.client.utils.QRRestRequestBuilder;
import org.apache.hc.core5.http.ContentType;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Factory class for creating ESCN client instances to interact with the European Student Card API.
 */
public class QRClientFactory {

    private final String host;

    private QRClientFactory(String host) {
        this.host = host;
    }

    public static QRClientFactory create() {
        return new QRClientFactory("http://router.europeanstudentcard.eu");
    }

    public static QRClientFactory create(String host) {
        return new QRClientFactory(host);
    }

    /**
     * Retrieves a QR code for a specific ESCN (European Student Card Number).
     *
     * @param escn        The European Student Card Number for which to generate the QR code.
     * @param orientation The orientation of the QR code logo (either VERTICAL or HORIZONTAL).
     * @param colours     The colour scheme for the QR code and logo (NORMAL or INVERTED).
     * @param size The size of the QR code:
     *             <ul>
     *               <li><b>XS</b> - 41x41px</li>
     *               <li><b>S</b> - 61.5x61.5px</li>
     *               <li><b>M</b> - 164x164px</li>
     *             </ul>
     * @return A byte array representing the QR code in SVG format.
     * @throws IOException          If an I/O error occurs during the request.
     * @throws URISyntaxException   If the URI is malformed.
     */
    public String getQRCode(String escn, Orientation orientation, Colours colours, Size size) throws IOException, URISyntaxException {

        // Initialize the request builder
        QRRestRequestBuilder builder = new QRRestRequestBuilder("GET", host.concat("/api/v2/cards/" + escn + "/qr"));

        // Add query parameters
        builder.addHeaders(null, ContentType.IMAGE_SVG)
                .addQueryParam("orientation", orientation.toString())
                .addQueryParam("colours", colours.toString())
                .addQueryParam("size", size.toString());

        // Build and execute the request
        return builder.buildRequest(new TypeReference<String>() {}).execute();

    }

    // Enum for orientation
    public enum Orientation {
        VERTICAL,
        HORIZONTAL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    // Enum for colours
    public enum Colours {
        NORMAL,
        INVERTED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    // Enum for size
    public enum Size {
        XS,
        S,
        M;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }


}