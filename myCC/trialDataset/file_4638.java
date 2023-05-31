    public RobotList<Location> sort_underscoreincr_underscoreLocation(RobotList<Location> list, String field) {
        int length = list.size();
        Index_underscorevalue[] enemy_underscoredist = new Index_underscorevalue[length];
        Location cur_underscoreloc = this.getLocation();
        for (int i = 0; i < length; i++) {
            enemy_underscoredist[i] = new Index_underscorevalue(i, distance(cur_underscoreloc, list.get(i)));
        }
        boolean permut;
        do {
            permut = false;
            for (int i = 0; i < length - 1; i++) {
                if (enemy_underscoredist[i].value > enemy_underscoredist[i + 1].value) {
                    Index_underscorevalue a = enemy_underscoredist[i];
                    enemy_underscoredist[i] = enemy_underscoredist[i + 1];
                    enemy_underscoredist[i + 1] = a;
                    permut = true;
                }
            }
        } while (permut);
        RobotList<Location> new_underscorelocation_underscorelist = new RobotList<Location>(Location.class);
        for (int i = 0; i < length; i++) {
            new_underscorelocation_underscorelist.addLast(list.get(enemy_underscoredist[i].index));
        }
        return new_underscorelocation_underscorelist;
    }

