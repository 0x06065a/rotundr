package ru.stereohorse.rotundr.server;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class WebUI extends Application {
    private static final long serialVersionUID = 8108499458505342554L;

    @Override
    public void init() {
        final Window mainWindow = new Window("Rotundr server");
        setMainWindow(mainWindow);
    }
}
