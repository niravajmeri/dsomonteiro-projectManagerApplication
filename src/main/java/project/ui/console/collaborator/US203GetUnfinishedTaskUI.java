package project.ui.console.collaborator;

import project.controller.US203GetUserStartedNotFinishedTaskListInIncreasingOrderController;
import project.controller.UpdateDbToContainersController;
import project.model.User;
import project.ui.console.MainMenuUI;

import java.util.Scanner;

public class US203GetUnfinishedTaskUI {
	private Boolean isPreviousUIFromTasks;

	public void displayOptions(User user1) {
		UpdateDbToContainersController infoUpdater = new UpdateDbToContainersController();
		infoUpdater.updateDBtoContainer();
		int projID;
		String[] split;
		User user = user1;
		US203GetUserStartedNotFinishedTaskListInIncreasingOrderController unfinishedTaskByUser = new US203GetUserStartedNotFinishedTaskListInIncreasingOrderController();
		int t;
		t = 0;
		Scanner scannerInput = new Scanner(System.in);

		String myname = user.getName();
		String function = user.getFunction().toUpperCase();

		System.out.println("\n" + myname + " \n" + function);
		System.out.println("___________________________________________________");

		for (int i = 0; i < unfinishedTaskByUser.getUserStartedNotFinishedTaskListInIncreasingOrder(user).size(); i++) {
			t = t + 1;
			System.out.println("["
					+ unfinishedTaskByUser.getUserStartedNotFinishedTaskListInIncreasingOrder(user).get(i).getTaskID() + "]" + " "
					+ unfinishedTaskByUser.getUserStartedNotFinishedTaskListInIncreasingOrder(user).get(i).getDescription());
		}
		System.out.println("___________________________________________________");
		System.out.println("[B] Back");
		System.out.println("[M] MainMenu");
		System.out.println("[E] Exit \n");

		String option = scannerInput.nextLine().toUpperCase();

		switch (option) {

		case "B":
			UserTasksFunctionalitiesMenuUI previousMenu = new UserTasksFunctionalitiesMenuUI(user);
			previousMenu.displayFunctionalities();
			break;
		case "M":
			MainMenuUI.mainMenu();
			break;
		case "E":
			break;
		default:


			try {
				split = option.split("\\.");
				projID = Integer.valueOf(split[0]);

				TaskDetailsUI taskSelected = new TaskDetailsUI(option, projID, user, this.isPreviousUIFromTasks);
				taskSelected.taskDataDisplay();
			}

			catch (NullPointerException npe) {
				System.out.println("Please choose a valid option: ");
				System.out.println("");
				US203GetUnfinishedTaskUI unfinishedTaskByUser1 = new US203GetUnfinishedTaskUI();
				unfinishedTaskByUser1.displayOptions(user);
			}

			break;
		}
	}
}
