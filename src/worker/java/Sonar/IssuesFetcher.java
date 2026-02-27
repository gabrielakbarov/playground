package Sonar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class IssuesFetcher {

    private final SonarHttpClient client = new SonarHttpClient();

    public List<Issue> fetchIssuesByProject() {

        Map<String, String> params = new HashMap<>();
        params.put("projects", Config.SQ_EXAMPLE_COMPONENT_KEYS);

        JSONObject response = client.get(params);

        JSONArray issues = response.optJSONArray("issues");
        if (issues == null) return Collections.emptyList();

        List<Issue> result = new ArrayList<>();

        for (int i = 0; i < issues.length(); i++) {
            JSONObject issueJson = issues.getJSONObject(i);

            String key = issueJson.optString("key");
            String rule = issueJson.optString("rule");
            String severity = issueJson.optString("severity");
            Issue.Status status = Issue.Status.valueOf(issueJson.optString("status"));

            result.add(new Issue(key, rule, severity, status));
        }

        return result;
    }
}