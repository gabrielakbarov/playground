package AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader {

    private static final Logger log = LoggerFactory.getLogger(FileReader.class);
    private final Path repositoryRoot;

    public FileReader(Path repositoryRoot) {
        this.repositoryRoot = repositoryRoot.toAbsolutePath().normalize();
    }

    private Path resolvePath(String filePath){
        String cleanPath = filePath.startsWith("/")
                ? filePath.substring(1)
                : filePath;

        Path fullPath = repositoryRoot.resolve(cleanPath).normalize();

        if (!fullPath.startsWith(repositoryRoot)) {
            throw new SecurityException("Path traversal detected");
        }

        return fullPath;
    }

    public String extract(
            String filePath,
            int startLine,
            int endLine,
            int startOffset,
            int endOffset
    ) throws IOException {

        if (startLine <= 0 || endLine < startLine) {
            throw new IllegalArgumentException("Invalid line range");
        }

        Path fullPath = resolvePath(filePath);

        List<String> lines = Files.readAllLines(fullPath, StandardCharsets.UTF_8);

        if (endLine > lines.size()) {
            throw new IllegalArgumentException("Line exceeds file length");
        }

        StringBuilder snippet = new StringBuilder();

        for (int i = startLine; i <= endLine; i++) {

            String currentLine = lines.get(i - 1);

            if (i == startLine && i == endLine) {
                snippet.append(currentLine, startOffset, endOffset);

            } else if (i == startLine) {
                snippet.append(currentLine.substring(startOffset));
                snippet.append(System.lineSeparator());

            } else if (i == endLine) {
                snippet.append(currentLine, 0, endOffset);

            } else {
                snippet.append(currentLine);
                snippet.append(System.lineSeparator());
            }
        }

//        log.info("Extracted snippet from {} ({}:{} → {}:{})",
//                fullPath, startLine, startOffset, endLine, endOffset);

//        log.debug("Snippet:\n{}", snippet);

        return snippet.toString();
    }

    public String getFile(String filePath){
        Path fullPath = resolvePath(filePath);

        String file = "";

        try{
            file = Files.readString(fullPath);
        } catch (Exception e){
            log.error("Error reading file.");
        }

        return file;
    }
}