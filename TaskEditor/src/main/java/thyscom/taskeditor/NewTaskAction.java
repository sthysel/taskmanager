/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thyscom.taskeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import thyscom.taskmodel.api.Task;
import thyscom.taskmodel.api.TaskManager;

@ActionID(category = "Edit",
id = "thyscom.taskeditor.NewTaskAction")
@ActionRegistration(iconBase = "thyscom/taskeditor/new.png",
displayName = "#CTL_NewTaskAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = "Toolbars/Edit", position = 1300),
    @ActionReference(path = "Shortcuts", name = "D-A", position = 1300)
})
@Messages("CTL_NewTaskAction=New Task")
public final class NewTaskAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TaskManager taskManager = Lookup.getDefault().lookup(TaskManager.class);
        if (taskManager != null) {
            Task task = taskManager.createTask();
            TaskEditorTopComponent tc = TaskEditorTopComponent.getInstance(task);
            tc.open();
            tc.requestActive();
        }
    }
}
