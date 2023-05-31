    public BasePolicy(String flaskPath) throws Exception {
        SWIGTYPE_underscorep_underscorep_underscorepolicy p_underscorep_underscorepol = apol.new_underscorepolicy_underscoret_underscorep_underscorep();
        if (!flaskPath.endsWith("/")) flaskPath += "/";
        File tmpPolConf = File.createTempFile("tmpBasePolicy", ".conf");
        BufferedWriter tmpPolFile = new BufferedWriter(new FileWriter(tmpPolConf));
        BufferedReader secClassFile = new BufferedReader(new FileReader(flaskPath + "security_underscoreclasses"));
        int bufSize = 1024;
        char[] buffer = new char[bufSize];
        int read;
        while ((read = secClassFile.read(buffer)) > 0) {
            tmpPolFile.write(buffer, 0, read);
        }
        secClassFile.close();
        BufferedReader sidsFile = new BufferedReader(new FileReader(flaskPath + "initial_underscoresids"));
        while ((read = sidsFile.read(buffer)) > 0) {
            tmpPolFile.write(buffer, 0, read);
        }
        sidsFile.close();
        BufferedReader axxVecFile = new BufferedReader(new FileReader(flaskPath + "access_underscorevectors"));
        while ((read = axxVecFile.read(buffer)) > 0) {
            tmpPolFile.write(buffer, 0, read);
        }
        axxVecFile.close();
        tmpPolFile.write("attribute ricka; \ntype rick_underscoret; \nrole rick_underscorer types rick_underscoret; \nuser rick_underscoreu roles rick_underscorer;\nsid kernel      rick_underscoreu:rick_underscorer:rick_underscoret\nfs_underscoreuse_underscorexattr ext3 rick_underscoreu:rick_underscorer:rick_underscoret;\ngenfscon proc /  rick_underscoreu:rick_underscorer:rick_underscoret\n");
        tmpPolFile.flush();
        tmpPolFile.close();
        if (apol.open_underscorepolicy(tmpPolConf.getAbsolutePath(), p_underscorep_underscorepol) == 0) {
            Policy = apol.policy_underscoret_underscorep_underscorep_underscorevalue(p_underscorep_underscorepol);
            if (Policy == null) {
                throw new Exception("Failed to allocate memory for policy_underscoret struct.");
            }
            tmpPolConf.delete();
        } else {
            throw new IOException("Failed to open/parse base policy file: " + tmpPolConf.getAbsolutePath());
        }
    }

