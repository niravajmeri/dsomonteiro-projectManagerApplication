package project.controller;

import project.model.Address;
import project.model.Company;
import project.model.User;

/**
 * This controller can provide informations about a specific user.
 * 
 * @author Group 3
 *
 */
public class PrintUserInfoController {

	/**
	 * Prints all the users in the User Repository
	 * 
	 */
	public void printAllUsersInfo() {
		for (User each : Company.getTheInstance().getUsersRepository().getAllUsersFromRepository()) {
			System.out.println(each.getName());
			System.out.println(each.getIdNumber());
			System.out.println(each.getEmail());
			System.out.println(each.getPhone());
			System.out.println(each.getFunction());
			System.out.println();
		}
	}

	/**
	 * @param emailToSearch
	 *            The string of email to search for
	 * @return This method returns a list of users with an email that contain the
	 *         same text that the user typed
	 */
	public User getUserByEmailController(String emailToSearch) {

		User user = null;
		if (Company.getTheInstance().getUsersRepository().getUserByEmail(emailToSearch) != null) {

			user = Company.getTheInstance().getUsersRepository().getUserByEmail(emailToSearch);
		}

		return user;

	}

	/**
	 * Prints all the addresses from a user
	 * 
	 */
	public void printAllAddressesFromUser(User userToSearchAddress) {
		for (Address each : userToSearchAddress.getAddressList()) {
			System.out.println(each.getStreet());
			System.out.println(each.getZipCode());
			System.out.println(each.getCity());
			System.out.println(each.getDistrict());
			System.out.println(each.getCountry());
			System.out.println();
		}
	}

	/**
	 * Prints all the active users in the User Repository
	 * 
	 */
	public void printAllActiveUsersInfo() {
		for (User each : Company.getTheInstance().getUsersRepository().getAllActiveCollaboratorsFromRepository()) {
			System.out.println("NAME " + each.getName());
			System.out.println("ID: " + each.getIdNumber());
			System.out.println("EMAIL: " + each.getEmail());
			System.out.println("PHONE: " + each.getPhone());
			System.out.println("FUNCTION: " + each.getFunction());
			System.out.println();
		}
	}
}
