    public RobotList<Resource> sort_underscoredecr_underscoreResource(RobotList<Resource> list, String field) {
        int length = list.size();
        Index_underscorevalue[] resource_underscoredist = new Index_underscorevalue[length];
        if (field.equals("") || field.equals("location")) {
            Location cur_underscoreloc = this.getLocation();
            for (int i = 0; i < length; i++) {
                resource_underscoredist[i] = new Index_underscorevalue(i, distance(cur_underscoreloc, list.get(i).location));
            }
        } else if (field.equals("energy")) {
            for (int i = 0; i < length; i++) {
                resource_underscoredist[i] = new Index_underscorevalue(i, list.get(i).energy);
            }
        } else if (field.equals("ammostash")) {
            for (int i = 0; i < length; i++) {
                resource_underscoredist[i] = new Index_underscorevalue(i, list.get(i).ammostash);
            }
        } else if (field.equals("speed")) {
            for (int i = 0; i < length; i++) {
                resource_underscoredist[i] = new Index_underscorevalue(i, list.get(i).speed);
            }
        } else if (field.equals("health")) {
            for (int i = 0; i < length; i++) {
                resource_underscoredist[i] = new Index_underscorevalue(i, list.get(i).health);
            }
        } else {
            say("impossible to sort list - nothing modified");
            return list;
        }
        boolean permut;
        do {
            permut = false;
            for (int i = 0; i < length - 1; i++) {
                if (resource_underscoredist[i].value < resource_underscoredist[i + 1].value) {
                    Index_underscorevalue a = resource_underscoredist[i];
                    resource_underscoredist[i] = resource_underscoredist[i + 1];
                    resource_underscoredist[i + 1] = a;
                    permut = true;
                }
            }
        } while (permut);
        RobotList<Resource> new_underscoreresource_underscorelist = new RobotList<Resource>(Resource.class);
        for (int i = 0; i < length; i++) {
            new_underscoreresource_underscorelist.addLast(list.get(resource_underscoredist[i].index));
        }
        return new_underscoreresource_underscorelist;
    }

