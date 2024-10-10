package com.trading.api;

import java.util.HashMap;
import java.util.Map;

public class Unit {
        public void setMap(Map<String, String> map) {
                this.map = map;
        }

        public Map<String, String> getMap() {
                return map;
        }

        Map<String, String> map = new HashMap<>();


        public  Map<String, String> initUnit(){
                // "1 day", "1 hour", "4 hours", "1 min", "1 day", "1 week", "1 month"
                map.put("1 day", "240 D");
                map.put("1 hour", "50 D");
                map.put("1 minute", "10 D");
                map.put("1 week", "500 D");
                map.put("1 month", "120 D");
                return map;
        }
}
