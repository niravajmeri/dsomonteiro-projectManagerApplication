package project.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import project.model.Company;
import project.model.Profile;
import project.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class US110andUS112SetUserProfileControllerTests {

	Company Blip;
	User newUser2, newUser3;
	int typeOfUser;

	@Before
	public void setUp() {

		Blip = Company.getTheInstance();

		newUser2 = Blip.getUsersContainer().createUser("Manel", "user2@gmail.com", "001", "Empregado", "930000000",
				"Testy Street", "2401-343", "Testburg", "Testo", "Testistan");
		newUser3 = Blip.getUsersContainer().createUser("Manelinho", "user3@gmail.com", "002", "Telefonista",
				"940000000", "Testy Street", "2401-343", "Testburg", "Testo", "Testistan");

		Blip.getUsersContainer().addUserToUserRepository(newUser2);
	}

	@After
	public void breakDown() {
		Company.clear();
		newUser2 = null;
		newUser3 = null;

	}

	@Test
	public void testSetUserAsDirectorController() {
		// given a User - newUser2 - asserts they start as Visitor when created
		// and then creates the controller
		assertEquals(newUser2.getUserProfile(), Profile.UNASSIGNED);
		US110andUS112SetUserProfileController testProfileController = new US110andUS112SetUserProfileController();

		// sets newUser2 as Director and asserts its profile
		testProfileController.setUserAsDirector(newUser2);
		assertEquals(newUser2.getUserProfile(), Profile.DIRECTOR);

		// finally, asserts that no other Users were changed
		// and that user repository contains the new director
		assertTrue(Blip.getUsersContainer().searchUsersByProfile(Profile.DIRECTOR).contains(newUser2));
		assertEquals(newUser3.getUserProfile(), Profile.UNASSIGNED);
	}

	@Test
	public void testSetUserAsCollaboratorController() {
		// given a User - newUser2 - asserts they start as Visitor when created
		// and then creates the controller
		assertEquals(newUser2.getUserProfile(), Profile.UNASSIGNED);
		US110andUS112SetUserProfileController testProfileController = new US110andUS112SetUserProfileController();

		// sets newUser2 as Collaborator and asserts its profile
		testProfileController.setUserAsCollaborator(newUser2);
		assertEquals(newUser2.getUserProfile(), Profile.COLLABORATOR);

		// finally, asserts that no other Users were changed
		// and that user repository contains the new director
		assertTrue(Blip.getUsersContainer().searchUsersByProfile(Profile.COLLABORATOR).contains(newUser2));
		assertEquals(newUser3.getUserProfile(), Profile.UNASSIGNED);
	}

	@Test
	public void testUserProfileAsString() {
		US110andUS112SetUserProfileController testProfileController = new US110andUS112SetUserProfileController();
		assertTrue("Unassigned".equals(testProfileController.userProfileAsString(newUser2)));
		testProfileController.setUserAsCollaborator(newUser2);
		assertTrue("Collaborator".equals(testProfileController.userProfileAsString(newUser2)));
		testProfileController.setUserAsDirector(newUser2);
		assertTrue("Director".equals(testProfileController.userProfileAsString(newUser2)));
	}

}
