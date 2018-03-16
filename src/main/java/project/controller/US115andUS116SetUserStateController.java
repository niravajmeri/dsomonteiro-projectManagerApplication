package project.controller;

import project.model.Company;
import project.model.User;

public class US115andUS116SetUserStateController {

	Company myCompany = Company.getTheInstance();
	User toChangeState;

	/**
	 * This controller recieves a user whose state is to be changed
	 * 
	 * 
	 * @param the
	 *            selected User
	 */
	public US115andUS116SetUserStateController(User toChange) {
		this.toChangeState = toChange;
	}

	/**
	 * 
	 * This method is called only after the controller is created, and changes the
	 * user's state
	 * 
	 * 
	 */
	public void changeUserState() {
		toChangeState.changeUserState();
		myCompany.getUsersContainer().addUserToUserRepositoryX(toChangeState);
	}

	/**
	 * This is a simple utility method to convert the User's state into a String to
	 * be displayed in the UI
	 * 
	 * @return the String with the User's state
	 */
	public String userStateAsString() {
		String output;

		if (toChangeState.isSystemUserStateActive()) {
			output = "(ACTIVE)";
		} else {
			output = "(DISABLED)";
		}

		return output;
	}
}
