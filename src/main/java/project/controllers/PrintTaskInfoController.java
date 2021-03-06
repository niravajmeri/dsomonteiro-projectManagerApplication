package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.User;
import project.services.ProjectService;
import project.services.TaskService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PrintTaskInfoController {

	@Autowired
	private ProjectService projService;

	@Autowired
	private TaskService taskService;


	private Task task;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	private String taskID;
	private Integer projeID;
	private Project project;


	public PrintTaskInfoController() {
		//Empty constructor created for JPA integration tests

	}

	PrintTaskInfoController(Task task) {
		this.task = task;
	}

	public void setProjectAndTask() {
		this.project = projService.getProjectById(projeID);
		this.task = taskService.getTaskByTaskID(taskID);
	}
	
	/**
	 * This method get the task's name and return it as a String
	 *
	 * @return String task's name
	 */
	public String printTaskNameInfo() {
		return this.task.getDescription();
	}

	/**
	 * This method get the task's ID and return it as a String
	 *
	 * @return String task's ID
	 */
	public String printTaskIDCodeInfo() {
		return this.task.getTaskID();
	}

	/**
	 * This method get the task's state and return it as a String
	 *
	 * @return String task's state
	 */
	public String printTaskStateInfo() {

		return this.task.viewTaskStateNameFromEnum();
	}

	/**
	 * This method get the task's estimated start date and return it as a String
	 *
	 * @return String task's estimated start date
	 */
	public String printTaskEstimatedStartDateInfo() {
		Calendar estimatedStartDate = this.task.getEstimatedTaskStartDate();
		String toPrint = "---";
		if (estimatedStartDate != null) {
			toPrint = this.dateFormat.format(estimatedStartDate.getTime());
		}
		return toPrint;
	}

	/**
	 * This method get the task's start date and return it as a String
	 *
	 * @return String task's start date
	 */
	public String printTaskStartDateInfo() {
		Calendar startDate = this.task.getStartDate();
		String toPrint = "---";
		if (startDate != null) {
			toPrint = this.dateFormat.format(startDate.getTime());
		}
		return toPrint;
	}

	/**
	 * This method get the task's deadline and return it as a String
	 *
	 * @return String task's deadline
	 */
	public String printTaskDeadlineInfo() {
		Calendar deadlineDate = this.task.getTaskDeadline();
		String toPrint = "---";
		if (deadlineDate != null) {
			toPrint = this.dateFormat.format(deadlineDate.getTime());
		}
		return toPrint;
	}

	/**
	 * This method get the task's finish date and return it as a String
	 *
	 * @return String task's finish date
	 */
	public String printTaskFinishDateInfo() {
		Calendar finishDate = this.task.getFinishDate();
		String toPrint = "---";
		if (finishDate != null) {
			toPrint = this.dateFormat.format(finishDate.getTime());
		}
		return toPrint;
	}

	/**
	 * This method get the task's team and return it as a String (team members
	 * separeted by commas)
	 *
	 * @return String task's team
	 */
	public String printTaskTeamInfo() {
		List<ProjectCollaborator> projectTeam = taskService.getProjectCollaboratorsFromTask(project, task);


		List<String> team = projectTeam.stream()
				.map(ProjectCollaborator::getUserFromProjectCollaborator)
				.map(User::getName)
				.collect(Collectors.toList());

		return String.join(", ", team);
	}

	/**
	 * This method get the task's budget and return it as a String
	 *
	 * @return String task's budget
	 */
	public String printTaskBudgetInfo() {
		return String.valueOf(this.task.getTaskBudget());
	}

	/**
	 * this method get the name of project associated with taskiD.
	 * 
	 * @return name of project associated with taskID
	 */
	public String printProjectNameInfo() {
		return this.project.getName();
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public void setProjeID(Integer projeID) {
		this.projeID = projeID;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public Task getTask() {
		return task;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public String getTaskID() {
		return taskID;
	}

	public Integer getProjeID() {
		return projeID;
	}

	public Project getProject() {
		return project;
	}

}
