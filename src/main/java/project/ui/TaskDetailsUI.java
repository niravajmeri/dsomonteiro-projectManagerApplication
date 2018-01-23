package project.ui;

import java.util.Scanner;

import project.controller.PrintProjectInfoController;
import project.controller.PrintTaskInfoController;
import project.controller.US205MarkTaskAsFinishedCollaborator;
import project.model.Project;
import project.model.Task;
import project.model.User;

public class TaskDetailsUI {
	private User user;
	private Project project;
	private Task task;

	public TaskDetailsUI(Task task, Project project, User user) {
		this.task = task;
		this.project = project;
		this.user = user;
	}

	/**
	 * This method executes all options to execute through this UI Presents the task
	 * details and the options about this specific task Uses a switch case to treat
	 * the user's input
	 */
	public void taskDataDisplay() {
		PrintTaskInfoController taskInfo = new PrintTaskInfoController(task);
		PrintProjectInfoController projectInfo = new PrintProjectInfoController(project);

		System.out.println("");
		System.out.println("*** " + taskInfo.printTaskNameInfo().toUpperCase() + " ***");
		System.out.println("In " + projectInfo.printProjectNameInfo());
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
		System.out.println("[P] Back to " + projectInfo.printProjectNameInfo());
		System.out.println("[B] Back to my tasks");
		System.out.println("[M] MainMenu");
		System.out.println("[E] Exit");

		Scanner scannerInput = new Scanner(System.in);
		String choice = scannerInput.nextLine().toUpperCase();
		switch (choice) {
		case "1":
			US205MarkTaskAsFinishedCollaborator taskToMark = new US205MarkTaskAsFinishedCollaborator();
			taskToMark.getProjectsThatIAmCollaborator(this.user);
			taskToMark.getUnfinishedTasksOfProjectFromCollaborator(this.project.getIdCode());
			taskToMark.getTaskToBeMarkedFinished(this.task.getTaskID());
			taskToMark.markTaskAsFinished();
			System.out.println("---- SUCESS Task Marked As Finished ----");
			break;
		case "P":
			ProjectViewMenuUI previousMenu = new ProjectViewMenuUI(project, user);
			previousMenu.projectDataDisplay();
			break;
		case "B":
			UserTasksFunctionalitiesMenuUI userTasks = new UserTasksFunctionalitiesMenuUI(user);
			userTasks.displayFunctionalities();
			break;
		case "M":
			MainMenuUI.mainMenu();
			break;
		case "E":
			System.out.println("----YOU HAVE EXIT FROM APPLICATION----");
			System.exit(0);
			break;
		default:
			System.out.println("Please choose a valid option: ");
			System.out.println("");
			TaskDetailsUI myAtualUIView = new TaskDetailsUI(task, project, user);
			myAtualUIView.taskDataDisplay();
			break;
		}
	}

}
