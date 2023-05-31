    public void run() {
        InputStreamReader in = null;
        OutputStreamWriter out = null;
        URL url = null;
        File net_underscorefile = null;
        long in_underscorelength = 0;
        progress_underscorebar.setValue(0);
        progress_underscorebar.setString("connecting!");
        progress_underscorebar.setStringPainted(true);
        if (sync_underscorehost_underscorepath_underscorename.length() > 0) {
            try {
                try {
                    if (protocol == Settings.protFTP) {
                        url = new URL("ftp://" + user_underscorename + ":" + password + "@" + sync_underscorehost_underscorepath_underscorename);
                        URLConnection connection = url.openConnection();
                        in = new InputStreamReader(connection.getInputStream());
                        in_underscorelength = connection.getContentLength();
                    } else {
                        net_underscorefile = new File(sync_underscorehost_underscorepath_underscorename);
                        in = new InputStreamReader(new FileInputStream(net_underscorefile), "US-ASCII");
                        in_underscorelength = net_underscorefile.length();
                    }
                    progress_underscorebar.setString("synchronising!");
                    EventMemory.get_underscoreinstance(null).import_underscorevCalendar(in, Math.max(in_underscorelength, 1), true, progress_underscorebar);
                    in.close();
                } catch (Exception x) {
                    progress_underscorebar.setString(x.getMessage());
                }
                progress_underscorebar.setValue(0);
                progress_underscorebar.setString("connecting!");
                if (protocol == Settings.protFTP) {
                    URLConnection connection = url.openConnection();
                    connection.setDoOutput(true);
                    out = new OutputStreamWriter(connection.getOutputStream(), "US-ASCII");
                } else if (protocol == Settings.protFile) {
                    out = new OutputStreamWriter(new FileOutputStream(net_underscorefile), "US-ASCII");
                }
                progress_underscorebar.setString("writing!");
                int[] i = new int[EventMemory.get_underscoreinstance(null).get_underscoresize()];
                for (int k = 0; k < i.length; k++) {
                    i[k] = k;
                }
                progress_underscorebar.setStringPainted(true);
                EventMemory.get_underscoreinstance(null).export_underscorevCalendar(out, i, true, progress_underscorebar, true);
                out.close();
                sync_underscoredialog.sync_underscorepanel.unlock_underscoreinput();
                sync_underscoredialog.dispose();
            } catch (Exception e) {
                progress_underscorebar.setString(e.getMessage());
                sync_underscoredialog.sync_underscorepanel.unlock_underscoreinput();
            }
        } else {
            progress_underscorebar.setString("enter a valid URL!");
            sync_underscoredialog.sync_underscorepanel.unlock_underscoreinput();
        }
    }

