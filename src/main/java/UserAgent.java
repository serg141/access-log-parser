public class UserAgent {
    private final String browser;
    private final Eos os;
    private final String ua;
    private boolean bot;

    //Разбирает UserAgent, выделяет тип браузера, вид ОС
    public UserAgent(String userAgent) {
        String userOs;
        String userBrowser = "";
        this.ua = userAgent;
        this.bot = false;

        if (userAgent.contains("Windows")) userOs = Eos.WINDOWS.name();
        else if (userAgent.contains("Mac OS")) userOs = Eos.MACOS.name();
        else if (userAgent.contains("Linux")) userOs = Eos.LINUX.name();
        else userOs = Eos.NO_EOS.name();

        if (userAgent.contains("Firefox/") && userAgent.contains("Gecko/")) userBrowser = "Firefox";
        else if (userAgent.contains("Edg/")) userBrowser = "Edge";
        else if (userAgent.contains("OPR/") || userAgent.contains("Presto/")) userBrowser = "Opera";
        else if (userAgent.contains("Safari/") && !userAgent.contains("Chrome/")) userBrowser = "Safari";
        else if (userAgent.contains("Chrome/")) userBrowser = "Chrome";

        if (!userAgent.contains("bot")) bot = true;

        this.os = Eos.valueOf(userOs);
        this.browser = userBrowser;
    }

    //Возвращает тип операционной системы
    Eos getOs() {
        return os;
    }

    //Возвращает тип браузера
    public String getBrowser() {
        if (browser.isEmpty()) return "Тип браузера не передан в запросе";
        else return browser;
    }

    //Возвращает UserAgent
    public String getUa() {
        return ua;
    }

    public boolean isBot() {
        return bot;
    }
}

enum Eos {
    WINDOWS, MACOS, LINUX,
    NO_EOS {
        public String toString() {
            return "OS не передана в запросе";
        }
    }
}
