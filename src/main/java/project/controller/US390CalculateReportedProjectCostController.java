package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.services.TaskService;
import project.model.Project;
import project.model.Task;

import java.util.ArrayList;
import java.util.List;

@Controller
public class US390CalculateReportedProjectCostController {

	@Autowired
	private TaskService taskService;

	/*
	 * 
	 * This controller allows a Project Manager to get the reported cost to the
	 * Project he is the manager of
	 *
	 */

	/**
	 * @param project
	 *            The project that the Project Manager wants to know the reported
	 *            cost of
	 * @return This method returns a double, which is the value that was reported on
	 *         that project
	 */
	public double calculateReportedProjectCostController(Project project) {

		return taskService.getTotalCostReportedToProjectUntilNow(project);

	}

	/**
	 * @param project
	 *            The project that the Project Manager wants to know the reported
	 *            cost of
	 * @return The cost reported to each task.
	 */

	public List<String> calculeReportedCostOfEachTaskController(Project project) {

		return new ArrayList<>(taskService.getReportedCostOfEachTask(project));
	}

	public List<String> getTaskId(Project project) {
		List<String> taskID = new ArrayList<>();

		for (Task other : taskService.getProjectTasks(project)) {
			taskID.add(other.getTaskID());
		}

		return taskID;
	}

}