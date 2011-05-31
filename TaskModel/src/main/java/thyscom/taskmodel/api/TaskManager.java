package thyscom.taskmodel.api;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author thys
 */
public interface TaskManager extends Lookup.Provider {

    Task createTask();

    Task createTask(String name, String parent);

    boolean removeTask(Task task);

    boolean removeTask(String ID);

    Task getTask(String ID);

    List<Task> getAllParentTasks();
    static final String PROPERTY_TASK_ADD = "task_added";
    static final String PROPERTY_TASK_REMOVE = "task_removed";

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
