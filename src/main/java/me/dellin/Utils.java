package me.dellin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    private static Logger log = LoggerFactory.getLogger(Utils.class);

    public static List<Client> getClients(String filename) {
        List<Client> clients = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            clients = stream.map(Client::createClient).collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("Unable to load clients due to IO error", ex);
        } catch (Exception ex) {
            log.error("Unable to load clients due to unknown error (possible malformed data)", ex);
        }
        return clients;
    }

    public static List<Offer> getOffers(String filename) {
        List<Offer> clients = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            clients = stream.map(Offer::createOffer).collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("Unable to load orders due to IO error", ex);
        } catch (Exception ex) {
            log.error("Unable to load orders due to unknown error (possible malformed data)", ex);
        }
        return clients;
    }

    public static void setClients(String filename, List<Client> clients) {
        List<String> arrayList = clients.stream().map(Client::presentation).collect(Collectors.toList());
        try {
            Files.write(Paths.get(filename), arrayList, Charset.defaultCharset()); //@TODO UTF-8 explicitly
        } catch (IOException ex) {
            log.error("Unable to store clients", ex);
        }

    }
}
