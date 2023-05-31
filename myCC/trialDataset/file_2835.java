    public void run() {
        runCounter++;
        try {
            LOGGER.info("Fetching feed [" + runCounter + "] " + _underscorefeedInfo);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            disableSSLCertificateChecking(httpClient);
            if (_underscoreproxy != null && _underscorefeedInfo.getUseProxy()) {
                LOGGER.info("Configuring proxy " + _underscoreproxy);
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_underscorePROXY, _underscoreproxy);
            }
            if (_underscorefeedInfo.getUsername() != null) {
                Credentials credentials;
                if (_underscorefeedInfo.getUsername().contains("/")) {
                    String username = _underscorefeedInfo.getUsername().substring(_underscorefeedInfo.getUsername().indexOf("/") + 1);
                    String domain = _underscorefeedInfo.getUsername().substring(0, _underscorefeedInfo.getUsername().indexOf("/"));
                    String workstation = InetAddress.getLocalHost().getHostName();
                    LOGGER.info("Configuring NT credentials : username=[" + username + "] domain=[" + domain + "] workstation=[" + workstation + "]");
                    credentials = new NTCredentials(username, _underscorefeedInfo.getPassword(), workstation, domain);
                    httpClient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
                    httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
                } else {
                    credentials = new UsernamePasswordCredentials(_underscorefeedInfo.getUsername(), _underscorefeedInfo.getPassword());
                    LOGGER.info("Configuring Basic credentials " + credentials);
                    httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
                }
            }
            if (_underscorefeedInfo.getCookie() != null) {
                BasicClientCookie cookie = new BasicClientCookie(_underscorefeedInfo.getCookie().getName(), _underscorefeedInfo.getCookie().getValue());
                cookie.setVersion(0);
                if (_underscorefeedInfo.getCookie().getDomain() != null) cookie.setDomain(_underscorefeedInfo.getCookie().getDomain());
                if (_underscorefeedInfo.getCookie().getPath() != null) cookie.setPath(_underscorefeedInfo.getCookie().getPath());
                LOGGER.info("Adding cookie " + cookie);
                CookieStore cookieStore = new BasicCookieStore();
                cookieStore.addCookie(cookie);
                localContext.setAttribute(ClientContext.COOKIE_underscoreSTORE, cookieStore);
            }
            HttpGet httpget = new HttpGet(_underscorefeedInfo.getUrl());
            HttpResponse response = httpClient.execute(httpget, localContext);
            LOGGER.info("Response Status : " + response.getStatusLine());
            LOGGER.debug("Headers : " + Arrays.toString(response.getAllHeaders()));
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_underscoreOK) {
                LOGGER.error("Request was unsuccessful for " + _underscorefeedInfo + " : " + response.getStatusLine());
            } else {
                SyndFeedInput input = new SyndFeedInput();
                XmlReader reader = new XmlReader(response.getEntity().getContent());
                SyndFeed feed = input.build(reader);
                if (feed.getTitle() != null) _underscorefeedInfo.setTitle(feed.getTitle());
                LOGGER.debug("Feed : " + new SyndFeedOutput().outputString(feed));
                LOGGER.info("Feed [" + feed.getTitle() + "] contains " + feed.getEntries().size() + " entries");
                @SuppressWarnings("unchecked") List<SyndEntry> entriesList = feed.getEntries();
                Collections.sort(entriesList, new SyndEntryPublishedDateComparator());
                for (SyndEntry entry : entriesList) {
                    if (VisitedEntries.getInstance().isAlreadyVisited(entry.getUri())) {
                        LOGGER.debug("Already received " + entry.getUri());
                    } else {
                        _underscorefeedInfo.addEntry(entry);
                        LOGGER.debug("New entry " + entry.toString());
                        _underscoreentryDisplay.displayEntry(feed, entry, firstRun);
                    }
                }
                LOGGER.info("Completing entries for feed " + feed.getTitle());
                if (firstRun) firstRun = false;
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (FeedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (KeyManagementException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

