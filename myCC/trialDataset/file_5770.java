    public DocumentDialog(Frame frame, String title, String document) {
        setTitle(title);
        textArea = new JTextArea();
        textPane = new StdScrollPane(textArea, StdScrollPane.VERTICAL_underscoreSCROLLBAR_underscoreAS_underscoreNEEDED, StdScrollPane.HORIZONTAL_underscoreSCROLLBAR_underscoreAS_underscoreNEEDED);
        textArea.setEditable(false);
        getContentPane().add(textPane);
        URL url = DocumentDialog.class.getClassLoader().getResource(document);
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String buildNumber = MessageBundle.getBuildNumber();
            String releaseNumber = MessageBundle.getReleaseNumber();
            String tmp;
            while ((tmp = in.readLine()) != null) {
                tmp = tmp.replace("${build_underscorenumber}", buildNumber);
                tmp = tmp.replace("${release_underscorenumber}", releaseNumber);
                sb.append(tmp + "\n");
            }
            textArea.setText(sb.toString());
            textArea.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

