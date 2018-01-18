package project.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import project.model.Company;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.User;
import project.model.taskStateInterface.Planned;
import project.model.taskStateInterface.Ready;
import project.model.taskStateInterface.TaskStateInterface;

public class ManageAssignmentRequestControllerTest {

	Company spaceX;

	String teamTesterName, teamTesterID;

	User managerTester, teamTester, teamPermanentMember;

	Project testProject;

	ProjectCollaborator teamTesterCollaborator;
	ProjectCollaborator teamPermanentCollaborator;

	Calendar estimatedStartDate;
	Calendar estimatedTaskDeadline;

	String taskDescription;
	String taskIDnumber;
	Task taskWithNoTeam;
	TaskStateInterface testingTaskState;

	ManageAssigmentRequestController assignmentRequestsController;

	@Before
	public void setUp() {
		spaceX = Company.getTheInstance();
		// creates test users for a manager and collaborator.
		// declares the collaborator's relevant data as Strings to facilitate assertions
		managerTester = new User("menager", "manager@mail.mail", "11111", "function", "123456789");

		teamTesterName = "testMan";
		teamTesterID = "22222";

		teamTester = new User(teamTesterName, "collab@mail.mail", teamTesterID, "function", "123456789");

		teamPermanentMember = new User("Mr permanent", "permie@mail.mail", "33333", "placeholding", "98644");

		spaceX.getUsersRepository().addUserToUserRepository(managerTester);
		spaceX.getUsersRepository().addUserToUserRepository(teamTester);
		spaceX.getUsersRepository().addUserToUserRepository(teamPermanentMember);

		// creates a new test project, and adds the test Collaborator to the team
		testProject = new Project(1, "testing proj", "shoot rocket... again", managerTester);
		teamTesterCollaborator = new ProjectCollaborator(teamTester, 2000);
		teamPermanentCollaborator = new ProjectCollaborator(teamPermanentMember, 2000);
		testProject.addProjectCollaboratorToProjectTeam(teamTesterCollaborator);
		testProject.addProjectCollaboratorToProjectTeam(teamPermanentCollaborator);
		spaceX.getProjectsRepository().addProjectToProjectRepository(testProject);

		// creates two estimated dates and uses them to generate a task
		// declares strings for the task's ID and description to facilitate assertion
		estimatedStartDate = Calendar.getInstance();
		estimatedStartDate.add(Calendar.DAY_OF_YEAR, -10);

		estimatedTaskDeadline = Calendar.getInstance();
		estimatedTaskDeadline.add(Calendar.DAY_OF_YEAR, 10);

		taskDescription = "dont blow up rocket";
		taskWithNoTeam = testProject.getTaskRepository().createTask(taskDescription, 2000, estimatedStartDate,
				estimatedTaskDeadline, 200000);
		taskIDnumber = taskWithNoTeam.getTaskID();

		testingTaskState = new Planned(taskWithNoTeam);
		taskWithNoTeam.setTaskState(testingTaskState);

		testProject.createTaskAssignementRequest(teamTesterCollaborator, taskWithNoTeam);
		assignmentRequestsController = new ManageAssigmentRequestController(testProject.getIdCode());

	}

	@After
	public void breakDown() {
		Company.clear();
		spaceX = null;

		managerTester = null;

		teamPermanentMember = null;

		teamTesterName = null;
		teamTesterID = null;
		teamTester = null;

		testProject = null;

		teamTesterCollaborator = null;

		estimatedStartDate = null;
		estimatedTaskDeadline = null;

		taskDescription = null;
		taskIDnumber = null;
		taskWithNoTeam = null;

		testingTaskState = null;

		assignmentRequestsController = null;
	}

	// this test asserts the showAssignmentRequests() Method returns a list of
	// requests converted to String
	@Test
	public void showAssignmentRequestsTest() {
		System.out.println("====== Testing showAssignmentRequests() Method =======");
		System.out.println();

		String expectedAssignmentRequest = teamTesterName + "\n" + "collab@mail.mail" + "\n" + taskIDnumber + "\n"
				+ "dont blow up rocket";

		assertEquals(testProject.getAssignmentRequestsList().size(), 1);
		assertEquals(assignmentRequestsController.showAllAssignmentRequests().size(), 1);

		assertTrue(assignmentRequestsController.showAllAssignmentRequests().get(0).equals(expectedAssignmentRequest));

		// asserts that an invalid project ID will return an empty List instead of a
		// Null
		int invalidProjectID = 0;
		assignmentRequestsController = new ManageAssigmentRequestController(invalidProjectID);

		assertEquals(assignmentRequestsController.showAllAssignmentRequests().size(), 0);

		System.out.println("");
	}

	// given an existing project with at least one pending request
	@Test
	public void selectAssignmentRequestsTest() {
		System.out.println("====== Testing selectAssignmentRequests() Method =======");
		System.out.println("");

		// when the index number exists, asserts the request can be selected and printed
		// onto the console
		assertTrue(assignmentRequestsController.selectAssignmentRequest(0));

		// when the index number doesn't exist, asserts the selection method returns
		// false
		assertFalse(assignmentRequestsController.selectAssignmentRequest(5));
		System.out.println("");
	}

	// given an existing project with at least one pending request, attempts to
	// approve the request
	@Test
	public void approveAssignmentRequestsTest() {
		System.out.println("====== Testing approveAssignmentRequests() Method =======");
		System.out.println("");

		// first, confirms if the requesting collaborator isn't in the team, and at
		// least one assignment request exists
		assertEquals(testProject.getAssignmentRequestsList().size(), 1);
		assertFalse(taskWithNoTeam.isProjectCollaboratorActiveInTaskTeam(teamTesterCollaborator));
		assignmentRequestsController.selectAssignmentRequest(0);

		// given the approval method, confirms that the collaborator was added to the
		// team, and the request was deleted from the list
		assertTrue(assignmentRequestsController.approveAssignmentRequest());
		assertTrue(taskWithNoTeam.isProjectCollaboratorActiveInTaskTeam(teamTesterCollaborator));
		assertEquals(testProject.getAssignmentRequestsList().size(), 0);

		// then, attempts to select a request when none should exist
		assertFalse(assignmentRequestsController.selectAssignmentRequest(0));
		System.out.println("");
	}

	// given an existing project with at least one pending request, attempts to
	// reject the request
	@Test
	public void rejectAssignmentRequestsTest() {
		System.out.println("====== Testing rejectAssignmentRequests() Method =======");
		System.out.println("");

		// first, confirms if the requesting collaborator isn't in the team, and at
		// least one assignment request exists
		assertEquals(testProject.getAssignmentRequestsList().size(), 1);
		assertFalse(taskWithNoTeam.isProjectCollaboratorActiveInTaskTeam(teamTesterCollaborator));
		assignmentRequestsController.selectAssignmentRequest(0);

		// given the rejection method, confirms that the collaborator wasn't added to
		// the
		// team, and the request was deleted from the list
		assertTrue(assignmentRequestsController.rejectAssignmentRequest());
		assertFalse(taskWithNoTeam.isProjectCollaboratorActiveInTaskTeam(teamTesterCollaborator));
		assertEquals(testProject.getAssignmentRequestsList().size(), 0);

		// then, attempts to select a request when none should exist
		assertFalse(assignmentRequestsController.selectAssignmentRequest(0));
		System.out.println("");
	}

	// This test validates the Task state update when a Collaborator is approved to
	// join a task that has no team
	@Ignore
	@Test
	public void validateTaskStateUpdatedTest() {
		System.out.println("====== Testing updateState() Method Called =======");
		System.out.println("");
		assertTrue(taskWithNoTeam.viewTaskStateName().equals("Planned"));

		assignmentRequestsController.selectAssignmentRequest(0);

		assertTrue(taskWithNoTeam.viewTaskStateName().equals("Ready"));

		System.out.println("");
	}

	// This test validates the Task state update is NOT called when a Collaborator
	// is approved to join a task that already has a team
	@Ignore
	@Test
	public void validateTaskStateNOTupdatedTest() {
		System.out.println("====== Testing updateState() Method Not Called =======");
		System.out.println("");

		taskWithNoTeam.addProjectCollaboratorToTask(teamPermanentCollaborator);

		testingTaskState = new Ready(taskWithNoTeam);

		assertTrue(taskWithNoTeam.viewTaskStateName().equals("Ready"));

		assignmentRequestsController.selectAssignmentRequest(0);

		assertTrue(taskWithNoTeam.viewTaskStateName().equals("Ready"));
		assertEquals(taskWithNoTeam.getTaskTeam().size(), 2);

		System.out.println("");
	}
}
