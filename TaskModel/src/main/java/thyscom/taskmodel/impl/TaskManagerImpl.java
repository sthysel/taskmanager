package thyscom.taskmodel.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;
import thyscom.taskmodel.api.Task;
import thyscom.taskmodel.api.TaskManager;

/**
 *
 * @author thys
 */
@ServiceProvider(service = TaskManager.class)
public class TaskManagerImpl implements TaskManager {

    private PropertyChangeSupport pcs;
    private List<Task> taskStore;
    private InstanceContent ic;
    private AbstractLookup lookup;

    public TaskManagerImpl() {
        taskStore = new ArrayList<Task>();
        this.pcs = new PropertyChangeSupport(this);
        ic = new InstanceContent();
        lookup = new AbstractLookup(ic);
    }

    @Override
    public Task createTask() {
        Task task = new TaskImpl();
        taskStore.add(task);
        pcs.firePropertyChange(PROPERTY_TASK_ADD, null, task);
        ic.add(task);
        return task;
    }

    @Override
    public Task createTask(String name, String parentID) {
        Task task = new TaskImpl(name, parentID);
        Task parent = getTask(parentID);
        if (parent != null) {
            parent.addChild(task);
        }

        pcs.firePropertyChange(PROPERTY_TASK_ADD, null, task);
        ic.add(task);
        return task;
    }

    @Override
    public boolean removeTask(Task task) {
        if (task != null) {
            return removeTask(task.getId());
        }
        return false;
    }

    @Override
    public boolean removeTask(String ID) {
        Task found = getTask(ID);
        if (found == null) {
            return false;
        }
        // root task
        if (found.getParentId().equals("")) {
            boolean rem = taskStore.remove(found);
            return fireRemoved(rem, taskStore, found);
        }
        // child task
        Task parent = getTask(found.getParentId());
        boolean rem = parent.removeChild(found);
        return fireRemoved(rem, parent.getChildren(), found);

    }

    private boolean fireRemoved(boolean removed, List<Task> list, Task found) {
        if (removed) {
            ic.remove(found);
            pcs.firePropertyChange(PROPERTY_TASK_REMOVE, list, found);
        }
        return removed;
    }

    @Override
    public Task getTask(String ID) {
        return searchForTask(ID, taskStore);
    }

    // search for the task
    private Task searchForTask(String ID, List<Task> taskList) {
        for (Task task : taskList) {
            if (task.getId().equals(ID)) {
                return task;
            } else {
                Task t = searchForTask(ID, task.getChildren());
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public List<Task> getAllParentTasks() {
        return (List<Task>) Collections.unmodifiableCollection(taskStore);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            pcs.addPropertyChangeListener(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            pcs.removePropertyChangeListener(listener);
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
