package main.project.model;


import java.util.Calendar;


public class TaskWorker {

	private ProjectCollaborator projCollaborator;
	private Calendar startDate;
	private Calendar finishDate;
//	private Integer hoursSpent;
//	private Integer costPerEffort;
	private boolean status;

	/**
	 * Constructor to create a new task worker
	 * 
	 * Collaborator is set as the Project Collaborator provided. A start date is set
	 * automatically. Hours Spent and Cost are set from the Project Collaborator.
	 * Finish date is added after collaborator is removed.
	 * 
	 * @param collaborator
	 *            user to set
	 */
	public TaskWorker(ProjectCollaborator projCollaborator) {
		this.projCollaborator = projCollaborator;
		this.startDate = Calendar.getInstance();
		this.finishDate = null;
//		this.hoursSpent = null;
//		this.costPerEffort = projCollaborator.getCollaboratorCost();
		this.status = true;
	}

	/**
	 * Returns the user associated to the ProjectCollaborator of this Task Worker
	 * 
	 * @return user
	 */
	public User getTaskWorker() {
		return this.projCollaborator.getCollaboratorUserData();
	}
	
	/**
	 * Returns the ProjectCollaborator of this Task Worker
	 * 
	 * @return Project Collaborator
	 */
	public ProjectCollaborator getProjectCollaboratorFromTaskWorker() {
		return this.projCollaborator;
	}
	
	/**
	 * Returns True if the Project Collaborator is in this TaskWorker
	 *  false if not
	 * 
	 * @return boolean
	 */
	public boolean isProjectCollaboratorInTaskWorker(ProjectCollaborator projCollabToCheck) {
	
		 return this.projCollaborator.equals(projCollabToCheck);

	}

	/**
	 * Returns the state of the TaskWorker.
	 * If the TaskWorker don't have a finish date, then it's Active, and returns True
	 * If the TaskWorker has a finish date, then it's Inactive, and returns False
	 * 
	 * @return boolean
	 */
	public boolean isTaskWorkerActiveInTask() {
	
		 return this.finishDate == null;

	}

	/**
	 * Adds a Finish Date to the task worker
	 */
	public void addFinishDateForTaskWorker() {
		this.finishDate = Calendar.getInstance();
		this.status = false;
	}


	
	/**
	 * Returns a specific Start Date
	 * 
	 * @param i
	 *            index of the the date wanted
	 * 
	 * @return StartDate
	 */
	public Calendar getStartDate() {
		return this.startDate;
	}

	/**
	 * Returns a specific Finish Date
	 * 
	 * @param i
	 *            index of the date wanted
	 * 
	 * @return Finish Date
	 */
	public Calendar getFinishDate() {
		return this.finishDate;
	}

	
//
//	/**
//	 * Returns the total hours spent by the collaborator
//	 * 
//	 * 
//	 * @return TotalHoursSpent
//	 */
//	public int getTotalHoursSpent() {
//
//		int result = 0;
//
//		for (int indexHoursSpent = 0; indexHoursSpent < this.hoursSpent.size(); indexHoursSpent++) {
//
//			result = result + this.hoursSpent.get(indexHoursSpent);
//		}
//
//		return result;
//
//	}
//
//	/**
//	 * Returns the cost of the collaborator in a specific period
//	 * 
//	 * @param i
//	 *            index of the cost wanted
//	 * 
//	 * @return cost
//	 */
//	public int getCost(int i) {
//		return cost.get(i);
//	}
//
//	/**
//	 * Sets the hours spent by the user in this task in this period
//	 * 
//	 * @param hoursSpent
//	 */
//	public void setHoursSpent(int hoursSpent) {
//		this.hoursSpent.set(this.hoursSpent.size() - 1, hoursSpent);
//	}
//
//	/**
//	 * Gets the size of the cost list
//	 * 
//	 */
//	public int getCostListSize() {
//		return this.cost.size();
//	}

}