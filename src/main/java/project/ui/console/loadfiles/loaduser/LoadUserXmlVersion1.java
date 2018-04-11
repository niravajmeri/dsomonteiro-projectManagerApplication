/**
 * 
 */
package project.ui.console.loadfiles.loaduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.model.Address;
import project.model.Profile;
import project.model.User;
import project.services.ProjectService;
import project.services.TaskService;
import project.services.UserService;
import project.ui.console.loadfiles.FileUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Group3
 *
 */
@Service
public class LoadUserXmlVersion1 implements LoadUserXmlVersion {
	ProjectService projectService;
	UserService userService;
	TaskService taskService;

	@Autowired
	public LoadUserXmlVersion1(ProjectService projectService, UserService userService, TaskService taskService){
		this.projectService = projectService;
		this.userService = userService;
		this.taskService = taskService;
	}

	public void usersReader(String pathFile) throws ParserConfigurationException, SAXException, IOException {

		Document documentUsers = FileUtils.readFromXmlFile(pathFile);

		NodeList nListUtilizadores = documentUsers.getElementsByTagName("utilizador");

		for (int i = 0; i < nListUtilizadores.getLength(); i++) {
			Node nNodeUtilizador = nListUtilizadores.item(i);

			if (nNodeUtilizador.getNodeType() == Node.ELEMENT_NODE) {
				Element eElementUtilizador = (Element) nNodeUtilizador;
				User eachUser = new User();
				eachUser.setName(eElementUtilizador.getElementsByTagName("nome_utilizador").item(0).getTextContent());
				eachUser.setEmail(eElementUtilizador.getElementsByTagName("email_utilizador").item(0).getTextContent());
				eachUser.setPassword(eElementUtilizador.getElementsByTagName("password").item(0).getTextContent());
				eachUser.setPhone(eElementUtilizador.getElementsByTagName("telefone").item(0).getTextContent());

				// sets users as Collaborators when they are created
				eachUser.setUserProfile(Profile.COLLABORATOR);

				eachUser.setIdNumber("XML ID");
				eachUser.setFunction("XML parser");

				// feeds DB with user state
				boolean systemUserStateActive = false;
				String active = eElementUtilizador.getElementsByTagName("estado_utilizador").item(0).getTextContent();
				if (active.equals("Ativo")){ systemUserStateActive = true;}
				eachUser.setSystemUserStateActive(systemUserStateActive);

				NodeList nList = (NodeList) eElementUtilizador.getElementsByTagName("lista_enderecos").item(0);

				for (int ii = 0; ii < nList.getLength(); ii++) {
					Node nNode = nList.item(ii);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						Address eachUserAddress = new Address();

						eachUserAddress.setStreet(eElement.getElementsByTagName("rua").item(0).getTextContent());
						eachUserAddress.setDistrict(eElement.getElementsByTagName("cp_loc").item(0).getTextContent());
						eachUserAddress.setZipCode(eElement.getElementsByTagName("cp_num").item(0).getTextContent());
						eachUserAddress.setCity(eElement.getElementsByTagName("localidade").item(0).getTextContent());
						eachUserAddress.setCountry(eElement.getElementsByTagName("pais").item(0).getTextContent());

						eachUserAddress.setUserInAddress(eachUser);
						eachUser.addAddress(eachUserAddress);

					}

				}
				userService.addUserToUserRepositoryX(eachUser);
			}

		}

	}

}