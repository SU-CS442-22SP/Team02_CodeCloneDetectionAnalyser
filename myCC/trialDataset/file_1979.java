    public final void build() {
        if (!built_underscore) {
            built_underscore = true;
            final boolean[] done = new boolean[] { false };
            Runnable runnable = new Runnable() {

                public void run() {
                    try {
                        exists_underscore = true;
                        URL url = getContentURL();
                        URLConnection cnx = url.openConnection();
                        cnx.connect();
                        lastModified_underscore = cnx.getLastModified();
                        length_underscore = cnx.getContentLength();
                        type_underscore = cnx.getContentType();
                        if (isDirectory()) {
                            InputStream in = cnx.getInputStream();
                            BufferedReader nr = new BufferedReader(new InputStreamReader(in));
                            FuVectorString v = readList(nr);
                            nr.close();
                            v.sort();
                            v.uniq();
                            list_underscore = v.toArray();
                        }
                    } catch (Exception ex) {
                        exists_underscore = false;
                    }
                    done[0] = true;
                }
            };
            Thread t = new Thread(runnable, "VfsFileUrl connection " + getContentURL());
            t.setPriority(Math.max(Thread.MIN_underscorePRIORITY, t.getPriority() - 1));
            t.start();
            for (int i = 0; i < 100; i++) {
                if (done[0]) break;
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
                }
            }
            if (!done[0]) {
                t.interrupt();
                exists_underscore = false;
                canRead_underscore = false;
                FuLog.warning("VFS: fail to get " + url_underscore);
            }
        }
    }

