    public boolean Load_underscoreClickable_underscorePeaks_underscoreSource_underscoreFile(String tam) {
        if (tam == null) return false;
        try {
            URL url = new URL(getDocumentBase(), tam);
            InputStream stream = url.openStream();
            DataInputStream fichier = new DataInputStream(stream);
            texte = new Vector();
            String s;
            while ((s = fichier.readLine()) != null) {
                texte.addElement(s);
            }
            My_underscoreZoneVisu.Nb_underscoreClickable_underscorePeaks = texte.size();
        } catch (Exception e) {
            return false;
        }
        My_underscoreZoneVisu.Peak_underscoreStart = new double[My_underscoreZoneVisu.Nb_underscoreClickable_underscorePeaks];
        My_underscoreZoneVisu.Peak_underscoreStop = new double[My_underscoreZoneVisu.Nb_underscoreClickable_underscorePeaks];
        My_underscoreZoneVisu.Peak_underscoreHtml = new String[My_underscoreZoneVisu.Nb_underscoreClickable_underscorePeaks];
        int cpt_underscoretokens = 0;
        int i = 0;
        StringTokenizer mon_underscoretoken;
        while (cpt_underscoretokens < My_underscoreZoneVisu.Nb_underscoreClickable_underscorePeaks) {
            do {
                String mysub = (String) texte.elementAt(cpt_underscoretokens);
                mon_underscoretoken = new StringTokenizer(mysub, " ");
                cpt_underscoretokens++;
            } while (cpt_underscoretokens < My_underscoreZoneVisu.Nb_underscoreClickable_underscorePeaks && mon_underscoretoken.hasMoreTokens() == false);
            if (mon_underscoretoken.hasMoreTokens() == true) {
                My_underscoreZoneVisu.Peak_underscoreStart[i] = Double.valueOf(mon_underscoretoken.nextToken()).doubleValue();
                My_underscoreZoneVisu.Peak_underscoreStop[i] = Double.valueOf(mon_underscoretoken.nextToken()).doubleValue();
                if (My_underscoreZoneVisu.Peak_underscoreStart[i] > My_underscoreZoneVisu.Peak_underscoreStop[i]) {
                    double temp = My_underscoreZoneVisu.Peak_underscoreStart[i];
                    My_underscoreZoneVisu.Peak_underscoreStart[i] = My_underscoreZoneVisu.Peak_underscoreStop[i];
                    My_underscoreZoneVisu.Peak_underscoreStop[i] = temp;
                }
                My_underscoreZoneVisu.Peak_underscoreHtml[i] = (String) mon_underscoretoken.nextToken();
            }
            i++;
        }
        return true;
    }

