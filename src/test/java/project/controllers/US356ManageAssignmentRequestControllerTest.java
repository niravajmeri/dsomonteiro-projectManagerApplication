package project.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import project.model.*;
import project.model.taskstateinterface.Planned;
import project.model.taskstateinterface.StandBy;
import project.model.taskstateinterface.TaskStateInterface;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import java.util.Calendar;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "project.services", "project.model", "project.controllers" })
public class US356ManageAssignmentRequestControllerTest {
	@Autowired
	UserService userContainer;
	@Autowired
	ProjectService projectContainer;
	@Autowired
	TaskService taskContainer;

	String teamTesterName, teamTesterID;

	User managerTester, teamTester, teamPermanentMember;

	Project testProject;

	ProjectCollaborator teamTesterCollaborator;
	ProjectCollaborator teamPermanentCollaborator;

	Calendar estimatedStartDate;
	Calendar estimatedTaskDeadline;

	String taskDescription;
	String taskIDnumber;
	Task taskWithNoTeam, standByTask;
	TaskStateInterface testingTaskState, testingTaskStateB;
	@Autowired
	US356ManageAssigmentRequestController assignmentRequestsController;

	@Before
	public void setUp() {
		
		
		// creates test users for a manager and collaborator.
		// declares the collaborator's relevant data as Strings to facilitate assertions
		managerTester = new User("menager", "manager@mail.mail", "11111", "function", "123456789");

		teamTesterName = "testMan";
		teamTesterID = "22222";

		teamTester = new User(teamTesterName, "collab@mail.mail", teamTesterID, "function", "123456789");

		teamPermanentMember = new User("Mr permanent", "permie@mail.mail", "33333", "placeholding", "98644");

		userContainer.addUserToUserRepositoryX(managerTester);
		userContainer.addUserToUserRepositoryX(teamTester);
		userContainer.addUserToUserRepositoryX(teamPermanentMember);

		// creates a new test project, and adds the test Collaborator to the team
		testProject = new Project("testing proj", "shoot rocket... again", managerTester);
		projectContainer.addProjectToProjectContainer(testProject);
		teamTesterCollaborator = testProject.createProjectCollaborator(teamTester, 2000);
		teamPermanentCollaborator = testProject.createProjectCollaborator(teamPermanentMember, 2000);
		projectContainer.addProjectCollaborator(teamTesterCollaborator);
		projectContainer.addProjectCollaborator(teamPermanentCollaborator);

		// creates two estimated dates and uses them to generate a task
		// declares strings for the task's ID and description to facilitate assertion
		estimatedStartDate = Calendar.getInstance();
		estimatedStartDate.add(Calendar.DAY_OF_YEAR, -10);

		estimatedTaskDeadline = Calendar.getInstance();
		estimatedTaskDeadline.add(Calendar.DAY_OF_YEAR, 10);

		taskDescription = "dont blow up rocket";
		taskWithNoTeam = taskContainer.createTask("dont blow up rocket", testProject);
		taskWithNoTeam.setEstimatedTaskEffort(2000);
		taskWithNoTeam.setEstimatedTaskStartDate(estimatedStartDate);
		taskWithNoTeam.setTaskDeadline(estimatedTaskDeadline);
		taskWithNoTeam.setTaskBudget(200000);
		taskIDnumber = taskWithNoTeam.getTaskID();


		taskWithNoTeam.setTaskState(new Planned());

//		testProject.createTaskAssignmentRequest(teamTesterCollaborator, taskWithNoTeam);
		taskWithNoTeam.createTaskAssignmentRequest(teamTesterCollaborator);
//		assignmentRequestsController = new US356ManageAssigmentRequestController(testProject);

		String taskDescriptionB = "do blow up rocket!";
		standByTask = taskContainer.createTask("do blow up rocket!", testProject);
		standByTask.setEstimatedTaskEffort(2000);
		standByTask.setEstimatedTaskStartDate(estimatedStartDate);
		standByTask.setTaskDeadline(estimatedTaskDeadline);
		standByTask.setTaskBudget(200000);


		standByTask.setTaskState(new StandBy());

//		testProject.createTaskAssignmentRequest(teamTesterCollaborator, standByTask);
		standByTask.createTaskAssignmentRequest(teamTesterCollaborator);
	}


	// this test asserts the showAssignmentRequests() Method returns a list of
	// requests converted to String
	@Test
	public void showAssignmentRequestsTest() {

		String expectedAssignmentRequest = teamTesterName + "\n" + "collab@mail.mail" + "\n" + taskIDnumber + "\n"
				+ "dont blow up rocket";

//		assertEquals(testProject.getPendingTaskAssignmentRequests().size(), 2);
		assertEquals(2, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
		assertEquals(2, taskContainer.viewAllProjectTaskAssignmentRequests(testProject).size());

		assertEquals(2, assignmentRequestsController.showAllAssignmentRequests(testProject).size());

		assertTrue(assignmentRequestsController.showAllAssignmentRequests(testProject).get(0).equals(expectedAssignmentRequest));

	}

	/**
	 * Tests the approveAssignmentRequest
	 */
	@Test
	public void acceptAssignmentRequestTest() {

		// first, confirms that there is only one assignment request in the list
		assertEquals(2, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
		
		// Sets the controllers request
		assignmentRequestsController.setSelectedAdditionRequest(0, testProject);
		assertTrue(taskContainer.getAllProjectTaskAssignmentRequests(testProject).get(0).getApprovalDate()==null);

		// accepts the requests
		assignmentRequestsController.approveAssignmentRequest();

		// first, confirms that the list still the same
		assertEquals(2, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
		assertTrue(taskContainer.getAllProjectTaskAssignmentRequests(testProject).get(0).getApprovalDate()!=null);


		// Now we test the same for a StandBy Task
		// Sets the controllers request
		assignmentRequestsController.setSelectedAdditionRequest(1, testProject);
		assertTrue(taskContainer.getAllProjectTaskAssignmentRequests(testProject).get(1).getApprovalDate()==null);

		// accepts the requests
		assignmentRequestsController.approveAssignmentRequest();

		// first, confirms that the list still the same
		assertEquals(2, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
		assertTrue(taskContainer.getAllProjectTaskAssignmentRequests(testProject).get(1).getApprovalDate()!=null);


	}
	
	/**
	 * Tests the reject assignment request
	 */
	@Test
	public void rejectAssignmentRequestsTest() {

		// first, confirms if the requesting collaborator isn't in the team, and at
		// least one assignment request exists
		assertEquals(2, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
		assertFalse(taskWithNoTeam.isProjectCollaboratorActiveInTaskTeam(teamTesterCollaborator));
		assignmentRequestsController.setSelectedAdditionRequest(0, testProject);
		assertTrue(taskContainer.getAllProjectTaskAssignmentRequests(testProject).get(0).getRejectDate()==null);
		;

		// given the rejection method, confirms that the collaborator wasn't added to
		// the
		// team, and the request was deleted from the list
		assertTrue(assignmentRequestsController.rejectAssignmentRequest());
		assertTrue(taskContainer.getAllProjectTaskAssignmentRequests(testProject).get(0).getRejectDate()!=null);
		assertFalse(taskWithNoTeam.isProjectCollaboratorActiveInTaskTeam(teamTesterCollaborator));
		assertEquals(2, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());


	}

//	/**
//	 * Tests the approveAssignmentRequest with a null object
//	 */
//	@Test
//	public void acceptNullRequest() {
//
//		// Sets the controllers request as null
////		testProject.getPendingTaskAssignmentRequests().add(null);
////		taskWithNoTeam.getPendingTaskAssignmentRequests().add(null);
//		assignmentRequestsController.setSelectedAdditionRequest(0, testProject);
//		assignmentRequestsController.rejectAssignmentRequest();
//		assignmentRequestsController.setSelectedAdditionRequest(0, testProject);
//		assignmentRequestsController.rejectAssignmentRequest();
//		
//		assertEquals(0, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
//		
//		taskContainer.getAllProjectTaskAssignmentRequests(testProject).add(null);
//		assertEquals(0, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
//		
//		assertEquals(0, taskWithNoTeam.viewPendingTaskAssignmentRequests().size());
////		taskWithNoTeam.viewPendingTaskAssignmentRequests().add(null);
//
//		taskWithNoTeam.getPendingTaskAssignmentRequests().add(null);
//		
////		taskWithNoTeam.getPendingTaskTeamRequests().add(null);
////		assertEquals(1, taskWithNoTeam.viewPendingTaskAssignmentRequests().size());
//		assertEquals(1, taskWithNoTeam.getPendingTaskAssignmentRequests().size());
////		assertEquals(1, taskWithNoTeam.getPendingTaskTeamRequests().size());
////		assertEquals(1, taskContainer.getAllProjectTaskAssignmentRequests(testProject).size());
//		assertEquals(1, taskContainer.viewAllProjectTaskAssignmentRequests(testProject).size());
//
//		
//		
//		// Now we test the same for a StandBy Task
//		// Sets the controllers request
////		assignmentRequestsController.setSelectedAdditionRequest(0, testProject);
//		
//
//		// Tries to approve and fails
//		assertFalse(assignmentRequestsController.approveAssignmentRequest());
//
//	}
//
//	/**
//	 * Tests the rejectAssignmentRequest with a null object
//	 */
//	@Test
//	public void rejectNullRequest() {
//
//		// Sets the controllers request as null
//		testProject.getPendingTaskAssignmentRequests().add(null);
//
//		// Tries to reject and fails
//		assertFalse(assignmentRequestsController.rejectAssignmentRequest());
//
//	}
	
	/**
	 * Tests the reject assignment request
	 */
	@Test
	public void testGetters_Setters() {

		assignmentRequestsController.setSelectedProject(testProject);
		assertTrue(assignmentRequestsController.getSelectedProject().equals(testProject));
		
		assignmentRequestsController.setSelectedTask(taskWithNoTeam);
		assertTrue(assignmentRequestsController.getSelectedTask().equals(taskWithNoTeam));
		
		TaskTeamRequest newReq = new TaskTeamRequest(teamPermanentCollaborator, taskWithNoTeam);
		newReq.setType(RequestType.ASSIGNMENT);
		assignmentRequestsController.setSelectedAdditionRequest(newReq);
		assertTrue(assignmentRequestsController.getSelectedAdditionRequest().equals(newReq));		


	}

	
}