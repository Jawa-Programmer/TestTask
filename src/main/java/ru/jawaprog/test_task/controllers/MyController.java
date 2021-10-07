package ru.jawaprog.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.jawaprog.test_task.entities.Client;
import ru.jawaprog.test_task.services.AccountsService;
import ru.jawaprog.test_task.services.ClientsService;
import ru.jawaprog.test_task.services.ContractsService;
import ru.jawaprog.test_task.services.PhoneNumbersService;

@Controller

public class MyController {
    @Autowired
    private AccountsService accountsService;
    @Autowired
    private ClientsService clientsService;
    @Autowired
    private ContractsService contractsService;
    @Autowired
    private PhoneNumbersService phoneNumbersService;


    @GetMapping("/test")
    public String test_menu(Model model) {
        model.addAttribute("client", new Client());
        return "test_menu";
    }

    @PostMapping("/test_proc")
    public String test_proc(@ModelAttribute Client client,
                            @RequestParam(value = "_method", required = true) String _method,
                            Model model) {
        switch (_method) {
            case "GET":
                return "redirect:/clients/" + client.getId() + '/';
            case "POST":
                Client toAdd = new Client();
                toAdd.setType(client.getType());
                toAdd.setFullName(client.getFullName());
                return "redirect:/clients/" + clientsService.save(toAdd).getId() + '/';
            case "PUT":
                return "redirect:/clients/" + clientsService.save(client).getId() + '/';
            case "DELETE":
                clientsService.delete(client.getId());
                break;
        }
        return "redirect:/test";
    }

    @GetMapping("/clients/{clientID}")
    public String getClient(Model model, @PathVariable long clientID) {
        model.addAttribute("client", clientsService.getClient(clientID));
        return "client";
    }
}
