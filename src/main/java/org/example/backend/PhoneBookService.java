package org.example.backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

/**
 * A very simple in memory service to provide access to PhoneBook entries.
 *
 * This example uses ApplicationScoped CDI bean as "database", but in a real
 * world app, you'll probably have something similar both with some real
 * persistency.
 */
@ApplicationScoped
public class PhoneBookService {

    private final Set<PhoneBookEntry> entries = new HashSet<>();
    private final List<PhoneBookGroup> groups = new ArrayList<>();

    public PhoneBookService() {
        populateDemoData();
    }

    public void save(PhoneBookEntry entry) {
        entries.add(entry);
    }

    public void save(PhoneBookGroup value) {
        groups.add(value);
    }

    public List<PhoneBookEntry> getEntries(String filter) {
        return entries.stream()
                .filter(e -> filter == null || e.getName().contains(filter))
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());
    }

    public List<PhoneBookGroup> getGroups(String filter) {
        return groups.stream()
                .filter(e -> filter == null || e.getName().contains(filter))
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());
    }

    public void delete(PhoneBookEntry value) {
        entries.remove(value);
    }

    public void delete(PhoneBookGroup value) {
        groups.remove(value);
    }

    private static final String[] names = new String[]{"Younker Patel", "Zollicoffer Robinson", "Zeh Haugen", "Yager Johansen", "Zickefoose Macdonald", "Yerkes Karlsson", "Yerby Gustavsson", "Zimple Svensson", "Youmans Stewart", "Zahn Davis", "Zenz Davis", "Zamastil Jackson", "Zamastil Gustavsson", "Zucchero Walker", "Zielke Martin", "Zabowski Carlsson", "Yoes Hansson", "Zuczek Smith", "Zeidler Watson", "Yingling Harris", "Zahn Karlsen", "Zimmermann Olsson", "Zerkey Martin", "Zatovich Andersson", "Yurky Andersson", "Yeary Carlsson", "Yeary Olsen", "Zabowski Olsen", "Zuber Jackson", "Zeim Nilsen"};
    private static final String[] groupNames = new String[]{"Collegues", "Friends", "Family", "Students"};

    private void populateDemoData() {
        Random r = new Random(0);

        for (String name : groupNames) {
            groups.add(new PhoneBookGroup(name));
        }

        for (String name : names) {
            String[] split = name.split(" ");
            final PhoneBookEntry phoneBookEntry = new PhoneBookEntry(name,
                    "+ 358 555 " + (100 + r.
                    nextInt(900)), split[0].toLowerCase() + "@" + split[1].
                    toLowerCase() + ".com");
            for (int i = 0; i < 2; i++) {
                phoneBookEntry.getGroups().add(groups.get(r.nextInt(groups.
                        size())));
            }
            entries.add(phoneBookEntry);
        }
    }

    public List getGroups() {
        return getGroups(null);
    }

}
