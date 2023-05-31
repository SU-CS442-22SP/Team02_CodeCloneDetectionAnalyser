    public void render(Map map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(OUTPUT_underscoreBYTE_underscoreARRAY_underscoreINITIAL_underscoreSIZE);
        File file = (File) map.get("targetFile");
        IOUtils.copy(new FileInputStream(file), baos);
        httpServletResponse.setContentType(getContentType());
        httpServletResponse.setContentLength(baos.size());
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + file.getName());
        ServletOutputStream out = httpServletResponse.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }

