package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.model.Project;
import project.model.Task;
import project.model.TaskTeamRequest;
import project.services.TaskService;

import java.util.List;

@Controller
public class US356ManageAssigmentRequestController {

	private Project selectedProject;
	private TaskTeamRequest selectedAdditionRequest;
	private Task selectedTask;

	@Autowired
	private TaskService taskService;

	/*
	 * This controllers manages Addition Requests by Project Collaborators * respond
	 * to US 356
	*/

	/*
	 * Getters and Setters
	 */
	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public TaskTeamRequest getSelectedAdditionRequest() {
		return selectedAdditionRequest;
	}

	public void setSelectedAdditionRequest(TaskTeamRequest selectedAdditionRequest) {
		this.selectedAdditionRequest = selectedAdditionRequest;
	}

	public Task getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
	}


	/**
	 * This method shows a list of strings from all addition requests from the
	 * selected project
	 * 
	 * 
	 * @return returns a list of strings with all addition requests, or an empty
	 *         list if the project is null
	 */
	public List<String> showAllAssignmentRequests(Project selectedProject) {

		return taskService.viewAllProjectTaskAssignmentRequests(selectedProject);
	}

	/**
	 * Sets the seletedAdditionRequest. The result is equal to the
	 * getPendingTaskAssigmentRequest().get(index)
	 * 
	 * @param index
	 *            Index of the request.
	 */
	public void setSelectedAdditionRequest(int index, Project selectedProject) {
		this.selectedAdditionRequest = taskService.getAllProjectTaskAssignmentRequests(selectedProject).get(index);
		this.selectedTask = selectedAdditionRequest.getTask();
	}

	/**
	 * This method approves an assignment request: Gets the target request from the
	 * index number, adding its collaborator field to the task in its task field
	 *
	 * 
	 * @return returns true or false if the request was found and approved
	 *         successfully
	 */
	public boolean approveAssignmentRequest() {
		if (selectedAdditionRequest != null) {
			selectedTask.approveTaskAssignmentRequest(selectedAdditionRequest.getProjCollab());
			taskService.saveTask(selectedTask);


			return true;
		} else
			return false;
	}

	/**
	 * This method rejects an assignment request: Gets the target request from the
	 * index number, adding its collaborator field to the task in its task field
	 *
	 * 
	 * @return returns true or false if the request was found and rejected
	 *         successfully
	 */
	public boolean rejectAssignmentRequest() {
		if (selectedAdditionRequest != null) {
			deleteRequest();

			return true;
		} else
			return false;
	}

	/**
	 * This method deletes an addition request; to be called after the approve or
	 * reject methods have resolved
	 * 
	 */
	public void deleteRequest() {

		selectedTask.rejectTaskAssignmentRequest(selectedAdditionRequest.getProjCollab());
		taskService.saveTask(selectedTask);
	}
}