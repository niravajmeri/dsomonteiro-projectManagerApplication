package project.ui.console.uiProjectManager.manageTeam;

import java.util.Scanner;

import project.controller.PrintUserInfoController;
import project.controller.US351AddColaboratorToProjectTeamController;
import project.model.Project;
import project.model.User;

public class US351AddCollaboratorToProjectTeamUI {

	User user;
	int costPerEffort;

	public void addCollaboratorToProjectTeam(Project project) {
		String line = "______________________________________________";
		US351AddColaboratorToProjectTeamController controller = new US351AddColaboratorToProjectTeamController();
		PrintUserInfoController userInfoPrinter = new PrintUserInfoController();
		Scanner dataIn = new Scanner(System.in);
		boolean invalidInputA = true;
		boolean invalidInputB = true;
		boolean invalidInputC = false;

		System.out.println(line);
		System.out.println("USER LIST");
		System.out.println(line);
		userInfoPrinter.printAllActiveUsersInfo();
		System.out.println(line);
		System.out.println("");

		while (invalidInputA) {
			System.out.println("Please choose a user (input its ID:");
			System.out.println("Inputing [E] will exit this menu.");
			System.out.println(line);
			String userID = dataIn.nextLine();
			if (userID.equals("E")) {
				System.out.println("Assignment of user to project cancelled.");
				invalidInputA = false;
				invalidInputB = false;
				invalidInputC = true;
			} else {
				user = controller.searchUserByID(userID);
				if (user != null) {
					System.out.println("\nUser selected: " + userID +"\n");
					invalidInputA = false;
				} else {
					System.out.println("Invalid ID provided. Please try again!");
				}

			}
		}

		while (invalidInputB) {
			System.out.println("Please input the cost per effort for the user selected:");
			System.out.println("Inputing [-1] will exit this menu.\n");
			costPerEffort = dataIn.nextInt();
			dataIn.nextLine();
			if (costPerEffort != -1) {
				System.out.println("Cost per effort: " + costPerEffort);
				invalidInputB = false;
			} else {
				System.out.println("Assignment of user to project cancelled.");
				invalidInputB = false;
				invalidInputC = true;
			}
		}

		if (!invalidInputC)
			System.out.println("Please input [Y] to confirm the addition of the user to the project team:");
		System.out.println("Inputing anything else will cancel the addition.\n");

		String confirm = dataIn.nextLine();
		if ("Y".equalsIgnoreCase(confirm)) {
			controller.addUserToProjectTeam(user, project, costPerEffort);
			System.out.println("\nUser successfully added to the project.");
			invalidInputC = true;
		} else {
			System.out.println("\nAssignment of user to project cancelled.");
			invalidInputC = true;
		}
	}
}
