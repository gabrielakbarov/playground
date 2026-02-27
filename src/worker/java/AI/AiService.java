package AI;

import Sonar.Issue;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class AiService {
    Dotenv dotenv = Dotenv.load();

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final Chat chat = new Chat(dotenv.get("AI_API_KEY"), dotenv.get("AI_BASEURL"));


    public List<Issue> chatService(List<Issue> issues) {
        if (issues == null) return null;

        int count = 0;
        int maxChats = Config.AI_MAX_CHATS;

        Iterator<Issue> iterator = issues.iterator();

        while (iterator.hasNext()) {
            if (maxChats > -1 && count >= maxChats) break;

            Issue issue = iterator.next();

            try {
                log.info("Call AI for Issue: {}", issue.key);

                issue.setAiResponse(chat.chat(Config.buildDefaultPrompt(issue)));
                log.debug(Config.buildDefaultPrompt(issue));

                iterator.remove();
            } catch (Exception e) {
                log.error("Fehler bei der KI-Anfrage für Issue {}", issue.key, e);
            }
            count++;
        }

        return (issues);
    }
}
