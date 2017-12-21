package project.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.TaskCollaborator;
import project.model.User;

public class ProjectCollaboratorAssociatedToTaskControllerTest {

	/**
	 * 
	 */

	User newUserA;
	Project project1;
	Task taskA;

	ProjectCollaborator projCollab1;
	TaskCollaborator taskWorker1;
	ProjectCollaboratorAssociatedToTaskController taskController;

	@Before
	public void setUp() {

		taskController = new ProjectCollaboratorAssociatedToTaskController();

		// create user
		newUserA = new User("Daniel", "daniel@gmail.com", "001", "collaborator", "910000000");

		// Creation of one project and newUser4 set as the project manager
		project1 = new Project(0, "name3", "description4", newUserA);

		// Creation of two tasks: taskA and taskB
		Calendar startDateA = Calendar.getInstance();
		startDateA.clear();
		startDateA.set(2017, 05, 15);
		Calendar finishDateA = Calendar.getInstance();
		finishDateA.clear();
		finishDateA.set(2017, 05, 16);
		taskA = project1.getTaskRepository().createTask("Test dis pls", 100, startDateA, finishDateA, 15000);

		// Creates a project collaborator
		projCollab1 = project1.createProjectCollaborator(newUserA, 250);

		// Creates a taks worker
		taskWorker1 = taskA.createTaskCollaborator(projCollab1);

	}

	@After
	public void tearDown() {

		// Clears all the instances
		newUserA = null;
		project1 = null;
		taskA = null;
		taskController = null;

	}

	/*
	 * 
	 * This method tests the addProjectCollaboratorToTask Controller
	 */
	@Test
	public void testAddProjectCollaboratorToTaskController() {

		// Expects that projCollab1 is not associated to the taskA
		assertFalse(taskA.isProjectCollaboratorActiveInTaskTeam(projCollab1));

		// Adds projCollab1 to task
		taskController.addProjectCollaboratorToTaskController(taskA, projCollab1);

		// Checks if projCollab1 is now associated to the Task
		assertTrue(taskA.isProjectCollaboratorActiveInTaskTeam(projCollab1));

	}

	/*
	 * 
	 * This method tests the removeProjectCollaboratorFromTask Controller
	 */
	@Test
	public void testRemoveProjectCollaboratorFromTaskController() {

		// Adds projCollab1 to task
		taskController.addProjectCollaboratorToTaskController(taskA, projCollab1);

		// Checks if projCollab1 is now associated to the Task
		assertTrue(taskA.isProjectCollaboratorActiveInTaskTeam(projCollab1));

		// Remove projCollab1 from task

		taskController.removeProjectCollaboratorFromTaskController(taskA, projCollab1);

		// Checks if projCollab1 is not associated to the task anymore
		assertFalse(taskA.isProjectCollaboratorActiveInTaskTeam(projCollab1));

	}

	/*
	 * 
	 * This method tests the markTaskAsFinished Controller
	 */
	@Test
	public void testMarkTaskAsFinishedController() {

		// Adds projCollab1 to taskA and taskB
		taskController.addProjectCollaboratorToTaskController(taskA, projCollab1);

		// Checks if taskA is finished. Result is false
		assertFalse(taskA.isTaskFinished());

		// Marks taskA as finished
		taskController.markTaskAsFinishedController(taskA);

		// Checks if taskA is finished. Result is true
		assertTrue(taskA.isTaskFinished());

	}

}
