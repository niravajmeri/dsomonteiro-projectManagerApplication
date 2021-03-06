/**
 * 
 */
package project.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import project.model.*;
import project.model.taskstateinterface.Cancelled;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the methods that are called in Controller to execute the
 * action of Canceling an OnGoing Task
 * 
 * @author Group3
 *
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "project.services", "project.model", "project.controllers" })
public class US347CancelOnGoingTaskControllerTest {

	// TasksFiltersController tasksFiltersController;
	@Autowired
	US347CancelOnGoingTaskController controllerCancel;

	@Autowired
	UserService userService;

	@Autowired
	ProjectService projectService;
	@Autowired
	TaskService taskService;

	User user1, user2, projectManager;
	Project project1;
	ProjectCollaborator projCollab1, projCollab2;
	Task task1, task2, task3;
	TaskCollaborator taskCollab1, taskCollab2;
	Calendar startDateTest;
	Calendar estimatedTaskStartDateTest;
	Calendar taskDeadlineDateTest;
	Calendar taskExpiredDeadlineDateTest;

	@Before
	public void setUp() {

		// create users
		user1 = userService.createUser("Joe Smith", "jsmith@gmail.com", "001", "Junior Programmer", "930000000",
				"Rua da Caparica, 19", "7894-654", "Porto", "Porto", "Portugal");
		user2 = userService.createUser("John Smith", "johnsmith@gmail.com", "001", "General Manager", "930025000",
				"Rua Doutor Armando", "4455-654", "Rio Tinto", "Gondomar", "Portugal");
		projectManager = userService.createUser("Mary MacJohn", "mmacjohn@gmail.com", "003", "Product Manager",
				"930025000", "Rua Terceira, 44", "4455-122", "Leça da Palmeira", "Matosinhos", "Portugal");

		// set user as collaborator
		user1.setUserProfile(Profile.COLLABORATOR);
		user2.setUserProfile(Profile.COLLABORATOR);
		projectManager.setUserProfile(Profile.COLLABORATOR);

		// create project and establishes collaborator projectManager as project manager
		// of project 1
		project1 = projectService.createProject("Project Management software", "This software main goals are ....",
				projectManager);

		// create project collaborators
		projCollab1 = new ProjectCollaborator(user1, 2);
		projCollab2 = new ProjectCollaborator(user2, 2);

		// add collaborators to Project
		project1.createProjectCollaborator(user1, 2);
		project1.createProjectCollaborator(user2, 2);

		// create tasks
		task1 = taskService.createTask("Task 1", project1);
		task2 = taskService.createTask("Task 2", project1);
		task3 = taskService.createTask("Task 3", project1);

		// create a estimated Task Start Date
		startDateTest = Calendar.getInstance();

		// create a estimated Task Start Date
		estimatedTaskStartDateTest = Calendar.getInstance();
		estimatedTaskStartDateTest.set(Calendar.YEAR, 2017);
		estimatedTaskStartDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		estimatedTaskStartDateTest.set(Calendar.DAY_OF_MONTH, 25);
		estimatedTaskStartDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create a estimated Task Dead line Date
		taskDeadlineDateTest = Calendar.getInstance();
		taskDeadlineDateTest.set(Calendar.YEAR, 2018);
		taskDeadlineDateTest.set(Calendar.MONTH, Calendar.JANUARY);
		taskDeadlineDateTest.set(Calendar.DAY_OF_MONTH, 29);
		taskDeadlineDateTest.set(Calendar.HOUR_OF_DAY, 14);

		// create a expired estimated Task Dead line Date
		taskExpiredDeadlineDateTest = Calendar.getInstance();
		taskExpiredDeadlineDateTest.set(Calendar.YEAR, 2017);
		taskExpiredDeadlineDateTest.set(Calendar.MONTH, Calendar.SEPTEMBER);
		taskExpiredDeadlineDateTest.set(Calendar.DAY_OF_MONTH, 29);
		taskExpiredDeadlineDateTest.set(Calendar.HOUR_OF_DAY, 14);


		// set estimated task start date and task dead line to tasks
		task1.setEstimatedTaskStartDate(estimatedTaskStartDateTest);
		task1.setTaskDeadline(taskDeadlineDateTest);

		task2.setEstimatedTaskStartDate(estimatedTaskStartDateTest);
		task2.setTaskDeadline(taskDeadlineDateTest);

		task3.setEstimatedTaskStartDate(estimatedTaskStartDateTest);
		task3.setTaskDeadline(taskDeadlineDateTest);

		task1.setTaskBudget(100.0);
		task1.setEstimatedTaskEffort(100.0);


		// create task workers
		taskCollab1 = new TaskCollaborator(projCollab1);
		taskCollab2 = new TaskCollaborator(projCollab2);

		// set active user
		task1.addProjectCollaboratorToTask(projCollab1);
		task2.addProjectCollaboratorToTask(projCollab2);
		task3.addProjectCollaboratorToTask(projCollab1);

		task1.setStartDateAndState(startDateTest);


		// create controllers
		controllerCancel.setTaskID(task1.getTaskID());
		controllerCancel.setProject(project1);

	}

	@After
	public void clear() {

		user1 = null;
		user2 = null;
		projectManager = null;
		project1 = null;
		projCollab1 = null;
		projCollab2 = null;
		task1 = null;
		task2 = null;
		task3 = null;
		taskCollab1 = null;
		taskCollab2 = null;
		startDateTest = null;
		estimatedTaskStartDateTest = null;
		taskDeadlineDateTest = null;
		taskExpiredDeadlineDateTest = null;

	}

	/**
	 * Test method that views the task state and returns this information in a
	 * string format
	 */
	@Test
	public void testViewTaskState() {

		assertEquals("OnGoing", controllerCancel.viewTaskState());
	}

	/**
	 * This test verifies if an task in state OnGoing is changed to Cancelled using
	 * the controllers.
	 */
	@Test
	public void testCancelOnGoingTask() {

		// Sets task2 to "cancelled"
		task1.setTaskState(new Cancelled());

		// Sets a cancel date for the task1
		task1.cancelTask();

		// use of control to set task1 to state cancelled
		controllerCancel.cancelOnGoingTask();

		// asserts that task1 has state cancelled
		assertEquals("Cancelled", task1.viewTaskStateName());
	}

	/**
	 * This test verifies if task in state OnGoing and then marked with a finish
	 * date can not be changed to Cancelled using the controllers.
	 */
	@Test
	public void testNotCancelOnGoingTask() {

		// Sets a finish date for the task1
		task1.setFinishDate(Calendar.getInstance());
		task1.markTaskAsFinished();

		// use of control to set task1 to state cancelled
		controllerCancel.cancelOnGoingTask();

		// asserts that task1 has state cancelled
		assertEquals("Finished", task1.viewTaskStateName());
	}
	

	@Test
	public void testGetters_Setters() {

		assertEquals(task1.getTaskID(), controllerCancel.getTaskID());
		assertTrue(project1.equals(controllerCancel.getProject()));
	}

}
