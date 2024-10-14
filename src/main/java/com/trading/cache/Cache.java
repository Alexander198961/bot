package com.trading.cache;





import com.google.common.cache.CacheBuilder;
import com.ib.client.Bar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Cache {
  public static enum Keys {
      Tickers, TradeConfig, EmaConfig, RequestConfig, tickersStateMap
  }
   public static com.google.common.cache.Cache<String, Object> cache =CacheBuilder.newBuilder()
            // .maximumSize(100)  // Limit cache size to 100 entries
            //.expireAfterAccess(12, TimeUnit.HOURS)  // Expire entries after 10 minutes of inactivity
            .build();;
    public void init(){
      /*
       cache = CacheBuilder.newBuilder()
               // .maximumSize(100)  // Limit cache size to 100 entries
                .expireAfterAccess(12, TimeUnit.HOURS)  // Expire entries after 10 minutes of inactivity
                .build();

       */

    }




}
