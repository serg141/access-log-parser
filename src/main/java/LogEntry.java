import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class LogEntry {
    private final String ipAddress, requestPath, referer;
    private final LocalDateTime requestDate;
    private final HttpMethod requestMethod;
    private final int responseCode, responseSize;
    private final UserAgent userAgent;

    public LogEntry(String line) {
        this.ipAddress = line.substring(0, line.indexOf(" "));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
        this.requestDate = LocalDateTime.parse(line.substring(line.indexOf("[") + 1, line.indexOf("]")), formatter);

        line = line.substring(line.indexOf("]") + 2).trim();
        this.requestMethod = HttpMethod.valueOf(line.substring(line.indexOf("\"") + 1, line.indexOf(" ")));

        line = line.substring(line.indexOf("/") + 1);
        this.requestPath = line.substring(0, line.indexOf("\""));

        line = line.substring(line.indexOf("\"") + 1).trim();
        this.responseCode = Integer.parseInt(line.substring(0, line.indexOf(" ")).trim());

        line = line.substring(line.indexOf(" ")).trim();
        this.responseSize = Integer.parseInt(line.substring(0, line.indexOf("\"")).trim());

        line = line.substring(line.indexOf(" ") + 2).trim();
        if (line.startsWith("http")) this.referer = line.substring(line.indexOf("http"), line.indexOf(" ") - 1).trim();
        else this.referer = "-";

        line = line.substring(line.indexOf(" ") + 2).trim();
        this.userAgent = new UserAgent(line);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}

enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS
}
