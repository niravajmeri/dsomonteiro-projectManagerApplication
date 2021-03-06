package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.model.Task;
import project.services.TaskService;

@Controller
public class US367MarkFinishedTaskAsUnfinishedController {

	@Autowired
	private TaskService taskService;

	private String taskID;


	public US367MarkFinishedTaskAsUnfinishedController() {
		//Empty constructor created for JPA integration tests

	}

	/*
	 * Getters and Setters
	 */

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	/**
	 * This constructor receives a Project and a Task's ID, storing these values and
	 * allowing them to be used when removing a task's finished state
	*/

	/**
	 *
	 * If this option is selected by the UI, it removes the selected task's finished
	 * state and returns it to StandBy state
	 *
	 */

	public void markFinishedTaskAsUnfinished(String idTask) {
		Task task;
		task = taskService.getTaskByTaskID(idTask);
		task.markAsOnGoing();
		taskService.saveTask(task);

	}

}
