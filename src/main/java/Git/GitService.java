package Git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GitService {
    private static Logger log = LoggerFactory.getLogger(GitService.class);

    static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static final String GITHUB_USER = dotenv.get("GH_USER");
    private static final String GITHUB_TOKEN = dotenv.get("GH_TOKEN");
    private static final String REPO_URL = dotenv.get("GH_REPO_URL");
    private static final File REPO_DIR = new File("repo");

    private final UsernamePasswordCredentialsProvider credentials =
            new UsernamePasswordCredentialsProvider(GITHUB_USER, GITHUB_TOKEN);

    private Git git;

    public GitService() throws Exception {
        this.git = initRepository();
    }

    private Git initRepository() throws Exception {
        if (REPO_DIR.exists()) {
            return Git.open(REPO_DIR);
        }
        return Git.cloneRepository()
                .setURI(REPO_URL)
                .setDirectory(REPO_DIR)
                .setCredentialsProvider(credentials)
                .call();
    }

    public void commitAndPush(Map<String, String> correctedFiles, String commitMessage) throws Exception {
        String timestamp = OffsetDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

        git.checkout()
                .setCreateBranch(true)
                .setName("proposal/" + timestamp)
                .call();


        for (Map.Entry<String, String> entry : correctedFiles.entrySet()) {
            Path filePath = REPO_DIR.toPath().resolve(entry.getKey());
            Files.createDirectories(filePath.getParent());

            String oldContent = Files.exists(filePath) ? Files.readString(filePath) : "";
            String newContent = entry.getValue();

            log.info("File: {} oldLength={} newLength={}", entry.getKey(), oldContent.length(), newContent.length());

            if (!oldContent.equals(newContent)) {
                Files.writeString(filePath, newContent);
                git.add().addFilepattern(entry.getKey()).call();
                log.info("File {} updated and staged.", entry.getKey());
            } else {
                log.info("File {} unchanged, skipping.", entry.getKey());
            }
        }

        git.commit().setMessage(commitMessage).call();
        log.info("Committed changes to git.");
        git.push().setCredentialsProvider(credentials).call();
        log.info("Pushed changes to git.");


        String repoName = "badCodeExample";
        String baseBranch = "main";

        JSONObject prData = new JSONObject();
        prData.put("title", commitMessage);
        prData.put("head", "proposal/" + timestamp);
        prData.put("base", baseBranch);
        prData.put("body", "Auto-generated pull request from commitAndPushWithPR");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/" + GITHUB_USER + "/" + repoName + "/pulls"))
                .header("Authorization", "token " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .POST(HttpRequest.BodyPublishers.ofString(prData.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Pull Request creation response: {}", response.body());
    }
}