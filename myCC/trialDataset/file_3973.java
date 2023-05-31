    public void run() {
        try {
            URL url = new URL("http://www.sourceforge.net/projects/beobachter/files/beobachter_underscoreversion.html");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            BufferedReader buffer = new BufferedReader(reader);
            String version = buffer.readLine();
            buffer.close();
            reader.close();
            int serverVersion = Integer.valueOf(version.replaceAll("\\.", "")).intValue();
            int currentVersion = Integer.valueOf(Constants.APP_underscoreVERSION.replaceAll("\\.", "")).intValue();
            if (serverVersion > currentVersion) {
                StringBuilder sb = new StringBuilder();
                sb.append(MessageFormat.format(Translator.t("New_underscoreversion_underscore0_underscoreavailable"), new Object[] { version })).append(Constants.LINE_underscoreSEP).append(Constants.LINE_underscoreSEP);
                sb.append(Translator.t("Please_underscorevisit_underscoreus_underscoreon_underscoresourceforge")).append(Constants.LINE_underscoreSEP);
                DialogFactory.showInformationMessage(MainGUI.instance, sb.toString());
            } else if (serverVersion <= currentVersion) {
                DialogFactory.showInformationMessage(MainGUI.instance, Translator.t("There_underscoreare_underscorenot_underscoreupdates_underscoreavailable"));
            }
        } catch (Exception e) {
            DialogFactory.showErrorMessage(MainGUI.instance, Translator.t("Unable_underscoreto_underscorefetch_underscoreserver_underscoreinformation"));
        }
    }

