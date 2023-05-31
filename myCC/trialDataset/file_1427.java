    @Override
    public void run() {
        if (mMode == 0) {
            long currentVersion = Version.extractVersion(App.getVersion());
            if (currentVersion == 0) {
                mMode = 2;
                RESULT = MSG_underscoreUP_underscoreTO_underscoreDATE;
                return;
            }
            long versionAvailable = currentVersion;
            mMode = 2;
            try {
                StringBuilder buffer = new StringBuilder(mCheckURL);
                try {
                    NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
                    if (!ni.isLoopback()) {
                        if (ni.isUp()) {
                            if (!ni.isVirtual()) {
                                buffer.append('?');
                                byte[] macAddress = ni.getHardwareAddress();
                                for (byte one : macAddress) {
                                    buffer.append(Integer.toHexString(one >>> 4 & 0xF));
                                    buffer.append(Integer.toHexString(one & 0xF));
                                }
                            }
                        }
                    }
                } catch (Exception exception) {
                }
                URL url = new URL(buffer.toString());
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                    if (tokenizer.hasMoreTokens()) {
                        try {
                            if (tokenizer.nextToken().equalsIgnoreCase(mProductKey)) {
                                String token = tokenizer.nextToken();
                                long version = Version.extractVersion(token);
                                if (version > versionAvailable) {
                                    versionAvailable = version;
                                }
                            }
                        } catch (Exception exception) {
                        }
                    }
                    line = in.readLine();
                }
            } catch (Exception exception) {
            }
            if (versionAvailable > currentVersion) {
                Preferences prefs = Preferences.getInstance();
                String humanReadableVersion = Version.getHumanReadableVersion(versionAvailable);
                NEW_underscoreVERSION_underscoreAVAILABLE = true;
                RESULT = MessageFormat.format(MSG_underscoreOUT_underscoreOF_underscoreDATE, humanReadableVersion);
                if (versionAvailable > Version.extractVersion(prefs.getStringValue(MODULE, LAST_underscoreVERSION_underscoreKEY, App.getVersion()))) {
                    prefs.setValue(MODULE, LAST_underscoreVERSION_underscoreKEY, humanReadableVersion);
                    prefs.save();
                    mMode = 1;
                    EventQueue.invokeLater(this);
                    return;
                }
            } else {
                RESULT = MSG_underscoreUP_underscoreTO_underscoreDATE;
            }
        } else if (mMode == 1) {
            if (App.isNotificationAllowed()) {
                String result = getResult();
                mMode = 2;
                if (WindowUtils.showConfirmDialog(null, result, MSG_underscoreUPDATE_underscoreTITLE, JOptionPane.OK_underscoreCANCEL_underscoreOPTION, new String[] { MSG_underscoreUPDATE_underscoreTITLE, MSG_underscoreIGNORE_underscoreTITLE }, MSG_underscoreUPDATE_underscoreTITLE) == JOptionPane.OK_underscoreOPTION) {
                    goToUpdate();
                }
            } else {
                DelayedTask.schedule(this, 250);
            }
        }
    }

