package project.ui.console.collaborator;

import project.controller.*;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.User;
import project.ui.console.MainMenuUI;

import java.util.Scanner;

public class TaskDetailsUI {
	private User user;
	private Integer projectID;
	private String taskID;
	private Task task;

	public TaskDetailsUI(String taskID, Integer projectID, User user) {
		this.taskID = taskID;
		this.projectID = projectID;
		this.user = user;
	}

	private static void printMenuOption(PrintProjectInfoController projectInfo, PrintTaskInfoController taskInfo){
		System.out.println("");
		System.out.println("PROJECT - " + projectInfo.printProjectNameInfo());
		System.out.println("");
		System.out.println("                     TASK                    ");
		System.out.println("*** " + taskInfo.printTaskNameInfo().toUpperCase() + " ***");
		System.out.println("______________________________________________");
		System.out.println("ID: " + taskInfo.printTaskIDCodeInfo());
		System.out.println("STATUS: " + taskInfo.printTaskStateInfo());
		System.out.println("ESTIMATED START DATE: " + taskInfo.printTaskEstimatedStartDateInfo());
		System.out.println("START DATE: " + taskInfo.printTaskStartDateInfo());
		System.out.println("DEADLINE: " + taskInfo.printTaskDeadlineInfo());
		System.out.println("FINISH DATE: " + taskInfo.printTaskFinishDateInfo());
		System.out.println("TASK TEAM: " + taskInfo.printTaskTeamInfo());
		System.out.println("TASK BUDGET: " + taskInfo.printTaskBudgetInfo());
		System.out.println("");

		System.out.println("[1] Mark task as completed");
		System.out.println("[2] Request assignment to task team");
		System.out.println("[3] Request task team unassignment");
		System.out.println("[4] Create/Update task report");
		System.out.println("______________________________________________");
		System.out.println("[B] Back \n");
	}

	/**
	 * This method executes all options to execute through this UI Presents the task
	 * details and the options about this specific task Uses a switch case to treat
	 * the user's input
	 */
	public void taskDataDisplay() {
		String cantDoIt = "You can't do it because you aren't assigned to this task.";
		PrintTaskInfoController taskInfo = new PrintTaskInfoController(this.taskID, this.projectID);
		taskInfo.setProjectAndTask();
		PrintProjectInfoController projectInfo = new PrintProjectInfoController(this.projectID);
		projectInfo.setProject();

		boolean condition = true;
		while (condition) {
			condition = false;

			printMenuOption(projectInfo, taskInfo);

			Scanner scannerInput = new Scanner(System.in);
			String choice = scannerInput.nextLine().toUpperCase();
			switch (choice) {
			case "1":
				US204v2createRequestAddCollaboratorToTaskTeamController controllerMember = new US204v2createRequestAddCollaboratorToTaskTeamController(
						this.taskID, this.user);
				task = controllerMember.getTaskByTaskID(this.taskID);
				ProjectCollaborator projCollaborator = new ProjectCollaborator(this.user, this.projectID);

				if (!task.isProjectCollaboratorActiveInTaskTeam(projCollaborator)) {
					System.out.println(cantDoIt);
				} else {
					US205MarkTaskAsFinishedCollaboratorController taskToMark = new US205MarkTaskAsFinishedCollaboratorController();
					taskToMark.getProjectsThatIAmCollaborator(this.user);
					taskToMark.getUnfinishedTasksOfProjectFromCollaborator(this.projectID);
					taskToMark.getTaskToBeMarkedFinished(this.taskID);
					taskToMark.markTaskAsFinished();
					System.out.println("---- SUCCESS Task Marked As Finished ----");
				}
				break;
			case "2":
				US204v2CreateTaskAssignmentToCollaboratorUI createAssignmentRequest = new US204v2CreateTaskAssignmentToCollaboratorUI(user, taskID, projectID);
				createAssignmentRequest.createTaskAssignment();
				break;
			case "3":
				US204v2createRequestAddCollaboratorToTaskTeamController controllerMember1 = new US204v2createRequestAddCollaboratorToTaskTeamController(this.taskID, this.user);
				task = controllerMember1.getTaskByTaskID(this.taskID);
				ProjectCollaborator projCollaborator1 = new ProjectCollaborator(this.user, this.projectID);
				checkAndAddRemovalRequest(projCollaborator1, cantDoIt);
				break;
			case "4":
				US204v2createRequestAddCollaboratorToTaskTeamController controllerMember2 = new US204v2createRequestAddCollaboratorToTaskTeamController(this.taskID, this.user);
				task = controllerMember2.getTaskByTaskID(this.taskID);
				ProjectCollaborator projCollaborator2 = new ProjectCollaborator(this.user, this.projectID);
				checkAndCreateReportRequest(projCollaborator2, cantDoIt);
				break;
			case "B":
				break;
			default:
				System.out.println("Please choose a valid option.");
				System.out.println("");
				condition = true;
				break;
			}
		}
	}

	private void checkAndAddRemovalRequest(ProjectCollaborator projCollaborator1, String cantDoIt) {
		if (!task.isProjectCollaboratorActiveInTaskTeam(projCollaborator1)) {
			System.out.println(cantDoIt);
		} else {
			US206CreateRemovalTaskRequestUI createCollabRemovalRequest = new US206CreateRemovalTaskRequestUI(user,
					taskID);
			createCollabRemovalRequest.cancelRemovalTaskRequestUI();
		}
	}

	private void checkAndCreateReportRequest(ProjectCollaborator projCollaborator2, String cantDoIt) {
		if (!task.isProjectCollaboratorActiveInTaskTeam(projCollaborator2)) {
			System.out.println(cantDoIt);
		} else {
			US207And208CreateOrUpdateTaskReportUI reportUI = new US207And208CreateOrUpdateTaskReportUI(user.getEmail(),
					taskID);


			reportUI.createReport();
		}
	}

//	public void goToPreviousUI(Integer projectID, User user) {
//		this.projectID = projectID;
//		this.user = user;
//		if (this.isPreviousUIFromTasks) {
//			ProjectViewMenuUI projectView = new ProjectViewMenuUI(projectID, user);
//			projectView.projectDataDisplay();
//		} else {
//			UserTasksFunctionalitiesMenuUI userTasks = new UserTasksFunctionalitiesMenuUI(user);
//			userTasks.displayFunctionalities();
//		}
//	}
}
