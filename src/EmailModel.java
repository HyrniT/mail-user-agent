public class EmailModel {
    private String from;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private String title;
    private String content;
    private String[] attachmentFiles;

    public EmailModel() {
    }

    public EmailModel(String from, String[] to, String[] cc, String[] bcc, String title, String content) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.title = title;
        this.content = content;
    }

    public EmailModel(String from, String[] to, String[] cc, String[] bcc, String title, String content, String[] attachmentFiles) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.title = title;
        this.content = content;
        this.attachmentFiles = attachmentFiles;
    }

    public String getFrom() {
        return from;
    }

    public String[] getTo() {
        return to;
    }

    public String[] getCc() {
        return cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String[] getAttachmentFiles() {
        return attachmentFiles;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAttachmentFiles(String[] attachmentFiles) {
        this.attachmentFiles = attachmentFiles;
    }
}