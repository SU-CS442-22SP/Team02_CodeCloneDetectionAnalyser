    public static AddUserResponse napiUserAdd(String user, String pass, String email) throws TimeoutException, InterruptedException, IOException {
        if (user.matches("^[a-zA-Z0-9]{2,20}$") == false) {
            return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreBAD_underscoreLOGIN;
        }
        if (pass.equals("")) {
            return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreBAD_underscorePASS;
        }
        if (email.matches("^[a-zA-Z0-9\\-\\_underscore]{1,30}@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$") == false) {
            return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreBAD_underscoreEMAIL;
        }
        URLConnection conn = null;
        ClientHttpRequest httpPost = null;
        InputStreamReader responseStream = null;
        URL url = new URL("http://www.napiprojekt.pl/users_underscoreadd.php");
        conn = url.openConnection(Global.getProxy());
        httpPost = new ClientHttpRequest(conn);
        httpPost.setParameter("login", user);
        httpPost.setParameter("haslo", pass);
        httpPost.setParameter("mail", email);
        httpPost.setParameter("z_underscoreprogramu", "true");
        responseStream = new InputStreamReader(httpPost.post(), "Cp1250");
        BufferedReader responseReader = new BufferedReader(responseStream);
        String response = responseReader.readLine();
        if (response.indexOf("login już istnieje") != -1) {
            return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreLOGIN_underscoreEXISTS;
        }
        if (response.indexOf("na podany e-mail") != -1) {
            return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreEMAIL_underscoreEXISTS;
        }
        if (response.indexOf("NPc0") == 0) {
            return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreOK;
        }
        return AddUserResponse.NAPI_underscoreADD_underscoreUSER_underscoreBAD_underscoreUNKNOWN;
    }

