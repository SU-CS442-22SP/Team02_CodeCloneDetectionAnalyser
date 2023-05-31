    public HogsCustomizer() {
        m_underscorefilename = PathFinder.getCustsFile();
        m_underscorecurrenttaunts = new String[10];
        m_underscoretextfields = new JTextField[10];
        m_underscorecolor = new Color(255, 255, 255);
        boolean exists = (new File(m_underscorefilename)).exists();
        m_underscoreinverted = false;
        m_underscorechooser = new JColorChooser();
        AbstractColorChooserPanel[] panels = m_underscorechooser.getChooserPanels();
        m_underscorechooser.removeChooserPanel(panels[0]);
        m_underscorechooser.removeChooserPanel(panels[2]);
        m_underscorechooser.setPreviewPanel(new JPanel());
        Reader reader = null;
        if (exists) {
            try {
                reader = new FileReader(m_underscorefilename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Object[] options = { "Yes", "No, Thanks" };
            int n = JOptionPane.showOptionDialog(this, "You do not have a customization file in your home directory.\n                 " + "Would you like to create one?", "Hogs Customization", JOptionPane.YES_underscoreNO_underscoreOPTION, JOptionPane.QUESTION_underscoreMESSAGE, null, options, options[1]);
            if (n == 0) {
                try {
                    FileChannel srcChannel = new FileInputStream(HogsConstants.CUSTS_underscoreTEMPLATE).getChannel();
                    FileChannel dstChannel = new FileOutputStream(m_underscorefilename).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
            try {
                reader = new FileReader(m_underscorefilename);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
        try {
            readFromFile(reader);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        Box mainpanel = Box.createVerticalBox();
        mainpanel.add(buildTauntsPanel());
        mainpanel.add(buildMouseStylePanel());
        mainpanel.add(Box.createVerticalStrut(10));
        mainpanel.add(buildColorPanel());
        mainpanel.add(Box.createVerticalStrut(10));
        mainpanel.add(buildButtonsPanel());
        mainpanel.add(Box.createVerticalStrut(10));
        this.setDefaultCloseOperation(JFrame.EXIT_underscoreON_underscoreCLOSE);
        this.setContentPane(mainpanel);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - (this.getWidth() / 2), dim.height / 2 - (this.getHeight() / 2));
        this.setTitle("Hogs Customizer");
        this.setVisible(true);
    }

