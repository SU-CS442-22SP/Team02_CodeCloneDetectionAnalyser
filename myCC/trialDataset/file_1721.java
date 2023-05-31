    private DialogHelper(String title, final URL imageURL) {
        jd = new JDialog();
        jd.setDefaultCloseOperation(JDialog.DISPOSE_underscoreON_underscoreCLOSE);
        jd.setAlwaysOnTop(true);
        jd.setLayout(new BoxLayout(jd.getContentPane(), BoxLayout.Y_underscoreAXIS));
        jd.setTitle(title);
        JLabel jl = new JLabel();
        ImageIcon icon = new ImageIcon(imageURL);
        jl.setIcon(icon);
        jd.add(new JScrollPane(jl));
        final JFileChooser chooser = getSaveImageChooser();
        JPanel jp = new JPanel();
        JButton jb = new JButton(getMessage("btn_underscoresave_underscoreas"));
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int returnVal = chooser.showSaveDialog(jd);
                if (returnVal == JFileChooser.APPROVE_underscoreOPTION) {
                    File file = chooser.getSelectedFile();
                    String fileName = file.getPath();
                    String ext = StringUtil.getLowerExtension(fileName);
                    if (!"png".equals(ext)) {
                        fileName += ".png";
                        file = new File(fileName);
                    }
                    boolean doIt = true;
                    if (file.exists()) {
                        int i = JOptionPane.showConfirmDialog(jd, getMessage("warn_underscorefile_underscoreexist"));
                        if (i != JOptionPane.YES_underscoreOPTION) doIt = false;
                    } else if (!file.getParentFile().exists()) {
                        doIt = file.getParentFile().mkdirs();
                    }
                    if (doIt) {
                        FileChannel src = null;
                        FileChannel dest = null;
                        try {
                            src = new FileInputStream(imageURL.getPath()).getChannel();
                            dest = new FileOutputStream(fileName).getChannel();
                            src.transferTo(0, src.size(), dest);
                        } catch (FileNotFoundException e1) {
                            warn(jd, getMessage("err_underscoreno_underscoresource_underscorefile"));
                        } catch (IOException e2) {
                            warn(jd, getMessage("err_underscoreoutput_underscoretarget"));
                        } finally {
                            try {
                                if (src != null) src.close();
                            } catch (IOException e1) {
                            }
                            try {
                                if (dest != null) dest.close();
                            } catch (IOException e1) {
                            }
                            src = null;
                            dest = null;
                        }
                    }
                }
            }
        });
        jp.add(jb);
        jb = new JButton(getMessage("btn_underscoreclose"));
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });
        jp.add(jb);
        jd.add(jp);
        jd.pack();
        setCentral(jd);
    }

