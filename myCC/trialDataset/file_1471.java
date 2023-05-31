        public IStatus runInUIThread(IProgressMonitor monitor) {
            monitor.beginTask(Strings.MSG_underscoreCONNECT_underscoreSERVER, 3);
            InputStream in = null;
            try {
                URL url = createOpenUrl(resource, pref);
                if (url != null) {
                    URLConnection con = url.openConnection();
                    monitor.worked(1);
                    monitor.setTaskName(Strings.MSG_underscoreWAIT_underscoreFOR_underscoreSERVER);
                    con.connect();
                    in = con.getInputStream();
                    in.read();
                    monitor.worked(1);
                    monitor.setTaskName(NLS.bind(Strings.MSG_underscoreOPEN_underscoreURL, url));
                    open(url, resource.getProject(), pref);
                    monitor.worked(1);
                }
            } catch (ConnectException con) {
                if (count < 3) {
                    ConnectAndOpenJob job = new ConnectAndOpenJob(resource, pref, ++count);
                    job.schedule(1000L);
                } else {
                    Activator.log(con);
                }
            } catch (Exception e) {
                Activator.log(e);
            } finally {
                Streams.close(in);
                monitor.done();
            }
            return Status.OK_underscoreSTATUS;
        }

