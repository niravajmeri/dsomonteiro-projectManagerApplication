package project.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import project.model.Profile;
import project.model.User;
import project.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "project.services", "project.model", "project.controllers" })
public class US130ListUsersControllerTest {
	@Autowired
	US130ListUsersController listUsersController;

	@Autowired
	UserService userService;

	User user1, user2, newUser2, newUser3;

	@Before
	public void setUp() {
		// creates user four users
		user1 = userService.createUser("Daniel", "daniel@gmail.com", "001", "Porteiro", "920000000", "Testy Street",
				"2401-343", "Testburg", "Testo", "Testistan");
		user2 = userService.createUser("DanielM", "daniel2M@gmail.com", "002", "Code Monkey", "920000000",
				"Testy Street", "2401-343", "Testburg", "Testo", "Testistan");
		newUser2 = userService.createUser("Manel", "user2@gmail.com", "001", "Empregado", "930000000", "Testy Street",
				"2401-343", "Testburg", "Testo", "Testistan");
		newUser3 = userService.createUser("Manelinho", "user3@gmail.com", "002", "Telefonista", "940000000",
				"Testy Street", "2401-343", "Testburg", "Testo", "Testistan");
	}

	@After
	public void clear() {
		user1 = null;
		user2 = null;
		newUser2 = null;
		newUser3 = null;

	}

	/**
	 * This test confirms the ListUsersController method is working correctly, and
	 * that it returns the Users as string of data and NOT an object
	 * 
	 */
	@Test
	public void testListUsersController() {

		// creates a string matching user1's data and asserts as true
		String user1String = "001 - Unassigned: Daniel (daniel@gmail.com; 920000000) - Porteiro";
		assertTrue(user1String.equals(listUsersController.userDataToString(user1)));

		// creates Strings for all Users and adds them to testList
		List<String> testList = new ArrayList<>();
		testList.add("[1] \n" + user1String);
		testList.add("[2] \n" + listUsersController.userDataToString(user2));
		testList.add("[3] \n" + listUsersController.userDataToString(newUser2));
		testList.add("[4] \n" + listUsersController.userDataToString(newUser3));

		// finally, asserts the listUsersController returns only the three Users added
		assertEquals(testList, listUsersController.listUsersController());
		assertEquals(listUsersController.listUsersController().size(), 4);
	}

	/**
	 * Asserts the user selection method is working correctly, attempting to select
	 * the first indexed User from the list
	 * 
	 */
	@Test
	public void testListUsersController_SelectUser() {
		// calls the List Users controllers to generate a List and add it to
		listUsersController.listUsersController();

		// selects user from visible Index number 1 (corresponding to the User List's
		// user 0)
		// it must be equal to User 1
		assertEquals(listUsersController.selectUser(1), user1);

		assertEquals(listUsersController.selectUser(2), user2);

		assertEquals(listUsersController.selectUser(3), newUser2);
	}

	@Test
	public void testUserDataToString() {

		String result = "001 - Unassigned: Daniel (daniel@gmail.com; 920000000) - Porteiro";
		assertTrue(result.equals(listUsersController.userDataToString(user1)));

		user1.setUserProfile(Profile.COLLABORATOR);
		result = "001 - Collaborator: Daniel (daniel@gmail.com; 920000000) - Porteiro";
		assertTrue(result.equals(listUsersController.userDataToString(user1)));

		user1.setUserProfile(Profile.DIRECTOR);
		result = "001 - Director: Daniel (daniel@gmail.com; 920000000) - Porteiro";
		assertTrue(result.equals(listUsersController.userDataToString(user1)));
	}

}
