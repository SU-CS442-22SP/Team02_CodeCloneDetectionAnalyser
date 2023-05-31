    private void downloadDirectory() throws SocketException, IOException {
        FTPClient client = new FTPClient();
        client.connect(source.getHost());
        client.login(username, password);
        FTPFile[] files = client.listFiles(source.getPath());
        for (FTPFile file : files) {
            if (!file.isDirectory()) {
                long file_underscoresize = file.getSize() / 1024;
                Calendar cal = file.getTimestamp();
                URL source_underscorefile = new File(source + file.getName()).toURI().toURL();
                DownloadQueue.add(new Download(projectName, parser.getParserID(), source_underscorefile, file_underscoresize, cal, target + file.getName()));
            }
        }
    }

