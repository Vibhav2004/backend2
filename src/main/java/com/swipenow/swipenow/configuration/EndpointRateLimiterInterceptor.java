package com.swipenow.swipenow.configuration;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class EndpointRateLimiterInterceptor implements HandlerInterceptor {

    // ================= PER-ENDPOINT LIMITS =================
    private static final Map<String, Integer> ENDPOINT_LIMITS;
    static {
        ENDPOINT_LIMITS = new HashMap<>();
        ENDPOINT_LIMITS.put("/send", 5);
        ENDPOINT_LIMITS.put("/verify", 10);
        ENDPOINT_LIMITS.put("/update-password", 3);
        ENDPOINT_LIMITS.put("/Register-User", 5);
        ENDPOINT_LIMITS.put("/Login-User", 20);
        ENDPOINT_LIMITS.put("/update", 10);
        ENDPOINT_LIMITS.put("/add-friends", 50);
        ENDPOINT_LIMITS.put("/view_friends", 100);
        ENDPOINT_LIMITS.put("/profile", 50);
        ENDPOINT_LIMITS.put("/profile-pic", 10);
        ENDPOINT_LIMITS.put("/Get-profile-pic", 100);
        ENDPOINT_LIMITS.put("/upload", 20);
        ENDPOINT_LIMITS.put("/urls", 200);
        ENDPOINT_LIMITS.put("/daily-memes", 300);
        ENDPOINT_LIMITS.put("/delete-account", 1);
    }

    // Tracks requests: Map<IP, Map<Endpoint, Counter>>
    private final Map<String, Map<String, IpCounter>> ipCounters = new ConcurrentHashMap<>();

    private static class IpCounter {
        int count;
        LocalDate date;

        IpCounter() {
            this.count = 1;
            this.date = LocalDate.now();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();

        // Normalize path for endpoints with path variables
        if (path.startsWith("/view_friends")) path = "/view_friends";
        else if (path.startsWith("/profile/")) path = "/profile";
        else if (path.startsWith("/urls/")) path = "/urls";

        // Skip endpoints not in the map
        if (!ENDPOINT_LIMITS.containsKey(path)) return true;

        int limit = ENDPOINT_LIMITS.get(path);

        ipCounters.computeIfAbsent(ip, k -> new ConcurrentHashMap<>());
        Map<String, IpCounter> endpointMap = ipCounters.get(ip);

        endpointMap.compute(path, (key, counter) -> {
            if (counter == null || !counter.date.equals(LocalDate.now())) {
                return new IpCounter();
            } else {
                counter.count++;
                return counter;
            }
        });

        IpCounter counter = endpointMap.get(path);
        if (counter.count > limit) {
            response.setStatus(429); // 429
            response.getWriter().write(
                    "Daily limit reached for IP: " + ip + " on endpoint: " + path +
                            " (limit: " + limit + ")"
            );
            return false;
        }

        return true; // allow request
    }
}
