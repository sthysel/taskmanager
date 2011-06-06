package thyscom.duelist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
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
    private DueTaskFilter dueFilter;
    
    public TaskChildren(JSpinner spinner) {
        this.spinner = spinner;
        spinner.addChangeListener(this);
        taskManager = Lookup.getDefault().lookup(TaskManager.class);
        dueFilter = new DueTaskFilter(taskManager);
        taskManager.addPropertyChangeListener(this);
    }
    
    @Override
    public Collection<Node> initCollection() {
        
        ArrayList<Task> dueTasks = new ArrayList<Task>();

        // populate the duetasks list
        dueFilter.getAllDueTasksForGivenWeek((Integer) spinner.getValue(), dueTasks);

        // wrap tasks in nodes and register propertychangelistener
        Collection<Node> dueNodes = new ArrayList<Node>(dueTasks.size());
        for (Task task : dueTasks) {
            task.addPropertyChangeListener(this);
            dueNodes.add(new TaskNode(task));
        }
        
        return dueNodes;
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
