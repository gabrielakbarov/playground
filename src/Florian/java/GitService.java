import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import io.github.cdimascio.dotenv.Dotenv;

public class GitService {

    static Dotenv dotenv = Dotenv.load();

    public static final String GITHUB_USER = dotenv.get("GITHUB_USER");
    public static final String GITHUB_TOKEN = dotenv.get("GITHUB_TOKEN");
    private static final String REPO_URL = dotenv.get("GITHUB_REPO_URL");

    private static final File REPO_DIR = new File("repo");

    public static void main(String[] args) {
        var credentials = new UsernamePasswordCredentialsProvider(GITHUB_USER, GITHUB_TOKEN);

        try (Git git = initRepository(credentials)) {

//            // Optional: vor Änderungen synchronisieren, danach FORCE-PUSH!!
//            git.pull().setCredentialsProvider(credentials).call();

            Path file = REPO_DIR.toPath().resolve("test.txt");

            Files.writeString(
                    file,
                    "Update\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            git.add().addFilepattern("test.txt").call();

            git.commit()
                    .setMessage("Automatischer Commit")
                    .call();

            git.push()
                    .setCredentialsProvider(credentials)
                    .call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Git initRepository(
            UsernamePasswordCredentialsProvider credentials) throws Exception {

        if (REPO_DIR.exists()) {
            return Git.open(REPO_DIR);
        }

        return Git.cloneRepository()
                .setURI(REPO_URL)
                .setDirectory(REPO_DIR)
                .setCredentialsProvider(credentials)
                .call();
    }
}
