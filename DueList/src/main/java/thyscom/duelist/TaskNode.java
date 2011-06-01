package thyscom.duelist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import thyscom.taskmodel.api.Task;

/**
 * Task wrapper for node system
 * @author thys
 */
public class TaskNode extends AbstractNode {

    public TaskNode(Task task) {
        super(Children.LEAF, Lookups.singleton(task));

        setName(task.getId());
        setDisplayName(task.getName());

        // need to update the display name when it changes
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(Task.PROPERTY_NAME)) {
                    setName(evt.getNewValue() + "");
                }
            }
        });
        
    }
}
