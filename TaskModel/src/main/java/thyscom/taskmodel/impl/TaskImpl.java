package thyscom.taskmodel.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.openide.util.Lookup;
import thyscom.taskidgenerator.api.IDGenerator;
import thyscom.taskmodel.api.Task;

/**
 *
 * @author thys
 */
public class TaskImpl implements Task {

    private String ID = "";
    private String parentID = "";
    private String name = "";
    private Date dueDate = new Date();
    private Priority priority = Priority.LOW;
    private String description = "";
    private List<Task> children = new ArrayList<Task>();
    private PropertyChangeSupport changeSupport;
    private int progress = 0;

    public TaskImpl(String name, String parentID) {
        this.name = name;
        this.parentID = parentID;
        this.ID = assignID();
        this.children = new ArrayList<Task>();
        changeSupport = new PropertyChangeSupport(this);
    }

    public TaskImpl() {
        this("", "");
    }

    private String assignID() {
        IDGenerator idGen = Lookup.getDefault().lookup(IDGenerator.class);
        if (idGen != null) {
            return "" + idGen.getID();
        } else {
            return "XXX" + System.currentTimeMillis();
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getParentId() {
        return parentID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        changeSupport.firePropertyChange(PROPERTY_NAME, old, name);
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public void setDueDate(Date dueDate) {
        Date old = this.dueDate;
        this.dueDate = dueDate;
        changeSupport.firePropertyChange(PROPERY_DUE_DATE, old, dueDate);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Priority priority) {
        Priority old = this.priority;
        this.priority = priority;
        changeSupport.firePropertyChange(PROPERTY_PRIORITY, old, priority);
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        int old = this.progress;
        this.progress = progress;
        changeSupport.firePropertyChange(PROPERTY_PROGRESS, old, progress);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        String old = this.description;
        this.description = description;
        changeSupport.firePropertyChange(PROPERY_DESCRIPTION, old, description);
    }

    @Override
    public void addChild(Task childTask) {
        children.add(childTask);
        changeSupport.firePropertyChange(PROPERTY_CHILD_ADD, null, children);
    }

    @Override
    public List<Task> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public boolean removeChild(Task child) {
        boolean res = children.remove(child);
        changeSupport.firePropertyChange(PROPERTY_CHILD_REMOVE, null, children);
        return res;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final TaskImpl other = (TaskImpl) obj;
        return other.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return 777 + (getId() != null ? getId().hashCode() : 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + getId() + "\n");
        sb.append("ParentID: " + getParentId() + "\n");
        sb.append("Name: " + getName() + "\n");
        sb.append("Due date:" + DateFormat.getInstance().format(dueDate) + "\n");
        sb.append("Description: " + getDescription() + "\n");
        sb.append("Priority: " + getPriority() + "\n");
        sb.append("Progress: " + getProgress() + "\n");

        return sb.toString();
    }
}
