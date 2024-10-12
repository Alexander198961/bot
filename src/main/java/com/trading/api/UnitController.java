package com.trading.api;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitController {
        public void setMap(Map<String, String> map) {
                this.initMapPeriod = map;
        }
        //MultiMap<String, Object> map = new MultiValuedHashMap();


        public Map<String, String> getPeriodMap() {
                return initMapPeriod;
        }

        final String [] barSize = {"1 min", "1 secs","1 hour", "1 day", "1 month" , "1 week"};
        final Long [] timeSecondsValues = {60L,1L,3600L, 86400L,  31536000L, 604800L   };
        final String[] periodValues = {"5 D", "4 D", "50 D", "240 D", "320 D", "320 D"};
        final String [] shortPeriodValues = {"120 S", "3 S", "7200 S", "2 D", "2 M", "2 W"};
        private Map<String, String> initMapPeriod = new HashMap<>();

        public Map<String, String> getShortPeriodMap() {
                return shortPeriodMap;
        }

        public void setShortPeriodMap(Map<String, String> shortPeriodMap) {
                this.shortPeriodMap = shortPeriodMap;
        }

        private  Map<String, String> shortPeriodMap = new HashMap<>();
        public Map<String, Long> getMapSeconds() {
                return mapSeconds;
        }

        public void setMapSeconds(Map<String, Long> mapSeconds) {
                this.mapSeconds = mapSeconds;
        }

        Map<String, Long> mapSeconds = new HashMap<>();

        public List<Map> init(){

                int i=0;
               // mapSeconds.put();
                for (String key : barSize) {
                        mapSeconds.put(key, timeSecondsValues[i]);
                        initMapPeriod.put(key, periodValues[i]);
                        shortPeriodMap.put(key, shortPeriodValues[i]);
                        //map.put(key, timeSecondsValues[i], periodValues[i]);
                        i=i+1;
                }
                /*
                mapSeconds.put("1 minute", 60L);
                mapSeconds.put("1 second", 1L);
                mapSeconds.put("1 hour", 3600L);
                mapSeconds.put("1 day", 86400L);
                mapSeconds.put("1 month", 31536000L);
                mapSeconds.put("1 week", 604800L);

                 */
                List<Map> list = new ArrayList<>();
                list.add(mapSeconds);
                list.add(initMapPeriod);
                return new ArrayList<>();

        }


        public String[] barSize(){
                // "1 day", "1 hour", "4 hours", "1 min", "1 day", "1 week", "1 month"
              /*
                initMap.put("1 day", "240 D");
                initMap.put("1 hour", "50 D");
                initMap.put("1 minute", "10 D");
                initMap.put("1 week", "500 D");
                initMap.put("1 month", "120 D");

               */
                return barSize;
        }
}
