    public void readData() throws IOException {
        i = 0;
        j = 0;
        URL url = getClass().getResource("resources/Chrom_underscore623_underscore620.dat");
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        s = br.readLine();
        StringTokenizer st = new StringTokenizer(s);
        s = br.readLine();
        st = new StringTokenizer(s);
        chrom_underscorex[i][j] = Double.parseDouble(st.nextToken());
        temp_underscoreprev = chrom_underscorex[i][j];
        chrom_underscorey[i][j] = Double.parseDouble(st.nextToken());
        gridmin = chrom_underscorex[i][j];
        gridmax = chrom_underscorex[i][j];
        sext1[i][j] = Double.parseDouble(st.nextToken());
        sext2[i][j] = Double.parseDouble(st.nextToken());
        sext3[i][j] = Double.parseDouble(st.nextToken());
        sext4[i][j] = Double.parseDouble(st.nextToken());
        j++;
        while ((s = br.readLine()) != null) {
            st = new StringTokenizer(s);
            temp_underscorenew = Double.parseDouble(st.nextToken());
            if (temp_underscorenew != temp_underscoreprev) {
                temp_underscoreprev = temp_underscorenew;
                i++;
                j = 0;
            }
            chrom_underscorex[i][j] = temp_underscorenew;
            chrom_underscorey[i][j] = Double.parseDouble(st.nextToken());
            sext1[i][j] = Double.parseDouble(st.nextToken());
            sext2[i][j] = Double.parseDouble(st.nextToken());
            sext3[i][j] = Double.parseDouble(st.nextToken());
            sext4[i][j] = Double.parseDouble(st.nextToken());
            imax = i;
            jmax = j;
            j++;
            if (chrom_underscorex[i][j] <= gridmin) gridmin = chrom_underscorex[i][j];
            if (chrom_underscorex[i][j] >= gridmax) gridmax = chrom_underscorex[i][j];
        }
    }

