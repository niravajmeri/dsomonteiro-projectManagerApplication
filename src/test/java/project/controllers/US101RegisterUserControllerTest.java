package project.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import project.dto.UserDTO;
import project.model.User;
import project.services.UserService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "project.services", "project.model", "project.controllers" })
public class US101RegisterUserControllerTest {

	@Autowired
	US101RegisterUserController testUserRegistrationController;

	@Autowired
	UserService userService;

	User user1;
	User user2;
	User user3;

	@Before
	public void setUp() {

		// create user
		user1 = userService.createUser("Daniel", "daniel@gmail.com", "001", "Porteiro",
				"920000000", "Testy Street", "2401-343", "Testburg", "Testo", "Testistan");

		UserDTO newUserDTO = testUserRegistrationController.createUserDTO("João", "joão.gmail.com",
				"034", "Testes", "919876787", "1", "a");
		newUserDTO.setPassword("Password");

		newUserDTO = testUserRegistrationController.setAddress(newUserDTO, "Street",
				"2401-343", "Testburg", "Testo", "Testistan");

		testUserRegistrationController.addNewUserToDbFromDTO(newUserDTO);
	}

	@After
	public void clear() {

		user1 = null;
		user2 = null;
		user3 = null;
	}

	/**
	 * Given a visitor, this test attempts the creation of a User Registration
	 * controllers
	 */

	@Test
	public void testUserRegistrationController() {
		// creates the controllers and asserts the list of users starts at 0
		assertEquals(userService.getAllUsersFromUserContainer().size(), 2);

		// Checks if the user1 is in the UserContainer
		assertEquals(testUserRegistrationController.isUserInUserRepository("daniel@gmail.com"), true);

		// uses the controllers to both create and add the user
		UserDTO anotherDTO = testUserRegistrationController.createUserDTO("Fabio", "fabio@gmail.com",
				"003", "worker", "919997775",  "1", "a");
		anotherDTO.setPassword("Password");
		anotherDTO = testUserRegistrationController.setAddress(anotherDTO, "Tasty streets", "4450-150",
				"Hellcity", "HellsBurg", "HellMam");
		testUserRegistrationController.addNewUserToDbFromDTO(anotherDTO);
		assertEquals(3, userService.getAllUsersFromUserContainer().size());
		assertTrue(userService.getAllUsersFromUserContainer().get(0).equals(user1));

		// verifies if the createUserDTO method returns null when user email already exists
		UserDTO dtoingAgain = testUserRegistrationController.createUserDTO("Daniel", "danielq@gmail.com",
				"001", "Porteiro", "920000000",
				 "1", "a");
		dtoingAgain.setPassword("Password");
		dtoingAgain = testUserRegistrationController.setAddress(dtoingAgain, "Testy Street", "2401-343",
				"Testburg", "Testo", "Testistan");
		testUserRegistrationController.addNewUserToDbFromDTO(dtoingAgain);

		UserDTO sweetDTO = testUserRegistrationController.createUserDTO("Daniel", "danicom", "001",
				"Porteiro", "920000000",  "1", "a");
		sweetDTO.setPassword("Password");
		sweetDTO = testUserRegistrationController.setAddress(sweetDTO, "Testy Street", "2401-343",
				"Testburg", "Testo", "Testistan");
		testUserRegistrationController.addNewUserToDbFromDTO(sweetDTO);
		user2 = userService.getAllUsersFromUserContainer().get(0);
		user3 = userService.getAllUsersFromUserContainer().get(1);

		// verifies if the createUserDTO method returns true when the user email already
		// exists in the repository
		assertEquals(testUserRegistrationController.isUserInUserRepository(user1.getEmail()), true);
		// verifies if the createUserDTO method returns false when user email is invalid
		assertEquals(testUserRegistrationController.isUserEmailValid(user3.getEmail()), false);
		// verifies if the createUserDTO method returns false when user email is valid
		assertEquals(testUserRegistrationController.isUserEmailValid(user1.getEmail()), true);

		assertFalse(user1.hasPassword());

	}

	@Test
	public void wasUserAddedTest() {

		UserDTO keepDTOing = testUserRegistrationController.createUserDTO("Daniel", "danicom", "001",
				"Porteiro", "920000000",  "1", "a");
		keepDTOing.setPassword("Password");
		keepDTOing = testUserRegistrationController.setAddress(keepDTOing, "Testy Street", "2401-343",
				"Testburg", "Testo", "Testistan");
		testUserRegistrationController.addNewUserToDbFromDTO(keepDTOing);

		assertTrue(testUserRegistrationController.wasUserAdded(true));

		// user 3 was created, added and email was set as invalid

		UserDTO sickOfTheseDTOs = testUserRegistrationController.createUserDTO("João", "joão@gmail.com",
				"034", "Testes", "919876787", "1", "a");
		sickOfTheseDTOs.setPassword("Password");
		sickOfTheseDTOs = testUserRegistrationController.setAddress(sickOfTheseDTOs, "Street", "2401-343",
				"Testburg", "Testo", "Testistan");
		testUserRegistrationController.addNewUserToDbFromDTO(sickOfTheseDTOs);

		assertTrue(testUserRegistrationController.wasUserAdded(true));

	}

	@Test
	public void isUserEmailValidRegistrationController() {

		// Creates a valid email
		String email = new String("validEmail@gmail.com");

		// creates the controllers and asserts the list of users starts at 0

		assertTrue(testUserRegistrationController.isEmailValidController(email));

		// Changes string value to invalid email
		email = "invalid";
		assertFalse(testUserRegistrationController.isEmailValidController(email));

	}

	@Test(expected=NullPointerException.class)
	public void sendVerificationCode() {
		assertTrue(testUserRegistrationController.doesCodeGeneratedMatch(null, "daniel@gmail.com"));
	}


}