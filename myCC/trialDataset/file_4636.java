    public RobotList<Percentage> sort_underscoreincr_underscorePercentage(RobotList<Percentage> list, String field) {
        int length = list.size();
        Index_underscorevalue[] distri = new Index_underscorevalue[length];
        for (int i = 0; i < length; i++) {
            distri[i] = new Index_underscorevalue(i, list.get(i).percent);
        }
        boolean permut;
        do {
            permut = false;
            for (int i = 0; i < length - 1; i++) {
                if (distri[i].value > distri[i + 1].value) {
                    Index_underscorevalue a = distri[i];
                    distri[i] = distri[i + 1];
                    distri[i + 1] = a;
                    permut = true;
                }
            }
        } while (permut);
        RobotList<Percentage> sol = new RobotList<Percentage>(Percentage.class);
        for (int i = 0; i < length; i++) {
            sol.addLast(new Percentage(distri[i].value));
        }
        return sol;
    }

