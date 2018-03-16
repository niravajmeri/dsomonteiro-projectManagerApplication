package project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.Project;
import project.model.ProjectCollaborator;
import project.model.User;

@Repository
public interface ProjCollabRepository extends JpaRepository<ProjectCollaborator, Integer>{

	/**
	 * Finds all projectCollaborators from a certain user
	 *
	 * @param user user to search
	 *
	 * @return a List of projectCollaborators from a certain user
	 */
	List<ProjectCollaborator> findAllByCollaborator(User user);


	/**
	 * Finds all projectCollaboators from a certain project
	 *
	 * @param project project to search
	 *
	 * @return List of Project Collaborators from a certain project
	 */
	List<ProjectCollaborator> findAllByProject(Project project);
	
}
