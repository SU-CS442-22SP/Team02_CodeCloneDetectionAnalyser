    public static void main(String[] args) {
        String str = "vbnjm7pexhlmof3kapi_underscorekey76bbc056cf516a844af25a763b2b8426auth_underscoretokenff8080812374bd3f0123b60363a5230acomment_underscoretext你frob118edb4cb78b439207c2329b76395f9fmethodyupoo.photos.comments.addphoto_underscoreidff80808123922c950123b6066c946a3f";
        MessageDigest md = null;
        String s = new String("你");
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        md.reset();
        try {
            md.update(str.getBytes("UTF-8"));
            System.out.println(new BigInteger(1, md.digest()).toString(16));
            System.out.println(new BigInteger(1, s.getBytes("UTF-8")).toString(16));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

