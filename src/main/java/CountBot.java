public class CountBot {
    private int google, yandex;

    public CountBot(String userAgent) {
        if (userAgent.contains(";") && userAgent.contains("(") && userAgent.contains(")")) {
            userAgent = userAgent.substring(userAgent.indexOf("(") + 1, userAgent.indexOf(")")).trim();

            String[] parts = userAgent.split(";");

            for (int j = 0; j < parts.length; j++) {
                parts[j] = parts[j].replace(" ", "");
            }

            if (parts.length >= 2) {
                String fragment = parts[1];
                if (fragment.contains("Googlebot")) google++;
                if (fragment.contains("YandexBot")) yandex++;
            }
        }
    }

    public int getGoogle() {
        return google;
    }

    public int getYandex() {
        return yandex;
    }
}
