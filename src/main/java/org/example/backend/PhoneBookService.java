package org.example.backend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

/**
 * A very simple in memory service to provide access to PhoneBook entries.
 *
 * In a real world app, you'll probably have something similar both with some
 * real persistency.
 */
@ApplicationScoped
public class PhoneBookService {

    private final Set<PhoneBookEntry> entries = new HashSet<>();

    public PhoneBookService() {
        entries.add(new PhoneBookEntry("Mark Sheridan", "+358 55 5233 322",
                "mark@vaadin.com"));
        entries.add(new PhoneBookEntry("Dan Sheridan", "+358 55 5233 328",
                "dan@gmail.com"));
        entries.add(new PhoneBookEntry("Anna Annala", "+358 55 5233 233",
                "anna@annala.com"));
    }

    public void save(PhoneBookEntry entry) {
        entries.add(entry);
    }

    public List<PhoneBookEntry> getEntries(String filter) {
        return entries.stream()
                .filter(e -> filter == null || e.getName().contains(filter))
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());
    }

    public void delete(PhoneBookEntry value) {
        entries.remove(value);
    }

}
