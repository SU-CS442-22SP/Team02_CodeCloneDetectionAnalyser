    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            File l_underscorefile = new File("C:/temp/testLoribel.html");
            String l_underscoreurl = "http://www.loribel.com/index.html";
            GB_underscoreHttpTools.loadUrlToFile(l_underscoreurl, l_underscorefile, ENCODING.ISO_underscore8859_underscore1);
            System.out.println("Try to connect...");
            client.connect("ftp://ftp.phpnet.org");
            System.out.println("Connected to server");
            System.out.println("Try to connect...");
            boolean b = client.login("fff", "ddd");
            System.out.println("Login: " + b);
            String[] l_underscorenames = client.listNames();
            GB_underscoreDebugTools.debugArray(GB_underscoreFtpDemo2.class, "names", l_underscorenames);
            b = client.makeDirectory("test02/toto");
            System.out.println("Mkdir: " + b);
            String l_underscoreremote = "test02/test.xml";
            InputStream l_underscorelocal = new StringInputStream("Test111111111111111");
            b = client.storeFile(l_underscoreremote, l_underscorelocal);
            System.out.println("Copy file: " + b);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

