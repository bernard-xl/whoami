package xl.application.hr.whoami.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
class SimpleUniqueIdentifierGenerator implements UniqueIdentifierGenerator {

    private static final Logger logger = LoggerFactory.getLogger(SimpleUniqueIdentifierGenerator.class);

    private final AtomicInteger seq;
    private final String mac;

    public SimpleUniqueIdentifierGenerator() {
        seq = new AtomicInteger();
        mac = lastTwoBytesOfMAC();
    }

    @Override
    public String next() {
        return date() + "-" + mac + "-" + nextSeq();
    }

    private String date() {
        LocalDate today = LocalDate.now();
        return String.format("%04d%02d%02d", today.getYear(), today.getMonthValue(), today.getDayOfMonth());
    }

    private String lastTwoBytesOfMAC() {
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
            logger.warn("failed to retrieve MAC address", e);
        }
        return "0000";
    }

    private String nextSeq() {
        int next = seq.getAndIncrement() & 0xFFFF;
        return String.format("%04X", next);
    }
}

