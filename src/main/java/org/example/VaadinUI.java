package org.example;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.commons.beanutils.BeanUtils;
import org.example.backend.PhoneBookEntry;
import org.example.backend.PhoneBookService;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * This is a small tutorial application for Vaadin. It also uses Vaadin CDI (so
 * deploy to Java EE server) and a dependency collection for small Java EE +
 * Vaadin applications.
 *
 * Note, that this application is just to showcase Vaadin UI development and
 * some handy utilities. Pretty much whole application is just dumped into this
 * class. For larger apps where you strive for excellent testability and
 * maintainability, you most likely want to use better structured UI code. E.g.
 * google for "Vaadin MVP pattern".
 */
@CDIUI("")
@Theme("valo")
public class VaadinUI extends UI {

    @Inject
    PhoneBookService service;

    @Inject // With Vaadin CDI one can also inject basic ui components
    PhoneBookEntryForm form;

    // Instantiate and configure a Table to list PhoneBookEntries
    MTable<PhoneBookEntry> entryList = new MTable<>(PhoneBookEntry.class)
            .withHeight("450px")
            .withFullWidth()
            .withProperties("name", "number")
            .withColumnHeaders("Name", "Phone number");

    // Instanticate buttons, hook directly to listener methods in this class
    Button addNew = new MButton(FontAwesome.PLUS, this::addNew);
    Button delete = new MButton(FontAwesome.TRASH_O, this::deleteSelected);
    TextField filter = new MTextField().withInputPrompt("filter...");

    @Override
    protected void init(VaadinRequest request) {

        // Add some event listners, e.g. to hook filter input to actually 
        // filter the displayed entries
        filter.addTextChangeListener(e -> {
            listEntries(e.getText());
        });
        entryList.addMValueChangeListener(this::entrySelected);
        form.setSavedHandler(this::entrySaved);
        form.setResetHandler(this::entryEditCanceled);

        setContent(
                new MVerticalLayout(
                        new Header("PhoneBook"),
                        new MHorizontalLayout(addNew, delete, filter),
                        new MHorizontalLayout(entryList, form)
                )
        );

        // List all entries and select first entry in the list
        listEntries();
        entryList.setValue(entryList.firstItemId());
    }

    private void addNew(Button.ClickEvent e) {
        PhoneBookEntry entry = new PhoneBookEntry();
        service.save(entry);
        listEntries();
        entryList.setValue(entry);
    }

    private void deleteSelected(Button.ClickEvent e) {
        service.delete(entryList.getValue());
        listEntries();
        entryList.setValue(null);
    }

    private void listEntries(String filter) {
        entryList.setBeans(service.getEntries(filter));
    }

    private void listEntries() {
        listEntries(filter.getValue());
    }

    public void entryEditCanceled(PhoneBookEntry entry) {
        editEntry(entryList.getValue());
    }

    public void entrySelected(MValueChangeEvent<PhoneBookEntry> event) {
        editEntry(event.getValue());
    }

    /**
     * Assigns the given entry to form for editing.
     *
     * @param entry
     */
    private void editEntry(final PhoneBookEntry entry) {
        if (entry == null) {
            form.setVisible(false);
            delete.setEnabled(false);
        } else {
            delete.setEnabled(true);
            try {
                // Example "DB" works with "attached entities", use a clone for 
                // editing to make changes "buffered". Vaadin also has buffering
                // on field level that you might find handy sometimes.
                // In typical applications using e.g. JPA this step is not relevant
                // as you will be working with a "detached entity"
                PhoneBookEntry clone = (PhoneBookEntry) BeanUtils.cloneBean(
                        entry);
                form.setEntity(clone);
                form.focusFirst();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(VaadinUI.class.getName()).log(Level.SEVERE,
                        null,
                        ex);
            }
        }
    }

    public void entrySaved(PhoneBookEntry clone) {
        PhoneBookEntry value = entryList.getValue();
        try {
            // Copy the saved state from the clone to "attached entity"
            // In e.g. typical JPA app this step is not relevant as you'd be
            // working with detached entities
            BeanUtils.copyProperties(value, clone);
            // "save" the modified entity with service call
            service.save(value);
            // deselect the entity
            entryList.setValue(null);
            // refresh list
            listEntries();
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(VaadinUI.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

}
