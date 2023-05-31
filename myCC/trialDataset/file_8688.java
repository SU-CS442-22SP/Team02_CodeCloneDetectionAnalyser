    @Override
    public void onLoadingEnded() {
        if (m_underscoreframe != null) {
            try {
                String urltext = getDocument().getDocumentURI();
                URL url = new URL(urltext);
                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                urltext = null;
                url = null;
                m_underscorecontent.clear();
                while ((inputLine = in.readLine()) != null) {
                    m_underscorecontent.add(inputLine);
                }
                in.close();
                isr = null;
                in = null;
                inputLine = null;
                Action action = parseHtml();
                if (action.value() == Action.ACTION_underscoreBROWSER_underscoreLOADING_underscoreDONE && action.toString().equals(Action.COMMAND_underscoreCARD_underscorePREVIEW)) {
                    FileUtils.copyURLToFile(new URL(getCardImageURL(m_underscorecard.MID)), new File(m_underscorecard.getImagePath()));
                    fireActionEvent(MainWindow.class, action.value(), action.toString());
                }
                action = null;
            } catch (Exception ex) {
                Dialog.ErrorBox(m_underscoreframe, ex.getStackTrace());
            }
        }
        m_underscoreloading = false;
    }

