    public static Shader loadShader(String vspath, String fspath, int textureUnits, boolean separateCam, boolean fog) throws ShaderProgramProcessException {
        if (vspath == "" || fspath == "") return null;
        BufferedReader in;
        String vert = "", frag = "";
        try {
            URL v_underscoreurl = Graphics.class.getClass().getResource("/eu/cherrytree/paj/graphics/shaders/" + vspath);
            String v_underscorepath = AppDefinition.getDefaultDataPackagePath() + "/shaders/" + vspath;
            if (v_underscoreurl != null) in = new BufferedReader(new InputStreamReader(v_underscoreurl.openStream())); else in = new BufferedReader(new InputStreamReader(new FileReader(v_underscorepath).getInputStream()));
            boolean run = true;
            String str;
            while (run) {
                str = in.readLine();
                if (str != null) vert += str + "\n"; else run = false;
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Couldn't read in vertex shader \"" + vspath + "\".");
            throw new ShaderNotLoadedException(vspath, fspath);
        }
        try {
            URL f_underscoreurl = Graphics.class.getClass().getResource("/eu/cherrytree/paj/graphics/shaders/" + fspath);
            String f_underscorepath = AppDefinition.getDefaultDataPackagePath() + "/shaders/" + fspath;
            if (f_underscoreurl != null) in = new BufferedReader(new InputStreamReader(f_underscoreurl.openStream())); else in = new BufferedReader(new InputStreamReader(new FileReader(f_underscorepath).getInputStream()));
            boolean run = true;
            String str;
            while (run) {
                str = in.readLine();
                if (str != null) frag += str + "\n"; else run = false;
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Couldn't read in fragment shader \"" + fspath + "\".");
            throw new ShaderNotLoadedException(vspath, fspath);
        }
        return loadShaderFromSource(vert, frag, textureUnits, separateCam, fog);
    }

