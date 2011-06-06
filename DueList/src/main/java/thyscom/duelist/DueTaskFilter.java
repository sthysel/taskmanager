package thyscom.duelist;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import thyscom.taskmodel.api.Task;
import thyscom.taskmodel.api.TaskManager;

/**
 * Filters through all tasks looking for due tasks for the given week
 * @author thys
 */
public class DueTaskFilter {

    TaskManager taskManager;

    public DueTaskFilter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Returns dueTasks for the week
     * @param week
     * @param dueTasks 
     */
    public List<Task> getAllDueTasksForGivenWeek(int week) {
        ArrayList<Task> dueTasks = new ArrayList<Task>();
        Interval interval = getInterval(week);
        if (taskManager != null) {
            List<Task> parents = taskManager.getAllParentTasks();
            for (Task task : parents) {
                findDueTasks(interval, task, dueTasks);
            }
        }
        return dueTasks;
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
    private Interval getInterval(int week) {
        // get today, set the week to the spinner value
        // set the day to thebegiining of the week to get the start time
        // add a week to get the end time
        DateTime now = new DateTime();
        DateTime begin = now.withWeekyear(week);
        DateTime end = begin.plusDays(7);
        return new Interval(begin, end);
    }
}
