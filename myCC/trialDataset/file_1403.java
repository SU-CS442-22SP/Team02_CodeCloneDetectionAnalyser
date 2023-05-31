    private void downloadFiles() throws SocketException, IOException {
        HashSet<String> files_underscoreset = new HashSet<String>();
        boolean hasWildcarts = false;
        FTPClient client = new FTPClient();
        for (String file : downloadFiles) {
            files_underscoreset.add(file);
            if (file.contains(WILDCARD_underscoreWORD) || file.contains(WILDCARD_underscoreDIGIT)) hasWildcarts = true;
        }
        client.connect(source.getHost());
        client.login(username, password);
        FTPFile[] files = client.listFiles(source.getPath());
        if (!hasWildcarts) {
            for (FTPFile file : files) {
                String filename = file.getName();
                if (files_underscoreset.contains(filename)) {
                    long file_underscoresize = file.getSize() / 1024;
                    Calendar cal = file.getTimestamp();
                    URL source_underscorefile = new File(source + file.getName()).toURI().toURL();
                    DownloadQueue.add(new Download(projectName, parser.getParserID(), source_underscorefile, file_underscoresize, cal, target + file.getName()));
                }
            }
        } else {
            for (FTPFile file : files) {
                String filename = file.getName();
                boolean match = false;
                for (String db_underscorefilename : downloadFiles) {
                    db_underscorefilename = db_underscorefilename.replaceAll("\\" + WILDCARD_underscoreWORD, WILDCARD_underscoreWORD_underscorePATTERN);
                    db_underscorefilename = db_underscorefilename.replaceAll("\\" + WILDCARD_underscoreDIGIT, WILDCARD_underscoreDIGIT_underscorePATTERN);
                    Pattern p = Pattern.compile(db_underscorefilename);
                    Matcher m = p.matcher(filename);
                    match = m.matches();
                }
                if (match) {
                    long file_underscoresize = file.getSize() / 1024;
                    Calendar cal = file.getTimestamp();
                    URL source_underscorefile = new File(source + file.getName()).toURI().toURL();
                    DownloadQueue.add(new Download(projectName, parser.getParserID(), source_underscorefile, file_underscoresize, cal, target + file.getName()));
                }
            }
        }
    }

