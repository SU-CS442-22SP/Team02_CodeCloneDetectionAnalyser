    public BufferedImage extractUsingCompositor() throws IOException, DjatokaException {
        boolean useRegion = false;
        int left = 0;
        int top = 0;
        int width = 50;
        int height = 50;
        boolean useleftDouble = false;
        Double leftDouble = 0.0;
        boolean usetopDouble = false;
        Double topDouble = 0.0;
        boolean usewidthDouble = false;
        Double widthDouble = 0.0;
        boolean useheightDouble = false;
        Double heightDouble = 0.0;
        if (params.getRegion() != null) {
            StringTokenizer st = new StringTokenizer(params.getRegion(), "{},");
            String token;
            if ((token = st.nextToken()).contains(".")) {
                topDouble = Double.parseDouble(token);
                usetopDouble = true;
            } else top = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                leftDouble = Double.parseDouble(token);
                useleftDouble = true;
            } else left = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                heightDouble = Double.parseDouble(token);
                useheightDouble = true;
            } else height = Integer.parseInt(token);
            if ((token = st.nextToken()).contains(".")) {
                widthDouble = Double.parseDouble(token);
                usewidthDouble = true;
            } else width = Integer.parseInt(token);
            useRegion = true;
        }
        if (is != null) {
            File f = File.createTempFile("tmp", ".jp2");
            f.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(f);
            sourceFile = f.getAbsolutePath();
            IOUtils.copyStream(is, fos);
        }
        Kdu_underscoresimple_underscorefile_underscoresource raw_underscoresrc = null;
        Jp2_underscorefamily_underscoresrc family_underscoresrc = new Jp2_underscorefamily_underscoresrc();
        Jpx_underscoresource wrapped_underscoresrc = new Jpx_underscoresource();
        Kdu_underscoreregion_underscorecompositor compositor = null;
        BufferedImage image = null;
        try {
            family_underscoresrc.Open(sourceFile);
            int success = wrapped_underscoresrc.Open(family_underscoresrc, true);
            if (success < 0) {
                family_underscoresrc.Close();
                wrapped_underscoresrc.Close();
                raw_underscoresrc = new Kdu_underscoresimple_underscorefile_underscoresource(sourceFile);
            }
            compositor = new Kdu_underscoreregion_underscorecompositor();
            if (raw_underscoresrc != null) compositor.Create(raw_underscoresrc); else compositor.Create(wrapped_underscoresrc);
            Kdu_underscoredims imageDimensions = new Kdu_underscoredims();
            compositor.Get_underscoretotal_underscorecomposition_underscoredims(imageDimensions);
            Kdu_underscorecoords imageSize = imageDimensions.Access_underscoresize();
            Kdu_underscorecoords imagePosition = imageDimensions.Access_underscorepos();
            if (useleftDouble) left = imagePosition.Get_underscorex() + (int) Math.round(leftDouble * imageSize.Get_underscorex());
            if (usetopDouble) top = imagePosition.Get_underscorey() + (int) Math.round(topDouble * imageSize.Get_underscorey());
            if (useheightDouble) height = (int) Math.round(heightDouble * imageSize.Get_underscorey());
            if (usewidthDouble) width = (int) Math.round(widthDouble * imageSize.Get_underscorex());
            if (useRegion) {
                imageSize.Set_underscorex(width);
                imageSize.Set_underscorey(height);
                imagePosition.Set_underscorex(left);
                imagePosition.Set_underscorey(top);
            }
            int reduce = 1 << params.getLevelReductionFactor();
            imageSize.Set_underscorex(imageSize.Get_underscorex());
            imageSize.Set_underscorey(imageSize.Get_underscorey());
            imagePosition.Set_underscorex(imagePosition.Get_underscorex() / reduce - (1 / reduce - 1) / 2);
            imagePosition.Set_underscorey(imagePosition.Get_underscorey() / reduce - (1 / reduce - 1) / 2);
            Kdu_underscoredims viewDims = new Kdu_underscoredims();
            viewDims.Assign(imageDimensions);
            viewDims.Access_underscoresize().Set_underscorex(imageSize.Get_underscorex());
            viewDims.Access_underscoresize().Set_underscorey(imageSize.Get_underscorey());
            compositor.Add_underscorecompositing_underscorelayer(0, viewDims, viewDims);
            if (params.getRotationDegree() == 90) compositor.Set_underscorescale(true, false, true, 1.0F); else if (params.getRotationDegree() == 180) compositor.Set_underscorescale(false, true, true, 1.0F); else if (params.getRotationDegree() == 270) compositor.Set_underscorescale(true, true, false, 1.0F); else compositor.Set_underscorescale(false, false, false, 1.0F);
            compositor.Get_underscoretotal_underscorecomposition_underscoredims(viewDims);
            Kdu_underscorecoords viewSize = viewDims.Access_underscoresize();
            compositor.Set_underscorebuffer_underscoresurface(viewDims);
            int[] imgBuffer = new int[viewSize.Get_underscorex() * viewSize.Get_underscorey()];
            Kdu_underscorecompositor_underscorebuf compositorBuffer = compositor.Get_underscorecomposition_underscorebuffer(viewDims);
            int regionBufferSize = 0;
            int[] kduBuffer = null;
            Kdu_underscoredims newRegion = new Kdu_underscoredims();
            while (compositor.Process(100000, newRegion)) {
                Kdu_underscorecoords newOffset = newRegion.Access_underscorepos();
                Kdu_underscorecoords newSize = newRegion.Access_underscoresize();
                newOffset.Subtract(viewDims.Access_underscorepos());
                int newPixels = newSize.Get_underscorex() * newSize.Get_underscorey();
                if (newPixels == 0) continue;
                if (newPixels > regionBufferSize) {
                    regionBufferSize = newPixels;
                    kduBuffer = new int[regionBufferSize];
                }
                compositorBuffer.Get_underscoreregion(newRegion, kduBuffer);
                int imgBuffereIdx = newOffset.Get_underscorex() + newOffset.Get_underscorey() * viewSize.Get_underscorex();
                int kduBufferIdx = 0;
                int xDiff = viewSize.Get_underscorex() - newSize.Get_underscorex();
                for (int j = 0; j < newSize.Get_underscorey(); j++, imgBuffereIdx += xDiff) {
                    for (int i = 0; i < newSize.Get_underscorex(); i++) {
                        imgBuffer[imgBuffereIdx++] = kduBuffer[kduBufferIdx++];
                    }
                }
            }
            if (params.getRotationDegree() == 90 || params.getRotationDegree() == 270) image = new BufferedImage(imageSize.Get_underscorey(), imageSize.Get_underscorex(), BufferedImage.TYPE_underscoreINT_underscoreRGB); else image = new BufferedImage(imageSize.Get_underscorex(), imageSize.Get_underscorey(), BufferedImage.TYPE_underscoreINT_underscoreRGB);
            image.setRGB(0, 0, viewSize.Get_underscorex(), viewSize.Get_underscorey(), imgBuffer, 0, viewSize.Get_underscorex());
            if (compositor != null) compositor.Native_underscoredestroy();
            wrapped_underscoresrc.Native_underscoredestroy();
            family_underscoresrc.Native_underscoredestroy();
            if (raw_underscoresrc != null) raw_underscoresrc.Native_underscoredestroy();
            return image;
        } catch (KduException e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        }
    }

