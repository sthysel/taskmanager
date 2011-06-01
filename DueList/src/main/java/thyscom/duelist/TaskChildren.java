package thyscom.duelist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import thyscom.taskmodel.api.Task;

import thyscom.taskmodel.api.TaskManager;

/**
 *
 * @author thys
 */
public class TaskChildren extends Children.Array implements ChangeListener, PropertyChangeListener {

    private JSpinner spinner;
    private TaskManager taskManager;

    public TaskChildren(JSpinner spinner) {
        this.spinner = spinner;
        spinner.addChangeListener(this);
        taskManager = Lookup.getDefault().lookup(TaskManager.class);
        taskManager.addPropertyChangeListener(this);
    }

    @Override
    public Collection<Node> initCollection() {
        ArrayList<TaskNode> taskNodes = new ArrayList<TaskNode>();

        if (taskManager != null) {
            List<Task> parents = taskManager.getAllParentTasks();
            for (Task task : parents) {
                taskNodes.add(new TaskNode(task));
            }
        }
        return taskNodes;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateNodes();
    }

    private void updateNodes() {
        remove(getNodes());
        add(initCollection().toArray(new Node[]{}));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (isThisATaskEvent(evt)) {
            updateNodes();
        }
    }

    private boolean isThisATaskEvent(PropertyChangeEvent evt) {
        return (evt.getSource() instanceof TaskManager)
                && (evt.getPropertyName().equals(TaskManager.PROPERTY_TASK_ADD) || evt.getPropertyName().equals(TaskManager.PROPERTY_TASK_REMOVE));
    }
}
