package com.skillbox.microservice.vaadin.view;

import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.vaadin.components.MyDialog;
import com.skillbox.microservice.vaadin.service.SomeService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.util.List;
import java.util.Optional;

@Route("support/messages")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends HorizontalLayout {

    private final MyDialog myDialog;
    private final SomeService someService;

    private Grid<Message> grid = new Grid<>(Message.class, false);

    public MainView(SomeService someServ, MyDialog myDialog) {
        this.someService = someServ;
        this.myDialog = myDialog;

        List<Message> messageList = someService.getAllMessages();

        grid.setItems(messageList);
        grid.addColumn(Message::getId).setWidth("50px").setHeader("Номер");
        grid.addColumn(Message::getClient).setAutoWidth(true).setHeader("Пользователь");
        grid.addColumn(Message::getMessage).setAutoWidth(true).setHeader("Сообщение");
        grid.addColumn(Message::getDateOfApplication).setAutoWidth(true).setHeader("Дата");
        grid.addSelectionListener(event -> {
            Optional<Message> messageOptional = event.getFirstSelectedItem();
            messageOptional.ifPresent(myDialog::setMess);
            myDialog.open();
        });

//        addClassName("centered-content");
        add(grid);
    }

}
