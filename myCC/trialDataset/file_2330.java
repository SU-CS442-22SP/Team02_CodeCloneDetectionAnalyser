    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("LOAD")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new JPEGFilter());
            chooser.setMultiSelectionEnabled(false);
            if (chooser.showOpenDialog(getTopLevelAncestor()) == JFileChooser.APPROVE_underscoreOPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
                    int read = is.read();
                    while (read != -1) {
                        bos.write(read);
                        read = is.read();
                    }
                    is.close();
                    _underscorechanged = true;
                    setImage(bos.toByteArray());
                } catch (Exception e1) {
                    _underscorelog.error("actionPerformed(ActionEvent)", e1);
                }
            }
        } else if (e.getActionCommand().equals("SAVE")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new JPEGFilter());
            chooser.setMultiSelectionEnabled(false);
            if (_underscoredata != null && chooser.showSaveDialog(getTopLevelAncestor()) == JFileChooser.APPROVE_underscoreOPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    os.write(_underscoredata);
                    os.flush();
                    os.close();
                } catch (Exception e1) {
                    _underscorelog.error("actionPerformed(ActionEvent)", e1);
                }
            }
        } else if (e.getActionCommand().equals("DELETE")) {
            if (_underscoredata != null) {
                int result = JOptionPane.showConfirmDialog(getTopLevelAncestor(), GuiStrings.getString("message.removeimg"), GuiStrings.getString("title.confirm"), JOptionPane.YES_underscoreNO_underscoreOPTION, JOptionPane.QUESTION_underscoreMESSAGE);
                if (result == JOptionPane.YES_underscoreOPTION) {
                    removeImage();
                    _underscorechanged = true;
                }
            }
        }
    }

