package AI;

import Sonar.Issue;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private final Chat chat =
            new Chat(dotenv.get("AI_API_KEY"), dotenv.get("AI_BASEURL"));

    public Map<String, String> chatService(List<Issue> issues) {

        if (issues == null || issues.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> correctedFiles = new HashMap<>();

        Map<String, List<Issue>> issuesByFile =
                issues.stream()
                        .collect(Collectors.groupingBy(
                                issue -> Optional.ofNullable(issue.getFilePath())
                                        .orElse("UNKNOWN_FILE")
                        ));

        for (Map.Entry<String, List<Issue>> entry : issuesByFile.entrySet()) {

            String filePath = entry.getKey();
            List<Issue> fileIssues = entry.getValue();

            try {
                log.info("Processing file {} with {} issues",
                        filePath, fileIssues.size());

                String fullFile = fileIssues.getFirst().getFile();

                String prompt = Config.buildMultiIssuePrompt(fullFile, fileIssues);

                String correctedFile = chat.chat(prompt);

                if (correctedFile != null) {
                    correctedFile = correctedFile.replaceAll(
                            "(?s)^```java\\s*|\\s*```$",
                            ""
                    );
                }

                correctedFiles.put(filePath, correctedFile);

                log.info("AI returned updated version for {}", filePath);

            } catch (Exception e) {
                log.error("Error processing file {}: {}", filePath, e.getMessage());
            }
        }

        return correctedFiles;
    }
}