    public void readData(int choice) throws IOException {
        for (i = 0; i < max; i++) for (j = 0; j < max; j++) {
            phase_underscorex[i][j] = 0.0;
            phase_underscorey[i][j] = 0.0;
        }
        URL url;
        InputStream is;
        InputStreamReader isr;
        if (choice == 0) {
            url = getClass().getResource("resources/Phase_underscore623_underscore620_underscoreAchromat.dat");
            is = url.openStream();
            isr = new InputStreamReader(is);
        } else {
            url = getClass().getResource("resources/Phase_underscore623_underscore620_underscoreNoAchromat.dat");
            is = url.openStream();
            isr = new InputStreamReader(is);
        }
        BufferedReader br = new BufferedReader(isr);
        s = br.readLine();
        StringTokenizer st = new StringTokenizer(s);
        i = 0;
        j = 0;
        phase_underscorex[i][j] = 4 * Double.parseDouble(st.nextToken());
        phase_underscorey[i][j] = 4 * Double.parseDouble(st.nextToken());
        xgridmin = phase_underscorex[i][j];
        ygridmin = phase_underscorey[i][j];
        temp_underscoreprev = phase_underscorex[i][j];
        kd[i][j] = Double.parseDouble(st.nextToken());
        kfs[i][j] = Double.parseDouble(st.nextToken());
        kfl[i][j] = Double.parseDouble(st.nextToken());
        kdee[i][j] = Double.parseDouble(st.nextToken());
        kdc[i][j] = Double.parseDouble(st.nextToken());
        kfc[i][j] = Double.parseDouble(st.nextToken());
        j++;
        int k = 0;
        while ((s = br.readLine()) != null) {
            st = new StringTokenizer(s);
            temp_underscorenew = 4 * Double.parseDouble(st.nextToken());
            if (temp_underscorenew != temp_underscoreprev) {
                temp_underscoreprev = temp_underscorenew;
                i++;
                j = 0;
            }
            phase_underscorex[i][j] = temp_underscorenew;
            phase_underscorey[i][j] = 4 * Double.parseDouble(st.nextToken());
            kd[i][j] = Double.parseDouble(st.nextToken());
            kfs[i][j] = Double.parseDouble(st.nextToken());
            kfl[i][j] = Double.parseDouble(st.nextToken());
            kdee[i][j] = Double.parseDouble(st.nextToken());
            kdc[i][j] = Double.parseDouble(st.nextToken());
            kfc[i][j] = Double.parseDouble(st.nextToken());
            imax = i;
            jmax = j;
            j++;
            k++;
        }
        xgridmax = phase_underscorex[i][j - 1];
        ygridmax = phase_underscorey[i][j - 1];
    }

