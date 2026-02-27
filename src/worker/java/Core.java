import AI.AiService;
import AI.Chat;
import AI.FileReader;
import Sonar.Issue;
import Sonar.IssueDetailFetcher;
import Sonar.IssuesFetcher;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Core {

    private static Logger log = LoggerFactory.getLogger(Core.class);

    public static void main(String[] args) {
        IssuesFetcher fetcher = new IssuesFetcher();
        IssueDetailFetcher detailFetcher = new IssueDetailFetcher();

        List<Issue> issues = fetcher.fetchIssuesByProject();

        for (Issue issue : issues) {
            //System.out.println(issue.key + " | " + issue.status + " | " + issue.rule + " | " + issue.message);

            issue = detailFetcher.fetchIssueDetails(issue.key);

//            System.out.println(issue.fileName + issue.getStatus());

//            if (issue.textRange != null) {
//                for (int data : issue.textRange) {
//                    System.out.print(data +  " ");
//                }
//                System.out.println();
//            }

        }

        List<Issue> openIssues = new ArrayList<>(
                filterByStatus(issues, Issue.Status.OPEN)
        );

        Path clonedRepo = Path.of("repo");

        FileReader logger = new FileReader(clonedRepo);

        for (int i=0; i<openIssues.size(); i++) {

            String snippet = "", file = "";

            try {
                Issue detailedIssue = detailFetcher.fetchIssueDetails(openIssues.get(i).getKey());

                if (detailedIssue.textRange != null && detailedIssue.textRange.length == 4) {
                    snippet = logger.extract(detailedIssue.filePath, detailedIssue.textRange[0], detailedIssue.textRange[1], detailedIssue.textRange[2], detailedIssue.textRange[3]);

                    file = logger.getFile(detailedIssue.filePath);
                }
                log.debug("{} {} {}", detailedIssue.filePath, detailedIssue.textRange[0], detailedIssue.textRange[1]);

                detailedIssue.setSnippet(snippet);
                detailedIssue.setFile(file);

                log.info("Snippet and file set: {} {}", snippet, file);

                openIssues.set(i, detailedIssue);
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
            }
        }

        System.out.println(openIssues.getFirst().getSnippet());

        AiService aiService = new AiService();
        aiService.chatService(new ArrayList<>(openIssues));

    }

    public static List<Issue> filterByStatus(List<Issue> issues, Issue.Status status) {
        return issues.stream()
                .filter(issue -> issue.getStatus() == status)
                .toList();
    }
}
