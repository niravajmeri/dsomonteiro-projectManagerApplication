package test.java.project.model;

//
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.project.model.Company;
import main.java.project.model.Project;
import main.java.project.model.ProjectCollaborator;
import main.java.project.model.Task;
import main.java.project.model.User;

class ProjectTests {

	Company c1;
	User u1;
	User u2;
	ProjectCollaborator projectCollaborator1;
	ProjectCollaborator projectCollaborator2;
	Calendar estimatedStartDate;
	Calendar taskDeadline;
	Task t1;
	Task t2;
	Task t3;
	Project p1;
	Project p2;

	@BeforeEach
	public void setUp() {

		c1 = Company.getTheInstance();
		c1.getUsersRepository().getAllUsersFromRepository().clear();
		u1 = new User("name", "email", "idNumber", "function", "123456789");
		u2 = new User("name2", "email2", "idNumber2", "function2", "987654321");
		projectCollaborator1 = new ProjectCollaborator(u2, 1200);
		projectCollaborator2 = new ProjectCollaborator(u1, 1200);
		p1 = new Project(1, "name3", "description4", u1);
		estimatedStartDate = Calendar.getInstance();
		estimatedStartDate.set(2017, Calendar.JANUARY, 14);
		taskDeadline = Calendar.getInstance();
		taskDeadline.set(2017, Calendar.NOVEMBER, 17);
		t1 = p1.getTaskRepository().createTask("description", 0, estimatedStartDate, taskDeadline, 0);
		p1.getTaskRepository().addProjectTask(t1);
		t2 = p1.getTaskRepository().createTask("description2", 0, null, null, 0);
		p1.getTaskRepository().addProjectTask(t2);
		t2.markTaskAsFinished();
		p2 = new Project(2, "name1", "description4", u2);
		t3 = p1.getTaskRepository().createTask("description3", 0, null, null, 0);
		t3.markTaskAsFinished();

	}

	@AfterEach
	void tearDown() {
		Company c1 = null;
		User u1 = null;
		User u2 = null;
		Task t1 = null;
		Task t2 = null;
		Task t3 = null;
		Project p1 = null;
		Project p2 = null;
		ProjectCollaborator projectCollaborator1 = null;
		ProjectCollaborator projectCollaborator2 = null;
	}

	/**
	 * Tests constructor for Project
	 */
	@Test
	void testProject() {

	}

	@Test
	void testAddTaskToProjectTaskList() {
		assertEquals(t1, p1.getTaskRepository().getProjectTaskList().get(0));
	}

	/**
	 * This test checks the project manager of Project 1
	 */
	@Test
	void testCheckProjectManagerTrue() {
		assertTrue(p1.isProjectManager(u1));
	}

	/**
	 * This test checks the project manager of Project 1
	 */
	@Test
	void testCheckProjectManagerFalse() {
		assertFalse(p1.isProjectManager(u2));
	}

	/**
	 * This test changes project manager of Project 1 and checks if this change
	 * occurred.
	 */
	@Test
	void testSetProjectManagerTrue() {

		p1.setProjectManager(u2);
		assertTrue(p1.isProjectManager(u2));
	}

	/**
	 * This test changes project manager of Project 1 and then checks if the
	 * previous Project Manager is no longer in the project manager.
	 */
	@Test
	void testSetProjectManagerFalse() {
		p1.setProjectManager(u2);
		assertFalse(p1.isProjectManager(u1));
	}

	/**
	 * This test changes project manager of Project 1 and then checks if the
	 * previous Project Manager is no longer in the project manager.
	 */
	@Test
	void testAddUserToProjectTeam() {
		p1.addUserToProjectTeam(projectCollaborator1);
		assertEquals(u2, p1.getProjectTeam().get(0).getCollaboratorUserData());
	}

	/**
	 * Tests that it is impossible to add the same object twice to the ProjectTeam
	 */
	@Test
	void testTryToAddTheSameUserTwiceToProjectTeam() {
		p1.addUserToProjectTeam(projectCollaborator2);
		p1.addUserToProjectTeam(projectCollaborator2);
		assertEquals(1, p1.getProjectTeam().size());
	}

	/*
	 * @Test void testEqualsTrue() { Company c2 = Company.getTheInstance(); Project
	 * p3 = new Project("name5", "description5", u2);
	 * c2.addProjectToProjectList(p3); assertFalse(p3.equals(p1)); }
	 */
	/**
	 * Tests the comparison between objects that are different
	 */
	@Test
	void testEqualsFalse() {
		assertFalse(p1.equals(p2));
	}

	/**
	 * Tests the comparison between objects that are different and from different
	 * types
	 */
	@Test
	void testEqualsDifferentObject() {
		assertFalse(p1.equals(u1));
	}

	/**
	 * Sets the status of a project to Execution
	 */
	@Test
	void testProjectState() {
		p1.setProjectStatus(Project.EXECUTION);
		assertEquals(Project.EXECUTION, p1.getProjectStatus());
	}

	@Test
	void testGetUnfinishedTasks() {
		t1.addUserToTask(projectCollaborator2);
		assertEquals(t1, p1.getTaskRepository().getUnFinishedTasksFromUser(u1).get(0));
	}

	@Test
	void testGetFinishedTasks() {
		t2.addUserToTask(projectCollaborator1);
		assertEquals(t2, p1.getTaskRepository().getFinishedTaskListofUserInProject(u2).get(0));
	}

	@Test
	void testGetAllTasks() {// The order of tasks in the test list changes because the getAllTasks method
							// shows the finished tasks first.
		t1.addUserToTask(projectCollaborator2);
		t2.addUserToTask(projectCollaborator2);
		List<Task> test = new ArrayList<Task>();
		test.add(t1);
		test.add(t2);
		assertEquals(test, p1.getTaskRepository().getAllTasks(u1));
	}

	@Test
	void testGetFinishedTaskListLastMonth() {
		Calendar test = Calendar.getInstance();
		test.add(Calendar.MONTH, -1);
		t1.addUserToTask(projectCollaborator2);
		t2.addUserToTask(projectCollaborator2);
		t3.addUserToTask(projectCollaborator2);
		p1.getTaskRepository().addProjectTask(t1);
		p1.getTaskRepository().addProjectTask(t2);
		p1.getTaskRepository().addProjectTask(t3);
		t2.setFinishDate();
		t3.setFinishDate();
		t2.getFinishDate().set(Calendar.MONTH, test.get(Calendar.MONTH) - 1);
		t3.getFinishDate().set(Calendar.MONTH, test.get(Calendar.MONTH));
		assertEquals(t3, p1.getTaskRepository().getFinishedTasksGivenMonth(u1, 1).get(0));
	}

	@Test
	void testAddUserToTask() {
		p1.getTaskRepository().addProjectTask(t1);
		p1.getTaskRepository().addProjectTask(t2);
		p1.getTaskRepository().addProjectTask(t3);
		t2.addUserToTask(projectCollaborator2);
		assertTrue(t2.taskTeamContainsUser(u1));
	}

	@Test
	void testFailToAddUserToTask() {
		p1.getTaskRepository().addProjectTask(t1);
		p1.getTaskRepository().addProjectTask(t2);
		p1.getTaskRepository().addProjectTask(t3);
		t2.addUserToTask(projectCollaborator2);
		assertFalse(t1.taskTeamContainsUser(u1));
	}

	@Test
	void testProjectContainsTask() {
		assertTrue(p1.getTaskRepository().containsTask(t2));
	}

	@Test
	void testProjectContainsTaskFalse() {
		assertFalse(p1.getTaskRepository().containsTask(t3));
	}

	/**
	 * This method allows removing a Project Collaborator from a Project Team and
	 * includes removing that Project Collaborator from all Tasks in this Project
	 * 
	 * projectCollaborator1 is removed from ProjectTeam
	 */
	@Test
	void testRemoveCollaboratorFromProjectTeam() {

		p1.addUserToProjectTeam(projectCollaborator1);
		p1.addUserToProjectTeam(projectCollaborator2);
		t1.addUserToTask(projectCollaborator1);
		t1.addUserToTask(projectCollaborator2);

		p1.removeCollaboratorFromProjectTeam(projectCollaborator1);

		assertEquals(1, p1.getProjectTeam().size());
		assertFalse(projectCollaborator1.equals(p1.getProjectTeam().get(0)));
		assertFalse(p1.getTaskRepository().getAllTasks(u2).get(0).getTaskTeam().get(0).isTaskWorkerActiveInTask());
	}

}