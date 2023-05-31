    public boolean renameTo(Folder f) throws MessagingException, StoreClosedException, NullPointerException {
        String[] aLabels = new String[] { "en", "es", "fr", "de", "it", "pt", "ca", "ja", "cn", "tw", "fi", "ru", "pl", "nl", "xx" };
        PreparedStatement oUpdt = null;
        if (!((DBStore) getStore()).isConnected()) throw new StoreClosedException(getStore(), "Store is not connected");
        if (oCatg.isNull(DB.gu_underscorecategory)) throw new NullPointerException("Folder is closed");
        try {
            oUpdt = getConnection().prepareStatement("DELETE FROM " + DB.k_underscorecat_underscorelabels + " WHERE " + DB.gu_underscorecategory + "=?");
            oUpdt.setString(1, oCatg.getString(DB.gu_underscorecategory));
            oUpdt.executeUpdate();
            oUpdt.close();
            oUpdt.getConnection().prepareStatement("INSERT INTO " + DB.k_underscorecat_underscorelabels + " (" + DB.gu_underscorecategory + "," + DB.id_underscorelanguage + "," + DB.tr_underscorecategory + "," + DB.url_underscorecategory + ") VALUES (?,?,?,NULL)");
            oUpdt.setString(1, oCatg.getString(DB.gu_underscorecategory));
            for (int l = 0; l < aLabels.length; l++) {
                oUpdt.setString(2, aLabels[l]);
                oUpdt.setString(3, f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1).toLowerCase());
                oUpdt.executeUpdate();
            }
            oUpdt.close();
            oUpdt = null;
            getConnection().commit();
        } catch (SQLException sqle) {
            try {
                if (null != oUpdt) oUpdt.close();
            } catch (SQLException ignore) {
            }
            try {
                getConnection().rollback();
            } catch (SQLException ignore) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        return true;
    }

