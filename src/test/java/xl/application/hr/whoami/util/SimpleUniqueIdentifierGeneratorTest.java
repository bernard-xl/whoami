package xl.application.hr.whoami.util;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

public class SimpleUniqueIdentifierGeneratorTest {

    private SimpleUniqueIdentifierGenerator ids;

    @Before
    public void setup() {
        ids = new SimpleUniqueIdentifierGenerator();
    }

    @Test
    public void threePartsSeparatedByHypen() {
        String id = ids.next();
        String[] parts = id.split("-");

        assertEquals(3, parts.length);
    }

    @Test
    public void firstPartIsDate() {
        String id = ids.next();
        LocalDate today = LocalDate.now();
        String yyyymmdd = String.format("%04d%02d%02d", today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        String[] parts = id.split("-");
        assertEquals(yyyymmdd, parts[0]);
    }

    @Test
    public void secondPartIsLastTwoBytesOfMAC() throws SocketException, UnknownHostException {
        String id = ids.next();
        String mac = lastTwoBytesOfMAC();

        String[] parts = id.split("-");
        assertEquals(mac, parts[1]);
    }

    @Test
    public void thirdPartIsHexSequence() {
        for (int i = 0; i < 32; i++) {
            String id = ids.next();
            String[] parts = id.split("-");

            String hexSeq = String.format("%04X", i);
            assertEquals(hexSeq, parts[2]);
        }
    }

    private String lastTwoBytesOfMAC() throws UnknownHostException, SocketException {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();

            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();

                if (mac != null && mac.length >= 2) {
                    return String.format("%02X%02X", mac[mac.length - 2], mac[mac.length - 1]);
                }
            }
        } catch (SocketException e) {
            // do nothing
        }
        return "0000";
    }
}
