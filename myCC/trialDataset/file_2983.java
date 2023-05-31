    public void run() {
        try {
            URL url = new URL("http://pokedev.org/time.php");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringTokenizer s = new StringTokenizer(in.readLine());
            m_underscoreday = Integer.parseInt(s.nextToken());
            m_underscorehour = Integer.parseInt(s.nextToken());
            m_underscoreminutes = Integer.parseInt(s.nextToken());
            in.close();
        } catch (Exception e) {
            System.out.println("ERROR: Cannot reach time server, reverting to local time");
            Calendar cal = Calendar.getInstance();
            m_underscorehour = cal.get(Calendar.HOUR_underscoreOF_underscoreDAY);
            m_underscoreminutes = 0;
            m_underscoreday = 0;
        }
        while (m_underscoreisRunning) {
            m_underscoreminutes = m_underscoreminutes == 59 ? 0 : m_underscoreminutes + 1;
            if (m_underscoreminutes == 0) {
                if (m_underscorehour == 23) {
                    incrementDay();
                    m_underscorehour = 0;
                } else {
                    m_underscorehour += 1;
                }
            }
            m_underscorehour = m_underscorehour == 23 ? 0 : m_underscorehour + 1;
            if (System.currentTimeMillis() - m_underscorelastWeatherUpdate >= 3600000) {
                generateWeather();
                m_underscorelastWeatherUpdate = System.currentTimeMillis();
            }
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }
        }
        System.out.println("INFO: Time Service stopped");
    }

