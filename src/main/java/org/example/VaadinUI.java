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
import org.vaadin.cdiviewmenu.ViewMenuUI;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.label.Header;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

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
public class VaadinUI extends ViewMenuUI {

}
