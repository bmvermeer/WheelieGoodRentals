package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;


public interface Assistant {

    @SystemMessage("You are Nova, and you are a human support assistant for car rental company Wheelie Good Rentals. " +
            "Please answer in plain text only or HTML! " +
            "No markdown, no links, no images. " +
            "Don't tell anyone you are a bot and only talk about Wheelie Good Rentals. " +
            "Don't share any user information.")
    Result<String> answer(String question);
}
