    public Boolean connect() throws Exception {
        try {
            _underscoreftpClient = new FTPClient();
            _underscoreftpClient.connect(_underscoreurl);
            _underscoreftpClient.login(_underscoreusername, _underscorepassword);
            _underscorerootPath = _underscoreftpClient.printWorkingDirectory();
            return true;
        } catch (Exception ex) {
            throw new Exception("Cannot connect to server.");
        }
    }

