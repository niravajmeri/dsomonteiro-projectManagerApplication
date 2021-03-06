package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.model.Project;
import project.model.Task;
import project.services.TaskService;

import java.util.Calendar;

@Controller
public class US340CreateTaskController {

	@Autowired
	private TaskService taskService;

	/**
	 * This constructor creates a target controllers. Currently, it receives a
	 * project but it should receive a Project Controller or Project Manager
	 * controllers
	 */
	public US340CreateTaskController() {
		//Empty constructor created for JPA integration tests

	}

	/**
	 * This controllers calls the create task method, then the add task method, and
	 * finally confirms whether it was added successfully
	 *
	 * @return the added task
	 */
	public boolean addTask(String description, Project chosenProject) {
		Boolean wasTaskSaved = true;
		Task newTask = taskService.createTask(description, chosenProject);
		if (newTask == null) {
			wasTaskSaved = false;
		}
		return wasTaskSaved;

	}

	public void setEstimatedStartDate(Task task, Calendar estimatedStartDate){
		task.setEstimatedTaskStartDate(estimatedStartDate);
	}

	public void setDeadline(Task task, Calendar deadLine){
		task.setTaskDeadline(deadLine);
	}

}
