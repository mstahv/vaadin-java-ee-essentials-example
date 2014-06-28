package org.example;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.commons.beanutils.BeanUtils;
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
 */
@CDIUI
@Theme("dawn")
public class VaadinUI extends UI {

    @Inject
    PhoneBookService service;

    @Inject
    PhoneBookEntryForm form;

    MTable<PhoneBookEntry> entryList = new MTable<>(PhoneBookEntry.class)
            .withHeight("450px")
            .withFullWidth()
            .withProperties("name", "number", "email");

    Button addNew = new MButton(FontAwesome.PLUS, this::addNew);
    Button delete = new MButton(FontAwesome.TRASH_O, this::deleteSelected);
    TextField filter = new MTextField().withInputPrompt("filter...");

    @Override
    protected void init(VaadinRequest request) {
        listEntries();

        form.setSavedHandler(this::entrySaved);
        form.setResetHandler(this::entryEditCanceled);

        filter.addTextChangeListener(e -> {
            listEntries(e.getText());
        });

        entryList.addMValueChangeListener(this::entrySelected);
        entryList.setValue(entryList.firstItemId());

        setContent(new MVerticalLayout(
                new Header("PhoneBook"),
                new MHorizontalLayout(addNew, delete, filter),
                new HorizontalSplitPanel(entryList, form)
        ));
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

    public void entrySaved(PhoneBookEntry clone) {
        PhoneBookEntry value = entryList.getValue();
        try {
        	// Copy the saved state from the clone to "attached entity"
        	// In e.g. typical JPA app this step is not relevant
            BeanUtils.copyProperties(value, clone);
            service.save(value);
            entryList.setValue(null);
            listEntries();
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(VaadinUI.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    public void entryEditCanceled(PhoneBookEntry entry) {
        editEntry(entryList.getValue());
    }

    public void entrySelected(MValueChangeEvent<PhoneBookEntry> event) {
        editEntry(event.getValue());
    }

    private void editEntry(final PhoneBookEntry entry) {
        if (entry == null) {
            form.setVisible(false);
        } else {
            try {
                // Example works with "attached entities", use clone for editing
            	// In e.g. typical JPA app this step is not relevant
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

}
