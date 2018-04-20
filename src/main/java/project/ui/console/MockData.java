package project.ui.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.model.Profile;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.User;
import project.services.ProjectService;
import project.services.UserService;

@Component
public class MockData {



    private UserService userService;
    private ProjectService projectService;

    private MockData() {

    }

    @Autowired
    public MockData(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }


    /**
     * Instantiates data and saves it to the database
     */
    public void instantiateMockData() {
        
        String street = "Avenida exemplo"; 
        String zipCode = "3700-000";
        String country = "country"; 
        String city = "Porto";

    // Instantiate the users, sets their passwords
    User userAdmin = userService.createUser("Teresa Ribeiro", "admin@gmail.com", "001",
            "Administrator", "917653635", street, zipCode, city, city, country);
		userAdmin.setPassword("123456");
		userAdmin.setQuestion("1");
        userAdmin.setAnswer("Kiko");

    User userDirector = userService.createUser("Roberto Santos", "director@gmail.com", "002",
            "Director", "917653636", street, zipCode, city, city, country);
		userDirector.setPassword("abcdef");
		userAdmin.setQuestion("2");
        userAdmin.setAnswer("School of Rock");

    User userJMatos = userService.createUser("Jorge Matos", "jmatos@gmail.com", "010", "Comercial",
            "937653635", street, zipCode, city, city, country);
		userJMatos.setPassword("switch");
		userAdmin.setQuestion("3");
        userAdmin.setAnswer("My Home");

    User userATirapicos = userService.createUser("Andreia Tirapicos", "atirapicos@gmail.com", "011",
            "Comercial", "955553635", street, "4455-654", "Leca da Palmeira", city, country);
		userATirapicos.setPassword("tirapicos");
		userAdmin.setQuestion("1");
        userAdmin.setAnswer("Rocky, the rock");

    User projectManager = userService.createUser("Sara Pereira", "spereira@gmail.com", "012",
            "Técnica de recursos humanos", "9333333", street, "4455-666", "Leca da Palmeira", city,
            country);
		projectManager.setPassword("manager");
		userAdmin.setQuestion("2");
        userAdmin.setAnswer("South Lake, Utah");

    Project project = projectService.createProject("Projecto Sprint 2", "Demo para a sprint", projectManager);
    ProjectCollaborator projectCollaborator = project.createProjectCollaborator(userATirapicos, 10);
		projectService.addProjectCollaborator(projectCollaborator);
		projectService.updateProject(project);


		userDirector.setUserProfile(Profile.DIRECTOR);
		userJMatos.setUserProfile(Profile.COLLABORATOR);
		userATirapicos.setUserProfile(Profile.COLLABORATOR);
		projectManager.setUserProfile(Profile.COLLABORATOR);


		userService.addUserToUserRepositoryX(userAdmin);
		userService.addUserToUserRepositoryX(userDirector);
		userService.addUserToUserRepositoryX(userJMatos);
		userService.addUserToUserRepositoryX(userATirapicos);
		userService.addUserToUserRepositoryX(projectManager);

    }
}