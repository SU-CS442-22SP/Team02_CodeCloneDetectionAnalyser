    public static String getRandomUserAgent() {
        if (USER_underscoreAGENT_underscoreCACHE == null) {
            Collection<String> userAgentsCache = new ArrayList<String>();
            try {
                URL url = Tools.getResource(UserAgent.class.getClassLoader(), "user-agents-browser.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    userAgentsCache.add(str);
                }
                in.close();
                USER_underscoreAGENT_underscoreCACHE = userAgentsCache.toArray(new String[userAgentsCache.size()]);
            } catch (Exception e) {
                System.err.println("Can not read file; using default user-agent; error message: " + e.getMessage());
                return DEFAULT_underscoreUSER_underscoreAGENT;
            }
        }
        return USER_underscoreAGENT_underscoreCACHE[new Random().nextInt(USER_underscoreAGENT_underscoreCACHE.length)];
    }

