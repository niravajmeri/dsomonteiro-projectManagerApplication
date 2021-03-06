package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.User;
import project.repository.ProjCollabRepository;
import project.repository.ProjectsRepository;
import project.services.exceptions.ObjectNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {


	private ProjectsRepository projectsRepository;
	private UserService userService;
	private ProjCollabRepository projectCollaboratorRepository;

	/**
	 * Constructor that allows one to create a new Project repository. There are no
	 * mandatory fields.
	 */
	public ProjectService() {
	}

	/**
	 * Constructor created for JPA purposes. It is not to be used in model context.
	 */
	@Autowired
	public ProjectService(ProjectsRepository projectsRepository, ProjCollabRepository projectCollabRepository, UserService userService) {
		this.projectsRepository = projectsRepository;
		this.projectCollaboratorRepository = projectCollabRepository;
		this.userService = userService;
	}

	/**
	 * Creates an instance of Project and saves it in the database
	 * 
	 * @param name
	 *            name to be given to the Project
	 * @param description
	 *            description to be given to the Project
	 * @param projectManager
	 *            what user will be this Project's manager
	 * 
	 * @return the project created
	 */
	public Project createProject(String name, String description, User projectManager) {

		Project newProject = new Project(name, description, projectManager);
		this.projectsRepository.save(newProject);
		return newProject;

	}

	/**
	 * Gets a project from the database by searching with the id
	 *
	 * @param id
	 *            id to search for a specific project
	 *
	 * @return project found
	 */
	public Project getProjectById(Integer id) {
		Optional<Project> project = this.projectsRepository.findByProjectId(id);

		//it returns an optional object and if not, it throws the exception ObjectNotFoundException
		return project.orElseThrow(() -> new ObjectNotFoundException("Project not found! Id: " + id));
	}

	/**
	 * This method tells the ProjectRepository to save a certain project in the
	 * database
	 * 
	 * @param project
	 *            Project added to the List of Projects
	 */
	public void addProjectToProjectContainer(Project project) {
		this.projectsRepository.save(project);
	}

	/**
	 * This method tells the projectRepository to call the method findAll in order
	 * to retrieve all projects from the database
	 * 
	 * @return allProjects This is a copy of he list of all Projects created
	 */
	public List<Project> getAllProjectsfromProjectsContainer() {

		List<Project> allProjects = new ArrayList<>();
		allProjects.addAll(this.projectsRepository.findAll());
		return allProjects;
	}

	/**
	 * Tells the projectRepository to call the method findByProjectStatus for each
	 * of the status that is considered as "Active" . ActiveStates 1 to 4
	 * 
	 * @return activeProjectsList The List of active Projects
	 */
	public List<Project> getActiveProjects() {

		List<Project> activeProjects = new ArrayList<>();

		for (Project project : getAllProjectsfromProjectsContainer()) {
			if (project.isProjectActive()) {
				activeProjects.add(project);
			}
		}
		return activeProjects;
	}

	/**
	 * Checks if the a project already exists
	 * 
	 * @return TRUE if the Project is in this project repository FALSE if not
	 */
	public boolean isProjectInProjectContainer(int projectId) {

		return this.projectsRepository.existsById(projectId);
	}

	/**
	 * This method returns a set of Projects where a certain user
	 *
	 * *
	 * 
	 * @param user
	 *            user whose projects are going to be found
	 * 
	 * @return List of Projects of a User
	 * 
	 */
	public List<Project> getProjectsFromUser(User user) {

		List<Project> projects = new ArrayList<>();

		// Compares de projectId of the projectCollaborator to the project id of the
		// projects in the database
        for(Project project : this.getAllProjectsfromProjectsContainer()) {
            if(project.isProjectManager(user) || this.isUserActiveInProject(user, project)) {
                projects.add(project);
            }
        }

		return projects;
	}

	/**
	 * This method returns a set of Projects where a certain user is the project
	 * manager
	 * 
	 * @param user
	 *            user for which to find the projects where it is the project
	 *            manager
	 * 
	 * @return List of Projects of a User
	 * 
	 */
	public List<Project> getProjectsFromProjectManager(User user) {

		List<Project> projectsOfPM = new ArrayList<>();
		projectsOfPM.addAll(this.projectsRepository.findAllByProjectManager(user));

		return projectsOfPM;
	}

	/**
	 * Thid method tells the projectRepository to call the method save, in order to
	 * update a certain project
	 *
	 * @param project
	 *            Project to update
	 */
	public void updateProject(Project project) {
		this.projectsRepository.save(project);
	}

	/**
	 * Calls the projectCollaboratorRepository to save the projectCollaborator to
	 * the database The projectCollaborator is created by the Project
	 *
	 * @param projectCollaborator
	 *            ProjectCollaborator to save
	 */
	public void addProjectCollaborator(ProjectCollaborator projectCollaborator) {

		this.projectCollaboratorRepository.save(projectCollaborator);
	}

	/**
	 * Creates an instance of ProjectCollaborator set the id of the project and
	 * saves projectCollaborator in the database
	 * 
	 * @param user
	 *            user to associate with projectCollaborator
	 * @param project
	 *            to set project id in projectCollaborator
	 * @param costPerEffort
	 * 
	 * @return the projectCollaborator created
	 */
	public ProjectCollaborator createProjectCollaborator(User user, Project project, double costPerEffort) {

		ProjectCollaborator newProjectCollaborator = new ProjectCollaborator(user, costPerEffort);
		newProjectCollaborator.setProject(project);
		this.projectCollaboratorRepository.save(newProjectCollaborator);
		return newProjectCollaborator;

	}

	/**
	 * Get the active users inside this Project's Team
	 *
	 * @return Project Team - Active Team of the Project
	 */
	public List<ProjectCollaborator> getActiveProjectTeam(Project project) {
		List<ProjectCollaborator> activeCollaborators = new ArrayList<>();

		for (ProjectCollaborator other : this.projectCollaboratorRepository.findAllByProject(project)) {
			if (other.isProjectCollaboratorActive())
				activeCollaborators.add(other);
		}

		return activeCollaborators;
	}

	/**
	 * Returns a list with all project collaborators from a certain project
	 *
	 * @param project
	 *            Project to get project collaborators from
	 *
	 * @return List of Project Collaborators of a certian project
	 */
	public List<ProjectCollaborator> getProjectTeam(Project project) {
		List<ProjectCollaborator> projectTeam = new ArrayList<>();
		projectTeam.addAll(this.projectCollaboratorRepository.findAllByProject(project));

		return projectTeam;
	}

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
	public boolean isUserInProjectTeam(User user, Project project) {
		List<ProjectCollaborator> projectTeam = new ArrayList<>();
		projectTeam.addAll(this.projectCollaboratorRepository.findAllByProject(project));

		for (ProjectCollaborator other : projectTeam) {
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
	public boolean isUserActiveInProject(User user, Project project) {
		List<ProjectCollaborator> projectTeam = new ArrayList<>();
		projectTeam.addAll(this.projectCollaboratorRepository.findAllByProject(project));

		for (ProjectCollaborator other : projectTeam) {
			if (user.equals(other.getUserFromProjectCollaborator()) && other.isProjectCollaboratorActive()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method retrieves the Project Collaborator in a ProjectTeam from the
	 * User. If there are more than one Project Collaborator corresponding to the
	 * same user, that information is not collected in this method.
	 *
	 * @param collaborator
	 * @return Optional of a ProjectCollaborator
	 */
	public Optional<ProjectCollaborator> findProjectCollaborator(User collaborator, Project project) {

		List<ProjectCollaborator> projectTeam = new ArrayList<>();
		projectTeam.addAll(this.projectCollaboratorRepository.findAllByProject(project));

		return projectTeam.stream().filter(projCollab -> projCollab.getCollaborator().equals(collaborator)).findFirst();
	}

	/**
	 * This method retrieves the Project Collaborator in a ProjectTeam from the
	 * User. If there are more than one Project Collaborator corresponding to the
	 * same user, that information is not collected in this method.
	 *
	 * @param collaborator
	 * @return Optional of a ProjectCollaborator
	 */
	public ProjectCollaborator findActiveProjectCollaborator(User collaborator, Project project) {

		List<ProjectCollaborator> projectTeam = new ArrayList<>();
		projectTeam.addAll(this.projectCollaboratorRepository.findAllByProject(project).stream()
                .filter(projectCollaborator -> projectCollaborator.getUserFromProjectCollaborator().equals(collaborator))
                .collect(Collectors.toList()));

		for (ProjectCollaborator toSearch : projectTeam) {
			if(toSearch.getStatus()) {
				return toSearch;
			}
		}
		return null;
	}


	/**
	 * This method updates a ProjectCollaborator
	 *
	 * @param projCollab Project Collaborator to update
	 */
	public void updateProjectCollaborator(ProjectCollaborator projCollab) {
		this.projectCollaboratorRepository.save(projCollab);
	}

	public void setProjectsRepository(ProjectsRepository projectsRepository) {
		this.projectsRepository = projectsRepository;
	}

	public void setProjectCollaboratorRepository(ProjCollabRepository projectCollaboratorRepository) {
		this.projectCollaboratorRepository = projectCollaboratorRepository;
	}

	/**
	 *This method upDate project from a given info.
     *
     * If the project's calculation cost is set to
	 *
	 * @param projectUpdates
	 */
	public void updateProjectData(Project projectUpdates, Project project){

		if(!StringUtils.isEmpty(projectUpdates.getName())) {
			project.setName(projectUpdates.getName());
		}

		if(!StringUtils.isEmpty(projectUpdates.getDescription())) {
			project.setDescription(projectUpdates.getDescription());
		}

		if((projectUpdates.getStartdate() != null)) {
			project.setStartdate(projectUpdates.getStartdate());
		}

		if((projectUpdates.getFinishdate() != null)) {
			project.setFinishdate(projectUpdates.getFinishdate());
		}

        if((projectUpdates.getBudget() > 0)) {
            project.setBudget((int) projectUpdates.getBudget());
        }

		if((projectUpdates.getProjectManager() != null)) {
			User user = userService.getUserByEmail(projectUpdates.getProjectManager().getEmail());
			project.setProjectManager(user);
		}

		if((projectUpdates.getAvailableCalculationMethods()!=null)) {
		    project.setAvailableCalculationMethods(projectUpdates.getAvailableCalculationMethods());
        }

		if(projectUpdates.getCalculationMethod()!= null && project.isCalculationMethodAllowed(projectUpdates.getCalculationMethod().getCode())){
			project.setCalculationMethod(projectUpdates.getCalculationMethod());
		}

        if(!project.isCalculationMethodAllowed(project.getCalculationMethod().getCode())) {
		    project.setCalculationMethod(project.listAvaliableCalculationMethods().get(0));
        }

		updateProject(project);
	}

	/**
	 * Gets a project Collaborator by its id
	 *
	 * @param id Id to search
	 *
	 * @return ProjectCollaborator if found. Throws ObjectNotFoundException if not found
	 */
	public ProjectCollaborator getProjectCollaboratorById(long id) {

		Optional<ProjectCollaborator> collaborator = this.projectCollaboratorRepository.findByProjectCollaboratorId(id);
		return collaborator.orElseThrow(() -> new ObjectNotFoundException("Project Collaborator not found! Id: " + id));
	}

	/**
	 * Creates an instance of ProjectCollaborator set the id of the project and
	 * saves projectCollaborator in the database
	 *
	 * @param email
	 *            user to associate with projectCollaborator
	 * @param projectId
	 *            to set project id in projectCollaborator
	 * @param costPerEffort
	 *
	 * @return the projectCollaborator created
	 */
	public ProjectCollaborator createProjectCollaboratorWithEmail(String email, int projectId, double costPerEffort) {

		User user = this.userService.getUserByEmail(email);
		Project project = this.getProjectById(projectId);
		ProjectCollaborator newProjectCollaborator = new ProjectCollaborator(user, costPerEffort);
		newProjectCollaborator.setProject(project);
		this.projectCollaboratorRepository.save(newProjectCollaborator);
		return newProjectCollaborator;

	}

	public ProjectsRepository getProjectsRepository() {
		return this.projectsRepository;
	}

	public ProjCollabRepository getProjectCollaboratorRepository() {
		return projectCollaboratorRepository;
	}
}
