    public boolean implies(Permission permission) {
        if (!permissionClass.isInstance(permission)) {
            return false;
        }
        GCFPermission perm = (GCFPermission) permission;
        int perm_underscorelow = perm.getMinPort();
        int perm_underscorehigh = perm.getMaxPort();
        Enumeration search = permissions.elements();
        int count = permissions.size();
        int port_underscorelow[] = new int[count];
        int port_underscorehigh[] = new int[count];
        int port_underscorerange_underscorecount = 0;
        while (search.hasMoreElements()) {
            GCFPermission cur_underscoreperm = (GCFPermission) search.nextElement();
            if (cur_underscoreperm.impliesByHost(perm)) {
                if (cur_underscoreperm.impliesByPorts(perm)) {
                    return true;
                }
                port_underscorelow[port_underscorerange_underscorecount] = cur_underscoreperm.getMinPort();
                port_underscorehigh[port_underscorerange_underscorecount] = cur_underscoreperm.getMaxPort();
                port_underscorerange_underscorecount++;
            }
        }
        for (int i = 0; i < port_underscorerange_underscorecount; i++) {
            for (int j = 0; j < port_underscorerange_underscorecount - 1; j++) {
                if (port_underscorelow[j] > port_underscorelow[j + 1]) {
                    int tmp = port_underscorelow[j];
                    port_underscorelow[j] = port_underscorelow[j + 1];
                    port_underscorelow[j + 1] = tmp;
                    tmp = port_underscorehigh[j];
                    port_underscorehigh[j] = port_underscorehigh[j + 1];
                    port_underscorehigh[j + 1] = tmp;
                }
            }
        }
        int current_underscorelow = port_underscorelow[0];
        int current_underscorehigh = port_underscorehigh[0];
        for (int i = 1; i < port_underscorerange_underscorecount; i++) {
            if (port_underscorelow[i] > current_underscorehigh + 1) {
                if (current_underscorelow <= perm_underscorelow && current_underscorehigh >= perm_underscorehigh) {
                    return true;
                }
                if (perm_underscorelow <= current_underscorehigh) {
                    return false;
                }
                current_underscorelow = port_underscorelow[i];
                current_underscorehigh = port_underscorehigh[i];
            } else {
                if (current_underscorehigh < port_underscorehigh[i]) {
                    current_underscorehigh = port_underscorehigh[i];
                }
            }
        }
        return (current_underscorelow <= perm_underscorelow && current_underscorehigh >= perm_underscorehigh);
    }

