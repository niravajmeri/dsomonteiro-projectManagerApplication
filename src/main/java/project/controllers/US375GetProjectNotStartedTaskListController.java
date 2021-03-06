package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.model.Project;
import project.model.Task;
import project.services.TaskService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class US375GetProjectNotStartedTaskListController {

	@Autowired
	private TaskService taskService;


	public US375GetProjectNotStartedTaskListController() {
		//Empty constructor created for JPA integration tests

	}

	/**
	 * this method return the list of non-started tasks
	 *
	 * @param proj
	 *            project for which to get the non-started tasks
	 * @return a list of tasks in the specified project that have not been started
	 */
	public List<Task> getProjectNotStartedTasks(Project proj) {
		return taskService.getProjectUnstartedTasks(proj);
	}

	/**
	 * This methods gets all the not started task and returns the Tasks with these
	 * conditions in the form of a List of Strings, with the taskId and Description
	 * of each task.
	 *
	 * @param proj
	 *            The project to search for not started Tasks
	 * @return Task List
	 */
	public List<String> getProjectNotStartedTaskList(Project proj) {

		List<Task> taskListNotStarted = taskService.getProjectUnstartedTasks(proj);
		List<String> taskListToPrint = new ArrayList<>();

		for (int i = 0; i < taskListNotStarted.size(); i++) {

			String taskDescription = taskListNotStarted.get(i).getDescription();
			String taskID = "[" + taskListNotStarted.get(i).getTaskID() + "]";
			String taskIDandDescription = taskID + " " + taskDescription;
			taskListToPrint.add(taskIDandDescription);
		}

		return taskListToPrint;
	}
}
