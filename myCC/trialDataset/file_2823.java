    public static void main(String[] args) {
        try {
            String default_underscoreuri = "http://www.cs.nmsu.edu/~bchisham/cgi-bin/phylows/tree/Tree3099?format=graphml";
            URL gurl = new URL(default_underscoreuri);
            InputStream is = gurl.openStream();
            Scanner iscan = new Scanner(is);
            while (iscan.hasNext()) {
                System.out.println(iscan.next());
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(GraphUrlLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
        }
    }

