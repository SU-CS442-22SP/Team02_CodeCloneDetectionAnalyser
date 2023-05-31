    public void updateChecksum() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            List<Parameter> sortedKeys = new ArrayList<Parameter>(parameter_underscoreinstances.keySet());
            for (Parameter p : sortedKeys) {
                if (parameter_underscoreinstances.get(p) != null && !(parameter_underscoreinstances.get(p) instanceof OptionalDomain.OPTIONS) && !(parameter_underscoreinstances.get(p).equals(FlagDomain.FLAGS.OFF))) {
                    md.update(parameter_underscoreinstances.get(p).toString().getBytes());
                }
            }
            this.checksum = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

