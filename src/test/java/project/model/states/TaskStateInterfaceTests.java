package project.model.states;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.Task;
import project.model.User;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class TaskStateInterfaceTests {

	User user; 
	User userPm; 
	Project project;
	Task task;
	Calendar estimatedTaskStartDate, taskDeadline;

	@Before
	public void setUp() {
		
		//Creates the users
		user = new User("Joao", "joao@gmail.com", "001", "Junior Programmer",
				"930000000");
		userPm = new User("Daniel", "daniel@gmail.com", "001", "Junior Programmer",
				"930000000");		
		//Creates the project
		project = new Project("Teste", "teste", user); 
		
		//Creates the Task
		task = project.createTask("Task de teste"); 

		// create a estimated task date
		estimatedTaskStartDate = Calendar.getInstance();

		// create a dead line date
		taskDeadline = Calendar.getInstance();

	}

	@After
	public void tearDown() {
		task = null;
		user = null;
		userPm = null;
		project = null;
		estimatedTaskStartDate = null;
		taskDeadline = null;

	}


	/**
	 * Tests the state interface, by calling all the actions that may provoce a state change. 
	 */
	@Test
	public final void doActionAndIsValid() {
		
		//Asserts that the task state is created 
		assertEquals("Created", task.viewTaskStateName()); 		
		
		//Adds the estimated start and deadline dates to the task 
		task.setEstimatedTaskStartDate(estimatedTaskStartDate);
		task.setTaskDeadline(taskDeadline);
		
		//Adds someone to the task team
		ProjectCollaborator collaborator = project.createProjectCollaborator(user, 10);
		task.addProjectCollaboratorToTask(collaborator);
		
		//Asserts that the state changed to Planned
		assertEquals("Planned", task.viewTaskStateName()); 		
	
		//Adds the estimated task effort and budget
		task.setTaskBudget(10);
		task.setEstimatedTaskEffort(10);
		
		//Asserts that the state changed to Ready
		assertEquals("Ready", task.viewTaskStateName()); 	
		
		//Removes the users from the task 
		task.removeAllCollaboratorsFromTaskTeam();
		
		//Asserts that the state changed again to Planned
		assertEquals("Planned", task.viewTaskStateName());
		
		//Adds someone to the task team again 
		task.addProjectCollaboratorToTask(collaborator); 
		
		//Asserts that the state changed to ready
		assertEquals("Ready", task.viewTaskStateName()); 
		
		//Sets start date 
		task.setStartDate(Calendar.getInstance());
		
		//Asserts that the state changed to OnGoing
		assertEquals("OnGoing", task.viewTaskStateName()); 
		
		//Removes all collaborators from task
		task.removeAllCollaboratorsFromTaskTeam();
		
		//Asserts that the state changed to StandBy
		assertEquals("StandBy", task.viewTaskStateName()); 
		
		//Adds someone to the task team 
		task.addProjectCollaboratorToTask(collaborator); 
		
		//Asserts that the state changed to OnGoing
		assertEquals("OnGoing", task.viewTaskStateName());
				
		//Mark task as finished 
		task.markTaskAsFinished(); 
		
		//Asserts that the task state changed to finished 
		assertEquals("Finished", task.viewTaskStateName());
		
		//Marks the task as Unfinished
		task.addProjectCollaboratorToTask(collaborator);
		task.UnfinishTask();
		
		//Asserts that the taskState changed to Ongoing again
		assertEquals("OnGoing", task.viewTaskStateName()); 
				
		//Cancels the task
		task.cancelTask();
				
		//Asserts that the taskState changed to Cancelled
		assertEquals("Cancelled", task.viewTaskStateName());		
	}
}