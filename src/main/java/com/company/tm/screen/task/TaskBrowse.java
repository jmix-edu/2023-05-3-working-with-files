package com.company.tm.screen.task;

import com.company.tm.app.TaskImportService;
import com.company.tm.entity.Task;
import io.jmix.ui.Notifications;
import io.jmix.ui.Notifications.NotificationType;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("tm_Task.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {

    @Autowired
    private CollectionLoader<Task> tasksDl;

    @Autowired
    private TaskImportService taskImportService;
    @Autowired
    private Notifications notifications;

    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Downloader downloader;

    @Subscribe("importBtn")
    public void onImportBtnClick(Button.ClickEvent event) {
        int tasks = taskImportService.importTasks();
        if (tasks > 0) {
            notifications.create()
                    .withCaption(tasks + " tasks imported")
                    .withType(NotificationType.TRAY)
                    .show();
        }

        tasksDl.load();
    }

    @Install(to = "tasksTable.attachment", subject = "columnGenerator")
    private Component tasksTableAttachmentColumnGenerator(Task task) {
        LinkButton linkButton = uiComponents.create(LinkButton.class);

        linkButton.setCaption(task.getAttachment().getFileName());
        linkButton.addClickListener(clickEvent ->
                downloader.download(task.getAttachment()));

        return linkButton;
    }
}