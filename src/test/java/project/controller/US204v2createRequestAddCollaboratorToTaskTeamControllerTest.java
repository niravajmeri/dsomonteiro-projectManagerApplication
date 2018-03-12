package project.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import project.model.*;

import static org.junit.Assert.assertEquals;

public class US204v2createRequestAddCollaboratorToTaskTeamControllerTest {

	Company myComp;
	ProjectContainer projRepo;
	UserContainer userRepo;
	TaskContainer taskRepo;
	Project proj;
	Task taskA;
	Task taskB;
	Task taskC;
	User user;
	US204v2createRequestAddCollaboratorToTaskTeamController controller;

	@Before
	public void setUp() {
		// Initialize company
		myComp = Company.getTheInstance();

		// Initialize Project Repository
		projRepo = myComp.getProjectsContainer();

		// Initialize User Repository
		userRepo = myComp.getUsersContainer();

		// Add user to User Repository
		userRepo.createUser("Fek Quin", "ugandan@nackls.com", "cluck1337", "Follower of da wae", "919898997",
				"Debil Strit", "SP1T-0N-H1M", "NacklsCiti", "QuinLend", "UGANDA");
		user = userRepo.getUserByEmail("ugandan@nackls.com");

		// Add a project to the project repository
		projRepo.addProjectToProjectContainer(
				projRepo.createProject("Best project", "Fainding da quin an spitting on de non-beleevahs!", user));
		proj = projRepo.getAllProjectsfromProjectsContainer().get(0);

		// Initialize Task Repository
		taskRepo = proj.getTaskRepository();

		// Create and add tasks to Task Repository
		taskA = new Task(1, 1, "Faind fek quin!");
		taskB = new Task(2, 1, "Spit on non-beleevahs!");
		taskC = new Task(3, 1, "Follou da wae!");
		taskRepo.addTaskToProject(taskA);
		taskRepo.addTaskToProject(taskB);
		taskRepo.addTaskToProject(taskC);

		// Create controller to be used
		controller = new US204v2createRequestAddCollaboratorToTaskTeamController("1.1", user);

	}

	@After
	public void tearDown() {
		Company.clear();
		projRepo = null;
		userRepo = null;
		taskRepo = null;
		proj = null;
		taskA = null;
		taskB = null;
		taskC = null;
		user = null;
		controller = null;
	}

	@Test
	public final void testCreateTaskTeamRequest() {
		assertEquals(0, proj.getPendingTaskAssignementRequests().size());
		controller.createTaskTeamRequest();
		assertEquals(1, proj.getPendingTaskAssignementRequests().size());
	}

	@Test
	public final void testSetProjectIDFromTaskID() {
		controller.setProjectIDFromTaskID("1.1");
		int projID = (int) controller.getProjectID();
		assertEquals(1, projID);
	}

	@Test
	public final void testSetProjectID() {
		controller.setProjectID(5);
		int projID = (int) controller.getProjectID();
		assertEquals(5, projID);
	}

	@Test
	public final void testGetTaskByTaskID() {
		assertEquals(taskA, controller.getTaskByTaskID("1.1"));
	}

}
