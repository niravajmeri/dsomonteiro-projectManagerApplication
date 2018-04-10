package project.ui.console.loadfiles.loaduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoadUserFactory {

    @Autowired
    LoadUserXml loadUserXml;

    private static final Map<String, LoadUser> fileExtensions = new HashMap<>();

    @PostConstruct
    public void initFileExtensions() {
        fileExtensions.put("XML", loadUserXml);
    }

    public LoadUser getLoadUserType(String filePath) throws InstantiationException, IllegalAccessException {

        String fileExtension = filePath.split("\\.")[1].trim().toUpperCase();

        return fileExtensions.get(fileExtension);
    }
}
