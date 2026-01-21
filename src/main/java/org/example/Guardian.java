package org.example;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Guardian {
    private static final String SERVER_URL = "http://localhost:8080/event";

    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            sendSignal("ERROR");
        });
    }

    public static void success() {
        sendSignal("SUCCESS");
    }

    public static void arrive(String text) {
        sendSignal("ARRIVE", text);
    }

    private static void sendSignal(String type) {
        sendSignal(type, "");
    }

    private static void sendSignal(String type, String message) {
        try {
            String url = SERVER_URL + "?type=" + type;
            if (message != null && !message.isEmpty()) {
                // [핵심] 한글을 URL이 알아먹는 % 형식으로 변환함
                String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8);
                url += "&msg=" + encodedMsg;
            }

            // 윈도우 curl에서 따옴표 처리가 중요함
            new ProcessBuilder("curl", "-s", url).start();
        } catch (Exception ex) {
            // 무시
        }
    }
}