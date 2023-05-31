    public void submitReport() {
        String subject = m_underscoreSubject.getText();
        String description = m_underscoreDescription.getText();
        String email = m_underscoreEmail.getText();
        if (subject.length() == 0) {
            Util.flashComponent(m_underscoreSubject, Color.RED);
            return;
        }
        if (description.length() == 0) {
            Util.flashComponent(m_underscoreDescription, Color.RED);
            return;
        }
        DynamicLocalisation loc = m_underscoreMainFrame.getLocalisation();
        if (email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(".") == -1 || email.startsWith("@")) {
            email = "anonymous@blaat.com";
        }
        try {
            String data = URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode("manual", "UTF-8");
            data += "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8");
            data += "&" + URLEncoder.encode("body", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
            data += "&" + URLEncoder.encode("jvm", "UTF-8") + "=" + URLEncoder.encode(System.getProperty("java.version"), "UTF-8");
            data += "&" + URLEncoder.encode("ocdsver", "UTF-8") + "=" + URLEncoder.encode(Constants.OPENCDS_underscoreVERSION, "UTF-8");
            data += "&" + URLEncoder.encode("os", "UTF-8") + "=" + URLEncoder.encode(Constants.OS_underscoreNAME + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"), "UTF-8");
            URL url = new URL(Constants.BUGREPORT_underscoreSCRIPT);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
            JOptionPane.showMessageDialog(this, loc.getMessage("ReportBug.SentMessage"));
        } catch (Exception e) {
            Logger.getInstance().logException(e);
        }
        dispose();
    }

