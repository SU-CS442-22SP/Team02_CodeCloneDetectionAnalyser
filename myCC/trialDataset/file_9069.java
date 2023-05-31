    public void actionPerformed(ActionEvent e) {
        if (mode == ADD_underscoreURL) {
            String url = JOptionPane.showInputDialog(null, "Enter URL", "Enter URL", JOptionPane.OK_underscoreCANCEL_underscoreOPTION);
            if (url == null) return;
            try {
                is = new URL(url).openStream();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (mode == ADD_underscoreFILE) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_underscoreONLY);
            chooser.showDialog(null, "Add tab");
            File file = chooser.getSelectedFile();
            if (file == null) return;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        if (repository == null) repository = PersistenceService.getInstance();
        List artists = repository.getAllArtists();
        EventList artistList = new BasicEventList();
        artistList.addAll(artists);
        addDialog = new AddSongDialog(artistList, JOptionPane.getRootFrame(), true);
        Song s = addDialog.getSong();
        if (is != null) {
            String tab;
            try {
                tab = readTab(is);
                s.setTablature(tab);
                addDialog.setSong(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        addDialog.setVisible(true);
        addDialog.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                int ok = addDialog.getReturnStatus();
                if (ok == AddSongDialog.RET_underscoreCANCEL) return;
                addSong();
            }
        });
    }

