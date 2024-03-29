

/*
 * @license
 * Your First PWA Codelab (https://g.co/codelabs/pwa)
 * Copyright 2019 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
'use strict';

// CODELAB: Update cache names any time any of the cached files change.
const CACHE_NAME = 'static-cache-v1';
const DATA_CACHE_NAME = 'data-cache-v1';

// CODELAB: Add list of files to cache here.
// CODELAB: Update cache names any time any of the cached files change.
// CODELAB: Add list of files to cache here.
const FILES_TO_CACHE = [
  '/',
  '/content/pwa/en.html',
  '/etc.clientlibs/pwa/clientlibs/clientlib-base.js',
  '/etc.clientlibs/pwa/clientlibs/clientlib-base.css',
  '/etc/clientlibs/pwa/images/add.svg',
  '/etc/clientlibs/pwa/images/clear-day.svg',
  '/etc/clientlibs/pwa/images/clear-night.svg',
  '/etc/clientlibs/pwa/images/cloudy.svg',
  '/etc/clientlibs/pwa/images/fog.svg',
  '/etc/clientlibs/pwa/images/hail.svg',
  '/etc/clientlibs/pwa/images/install.svg',
  '/etc/clientlibs/pwa/images/partly-cloudy-day.svg',
  '/etc/clientlibs/pwa/images/partly-cloudy-night.svg',
  '/etc/clientlibs/pwa/images/rain.svg',
  '/etc/clientlibs/pwa/images/refresh.svg',
  '/etc/clientlibs/pwa/images/sleet.svg',
  '/etc/clientlibs/pwa/images/snow.svg',
  '/etc/clientlibs/pwa/images/thunderstorm.svg',
  '/etc/clientlibs/pwa/images/tornado.svg',
  '/etc/clientlibs/pwa/images/wind.svg',
  '/etc/clientlibs/pwa/images/content-image.jpg',
  '/etc/clientlibs/pwa/images/space-iphone-wallpaper-2.jpg',
  '/etc/clientlibs/pwa/images/deep-space.mp4',
  '/etc/clientlibs/pwa/images/rocket-2.jpg',
   '/etc/clientlibs/pwa/images/rocket-3.jpg',
];
self.addEventListener('install', (evt) => {
  console.log('[ServiceWorker] Install');
  // CODELAB: Precache static resources here.
  evt.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      console.log('[ServiceWorker] Pre-caching offline page');
      return cache.addAll(FILES_TO_CACHE);
    })
  );
  self.skipWaiting();
});

self.addEventListener('activate', (evt) => {
  console.log('[ServiceWorker] Activate');
  // CODELAB: Remove previous cached data from disk.
  evt.waitUntil(
    caches.keys().then((keyList) => {
      return Promise.all(keyList.map((key) => {
        if (key !== CACHE_NAME && key !== DATA_CACHE_NAME) {
          console.log('[ServiceWorker] Removing old cache', key);
          return caches.delete(key);
        }
      }));
    })
  );
  self.clients.claim();
});

self.addEventListener('fetch', (evt) => {
  console.log('[ServiceWorker] Fetch', evt.request.url);
  // CODELAB: Add fetch event handler here.
  if (evt.request.url.includes('/forecast/')) {
    console.log('[Service Worker] Fetch (data)', evt.request.url);
    evt.respondWith(
        caches.open(DATA_CACHE_NAME).then((cache) => {
          return fetch(evt.request)
              .then((response) => {
                // If the response was good, clone it and store it in the cache.
                if (response.status === 200) {
                  cache.put(evt.request.url, response.clone());
                }
                return response;
              }).catch((err) => {
                // Network request failed, try to get it from the cache.
                return cache.match(evt.request);
              });
        }));
    return;
  }
  evt.respondWith(
      caches.open(CACHE_NAME).then((cache) => {
        return cache.match(evt.request)
            .then((response) => {
              return response || fetch(evt.request);
            });
      })
  );

});
