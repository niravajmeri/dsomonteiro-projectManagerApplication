package project.ui.console.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.controllers.US110andUS112SetUserProfileController;
import project.controllers.US115andUS116SetUserStateController;
import project.model.User;

import java.util.Scanner;

@Component
public class US110andUS112SetUserProfileUI {

	@Autowired
	private US110andUS112SetUserProfileController controllerA;
	@Autowired
	private US115andUS116SetUserStateController controllerB;

	public void changeUserProfile(User user) {
		controllerB.setToChangeState(user);
		Scanner input = new Scanner(System.in);
		System.out.println(user.getIdNumber() + " - " + controllerB.userStateAsString() + ": " + user.getName() + " - "
				+ controllerA.userProfileAsString(user));
		switch (user.getUserProfile()) {
		case COLLABORATOR:
            setUserAsCollaborator(user, input);
            break;
            case DIRECTOR:
                setUserAsDirector(user, input);
                break;
            default:
                defaultCase(user, input);
                break;
        }
    }

    private void setUserAsDirector(User user, Scanner input) {
        System.out.println("Proceeding will set this user profile as Director. Continue?");
        System.out.println("Press [Y] to confirm.");
        String option = input.nextLine();
        if ("Y".equalsIgnoreCase(option)) {
            controllerA.setUserAsDirector(user);
            System.out.println("User profile successfully set as Director.");
        } else {
            System.out.println("User profile wasn't changed.");
        }
    }

    private void setUserAsCollaborator(User user, Scanner input) {
        System.out.println("Proceeding will set this user profile as Collaborator. Continue?");
        System.out.println("Press [Y] to confirm.");
        String option = input.nextLine();
        if ("Y".equalsIgnoreCase(option)) {
            controllerA.setUserAsCollaborator(user);
            System.out.println("User profile successfully set as Collaborator.");
        } else {
            System.out.println("User profile wasn't changed.");
        }
    }

    private void defaultCase(User user, Scanner input) {
        System.out.println("Please select which profile to give to the user:");
        System.out.println("[D] - Director");
        System.out.println("[C] - Collaborator");
        String option = input.nextLine();
        if ("D".equalsIgnoreCase(option)) {
            controllerA.setUserAsDirector(user);
            System.out.println("User profile successfully set as Director.");
        } else if ("C".equalsIgnoreCase(option)) {
            controllerA.setUserAsCollaborator(user);
            System.out.println("User profile successfully set as Collaborator.");
        } else {
            System.out.println("Invalid input.");
        }
    }
}
