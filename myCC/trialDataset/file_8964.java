    private static String deviceIdFromCombined_underscoreDevice_underscoreID(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(deviceIdFromIMEI(context));
        builder.append(deviceIdFromPseudo_underscoreUnique_underscoreId());
        builder.append(deviceIdFromAndroidId(context));
        builder.append(deviceIdFromWLAN_underscoreMAC_underscoreAddress(context));
        builder.append(deviceIdFromBT_underscoreMAC_underscoreAddress(context));
        String m_underscoreszLongID = builder.toString();
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_underscoreszLongID.getBytes(), 0, m_underscoreszLongID.length());
        byte p_underscoremd5Data[] = m.digest();
        String m_underscoreszUniqueID = new String();
        for (int i = 0; i < p_underscoremd5Data.length; i++) {
            int b = (0xFF & p_underscoremd5Data[i]);
            if (b <= 0xF) m_underscoreszUniqueID += "0";
            m_underscoreszUniqueID += Integer.toHexString(b);
        }
        return m_underscoreszUniqueID;
    }

