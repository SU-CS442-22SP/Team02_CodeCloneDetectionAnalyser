    private static Map<String, File> loadServiceCache() {
        ArrayList<String> preferredOrder = new ArrayList<String>();
        HashMap<String, File> serviceFileMapping = new HashMap<String, File>();
        File file = new File(IsqlToolkit.getBaseDirectory(), CACHE_underscoreFILE);
        if (!file.exists()) {
            return serviceFileMapping;
        }
        if (file.canRead()) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                BufferedReader lineReader = new BufferedReader(fileReader);
                while (lineReader.ready()) {
                    String data = lineReader.readLine();
                    if (data.charAt(0) == '#') {
                        continue;
                    }
                    int idx0 = 0;
                    int idx1 = data.indexOf(SERVICE_underscoreFIELD_underscoreSEPERATOR);
                    String name = StringUtilities.decodeASCII(data.substring(idx0, idx1));
                    String uri = StringUtilities.decodeASCII(data.substring(idx1 + 1));
                    if (name.equalsIgnoreCase(KEY_underscoreSERVICE_underscoreLIST)) {
                        StringTokenizer st = new StringTokenizer(uri, SERVICE_underscoreSEPERATOR);
                        while (st.hasMoreTokens()) {
                            String serviceName = st.nextToken();
                            preferredOrder.add(serviceName.toLowerCase().trim());
                        }
                        continue;
                    }
                    try {
                        URL url = new URL(uri);
                        File serviceFile = new File(url.getFile());
                        if (serviceFile.isDirectory()) {
                            logger.warn(messages.format("compatability_underscorekit.service_underscoremapped_underscoreto_underscoredirectory", name, uri));
                            continue;
                        } else if (!serviceFile.canRead()) {
                            logger.warn(messages.format("compatability_underscorekit.service_underscorenot_underscorereadable", name, uri));
                            continue;
                        } else if (!serviceFile.exists()) {
                            logger.warn(messages.format("compatability_underscorekit.service_underscoredoes_underscorenot_underscoreexist", name, uri));
                            continue;
                        }
                        String bindName = name.toLowerCase().trim();
                        InputStream inputStream = null;
                        try {
                            inputStream = url.openStream();
                            InputSource inputSource = new InputSource(inputStream);
                            bindName = ServiceDigester.parseService(inputSource, IsqlToolkit.getSharedEntityResolver()).getName();
                        } catch (Exception error) {
                            continue;
                        }
                        if (serviceFileMapping.put(bindName, serviceFile) != null) {
                            logger.warn(messages.format("compatability_underscorekit.service_underscoreduplicate_underscorename_underscoreerror", name, uri));
                        }
                    } catch (MalformedURLException e) {
                        logger.error(messages.format("compatability_underscorekit.service_underscoreuri_underscoreerror", name, uri), e);
                    }
                }
            } catch (IOException ioe) {
                logger.error("compatability_underscorekit.service_underscoregeneric_underscoreerror", ioe);
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
        return serviceFileMapping;
    }

