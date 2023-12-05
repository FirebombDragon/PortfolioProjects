function log(...data) {
    console.log("SWv1.0", ...data);
  }
  
  log("SW Script executing - adding event listeners");
  
  
  const STATIC_CACHE_NAME = 'cooke-static-v0';
  
  self.addEventListener('install', event => {
    log('install', event);
    event.waitUntil(
      caches.open(STATIC_CACHE_NAME).then(cache => {
        return cache.addAll([
          '/offline',
          '/shoppinglist',
          //CSS
          '/css/login.css',
          '/css/recipes.css',
          '/css/shopping.css',
          '/css/styles.css',
          //Images
          '/images/CookE_logo_nav.png',
          //Scripts
          '/js/login.js',
          '/js/viewrecipescript.js',
          '/js/addrecipescript.js',
          '/js/editrecipescript.js',
          '/js/recipes.js',
          '/js/shopping.js',
          '/js/APIClient.js',
          '/js/common.js',
          //External Resources
          'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css',
          'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'
        ]);
      })
    );
  });
  
  self.addEventListener('activate', event => {
    log('activate', event);
    event.waitUntil(
      caches.keys().then(cacheNames => {
        return Promise.all(
          cacheNames.filter(cacheName => {
            return cacheName.startsWith('cooke-') && cacheName != STATIC_CACHE_NAME;
          }).map(cacheName => {
            return caches.delete(cacheName);
          })
        );
      })
    );
  });
  
  self.addEventListener('fetch', event => {
    var requestUrl = new URL(event.request.url);
    // Intercept API call
    if(requestUrl.origin === location.origin && requestUrl.pathname.startsWith('/api')) {
      if(event.request.method === "GET") {
        event.respondWith(
          fetchFirst(event.request)
        );
      }
    }
    // Handle non-API call
    else {
      event.respondWith(
        cacheFirst(event.request)
      );
    }
  
  });
  
  function cacheFirst(request) {
    // On static resource request, first check cache for resource
    return caches.match(request).then(response => {
      return response || fetchAndCache(request);
    }).catch(error => {
        // If offline and requested resource was not cached, redirect to offline page
        return caches.match('/offline');
    })
  }

  function fetchFirst(request) {
    // On dynamic (API) request, first attempt to use the network
    return fetchAndCache(request).catch(error => {
        // If unable to use the network, check the cache for the resource
        return caches.match(request).catch(err => {
            // If resource is also not in cache, redirect to offline page
            return caches.match('/offline');
        })
    })
  }
  
  function fetchAndCache(request) {
    return fetch(request).then(response => {
      var requestUrl = new URL(request.url);
      //Cache everything except for login
      if(response.ok && !requestUrl.pathname.startsWith('/login')) {
        caches.open(STATIC_CACHE_NAME).then((cache) => {
          cache.put(request, response);
        });
      }
      return response.clone();
    });
  }
  
  
  
  self.addEventListener('message', event => {
    log('message', event.data);
    if(event.data.action === 'skipWaiting') {
      self.skipWaiting();
    }
  });