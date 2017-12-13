package test.usTest.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.project.model.Company;
import main.project.model.Profile;
import main.project.model.User;

class US130Tests {

	/**
	 * Tests US130
	 * 
	 * US130: Como Administrador, pretendo listar todos os utilizadores do sistema.
	 */

	Company myCompany;
	User user1;
	User user2;
	User user3;
	User user4;
	User user5;

	@BeforeEach
	void clearCompany() {

		myCompany = Company.getTheInstance();

		user1 = myCompany.getUsersRepository().createUser("Dani", "daniel@gmail.com", "001", "Programador", "910000000",
				"ruinha", "7040-531", "Bucareste", "Porto", "Portugal");
		user2 = myCompany.getUsersRepository().createUser("Rita", "rita@gmail.com", "002", "Gestora de Projeto",
				"920000000", "ruinha", "7040-531", "Bucareste", "Porto", "Portugal");
		user3 = myCompany.getUsersRepository().createUser("Joao", "joao@gmail.com", "003", "Programador", "910000000",
				"ruinha", "7040-531", "Bucareste", "Porto", "Portugal");
		user4 = myCompany.getUsersRepository().createUser("Maria", "maria@gmail.com", "004", "Gestora de Projeto",
				"920000000", "ruinha", "7040-531", "Bucareste", "Porto", "Portugal");
		user5 = myCompany.getUsersRepository().createUser("Manel", "manel@gmail.com", "005", "Programador", "910000000",
				"ruinha", "7040-531", "Bucareste", "Porto", "Portugal");

		// Adds the created users to the Company user list.
		myCompany.getUsersRepository().addUserToUserRepository(user1);
		myCompany.getUsersRepository().addUserToUserRepository(user2);
		myCompany.getUsersRepository().addUserToUserRepository(user3);
		myCompany.getUsersRepository().addUserToUserRepository(user4);
		myCompany.getUsersRepository().addUserToUserRepository(user5);

		// Set users's profile type to collaborator
		user1.setUserProfile(Profile.COLLABORATOR);
		user2.setUserProfile(Profile.COLLABORATOR);
		user3.setUserProfile(Profile.COLLABORATOR);
		user4.setUserProfile(Profile.COLLABORATOR);
		user5.setUserProfile(Profile.COLLABORATOR);
	}

	@AfterEach
	void tearDown() {
		myCompany.clear();
		user1 = null;
		user2 = null;
		user3 = null;
		user4 = null;
		user5 = null;
	}

	@Test
	void US130test() {
		// Create the list of all users in the system
		List<User> result = myCompany.getUsersRepository().getAllUsersFromRepository();

		// Create list of users to compare and add users to it
		List<User> listOfusers = new ArrayList<User>();

		listOfusers.add(user1);
		listOfusers.add(user2);
		listOfusers.add(user3);
		listOfusers.add(user4);
		listOfusers.add(user5);

		assertEquals(listOfusers, result);
	}

}
