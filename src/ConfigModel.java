import java.util.*;

public class ConfigModel {
    private String _mailServer;
    private String _smtp;
    private String _pop3;
    private String _autoload;
    private Map<String, Map<String, List<String>>> _filterMap;

    public ConfigModel() {
        
    }

    public ConfigModel(String mailServer, String smtp, String pop3, String autoload) {
        _filterMap = new HashMap<>();
        this._mailServer = mailServer;
        this._smtp = smtp;
        this._pop3 = pop3;
        this._autoload = autoload;
    }

    public String getMailServer() {
        return _mailServer;
    }

    public String getSMTP() {
        return _smtp;
    }

    public String getPOP3() {
        return _pop3;
    }

    public String getAutoload() {
        return _autoload;
    }

    public Map<String, Map<String, List<String>>> getFilterMap() {
        return _filterMap;
    }

    public void setMailServer(String mailServer) {
        this._mailServer = mailServer;
    }

    public void setSMTP(String smtp) {
        this._smtp = smtp;
    }

    public void setPOP3(String pop3) {
        this._pop3 = pop3;
    }

    public void setAutoload(String autoload) {
        this._autoload = autoload;
    }

    public void setFilterMap(Map<String, Map<String, List<String>>> filterMap) {
        this._filterMap = filterMap;
    }
}
