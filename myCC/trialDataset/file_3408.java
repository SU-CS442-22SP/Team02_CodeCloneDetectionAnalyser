    protected boolean process(final TranscodeJobImpl job) {
        TranscodePipe pipe = null;
        current_underscorejob = job;
        DeviceImpl device = job.getDevice();
        device.setTranscoding(true);
        try {
            job.starts();
            TranscodeProvider provider = job.getProfile().getProvider();
            final TranscodeException[] error = { null };
            TranscodeProfile profile = job.getProfile();
            final TranscodeFileImpl transcode_underscorefile = job.getTranscodeFile();
            TranscodeProviderAnalysis provider_underscoreanalysis;
            boolean xcode_underscorerequired;
            if (provider == null) {
                xcode_underscorerequired = false;
                provider_underscoreanalysis = null;
            } else {
                provider_underscoreanalysis = analyse(job);
                xcode_underscorerequired = provider_underscoreanalysis.getBooleanProperty(TranscodeProviderAnalysis.PT_underscoreTRANSCODE_underscoreREQUIRED);
                int tt_underscorereq;
                if (job.isStream()) {
                    tt_underscorereq = TranscodeTarget.TRANSCODE_underscoreALWAYS;
                } else {
                    tt_underscorereq = job.getTranscodeRequirement();
                    if (device instanceof TranscodeTarget) {
                        if (provider_underscoreanalysis.getLongProperty(TranscodeProviderAnalysis.PT_underscoreVIDEO_underscoreHEIGHT) == 0) {
                            if (((TranscodeTarget) device).isAudioCompatible(transcode_underscorefile)) {
                                tt_underscorereq = TranscodeTarget.TRANSCODE_underscoreNEVER;
                            }
                        }
                    }
                }
                if (tt_underscorereq == TranscodeTarget.TRANSCODE_underscoreNEVER) {
                    xcode_underscorerequired = false;
                } else if (tt_underscorereq == TranscodeTarget.TRANSCODE_underscoreALWAYS) {
                    xcode_underscorerequired = true;
                    provider_underscoreanalysis.setBooleanProperty(TranscodeProviderAnalysis.PT_underscoreFORCE_underscoreTRANSCODE, true);
                }
            }
            if (xcode_underscorerequired) {
                final AESemaphore xcode_underscoresem = new AESemaphore("xcode:proc");
                final TranscodeProviderJob[] provider_underscorejob = { null };
                TranscodeProviderAdapter xcode_underscoreadapter = new TranscodeProviderAdapter() {

                    private boolean resolution_underscoreupdated;

                    private final int ETA_underscoreAVERAGE_underscoreSIZE = 10;

                    private int last_underscoreeta;

                    private int eta_underscoresamples;

                    private Average eta_underscoreaverage = AverageFactory.MovingAverage(ETA_underscoreAVERAGE_underscoreSIZE);

                    private int last_underscorepercent;

                    public void updateProgress(int percent, int eta_underscoresecs, int new_underscorewidth, int new_underscoreheight) {
                        last_underscoreeta = eta_underscoresecs;
                        last_underscorepercent = percent;
                        TranscodeProviderJob prov_underscorejob = provider_underscorejob[0];
                        if (prov_underscorejob == null) {
                            return;
                        }
                        int job_underscorestate = job.getState();
                        if (job_underscorestate == TranscodeJob.ST_underscoreCANCELLED || job_underscorestate == TranscodeJob.ST_underscoreREMOVED) {
                            prov_underscorejob.cancel();
                        } else if (paused || job_underscorestate == TranscodeJob.ST_underscorePAUSED) {
                            prov_underscorejob.pause();
                        } else {
                            if (job_underscorestate == TranscodeJob.ST_underscoreRUNNING) {
                                prov_underscorejob.resume();
                            }
                            job.updateProgress(percent, eta_underscoresecs);
                            prov_underscorejob.setMaxBytesPerSecond(max_underscorebytes_underscoreper_underscoresec);
                            if (!resolution_underscoreupdated) {
                                if (new_underscorewidth > 0 && new_underscoreheight > 0) {
                                    transcode_underscorefile.setResolution(new_underscorewidth, new_underscoreheight);
                                    resolution_underscoreupdated = true;
                                }
                            }
                        }
                    }

                    public void streamStats(long connect_underscorerate, long write_underscorespeed) {
                        if (Constants.isOSX && job.getEnableAutoRetry() && job.canUseDirectInput() && job.getAutoRetryCount() == 0) {
                            if (connect_underscorerate > 5 && last_underscorepercent < 100) {
                                long eta = (long) eta_underscoreaverage.update(last_underscoreeta);
                                eta_underscoresamples++;
                                if (eta_underscoresamples >= ETA_underscoreAVERAGE_underscoreSIZE) {
                                    long total_underscoretime = (eta * 100) / (100 - last_underscorepercent);
                                    long total_underscorewrite = total_underscoretime * write_underscorespeed;
                                    DiskManagerFileInfo file = job.getFile();
                                    long length = file.getLength();
                                    if (length > 0) {
                                        double over_underscorewrite = ((double) total_underscorewrite) / length;
                                        if (over_underscorewrite > 5.0) {
                                            failed(new TranscodeException("Overwrite limit exceeded, abandoning transcode"));
                                            provider_underscorejob[0].cancel();
                                        }
                                    }
                                }
                            } else {
                                eta_underscoresamples = 0;
                            }
                        }
                    }

                    public void failed(TranscodeException e) {
                        if (error[0] == null) {
                            error[0] = e;
                        }
                        xcode_underscoresem.release();
                    }

                    public void complete() {
                        xcode_underscoresem.release();
                    }
                };
                boolean direct_underscoreinput = job.useDirectInput();
                if (job.isStream()) {
                    pipe = new TranscodePipeStreamSource2(new TranscodePipeStreamSource2.streamListener() {

                        public void gotStream(InputStream is) {
                            job.setStream(is);
                        }
                    });
                    provider_underscorejob[0] = provider.transcode(xcode_underscoreadapter, provider_underscoreanalysis, direct_underscoreinput, job.getFile(), profile, new URL("tcp://127.0.0.1:" + pipe.getPort()));
                } else {
                    File output_underscorefile = transcode_underscorefile.getCacheFile();
                    provider_underscorejob[0] = provider.transcode(xcode_underscoreadapter, provider_underscoreanalysis, direct_underscoreinput, job.getFile(), profile, output_underscorefile.toURI().toURL());
                }
                provider_underscorejob[0].setMaxBytesPerSecond(max_underscorebytes_underscoreper_underscoresec);
                TranscodeQueueListener listener = new TranscodeQueueListener() {

                    public void jobAdded(TranscodeJob job) {
                    }

                    public void jobChanged(TranscodeJob changed_underscorejob) {
                        if (changed_underscorejob == job) {
                            int state = job.getState();
                            if (state == TranscodeJob.ST_underscorePAUSED) {
                                provider_underscorejob[0].pause();
                            } else if (state == TranscodeJob.ST_underscoreRUNNING) {
                                provider_underscorejob[0].resume();
                            } else if (state == TranscodeJob.ST_underscoreCANCELLED || state == TranscodeJob.ST_underscoreSTOPPED) {
                                provider_underscorejob[0].cancel();
                            }
                        }
                    }

                    public void jobRemoved(TranscodeJob removed_underscorejob) {
                        if (removed_underscorejob == job) {
                            provider_underscorejob[0].cancel();
                        }
                    }
                };
                try {
                    addListener(listener);
                    xcode_underscoresem.reserve();
                } finally {
                    removeListener(listener);
                }
                if (error[0] != null) {
                    throw (error[0]);
                }
            } else {
                DiskManagerFileInfo source = job.getFile();
                transcode_underscorefile.setTranscodeRequired(false);
                if (job.isStream()) {
                    PluginInterface av_underscorepi = PluginInitializer.getDefaultInterface().getPluginManager().getPluginInterfaceByID("azupnpav");
                    if (av_underscorepi == null) {
                        throw (new TranscodeException("Media Server plugin not found"));
                    }
                    IPCInterface av_underscoreipc = av_underscorepi.getIPC();
                    String url_underscorestr = (String) av_underscoreipc.invoke("getContentURL", new Object[] { source });
                    if (url_underscorestr == null || url_underscorestr.length() == 0) {
                        File source_underscorefile = source.getFile();
                        if (source_underscorefile.exists()) {
                            job.setStream(new BufferedInputStream(new FileInputStream(source_underscorefile)));
                        } else {
                            throw (new TranscodeException("No UPnPAV URL and file doesn't exist"));
                        }
                    } else {
                        URL source_underscoreurl = new URL(url_underscorestr);
                        job.setStream(source_underscoreurl.openConnection().getInputStream());
                    }
                } else {
                    if (device.getAlwaysCacheFiles()) {
                        PluginInterface av_underscorepi = PluginInitializer.getDefaultInterface().getPluginManager().getPluginInterfaceByID("azupnpav");
                        if (av_underscorepi == null) {
                            throw (new TranscodeException("Media Server plugin not found"));
                        }
                        IPCInterface av_underscoreipc = av_underscorepi.getIPC();
                        String url_underscorestr = (String) av_underscoreipc.invoke("getContentURL", new Object[] { source });
                        InputStream is;
                        long length;
                        if (url_underscorestr == null || url_underscorestr.length() == 0) {
                            File source_underscorefile = source.getFile();
                            if (source_underscorefile.exists()) {
                                is = new BufferedInputStream(new FileInputStream(source_underscorefile));
                                length = source_underscorefile.length();
                            } else {
                                throw (new TranscodeException("No UPnPAV URL and file doesn't exist"));
                            }
                        } else {
                            URL source_underscoreurl = new URL(url_underscorestr);
                            URLConnection connection = source_underscoreurl.openConnection();
                            is = source_underscoreurl.openConnection().getInputStream();
                            String s = connection.getHeaderField("content-length");
                            if (s != null) {
                                length = Long.parseLong(s);
                            } else {
                                length = -1;
                            }
                        }
                        OutputStream os = null;
                        final boolean[] cancel_underscorecopy = { false };
                        TranscodeQueueListener copy_underscorelistener = new TranscodeQueueListener() {

                            public void jobAdded(TranscodeJob job) {
                            }

                            public void jobChanged(TranscodeJob changed_underscorejob) {
                                if (changed_underscorejob == job) {
                                    int state = job.getState();
                                    if (state == TranscodeJob.ST_underscorePAUSED) {
                                    } else if (state == TranscodeJob.ST_underscoreRUNNING) {
                                    } else if (state == TranscodeJob.ST_underscoreCANCELLED || state == TranscodeJob.ST_underscoreSTOPPED) {
                                        cancel_underscorecopy[0] = true;
                                    }
                                }
                            }

                            public void jobRemoved(TranscodeJob removed_underscorejob) {
                                if (removed_underscorejob == job) {
                                    cancel_underscorecopy[0] = true;
                                }
                            }
                        };
                        try {
                            addListener(copy_underscorelistener);
                            os = new FileOutputStream(transcode_underscorefile.getCacheFile());
                            long total_underscorecopied = 0;
                            byte[] buffer = new byte[128 * 1024];
                            while (true) {
                                if (cancel_underscorecopy[0]) {
                                    throw (new TranscodeException("Copy cancelled"));
                                }
                                int len = is.read(buffer);
                                if (len <= 0) {
                                    break;
                                }
                                os.write(buffer, 0, len);
                                total_underscorecopied += len;
                                if (length > 0) {
                                    job.updateProgress((int) (total_underscorecopied * 100 / length), -1);
                                }
                                total_underscorecopied += len;
                            }
                        } finally {
                            try {
                                is.close();
                            } catch (Throwable e) {
                                Debug.out(e);
                            }
                            try {
                                if (os != null) {
                                    os.close();
                                }
                            } catch (Throwable e) {
                                Debug.out(e);
                            }
                            removeListener(copy_underscorelistener);
                        }
                    }
                }
            }
            job.complete();
            return (true);
        } catch (Throwable e) {
            job.failed(e);
            e.printStackTrace();
            if (!job.isStream() && job.getEnableAutoRetry() && job.getAutoRetryCount() == 0 && job.canUseDirectInput() && !job.useDirectInput()) {
                log("Auto-retrying transcode with direct input");
                job.setUseDirectInput();
                job.setAutoRetry(true);
                queue_underscoresem.release();
            }
            return (false);
        } finally {
            if (pipe != null) {
                pipe.destroy();
            }
            device.setTranscoding(false);
            current_underscorejob = null;
        }
    }

