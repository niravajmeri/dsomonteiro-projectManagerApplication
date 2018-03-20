package project.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.Services.ProjectService;
import project.Services.TaskService;
import project.Services.UserService;
import project.model.*;
import project.model.taskstateinterface.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class US377CollectionOfCancelledTasksFromAProjectTest {

	UserService userContainer;
	ProjectService projectContainer;
	
	User user1;
	User userAdmin;

	TaskService taskContainer;

	TaskCollaborator taskWorker1;

	ProjectCollaborator collab1;

	Project project;
	Project project2;

	Task testTask;
	Task testTask2;
	Task testTask3;

	Calendar estimatedTaskStartDateTest;
	Calendar taskDeadlineDateTest;
	Calendar startDateTest;

	US377CollectionOfCancelledTasksFromAProjectController controller;

	@Before
	public void setUp() {
		// creates an UserContainer
		userContainer = new UserService();
								
		// creates a Project Container
		projectContainer = new ProjectService();

		// create user
		user1 = userContainer.createUser("Daniel", "daniel@gmail.com", "001", "collaborator",
				"910000000", "Rua", "2401-00", "Test", "Testo", "Testistan");

		// create user admin
		userAdmin = userContainer.createUser("João", "joao@gmail.com", "001", "Admin", "920000000",
				"Rua", "2401-00", "Test", "Testo", "Testistan");

		// add user to user list
		userContainer.addUserToUserRepository(user1);
		userContainer.addUserToUserRepository(userAdmin);

		// Creates one Project
		project = projectContainer.createProject("name3", "description4", userAdmin);
		project2 = projectContainer.createProject("name1", "description4", userAdmin);

		// add project to project repository
		projectContainer.addProjectToProjectContainer(project);
		projectContainer.addProjectToProjectContainer(project2);

		// create project collaborators
		collab1 = new ProjectCollaborator(user1, 2);

		// create taskContainer

		taskContainer = project.getTaskService();

		// create task workers
		taskWorker1 = new TaskCollaborator(collab1);

		// set user as collaborator
		user1.setUserProfile(Profile.COLLABORATOR);

		userAdmin.setUserProfile(Profile.COLLABORATOR);

		// add user to project team
		project.addProjectCollaboratorToProjectTeam(collab1);
		project2.addProjectCollaboratorToProjectTeam(collab1);

		// create a estimated Task Start Date
		Calendar startDateTest = Calendar.getInstance();

		// create a estimated Task Start Date
		Calendar estimatedTaskStartDateTest = Calendar.getInstance();
		estimatedTaskStartDateTest.set(Calendar.YEAR, 2017);
		estimatedTaskStartDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		estimatedTaskStartDateTest.set(Calendar.DAY_OF_MONTH, 25);
		estimatedTaskStartDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create a estimated Task Dead line Date
		Calendar taskDeadlineDateTest = Calendar.getInstance();
		taskDeadlineDateTest.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest.set(Calendar.MONTH, Calendar.JANUARY);
		taskDeadlineDateTest.set(Calendar.DAY_OF_MONTH, 29);
		taskDeadlineDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create a expired estimated Task Dead line Date
		Calendar taskExpiredDeadlineDateTest = Calendar.getInstance();
		taskExpiredDeadlineDateTest.set(Calendar.YEAR, 2017);
		taskExpiredDeadlineDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		taskExpiredDeadlineDateTest.set(Calendar.DAY_OF_MONTH, 29);
		taskExpiredDeadlineDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create 4 tasks
		testTask = taskContainer.createTask("Test dis agen pls");
		testTask2 = taskContainer.createTask("Test dis agen pls");
		testTask3 = taskContainer.createTask("Test moar yeh");

		// Adds 5 tasks to the TaskContainer
		taskContainer.addTaskToProject(testTask);
		taskContainer.addTaskToProject(testTask2);
		taskContainer.addTaskToProject(testTask3);

		// set estimated task start date and task dead line to tasks
		testTask.setEstimatedTaskStartDate(estimatedTaskStartDateTest);
		testTask.setTaskDeadline(taskDeadlineDateTest);

		testTask2.setEstimatedTaskStartDate(estimatedTaskStartDateTest);
		testTask2.setTaskDeadline(taskDeadlineDateTest);

		testTask3.setEstimatedTaskStartDate(estimatedTaskStartDateTest);
		testTask3.setTaskDeadline(taskDeadlineDateTest);

		// set active user
		testTask.addTaskCollaboratorToTask(taskWorker1);
		testTask2.addTaskCollaboratorToTask(taskWorker1);
		testTask3.addTaskCollaboratorToTask(taskWorker1);

		// Sets the tasks to "Planned"
		testTask.setTaskState(new Planned());
		testTask2.setTaskState(new Planned());
		testTask3.setTaskState(new Planned());

		// set start date
		testTask.setStartDate(startDateTest);
		testTask2.setStartDate(startDateTest);
		testTask3.setStartDate(startDateTest);

		// Sets the tasks to "Ready"
		testTask.setTaskState(new Ready());
		testTask2.setTaskState(new Ready());
		testTask3.setTaskState(new Ready());

		// Sets the tasks to "onGoing"
		testTask.setTaskState(new OnGoing());
		testTask2.setTaskState(new OnGoing());
		testTask3.setTaskState(new OnGoing());

		// Sets the tasks to "cancelled"
		testTask.setTaskState(new Cancelled());
		testTask2.setTaskState(new Cancelled());

		// Creates the controller to be tested
		controller = new US377CollectionOfCancelledTasksFromAProjectController(project);
	}

	@After
	public void tearDown() {
		userContainer = null;
		projectContainer = null;
		user1 = null;
		testTask = null;
		testTask2 = null;
		testTask3 = null;
		project = null;
		taskContainer = null;
		taskWorker1 = null;
		collab1 = null;
		estimatedTaskStartDateTest = null;
		taskDeadlineDateTest = null;
		startDateTest = null;
	}

	/**
	 * this test verify if the list of canceled projects is equals to the list
	 * created.
	 */
	@Test
	public final void testGetCancelledTasksFromAProject() {

		// create list with cancelled task to compare
		List<Task> cancelledTaskToCompare = new ArrayList<Task>();

		// add task to the list
		cancelledTaskToCompare.add(testTask);
		cancelledTaskToCompare.add(testTask2);

		assertEquals(cancelledTaskToCompare, controller.getCancelledTasksFromAProject());

	}

	@Test
	public final void testGetCancelledTaskListId() {
		String result = "[1.1] Test dis agen pls";
		assertTrue(result.equals(controller.getCancelledTaskListId(project).get(0)));
		result = "[1.2] Test dis agen pls";
		assertTrue(result.equals(controller.getCancelledTaskListId(project).get(1)));
	}

	@Test
	public final void testSplitStringByFirstSpace() {
		String input = "Test me master!";
		assertTrue("Test".equals(controller.splitStringByFirstSpace(input)));
	}

	@Test
	public final void getProjectCancelledTasks() {
		assertEquals(2, controller.getCancelledTasksFromAProject().size());
	}

}
