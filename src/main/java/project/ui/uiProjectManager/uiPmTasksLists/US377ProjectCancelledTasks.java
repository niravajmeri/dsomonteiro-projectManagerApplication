package project.ui.uiProjectManager.uiPmTasksLists;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project.controller.PrintProjectInfoController;
import project.controller.US377CollectionOfCancelledTasksFromAProject;
import project.model.Project;
import project.model.User;
import project.ui.MainMenuUI;
import project.ui.uiProjectManager.ProjectManagerMainMenuUI;

public class US377ProjectCancelledTasks {

	private Project project;
	private User user;

	public void displayCancelledTasksOfProject(Project project, User user) {

		this.project = project;
		this.user = user;

		Scanner scannerInput = new Scanner(System.in);

		PrintProjectInfoController projectInfo = new PrintProjectInfoController(this.project);

		System.out.println("");
		System.out.println("PROJECT " + projectInfo.printProjectNameInfo().toUpperCase());
		System.out.println("___________________________________________________");
		System.out.println("ID: " + projectInfo.printProjectIDCodeInfo());
		System.out.println("STATUS: " + projectInfo.printProjectStatusInfo());
		System.out.println("DESCRIPTION: " + projectInfo.printProjectDescriptionInfo());
		System.out.println("START DATE: " + projectInfo.printProjectStartDateInfo());
		System.out.println("PROJECT MANAGER: " + projectInfo.printProjectManagerInfo());
		System.out.println("PROJECT TEAM: " + projectInfo.printProjectTeamInfo());
		System.out.println("PROJECT BUDGET: " + projectInfo.printProjectBudgetInfo());
		System.out.println("");
		System.out.println("___________________________________________________");
		System.out.println("     CANCELLED TASKS");
		System.out.println("___________________________________________________");

		US377CollectionOfCancelledTasksFromAProject controller = new US377CollectionOfCancelledTasksFromAProject(
				project);

		List<String> listOfCancelledTasks = new ArrayList<>();

		for (int i = 0; i < controller.getCancelledTasksFromAProject().size(); i++) {
			String taskInfo = controller.getCancelledTaskListId(project).get(i);
			System.out.println(taskInfo);
			listOfCancelledTasks.add(controller.splitStringByFirstSpace(taskInfo));
		}

		System.out.println("___________________________________________________");
		System.out.println("[B] Back");
		System.out.println("[M] MainMenu");
		System.out.println("[E] Exit \n");

		String option = scannerInput.nextLine().toUpperCase();

		// creation of a list with the options B,E and M
		List<String> listOfOptionsToCompare = new ArrayList<>();
		listOfOptionsToCompare.add("B");
		listOfOptionsToCompare.add("M");
		listOfOptionsToCompare.add("E");

		for (String ii : listOfCancelledTasks) {

			if (option.equals(ii)) {
				// TODO
			} else if (option.equals("B")) {
				ProjectManagerMainMenuUI projectManagerMainMenuUI = new ProjectManagerMainMenuUI(this.user,
						this.project);
				projectManagerMainMenuUI.displayOptions();

			} else if (option.equals("M")) {
				MainMenuUI.mainMenu();
			} else if (option.equals("E")) {
				System.exit(0);
			}
			listOfOptionsToCompare.add(ii);
		}

		// In case the user input is an invalid option, the console shows a message and
		// returns to the beginning of this same menu
		if (!(listOfOptionsToCompare.contains(option))) {
			System.out.println("Please choose a valid option: ");
			this.displayCancelledTasksOfProject(project, user);
		}
	}

}
