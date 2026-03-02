package AI;

import Sonar.Issue;

import java.util.List;

public class Config {
    public static final int AI_MAX_TOKENS = 10000;
    public static final int AI_MAX_CHATS = 1;//set to -1 for unlimited
    public static final double AI_TEMPERATURE = 0.1;
    public static final String AI_MODEL = "gpt-oss-120b-sovereign";

    public static String buildDefaultPrompt(Issue issue){
        return("The following issue was found by SonarCloud. As a senior software developer provide code to solve the specific issue and provide only the affected lines of code: Rule: '" + issue.rule + "', Message: '" + issue.message + "', the code: '" + issue.getSnippet() + "', whole file: '" + issue.getFile() + "'." );
    }

    public static String buildMultiIssuePrompt(String fullFile, List<Issue> issues) {

        StringBuilder builder = new StringBuilder();

        builder.append("The following issues were found by SonarCloud. ")
                .append("As a senior software developer provide code to solve ALL issues. ")
                .append("Return ONLY the full corrected file. ")
                .append("Do not explain anything.\n\n");

        builder.append("Issues:\n");

        for (Issue issue : issues) {
            builder.append("Rule: '")
                    .append(issue.rule)
                    .append("', Message: '")
                    .append(issue.message)
                    .append("'");

            if (issue.getSnippet() != null) {
                builder.append(", Affected code: '")
                        .append(issue.getSnippet())
                        .append("'");
            }

            builder.append(".\n");
        }

        builder.append("\nWhole file:\n'")
                .append(fullFile)
                .append("'.");

        return builder.toString();
    }

    private Config(){}
}
