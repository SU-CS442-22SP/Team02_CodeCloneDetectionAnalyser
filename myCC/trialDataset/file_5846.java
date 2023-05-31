    public Controller(String m_underscorehostname, String team, boolean m_underscoreshouldexit) throws InternalException {
        m_underscorereceived_underscoremessages = new ConcurrentLinkedQueue<ReceivedMessage>();
        m_underscorefragmsgs = new ArrayList<String>();
        m_underscorecustomizedtaunts = new HashMap<Integer, String>();
        m_underscorenethandler = new CachingNetHandler();
        m_underscoredrawingpanel = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
        m_underscoreuser = System.getProperty("user.name");
        m_underscorechatbuffer = new StringBuffer();
        this.m_underscoreshouldexit = m_underscoreshouldexit;
        isChatPaused = false;
        isRunning = true;
        m_underscorelastbullet = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(HogsConstants.FRAGMSGS_underscoreFILE));
            String str;
            while ((str = in.readLine()) != null) {
                m_underscorefragmsgs.add(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newFile = PathFinder.getCustsFile();
        boolean exists = (new File(newFile)).exists();
        Reader reader = null;
        if (exists) {
            try {
                reader = new FileReader(newFile);
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        } else {
            Object[] options = { "Yes, create a .hogsrc file", "No, use default taunts" };
            int n = JOptionPane.showOptionDialog(m_underscorewindow, "You do not have customized taunts in your home\n " + "directory.  Would you like to create a customizable file?", "Hogs Customization", JOptionPane.YES_underscoreNO_underscoreOPTION, JOptionPane.QUESTION_underscoreMESSAGE, null, options, options[1]);
            if (n == 0) {
                try {
                    FileChannel srcChannel = new FileInputStream(HogsConstants.CUSTS_underscoreTEMPLATE).getChannel();
                    FileChannel dstChannel;
                    dstChannel = new FileOutputStream(newFile).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                    reader = new FileReader(newFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    reader = new FileReader(HogsConstants.CUSTS_underscoreTEMPLATE);
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
        }
        try {
            m_underscorenetengine = NetEngine.forHost(m_underscoreuser, m_underscorehostname, 1820, m_underscorenethandler);
            m_underscorenetengine.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (NetException e) {
            e.printStackTrace();
        }
        m_underscoregamestate = m_underscorenetengine.getCurrentState();
        m_underscoregamestate.setInChatMode(false);
        m_underscoregamestate.setController(this);
        try {
            readFromFile(reader);
        } catch (NumberFormatException e3) {
            e3.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        } catch (InternalException e3) {
            e3.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice m_underscoregraphicsdevice = ge.getDefaultScreenDevice();
        m_underscorewindow = new GuiFrame(m_underscoredrawingpanel, m_underscoregamestate);
        m_underscoregraphics = null;
        try {
            m_underscoregraphics = new GraphicsEngine(m_underscoredrawingpanel, m_underscoregamestate);
        } catch (InternalException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
        m_underscoredrawingpanel.addGLEventListener(m_underscoregraphics);
        m_underscorephysics = new Physics();
        if (team == null) {
            team = HogsConstants.TEAM_underscoreNONE;
        }
        if (!(team.toLowerCase().equals(HogsConstants.TEAM_underscoreNONE) || team.toLowerCase().equals(HogsConstants.TEAM_underscoreRED) || team.toLowerCase().equals(HogsConstants.TEAM_underscoreBLUE))) {
            throw new InternalException("Invalid team name!");
        }
        String orig_underscoreteam = team;
        Craft local_underscorecraft = m_underscoregamestate.getLocalCraft();
        if (m_underscoregamestate.getNumCrafts() == 0) {
            local_underscorecraft.setTeamname(team);
        } else if (m_underscoregamestate.isInTeamMode()) {
            if (team == HogsConstants.TEAM_underscoreNONE) {
                int red_underscorecraft = m_underscoregamestate.getNumOnTeam(HogsConstants.TEAM_underscoreRED);
                int blue_underscorecraft = m_underscoregamestate.getNumOnTeam(HogsConstants.TEAM_underscoreBLUE);
                String new_underscoreteam;
                if (red_underscorecraft > blue_underscorecraft) {
                    new_underscoreteam = HogsConstants.TEAM_underscoreBLUE;
                } else if (red_underscorecraft < blue_underscorecraft) {
                    new_underscoreteam = HogsConstants.TEAM_underscoreRED;
                } else {
                    new_underscoreteam = Math.random() > 0.5 ? HogsConstants.TEAM_underscoreBLUE : HogsConstants.TEAM_underscoreRED;
                }
                m_underscoregamestate.getLocalCraft().setTeamname(new_underscoreteam);
            } else {
                local_underscorecraft.setTeamname(team);
            }
        } else {
            local_underscorecraft.setTeamname(HogsConstants.TEAM_underscoreNONE);
            if (orig_underscoreteam != null) {
                m_underscorewindow.displayText("You cannot join a team, this is an individual game.");
            }
        }
        if (!local_underscorecraft.getTeamname().equals(HogsConstants.TEAM_underscoreNONE)) {
            m_underscorewindow.displayText("You are joining the " + local_underscorecraft.getTeamname() + " team.");
        }
        m_underscoredrawingpanel.setSize(m_underscoredrawingpanel.getWidth(), m_underscoredrawingpanel.getHeight());
        m_underscoremiddlepos = new java.awt.Point(m_underscoredrawingpanel.getWidth() / 2, m_underscoredrawingpanel.getHeight() / 2);
        m_underscorecurpos = new java.awt.Point(m_underscoredrawingpanel.getWidth() / 2, m_underscoredrawingpanel.getHeight() / 2);
        GuiKeyListener k_underscorelistener = new GuiKeyListener();
        GuiMouseListener m_underscorelistener = new GuiMouseListener();
        m_underscorewindow.addKeyListener(k_underscorelistener);
        m_underscoredrawingpanel.addKeyListener(k_underscorelistener);
        m_underscorewindow.addMouseListener(m_underscorelistener);
        m_underscoredrawingpanel.addMouseListener(m_underscorelistener);
        m_underscorewindow.addMouseMotionListener(m_underscorelistener);
        m_underscoredrawingpanel.addMouseMotionListener(m_underscorelistener);
        m_underscoredrawingpanel.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                m_underscorewindow.setMouseTrapped(false);
                m_underscorewindow.returnMouseToCenter();
            }
        });
        m_underscorewindow.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                m_underscorewindow.setMouseTrapped(false);
                m_underscorewindow.returnMouseToCenter();
            }
        });
        m_underscorewindow.requestFocus();
    }

