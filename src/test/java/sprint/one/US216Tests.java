package sprint.one;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.model.Company;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.ProjectRepository;
import project.model.Task;
import project.model.TaskRepository;
import project.model.TaskCollaborator;
import project.model.User;
import project.model.UserRepository;

public class US216Tests {

	/**
	 * Tests US216
	 * 
	 * US216: Como colaborador, eu pretendo obter a média de tempo gasto por tarefa,
	 * nas tarefas que concluí no último mês.
	 * 
	 * uses methods getAverageTimeLastMonthFinishedTasksUser() and
	 * getTimeSpentOnTask();
	 */

	/**
	 * This method returns the average time spent by task during the last month.
	 * This method gets the total time spent on every task finished on last month.
	 * Then it will divide that time by the number of tasks.
	 * 
	 * @return Returns the average time spent by finished task in the last month.
	 * 
	 */

	Company myCompany;
	UserRepository userRepository;
	User user1;
	User user2;
	ProjectCollaborator projectCollaborator1;
	TaskCollaborator taskWorker1;
	ProjectRepository projectRepository;
	Project myProject;
	TaskRepository taskRepository;
	Task task1;
	Task task2;
	Task task3;
	Task task4;

	@Before
	public void setUp() {

		myCompany = Company.getTheInstance();

		user1 = myCompany.getUsersRepository().createUser("Daniel", "daniel@gmail.com", "001", "Programador",
				"910000000", "Rua Azul", "5679-987", "braga", "braga", "portugal");
		user2 = myCompany.getUsersRepository().createUser("Rita", "rita@gmail.com", "002", "Gestora de Projeto",
				"920000000", "rua verde", "6789", "porto", "porto", "portugal");

		// create myProject
		myProject = myCompany.getProjectsRepository().createProject("Projecto I", "Projecto de Gestão", user1);

		// Generate a Start Calendar
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.MONTH, -3);

		// Generates a Finish Calendar
		Calendar finishDate = Calendar.getInstance();
		finishDate.add(Calendar.MONTH, -1);
		Calendar otherFinishDate = Calendar.getInstance();
		otherFinishDate.add(Calendar.MONTH, -2);

		// Four new tasks were created and added to project1
		task1 = myProject.getTaskRepository().createTask("Task 1", 1, startDate, finishDate, 10);
		task2 = myProject.getTaskRepository().createTask("Task 2", 2, startDate, finishDate, 10);
		task3 = myProject.getTaskRepository().createTask("Task 3", 3, startDate, finishDate, 10);
		task4 = myProject.getTaskRepository().createTask("Task 4", 4, startDate, finishDate, 10);

		// Users 1 and 2 added to the users repository.
		myCompany.getUsersRepository().addUserToUserRepository(user1);
		myCompany.getUsersRepository().addUserToUserRepository(user2);

		// Project 1 added to the project repository.
		myCompany.getProjectsRepository().addProjectToProjectRepository(myProject);

		// create project collaborators
		projectCollaborator1 = myProject.createProjectCollaborator(user1, 10);

		// user2 added user 1 to the ProjectTeam
		myProject.addProjectCollaboratorToProjectTeam(projectCollaborator1);

		// Add Tasks to project 1
		myProject.getTaskRepository().addProjectTask(task1);
		myProject.getTaskRepository().addProjectTask(task2);
		myProject.getTaskRepository().addProjectTask(task3);
		myProject.getTaskRepository().addProjectTask(task4);

		// create task workers
		taskWorker1 = task1.createTaskCollaborator(projectCollaborator1);

		// Associates users to tasks
		task1.addTaskCollaboratorToTask(taskWorker1);
		task2.addTaskCollaboratorToTask(taskWorker1);
		task3.addTaskCollaboratorToTask(taskWorker1);
		task4.addTaskCollaboratorToTask(taskWorker1);

		// Finish dates were attributed to each task
		task1.setFinishDate(otherFinishDate);
		task2.setFinishDate(finishDate);
		task3.setFinishDate(finishDate);

		// Tasks were marked as Finished.
		task1.markTaskAsFinished();
		task2.markTaskAsFinished();
		task3.markTaskAsFinished();

		// List to compare to the getLastMonthFinishedUserTaskList.
		List<Task> expResult = new ArrayList<Task>();
		// expResult.add(t1);
		expResult.add(task2);
		expResult.add(task3);

	}

	@After
	public void tearDown() {
		myCompany.clear();
		userRepository = null;
		user1 = null;
		user2 = null;
		projectCollaborator1 = null;
		taskWorker1 = null;
		projectRepository = null;
		myProject = null;
		taskRepository = null;
		task1 = null;
		task2 = null;
		task3 = null;
		task4 = null;
	}

	@Test
	public void getAverageTimeLastMonthFinishedTasksUser() {

		// Calculate expected Total time spent in task 2 and task3, by calling the
		// method getTimeSpentOnTask
		double expectTotalTime = (task2.getTimeSpentByProjectCollaboratorOntask(projectCollaborator1)
				+ task3.getTimeSpentByProjectCollaboratorOntask(projectCollaborator1));
		// Calculate expected Average total time spent in task 2 and task3, by dividing
		// the total time spent on these tasks and
		// dividing by the number of tasks (in the case 2 tasks).
		double expectAverageTime = expectTotalTime / 2;

		assertEquals(expectAverageTime,
				myCompany.getProjectsRepository().getAverageTimeOfFinishedTasksFromUserLastMonth(user1), 0.000000001);

	}
}
