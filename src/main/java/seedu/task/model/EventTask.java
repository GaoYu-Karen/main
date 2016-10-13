package seedu.task.model;
import seedu.task.model.task.Name;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskDate;

/*
 * Represents a task as an event, i.e. it will have a start and end time
 */
public class EventTask extends Task{
	private TaskDate startDate, endDate;
	
	public EventTask(Name name, TaskDate startDate, TaskDate endDate) {
		super(name);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public TaskDate getStartDate(){
		return startDate;
	}
	
	/*
	 * Replaces this task's end date with the new end date
	 */
	public void setStartDate(TaskDate startDate){
		this.startDate = startDate;
	}
	
	public TaskDate getEndDate(){
		return endDate;
	}
	
	/*
	 * Replaces this task's end date with the new end date
	 */
	public void setEndDate(TaskDate newEndDate){
		this.endDate = newEndDate;
	}
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName().toString());
        sb.append(" start from ");
        sb.append(startDate.toString());
        sb.append(" to ");
        sb.append(endDate.toString());
        return sb.toString();
    }
	
	@Override
	public TaskDate getStart() {
		return startDate;
	}
	
	@Override
	public TaskDate getEnd() {
		return endDate;
	}

}