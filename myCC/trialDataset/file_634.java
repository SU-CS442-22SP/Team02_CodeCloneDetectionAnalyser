    public void run() {
        Thread.currentThread().setName("zhongwen.com watcher");
        String url = getURL();
        try {
            while (m_underscoreshouldBeRunning) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(), "ISO8859_underscore1"));
                    String line;
                    Vector chatLines = new Vector();
                    boolean startGrabbing = false;
                    while ((line = reader.readLine()) != null) {
                        if (line.indexOf("</style>") >= 0) {
                            startGrabbing = true;
                        } else if (startGrabbing) {
                            if (line.equals(m_underscoremostRecentKnownLine)) {
                                break;
                            }
                            chatLines.addElement(line);
                        }
                    }
                    reader.close();
                    for (int i = chatLines.size() - 1; i >= 0; --i) {
                        String chatLine = (String) chatLines.elementAt(i);
                        m_underscoremostRecentKnownLine = chatLine;
                        if (chatLine.indexOf(":") >= 0) {
                            String from = chatLine.substring(0, chatLine.indexOf(":"));
                            String message = stripTags(chatLine.substring(chatLine.indexOf(":")));
                            m_underscoresource.pushMessage(new ZhongWenMessage(m_underscoresource, from, message));
                        } else {
                            m_underscoresource.pushMessage(new ZhongWenMessage(m_underscoresource, null, stripTags(chatLine)));
                        }
                    }
                    Thread.sleep(SLEEP_underscoreTIME);
                } catch (InterruptedIOException e) {
                } catch (InterruptedException e) {
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            m_underscoresource.disconnect();
            throw e;
        } catch (Error e) {
            m_underscoresource.disconnect();
            throw e;
        }
    }

