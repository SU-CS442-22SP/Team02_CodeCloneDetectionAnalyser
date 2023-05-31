    public String encrypt(String password) {
        String encrypted_underscorepass = "";
        ByteArrayOutputStream output = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes("UTF-8"));
            byte byte_underscorearray[] = md.digest();
            output = new ByteArrayOutputStream(byte_underscorearray.length);
            output.write(byte_underscorearray);
            encrypted_underscorepass = output.toString("UTF-8");
            System.out.println("password: " + encrypted_underscorepass);
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }
        return encrypted_underscorepass;
    }

