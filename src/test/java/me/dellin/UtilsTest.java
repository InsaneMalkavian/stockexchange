package me.dellin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    private static Logger log = LoggerFactory.getLogger(UtilsTest.class);
    private static final String CLIENTS_ORIGINAL = "clients.txt";
    private static final String CLIENTS_COPY = "clients_copy.txt";
    private static final String ORDERS = "orders.txt";
    private static final String CLIENTS_MALFORMED = "clients_malformed.txt";
    private static final String CLIENTS_NOT_EXIST = "clients_not_exist.txt";
    private static final String ORDERS_MALFORMED = "orders_malformed.txt";
    private static final String ORDERS_NOT_EXIST = "orders_not_exist.txt";

    @BeforeAll
    public static void prepare() {
        try {
            if (Files.exists(Paths.get(CLIENTS_COPY))) {
                Files.delete(Paths.get(CLIENTS_COPY));
            }
        } catch (IOException ex) {
            log.error("Unable delete test files", ex);
        }
    }

    @AfterAll
    public static void cleanup() {
        prepare();
    }

    @Test
    public void testShouldReturnClients() {
        assertNotNull(Utils.getClients(CLIENTS_ORIGINAL));
        assertEquals(Utils.getClients(CLIENTS_ORIGINAL).size(), 9);
    }

    @Test
    public void testShouldReturnNoClients() {
        assertTrue(Utils.getClients(CLIENTS_MALFORMED).isEmpty());
        assertTrue(Utils.getClients(CLIENTS_NOT_EXIST).isEmpty());
    }

    @Test
    public void testShouldReturnOffers() {
        assertNotNull(Utils.getOffers(ORDERS));
        assertEquals(Utils.getOffers(ORDERS).size(), 8070);
    }

    @Test
    public void testShouldReturnNoOffers() {
        assertTrue(Utils.getOffers(ORDERS_MALFORMED).isEmpty());
        assertTrue(Utils.getOffers(ORDERS_NOT_EXIST).isEmpty());
    }

    @Test
    public void testSetClients() {
        List<Client> clients = Utils.getClients(CLIENTS_ORIGINAL);
        Utils.setClients(CLIENTS_COPY, clients);
        try {
            byte[] f1 = Files.readAllBytes(Paths.get(CLIENTS_ORIGINAL));
            byte[] f2 = Files.readAllBytes(Paths.get(CLIENTS_COPY));
            assertTrue(Arrays.equals(f1, f2));
        } catch (IOException ex) {
            log.error("Unable to read files", ex);
        }

        /*clients = new ArrayList<>();
        Utils.setClients(CLIENTS_COPY, clients);
        try {
            byte[] f1 = Files.readAllBytes(Paths.get(CLIENTS_ORIGINAL));
            byte[] f2 = Files.readAllBytes(Paths.get(CLIENTS_COPY));
            assertFalse(Arrays.equals(f1, f2));
        } catch (IOException ex) {
            log.error("Unable to read files", ex);
        }*/
    }

    @Test
    public void testInvalidSetClients () {
        assertThrows(NullPointerException.class, () -> Utils.setClients(CLIENTS_COPY, null));
    }
}
