package project.model;

//
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

/**
 * Class to build Projects.
 * 
 * From this class one can create a new Project(object), which is defined by its
 * name, email, idNumber, function, address list, phone number, and by its
 * profile.
 * 
 * @author Group 3
 *
 */
@Entity
@Table(name = "Project")
public class Project implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int projectIdCode;
	private int status;

    @Embedded
	private TaskRepository taskRepository;
	@OneToOne
	private User projectManager;
	@OneToMany (fetch = LAZY, cascade = ALL, mappedBy = "project")
	private List<ProjectCollaborator> projectTeam;
	private String name;
	private String description;
	@Enumerated(EnumType.STRING)
	private EffortUnit effortUnit;
	private int budget;
	private Calendar startdate;
	private Calendar finishdate;

    @OneToMany (fetch = LAZY, cascade = ALL, mappedBy = "project")
	private List<TaskTeamRequest> pendingTaskAssignementRequests;

    @OneToMany (fetch = LAZY, cascade = ALL, mappedBy = "project")
	private List<TaskTeamRequest> pendingTaskRemovalRequests;

	public static final int PLANNING = 0; // planeado
	public static final int INITIATION = 1; // arranque
	public static final int EXECUTION = 2; // execução
	public static final int DELIVERY = 3; // entrega
	public static final int REVIEW = 4; // garantia
	public static final int CLOSE = 5; // fecho
	static final long serialVersionUID = 43L;

	/**
	 * Empty Constructor for Project
	 */
	protected Project(){

	}

	/**
	 * Project Constructor that demands the fields name, description and the Project
	 * Manager and contains the other non mandatory fields idCode, status,
	 * projectTaskList, projectTeam
	 * 
	 * @param name
	 * @param description
	 * @param projectManager
	 */
	public Project(int projCounter, String name, String description, User projectManager) {

		this.projectIdCode = projCounter;
		this.name = name;
		this.description = description;
		this.projectManager = projectManager;
		this.effortUnit = EffortUnit.HOURS;
		this.budget = 0;
		this.status = PLANNING;
		this.startdate = null;
		this.finishdate = null;
		this.taskRepository = new TaskRepository(projectIdCode);
		this.taskRepository.setProject(this);
		this.projectTeam = new ArrayList<>();
		this.pendingTaskAssignementRequests = new ArrayList<>();
		this.pendingTaskRemovalRequests = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int getPLANNING() {
		return PLANNING;
	}

	public static int getINITIATION() {
		return INITIATION;
	}

	public static int getEXECUTION() {
		return EXECUTION;
	}

	public static int getDELIVERY() {
		return DELIVERY;
	}

	public static int getREVIEW() {
		return REVIEW;
	}

	public static int getCLOSE() {
		return CLOSE;
	}

	public int getProjectIdCode() {
		return projectIdCode;
	}

	public void setProjectIdCode(int projectIdCode) {
		this.projectIdCode = projectIdCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public void setPendingTaskAssignementRequests(List<TaskTeamRequest> pendingTaskAssignementRequests) {
		this.pendingTaskAssignementRequests = pendingTaskAssignementRequests;
	}

	public void setPendingTaskRemovalRequests(List<TaskTeamRequest> pendingTaskRemovalRequests) {
		this.pendingTaskRemovalRequests = pendingTaskRemovalRequests;
	}
	/**
	 * This Method adds a User to a project. This action converts the User in a
	 * Project Collaborator ( User + costPerEffort)
	 * 
	 * @param userToAdd
	 * @param costPerEffort
	 */
	public void addUserToProjectTeam(User userToAdd, int costPerEffort) {
		addProjectCollaboratorToProjectTeam(createProjectCollaborator(userToAdd, costPerEffort));
	}

	/**
	 * Creates an instance of ProjectCollaborator
	 * 
	 * @param collaborator
	 *            User to add to Project Collaborator
	 * @param costPerEffort
	 *            Cost per Effort of user In Project
	 * 
	 * @return The new Project Collaborator instantiated
	 */
	public ProjectCollaborator createProjectCollaborator(User collaborator, int costPerEffort) {

		ProjectCollaborator newCollaborator = new ProjectCollaborator(collaborator, costPerEffort);
		newCollaborator.setProject(this);

		return newCollaborator;
	}

	/**
	 * Add Project Collaborator to project team if is missing from the projectTeam.
	 * 
	 * @param newAddedProjectCollaborator
	 *            Project Collaborator to add to the Project Team
	 */
	public void addProjectCollaboratorToProjectTeam(ProjectCollaborator newAddedProjectCollaborator) {
		if (!isUserInProjectTeam(newAddedProjectCollaborator.getUserFromProjectCollaborator())) {
			this.projectTeam.add(newAddedProjectCollaborator);
		} else if (!newAddedProjectCollaborator.isProjectCollaboratorActive()) {
			this.projectTeam.add(newAddedProjectCollaborator);
		}
	}

	/**
	 * Returns the start date of the project
	 * 
	 * @return startDate
	 */
	public Calendar getStartdate() {
		return startdate;
	}

	/**
	 * Sets the start date of the project
	 * 
	 * @param startdate
	 */
	public void setStartdate(Calendar startdate) {
		this.startdate = startdate;
	}

	/**
	 * Returns the finish date of the project
	 * 
	 * @return finishDate
	 */
	public Calendar getFinishdate() {
		return finishdate;
	}

	/**
	 * Sets the finish date of the project
	 * 
	 * @param finishdate
	 */
	public void setFinishdate(Calendar finishdate) {
		this.finishdate = finishdate;
	}

	/**
	 * This method allows the projectManager to be changed
	 * 
	 * @param newProjectManager
	 *            New Project Manager to Set
	 */
	public void setProjectManager(User newProjectManager) {
		this.projectManager = newProjectManager;
	}

	/**
	 * Get the users that is Project's Manager
	 * 
	 * @return projectManager
	 */
	public User getProjectManager() {
		return this.projectManager;
	}

	/**
	 * This method returns the Task Repository for this specific Project
	 * 
	 * @return taskRepository Task Repository for this Project
	 */
	public TaskRepository getTaskRepository() {
		return taskRepository;
	}

	/**
	 * Get the project's description
	 * 
	 * @return description of project (String)
	 */
	public String getProjectDescription() {
		return this.description;
	}

	/**
	 * This method allows to set the project's description
	 * 
	 * @param newDescription
	 *            to set
	 * 
	 */
	public void setProjectDescription(String newDescription) {
		this.description = newDescription;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "Project")
	/**
	 * Get the users that belong to this Project's Team
	 * 
	 * @return Project Team Team of the Project
	 */
	public List<ProjectCollaborator> getProjectTeam() {
		return this.projectTeam;
	}

	public void setProjectTeam(List<ProjectCollaborator> projectTeam){
		this.projectTeam = projectTeam;
	}

	/**
	 * Get the active users inside this Project's Team
	 * 
	 * @return Project Team - Active Team of the Project
	 */
	public ArrayList<ProjectCollaborator> getActiveProjectTeam() {
		ArrayList<ProjectCollaborator> activeCollaborators = new ArrayList<>();

		for (ProjectCollaborator other : this.projectTeam) {
			if (other.isProjectCollaboratorActive())
				activeCollaborators.add(other);
		}

		return activeCollaborators;
	}

	/**
	 * This method returns the state of the attribute status that can be planning,
	 * initiation, execution , delivery, review and close.
	 * 
	 * @return the status planning, initiation, execution , delivery, review or
	 *         close
	 */
	public int getProjectStatus() {
		return this.status;
	}

	public String getProjectStatusName() {
		switch (status) {
		case 0:
			return "Planning";
		case 1:
			return "Initiation";
		case 2:
			return "Execution";
		case 3:
			return "Delivery";
		case 4:
			return "Review";
		case 5:
			return "Closed";
		default:
			return "Unknown";

		}
	}

	/**
	 * This method allows the setting of the Project status, regardless of how it is
	 * previously instanced
	 * 
	 * @param newStatus
	 * @return the set status that can be planning, initiation, execution ,
	 *         delivery, review or close
	 */
	public void setProjectStatus(int newStatus) {
		this.status = newStatus;
	}

	/**
	 * Check if the user provided is the project manager or not. This method exists
	 * because when adding a user to a UserTeam, the user cannot be the project
	 * manager.
	 * 
	 * @param toCheck
	 *            User provided to check if he is the Project Manager
	 * @return TRUE if the user is the project manager False if the user is not the
	 *         project manager
	 */
	public boolean isProjectManager(User toCheck) {
		boolean result = false;
		if (toCheck.equals(this.projectManager)) {
			result = true;
		}
		return result;
	}

	/**
	 * 
	 * Add user to project team if is missing from the projectTeam. Checks if it's
	 * the project manager that wants to add the user to the team.
	 * 
	 * @param projectManager
	 *            User to compare to the Project's Manager
	 * @param toAdd
	 *            User to add to the Project Team
	 */

	/*
	 * public void addUserToProjectTeam(User projectManager, User toAdd) { if
	 * (this.isProjectManager(projectManager)) { if
	 * (!this.projectTeam.contains(toAdd)) { this.projectTeam.add(toAdd); } } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 3;
		result = prime * result + projectIdCode;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return projectIdCode == other.projectIdCode;
	}

	/*
	 * THIS METHOD SHOULD ONLY EXIST IN TASK AS IT IS THE TASK RESPONSIBILITY TO ADD
	 * A USER TO ITS TASK
	 *//**
		 * Adds user to task in project. Company calls this method to add user to Task.
		 *
		 * @param user
		 * @param task
		 *//*
			 * public void addUserToTaskInProject(User user, Task task) { for (Task other :
			 * this.projectTaskList) { if (task.equals(other)) { task.addUserToTask(user); }
			 * } }
			 */

	/**
	 * Checks if the user is in the Project Team. A project collaborator contains a
	 * User that is a collaborator and has a cost associated to him
	 * 
	 * @param user
	 *            User to add as ProjectCollaborator
	 * 
	 * @return TRUE if the user exists in the project team FALSE if the user does
	 *         not exist in the project team
	 */
	public boolean isUserInProjectTeam(User user) {
		for (ProjectCollaborator other : this.projectTeam) {
			if (user.equals(other.getUserFromProjectCollaborator())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the user is in the Project Team and is active. A project
	 * collaborator contains a User that is a collaborator and has a cost associated
	 * to him
	 * 
	 * @param user
	 *            User to add as ProjectCollaborator
	 * 
	 * @return TRUE if the user exists in the project team AND is active FALSE if
	 *         the user does not exist in the project team OR isn't active
	 */
	public boolean isUserActiveInProject(User user) {
		for (ProjectCollaborator other : this.projectTeam) {
			if (user.equals(other.getUserFromProjectCollaborator()) && other.isProjectCollaboratorActive()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get Project idCode
	 * 
	 * @return idCode of Project
	 */
	public int getIdCode() {
		return this.projectIdCode;
	}

	/**
	 * Get Project effortUnit
	 * 
	 * @return EfforUnit of Project
	 */
	public EffortUnit getEffortUnit() {
		return this.effortUnit;
	}

	/**
	 * Set Project effortUnit
	 */
	public void setEffortUnit(EffortUnit newEffortUnit) {
		this.effortUnit = newEffortUnit;
	}

	/**
	 * Get Project budget
	 * 
	 * @return Budget (int) of Project
	 */
	public int getProjectBudget() {
		return this.budget;
	}

	/**
	 * Set Project budget
	 */
	public void setProjectBudget(int newBudget) {
		this.budget = newBudget;
	}

	/**
	 * This method checks which ProjectCollaborators don't have tasks assigned.
	 * First, it gets all the Project Collaborators in project team; then, cycles
	 * each Project Collaborator through each task and verifies if it's active on
	 * any task. If so, it's removed from the inactiveCollaborators list.
	 *
	 * @Return returns a List of Project Collaborators that are not assigned to a
	 *         Task
	 *
	 */

	public ArrayList<ProjectCollaborator> getCollaboratorsWithoutTasks() {
		ArrayList<ProjectCollaborator> inactiveCollaborators = new ArrayList<>();
		inactiveCollaborators.addAll(this.getProjectTeam());
		for (ProjectCollaborator other : this.getProjectTeam()) {
			if (this.taskRepository.isCollaboratorActiveOnAnyTask(other)) // needs to check if
				// collaborator is
				// active
				inactiveCollaborators.remove(other);
		}
		return inactiveCollaborators;
	}

	/**
	 * This method allows removing a Project Collaborator from a Project Team and
	 * includes removing that Project Collaborator from all Tasks in this Project
	 *
	 * @param collaboratorToRemoveFromProjectTeam
	 *            Collaborator to remove from project
	 */
	/*
	 * private void removeCollaboratorFromProjectTeam(ProjectCollaborator
	 * collaboratorToRemoveFromProjectTeam) { // REFACTOR TO
	 * removeProjectCollaboratorFromProjectTeam
	 * 
	 * if (this.projectTeam.contains(collaboratorToRemoveFromProjectTeam)) {
	 * collaboratorToRemoveFromProjectTeam.setState(false); for (Task otherTask :
	 * this.taskRepository.getAllTasks(collaboratorToRemoveFromProjectTeam)) {
	 * otherTask.removeUserFromTask(collaboratorToRemoveFromProjectTeam); } }
	 * 
	 * }
	 */

	/**
	 * This method allows the inactivation of a User from a Project Team which
	 * includes deactivate the Project Collaborator in the Project Team of this
	 * Project
	 * 
	 * @param collaboratorToRemoveFromProjectTeam
	 *            Collaborator to remove from project
	 * 
	 * @return remove
	 */

	public boolean removeProjectCollaboratorFromProjectTeam(User collaboratorToRemoveFromProjectTeam) {
		boolean remove = false;
		for (ProjectCollaborator other : projectTeam) {
			if (other.getUserFromProjectCollaborator().equals(collaboratorToRemoveFromProjectTeam)) {

				other.setState(false);

				for (Task otherTask : this.taskRepository.getAllTasksFromProjectCollaborator(other)) {
					otherTask.removeProjectCollaboratorFromTask(other);
				}
				remove = true;
			}

		}
		return remove;
	}

	/**
	 * This method retrieves the Project Collaborator in a ProjectTeam from the
	 * User. If there are more than one Project Collaborator corresponding to the
	 * same user, that information is not collected in this method.
	 * 
	 * @param collaborator
	 * @return
	 */
	public ProjectCollaborator findProjectCollaborator(User collaborator) {

		for (ProjectCollaborator other : projectTeam) {
			if (other.getUserFromProjectCollaborator().equals(collaborator)) {
				return other;
			}
		}
		return null;
	}

	/**
	 * This method calculates the sum of the values reported to the task until the
	 * moment
	 * 
	 * @return The cost value reported for the Project until the moment
	 */

	public double getTotalCostReportedToProjectUntilNow() {
		double reportedCost = 0.0;

		for (Task task : taskRepository.getProjectTaskRepository()) {
			reportedCost += task.getTaskCost();
		}

		return reportedCost;
	}

	/**
	 * This method returns the name of the project in a String
	 * 
	 * @return The name of the project
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Creates a new assignment request, and adds the request to the list of pending
	 * task assignment requests if it isn't already created
	 * 
	 * @param request
	 *            Request to add to the pending task assignment requests list
	 * @return True if it adds, false if there is already an equal request
	 */
	public boolean createTaskAssignementRequest(ProjectCollaborator projCollab, Task task) {// uso de if incorreto?
		TaskTeamRequest newReq = new TaskTeamRequest(projCollab, task);
		return this.pendingTaskAssignementRequests.add(newReq);
	}

	/**
	 * Creates a new removal request, and adds the request to the list of pending
	 * task removal requests if it isn't already created
	 * 
	 * @param request
	 *            Request to add to the list of pending task removal requests
	 * @return True if it adds, false if there is already an equal request
	 */
	public boolean createTaskRemovalRequest(ProjectCollaborator projCollab, Task task) {
		TaskTeamRequest newReq = new TaskTeamRequest(projCollab, task);
		if (!this.isRemovalRequestAlreadyCreated(projCollab, task)) {
			return this.pendingTaskRemovalRequests.add(newReq);
		}
		return false;
	}

	/**
	 * Removes request to add a certain project collaborator to a specific task
	 * team.
	 * 
	 * @param request
	 *            Request to remove from the list
	 */

	public void deleteTaskAssignementRequest(TaskTeamRequest request) {
		this.pendingTaskAssignementRequests.remove(request);
	}

	/**
	 * Removes the removal request of a certain project collaborator to a specific
	 * task team.
	 * 
	 * @param request
	 *            TaskTeamRequest to remove from the list
	 */

	public boolean deleteTaskRemovalRequest(ProjectCollaborator projCollab, Task task) {
		TaskTeamRequest request = getRemovalTaskTeamRequest(projCollab, task);
		return this.pendingTaskRemovalRequests.remove(request);
	}

	/**
	 * Returns the relevant info in the form of a list of strings
	 * 
	 * @return toString List of strings which contains the info about the task and
	 *         the project collaborator for each request
	 */
	public ArrayList<String> viewPendingTaskAssignementRequests() {// sera melhor com DTO?
		ArrayList<String> toString = new ArrayList<>();
		for (TaskTeamRequest req : this.pendingTaskAssignementRequests) {
			toString.add(req.viewStringRepresentation());
		}
		return toString;
	}

	/**
	 * Returns the relevant info in the form of a list of strings of the pending
	 * task removal requests
	 * 
	 * @return toString List of strings which contains the info about the task and
	 *         the project collaborator for each request
	 */
	public ArrayList<String> viewPendingTaskRemovalRequests() {
		ArrayList<String> toString = new ArrayList<>();
		for (TaskTeamRequest req : this.pendingTaskRemovalRequests) {
			toString.add(req.getProjCollab().getUserFromProjectCollaborator().getName() + "\n"
					+ req.getProjCollab().getUserFromProjectCollaborator().getEmail() + "\n"
					+ req.getTask().getTaskID() + "\n" + req.getTask().getDescription());
		}
		return toString;
	}

	/**
	 * This method receives a Project Collaborator and a Task, creates a new
	 * TaskTeamRequest with those objects and searches if there's a removal request
	 * equal to the created one, in the pending removal requests list.
	 * 
	 * @param projCollaborator
	 *            Project Collaborator to create the request
	 * @param task
	 *            Task to create the request
	 * 
	 * @return Returns the removal request within the list if it exists or NULL if
	 *         it does not
	 */
	public TaskTeamRequest getRemovalTaskTeamRequest(ProjectCollaborator projCollaborator, Task task) {

		TaskTeamRequest removalRequestToFind = new TaskTeamRequest(projCollaborator, task);
		for (TaskTeamRequest other : this.pendingTaskRemovalRequests) {
			if (removalRequestToFind.equals(other)) {
				return other;
			}
		}

		return null;
	}

	/**
	 * Returns the list of Task assigment requests by collaborators, to be handled
	 * by the model or controller
	 * 
	 * @return List of TaskTeamRequest Objects from all users asking to be assigned
	 *         to a certain task
	 */

	public List<TaskTeamRequest> getAssignmentRequestsList() {
		return this.pendingTaskAssignementRequests;
	}

	// Do we use this method give the Removal requests to the controller, or
	// create a method in Project that handles the approvals/rejections by receiving
	// index numbers from the controller?
	/**
	 * Returns the list of Task removal requests by collaborators, to be handled by
	 * the model or controller
	 * 
	 * @return List of TaskTeamRequest Objects from all users asking to be assigned
	 *         to a certain task
	 */

	public List<TaskTeamRequest> getRemovalRequestsList() {
		return this.pendingTaskRemovalRequests;
	}

	/**
	 * Checks if a certain request already exists
	 * 
	 * @param projCollab
	 *            Projector collaborator that wants to create the request
	 * @param task
	 *            Task chosen by the project collaborator
	 * @return True if request already exists, false if not
	 */
	public boolean isAssignementRequestAlreadyCreated(ProjectCollaborator projCollab, Task task) {
		TaskTeamRequest request = new TaskTeamRequest(projCollab, task);
		return this.pendingTaskAssignementRequests.contains(request);
	}

	/**
	 * Checks if a certain request already exists
	 * 
	 * @param projCollab
	 *            Projector collaborator that wants to create the request
	 * @param task
	 *            Task chosen by the project collaborator
	 * @return True if request already exists, false if not
	 */
	public boolean isRemovalRequestAlreadyCreated(ProjectCollaborator projCollab, Task task) {
		TaskTeamRequest request = new TaskTeamRequest(projCollab, task);
		return this.pendingTaskRemovalRequests.contains(request);
	}

	/**
	 * Gets the request associated with the project collaborator and task provided
	 * 
	 * @param projCollaborator
	 *            Project Collaborator to search
	 * @param task
	 *            Task to search
	 * @return The request associated with the data provided, if it exists, else
	 *         return null.
	 */
	public TaskTeamRequest getAssignementTaskTeamRequest(ProjectCollaborator projCollaborator, Task task) {
		TaskTeamRequest result = null;
		TaskTeamRequest assignementRequestToFind = new TaskTeamRequest(projCollaborator, task);
		for (TaskTeamRequest other : this.pendingTaskAssignementRequests) {
			if (assignementRequestToFind.equals(other)) {
				result = other;
			}
		}
		return result;
	}

	/**
	 * Searches the assignement request list for the task selected. If it finds any
	 * request with this task, removes it from the list.
	 * 
	 * @param task
	 *            Task to remove from the assignement request list
	 */
	public void removeAllRequestsWithASpecificTaskFromAssignementRequests(Task task) {
		List<TaskTeamRequest> assignementCopy = new ArrayList<>();
		assignementCopy.addAll(this.pendingTaskAssignementRequests);
		for (int i = assignementCopy.size() - 1; i >= 0; i--) {
			if (assignementCopy.get(i).getTask().equals(task)) {
				this.pendingTaskAssignementRequests.remove(this.pendingTaskAssignementRequests.get(i));
			}
		}
	}

	/**
	 * Searches the removal request list for the task selected. If it finds any
	 * request with this task, removes it from the list.
	 * 
	 * @param task
	 *            Task to remove from the removal request list
	 */
	public void removeAllRequestsWithASpecificTaskFromRemovalRequests(Task task) {
		List<TaskTeamRequest> removalCopy = new ArrayList<>();
		removalCopy.addAll(this.pendingTaskRemovalRequests);
		for (int i = removalCopy.size() - 1; i >= 0; i--) {
			if (removalCopy.get(i).getTask().equals(task)) {
				this.pendingTaskRemovalRequests.remove(this.pendingTaskRemovalRequests.get(i));
			}
		}
	}

	/**
	 * Searches both request lists for the task selected. If it finds any request
	 * with this task, removes it from the list.
	 * 
	 * @param task
	 *            Task to remove from the request lists
	 */
	public void removeAllRequestsWithASpecificTask(Task task) {
		this.removeAllRequestsWithASpecificTaskFromAssignementRequests(task);
		this.removeAllRequestsWithASpecificTaskFromRemovalRequests(task);
	}

	public ProjectCollaborator getProjectCollaboratorFromUser(User user) {

		for (ProjectCollaborator other : projectTeam) {
			if (other.getUserFromProjectCollaborator().equals(user)) {
				return other;
			}
		}
		return null;

	}

	/**
	 * @return the pendingTaskAssignementRequests
	 */
	public List<TaskTeamRequest> getPendingTaskAssignementRequests() {
		return pendingTaskAssignementRequests;
	}

	/**
	 * @return the pendingTaskRemovalRequests
	 */
	public List<TaskTeamRequest> getPendingTaskRemovalRequests() {
		return pendingTaskRemovalRequests;
	}

	/**
	 * This method returns the List of Collaborators from a specific task
	 * 
	 * @return Returns a list with the task team
	 */
	public ArrayList<ProjectCollaborator> getProjectCollaboratorsFromTask(Task task) {

		ArrayList<ProjectCollaborator> collaboratorsFromTask = new ArrayList<>();
		for (ProjectCollaborator other : this.getActiveProjectTeam()) {
			if (task.isProjectCollaboratorActiveInTaskTeam(other)) {
				collaboratorsFromTask.add(other);

			}
		}
		return collaboratorsFromTask;
	}

}