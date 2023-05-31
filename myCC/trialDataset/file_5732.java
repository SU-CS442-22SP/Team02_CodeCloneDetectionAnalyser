    private void add(Hashtable applicantInfo) throws Exception {
        String mode = "".equals(getParam("applicant_underscoreid_underscoregen").trim()) ? "update" : "insert";
        String applicant_underscoreid = getParam("applicant_underscoreid");
        String password = getParam("password");
        if ("".equals(applicant_underscoreid)) applicant_underscoreid = getParam("applicant_underscoreid_underscoregen");
        if ("".equals(getParam("applicant_underscorename"))) throw new Exception("Can not have empty fields!");
        applicantInfo.put("id", applicant_underscoreid);
        applicantInfo.put("password", password);
        applicantInfo.put("name", getParam("applicant_underscorename"));
        applicantInfo.put("address1", getParam("address1"));
        applicantInfo.put("address2", getParam("address2"));
        applicantInfo.put("address3", getParam("address3"));
        applicantInfo.put("city", getParam("city"));
        applicantInfo.put("state", getParam("state"));
        applicantInfo.put("poscode", getParam("poscode"));
        applicantInfo.put("country_underscorecode", getParam("country_underscorelist"));
        applicantInfo.put("email", getParam("email"));
        applicantInfo.put("phone", getParam("phone"));
        String birth_underscoreyear = getParam("birth_underscoreyear");
        String birth_underscoremonth = getParam("birth_underscoremonth");
        String birth_underscoreday = getParam("birth_underscoreday");
        applicantInfo.put("birth_underscoreyear", birth_underscoreyear);
        applicantInfo.put("birth_underscoremonth", birth_underscoremonth);
        applicantInfo.put("birth_underscoreday", birth_underscoreday);
        applicantInfo.put("gender", getParam("gender"));
        String birth_underscoredate = birth_underscoreyear + "-" + fmt(birth_underscoremonth) + "-" + fmt(birth_underscoreday);
        applicantInfo.put("birth_underscoredate", birth_underscoredate);
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("applicant_underscoreid");
                r.add("applicant_underscoreid", (String) applicantInfo.get("id"));
                sql = r.getSQLSelect("adm_underscoreapplicant");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true; else found = false;
            }
            if (found && !"update".equals(mode)) throw new Exception("Applicant Id was invalid!");
            {
                r.clear();
                r.add("password", (String) applicantInfo.get("password"));
                r.add("applicant_underscorename", (String) applicantInfo.get("name"));
                r.add("address1", (String) applicantInfo.get("address1"));
                r.add("address2", (String) applicantInfo.get("address2"));
                r.add("address3", (String) applicantInfo.get("address3"));
                r.add("city", (String) applicantInfo.get("city"));
                r.add("state", (String) applicantInfo.get("state"));
                r.add("poscode", (String) applicantInfo.get("poscode"));
                r.add("country_underscorecode", (String) applicantInfo.get("country_underscorecode"));
                r.add("phone", (String) applicantInfo.get("phone"));
                r.add("birth_underscoredate", (String) applicantInfo.get("birth_underscoredate"));
                r.add("gender", (String) applicantInfo.get("gender"));
            }
            if (!found) {
                r.add("applicant_underscoreid", (String) applicantInfo.get("id"));
                sql = r.getSQLInsert("adm_underscoreapplicant");
                stmt.executeUpdate(sql);
            } else {
                r.update("applicant_underscoreid", (String) applicantInfo.get("id"));
                sql = r.getSQLUpdate("adm_underscoreapplicant");
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
    }

