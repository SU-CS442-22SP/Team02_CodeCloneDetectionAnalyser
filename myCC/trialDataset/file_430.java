    public static int load(Context context, URL url) throws Exception {
        int texture[] = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        int textureId = texture[0];
        GLES20.glBindTexture(GLES20.GL_underscoreTEXTURE_underscore2D, textureId);
        InputStream is = url.openStream();
        Bitmap tmpBmp;
        try {
            tmpBmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        GLES20.glTexParameterf(GLES20.GL_underscoreTEXTURE_underscore2D, GLES20.GL_underscoreTEXTURE_underscoreMIN_underscoreFILTER, GLES20.GL_underscoreLINEAR_underscoreMIPMAP_underscoreNEAREST);
        MyGLUtils.checkGlError("glTexParameterf GL_underscoreTEXTURE_underscoreMIN_underscoreFILTER");
        GLES20.glTexParameterf(GLES20.GL_underscoreTEXTURE_underscore2D, GLES20.GL_underscoreTEXTURE_underscoreMAG_underscoreFILTER, GLES20.GL_underscoreLINEAR);
        MyGLUtils.checkGlError("glTexParameterf GL_underscoreTEXTURE_underscoreMAG_underscoreFILTER");
        GLUtils.texImage2D(GLES20.GL_underscoreTEXTURE_underscore2D, 0, tmpBmp, 0);
        MyGLUtils.checkGlError("texImage2D");
        GLES20.glGenerateMipmap(GLES20.GL_underscoreTEXTURE_underscore2D);
        MyGLUtils.checkGlError("glGenerateMipmap");
        tmpBmp.recycle();
        GLES20.glBindTexture(GLES20.GL_underscoreTEXTURE_underscore2D, 0);
        return textureId;
    }

