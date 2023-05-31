    public RobotList<Float> sort_underscoreincr_underscoreFloat(RobotList<Float> list, String field) {
        int length = list.size();
        Index_underscorevalue[] distri = new Index_underscorevalue[length];
        for (int i = 0; i < length; i++) {
            distri[i] = new Index_underscorevalue(i, list.get(i));
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
        RobotList<Float> sol = new RobotList<Float>(Float.class);
        for (int i = 0; i < length; i++) {
            sol.addLast(new Float(distri[i].value));
        }
        return sol;
    }

