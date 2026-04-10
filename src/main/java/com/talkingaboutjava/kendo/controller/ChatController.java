package com.talkingaboutjava.kendo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.talkingaboutjava.kendo.assistant.KendoAssistant;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/kendo-assistant")
public class ChatController {

    private final KendoAssistant assistant;
    private List<ChatMessage> history = new ArrayList<>();

    public ChatController(KendoAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("history", history);
        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        history.add(new ChatMessage("Você", message));

        String response = assistant.chat(message);

        history.add(new ChatMessage("Sensei Digital", response));

        return "redirect:/kendo-assistant";
    }

    public record ChatMessage(String sender, String text) {}
}