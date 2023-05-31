    public void SetRoles(Connection conn, User user, String[] roles) throws NpsException {
        if (!IsSysAdmin() && !IsLocalAdmin()) throw new NpsException(ErrorHelper.ACCESS_underscoreNOPRIVILEGE);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "delete from userrole where userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.id);
            pstmt.executeUpdate();
            if (roles != null && roles.length > 0) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
                sql = "insert into userrole(userid,roleid) values(?,?)";
                pstmt = conn.prepareStatement(sql);
                for (int i = 0; i < roles.length; i++) {
                    if (roles[i] != null && roles[i].length() > 0) {
                        pstmt.setString(1, user.GetId());
                        pstmt.setString(2, roles[i]);
                        pstmt.executeUpdate();
                    }
                }
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            if (user.roles_underscoreby_underscorename != null) user.roles_underscoreby_underscorename.clear();
            if (user.roles_underscoreby_underscoreid != null) user.roles_underscoreby_underscoreid.clear();
            if (roles != null && roles.length > 0) {
                sql = "select b.* from UserRole a,Role b where a.roleid = b.id and a.userid=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (user.roles_underscoreby_underscorename == null) user.roles_underscoreby_underscorename = new Hashtable();
                    if (user.roles_underscoreby_underscoreid == null) user.roles_underscoreby_underscoreid = new Hashtable();
                    user.roles_underscoreby_underscorename.put(rs.getString("name"), rs.getString("id"));
                    user.roles_underscoreby_underscoreid.put(rs.getString("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
            }
            nps.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

