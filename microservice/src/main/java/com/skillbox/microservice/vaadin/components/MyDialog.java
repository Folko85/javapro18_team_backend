package com.skillbox.microservice.vaadin.components;

import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.vaadin.service.SomeService;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class MyDialog extends Dialog implements KeyNotifier {

    private final SomeService someService;
    //Сообщение по которому клинкули
    private Message mess;

    private TextField recipients;
    private TextArea subject;
    private TextArea response;

    public String DOCK = "dock";
    public String FULLSCREEN = "fullscreen";

    private boolean isDocked = false;
    private boolean isFullScreen = false;

    private final Header header;
    private final Button min;
    private final Button max;

    private final VerticalLayout content;
    private final Footer footer;

    public MyDialog(SomeService someService) {
        this.someService = someService;
        setDraggable(true);
        setModal(false);
        setResizable(true);

        // Dialog theming
        getElement().getThemeList().add("my-dialog");
        setWidth("600px");

        // Accessibility
        getElement().setAttribute("aria-labelledby", "dialog-title");

        min = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        min.addClickListener(event -> minimise());

        max = new Button(VaadinIcon.EXPAND_SQUARE.create());
        max.addClickListener(event -> maximise());

        Button close = new Button(VaadinIcon.CLOSE_SMALL.create());
        close.addClickListener(event -> close());

        header = new Header(min, max, close);
        header.getElement().getThemeList().add(Lumo.DARK);
        add(header);

        recipients = new TextField("Почта получателя");
        subject = new TextArea("Полученное сообщение");
        subject.setMinHeight("200px");


        response = new TextArea();
        response.setPlaceholder("Текст ответа");
        response.setMinHeight("200px");

        content = new VerticalLayout(recipients, subject, response);
        content.addClassName("dialog-content");
        content.setAlignItems(FlexComponent.Alignment.STRETCH);
        add(content);

        // Footer
        Button send = new Button("Ответить");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.addClickListener(event -> send(mess));

        Button cancel = new Button("Сброс");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addClickListener(event -> cancel());

        Button delete = new Button("Удалить");
        delete.addClickListener(event -> delete(mess));
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        footer = new Footer(send, cancel, delete);

        add(footer);

        // Button theming
        for (Button button : new Button[]{min, max, close}) {
            button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        }
    }

    private void delete(Message mess) {
        someService.deleteMessage(mess);
        this.close();
    }

    private void send(Message mess) {
        this.close();
    }

    private void cancel() {
        recipients.setValue("");
        subject.setValue("");
        response.setValue("");
    }

    private void minimise() {
        if (isDocked) {
            initialSize();
        } else {
            if (isFullScreen) {
                initialSize();
            }
            min.setIcon(VaadinIcon.UPLOAD_ALT.create());
            getElement().getThemeList().add(DOCK);
            setWidth("320px");
        }
        isDocked = !isDocked;
        isFullScreen = false;
        content.setVisible(!isDocked);
        footer.setVisible(!isDocked);
    }

    private void initialSize() {
        min.setIcon(VaadinIcon.DOWNLOAD_ALT.create());
        getElement().getThemeList().remove(DOCK);
        max.setIcon(VaadinIcon.EXPAND_SQUARE.create());
        getElement().getThemeList().remove(FULLSCREEN);
        setHeight("auto");
        setWidth("600px");
    }

    private void maximise() {
        if (isFullScreen) {
            initialSize();
        } else {
            if (isDocked) {
                initialSize();
            }
            max.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
            getElement().getThemeList().add(FULLSCREEN);
            setSizeFull();
            content.setVisible(true);
            footer.setVisible(true);
        }
        isFullScreen = !isFullScreen;
        isDocked = false;
    }

    public void setMess(Message message) {
        this.mess = message;

        recipients.setValue(mess.getClient().getEmail());
        subject.setValue(mess.getMessage());
    }

}
