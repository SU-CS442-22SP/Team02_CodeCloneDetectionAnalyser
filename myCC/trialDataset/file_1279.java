    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(BitmapFont.class.getResource("Candara-38-Bold.png"));
        URL url = BitmapFontData.class.getResource("Candara-38-Bold.fnt");
        BitmapFontData bitmapFontData = new BitmapFontData(url.openStream(), true);
        BitmapFont font = new BitmapFont(bitmapFontData, true);
        font.drawMultiLine("Hello world\nthis is a\ntest!!!", 100, 100);
        VertexData vertexData = font.createVertexData();
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.create();
        RenderPass renderPass = new RenderPass();
        renderPass.setClearMask(GL11.GL_underscoreCOLOR_underscoreBUFFER_underscoreBIT | GL11.GL_underscoreDEPTH_underscoreBUFFER_underscoreBIT);
        renderPass.setClearColor(new Color4f(0.3f, 0.4f, 0.5f, 1f));
        renderPass.setView(View.createOrtho(0, 640, 0, 480, -1000, 1000));
        ByteBuffer[][] pixels = { { TextureLoader.getImageData(image) } };
        Texture texture = new Texture(TextureType.TEXTURE_underscore2D, 4, image.getWidth(), image.getHeight(), 0, Format.BGRA, pixels, false, false);
        Shape shape = new Shape(vertexData);
        shape.getState().setUnit(0, new Unit(texture));
        shape.getState().setBlendEnabled(true);
        shape.getState().setBlendSrcFunc(BlendSrcFunc.SRC_underscoreALPHA);
        shape.getState().setBlendDstFunc(BlendDstFunc.ONE_underscoreMINUS_underscoreSRC_underscoreALPHA);
        renderPass.getRootNode().addShape(shape);
        Renderer renderer = new Renderer(new SceneGraph(renderPass));
        while (!Display.isCloseRequested()) {
            renderer.render();
            Display.update();
        }
        Display.destroy();
    }

