package br.com.hb.hyomobile.util;

import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;


/**
 * Created by vanderson on 23/03/2017.
 * Site Referencia : http://memorynotfound.com/calculate-relative-time-time-ago-java/
 */
public class TimeAgo {

    public static final Date TODAY = new Date();

    private static final int RANDOM = 3521;

    public static final Map<String, Long> times = new LinkedHashMap<>();

    static {
        times.put("ano", TimeUnit.DAYS.toMillis(365));
        times.put("mês", TimeUnit.DAYS.toMillis(30));
        times.put("semana", TimeUnit.DAYS.toMillis(7));
        times.put("dia", TimeUnit.DAYS.toMillis(1));
        times.put("hora", TimeUnit.HOURS.toMillis(1));
        times.put("minuto", TimeUnit.MINUTES.toMillis(1));
        times.put("segundo", TimeUnit.SECONDS.toMillis(1));
    }

    public static String toRelative(long duration, int maxLevel) {
        StringBuilder res = new StringBuilder();
        int level = 0;
        for (Map.Entry<String, Long> time : times.entrySet()){
            long timeDelta = duration / time.getValue();
            if (timeDelta > 0){
                res.append(timeDelta)
                        .append(" ")
                        .append(time.getKey())
                        .append(timeDelta > 1 ? "s" : "")
                        .append(", ");
                duration -= time.getValue() * timeDelta;
                level++;
            }
            if (level == maxLevel){
                break;
            }
        }
        if ("".equals(res.toString())) {
            return "0 seconds ago";
        } else {
            res.setLength(res.length() - 2);
            res.append(" ago");
            return res.toString();
        }
    }

    public static String toRelative(long duration) {
        return toRelative(duration, times.size());
    }

    public static String toRelative(Date start, Date end){
        assert start.after(end);
        return toRelative(end.getTime() - start.getTime());
    }

    public static String toRelative(Date start, Date end, int level){
        assert start.after(end);
        return toRelative(end.getTime() - start.getTime(), level);
    }
}