package thyscom.duelist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.joda.time.DateTime;
import org.joda.time.Interval;
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

        ArrayList<Task> dueTasks = new ArrayList<Task>();

        // populate the duetasks list
        getAllDueTasksForGivenWeek(dueTasks);

        // wrap tasks in nodes and register propertychangelistener
        Collection<Node> dueNodes = new ArrayList<Node>(dueTasks.size());
        for (Task task : dueTasks) {
            task.addPropertyChangeListener(this);
            dueNodes.add(new TaskNode(task));
        }

        return dueNodes;
    }

    private void getAllDueTasksForGivenWeek(List<Task> dueTasks) {
        Interval interval = getInterval();
        if (taskManager != null) {
            List<Task> parents = taskManager.getAllParentTasks();
            for (Task task : parents) {
                findDueTasks(interval, task, dueTasks);
            }
        }
    }

    /*
     * Recursively populate the duetask list
     */
    private void findDueTasks(Interval interval, Task task, List<Task> dueTasks) {
        DateTime duedate = new DateTime(task.getDueDate());
        if (interval.contains(duedate)) {
            dueTasks.add(task);
            for (Task child : task.getChildren()) {
                findDueTasks(interval, child, dueTasks);
            }
        }
    }

    /**
     * Given the week, get the period of the week
     * @return 
     */
    private Interval getInterval() {
        // get today, set the week to the spinner value
        // set the day to thebegiining of the week to get the start time
        // add a week to get the end time
        DateTime now = new DateTime();
        DateTime begin = now.withWeekyear((Integer) spinner.getValue());
        DateTime end = begin.plusDays(7);
        return new Interval(begin, end);
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
//        if (thisIsATaskEvent(evt)) {
//            updateNodes();
//        }
        updateNodes();
    }

    private boolean thisIsATaskEvent(PropertyChangeEvent evt) {
        return (evt.getSource() instanceof TaskManager)
                && (evt.getPropertyName().equals(TaskManager.PROPERTY_TASK_ADD) || evt.getPropertyName().equals(TaskManager.PROPERTY_TASK_REMOVE));
    }
}
