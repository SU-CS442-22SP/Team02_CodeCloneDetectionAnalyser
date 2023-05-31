    public final void run() {
        active = true;
        String s = findcachedir();
        uid = getuid(s);
        try {
            File file = new File(s + "main_underscorefile_underscorecache.dat");
            if (file.exists() && file.length() > 0x3200000L) file.delete();
            cache_underscoredat = new RandomAccessFile(s + "main_underscorefile_underscorecache.dat", "rw");
            for (int j = 0; j < 5; j++) cache_underscoreidx[j] = new RandomAccessFile(s + "main_underscorefile_underscorecache.idx" + j, "rw");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        for (int i = threadliveid; threadliveid == i; ) {
            if (socketreq != 0) {
                try {
                    socket = new Socket(socketip, socketreq);
                } catch (Exception _underscoreex) {
                    socket = null;
                }
                socketreq = 0;
            } else if (threadreq != null) {
                Thread thread = new Thread(threadreq);
                thread.setDaemon(true);
                thread.start();
                thread.setPriority(threadreqpri);
                threadreq = null;
            } else if (dnsreq != null) {
                try {
                    dns = InetAddress.getByName(dnsreq).getHostName();
                } catch (Exception _underscoreex) {
                    dns = "unknown";
                }
                dnsreq = null;
            } else if (savereq != null) {
                if (savebuf != null) try {
                    FileOutputStream fileoutputstream = new FileOutputStream(s + savereq);
                    fileoutputstream.write(savebuf, 0, savelen);
                    fileoutputstream.close();
                } catch (Exception _underscoreex) {
                }
                if (waveplay) {
                    wave = s + savereq;
                    waveplay = false;
                }
                if (midiplay) {
                    midi = s + savereq;
                    midiplay = false;
                }
                savereq = null;
            } else if (urlreq != null) {
                try {
                    urlstream = new DataInputStream((new URL(mainapp.getCodeBase(), urlreq)).openStream());
                } catch (Exception _underscoreex) {
                    urlstream = null;
                }
                urlreq = null;
            }
            try {
                Thread.sleep(50L);
            } catch (Exception _underscoreex) {
            }
        }
    }

