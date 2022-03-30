package com.whatsapp.profile_service.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class RateLimiterService {
      private LoadingCache<String, Long> userTriesCache;

      public RateLimiterService() {
            CacheLoader<String, Long> loader = new CacheLoader<String, Long>() {
                  @Override
                  public Long load(String ip) throws Exception {
                        return 0l;
                  }
            };
            userTriesCache = CacheBuilder.newBuilder()
                        .maximumSize(100)
                        .expireAfterAccess(3, TimeUnit.MINUTES)
                        .build(loader);
      }

      public void incrementTries(String ip) {
            try {
                  Long tries = userTriesCache.get(ip);
                  if (tries == null)
                        userTriesCache.put(ip, 1l);
                  userTriesCache.put(ip, tries + 1);
            } catch (ExecutionException e) {
                  userTriesCache.put(ip, 1l);
                  e.printStackTrace();
            }
      }

      public void clear(String ip) {
            userTriesCache.invalidate(ip);
      }

      public boolean isEligibleForLogin(String ip) {
            try {
                  return userTriesCache.get(ip) < 5l;
            } catch (ExecutionException e) {
                  return false;
            }
      }
}
