    public void login(String a_underscoreusername, String a_underscorepassword) throws GB_underscoreSecurityException {
        Exception l_underscoreexception = null;
        try {
            if (clientFtp == null) {
                clientFtp = new FTPClient();
                clientFtp.connect("ftp://" + ftp);
            }
            boolean b = clientFtp.login(a_underscoreusername, a_underscorepassword);
            if (b) {
                username = a_underscoreusername;
                password = a_underscorepassword;
                return;
            }
        } catch (Exception ex) {
            l_underscoreexception = ex;
        }
        String l_underscoremsg = "Cannot login to ftp server with user [{1}], {2}";
        String[] l_underscorereplaces = new String[] { a_underscoreusername, ftp };
        l_underscoremsg = STools.replace(l_underscoremsg, l_underscorereplaces);
        throw new GB_underscoreSecurityException(l_underscoremsg, l_underscoreexception);
    }

