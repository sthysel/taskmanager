package thyscom.taskmodel.api;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author thys
 */
public interface Task extends Serializable {

    String getId();

    String getParentId();

    String getName();

    void setName(String name);

    Date getDueDate();

    void setDueDate(Date dueDate);

    enum Priority {

        LOW, MEDIUM, HIGH
    }

    Priority getPriority();

    void setPriority(Priority priority);

    int getProgress();

    void setProgress(int progress);

    String getDescription();

    void setDescription(String description);

    void addChild(Task childTask);

    List<Task> getChildren();

    boolean removeChild(Task child);

    // property change support
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERY_DUE_DATE = "due_date";
    public static final String PROPERTY_PRIORITY = "priority";
    public static final String PROPERTY_PROGRESS = "progress";
    public static final String PROPERY_DESCRIPTION = "description";
    public static final String PROPERTY_CHILD_ADD = "child_add";
    public static final String PROPERTY_CHILD_REMOVE = "child_remove";
}
