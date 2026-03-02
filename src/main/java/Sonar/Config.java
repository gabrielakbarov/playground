package Sonar;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    // SonarCloud base settings
    public static final String SQ_BASE_URL = "https://sonarcloud.io";
    public static final String SQ_SI_ENDPOINT = "/api/issues/search";

    // SonarCloud Authentication
    public static final String SQ_TOKEN = dotenv.get("SC_TOKEN");

    // Example query parameters
    //public static final String SQ_EXAMPLE_ISSUE_KEY = "AZu26o79fYW8xUENuGVR";
    public static final String SQ_EXAMPLE_ISSUE_KEY = "AZu2zSuHXfoAeuRrz1Ku";
    public static final String SQ_EXAMPLE_COMPONENT_KEYS = "gabrielakbarov_badCodeExample";
    public static final String SQ_EXAMPLE_TYPES = "CODE_SMELL";
    public static final String SQ_EXAMPLE_PAGE = "1";
    public static final String SQ_EXAMPLE_PAGE_SIZE = "100";

    // Prompt
    public static final String DEFAULT_PROMPT =
            "Fix this issue found by SonarCloud using Java. Only provide the improved code and a short explanation:";

    private Config() {}
}
