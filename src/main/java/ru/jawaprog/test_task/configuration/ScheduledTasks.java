package ru.jawaprog.test_task.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.jawaprog.test_task.repositories.entities.ClientsDTO;
import ru.jawaprog.test_task.services.ClientsService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class ScheduledTasks {
    @Autowired
    private ClientsService clientsService;

    @Scheduled(cron = "* */5 * * * *")
    public void saveClientsBackup() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/backup.xml"));
                ClientsDTO cl = new ClientsDTO();
                cl.setClient(clientsService.findAllSoap());
                JAXBContext context = JAXBContext.newInstance(ClientsDTO.class);
                Marshaller m = context.createMarshaller();
                m.marshal(cl, bw);
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


