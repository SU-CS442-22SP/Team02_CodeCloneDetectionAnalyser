    public void setImg() {
        JFileChooser jFileChooser1 = new JFileChooser();
        String separator = "";
        if (JFileChooser.APPROVE_underscoreOPTION == jFileChooser1.showOpenDialog(this.getFatherFrame())) {
            setPath(jFileChooser1.getSelectedFile().getPath());
            separator = jFileChooser1.getSelectedFile().separator;
            File dirImg = new File("." + separator + "images");
            if (!dirImg.exists()) {
                dirImg.mkdir();
            }
            int index = getPath().lastIndexOf(separator);
            String imgName = getPath().substring(index);
            String newPath = dirImg + imgName;
            try {
                File inputFile = new File(getPath());
                File outputFile = new File(newPath);
                if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                    FileInputStream in = new FileInputStream(inputFile);
                    FileOutputStream out = new FileOutputStream(outputFile);
                    int c;
                    while ((c = in.read()) != -1) out.write(c);
                    in.close();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LogHandler.log(ex.getMessage(), Level.INFO, "LOG_underscoreMSG", isLoggingEnabled());
                JOptionPane.showMessageDialog(null, ex.getMessage().substring(0, Math.min(ex.getMessage().length(), getFatherPanel().MAX_underscoreDIALOG_underscoreMSG_underscoreSZ)) + "-" + getClass(), "Set image", JOptionPane.ERROR_underscoreMESSAGE);
            }
            setPath(newPath);
            bckImg = new ImageIcon(getPath());
        }
    }

