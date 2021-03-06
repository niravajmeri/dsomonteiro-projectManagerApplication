package project.ui.console.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.controllers.US110andUS112SetUserProfileController;
import project.controllers.US115andUS116SetUserStateController;
import project.model.User;

import java.util.Scanner;

@Component
public class US115andUS116SetUserStateUI {

	@Autowired
	private US110andUS112SetUserProfileController controllerB;
	@Autowired
	private US115andUS116SetUserStateController controllerA;

	public void changeUserState(User user) {
		controllerA.setToChangeState(user);
		System.out.println(user.getIdNumber() + " - " + controllerA.userStateAsString() + ": " + user.getName() + " - "
				+ controllerB.userProfileAsString(user));

		Scanner input = new Scanner(System.in);
		System.out.println("Proceeding will change this user's state. Are you sure you want to continue?");
		System.out.println("Press [Y] to confirm.");
		String choice = input.nextLine();

		if ("Y".equalsIgnoreCase(choice)) {
			controllerA.changeUserState();
			System.out.println("User state was changed successfully.");
		} else {
			System.out.println("User state was not changed.");
		}
		System.out.println("Returning to admin menu...");
		System.out.println("");
	}

}
