    public static boolean sendInformation(String reportType, HashMap<String, String> data) {
        if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_underscoreUDC)) {
            logger.debug("Report usage information to devs...");
            String transferData = "reportType=" + reportType;
            transferData += "&build=" + Platform.getBundle("de.uni_underscoremannheim.swt.codeconjurer").getHeaders().get("Bundle-Version");
            transferData += "&Suppl-Server=" + Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_underscoreSERVER);
            for (String key : data.keySet()) {
                transferData += "&Suppl-" + key + "=" + data.get(key);
            }
            try {
                URL url = new URL("http://www.merobase.com:7777/org.code_underscoreconjurer.udc/UsageReport");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(transferData);
                writer.flush();
                StringBuffer answer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line + "\r\n");
                }
                writer.close();
                reader.close();
                logger.debug("UDC Server answer: " + answer.toString());
            } catch (Exception e) {
                CrashReporter.reportException(e);
                logger.debug("Could not report usage data: " + e.toString());
                return false;
            }
            return true;
        } else {
            logger.debug("Reporting not wished!");
            return false;
        }
    }

