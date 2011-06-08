package thyscom.duelist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import thyscom.taskmodel.api.Task;
import thyscom.taskmodel.api.TaskManager;

/**
 * Filters through all tasks looking for due tasks for the given week
 * @author thys
 */
public class DueTaskFilter {

    private TaskManager taskManager;

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
        } else {
        }
        return dueTasks;
    }

    /*
     * Recursively populate the duetask list
     */
    private void findDueTasks(Interval interval, Task task, List<Task> dueTasks) {
        DateTime duedate = new DateTime(task.getDueDate());
        out(duedate.toString());
        out(interval.toString());
        if (interval.contains(duedate)) {
            out(duedate.toString());
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
    public Interval getInterval(int week) {
        // get today, set the week to the spinner value
        // set the day to the begining of the week to get the start time
        // add a week to get the end time
        DateTime now = new DateTime();      
        // wow is this the only way ?
        DateTime begin = now.weekOfWeekyear().setCopy(week).dayOfWeek().setCopy(DateTimeConstants.MONDAY).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        DateTime end = begin.plusDays(7);
        return new Interval(begin, end);
    }
    
    /**
     * Write to the output window
     * @param msg 
     */
    private void out(String msg) {
        InputOutput io = IOProvider.getDefault().getIO("Due Task Filter", false);
        io.getOut().println(msg);
        io.getOut().close();
    }
}
