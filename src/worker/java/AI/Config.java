package AI;

import Sonar.Issue;

public class Config {
    public static final int AI_MAX_TOKENS = 10000;
    public static final int AI_MAX_CHATS = 1;//set to -1 for unlimited
    public static final double AI_TEMPERATURE = 0.1;
    public static final String AI_MODEL = "gpt-oss-120b-sovereign";

    public static String buildDefaultPrompt(Issue issue){
        return("The following issue was found by SonarCloud. As a senior developer provide code to solve the specific issue and provide only the affected lines of code: Rule: '" + issue.rule + "', Message: '" + issue.message + "', the code: '" + issue.getSnippet() + "', whole file: '" + issue.getFile() + "'." );
    }

    private Config(){}
}
