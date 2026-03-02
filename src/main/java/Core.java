import AI.AiService;
import AI.FileReader;
import Git.GitService;
import Sonar.Issue;
import Sonar.IssueDetailFetcher;
import Sonar.IssuesFetcher;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Core {

    private static final Logger log = LoggerFactory.getLogger(Core.class);

    public static void main(String[] args) {
        long start = System.nanoTime();

        IssuesFetcher fetcher = new IssuesFetcher();
        IssueDetailFetcher detailFetcher = new IssueDetailFetcher();

        List<Issue> issues = fetcher.fetchIssuesByProject();

        List<Issue> openIssues = new ArrayList<>(
                filterByStatus(issues, Issue.Status.OPEN)
        );

        GitService gitService;

        try {
            gitService = new GitService();
        } catch (Exception e) {
            log.error("Error initializing repository.");
            throw new RuntimeException(e);
        }

        Path clonedRepo = Path.of("repo");

        FileReader logger = new FileReader(clonedRepo);

        for (int i=0; i<openIssues.size(); i++) {

            String snippet = "", file = "";

            try {
                Issue detailedIssue = detailFetcher.fetchIssueDetails(openIssues.get(i).getKey());

                try {
                    file = logger.getFile(detailedIssue.filePath);
                } catch (Exception e) {
                    log.error("Failed to get file for {}: {}", detailedIssue.filePath, e.getMessage(), e);
                }

                if (detailedIssue.textRange != null && detailedIssue.textRange.length == 4) {
                    try {
                        snippet = logger.extract(
                                detailedIssue.filePath,
                                detailedIssue.textRange[0],
                                detailedIssue.textRange[1],
                                detailedIssue.textRange[2],
                                detailedIssue.textRange[3]
                        );
                    } catch (Exception e) {
                        log.error("Failed to extract snippet for {}: {}", detailedIssue.filePath, e.getMessage(), e);
                    }
                } else {
                    log.info("No valid textRange for {}, skipping snippet extraction", detailedIssue.filePath);
                }

                detailedIssue.setSnippet(snippet);
                detailedIssue.setFile(file);

                log.info("Snippet and file set: {} {}", snippet, file);

                openIssues.set(i, detailedIssue);
            } catch (Exception e) {
                log.error("Error2: {}", e.getMessage());
            }
        }


        AiService aiService = new AiService();

        long aiStart = System.nanoTime();
        Map<String, String> correctedFiles = aiService.chatService(new ArrayList<>(openIssues));
        long aiEnd = System.nanoTime();

        log.info(correctedFiles.toString());


        try {
            gitService.commitAndPush(correctedFiles, "Commit aus Workflow " + OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        } catch (Exception e) {
            log.error("Error pushing changes.");
            throw new RuntimeException(e);
        }

        long end = System.nanoTime();
        long duration = end - start;
        double durationSeconds = duration / 1_000_000_000.0;

        long aiDuration = aiEnd - aiStart;
        double aiDurationSeconds = aiDuration / 1_000_000_000.0;

        log.info("Gesamtdauer: " + durationSeconds + ", davon Warten auf KI-Antwort: " + aiDurationSeconds);

    }

    public static List<Issue> filterByStatus(List<Issue> issues, Issue.Status status) {
        return issues.stream()
                .filter(issue -> issue.getStatus() == status)
                .toList();
    }
}
