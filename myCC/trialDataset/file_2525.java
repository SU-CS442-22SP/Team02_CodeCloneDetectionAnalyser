    @SuppressWarnings("unchecked")
    public static <T> List<T> getServices(String service) {
        String serviceUri = "META-INF/services/" + service;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(serviceUri);
            if (urls.hasMoreElements()) {
                List<T> services = new ArrayList<T>(1);
                Set<String> keys = new HashSet<String>(20);
                do {
                    URL url = urls.nextElement();
                    if (_underscoreLOG.isLoggable(Level.FINEST)) {
                        _underscoreLOG.finest("Processing: " + url);
                    }
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        try {
                            while (true) {
                                String line = in.readLine();
                                if (line == null) break;
                                String className = _underscoreparseLine(line);
                                if (className != null && keys.add(className)) {
                                    T instance = (T) _underscoregetClass(loader, className);
                                    services.add(instance);
                                }
                            }
                        } finally {
                            in.close();
                        }
                    } catch (Exception e) {
                        if (_underscoreLOG.isLoggable(Level.WARNING)) {
                            _underscoreLOG.log(Level.WARNING, "Error parsing URL: " + url, e);
                        }
                    }
                } while (urls.hasMoreElements());
                if (services.size() == 1) return Collections.singletonList(services.get(0));
                return Collections.unmodifiableList(services);
            }
        } catch (IOException e) {
            if (_underscoreLOG.isLoggable(Level.SEVERE)) {
                _underscoreLOG.log(Level.SEVERE, "Error loading Resource: " + serviceUri, e);
            }
        }
        return Collections.emptyList();
    }

