    public boolean PrintPage(String page, String url_underscoreaddr, String charset) {
        File parent_underscorepath = new File(new File(page).getParent());
        if (!parent_underscorepath.exists()) {
            parent_underscorepath.mkdirs();
        }
        String r_underscoreline = null;
        BufferedReader bReader = null;
        FileOutputStream out = null;
        OutputStreamWriter writer = null;
        PrintWriter fileOut = null;
        File file = null;
        try {
            InputStream ins = new URL(url_underscoreaddr).openStream();
            file = new File(page);
            if (!file.exists()) {
                file.createNewFile();
            }
            bReader = new BufferedReader(new InputStreamReader(ins, charset));
            out = new FileOutputStream(page);
            writer = new OutputStreamWriter(out, charset);
            fileOut = new PrintWriter(writer);
            while ((r_underscoreline = bReader.readLine()) != null) {
                r_underscoreline = r_underscoreline.trim();
                int str_underscorelen = r_underscoreline.length();
                if (str_underscorelen > 0) {
                    fileOut.println(r_underscoreline);
                    fileOut.flush();
                }
            }
            ins.close();
            ins = null;
            fileOut.close();
            writer.close();
            out.close();
            bReader.close();
            fileOut = null;
            writer = null;
            out = null;
            bReader = null;
            parent_underscorepath = null;
            file = null;
            r_underscoreline = null;
            return true;
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            ioe.printStackTrace();
            return false;
        } catch (Exception es) {
            es.printStackTrace();
            log.error("static----------" + es.getMessage());
            return false;
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                    fileOut = null;
                }
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (bReader != null) {
                    bReader.close();
                    bReader = null;
                }
            } catch (IOException ioe) {
                log.error(ioe.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

