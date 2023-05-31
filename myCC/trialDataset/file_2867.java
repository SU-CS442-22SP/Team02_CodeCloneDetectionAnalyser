    public void setBckImg(String newPath) {
        try {
            File inputFile = new File(getPath());
            File outputFile = new File(newPath);
            if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                FileInputStream in = new FileInputStream(inputFile);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(outputFile);
                } catch (FileNotFoundException ex1) {
                    ex1.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex1.getMessage().substring(0, Math.min(ex1.getMessage().length(), drawPanel.MAX_underscoreDIALOG_underscoreMSG_underscoreSZ)) + "-" + getClass(), "Set Bck Img", JOptionPane.ERROR_underscoreMESSAGE);
                }
                int c;
                if (out != null) {
                    while ((c = in.read()) != -1) out.write(c);
                    out.close();
                }
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_underscoreMSG", isLoggingEnabled());
            JOptionPane.showMessageDialog(null, ex.getMessage().substring(0, Math.min(ex.getMessage().length(), drawPanel.MAX_underscoreDIALOG_underscoreMSG_underscoreSZ)) + "-" + getClass(), "Set Bck Img", JOptionPane.ERROR_underscoreMESSAGE);
        }
        setPath(newPath);
        bckImg = new ImageIcon(getPath());
    }

