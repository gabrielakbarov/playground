package Sonar;

public class Issue {
    public enum Status {
        OPEN, CONFIRMED, FALSE_POSITIVE, ACCEPTED, FIXED, RESOLVED, REOPENED, CLOSED
    }

    public String key;
    public String rule;
    String severity;
    Status status;

    public int[] textRange;

    String fileId;
    String fileComponent;
    public String fileName;
    public String filePath;
    public String file;

    public String aiResponse;

    public String message;

    public String snippet;

    public Issue(String key, String rule, String severity, Status status, int[] textRange, String fileId, String fileName, String filePath, String message) {
        this.key = key;
        this.rule = rule;
        this.severity = severity;
        this.status = status;

        this.textRange = textRange;


        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;


        this.message = message;
    }

    public Issue(String key, String rule, String severity, Status status) {
        this.key = key;
        this.rule = rule;
        this.severity = severity;
        this.status = status;
    }

    public Status getStatus(){
        return this.status;
    }

    public void setSnippet(String snippet){
        this.snippet = snippet;
    }

    public void setAiResponse(String response){
        this.aiResponse = response;
    }

    public String getAiResponse(){
        return this.aiResponse;
    }

    public void setFile(String file){
        this.file = file;
    }

    public String getFile(){
        return this.file;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public int[] getTextRange(){
        return this.textRange;
    }

    public void setTextRange(int[] textRange){
        System.arraycopy(textRange, 0, this.textRange, 0, this.textRange.length);
    }

    public String getSnippet(){
        return this.snippet;
    }

    public String getKey(){
        return this.key;
    }
}
