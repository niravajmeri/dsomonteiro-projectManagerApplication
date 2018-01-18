package project.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.model.Company;
import project.model.Profile;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.TaskCollaborator;
import project.model.TaskRepository;
import project.model.User;
import project.model.taskStateInterface.Assigned;
import project.model.taskStateInterface.Cancelled;
import project.model.taskStateInterface.OnGoing;
import project.model.taskStateInterface.Planned;
import project.model.taskStateInterface.Ready;

public class US377CollectionOfCancelledTasksFromAProjectTest {

	Company myCompany;

	User user1;
	User userAdmin;

	TaskRepository taskRepository;

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

	@Before
	public void setUp() {
		// create company
		myCompany = Company.getTheInstance();

		// create user
		user1 = myCompany.getUsersRepository().createUser("Daniel", "daniel@gmail.com", "001", "collaborator",
				"910000000", "Rua", "2401-00", "Test", "Testo", "Testistan");

		// create user admin
		userAdmin = myCompany.getUsersRepository().createUser("João", "joao@gmail.com", "001", "Admin", "920000000",
				"Rua", "2401-00", "Test", "Testo", "Testistan");

		// add user to user list
		myCompany.getUsersRepository().addUserToUserRepository(user1);
		myCompany.getUsersRepository().addUserToUserRepository(userAdmin);

		// Creates one Project
		project = myCompany.getProjectsRepository().createProject("name3", "description4", userAdmin);
		project2 = myCompany.getProjectsRepository().createProject("name1", "description4", userAdmin);

		// add project to project repository
		myCompany.getProjectsRepository().addProjectToProjectRepository(project);
		myCompany.getProjectsRepository().addProjectToProjectRepository(project2);

		// create project collaborators
		collab1 = new ProjectCollaborator(user1, 2);

		// create taskRepository

		taskRepository = project.getTaskRepository();

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
		testTask = taskRepository.createTask("Test dis agen pls");
		testTask2 = taskRepository.createTask("Test dis agen pls");
		testTask3 = taskRepository.createTask("Test moar yeh");

		// Adds 5 tasks to the TaskRepository
		taskRepository.addProjectTask(testTask);
		taskRepository.addProjectTask(testTask2);
		taskRepository.addProjectTask(testTask3);

		// Creates State Objects planned for task.
		Planned PlannedTestTask = new Planned(testTask);
		Planned PlannedTestTask2 = new Planned(testTask2);
		Planned PlannedTestTask3 = new Planned(testTask3);

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
		testTask.setTaskState(PlannedTestTask);
		testTask2.setTaskState(PlannedTestTask2);
		testTask3.setTaskState(PlannedTestTask3);

		// Creates State Objects assigned for task.
		Assigned AssignedTestTask = new Assigned(testTask);
		Assigned AssignedTestTask2 = new Assigned(testTask2);
		Assigned AssignedTestTask3 = new Assigned(testTask3);

		// Sets the tasks to "Assigned"
		testTask.setTaskState(AssignedTestTask);
		testTask2.setTaskState(AssignedTestTask2);
		testTask3.setTaskState(AssignedTestTask3);

		// Creates State Objects Ready for task.
		Ready ReadyTestTask = new Ready(testTask);
		Ready ReadyTestTask2 = new Ready(testTask2);
		Ready ReadyTestTask3 = new Ready(testTask3);

		// set start date
		testTask.setStartDate(startDateTest);
		testTask2.setStartDate(startDateTest);
		testTask3.setStartDate(startDateTest);

		// Sets the tasks to "Ready"
		testTask.setTaskState(ReadyTestTask);
		testTask2.setTaskState(ReadyTestTask2);
		testTask3.setTaskState(ReadyTestTask3);

		// Creates State Objects OnGoing for task.
		OnGoing onGoingTestTask = new OnGoing(testTask);
		OnGoing onGoingTestTask2 = new OnGoing(testTask2);
		OnGoing onGoingTestTask3 = new OnGoing(testTask3);

		// Sets the tasks to "onGoing"
		testTask.setTaskState(onGoingTestTask);
		testTask2.setTaskState(onGoingTestTask2);
		testTask3.setTaskState(onGoingTestTask3);

		// Creates State Objects Cancelled for task.
		Cancelled cancelledTestTask = new Cancelled(testTask);
		Cancelled cancelledTestTask2 = new Cancelled(testTask2);

		// Sets the tasks to "cancelled"
		testTask.setTaskState(cancelledTestTask);
		testTask2.setTaskState(cancelledTestTask2);

	}

	@After
	public void tearDown() {
		user1 = null;
		testTask = null;
		testTask2 = null;
		testTask3 = null;
		project = null;
		taskRepository = null;
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

		// create controller

		US377CollectionOfCancelledTasksFromAProject controllerUS377 = new US377CollectionOfCancelledTasksFromAProject(
				project.getIdCode());

		// create list with cancelled task to compare
		List<Task> cancelledTaskToCompare = new ArrayList<Task>();

		// add task to the list
		cancelledTaskToCompare.add(testTask);
		cancelledTaskToCompare.add(testTask2);

		assertEquals(cancelledTaskToCompare, controllerUS377.getCancelledTasksFromAProject());

	}

}