    public void maj(String titre, String num_underscoreversion) {
        int res = 2;
        String content_underscorexml = "";
        try {
            URL url = new URL("http://code.google.com/feeds/p/tux-team/downloads/basic");
            InputStreamReader ipsr = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(ipsr);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            br.close();
            content_underscorexml = buffer.toString();
            res = lecture_underscorexml(titre, num_underscoreversion, content_underscorexml);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch(res) {
            case 0:
                ihm.jl_underscoremaj.setText("Pas de mises à jour disponible. (" + num_underscoreversion + ")");
                ihm.jl_underscoremaj.setIcon(Resources.getImageIcon("images/valide.png", IHM_underscoreAProposDe.class));
                break;
            case 1:
                ihm.jl_underscoremaj.setText("Une mise à jour est diponible. (" + maj_underscorefile_underscoreversion + ")");
                ihm.jl_underscoremaj.setIcon(Resources.getImageIcon("images/warning.png", IHM_underscoreAProposDe.class));
                ihm.jb_underscoremaj.setVisible(true);
                break;
            default:
                ihm.jl_underscoremaj.setText("Serveur de mise à jour non disponible.");
                ihm.jl_underscoremaj.setIcon(Resources.getImageIcon("images/erreur.png", IHM_underscoreAProposDe.class));
        }
    }

