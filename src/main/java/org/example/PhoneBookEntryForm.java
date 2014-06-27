package org.example;

import org.example.backend.PhoneBookEntry;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MMarginInfo;
import org.vaadin.maddon.layouts.MVerticalLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class PhoneBookEntryForm extends AbstractForm<PhoneBookEntry> {

    TextField name = new MTextField("Name");
    TextField number = new MTextField("Number");
    TextField email = new MTextField("Email");

    public PhoneBookEntryForm() {
        setEagarValidation(true);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        number,
                        email
                ).withMargin(false),
                getToolbar()
        ).withMargin(new MMarginInfo(false, true));
    }

}
