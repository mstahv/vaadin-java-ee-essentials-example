package org.example;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import javax.inject.Inject;
import org.example.backend.PhoneBookEntry;
import org.example.backend.PhoneBookService;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.label.Header;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 *
 * @author matti ät vaadin.com
 */
@CDIView("")
public class MainView extends CssLayout implements View {

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
    private void editEntry(PhoneBookEntry entry) {
        if (entry == null) {
            form.setVisible(false);
            delete.setEnabled(false);
        } else {
            entry = service.refreshEntry(entry);
            delete.setEnabled(true);
            form.setEntity(entry);
            form.focusFirst();
        }
    }

    public void entrySaved(PhoneBookEntry value) {
        try {
            service.save(value);
        } catch (Exception e) {
            // Most likely optimistic locking exception
            Notification.show("Saving entity failed!", e.
                    getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
        }
        // deselect the entity
        entryList.setValue(null);
        // refresh list
        listEntries();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        service.ensureDemoData();
        // Add some event listners, e.g. to hook filter input to actually 
        // filter the displayed entries
        filter.addTextChangeListener(e -> {
            listEntries(e.getText());
        });
        entryList.addMValueChangeListener(this::entrySelected);
        form.setSavedHandler(this::entrySaved);
        form.setResetHandler(this::entryEditCanceled);

        removeAllComponents();
        addComponents(
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

}
