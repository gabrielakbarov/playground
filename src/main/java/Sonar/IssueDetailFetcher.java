package Sonar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class IssueDetailFetcher {

    private static final Logger log =
            Logger.getLogger(IssueDetailFetcher.class.getName());
    private final SonarHttpClient client = new SonarHttpClient();

    public Issue fetchIssueDetails(String issueKey) {

        Map<String, String> params = new HashMap<>();
        params.put("issues", issueKey);

        JSONObject response = client.get(params);

        JSONArray issuesArray = response.optJSONArray("issues");
        if (issuesArray == null || issuesArray.isEmpty()) {
            return null;
        }

        JSONObject issueJson = issuesArray.getJSONObject(0);

        String key = issueJson.optString("key");
        String rule = issueJson.optString("rule");
        String severity = issueJson.optString("severity");
        String message = issueJson.optString("message");

        Issue.Status status;
        status = Issue.Status.valueOf(issueJson.optString("status"));

        int[] textRange = new int[0];
        if (issueJson.has("textRange")) {
            JSONObject tr = issueJson.getJSONObject("textRange");
            textRange = new int[]{
                    tr.optInt("startLine"),
                    tr.optInt("endLine"),
                    tr.optInt("startOffset"),
                    tr.optInt("endOffset")
            };
        }

        String componentKey = issueJson.optString("component");
        String fileId = null;
        String fileName = null;
        String filePath = null;

        JSONArray components = response.optJSONArray("components");
        if (components != null) {
            for (int i = 0; i < components.length(); i++) {
                JSONObject comp = components.getJSONObject(i);
                if (componentKey.equals(comp.optString("key"))) {
                    fileId = comp.optString("uuid");
                    fileName = comp.optString("name");
                    filePath = comp.optString("path");
                    break;
                }
            }
        }

        return new Issue(
                key,
                rule,
                severity,
                status,
                textRange,
                fileId,
                fileName,
                filePath,
                message
        );


    }
}