package org.example.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.BeanUtils;

/**
 * A very simple in memory service to provide access to PhoneBook entries.
 *
 * This example uses ApplicationScoped CDI bean as "database", but in a real
 * world app, you'll probably have something similar both with some real
 * persistency.
 */
@Stateless
public class PhoneBookService {

    @PersistenceContext(unitName = "customerdb")
    EntityManager em;

    public PhoneBookService() {
    }

    public void save(PhoneBookEntry entry) {
        if (entry.getId() == null) {
            em.persist(entry);
        } else {
            em.merge(entry);
        }
    }

    public void save(PhoneBookGroup value) {
        if (value.getId() == null) {
            em.persist(value);
        } else {
            em.merge(value);
        }
    }

    public List<PhoneBookEntry> getEntries(String filter) {
        if (filter == null) {
            return em.createQuery("SELECT e from PhoneBookEntry e",
                    PhoneBookEntry.class).getResultList();
        }
        return em.createNamedQuery("Entry.byName", PhoneBookEntry.class).
                setParameter(
                        "filter", "%" + filter + "%").getResultList();
    }

    public List<PhoneBookGroup> getGroups(String filter) {
        return em.createNamedQuery("Group.byName", PhoneBookGroup.class).
                setParameter(
                        "filter", "%" + filter + "%").getResultList();
    }

    public void delete(PhoneBookEntry value) {
        em.remove(value);
    }

    public void delete(PhoneBookGroup value) {
        em.remove(value);
    }

    private static final String[] names = new String[]{"Younker Patel", "Zollicoffer Robinson", "Zeh Haugen", "Yager Johansen", "Zickefoose Macdonald", "Yerkes Karlsson", "Yerby Gustavsson", "Zimple Svensson", "Youmans Stewart", "Zahn Davis", "Zenz Davis", "Zamastil Jackson", "Zamastil Gustavsson", "Zucchero Walker", "Zielke Martin", "Zabowski Carlsson", "Yoes Hansson", "Zuczek Smith", "Zeidler Watson", "Yingling Harris", "Zahn Karlsen", "Zimmermann Olsson", "Zerkey Martin", "Zatovich Andersson", "Yurky Andersson", "Yeary Carlsson", "Yeary Olsen", "Zabowski Olsen", "Zuber Jackson", "Zeim Nilsen"};
    private static final String[] groupNames = new String[]{"Collegues", "Friends", "Family", "Students"};

    public void ensureDemoData() {
        if (getGroups().isEmpty()) {

            Random r = new Random(0);

            for (String name : groupNames) {
                em.persist(new PhoneBookGroup(name));
            }
            em.flush();

            List<PhoneBookGroup> groups = getGroups();

            for (String name : names) {
                String[] split = name.split(" ");
                final PhoneBookEntry phoneBookEntry = new PhoneBookEntry(name,
                        "+ 358 555 " + (100 + r.
                        nextInt(900)), split[0].toLowerCase() + "@" + split[1].
                        toLowerCase() + ".com");
                for (int i = r.nextInt(groups.size()); i < groups.size(); i = i + r.nextInt(groups.size()) +1) {
                    phoneBookEntry.getGroups().add(groups.get(i));
                }
                em.persist(phoneBookEntry);
                em.flush();
            }
        }
    }

    public List getGroups() {
        return em.createQuery("SELECT e from PhoneBookGroup e",
                PhoneBookGroup.class).getResultList();
    }

    public PhoneBookEntry refreshEntry(PhoneBookEntry entry) {
        final PhoneBookEntry e = em.find(PhoneBookEntry.class, entry.getId());
        // hack needed for hibernates proxies, forces initialisation
        e.getGroups().size();
        return e;
    }

}
