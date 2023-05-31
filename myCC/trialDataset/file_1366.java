    public boolean validateLogin(String username, String password) {
        boolean user_underscoreexists = false;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            String password_underscorehash = hash.toString(16);
            statement = connect.prepareStatement("SELECT id from toepen.users WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password_underscorehash);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user_underscoreexists = true;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            close();
            return user_underscoreexists;
        }
    }

