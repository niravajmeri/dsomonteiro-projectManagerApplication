package project.controller;

import project.Services.TaskService;
import project.model.Project;
import project.model.Task;

public class US340CreateTaskController {

	private TaskService taskContainer;

	/**
	 * This constructor creates a target controller. Currently, it receives a
	 * project but it should receive a Project Controller or Project Manager
	 * controller
	 * 
	 * 
	 * @param target
	 *            Task Repository
	 */
	public US340CreateTaskController(Project target) {

		this.taskContainer = target.getTaskRepository();
	}

	public TaskService getTaskRepository() {
		return this.taskContainer;
	}

	/**
	 * This controller calls the create task method, then the add task method, and
	 * finally confirms whether it was added successfully
	 * 
	 * @param taskDescription
	 * @param estimatedTaskEffort
	 * @param estimatedTaskStartDate
	 * @param taskDeadline
	 * @param estimatedCost
	 * @return the added task
	 */
	public Task addTask(String description) {
		Task newTask = taskContainer.createTask(description);

		taskContainer.addTaskToProject(newTask);

		return newTask;

	}

}
