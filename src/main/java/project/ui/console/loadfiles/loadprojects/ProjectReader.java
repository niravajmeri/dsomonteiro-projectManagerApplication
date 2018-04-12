package project.ui.console.loadfiles.loadprojects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import project.model.ProjectCollaborator;
import project.services.ProjectService;
import project.services.TaskService;
import project.ui.console.loadfiles.loadprojects.LoadProject;
import project.ui.console.loadfiles.loadprojects.LoadProjectFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

@Service
public class ProjectReader {

    ProjectService projectService;

    TaskService taskService;

    LoadProjectFactory creator;

    private ProjectReader(){

    }

    @Autowired
    public ProjectReader(ProjectService projectService, TaskService taskService, LoadProjectFactory creator) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.creator = creator;
    }

    /**
     *  Instantiates a LoadUser strategy with the LoadUserFactory
     *
     * @param file file to read
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    public void readFile(String file) throws ParserConfigurationException, SAXException, IOException, ParseException{
        LoadProject reader = creator.getReader(file);
        reader.readProjectFile(file);
    }

}
