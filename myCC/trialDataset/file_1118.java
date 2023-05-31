    public BufferedImage extract() throws DjatokaException {
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
        try {
            if (is != null) {
                File f = File.createTempFile("tmp", ".jp2");
                f.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(f);
                sourceFile = f.getAbsolutePath();
                IOUtils.copyStream(is, fos);
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            throw new DjatokaException(e);
        }
        try {
            Jp2_underscoresource inputSource = new Jp2_underscoresource();
            Kdu_underscorecompressed_underscoresource input = null;
            Jp2_underscorefamily_underscoresrc jp2_underscorefamily_underscorein = new Jp2_underscorefamily_underscoresrc();
            Jp2_underscorelocator loc = new Jp2_underscorelocator();
            jp2_underscorefamily_underscorein.Open(sourceFile, true);
            inputSource.Open(jp2_underscorefamily_underscorein, loc);
            inputSource.Read_underscoreheader();
            input = inputSource;
            Kdu_underscorecodestream codestream = new Kdu_underscorecodestream();
            codestream.Create(input);
            Kdu_underscorechannel_underscoremapping channels = new Kdu_underscorechannel_underscoremapping();
            if (inputSource.Exists()) channels.Configure(inputSource, false); else channels.Configure(codestream);
            int ref_underscorecomponent = channels.Get_underscoresource_underscorecomponent(0);
            Kdu_underscorecoords ref_underscoreexpansion = getReferenceExpansion(ref_underscorecomponent, channels, codestream);
            Kdu_underscoredims image_underscoredims = new Kdu_underscoredims();
            codestream.Get_underscoredims(ref_underscorecomponent, image_underscoredims);
            Kdu_underscorecoords imageSize = image_underscoredims.Access_underscoresize();
            Kdu_underscorecoords imagePosition = image_underscoredims.Access_underscorepos();
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
            imageSize.Set_underscorex(imageSize.Get_underscorex() * ref_underscoreexpansion.Get_underscorex());
            imageSize.Set_underscorey(imageSize.Get_underscorey() * ref_underscoreexpansion.Get_underscorey());
            imagePosition.Set_underscorex(imagePosition.Get_underscorex() * ref_underscoreexpansion.Get_underscorex() / reduce - ((ref_underscoreexpansion.Get_underscorex() / reduce - 1) / 2));
            imagePosition.Set_underscorey(imagePosition.Get_underscorey() * ref_underscoreexpansion.Get_underscorey() / reduce - ((ref_underscoreexpansion.Get_underscorey() / reduce - 1) / 2));
            Kdu_underscoredims view_underscoredims = new Kdu_underscoredims();
            view_underscoredims.Assign(image_underscoredims);
            view_underscoredims.Access_underscoresize().Set_underscorex(imageSize.Get_underscorex());
            view_underscoredims.Access_underscoresize().Set_underscorey(imageSize.Get_underscorey());
            int region_underscorebuf_underscoresize = imageSize.Get_underscorex() * imageSize.Get_underscorey();
            int[] region_underscorebuf = new int[region_underscorebuf_underscoresize];
            Kdu_underscoreregion_underscoredecompressor decompressor = new Kdu_underscoreregion_underscoredecompressor();
            decompressor.Start(codestream, channels, -1, params.getLevelReductionFactor(), 16384, image_underscoredims, ref_underscoreexpansion, new Kdu_underscorecoords(1, 1), false, Kdu_underscoreglobal.KDU_underscoreWANT_underscoreOUTPUT_underscoreCOMPONENTS);
            Kdu_underscoredims new_underscoreregion = new Kdu_underscoredims();
            Kdu_underscoredims incomplete_underscoreregion = new Kdu_underscoredims();
            Kdu_underscorecoords viewSize = view_underscoredims.Access_underscoresize();
            incomplete_underscoreregion.Assign(image_underscoredims);
            int[] imgBuffer = new int[viewSize.Get_underscorex() * viewSize.Get_underscorey()];
            int[] kduBuffer = null;
            while (decompressor.Process(region_underscorebuf, image_underscoredims.Access_underscorepos(), 0, 0, region_underscorebuf_underscoresize, incomplete_underscoreregion, new_underscoreregion)) {
                Kdu_underscorecoords newOffset = new_underscoreregion.Access_underscorepos();
                Kdu_underscorecoords newSize = new_underscoreregion.Access_underscoresize();
                newOffset.Subtract(view_underscoredims.Access_underscorepos());
                kduBuffer = region_underscorebuf;
                int imgBuffereIdx = newOffset.Get_underscorex() + newOffset.Get_underscorey() * viewSize.Get_underscorex();
                int kduBufferIdx = 0;
                int xDiff = viewSize.Get_underscorex() - newSize.Get_underscorex();
                for (int j = 0; j < newSize.Get_underscorey(); j++, imgBuffereIdx += xDiff) {
                    for (int i = 0; i < newSize.Get_underscorex(); i++) {
                        imgBuffer[imgBuffereIdx++] = kduBuffer[kduBufferIdx++];
                    }
                }
            }
            BufferedImage image = new BufferedImage(imageSize.Get_underscorex(), imageSize.Get_underscorey(), BufferedImage.TYPE_underscoreINT_underscoreRGB);
            image.setRGB(0, 0, viewSize.Get_underscorex(), viewSize.Get_underscorey(), imgBuffer, 0, viewSize.Get_underscorex());
            if (params.getRotationDegree() > 0) {
                image = ImageProcessingUtils.rotate(image, params.getRotationDegree());
            }
            decompressor.Native_underscoredestroy();
            channels.Native_underscoredestroy();
            if (codestream.Exists()) codestream.Destroy();
            inputSource.Native_underscoredestroy();
            input.Native_underscoredestroy();
            jp2_underscorefamily_underscorein.Native_underscoredestroy();
            return image;
        } catch (KduException e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DjatokaException(e);
        }
    }

